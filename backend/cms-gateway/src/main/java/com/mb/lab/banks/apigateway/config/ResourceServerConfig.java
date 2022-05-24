package com.mb.lab.banks.apigateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.JwtAccessTokenConverterConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import com.mb.lab.banks.apigateway.filter.StoreAuthenticationToAttributeFilter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig implements ResourceServerConfigurer, Ordered {
    
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    
    private int order = 4;

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
    }
    
    @Override
    public void configure(final HttpSecurity http) throws Exception {
        // @formatter:off

		http
            .requestMatchers()
                .antMatchers("/api/**", "/auth/**", "/mobile-api/**", "/open-api/**")
                .and()  
			.authorizeRequests()
    			.antMatchers("/api/internal/**", "/auth/api/internal/**")
                    .denyAll()
                .antMatchers("/auth/**", "/mobile-api/auth/**")
                    .permitAll()
                .antMatchers("/open-api/**")
                    .access("#oauth2.hasScope('open-api')")
				.antMatchers("/**")
				
				    .access("hasRole('ROLE_USER')")
				.and()
		    .addFilterAfter(new StoreAuthenticationToAttributeFilter(), AbstractPreAuthenticatedProcessingFilter.class)
		    ;
		 
		// @formatter:on
    }
    
    @Bean
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new RevocableJwtTokenStore(jwtAccessTokenConverter, redisConnectionFactory);
    }
    
    @Configuration
    public static class CustomJwtAccessTokenConverterConfigurer implements JwtAccessTokenConverterConfigurer {
        
        @Override
        public void configure(JwtAccessTokenConverter converter) {
            final DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
            accessTokenConverter.setUserTokenConverter(new UserAuthenticationConverter());
            
            converter.setAccessTokenConverter(accessTokenConverter);
        }
        
    }
}
