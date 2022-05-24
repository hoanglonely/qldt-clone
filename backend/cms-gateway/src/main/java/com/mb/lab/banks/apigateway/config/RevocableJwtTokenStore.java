package com.mb.lab.banks.apigateway.config;

import java.util.Collection;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

public class RevocableJwtTokenStore extends JwtTokenStore {

    private static final String OAUTH_TOKEN_PREFIX = "{spring:security:oauth}:";

    private RedisTokenStore redisTokenStore;

    public RevocableJwtTokenStore(JwtAccessTokenConverter jwtTokenEnhancer, RedisConnectionFactory redisConnectionFactory) {
        super(jwtTokenEnhancer);
        redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        redisTokenStore.setPrefix(OAUTH_TOKEN_PREFIX);
    }
    
    @Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {
        OAuth2AccessToken accessToken = redisTokenStore.readAccessToken(tokenValue);
        if (accessToken == null) {
            return null;
        }
        return super.readAccessToken(tokenValue);
    }
    
    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
        OAuth2Authentication authentication = redisTokenStore.readAuthenticationForRefreshToken(token);
        if (authentication == null) {
            return null;
        }
        return super.readAuthentication(token.getValue());
    }
    
    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        OAuth2Authentication authentication = redisTokenStore.readAuthentication(token);
        if (authentication == null) {
            return null;
        }
        return super.readAuthentication(token.getValue());
    }
    
    @Override
    public OAuth2Authentication readAuthentication(String token) {
        OAuth2Authentication authentication = redisTokenStore.readAuthentication(token);
        if (authentication == null) {
            return null;
        }
        return super.readAuthentication(token);
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String tokenValue) {
        OAuth2RefreshToken refreshToken = redisTokenStore.readRefreshToken(tokenValue);
        if (refreshToken == null) {
            return null;
        }
        return super.readRefreshToken(tokenValue);
    }
    
    @Override
    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        redisTokenStore.storeAccessToken(token, authentication);
    }

    @Override
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        redisTokenStore.storeRefreshToken(refreshToken, authentication);
    }
    
    @Override
    public void removeAccessToken(OAuth2AccessToken token) {
        super.removeAccessToken(token);
        redisTokenStore.removeAccessToken(token);
    }
    
    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
        super.removeAccessTokenUsingRefreshToken(refreshToken);
        redisTokenStore.removeAccessTokenUsingRefreshToken(refreshToken);
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken token) {
        super.removeRefreshToken(token);
        redisTokenStore.removeRefreshToken(token);;
    }
    
    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        return redisTokenStore.findTokensByClientId(clientId);
    }
    
    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
        return redisTokenStore.findTokensByClientIdAndUserName(clientId, userName);
    }

}
