package top.glidea.common.message.singlechat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.glidea.common.message.AbstractPageRequest;

/**
 * 查询所有用户名的请求消息
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ListUsersRequest extends AbstractPageRequest {

    public ListUsersRequest(int pageNum, int pageSize) {
        super(pageNum, pageSize);
    }

    @Override
    public int getMessageNO() {
        return LIST_USERS_REQUEST_NO;
    }
}
