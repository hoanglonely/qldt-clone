package com.mb.lab.banks.user.business.dto.login;

import java.util.Set;

import com.mb.lab.banks.user.business.dto.base.DTO;
import com.mb.lab.banks.user.persistence.domain.User;
import com.mb.lab.banks.user.persistence.domain.entity.Feature;
import com.mb.lab.banks.user.persistence.domain.entity.UserRole;

public class UserInfoDto extends DTO {

    private UserRole role;
    private String fullname;
    private String username;

    private boolean needCheckFeature;
    private Set<Feature> features;
    
    private Long partnerId;
    private Long storeId;

    public UserInfoDto(User user, Set<Feature> features) {
        super(user);

        this.role = user.getRole();
        this.fullname = user.getFullname();
        this.username = user.getUsername();

        if (features != null) {
            this.needCheckFeature = true;
            this.features = features;
        } else {
            this.needCheckFeature = false;
        }
        
        this.partnerId = user.getPartnerId();
        this.storeId = user.getStoreId();
    }

    public UserInfoDto(User user) {
        this(user, null);
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

    public boolean isNeedCheckFeature() {
        return needCheckFeature;
    }

    public Set<Feature> getFeatures() {
        return features;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public Long getStoreId() {
        return storeId;
    }

}
