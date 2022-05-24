package com.mb.lab.banks.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter.XFrameOptionsMode;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import com.github.mkopylec.recaptcha.security.login.LoginFailuresManager;
import com.github.mkopylec.recaptcha.security.login.RecaptchaAwareRedirectStrategy;
import com.mb.lab.banks.auth.security.UserAuthenticationProvider;
import com.mb.lab.banks.auth.security.captcha.CaptchaFormLoginConfigurerEnhancer;
import com.mb.lab.banks.auth.security.captcha.CaptchaProperties;
import com.mb.lab.banks.auth.security.recaptcha.AppLoginFailuresClearingHandler;
import com.mb.lab.banks.auth.security.recaptcha.AppLoginFailuresCountingHandler;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter implements Ordered {

    private int order = 5;

    @Autowired
    private CommonProperties commonProperties;

    @Autowired
    private CaptchaProperties captchaProperties;

    @Autowired(required = false)
    private CaptchaFormLoginConfigurerEnhancer captchaFormLoginConfigurerEnhancer;

    @Autowired(required = false)
    private LoginFailuresManager rechaptchaLoginFailuresManager;

    @Autowired(required = false)
    private RecaptchaAwareRedirectStrategy recaptchaAwareRedirectStrategy;

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(userAuthenticationProvider());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers().antMatchers("/error", "/webjars/**", "/assets/**");
    }

    @Bean
    public AuthenticationProvider userAuthenticationProvider() {
        UserAuthenticationProvider authenticationProvider = new UserAuthenticationProvider();
        return authenticationProvider;
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .authorizeRequests()
                .anyRequest()
                .permitAll()
                .and()
            .exceptionHandling()
                .accessDeniedPage("/login?authorization_error=true")
                .and()
            .csrf()
                .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/authorize")).disable()
            .logout()
                .logoutSuccessUrl("/login")
                .logoutUrl("/logout")
                .deleteCookies("SESSIONID", CookieLocaleResolver.DEFAULT_COOKIE_NAME)
                .permitAll()
                .and()
             ;
        
        // Only allow frame from same domain
        http.headers().addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsMode.SAMEORIGIN));
        
        String loginSuccessUrl = commonProperties.getWebUrl();
        String loginFailUrl = "/login?authentication_error=true";
        FormLoginConfigurer<HttpSecurity> formLoginConfigurer;
        // If CAPTCHA not enabled
        if (!captchaProperties.isEnabled()) {
            formLoginConfigurer = http.formLogin()
                    .failureUrl(loginFailUrl)
                    .defaultSuccessUrl(loginSuccessUrl);
        }
        // If CAPTCHA enabled
        else {
            rechaptchaLoginFailuresManager.setUsernameParameter("j_username");
            
            AppLoginFailuresClearingHandler successHandler = new AppLoginFailuresClearingHandler(loginSuccessUrl, rechaptchaLoginFailuresManager);
            AppLoginFailuresCountingHandler failureHandler = new AppLoginFailuresCountingHandler(loginFailUrl,
                    rechaptchaLoginFailuresManager, captchaProperties, recaptchaAwareRedirectStrategy);
            
            // CAPTCHA will replace authentication filter, so any configuration must be place after this call
            formLoginConfigurer = captchaFormLoginConfigurerEnhancer.addRecaptchaSupport(http.formLogin())
                    .successHandler(successHandler)
                    .failureHandler(failureHandler);
        }

        formLoginConfigurer
            .usernameParameter("j_username")
            .passwordParameter("j_password")
            .loginPage("/login")
            .loginProcessingUrl("/login")
            .permitAll();
        
        // @formatter:on
    }

}
