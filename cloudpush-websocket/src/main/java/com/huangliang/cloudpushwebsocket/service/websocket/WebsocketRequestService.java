package com.huangliang.cloudpushwebsocket.service.websocket;

import com.huangliang.cloudpushwebsocket.service.websocket.WebsocketServiceStrategy;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WebsocketRequestService {

    @Autowired
    private WebsocketServiceStrategy websocketServiceStrategy;

    public void handler(ChannelHandlerContext ctx, WebSocketFrame frame) {
        try {
            websocketServiceStrategy.execute(ctx,frame);
        } catch (Exception e) {
            log.error("处理消息异常", e);
        }
    }
}
