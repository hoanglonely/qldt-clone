package com.mb.lab.banks.user.business.service.sms;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mb.lab.banks.user.business.dto.sms.SmsContentConfigDto;
import com.mb.lab.banks.user.persistence.domain.entity.SmsType;

@FeignClient(name = "promotion-service", path = "/api/internal/sms", contextId = "InternalSmsService")
public interface InternalSmsService {

    @RequestMapping(value = "/content-config", method = RequestMethod.GET)
    public SmsContentConfigDto getContentConfig(@RequestParam(name = "type") SmsType type);

}