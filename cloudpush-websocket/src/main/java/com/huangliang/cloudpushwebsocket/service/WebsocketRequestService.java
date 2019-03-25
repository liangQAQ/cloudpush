package com.huangliang.cloudpushwebsocket.service;

import com.huangliang.cloudpushwebsocket.constants.Constants;
import com.huangliang.cloudpushwebsocket.service.websocket.WebsocketServiceFactory;
import com.huangliang.cloudpushwebsocket.util.JsonUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class WebsocketRequestService {

    @Autowired
    private WebsocketServiceFactory websocketServiceFactory;


    public void handler(ChannelHandlerContext ctx, WebSocketFrame frame) {
        try {
            websocketServiceFactory.execute(ctx,frame);
        } catch (Exception e) {
            log.error("发送消息异常", e);
        }
    }

//	private void sendToChannel(ChannelHandlerContext ctx,ReceiveMessage msgEntity){
//		String[] channelIds = msgEntity.getTo().split(",");
//		SendMessage msg = msgEntity.getSendMessage();
//		for(String channelId:channelIds){
//			sendMessageService.send(ctx,channelId,msg);
//		}
//	}

//	private void sendToGroup(ChannelHandlerContext ctx,ReceiveMessage msgEntity){
//		String groupCode = msgEntity.getTo();
//		SendMessage msg = msgEntity.getSendMessage();
//		List<String> clients = userGroupCacheService.get(groupCode);
//		if(clients!=null){
//			for(String channelId:clients){
//				sendMessageService.send(ctx,channelId,msg);
//			}
//		}
//	}
}
