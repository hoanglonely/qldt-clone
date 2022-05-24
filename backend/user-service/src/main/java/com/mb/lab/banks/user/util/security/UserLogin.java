package com.mb.lab.banks.user.util.security;

import java.io.Serializable;

public class UserLogin implements Serializable {

    private static final long serialVersionUID = -7942635748301089503L;

    private String role;
    private Long id;
    private String username;
    private Long partnerId;
    private Long storeId;

    public UserLogin() {
        super();
    }

    public UserLogin(String role, Long id, String username, Long partnerId, Long storeId) {
        super();

        this.role = role;
        this.id = id;
        this.username = username;
        this.partnerId = partnerId;
        this.storeId = storeId;
    }

    public boolean isRole(String... roles) {
        if (this.role != null && roles != null) {
            for (String role : roles) {
                if (this.role.equals(role)) {
                    return true;
                }
            }
        }

        return false;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
