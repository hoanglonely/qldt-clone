package com.mb.lab.banks.utils.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.mb.lab.banks.utils.storage.AwsUrlCreator.AwsUrlCreatorBuilder.AwsUrlType;

@ConfigurationProperties("storage")
public class StorageProperties {

    private StorageType type;

    private File file = new File();

    private S3 s3;

    public StorageType getType() {
        return type;
    }

    public void setType(StorageType type) {
        this.type = type;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public S3 getS3() {
        return s3;
    }

    public void setS3(S3 s3) {
        this.s3 = s3;
    }

    public static enum StorageType {
        FILE, S3;
    }

    public static class File {

        private String rootPath;
        private String rootUrl;
        private boolean createMapping;

        public String getRootPath() {
            return rootPath;
        }

        public void setRootPath(String rootPath) {
            this.rootPath = rootPath;
        }

        public String getRootUrl() {
            return rootUrl;
        }

        public void setRootUrl(String rootUrl) {
            this.rootUrl = rootUrl;
        }

        public boolean isCreateMapping() {
            return createMapping;
        }

        public void setCreateMapping(boolean createMapping) {
            this.createMapping = createMapping;
        }

    }

    public static class S3 {

        private AwsCredentialsInfo credentials;
        private S3EndpointInfo endpoint;
        private String bucket;
        private boolean useHttps = false;
        private AwsUrlType urlType = AwsUrlType.S3;
        private String cname;
        private String cloudfrontDomain;
        private boolean grantPublicPermission = false;
        private boolean preSignedOutputUrl = false;
        private String preSignedHost;

        /**
         * If bucket not allow public read, how long URL should live before expire (in second). Default is 1 hour
         */
        private long preSignedUrlExpiration = 60 * 60;

        public AwsCredentialsInfo getCredentials() {
            return credentials;
        }

        public void setCredentials(AwsCredentialsInfo credentials) {
            this.credentials = credentials;
        }

        public S3EndpointInfo getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(S3EndpointInfo endpoint) {
            this.endpoint = endpoint;
        }

        public String getBucket() {
            return bucket;
        }

        public void setBucket(String bucket) {
            this.bucket = bucket;
        }

        public boolean isUseHttps() {
            return useHttps;
        }

        public void setUseHttps(boolean useHttps) {
            this.useHttps = useHttps;
        }

        public AwsUrlType getUrlType() {
            return urlType;
        }

        public void setUrlType(AwsUrlType urlType) {
            this.urlType = urlType;
        }

        public String getCname() {
            return cname;
        }

        public void setCname(String cname) {
            this.cname = cname;
        }

        public String getCloudfrontDomain() {
            return cloudfrontDomain;
        }

        public void setCloudfrontDomain(String cloudfrontDomain) {
            this.cloudfrontDomain = cloudfrontDomain;
        }

        public boolean isGrantPublicPermission() {
            return grantPublicPermission;
        }

        public void setGrantPublicPermission(boolean grantPublicPermission) {
            this.grantPublicPermission = grantPublicPermission;
        }

        public boolean isPreSignedOutputUrl() {
            return preSignedOutputUrl;
        }

        public void setPreSignedOutputUrl(boolean preSignedOutputUrl) {
            this.preSignedOutputUrl = preSignedOutputUrl;
        }

        public long getPreSignedUrlExpiration() {
            return preSignedUrlExpiration;
        }

        public void setPreSignedUrlExpiration(long preSignedUrlExpiration) {
            this.preSignedUrlExpiration = preSignedUrlExpiration;
        }

        public String getPreSignedHost() {
            return preSignedHost;
        }

        public void setPreSignedHost(String preSignedHost) {
            this.preSignedHost = preSignedHost;
        }

    }

}
