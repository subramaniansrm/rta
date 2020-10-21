package com.srm.rta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.srm.rta.entity.RinFlashNewsEntity;


public interface FlashNewsRepository extends JpaRepository<RinFlashNewsEntity, Integer> {
	
	@Query(value = "select r from RinFlashNewsEntity r where r.deleteFlag ='0' and r.entityLicenseId=:entityId order by r.id desc ")
	public List<RinFlashNewsEntity> getAll(@Param("entityId") Integer entityId);
	
}
