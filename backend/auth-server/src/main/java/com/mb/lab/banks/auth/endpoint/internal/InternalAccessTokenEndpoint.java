package com.mb.lab.banks.auth.endpoint.internal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mb.lab.banks.auth.dto.internal.TokenDto;
import com.mb.lab.banks.auth.service.internal.InternalAccessTokenService;

@RestController
@RequestMapping("/api/internal/access-token")
public class InternalAccessTokenEndpoint {

    @Autowired
    private InternalAccessTokenService internalAccessTokenService;

    @RequestMapping(value = "/create-for-merchant", method = RequestMethod.PUT)
    public TokenDto createMerchantAccessToken(@RequestParam(name = "clientId") Long clientId) {
        return internalAccessTokenService.createMerchantAccessToken(clientId);
    }

    @RequestMapping(value = "/restore-for-merchant", method = RequestMethod.PUT)
    public TokenDto restoreMerchantAccessToken(@RequestParam(name = "clientId") Long clientId, @RequestParam(name = "token") String token) {
        return internalAccessTokenService.restoreMerchantAccessToken(clientId, token);
    }

    @RequestMapping(value = "/delete-for-merchant", method = RequestMethod.PUT)
    public void deleteMerchantAccessToken(@RequestParam(name = "clientId") Long clientId) {
        internalAccessTokenService.deleteMerchantAccessToken(clientId);
    }

    @RequestMapping(value = "/create-for-app", method = RequestMethod.PUT)
    public TokenDto createAppAccessToken(@RequestParam(name = "clientId") String clientId) {
        return internalAccessTokenService.createAppAccessToken(clientId);
    }

    @RequestMapping(value = "/restore-for-app", method = RequestMethod.PUT)
    public TokenDto restoreAppAccessToken(@RequestParam(name = "clientId") String clientId, @RequestParam(name = "token") String token) {
        return internalAccessTokenService.restoreAppAccessToken(clientId, token);
    }

    @RequestMapping(value = "/delete-for-app", method = RequestMethod.PUT)
    public void deleteAppAccessToken(@RequestParam(name = "clientId") String clientId) {
        internalAccessTokenService.deleteAppAccessToken(clientId);
    }
}
