package com.mb.lab.banks.user.business.service.internal;

import java.util.List;
import java.util.Set;

import com.mb.lab.banks.user.business.dto.login.ChangePasswordDto;
import com.mb.lab.banks.user.business.dto.login.UserInfoDto;
import com.mb.lab.banks.user.business.dto.login.UserLoginDto;
import com.mb.lab.banks.user.business.dto.user.UserSearchParamDto;
import com.mb.lab.banks.user.business.dto.user.UserSimpleDto;

public interface InternalUserService {

    public UserLoginDto getByUsername(String username);

    public UserInfoDto getUserInfo(Long id);

    public void changePassword(Long id, ChangePasswordDto dto);

    public UserSimpleDto getById(Long id);

    public List<UserSimpleDto> getList(UserSearchParamDto searchParam);

    public void deactivateAdminStore(Long storeId);

    public void deactivateAdminPartner(Long partnerId);

    public Set<String> getFeatures(Long id);

}
