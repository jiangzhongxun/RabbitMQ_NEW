package com.infinite.rabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;

/**
 * @author jzx
 * @create 2022-10-15-17:04
 *
 * 回调接口
 */

@Slf4j
@Component
public class MyCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 注入
     * 因为 ConfirmCallback、ReturnCallback 是一个内部接口，需要将当前类的实例注入到 RabbitTemplate 中
     * 而 @PostConstruct 注解在其他注解完成之后才执行
     */
    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }

    /**
     * 发消息给交换机，交换机确认回调方法————成功 或 失败 都会调用
     * @param correlationData   回调信息的ID和相关信息
     * @param ack               ack 是否应答，收到消息为true，未收到消息为false
     * @param cause             cause 接收消息失败的原因，成功则为null
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            log.info("交换机接收到 ID 为：{}的消息", id);
        } else {
            log.info("交换机没有接收到 ID 为：{}的消息，接收消息失败的原因为：{}", id, cause);
        }
    }

    /**
     * 回退方法————当消息传递过程中不可达目的地时，将消息返回给生产者
     * @param message    消息
     * @param replyCode  应答代码
     * @param replyText  应答文本
     * @param exchange   交换机
     * @param routingKey 路由key
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("消息 {}，被交换机 {} 退回，退回原因 {}，退回代码 {}，路由Key：{}",
                new String(message.getBody(), StandardCharsets.UTF_8), exchange, replyText, replyCode, routingKey);
    }

}
