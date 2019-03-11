package com.huangliang.cloudpushwebsocket.service;

import com.huangliang.api.constants.Constants;
import com.huangliang.cloudpushwebsocket.netty.HttpRequestHandler;
import io.github.rhwayfun.springboot.rocketmq.starter.common.AbstractRocketMqConsumer;
import io.github.rhwayfun.springboot.rocketmq.starter.constants.RocketMqContent;
import io.github.rhwayfun.springboot.rocketmq.starter.constants.RocketMqTopic;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 处理MQ中的推送消息，对客户端发起推送
 */
@Service
@Slf4j
public class MqConsumerService extends AbstractRocketMqConsumer<RocketMqTopic, RocketMqContent> {

    @Value("${eureka.instance.instance-id}")
    private String instanceId;

    @Override
    public boolean consumeMsg(RocketMqContent content, MessageExt msg) {
        try {
            log.info("=====消费消息:"+new String(msg.getBody()));
            //msg中tag存的是客户端对应的标识
            Channel channel = HttpRequestHandler.channels.get(msg.getTags());
            if(channel == null){
                log.info("不存在的客户端");
                return false;
            }
            if(!channel.isOpen()) {
                log.info("客户端通道已关闭，消息丢弃");
                return false;
            }
            channel.writeAndFlush(new TextWebSocketFrame(new String(msg.getBody())));
            return true;
        }catch (Exception e){
            log.error("消费消息失败.",e);
        }
        return false;
    }

    /**
     * 订阅该服务实例名的topic
     * @return
     */
    @Override
    public Map<String, Set<String>> subscribeTopicTags() {
        Map<String, Set<String>> map = new HashMap<>();
        map.put(Constants.ROCKETMQ_TOPIC_PREFIX+getMqInstance(), null);
        return map;
    }

    /**
     * 消费组为服务实例名
     * @return
     */
    @Override
    public String getConsumerGroup() {
        return getMqInstance();
    }

    //mq群组中不允许出现实例中的":"，故替换成"-"
    private String getMqInstance(){
        return instanceId.replace(":","-").replace(".","-");
    }

}