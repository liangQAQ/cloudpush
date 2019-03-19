package com.huangliang.cloudpushportal.controller;

import com.huangliang.api.config.RocketMQConfig;
import com.huangliang.cloudpushportal.entity.req.SendFrom;
import com.huangliang.cloudpushportal.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    @RequestMapping(value="/message/send",method = RequestMethod.POST)
    public String send(@RequestBody @Valid SendFrom form){
        messageService.execute(form);
        System.out.println(form);

        return form.toString();
    }
}
