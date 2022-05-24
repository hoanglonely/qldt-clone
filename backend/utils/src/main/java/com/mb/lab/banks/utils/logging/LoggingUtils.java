package com.mb.lab.banks.utils.logging;

import java.util.Date;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.slf4j.event.Level;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mb.lab.banks.utils.event.BusinessLogEvent;
import com.mb.lab.banks.utils.event.ErrorLogEvent;
import com.mb.lab.banks.utils.event.stream.BusinessLogStreams;
import com.mb.lab.banks.utils.event.stream.ErrorLogStreams;
import com.mb.lab.banks.utils.event.stream.EventStreamsHelper;

public class LoggingUtils {

    public static final String MARKER_ERROR = "error";
    public static final String MARKER_START_ACTION = "start_action";
    public static final String MARKER_END_ACTION = "end_action";
    public static final String MARKER_START_CONNECT = "start_connect";
    public static final String MARKER_END_CONNECT = "end_connect";

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingUtils.class);
    private static final ObjectMapper OBJECT_MAPPER = new CensorObjectMapper();
    private static final EventStreamsHelper EVENT_STREAM_HELPER = new EventStreamsHelper();

    public static void error(Logger logger,
            ErrorLogStreams.OutBound errorLogStreamsOutBound,
            Level level,
            String errorCode,
            String message,
            Throwable throwable) {
        Marker marker = MarkerFactory.getMarker(MARKER_ERROR);

        MDC.put("errorCode", errorCode);

        try {
            if (level.equals(Level.DEBUG)) {
                if (logger.isDebugEnabled()) {
                    logger.debug(marker, message, throwable);
                }
            } else if (level.equals(Level.INFO)) {
                if (logger.isInfoEnabled()) {
                    logger.info(marker, message, throwable);
                }
            } else if (level.equals(Level.WARN)) {
                if (logger.isWarnEnabled()) {
                    logger.warn(marker, message, throwable);
                }
            } else if (level.equals(Level.ERROR)) {
                if (logger.isErrorEnabled()) {
                    logger.error(marker, message, throwable);
                }
            } else {
                throw new UnsupportedOperationException("Business log does not allow logging at level '" + level + "'");
            }
            
            ErrorLogEvent errorLogEvent = new ErrorLogEvent();
            errorLogEvent.setLogType("error");
            errorLogEvent.setEventDate(new Date());
            errorLogEvent.setClazzName(logger.getName());
            if (throwable != null) {
                if (level.equals(Level.ERROR)) {
                    errorLogEvent.setDescription(message + "\n" + ExceptionUtils.getStackTrace(throwable));
                } else {
                    errorLogEvent.setDescription(message + "\n" + throwable.getMessage());
                }
            } else {
                errorLogEvent.setDescription(message);
            }
            errorLogEvent.setErrorCode(errorCode);

            EVENT_STREAM_HELPER.sendEvent(errorLogStreamsOutBound.channel(), errorLogEvent);
        } finally {
            MDC.put("errorCode", null);
        }
    }

    public static void business(Logger logger,
            BusinessLogStreams.OutBound businessLogStreamsOutBound,
            Level level,
            String markerName,
            String functionClass,
            String functionName,
            Object functionArgs,
            Long duration,
            boolean forceLogArgs,
            String message) {
        Marker marker = MarkerFactory.getMarker(markerName);
        boolean logArgs = forceLogArgs || logger.isDebugEnabled(marker);
        String functionArgsString = logArgs ? convertArgsToString(functionArgs) : null;

        MDC.put("functionName", functionName);
        MDC.put("functionArgs", functionArgsString);
        MDC.put("functionClass", functionClass);
        MDC.put("duration", duration == null ? null : String.valueOf(duration));

        try {
            if (level.equals(Level.DEBUG)) {
                if (logger.isDebugEnabled()) {
                    logger.debug(marker, message);
                }
            } else if (level.equals(Level.INFO)) {
                if (logger.isInfoEnabled()) {
                    logger.info(marker, message);
                }
            } else {
                throw new UnsupportedOperationException("Business log does not allow logging at level '" + level + "'");
            }

            BusinessLogEvent businessLogEvent = new BusinessLogEvent();
            businessLogEvent.setLogType(markerName);
            businessLogEvent.setAppCode("QLDT");
            businessLogEvent.setStartTime(new Date());
            businessLogEvent.setUsername("MyViettel");
            businessLogEvent.setIpAddress(MDC.get("remoteIp"));
            businessLogEvent.setPath(MDC.get("requestedPath"));
            businessLogEvent.setFuncName(functionName);
            businessLogEvent.setFuncArgs(functionArgsString);
            businessLogEvent.setFuncClass(functionClass);
            businessLogEvent.setDuration(duration);
            businessLogEvent.setDescription(message);

            EVENT_STREAM_HELPER.sendEvent(businessLogStreamsOutBound.channel(), businessLogEvent);
        } finally {
            MDC.put("functionName", null);
            MDC.put("functionArgs", null);
            MDC.put("functionClass", null);
            MDC.put("duration", null);
        }
    }

    private static String convertArgsToString(Object args) {
        if (args == null) {
            return null;
        }

        try {
            return OBJECT_MAPPER.writeValueAsString(args);
        } catch (JsonProcessingException e) {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("Fail to convert args to JSON string", e);
            }
            return "error";
        }
    }
    
}
