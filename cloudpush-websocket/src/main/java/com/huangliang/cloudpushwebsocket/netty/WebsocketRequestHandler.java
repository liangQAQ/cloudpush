package com.huangliang.cloudpushwebsocket.netty;

import com.huangliang.cloudpushwebsocket.constants.Constants;
import com.huangliang.cloudpushwebsocket.service.HttpResponseHandler;
import com.huangliang.cloudpushwebsocket.util.JsonUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ChannelHandler.Sharable
@Component
@Slf4j
public class WebsocketRequestHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    @Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private HttpRequestHandler httpRequestHandler;
	@Autowired
	private HttpResponseHandler httpResponseHandler;

	private WebSocketServerHandshaker handshaker;

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame){
		try {
			/**
			 * 判断是否关闭链路的指令
			 */
		if (frame instanceof CloseWebSocketFrame) {
			ctx.close();
			return;
		}
		/**
		 * 判断是否ping消息
		 */
		if (frame instanceof PingWebSocketFrame) {
			ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
			return;
		}
		/**
		 * 支持文本消息，不支持二进制消息
		 */
		if (frame instanceof BinaryWebSocketFrame) {
			throw new UnsupportedOperationException(String.format("%s frame types not supported", frame.getClass()
					.getName()));
		}
		if (frame instanceof TextWebSocketFrame) {
			try {
				// 返回应答消息
				String sendMsg = ((TextWebSocketFrame) frame).text();
				log.info("websocket message:"+sendMsg);
				log.info("channelId:"+ctx.channel().attr(Constants.attrChannelId).get());
//				//按约定好的json格式解析
//				ReceiveMessage msgEntity = JsonUtil.jsonToBean(sendMsg, ReceiveMessage.class);
//				if(Constants.SendType.ChannelId.code.equals(msgEntity.getSendType())){
//					//根据推送标识推送
//					sendToChannel(ctx,msgEntity);
//				}else if(Constants.SendType.GroupCode.code.equals(msgEntity.getSendType())){
//					//根据群组标识推送
//					sendToGroup(ctx,msgEntity);
//				}
			} catch (Exception e) {
				log.error("发送消息异常",e);
			}
		}
		} catch (Exception e) {
		    e.printStackTrace();
//			log.error("请求异常",e);
//			httpResponseHandler.responseFailed(ctx);
		}

	}

//	@Override
//	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//		ctx.flush();
//	}
//
//	@Override
//	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//		cause.printStackTrace();
//	}

}