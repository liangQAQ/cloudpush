package com.huangliang.cloudpushwebsocket.netty;

import com.huangliang.api.constants.CommonConsts;
import com.huangliang.api.constants.Constants;
import com.huangliang.cloudpushwebsocket.constants.ErrorConstants;
import com.huangliang.api.entity.response.Message;
import com.huangliang.cloudpushwebsocket.service.ChannelService;
import com.huangliang.cloudpushwebsocket.service.HttpResponseService;
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
import org.springframework.stereotype.Component;

import java.util.Map;

@ChannelHandler.Sharable
@Component
@Slf4j
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	@Autowired
	private HttpResponseService httpResponseService;

	@Autowired
	private ChannelService clientsService;

	private WebSocketServerHandshaker handshaker;

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg){
		try {
			shakeHandsHandler(ctx, msg);
		} catch (Exception e) {
		    e.printStackTrace();
		}

	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
	}

	/**
	 * 处理握手请求
	 * @param ctx
	 * @param req
	 */
	private void shakeHandsHandler(ChannelHandlerContext ctx, FullHttpRequest req){
		//解析握手请求
		String channelId = "";
		Channel channel = ctx.channel();
		Map<String,String> requestParam = NettyUtil.getRequestParams(req);
		if(!requestParam.containsKey(Constants.CHANNELID)||!StringUtils.isNotEmpty(requestParam.get(Constants.CHANNELID)))
		{
			httpResponseService.responseJson(ctx, new Message(CommonConsts.SUCCESS, ErrorConstants.ErrorChannelId));
			log.error("握手失败:缺少channelId");
			return ;
		}
		channelId = requestParam.get(Constants.CHANNELID);
		WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws://127.0.0.1"+req.getUri(),
				null, false);
		handshaker = wsFactory.newHandshaker(req);
		if (handshaker == null) {
			WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(channel);
		} else {
			//处理握手,协议升级
			handshaker.handshake(ctx.channel(), req);
			//将客户端放入集合
			clientsService.put(channelId,channel);
			//以websocket的形式将标识返回
			ctx.channel().writeAndFlush(new TextWebSocketFrame(channelId));
		}
	}
}