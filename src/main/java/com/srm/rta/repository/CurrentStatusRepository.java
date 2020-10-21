package com.srm.rta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.srm.rta.entity.CurrentStatusEntity;

public interface CurrentStatusRepository extends JpaRepository<CurrentStatusEntity, Integer> {

	@Query(value = " select c from CurrentStatusEntity c where c.deleteFlag='0' ")
	List<CurrentStatusEntity> getAllCurrentStatus();
	
	
	

}
