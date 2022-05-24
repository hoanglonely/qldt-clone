package com.mb.lab.banks.utils.exception;

/**
 * The Class BussinessException.
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String code;

    public BusinessException() {
    }

    public BusinessException(String code) {
        this(code, null);
    }

    public BusinessException(String code, String message) {
        this(code, message, null);
    }

    public BusinessException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
