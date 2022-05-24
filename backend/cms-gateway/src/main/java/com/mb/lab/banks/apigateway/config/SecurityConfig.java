package com.mb.lab.banks.apigateway.config;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter implements Ordered {
    
    @Value("${storage.listOfBuckets}")
    private String[] listOfBuckets;

    private int order = 5;

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        String[] ignoredPaths = new String[] {
                // Error page
                "/401", "/404", "/500",

                // Other
                "/assets/**",
                
                // ViettelPay callback endpoints
                "/payment/vtpay/**"
        };
        List<RequestMatcher> requestMatchers = new LinkedList<>();
        for (String ignoredPath : ignoredPaths) {
            requestMatchers.add(new AntPathRequestMatcher(ignoredPath));
        }

        web.ignoring().requestMatchers(new OrRequestMatcher(requestMatchers));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String[] bucketPatterns = new String[listOfBuckets.length];
        for (int i = 0; i < listOfBuckets.length; i++) {
            bucketPatterns[i] = "/files/" + listOfBuckets[i] + "/**";
        }
        // @formatter:off
        // Match to any request except /api and /auth
        http
            .requestMatcher(new AntPathNegativeRequestMatcher("/api/**", "/auth/**"))
            .authorizeRequests()
                .antMatchers(HttpMethod.GET, bucketPatterns)
                    .permitAll()
                .antMatchers("/files/**")
                    .denyAll()
                .anyRequest()
                    .permitAll()
                .and()
            .headers()
                .frameOptions()
                    .sameOrigin()
            ;
        
        // @formatter:on
    }

    public static class AntPathNegativeRequestMatcher implements RequestMatcher {

        private final List<AntPathRequestMatcher> delegates;

        public AntPathNegativeRequestMatcher(String... paths) {
            this.delegates = new ArrayList<>(paths.length);
            for (String path : paths) {
                this.delegates.add(new AntPathRequestMatcher(path));
            }
        }

        @Override
        public boolean matches(HttpServletRequest request) {
            for (AntPathRequestMatcher delegate : delegates) {
                if (delegate.matches(request)) {
                    return false;
                }
            }
            return true;
        }
    }

}
