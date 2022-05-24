package com.mb.lab.banks.auth.service.merchant;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "promotion-service", path = "/api/internal/merchant", contextId = "InternalMerchantService")
public interface InternalMerchantService {
    
    @RequestMapping(value = "/by-client-id/{clientId}", method = RequestMethod.GET)
    public MerchantDto getByClientId(@PathVariable(name = "clientId") Long clientId);
    
}
