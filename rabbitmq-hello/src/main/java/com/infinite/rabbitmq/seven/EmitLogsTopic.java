package com.infinite.rabbitmq.seven;

import com.infinite.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author jzx
 * @create 2022-10-13-21:37
 *
 * 生产者
 */
public class EmitLogsTopic {

    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        // 声明主题
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        // 发送消息
        Map<String, String> bingingKeyMap = new HashMap<>();
        bingingKeyMap.put("quick.orange.rabbit", "被队列Q1 Q2接收到");
        bingingKeyMap.put("lazy.orange.elephant", "被队列Q1 Q2接收到");
        bingingKeyMap.put("quick.orange.fox", "被队列Q1接收到");
        bingingKeyMap.put("lazy.brown.fox", "被队列Q2接收到");
        bingingKeyMap.put("lazy.pink.rabbit", "虽然满足两个绑定，但只被队列Q2接收一次");
        bingingKeyMap.put("quick.brow.fox", "不匹配任何绑定，不会被任何队列接收到，会被丢弃");
        bingingKeyMap.put("quick.orange.male.rabbit", "是四个单词，不匹配任何绑定，会被丢弃");
        bingingKeyMap.put("lazy.orange.male.rabbit", "是四个单词，但匹配Q2");
        for (Map.Entry<String, String> bingingKeyEntry : bingingKeyMap.entrySet()) {
            String routingKey = bingingKeyEntry.getKey();
            String message = bingingKeyEntry.getValue();
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("=== 生产者发出消息：" + message + " ===");
        }
    }

}
