package com.huangliang.cloudpushportal.service;

import com.huangliang.api.config.RocketMQConfig;
import com.huangliang.api.constants.Constants;
import com.huangliang.api.constants.RedisPrefix;
import com.huangliang.api.entity.WebsocketMessage;
import com.huangliang.cloudpushportal.entity.req.SendFrom;
import io.github.rhwayfun.springboot.rocketmq.starter.common.DefaultRocketMqProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 向RocketMQ投递消息操作类
 */
@Slf4j
@Service
public class MessageService {

    @Resource
    private DefaultRocketMqProducer producer;

    @Autowired
    private RedisTemplate redisTemplate;

    public void execute(SendFrom form) {
        Map<String, String> client = null;
        if (form.getSendToAll()) {
            //根据服务下的设备标识推送
            //1.查询redis中所有的websocket服务
            Set<String> set = redisTemplate.keys(RedisPrefix.PREFIX_SERVERCLIENTS + "*");
            if (set == null) {
                return;
            }
            //2.遍历各个服务下的设备进行推送
            //serverKey => serverclients_10.9.217.160:9003
            for(String serverKey : set){
                Set<String> clients = redisTemplate.opsForSet().members(serverKey);
                if (clients == null) {
                    continue;
                }
                //服务下的设备标识
                for (String clientItem : clients){
                    producer.sendMsg(getInstants(RocketMQConfig.getWebsocketTopic(serverKey.split("_")[1]), clientItem, form.getMsg()));
                }
            }
        } else {
            //根据参数中的客户端标识逐一推送
            for (String channelId : form.getTo()) {
                //遍历list 依次存入推送消息
                //根据id找到对应的客户端对象所对应的实例名
                client = new HashMap();
                client = redisTemplate.opsForHash().entries(RedisPrefix.PREFIX_CLIENT + channelId);
                if (CollectionUtils.isEmpty(client)) {
                    log.info("不存在的客户端[{}]", channelId);
                    continue;
                }
                producer.sendMsg(getInstants(RocketMQConfig.getWebsocketTopic(client.get("host")), channelId, form.getMsg()));
            }
        }
    }

    private Message getInstants(String topic, String channelId, String msg) {
        //构建message消息体
        Message message = new Message(topic, channelId, msg.getBytes());
        //由调用接口的方式触发消息
        message.putUserProperty(Constants.Trigger, WebsocketMessage.Trigger.HTTP.code + "");
        return message;
    }

}
