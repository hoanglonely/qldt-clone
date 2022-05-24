package com.mb.lab.banks.apigateway.config;

import java.io.IOException;

import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.starter.RedissonProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mb.lab.banks.apigateway.utils.CustomRedisson;
import com.mb.lab.banks.apigateway.utils.SpringRedissonConfigHelper;

@Configuration
public class RedissonConfig {

    @Autowired
    private RedisProperties redisProperties;

    @Autowired
    private RedissonProperties redissonProperties;

    @Autowired
    private ApplicationContext ctx;

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() throws IOException {
        return createRedissonClient();
    }
    
    private RedissonClient createRedissonClient() {
        Config config = null;

        if (redissonProperties.getConfig() != null) {
            try {
                config = SpringRedissonConfigHelper.loadConfig(ctx, redissonProperties.getConfig());
            } catch (IOException e) {
                throw new IllegalArgumentException("Can't parse config", e);
            }
        }
        
        if (config == null) {
            config = new Config();
        }

        SpringRedissonConfigHelper.applySpringRedisConfig(config, redisProperties);

        return CustomRedisson.create(config);
    }
}
