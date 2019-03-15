package com.huangliang.cloudpushwebsocket.service;

import com.huangliang.api.constants.RedisPrefix;
import com.huangliang.api.entity.Client;
import com.huangliang.api.util.ObjUtils;
import com.huangliang.cloudpushwebsocket.constants.Constants;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 维护客户端集合的操作类
 */
@Slf4j
@Service
public class ClientsService {

    @Value("${eureka.instance.instance-id}")
    public String instanceId;

    @Autowired
    private RedisTemplate redisTemplate;

    //初始化1000容量，减少扩容带来的消耗
    private static Map<String,Channel> channels = new ConcurrentHashMap(1000);

    /**
     * 根据channelId获取单个对象
     * @param channelId
     * @return
     */
    public Channel get(String channelId){
        return channels.get(channelId);
    }

    /**
     * 获取所有对象
     * @return
     */
    public Set<Channel> getAll(){
        return (Set)channels.values();
    }

    /**
     * 放入对象
     * @param channelId
     * @param channel
     * @return
     */
    public Channel put(String channelId,Channel channel){
        try {
            //缓存客户端信息
            redisTemplate.opsForHash().putAll(RedisPrefix.PREFIX_CLIENT+channelId, ObjUtils.ObjToMap(new Client(channelId,instanceId)));
            //缓存服务端与客户端关联信息
            redisTemplate.opsForSet().add(RedisPrefix.PREFIX_SERVERCLIENTS+instanceId,channelId);
            //给channel对象绑定channelId标识
            channel.attr(Constants.attrChannelId).set(channelId);
            log.info("加入了客户端：[{}]",channelId);
            return  channels.put(channelId,channel);
        }catch (Exception e){
            log.error("加入客户端失败.",e);
        }
        return null;
    }
}
