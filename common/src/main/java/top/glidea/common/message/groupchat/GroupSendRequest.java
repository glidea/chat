package top.glidea.common.message.groupchat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.glidea.common.message.Message;

/**
 * 发送群聊消息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class GroupSendRequest extends Message {
    /**
     * 发送内容
     */
    private String content;
    /**
     * 发送所在群
     */
    private String groupName;
    /**
     * 发送者
     */
    private String from;

    @Override
    public int getMessageNO() {
        return GROUP_SEND_REQUEST_NO;
    }
}
