package com.huangliang.cloudpushwebsocket.service;

import com.huangliang.cloudpushwebsocket.util.JsonUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.springframework.stereotype.Service;

@Service
public class HttpResponseHandler {
	
	/**
	 * 以json形式返回
	 * @param ctx
	 * @param object
	 */
	public void responseJson(ChannelHandlerContext ctx, Object object) {
		String context = JsonUtil.objectToJson(object);
		response(ctx,context);
	}
	
	/**
     * 发送的返回值
     * @param ctx     返回
     * @param context 消息
     * @param status 状态
     */
    public void response(ChannelHandlerContext ctx, String context) {
        FullHttpResponse res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.copiedBuffer(context, CharsetUtil.UTF_8));
        res.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UTF-8");
        ctx.writeAndFlush(res).addListener(ChannelFutureListener.CLOSE);
    }
    
    public void response404(ChannelHandlerContext ctx) {
        FullHttpResponse res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
        ctx.writeAndFlush(res).addListener(ChannelFutureListener.CLOSE);
    }
    
    //返回错误信息
//    public void responseError(ChannelHandlerContext ctx,CommonException e){
//    	responseJson(ctx,new Message(CommonConsts.ERROR, e.getMessage()));
//    }
    
    //返回失败
    public void responseFailed(ChannelHandlerContext ctx){
    	FullHttpResponse res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR);
        ctx.writeAndFlush(res).addListener(ChannelFutureListener.CLOSE);
    }
}
