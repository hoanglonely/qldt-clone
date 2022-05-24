package com.mb.lab.banks.user.business.service.base.impl;

import com.mb.lab.banks.user.business.dto.base.DraftableDto;
import com.mb.lab.banks.user.business.service.A_Service;
import com.mb.lab.banks.user.business.service.base.PODraftableService;
import com.mb.lab.banks.user.persistence.domain.base.PODraftable;
import com.mb.lab.banks.user.persistence.domain.entity.ActiveStatus;
import com.mb.lab.banks.user.persistence.repository.PODraftableRepository;
import com.mb.lab.banks.user.util.security.UserLogin;
import com.mb.lab.banks.utils.exception.BusinessAssert;
import com.mb.lab.banks.utils.exception.BusinessException;
import com.mb.lab.banks.utils.exception.BusinessExceptionCode;

public abstract class PODraftableServiceImpl<DOMAIN extends PODraftable, READDTO extends DraftableDto, WRITEDTO>
		extends A_Service implements PODraftableService<READDTO, WRITEDTO> {

	// PROTECTED ABSTRACT
	protected abstract PODraftableRepository<DOMAIN> getRepository();

	protected abstract READDTO convertToReadDto(DOMAIN domain);

	// SUPPORT
	protected READDTO getByIdSupport(UserLogin userLogin, Long id) {
		DOMAIN domain = getPOMandatory(getRepository(), id);
		BusinessAssert.isTrue(isDomainValid(userLogin, domain));
		return convertToReadDto(domain);
	}

	protected void checkExistSupport(UserLogin userLogin, Long id) {
		if (id == null) {
			throw new BusinessException(BusinessExceptionCode.INVALID_PARAM);
		}

		if (!getRepository().existsById(id)) {
			throw new BusinessException(BusinessExceptionCode.INVALID_PARAM);
		}
	}

	protected void activateSupport(UserLogin userLogin, Long id) {
		DOMAIN domain = getPOMandatory(getRepository(), id);
		BusinessAssert.isTrue(isDomainValid(userLogin, domain));
		BusinessAssert.isTrue(domain.getActiveStatus().equals(ActiveStatus.INACTIVE));
		beforeActivate(userLogin, domain);
		getRepository().activate(id);
		afterActivate(userLogin, domain);
	}

	protected void deactivateSupport(UserLogin userLogin, Long id) {
		DOMAIN domain = getPOMandatory(getRepository(), id);
		BusinessAssert.isTrue(isDomainValid(userLogin, domain));
		BusinessAssert.isTrue(domain.getActiveStatus().equals(ActiveStatus.ACTIVE));
		beforeDeactivate(userLogin, domain);
		getRepository().deactivate(id);
		afterDeactivate(userLogin, domain);
	}

	// PRIVATE + PROTECTED
	protected boolean isDomainValid(UserLogin userLogin, DOMAIN domain) {
		return true;
	}

	protected void beforeActivate(UserLogin userLogin, DOMAIN domain) {
		// DO NOTHING
	}

	protected void afterActivate(UserLogin userLogin, DOMAIN id) {
		// DO NOTHING
	}

	protected void beforeDeactivate(UserLogin userLogin, DOMAIN domain) {
		// DO NOTHING
	}

	protected void afterDeactivate(UserLogin userLogin, DOMAIN id) {
		// DO NOTHING
	}

}
