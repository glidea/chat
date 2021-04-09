package top.glidea.common.message.groupchat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.glidea.common.message.Message;

/**
 * 退出群聊
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class GroupQuitRequest extends Message {
    private String groupName;

    @Override
    public int getMessageNO() {
        return GROUP_QUIT_REQUEST_NO;
    }
}
