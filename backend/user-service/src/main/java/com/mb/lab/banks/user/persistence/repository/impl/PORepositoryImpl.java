package com.mb.lab.banks.user.persistence.repository.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.Assert;

import com.mb.lab.banks.user.persistence.domain.base.PO;
import com.mb.lab.banks.user.persistence.repository.PORepository;

public class PORepositoryImpl<T extends PO> extends SimpleJpaRepository<T, Long> implements PORepository<T> {

	protected static final int MAX_IN_PARAM = 1000;
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	protected final JpaEntityInformation<T, ?> entityInformation;
	protected final EntityManager entityManager;
	protected final String tableName;

	public PORepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
		this(JpaEntityInformationSupport.getEntityInformation(domainClass, entityManager), entityManager);
	}
	
	private PORepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.entityInformation = entityInformation;
		this.entityManager = entityManager;
		this.tableName = determineTableName();
	}
	
	@Override
	public List<T> findAllById(Iterable<Long> ids) {
		Assert.notNull(ids, "The given Iterable of Id's must not be null!");

		if (!ids.iterator().hasNext()) {
			return Collections.emptyList();
		}
		
		List<List<Long>> idsSplitedList = splitCollection(ids, MAX_IN_PARAM);
		List<T> results = new ArrayList<>();
		for (List<Long> idsSplited : idsSplitedList) {
            List<T> list = super.findAllById(idsSplited);
            if (CollectionUtils.isEmpty(list)) {
                continue;
            }

            results.addAll(list);
        }
		
		return results;
	}

    @Override
    public Map<Long, T> getMap(Iterable<Long> _ids) {
    	Assert.notNull(_ids, "The given Iterable of Id's must not be null!");

		if (!_ids.iterator().hasNext()) {
			return Collections.emptyMap();
		}

        List<List<Long>> idsSplitedList = splitCollection(_ids, MAX_IN_PARAM);

        Map<Long, T> map = new HashMap<>();
        for (List<Long> idsSplited : idsSplitedList) {
            List<T> list = findAllById(idsSplited);
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
    public String getTableName() {
        return this.tableName;
    }
    
    /**
     * Returns the table name for a given entity type in the {@link EntityManager}.
     */
    protected String determineTableName() {
        /*
         * Check if the specified class is present in the metamodel.
         * Throws IllegalArgumentException if not.
         */
        Metamodel meta = this.entityManager.getMetamodel();
        Class<T> entityClass = this.entityInformation.getJavaType();
        EntityType<T> entityType = meta.entity(entityClass);

        // Check whether @Table annotation is present on the class.
        Table annotation = entityClass.getAnnotation(Table.class);

        return (annotation == null) ? entityType.getName().toUpperCase() : annotation.name();
    }

    protected static final <T> List<List<T>> splitCollection(Iterable<T> ids, int maxItemPerList) {
    	Assert.notNull(ids, "The given Iterable of Id's must not be null!");

		if (!ids.iterator().hasNext()) {
			return Collections.emptyList();
		}

        List<T> list = new ArrayList<T>();
        CollectionUtils.addAll(list, ids);

        List<List<T>> splitedList = null;
        if (list.size() > maxItemPerList) {
            splitedList = ListUtils.partition(list, maxItemPerList);
        } else {
            splitedList = Collections.singletonList(list);
        }

        return splitedList;
    }
    
    public static final class ByIdSpecification<T> implements Specification<T> {

		private static final long serialVersionUID = 1L;

		private final JpaEntityInformation<T, ?> entityInformation;
		private final Object id;

		public ByIdSpecification(JpaEntityInformation<T, ?> entityInformation, Object id) {
			this.entityInformation = entityInformation;
			this.id = id;
		}

		public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
			return cb.equal(root.get(entityInformation.getIdAttribute()), id);
		}
	}

    public static final class ByNotIdSpecification<T> implements Specification<T> {

		private static final long serialVersionUID = 1L;

		private final JpaEntityInformation<T, ?> entityInformation;
		private final Object id;

		public ByNotIdSpecification(JpaEntityInformation<T, ?> entityInformation, Object id) {
			this.entityInformation = entityInformation;
			this.id = id;
		}

		public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
			return cb.notEqual(root.get(entityInformation.getIdAttribute()), id);
		}
	}

	public static final class ByIdsSpecification<T> implements Specification<T> {

		private static final long serialVersionUID = 1L;

		private final JpaEntityInformation<T, ?> entityInformation;
		private final Collection<?> ids;

		public ByIdsSpecification(JpaEntityInformation<T, ?> entityInformation, Collection<?> ids) {
			this.entityInformation = entityInformation;
			this.ids = ids;
		}

		public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
			return root.get(entityInformation.getIdAttribute()).in(ids);
		}
	}

}
