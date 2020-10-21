package com.srm.rta.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.srm.coreframework.constants.DecisionTypeEnum;
import com.srm.coreframework.dao.CommonDAO;
import com.srm.coreframework.util.CommonConstant;

 

/**
 * This Dao class is used to get the escalation list of approval and resolver.
 * 
 * @author vigneshs
 *
 */
@Repository
public class EscalationDao extends CommonDAO {

	Logger logger = LoggerFactory.getLogger(EscalationDao.class);

	/**
	 * This method is used to get all approval list
	 * 
	 * @return requestEntityList List<Object>
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getAllPendingList() {

		String query = "SELECT audit.*, req.update_date reqUpdate ,req.rin_tr_request_code, w.rin_ma_req_workflow_is_mgt_escaltion_required FROM rin_tr_req_workflow_audit audit "
				+ " JOIN rin_tr_request req ON audit.rin_tr_request_id = req.idrin_tr_request_id"
				+ " JOIN rin_ma_req_workflow w ON w.idrin_ma_req_workflow_id = audit.rin_ma_req_workflow_id"
				+ " WHERE req.rin_tr_request_sequence LIKE CONCAT('%,',audit.rin_tr_req_workflow_audit_sequence,',%')"
				+ " AND req.rin_tr_request_is_cancel = " + CommonConstant.CONSTANT_ZERO + " AND audit.delete_flag = "
				+ CommonConstant.CONSTANT_ZERO + " AND audit.rin_tr_req_workflow_audit_descision_type = "
				+ CommonConstant.CONSTANT_ZERO
				+ " AND w.rin_ma_req_workflow_is_mgt_escaltion_required = 1";
				//+ " ORDER BY audit.`idrin_tr_req_workflow_audit_id` DESC LIMIT 1";

		List<Object> objList = new ArrayList<>();
		objList = (List<Object>) getEntityManager().createNativeQuery(query).getResultList();

		return objList;

	}

	/**
	 * This method is used to get all the resolver list
	 * 
	 * @return requestEntityList List<Object>
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getAllApprovedList() {

		String query = " SELECT audit.*, req.update_date reqUpdate,req.rin_tr_request_code FROM rin_tr_req_workflow_audit audit "
				+ " JOIN rin_tr_request req ON audit.rin_tr_request_id = req.idrin_tr_request_id"
				+ " JOIN rin_ma_current_status c ON req.current_status_id = c.idrin_ma_current_status_id"
				+ " WHERE c.rin_ma_current_status_code = " + DecisionTypeEnum.APP.toString()
				+ " AND audit.rin_tr_req_workflow_audit_approval_executer = " + CommonConstant.CONSTANT_TWO
				+ " AND req.rin_tr_request_is_cancel = " + CommonConstant.CONSTANT_ZERO + " AND audit.delete_flag = "
				+ CommonConstant.CONSTANT_ZERO + " AND audit.rin_tr_req_workflow_audit_descision_type = "
				+ CommonConstant.CONSTANT_ZERO;

		List<Object> objList = new ArrayList<>();
		objList = (List<Object>) getEntityManager().createNativeQuery(query).getResultList();

		return objList;
	}

}
