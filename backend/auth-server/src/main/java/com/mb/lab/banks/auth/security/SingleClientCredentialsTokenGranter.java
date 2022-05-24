package com.mb.lab.banks.auth.security;

import java.util.Collection;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

public class SingleClientCredentialsTokenGranter extends ClientCredentialsTokenGranter {

    private static final String GRANT_TYPE = "single_client_credentials";

    private TokenStore tokenStore;

    public SingleClientCredentialsTokenGranter(AuthorizationServerTokenServices tokenServices,
            ClientDetailsService clientDetailsService,
            OAuth2RequestFactory requestFactory,
            TokenStore tokenStore) {
        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        setAllowRefresh(false);
        this.tokenStore = tokenStore;
    }

    @Override
    protected OAuth2AccessToken getAccessToken(ClientDetails client, TokenRequest tokenRequest) {
        if (client.getAdditionalInformation() != null
                && Boolean.TRUE.equals(client.getAdditionalInformation().get(CompositeClientDetailsService.KEY_INFO_SINGLE_TOKEN))) {
            Collection<OAuth2AccessToken> accessTokens = tokenStore.findTokensByClientId(client.getClientId());
            if (accessTokens != null && accessTokens.size() > 0) {
                throw new InvalidClientException("Given client ID does not allow multiple token");
            }
        }
        return super.getAccessToken(client, tokenRequest);
    }

}
