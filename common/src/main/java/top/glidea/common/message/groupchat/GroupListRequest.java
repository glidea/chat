package top.glidea.common.message.groupchat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.glidea.common.message.AbstractPageRequest;

/**
 * 查看所有群聊名的请求消息
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GroupListRequest extends AbstractPageRequest {

    public GroupListRequest(int pageNum, int pageSize) {
        super(pageNum, pageSize);
    }

    @Override
    public int getMessageNO() {
        return GROUP_LIST_REQUEST_NO;
    }
}
