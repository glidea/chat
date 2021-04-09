package top.glidea.server.handler.singlechat;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import top.glidea.common.message.singlechat.ListUsersRequest;
import top.glidea.common.message.singlechat.ListUsersResponse;
import top.glidea.server.service.UserService;

import java.util.List;

/**
 * 查看所有用户名请求处理器
 */
@ChannelHandler.Sharable
public class ListUsersRequestHandler extends SimpleChannelInboundHandler<ListUsersRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ListUsersRequest request) {
        List<String> users = UserService.listUsername(request.getPageNum(), request.getPageSize());
        ctx.writeAndFlush(new ListUsersResponse(users));
    }

    private ListUsersRequestHandler() {}
    private static final ListUsersRequestHandler INSTANCE = new ListUsersRequestHandler();
    public static ListUsersRequestHandler getInstance() {
        return INSTANCE;
    }
}
