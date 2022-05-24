package com.mb.lab.banks.utils.storage;

import java.net.URL;
import java.util.Date;

import org.springframework.util.Assert;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.HttpMethod;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.mb.lab.banks.utils.storage.AwsUrlCreator.AwsUrlCreatorBuilder.AwsUrlType;
import com.mb.lab.banks.utils.storage.StorageProperties.S3;

/**
 * @author Thanh
 */
public class AwsUrlCreator {

    public static AwsUrlCreator buildAwsUrlCreator(S3 s3, AWSCredentialsProvider awsCredentialsProvider, AmazonS3 amazonS3) {
        Assert.notNull(s3.getBucket(), "Bucket name is required");
        S3EndpointInfo endpointInfo = s3.getEndpoint();

        AmazonS3 presignAmazonS3 = amazonS3;

        // @formatter:off
        
        // If bucket policy not allow public read, we need to generate signed URL
        // If we use custom host name for access URL, we need to create another AmazonS3 to sign request to that host name
        if (s3.isPreSignedOutputUrl() && s3.getUrlType() != AwsUrlType.S3) {
            ClientConfiguration config = new ClientConfiguration();
            
            AmazonS3ClientBuilder builder = AmazonS3ClientBuilder
                    .standard()
                    .withCredentials(awsCredentialsProvider)
                    ;
            
            builder = builder.withEndpointConfiguration(new EndpointConfiguration(s3.getPreSignedHost(), endpointInfo.getRegion()));
            config.setProtocol(s3.isUseHttps() ? Protocol.HTTPS : Protocol.HTTP);
            config.setSignerOverride(endpointInfo.getSignatureVersion().getValue());
            
            builder.setPathStyleAccessEnabled(endpointInfo.isUsePathStyleAccess());
            builder = builder.withClientConfiguration(config);
            
            presignAmazonS3 = builder.build();
        }

        AwsUrlCreatorBuilder builder = new AwsUrlCreatorBuilder()
                .setBucketName(s3.getBucket())
                .useHttps(s3.isUseHttps())
                .setPreSignedOutputUrl(s3.isPreSignedOutputUrl())
                .setPreSignedUrlExpiration(s3.getPreSignedUrlExpiration())
                .setAmazonS3(presignAmazonS3)
                ;

        AwsUrlType type = s3.getUrlType();
        switch (type) {
            case CLOUD_FRONT:
                Assert.notNull(s3.getCloudfrontDomain(), "CloudFront domain name is required");
                builder.useCloudFrontUrl(s3.getCloudfrontDomain());
                break;
            case CNAME:
                Assert.notNull(s3.getCname(), "CNAME is required");
                builder.useCNAMEUrl(s3.getCname());
                break;
            case VT_ACCESS_KEY:
                Assert.notNull(awsCredentialsProvider.getCredentials().getAWSAccessKeyId(), "AccessKey is required");
                builder.useVtAccessKey(awsCredentialsProvider.getCredentials().getAWSAccessKeyId());
                break;
            default:
                builder.useS3Url();
                break;
        }

        // @formatter:on

        return builder.build();
    }

    private String urlPrefix;
    private boolean useS3Url;
    private boolean preSignedOutputUrl;
    private AmazonS3 amazonS3;
    private String bucketName;

    /**
     * If bucket not allow public read, how long URL should live before expire (in second). Default is 1 hour
     */
    private long preSignedUrlExpiration;

    private AwsUrlCreator(String urlPrefix, String bucketName, AmazonS3 amazonS3, boolean useS3Url, boolean preSignedOutputUrl, long preSignedUrlExpiration) {
        this.urlPrefix = urlPrefix;
        this.bucketName = bucketName;
        this.useS3Url = useS3Url;
        this.preSignedOutputUrl = preSignedOutputUrl;

        Assert.notNull(amazonS3, "An instance of 'amazonS3' need to provided");
        this.amazonS3 = amazonS3;

        if (preSignedOutputUrl) {
            this.preSignedUrlExpiration = preSignedUrlExpiration;
        }
    }

    public String getUrl(String fileId) {
        if (preSignedOutputUrl) {
            Date expiration = new Date();
            long msec = expiration.getTime();
            msec += preSignedUrlExpiration * 1000;
            expiration.setTime(msec);

            GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, fileId);
            generatePresignedUrlRequest.setMethod(HttpMethod.GET);
            generatePresignedUrlRequest.setExpiration(expiration);

            URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);

            if (this.useS3Url) {
                return url.toString();
            }

            return urlPrefix + fileId + "?" + url.getQuery();
        } else {
            if (this.useS3Url) {
                return amazonS3.getUrl(bucketName, fileId).toString();
            }

            return urlPrefix + fileId;
        }
    }

    public static class AwsUrlCreatorBuilder {

        public static enum AwsUrlType {
            S3, CLOUD_FRONT, CNAME, VT_ACCESS_KEY;
        }

        private boolean useHttps = false;
        private AwsUrlType type = AwsUrlType.S3;
        private String bucketName;
        private String cname;
        private String domainName;
        private String accessKey;
        private AmazonS3 amazonS3;
        private boolean preSignedOutputUrl;
        private long preSignedUrlExpiration;

        public AwsUrlCreatorBuilder setBucketName(String bucketName) {
            this.bucketName = bucketName;
            return this;
        }

        public AwsUrlCreatorBuilder useHttps(boolean useHttps) {
            this.useHttps = useHttps;
            return this;
        }

        public AwsUrlCreatorBuilder setPreSignedOutputUrl(boolean preSignedOutputUrl) {
            this.preSignedOutputUrl = preSignedOutputUrl;
            return this;
        }

        public AwsUrlCreatorBuilder setPreSignedUrlExpiration(long preSignedUrlExpiration) {
            this.preSignedUrlExpiration = preSignedUrlExpiration;
            return this;
        }

        public AwsUrlCreatorBuilder setAmazonS3(AmazonS3 amazonS3) {
            this.amazonS3 = amazonS3;
            return this;
        }

        public AwsUrlCreatorBuilder useS3Url() {
            type = AwsUrlType.S3;
            return this;
        }

        public AwsUrlCreatorBuilder useCloudFrontUrl(String domainName) {
            this.domainName = domainName;
            type = AwsUrlType.CLOUD_FRONT;
            return this;
        }

        public AwsUrlCreatorBuilder useCNAMEUrl(String cname) {
            this.cname = cname;
            type = AwsUrlType.CNAME;
            return this;
        }

        public AwsUrlCreatorBuilder useVtAccessKey(String accessKey) {
            this.accessKey = accessKey;
            type = AwsUrlType.VT_ACCESS_KEY;
            return this;
        }

        public AwsUrlCreator build() {
            StringBuilder prefix = new StringBuilder(useHttps ? "https://" : "http://");
            switch (type) {
                case CLOUD_FRONT:
                    prefix.append(domainName);
                    break;
                case CNAME:
                    prefix.append(cname);
                    break;
                case VT_ACCESS_KEY:
                    prefix.append(accessKey).append(".cloudstorage.com.vn").append("/").append(bucketName);
                    break;
                case S3:
                    prefix.append(bucketName).append(".s3-" + bucketName + ".amazonaws.com");
                    break;
            }
            prefix.append("/");

            return new AwsUrlCreator(prefix.toString(), bucketName, amazonS3, type == AwsUrlType.S3, preSignedOutputUrl, preSignedUrlExpiration);
        }

    }

}
