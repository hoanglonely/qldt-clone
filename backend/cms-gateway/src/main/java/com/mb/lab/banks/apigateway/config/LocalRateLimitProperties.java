package com.mb.lab.banks.apigateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("local-rate-limit")
public class LocalRateLimitProperties {
    
    private boolean enabled = true;
    private int requestPerSecond = 10000;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getRequestPerSecond() {
        return requestPerSecond;
    }

    public void setRequestPerSecond(int requestPerSecond) {
        this.requestPerSecond = requestPerSecond;
    }
}
