package top.glidea.common.message.groupchat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.glidea.common.message.Message;

/**
 * 退出群聊
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GroupQuitResponse extends Message {

    @Override
    public int getMessageNO() {
        return GROUP_QUIT_RESPONSE_NO;
    }
}
