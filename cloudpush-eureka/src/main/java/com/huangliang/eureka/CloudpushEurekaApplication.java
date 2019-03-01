package com.huangliang.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class CloudpushEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudpushEurekaApplication.class, args);
    }

}
