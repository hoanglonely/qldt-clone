package com.mb.lab.banks.auth.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mb.lab.banks.auth.service.user.InternalUserService;

@Configuration
@EnableFeignClients(basePackageClasses = { InternalUserService.class })
public class ServiceConfig {

	@Bean
	public FeignErrorDecoder feignErrorDecoder() {
		return new FeignErrorDecoder();
	}

}
