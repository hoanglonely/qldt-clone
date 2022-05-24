package com.mb.lab.banks.user.business.dto.user;

import com.mb.lab.banks.user.business.dto.base.DraftableDto;
import com.mb.lab.banks.user.persistence.domain.User;
import com.mb.lab.banks.user.persistence.domain.entity.UserRole;

public class UserSimpleDto extends DraftableDto {

    private UserRole role;
    private String fullname;
    private String username;
    private String phone;
    private String email;
    private boolean systemUser;
    private Long partnerId;

    public UserSimpleDto(User user) {
        super(user);

        this.role = user.getRole();
        this.fullname = user.getFullname();
        this.username = user.getUsername();
        this.phone = user.getPhone();
        this.email = user.getEmail();
        this.systemUser = user.isSystemUser();
        this.partnerId = user.getPartnerId();
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isSystemUser() {
        return systemUser;
    }

    public void setSystemUser(boolean systemUser) {
        this.systemUser = systemUser;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

}
