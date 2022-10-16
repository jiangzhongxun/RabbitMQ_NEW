package com.infinite.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jzx
 * @create 2022-10-15-14:59
 *
 * 发布确认 配置类   ( 高级篇 )
 */

@Configuration
public class ConfirmConfig {

    /**
     * 发布确认交换机
     */
    public static final String CONFIRM_EXCHANGE = "confirm.exchange";
    /**
     * 发布确认队列
     */
    public static final String CONFIRM_QUEUE = "confirm.queue";
    /**
     * 发布确认 RoutingKey
     */
    public static final String CONFIRM_ROUTING_KEY = "key1";
    /**
     * 备份交换机
     */
    public static final String BACKUP_EXCHANGE = "backup.exchange";
    /**
     * 备份队列
     */
    public static final String BACKUP_QUEUE = "backup.queue";
    /**
     * 报警队列
     */
    public static final String WARNING_QUEUE = "warning.queue";

    /**
     * 声明发布确认交换机
     * @return {@code DirectExchange}
     */
    @Bean("confirmExchange")
    public DirectExchange confirmExchange() {
//        return new DirectExchange(CONFIRM_EXCHANGE);
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE)
                .durable(true)
                .alternate(BACKUP_EXCHANGE)     //  .withArgument("alternate-exchange", BACKUP_EXCHANGE)
                .build();
    }

    /**
     * 声明备份交换机
     * @return {@code FanoutExchange}
     */
    @Bean("backupExchange")
    public FanoutExchange backupExchange() {
        return new FanoutExchange(BACKUP_EXCHANGE);
    }

    /**
     * 声明发布确认队列
     * @return {@code Queue}
     */
    @Bean("confirmQueue")
    public Queue confirmQueue() {
        return QueueBuilder.durable(CONFIRM_QUEUE)
                .build();
    }

    /**
     * 声明备份队列
     * @return {@code Queue}
     */
    @Bean("backupQueue")
    public Queue backupQueue() {
        return QueueBuilder.durable(BACKUP_QUEUE)
                .build();
    }

    /**
     * 声明报警队列
     * @return {@code Queue}
     */
    @Bean("warningQueue")
    public Queue warningQueue() {
        return QueueBuilder.durable(WARNING_QUEUE)
                .build();
    }

    /**
     * 通过 RoutingKey confirm.routingKey 将 队列confirm.queue 与 交换机confirm.exchange 绑定
     * @param confirmQueue    发布确认队列
     * @param confirmExchange 发布确认交换机
     * @return {@code Binding}
     */
    @Bean
    public Binding confirmQueueBindConfirmExchange(@Qualifier("confirmQueue") Queue confirmQueue,
                                                   @Qualifier("confirmExchange") DirectExchange confirmExchange) {
        return BindingBuilder.bind(confirmQueue)
                .to(confirmExchange)
                .with(CONFIRM_ROUTING_KEY);
    }

    /**
     * 将 队列backup.queue 与 交换机backup.exchange 绑定
     * @param backupQueue    备份队列
     * @param backupExchange 备份交换机
     * @return {@code Binding}
     */
    @Bean
    public Binding backupQueueBindBackupExchange(@Qualifier("backupQueue") Queue backupQueue,
                                                 @Qualifier("backupExchange") FanoutExchange backupExchange) {
        return BindingBuilder.bind(backupQueue).to(backupExchange);
    }

    /**
     * 将 队列warning.queue 与 交换机backup.exchange 绑定
     * @param warningQueue   报警队列
     * @param backupExchange 备份交换机
     * @return {@code Binding}
     */
    @Bean
    public Binding warningQueueBindBackupExchange(@Qualifier("warningQueue") Queue warningQueue,
                                                  @Qualifier("backupExchange") FanoutExchange backupExchange) {
        return BindingBuilder.bind(warningQueue).to(backupExchange);
    }

}
