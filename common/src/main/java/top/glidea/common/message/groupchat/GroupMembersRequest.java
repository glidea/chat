package top.glidea.common.message.groupchat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.glidea.common.message.AbstractPageRequest;

/**
 * 查看群成员
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class GroupMembersRequest extends AbstractPageRequest {
    private String groupName;

    public GroupMembersRequest(String groupName, int pageNum, int pageSize) {
        super(pageNum, pageSize);
        this.groupName = groupName;
    }

    @Override
    public int getMessageNO() {
        return GROUP_MEMBERS_REQUEST_NO;
    }
}
