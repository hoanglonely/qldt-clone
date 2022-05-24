package com.mb.lab.banks.user.util.security;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * @author thanh
 */
public class SecurityContextHelper {

    public static UserLogin getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof UserAuthenticationToken) {
            return extractUserLogin((UserAuthenticationToken) authentication);
        }
        if (authentication instanceof OAuth2Authentication) {
            return extractUserLogin((OAuth2Authentication) authentication);
        }
        return null;
    }

    public static Long getCurrentUserId() {
        UserLogin userLogin = getCurrentUser();
        return userLogin != null ? userLogin.getId() : null;
    }

    public static boolean isUserHasRole(String role) {
        UserLogin userLogin = getCurrentUser();
        if (userLogin == null) {
            return false;
        }
        return userLogin.getRole() == role;
    }
    
    public static UserLogin getUser(Principal principal) {
        if (principal != null && principal instanceof Authentication) {
            Authentication authentication = (Authentication) principal;
            if (authentication instanceof UserAuthenticationToken) {
                return extractUserLogin((UserAuthenticationToken) authentication);
            }
            if (authentication instanceof OAuth2Authentication) {
                return extractUserLogin((OAuth2Authentication) authentication);
            }
        }
        return null;
    }

    public static UserLogin extractUserLogin(UserAuthenticationToken auth) {
        if (auth == null) {
            return null;
        }
        return auth.getUserLogin();
    }

    public static UserLogin extractUserLogin(OAuth2Authentication auth) {
        if (auth == null) {
            return null;
        }
        
        UserAuthenticationToken userAuth = (UserAuthenticationToken) auth.getUserAuthentication();
        return extractUserLogin(userAuth);
    }

}
