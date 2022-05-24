package com.mb.lab.banks.utils.rest.mobile;

public class MobileApiResponse {
    
    public static final MobileApiResponse SUCCESS = new MobileApiResponse(0, "Success");
    public static final MobileApiResponse INTERNAL_SERVER_ERROR = new MobileApiResponse(2000, "Unexpected error");
    public static final MobileApiResponse INVALID_REQUEST = new MobileApiResponse(2001, "Invalid request");
    public static final MobileApiResponse INVALID_PARAM = new MobileApiResponse(2002, "Invalid request parameter");
    public static final MobileApiResponse NOT_FOUND = new MobileApiResponse(2003, "Requested resource not found");

    private int errorCode;
    private String message;
    private Object data;

    public MobileApiResponse() {
        super();
    }
    
    public MobileApiResponse(Object data) {
        this.errorCode = 0;
        this.message = "Success";
        this.data = data;
    }

    public MobileApiResponse(int code, String message) {
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
