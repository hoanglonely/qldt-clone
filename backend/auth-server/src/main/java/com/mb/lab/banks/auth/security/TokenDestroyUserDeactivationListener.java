package com.mb.lab.banks.auth.security;

import java.util.Collection;
import java.util.List;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;

import com.mb.lab.banks.utils.event.UserDeactivationEvent;
import com.mb.lab.banks.utils.event.UserPasswordChangedEvent;
import com.mb.lab.banks.utils.event.stream.UserDeactivationStreams;
import com.mb.lab.banks.utils.event.stream.UserPasswordChangedStreams;

/**
 * Listen for {@link UserDeactivationEvent} and destroy all sessions associated with this user
 * 
 * @author thanh
 */
public class TokenDestroyUserDeactivationListener {

    private List<String> clientIds;
    private TokenStore tokenStore;

    public TokenDestroyUserDeactivationListener(TokenStore tokenStore, List<String> clientIds) {
        this.tokenStore = tokenStore;
        this.clientIds = clientIds;
    }
    
    @StreamListener(UserDeactivationStreams.InBound.NAME)
    public void onEvent(@Payload UserDeactivationEvent event) {
        removeToken(String.valueOf(event.getUserId()));
    }
    
    @StreamListener(UserPasswordChangedStreams.InBound.NAME)
    public void onEvent(@Payload UserPasswordChangedEvent event) {
        removeToken(String.valueOf(event.getUserId()));
    }
    
    private void removeToken(String userId) {
        for (String clientId : clientIds) {
            Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientIdAndUserName(clientId, userId);
            if (tokens == null || tokens.isEmpty()) {
                continue;
            }
            for (OAuth2AccessToken accessToken : tokens) {
                if (accessToken.getRefreshToken() != null) {
                    tokenStore.removeRefreshToken(accessToken.getRefreshToken());
                }
                tokenStore.removeAccessToken(accessToken);
            }
        }
    }

}
