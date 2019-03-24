package com.huangliang.cloudpushwebsocket.service.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TextWebSocketService implements IWebSocketService {
    @Override
    public void handler(ChannelHandlerContext ctx,WebSocketFrame frame) {

        log.info("text来了。。");
    }
}
