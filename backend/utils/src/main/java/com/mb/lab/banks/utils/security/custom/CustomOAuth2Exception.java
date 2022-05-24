package com.mb.lab.banks.utils.security.custom;

import java.util.Map;

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@SuppressWarnings("serial")
@JsonSerialize(using = CustomOAuth2ExceptionJackson2Serializer.class)
public class CustomOAuth2Exception extends OAuth2Exception {
    
    public static final String ERROR = "error";
    public static final String DESCRIPTION = "error_description";
    public static final String URI = "error_uri";
    public static final String INVALID_REQUEST = "invalid_request";
    public static final String INVALID_CLIENT = "invalid_client";
    public static final String INVALID_GRANT = "invalid_grant";
    public static final String UNAUTHORIZED_CLIENT = "unauthorized_client";
    public static final String UNSUPPORTED_GRANT_TYPE = "unsupported_grant_type";
    public static final String INVALID_SCOPE = "invalid_scope";
    public static final String INSUFFICIENT_SCOPE = "insufficient_scope";
    public static final String INVALID_TOKEN = "invalid_token";
    public static final String REDIRECT_URI_MISMATCH ="redirect_uri_mismatch";
    public static final String UNSUPPORTED_RESPONSE_TYPE ="unsupported_response_type";
    public static final String ACCESS_DENIED = "access_denied";

    private OAuth2Exception source;
    
    public CustomOAuth2Exception(OAuth2Exception source) {
        super(source.getMessage(), source.getCause());
        this.source = source;
    }
    
    public int getIntErrorCode() {
        String oauthErrorCode = getOAuth2ErrorCode();
        if (oauthErrorCode != null) {
            switch (oauthErrorCode) {
                case INVALID_REQUEST:
                    // The request is missing a parameter so the server can’t proceed with the request. This may also be returned if the request includes an
                    // unsupported parameter or repeats a parameter.
                    return 1001;
                case INVALID_CLIENT:
                    // Client authentication failed, such as if the request contains an invalid client ID or secret. Send an HTTP 401 response in this case.
                    return 1002;
                case UNAUTHORIZED_CLIENT:
                    // This client is not authorized to use the requested grant type. For example, if you restrict which applications can use the Implicit
                    // grant, you would return this error for the other apps.
                    return 1003;
                case UNSUPPORTED_GRANT_TYPE:
                    // If a grant type is requested that the authorization server doesn’t recognize, use this code. Note that unknown grant types also use this
                    // specific error code rather than using the invalid_request above.
                    return 1004;
                case INVALID_SCOPE:
                    return 1005;
                case REDIRECT_URI_MISMATCH:
                    return 1006;
                case UNSUPPORTED_RESPONSE_TYPE:
                    return 1007;
                case INSUFFICIENT_SCOPE:
                    return 1008;
                case INVALID_GRANT:
                    // The authorization code (or user’s password for the password grant type) is invalid or expired. This is also the error you would return if
                    // the redirect URL given in the authorization grant does not match the URL provided in this access token request.
                    if (source.getMessage().contains("Bad User Credentials.")) {
                        return 1009;
                    }
                    if (source.getMessage().contains("User was locked.")) {
                        return 1010;
                    }
                    if (source.getMessage().contains("Invalid refresh token")) {
                        return 1011;
                    }
                    return 1009;
                case INVALID_TOKEN:
                    return 1012;
                case ACCESS_DENIED:
                    return 1013;
            }
        }
        return 1000;
    }

    public String getOAuth2ErrorCode() {
        return source.getOAuth2ErrorCode();
    }

    public int getHttpErrorCode() {
        String oauthErrorCode = getOAuth2ErrorCode();
        if (oauthErrorCode != null) {
            if (INVALID_GRANT.equals(oauthErrorCode) && source.getMessage().contains("Invalid refresh token")) {
                return 401;
            }
            if (INVALID_TOKEN.equals(oauthErrorCode)) {
                return 401;
            }
        }
        return source.getHttpErrorCode();
    }

    public Map<String, String> getAdditionalInformation() {
        return source.getAdditionalInformation();
    }

    public void addAdditionalInformation(String key, String value) {
        source.addAdditionalInformation(key, value);
    }

    public String toString() {
        return source.toString();
    }

    public String getSummary() {
        return source.getSummary();
    }

    public String getMessage() {
        return source.getMessage();
    }

}
