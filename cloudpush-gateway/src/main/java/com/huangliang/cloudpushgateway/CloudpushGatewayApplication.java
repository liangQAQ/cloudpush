package com.huangliang.cloudpushgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CloudpushGatewayApplication {

//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//        return builder.routes()
//                //basic proxy
//                .route(r -> r.path("/baidu/ss")
//                        .uri("http://10.9.212.119:8001/server/get")
//                ).build();
//    }

    public static void main(String[] args) {
        SpringApplication.run(CloudpushGatewayApplication.class, args);
    }

}
