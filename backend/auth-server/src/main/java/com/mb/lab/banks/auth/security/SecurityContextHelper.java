package com.mb.lab.banks.auth.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.session.FindByIndexNameSessionRepository;

import com.mb.lab.banks.utils.security.UserAuthenticationToken;
import com.mb.lab.banks.utils.security.UserLogin;

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

    public static UserLogin extractUserLogin(OAuth2Authentication auth) {
        if (auth == null) {
            return null;
        }
        
        UserAuthenticationToken userAuth = (UserAuthenticationToken) auth.getUserAuthentication();
        return extractUserLogin(userAuth);
    }

    public static UserLogin extractUserLogin(UserAuthenticationToken auth) {
        if (auth == null) {
            return null;
        }
        return auth.getUserLogin();
    }

    /**
     * Check if user already logged via login page
     */
    public static boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof UserAuthenticationToken) {
            return true;
        }
        return false;
    }
    
    public static void saveUserIdToSession(HttpServletRequest request, Authentication authentication) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            if (authentication instanceof UserAuthenticationToken) {
                // Use userId to find sessions instead of username, because users may log in via social methods
                String userId = ((UserAuthenticationToken) authentication).getUserLogin().getId().toString();
                session.setAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, userId);
            }
        }
    }

}
