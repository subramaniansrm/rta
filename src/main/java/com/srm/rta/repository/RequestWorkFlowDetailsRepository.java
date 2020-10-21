package com.srm.rta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.srm.rta.entity.RequestWorkFlowDetailsEntity;

public interface RequestWorkFlowDetailsRepository extends JpaRepository<RequestWorkFlowDetailsEntity, Integer>{
	
	
	@Query(value = "SELECT r FROM RequestWorkFlowDetailsEntity r where r.deleteFlag ='0' "
			+ "  AND r.reqWorkFlowId =:workFlowId ")
	public List<RequestWorkFlowDetailsEntity> findWorkFlowDetails(@Param("workFlowId") Integer workFlowId);
	

}
