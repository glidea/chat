package top.glidea.server.service;

import top.glidea.server.dao.UserDAO;

import java.util.List;

public class UserService {

    /**
     * 登录/注册
     * 老用户登录，新用户注册
     */
    public static boolean loginOrRegister(String username, String password) {
        boolean isSuccess = UserDAO.login(username, password);
        if (!isSuccess) {
            // 注册
            Integer id = UserDAO.insert(username, password);
            if (id == null) {
                // 用户名已被占用
                return false;
            }
        }
        return true;
    }

    /**
     * 查询系统内所有的用户（include offline）
     */
    public static List<String> listUsername(int pageNum, int pageSize) {
        return UserDAO.listUsername(pageNum, pageSize);
    }

    /**
     * 查询用户已加入的群聊
     */
    public static List<String> listJoinedGroup(String username, int pageNum, int pageSize) {
        return UserDAO.listJoinedGroup(username, pageNum, pageSize);
    }
}
