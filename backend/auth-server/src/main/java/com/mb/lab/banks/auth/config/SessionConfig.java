package com.mb.lab.banks.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import com.mb.lab.banks.auth.security.SessionConfigParams;

@Configuration
public class SessionConfig {

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
        cookieSerializer.setCookieName(SessionConfigParams.SESSION_COOKIE_NAME);
        cookieSerializer.setCookiePath(SessionConfigParams.SESSION_COOKIE_PATH);
        cookieSerializer.setUseHttpOnlyCookie(true);
        return cookieSerializer;
    }

    @Bean
    public static ConfigureRedisAction configureRedisAction() {
        return ConfigureRedisAction.NO_OP;
    }

}