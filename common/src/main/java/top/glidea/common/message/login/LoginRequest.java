package top.glidea.common.message.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.glidea.common.message.Message;

/**
 * 登录请求信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class LoginRequest extends Message {
    private String username;
    private String password;

    @Override
    public int getMessageNO() {
        return LOGIN_REQUEST_NO;
    }
}
