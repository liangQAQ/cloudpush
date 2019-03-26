package com.huangliang.cloudpushwebsocket.service;

import com.alibaba.fastjson.JSONObject;
import com.huangliang.api.config.RocketMQConfig;
import com.huangliang.api.constants.Constants;
import com.huangliang.api.entity.WebsocketMessage;
import io.github.rhwayfun.springboot.rocketmq.starter.common.AbstractRocketMqConsumer;
import io.github.rhwayfun.springboot.rocketmq.starter.constants.RocketMqContent;
import io.github.rhwayfun.springboot.rocketmq.starter.constants.RocketMqTopic;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    @Autowired
    private ChannelService clientsService;

    @Override
    public boolean consumeMsg(RocketMqContent content, MessageExt msg) {
        try {
            log.info("=====消费消息:"+new String(msg.getBody()));
            //msg中tag存的是客户端对应的标识
            Channel channel = clientsService.get(msg.getTags());
            if(channel == null){
                log.info("不存在的客户端");
                return false;
            }
            if(!channel.isOpen()) {
                log.info("[{}]客户端[{}]不可达，消息丢弃",channel.attr(com.huangliang.cloudpushwebsocket.constants.Constants.attrChannelId).get());
                return false;
            }
            //发起推送
//            channel.writeAndFlush(new TextWebSocketFrame(new String(msg.getBody())));
            channel.writeAndFlush(getMessage(msg));
            //更新活跃时间
            clientsService.updateActiveTime(channel);
            return true;
        }catch (Exception e){
            log.error("推送失败.",e);
        }
        return false;
    }

    //构造推送消息体
    private TextWebSocketFrame getMessage(MessageExt msg) {
        WebsocketMessage websocketMsg = new WebsocketMessage(
                msg.getMsgId(),
                WebsocketMessage.Type.SENDTOCLIENT.code,
                msg.getTags(),
                new String(msg.getBody()),
                Constants.SYSTEM,
                Integer.parseInt(msg.getProperty(Constants.Trigger))
                );
        return new TextWebSocketFrame(JSONObject.toJSONString(websocketMsg));
    }


    /**
     * 订阅该服务实例名的topic
     * @return
     */
    @Override
    public Map<String, Set<String>> subscribeTopicTags() {
        Map<String, Set<String>> map = new HashMap<>();
        map.put(RocketMQConfig.getWebsocketTopic(instanceId), null);
        return map;
    }

    /**
     * 消费组为服务实例名
     * @return
     */
    @Override
    public String getConsumerGroup() {
        return RocketMQConfig.getWebsocketGroup(instanceId);
    }

}