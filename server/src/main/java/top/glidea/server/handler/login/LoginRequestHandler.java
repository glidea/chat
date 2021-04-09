package top.glidea.server.handler.login;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import top.glidea.common.message.FailResponse;
import top.glidea.common.message.login.LoginRequest;
import top.glidea.common.message.login.LoginResponse;
import top.glidea.server.session.SessionManger;
import top.glidea.server.service.UserService;

/**
 * 登录请求处理器
 */
@ChannelHandler.Sharable
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        boolean isSuccess = UserService.loginOrRegister(username, password);
        if (isSuccess) {
            Channel channel = SessionManger.getChannel(username);
            if (channel != null) {
                // 挤下线
                channel.close();
            }
            SessionManger.create(username, ctx.channel());
            ctx.writeAndFlush(new LoginResponse());
        } else {
            ctx.writeAndFlush(new FailResponse(LoginRequest.class, "密码不正确"));
        }
    }

    private LoginRequestHandler() {}
    private static final LoginRequestHandler INSTANCE = new LoginRequestHandler();
    public static LoginRequestHandler getInstance() {
        return INSTANCE;
    }
}
