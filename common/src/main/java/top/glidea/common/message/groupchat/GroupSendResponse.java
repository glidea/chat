package top.glidea.common.message.groupchat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import top.glidea.common.message.Message;

/**
 * 发送群聊消息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupSendResponse extends Message {
    /**
     * 转发消息内容
     */
    private String content;
    /**
     * 发送者
     */
    private String from;
    /**
     * 群名
     */
    private String groupName;

    @Override
    public int getMessageNO() {
        return GROUP_SEND_RESPONSE_NO;
    }
}
