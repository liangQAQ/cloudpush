package com.huangliang.cloudpushportal.controller;

import com.huangliang.api.constants.CommonConsts;
import com.huangliang.api.constants.RedisPrefix;
import com.huangliang.api.entity.response.Data;
import com.huangliang.cloudpushportal.entity.res.WebsocketServer;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Map;

@RestController
public class ServerController {

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 获取websocket的服务地址(ip+port)
     * @param channelId
     * @return
     */
    @RequestMapping(value = "/server/get",method = RequestMethod.GET)
    public Data getServer(@NotNull String channelId){
        String websocketServer = null;
        //查询客户端之前所连接的实例
        Map<String,String> client = redisTemplate.opsForHash().entries(RedisPrefix.PREFIX_CLIENT+channelId);
        if(client!=null){
            //如果是已连接过的用户，优先分配之前的websocket服务地址
            websocketServer = client.get("server");
        }
        //查询所有可连接的实例
        Map<String,String> servers = (Map<String, String>) redisTemplate.opsForHash().entries(RedisPrefix.WEBSOCKETSERVER);

        if(CollectionUtils.isEmpty(servers)){
            return new Data<WebsocketServer>(CommonConsts.ERROR,"没有可用的服务，请稍后再试");
        }

        if(!servers.containsKey(websocketServer)){
            //重新分配较少连接数的websocket服务
            Long min = Long.MAX_VALUE;
            for(String key : servers.keySet()){
                Long count = redisTemplate.opsForSet().size(RedisPrefix.PREFIX_SERVERCLIENTS+key);
                if(count < min){
                    min = count;
                    websocketServer = key;
                }
            }
        }
        //解析得到真正的websocket地址
        websocketServer = getWebsocketServer(websocketServer,servers);
        return new Data<WebsocketServer>(CommonConsts.SUCCESS,CommonConsts.REQUST_SUC,new WebsocketServer(websocketServer));
    }

    /**
     * 解析websocket的连接地址，map中存的是<tomcatIp:port,websocketPort>
     * @param websocketServer
     * @param servers
     * @return
     */
    private String getWebsocketServer(String websocketServer, Map<String,String> servers) {
        return websocketServer.split(":")[0] +":"+ servers.get(websocketServer);
    }
}
