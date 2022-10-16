package com.infinite.rabbitmq.three;

import com.infinite.rabbitmq.utils.RabbitMQUtils;
import com.infinite.rabbitmq.utils.SleepUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author jzx
 * @create 2022-10-10-21:46
 *
 * 要求消息在消费者手动应答出现问题时不丢失，并能放回队列中重新消费
 */
public class Worker2 {

    private static final String TASK_QUEUE_NAME = "ack-queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        System.out.println("=== 消费者 C1 等待接收消息处理时间较短 ===");
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            /**
             * 模拟场景，沉睡 1S
             */
            SleepUtils.sleep(1);
            System.out.println("接收到的消息：" + new String(message.getBody(), StandardCharsets.UTF_8));
            /**
             * 手动应答
             *   参数说明：
             *     1、deliveryTag 表示消息的标识 tag
             *     2、multiple 表示是否批量应答，true 为批量应答
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };
        /**
         * 设置 不公平分发、预取值
         * prefetchCount = 0 表示轮训
         * prefetchCount = 1 表示不公平分发
         * prefetchCount = 其他值 表示预取值
         */
        // int prefetchCount = 1;
        int prefetchCount = 2;
        channel.basicQos(prefetchCount);
        /**
         * 采用手动应答
         */
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, deliverCallback, consumerTag -> {
            System.out.println("消费者取消消费消息的回调逻辑");
        });

    }
}
