package com.infinite.rabbitmq.consumer;

import com.infinite.rabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author jzx
 * @create 2022-10-15-15:43
 *
 * 发布确认 消费者 （ 高级篇 ）
 */
@Slf4j
@Component
public class ConfirmConsumer {

    /**
     * 接收消息   发布确认
     * @param message 消息
     */
    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE)
    public void receiveConfirm(Message message) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("接收到的消息为：{}", msg);
    }

}
