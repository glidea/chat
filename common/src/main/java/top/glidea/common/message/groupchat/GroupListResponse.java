package top.glidea.common.message.groupchat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.glidea.common.message.Message;

import java.util.List;

/**
 * 查看所有群聊名的响应消息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class GroupListResponse extends Message {
    private List<String> groupNames;

    @Override
    public int getMessageNO() {
        return GROUP_LIST_RESPONSE_NO;
    }
}
