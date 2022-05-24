package com.mb.lab.banks.auth.security;

import java.util.Map;

import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;

import com.mb.lab.banks.utils.event.UserDeactivationEvent;

/**
 * Listen for {@link UserDeactivationEvent} and destroy all sessions associated
 * with this user
 * 
 * @author thanh
 */
public class SessionDestroyUserDeactivationListener {

	private FindByIndexNameSessionRepository<? extends Session> sessionRepository;

	public SessionDestroyUserDeactivationListener(
			FindByIndexNameSessionRepository<? extends Session> sessionRepository) {
		this.sessionRepository = sessionRepository;
	}

	// TODO use this method
	public void deleteSession(String userId) {
		Map<String, ? extends Session> sessions = sessionRepository
				.findByIndexNameAndIndexValue(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, userId);
		for (Session session : sessions.values()) {
			sessionRepository.deleteById(session.getId());
		}
	}

}
