package com.huangliang.cloudpushwebsocket.controller;

import com.alibaba.fastjson.JSONObject;
import com.huangliang.api.constants.CommonConsts;
import com.huangliang.api.entity.request.SendRequest;
import com.huangliang.api.entity.response.Data;
import com.huangliang.cloudpushwebsocket.service.ChannelService;
import com.huangliang.cloudpushwebsocket.service.MessageService;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@Slf4j
@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    @RequestMapping("/message/send")
    public Data sendToAllClient(SendRequest request){
        messageService.send(request);
        return new Data(CommonConsts.SUCCESS,CommonConsts.REQUST_SUC);
    }
}
