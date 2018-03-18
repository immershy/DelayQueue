package com.zw.self;

/**
 * Created by zhengwei on 2018/3/17.
 */
public interface DelayQueue {

    /**
     * 添加延迟消息
     *
     * @param msg     消息体
     * @return
     */
    boolean put(DelayMessage msg);

    /**
     * 删除消息
     *
     * @param msgId 消息编号
     */
    void remove(String msgId);
}
