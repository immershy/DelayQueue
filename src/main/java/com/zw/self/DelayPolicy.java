package com.zw.self;

/**
 * 消息重新入队策略
 *
 * @author zhengwei
 * @date 2018/3/17
 */
public interface DelayPolicy {

    /**
     * 计算延时时间
     *
     * @param delayTime 当前延时时间
     * @return
     */
    int nextDelayTime(int delayTime);

}
