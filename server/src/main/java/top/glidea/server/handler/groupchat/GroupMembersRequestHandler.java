package top.glidea.server.handler.groupchat;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import top.glidea.common.message.groupchat.GroupMembersRequest;
import top.glidea.common.message.groupchat.GroupMembersResponse;
import top.glidea.server.service.GroupService;

import java.util.List;

@ChannelHandler.Sharable
public class GroupMembersRequestHandler extends SimpleChannelInboundHandler<GroupMembersRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMembersRequest request) {
        String groupName = request.getGroupName();
        int pageNum = request.getPageNum();
        int pageSize = request.getPageSize();
        List<String> memberNames = GroupService.listMemberName(groupName, pageNum, pageSize);
        ctx.writeAndFlush(new GroupMembersResponse(memberNames));
    }

    private GroupMembersRequestHandler() {
    }
    private static final GroupMembersRequestHandler INSTANCE = new GroupMembersRequestHandler();
    public static GroupMembersRequestHandler getInstance() {
        return INSTANCE;
    }
}
