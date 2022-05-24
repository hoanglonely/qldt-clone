package com.mb.lab.banks.user.endpoint.base;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mb.lab.banks.user.business.service.base.PODraftableService;
import com.mb.lab.banks.utils.rest.Meta;

public abstract class AbstractPODraftableSimpleEndpoint extends AbstractEndpoint {

	protected abstract PODraftableService<?, ?> getService();

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable Long id) {
		return getResponseEntity(getService().getById(getUserLogin(), id));
	}

	@RequestMapping(value = "/{id}/activate", method = RequestMethod.PUT)
	public ResponseEntity<?> activate(@PathVariable Long id) {
		getService().activate(getUserLogin(), id);
		return getResponseEntity(Meta.OK);
	}

	@RequestMapping(value = "/{id}/deactivate", method = RequestMethod.PUT)
	public ResponseEntity<?> deactivate(@PathVariable Long id) {
		getService().deactivate(getUserLogin(), id);
		return getResponseEntity(Meta.OK);
	}

}
