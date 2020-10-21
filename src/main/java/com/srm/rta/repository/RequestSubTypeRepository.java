package com.srm.rta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.srm.rta.entity.RequestSubTypeEntity;

@Repository
public interface RequestSubTypeRepository extends JpaRepository<RequestSubTypeEntity, Integer> {

	@Query(value = "select subt.rin_ma_request_subtype_code,subt.rin_ma_request_subtype_name,subt.rin_ma_request_subtype_is_active,"
			+ "type.rin_ma_request_type_name,subt.idrin_ma_request_subtype_id,subt.rin_ma_request_type_id,subt.rin_ma_request_subtype_priorty "
			+ "	FROM rta_2_local.rin_ma_request_subtype subt,"
			+ "  rta_2_local.rin_ma_request_type type where type.idrin_ma_request_type_id = subt.rin_ma_request_type_id and subt.rin_ma_entity_id = :entityId and subt.delete_flag = '0'"
			+ " and type.delete_flag = '0' order by subt.idrin_ma_request_subtype_id desc ", nativeQuery = true)
	public List<Object[]> getAll(@Param("entityId") Integer entityId);

	/*@Query(value = "select sub,type.rin_ma_request_type_name FROM rta_2_local.rin_ma_request_subtype sub "
			+ " join rta_2_local.rin_ma_request_type type on type.idrin_ma_request_type_id = sub.rin_ma_request_type_id"
			+ " where sub.rin_ma_entity_id = :entityId and sub.delete_flag = '0'" + " and type.delete_flag = '0'"
			+ " and sub.idrin_ma_request_subtype_id = :id  ", nativeQuery = true)
	public Object[] findId(@Param("entityId") Integer entityId, @Param("id") Integer Id);*/

	/*
	 * @Query(value =
	 * "SELECT COUNT(requestSubTypeId) FROM RequestWorkFlowEntity  where  entityLicenseId = :entityId and deleteFlag = '0' "
	 * + "	AND	requestSubTypeId = :subTypeId" ) (Integer)(Long)
	 * findWorkflow(@Param("entityId") Integer entityId,@Param("subTypeId") Long
	 * subTypeId);
	 */
}
