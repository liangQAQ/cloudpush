package com.huangliang.cloudpushwebsocket.task;

import com.huangliang.api.constants.RedisPrefix;
import com.huangliang.api.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 定时批量更新redis中的客户端活跃时间(减少实时单个修改带来的消耗)
 */
@Component
@Slf4j
public class UpdateRedisChannelActiveTimeTask {

    //待更新至redis中的客户端标识与活跃时间
    private Map<String,Date> channelActiveTime = new ConcurrentHashMap<String,Date>();

    @Autowired
    private RedisTemplate redisTemplate;

    public void addChannel(String channelId){
        //重复就覆盖  保存最新的
        channelActiveTime.put(channelId,new Date());
    }

    //批量更新redis中的客户端活跃时间
    public void refresh(){
        int count = 0;
        Iterator<Map.Entry<String,Date>> it = channelActiveTime.entrySet().iterator();
        if(!it.hasNext()){
            return ;
        }
        executePipelined(it);
    }

    private void executePipelined(Iterator<Map.Entry<String,Date>> it){
        redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.openPipeline();
                int count = 0;
                //1000条客户端活跃时间提交一次redis
                while(it.hasNext()||count>=1000){
                    Map.Entry<String,Date> entry = it.next();
                    //redisTemplate.opsForHash().put(RedisPrefix.PREFIX_CLIENT+channel.attr(Constants.attrChannelId).get(),"lastActiveTime" ,DateUtils.dateToDateTime(now));
                    redisConnection.hSet((RedisPrefix.PREFIX_CLIENT).getBytes(),"lastActiveTime".getBytes(),DateUtils.dateToDateTime(entry.getValue()).getBytes() );
                    it.remove();
                    count++;
                }
                return null;
            }
        });
        if(it.hasNext()){
            executePipelined(it);
        }
    }
}
