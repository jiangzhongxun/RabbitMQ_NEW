package com.infinite.rabbitmq.eight;

import com.infinite.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author jzx
 * @create 2022-10-14-16:37
 *
 * 死信队列 消费者 2
 */
public class Consumer2 {

    public static final String DEAD_QUEUE = "dead_queue";               // 死信队列

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        System.out.println("=== 消费者 C2 等待接收消息 ===");
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(new String(message.getBody(), StandardCharsets.UTF_8));
        };
        // 接收消息
        channel.basicConsume(DEAD_QUEUE, true, deliverCallback, consumerTag -> {});
    }

}
