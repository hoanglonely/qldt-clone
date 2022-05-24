package com.mb.lab.banks.apigateway.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

public class StoreAuthenticationToAttributeFilter extends OncePerRequestFilter {
    
    public static final String ATTRIBUTE_AUTHENTICATION = "QLDT_AUTHENTICATION";
    
    private final AntPathRequestMatcher requestMatcher = new AntPathRequestMatcher("/open-api/**");
    
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !requestMatcher.matches(request);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        request.setAttribute(ATTRIBUTE_AUTHENTICATION, authentication);
        filterChain.doFilter(request, response);
    }

}
