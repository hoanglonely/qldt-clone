package com.mb.lab.banks.user.persistence.domain.base;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mb.lab.banks.user.persistence.domain.entity.ActiveStatus;

@MappedSuperclass
public abstract class PODraftable extends PO {

    private static final long serialVersionUID = 1L;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "ACTIVE_STATUS")
    private ActiveStatus activeStatus;
    
    @CreationTimestamp
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "CREATED_TIME", nullable = false)
    private Date createdTime;
    
    @UpdateTimestamp
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_TIME", nullable = false)
    private Date updatedTime;

    public PODraftable() {
        super();
        this.activeStatus = ActiveStatus.DRAFT;
    }

    public ActiveStatus getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(ActiveStatus activeStatus) {
        this.activeStatus = activeStatus;
    }

    @JsonIgnore
    public boolean isActive() {
        return activeStatus != null && activeStatus.equals(ActiveStatus.ACTIVE);
    }

    @JsonIgnore
    public boolean isInactive() {
        return activeStatus != null && activeStatus.equals(ActiveStatus.INACTIVE);
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

}
