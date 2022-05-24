package com.mb.lab.banks.utils.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author thanh
 */
@JsonPropertyOrder(value = { "error_type", "code", "error_message" })
public class RestError extends Meta {

    @JsonProperty("error_type")
    String type;

    @JsonProperty("error_message")
    String message;

    public RestError() {
        super();
    }

    public RestError(String type, int code, String message) {
        super(code);
        this.type = type;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
