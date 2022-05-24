package com.mb.lab.banks.auth.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import com.mb.lab.banks.utils.security.UserAuthenticationToken;
import com.mb.lab.banks.utils.security.UserLogin;

public class CustomJwtAccessTokenConverter extends JwtAccessTokenConverter {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String, Object> additionalInfo = cloneMap(((DefaultOAuth2AccessToken) accessToken).getAdditionalInformation());

        UserAuthenticationToken userAuthentication = (UserAuthenticationToken) authentication.getUserAuthentication();
        if (userAuthentication != null) {
            UserLogin userLogin = userAuthentication.getUserLogin();
            additionalInfo.put("username", userLogin.getUsername());
            additionalInfo.put("partnerId", userLogin.getPartnerId());
            additionalInfo.put("storeId", userLogin.getStoreId());
        }

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        accessToken = super.enhance(accessToken, authentication);
        
        Map<String, Object> updatedAdditionalInfo = cloneMap(((DefaultOAuth2AccessToken) accessToken).getAdditionalInformation());
        updatedAdditionalInfo.remove("username");
        updatedAdditionalInfo.remove("partnerId");
        updatedAdditionalInfo.remove("storeId");
        
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(updatedAdditionalInfo);
        return accessToken;
    }
    
    private Map<String, Object> cloneMap(Map<String, Object> source) {
        Map<String, Object> cloned = new HashMap<>();
        if (source != null) {
            cloned.putAll(source);
        }
        return cloned;
    }
}
