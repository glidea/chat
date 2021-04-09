package top.glidea.client;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import top.glidea.common.message.groupchat.*;
import top.glidea.common.message.login.LoginRequest;
import top.glidea.common.message.singlechat.ListUsersRequest;
import top.glidea.common.message.singlechat.SendRequest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class SystemInThread extends Thread {
    private Scanner sc = new Scanner(System.in);
    private String username;
    private volatile boolean isStop;
    private ChannelHandlerContext ctx;

    /**
     * 接收 Netty NIO线程的结果
     */
    private CountDownLatch loginRespLatch;
    private AtomicBoolean isLoginSuccess;

    public SystemInThread(ChannelHandlerContext ctx, CountDownLatch loginRespLatch, AtomicBoolean isLoginSuccess) {
        this.setName("Input Handle Thread");
        this.ctx = ctx;
        this.loginRespLatch = loginRespLatch;
        this.isLoginSuccess = isLoginSuccess;
    }

    @Override
    public void run() {
        LoginRequest message = waitLoginInput();
        if (isStop) { return; }
        ctx.writeAndFlush(message);

        try {
            loginRespLatch.await();
        } catch (InterruptedException e) {
            return;
        }
        if (!isLoginSuccess.get()) {
            closeChannel();
            return;
        }

        printHelp();
        while (!isStop) {
            try {
                dealCommand();
            } catch (Exception e) {
                printHelp();
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void dealCommand() {
        String command = sc.nextLine();
        String[] strArr = command.split(" ", 2);
        String cmdType = strArr[0].toLowerCase();
        String paramString = null;
        if (strArr.length > 1) {
            paramString = strArr[1];
        }

        switch (cmdType) {
            case "send":
                String[] params = paramString.split(" ", 2);
                ctx.writeAndFlush(new SendRequest(params[1], username, params[0]));
                break;
            case "list":
                int pageNum = Integer.parseInt(paramString);
                ctx.writeAndFlush(new ListUsersRequest(pageNum, 10));
                break;
            case "glist":
                pageNum = Integer.parseInt(paramString);
                ctx.writeAndFlush(new GroupListRequest(pageNum, 10));
                break;
            case "glistm":
                pageNum = Integer.parseInt(paramString);
                ctx.writeAndFlush(new GroupListMyRequest(pageNum, 10));
                break;
            case "gcreate":
                params = paramString.split(" ", 2);
                String groupName = params[0];
                String[] members = params[1].split(",");

                Set<String> memberSet = new HashSet<>(Arrays.asList(members));
                memberSet.add(username);
                ctx.writeAndFlush(new GroupCreateRequest(groupName, memberSet));
                break;
            case "gjoin":
                ctx.writeAndFlush(new GroupJoinRequest(paramString));
                break;
            case "gquit":
                ctx.writeAndFlush(new GroupQuitRequest(paramString));
                break;
            case "gsend":
                params = paramString.split(" ", 2);
                groupName = params[0];
                String content = params[1];
                ctx.writeAndFlush(new GroupSendRequest(content, groupName, username));
                break;
            case "gmembers":
                params = paramString.split(" ", 2);
                groupName = params[0];
                pageNum = Integer.parseInt(params[1]);
                ctx.writeAndFlush(new GroupMembersRequest(groupName, pageNum, 10));
                break;
            case "quit":
                closeChannel();
                break;
            default:
                printHelp();
        }
    }

    private LoginRequest waitLoginInput() {
        System.out.println("登录 or 注册");
        System.out.print("用户名：");
        this.username = sc.nextLine();
        if (isStop) { return null; }
        System.out.print("密码：");
        String password = sc.nextLine();
        if (isStop) { return null; }
        return new LoginRequest(username, password);
    }

    private void printHelp() {
        System.out.println("单聊");
        System.out.println("发送消息：send [username] [content]");
        System.out.println("查看所有用户名：list [page_no]  （page_no start on 1）");
        System.out.println("==================================");
        System.out.println("群聊");
        System.out.println("发送消息：gsend [group name] [content] ");
        System.out.println("查看所有群聊名：glist [page_no]");
        System.out.println("查看已加入群聊名 glistm [page_no]");
        System.out.println("查看群成员：gmembers [group name] [page_no]");
        System.out.println("创建群聊：gcreate [group name] [m1,m2,m3...]");
        System.out.println("加入群聊：gjoin [group name]");
        System.out.println("退出群聊：gquit [group name]");
        System.out.println("==================================");
        System.out.println("下线：quit");
        System.out.println("==================================");
    }

    private void closeChannel() {
        ctx.channel().close();
    }

    public void stopThread() {
        isStop = true;
    }
}
