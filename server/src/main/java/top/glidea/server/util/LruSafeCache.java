package top.glidea.server.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 线程安全的LRU Cache
 * TODO 待优化
 */
public class LruSafeCache<K, V> {
    private Map<K, Node<K, V>> map = new HashMap<>();
    private DoubleLinkedList<K, V> list = new DoubleLinkedList<>();
    /**
     * 缓存最大容量
     */
    private int DEFAULT_MAX_CAP = 1024;
    private int maxCap = DEFAULT_MAX_CAP;
    /**
     * 读写锁
     */
    private ReadWriteLock rw = new ReentrantReadWriteLock();
    private Lock readLock = rw.readLock();
    private Lock writeLock = rw.writeLock();

    public LruSafeCache() {
    }

    public LruSafeCache(int maxCap) {
        if (maxCap <= 0) {
            throw new IllegalArgumentException("Cache 的容量必须为正整数！");
        }
        this.maxCap = maxCap;
    }

    public V get(K key) {
        readLock.lock();
        try {
            if (!map.containsKey(key)) {
                return null;
            }

            Node<K, V> node = map.get(key);
            list.moveToTail(node);
            return node.value;

        } finally {
            readLock.unlock();
        }
    }

    public void put(K key, V value) {
        writeLock.lock();
        try {
            if (map.containsKey(key)) {
                // put 为修改操作
                Node<K, V> node = map.get(key);
                node.value = value;
                map.put(key, node);

                list.moveToTail(node);
                return;
            }

            // put 为新增操作
            if (map.size() == maxCap) {
                // 容量不足，淘汰上次使用时间离现在最久的 key-value
                Node<K, V> delNode = list.removeFirst();
                map.remove(delNode.key);
            }
            Node<K, V> node = new Node<>(key, value);
            map.put(key, node);
            list.addLast(node);

        } finally {
            writeLock.unlock();
        }
    }

    public void remove(K key) {
        writeLock.lock();
        try {
            Node<K, V> delNode = map.remove(key);
            list.remove(delNode);
        } finally {
            writeLock.unlock();
        }
    }


    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> prev, next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public Node() {
        }
    }

    /**
     * TODO 待优化
     */
    private static class DoubleLinkedList<K, V> {
        private Node<K, V> dumyHead, dumyTail;

        public DoubleLinkedList() {
            dumyHead = new Node<>();
            dumyTail = new Node<>();
            dumyHead.next = dumyTail;
            dumyTail.prev = dumyHead;
        }

        public synchronized void addLast(Node<K, V> node) {
            node.prev = dumyTail.prev;
            node.next = dumyTail;
            dumyTail.prev.next = node;
            dumyTail.prev = node;
        }

        public synchronized void remove(Node<K, V> node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }

        public synchronized Node<K, V> removeFirst() {
            Node<K, V> first = dumyHead.next;
            remove(first);
            return first;
        }

        public synchronized void moveToTail(Node<K, V> node) {
            remove(node);
            addLast(node);
        }
    }
}


