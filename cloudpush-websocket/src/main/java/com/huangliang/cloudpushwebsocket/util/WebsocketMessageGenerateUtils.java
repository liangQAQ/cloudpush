package com.huangliang.cloudpushwebsocket.util;

import com.alibaba.fastjson.JSONObject;
import com.huangliang.api.constants.Constants;
import com.huangliang.api.entity.WebsocketMessage;
import com.huangliang.cloudpushwebsocket.constants.AttrConstants;
import com.huangliang.cloudpushwebsocket.constants.MessageConstants;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * 消息对象构建类
 */
public class WebsocketMessageGenerateUtils {

    public static WebsocketMessage generateWebsocketMessage(String channelId, JSONObject msgJson){
        return generateWebsocketMessage(channelId,null,null,msgJson);
    }

    public static WebsocketMessage generateWebsocketMessage(String channelId, String requestId, JSONObject msgJson){
        return generateWebsocketMessage(channelId,requestId,null,msgJson);
    }

    //根据mq中的内容构建发送消息
//    public static WebsocketMessage generateWebsocketMessage(MessageExt msg){
//        return generateWebsocketMessage(msg.getTags(),null,null,new String(msg.getBody()));
//    }

    //根据请求中的参数构建发送消息
    public static WebsocketMessage generateWebsocketMessage(String channelId,String requestId,String msgId,JSONObject msgJson){
        WebsocketMessage websocketMsg = new WebsocketMessage(
                msgId,
                WebsocketMessage.Type.BUSSINESS.code,
                channelId,
                msgJson,
                Constants.SYSTEM,
                WebsocketMessage.Trigger.HTTP.code
        );
        return websocketMsg;
    }

    //根据请求中的参数构建发送消息
    public static WebsocketMessage generateErrorWebsocketMessage(Channel channel,String str){
        WebsocketMessage websocketMsg = new WebsocketMessage();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constants.SYSTEM, String.format(MessageConstants.ParseErrorSuccess,str));
        websocketMsg.setMsg(jsonObject);
        websocketMsg.setSessionId(channel.attr(AttrConstants.sessionId).get());
        websocketMsg.setFrom(Constants.SYSTEM);
        websocketMsg.setTrigger(WebsocketMessage.Trigger.WEBSOCKET.code);
        return websocketMsg;
    }

    public static WebsocketMessage generateShakeHands(Channel channel){
        WebsocketMessage websocketMsg = new WebsocketMessage();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constants.SYSTEM, String.format(MessageConstants.ShakeSuccess,channel.attr(AttrConstants.channelId).get()));
        websocketMsg.setMsg(jsonObject);
        websocketMsg.setSessionId(channel.attr(AttrConstants.sessionId).get());
        websocketMsg.setFrom(Constants.SYSTEM);
        websocketMsg.setTrigger(WebsocketMessage.Trigger.WEBSOCKET.code);
        return websocketMsg;
    }

    public static TextWebSocketFrame generateResponse(WebsocketMessage ws){
        return new TextWebSocketFrame(JSONObject.toJSONString(ws));
    }
}
