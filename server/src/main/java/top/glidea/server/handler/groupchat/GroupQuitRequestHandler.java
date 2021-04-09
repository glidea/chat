package top.glidea.server.handler.groupchat;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import top.glidea.common.message.FailResponse;
import top.glidea.common.message.groupchat.GroupQuitRequest;
import top.glidea.common.message.groupchat.GroupQuitResponse;
import top.glidea.server.service.GroupService;
import top.glidea.server.session.SessionManger;

@ChannelHandler.Sharable
public class GroupQuitRequestHandler extends SimpleChannelInboundHandler<GroupQuitRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupQuitRequest request) {
        String groupName = request.getGroupName();
        String username = SessionManger.getUsername(ctx.channel());
        boolean isSuccess = GroupService.removeMember(username, groupName);

        if (isSuccess) {
            ctx.writeAndFlush(new GroupQuitResponse());
        } else {
            ctx.writeAndFlush(new FailResponse(GroupQuitRequest.class, "退群失败"));
        }
    }

    private GroupQuitRequestHandler() {
    }
    private static final GroupQuitRequestHandler INSTANCE = new GroupQuitRequestHandler();
    public static GroupQuitRequestHandler getInstance() {
        return INSTANCE;
    }
}
