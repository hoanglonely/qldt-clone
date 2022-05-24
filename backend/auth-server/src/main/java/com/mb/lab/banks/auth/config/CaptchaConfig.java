package com.mb.lab.banks.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.github.mkopylec.recaptcha.security.login.LoginFailuresManager;
import com.github.mkopylec.recaptcha.security.login.RecaptchaAwareRedirectStrategy;
import com.github.mkopylec.recaptcha.testing.TestRecaptchaValidator;
import com.github.mkopylec.recaptcha.validation.RecaptchaValidator;
import com.mb.lab.banks.auth.security.captcha.CaptchaAuthenticationFilter;
import com.mb.lab.banks.auth.security.captcha.CaptchaFormLoginConfigurerEnhancer;
import com.mb.lab.banks.auth.security.captcha.CaptchaProperties;
import com.mb.lab.banks.auth.security.captcha.CaptchaValidator;
import com.mb.lab.banks.auth.security.recaptcha.AppLoginFailuresManager;
import com.mb.lab.banks.auth.security.recaptcha.LoginAttemptRepository;
import com.mb.lab.banks.auth.security.recaptcha.RedisLoginAttemptRepository;

@Configuration
@EnableConfigurationProperties(CaptchaProperties.class)
public class CaptchaConfig {

    @ConditionalOnProperty(name = "captcha.enabled", havingValue = "true", matchIfMissing = true)
    public static class ActualConfiguration {

        @Configuration("captchaSecurityConfiguration")
        @ConditionalOnClass({EnableWebSecurity.class, AbstractAuthenticationProcessingFilter.class})
        public static class SecurityConfiguration {

            @Autowired
            private RedisConnectionFactory redisConnectionFactory;

            @Autowired
            private CaptchaProperties captcha;

            @Bean
            public LoginAttemptRepository loginAttemptRepository() {
                return new RedisLoginAttemptRepository(redisConnectionFactory);
            }

            @Bean
            public CaptchaFormLoginConfigurerEnhancer formLoginConfigurerEnhancer(CaptchaValidator captchaValidator, LoginFailuresManager failuresManager) {
                CaptchaAuthenticationFilter authenticationFilter = new CaptchaAuthenticationFilter(captchaValidator, captcha, failuresManager);
                return new CaptchaFormLoginConfigurerEnhancer(authenticationFilter);
            }

            @Bean
            public LoginFailuresManager loginFailuresManager(LoginAttemptRepository loginAttemptRepository) {
                return new AppLoginFailuresManager(loginAttemptRepository, captcha);
            }

            @Bean
            public RecaptchaAwareRedirectStrategy recaptchaAwareRedirectStrategy(LoginFailuresManager failuresManager) {
                return new RecaptchaAwareRedirectStrategy(failuresManager);
            }
        }

        @Configuration("captchaValidationConfiguration")
        @ConditionalOnProperty(name = "captcha.testing.enabled", havingValue = "false", matchIfMissing = true)
        public static class ValidationConfiguration {

            @Bean
            @ConditionalOnMissingBean
            public CaptchaValidator userResponseValidator() {
                return new CaptchaValidator();
            }
        }

        @Configuration("captchaTestingConfiguration")
        @ConditionalOnProperty(name = "captcha.testing.enabled")
        public static class TestingConfiguration {

            private final CaptchaProperties captcha;

            public TestingConfiguration(CaptchaProperties captcha) {
                this.captcha = captcha;
            }

            @Bean
            @ConditionalOnMissingBean
            public RecaptchaValidator userResponseValidator() {
                return new TestRecaptchaValidator(captcha);
            }
        }
    }

}
