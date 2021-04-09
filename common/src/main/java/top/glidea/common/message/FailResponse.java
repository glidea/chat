package top.glidea.common.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 请求失败的统一响应消息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FailResponse extends Message {
    /**
     * 失败请求的 class类型
     */
    private Class requestClass;
    /**
     * 请求失败原因
     */
    private String reason;

    @Override
    public int getMessageNO() {
        return FAIL_NO;
    }
}
