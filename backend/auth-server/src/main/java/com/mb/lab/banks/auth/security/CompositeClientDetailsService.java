package com.mb.lab.banks.auth.security;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

import com.mb.lab.banks.auth.service.app.AppDto;
import com.mb.lab.banks.auth.service.app.InternalAppService;
import com.mb.lab.banks.auth.service.merchant.InternalMerchantService;
import com.mb.lab.banks.auth.service.merchant.MerchantDto;
import com.mb.lab.banks.utils.security.SecurityUtils;

public class CompositeClientDetailsService implements ClientDetailsService {

    public static final String KEY_INFO_SINGLE_TOKEN = "single_token";

    // Defaul to 20 years
    private static final int APP_TOKEN_VALIDITY_SECONDS = 20 * 365 * 24 * 60 * 60;

    private PasswordEncoder passwordEncoder;
    private InternalMerchantService internalMerchantService;
    private InternalAppService internalAppService;

    private Map<String, ClientDetails> clientDetailsStore = new HashMap<String, ClientDetails>();

    public CompositeClientDetailsService(PasswordEncoder passwordEncoder,
            InternalMerchantService internalMerchantService,
            InternalAppService internalAppService) {
        this.passwordEncoder = passwordEncoder;
        this.internalMerchantService = internalMerchantService;
        this.internalAppService = internalAppService;
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        ClientDetails details = null;

        if (SecurityUtils.isMerchantClientId(clientId)) {
            MerchantDto merchant = internalMerchantService.getByClientId(Long.valueOf(clientId));

            if (merchant != null) {
                BaseClientDetails clientDetails = new BaseClientDetails();
                clientDetails.setClientId(String.valueOf(merchant.getClientId()));
                clientDetails.setClientSecret(passwordEncoder.encode(merchant.getClientSecret()));
                clientDetails.setAuthorizedGrantTypes(Arrays.asList("single_client_credentials"));
                clientDetails.setScope(Arrays.asList("open-api"));
                clientDetails.setAutoApproveScopes(Arrays.asList("open-api"));
                clientDetails.setAccessTokenValiditySeconds(APP_TOKEN_VALIDITY_SECONDS);
                clientDetails.addAdditionalInformation(KEY_INFO_SINGLE_TOKEN, true);

                details = clientDetails;
            }
        } else if (SecurityUtils.isMyViettelAppClientId(clientId)) {
            AppDto app = internalAppService.getByClientId(clientId);

            if (app != null) {
                BaseClientDetails clientDetails = new BaseClientDetails();
                clientDetails.setClientId(app.getClientId());
                clientDetails.setClientSecret(passwordEncoder.encode(app.getClientSecret()));
                clientDetails.setAuthorizedGrantTypes(Arrays.asList("single_client_credentials"));
                clientDetails.setScope(Arrays.asList("myvt-api"));
                clientDetails.setAutoApproveScopes(Arrays.asList("myvt-api"));
                clientDetails.setAccessTokenValiditySeconds(APP_TOKEN_VALIDITY_SECONDS);
                clientDetails.addAdditionalInformation(KEY_INFO_SINGLE_TOKEN, true);

                details = clientDetails;
            }
        } else {
            details = clientDetailsStore.get(clientId);
        }

        if (details == null) {
            throw new NoSuchClientException("No client with requested id: " + clientId);
        }

        return details;
    }

    public void setClientDetailsStore(Map<String, ClientDetails> clientDetailsStore) {
        this.clientDetailsStore = clientDetailsStore;
    }

}
