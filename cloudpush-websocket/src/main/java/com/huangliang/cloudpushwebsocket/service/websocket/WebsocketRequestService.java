package com.huangliang.cloudpushwebsocket.service.websocket;

import com.huangliang.cloudpushwebsocket.service.channel.ChannelService;
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

    /**
     *
     * 策略模式
     * 判断是否关闭链路的指令
     * 判断是否ping消息
     * 判断是否二进制消息
     * 判断是否文本消息
     *
     * @param ctx
     * @param frame
     */
    public void handler(ChannelHandlerContext ctx, WebSocketFrame frame) {
        try {
            //处理消息
            websocketServiceStrategy.execute(ctx,frame);
        } catch (Exception e) {
            log.error("处理消息异常", e);
        }
    }
}
