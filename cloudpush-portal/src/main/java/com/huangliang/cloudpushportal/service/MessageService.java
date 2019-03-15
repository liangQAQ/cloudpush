package com.huangliang.cloudpushportal.service;

import com.huangliang.api.config.RocketMQConfig;
import com.huangliang.api.constants.RedisPrefix;
import com.huangliang.cloudpushportal.entity.req.SendFrom;
import io.github.rhwayfun.springboot.rocketmq.starter.common.DefaultRocketMqProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

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

    public void execute(SendFrom form){
        Map<String,String> client = null;
        for(String channelId : form.getTo()){
            //遍历list 依次存入推送消息
            //根据id找到对应的客户端对象所对应的实例名
            client = new HashMap();
            client = redisTemplate.opsForHash().entries(RedisPrefix.PREFIX_CLIENT+channelId);
            if(client==null){
                log.info("不存在的客户端[{}]",channelId);
                continue;
            }
            producer.sendMsg(getInstants(RocketMQConfig.getWebsocketTopic(client.get("host")),channelId,form.getMsg()));
        }
    }

    private Message getInstants(String topic,String channelId,String msg){
        //构建message消息体
        return new Message(topic,channelId,msg.getBytes());
    }

}
