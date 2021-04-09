package top.glidea.common.message.singlechat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.glidea.common.message.Message;

/**
 * 单聊发送响应消息（响应目标非请求的client）
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class SendResponse extends Message {
    /**
     * 转发内容
     */
    private String content;
    /**
     * 接收者
     */
    private String from;

    @Override
    public int getMessageNO() {
        return SEND_FORWARD_NO;
    }
}
