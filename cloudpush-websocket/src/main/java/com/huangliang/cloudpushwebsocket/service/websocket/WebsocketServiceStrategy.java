package com.huangliang.cloudpushwebsocket.service.websocket;

import com.huangliang.cloudpushwebsocket.service.websocket.handlerClose.CloseWebSocketService;
import com.huangliang.cloudpushwebsocket.service.websocket.handlerPing.PingWebSocketService;
import com.huangliang.cloudpushwebsocket.service.websocket.handlerText.TextWebSocketService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

@Service
public class WebsocketServiceStrategy {

    @Autowired
    private PingWebSocketService pingWebSocketService;
    @Autowired
    private TextWebSocketService textWebSocketService;
    @Autowired
    private CloseWebSocketService closeWebSocketService;

    private static Map<Class,IWebSocketService> map = new HashedMap();

    @PostConstruct
    private void init() {
        map.put(CloseWebSocketFrame.class,closeWebSocketService);
        map.put(PingWebSocketFrame.class,pingWebSocketService);
        map.put(TextWebSocketFrame.class,textWebSocketService);
    }

    private IWebSocketService getInstants(WebSocketFrame frame){
        return map.get(frame.getClass());
    }

    public void execute(ChannelHandlerContext ctx, WebSocketFrame frame){
        getInstants(frame).handler(ctx,frame);
    }
}
