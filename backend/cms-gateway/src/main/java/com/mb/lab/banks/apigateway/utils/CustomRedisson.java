package com.mb.lab.banks.apigateway.utils;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class CustomRedisson extends Redisson {

    public static RedissonClient create(Config config) {
        CustomRedisson redisson = new CustomRedisson(config);
        if (config.isReferenceEnabled()) {
            redisson.enableRedissonReferenceSupport();
        }
        return redisson;
    }

    protected CustomRedisson(Config config) {
        super(config);
    }

}
