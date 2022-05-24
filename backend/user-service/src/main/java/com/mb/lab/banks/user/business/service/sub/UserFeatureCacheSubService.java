package com.mb.lab.banks.user.business.service.sub;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.LocalCachedMapOptions.CacheProvider;
import org.redisson.api.LocalCachedMapOptions.ReconnectionStrategy;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RedissonClient;
import org.redisson.api.map.MapLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mb.lab.banks.user.business.service.internal.InternalUserService;

@Service
public class UserFeatureCacheSubService implements InitializingBean {

    private static final String KEY_FEATURES = "user:features";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private InternalUserService internalUserService;

    private RLocalCachedMap<Long, Set<String>> featureMap;

    @Override
    public void afterPropertiesSet() throws Exception {
        MapLoader<Long, Set<String>> mapLoader = new MapLoader<Long, Set<String>>() {

            @Override
            public Set<String> load(Long key) {
                return internalUserService.getFeatures(key);
            }

            @Override
            public Iterable<Long> loadAllKeys() {
                logger.error("loadAll method should not be called on this cache");
                return Collections.emptyList();
            }
        };

        // @formatter:off
        LocalCachedMapOptions<Long, Set<String>> mapOptions = LocalCachedMapOptions
                .<Long, Set<String>> defaults()
                .cacheProvider(CacheProvider.CAFFEINE)
                .reconnectionStrategy(ReconnectionStrategy.CLEAR)
                .timeToLive(2, TimeUnit.MINUTES)
                .loader(mapLoader);
        // @formatter:on

        featureMap = redissonClient.getLocalCachedMap(KEY_FEATURES, mapOptions);
    }

    public Set<String> getFeatures(Long userId) {
        return featureMap.get(userId);
    }

    public void invalidate(Long userId) {
        featureMap.remove(userId);
    }

}
