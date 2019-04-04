package com.huangliang.cloudpushportal.controller;

import com.huangliang.api.config.RocketMQConfig;
import com.huangliang.cloudpushportal.entity.req.SendFrom;
import com.huangliang.cloudpushportal.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

@RestController
@Api(value = "MessageController")
        public class MessageController {

    @Autowired
    private MessageService messageService;

    @ApiOperation(value="消息推送接口", notes="根据用户标识进行推送")
    @RequestMapping(value="/message/send",method = RequestMethod.POST)
    public String send(@RequestBody @Valid SendFrom form){
        messageService.execute(form);

        return "success";
    }
}
