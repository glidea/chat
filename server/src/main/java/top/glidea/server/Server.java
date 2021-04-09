package top.glidea.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import top.glidea.common.protocol.MessageSharableCodec;
import top.glidea.common.protocol.ProtocolFrameDecoder;
import top.glidea.server.handler.QuitHandler;
import top.glidea.server.handler.groupchat.*;
import top.glidea.server.handler.login.LoginRequestHandler;
import top.glidea.server.handler.singlechat.ListUsersRequestHandler;
import top.glidea.server.handler.singlechat.SendRequestHandler;

@Slf4j
public class Server {
    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        LoggingHandler sharedLoggingHandler = new LoggingHandler();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap()
            .group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) {
                    ch.pipeline()
                    .addLast(new IdleStateHandler(90, 0, 0))
                    .addLast(new ChannelDuplexHandler() {
                        @Override
                        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception{
                            IdleStateEvent event = (IdleStateEvent) evt;
                            if (event.state() == IdleState.READER_IDLE) {
                                ctx.channel().close();
                            }
                        }
                    })
                    .addLast(new ProtocolFrameDecoder())
                    // 所有EventLoop共享Shared Handler（不存储状态）
                    .addLast(sharedLoggingHandler)
                    .addLast(MessageSharableCodec.getInstance())
                    .addLast(LoginRequestHandler.getInstance())
                    .addLast(SendRequestHandler.getInstance())
                    .addLast(ListUsersRequestHandler.getInstance())
                    .addLast(GroupListRequestHandler.getInstance())
                    .addLast(GroupListMyRequestHandler.getInstance())
                    .addLast(GroupCreateRequestHandler.getInstance())
                    .addLast(GroupJoinRequestHandler.getInstance())
                    .addLast(GroupQuitRequestHandler.getInstance())
                    .addLast(GroupSendRequestHandler.getInstance())
                    .addLast(GroupMembersRequestHandler.getInstance())
                    .addLast(QuitHandler.getInstance());
                }
            });
            Channel channel = serverBootstrap.bind(8080).sync().channel();
            channel.closeFuture().sync();

        } catch (InterruptedException e) {
            log.error("server error: ", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
