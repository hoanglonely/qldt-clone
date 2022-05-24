package com.mb.lab.banks.auth.service.store;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "promotion-service", path = "/api/internal/store", contextId = "InternalStoreService")
public interface InternalStoreService {
    
    @RequestMapping(value = "/{id}/", method = RequestMethod.GET)
    public StoreDto getById(@PathVariable(name = "id") Long id);
    
    @RequestMapping(value = "/get-list", method = RequestMethod.POST)
    public List<StoreDto> getList(@RequestBody StoreSearchParamDto searchParam);
    
}
