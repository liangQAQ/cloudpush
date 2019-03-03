package com.huangliang.cloudpushwebsocket.service;//package com.huangliang.websocket.service;
//
//import com.huangliang.constants.Constants;
//import com.huangliang.ehcache.UserGroupCacheService;
//import com.huangliang.entity.message.ReceiveMessage;
//import com.huangliang.entity.message.SendMessage;
//import com.huangliang.service.SendMessageService;
//import com.huangliang.util.JsonUtil;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.handler.codec.http.websocketx.*;
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class WebsocketRequestHandler {
//
//	private static Logger log = Logger.getLogger(WebsocketRequestHandler.class);
//
//	@Autowired
//	SendMessageService sendMessageService;
//
//	@Autowired
//	UserGroupCacheService userGroupCacheService;
//
//	public void handler(ChannelHandlerContext ctx, WebSocketFrame frame) {
//		/**
//		 * 判断是否关闭链路的指令
//		 */
//		if (frame instanceof CloseWebSocketFrame) {
//			ctx.close();
//			return;
//		}
//		/**
//		 * 判断是否ping消息
//		 */
//		if (frame instanceof PingWebSocketFrame) {
//			ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
//			return;
//		}
//		/**
//		 * 支持文本消息，不支持二进制消息
//		 */
//		if (frame instanceof BinaryWebSocketFrame) {
//			throw new UnsupportedOperationException(String.format("%s frame types not supported", frame.getClass()
//					.getName()));
//		}
//		if (frame instanceof TextWebSocketFrame) {
//			try {
//				// 返回应答消息
//				String sendMsg = ((TextWebSocketFrame) frame).text();
//				log.info("websocket message:"+sendMsg);
//				//按约定好的json格式解析
//				ReceiveMessage msgEntity = JsonUtil.jsonToBean(sendMsg, ReceiveMessage.class);
//				if(Constants.SendType.ChannelId.code.equals(msgEntity.getSendType())){
//					//根据推送标识推送
//					sendToChannel(ctx,msgEntity);
//				}else if(Constants.SendType.GroupCode.code.equals(msgEntity.getSendType())){
//					//根据群组标识推送
//					sendToGroup(ctx,msgEntity);
//				}
//			} catch (Exception e) {
//				log.error("发送消息异常",e);
//			}
//		}
//	}
//
//	private void sendToChannel(ChannelHandlerContext ctx,ReceiveMessage msgEntity){
//		String[] channelIds = msgEntity.getTo().split(",");
//		SendMessage msg = msgEntity.getSendMessage();
//		for(String channelId:channelIds){
//			sendMessageService.send(ctx,channelId,msg);
//		}
//	}
//
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
//}
