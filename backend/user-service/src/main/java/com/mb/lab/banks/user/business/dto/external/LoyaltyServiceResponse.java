package com.mb.lab.banks.user.business.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoyaltyServiceResponse {

    private String code;
    private String message;
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    @JsonIgnore
    public boolean isSuccess() {
        return "000".equals(code);
    }

    @Override
    public String toString() {
        return "LoyaltyServiceResponse [code=" + code + ", message=" + message + "]";
    }
}
