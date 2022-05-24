package com.mb.lab.banks.auth.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mb.lab.banks.auth.security.SecurityContextHelper;
import com.mb.lab.banks.auth.service.user.InternalUserService;
import com.mb.lab.banks.auth.service.user.model.ChangePasswordDto;
import com.mb.lab.banks.auth.service.user.model.UserInfoDto;
import com.mb.lab.banks.utils.exception.BusinessException;
import com.mb.lab.banks.utils.rest.Envelope;
import com.mb.lab.banks.utils.rest.Meta;
import com.mb.lab.banks.utils.rest.RestError;
import com.mb.lab.banks.utils.security.UserLogin;

@Controller
public class UserInfoEndpoint {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private InternalUserService internalUserService;
	
	@RequestMapping("/oauth/userinfo")
    public ResponseEntity<?> getUserInfo() {
        UserLogin userLogin = SecurityContextHelper.getCurrentUser();
        if (userLogin == null) {
            RestError error = new RestError("AuthenticationException", HttpStatus.UNAUTHORIZED.value(), "user.not.loggedin");
            Envelope response = new Envelope(error);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        UserInfoDto userLoginDto = internalUserService.getUserInfo(userLogin.getId());
        
        if (userLoginDto == null) {
        	logger.warn("Fail to load user info for userId=" + userLogin.getId());
            RestError error = new RestError("AuthenticationException", HttpStatus.UNAUTHORIZED.value(), "user.not.found");
            Envelope response = new Envelope(error);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        
        return new ResponseEntity<UserInfoDto>(userLoginDto, HttpStatus.OK);
    }
	

    @RequestMapping(value = "/oauth/password", method = RequestMethod.PUT)
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        UserLogin userLogin = SecurityContextHelper.getCurrentUser();
        try {
        	internalUserService.changePassword(userLogin.getId(), changePasswordDto);
            return new Envelope(Meta.OK).toResponseEntity(HttpStatus.OK);
        } catch (BusinessException ex) {
            logger.debug("Fail to update user's password", ex);

            RestError error = new RestError("BusinessException", HttpStatus.BAD_REQUEST.value(), ex.getCode());
            Envelope response = new Envelope(error);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

}
