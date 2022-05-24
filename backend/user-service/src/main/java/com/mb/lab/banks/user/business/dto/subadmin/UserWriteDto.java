package com.mb.lab.banks.user.business.dto.subadmin;

import java.util.Set;

import com.mb.lab.banks.user.business.dto.user.UserSimpleWriteDto;
import com.mb.lab.banks.user.persistence.domain.entity.Feature;

public class UserWriteDto extends UserSimpleWriteDto {

    private static final long serialVersionUID = 1030333344858957576L;

    private Set<Feature> features;

    public Set<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(Set<Feature> features) {
        this.features = features;
    }

}
