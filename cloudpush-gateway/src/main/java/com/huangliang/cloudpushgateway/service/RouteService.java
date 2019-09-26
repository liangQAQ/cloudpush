package com.huangliang.cloudpushgateway.service;

import com.huangliang.api.constants.CommonConsts;
import com.huangliang.api.constants.Constants;
import com.huangliang.api.constants.RedisPrefix;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.support.WeightConfig;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * 定时扫描redis中websocket的地址，刷新本地缓存
 */
@Component
@EnableScheduling
@Slf4j
public class RouteService {

    @Autowired
    private RedisTemplate redisTemplate;
    //拦截ws的url
    @Value("${config.route.websocket.path}")
    private String WS_Predicate_Path;
    //拦截ws的权重群组名
    @Value("${config.route.websocket.group}")
    private String WS_Predicate_GROUP;

    private List<RouteDefinition> routes = null;

    private long lastActiveTime = System.currentTimeMillis();

    //当有请求时,定时刷新websocket路由表配置
    @PostConstruct
    @Scheduled(fixedRate=60000)
    public void refresh(){
        List<RouteDefinition> currentRoutes = new ArrayList<>();
        //获取websocket服务的实例列表
        Map<String,String> map = redisTemplate.opsForHash().entries(RedisPrefix.WEBSOCKETSERVER);
        for(Map.Entry<String,String> entry : map.entrySet()){
            if(StringUtils.isEmpty(entry.getValue())){
                continue;//只返回设置好netty websocket端口的实例地址
            }
            String websocketUrl =CommonConsts.STRING_WS //ws://
                    +entry.getKey().split(CommonConsts.COLON_FLAG)[0]//ip
                    +CommonConsts.COLON_FLAG+entry.getValue()//:port
                    +CommonConsts.FILE_SEPARATOR;// /
            RouteDefinition route = getRouteDefinition(websocketUrl,websocketUrl);
            currentRoutes.add(route);
        }
        this.routes = currentRoutes;
        log.info("websocket刷新路由配置成功:"+currentRoutes);
    }

    private RouteDefinition getRouteDefinition(String url,String websocketUrl){
        RouteDefinition r = new RouteDefinition();
        try {
            r.setId(WS_Predicate_GROUP+"_"+websocketUrl);
            r.setUri(new URI(url));
        } catch (URISyntaxException e) {
            log.error("路径异常",e);
        }
        List<PredicateDefinition> predicateDefinitions = new ArrayList<>();
        PredicateDefinition path = new PredicateDefinition();

        //设置路径
        path.setName("Path");
        path.addArg("pattern",WS_Predicate_Path);
        predicateDefinitions.add(path);

        //设置权重
        PredicateDefinition weight = new PredicateDefinition();
        weight.setName("Weight");
        weight.addArg("weight.group",WS_Predicate_GROUP);
        weight.addArg("weight.weight","1");//每个url权重都是1
        predicateDefinitions.add(weight);

        r.setPredicates(predicateDefinitions);
        return r;
    }

    public List<RouteDefinition> getRoutes() {
        return routes;
    }
}
