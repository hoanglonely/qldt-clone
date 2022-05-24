package com.mb.lab.banks.auth.service.partner;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "promotion-service", path = "/api/internal/partner")
public interface InternalPartnerService {
    
    @RequestMapping(value = "/{id}/", method = RequestMethod.GET)
    public PartnerDto getById(@PathVariable(name = "id") Long id);

}
