package com.huangliang.cloudpushwebsocket.service.websocket;

import com.huangliang.cloudpushwebsocket.constants.Constants;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PingWebSocketService implements IWebSocketService{
    @Override
    public void handler(ChannelHandlerContext ctx,WebSocketFrame frame) {
        log.info("[{}]Ping来了。。。。",ctx.channel().attr(Constants.attrChannelId).get());
        ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
        return ;
    }
}
