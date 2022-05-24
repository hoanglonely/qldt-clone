package com.mb.lab.banks.auth.security;

import java.util.Map;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;

import com.mb.lab.banks.utils.event.UserDeactivationEvent;
import com.mb.lab.banks.utils.event.UserPasswordChangedEvent;
import com.mb.lab.banks.utils.event.stream.UserDeactivationStreams;
import com.mb.lab.banks.utils.event.stream.UserPasswordChangedStreams;

/**
 * Listen for {@link UserDeactivationEvent} and destroy all sessions associated with this user
 * 
 * @author thanh
 */
public class SessionDestroyUserDeactivationListener {

    private FindByIndexNameSessionRepository<? extends Session> sessionRepository;

    public SessionDestroyUserDeactivationListener(FindByIndexNameSessionRepository<? extends Session> sessionRepository) {
        this.sessionRepository = sessionRepository;
    }
    
    @StreamListener(UserDeactivationStreams.InBound.NAME)
    public void onEvent(@Payload UserDeactivationEvent event) {
        deleteSession(String.valueOf(event.getUserId()));
    }
    
    @StreamListener(UserPasswordChangedStreams.InBound.NAME)
    public void onEvent(@Payload UserPasswordChangedEvent event) {
        deleteSession(String.valueOf(event.getUserId()));
    }
    
    private void deleteSession(String userId) {
        Map<String, ? extends Session> sessions = sessionRepository.findByIndexNameAndIndexValue(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME,
                userId);
        for (Session session : sessions.values()) {
            sessionRepository.deleteById(session.getId());
        }
    }

}
