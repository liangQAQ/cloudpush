package com.huangliang.cloudpushgateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义路由
 */
@Component
@Slf4j
public class RedisRouteDefinitionRepository implements RouteDefinitionRepository {
    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        List<RouteDefinition> routeDefinitions = new ArrayList<>();
        RouteDefinition r = new RouteDefinition();
        try {
            r.setUri(new URI("ws://10.9.212.112:9000"));
        } catch (URISyntaxException e) {
            log.error("路径异常",e);
        }
        List<PredicateDefinition> predicateDefinitions = new ArrayList<>();
        PredicateDefinition p = new PredicateDefinition();
        p.setName("Path");
        Map<String, String> stringStringMap = new HashMap<>();
        stringStringMap.put("pattern","/websockett/**");
        p.setArgs(stringStringMap);
        predicateDefinitions.add(p);
        r.setPredicates(predicateDefinitions);
        routeDefinitions.add(r);
        return Flux.fromIterable(routeDefinitions);
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return null;
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return null;
    }
}
