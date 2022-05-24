package com.mb.lab.banks.auth.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mb.lab.banks.auth.service.app.InternalAppService;
import com.mb.lab.banks.auth.service.merchant.InternalMerchantService;
import com.mb.lab.banks.auth.service.partner.InternalPartnerService;
import com.mb.lab.banks.auth.service.store.InternalStoreService;
import com.mb.lab.banks.auth.service.user.InternalUserService;
import com.mb.lab.banks.utils.event.stream.EventStreamsHelper;
import com.mb.lab.banks.utils.event.stream.UserDeactivationStreams;
import com.mb.lab.banks.utils.event.stream.UserPasswordChangedStreams;

@Configuration
@EnableBinding({ UserDeactivationStreams.InBound.class, UserPasswordChangedStreams.InBound.class })
@EnableFeignClients(basePackageClasses = { InternalUserService.class, InternalPartnerService.class, InternalStoreService.class, InternalMerchantService.class,
        InternalAppService.class })
public class ServiceConfig {

    @Bean
    public FeignErrorDecoder feignErrorDecoder() {
        return new FeignErrorDecoder();
    }

    @Bean
    public EventStreamsHelper eventStreamsHelper() {
        return new EventStreamsHelper();
    }

}
