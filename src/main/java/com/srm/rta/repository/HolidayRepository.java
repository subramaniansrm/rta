package com.srm.rta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.srm.rta.entity.Holiday;

public interface HolidayRepository extends JpaRepository<Holiday, Integer>{
	
	@Query(value = "select h from Holiday h where h.deleteFlag ='0' and h.entityLicenseId=:entityId order by h.id desc ")
	public List<Holiday> getHolidayList(@Param("entityId") Integer entityId);

}
