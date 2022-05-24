package com.mb.lab.banks.auth.security.recaptcha;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.Assert;

/**
 * An implementation of {@link LoginAttemptRepository} backed by Redis.
 * 
 * @author thanh
 */
public class RedisLoginAttemptRepository implements LoginAttemptRepository {

    private static final long EXPIRE_TIME_SECONDS = 1l * 24 * 60 * 60;

    /**
     * The prefix for each key of the Redis Value representing a user's login attempts count. The suffix is the unique user id.
     */
    static final String BOUNDED_VALUE_KEY_PREFIX = "{spring:session:login_attempts}:";

    private final ValueOperations<String, Integer> redisOperations;
    private final RedisSerializer<String> keySerializer;

    public RedisLoginAttemptRepository(RedisConnectionFactory redisConnectionFactory) {
        this(createDefaultTemplate(redisConnectionFactory));
    }

    @SuppressWarnings("unchecked")
    public RedisLoginAttemptRepository(RedisOperations<String, Integer> redisOperations) {
        Assert.notNull(redisOperations, "redisOperations cannot be null");
        this.redisOperations = redisOperations.opsForValue();
        this.keySerializer = (RedisSerializer<String>) redisOperations.getKeySerializer();
    }

    @Override
    public int countLoginAttempts(String username) {
        Integer loginCount = redisOperations.get(getKey(username));
        return (loginCount != null) ? loginCount : 0;
    }

    @Override
    public void increaseLoginAttempt(final String username) {
        final byte[] key = keySerializer.serialize(getKey(username));
        redisOperations.getOperations().executePipelined(new RedisCallback<Void>() {

            @Override
            public Void doInRedis(RedisConnection connection) throws DataAccessException {
                connection.incrBy(key, 1);
                connection.expire(key, EXPIRE_TIME_SECONDS);
                return null;
            }

        });
    }

    @Override
    public void clearLoginAttempts(String username) {
        redisOperations.getOperations().delete(getKey(username));
    }

    /**
     * Gets the key for this user by prefixing it appropriately.
     */
    static String getKey(String username) {
        return BOUNDED_VALUE_KEY_PREFIX + username;
    }

    private static RedisTemplate<String, Integer> createDefaultTemplate(RedisConnectionFactory connectionFactory) {
        Assert.notNull(connectionFactory, "connectionFactory cannot be null");
        RedisTemplate<String, Integer> template = new RedisTemplate<String, Integer>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericToStringSerializer<Integer>(Integer.class));
        template.afterPropertiesSet();
        return template;
    }

}
