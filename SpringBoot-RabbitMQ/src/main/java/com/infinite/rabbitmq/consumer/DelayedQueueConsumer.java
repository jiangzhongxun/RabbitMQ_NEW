package com.infinite.rabbitmq.consumer;

import com.infinite.rabbitmq.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author jzx
 * @create 2022-10-15-14:04
 *
 * 接收基于插件的延迟消息 队列delayed.queue 的 消费者
 */

@Slf4j
@Component
public class DelayedQueueConsumer {

    /**
     * 接收延迟队列的延迟消息
     * @param message 消息
     */
    @RabbitListener(queues = DelayedQueueConfig.DELAYED_QUEUE)
    public void receiveDelayed(Message message) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("当前时间：{}，收到的延迟队列的消息：{}", new Date().toString(), msg);
    }

}
