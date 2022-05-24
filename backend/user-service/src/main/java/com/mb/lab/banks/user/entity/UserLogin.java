package com.mb.lab.banks.user.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mb.lab.banks.user.persistence.domain.entity.UserRole;

public class UserLogin implements Serializable {

    private static final long serialVersionUID = -7942635748301089503L;

    private UserRole role;
    private Long id;
    private String username;
    private Long partnerId;

    public UserLogin() {
        super();
    }

    public UserLogin(UserRole role, Long id, String username, Long partnerId) {
        super();

        this.role = role;
        this.id = id;
        this.username = username;
        this.partnerId = partnerId;
    }

    @JsonIgnore
    public boolean isRole(UserRole... roles) {
        if (this.role != null && roles != null) {
            for (UserRole role : roles) {
                if (this.role.equals(role)) {
                    return true;
                }
            }
        }

        return false;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
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

}
