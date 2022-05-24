package com.mb.lab.banks.apigateway.service;

import java.util.concurrent.atomic.AtomicBoolean;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.LongCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ApiStopCacheSubService implements InitializingBean {

    private static final String KEY_API_STOP = "setting_api_stop";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedissonClient redissonClient;

    private RBucket<Long> bucket;
    private AtomicBoolean apiStop = new AtomicBoolean(false);

    @Override
    public void afterPropertiesSet() throws Exception {
        bucket = redissonClient.getBucket(KEY_API_STOP, LongCodec.INSTANCE);
    }

    @Scheduled(fixedRate = 1000 * 60 * 5)
    public void checkApiStopSetting() {
        try {
            Long config = bucket.get();
            apiStop.set(config != null && config.longValue() > 0l);
        } catch (Exception e) {
            logger.error("Fail to fetch setting_api_stop from Redis", e);
            apiStop.set(false);
        }
    }

    public boolean isApiStop() {
        return apiStop.get();
    }
}
