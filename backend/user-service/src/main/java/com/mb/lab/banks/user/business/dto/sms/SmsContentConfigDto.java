package com.mb.lab.banks.user.business.dto.sms;

import com.mb.lab.banks.user.persistence.domain.entity.SmsType;

public class SmsContentConfigDto {

    private String content;
    private SmsType type;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SmsType getType() {
        return type;
    }

    public void setType(SmsType type) {
        this.type = type;
    }

}
