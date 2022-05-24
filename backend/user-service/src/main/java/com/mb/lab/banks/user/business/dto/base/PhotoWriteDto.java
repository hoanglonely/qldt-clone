package com.mb.lab.banks.user.business.dto.base;

import java.io.Serializable;

public class PhotoWriteDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String key;
    private String base64String;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getBase64String() {
        return base64String;
    }

    public void setBase64String(String base64String) {
        this.base64String = base64String;
    }

}
