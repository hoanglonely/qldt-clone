package com.mb.lab.banks.utils.storage;

public enum AwsSignatureVersion {

    SignatureV2("S3SignerType"), SignatureV4("AWSS3V4SignerType");

    private String value;

    private AwsSignatureVersion(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
