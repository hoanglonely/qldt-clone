package com.mb.lab.banks.auth.endpoint.mobile;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mb.lab.banks.utils.rest.mobile.MobileApiResponse;

@RestController
public class MobileRevokeTokenEndpoint {

    @Autowired
    @Lazy
    private ConsumerTokenServices tokenServices;

    @RequestMapping(method = RequestMethod.DELETE, value = "/mobile-api/revoke")
    public ResponseEntity<MobileApiResponse> revokeToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.contains("Bearer")) {
            String tokenId = authorization.substring("Bearer".length() + 1);
            tokenServices.revokeToken(tokenId);
        }
        return new ResponseEntity<>(new MobileApiResponse(null), HttpStatus.OK);
    }
}
