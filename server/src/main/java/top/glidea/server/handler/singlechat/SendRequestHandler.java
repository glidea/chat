package top.glidea.server.handler.singlechat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import top.glidea.common.message.FailResponse;
import top.glidea.common.message.singlechat.SendRequest;
import top.glidea.common.message.singlechat.SendResponse;
import top.glidea.server.session.SessionManger;

/**
 * 单聊发送请求处理器
 */
@ChannelHandler.Sharable
public class SendRequestHandler extends SimpleChannelInboundHandler<SendRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SendRequest request) {
        Channel respChannel = SessionManger.getChannel(request.getTo());
        if (respChannel != null) {
            respChannel.writeAndFlush(new SendResponse(request.getContent(), request.getFrom()));
        } else {
            ctx.writeAndFlush(new FailResponse(SendRequest.class, "对方用户不存在或者不在线"));
        }
    }

    private SendRequestHandler() {
    }
    private static final SendRequestHandler INSTANCE = new SendRequestHandler();
    public static SendRequestHandler getInstance() {
        return INSTANCE;
    }
}
