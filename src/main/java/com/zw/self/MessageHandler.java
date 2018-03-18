package com.zw.self;

/**
 * @author zhengwei
 * @date 2018/3/17
 */
public interface MessageHandler {

    /**
     * 消息处理器
     *
     * @param msg@return
     */
    boolean handle(DelayMessage msg);
}
