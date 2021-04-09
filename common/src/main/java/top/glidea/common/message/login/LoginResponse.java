package top.glidea.common.message.login;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.glidea.common.message.Message;

/**
 * 登录成功响应消息
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LoginResponse extends Message {

    @Override
    public int getMessageNO() {
        return LOGIN_RESPONSE_NO;
    }
}
