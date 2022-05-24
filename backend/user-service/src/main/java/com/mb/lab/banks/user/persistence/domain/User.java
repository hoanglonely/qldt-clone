package com.mb.lab.banks.user.persistence.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.mb.lab.banks.user.persistence.domain.base.PODraftable;
import com.mb.lab.banks.user.persistence.domain.entity.UserRole;

@Entity
@Table(name = "USER")
public class User extends PODraftable {

    private static final long serialVersionUID = 4564702546634359910L;

    @Column(name = "IS_SYSTEM_USER", nullable = false)
    private boolean systemUser;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", length = 100, nullable = false)
    private UserRole role;

    @Column(name = "FULLNAME", length = 100, nullable = false)
    private String fullname;

    @Column(name = "USERNAME", length = 50, nullable = false)
    private String username;

    @Column(name = "PHONE", length = 20, nullable = true)
    private String phone;

    @Column(name = "EMAIL", length = 255, nullable = true)
    private String email;

    @Column(name = "PASSWORD", length = 1000, nullable = false)
    private String password;

    @Column(name = "PARTNER_ID", nullable = true)
    private Long partnerId;

    @Column(name = "STORE_ID", nullable = true)
    private Long storeId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserFeature> features;

    public boolean isSystemUser() {
        return systemUser;
    }

    public void setSystemUser(boolean systemUser) {
        this.systemUser = systemUser;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<UserFeature> getFeatures() {
        return features;
    }

    public void setFeatures(Set<UserFeature> features) {
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

}
