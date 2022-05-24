package com.mb.lab.banks.auth.security.captcha;

import static org.springframework.util.ReflectionUtils.findField;
import static org.springframework.util.ReflectionUtils.makeAccessible;
import static org.springframework.util.ReflectionUtils.setField;

import java.lang.reflect.Field;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

public class CaptchaFormLoginConfigurerEnhancer {
	
	protected static final String AUTHENTICATION_PROCESSING_FILTER_FIELD = "authFilter";

    protected final CaptchaAuthenticationFilter authenticationFilter;

    public CaptchaFormLoginConfigurerEnhancer(CaptchaAuthenticationFilter authenticationFilter) {
        this.authenticationFilter = authenticationFilter;
    }

    public FormLoginConfigurer<HttpSecurity> addRecaptchaSupport(FormLoginConfigurer<HttpSecurity> loginConfigurer) {
        // Use reflection to replace authentication filter (default is UsernamePasswordAuthenticationFilter)
        Field authFilterField = findField(loginConfigurer.getClass(), AUTHENTICATION_PROCESSING_FILTER_FIELD, AbstractAuthenticationProcessingFilter.class);
        makeAccessible(authFilterField);
        setField(authFilterField, loginConfigurer, authenticationFilter);

        return loginConfigurer;
    }
}
