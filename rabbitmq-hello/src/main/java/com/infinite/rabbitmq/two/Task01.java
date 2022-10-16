package com.infinite.rabbitmq.two;

import com.infinite.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * 生产者，发送消息
 * @author jzx
 * @create 2022-10-07-16:19
 */
public class Task01 {

    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        /**
         * 生成一个队列
         * 参数说明：
         *   1、queue 队列名称
         *   2、durable 队列中的消息是否进行持久化（磁盘），默认情况下消息存储在内存中
         *   3、exclusive 该队列是否只供一个消费者进行消费，是否进行消息共享，true 表示可以多个消费者消费
         *   4、autoDelete 是否自动删除，最后一个消费者端开连接以后，该队列是否自动删除，true 表示自动删除
         *   5、Map<String, Object> arguments 其他参数，如 延迟消息、死信消息，没有时可以为 null
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        /**
         * 参数说明：
         *   1、exchange 交换机，没有时可以为 ""
         *   2、routingKey 路由key，队列名
         *   3、props 其他参数，没有时可以为 null
         *   4、body 消息体
         */
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("=== message publish succeed：" + message);
        }
    }

}
