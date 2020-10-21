package com.srm.rta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.srm.rta.entity.HolidayDetails;

public interface HolidayDetailsRepository extends JpaRepository<HolidayDetails, Integer>{
	
	@Query(value = "select h from HolidayDetails h where h.deleteFlag ='0' and h.entityLicenseId=:entityId ")
	public List<HolidayDetails> getAllHolidayList(@Param("entityId") Integer entityId);
	
	@Query(value = "select h from HolidayDetails h where h.deleteFlag ='0' and h.entityLicenseId=:entityId and h.holidayId=:holidayFk")
	public List<HolidayDetails> findDetailId(@Param("entityId") Integer entityId,@Param("holidayFk")Integer holidayFk);

}
