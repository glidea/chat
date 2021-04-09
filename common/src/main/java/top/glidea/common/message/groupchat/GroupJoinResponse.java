package top.glidea.common.message.groupchat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.glidea.common.message.Message;

/**
 * 加入群聊
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GroupJoinResponse extends Message {

    @Override
    public int getMessageNO() {
        return GROUP_JOIN_RESPONSE_NO;
    }
}
