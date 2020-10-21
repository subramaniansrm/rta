package com.srm.rta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.srm.rta.entity.EmergencyContactEntity;

public interface EmergencyContactRepository extends JpaRepository<EmergencyContactEntity, Integer> {

	@Query(value = "select r from EmergencyContactEntity r where r.deleteFlag = '0' and r.entityLicenseId=:entityId order by r.emergencyContactPathId desc ")
	List<EmergencyContactEntity> getAll(@Param("entityId") Integer entityId);

	@Query(value = "select r from EmergencyContactEntity r where r.deleteFlag = '0'and  r.emergencyContactPathId=:emergencyContactPathId ORDER BY r.emergencyContactPathId DESC")
	EmergencyContactEntity load(@Param("emergencyContactPathId") int emergencyContactPathId);

	@Query(value = "select r from EmergencyContactEntity r where r.deleteFlag = '0' and r.entityLicenseId=:entityId and r.emergencyContactPathId=:emergencyContactPathId")
	EmergencyContactEntity attachmentDownload(@Param("emergencyContactPathId") int emergencyContactPathId, @Param("entityId") Integer entityId);

}
