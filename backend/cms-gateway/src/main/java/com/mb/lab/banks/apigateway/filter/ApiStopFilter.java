package com.mb.lab.banks.apigateway.filter;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import com.mb.lab.banks.apigateway.service.ApiStopCacheSubService;
import com.mb.lab.banks.utils.rest.ResponseEntityRenderer;
import com.mb.lab.banks.utils.rest.RestError;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE - 2)
public class ApiStopFilter extends OncePerRequestFilter {

    @Autowired
    private ApiStopCacheSubService apiStopCacheSubService;

    private final RequestMatcher requestMatcher = new OrRequestMatcher(
            Arrays.asList(new AntPathRequestMatcher("/open-api/**"), new AntPathRequestMatcher("/mobile-api/**")));

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !requestMatcher.matches(request);
    }

    private ResponseEntityRenderer responseEntityRenderer = new ResponseEntityRenderer();

    // This is from Spring MVC.
    private HandlerExceptionResolver handlerExceptionResolver = new DefaultHandlerExceptionResolver();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (apiStopCacheSubService.isApiStop()) {
            try {
                if (request.getRequestURI().startsWith("/mobile-api/")) {
                    MobileApiErrorResponse error = MobileApiErrorResponse.INTERNAL_SERVER_ERROR;
                    responseEntityRenderer.handleHttpEntityResponse(new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE),
                            new ServletWebRequest(request, response));
                } else {
                    RestError error = new RestError("unavailable", 503, "H\u1ec7 th\u1ed1ng \u0111ang n\u00e2ng c\u1ea5p");
                    responseEntityRenderer.handleHttpEntityResponse(new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE),
                            new ServletWebRequest(request, response));
                }
                response.flushBuffer();
            } catch (ServletException e) {
                // Re-use some of the default Spring dispatcher behaviour - the exception came from the filter chain and
                // not from an MVC handler so it won't be caught by the dispatcher (even if there is one)
                if (handlerExceptionResolver.resolveException(request, response, this, e) == null) {
                    throw e;
                }
            } catch (IOException e) {
                throw e;
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                // Wrap other Exceptions. These are not expected to happen
                throw new RuntimeException(e);
            }
            return;
        }

        filterChain.doFilter(request, response);
    }

    public static class MobileApiErrorResponse {

        public static final MobileApiErrorResponse INTERNAL_SERVER_ERROR = new MobileApiErrorResponse(2000, "Unexpected error");

        private int errorCode;
        private String message;
        private Object data;

        public MobileApiErrorResponse(int code, String message) {
            this.errorCode = code;
            this.message = message;
        }

        public int getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }

    public static class ErrorResponse {

        private Integer code;
        private String message;

        public ErrorResponse() {
            this.code = 2;
            this.message = "H\u1ec7 th\u1ed1ng \u0111ang n\u00e2ng c\u1ea5p";
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }
}
