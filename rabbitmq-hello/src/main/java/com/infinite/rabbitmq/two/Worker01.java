package com.infinite.rabbitmq.two;

import com.infinite.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 工作线程（相当于消费者）
 * @author jzx
 * @create 2022-10-07-15:55
 */
public class Worker01 {

    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        System.out.println("=== 消费者 2 ===");
        /**
         * 参数说明：
         *   1、queue 队列名
         *   2、autoAck 消费成功之后是否要自动应答，true 表示自动应答
         *   3、deliverCallback 消费者成功消费的回调
         *   4、cancelCallback 消费者取消消费的回调
         */
        channel.basicConsume(QUEUE_NAME, true, (consumerTag, message) -> {
            System.out.println("接收到的消息： " + new String(message.getBody()));
        }, consumerTag -> {
            System.out.println("取消 / 中端消费");
        });
    }

}
