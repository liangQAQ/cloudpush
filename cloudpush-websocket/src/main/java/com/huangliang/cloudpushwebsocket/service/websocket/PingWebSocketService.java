package com.huangliang.cloudpushwebsocket.service.websocket;

import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PingWebSocketService implements IWebSocketService{
    @Override
    public void handler(WebSocketFrame frame) {
        log.info("Ping来了。。。。");
    }
}
