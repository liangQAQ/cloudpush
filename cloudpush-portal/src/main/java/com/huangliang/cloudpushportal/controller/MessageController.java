package com.huangliang.cloudpushportal.controller;

import com.huangliang.api.config.RocketMQConfig;
import com.huangliang.cloudpushportal.entity.req.SendFrom;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

@RestController
public class MessageController {

    @RequestMapping(value="/message/send",method = RequestMethod.POST)
    public String send(@RequestBody @Valid SendFrom form){


        return "";
    }
}
