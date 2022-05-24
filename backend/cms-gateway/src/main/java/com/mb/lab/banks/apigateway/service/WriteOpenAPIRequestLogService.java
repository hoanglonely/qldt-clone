package com.mb.lab.banks.apigateway.service;

import java.net.URI;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.mb.lab.banks.utils.event.OpenAPIRequestLogEvent;
import com.mb.lab.banks.utils.event.stream.EventStreamsHelper;
import com.mb.lab.banks.utils.event.stream.OpenAPIRequestLogStreams;

@Service
public class WriteOpenAPIRequestLogService {

    @Autowired
    private EventStreamsHelper eventStreamsHelper;

    @Autowired
    private OpenAPIRequestLogStreams.OutBound openAPIRequestLogStreamsOutBound;
    
    private final Map<String, TypeCheckValue> mapTypeCheck = new ConcurrentHashMap<>();

    @Async
    public void writeRequestLog(WriteLogData data) {
        OpenAPIRequestLogEvent event = new OpenAPIRequestLogEvent();
        
        OpenAPIRequestType type = getRequestType(data.getUri());
        event.setSystemName("QLDT");
        event.setDuration(data.getDuration());
        event.setRequestedTime(data.getRequestedTime());
        event.setUrl(data.getUrl());
        event.setClientId(data.getClientId());
        event.setRemoteIp(data.getRemoteIp());
        event.setRequest(data.getRequestContent());
        event.setResponse(data.getResponseContent());
        event.setStatusCode(data.getResponseCode());
        event.setType(type != null ? type.toString() : null);
        
        eventStreamsHelper.sendEvent(openAPIRequestLogStreamsOutBound.channel(), event);
    }
    
    private OpenAPIRequestType getRequestType(String uriString) {
        URI uri = URI.create(uriString);
        String path = uri.getPath();
        if (mapTypeCheck.containsKey(path)) {
            TypeCheckValue typeCheckValue = mapTypeCheck.get(path);
            return typeCheckValue.getType();
        }
        OpenAPIRequestType type = OpenAPIRequestType.from(uri);
        mapTypeCheck.put(path, new TypeCheckValue(type));
        return type;
    }
    
    private static class TypeCheckValue {

        private OpenAPIRequestType type;

        private TypeCheckValue(OpenAPIRequestType requestType) {
            this.type = requestType;
        }

        public OpenAPIRequestType getType() {
            return type;
        }
    }
    
    public static class WriteLogData {

        private String url;
        private Date requestedTime;
        private long duration;
        
        private String requestContentType;
        private String requestContentEncoding;
        private String requestContent;
        
        private String responseContentType;
        private String responseContentEncoding;
        private String responseContent;
        private int responseCode;
        private String remoteIp;
        private Long clientId;
        private String uri;

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public Date getRequestedTime() {
            return requestedTime;
        }

        public void setRequestedTime(Date requestedTime) {
            this.requestedTime = requestedTime;
        }

        public long getDuration() {
            return duration;
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }

        public String getRequestContent() {
            return requestContent;
        }

        public void setRequestContent(String requestContent) {
            this.requestContent = requestContent;
        }

        public String getResponseContent() {
            return responseContent;
        }

        public void setResponseContent(String responseContent) {
            this.responseContent = responseContent;
        }
 
        public int getResponseCode() {
            return responseCode;
        }

        public void setResponseCode(int responseCode) {
            this.responseCode = responseCode;
        }

        public String getRemoteIp() {
            return remoteIp;
        }

        public void setRemoteIp(String remoteIp) {
            this.remoteIp = remoteIp;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Long getClientId() {
            return clientId;
        }

        public void setClientId(Long clientId) {
            this.clientId = clientId;
        }

        public String getRequestContentType() {
            return requestContentType;
        }

        public void setRequestContentType(String requestContentType) {
            this.requestContentType = requestContentType;
        }

        public String getRequestContentEncoding() {
            return requestContentEncoding;
        }

        public void setRequestContentEncoding(String requestContentEncoding) {
            this.requestContentEncoding = requestContentEncoding;
        }

        public String getResponseContentType() {
            return responseContentType;
        }

        public void setResponseContentType(String responseContentType) {
            this.responseContentType = responseContentType;
        }

        public String getResponseContentEncoding() {
            return responseContentEncoding;
        }

        public void setResponseContentEncoding(String responseContentEncoding) {
            this.responseContentEncoding = responseContentEncoding;
        }
        
    }

}
