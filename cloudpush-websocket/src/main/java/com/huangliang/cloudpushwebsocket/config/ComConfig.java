package com.huangliang.cloudpushwebsocket.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class ComConfig {
    @Value("${hostip}")
    private String hostip;
    @Value("${eureka.instance.instance-id}")
    private String instanceId;
    @Value("${config.client.interval-time}")
    private Integer intervalTime;
    @Value("${config.client.expire-time}")
    private Integer expireTime;
    @Value("${config.client.interval-client-active-time}")
    private Integer intervalClientActiveTime;
}
