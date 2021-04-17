package top.glidea.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import top.glidea.common.message.LivingRequest;
import top.glidea.common.protocol.MessageSharableCodec;
import top.glidea.common.protocol.ProtocolFrameDecoder;

@Slf4j
public class Client {
    private static NioEventLoopGroup group = new NioEventLoopGroup();

    public static void main(String[] args) {
        try {
            Bootstrap bootstrap = new Bootstrap()
            .channel(NioSocketChannel.class).group(group)
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                ch.pipeline()
                .addLast(new ProtocolFrameDecoder())
                .addLast(MessageSharableCodec.getInstance())
                .addLast(new IdleStateHandler(0, 60, 0))
                .addLast(new ChannelDuplexHandler() {
                    @Override
                    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
                        IdleStateEvent event = (IdleStateEvent) evt;
                        if (event.state() == IdleState.WRITER_IDLE) {
                            ctx.writeAndFlush(new LivingRequest());
                        }
                    }
                })
                .addLast(new GlobalHandler())
                ;
                }
            });

            Channel channel = bootstrap.connect("localhost", 8080)
                    .sync().channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            log.error("client error", e);
        } finally {
            group.shutdownGracefully();
        }
    }
}
