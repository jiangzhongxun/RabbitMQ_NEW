package com.infinite.rabbitmq.five;

import com.infinite.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author jzx
 * @create 2022-10-13-16:08
 *
 * 接收消息
 */
public class ReceiveLogs1 {

    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        // 声明交换机————扇出
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        /**
         * 声明队列，临时队列
         *   生成的临时队列，队列名称是随机的，当消费者断开与队列的连接的时候，队列就自动删除了
         */
        String queueName = channel.queueDeclare().getQueue();
        /**
         * 绑定交换机与队列
         */
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println("=== 等待接收消息 ===");
        // 接收消息
        channel.basicConsume(queueName, true, (consumerTag, message) -> {
            System.out.println("ReceiveLogs1 接收到消息：" + new String(message.getBody(), StandardCharsets.UTF_8));
        }, consumerTag -> {
            System.out.println("取消接收消息的回调");
        });
    }

}
