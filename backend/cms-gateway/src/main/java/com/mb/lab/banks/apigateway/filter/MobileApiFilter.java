package com.mb.lab.banks.apigateway.filter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
public class MobileApiFilter extends ZuulFilter {

    private static final String TOKEN_ENDPOINT_PATH_BEFORE = "/mobile-api/auth/token";
    private static final String TOKEN_ENDPOINT_PATH_AFTER = "/auth/oauth/token";
    private static final String AUTH_ENDPOINT_PATH_BEFORE = "/mobile-api/auth";
    private static final String AUTH_ENDPOINT_PATH_AFTER = "/auth/mobile-api";

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return ctx.getRequest().getRequestURI().startsWith("/mobile-api/") && ctx.sendZuulResponse();
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        if (context.getRequestQueryParams() == null || !context.getRequestQueryParams().containsKey("error-format")) {
            Map<String, List<String>> newQueryParams = new HashMap<>();
            if (context.getRequestQueryParams() != null) {
                newQueryParams.putAll(context.getRequestQueryParams());
            }
            newQueryParams.put("error-format", Collections.singletonList("mobile"));
            context.setRequestQueryParams(newQueryParams);
        }

        // Rewrite /mobile-api/auth/token -> /auth/oauth/token
        // Rewrite /mobile-api/auth/userinfo -> /auth/mobile-api/userinfo
        String originalRequestPath = (String) context.get(FilterConstants.REQUEST_URI_KEY);
        if (originalRequestPath.startsWith(TOKEN_ENDPOINT_PATH_BEFORE)) {
            String modifiedRequestPath = TOKEN_ENDPOINT_PATH_AFTER + originalRequestPath.substring(TOKEN_ENDPOINT_PATH_BEFORE.length());
            context.put(FilterConstants.REQUEST_URI_KEY, modifiedRequestPath);
        } else if (originalRequestPath.startsWith(AUTH_ENDPOINT_PATH_BEFORE)) {
            String modifiedRequestPath = AUTH_ENDPOINT_PATH_AFTER + originalRequestPath.substring(AUTH_ENDPOINT_PATH_BEFORE.length());
            context.put(FilterConstants.REQUEST_URI_KEY, modifiedRequestPath);
        }

        return null;
    }

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER + 1;
    }

}
