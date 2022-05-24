package com.mb.lab.banks.utils.exception.mobile;

import java.util.Collection;
import java.util.Map;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public class MobileApiAssert {

    public static final int INVALID_PARAM_ERROR_CODE = 2002;
    public static final String INVALID_PARAM_ERROR_MESSAGE = "Invalid request parameter";

    public static void isTrue(boolean expression, int code, String message) {
        if (!expression) {
            throw new MobileApiException(code, message);
        }
    }

    public static void isTrue(boolean expression, int code) {
        isTrue(expression, code, null);
    }

    public static void isTrue(boolean expression, String message) {
        isTrue(expression, INVALID_PARAM_ERROR_CODE, message);
    }

    public static void isTrue(boolean expression) {
        isTrue(expression, INVALID_PARAM_ERROR_MESSAGE);
    }

    public static void notTrue(boolean expression, int code, String message) {
        if (expression) {
            throw new MobileApiException(code, message);
        }
    }

    public static void notTrue(boolean expression, int code) {
        notTrue(expression, code, null);
    }

    public static void notTrue(boolean expression, String message) {
        notTrue(expression, INVALID_PARAM_ERROR_CODE, message);
    }

    public static void notTrue(boolean expression) {
        notTrue(expression, INVALID_PARAM_ERROR_MESSAGE);
    }

    public static void notNull(Object object, int code, String message) {
        if (object == null) {
            throw new MobileApiException(code, message);
        }
    }

    public static void notNull(Object object, int code) {
        notNull(object, code, null);
    }

    public static void notNull(Object object, String message) {
        notNull(object, INVALID_PARAM_ERROR_CODE, message);
    }

    public static void notNull(Object object) {
        notNull(object, INVALID_PARAM_ERROR_MESSAGE);
    }

    public static void notEmptyText(Object object, int code, String message) {
        isTrue(!StringUtils.isEmpty(object), code, message);
    }

    public static void notEmptyText(Object object, String message) {
        notEmptyText(object, INVALID_PARAM_ERROR_CODE, message);
    }

    public static void notEmptyText(Object object) {
        notEmptyText(object, INVALID_PARAM_ERROR_MESSAGE);
    }

    public static void notEmpty(Collection<?> collection, int code, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new MobileApiException(code, message);
        }
    }

    public static void notEmpty(Collection<?> collection, int code) {
        notEmpty(collection, code, null);
    }

    public static void notEmpty(Collection<?> collection, String message) {
        notEmpty(collection, INVALID_PARAM_ERROR_CODE, message);
    }

    public static void notEmpty(Collection<?> collection) {
        notEmpty(collection, INVALID_PARAM_ERROR_MESSAGE);
    }

    public static void notEmpty(Map<?, ?> map, int code, String message) {
        if (CollectionUtils.isEmpty(map)) {
            throw new MobileApiException(code, message);
        }
    }

    public static void notEmpty(Map<?, ?> map, String message) {
        notEmpty(map, INVALID_PARAM_ERROR_CODE, message);
    }

    public static void notEmpty(Map<?, ?> map) {
        notEmpty(map, INVALID_PARAM_ERROR_MESSAGE);
    }

    public static void contain(Collection<?> collection, Object object, int code, String message) {
        if (collection == null || collection.isEmpty() || object == null || !collection.contains(object)) {
            throw new MobileApiException(code, message);
        }
    }

    public static void contain(Collection<?> collection, Object object, String message) {
        contain(collection, object, INVALID_PARAM_ERROR_CODE, message);
    }

    public static void contain(Collection<?> collection, Object object) {
        contain(collection, object, INVALID_PARAM_ERROR_MESSAGE);
    }

    public static void equals(Object expected, Object actual, int code, String message) {
        if (expected == null && actual == null)
            return;
        if (expected != null && expected.equals(actual))
            return;
        throw new MobileApiException(code, message);
    }

    public static void equals(Object expected, Object actual, String message) {
        equals(expected, actual, INVALID_PARAM_ERROR_CODE, message);
    }

    public static void equals(Object expected, Object actual) {
        equals(expected, actual, INVALID_PARAM_ERROR_MESSAGE);
    }

    public static void equals(String expected, String actual, int code, String message) {
        if (expected == null && actual == null)
            return;
        if (expected != null && expected.equals(actual))
            return;
        throw new MobileApiException(code, message);
    }

    public static void equals(String expected, String actual, String message) {
        equals(expected, actual, INVALID_PARAM_ERROR_CODE, message);
    }

    public static void equals(String expected, String actual) {
        equals(expected, actual, INVALID_PARAM_ERROR_MESSAGE);
    }

    public static void throwHere() {
        throw new MobileApiException(INVALID_PARAM_ERROR_CODE, INVALID_PARAM_ERROR_MESSAGE);
    }

    public static void throwHere(int code, String message) {
        throw new MobileApiException(code, message);
    }

}
