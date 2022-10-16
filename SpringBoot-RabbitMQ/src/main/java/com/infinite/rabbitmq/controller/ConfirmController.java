package com.infinite.rabbitmq.controller;

import com.infinite.rabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jzx
 * @create 2022-10-15-15:38
 *
 * 发布确认 生产者 （ 高级篇 ）
 */
@Slf4j
@RestController
@RequestMapping("confirm")
public class ConfirmController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息   发布确认
     * @param message 消息
     */
    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable String message) {
        CorrelationData correlationData = new CorrelationData("1");
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE, ConfirmConfig.CONFIRM_ROUTING_KEY, message, correlationData);
        log.info("发送的消息为：{}", message);
    }

}
