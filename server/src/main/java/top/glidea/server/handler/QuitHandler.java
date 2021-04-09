package top.glidea.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import top.glidea.server.session.SessionManger;

/**
 * Channel关闭事件处理器
 */
@Slf4j
@ChannelHandler.Sharable
public class QuitHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        SessionManger.destroy(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        SessionManger.destroy(ctx.channel());
    }

    private QuitHandler() {
    }
    private static final QuitHandler INSTANCE = new QuitHandler();
    public static QuitHandler getInstance() {
        return INSTANCE;
    }
}
