package com.mb.lab.banks.utils.storage;

public class S3EndpointInfo {

    private String region;
    private AwsSignatureVersion signatureVersion = AwsSignatureVersion.SignatureV4;
    private boolean useCustomEndpoint;
    private String customEndpoint;
    private boolean usePathStyleAccess;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public AwsSignatureVersion getSignatureVersion() {
        return signatureVersion;
    }

    public void setSignatureVersion(AwsSignatureVersion signatureVersion) {
        this.signatureVersion = signatureVersion;
    }

    public boolean isUseCustomEndpoint() {
        return useCustomEndpoint;
    }

    public void setUseCustomEndpoint(boolean useCustomEndpoint) {
        this.useCustomEndpoint = useCustomEndpoint;
    }

    public String getCustomEndpoint() {
        return customEndpoint;
    }

    public void setCustomEndpoint(String customEndpoint) {
        this.customEndpoint = customEndpoint;
    }

    public boolean isUsePathStyleAccess() {
        return usePathStyleAccess;
    }

    public void setUsePathStyleAccess(boolean usePathStyleAccess) {
        this.usePathStyleAccess = usePathStyleAccess;
    }

}
