package com.infinite.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * RabbitMQ 工具类
 * @author jzx
 * @date 2022/10/06
 */
public class RabbitMQUtils {

//    public static final String HOST_NAME = "192.168.0.66";
    public static final String HOST_NAME = "192.168.1.66";
    public static final String USER = "admin";
    public static final String PASSWORD = "admin";

    /**
     * 通过连接工厂创建信道
     * @return {@code Channel}
     * @throws IOException io异常
     * @throws TimeoutException 超时异常
     */
    public static Channel getChannel() throws IOException, TimeoutException {
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
        return connection.createChannel();
    }

}
