package com.infinite.rabbitmq.one;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 生产者
 * @author jzx
 * @date 2022/10/06
 */
public class Producer {

    public static final String QUEUE_NAME = "hello";
    public static final String HOST_NAME = "192.168.1.88";
    public static final String USER = "admin";
    public static final String PASSWORD = "admin";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 设置工厂 IP 连接 RabbitMQ 的队列
        connectionFactory.setHost(HOST_NAME);
        // 用户名
        connectionFactory.setUsername(USER);
        // 密码
        connectionFactory.setPassword(PASSWORD);
        // 创建连接
        Connection connection = connectionFactory.newConnection();
        // 获取信道
        Channel channel = connection.createChannel();
        /**
         * 生成一个队列
         * 参数说明：
         *   1、queue 队列名称
         *   2、durable 队列中的消息是否进行持久化（磁盘），默认情况下消息存储在内存中
         *   3、exclusive 该队列是否只供一个消费者进行消费，是否进行消息共享，true 表示可以多个消费者消费
         *   4、autoDelete 是否自动删除，最后一个消费者端开连接以后，该队列是否自动删除，true 表示自动删除
         *   5、Map<String, Object> arguments 其他参数，如 延迟消息、死信消息，没有时可以为 null
         */
        Map<String, Object> argument = new HashMap<>();
        argument.put("x-max-priority", 10);   // 队列优先级的范围，官方允许是 0--255，此处设置 10，则允许范围是 0--10
        channel.queueDeclare(QUEUE_NAME, false, false, false, argument);

        for (int i = 1; i < 11; i++) {
            String message = "hello world!" + i;
            if (i == 5) {
                AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().priority(5).build();
                channel.basicPublish("", QUEUE_NAME, properties, message.getBytes());
            } else {
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            }
        }
        /**
         * 发送消息
         * 参数说明：
         *   1、exchange 交换机，没有时可以为 ""
         *   2、routingKey 路由key，队列名
         *   3、props 其他参数，没有时可以为 null
         *   4、body 消息体
         */
//        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println("=== message publish success！！！ ===");
    }

}
