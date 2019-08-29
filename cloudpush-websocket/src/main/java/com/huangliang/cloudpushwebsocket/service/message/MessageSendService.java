package com.huangliang.cloudpushwebsocket.service.message;


import com.alibaba.fastjson.JSONObject;
import com.huangliang.api.entity.WebsocketMessage;
import com.huangliang.cloudpushwebsocket.service.channel.ChannelService;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 消息发送类
 */
@Slf4j
@Service
public class MessageSendService {

    @Autowired
    private ChannelService channelService;

    /**
     * 具体的推送实现方法
     * @param channelId 推送的目标对象客户端标识
     * @param wsMessage 推送的具体内容
     */
    public void sendMessage(String channelId, WebsocketMessage wsMessage){
        Channel channel = channelService.get(channelId);
        //0.校验客户端合法性
        if(channel==null){
            log.info("找不到该设备[{}].",channelId);
            return;
        }
        if(!channel.isOpen()){
            log.info("设备不可达[{}].",channelId);
            return;
        }
        //1.发起对客户端的推送(websocket消息)
        channel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(wsMessage)));
        //2.修改客户端的活跃时间
        channelService.updateActiveTime(channel);
        //3.记录日志

    }
}
