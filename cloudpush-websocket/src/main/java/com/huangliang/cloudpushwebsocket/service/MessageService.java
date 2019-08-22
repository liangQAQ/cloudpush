package com.huangliang.cloudpushwebsocket.service;

import com.alibaba.fastjson.JSONObject;
import com.huangliang.api.constants.Constants;
import com.huangliang.api.entity.WebsocketMessage;
import com.huangliang.api.entity.request.SendRequest;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MessageService {

    @Autowired
    private ChannelService channelService;

    public void send(SendRequest request){
        Map<String,Channel> map = channelService.getAll();
        if(request.getSendToAll()){
            //遍历服务上的所有设备进行推送
            for(Map.Entry<String, Channel> entry : map.entrySet()){
                Channel channel = entry.getValue();
                if(!channel.isOpen()){
                    log.info("[{}]设备不可达.",entry.getKey());
                    continue;
                }
                String msgData = JSONObject.toJSONString(
                        generateWebsocketMessage(entry.getKey(),request.getRequestId(),request.getMsg()));
                channel.writeAndFlush(new TextWebSocketFrame(msgData));
            }
        }else{
            //根据标识进行推送
            List<String> list = request.getTo();
            if(CollectionUtils.isEmpty(list)){
                return ;
            }
            for(String client : list){
                Channel channel = map.get(client);
                if(channel==null){
                    log.info("[{}]设备不存在与当前服务器.",client);
                    continue;
                }
                if(!channel.isOpen()){
                    log.info("[{}]设备不可达.",client);
                    continue;
                }
                String msgData = JSONObject.toJSONString(
                        generateWebsocketMessage(client,request.getRequestId(),request.getMsg()));
                channel.writeAndFlush(new TextWebSocketFrame(msgData));
            }
        }
    }


    public WebsocketMessage generateWebsocketMessage(String channelId,String msgJson){
        return generateWebsocketMessage(channelId,null,null,msgJson);
    }

    public WebsocketMessage generateWebsocketMessage(String channelId,String requestId,String msgJson){
        return generateWebsocketMessage(channelId,requestId,null,msgJson);
    }

    //根据mq中的内容构建发送消息
    public WebsocketMessage generateWebsocketMessage(MessageExt msg){
        return generateWebsocketMessage(msg.getTags(),null,null,new String(msg.getBody()));
    }

    //根据请求中的参数构建发送消息
    public WebsocketMessage generateWebsocketMessage(String channelId,String requestId,String msgId,String msgJson){
        WebsocketMessage websocketMsg = new WebsocketMessage(
                msgId,
                WebsocketMessage.Type.SENDTOCLIENT.code,
                channelId,
                new String(msgJson),
                Constants.SYSTEM,
                WebsocketMessage.Trigger.HTTP.code
        );
        return websocketMsg;
    }
}
