package com.infinite.rabbitmq.one;

import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消费者
 * @author jzx
 * @date 2022/10/06
 */
public class Consumer {

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
         * 创建消费者
         * 参数说明：
         *   1、queue 队列名
         *   2、autoAck 消费成功之后是否要自动应答，true 表示自动应答
         *   3、deliverCallback 消费者成功消费的回调
         *   4、cancelCallback 消费者取消消费的回调
         */
        // 声明接收消息的回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("消费者成功消费 " + new String(message.getBody()));
        };
        // 声明取消消息的回调
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消费者取消 / 被中断 消费");
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }

}
