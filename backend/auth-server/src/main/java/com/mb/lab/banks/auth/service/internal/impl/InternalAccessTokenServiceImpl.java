package com.mb.lab.banks.auth.service.internal.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.mb.lab.banks.auth.dto.internal.TokenDto;
import com.mb.lab.banks.auth.security.RevocableJwtTokenStore;
import com.mb.lab.banks.auth.service.internal.InternalAccessTokenService;

@Service
public class InternalAccessTokenServiceImpl implements InternalAccessTokenService {

    @Autowired
    private RevocableJwtTokenStore tokenStore;

    @Autowired
    private OAuth2RequestFactory requestFactory;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private TokenGranter tokenGranter;

    @Override
    public TokenDto createMerchantAccessToken(Long _clientId) {
        String clientId = String.valueOf(_clientId);
        return createAppAccessToken(clientId);
    }

    @Override
    public TokenDto restoreMerchantAccessToken(Long _clientId, String accessToken) {
        String clientId = String.valueOf(_clientId);
        return restoreAppAccessToken(clientId, accessToken);
    }

    @Override
    public void deleteMerchantAccessToken(Long _clientId) {
        String clientId = String.valueOf(_clientId);
        deleteAppAccessToken(clientId);
    }

    @Override
    public TokenDto createAppAccessToken(String clientId) {
        deleteAppAccessToken(clientId);

        ClientDetails client = clientDetailsService.loadClientByClientId(clientId);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("client_id", client.getClientId());
        parameters.put("client_secret", client.getClientSecret());
        parameters.put("grant_type", "single_client_credentials");

        TokenRequest tokenRequest = requestFactory.createTokenRequest(parameters, client);
        OAuth2AccessToken token = tokenGranter.grant("single_client_credentials", tokenRequest);

        return new TokenDto(token.getValue());
    }

    @Override
    public TokenDto restoreAppAccessToken(String clientId, String accessToken) {
        OAuth2AccessToken token = tokenStore.restoreToken(accessToken);
        return new TokenDto(token.getValue());
    }

    @Override
    public void deleteAppAccessToken(String clientId) {
        Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientId(clientId);
        if (!CollectionUtils.isEmpty(tokens)) {
            for (OAuth2AccessToken token : tokens) {
                tokenStore.removeAccessToken(token);
            }
        }
    }

    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        OAuth2Request storedOAuth2Request = requestFactory.createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, null);
    }

}
