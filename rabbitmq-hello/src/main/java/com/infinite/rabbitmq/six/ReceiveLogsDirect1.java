package com.infinite.rabbitmq.six;

import com.infinite.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author jzx
 * @create 2022-10-13-17:04
 */
public class ReceiveLogsDirect1 {

    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        // 声明交换机————直接交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        // 声明队列
        String queueName = "console";
        channel.queueDeclare(queueName, false, false, false, null);
        channel.queueBind(queueName, EXCHANGE_NAME, "info");
        channel.queueBind(queueName, EXCHANGE_NAME, "warning");
        // 接收消息
        channel.basicConsume(queueName, true, (consumerTag, message) -> {
            System.out.println("ReceiveLogsDirect1 接收到消息：" + new String(message.getBody(), StandardCharsets.UTF_8));
        }, consumerTag -> {
            System.out.println("取消接收消息的回调");
        });
    }

}
