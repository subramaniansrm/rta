package com.srm.rta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.srm.rta.entity.WidgetEntity;
import com.srm.rta.vo.WidgetVO;

public interface WidgetRepository extends JpaRepository<WidgetEntity, Integer> {

	@Query(value = "select r from WidgetEntity r where r.deleteFlag = '0' and r.entityLicenseId =:entityId ORDER BY r.widgetId DESC ")
	List<WidgetEntity> getAll(@Param("entityId") Integer entityId);

	@Query(value = " select w from WidgetEntity w " + "  where w.entityLicenseId =:entityId AND w.deleteFlag ='0' "
			+ "   AND w.widgetIndex!= 5 " + " order by w.widgetId desc")
	List<WidgetEntity> getAllHR(@Param("entityId") Integer entityId);

}
