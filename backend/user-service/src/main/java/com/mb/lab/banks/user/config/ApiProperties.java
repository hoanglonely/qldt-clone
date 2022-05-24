package com.mb.lab.banks.user.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("api")
public class ApiProperties {

    private ViettelId viettelId = new ViettelId();

    public ViettelId getViettelId() {
        return viettelId;
    }

    public void setViettelId(ViettelId viettelId) {
        this.viettelId = viettelId;
    }

    public static class ViettelId {

        private String username;
        private String password;

        /**
         * Set the underlying URLConnection's connect timeout (in milliseconds). A timeout value of 0 specifies an infinite timeout.
         */
        private int connectTimeout = 10000;
        /**
         * Set the underlying URLConnection's read timeout (in milliseconds). A timeout value of 0 specifies an infinite timeout.
         */
        private int readTimeout = 10000;

        private RewardService rewardService = new RewardService();

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getConnectTimeout() {
            return connectTimeout;
        }

        public void setConnectTimeout(int connectTimeout) {
            this.connectTimeout = connectTimeout;
        }

        public int getReadTimeout() {
            return readTimeout;
        }

        public void setReadTimeout(int readTimeout) {
            this.readTimeout = readTimeout;
        }

        public RewardService getRewardService() {
            return rewardService;
        }

        public void setRewardService(RewardService rewardService) {
            this.rewardService = rewardService;
        }
    }

    public static class RewardService {

        private String baseUrl;
        private boolean retryFailedSms = true;

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public boolean isRetryFailedSms() {
            return retryFailedSms;
        }

        public void setRetryFailedSms(boolean retryFailedSms) {
            this.retryFailedSms = retryFailedSms;
        }
    }
}
