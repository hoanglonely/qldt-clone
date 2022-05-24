package com.mb.lab.banks.auth.service.user.model;

import java.util.Set;

public class UserInfoDto {

    private Long id;
    private String role;
    private String fullname;
    private String username;

    private boolean needCheckFeature;
    private Set<String> features;
    
    private Long partnerId;
    private Long storeId;

    private String dateFormat;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isNeedCheckFeature() {
		return needCheckFeature;
	}

	public void setNeedCheckFeature(boolean needCheckFeature) {
		this.needCheckFeature = needCheckFeature;
	}

	public Set<String> getFeatures() {
		return features;
	}

	public void setFeatures(Set<String> features) {
		this.features = features;
	}

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

}
