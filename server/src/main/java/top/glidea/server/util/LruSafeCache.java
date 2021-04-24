package top.glidea.server.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 线程安全的LRU Cache
 */
public class LruSafeCache<K, V> {
    private final Map<K, Node<K, V>> map;
    private final ConcurrentOrderList<K, V> list;
    private final int maxCap;

    private static final int DEFAULT_MAX_CAP = 1024;
    private static final int DEFAULT_CONCURRENCY_LEVEL = 4;

    public LruSafeCache() {
        this(DEFAULT_MAX_CAP, DEFAULT_CONCURRENCY_LEVEL);
    }

    public LruSafeCache(int maxCap) {
        this(maxCap, DEFAULT_CONCURRENCY_LEVEL);
    }

    /**
     * concurrentLevel 代表ConcurrentOrderList#Segment 的个数
     * 值越大，LruSafeCache读读，读写并发度越高（写写仍互斥，但因为缓存本就是读多写少才用，所以即便支持并发修改，性能提高也很有限）
     * 但代价是实际缓存淘汰策略会逐步背离 LRU
     */
    public LruSafeCache(int maxCap, int concurrentLevel) {
        this.maxCap = maxCap;
        map = new HashMap<>();
        list = new ConcurrentOrderList<>(concurrentLevel);
    }

    /**
     * no lock，弱一致性
     */
    public V get(K key) {
        Node<K, V> node = map.get(key);
        if (node == null) {
            return null;
        }

        list.moveToLast(node);
        return node.value;
    }

    public synchronized void put(K key, V value) {
        if (map.containsKey(key)) {
            // put 为修改操作
            Node<K, V> node = map.get(key);
            node.value = value;
            map.put(key, node);

            list.moveToLast(node);
            return;
        }

        // put 为新增操作
        if (map.size() == maxCap) {
            // 容量不足，淘汰上次使用时间离现在最久的 key-value
            List<Node<K, V>> delNodes = list.removeAllFirst();
            for (Node<K, V> delNode : delNodes) {
                map.remove(delNode.key);
            }
        }
        Node<K, V> node = new Node<>(key, value);
        map.put(key, node);
        list.addLast(node);
    }

    public synchronized void remove(K key) {
        Node<K, V> delNode = map.remove(key);
        list.remove(delNode);
    }


    /* ---------------- ConcurrentOrderList -------------- */
    /**
     * 维护使用顺序的双向链表
     * 支持并发修改
     */
    private static final class ConcurrentOrderList<K, V> {
        private final Segment<K, V>[] segments;

        @SuppressWarnings("unchecked")
        ConcurrentOrderList(int concurrentLevel) {
            int n = overToPowerOf2(concurrentLevel);
            segments = (Segment<K, V>[]) new Segment[n];
            for (int i = 0; i < segments.length; i++) {
                segments[i] = new Segment<>();
            }
        }

        /**
         * 将node 添加到链表尾部
         */
        void addLast(Node<K, V> node) {
            int idx = locate(node.key.hashCode());
            segments[idx].addLast(node);
        }

        /**
         * 移除node
         */
        void remove(Node<K, V> node) {
            int idx = locate(node.key.hashCode());
            segments[idx].remove(node);
        }

        /**
         * 移除所有Segment 链表的头部
         */
        List<Node<K, V>> removeAllFirst() {
            List<Node<K, V>> firsts = new ArrayList<>();
            for (Segment<K, V> segment : segments) {
                Node<K, V> first = segment.removeFirst();
                if (first != null) {
                    firsts.add(first);
                }
            }
            return firsts;
        }

        /**
         * 移动node 到末尾
         */
        void moveToLast(Node<K, V> node) {
            int idx = locate(node.key.hashCode());
            segments[idx].moveToLast(node);
        }

        /**
         * hash定位子链表
         */
        private int locate(int hashCode) {
            int spread = (hashCode ^ (hashCode >>> 16)) & 0x7fffffff;
            return spread & (segments.length - 1);
        }

        /**
         * 将n -> 2次幂（大于n）
         */
        private int overToPowerOf2(int n) {
            int r = n - 1;
            r |= r >>> 1;
            r |= r >>> 2;
            r |= r >>> 4;
            r |= r >>> 8;
            r |= r >>> 16;
            return (r < 0) ? 1 : r + 1;
        }


        /* ---------------- Segment -------------- */
        /**
         * 子链表（Thread Safe）
         */
        final static class Segment<K, V> {
            private Node<K, V> dummyHead, dummyTail;

            public Segment() {
                dummyHead = new Node<>();
                dummyTail = new Node<>();
                dummyHead.next = dummyTail;
                dummyTail.prev = dummyHead;
            }

            /**
             * 将node 添加到链表尾部
             */
            public synchronized void addLast(Node<K, V> node) {
                node.prev = dummyTail.prev;
                node.next = dummyTail;
                dummyTail.prev.next = node;
                dummyTail.prev = node;
            }

            /**
             * 移除node
             */
            public synchronized void remove(Node<K, V> node) {
                if (node.prev == null || node.next == null) {
                    return;
                }
                node.prev.next = node.next;
                node.next.prev = node.prev;
                node.prev = null;
                node.next = null;
            }

            /**
             * 移除链表头部
             */
            public synchronized Node<K, V> removeFirst() {
                Node<K, V> first = dummyHead.next;
                if (first != null) {
                    remove(first);
                }
                return first;
            }

            /**
             * 移动node 到末尾
             */
            public synchronized void moveToLast(Node<K, V> node) {
                if (node.prev == null || node.next == null) {
                    return;
                }
                remove(node);
                addLast(node);
            }
        }
    }


    /* ---------------- Node -------------- */
    /**
     * map的value，list的节点
     */
    private static final class Node<K, V> {
        final K key;
        volatile V value;
        volatile Node<K, V> prev, next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        Node() {
            this.key = null;
        }
    }
}


