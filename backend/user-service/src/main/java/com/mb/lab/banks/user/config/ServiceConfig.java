package com.mb.lab.banks.user.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mb.lab.banks.user.business.aop.ServiceAspect;
import com.mb.lab.banks.user.business.service.A_Service;
import com.mb.lab.banks.user.business.service.accesstoken.InternalAccessTokenService;
import com.mb.lab.banks.user.business.service.sms.InternalSmsService;
import com.mb.lab.banks.user.persistence.repository.impl.PORepositoryImpl;

@Configuration
@EnableCaching
@ComponentScan(basePackageClasses = { PORepositoryImpl.class, A_Service.class, ServiceAspect.class })
@EnableFeignClients(basePackageClasses = { InternalSmsService.class, InternalAccessTokenService.class })
@EnableConfigurationProperties(ApiProperties.class)
public class ServiceConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public FeignErrorDecoder feignErrorDecoder() {
		return new FeignErrorDecoder();
	}

	@Bean
	public PasswordStrenghtProperties passwordStrenghtProperties() {
		return new PasswordStrenghtProperties();
	}
}
