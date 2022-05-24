package com.mb.lab.banks.auth.endpoint.mobile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mb.lab.banks.auth.dto.MobileUserInfoDto;
import com.mb.lab.banks.auth.dto.MobileUserInfoDto.MobileShopInfoDto;
import com.mb.lab.banks.auth.security.SecurityContextHelper;
import com.mb.lab.banks.auth.service.base.ActiveStatus;
import com.mb.lab.banks.auth.service.base.ReviewStatus;
import com.mb.lab.banks.auth.service.partner.InternalPartnerService;
import com.mb.lab.banks.auth.service.partner.PartnerDto;
import com.mb.lab.banks.auth.service.store.InternalStoreService;
import com.mb.lab.banks.auth.service.store.StoreDto;
import com.mb.lab.banks.auth.service.store.StoreSearchParamDto;
import com.mb.lab.banks.auth.service.user.InternalUserService;
import com.mb.lab.banks.auth.service.user.model.ChangePasswordDto;
import com.mb.lab.banks.auth.service.user.model.UserInfoDto;
import com.mb.lab.banks.utils.exception.BusinessException;
import com.mb.lab.banks.utils.exception.BusinessExceptionCode;
import com.mb.lab.banks.utils.rest.mobile.MobileApiResponse;
import com.mb.lab.banks.utils.security.UserLogin;

@Controller
public class MobileUserInfoEndpoint {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private InternalUserService internalUserService;
    
    @Autowired
    private InternalPartnerService internalPartnerService;
    
    @Autowired
    private InternalStoreService internalStoreService;
	
	@RequestMapping("/mobile-api/userinfo")
    public ResponseEntity<MobileApiResponse> getUserInfo() {
        UserLogin userLogin = SecurityContextHelper.getCurrentUser();
        UserInfoDto userLoginDto = internalUserService.getUserInfo(userLogin.getId());
        
        if (userLoginDto == null) {
        	logger.warn("Fail to load user info for userId=" + userLogin.getId());
            return new ResponseEntity<>(MobileApiResponse.INVALID_PARAM, HttpStatus.OK);
        }
        
        MobileUserInfoDto result = new MobileUserInfoDto();
        result.setUserId(userLoginDto.getId());
        result.setAccount(userLoginDto.getUsername());
        
        if (userLoginDto.getPartnerId() != null) {
            PartnerDto partner = internalPartnerService.getById(userLoginDto.getPartnerId());
            result.setAvatar(partner.getLogo());
        }
        
        if (UserLogin.PARTNER_ADMIN.equals(userLoginDto.getRole())) {
            result.setUserType(1);
            
            if (userLoginDto.getPartnerId() != null) {
                StoreSearchParamDto searchParam = new StoreSearchParamDto();
                searchParam.setActiveStatus(ActiveStatus.ACTIVE);
                searchParam.setPartnerId(userLoginDto.getPartnerId());
                
                List<StoreDto> stores = internalStoreService.getList(searchParam);
                // @formatter:off
                result.setListShop(stores
                        .stream()
                        .filter(store -> store.getActiveStatus() == ActiveStatus.ACTIVE && store.getStatus() != null && store.getStatus() == ReviewStatus.APPROVED)
                        .map(store -> new MobileShopInfoDto(store))
                        .collect(Collectors.toList()));
                // @formatter:on
            } else {
                result.setListShop(Collections.emptyList());
            }
            
        } else if (UserLogin.STORE_ADMIN.equals(userLoginDto.getRole())) {
            result.setUserType(0);
            
            if (userLoginDto.getStoreId() != null) {
                StoreDto store = internalStoreService.getById(userLoginDto.getStoreId());
                result.setListShop(Arrays.asList(new MobileShopInfoDto(store)));
            } else {
                result.setListShop(Collections.emptyList());
            }
        } else {
            result.setUserType(3);
            result.setListShop(Collections.emptyList());
        }
        
        return new ResponseEntity<>(new MobileApiResponse(result), HttpStatus.OK);
    }

    @RequestMapping(value = "/mobile-api/change-password", method = RequestMethod.POST)
    public ResponseEntity<MobileApiResponse> changePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        UserLogin userLogin = SecurityContextHelper.getCurrentUser();
        try {
        	internalUserService.changePassword(userLogin.getId(), changePasswordDto);
        	return new ResponseEntity<>(MobileApiResponse.SUCCESS, HttpStatus.OK);
        } catch (BusinessException e) {
            if (BusinessExceptionCode.INVALID_OLD_PASSWORD.equals(e.getCode())) {
                return new ResponseEntity<>(new MobileApiResponse(1, "Invalid old password"), HttpStatus.OK);
            } else if (BusinessExceptionCode.INVALID_NEW_PASSWORD.equals(e.getCode()) || BusinessExceptionCode.WEAK_PASSWORD.equals(e.getCode())) {
                return new ResponseEntity<>(new MobileApiResponse(2, "Password not strong enough"), HttpStatus.OK);
            } else if (BusinessExceptionCode.PASSWORD_NOT_SAME.equals(e.getCode())) {
                return new ResponseEntity<>(new MobileApiResponse(3, "New password same as old password"), HttpStatus.OK);
            }
            throw e;
        }
    }

}
