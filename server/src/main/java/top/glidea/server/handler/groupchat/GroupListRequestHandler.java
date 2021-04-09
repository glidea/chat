package top.glidea.server.handler.groupchat;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import top.glidea.common.message.groupchat.GroupListRequest;
import top.glidea.common.message.groupchat.GroupListResponse;
import top.glidea.server.service.GroupService;

import java.util.List;

/**
 * 查看所有群聊名请求处理器
 */
@ChannelHandler.Sharable
public class GroupListRequestHandler extends SimpleChannelInboundHandler<GroupListRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupListRequest request) {
        List<String> groupNames = GroupService.listGroupName(request.getPageNum(), request.getPageSize());
        ctx.writeAndFlush(new GroupListResponse(groupNames));
    }

    private GroupListRequestHandler() {
    }
    private static final GroupListRequestHandler INSTANCE = new GroupListRequestHandler();
    public static GroupListRequestHandler getInstance() {
        return INSTANCE;
    }
}
