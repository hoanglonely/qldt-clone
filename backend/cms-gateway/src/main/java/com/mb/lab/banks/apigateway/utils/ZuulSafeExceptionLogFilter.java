package com.mb.lab.banks.apigateway.utils;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;

import org.slf4j.Marker;
import org.springframework.cloud.netflix.zuul.filters.post.SendErrorFilter;
import org.springframework.http.HttpStatus;

import com.netflix.zuul.FilterProcessor;
import com.netflix.zuul.exception.ZuulException;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.spi.FilterReply;

public class ZuulSafeExceptionLogFilter extends TurboFilter {

    private static final String SEND_ERROR_FILTER_CLASS_NAME = SendErrorFilter.class.getName();
    private static final String FILTER_PROCESSOR_CLASS_NAME = FilterProcessor.class.getName();

    @Override
    public FilterReply decide(Marker marker, Logger logger, Level level, String format, Object[] params, Throwable throwable) {
        if (logger.getName().equals(SEND_ERROR_FILTER_CLASS_NAME)) {
            if (throwable == null || !(throwable instanceof ZuulException)) {
                return FilterReply.NEUTRAL;
            }

            ZuulException exception = (ZuulException) throwable;
            if (exception.nStatusCode == HttpStatus.GATEWAY_TIMEOUT.value()) {
                logger.info("Gateway timeout has been occurs");
                return FilterReply.DENY;
            }

            if (exception.getCause() instanceof UndeclaredThrowableException) {
                UndeclaredThrowableException undeclaredEx = (UndeclaredThrowableException) exception.getCause();
                Throwable innerEx = undeclaredEx.getUndeclaredThrowable();

                if (innerEx != null && innerEx instanceof IOException) {
                    if (innerEx.getMessage().contains("Connection reset by peer")) {
                        logger.info("Connection reset by peer has been occurs");
                        return FilterReply.DENY;
                    }

                    if (innerEx.getMessage().contains("Broken pipe")) {
                        logger.info("Broken pipe has been occurs");
                        return FilterReply.DENY;
                    }
                }
            }

        } else if (logger.getName().equals(FILTER_PROCESSOR_CLASS_NAME)) {
            if (throwable == null || !(throwable instanceof ZuulException)) {
                return FilterReply.NEUTRAL;
            }

            ZuulException exception = (ZuulException) throwable;
            if (exception.getCause() == null || !(exception.getCause() instanceof UndeclaredThrowableException)) {
                return FilterReply.NEUTRAL;
            }

            UndeclaredThrowableException undeclaredEx = (UndeclaredThrowableException) exception.getCause();
            Throwable innerEx = undeclaredEx.getUndeclaredThrowable();
            if (innerEx == null || !(innerEx instanceof IOException)) {
                return FilterReply.NEUTRAL;
            }

            if (innerEx.getMessage().contains("Broken pipe")) {
                logger.info("Broken pipe has been occurs");
                return FilterReply.DENY;
            }
        }

        return FilterReply.NEUTRAL;
    }

}
