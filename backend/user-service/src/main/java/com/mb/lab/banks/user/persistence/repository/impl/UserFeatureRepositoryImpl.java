package com.mb.lab.banks.user.persistence.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.mb.lab.banks.user.persistence.domain.UserFeature;
import com.mb.lab.banks.user.persistence.repository.UserFeatureRepository;

@Repository
public class UserFeatureRepositoryImpl extends PORepositoryImpl<UserFeature> implements UserFeatureRepository {

	public UserFeatureRepositoryImpl(EntityManager entityManager) {
		super(UserFeature.class, entityManager);
	}

	@Override
	public List<UserFeature> findByUserId(Long userId) {
		Specification<UserFeature> specification = new ByUserSpec(userId);
		TypedQuery<UserFeature> query = getQuery(specification, Sort.unsorted());
		return query.getResultList();
	}
	
	public static class ByUserSpec implements Specification<UserFeature> {
    	
		private static final long serialVersionUID = 1L;
		
		private Long userId;
    	
    	public ByUserSpec(Long userId) {
    		this.userId = userId;
    	}

		@Override
		public Predicate toPredicate(Root<UserFeature> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
			return criteriaBuilder.equal(root.get("user").get("id"), userId);
		}
    }

}
