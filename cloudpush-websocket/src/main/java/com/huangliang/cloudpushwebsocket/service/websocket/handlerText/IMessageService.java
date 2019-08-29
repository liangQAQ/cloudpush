package com.huangliang.cloudpushwebsocket.service.websocket.handlerText;

import com.huangliang.api.entity.WebsocketMessage;
import io.netty.channel.Channel;

/**
 * 各种消息类型对应的处理类
 */
public interface IMessageService {
    public void handler(Channel channel, WebsocketMessage websocketMessage);
}
