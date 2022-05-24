package com.mb.lab.banks.user.persistence.repository.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.mb.lab.banks.user.persistence.domain.base.PODraftable;
import com.mb.lab.banks.user.persistence.domain.entity.ActiveStatus;
import com.mb.lab.banks.user.persistence.repository.PODraftableRepository;

public class PODraftableRepositoryImpl<T extends PODraftable> extends PORepositoryImpl<T> implements PODraftableRepository<T> {

    public PODraftableRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
		super(domainClass, entityManager);
	}

    @Override
    public Map<Long, T> getMap(Iterable<Long> ids, Collection<ActiveStatus> activeStatuses) {
    	Assert.notNull(ids, "The given Iterable of Id's must not be null!");

		if (!ids.iterator().hasNext()) {
			return Collections.emptyMap();
		}

        List<List<Long>> idsSplitedList = splitCollection(ids, MAX_IN_PARAM);

        Map<Long, T> map = new HashMap<>();
        for (List<Long> idsSplited : idsSplitedList) {
            Specification<T> specification = Specification
            		.where(new ByIdsSpecification<>(entityInformation, idsSplited))
            		.and(new ByActiveStatusSpec<>(activeStatuses));
            
    		TypedQuery<T> query = getQuery(specification, Sort.unsorted());

    		List<T> list = query.getResultList();

            if (CollectionUtils.isEmpty(list)) {
                continue;
            }

            for (T t : list) {
                map.put(t.getId(), t);
            }
        }

        return map;
    }

    @Override
    public boolean existsById(Long id, Collection<ActiveStatus> activeStatuses) {
        if (activeStatuses == null) {
            return existsById(id);
        }

        if (CollectionUtils.isEmpty(activeStatuses)) {
            return false;
        }
        
        Specification<T> specification = Specification
        		.where(new ByIdSpecification<>(entityInformation, id))
        		.and(new ByActiveStatusSpec<>(activeStatuses));
        
		long count = count(specification);
		return count > 0;
    }

    @Override
    public boolean enable(Long id) {
        Optional<T> value = findById(id);
    	if (!value.isPresent()) {
    		return false;
    	}
    	
    	T domain = value.get();
    	domain.setActiveStatus(ActiveStatus.ACTIVE);
        this.entityManager.merge(domain);
        return true;
    }

    @Override
    public boolean activate(Long id) {
        Optional<T> value = findById(id);
    	if (!value.isPresent()) {
    		return false;
    	}
    	
    	T domain = value.get();
    	domain.setActiveStatus(ActiveStatus.ACTIVE);
        this.entityManager.merge(domain);
        return true;
    }

    @Override
    public boolean deactivate(Long id) {
    	Optional<T> value = findById(id);
    	if (!value.isPresent()) {
    		return false;
    	}
    	
    	T domain = value.get();
    	domain.setActiveStatus(ActiveStatus.INACTIVE);
        this.entityManager.merge(domain);
        return true;
    }

    @Override
    public boolean deactivate(Set<Long> ids) {
        List<T> list = findAllById(ids);
        for (T domain : list) {
        	domain.setActiveStatus(ActiveStatus.INACTIVE);
            this.entityManager.merge(domain);
        }
        return true;
    }
    
    protected Specification<T> buildSpecification(ActiveStatus activeStatus) {
    	return new ByActiveStatusSpec<T>(activeStatus != null ? Collections.singleton(activeStatus) : null);
    }
    
    protected Specification<T> buildSpecification(Collection<ActiveStatus> activeStatuses) {
    	return new ByActiveStatusSpec<T>(activeStatuses);
    }
    
    public static class ByActiveStatusSpec<T extends PODraftable> implements Specification<T> {
    	
		private static final long serialVersionUID = 1L;
		
		private Collection<ActiveStatus> statuses;
    	
    	public ByActiveStatusSpec(Collection<ActiveStatus> statuses) {
    		this.statuses = statuses;
    	}

		@Override
		public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
			if (statuses == null || statuses.size() == 0) {
				return null;
			}
			if (statuses.size() > 1) {
				return root.get("activeStatus").in(statuses);
			}
			return criteriaBuilder.equal(root.get("activeStatus"), statuses.iterator().next());
		}
    }

}
