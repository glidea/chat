package top.glidea.server.handler.groupchat;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import top.glidea.common.message.FailResponse;
import top.glidea.common.message.groupchat.GroupJoinRequest;
import top.glidea.common.message.groupchat.GroupJoinResponse;
import top.glidea.server.service.GroupService;
import top.glidea.server.session.SessionManger;

@ChannelHandler.Sharable
public class GroupJoinRequestHandler extends SimpleChannelInboundHandler<GroupJoinRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupJoinRequest request) {
        String groupName = request.getGroupName();
        String username = SessionManger.getUsername(ctx.channel());
        boolean isSuccess = GroupService.joinMember(groupName, username);

        if (isSuccess) {
            ctx.writeAndFlush(new GroupJoinResponse());
        } else {
            ctx.writeAndFlush(new FailResponse
                    (GroupJoinRequest.class, "【" + request.getGroupName() + "】" + "群不存在"));
        }
    }

    private GroupJoinRequestHandler() {
    }
    private static final GroupJoinRequestHandler INSTANCE = new GroupJoinRequestHandler();
    public static GroupJoinRequestHandler getInstance() {
        return INSTANCE;
    }
}
