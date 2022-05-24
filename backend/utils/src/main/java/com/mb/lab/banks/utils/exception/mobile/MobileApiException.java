package com.mb.lab.banks.utils.exception.mobile;

public class MobileApiException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private int code;

    public MobileApiException(int code, String message) {
        super(message);
        this.code = code;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    public int getCode() {
        return code;
    }

}
