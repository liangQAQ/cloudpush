package com.huangliang.eureka.listener;

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
     * @param eurekaInstanceCanceledEvent
     */
    @EventListener
    public void listen(EurekaInstanceCanceledEvent eurekaInstanceCanceledEvent) {
        String appName = eurekaInstanceCanceledEvent.getAppName();
        String serverId = eurekaInstanceCanceledEvent.getServerId();
        log.info("服务下线事件:"+appName+"-"+serverId);
    }

    /**
     * 服务注册事件
     * @param eurekaInstanceRegisteredEvent
     */
    @EventListener
    public void listen(EurekaInstanceRegisteredEvent eurekaInstanceRegisteredEvent) {
        InstanceInfo instanceInfo = eurekaInstanceRegisteredEvent.getInstanceInfo();

        redisTemplate.opsForHash().put(RedisPrefix.SERVER_COUNT,instanceInfo.getAppName(),0);
        log.info("服务注册事件:"+instanceInfo.getAppName()+"-"+instanceInfo.getIPAddr()+"-"+instanceInfo.getStatus());
    }

    @EventListener
    public void listen(EurekaRegistryAvailableEvent event) {

    }

    /**
     *  服务续约事件
     */
    @EventListener
    public void listen(EurekaInstanceRenewedEvent event) {
    }

    @EventListener
    public void listen(EurekaServerStartedEvent event) {
        //Server启动
    }
}
