package com.huangliang.cloudpushwebsocket.task;

import com.huangliang.cloudpushwebsocket.constants.Constants;
import com.huangliang.cloudpushwebsocket.service.ChannelService;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * 扫描不在线的用户
 */
@Component
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@Slf4j
public class ScanClientsNotOnline {

    @Autowired
    private ChannelService channelService;

    private static Long now = null;

    @Scheduled(fixedRate=60000,fixedDelay = 60000)
    private void execute() {
        log.info("过期客户端扫描任务开启...");
        now = System.currentTimeMillis();
        Map<String,Channel> channels = channelService.getAll();
        if(!CollectionUtils.isEmpty(channels)){
            log.info("没有客户端连接.");
            return ;
        }
        Channel channel = null;
//        for(String key : channels.keySet()){
//            channel = channels.get(key);
//            if(!channel.isOpen()&&outOfTime(channel)){
//                channelService.remove(channel);
//            }
//        }
        log.info("过期客户端扫描任务执行完毕");
    }

    private boolean outOfTime(Channel channel) {
        String activeTime = channel.attr(Constants.attrActiveTime).get();
        return false;
    }
}
