package com.mb.lab.banks.auth.service.user.model;

public class UserLoginDto {

    private Long id;
    private String role;
    private String fullname;
    private String username;
    private String password;
    private boolean active;
    private Long partnerId;
    private Long storeId;

	public Long getId() {
		return id;
	}

    public String getRole() {
        return role;
    }

    public String getFullname() {
        return fullname;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isActive() {
        return active;
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

}

