package com.huangliang.cloudpushwebsocket.controller;

import com.alibaba.fastjson.JSONObject;
import com.huangliang.api.constants.CommonConsts;
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
    private ChannelService channelService;
    @Autowired
    private MessageService messageService;

    @RequestMapping("/message/send")
    public Data sendToAllClient(String jsonData){
        Map<String,Channel> map = channelService.getAll();
        Long start = System.currentTimeMillis();
        for(Map.Entry<String, Channel> channel : map.entrySet()){
            channel.getValue().writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(messageService.genarateWebsocketMessage(jsonData))));
        }
        Long end = System.currentTimeMillis();
        log.info("耗时[{}]ms",end-start);
        return new Data(CommonConsts.SUCCESS,CommonConsts.REQUST_SUC);
    }
}
