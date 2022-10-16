package com.infinite.rabbitmq.four;

import com.infinite.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.MessageProperties;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

/**
 * @author jzx
 * @create 2022-10-11-16:31
 *
 * 发布确认模式：
 *   1、单个确认
 *   2、批量确认
 *   3、异步批量确认(异步确认发布)
 */
public class ConfirmMessage {

    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException {
        // 单个确认
//        ConfirmMessage.publishMessageIndividually();      // 发布1000条消息，单个确认模式，花费时间为：968ms
        // 批量确认
//        ConfirmMessage.publishMessageBatch();          // 发布1000条消息，批量确认模式，花费时间为：108ms
        // 异步批量确认
        ConfirmMessage.publishMessageAsync();           // 发布1000条消息，异步批量确认模式，花费时间为：91ms
    }


    /**
     * 单个发布确认
     * @throws IOException
     * @throws TimeoutException
     * @throws InterruptedException
     */
    public static void publishMessageIndividually() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMQUtils.getChannel();
        // 声明队列
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        // 开启发布确认
        channel.confirmSelect();
        // 开始时间
        long beginTime = System.currentTimeMillis();
        // 批量发消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            // 单个消息进行发布确认
            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println(i + " 消息发送成功");
            }
        }
        // 结束时间
        long endTime = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "条消息，单个确认模式，花费时间为：" + (endTime - beginTime) + "ms");
    }

    /**
     * 批量发布确认
     * @throws IOException
     * @throws TimeoutException
     * @throws InterruptedException
     */
    public static void publishMessageBatch() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMQUtils.getChannel();
        // 声明队列
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        // 开启发布确认
        channel.confirmSelect();
        // 开始时间
        long beginTime = System.currentTimeMillis();
        // 批量确认消息的大小
        int batchSize = 100;
        // 批量发送消息，并进行批量确认
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            // 判断达到 100条消息的时候，批量确认一次
            if (i % batchSize == 0) {
                channel.waitForConfirms();
            }
        }
        // 结束时间
        long endTime = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "条消息，批量确认模式，花费时间为：" + (endTime - beginTime) + "ms");
    }

    /**
     * 异步批量确认
     * @throws IOException
     * @throws TimeoutException
     */
    public static void publishMessageAsync() throws IOException, TimeoutException {
        Channel channel = RabbitMQUtils.getChannel();
        // 声明队列
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        // 开启发布确认
        channel.confirmSelect();
        /**
         * 线程安全有序的一个哈希表，适用于高并发情况下
         *   1、能够轻松的将序号与消息进行关联，Map，序号为 key, 消息为 value
         *   2、可以轻松的批量删除信息，通过序号（key）进行操作
         *   3、支持高并发（多线程）
         */
        ConcurrentSkipListMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();
        /**
         * 消息确认成功的回调函数
         * 参数说明：
         *   1、deliveryTag 表示消息的标识
         *   2、multiple 表示是否为批量确认
         */
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            // 判断是否为批量确认
            if (multiple) {
                // 2、删除掉已确认的消息，剩下的就是未确认的消息
                ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(deliveryTag);
                confirmed.clear();
            } else {
                // 不是批量就直接删除
                outstandingConfirms.remove(deliveryTag);
            }
            System.out.println("确认的消息：" + deliveryTag);
        };
        // 消息确认失败的回调函数
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            // 3、打印未确认的消息
            String nAckMessage = outstandingConfirms.get(deliveryTag);
            System.out.println("未确认的消息是：" + nAckMessage + "未确认消息的tag：" + deliveryTag);
        };
        // 准备消息的监听器，监听哪些消息成功了，哪些消息失败了；不监听则为 null
        channel.addConfirmListener(ackCallback, nackCallback);
        // 开始时间
        long beginTime = System.currentTimeMillis();
        // 批量发送消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "消息 " + i;
            channel.basicPublish("", queueName, null, message.getBytes());
            // 1、记录所有要发送的消息，消息的总和
            outstandingConfirms.put(channel.getNextPublishSeqNo(), message);
        }
        // 结束时间
        long endTime = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "条消息，异步批量确认模式，花费时间为：" + (endTime - beginTime) + "ms");
    }

}
