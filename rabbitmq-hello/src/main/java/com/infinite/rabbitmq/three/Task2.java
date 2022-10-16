package com.infinite.rabbitmq.three;

import com.infinite.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author jzx
 * @create 2022-10-10-21:36
 *
 * 要求消息在消费者手动应答出现问题时不丢失，并能放回队列中重新消费
 */
public class Task2 {

    private static final String TASK_QUEUE_NAME = "ack-queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        /**
         * 开启发布确认
         */
        channel.confirmSelect();
        /**
         * 将队列持久化
         */
        boolean durable = true;
        channel.queueDeclare(TASK_QUEUE_NAME, durable, false, false, null);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            /**
             * 设置生产者发送消息为持久化消息，（要求保存到磁盘上），未持久化是保存在内存中
             */
            channel.basicPublish("",
                    TASK_QUEUE_NAME,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发出消息：" + message);
        }
    }

}
