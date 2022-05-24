package com.mb.lab.banks.apigateway.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.mb.lab.banks.apigateway.service.WriteOpenAPIRequestLogService;
import com.mb.lab.banks.apigateway.service.WriteOpenAPIRequestLogService.WriteLogData;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE - 1)
public class OpenApiLoggingFilter extends OncePerRequestFilter {

    private static final String ATTRIBUTE_START_TIME = "QLDT_START_TIME";
    private static final String DEFAULT_RESPONSE_CHARSET = "UTF8";

    // @formatter:off
    private static final List<MediaType> HUMAN_READABLE_TYPES = Arrays.asList(
            MediaType.APPLICATION_JSON,
            MediaType.APPLICATION_FORM_URLENCODED,
            MediaType.TEXT_PLAIN,
            MediaType.TEXT_HTML,
            MediaType.TEXT_XML,
            MediaType.APPLICATION_XML,
            MediaType.MULTIPART_FORM_DATA
    );
    // @formatter:on

    // @formatter:off
    private static final String[] REMOTE_IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };
    // @formatter:on

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final AntPathRequestMatcher requestMatcher = new AntPathRequestMatcher("/open-api/**");

    private final Map<String, Boolean> mapMediaTypeCheck = new ConcurrentHashMap<>();

    @Autowired
    private WriteOpenAPIRequestLogService writeOpenAPIRequestLogService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !requestMatcher.matches(request);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Skip async request
        if (isAsyncDispatch(request)) {
            filterChain.doFilter(request, response);
        } else {
            doFilterWrapped(wrapRequest(request), wrapResponse(response), filterChain, request);
        }
    }

    protected void doFilterWrapped(ContentCachingRequestWrapper request,
            ContentCachingResponseWrapper response,
            FilterChain filterChain,
            HttpServletRequest httpRequest) throws ServletException,
            IOException {
        try {
            beforeRequest(request, response);
            filterChain.doFilter(request, response);
        } finally {
            afterRequest(request, response, httpRequest);
            response.copyBodyToResponse();
        }
    }

    protected void beforeRequest(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
        setStartTime(request);
    }

    protected void afterRequest(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, HttpServletRequest httpRequest) {
        Authentication authentication = (Authentication) request.getAttribute(StoreAuthenticationToAttributeFilter.ATTRIBUTE_AUTHENTICATION);
        Date startTime = getStartTime(request);
        long duration = new Date().getTime() - startTime.getTime();
        String url = getRequestedUrl(request);
        WriteLogData data = new WriteLogData();
        data.setUrl(url);
        data.setRequestedTime(startTime);
        data.setDuration(duration);
        if (authentication instanceof OAuth2Authentication) {
            OAuth2Authentication oauth2Auth = (OAuth2Authentication) authentication;
            String clientId = oauth2Auth.getOAuth2Request().getClientId();
            try {
                data.setClientId(Long.valueOf(clientId));
            } catch (NumberFormatException e) {
                logger.debug("Fail to convert clientId to Long value", e);
            }
        }
        
        data.setRequestContentType(request.getContentType());
        data.setRequestContentEncoding(request.getCharacterEncoding());
        data.setRequestContent(getContent(request.getContentAsByteArray(), request.getContentType(), request.getCharacterEncoding()));
        data.setResponseCode(response.getStatus());
        
        data.setResponseContentType(response.getContentType());
        data.setResponseContentEncoding(DEFAULT_RESPONSE_CHARSET);
        data.setResponseContent(getContent(response.getContentAsByteArray(), response.getContentType(), DEFAULT_RESPONSE_CHARSET));
        data.setUri(request.getRequestURI());
        data.setRemoteIp(getClientIpAddress(httpRequest));
        writeOpenAPIRequestLogService.writeRequestLog(data);
    }

    private String getContent(byte[] content, String contentType, String contentEncoding) {
        if (content == null || content.length == 0) {
            return null;
        }
        if (isContentTypeHumanReadable(contentType)) {
            try {
                return new String(content, contentEncoding);
            } catch (UnsupportedEncodingException e) {
                logger.error("Encoding {} is not supported", contentEncoding);
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Content type {} is not human readable", contentType);
        }
        return null;
    }

    private boolean isContentTypeHumanReadable(String contentType) {
        if (mapMediaTypeCheck.containsKey(contentType)) {
            return mapMediaTypeCheck.get(contentType);
        }
        MediaType mediaType = MediaType.valueOf(contentType);
        boolean humanReadable = HUMAN_READABLE_TYPES.stream().anyMatch(visibleType -> visibleType.includes(mediaType));
        mapMediaTypeCheck.put(contentType, humanReadable);
        return humanReadable;
    }

    private void setStartTime(HttpServletRequest request) {
        request.setAttribute(ATTRIBUTE_START_TIME, new Date());
    }

    private Date getStartTime(HttpServletRequest request) {
        return (Date) request.getAttribute(ATTRIBUTE_START_TIME);
    }

    public static String getClientIpAddress(HttpServletRequest request) {
        for (String header : REMOTE_IP_HEADER_CANDIDATES) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }

        return request.getRemoteAddr();
    }

    private String getRequestedUrl(HttpServletRequest request) {
        StringBuffer requestURL = request.getRequestURL();
        if (request.getQueryString() != null) {
            requestURL.append("?").append(request.getQueryString());
        }
        return requestURL.toString();
    }

    private ContentCachingRequestWrapper wrapRequest(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            return (ContentCachingRequestWrapper) request;
        } else {
            return new ContentCachingRequestWrapper(request);
        }
    }

    private ContentCachingResponseWrapper wrapResponse(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper) {
            return (ContentCachingResponseWrapper) response;
        } else {
            return new ContentCachingResponseWrapper(response);
        }
    }
}
