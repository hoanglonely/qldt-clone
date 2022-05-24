package com.mb.lab.banks.user.endpoint.internal;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mb.lab.banks.user.business.dto.login.ChangePasswordDto;
import com.mb.lab.banks.user.business.dto.login.UserInfoDto;
import com.mb.lab.banks.user.business.dto.login.UserLoginDto;
import com.mb.lab.banks.user.business.dto.user.UserSearchParamDto;
import com.mb.lab.banks.user.business.dto.user.UserSimpleDto;
import com.mb.lab.banks.user.business.service.internal.InternalUserService;

@RestController
@RequestMapping("/api/internal/user")
public class InternalUserEndpoint {

    @Autowired
    private InternalUserService internalUserService;

    @RequestMapping(value = "/get-by-username", method = RequestMethod.GET)
    public UserLoginDto getByUsername(@RequestParam(name = "username") String username) {
        return internalUserService.getByUsername(username);
    }

    @RequestMapping(value = "/{id}/", method = RequestMethod.GET)
    public UserSimpleDto getById(@PathVariable(name = "id") Long id) {
        return internalUserService.getById(id);
    }

    @RequestMapping(value = "/{id}/userinfo", method = RequestMethod.GET)
    public UserInfoDto getUserInfo(@PathVariable(name = "id") Long id) {
        return internalUserService.getUserInfo(id);
    }

    @RequestMapping(value = "/{id}/password", method = RequestMethod.PUT)
    public void changePassword(@PathVariable(name = "id") Long id, @RequestBody ChangePasswordDto changePasswordDto) {
        internalUserService.changePassword(id, changePasswordDto);
    }

    @RequestMapping(value = "/get-list", method = RequestMethod.POST)
    public List<UserSimpleDto> getList(@RequestBody UserSearchParamDto searchParam) {
        return internalUserService.getList(searchParam);
    }

    @RequestMapping(value = "/store/{id}/deactivate", method = RequestMethod.PUT)
    public void deactivateAdminStore(@PathVariable(name = "id") Long storeId) {
        internalUserService.deactivateAdminStore(storeId);
    }

    @RequestMapping(value = "/partner/{id}/deactivate", method = RequestMethod.PUT)
    public void deactivateAdminPartner(@PathVariable(name = "id") Long partnerId) {
        internalUserService.deactivateAdminPartner(partnerId);
    }

    @RequestMapping(value = "/{id}/features", method = RequestMethod.GET)
    public Set<String> getFeatures(@PathVariable(name = "id") Long id) {
        return internalUserService.getFeatures(id);
    }

}
