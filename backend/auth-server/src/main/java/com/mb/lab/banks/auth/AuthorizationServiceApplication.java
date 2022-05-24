package com.mb.lab.banks.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import com.mb.lab.banks.utils.security.custom.CustomWebResponseExceptionTranslatorProcessor;

@SpringBootApplication
@EnableResourceServer
@EnableDiscoveryClient
public class AuthorizationServiceApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(AuthorizationServiceApplication.class);
        springApplication.addListeners(new ApplicationPidFileWriter());
        springApplication.run(args);
    }
    
    @Bean
    public CustomWebResponseExceptionTranslatorProcessor customWebResponseExceptionTranslatorProcessor() {
        return new CustomWebResponseExceptionTranslatorProcessor();
    }

}
