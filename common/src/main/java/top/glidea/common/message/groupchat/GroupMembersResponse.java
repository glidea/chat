package top.glidea.common.message.groupchat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import top.glidea.common.message.Message;

import java.util.List;

/**
 * 查看群成员
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupMembersResponse extends Message {
    private List<String> memberNames;

    @Override
    public int getMessageNO() {
        return GROUP_MEMBERS_RESPONSE_NO;
    }
}
