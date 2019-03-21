package com.huangliang.cloudpushwebsocket.service.websocket;

import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TextWebSocketService implements IWebSocketService {
    @Override
    public void handler(WebSocketFrame frame) {
        log.info("text来了。。");
    }
}
