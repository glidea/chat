package top.glidea.server.handler.groupchat;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import top.glidea.common.message.groupchat.GroupListMyRequest;
import top.glidea.common.message.groupchat.GroupListMyResponse;
import top.glidea.server.session.SessionManger;
import top.glidea.server.service.UserService;

import java.util.List;

/**
 * 查看已加入群聊名请求处理器
 */
@ChannelHandler.Sharable
public class GroupListMyRequestHandler extends SimpleChannelInboundHandler<GroupListMyRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupListMyRequest request) {
        String username = SessionManger.getUsername(ctx.channel());
        List<String> joinedGroups = UserService.listJoinedGroup
                (username, request.getPageNum(), request.getPageSize());
        ctx.writeAndFlush(new GroupListMyResponse(joinedGroups));
    }

    private GroupListMyRequestHandler() {
    }
    private static final GroupListMyRequestHandler INSTANCE = new GroupListMyRequestHandler();
    public static GroupListMyRequestHandler getInstance() {
        return INSTANCE;
    }
}
