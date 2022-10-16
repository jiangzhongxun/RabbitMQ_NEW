package com.infinite.rabbitmq.utils;

/**
 * 睡眠工具类
 * @author jzx
 * @create 2022-10-10-22:04
 */
public class SleepUtils {

    public static void sleep(int second) {
        try {
            Thread.sleep(1000L * second);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
