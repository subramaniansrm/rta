package com.srm.rta.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.srm.coreframework.auth.AuthUtil;
import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.dao.CommonDAO;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.util.DateUtil;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.rta.vo.RequestVO;
import com.srm.rta.vo.RequestWorkFlowAuditVO;

@Repository
public class SubRequestDAO extends CommonDAO {

	@Autowired
	RequestDAO requestDao;

	public void updateRequest(RequestWorkFlowAuditVO requestWorkFlowAuditVo, AuthDetailsVo authDetailsVo) throws CommonException , Exception{
		try {

			String query = "UPDATE rin_tr_request SET current_status_id = " + CommonConstant.CONSTANT_FOURTEEN
					+ " ,update_by = " + authDetailsVo.getUserId() + " ,update_date = ' "
					+ DateUtil.getCurrentDateTimeStr() + " ' " + " WHERE idrin_tr_request_id = "
					+ requestWorkFlowAuditVo.getRequestId();
			getEntityManager().createNativeQuery(query).executeUpdate();
		} catch (Exception e) {
			throw new CommonException("request_validation_executersNotAvailble");
		}
	}

	public void updateAudit(RequestWorkFlowAuditVO requestWorkFlowAuditVo, AuthDetailsVo authDetailsVo) throws CommonException , Exception{
		try {
		String query = "UPDATE `rin_tr_req_workflow_audit` audit "
				+ " SET audit.rin_tr_req_workflow_audit_descision_type = 9 "
				+ " ,update_by = "
				+ authDetailsVo.getUserId() + " ,update_date = ' " + DateUtil.getCurrentDateTimeStr() + " ' "
				+ " WHERE audit.rin_tr_request_id = "
				+ requestWorkFlowAuditVo.getRequestId() + " "
				+ " AND audit.rin_tr_req_workflow_audit_approval_executer =2 "
				+ " AND audit.rin_tr_req_workflow_audit_user_id!= " + authDetailsVo.getUserId() + "";

		getEntityManager().createNativeQuery(query).executeUpdate();
		} catch (Exception e) {

			throw new CommonException("dataFailure");
		}
	}

	public void updateAuditExecutor(RequestWorkFlowAuditVO requestWorkFlowAuditVo, AuthDetailsVo authDetailsVo) throws CommonException , Exception{

		try {
			String query = "UPDATE `rin_tr_req_workflow_audit` audit "
					+ " SET audit.rin_tr_req_workflow_audit_descision_type = 14 " + " ,update_by = "
					+ authDetailsVo.getUserId() + " ,update_date = ' " + DateUtil.getCurrentDateTimeStr() + " ' "
					+ " WHERE audit.rin_tr_request_id = " + requestWorkFlowAuditVo.getRequestId() + " "
					+ " AND audit.rin_tr_req_workflow_audit_approval_executer =2 "
					+ " AND audit.rin_tr_req_workflow_audit_user_id= " + authDetailsVo.getUserId() + "";
			getEntityManager().createNativeQuery(query).executeUpdate();
		} catch (Exception e) {

			throw new CommonException("request_validation_executersNotAvailble");
		}
	}

	public void updateRemarks(RequestVO requestVo,AuthDetailsVo authDetailsVo) throws CommonException{

		try {
		String query = "UPDATE rin_tr_request SET forward_redirect_remarks = '"+ requestVo.getForwardRedirectRemarks()+"' " 
				+ " ,update_by = "
				+ authDetailsVo.getUserId() + " ,update_date = ' " + DateUtil.getCurrentDateTimeStr() + " ' "
				+ " WHERE idrin_tr_request_id = " + requestVo.getRequestId();
		getEntityManager().createNativeQuery(query).executeUpdate();
		} catch (Exception e) {

			throw new CommonException("dataFailure");
		}
	}

	
}
