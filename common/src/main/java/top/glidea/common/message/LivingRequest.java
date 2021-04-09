package top.glidea.common.message;

/**
 * 心跳包
 */
public class LivingRequest extends Message {
    @Override
    public int getMessageNO() {
        return LIVING_NO;
    }
}
