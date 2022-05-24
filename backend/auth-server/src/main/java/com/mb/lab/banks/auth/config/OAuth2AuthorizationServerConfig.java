package com.mb.lab.banks.auth.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import com.mb.lab.banks.auth.config.OAuth2ClientDetailsProperties.ClientDetails;
import com.mb.lab.banks.auth.security.CompositeClientDetailsServiceBuilder;
import com.mb.lab.banks.auth.security.EnableUserDeactivationAccessTokenDestroy;
import com.mb.lab.banks.auth.security.EnableUserDeactivationSessionDestroy;
import com.mb.lab.banks.auth.security.RedisAuthorizationCodeServices;
import com.mb.lab.banks.auth.security.RevocableJwtTokenStore;
import com.mb.lab.banks.auth.security.SingleClientCredentialsTokenGranter;
import com.mb.lab.banks.auth.service.app.InternalAppService;
import com.mb.lab.banks.auth.service.merchant.InternalMerchantService;
import com.mb.lab.banks.utils.security.custom.CustomWebResponseExceptionTranslator;

@Configuration
@EnableAuthorizationServer
@EnableUserDeactivationSessionDestroy
@EnableUserDeactivationAccessTokenDestroy
@EnableConfigurationProperties(OAuth2ClientDetailsProperties.class)
public class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${security.oauth2.authorization.jwt.key-value}")
    private String jwtKey;

    @Autowired
    private OAuth2ClientDetailsProperties clientDetailsProperties;

    @Autowired
    private InternalMerchantService internalMerchantService;

    @Autowired
    private InternalAppService internalAppService;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private DefaultTokenServices defaultTokenServices;

    @Autowired
    private OAuth2RequestFactory auth2RequestFactory;

    @Autowired
    private SingleClientCredentialsTokenGranter singleClientCredentialsTokenGranter;

    @Override
    public void configure(final AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        OAuth2AccessDeniedHandler accessDeniedHandler = new OAuth2AccessDeniedHandler();
        accessDeniedHandler.setExceptionTranslator(new CustomWebResponseExceptionTranslator());

        // @formatter:off
        oauthServer
            .allowFormAuthenticationForClients()
            .tokenKeyAccess("permitAll()")
            .checkTokenAccess("isAuthenticated()")
            .accessDeniedHandler(accessDeniedHandler);
        // @formatter:on
    }

    @Override
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
        CompositeClientDetailsServiceBuilder clientDetailsServiceBuilder = new CompositeClientDetailsServiceBuilder();
        for (ClientDetails clientDetails : clientDetailsProperties.getClientDetails().values()) {
            // @formatter:off
            clientDetailsServiceBuilder
                    .withClient(clientDetails.getClientId())
                    .secret(passwordEncoder.encode(clientDetails.getClientSecret()))
                    .authorizedGrantTypes(toArray(clientDetails.getGrantType()))
                    .scopes(toArray(clientDetails.getScope()))
                    .autoApprove(true)
                    .accessTokenValiditySeconds(clientDetails.getAccessTokenValidity())
                    .refreshTokenValiditySeconds(clientDetails.getRefreshTokenValidity())
                    .redirectUris(toArray(clientDetails.getRedirectUri()))
                    ;
            // @formatter:on
        }
        // @formatter:off
        clientDetailsServiceBuilder
            .passwordEncoder(passwordEncoder)
            .internalMerchantService(internalMerchantService)
            .internalAppService(internalAppService);
        // @formatter:on

        clients.setBuilder(clientDetailsServiceBuilder);
    }

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter()));
        // @formatter:off
        endpoints
            .tokenStore(tokenStore())
            .tokenEnhancer(tokenEnhancerChain)
            .authenticationManager(authenticationManager)
            .tokenServices(defaultTokenServices)
            .authorizationCodeServices(authorizationCodeServices())
            .requestFactory(auth2RequestFactory)
            .tokenGranter(tokenGranter(endpoints, singleClientCredentialsTokenGranter))
            .exceptionTranslator(new CustomWebResponseExceptionTranslator())
            ;
        // @formatter:on
    }

    @Bean
    public DefaultTokenServices tokenServices(ClientDetailsService clientDetailsService) {
        final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter()));

        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setTokenEnhancer(tokenEnhancerChain);
        tokenServices.setClientDetailsService(clientDetailsService);
        tokenServices.setReuseRefreshToken(false);
        return tokenServices;
    }

    @Bean
    public RevocableJwtTokenStore tokenStore() {
        return new RevocableJwtTokenStore(accessTokenConverter(), redisConnectionFactory);
    }

    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new RedisAuthorizationCodeServices(redisConnectionFactory);
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        final DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
        accessTokenConverter.setUserTokenConverter(new UserAuthenticationConverter());

        final JwtAccessTokenConverter converter = new CustomJwtAccessTokenConverter();
        converter.setAccessTokenConverter(accessTokenConverter);
        converter.setSigningKey(jwtKey);
        return converter;
    }

    @Bean
    public OAuth2RequestFactory oAuth2RequestFactory(ClientDetailsService clientDetailsService) {
        return new DefaultOAuth2RequestFactory(clientDetailsService);
    }

    @Bean
    @Lazy
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    public SingleClientCredentialsTokenGranter singleClientCredentialsTokenGranter(AuthorizationServerTokenServices tokenServices,
            ClientDetailsService clientDetailsService,
            OAuth2RequestFactory requestFactory,
            TokenStore tokenStore) {
        return new SingleClientCredentialsTokenGranter(tokenServices, clientDetailsService, requestFactory, tokenStore);
    }

    private TokenGranter tokenGranter(AuthorizationServerEndpointsConfigurer endpoints,
            SingleClientCredentialsTokenGranter singleClientCredentialsTokenGranter) {
        List<TokenGranter> granters = new ArrayList<TokenGranter>(Arrays.asList(endpoints.getTokenGranter()));
        granters.add(singleClientCredentialsTokenGranter);
        return new CompositeTokenGranter(granters);
    }

    private String[] toArray(List<String> list) {
        if (list == null) {
            return new String[0];
        }
        String[] results = new String[list.size()];
        results = list.toArray(results);
        return results;
    }

}
