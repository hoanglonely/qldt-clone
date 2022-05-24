package com.mb.lab.banks.user.persistence.repository.impl;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.mb.lab.banks.user.persistence.domain.User;
import com.mb.lab.banks.user.persistence.domain.entity.ActiveStatus;
import com.mb.lab.banks.user.persistence.domain.entity.UserRole;
import com.mb.lab.banks.user.persistence.repository.UserRepository;
import com.mb.lab.banks.utils.common.StringUtils;

@Repository
public class UserRepositoryImpl extends PODraftableRepositoryImpl<User> implements UserRepository {

    public UserRepositoryImpl(EntityManager entityManager) {
        super(User.class, entityManager);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Specification<User> specification = new ByUsernameSpec(username);
        TypedQuery<User> query = getQuery(specification, Sort.unsorted());
        
        List<User> users = query.getResultList();
        if (users.size() == 1) {
            return Optional.of(users.get(0));
        }
        return Optional.empty();
    }

    @Override
    public List<User> findByRole(UserRole role,
            ActiveStatus activeStatus,
            String username,
            String keyword,
            Long storeId,
            Long partnerId,
            Collection<Long> idList,
            Collection<String> usernameList,
            Pageable pageable) {
        Specification<User> specification = buildSpecification(activeStatus, role, username, keyword, storeId, partnerId, idList, usernameList);
        TypedQuery<User> query = getQuery(specification, Sort.by(Direction.ASC, "fullname"));
        if (pageable != null) {
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
        }
        return query.getResultList();
    }

    @Override
    public long countByRole(UserRole role,
            ActiveStatus activeStatus,
            String username,
            String keyword,
            Long storeId,
            Long partnerId,
            Collection<Long> idList,
            Collection<String> usernameList) {
        Specification<User> specification = buildSpecification(activeStatus, role, username, keyword, storeId, partnerId, idList, usernameList);
        return count(specification);
    }

    protected Specification<User> buildSpecification(ActiveStatus activeStatus,
            UserRole role,
            String username,
            String keyword,
            Long storeId,
            Long partnerId,
            Collection<Long> idList,
            Collection<String> usernameList) {
        Specification<User> spec = super.buildSpecification(activeStatus);

        if (role != null) {
            spec = spec.and(new ByRoleSpec(role));
        }

        if (!StringUtils.isEmpty(username)) {
            spec = spec.and(new ByUsernameSpec(username));
        }

        if (!StringUtils.isEmpty(keyword)) {
            spec = spec.and(new ByKeywordSpec(keyword));
        }

        if (storeId != null) {
            spec = spec.and(new ByStoreSpec(storeId));
        }

        if (partnerId != null) {
            spec = spec.and(new ByPartnerSpec(partnerId));
        }

        if (!CollectionUtils.isEmpty(idList)) {
            spec = spec.and(new ByIdsSpecification<>(this.entityInformation, idList));
        }

        if (!CollectionUtils.isEmpty(usernameList)) {
            spec = spec.and(new ByUsernameListSpec(usernameList));
        }

        return spec;
    }

    public static class ByRoleSpec implements Specification<User> {

        private static final long serialVersionUID = 1L;

        private UserRole role;

        public ByRoleSpec(UserRole role) {
            this.role = role;
        }

        @Override
        public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            return criteriaBuilder.equal(root.get("role"), role);
        }
    }

    public static class ByUsernameSpec implements Specification<User> {

        private static final long serialVersionUID = 1L;

        private String username;

        public ByUsernameSpec(String username) {
            this.username = username;
        }

        @Override
        public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            return criteriaBuilder.equal(root.get("username"), username.toLowerCase());
        }
    }

    public static final class ByUsernameListSpec implements Specification<User> {

        private static final long serialVersionUID = 1L;

        private final Collection<String> usernameList;

        public ByUsernameListSpec(Collection<String> usernameList) {
            this.usernameList = usernameList;
        }

        public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            return root.get("username").in(usernameList);
        }
    }

    public static class ByPartnerSpec implements Specification<User> {

        private static final long serialVersionUID = 1L;

        private Long partnerId;

        public ByPartnerSpec(Long partnerId) {
            this.partnerId = partnerId;
        }

        @Override
        public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            return criteriaBuilder.equal(root.get("partnerId"), partnerId);
        }
    }

    public static class ByStoreSpec implements Specification<User> {

        private static final long serialVersionUID = 1L;

        private Long storeId;

        public ByStoreSpec(Long storeId) {
            this.storeId = storeId;
        }

        @Override
        public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            return criteriaBuilder.equal(root.get("storeId"), storeId);
        }
    }

    public static class ByKeywordSpec implements Specification<User> {

        private static final long serialVersionUID = 1L;

        private String keyword;

        public ByKeywordSpec(String keyword) {
            this.keyword = keyword;
        }

        @Override
        public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            return cb.or(cb.like(cb.lower(root.get("username")), "%" + StringUtils.createCriteriaForMySqlLikeSearch(keyword.toLowerCase()) + "%"),
                    cb.like(cb.lower(root.get("fullname")), "%" + StringUtils.createCriteriaForMySqlLikeSearch(keyword.toLowerCase()) + "%"),
                    cb.like(cb.lower(root.get("phone")), "%" + StringUtils.createCriteriaForMySqlLikeSearch(keyword.toLowerCase()) + "%"),
                    cb.like(cb.lower(root.get("email")), "%" + StringUtils.createCriteriaForMySqlLikeSearch(keyword.toLowerCase()) + "%"));
        }
    }

}
