package com.mb.lab.banks.auth.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;

public class CompositeClientDetailsService implements ClientDetailsService {

	public static final String KEY_INFO_SINGLE_TOKEN = "single_token";

	private Map<String, ClientDetails> clientDetailsStore = new HashMap<String, ClientDetails>();

	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
		ClientDetails details = clientDetailsStore.get(clientId);

		if (details == null) {
			throw new NoSuchClientException("No client with requested id: " + clientId);
		}

		return details;
	}

	public void setClientDetailsStore(Map<String, ClientDetails> clientDetailsStore) {
		this.clientDetailsStore = clientDetailsStore;
	}

}
