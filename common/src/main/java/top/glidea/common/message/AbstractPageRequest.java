package top.glidea.common.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 分页请求型消息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractPageRequest extends Message {
    /**
     * 页号 - 从 1 开始
     */
    private int pageNum;
    /**
     * 页大小
     */
    private int pageSize;
}
