package com.srm.rta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.srm.rta.entity.RequestTypeEntity;

public interface RequestTypeRepository extends JpaRepository<RequestTypeEntity, Integer>{
	
	@Query(value = "select r from RequestTypeEntity r Where r.deleteFlag = '0' and r.entityLicenseId =:entityId order by r.requestTypeId desc ")
	public List<RequestTypeEntity> getAllRequestType(@Param("entityId") Integer entityId);
	
}
