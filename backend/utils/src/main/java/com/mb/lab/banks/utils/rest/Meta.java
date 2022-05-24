package com.mb.lab.banks.utils.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author thanh
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Meta {

    public static final Meta OK = new Meta(200);

    @JsonProperty("code")
    int code;

    public Meta() {
        super();
    }

    public Meta(int code) {
        super();
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
