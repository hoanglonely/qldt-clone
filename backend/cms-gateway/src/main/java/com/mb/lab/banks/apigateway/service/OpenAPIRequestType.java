package com.mb.lab.banks.apigateway.service;

import java.net.URI;

public enum OpenAPIRequestType {

    PARTNER("/open-api/partner"), VOUCHER("/open-api/voucher"), VOUCHER_CODE("/open-api/code");

    public static OpenAPIRequestType from(URI uri) {
        String path = uri.getPath();
        for (OpenAPIRequestType type : values()) {
            if (path.startsWith(type.getPath()) && !path.endsWith("activate") && !path.endsWith("deactivate") && !path.endsWith("accumulate-point")) {
                return type;
            }
        }
        return null;
    }

    private String path;

    private OpenAPIRequestType(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
