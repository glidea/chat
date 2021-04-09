package top.glidea.server.handler.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import top.glidea.common.message.FailResponse;
import top.glidea.common.message.groupchat.GroupSendRequest;
import top.glidea.common.message.groupchat.GroupSendResponse;
import top.glidea.server.service.GroupService;
import top.glidea.server.session.SessionManger;

import java.util.List;

@ChannelHandler.Sharable
public class GroupSendRequestHandler extends SimpleChannelInboundHandler<GroupSendRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupSendRequest request) {
        List<String> memberNames = GroupService.listAllMemberName(request.getGroupName());
        if (memberNames != null && memberNames.size() > 0) {
            boolean isJoinedGroup = false;

            for (String memberName : memberNames) {
                Channel channel = SessionManger.getChannel(memberName);
                if (channel == null) {
                    continue;
                }
                if (channel.equals(ctx.channel())) {
                    isJoinedGroup = true;
                    continue;
                }
                channel.writeAndFlush(new GroupSendResponse
                        (request.getContent(), request.getFrom(), request.getGroupName()));
            }

            if (!isJoinedGroup) {
                GroupService.joinMember(request.getGroupName(), request.getFrom());
            }
        } else {
            ctx.writeAndFlush(new FailResponse(GroupSendRequest.class, "群名不存在"));
        }
    }

    private GroupSendRequestHandler() {
    }
    private static final GroupSendRequestHandler INSTANCE = new GroupSendRequestHandler();
    public static GroupSendRequestHandler getInstance() {
        return INSTANCE;
    }
}
