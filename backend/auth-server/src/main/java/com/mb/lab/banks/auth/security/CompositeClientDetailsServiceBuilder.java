package com.mb.lab.banks.auth.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;

import com.mb.lab.banks.auth.service.app.InternalAppService;
import com.mb.lab.banks.auth.service.merchant.InternalMerchantService;

public class CompositeClientDetailsServiceBuilder extends ClientDetailsServiceBuilder<CompositeClientDetailsServiceBuilder> {

    private PasswordEncoder passwordEncoder;
    private InternalMerchantService internalMerchantService;
    private InternalAppService internalAppService;

    private Map<String, ClientDetails> clientDetails = new HashMap<String, ClientDetails>();

    public CompositeClientDetailsServiceBuilder passwordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        return this;
    }

    public CompositeClientDetailsServiceBuilder internalMerchantService(InternalMerchantService internalMerchantService) {
        this.internalMerchantService = internalMerchantService;
        return this;
    }

    public CompositeClientDetailsServiceBuilder internalAppService(InternalAppService internalAppService) {
        this.internalAppService = internalAppService;
        return this;
    }

    @Override
    protected void addClient(String clientId, ClientDetails value) {
        clientDetails.put(clientId, value);
    }

    @Override
    protected ClientDetailsService performBuild() {
        CompositeClientDetailsService clientDetailsService = new CompositeClientDetailsService(passwordEncoder, internalMerchantService, internalAppService);
        clientDetailsService.setClientDetailsStore(clientDetails);
        return clientDetailsService;
    }

}