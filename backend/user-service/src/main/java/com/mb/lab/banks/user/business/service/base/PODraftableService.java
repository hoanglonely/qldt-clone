package com.mb.lab.banks.user.business.service.base;

import com.mb.lab.banks.user.business.dto.base.DTO;
import com.mb.lab.banks.user.business.dto.base.DraftableDto;
import com.mb.lab.banks.user.util.security.UserLogin;

public interface PODraftableService<READDTO extends DraftableDto, WRITEDTO> {

    public READDTO getById(UserLogin userLogin, Long id);

    public void checkExist(UserLogin userLogin, Long id);

    public DTO create(UserLogin userLogin, WRITEDTO writeDto);

    public DTO update(UserLogin userLogin, Long id, WRITEDTO writeDto);

    public void activate(UserLogin userLogin, Long id);

    public void deactivate(UserLogin userLogin, Long id);

}
