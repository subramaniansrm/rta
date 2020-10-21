package com.srm.rta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.srm.rta.entity.RequestWorkFlowSeqEntity;

public interface RequestWorkFlowSequenceRepository extends JpaRepository<RequestWorkFlowSeqEntity, Integer> {
	
	
	@Query(value = "SELECT r FROM RequestWorkFlowSeqEntity r where r.deleteFlag ='0'" 
				+ " AND r.reqWorkFlowSeqIsActive = '1'" 
				+ " AND r.reqWorkFlowId =:workFlowId" 
				+ " ORDER BY r.reqWorkFlowSeqSequence ")
	public List<RequestWorkFlowSeqEntity> findWorkFlowId(@Param("workFlowId") Integer workFlowId);
	
	
	

}
