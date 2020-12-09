package com.huangliang.cloudpushwebsocket.service.websocket.handlerText;

import com.alibaba.fastjson.JSONObject;
import com.huangliang.api.entity.WebsocketMessage;
import com.huangliang.cloudpushwebsocket.constants.MessageConstants;
import com.huangliang.cloudpushwebsocket.util.WebsocketMessageGenerateUtils;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class MessageServiceStrategy {

    @Autowired
    private HeartBeatService heartBeatService;
    @Autowired
    private BussinessService bussinessService;

    //处理消息策略
    private static Map<Integer,IMessageService> map = new HashMap<>();

    @PostConstruct
    public void init(){
        //处理业务逻辑的消息
        map.put(WebsocketMessage.MsgType.BUSSINESS.code,bussinessService);
        //处理心跳的消息
        map.put(WebsocketMessage.MsgType.HEARTBEAT.code,heartBeatService);
    }

    public void handler(Channel channel, WebsocketMessage websocketMessage){
        IMessageService service = map.get(websocketMessage.getMsgType());
        if(service == null){
            log.info("无法解析的消息类型:"+websocketMessage.toString());
            channel.writeAndFlush(generate(channel,websocketMessage));
            return;
        }
        service.handler(channel,websocketMessage);
    }

    private TextWebSocketFrame generate(Channel channel, WebsocketMessage str){
        return WebsocketMessageGenerateUtils.generateResponse(
                WebsocketMessageGenerateUtils.generateErrorWebsocketMessage(
                        channel,
                        MessageConstants.ParseError,
                        JSONObject.toJSONString(str)));
    }
}
