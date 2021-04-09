package top.glidea.common.message.groupchat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.glidea.common.message.Message;

import java.util.Set;

/**
 * 创建群聊
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class GroupCreateRequest extends Message {
    /**
     * 群名
     */
    private String groupName;
    /**
     * 初始成员用户名
     */
    private Set<String> members;

    @Override
    public int getMessageNO() {
        return GROUP_CREATE_REQUEST_NO;
    }
}
