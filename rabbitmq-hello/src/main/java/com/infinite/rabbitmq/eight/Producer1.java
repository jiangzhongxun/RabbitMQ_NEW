package com.infinite.rabbitmq.eight;

import com.infinite.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author jzx
 * @create 2022-10-14-16:26
 *
 * 死信队列 生产者 1
 */
public class Producer1 {

    public static final String NORMAL_EXCHANGE = "normal_exchange";     // 普通交换机
    public static final String NORMAL_ROUTING_KEY = "zhangsan";         // 普通 RoutingKey

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        // 死信消息，设置 TTL 时间，单位是 ms
        AMQP.BasicProperties properties = null;
//                new AMQP.BasicProperties()
//                .builder()
//                .expiration("10000")
//                .build();
        // 发送消息
        for (int i = 1; i < 11; i++) {
            String message = "info" + i;
            channel.basicPublish(NORMAL_EXCHANGE, NORMAL_ROUTING_KEY, properties, message.getBytes());
        }
    }

}
