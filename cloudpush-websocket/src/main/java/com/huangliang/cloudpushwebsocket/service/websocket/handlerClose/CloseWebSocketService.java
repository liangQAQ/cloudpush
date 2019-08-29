package com.huangliang.cloudpushwebsocket.service.websocket.handlerClose;

import com.huangliang.cloudpushwebsocket.service.websocket.IWebSocketService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.springframework.stereotype.Service;

@Service
public class CloseWebSocketService implements IWebSocketService {
    @Override
    public void handler(ChannelHandlerContext ctx, WebSocketFrame frame) {

    }
}
