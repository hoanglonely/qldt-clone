package com.mb.lab.banks.user.business.dto.login;

import com.mb.lab.banks.user.business.dto.base.DTO;
import com.mb.lab.banks.user.persistence.domain.User;
import com.mb.lab.banks.user.persistence.domain.entity.UserRole;

public class UserLoginDto extends DTO {

    private UserRole role;
    private String fullname;
    private String username;
    private String password;
    private boolean active;
    private Long partnerId;
    private Long storeId;

    public UserLoginDto(User user) {
        super(user);

        this.role = user.getRole();
        this.fullname = user.getFullname();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.active = user.isActive();
        this.partnerId = user.getPartnerId();
        this.storeId = user.getStoreId();
    }

    public UserRole getRole() {
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
