package top.glidea.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import top.glidea.common.message.FailResponse;
import top.glidea.common.message.groupchat.*;
import top.glidea.common.message.login.LoginRequest;
import top.glidea.common.message.login.LoginResponse;
import top.glidea.common.message.singlechat.ListUsersResponse;
import top.glidea.common.message.singlechat.SendResponse;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class GlobalHandler extends ChannelInboundHandlerAdapter {
    private CountDownLatch loginRespLatch = new CountDownLatch(1);
    private AtomicBoolean isLoginSuccess = new AtomicBoolean();
    private SystemInThread systemInThread;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof FailResponse) {
            FailResponse resp = (FailResponse) msg;
            System.out.println(resp.getReason());
            Class requestClass = resp.getRequestClass();
            if (LoginRequest.class.equals(requestClass)) {
                loginRespLatch.countDown();
            }

        } else if (msg instanceof LoginResponse) {
            loginRespLatch.countDown();
            isLoginSuccess.set(true);

        } else if (msg instanceof SendResponse) {
            SendResponse resp = (SendResponse) msg;
            System.out.println(resp.getFrom() + " : " + resp.getContent());

        } else if (msg instanceof ListUsersResponse) {
            ListUsersResponse resp = (ListUsersResponse) msg;
            printList(resp.getUsers());

        } else if (msg instanceof GroupListResponse) {
            GroupListResponse resp = (GroupListResponse) msg;
            printList(resp.getGroupNames());

        } else if (msg instanceof GroupListMyResponse) {
            GroupListMyResponse resp = (GroupListMyResponse) msg;
            printList(resp.getGroupNames());

        } else if (msg instanceof GroupCreateResponse) {
            GroupCreateResponse resp = (GroupCreateResponse) msg;
            System.out.println(resp.getHint());

        } else if (msg instanceof GroupJoinResponse) {
            System.out.println("加群成功");

        } else if (msg instanceof GroupQuitResponse) {
            System.out.println("退群成功");

        } else if (msg instanceof GroupSendResponse) {
            GroupSendResponse resp = (GroupSendResponse) msg;
            System.out.println("【" + resp.getGroupName() + "】" + resp.getFrom() + "：" + resp.getContent());

        } else if (msg instanceof GroupMembersResponse) {
            GroupMembersResponse resp = (GroupMembersResponse) msg;
            printList(resp.getMemberNames());
        }
        System.out.println("==================================");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        systemInThread = new SystemInThread(ctx, loginRespLatch, isLoginSuccess);
        systemInThread.start();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        closeSystemInThread();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        closeSystemInThread();
    }

    private void closeSystemInThread() {
        System.out.println("连接已断开，按回车退出");
        systemInThread.stopThread();
    }

    private void printList(List<String> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("nothing");
            return;
        }
        for (String e : list) {
            System.out.println(e);
        }
    }
}
