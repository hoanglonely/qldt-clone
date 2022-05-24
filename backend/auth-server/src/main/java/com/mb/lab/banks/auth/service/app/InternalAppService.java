package com.mb.lab.banks.auth.service.app;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "user-service", path = "/api/internal/app", contextId = "InternalAppService")
public interface InternalAppService {

    @RequestMapping(value = "/by-client-id/{clientId}", method = RequestMethod.GET)
    public AppDto getByClientId(@PathVariable(name = "clientId") String clientId);

}
