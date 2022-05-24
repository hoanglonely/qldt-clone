package com.mb.lab.banks.apigateway.filter;

import java.io.IOException;
import java.time.Duration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.mb.lab.banks.apigateway.config.LocalRateLimitProperties;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LocalRateLimiterFilter extends OncePerRequestFilter {

    private LocalRateLimitProperties localRateLimitProperties;
    private RateLimiter rateLimiter;

    public LocalRateLimiterFilter(LocalRateLimitProperties localRateLimitProperties) {
        // @formatter:off
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .limitForPeriod(localRateLimitProperties.getRequestPerSecond())
                .timeoutDuration(Duration.ofMillis(0))
                .build();
        // @formatter:on

        RateLimiterRegistry rateLimiterRegistry = RateLimiterRegistry.of(config);

        this.rateLimiter = rateLimiterRegistry.rateLimiter("local-rate-limiter");
        this.localRateLimitProperties = localRateLimitProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!localRateLimitProperties.isEnabled()) {
            filterChain.doFilter(request, response);
            return;
        }
        
        if (rateLimiter.acquirePermission()) {
            filterChain.doFilter(request, response);
        } else {
            response.sendError(HttpStatus.TOO_MANY_REQUESTS.value());
            return;
        }
    }

}
