package com.mb.lab.banks.user.persistence.repository;

import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.mb.lab.banks.user.persistence.domain.base.PO;

@NoRepositoryBean
public interface PORepository<T extends PO> extends JpaRepository<T, Long> {
	
	public Map<Long, T> getMap(Iterable<Long> ids);
	
	public String getTableName();
    
}
