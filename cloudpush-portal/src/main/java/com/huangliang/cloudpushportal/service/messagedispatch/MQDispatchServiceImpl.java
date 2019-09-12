package com.huangliang.cloudpushportal.service.messagedispatch;

import com.alibaba.fastjson.JSONObject;
import com.huangliang.api.config.RocketMQConfig;
import com.huangliang.api.constants.Constants;
import com.huangliang.api.entity.WebsocketMessage;
import com.huangliang.api.entity.request.SendRequest;
import io.github.rhwayfun.springboot.rocketmq.starter.common.DefaultRocketMqProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.Message;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 基于MQ的形式，分发消息给对应客户端所在的websocket服务器所订阅的topic
 */
@Service
@Slf4j
public class MQDispatchServiceImpl implements MessageDispatchService {

    @Resource
    private DefaultRocketMqProducer producer;

    @Override
    public void send(String instants, SendRequest request) {
        producer.sendMsg(getInstants(instants,  request));
    }

    private Message getInstants(String topic, SendRequest msg) {
        //构建message消息体
        Message message = new Message(RocketMQConfig.getWebsocketTopic(topic), JSONObject.toJSONString(msg).getBytes());
        //由调用接口的方式触发消息
        message.putUserProperty(Constants.Trigger, WebsocketMessage.Trigger.HTTP.code + "");
        return message;
    }
}
