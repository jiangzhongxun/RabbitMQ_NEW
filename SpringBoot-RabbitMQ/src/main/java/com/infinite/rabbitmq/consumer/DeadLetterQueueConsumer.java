package com.infinite.rabbitmq.consumer;

import com.infinite.rabbitmq.config.TtlQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author jzx
 * @create 2022-10-15-10:57
 *
 * 接收延迟消息 队列TTL 消费者
 */

@Slf4j
@Component
public class DeadLetterQueueConsumer {

    /**
     * 死信队列
     */
    public static final String DEAD_LETTER_QUEUE = "QD";

    /**
     * 接收死信队列的延迟消息
     * @param message 消息
     */
//    @RabbitListener(queues = TtlQueueConfig.DEAD_LETTER_QUEUE)
    @RabbitListener(queues = DEAD_LETTER_QUEUE)
    public void receiveD(Message message) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("当前时间：{}，收到的死信队列的消息：{}", new Date().toString(), msg);
    }

}
