package com.mb.lab.banks.user.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mb.lab.banks.user.persistence.domain.UserFeature;

public interface UserFeatureRepository extends JpaRepository<UserFeature, Long> {

    public List<UserFeature> findByUserId(Long userId);

}
