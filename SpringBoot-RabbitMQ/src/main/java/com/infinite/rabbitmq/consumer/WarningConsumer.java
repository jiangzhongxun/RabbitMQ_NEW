package com.infinite.rabbitmq.consumer;

import com.infinite.rabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author jzx
 * @create 2022-10-16-11:38
 *
 * 报警 消费者 ( 高级篇 )
 */

@Slf4j
@Component
public class WarningConsumer {

    @RabbitListener(queues = ConfirmConfig.WARNING_QUEUE)
    public void receiveWarningMsg(Message message) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("发现报警的路由不可达信息：{}", msg);
    }

}
