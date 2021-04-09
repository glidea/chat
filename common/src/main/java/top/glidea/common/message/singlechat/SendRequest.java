package top.glidea.common.message.singlechat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.glidea.common.message.Message;

/**
 * 单聊发送请求消息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class SendRequest extends Message {
    /**
     * 消息内容
     */
    private String content;
    /**
     * 发送者
     */
    private String from;
    /**
     * 接收者
     */
    private String to;

    @Override
    public int getMessageNO() {
        return SEND_REQUEST_NO;
    }
}
