package com.mb.lab.banks.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.mb.lab.banks.apigateway.config.LocalRateLimitProperties;
import com.mb.lab.banks.utils.security.custom.CustomWebResponseExceptionTranslatorProcessor;

@SpringBootApplication
@EnableZuulProxy
@EnableDiscoveryClient
@EnableConfigurationProperties(LocalRateLimitProperties.class)
@EnableScheduling
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ApiGatewayApplication.class);
        springApplication.addListeners(new ApplicationPidFileWriter());
        springApplication.run(args);
    }

    @Bean
    public CustomWebResponseExceptionTranslatorProcessor customWebResponseExceptionTranslatorProcessor() {
        return new CustomWebResponseExceptionTranslatorProcessor();
    }

}
