package com.infinite.rabbitmq.controller;

import com.infinite.rabbitmq.config.DelayedQueueConfig;
import com.infinite.rabbitmq.config.TtlQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Date;

/**
 * @author jzx
 * @create 2022-10-15-10:47
 *
 * 发送延迟消息 生产者
 */

@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMsgController {

    /**
     * RabbitMQ 模板
     */
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息
     * @param message 消息
     */
    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable String message) {
        log.info("当前时间：{}，发送一条消息给两个TTL队列：{}", new Date().toString(), message);
        rabbitTemplate.convertAndSend(TtlQueueConfig.NORMAL_EXCHANGE,
                TtlQueueConfig.NORMAL_ROUTING_KEY_A,
                "消息来自TTL为 10s的队列：" + message);
        rabbitTemplate.convertAndSend(TtlQueueConfig.NORMAL_EXCHANGE,
                TtlQueueConfig.NORMAL_ROUTING_KEY_B,
                "消息来自TTL为 40s的队列：" + message);
    }

    /**
     * 发送消息，基于消息和过期(TTL)时间
     * @param message 消息
     * @param ttlTime ttl时间
     */
    @GetMapping("/sendExpirationMsg/{message}/{ttlTime}")
    public void sengMsg(@PathVariable String message,
                        @PathVariable String ttlTime) {
        log.info("当前时间：{}，发送一个TTL为：{}毫秒的消息给队列QC：{}", new Date().toString(), ttlTime, message);
        rabbitTemplate.convertAndSend(TtlQueueConfig.NORMAL_EXCHANGE, TtlQueueConfig.NORMAL_ROUTING_KEY_C, message, msg -> {
            // 发送消息的时长，延迟时长
            msg.getMessageProperties().setExpiration(ttlTime);
            return msg;
        });
    }

    /**
     * 发送消息，基于插件的 消息和延迟（delayed）时间
     * @param message     消息
     * @param delayedTime 延迟时间
     */
    @GetMapping("/sendDelayedMsg/{message}/{delayedTime}")
    public void sendMsg(@PathVariable String message,
                        @PathVariable Integer delayedTime) {
        log.info("当前时间：{}，发送一个延迟时间为：{}毫秒的消息给延迟队列delayed.queue：{}", new Date().toString(), delayedTime, message);
        rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE, DelayedQueueConfig.DELAYED_ROUTING_KEY, message, msg -> {
            // 发送消息的时长，延迟时长
            msg.getMessageProperties().setDelay(delayedTime);
            return msg;
        });
    }

}
