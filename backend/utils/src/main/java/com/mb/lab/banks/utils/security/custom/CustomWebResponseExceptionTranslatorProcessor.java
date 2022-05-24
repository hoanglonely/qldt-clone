package com.mb.lab.banks.utils.security.custom;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.oauth2.provider.error.AbstractOAuth2SecurityExceptionHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;

public class CustomWebResponseExceptionTranslatorProcessor implements BeanPostProcessor {
    
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof AbstractOAuth2SecurityExceptionHandler) {
            ((AbstractOAuth2SecurityExceptionHandler) bean).setExceptionTranslator(new CustomWebResponseExceptionTranslator());
        } else if (bean instanceof ClientCredentialsTokenEndpointFilter) {
            OAuth2AuthenticationEntryPoint authenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
            authenticationEntryPoint.setExceptionTranslator(new CustomWebResponseExceptionTranslator());
            ((ClientCredentialsTokenEndpointFilter) bean).setAuthenticationEntryPoint(authenticationEntryPoint);
        }
        return bean;
    }
}