package com.mb.lab.banks.user.business.dto.subadmin;

import java.util.Collections;
import java.util.Set;

import com.mb.lab.banks.user.business.dto.user.UserSimpleDto;
import com.mb.lab.banks.user.persistence.domain.User;
import com.mb.lab.banks.user.persistence.domain.entity.Feature;

public class UserDto extends UserSimpleDto {

    private Set<Feature> features;

    public UserDto(User user, Set<Feature> features) {
        super(user);

        this.features = features == null ? Collections.emptySet() : features;
    }

    public Set<Feature> getFeatures() {
        return features;
    }

}
