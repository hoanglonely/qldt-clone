package com.mb.lab.banks.auth.service.merchant;

import com.mb.lab.banks.auth.service.base.ActiveStatus;

public class MerchantDto {

    private Long id;
    private ActiveStatus activeStatus;
    private String name;
    private Long clientId;
    private String clientSecret;
    private String accessToken;
    private String merchantApiUrl;
    private String merchantAccessKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ActiveStatus getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(ActiveStatus activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getMerchantApiUrl() {
        return merchantApiUrl;
    }

    public void setMerchantApiUrl(String merchantApiUrl) {
        this.merchantApiUrl = merchantApiUrl;
    }

    public String getMerchantAccessKey() {
        return merchantAccessKey;
    }

    public void setMerchantAccessKey(String merchantAccessKey) {
        this.merchantAccessKey = merchantAccessKey;
    }

}
