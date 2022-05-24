package com.mb.lab.banks.auth.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * OAuth 2.0 configuration properties.
 * 
 * @author thanh
 */
@ConfigurationProperties("security.oauth2.authorization")
public class OAuth2ClientDetailsProperties {
    
    private Map<String, ClientDetails> clientDetails = new HashMap<>();

    public Map<String, ClientDetails> getClientDetails() {
        return clientDetails;
    }

    public void setClientDetails(Map<String, ClientDetails> clientDetails) {
        this.clientDetails = clientDetails;
    }

    public static class ClientDetails {
        /**
         * OAuth 2 client_id
         */
        private String clientId;

        /**
         * OAuth 2 client_secret
         */
        private String clientSecret;

        /**
         * List of allowed grant type: implicit,refresh_token,password,authorization_code,client_credentials,signin_key
         */
        private List<String> grantType;

        /**
         * Declare any scope you want, you can restrict access base on scope
         */
        private List<String> scope;

        /**
         * Refresh token life time in seconds
         */
        private int refreshTokenValidity;

        /**
         * Access token life time in seconds
         */
        private int accessTokenValidity;

        /**
         * Only allow following redirect urls
         */
        private List<String> redirectUri;

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getClientSecret() {
            return clientSecret;
        }

        public void setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
        }

        public List<String> getGrantType() {
            return grantType;
        }

        public void setGrantType(List<String> grantType) {
            this.grantType = grantType;
        }

        public List<String> getScope() {
            return scope;
        }

        public void setScope(List<String> scope) {
            this.scope = scope;
        }

        public int getRefreshTokenValidity() {
            return refreshTokenValidity;
        }

        public void setRefreshTokenValidity(int refreshTokenValidity) {
            this.refreshTokenValidity = refreshTokenValidity;
        }

        public int getAccessTokenValidity() {
            return accessTokenValidity;
        }

        public void setAccessTokenValidity(int accessTokenValidity) {
            this.accessTokenValidity = accessTokenValidity;
        }

        public List<String> getRedirectUri() {
            return redirectUri;
        }

        public void setRedirectUri(List<String> redirectUri) {
            this.redirectUri = redirectUri;
        }
    }

}
