package com.mb.lab.banks.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.Bean;

import com.mb.lab.banks.utils.security.custom.CustomWebResponseExceptionTranslatorProcessor;

@SpringBootApplication(exclude = {
        // Exclude reCAPTCHA auto-configuration
        com.github.mkopylec.recaptcha.security.SecurityConfiguration.class,
        com.github.mkopylec.recaptcha.validation.ValidationConfiguration.class,
        com.github.mkopylec.recaptcha.testing.TestingConfiguration.class
})
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
