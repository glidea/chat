package top.glidea.server.dao;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import top.glidea.common.util.ListPagingUtil;
import top.glidea.common.util.Serializer;
import top.glidea.server.service.GroupService;
import top.glidea.server.util.QueryRunnerHolder;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class GroupDAO {
    private static QueryRunner qr = QueryRunnerHolder.get();

    public static List<String> listGroupName(int pageNO, int pageSize) {
        String sql = "SELECT `name` FROM `group` LIMIT ?,?";
        try {
            return qr.query(sql, new ColumnListHandler<>()
                    , (Math.max(pageNO, 1) - 1) * pageSize, pageSize);
        } catch (SQLException e) {
            log.error("DB Error: " + e);
            throw new RuntimeException();
        }
    }

    @SuppressWarnings("unchecked")
    public static List<String> listAllMemberName(String groupName) {
        String sql = "SELECT `memberNames` FROM `group` WHERE `name`=?";
        try {
            String json = qr.query(sql, new ScalarHandler<>()
                    , groupName);
            return Serializer.Json.toObj(json, List.class);
        } catch (SQLException e) {
            log.error("DB Error: " + e);
            throw new RuntimeException();
        }
    }

    @SuppressWarnings("unchecked")
    public static List<String> listMemberName(String groupName, int pageNum, int pageSize) {
        String sql = "SELECT `memberNames` FROM `group` WHERE `name`=?";
        try {
            String json = qr.query(sql, new ScalarHandler<>()
                    , groupName);
            List<String> memberNames = Serializer.Json.toObj(json, ArrayList.class);
            if (memberNames == null) {
                // 区分 Exception 和空集的情况，也是为了尽量避免 NPE
                memberNames = new ArrayList<>();
            }

            return ListPagingUtil.getPage(memberNames, pageNum, pageSize);
        } catch (SQLException e) {
            log.error("DB Error: " + e);
            throw new RuntimeException();
        }
    }

    public static Integer insert(String groupName, Set<String> memberNames) {
        StringBuilder sql = new StringBuilder("INSERT INTO `group`(`name`,`memberNames`) VALUES (?, JSON_ARRAY(");
        Object[] params = new Object[memberNames.size() + 1];
        params[0] = groupName;
        int i = 0;
        for (String memberName : memberNames) {
            params[i + 1] = memberName;
            sql.append("?");
            if (i != memberNames.size() - 1) {
                sql.append(",");
            } else {
                sql.append("))");
            }
            i++;
        }

        try {
            BigInteger id = qr.insert(sql.toString(), new ScalarHandler<>()
                    , params);
            return id.intValue();
        } catch (SQLException e) {
            return null;
        }
    }

    public static boolean insertInMemberNames(String memberName, String groupName) {
        String sql = "UPDATE `group` SET `memberNames` = JSON_INSERT(`memberNames`, '$[2147483647]', ?) " +
                "WHERE `name`=?";
        try {
            int update = qr.update(sql, memberName, groupName);
            return update > 0;
        } catch (SQLException e) {
            log.error("DB Error: " + e);
            throw new RuntimeException();
        }
    }

    public static boolean deleteInMemberNames(String memberName, String groupName) {
        String sql1 = "SELECT `memberNames` FROM `group` WHERE `name`=?";
        String sql2 = "UPDATE `group` SET `memberNames`=? WHERE `name`=?";
        try {
            String memberNamesJson = qr.query(sql1, new ScalarHandler<>()
                    , groupName);
            Set memberNames = Serializer.Json.toObj(memberNamesJson, HashSet.class);
            memberNames.remove(memberName);
            if (memberNames.isEmpty()) {
                delete(groupName);
                GroupService.groupMemberCache.remove(groupName);
                return true;
            }
            int update = qr.update(sql2
                    , Serializer.Json.toJsonString(memberNames), groupName);
            return update > 0;
        } catch (SQLException e) {
            log.error("DB Error: " + e);
            throw new RuntimeException();
        }
    }

    private static void delete(String groupName) {
        String sql = "DELETE FROM `group` WHERE `name`=?";
        try {
            qr.update(sql, groupName);
        } catch (SQLException e) {
            log.error("DB Error: " + e);
            throw new RuntimeException();
        }
    }
}
