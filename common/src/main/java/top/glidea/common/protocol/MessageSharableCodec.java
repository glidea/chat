package top.glidea.common.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import top.glidea.common.message.Message;
import top.glidea.common.util.Serializer;

import java.util.List;

/**
 * 编解码器
 * 必须和 LengthFieldBasedFrameDecoder 一起使用，确保decode接到的 ByteBuf 消息是完整的
 *
 * 自定义协议格式：
 *     魔数："NETTYBABY" 9b（魔数一般设4byte，NETTYBABY纯打趣）
 *     消息编号：1b
 *     内容填充：2b（填充header，使其大小为2^n byte）
 *     正文"字节"长度：4b
 *     正文：不定长
 */
@Slf4j
@ChannelHandler.Sharable
public class MessageSharableCodec extends MessageToMessageCodec<ByteBuf, Message> {
    private static final String MAGIC_NUM = "NETTYBABY";

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> outList) {
        ByteBuf packet = ctx.alloc().buffer();
        packet.writeBytes(MAGIC_NUM.getBytes());
        packet.writeByte(msg.getMessageNO());
        packet.writeBytes(new byte[2]);

        @SuppressWarnings("unchecked") byte[] contentBytes =
                Serializer.Protobuf.serialize(msg, Message.getMessageClass(msg.getMessageNO()));
        packet.writeInt(contentBytes.length);
        packet.writeBytes(contentBytes);
        outList.add(packet);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf packet, List<Object> out) {
        // decode header
        String magicNum = packet.readBytes(MAGIC_NUM.length()).toString(CharsetUtil.UTF_8);
        if (!MAGIC_NUM.equals(magicNum)) {
            return;
        }
        byte messageNO = packet.readByte();
        packet.readBytes(2);
        int length = packet.readInt();
        // decode body
        byte[] contentBytes = new byte[length];
        packet.readBytes(length).getBytes(0, contentBytes);
        Class<?> messageClass = Message.getMessageClass(messageNO);
        try {
            Message message = (Message) Serializer.Protobuf.deSerialize(contentBytes, messageClass);
            out.add(message);
        } catch (Exception ignored) {
            // contentBytes 若是随意构造的，反序列化会失败，并抛出异常
            // 不执行 out.add(message)，该消息就不会得到处理
        }
    }

    private MessageSharableCodec() {}
    private static final MessageSharableCodec INSTANCE = new MessageSharableCodec();
    public static MessageSharableCodec getInstance() {
        return INSTANCE;
    }
}
