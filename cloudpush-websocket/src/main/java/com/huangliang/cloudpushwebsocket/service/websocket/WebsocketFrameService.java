package com.huangliang.cloudpushwebsocket.service.websocket;

import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public interface WebsocketFrameService {
    public void execute(WebSocketFrame Frame);
}
