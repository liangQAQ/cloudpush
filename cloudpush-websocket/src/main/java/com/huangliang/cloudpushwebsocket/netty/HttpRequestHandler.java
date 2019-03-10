package com.huangliang.cloudpushwebsocket.netty;

import com.huangliang.api.constants.RedisPrefix;
import com.huangliang.api.util.ObjUtils;
import com.huangliang.cloudpushwebsocket.constants.CommonConsts;
import com.huangliang.cloudpushwebsocket.constants.Constants;
import com.huangliang.cloudpushwebsocket.constants.ErrorConstants;
import com.huangliang.api.entity.Client;
import com.huangliang.cloudpushwebsocket.entity.response.Message;
import com.huangliang.cloudpushwebsocket.service.HttpResponseHandler;
import com.huangliang.cloudpushwebsocket.util.NettyUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ChannelHandler.Sharable
@Component
@Slf4j
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	public static Map<String,Channel> channels = new ConcurrentHashMap(1000);

	@Value("${eureka.instance.instance-id}")
	public String instanceId;

    @Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private HttpRequestHandler httpRequestHandler;
	@Autowired
	private HttpResponseHandler httpResponseHandler;

	private WebSocketServerHandshaker handshaker;

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg){
		try {
			shakeHandsHandler(ctx, msg);
		} catch (Exception e) {
		    e.printStackTrace();
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
		Channel channel = null;
		Map<String,String> requestParam = NettyUtil.getRequestParams(req);
		if(requestParam.containsKey(Constants.CHANNELID))
		{
			channelId = requestParam.get(Constants.CHANNELID);
			if(StringUtils.isNotEmpty(channelId)){
				//赋值客户端连接对象
				channel = ctx.channel();
				channel.attr(Constants.attrChannelId).set(channelId);
			}
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
			handshaker.handshake(ctx.channel(), req);
			channels.put(channelId,channel);
			log.info(channels.get(channelId).attr(Constants.attrChannelId).get());
			//缓存客户端信息
            redisTemplate.opsForHash().putAll(RedisPrefix.PREFIX_CLIENT+channelId, ObjUtils.ObjToMap(new Client(channelId,instanceId)));
            //缓存服务端与客户端关联信息
			redisTemplate.opsForSet().add(RedisPrefix.PREFIX_SERVERCLIENTS+instanceId,channelId);
			//以websocket的形式将标识返回
			ctx.channel().writeAndFlush(new TextWebSocketFrame(channelId));
		}
	}
}