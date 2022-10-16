package com.infinite.rabbitmq.seven;

import com.infinite.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author jzx
 * @create 2022-10-13-21:16
 *
 * 声明主题交换机 及 相关队列
 */
public class ReceiveLogsTopic2 {

    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        // 声明队列
        String queueName = "Q2";
        channel.queueDeclare(queueName, false, false, false, null);
        /**
         *  # 表示 0 个 或 多个
         *  * 表示 1 个
         */
        channel.queueBind(queueName, EXCHANGE_NAME, "*.*.rabbit");
        channel.queueBind(queueName, EXCHANGE_NAME, "lazy.#");
        System.out.println("=== ReceiveLogsTopic2 等待接收消息 ===");
        channel.basicConsume(queueName, true, (consumerTag, message) -> {
            System.out.println("=== ReceiveLogsTopic2 接收到消息：" + new String(message.getBody(), StandardCharsets.UTF_8) + " ===");
            System.out.println("=== 接收队列：" + queueName + " , 绑定键：" + message.getEnvelope().getRoutingKey() + " ===");
        }, consumerTag -> {
            System.out.println("=== 消费者取消接收消息的回调 ===");
        });
    }

}
