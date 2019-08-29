package com.huangliang.cloudpushwebsocket.service.websocket.handlerText;

import com.huangliang.api.entity.WebsocketMessage;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

/**
 * 接收处理websocket消息的业务逻辑类
 */
@Service
public class SendToServerService implements IMessageService {
    @Override
    public void handler(Channel channel, WebsocketMessage websocketMessage) {

    }
}
