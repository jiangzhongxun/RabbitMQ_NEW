package com.infinite.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jzx
 * @create 2022-10-15-13:17
 *
 * 延迟队列配置类
 */

@Configuration
public class DelayedQueueConfig {

    /**
     * 延迟队列
     */
    public static final String DELAYED_QUEUE = "delayed.queue";
    /**
     * 延迟交换机
     */
    public static final String DELAYED_EXCHANGE = "delayed.exchange";
    /**
     * 延迟 RoutingKey
     */
    public static final String DELAYED_ROUTING_KEY = "delayed.routingKey";

    /**
     * 声明延迟交换机，基于插件的 x-delayed-message
     * @return {@code CustomExchange}
     */
    @Bean
    public CustomExchange delayedExchange() {
        Map<String, Object> argument = new HashMap<>();
        argument.put("x-delayed-type", "direct");
        return new CustomExchange(DELAYED_EXCHANGE, "x-delayed-message", true, false, argument);
    }

    /**
     * 声明延迟队列
     * @return {@code Queue}
     */
    @Bean
    public Queue delayedQueue() {
        return new Queue(DELAYED_QUEUE);
    }

    /**
     * 通过 RoutingKey delayed.routingKey 将 队列 delayed.queue 与 交换机 delayed.exchange 绑定
     * @param delayedQueue    延迟队列
     * @param delayedExchange 延迟交换机
     * @return {@code Binding}
     */
    @Bean
    public Binding delayedQueueBindDelayedExchange(@Qualifier("delayedQueue") Queue delayedQueue,
                                                   @Qualifier("delayedExchange") CustomExchange delayedExchange) {
        return BindingBuilder.bind(delayedQueue)
                .to(delayedExchange)
                .with(DELAYED_ROUTING_KEY)
                .noargs();
    }

}
