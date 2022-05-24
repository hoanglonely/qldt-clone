package com.mb.lab.banks.user.persistence.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.mb.lab.banks.user.persistence.domain.User;
import com.mb.lab.banks.user.persistence.domain.entity.ActiveStatus;
import com.mb.lab.banks.user.persistence.domain.entity.UserRole;

public interface UserRepository extends PODraftableRepository<User> {

    public Optional<User> findByUsername(String username);

    public List<User> findByRole(UserRole role,
            ActiveStatus activeStatus,
            String username,
            String keyword,
            Long storeId,
            Long partnerId,
            Collection<Long> idList,
            Collection<String> usernameList,
            Pageable pageable);

    public long countByRole(UserRole role,
            ActiveStatus activeStatus,
            String username,
            String keyword,
            Long storeId,
            Long partnerId,
            Collection<Long> idList,
            Collection<String> usernameList);

}
