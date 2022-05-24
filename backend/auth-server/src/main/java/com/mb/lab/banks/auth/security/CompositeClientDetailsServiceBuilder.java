package com.mb.lab.banks.auth.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;

public class CompositeClientDetailsServiceBuilder
		extends ClientDetailsServiceBuilder<CompositeClientDetailsServiceBuilder> {

	private Map<String, ClientDetails> clientDetails = new HashMap<String, ClientDetails>();

	public CompositeClientDetailsServiceBuilder passwordEncoder(PasswordEncoder passwordEncoder) {
		return this;
	}

	@Override
	protected void addClient(String clientId, ClientDetails value) {
		clientDetails.put(clientId, value);
	}

	@Override
	protected ClientDetailsService performBuild() {
		CompositeClientDetailsService clientDetailsService = new CompositeClientDetailsService();
		clientDetailsService.setClientDetailsStore(clientDetails);
		return clientDetailsService;
	}

}