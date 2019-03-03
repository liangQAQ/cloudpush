package com.huangliang.cloudpushwebsocket.service;

import org.springframework.stereotype.Component;

@Component
public class Kafka {

    //@KafkaListener(id = "test",topics = {"server"})
    public void receive(String message){
        System.out.println("消费消息:" + message);
    }

}
