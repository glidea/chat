package top.glidea.server.handler.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import top.glidea.common.message.FailResponse;
import top.glidea.common.message.groupchat.GroupCreateRequest;
import top.glidea.common.message.groupchat.GroupCreateResponse;
import top.glidea.server.service.GroupService;
import top.glidea.server.session.SessionManger;

import java.util.Set;

@ChannelHandler.Sharable
public class GroupCreateRequestHandler extends SimpleChannelInboundHandler<GroupCreateRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupCreateRequest request) {
        String groupName = request.getGroupName();
        Set<String> memberNames = request.getMembers();

        Integer groupId = GroupService.createGroup(groupName, memberNames);
        if (groupId != null) {
            ctx.writeAndFlush(new GroupCreateResponse("创建成功"));

            for (String memberName : memberNames) {
                Channel channel = SessionManger.getChannel(memberName);
                if (channel == null || channel.equals(ctx.channel())) {
                    continue;
                }
                channel.writeAndFlush(new GroupCreateResponse("您已被拉入【" + groupName + "】中"));
            }
        } else {
            ctx.writeAndFlush(new FailResponse
                    (GroupCreateRequest.class, "【" + groupName + "】" + "已存在"));
        }
    }

    private GroupCreateRequestHandler() {
    }
    private static final GroupCreateRequestHandler INSTANCE = new GroupCreateRequestHandler();
    public static GroupCreateRequestHandler getInstance() {
        return INSTANCE;
    }
}
