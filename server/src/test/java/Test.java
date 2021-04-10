import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import top.glidea.common.message.login.LoginRequest;
import top.glidea.common.util.Serializer;

import java.util.Arrays;

@Slf4j
public class Test {
    public static void main(String[] args) {
        LoginRequest message = new LoginRequest("laowang", "123");

        byte[] bytes = Serializer.Protobuf.serialize(message, LoginRequest.class);
        byte[] bytes1 = Serializer.Json.toJsonString(message).getBytes();

        for (byte aByte : bytes) {
            System.out.print((char)aByte);
        }
        System.out.println();
        for (byte aByte : bytes1) {
            System.out.print((char)aByte);
        }
        System.out.println();
        System.out.println(Arrays.toString(bytes));
        System.out.println(Arrays.toString(bytes1));
    }
}
