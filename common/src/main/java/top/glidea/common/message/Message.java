package top.glidea.common.message;

import lombok.Data;
import top.glidea.common.message.groupchat.*;
import top.glidea.common.message.login.LoginRequest;
import top.glidea.common.message.login.LoginResponse;
import top.glidea.common.message.singlechat.ListUsersRequest;
import top.glidea.common.message.singlechat.ListUsersResponse;
import top.glidea.common.message.singlechat.SendRequest;
import top.glidea.common.message.singlechat.SendResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息父类、Util类
 */
@Data
public abstract class Message {
    public static final int LIVING_NO  = -1;
    public static final int FAIL_NO  = -2;
    /**
     * 登录消息编号，与具体的消息对应
     * 用于 MessageCodec中的 encode 和 decode
     * decode过程中，在把content --> 消息对象时，需要消息的 Class类型才能完成转换
     * 而Class类型通过 NO找到（NO 在另一端通过decode 打包到了 packet里）
     */
    public static final int LOGIN_REQUEST_NO  = 0;
    public static final int LOGIN_RESPONSE_NO = 1;
    /**
     * 单聊消息编号
     */
    public static final int SEND_REQUEST_NO        = 10;
    public static final int SEND_FORWARD_NO        = 11;
    public static final int LIST_USERS_REQUEST_NO  = 12;
    public static final int LIST_USERS_RESPONSE_NO = 13;
    /**
     * 群聊消息编号
     */
    public static final int GROUP_LIST_REQUEST_NO     = 20;
    public static final int GROUP_LIST_RESPONSE_NO    = 21;
    public static final int GROUP_LIST_MY_REQUEST_NO  = 22;
    public static final int GROUP_LIST_MY_RESPONSE_NO = 23;
    public static final int GROUP_CREATE_REQUEST_NO   = 24;
    public static final int GROUP_CREATE_RESPONSE_NO  = 25;
    public static final int GROUP_JOIN_REQUEST_NO     = 26;
    public static final int GROUP_JOIN_RESPONSE_NO    = 27;
    public static final int GROUP_QUIT_REQUEST_NO     = 28;
    public static final int GROUP_QUIT_RESPONSE_NO    = 29;
    public static final int GROUP_SEND_REQUEST_NO     = 30;
    public static final int GROUP_SEND_RESPONSE_NO    = 31;
    public static final int GROUP_MEMBERS_REQUEST_NO  = 32;
    public static final int GROUP_MEMBERS_RESPONSE_NO = 33;

    private static final Map<Integer, Class> NO_CLASS_MAP = new HashMap<>();
    static {
        NO_CLASS_MAP.put(LIVING_NO, LivingRequest.class);
        NO_CLASS_MAP.put(FAIL_NO, FailResponse.class);
        NO_CLASS_MAP.put(LOGIN_REQUEST_NO, LoginRequest.class);
        NO_CLASS_MAP.put(LOGIN_RESPONSE_NO, LoginResponse.class);
        NO_CLASS_MAP.put(SEND_REQUEST_NO, SendRequest.class);
        NO_CLASS_MAP.put(SEND_FORWARD_NO, SendResponse.class);
        NO_CLASS_MAP.put(LIST_USERS_REQUEST_NO, ListUsersRequest.class);
        NO_CLASS_MAP.put(LIST_USERS_RESPONSE_NO, ListUsersResponse.class);
        NO_CLASS_MAP.put(GROUP_LIST_REQUEST_NO, GroupListRequest.class);
        NO_CLASS_MAP.put(GROUP_LIST_RESPONSE_NO, GroupListResponse.class);
        NO_CLASS_MAP.put(GROUP_LIST_MY_REQUEST_NO, GroupListMyRequest.class);
        NO_CLASS_MAP.put(GROUP_LIST_MY_RESPONSE_NO, GroupListMyResponse.class);
        NO_CLASS_MAP.put(GROUP_CREATE_REQUEST_NO, GroupCreateRequest.class);
        NO_CLASS_MAP.put(GROUP_CREATE_RESPONSE_NO, GroupCreateResponse.class);
        NO_CLASS_MAP.put(GROUP_JOIN_REQUEST_NO, GroupJoinRequest.class);
        NO_CLASS_MAP.put(GROUP_JOIN_RESPONSE_NO, GroupJoinResponse.class);
        NO_CLASS_MAP.put(GROUP_QUIT_REQUEST_NO, GroupQuitRequest.class);
        NO_CLASS_MAP.put(GROUP_QUIT_RESPONSE_NO, GroupQuitResponse.class);
        NO_CLASS_MAP.put(GROUP_SEND_REQUEST_NO, GroupSendRequest.class);
        NO_CLASS_MAP.put(GROUP_SEND_RESPONSE_NO, GroupSendResponse.class);
        NO_CLASS_MAP.put(GROUP_MEMBERS_REQUEST_NO, GroupMembersRequest.class);
        NO_CLASS_MAP.put(GROUP_MEMBERS_RESPONSE_NO, GroupMembersResponse.class);
    }

    /**
     * 获取消息对象的Class
     */
    public static Class getMessageClass(int messageNO) {
        return NO_CLASS_MAP.get(messageNO);
    }

    /**
     * 获取消息对象的编号
     */
    public abstract int getMessageNO();
}
