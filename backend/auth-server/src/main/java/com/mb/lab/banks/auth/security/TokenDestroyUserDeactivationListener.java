package com.mb.lab.banks.auth.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;

import com.mb.lab.banks.utils.event.UserDeactivationEvent;

/**
 * Listen for {@link UserDeactivationEvent} and destroy all sessions associated
 * with this user
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

	// TODO use this method
	public void removeToken(String userId) {
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
