package com.huangliang.cloudpushwebsocket.service;

import com.huangliang.cloudpushwebsocket.constants.Constants;
import com.huangliang.cloudpushwebsocket.service.websocket.WebsocketServiceFactory;
import com.huangliang.cloudpushwebsocket.util.JsonUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class WebsocketRequestService {

    @Autowired
    private WebsocketServiceFactory websocketServiceFactory;


    public void handler(ChannelHandlerContext ctx, WebSocketFrame frame) {
        try {
            websocketServiceFactory.execute(ctx,frame);
        } catch (Exception e) {
            log.error("处理消息异常", e);
        }
    }
}
