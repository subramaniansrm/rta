package com.srm.rta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.srm.rta.entity.WidgetDetailEntity;

public interface WidgetDetailRepository extends JpaRepository<WidgetDetailEntity, Integer> {

	@Query(value = "select count(widgetDetailHeadingIndex) from WidgetDetailEntity where entityLicenseId =:entityId and deleteFlag='0' and widgetId=:widgetId")
	int createIndex(@Param("widgetId") int widgetId, @Param("entityId") int entityId);

	@Query(value = "select wd from WidgetDetailEntity wd where wd.entityLicenseId =:entityId and wd.deleteFlag='0' and wd.widgetId=:widgetId")
	List<WidgetDetailEntity> findDetailList(@Param("widgetId") int widgetId, @Param("entityId")  Integer entityId);

	@Query(value = "select wd from WidgetDetailEntity wd where wd.widgetDetailId =:widgetDetailId and wd.deleteFlag='0' and wd.widgetId=:widgetId")
	WidgetDetailEntity findDetailLoad(@Param("widgetDetailId") int widgetDetailId, @Param("widgetId")  int widgetId);

	}
