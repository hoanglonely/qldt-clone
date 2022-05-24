package com.mb.lab.banks.auth.security;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;

public class RedisAuthorizationCodeServices extends RandomValueAuthorizationCodeServices {

    private final RedisConnectionFactory connectionFactory;
    private RedisTokenStoreSerializationStrategy serializationStrategy = new JdkSerializationStrategy();
    private String prefix = "{spring:security:oauth-code}:";
    /**
     * Expires in seconds
     */
    private int expiresIn = 5 * 60;

    public RedisAuthorizationCodeServices(RedisConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void setSerializationStrategy(RedisTokenStoreSerializationStrategy serializationStrategy) {
        this.serializationStrategy = serializationStrategy;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    @Override
    protected void store(final String code, final OAuth2Authentication authentication) {
        byte[] key = serializeKey(code);
        byte[] value = serialize(authentication);

        RedisConnection conn = getConnection();
        try {
            conn.openPipeline();
            conn.set(key, value);
            if (this.expiresIn > 0) {
                conn.expire(key, this.expiresIn);
            }
            conn.closePipeline();
        } finally {
            conn.close();
        }
    }

    @Override
    protected OAuth2Authentication remove(final String code) {
        byte[] key = serializeKey(code);
        RedisConnection conn = getConnection();
        try {
//            conn.openPipeline();
//            conn.get(key);
//            conn.del(key);
//            List<Object> results = conn.closePipeline();
//            byte[] auth = (byte[]) results.get(0);
            // Not execute in pipeline mode because Redisson not execute them in right order (don't known why)
            byte[] auth = conn.get(key);
            conn.del(key);
            return deserializeAuthentication(auth);
        } finally {
            conn.close();
        }
    }

    private RedisConnection getConnection() {
        return connectionFactory.getConnection();
    }

    private byte[] serialize(Object object) {
        return serializationStrategy.serialize(object);
    }

    private byte[] serializeKey(String object) {
        return serialize(prefix + object);
    }

    private OAuth2Authentication deserializeAuthentication(byte[] bytes) {
        return serializationStrategy.deserialize(bytes, OAuth2Authentication.class);
    }

    private byte[] serialize(String string) {
        return serializationStrategy.serialize(string);
    }

}
