package com.huangliang.cloudpushportal.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class NacosTest {

    @Value("${useraaa:sss}")
    private String a1;

    @Value("${useLocalCache:false}")
    private boolean useLocalCache;

    @RequestMapping("/user")
    public String a1(){
        return a1+"_"+useLocalCache;
    }
}
