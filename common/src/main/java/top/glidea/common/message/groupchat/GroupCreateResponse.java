package top.glidea.common.message.groupchat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import top.glidea.common.message.Message;

/**
 * 创建群聊
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupCreateResponse extends Message {
    private String hint;

    @Override
    public int getMessageNO() {
        return GROUP_CREATE_RESPONSE_NO;
    }
}
