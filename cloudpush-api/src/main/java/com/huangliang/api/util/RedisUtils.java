package com.huangliang.api.util;

import com.huangliang.api.constants.RedisPrefix;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;

import java.util.List;

public class RedisUtils {

    public static RedisCallback<?> getClientHostByClientFromRedis(List<String> requestClients){
        return new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.openPipeline();
                for (String channelId : requestClients) {
                    redisConnection.hGet((RedisPrefix.PREFIX_CLIENT + channelId).getBytes(),"host".getBytes());
                }
                return null;
            }
        };
    }
}
