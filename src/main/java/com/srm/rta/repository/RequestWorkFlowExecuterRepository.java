package com.srm.rta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.srm.rta.entity.RequestWorkFlowExecuterEntity;

@Repository
public interface RequestWorkFlowExecuterRepository extends JpaRepository<RequestWorkFlowExecuterEntity, Integer> {

	@Query(value = "SELECT r FROM RequestWorkFlowExecuterEntity r where r.deleteFlag ='0'" 
				+ " AND r.reqWorkFlowId =:workFlowId")
	public List<RequestWorkFlowExecuterEntity> findWorkFlowExecuter(@Param("workFlowId") Integer workFlowId);

}
