package com.srm.rta.dao;

import org.springframework.stereotype.Repository;

import com.srm.coreframework.dao.CommonDAO;
import com.srm.coreframework.util.DateUtil;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.rta.entity.RequestWorkFlowAuditEntity;
import com.srm.rta.vo.RequestVO;

@Repository
public class ForwardRequestDAO extends CommonDAO{

	public void updateCurrentStatusInRequest(String currentStatusCode,
			RequestWorkFlowAuditEntity requestWorkFlowAuditEntity,AuthDetailsVo authDetailsVo) {

		int currentStatusId = findStatusId(currentStatusCode);
	 
		String query = "update RequestEntity  set  currentStatusId = " + currentStatusId + ""
				+ " ,updateBy = "
				+ authDetailsVo.getUserId() + " ,updateDate = ' " + DateUtil.getCurrentDateTimeStr() + " ' "
				+ " where requestId = " + requestWorkFlowAuditEntity.getRequestId();
		 	
		
		getEntityManager().createQuery(query).executeUpdate();

	}

	public int findStatusId(String status) {

		String query = "SELECT currentStatusId from CurrentStatusEntity where currentStatusCode = '" + status + "'";

		int id = (int) getEntityManager().createQuery(query).getSingleResult();

		return id;

	}
	
	public void updateAudit(RequestWorkFlowAuditEntity requestWorkFlowAuditEntity,AuthDetailsVo authDetailsVo) {
		
		String query = "UPDATE `rin_tr_req_workflow_audit` audit "
				+ " SET audit.rin_tr_req_workflow_audit_descision_type = 8 "
				+ " ,update_by = "
				+ authDetailsVo.getUserId() + " ,update_date = ' " + DateUtil.getCurrentDateTimeStr() + " ' "
				+ " WHERE audit.rin_tr_request_id = "
				+ requestWorkFlowAuditEntity.getRequestId() + " "
				+ " AND audit.rin_tr_req_workflow_audit_approval_executer =2 "
				+ " AND audit.rin_tr_req_workflow_audit_user_id != " + authDetailsVo.getUserId() + "";

		getEntityManager().createNativeQuery(query).executeUpdate();
	}
		
	public void updateRemarks(RequestVO requestVo,AuthDetailsVo authDetailsVo) {
	 	 	
		String query = "update RequestEntity  set forward_redirect_remarks = '"+ requestVo.getForwardRedirectRemarks()+"' ,updateBy = "
				+ authDetailsVo.getUserId() + " ,updateDate = ' " + DateUtil.getCurrentDateTimeStr() + " ' "
				+ " where requestId = " + requestVo.getForwardRequestId();
		 			
		getEntityManager().createQuery(query).executeUpdate();

	}



}
