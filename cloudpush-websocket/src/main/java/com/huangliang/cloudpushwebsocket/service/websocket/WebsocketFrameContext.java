package com.huangliang.cloudpushwebsocket.service.websocket;

import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public class WebsocketFrameContext {

    private WebsocketFrameService websocketFrameService;

    public WebsocketFrameContext(WebsocketFrameService websocketFrameService){
        this.websocketFrameService = websocketFrameService;
    }

    public void execute(WebSocketFrame Frame){
        websocketFrameService.execute(Frame);

    }
}
