package top.glidea.server.session;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 维护已连接channel和username的关系，以保持会话
 */
public class SessionManger {
    private static final Map<String, Channel> USERNAME_CHANNEL_MAP = new ConcurrentHashMap<>();
    private static final Map<Channel, String> CHANNEL_USERNAME_MAP = new ConcurrentHashMap<>();

    public static void create(String username, Channel channel) {
        USERNAME_CHANNEL_MAP.put(username, channel);
        CHANNEL_USERNAME_MAP.put(channel, username);
    }

    public static void destroy(Channel channel) {
        String username = CHANNEL_USERNAME_MAP.remove(channel);
        USERNAME_CHANNEL_MAP.remove(username);
    }

    public static Channel getChannel(String username) {
        return USERNAME_CHANNEL_MAP.get(username);
    }

    public static String getUsername(Channel channel) {
        return CHANNEL_USERNAME_MAP.get(channel);
    }
}
