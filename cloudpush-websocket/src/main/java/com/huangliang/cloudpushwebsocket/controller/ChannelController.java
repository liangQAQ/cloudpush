package com.huangliang.cloudpushwebsocket.controller;

import com.huangliang.cloudpushwebsocket.service.ChannelService;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

@RestController
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    @RequestMapping(value="channels")
    public Map<String, Channel> getChannels(){
        return channelService.getAll();
    }
}
