package top.glidea.server.dao;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import top.glidea.common.util.ListPagingUtil;
import top.glidea.common.util.Serializer;
import top.glidea.server.util.QueryRunnerHolder;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class UserDAO {
    private static QueryRunner qr = QueryRunnerHolder.get();

    public static boolean login(String username, String password) {
        String sql = "SELECT COUNT(1) FROM `user` WHERE `username`=? AND `password`=?";
        try {
            Long count = qr.query(sql, new ScalarHandler<>()
                    , username, password);
            return count == 1;
        } catch (SQLException e) {
            log.error("DB Error: " + e);
            throw new RuntimeException();
        }
    }

    public static List<String> listUsername(int pageNum, int pageSize) {
        String sql = "SELECT `username` FROM `user` LIMIT ?,?";
        try {
            return qr.query(sql, new ColumnListHandler<>()
                    , (Math.max(pageNum, 1) - 1) * pageSize, pageSize);
        } catch (SQLException e) {
            log.error("DB Error: " + e);
            throw new RuntimeException();
        }
    }

    @SuppressWarnings("unchecked")
    public static List<String> listJoinedGroup(String username, int pageNum, int pageSize) {
        String sql = "SELECT `groupNames` FROM `user` WHERE `username`=?";
        try {
            String json = qr.query(sql, new ScalarHandler<>()
                    , username);
            List<String> groupNames = Serializer.Json.toObj(json, ArrayList.class);
            return ListPagingUtil.getPage(groupNames, pageNum, pageSize);
        } catch (SQLException e) {
            log.error("DB Error: " + e);
            throw new RuntimeException();
        }
    }

    public static Integer insert(String username, String password) {
        String sql = "INSERT INTO `user`(`username`,`password`,`groupNames`) VALUES (?, ?, '[]')";
        try {
            BigInteger id = qr.insert(sql, new ScalarHandler<>()
                    , username, password);
            return id.intValue();
        } catch (SQLException e) {
            return null;
        }
    }

    public static boolean insertInGroupNames(String groupName, String username) {
        // 2147483647是有符号int最大值，JSON插入必须要指定下标，或者指定大于下标的值（再用JSON，我就是**）
        String sql = "UPDATE `user` SET `groupNames` = JSON_INSERT(`groupNames`, '$[2147483647]', ?) " +
                    "WHERE `username`=?";
        try {
            int update = qr.update(sql, groupName, username);
            return update > 0;
        } catch (SQLException e) {
            log.error("DB Error: " + e);
            throw new RuntimeException();
        }
    }

    public static boolean deleteInGroupNames(String groupName, String username) {
        String sql1 = "SELECT `groupNames` FROM `user` WHERE `username`=?";
        String sql2 = "UPDATE `user` SET `groupNames`=? WHERE `username`=?";
        try {
            String groupNamesJson = qr.query(sql1, new ScalarHandler<>()
                    , username);
            Set groupNames = Serializer.Json.toObj(groupNamesJson, HashSet.class);
            groupNames.remove(groupName);
            int update = qr.update(sql2
                    , Serializer.Json.toJsonString(groupNames), username);
            return update > 0;
        } catch (SQLException e) {
            log.error("DB Error: " + e);
            throw new RuntimeException();
        }
    }
}
