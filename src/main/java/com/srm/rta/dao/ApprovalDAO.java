package com.srm.rta.dao;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.srm.coreframework.dao.CommonDAO;
import com.srm.coreframework.entity.UserMappingEntity;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.util.DateUtil;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.rta.vo.ApprovalVO;
import com.srm.rta.vo.RequestVO;
import com.srm.rta.vo.RequestWorkFlowAuditVO;

@Repository
public class ApprovalDAO extends CommonDAO {

	Logger logger = LoggerFactory.getLogger(ApprovalDAO.class);

	/**
	 * Method is used to get all the approval list
	 * 
	 * @param approvalVO
	 * @return requestEntityList
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getAll(AuthDetailsVo authDetailsVo) {
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		String listfromDate = new String("");
		String listtoDate = new String("");

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		Date result = cal.getTime();

		try {
			listfromDate = formatter.format(result);
		} catch (Exception e) {
			logger.error("error", e);
		}

	    listtoDate = formatter.format(CommonConstant.getCalenderDate());		
		
		StringBuffer allUsers = getApprovalListUser(authDetailsVo);
		

		  String query = " SELECT request.idrin_tr_request_id , request.rin_tr_request_code, request.current_status_id," //2
				+ " curstatus.rin_ma_current_status_code ,curstatus.rin_ma_current_status_name,"//4
				+ " reqtype.idrin_ma_request_type_id , reqtype.rin_ma_request_type_name , subtype.idrin_ma_request_subtype_id ,"//7
				+ " subtype.rin_ma_request_subtype_name , us.USER_ID , CONCAT(us.FIRST_NAME,' ',us.LAST_NAME) , request.create_date ,"//11
				+ " audit.idrin_tr_req_workflow_audit_id , audit.rin_ma_req_workflow_id , audit.rin_tr_req_workflow_seq_id ,"//14
				+ " audit.rin_tr_req_workflow_audit_group_id , audit.rin_tr_req_workflow_audit_sequence ,"//16
				+ " audit.rin_tr_req_workflow_audit_descision_type,"//17
				+ " audit.rin_tr_req_workflow_audit_approval_executer , audit.rin_tr_req_workflow_audit_remarks,"//19
				+ " request.rin_tr_request_priority,request.rin_tr_request_subject , request.remarks,request.forward_redirect_remarks ,"
				+ " loc.USER_LOCATION_NAME , subloc.rin_ma_sublocation_name ,dept.USER_DEPARTMENT_NAME ,request.`forward_request_id`,request.`rin_tr_subrequest`  "//21
				+ " FROM "+getRtaDatabaseSchema()+".rin_tr_request request "
				+ " JOIN "+getRtaDatabaseSchema()+".rin_tr_req_workflow_audit audit ON "
				+ " request.idrin_tr_request_id = audit.rin_tr_request_id "
				+ " LEFT JOIN "+getRtaDatabaseSchema() +".rin_ma_request_type reqtype ON"
				+ " request.rin_ma_request_type_id = reqtype.idrin_ma_request_type_id "
				+ " LEFT JOIN "+getRtaDatabaseSchema()+".rin_ma_request_subtype subtype ON"
				+ " request.rin_ma_request_subtype_id = subtype.idrin_ma_request_subtype_id"
				+ " LEFT JOIN "+getCommonDatabaseSchema()+".user us ON"
				+ " request.create_by = us.USER_ID "
				+ " LEFT JOIN "+getRtaDatabaseSchema()+".rin_ma_current_status curstatus ON"
				+ " request.current_status_id = curstatus.idrin_ma_current_status_id "
				
				+ " LEFT JOIN "+getCommonDatabaseSchema()+".user_location loc ON loc.USER_LOCATION_ID = request.rin_tr_request_location_id"
				+ " LEFT JOIN "+getCommonDatabaseSchema()+".rin_ma_sublocation subloc ON subloc.idrin_ma_sublocation_sublocationId = request.rin_tr_request_sub_location_id"

				
				/*+ " LEFT JOIN "+getCommonDatabaseSchema()+".user_location loc ON loc.USER_LOCATION_ID = request.rin_tr_request_user_location_id"
				+ " LEFT JOIN "+getCommonDatabaseSchema()+".rin_ma_sublocation subloc ON subloc.idrin_ma_sublocation_sublocationId = request.rin_tr_request_sublocation_id"
				*/+ " LEFT JOIN "+getCommonDatabaseSchema()+".user_department dept ON dept.USER_DEPARTMENT_ID = request.rin_tr_request_user_department_id"	
				
				+ " WHERE request.rin_ma_entity_id = '"
				+ authDetailsVo.getEntityId() + "'"
				+ " AND audit.rin_tr_req_workflow_audit_is_active = 1 AND reqtype.delete_flag = " + CommonConstant.FLAG_ZERO + " AND subtype.delete_flag =" + CommonConstant.FLAG_ZERO + " AND request.rin_tr_request_is_cancel = '0'"
				+ " AND request.delete_flag = " + CommonConstant.FLAG_ZERO + " AND audit.delete_flag = " + CommonConstant.FLAG_ZERO ;
				
		  		if ( !authDetailsVo.getUserId().equals(CommonConstant.SUPER_ADMIN_ID)) {
					query = query	+ " AND audit.rin_tr_req_workflow_audit_user_id IN("+allUsers+") " ;
				}
			
		query = query 
				+ " AND request.current_status_id = 2 AND audit.rin_tr_req_workflow_audit_approval_executer = 1"
				+ " AND audit.rin_tr_req_workflow_audit_descision_type = 0 AND request.rin_tr_request_sequence LIKE CONCAT('%,',audit.rin_tr_req_workflow_audit_sequence,',%')"
				+ " AND Date(request.create_date) BETWEEN '" + listfromDate + "' AND  '" + listtoDate + "' "
				+ " GROUP BY request.idrin_tr_request_id ORDER BY request.idrin_tr_request_id DESC  ";

		List<Object[]> requestEntityList = new ArrayList<>();

		try {
			requestEntityList = (List<Object[]>) getEntityManager().createNativeQuery(query).getResultList();
		} catch (Exception e) {
			logger.error("error", e);
		}

		return requestEntityList;

	}

	public StringBuffer getApprovalListUser(AuthDetailsVo authDetailsVo){

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		 Date date = new Date();  
		 String currentDate = "";
		 currentDate =  (formatter.format(date)); 		
		
		String delegationQuery = "SELECT u.idrin_ma_delegation_userid FROM rin_ma_user_delegation u  "
				+ " LEFT JOIN rin_ma_user_delegation_details ud "
				+ " ON u.idrin_ma_user_delegation_id = ud.idrin_ma_user_delegation_id "
				+ " WHERE ud.idrin_ma_delegated_user_id = " + authDetailsVo.getUserId()+ " "
				+ " AND ud.idrin_ma_user_type = 1 AND"
				+ " ud.idrin_ma_user_active_from<='"+currentDate+"' AND ud.idrin_ma_user_active_to>='"+currentDate+"' "
				+ " and ud.delete_flag = "+CommonConstant.FLAG_ZERO
				+ " and u.rin_ma_entity_id = " + authDetailsVo.getEntityId()
				+ " and ud.idrin_ma_user_delegation_active = 1"
				+ " GROUP BY u.idrin_ma_user_delegation_id ";

		List<Integer> resultUser = (List<Integer>) getEntityManager().createNativeQuery(delegationQuery).getResultList();

		StringBuffer allUsers = new StringBuffer();
		try {
			for (Integer single : resultUser) {

				allUsers.append(single);
				allUsers.append(',');

			}

			allUsers.append(authDetailsVo.getUserId());
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
		
		return allUsers;
	}
	
	
	/**
	 * Method is used to get approval list count.
	 * 
	 * @return count int
	 */
	@SuppressWarnings("unchecked")
	public int getAllCount(AuthDetailsVo authDetailsVo) {

		int id = authDetailsVo.getUserId();

		int count = 0;

		String query = " SELECT request.idrin_tr_request_id , request.rin_tr_request_code, request.current_status_id,"
				+ " curstatus.rin_ma_current_status_code ,curstatus.rin_ma_current_status_name,"
				+ " reqtype.idrin_ma_request_type_id , reqtype.rin_ma_request_type_name , subtype.idrin_ma_request_subtype_id ,"
				+ "subtype.rin_ma_request_subtype_name , us.USER_ID , CONCAT(us.FIRST_NAME,' ',us.LAST_NAME) , request.create_date ,"
				+ " audit.idrin_tr_req_workflow_audit_id , audit.rin_ma_req_workflow_id , audit.rin_tr_req_workflow_seq_id ,"
				+ " audit.rin_tr_req_workflow_audit_group_id , audit.rin_tr_req_workflow_audit_sequence , audit.rin_tr_req_workflow_audit_descision_type,"
				+ " audit.rin_tr_req_workflow_audit_approval_executer , audit.rin_tr_req_workflow_audit_remarks,request.rin_tr_request_priority,request.rin_tr_request_subject"
				+ " FROM rin_tr_request request JOIN rin_tr_req_workflow_audit audit ON "
				+ " request.idrin_tr_request_id = audit.rin_tr_request_id LEFT JOIN rin_ma_request_type reqtype ON"
				+ " request.rin_ma_request_type_id = reqtype.idrin_ma_request_type_id LEFT JOIN rin_ma_request_subtype subtype ON"
				+ " request.rin_ma_request_subtype_id = subtype.idrin_ma_request_subtype_id LEFT JOIN common_rta_2_local.user us ON"
				+ " audit.rin_tr_req_workflow_audit_user_id = us.USER_ID LEFT JOIN rin_ma_current_status curstatus ON"
				+ " request.current_status_id = curstatus.idrin_ma_current_status_id WHERE request.rin_ma_entity_id = '"
				+ authDetailsVo.getEntityId() + "'"
				+ " AND audit.rin_tr_req_workflow_audit_is_active = 1 AND reqtype.delete_flag =" + CommonConstant.FLAG_ZERO + " AND subtype.delete_flag = " + CommonConstant.FLAG_ZERO + " AND request.rin_tr_request_is_cancel = '0'"
				+ " AND request.delete_flag = " + CommonConstant.FLAG_ZERO + " AND audit.delete_flag = " + CommonConstant.FLAG_ZERO + " AND audit.rin_tr_req_workflow_audit_user_id = "
				+ id + " AND request.current_status_id = 2 AND audit.rin_tr_req_workflow_audit_approval_executer = 1"
				+ " AND audit.rin_tr_req_workflow_audit_descision_type = 0 AND request.rin_tr_request_sequence LIKE CONCAT('%,',audit.rin_tr_req_workflow_audit_sequence,',%')"
				+ " GROUP BY request.idrin_tr_request_id ORDER BY request.idrin_tr_request_id DESC  ";

		List<Object[]> totalCount = (List<Object[]>) getEntityManager().createNativeQuery(query).getResultList();

		if (null != totalCount && !totalCount.isEmpty()) {
			count = totalCount.size();
		}
		return count;

	}

	/**
	 * Method is used for Load the Approval List
	 * 
	 * @param requestVo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> load(RequestWorkFlowAuditVO requestWorkFlowAuditVo,AuthDetailsVo authDetailsVo) {
				
		String delegationQuery = "SELECT u.idrin_ma_delegation_userid FROM rin_ma_user_delegation u  "
				+ " LEFT JOIN rin_ma_user_delegation_details ud "
				+ " ON u.idrin_ma_user_delegation_id = ud.idrin_ma_user_delegation_id "
				+ " WHERE ud.idrin_ma_delegated_user_id = " + authDetailsVo.getUserId()
				+ " GROUP BY u.idrin_ma_user_delegation_id ";

		List<Integer> resultUser = (List<Integer>) getEntityManager().createNativeQuery(delegationQuery).getResultList();

		StringBuffer allUsers = new StringBuffer();
		try {
			for (Integer single : resultUser) {

				allUsers.append(single);
				allUsers.append(',');

			}
		
			allUsers.append(authDetailsVo.getUserId());
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
		

		String query = "SELECT CONCAT(u.FIRST_NAME,' ',u.LAST_NAME),c.idrin_tr_req_workflow_audit_id,c.rin_ma_req_workflow_id,c.rin_tr_req_workflow_seq_id,"
				+ "c.rin_tr_request_id,c.rin_tr_req_workflow_audit_user_id,c.rin_tr_req_workflow_audit_group_id,"
				+ "c.rin_tr_req_workflow_audit_sequence,c.rin_tr_req_workflow_audit_descision_type,c.rin_tr_req_workflow_audit_approval_executer,"
				+ "c.rin_tr_req_workflow_audit_reassign_flag,c.rin_tr_req_workflow_audit_reassign_user_id, "
				+ "c.rin_tr_req_workflow_audit_remarks,c.rin_tr_req_workflow_audit_is_active,c.update_date,c.create_date,c.rin_tr_req_workflow_audit_sla, "
				+ " c.rin_tr_req_workflow_audit_forward_remarks"
				+ " FROM "+getRtaDatabaseSchema()+".rin_tr_req_workflow_audit c "
				+ " JOIN "+getCommonDatabaseSchema()+".user u ON c.rin_tr_req_workflow_audit_user_id = u.USER_ID "
				+ " WHERE c.rin_ma_entity_id = '" + authDetailsVo.getEntityId() + "' AND c.delete_flag  = '"
				+ CommonConstant.FLAG_ZERO + "' and c.rin_tr_request_id = " + requestWorkFlowAuditVo.getRequestId()
				// + " and c.rin_tr_req_workflow_audit_descision_type = 0 "
				//+ " and c.rin_tr_req_workflow_audit_user_id IN("+allUsers+")  " 
				+ " GROUP BY c.idrin_tr_req_workflow_audit_id ";

		List<Object[]> requestEntity = new ArrayList<>();
		try {
			requestEntity = (List<Object[]>) getEntityManager().createNativeQuery(query).getResultList();
		} catch (Exception e) {
			logger.error("error", e);
		}
		return requestEntity;

	}

	public int getbuttonCount(RequestVO requestVo,AuthDetailsVo authDetailsVo) {

		String query = " SELECT COUNT(request.idrin_tr_request_id) FROM rin_tr_request request JOIN rin_tr_req_workflow_audit audit  WHERE"
				+ " request.rin_ma_entity_id = '" + authDetailsVo.getEntityId()
				+ "' AND audit.rin_tr_req_workflow_audit_is_active = 1 AND audit.delete_flag = " + CommonConstant.FLAG_ZERO + " AND audit.rin_tr_req_workflow_audit_user_id = "
				+ authDetailsVo.getUserId()
				+ " AND request.current_status_id = 2 AND audit.rin_tr_req_workflow_audit_descision_type = 0 AND request.idrin_tr_request_id = "
				+ requestVo.getRequestWorkFlowAuditVo().getRequestId()
				+ " AND request.rin_tr_request_sequence LIKE CONCAT('%,',audit.rin_tr_req_workflow_audit_sequence,',%')";

		BigInteger countSecond = (BigInteger) getEntityManager().createNativeQuery(query).getSingleResult();

		return countSecond.intValue();
	}

	/**
	 * Method is used for List all the approval
	 * 
	 * @param requestVo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> loadAll(RequestWorkFlowAuditVO requestWorkFlowAuditVo,AuthDetailsVo authDetailsVo) {
		/*String query = "SELECT CONCAT(u.FIRST_NAME,' ',u.LAST_NAME),c.idrin_tr_req_workflow_audit_id,c.rin_ma_req_workflow_id,c.rin_tr_req_workflow_seq_id,"
				+ "c.rin_tr_request_id,c.rin_tr_req_workflow_audit_user_id,c.rin_tr_req_workflow_audit_group_id,"
				+ "c.rin_tr_req_workflow_audit_sequence,c.rin_tr_req_workflow_audit_descision_type,c.rin_tr_req_workflow_audit_approval_executer,"
				+ "c.rin_tr_req_workflow_audit_reassign_flag,c.rin_tr_req_workflow_audit_reassign_user_id, "
				+ "c.rin_tr_req_workflow_audit_remarks,c.rin_tr_req_workflow_audit_is_active,c.update_date,c.create_date,c.rin_tr_req_workflow_audit_sla "
				+ " FROM `rta_2_local`.rin_tr_req_workflow_audit c "
				+ " JOIN `common_rta_2_local`.`user` u ON c.rin_tr_req_workflow_audit_user_id = u.USER_ID "
				+ " WHERE c.rin_ma_entity_id = '" + authDetailsVo.getEntityId()
				+ "' AND (c.rin_tr_req_workflow_audit_descision_type!=0 AND c.rin_tr_req_workflow_audit_descision_type!=9) AND  "
				+ " c.delete_flag  = '" + CommonConstant.FLAG_ZERO + "' and c.rin_tr_request_id= "
				+ requestWorkFlowAuditVo.getRequestId() + " and c.rin_tr_req_workflow_audit_user_id !=  "
				+ authDetailsVo.getUserId() + " ORDER BY c.rin_tr_req_workflow_audit_sequence ASC ";*/
		
		String query = "SELECT CONCAT(u.FIRST_NAME,' ',u.LAST_NAME),c.idrin_tr_req_workflow_audit_id,c.rin_ma_req_workflow_id,c.rin_tr_req_workflow_seq_id,"
				+ "c.rin_tr_request_id,c.rin_tr_req_workflow_audit_user_id,c.rin_tr_req_workflow_audit_group_id,"
				+ "c.rin_tr_req_workflow_audit_sequence,c.rin_tr_req_workflow_audit_descision_type,c.rin_tr_req_workflow_audit_approval_executer,"
				+ "c.rin_tr_req_workflow_audit_reassign_flag,c.rin_tr_req_workflow_audit_reassign_user_id, "
				+ "c.rin_tr_req_workflow_audit_remarks,c.rin_tr_req_workflow_audit_is_active,c.update_date,c.create_date,c.rin_tr_req_workflow_audit_sla "
				+ " FROM "+getRtaDatabaseSchema()+".rin_tr_req_workflow_audit c "
				+ " JOIN "+getCommonDatabaseSchema()+".`user` u ON c.rin_tr_req_workflow_audit_user_id = u.USER_ID "
				+ " WHERE c.rin_ma_entity_id = '" + authDetailsVo.getEntityId()
				+ "' AND  "
				+ " c.delete_flag  = '" + CommonConstant.FLAG_ZERO + "' and c.rin_tr_request_id= "
				+ requestWorkFlowAuditVo.getRequestId() 
				/*+ " and c.rin_tr_req_workflow_audit_user_id !=  "	+ authDetailsVo.getUserId() */
				+ " ORDER BY c.rin_tr_req_workflow_audit_sequence ASC ";

		List<Object[]> listRequestEntity = new ArrayList<>();
		try {
			listRequestEntity = (List<Object[]>) getEntityManager().createNativeQuery(query).getResultList();
		} catch (Exception e) {
			logger.error("error", e);
		}
		return listRequestEntity;

	}

	/**
	 * Method is used to get all the searches
	 * 
	 * @param requestVo
	 * @return search
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getAllSearch(ApprovalVO approvalVO,AuthDetailsVo authDetailsVo) {
		int id = authDetailsVo.getUserId();
 		 
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String fromDate = null;
		String toDate = null;

		Date result = null;
		Date tresult = null;
		
		if (null != approvalVO.getFlag()) {
			if (2 == approvalVO.getFlag()) {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, -45);
				result = cal.getTime();
				fromDate = formatter.format(result);

				Calendar tcal = Calendar.getInstance();
				tcal.add(Calendar.DATE, -30);
				tresult = tcal.getTime();

				toDate = formatter.format(tresult);
			} else if (3 == approvalVO.getFlag()) {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, -90);
				result = cal.getTime();
				fromDate = formatter.format(result);

				Calendar tcal = Calendar.getInstance();
				tcal.add(Calendar.DATE, -45);
				tresult = tcal.getTime();

				toDate = formatter.format(tresult);
			} else if (4 == approvalVO.getFlag()) {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.YEAR, -1);
				result = cal.getTime();
				fromDate = formatter.format(result);

				Calendar tcal = Calendar.getInstance();
				tcal.add(Calendar.DATE, -90);
				tresult = tcal.getTime();

				toDate = formatter.format(tresult);
			} else {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.MONTH, -1);
				result = cal.getTime();

				toDate = formatter.format(CommonConstant.getCalenderDate());

				// toDate = result.toString();
				try {
					fromDate = formatter.format(result);
				} catch (Exception e) {
					e.getMessage();
				}
			}
		} else {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -1);
			result = cal.getTime();

			toDate = formatter.format(CommonConstant.getCalenderDate());

			// toDate = result.toString();
			try {
				fromDate = formatter.format(result);
			} catch (Exception e) {
				e.getMessage();
			}
		}
		
		StringBuffer allUsers = getApprovalListUser(authDetailsVo);
		
		String query = "SELECT request.idrin_tr_request_id , request.rin_tr_request_code, request.current_status_id,"
				+ " curstatus.rin_ma_current_status_code ,curstatus.rin_ma_current_status_name,"
				+ " reqtype.idrin_ma_request_type_id , reqtype.rin_ma_request_type_name , subtype.idrin_ma_request_subtype_id ,"
				+ "subtype.rin_ma_request_subtype_name , us.USER_ID , CONCAT(us.FIRST_NAME,' ',us.LAST_NAME) , request.create_date ,"
				+ " audit.idrin_tr_req_workflow_audit_id , audit.rin_ma_req_workflow_id , audit.rin_tr_req_workflow_seq_id ,"
				+ " audit.rin_tr_req_workflow_audit_group_id , audit.rin_tr_req_workflow_audit_sequence , audit.rin_tr_req_workflow_audit_descision_type,"
				+ " audit.rin_tr_req_workflow_audit_approval_executer , audit.rin_tr_req_workflow_audit_remarks,request.rin_tr_request_priority,request.rin_tr_request_subject"
				+ " , request.remarks,request.forward_redirect_remarks , loc.USER_LOCATION_NAME , subloc.rin_ma_sublocation_name ,dept.USER_DEPARTMENT_NAME "
				+ " , request.`forward_request_id`,request.`rin_tr_subrequest` FROM rin_tr_request request "
				+ " JOIN rin_tr_req_workflow_audit audit ON  request.idrin_tr_request_id = audit.rin_tr_request_id "
				+ " LEFT JOIN rin_ma_request_type reqtype ON request.rin_ma_request_type_id = reqtype.idrin_ma_request_type_id "
				+ " LEFT JOIN rin_ma_request_subtype subtype ON request.rin_ma_request_subtype_id = subtype.idrin_ma_request_subtype_id "
				+ " LEFT JOIN "+getCommonDatabaseSchema()+".user us ON audit.rin_tr_req_workflow_audit_user_id = us.USER_ID "
				+ " LEFT JOIN rin_ma_current_status curstatus ON request.current_status_id = curstatus.idrin_ma_current_status_id "
				+ " LEFT JOIN "+getCommonDatabaseSchema()+".user_location loc ON loc.USER_LOCATION_ID = request.rin_tr_request_location_id"
				+ " LEFT JOIN "+getCommonDatabaseSchema()+".rin_ma_sublocation subloc ON subloc.idrin_ma_sublocation_sublocationId = request.rin_tr_request_sub_location_id"
				+ " LEFT JOIN "+getCommonDatabaseSchema()+".user_department dept ON dept.USER_DEPARTMENT_ID = request.rin_tr_request_user_department_id"					
				+ " WHERE request.rin_ma_entity_id = '"
				+ authDetailsVo.getEntityId() + "'"
				+ " AND audit.rin_tr_req_workflow_audit_is_active = 1 AND reqtype.delete_flag = " + CommonConstant.FLAG_ZERO + " AND subtype.delete_flag =" + CommonConstant.FLAG_ZERO + " AND request.rin_tr_request_is_cancel = '0'"
				+ " AND request.delete_flag = " + CommonConstant.FLAG_ZERO + " AND audit.delete_flag = " + CommonConstant.FLAG_ZERO ;
				
		  		if ( !authDetailsVo.getUserId().equals(CommonConstant.SUPER_ADMIN_ID)) {
					query = query	+ " AND audit.rin_tr_req_workflow_audit_user_id IN("+allUsers+") " ;
				}
			
		query = query 
				+ " AND request.current_status_id = 2 AND audit.rin_tr_req_workflow_audit_approval_executer = 1"
				+ " AND audit.rin_tr_req_workflow_audit_descision_type = 0 AND request.rin_tr_request_sequence LIKE CONCAT('%,',audit.rin_tr_req_workflow_audit_sequence,',%')"
				+ " AND Date(request.create_date) BETWEEN '" + fromDate + "' AND  '" + toDate + "' " ;
				
		StringBuilder modifiedQuery = new StringBuilder(query);
 
		if (approvalVO.getLocationName() != null) {
			modifiedQuery.append(
					" AND loc.USER_LOCATION_NAME like '%" + approvalVO.getLocationName() + "%'");
		}
		
		if (approvalVO.getSublocationName() != null) {
			modifiedQuery.append(
			" AND subloc.rin_ma_sublocation_name like '%" + approvalVO.getSublocationName() + "%'");
		}  

		if (approvalVO.getUserDepartmentName() != null) {
			modifiedQuery.append(
			" AND dept.USER_DEPARTMENT_NAME like '%" + approvalVO.getUserDepartmentName() + "%'");
		}  

		if (approvalVO.getRequestCode() != null) {
			modifiedQuery.append(" AND request.rin_tr_request_code like '%" + approvalVO.getRequestCode() + "%'");
		}
		if (approvalVO.getRequestDate() != null) {
			modifiedQuery.append(" AND request.rin_tr_request_date like '%"
					+ DateUtil.dateToString(approvalVO.getRequestDate(), "yyyy-MM-dd") + "%'");
		}
		if (approvalVO.getRequestTypeName() != null) {
			modifiedQuery
					.append(" AND reqtype.rin_ma_request_type_name like '%" + approvalVO.getRequestTypeName() + "%'");
		}
		if (approvalVO.getRequestSubTypeName() != null) {
			modifiedQuery.append(
					" AND subtype.rin_ma_request_subtype_name like '%" + approvalVO.getRequestSubTypeName() + "%'");
		}
		if (approvalVO.getCurrentStatusName() != null) {
			modifiedQuery.append(
					" AND curstatus.rin_ma_current_status_name like '%" + approvalVO.getCurrentStatusName() + "%'");
		}
		if (approvalVO.getUserName() != null) {
			modifiedQuery
					.append(" AND CONCAT(us.FIRST_NAME,' ',us.LAST_NAME) like '%" + approvalVO.getUserName() + "%'");
		}
		modifiedQuery.append(" AND audit.rin_tr_req_workflow_audit_descision_type = 0" );

		modifiedQuery.append(" GROUP BY request.idrin_tr_request_id ORDER BY request.idrin_tr_request_id DESC  ");

		List<Object[]> search = new ArrayList<>();
		try {
			search = (List<Object[]>) getEntityManager().createNativeQuery(modifiedQuery.toString()).getResultList();
		} catch (Exception e) {
			logger.error("error", e);
		}
		return search;

	}

	/**
	 * Get all the approval for summary
	 * 
	 * @param requestVo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getAllHistory(RequestVO requestVo, List<Integer> subOrdinate,AuthDetailsVo authDetailsVo) {
		int id = authDetailsVo.getUserId();

		String subOrdinateFirst = subOrdinate.toString().replace("[", "");

		String subOrdinateSecond = subOrdinateFirst.replace("]", "");

		if ("".equals(subOrdinateSecond)) {
			subOrdinateSecond = "0";
		}

		int limit = 10;

		int offset = 0;
		if (requestVo != null) {
			if (requestVo.getPageLimit() != 0) {
				limit = requestVo.getPageLimit();
			} else {
				requestVo.setPageLimit(limit);
			}

			if (requestVo.getPageNo() != 0) {
				offset = requestVo.getPageNo() * limit;
			}
		}

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String fromDate = new String("");
		String toDate = new String("");
		if (requestVo != null) {
			if (requestVo.getListFromDate() != null) {
				fromDate = formatter.format(requestVo.getListFromDate());
			} else {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.MONTH, -1);
				Date result = cal.getTime();

				try {
					fromDate = formatter.format(result);
				} catch (Exception e) {
					logger.error("error", e);
				}
			}

			if (requestVo.getListToDate() != null) {
				toDate = formatter.format(requestVo.getListToDate());

			} else {

				toDate = formatter.format(CommonConstant.getCalenderDate());
			}
		}

		String query = " SELECT request.idrin_tr_request_id , request.rin_tr_request_code, request.current_status_id,"
				+ " curstatus.rin_ma_current_status_code ,curstatus.rin_ma_current_status_name,"
				+ " reqtype.idrin_ma_request_type_id , reqtype.rin_ma_request_type_name , subtype.idrin_ma_request_subtype_id ,"
				+ "subtype.rin_ma_request_subtype_name , us.USER_ID , CONCAT(us.FIRST_NAME,' ',us.LAST_NAME), request.create_date ,"
				+ " audit.idrin_tr_req_workflow_audit_id , audit.rin_ma_req_workflow_id , audit.rin_tr_req_workflow_seq_id ,"
				+ " audit.rin_tr_req_workflow_audit_group_id , audit.rin_tr_req_workflow_audit_sequence , audit.rin_tr_req_workflow_audit_descision_type,"
				+ " audit.rin_tr_req_workflow_audit_approval_executer , audit.rin_tr_req_workflow_audit_remarks,request.rin_tr_request_priority,request.rin_tr_request_subject"
				+ " FROM rin_tr_request request JOIN rin_tr_req_workflow_audit audit ON "
				+ " request.idrin_tr_request_id = audit.rin_tr_request_id "
				+ " LEFT JOIN rin_ma_request_type reqtype ON"
				+ " request.rin_ma_request_type_id = reqtype.idrin_ma_request_type_id "
				+ " LEFT JOIN rin_ma_request_subtype subtype ON"
				+ " request.rin_ma_request_subtype_id = subtype.idrin_ma_request_subtype_id "
				+ " LEFT JOIN "+getCommonDatabaseSchema()+".user us ON request.create_by = us.USER_ID"
				+ " LEFT JOIN rin_ma_current_status curstatus ON request.current_status_id = curstatus.idrin_ma_current_status_id WHERE"
				+ " request.rin_ma_entity_id = '" + authDetailsVo.getEntityId()
				+ "' AND audit.rin_tr_req_workflow_audit_is_active = 1 AND reqtype.delete_flag = " + CommonConstant.FLAG_ZERO + " AND subtype.delete_flag = " + CommonConstant.FLAG_ZERO + " "
				+ " AND request.rin_tr_request_is_cancel = '0' AND request.delete_flag = " + CommonConstant.FLAG_ZERO + " AND audit.delete_flag = " + CommonConstant.FLAG_ZERO + " "
				+ " AND (audit.rin_tr_req_workflow_audit_user_id = " + id + " OR request.create_by IN ("
				+ subOrdinateSecond + "))" + " AND Date(request.create_date) BETWEEN '" + fromDate + "' AND '" + toDate
				+ "' " + " GROUP BY request.idrin_tr_request_id ORDER BY request.idrin_tr_request_id DESC  ";

		List<Object[]> requestEntityList = new ArrayList<>();

		try {
			requestEntityList = (List<Object[]>) getEntityManager().createNativeQuery(query).setFirstResult(offset)
					.setMaxResults(limit).getResultList();
		} catch (Exception e) {
			logger.error("error", e);
		}
		return requestEntityList;

	}

	/**
	 * Get all the approval for summary
	 * 
	 * @param requestVo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getAllHistorySearch(RequestVO requestVo, List<Integer> subOrdinate,AuthDetailsVo authDetailsVo) {
		int id = authDetailsVo.getUserId();

		String subOrdinateFirst = subOrdinate.toString().replace("[", "");

		String subOrdinateSecond = subOrdinateFirst.replace("]", "");

		if ("".equals(subOrdinateSecond)) {
			subOrdinateSecond = "0";
		}

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String fromDate = new String("");
		String toDate = new String("");

		if (requestVo != null) {
			if (requestVo.getListFromDate() != null) {
				fromDate = formatter.format(requestVo.getListFromDate());
			} else {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.MONTH, -1);
				Date result = cal.getTime();

				try {
					fromDate = formatter.format(result);
				} catch (Exception e) {
					logger.error("error", e);
				}
			}

			if (requestVo.getListToDate() != null) {
				toDate = formatter.format(requestVo.getListToDate());

			} else {

				toDate = formatter.format(CommonConstant.getCalenderDate());
			}
		}
		String query = " SELECT request.idrin_tr_request_id , request.rin_tr_request_code, request.current_status_id,"
				+ " curstatus.rin_ma_current_status_code ,curstatus.rin_ma_current_status_name,"
				+ " reqtype.idrin_ma_request_type_id , reqtype.rin_ma_request_type_name , subtype.idrin_ma_request_subtype_id ,"
				+ "subtype.rin_ma_request_subtype_name , us.USER_ID , CONCAT(us.FIRST_NAME,' ',us.LAST_NAME) , request.create_date ,"
				+ " audit.idrin_tr_req_workflow_audit_id , audit.rin_ma_req_workflow_id , audit.rin_tr_req_workflow_seq_id ,"
				+ " audit.rin_tr_req_workflow_audit_group_id , audit.rin_tr_req_workflow_audit_sequence , audit.rin_tr_req_workflow_audit_descision_type,"
				+ " audit.rin_tr_req_workflow_audit_approval_executer , audit.rin_tr_req_workflow_audit_remarks,request.rin_tr_request_priority,request.rin_tr_request_subject"
				+ " FROM rin_tr_request request "
				+ " JOIN rin_tr_req_workflow_audit audit ON  request.idrin_tr_request_id = audit.rin_tr_request_id "
				+ " LEFT JOIN rin_ma_request_type reqtype ON  request.rin_ma_request_type_id = reqtype.idrin_ma_request_type_id "
				+ " LEFT JOIN rin_ma_request_subtype subtype ON"
				+ " request.rin_ma_request_subtype_id = subtype.idrin_ma_request_subtype_id "
				+ " LEFT JOIN "+getCommonDatabaseSchema()+".user us ON  request.create_by = us.USER_ID LEFT JOIN rin_ma_current_status curstatus ON"
				+ " request.current_status_id = curstatus.idrin_ma_current_status_id "
				+ " WHERE request.rin_ma_entity_id = '"
				+ authDetailsVo.getEntityId() + "' AND"
				+ " audit.rin_tr_req_workflow_audit_is_active = 1 AND reqtype.delete_flag = " + CommonConstant.FLAG_ZERO + " AND subtype.delete_flag = " + CommonConstant.FLAG_ZERO + " AND request.rin_tr_request_is_cancel = '0'"
				+ " AND request.delete_flag = " + CommonConstant.FLAG_ZERO + " AND audit.delete_flag = " + CommonConstant.FLAG_ZERO + " "
				+ " AND Date(request.create_date) BETWEEN'" + fromDate + "' AND '" + toDate + "' "
				+ " AND (audit.rin_tr_req_workflow_audit_user_id = " + id + " OR request.create_by IN ("
				+ subOrdinateSecond + "))";

		StringBuilder modifiedQuery = new StringBuilder(query);

		if (requestVo.getRequestCode() != null) {
			modifiedQuery.append(" AND request.rin_tr_request_code like '%" + requestVo.getRequestCode() + "%'");
		}
		if (requestVo.getRequestDate() != null) {
			modifiedQuery.append(" AND request.rin_tr_request_date like '%"
					+ DateUtil.dateToString(requestVo.getRequestDate(), "yyyy-MM-dd") + "%'");
		}
		if (requestVo.getRequestTypeName() != null) {
			modifiedQuery
					.append(" AND reqtype.rin_ma_request_type_name like '%" + requestVo.getRequestTypeName() + "%'");
		}
		if (requestVo.getRequestSubTypeName() != null) {
			modifiedQuery.append(
					" AND subtype.rin_ma_request_subtype_name like '%" + requestVo.getRequestSubTypeName() + "%'");
		}
		if (requestVo.getRequestSubject() != null) {
			modifiedQuery.append(" AND request.rin_tr_request_subject like '%" + requestVo.getRequestSubject() + "%'");
		}
		if (requestVo.getRequestPriority() != 0) {
			modifiedQuery
					.append(" AND request.rin_tr_request_priority like '%" + requestVo.getRequestPriority() + "%'");
		}
		if (0 != requestVo.getCurrentStatusId()) {
			modifiedQuery.append(" AND request.current_status_id = " + requestVo.getCurrentStatusId());
		}

		modifiedQuery.append(" GROUP BY request.idrin_tr_request_id ORDER BY request.idrin_tr_request_id DESC  ");

		List<Object[]> search = new ArrayList<>();

		try {
			search = (List<Object[]>) getEntityManager().createNativeQuery(modifiedQuery.toString()).getResultList();
		} catch (Exception e) {
			logger.error("error", e);
		}
		return search;

	}

	/**
	 * Method is used to add list of users
	 * 
	 * @param currentLoginUserId
	 * @return usermappingEntityList
	 */
	@SuppressWarnings("unchecked")
	public List<UserMappingEntity> getListOfUser(int currentLoginUserId,AuthDetailsVo authDetailsVo) {

		String query = " From UserMappingEntity where deleteFlag = " + CommonConstant.FLAG_ONE
				+ " AND reportingToUser.id = " + currentLoginUserId + "AND entityLicenseEntity.id ="
				+ authDetailsVo.getEntityId();

		List<UserMappingEntity> usermappingEntityList = new ArrayList<>();
		try {
			usermappingEntityList = (List<UserMappingEntity>) getEntityManager().createQuery(query).getResultList();
		} catch (Exception e) {
			logger.error("error", e);
		}
		return usermappingEntityList;
	}

}
