package com.huangliang.cloudpushwebsocket.service.websocket.handlerText;

import com.huangliang.api.constants.CommonConsts;
import com.huangliang.api.entity.WebsocketMessage;
import com.huangliang.api.util.RedisUtils;
import com.huangliang.cloudpushwebsocket.config.ComConfig;
import com.huangliang.cloudpushwebsocket.constants.AttrConstants;
import com.huangliang.cloudpushwebsocket.service.message.MessageSendService;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 接收处理websocket消息的业务逻辑类
 */
@Service
@Slf4j
public class BussinessService implements IMessageService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MessageSendService messageSendService;
    @Autowired
    private ComConfig comConfig;

    @Override
    public void handler(Channel channel, WebsocketMessage websocketMessage) {
        if(!checkWsMessage(channel,websocketMessage)){
            return ;
        }
        String arr[] = websocketMessage.getTo().split(CommonConsts.COMMA_FLAG);
        List<String> toClients = Arrays.asList(arr);
        Map<String,List<String>> hostClientsMap = new HashMap<>();
        List<Object> pipeResult = redisTemplate.executePipelined(RedisUtils.getClientHostByClientFromRedis(toClients));
        for (int i=0;i<toClients.size();i++) {
            //遍历list 依次推送消息
            //根据channelId找到对应的客户端对象所对应websocket服务的实例名
            String channelId = toClients.get(i);
            Object hostObj = pipeResult.get(i);
            if (hostObj == null) {
                log.info("不存在的客户端[{}]", channelId);
                continue;
            }
            String host = hostObj.toString();
            //如果所在的host正好是节点上，则直接发起推送
            if(comConfig.getInstanceId().equals(host)){
                messageSendService.sendMessage(channelId,websocketMessage);
                continue;
            }
            //如果不是，则请求其客户端所在的节点发起推送
            if (hostClientsMap.containsKey(host)) {
                hostClientsMap.get(host).add(channelId);
            } else {
                List<String> clients = new LinkedList<>();
                clients.add(channelId);
                hostClientsMap.put(host, clients);
            }
        }
    }

    private boolean checkWsMessage(Channel channel,WebsocketMessage websocketMessage) {
        String sendTo = websocketMessage.getTo();
        if(StringUtils.isEmpty(sendTo)){
            log.info("目标对象为空"+websocketMessage);
            return false;
        }
        websocketMessage.setTrigger(WebsocketMessage.Trigger.WEBSOCKET.code);
        websocketMessage.setType(WebsocketMessage.Type.BUSSINESS.code);
        websocketMessage.setFrom(channel.attr(AttrConstants.channelId).get());
        return true;
    }
}
