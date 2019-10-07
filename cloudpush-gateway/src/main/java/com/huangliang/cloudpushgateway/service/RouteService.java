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

    //30s刷新一次本地缓存
    @PostConstruct
    @Scheduled(fixedRate=30000)
    public void refresh(){
        List<RouteDefinition> currentRoutes = new ArrayList<>();
        //获取websocket服务的实例列表
        Map<String,String> map = redisTemplate.opsForHash().entries(RedisPrefix.WEBSOCKETSERVER);
        //获取websocket服务的实例对应的权重列表
        Map<String,Integer> instantceWeight = redisTemplate.opsForHash().entries(RedisPrefix.WEBSOCKETWEIGHT);
        int totalCount = 0;
        int instanceCount = 0;
        for(Map.Entry<String,String> entry : map.entrySet()){
            if(StringUtils.isEmpty(entry.getValue())){
                continue;//只返回设置好netty websocket端口的实例地址
            }
            RouteDefinition route = getRouteDefinition(entry,instantceWeight);
            currentRoutes.add(route);
            totalCount = totalCount + instanceCount ;
        }
        this.routes = currentRoutes;
    }

    /**
     *
     * @param entry      websocket实例名  对应的端口
     * @param instanceWeight    对应的实例权重
     * @return
     */
    private RouteDefinition getRouteDefinition(Map.Entry<String,String> entry,Map<String,Integer> instanceWeight){
        RouteDefinition r = new RouteDefinition();
        try {
            String websocketUrl =CommonConsts.STRING_WS //ws://
                    +entry.getKey().split(CommonConsts.COLON_FLAG)[0]//ip
                    +CommonConsts.COLON_FLAG+entry.getValue()//:port
                    +CommonConsts.FILE_SEPARATOR;// /
            r.setId(WS_Predicate_GROUP+"_"+websocketUrl);
            r.setUri(new URI(websocketUrl));
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
        weight.addArg("weight.weight",instanceWeight.size()<=0?"1":instanceWeight.get(entry.getKey())+"");
        predicateDefinitions.add(weight);

        r.setPredicates(predicateDefinitions);
        return r;
    }

    public List<RouteDefinition> getRoutes() {
        return routes;
    }
}
