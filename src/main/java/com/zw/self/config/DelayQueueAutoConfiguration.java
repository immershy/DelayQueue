package com.zw.self.config;

import com.zw.self.DelayPolicy;
import com.zw.self.DelayQueue;
import com.zw.self.MessageHandler;
import com.zw.self.impl.FixedDelayPolicy;
import com.zw.self.impl.Processor;
import com.zw.self.impl.RabbitMqDelayQueue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhengwei
 * @date 2018/3/17
 */
@Configuration
@EnableConfigurationProperties(DelayQueueRabbitMqProperty.class)
@ConditionalOnProperty(name = "delayqueue.rabbit.enable", havingValue = "true")
@ConditionalOnBean({ConnectionFactory.class, MessageHandler.class})
public class DelayQueueAutoConfiguration {

    @Autowired
    private DelayQueueRabbitMqProperty rabbitMqProperty;

    @Autowired
    private ConnectionFactory connectionFactory;

    @Bean
    @ConditionalOnMissingBean(DelayPolicy.class)
    public DelayPolicy delayPolicy() {
        return new FixedDelayPolicy();
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(rabbitMqProperty.getExchange());
    }

    @Bean
    public Queue bufferQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", rabbitMqProperty.getExchange());
        args.put("x-dead-letter-routing-key", rabbitMqProperty.getProcessRoutingKey());
        return new Queue(rabbitMqProperty.getBufferQueue(), true, false, true, args);
    }

    @Bean
    public Binding bufferBinding() {
        return BindingBuilder.bind(bufferQueue())
                .to(exchange())
                .with(rabbitMqProperty.getBufferRoutingKey());
    }

    @Bean
    public Queue processQueue() {
        return new Queue(rabbitMqProperty.getProcessQueue(), true, false, true);
    }

    @Bean
    Binding processBinding() {
        return BindingBuilder.bind(processQueue())
                .to(exchange())
                .with(rabbitMqProperty.getProcessRoutingKey());
    }

    @Bean
    public SimpleMessageListenerContainer processContainer(Processor processor) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(rabbitMqProperty.getProcessQueue());
        container.setMessageListener(new MessageListenerAdapter(processor));
        return container;
    }

    @ConditionalOnMissingBean(name = "delayQueueRabbitTemplate")
    @Bean(name = "delayQueueRabbitTemplate")
    public RabbitTemplate delayQueueRabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        return template;
    }

    @ConditionalOnMissingBean(DelayQueue.class)
    @Bean
    public DelayQueue delayQueue() {
        return new RabbitMqDelayQueue(delayQueueRabbitTemplate(), rabbitMqProperty);
    }

    @Bean
    public Processor processor(MessageHandler handler, DelayPolicy delayPolicy, DelayQueue delayQueue) {
        return new Processor(handler, delayPolicy, delayQueue);
    }
}
