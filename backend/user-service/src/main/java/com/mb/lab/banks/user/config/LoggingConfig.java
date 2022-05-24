package com.mb.lab.banks.user.config;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.mb.lab.banks.user.util.security.SecurityContextHelper;
import com.mb.lab.banks.user.util.security.UserLogin;

@Configuration
public class LoggingConfig extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private MdcLogEnhancerFilter mdcLogEnhancerFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterAfter(mdcLogEnhancerFilter, AbstractPreAuthenticatedProcessingFilter.class);
    }

    @Component
    public static class MdcLogEnhancerFilter extends GenericFilterBean {
        
        private static final String[] REMOTE_IP_HEADER_CANDIDATES = {
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_X_FORWARDED_FOR",
                "HTTP_X_FORWARDED",
                "HTTP_X_CLUSTER_CLIENT_IP",
                "HTTP_CLIENT_IP",
                "HTTP_FORWARDED_FOR",
                "HTTP_FORWARDED",
                "HTTP_VIA", "REMOTE_ADDR"
        };
        
        @Value("${spring.application.name}")
        private String applicationName;

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            MDC.put("applicationName", applicationName);
            
            if (request instanceof HttpServletRequest) {
                HttpServletRequest servletRequest = (HttpServletRequest) request;
                
                // Add username
                Principal user = servletRequest.getUserPrincipal();
                UserLogin userLogin = SecurityContextHelper.getUser(user);
                if (userLogin != null) {
                    MDC.put("username", userLogin.getUsername());
                }
                
                // Add remote address
                MDC.put("remoteIp", getClientIpAddress(servletRequest));
                
                // Add requested path
                MDC.put("requestedPath", servletRequest.getRequestURI());
            }
            
            chain.doFilter(request, response);
            MDC.clear();
        }

        public static String getClientIpAddress(HttpServletRequest request) {
            for (String header : REMOTE_IP_HEADER_CANDIDATES) {
                String ipList = request.getHeader(header);
                if (ipList != null && ipList.length() != 0 && !"unknown".equalsIgnoreCase(ipList)) {
                    String ip = ipList.split(",")[0];
                    return ip;
                }
            }

            return request.getRemoteAddr();
        }
    }

}
