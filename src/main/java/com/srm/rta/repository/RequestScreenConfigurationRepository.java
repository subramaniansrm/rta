package com.srm.rta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.srm.rta.entity.RequestScreenConfigurationEntity;

public interface RequestScreenConfigurationRepository extends JpaRepository<RequestScreenConfigurationEntity, Integer> {

	
	@Query(value = " SELECT COUNT(request.requestScreenConfigurationCode) FROM RequestScreenConfigurationEntity request where request.entityLicenseId =:entityId "
			+ " and request.requestScreenConfigurationCode =:requestScreenConfigurationCode")
	Integer isRequestScreenConfigurationCodeAvailable(@Param("entityId") Integer entityId,@Param("requestScreenConfigurationCode") String requestScreenConfigurationCode);
	
}


