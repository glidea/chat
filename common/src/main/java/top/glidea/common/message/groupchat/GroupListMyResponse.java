package top.glidea.common.message.groupchat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.glidea.common.message.Message;

import java.util.List;


/**
 * 查看该用户已加入的群聊
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class GroupListMyResponse extends Message {
    private List<String> groupNames;

    @Override
    public int getMessageNO() {
        return GROUP_LIST_MY_RESPONSE_NO;
    }
}
