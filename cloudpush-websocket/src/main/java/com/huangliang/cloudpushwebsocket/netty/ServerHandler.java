package com.huangliang.cloudpushwebsocket.netty;

import com.huangliang.cloudpushwebsocket.constants.CommonConsts;
import com.huangliang.cloudpushwebsocket.constants.Constants;
import com.huangliang.cloudpushwebsocket.constants.ErrorConstants;
import com.huangliang.cloudpushwebsocket.entity.Websocket;
import com.huangliang.cloudpushwebsocket.entity.response.Message;
import com.huangliang.cloudpushwebsocket.service.HttpResponseHandler;
import com.huangliang.cloudpushwebsocket.util.NettyUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;

import java.util.Map;

@ChannelHandler.Sharable
@Component
public class ServerHandler extends SimpleChannelInboundHandler<Object> {

    private static ChannelGroup group;
	
	private static Logger log = null;

    @Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private HttpRequestHandler httpRequestHandler;
	@Autowired
	private HttpResponseHandler httpResponseHandler;

	private WebSocketServerHandshaker handshaker;

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg){
		try {
			/**
			 * HTTP接入，WebSocket第一次连接使用HTTP连接,用于握手
			 */
			if (msg instanceof FullHttpRequest) {
				shakeHandsHandler(ctx, (FullHttpRequest) msg);
//				httpRequestHandler.handler(ctx, (FullHttpRequest) msg);
			}
//			/**
//			 * Websocket 消息请求
//			 */
			else if (msg instanceof WebSocketFrame) {
//				WebsocketRequestHandler.handler(ctx, (WebSocketFrame) msg);
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

	/**
	 * 处理握手请求
	 * @param ctx
	 * @param req
	 */
	private void shakeHandsHandler(ChannelHandlerContext ctx, FullHttpRequest req){
		//解析握手请求
		String channelId = "";
		Map<String,String> requestParam = NettyUtil.getRequestParams(req);
		if(requestParam.containsKey(Constants.CHANNELID))
		{
			channelId = requestParam.get(Constants.CHANNELID);
		}else{
			httpResponseHandler.responseJson(ctx, new Message(CommonConsts.SUCCESS, ErrorConstants.ErrorChannelId));
			log.error("握手失败:缺少channelId");
			return ;
		}
		WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws://127.0.0.1"+req.getUri(),
				null, false);
		handshaker = wsFactory.newHandshaker(req);
		if (handshaker == null) {
			WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
		} else {
//			handshaker.handshake(ctx.channel().attr("channelId").set(channelId);, req);
			//存redis缓存
            redisTemplate.opsForValue().set("websocket_"+channelId,new Websocket(channelId,ctx.channel()));
			//以websocket的形式将标识返回
			ctx.channel().writeAndFlush(new TextWebSocketFrame(channelId));
		}
	}
}