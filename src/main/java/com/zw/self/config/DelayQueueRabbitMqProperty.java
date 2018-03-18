package com.zw.self.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhengwei
 * @date 2018/3/17
 */
@ConfigurationProperties(prefix = "delayqueue.rabbit.config")
@Getter
@Setter
public class DelayQueueRabbitMqProperty {

    /**
     * 队列host
     */
    private String host = "127.0.0.1";

    /**
     * 端口
     */
    private Integer port = 5672;

    /**
     * 用户名
     */
    private String userName = "root";

    /**
     * 密码
     */
    private String password = "root";

    /**
     * virtual host
     */
    private String vHost = "default";

    /**
     * exchange
     */
    private String exchange = "zwDelayQueueExchange";

    /**
     * 缓冲队列名称
     */
    private String bufferQueue = "zw_delayqueue_buffer_queue";

    /**
     * buffer routing key
     */
    private String bufferRoutingKey = "zwDelayQueueBufferRoutingKey";

    /**
     * 消费队列
     */
    private String processQueue = "zw_delayqueue_process_queue";

    /**
     * 处理队列 routing key
     */
    private String processRoutingKey = "zwDelayQueueProcessRoutingKey";
}
