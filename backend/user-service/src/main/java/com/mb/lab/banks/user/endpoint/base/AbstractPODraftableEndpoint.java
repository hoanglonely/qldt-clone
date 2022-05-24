package com.mb.lab.banks.user.endpoint.base;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mb.lab.banks.user.business.service.base.PODraftableService;
import com.mb.lab.banks.utils.rest.Meta;

public abstract class AbstractPODraftableEndpoint<WRITE> extends AbstractPODraftableSimpleEndpoint {

    @Override
    protected abstract PODraftableService<?, WRITE> getService();

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody WRITE writeDto) {
        return getResponseEntity(getService().create(getUserLogin(), writeDto));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody WRITE writeDto) {
        return getResponseEntity(getService().update(getUserLogin(), id, writeDto));
    }

    @RequestMapping(value = "/{id}/exist", method = RequestMethod.GET)
    public ResponseEntity<?> checkExist(@PathVariable Long id) {
        getService().checkExist(getUserLogin(), id);
        return getResponseEntity(Meta.OK);
    }

}
