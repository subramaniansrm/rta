package com.srm.rta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.srm.rta.entity.ExternalLinkEntity;

public interface ExternalLinkRepository extends JpaRepository<ExternalLinkEntity, Integer> {

	@Query(value = "select r from ExternalLinkEntity r where r.deleteFlag = '0' and r.entityLicenseId =:entityId ORDER BY r.id DESC ")
	List<ExternalLinkEntity> getAll(@Param("entityId") Integer entityId);

	@Query(value = "select r from ExternalLinkEntity r where r.deleteFlag = '0' and r.id=:i ")
	ExternalLinkEntity attachmentDownload(@Param("i") int i);

}
