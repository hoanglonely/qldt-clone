package com.mb.lab.banks.user.business.service.manager;

import com.mb.lab.banks.user.business.dto.base.ListDto;
import com.mb.lab.banks.user.business.dto.subadmin.UserDto;
import com.mb.lab.banks.user.business.dto.subadmin.UserWriteDto;
import com.mb.lab.banks.user.business.dto.user.UserSearchParamDto;
import com.mb.lab.banks.user.business.dto.user.UserSimpleDto;
import com.mb.lab.banks.user.business.service.base.PODraftableService;
import com.mb.lab.banks.user.util.security.UserLogin;

public interface SubAdminManagementService extends PODraftableService<UserDto, UserWriteDto> {

    public ListDto<UserSimpleDto> search(UserLogin userLogin, UserSearchParamDto draftableSearchParamDto);

    public void resetPassword(UserLogin userLogin, Long id);

}
