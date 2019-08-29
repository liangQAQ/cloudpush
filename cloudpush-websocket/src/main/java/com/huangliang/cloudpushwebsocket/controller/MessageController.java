package com.huangliang.cloudpushwebsocket.controller;

import com.huangliang.api.annotation.LogOperate;
import com.huangliang.api.constants.CommonConsts;
import com.huangliang.api.entity.request.SendRequest;
import com.huangliang.api.entity.response.Data;
import com.huangliang.cloudpushwebsocket.service.message.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    @LogOperate
    @RequestMapping("/message/send")
    public Data sendToAllClient(@RequestBody SendRequest request){
        messageService.send(request);
        return new Data(CommonConsts.SUCCESS,CommonConsts.REQUST_SUC);
    }
}
