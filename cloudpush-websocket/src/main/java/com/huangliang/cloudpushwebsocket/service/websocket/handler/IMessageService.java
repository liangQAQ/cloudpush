package com.huangliang.cloudpushwebsocket.service.websocket.handler;

import com.huangliang.api.entity.WebsocketMessage;
import io.netty.channel.Channel;

public interface IMessageService {
    public void handler(Channel channel, WebsocketMessage websocketMessage);
}
