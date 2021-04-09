package top.glidea.common.message.groupchat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.glidea.common.message.Message;

/**
 * 加入群聊
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class GroupJoinRequest extends Message {
    private String groupName;

    @Override
    public int getMessageNO() {
        return GROUP_JOIN_REQUEST_NO;
    }
}
