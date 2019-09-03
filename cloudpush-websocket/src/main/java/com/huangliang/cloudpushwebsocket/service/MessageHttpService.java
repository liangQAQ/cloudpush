package com.huangliang.cloudpushwebsocket.service;

import com.huangliang.api.entity.request.SendRequest;
import com.huangliang.cloudpushwebsocket.service.channel.ChannelService;
import com.huangliang.cloudpushwebsocket.service.message.MessageSendService;
import com.huangliang.cloudpushwebsocket.util.WebsocketMessageGenerateUtils;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 处理http的推送请求
 */
@Slf4j
@Service
public class MessageHttpService {

    @Autowired
    private ChannelService channelService;
    @Autowired
    private MessageSendService messageSendService;

    public void send(SendRequest request){
        Map<String,Channel> map = channelService.getAll();
        if(request.getSendToAll()){
            //遍历服务上的所有设备进行推送
            for(String channelId : map.keySet()){
                messageSendService.sendMessage(channelId,WebsocketMessageGenerateUtils.generateWebsocketMessage(channelId,request.getRequestId(),request.getMsg()));
            }
        }else{
            //根据标识进行推送
            List<String> list = request.getTo();
            if(CollectionUtils.isEmpty(list)){
                return ;
            }
            for(String client : list){
                messageSendService.sendMessage(client,WebsocketMessageGenerateUtils.generateWebsocketMessage(client,request.getRequestId(),request.getMsg()));
            }
        }
    }
}
