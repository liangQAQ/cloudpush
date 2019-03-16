package com.huangliang.cloudpushwebsocket.service.websocket;

import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public interface IWebSocketService {
    public void handler(WebSocketFrame frame);
}
