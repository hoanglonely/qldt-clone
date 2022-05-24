package com.mb.lab.banks.apigateway.utils;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.loadbalancer.PingUrl;
import com.netflix.loadbalancer.Server;

public class MinIOPingUrl extends PingUrl {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public MinIOPingUrl() {
        super(false, "/minio/health/ready");
    }

    @Override
    public boolean isAlive(Server server) {
        String urlStr = "";
        if (isSecure()) {
            urlStr = "https://";
        } else {
            urlStr = "http://";
        }
        urlStr += server.getId();
        urlStr += getPingAppendString();

        boolean isAlive = false;

        // Very quick ping
        RequestConfig config = RequestConfig.custom().setConnectTimeout(100).setConnectionRequestTimeout(100).setSocketTimeout(100).build();
        CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();

        HttpUriRequest getRequest = new HttpGet(urlStr);
        String content = null;
        try {
            HttpResponse response = client.execute(getRequest);
            content = EntityUtils.toString(response.getEntity());
            isAlive = (response.getStatusLine().getStatusCode() == 200);
            if (getExpectedContent() != null) {
                logger.debug("content:" + content);
                if (content == null) {
                    isAlive = false;
                } else {
                    if (content.equals(getExpectedContent())) {
                        isAlive = true;
                    } else {
                        isAlive = false;
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Fail to ping storage service", e);
        } finally {
            // Release the connection.
            getRequest.abort();
        }

        return isAlive;
    }
}
