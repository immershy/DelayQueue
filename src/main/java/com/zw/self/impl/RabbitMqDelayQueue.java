package com.zw.self.impl;

import com.zw.self.DelayMessage;
import com.zw.self.DelayQueue;
import com.zw.self.config.DelayQueueRabbitMqProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * 基于rabbitMq的延迟队列
 *
 * @author zhengwei
 * @date 2018/3/17
 */
@Slf4j
public class RabbitMqDelayQueue implements DelayQueue {

    private RabbitTemplate rabbitTemplate;

    private DelayQueueRabbitMqProperty rabbitMqProperty;

    public RabbitMqDelayQueue(RabbitTemplate rabbitTemplate, DelayQueueRabbitMqProperty rabbitMqProperty) {
        this.rabbitMqProperty = rabbitMqProperty;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public boolean put(final DelayMessage msg) {
        if (msg == null) {
            throw new RuntimeException("消息不能为空");
        }
        if (msg.getDelayMs() < 0) {
            throw new RuntimeException("过期时间无效");
        }
        try {
            rabbitTemplate.convertAndSend(rabbitMqProperty.getExchange(), rabbitMqProperty.getBufferRoutingKey(), msg.toString(), message -> {
                message.getMessageProperties().setExpiration(String.valueOf(msg.getDelayMs()));
                return message;
            });
            return true;
        } catch (Exception e) {
            log.error("消息发送失败,msg={}", msg, e);
            return false;
        }
    }

    @Override
    public void remove(String msgId) {
        throw new NotImplementedException();
    }
}


