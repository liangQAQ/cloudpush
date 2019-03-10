package com.huangliang.eureka.listener;

import com.huangliang.api.constants.Constants;
import com.huangliang.api.constants.RedisPrefix;
import com.netflix.appinfo.InstanceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.eureka.server.event.*;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 监听提供websocket的服务的状态，维护进redis，供portal服务提供websocket地址给客户端进行连接
 */
@Component
@Slf4j
public class EurekaStateChangeListener {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 服务下线事件
     * @param event
     */
    @EventListener
    public void listen(EurekaInstanceCanceledEvent event) {
        String appName = event.getAppName();
        String serverId = event.getServerId();
        if(Constants.WEBSOCKET_SERVER.equalsIgnoreCase(appName)){
            redisTemplate.opsForHash().delete(RedisPrefix.WEBSOCKETSERVER,serverId);
        }
        log.info("服务下线事件:"+appName.toLowerCase()+":"+serverId);
    }

    /**
     * 服务注册事件
     * @param event
     */
    @EventListener(condition = "#event.replication==false")
    public void listen(EurekaInstanceRegisteredEvent event) {
        InstanceInfo instanceInfo = event.getInstanceInfo();
        log.info("服务注册事件:"+instanceInfo.getAppName().toLowerCase()+"-"+instanceInfo.getIPAddr()+":"+instanceInfo.getPort());
        if(Constants.WEBSOCKET_SERVER.equalsIgnoreCase(instanceInfo.getAppName())){
            redisTemplate.opsForHash().put(RedisPrefix.WEBSOCKETSERVER,instanceInfo.getInstanceId(),"");
        }
    }

    @EventListener
    public void listen(EurekaRegistryAvailableEvent event) {

    }

    /**
     *  服务续约事件
     */
    @EventListener
    public void listen(EurekaInstanceRenewedEvent event) {
        log.info("服务续约事件");
    }

    @EventListener
    public void listen(EurekaServerStartedEvent event) {
        //Server启动
    }
}
