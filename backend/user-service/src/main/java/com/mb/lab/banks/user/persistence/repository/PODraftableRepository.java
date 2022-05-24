package com.mb.lab.banks.user.persistence.repository;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.data.repository.NoRepositoryBean;

import com.mb.lab.banks.user.persistence.domain.base.PODraftable;
import com.mb.lab.banks.user.persistence.domain.entity.ActiveStatus;

@NoRepositoryBean
public interface PODraftableRepository<T extends PODraftable> extends PORepository<T> {

    public Map<Long, T> getMap(Iterable<Long> ids, Collection<ActiveStatus> activeStatuses);

    public boolean existsById(Long id, Collection<ActiveStatus> activeStatuses);

    public boolean enable(Long id);

    public boolean activate(Long id);

    public boolean deactivate(Long id);

    public boolean deactivate(Set<Long> ids);

}
