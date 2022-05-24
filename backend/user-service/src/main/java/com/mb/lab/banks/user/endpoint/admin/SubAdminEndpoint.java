package com.mb.lab.banks.user.endpoint.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mb.lab.banks.user.business.dto.subadmin.UserWriteDto;
import com.mb.lab.banks.user.business.dto.user.UserSearchParamDto;
import com.mb.lab.banks.user.business.service.base.PODraftableService;
import com.mb.lab.banks.user.business.service.manager.SubAdminManagementService;
import com.mb.lab.banks.user.endpoint.base.AbstractPODraftableEndpoint;
import com.mb.lab.banks.utils.rest.Meta;

@RestController
@RequestMapping(value = "/api/admin/sub-admin")
public class SubAdminEndpoint extends AbstractPODraftableEndpoint<UserWriteDto> {

	@Autowired
	private SubAdminManagementService subAdminManagementService;

	@Override
	protected PODraftableService<?, UserWriteDto> getService() {
		return subAdminManagementService;
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public final ResponseEntity<?> search(@RequestBody UserSearchParamDto draftableSearchParamDto) {
		return getResponseEntity(subAdminManagementService.search(getUserLogin(), draftableSearchParamDto));
	}

	@RequestMapping(value = "/{id}/reset-password", method = RequestMethod.PUT)
	public final ResponseEntity<?> resetPassword(@PathVariable Long id) {
		subAdminManagementService.resetPassword(getUserLogin(), id);
		return getResponseEntity(Meta.OK);
	}

}
