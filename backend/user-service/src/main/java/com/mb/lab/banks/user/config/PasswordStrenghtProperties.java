package com.mb.lab.banks.user.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("password")
public class PasswordStrenghtProperties {

    private List<String> weakPassword;

    public List<String> getWeakPassword() {
        return weakPassword;
    }

    public void setWeakPassword(List<String> weakPassword) {
        this.weakPassword = weakPassword;
    }

}
