package com.infinite.rabbitmq.eight;

import com.infinite.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author jzx
 * @create 2022-10-14-10:55
 *
 * 死信队列  消费者 1
 */
public class Consumer1 {

    public static final String NORMAL_EXCHANGE = "normal_exchange";     // 普通交换机
    public static final String DEAD_EXCHANGE = "dead_exchange";         // 死信交换机
    public static final String NORMAL_QUEUE = "normal_queue";           // 普通队列
    public static final String DEAD_QUEUE = "dead_queue";               // 死信队列
    public static final String NORMAL_ROUTING_KEY = "zhangsan";         // 普通 RoutingKey
    public static final String DEAD_ROUTING_KEY = "lisi";               // 死信 RoutingKey

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        // 声明交换机 （普通交换机、死信交换机）
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
        // 声明普通队列
        Map<String, Object> arguments = new HashMap<>();
        /**
         * 设置消息过期时间 10s，可以在生产者端进行设置，比较灵活。设置在消费者端就不能灵活更改了
         *  x-message-ttl 是固定写法，表示过期时间
         */
        // arguments.put("x-message-ttl", 10000);
        /**
         * 设置正常队列的死信交换机，用于将消息从正常队列 转发到 死信交换机
         *  x-dead-letter-exchange 是固定写法
         */
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        /**
         * 设置死性 RoutingKey，用于将消息从死信交换机 路由绑定 到死信队列
         *  x-dead-letter-routing-key 是固定写法
         */
        arguments.put("x-dead-letter-routing-key", DEAD_ROUTING_KEY);
        /**
         * 设置正常队列的长度限制
         * x-max-length 是固定写法，表示正常队列的长度
         */
        arguments.put("x-max-length", 6);
        channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);
        // 声明死信队列
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);
        // 绑定交换机与队列
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, NORMAL_ROUTING_KEY);
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, DEAD_ROUTING_KEY);
        System.out.println("=== 消费者 C1 等待接收消息 ===");
        // 接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String msg = new String(message.getBody(), StandardCharsets.UTF_8);
            if ("info5".equals(msg)) {
                System.out.println("Consumer1 接收的消息是：" + msg + " 此消息是被C1拒绝的");
                // 拒绝
                channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
            } else {
                System.out.println("Consumer1 接收的消息是：" + msg);
                // 确认
                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
            }
        };
        channel.basicConsume(NORMAL_QUEUE, false, deliverCallback, consumerTag -> {});
    }

}
