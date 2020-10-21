package com.srm.rta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.srm.rta.entity.RequestWorkFlowDetailsEntity;
import com.srm.rta.entity.RequestWorkFlowEntity;

public interface RequestWorkFlowRepository extends JpaRepository<RequestWorkFlowEntity, Integer> {
	
	
	@Query(value = "SELECT wflow.rin_ma_request_workflow_code,"
			+ " wflow.rin_ma_req_workflow_description,reqtype.rin_ma_request_type_name,subtype.rin_ma_request_subtype_name,"
			+ " wflow.idrin_ma_req_workflow_id,wflow.rin_ma_req_workflow_is_active "
			+ " FROM rta_2_local.rin_ma_req_workflow wflow " + " LEFT JOIN rta_2_local.rin_ma_request_type reqtype "
			+ " ON wflow.rin_ma_request_type_id = reqtype.idrin_ma_request_type_id "
			+ " LEFT JOIN rta_2_local.rin_ma_request_subtype subtype "
			+ " ON wflow.rin_ma_request_subtype_id = subtype.idrin_ma_request_subtype_id "
			+ " where wflow.delete_flag  = '0' "
			+ " and wflow.rin_ma_entity_id =:entityId" 
			+ " order by wflow.create_date desc" ,nativeQuery = true)
	public List<Object> getAll(@Param("entityId") Integer entityId);
	
	
	@Query(value = "SELECT wflow.*,reqtype.rin_ma_request_type_name,subtype.rin_ma_request_subtype_name"
			+ " FROM rta_2_local.rin_ma_req_workflow wflow " + " LEFT JOIN rta_2_local.rin_ma_request_type reqtype "
			+ " ON wflow.rin_ma_request_type_id = reqtype.idrin_ma_request_type_id "
			+ " LEFT JOIN rta_2_local.rin_ma_request_subtype subtype "
			+ " ON wflow.rin_ma_request_subtype_id = subtype.idrin_ma_request_subtype_id "
			+ " where wflow.delete_flag  ='0'" 
			+ " AND wflow.idrin_ma_req_workflow_id =:workFlowId " ,nativeQuery = true)
	public Object findWorkFlow(@Param("workFlowId") Integer workFlowId);
	
	
	

}
