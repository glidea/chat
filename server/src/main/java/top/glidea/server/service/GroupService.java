package top.glidea.server.service;

import org.apache.commons.dbutils.QueryRunner;
import top.glidea.common.util.ListPagingUtil;
import top.glidea.server.dao.GroupDAO;
import top.glidea.server.dao.UserDAO;
import top.glidea.server.util.LruSafeCache;
import top.glidea.server.util.QueryRunnerHolder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class GroupService {
    private static QueryRunner qr = QueryRunnerHolder.get();
    public static LruSafeCache<String, CopyOnWriteArrayList<String>> groupMemberCache = new LruSafeCache<>();

    /**
     * 获取全部群名称
     */
    public static List<String> listGroupName(int pageNum, int pageSize) {
        return GroupDAO.listGroupName(pageNum, pageSize);
    }

    /**
     * 获取全部成员
     */
    public static List<String> listAllMemberName(String groupName) {
        if (groupMemberCache.get(groupName) == null) {
            synchronized (GroupService.class) {
                if (groupMemberCache.get(groupName) == null) {
                    List<String> memberNames = GroupDAO.listAllMemberName(groupName);
                    if (memberNames == null) {
                        return new ArrayList<>();
                    }
                    groupMemberCache.put(groupName, new CopyOnWriteArrayList<>(memberNames));
                }
            }
        }
        return groupMemberCache.get(groupName);
    }

    /**
     * 获取群成员
     */
    public static List<String> listMemberName(String groupName, int pageNum, int pageSize) {
        CopyOnWriteArrayList<String> cacheMembers = groupMemberCache.get(groupName);
        if (cacheMembers != null) {
            return ListPagingUtil.getPage(cacheMembers, pageNum, pageSize);
        }

        return GroupDAO.listMemberName(groupName, pageNum, pageSize);
    }

    /**
     * 创建群
     */
    public static Integer createGroup(String groupName, Set<String> memberNames) {
        Integer groupId = GroupDAO.insert(groupName, memberNames);
        if (groupId == null) {
            return null;
        }
        for (String memberName : memberNames) {
            boolean isSuccess = UserDAO.insertInGroupNames(groupName, memberName);
            if (!isSuccess) {
                return null;
            }
        }

        groupMemberCache.put(groupName, new CopyOnWriteArrayList<>(memberNames));
        return groupId;
    }

    /**
     * 加群
     */
    public static boolean joinMember(String groupName, String memberName) {
        try {
            qr.update("BEGIN");
            boolean ok1 = GroupDAO.insertInMemberNames(memberName, groupName);
            boolean ok2 = UserDAO.insertInGroupNames(groupName, memberName);
            if (!ok1 || !ok2) {
                throw new Exception();
            }
            qr.update("COMMIT");

            CopyOnWriteArrayList<String> cacheMembers = groupMemberCache.get(groupName);
            if (cacheMembers != null) {
                cacheMembers.add(memberName);
            }
            return true;

        } catch (Exception e) {
            try {
                qr.update("ROLLBACK");
            } catch (SQLException ignored) {
            }
            return false;
        }
    }

    /**
     * 退群
     */
    public static boolean removeMember(String memberName, String groupName) {
        try {
            qr.update("BEGIN");
            boolean ok1 = GroupDAO.deleteInMemberNames(memberName, groupName);
            boolean ok2 = UserDAO.deleteInGroupNames(groupName, memberName);
            if (!ok1 || !ok2) {
                throw new Exception();
            }
            qr.update("COMMIT");

            CopyOnWriteArrayList<String> cacheMembers = groupMemberCache.get(groupName);
            if (cacheMembers != null) {
                cacheMembers.remove(memberName);
            }
            return true;

        } catch (Exception e) {
            try {
                qr.update("ROLLBACK");
            } catch (SQLException ignored) {
            }
            return false;
        }
    }
}
