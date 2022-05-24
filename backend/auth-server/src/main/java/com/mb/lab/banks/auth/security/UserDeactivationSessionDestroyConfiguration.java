package com.mb.lab.banks.auth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;

import com.mb.lab.banks.utils.event.UserDeactivationEvent;

/**
 * Listen for {@link UserDeactivationEvent} to invalidate related sessions
 * 
 * @author thanh
 */
@Configuration
public class UserDeactivationSessionDestroyConfiguration {

    @Autowired
    private FindByIndexNameSessionRepository<? extends Session> sessionRepository;

    @Bean
    public SessionDestroyUserDeactivationListener sessionDestroyUserDeactivationListener() {
        return new SessionDestroyUserDeactivationListener(sessionRepository);
    }

}
