package com.mb.lab.banks.auth.service.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mb.lab.banks.auth.service.user.model.ChangePasswordDto;
import com.mb.lab.banks.auth.service.user.model.UserInfoDto;
import com.mb.lab.banks.auth.service.user.model.UserLoginDto;

@FeignClient(name = "user-service", path = "/api/internal/user")
public interface InternalUserService {

    @RequestMapping(value = "/get-by-username", method = RequestMethod.GET)
    public UserLoginDto getByUsername(@RequestParam(name = "username") String username);
    
    @RequestMapping(value = "/{id}/userinfo", method = RequestMethod.GET)
    public UserInfoDto getUserInfo(@PathVariable(name = "id") Long id);
    
    @RequestMapping(value = "/{id}/password", method = RequestMethod.PUT)
    public void changePassword(@PathVariable(name = "id") Long id, @RequestBody ChangePasswordDto changePasswordDto);
    
}
