package com.huangliang.cloudpushwebsocket.service.websocket.handlerText;

import com.huangliang.api.entity.WebsocketMessage;
import io.netty.channel.Channel;
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
        map.put(WebsocketMessage.Type.BUSSINESS.code,bussinessService);
        //处理心跳的消息
        map.put(WebsocketMessage.Type.HEARTBEAT.code,heartBeatService);
    }

    public void handler(Channel channel, WebsocketMessage websocketMessage){
        IMessageService service = map.get(websocketMessage.getType());
        if(service == null){
            log.info("无法解析的消息类型:"+websocketMessage.toString());
            return;
        }
        service.handler(channel,websocketMessage);
    }
}
