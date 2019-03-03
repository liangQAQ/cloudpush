package com.huangliang.cloudpushwebsocket.entity;

import io.netty.channel.Channel;

import java.io.Serializable;

public class Websocket implements Serializable {

    private String channelId;

    private Channel channel;

    public Websocket(String channelId, Channel channel) {
        this.channelId = channelId;
        this.channel = channel;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
}
