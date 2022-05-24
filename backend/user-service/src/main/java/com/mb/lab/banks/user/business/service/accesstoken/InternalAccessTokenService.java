package com.mb.lab.banks.user.business.service.accesstoken;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth-server", path = "/auth/api/internal/access-token")
public interface InternalAccessTokenService {

    @RequestMapping(value = "/create-for-app", method = RequestMethod.PUT)
    public TokenDto createAppAccessToken(@RequestParam(name = "clientId") String clientId);

    @RequestMapping(value = "/restore-for-app", method = RequestMethod.PUT)
    public TokenDto restoreAppAccessToken(@RequestParam(name = "clientId") String clientId, @RequestParam(name = "token") String token);

    @RequestMapping(value = "/delete-for-app", method = RequestMethod.PUT)
    public void deleteAppAccessToken(@RequestParam(name = "clientId") String clientId);

}
