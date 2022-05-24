package com.mb.lab.banks.user.persistence.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.mb.lab.banks.user.persistence.domain.base.PO;
import com.mb.lab.banks.user.persistence.domain.entity.SmsStatus;

@Entity
@Table(name = "SMS")
public class Sms extends PO {
    
    private static final long serialVersionUID = 1L;

    @Column(name = "PHONE", length = 50, nullable = true)
    private String phone;
    
    @Column(name = "CONTENT", length = 255, nullable = true)
    private String content;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "STATUS")
    private SmsStatus status;
    
    @CreationTimestamp
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "CREATED_TIME", nullable = false)
    private Date createdTime;
    
    @UpdateTimestamp
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_TIME", nullable = false)
    private Date updatedTime;
    
    @Column(name = "MESSAGE_ID", length = 50, nullable = true)
    private String messageId;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SmsStatus getStatus() {
        return status;
    }

    public void setStatus(SmsStatus status) {
        this.status = status;
    }
    
    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
    
}
