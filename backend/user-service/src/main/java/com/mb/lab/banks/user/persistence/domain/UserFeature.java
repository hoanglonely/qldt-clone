package com.mb.lab.banks.user.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.mb.lab.banks.user.persistence.domain.base.PO;
import com.mb.lab.banks.user.persistence.domain.entity.Feature;

@Entity
@Table(name = "USER_FEATURE")
public class UserFeature extends PO {

    private static final long serialVersionUID = 1L;
    
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", nullable = false)
    private User user;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "FEATURE", length = 100)
    private Feature feature;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

}
