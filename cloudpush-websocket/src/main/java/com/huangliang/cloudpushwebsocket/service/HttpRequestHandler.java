package com.huangliang.cloudpushwebsocket.service;//package com.huangliang.websocket.service;
//
//import com.huangliang.action.GetUserAction;
//import com.huangliang.action.GroupAction;
//import com.huangliang.action.SendMessageAction;
//import com.huangliang.constants.CommonConsts;
//import com.huangliang.constants.Constants;
//import com.huangliang.constants.ErrorConstants;
//import com.huangliang.ehcache.WebsocketCacheService;
//import com.huangliang.entity.WebsocketEntity;
//import com.huangliang.entity.response.Message;
//import com.huangliang.exception.CommonException;
//import com.huangliang.netty.Server;
//import com.huangliang.util.NettyUtil;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.handler.codec.http.FullHttpRequest;
//import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
//import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
//import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Map;
//
//@Service
//public class HttpRequestHandler {
//
//	private static Logger log = Logger.getLogger(HttpRequestHandler.class);
//
//	private final String wsPath = "ws://127.0.0.1:"+Server.portNumber;
//
//	@Autowired
//	private WebsocketCacheService websocketCacheService;
//
//	@Autowired
//	private HttpResponseHandler httpResponseHandler;
//
//	@Autowired
//	private SendMessageAction sendMessage;
//
//	@Autowired
//	private GetUserAction getUser;
//
//	@Autowired
//	private GroupAction groupAction;
//
//	private WebSocketServerHandshaker handshaker;
//
//	/**
//	 * 处理http请求的入口
//	 * @param ctx
//	 * @param req
//	 * @throws CommonException
//	 */
//	public void handler(ChannelHandlerContext ctx, FullHttpRequest req) throws CommonException {
//		if (!req.getDecoderResult().isSuccess() || (!isShakeHands(req))) {
//			//处理普通http请求
//			handHttpReq(ctx,req);
//		}else{
//			//处理websocket握手请求
//			shakeHandsHandler(ctx,req);
//		}
//	}
//
//	public void handHttpReq(ChannelHandlerContext ctx, FullHttpRequest req) throws CommonException{
//
//		String reqUrl = subReqUrl(req.getUri());
//
//		//拦截url
//		if(Constants.HttpUrlType.GetGroupUser.url.equals(reqUrl)){
//			getUser.getGroupUser(ctx,req);
//		}else if(Constants.HttpUrlType.GetUserGroup.url.equals(reqUrl)){
//			getUser.getUserGroup(ctx,req);
//		}else if(Constants.HttpUrlType.GetUser.url.equals(reqUrl)){
//			getUser.getUser(ctx,req);
//		}else if(Constants.HttpUrlType.CreateGroup.url.equals(reqUrl)){
//			groupAction.createGroup(ctx, req);
//		}else if(Constants.HttpUrlType.AddUserToGroup.url.equals(reqUrl)){
//			groupAction.addUserToGroup(ctx, req);
//		}else if(Constants.HttpUrlType.RemoveUserFromGroup.url.equals(reqUrl)){
//			groupAction.removeUserFromGroup(ctx, req);
//		}else if(Constants.HttpUrlType.SendToUser.url.equals(reqUrl)){
//			sendMessage.sendToUser(ctx,req);
//		}else if(Constants.HttpUrlType.SendToGroup.url.equals(reqUrl)){
//			sendMessage.sendToGroup(ctx,req);
//		}else{
//			httpResponseHandler.response404(ctx);
//		}
//	}
//
//	/**
//	 * 处理握手请求
//	 * @param ctx
//	 * @param req
//	 */
//	private void shakeHandsHandler(ChannelHandlerContext ctx, FullHttpRequest req){
//		//解析握手请求
//		String channelId = "";
//		Map<String,String> requestParam = NettyUtil.getRequestParams(req);
//		if(requestParam.containsKey(Constants.CHANNELID))
//		{
//			channelId = requestParam.get(Constants.CHANNELID);
//		}else{
//			httpResponseHandler.responseJson(ctx, new Message(CommonConsts.SUCCESS, ErrorConstants.ErrorChannelId));
//			log.error("握手失败:缺少channelId");
//			return ;
//		}
//		WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(wsPath+req.getUri(),
//				null, false);
//		handshaker = wsFactory.newHandshaker(req);
//		if (handshaker == null) {
//			WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
//		} else {
//			handshaker.handshake(ctx.channel(), req);
//			websocketCacheService.put(new WebsocketEntity(channelId, ctx.channel()));
//			//以websocket的形式将标识返回
//			ctx.channel().writeAndFlush(new TextWebSocketFrame(channelId));
//		}
//	}
//
//	/**
//	 * 判断是否是握手请求
//	 * @param req
//	 * @return
//	 */
//	private boolean isShakeHands(FullHttpRequest req) {
//		return Constants.WEBSOCKET.equals(req.headers().get(Constants.UPGRADE));
//	}
//
//	private String subReqUrl(String url){
//		if(url.contains("?")){
//			return url.substring(1, url.indexOf("?"));
//		}else{
//			return url.substring(1, url.length());
//		}
//	}
//}
