package com.huangliang.cloudpushportal.templet;

import com.huangliang.api.config.RedisComConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class Redis {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        return RedisComConfig.getTemplate(factory);
    }
}
