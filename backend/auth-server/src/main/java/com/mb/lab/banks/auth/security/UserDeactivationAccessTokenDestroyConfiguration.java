package com.mb.lab.banks.auth.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;

import com.mb.lab.banks.auth.config.OAuth2ClientDetailsProperties;
import com.mb.lab.banks.auth.config.OAuth2ClientDetailsProperties.ClientDetails;
import com.mb.lab.banks.utils.event.UserDeactivationEvent;

/**
 * Listen for {@link UserDeactivationEvent} to remove related access tokens
 * 
 * @author thanh
 */
@Configuration
public class UserDeactivationAccessTokenDestroyConfiguration {

    @Autowired
    private TokenStore tokenStore;
    
    @Autowired
    private OAuth2ClientDetailsProperties clientDetailsProperties;

    @Bean
    public TokenDestroyUserDeactivationListener tokenDestroyUserDeactivationListener() {
        List<String> clientIds = new ArrayList<>(clientDetailsProperties.getClientDetails().size());
        for (ClientDetails clientDetails : clientDetailsProperties.getClientDetails().values()) {
            clientIds.add(clientDetails.getClientId());
        }
        
        return new TokenDestroyUserDeactivationListener(tokenStore, clientIds);
    }

}
