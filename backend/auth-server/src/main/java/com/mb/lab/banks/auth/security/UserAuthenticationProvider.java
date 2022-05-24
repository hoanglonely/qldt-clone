package com.mb.lab.banks.auth.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import com.mb.lab.banks.auth.service.user.InternalUserService;
import com.mb.lab.banks.auth.service.user.model.UserLoginDto;
import com.mb.lab.banks.utils.security.UserAuthenticationToken;
import com.mb.lab.banks.utils.security.UserLogin;

/**
 * Provider for users of system
 * 
 * @author thanh
 */
public class UserAuthenticationProvider implements AuthenticationProvider {

    public static final String ROLES_PREFIX = "ROLE_";
    public static final String ROLE_USER = ROLES_PREFIX + "USER";

    @Autowired
    private InternalUserService internalUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getPrincipal() != null ? authentication.getPrincipal().toString() : null;
        String password = authentication.getCredentials() != null ? authentication.getCredentials().toString() : null;

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new BadCredentialsException("Bad User Credentials.");
        }

        UserLoginDto user = internalUserService.getByUsername(username);

        if (user == null) {
            throw new BadCredentialsException("Bad User Credentials.");
        }
        if (user.getPassword() == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Bad User Credentials.");
        }
        if (!user.isActive()) {
            throw new LockedException("User was locked.");
        }

        UserLogin userLogin = new UserLogin(user.getRole(), user.getId(), user.getUsername());

        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        // Always have role USER
        grantedAuthorities.add(new SimpleGrantedAuthority(ROLE_USER));
        grantedAuthorities.add(new SimpleGrantedAuthority(ROLES_PREFIX + userLogin.getRole().toString()));

        UserAuthenticationToken auth = new UserAuthenticationToken(String.valueOf(user.getId()), authentication.getCredentials(), grantedAuthorities);
        auth.setUserLogin(userLogin);

        return auth;
    }

    @Override
    public boolean supports(Class<?> authClass) {
        return true;
    }

}