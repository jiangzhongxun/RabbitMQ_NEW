package com.infinite.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jzx
 * @create 2022-10-14-23:08
 *
 * TTL 队列  配置文件类代码
 */

@Configuration
public class TtlQueueConfig {

    /**
     * 正常交换机
     */
    public static final String NORMAL_EXCHANGE = "X";
    /**
     * 死信交换机
     */
    public static final String DEAD_LETTER_EXCHANGE = "Y";
    /**
     * 正常队列 A
     */
    public static final String NORMAL_QUEUE_A = "QA";
    /**
     * 正常队列 B
     */
    public static final String NORMAL_QUEUE_B = "QB";
    /**
     * 通用队列 C
     */
    public static final String NORMAL_QUEUE_C = "QC";
    /**
     * 死信队列
     */
    public static final String DEAD_LETTER_QUEUE = "QD";
    /**
     * 正常 RoutingKey A
     */
    public static final String NORMAL_ROUTING_KEY_A = "XA";
    /**
     * 正常 RoutingKey B
     */
    public static final String NORMAL_ROUTING_KEY_B = "XB";
    /**
     * 通用 RoutingKey C
     */
    public static final String NORMAL_ROUTING_KEY_C = "XC";
    /**
     * 死信 RoutingKey
     */
    public static final String DEAD_LETTER_ROUTING_KEY = "YD";

    /**
     * 声明正常交换机
     * @return {@code DirectExchange}
     */
    @Bean("xExchange")
    public DirectExchange xExchange() {
        return new DirectExchange(NORMAL_EXCHANGE);
    }

    /**
     * 声明死信交换机
     * @return {@code DirectExchange}
     */
    @Bean("yExchange")
    public DirectExchange yExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE);
    }

    /**
     * 声明正常队列 A     TTL 为 10s
     * @return {@code Queue}
     */
    @Bean("queueA")
    public Queue queueA() {
        Map<String, Object> argument = new HashMap<>(3);
        // 设置死信交换机
        argument.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
        // 设置死信 RoutingKey
        argument.put("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY);
        // 设置过期时间 TTL
        argument.put("x-message-ttl", 10000);
        return QueueBuilder.durable(NORMAL_QUEUE_A)
                .withArguments(argument)
                .build();
    }

    /**
     * 声明正常队列 B     TTL 为 40s
     * @return {@code Queue}
     */
    @Bean("queueB")
    public Queue queueB() {
        Map<String, Object> argument = new HashMap<>(3);
        // 设置死信交换机
        argument.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
        // 设置死信 RoutingKey
        argument.put("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY);
        // 设置过期时间 TTL
        argument.put("x-message-ttl", 40000);
        return QueueBuilder.durable(NORMAL_QUEUE_B)
                .withArguments(argument)
                .build();
    }

    /**
     * 声明通用队列 B
     * @return {@code Queue}
     */
    @Bean("queueC")
    public Queue queueC() {
        Map<String, Object> argument = new HashMap<>(3);
        // 设置死信交换机
        argument.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
        // 设置死信 RoutingKey
        argument.put("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY);
        return QueueBuilder.durable(NORMAL_QUEUE_C)
                .withArguments(argument)
                .build();
    }

    /**
     * 声明死信队列
     * @return {@code Queue}
     */
    @Bean("queueD")
    public Queue queueD() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE)
                .build();
    }

    /**
     * 通过 RoutingKey XA 将 队列 QA 与 交换机 X 绑定
     * @param queueA    queueA
     * @param xExchange X 交换机
     * @return {@code Binding}
     */
    @Bean
    public Binding queueABindingX(@Qualifier("queueA") Queue queueA,
                                  @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueA)
                .to(xExchange)
                .with(NORMAL_ROUTING_KEY_A);
    }

    /**
     * 通过 RoutingKey XB 将 队列 QB 与 交换机 X 绑定
     * @param queueB    queueB
     * @param xExchange X 交换机
     * @return {@code Binding}
     */
    @Bean
    public Binding queueBBindingX(@Qualifier("queueB") Queue queueB,
                                  @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueB)
                .to(xExchange)
                .with(NORMAL_ROUTING_KEY_B);
    }

    /**
     * 通过 RoutingKey XC 将 通用队列 QC 与 交换机 X 绑定
     * @param queueC    queueC
     * @param xExchange X 交换机
     * @return {@code Binding}
     */
    @Bean
    public Binding queueCBindingX(@Qualifier("queueC") Queue queueC,
                                  @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueC)
                .to(xExchange)
                .with(NORMAL_ROUTING_KEY_C);
    }

    /**
     * 通过 RoutingKey YD 将 队列 QD 与 交换机 Y 绑定
     * @param queueD    queueD
     * @param yExchange Y 交换机
     * @return {@code Binding}
     */
    @Bean
    public Binding queueDBindingY(@Qualifier("queueD") Queue queueD,
                                  @Qualifier("yExchange") DirectExchange yExchange) {
        return BindingBuilder.bind(queueD)
                .to(yExchange)
                .with(DEAD_LETTER_ROUTING_KEY);
    }

}
