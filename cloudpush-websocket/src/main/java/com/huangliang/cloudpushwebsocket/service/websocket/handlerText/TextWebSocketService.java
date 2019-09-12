package com.huangliang.cloudpushwebsocket.service.websocket.handlerText;

import com.alibaba.fastjson.JSONObject;
import com.huangliang.api.entity.WebsocketMessage;
import com.huangliang.cloudpushwebsocket.constants.AttrConstants;
import com.huangliang.cloudpushwebsocket.service.websocket.IWebSocketService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 处理客户端发过来的推送消息
 */
@Service
@Slf4j
public class TextWebSocketService implements IWebSocketService {

    @Autowired
    private MessageServiceStrategy messageServiceStrategy;

    @Override
    public void handler(ChannelHandlerContext ctx, WebSocketFrame frame) {
        String str = ((TextWebSocketFrame) frame).text();
        if (StringUtils.isEmpty(str)) {
            return ;
        }
        Channel channel = ctx.channel();
        log.info("receive[{}]:" + str,channel.attr(AttrConstants.channelId).get());
        //按规定规则解析消息
        WebsocketMessage msg = init(str);
        if(msg == null ){
            return ;
        }
        //处理消息
        messageServiceStrategy.handler(channel,msg);
    }

    private WebsocketMessage init(String str) {
        try {
            return JSONObject.parseObject(str, WebsocketMessage.class);
        } catch (Exception e) {
            log.error("json解析失败,无法识别的消息", e);
            return null;
        }
    }
}
