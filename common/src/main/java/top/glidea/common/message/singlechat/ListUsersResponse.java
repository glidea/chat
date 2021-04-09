package top.glidea.common.message.singlechat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.glidea.common.message.Message;

import java.util.List;

/**
 * 查询所有用户名的响应消息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class ListUsersResponse extends Message {
    private List<String> users;

    @Override
    public int getMessageNO() {
        return LIST_USERS_RESPONSE_NO;
    }
}
