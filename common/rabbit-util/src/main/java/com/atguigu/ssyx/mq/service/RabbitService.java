package com.atguigu.ssyx.mq.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.management.ObjectName;

/**
 * @BelongsProject: guigu-ssyx-parent
 * @BelongsPackage: com.atguigu.ssyx.mq
 * @Author: zt
 * @CreateTime: 2023-06-12  15:07
 * @Description:
 */

@Service
public class RabbitService {

    @Resource
    private RabbitTemplate rabbitTemplate;

    //发送消息的方法
    //exchange 交换机
    //routingKey 路由
    //message 消息
    public boolean sendMessage(String exchange, String routingKey, Object message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
        return true;
    }

}
