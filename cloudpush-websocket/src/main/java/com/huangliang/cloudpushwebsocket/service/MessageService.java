package com.huangliang.cloudpushwebsocket.service;

import com.huangliang.api.constants.Constants;
import com.huangliang.api.entity.WebsocketMessage;
import io.netty.channel.Channel;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MessageService {

    //根据mq中的内容构建发送消息
    public WebsocketMessage genarateWebsocketMessage(MessageExt msg){
        WebsocketMessage websocketMsg = new WebsocketMessage(
                msg.getMsgId(),
                WebsocketMessage.Type.SENDTOCLIENT.code,
                msg.getTags(),
                new String(msg.getBody()),
                Constants.SYSTEM,
                Integer.parseInt(msg.getUserProperty(Constants.Trigger))
        );
        return websocketMsg;
    }

    //根据请求中的参数构建发送消息
    public WebsocketMessage genarateWebsocketMessage(String msgJson){
        WebsocketMessage websocketMsg = new WebsocketMessage(
                "112233",
                WebsocketMessage.Type.SENDTOCLIENT.code,
                "all",
                new String(msgJson),
                Constants.SYSTEM,
                WebsocketMessage.Trigger.HTTP.code
        );
        return websocketMsg;
    }
}
