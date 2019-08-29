package com.huangliang.cloudpushwebsocket.service.websocket.handlerText;

import com.alibaba.fastjson.JSONObject;
import com.huangliang.api.entity.WebsocketMessage;
import com.huangliang.cloudpushwebsocket.service.channel.ChannelService;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 处理心跳websocket消息类型
 */
@Service
public class HeartBeatService implements IMessageService {

    @Autowired
    private ChannelService channelService;

    @Override
    public void handler(Channel channel, WebsocketMessage websocketMessage) {
        //给客户端发送心跳消息回执
        channel.writeAndFlush(getFrame(websocketMessage));
        //更新客户端活跃时间
        channelService.updateActiveTime(channel);
    }

    private TextWebSocketFrame getFrame(WebsocketMessage websocketMessage){
        websocketMessage.setType(WebsocketMessage.Type.HEARTBEATACK.code);
        websocketMessage.setActiveTime(new Date());
        return new TextWebSocketFrame(JSONObject.toJSONString(websocketMessage));
    }
}
