package com.zw.self.impl;

import com.rabbitmq.client.Channel;
import com.zw.self.DelayMessage;
import com.zw.self.DelayPolicy;
import com.zw.self.DelayQueue;
import com.zw.self.MessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

/**
 * @author zhengwei
 * @date 2018/3/17
 */
@Slf4j
public class Processor implements ChannelAwareMessageListener {

    private MessageHandler handler;

    private DelayPolicy delayPolicy;

    private DelayQueue delayQueue;

    public Processor(MessageHandler handler, DelayPolicy delayPolicy, DelayQueue delayQueue) {
        this.handler = handler;
        this.delayPolicy = delayPolicy;
        this.delayQueue = delayQueue;
    }

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        final DelayMessage custMsg = new DelayMessage(new String(message.getBody()));
        boolean success;
        try {
            success = handler.handle(custMsg);
        } catch (Exception e) {
            log.error("消息处理异常,msg={}", custMsg);
            return;
        }

        if (!success) {
            custMsg.setDelayMs(delayPolicy.nextDelayTime(custMsg.getDelayMs()));
            delayQueue.put(custMsg);
        }
    }

}
