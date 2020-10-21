package com.srm.rta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.srm.rta.entity.RequestWorkFlowSlaEntity;

public interface RequestWorkFlowSlaRepository extends JpaRepository<RequestWorkFlowSlaEntity, Integer> {

	@Query(value = "Select r FROM RequestWorkFlowSlaEntity r where r.deleteFlag ='0'"
			+ " AND r.reqWorkFlowId =:workFlowId " + "  AND r.reqWorkFlowExecuterId =:reqWorkFlowExecuterId")
	public List<RequestWorkFlowSlaEntity> findExecuterSlaId(@Param("workFlowId") Integer workFlowId,
			@Param("reqWorkFlowExecuterId") Integer reqWorkFlowExecuterId);

	@Query(value = "Select r FROM RequestWorkFlowSlaEntity r where r.deleteFlag ='0'"
			+ " AND r.reqWorkFlowId =:workFlowId " + "  AND r.reqWorkFlowSeqId =:reqWorkFlowSeqId")
	public List<RequestWorkFlowSlaEntity> findSlaId(@Param("workFlowId") Integer workFlowId,
			@Param("reqWorkFlowSeqId") Integer reqWorkFlowSeqId);

}
