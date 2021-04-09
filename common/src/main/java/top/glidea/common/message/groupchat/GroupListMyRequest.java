package top.glidea.common.message.groupchat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.glidea.common.message.AbstractPageRequest;

/**
 * 查看该用户已加入的群聊
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GroupListMyRequest extends AbstractPageRequest {

    public GroupListMyRequest(int pageNum, int pageSize) {
        super(pageNum, pageSize);
    }

    @Override
    public int getMessageNO() {
        return GROUP_LIST_MY_REQUEST_NO;
    }
}
