package com.srm.rta.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.srm.coreframework.dao.CommonDAO;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.util.DateUtil;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.rta.controller.RequestResolverController;
import com.srm.rta.entity.ExternalLinkEntity;
import com.srm.rta.entity.MoreApplicationEntity;
import com.srm.rta.vo.ApprovalVO;
import com.srm.rta.vo.RequestResolverVO;
import com.srm.rta.vo.RequestVO;

@Repository
public class DashboardDAO extends CommonDAO {
	
	private static final Logger logger = LogManager.getLogger(DashboardDAO.class);
	
	@Autowired
	ApprovalDAO approvalDAo;
	
	@Autowired
	RequestResolverDAO requestResolverDAO;
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Integer> getRequestStatus(AuthDetailsVo authDetailsVo) {
		
		String MYREQUEST_INSIGHTS_QUERY = "SELECT stat.currentStatusName,COUNT(req.currentStatusId) "
				+ "FROM RequestEntity req " + "	,CurrentStatusEntity stat "
				+ "  Where stat.currentStatusId = req.currentStatusId and req.deleteFlag = " + CommonConstant.FLAG_ZERO;

		if (authDetailsVo.getRoleId().equals(CommonConstant.SUPER_ADMIN_ID)) {
			MYREQUEST_INSIGHTS_QUERY = MYREQUEST_INSIGHTS_QUERY + " and req.entityLicenseId = "
					+ authDetailsVo.getEntityId();
		} else {
			MYREQUEST_INSIGHTS_QUERY = MYREQUEST_INSIGHTS_QUERY + " and req.createBy = " + authDetailsVo.getUserId();
		}

		MYREQUEST_INSIGHTS_QUERY = MYREQUEST_INSIGHTS_QUERY
				+ " GROUP BY req.currentStatusId order by stat.currentStatusId desc ";

		List<Object[]> requestStatusList = getEntityManager().createQuery(MYREQUEST_INSIGHTS_QUERY).getResultList();

		HashMap<String, Integer> requestStatusMap = new HashMap<String, Integer>();

		if (requestStatusList != null && requestStatusList.size() > 0) {
			for (Object[] requestStatus : requestStatusList) {
				if (requestStatus != null) {
					requestStatusMap.put(requestStatus[0].toString(), Integer.parseInt(requestStatus[1].toString()));
				}
			}
		}
		return requestStatusMap;

	}

	@SuppressWarnings("unchecked")
	public List<ExternalLinkEntity> getAll(AuthDetailsVo authDetailsVo) {
		String query = "FROM ExternalLinkEntity where deleteFlag = " + CommonConstant.FLAG_ZERO
				+ " and rin_ma_entity_id = " + authDetailsVo.getEntityId() 
				+ " and externalLinkIsActive = " + CommonConstant.ACTIVE
				+ " ORDER BY externalLinkDisplaySeq DESC ";
		List<ExternalLinkEntity> externalLinkEntityList = (List<ExternalLinkEntity>) getEntityManager().createQuery(query)
				.getResultList();

		return externalLinkEntityList;
	}

	//Commented since not used while checking for remarks
	/*@SuppressWarnings("unchecked")
	public List<Object[]> findRequest(AuthDetailsVo authDetailsVo) {
		String query = " SELECT DISTINCT audit.*, req.rin_tr_request_sequence,"
				+ " sla.rin_ma_req_workflow_sla_type,sla.rin_ma_req_workflow_sla"
				+ " FROM rin_tr_req_workflow_audit audit "
				+ " LEFT JOIN rin_tr_request req ON req.idrin_tr_request_id = audit.rin_tr_request_id "
				+ " LEFT JOIN rin_ma_req_workflow workflow ON workflow.idrin_ma_req_workflow_id = audit.rin_ma_req_workflow_id"
				+ " LEFT JOIN rin_ma_req_workflow_seq seq "
				+ " ON workflow.idrin_ma_req_workflow_id = seq.rin_ma_req_workflow_id "
				+ " LEFT JOIN rin_ma_req_workflow_sla sla ON sla.rin_ma_req_workflow_prototype = req.rin_tr_request_priority"
				+ " AND workflow.idrin_ma_req_workflow_id = sla.rin_ma_req_workflow_id"
				+ " WHERE req.current_status_id IN (2,5) AND audit.delete_flag = '"+CommonConstant.FLAG_ZERO+"'"
				+ " AND audit.rin_tr_req_workflow_audit_descision_type = 0 "
				+ " AND req.rin_tr_request_sequence LIKE CONCAT('%,',audit.rin_tr_req_workflow_audit_sequence,',%')"
				+ " AND audit.rin_tr_req_workflow_audit_approval_executer"
				+ " NOT  IN (3) AND audit.create_date <= DATE_SUB(NOW(), INTERVAL -90 DAY)"
				+ " AND audit.create_date >= DATE_ADD(NOW(), INTERVAL -90 DAY) "
				+ " AND audit.rin_tr_req_workflow_audit_user_id = " + authDetailsVo.getUserId();

		List<Object[]> request = (List<Object[]>) getEntityManager().createNativeQuery(query).getResultList();
		return request;

	}*/

	public RequestVO findId(int id) {
		String query = "FROM RequestEntity where deleteFlag = " + CommonConstant.FLAG_ZERO + " AND id = " + id;
		RequestVO requestEntity = (RequestVO) getEntityManager().createQuery(query).getSingleResult();
		return requestEntity;
	}

	public Date findauditse(int sequence, int requestId) {
		String query = " select updateDate  FROM RequestWorkFlowAuditEntity where deleteFlag = "
				+ CommonConstant.FLAG_ZERO + " AND requestId = " + requestId + "and sequence" + sequence;
		Date requestDetailEntity = (Date) getEntityManager().createQuery(query).getSingleResult();

		return requestDetailEntity;

	}

	@SuppressWarnings("unchecked")
	public List<MoreApplicationEntity> getAllMoreApplication(AuthDetailsVo authDetailsVo) {
		String query = "FROM MoreApplicationEntity where deleteFlag = " + CommonConstant.FLAG_ZERO 
				+ " AND rin_ma_entity_id = " + authDetailsVo.getEntityId()
				+ " ORDER BY id DESC ";

		List<MoreApplicationEntity> moreApplicationEntityList = (List<MoreApplicationEntity>) getEntityManager()
				.createQuery(query).getResultList();

		return moreApplicationEntityList;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getSlaInsight(AuthDetailsVo authDetailsVo){
		
		String query = "SELECT c.rin_ma_current_status_name,c.idrin_ma_current_status_id,"
				+ " COUNT(c.rin_ma_current_status_name) count"
				+ " FROM rin_tr_req_workflow_audit a"
				+ " LEFT JOIN rin_tr_request r ON r.idrin_tr_request_id = a.rin_tr_request_id"
				+ " JOIN rin_ma_current_status c ON c.idrin_ma_current_status_id = r.current_status_id"
				+ " WHERE c.rin_ma_current_status_code IN ('COM','PEN','ESC','APP','RO','CLO') "
				+ " AND a.rin_tr_req_workflow_audit_user_id = " + authDetailsVo.getUserId() 
				//+ " AND a.rin_tr_req_workflow_audit_approval_executer!=3"
				+ " AND  a.`rin_tr_req_workflow_audit_is_active` = 1 "
				+ " AND a.`rin_tr_req_workflow_audit_descision_type` != 9 "
				+ " AND r.rin_ma_entity_id =" + authDetailsVo.getEntityId()
				+ " GROUP BY c.rin_ma_current_status_name";
		
		List<Object[]> slaInsights = (List<Object[]>) getEntityManager()
				.createNativeQuery(query).getResultList();

		return slaInsights;
		
	}

	
	@SuppressWarnings("unchecked")
	public List<Object[]> findRequest(int id,AuthDetailsVo authDetailsVo) {

		String query = "SELECT req.idrin_tr_request_id,req.rin_tr_request_code,req.rin_tr_request_date,rtype.rin_ma_request_type_name,"
				+ " rstype.rin_ma_request_subtype_name,loca.USER_LOCATION_NAME,sub.rin_ma_sublocation_name,depart.USER_DEPARTMENT_NAME, "
				+ " cur.rin_ma_current_status_name,req.rin_ma_request_type_id,req.rin_ma_request_subtype_id,"
				+ " req.rin_tr_request_user_location_id,req.rin_tr_request_sublocation_id,req.rin_tr_request_user_department_id,"
				+ " req.current_status_id,cur.rin_ma_current_status_code,CONCAT(ur.FIRST_NAME,' ',ur.LAST_NAME),req.rin_tr_request_subject,req.rin_tr_request_priority "
				+ " ,loc.USER_LOCATION_NAME reqloc,sl.rin_ma_sublocation_name reqSubloc,req.rin_tr_request_location_id,req.rin_tr_request_sub_location_id "
				+ " FROM "+getRtaDatabaseSchema()+".rin_tr_request req "
				+ " LEFT JOIN "+getRtaDatabaseSchema()+".rin_ma_request_type rtype ON req.rin_ma_request_type_id = rtype.idrin_ma_request_type_id "
				+ " LEFT JOIN "+getRtaDatabaseSchema()+".rin_ma_request_subtype rstype ON req.rin_ma_request_subtype_id = rstype.idrin_ma_request_subtype_id "
				+ " LEFT JOIN "+ getCommonDatabaseSchema()+".user_location loca ON req.rin_tr_request_user_location_id = loca.USER_LOCATION_ID "
				+ " LEFT JOIN "+getCommonDatabaseSchema()+".user_location loc ON req.rin_tr_request_location_id = loc.USER_LOCATION_ID "
				+ " LEFT JOIN "+getCommonDatabaseSchema()+".rin_ma_sublocation sl ON req.rin_tr_request_sub_location_id = sl.idrin_ma_sublocation_sublocationId "
				+ " LEFT JOIN "+getCommonDatabaseSchema()+".rin_ma_sublocation sub ON req.rin_tr_request_sublocation_id = sub.idrin_ma_sublocation_sublocationId "
				+ " LEFT JOIN "+getCommonDatabaseSchema()+".user_department depart ON req.rin_tr_request_user_department_id = depart.USER_DEPARTMENT_ID "
				+ " LEFT JOIN "+getRtaDatabaseSchema()+".rin_ma_current_status cur ON req.current_status_id = cur.idrin_ma_current_status_id "
				+ " LEFT JOIN "+getCommonDatabaseSchema()+".user ur ON req.create_by = ur.USER_ID" + "   WHERE  req.delete_flag = "
				+ CommonConstant.FLAG_ZERO + " and cur.idrin_ma_current_status_id = " + id + " and req.create_by = "
				+ authDetailsVo.getUserId()+ " ORDER BY req.idrin_tr_request_id DESC ";		

		List<Object[]> resultobject = null;

		resultobject = (List<Object[]>) getEntityManager().createNativeQuery(query).getResultList();

		return resultobject;

	}
	
	
	@SuppressWarnings("unchecked")
	public int getAllApprovalCount(AuthDetailsVo authDetailsVo) {
		
		    int count = 0;
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
			
			StringBuffer allUsers = approvalDAo.getApprovalListUser(authDetailsVo);
			
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
					//+ " AND Date(request.create_date) BETWEEN '" + listfromDate + "' AND  '" + listtoDate + "' "
					+ " GROUP BY request.idrin_tr_request_id ORDER BY request.idrin_tr_request_id DESC  ";


		List<Object[]> totalCount = (List<Object[]>) getEntityManager().createNativeQuery(query).getResultList();

		if (null != totalCount && !totalCount.isEmpty()) {
			count = totalCount.size();
		}
		return count;

	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getAllApproval(ApprovalVO approvalVO,AuthDetailsVo authDetailsVo) {				
 
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
		
		StringBuffer allUsers = approvalDAo.getApprovalListUser(authDetailsVo);
		

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
				//+ " AND Date(request.create_date) BETWEEN '" + listfromDate + "' AND  '" + listtoDate + "' "
				+ " GROUP BY request.idrin_tr_request_id ORDER BY request.idrin_tr_request_id DESC  ";

		List<Object[]> requestEntityList = (List<Object[]>) getEntityManager().createNativeQuery(query).getResultList();
		
		if(requestEntityList!=null && !requestEntityList.isEmpty() ){

		return requestEntityList;
		}
		else {
			return null;
		}

	}
	
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getAwaitingResolverList(RequestResolverVO requestResolverVO,AuthDetailsVo authDetailsVo) {
 
	/*	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String fromDate = null;
		String toDate = null;
 
		if (requestResolverVO.getListFromDate() != null) {
			fromDate = formatter.format(requestResolverVO.getListFromDate());
		} else {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -1);
			Date result = cal.getTime();

			try {
				fromDate = formatter.format(result);
			} catch (Exception e) {
				e.getMessage();
			}
		}

		if (requestResolverVO.getListToDate() != null) {
			toDate = formatter.format(requestResolverVO.getListToDate());

		} else {

			toDate = formatter.format(DateUtil.getCalenderDate());
		}
		*/
		StringBuffer allUsers =  getResolverIdList(authDetailsVo);
		
		String query = "SELECT CONCAT(userdetail.FIRST_NAME,' ',userdetail.LAST_NAME),req.idrin_tr_request_id,req.rin_tr_request_code,rtype.idrin_ma_request_type_id,"
				+ " rtype.rin_ma_request_type_name,rstype.idrin_ma_request_subtype_id,rstype.rin_ma_request_subtype_name,req.rin_tr_request_date, "
				+ " depart.USER_DEPARTMENT_ID,depart.USER_DEPARTMENT_NAME,loca.USER_LOCATION_ID, loca.USER_LOCATION_NAME,sub.idrin_ma_sublocation_sublocationId, "
				+ " sub.rin_ma_sublocation_name,cur.idrin_ma_current_status_id, cur.rin_ma_current_status_name, audit.idrin_tr_req_workflow_audit_id, "
				+ " audit.rin_ma_req_workflow_id,audit.rin_tr_req_workflow_seq_id,audit.rin_tr_req_workflow_audit_user_id, "
				+ " audit.rin_tr_req_workflow_audit_group_id,audit.rin_tr_req_workflow_audit_sequence,audit.rin_tr_req_workflow_audit_descision_type, "
				+ " audit.rin_tr_req_workflow_audit_approval_executer,audit.rin_tr_req_workflow_audit_remarks,userdetail.LAST_NAME "
				+ " , req.create_by , req.forward_redirect_remarks , req.forward_request_id,req.rin_tr_subrequest, req.remarks , cur.rin_ma_current_status_code"
				+ " FROM  "+getRtaDatabaseSchema()+".rin_tr_request req  "
				//+ " JOIN "+getCommonDatabaseSchema()+".user_location loca ON   req.rin_tr_request_user_location_id = loca.USER_LOCATION_ID  "
				//+ " JOIN "+getCommonDatabaseSchema()+".rin_ma_sublocation sub  ON req.rin_tr_request_sublocation_id = sub.idrin_ma_sublocation_sublocationId "
				
			+ " JOIN "+getCommonDatabaseSchema()+".user_location loca ON   req.rin_tr_request_location_id = loca.USER_LOCATION_ID  "
			+ " JOIN "+getCommonDatabaseSchema()+".rin_ma_sublocation sub  ON req.rin_tr_request_sub_location_id = sub.idrin_ma_sublocation_sublocationId "

				+ " JOIN "+getRtaDatabaseSchema()+".rin_ma_request_type rtype ON req.rin_ma_request_type_id = rtype.idrin_ma_request_type_id "
				+ " JOIN "+getRtaDatabaseSchema()+".rin_ma_request_subtype rstype ON req.rin_ma_request_subtype_id = rstype.idrin_ma_request_subtype_id "
				+ " JOIN "+getCommonDatabaseSchema()+".user_department depart ON req.rin_tr_request_user_department_id = depart.USER_DEPARTMENT_ID "
				+ " JOIN "+getRtaDatabaseSchema()+".rin_ma_current_status cur ON req.current_status_id = cur.idrin_ma_current_status_id  "
				+ " JOIN "+getCommonDatabaseSchema()+".user userdetail ON req.create_by = userdetail.USER_ID "
				+ " JOIN "+getRtaDatabaseSchema()+".rin_tr_req_workflow_audit audit  ON req.idrin_tr_request_id  = audit.rin_tr_request_id  "

				+ " WHERE req.rin_ma_entity_id = '" + authDetailsVo.getEntityId() + "' and req.delete_flag = "
				+ CommonConstant.FLAG_ZERO + " " + " AND cur.delete_flag = " + CommonConstant.FLAG_ZERO
				+ " AND audit.rin_tr_req_workflow_audit_approval_executer=2 " ;
				
			if ( !authDetailsVo.getUserId().equals(CommonConstant.SUPER_ADMIN_ID)) {
					//query = query + " AND audit.rin_tr_req_workflow_audit_user_id IN(" + allUsers + ")" ;
					
					query = query + "  AND (audit.rin_tr_req_workflow_audit_user_id = "+authDetailsVo.getUserId()+" OR "
				    	     + " ( audit.rin_tr_req_workflow_audit_user_id  IN(" + allUsers + ") AND audit.`rin_tr_req_workflow_audit_descision_type` != 5) ) ";
					
				}
				
				query= query 	
				+ " AND req.current_status_id   IN ( 5 ,8,9,10,14)"
				+ " AND cur.rin_ma_current_status_code NOT IN ('COM','CLO') " + " AND req.rin_tr_request_is_cancel = 0 "
				+ " AND audit.rin_tr_req_workflow_audit_descision_type!=9 "
				+ " AND audit.rin_tr_req_workflow_audit_is_active = " + CommonConstant.CONSTANT_ONE
			//	+ " AND Date(req.create_date) BETWEEN '" + fromDate + "' AND '" + toDate + "'"

				+ " GROUP BY req.idrin_tr_request_id order by  req.idrin_tr_request_id DESC";

		List<Object[]> listRequestEntity = (List<Object[]>) getEntityManager().createNativeQuery(query).getResultList();

		return listRequestEntity;

	}
	
	
	@SuppressWarnings("unchecked")
	public int getResolverListCount(AuthDetailsVo authDetailsVo) {
		/*SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");

		Date date = new Date();
		String currentDate = "";
		currentDate = (formatter1.format(date));
		String delegationQuery = "SELECT u.idrin_ma_delegation_userid FROM rin_ma_user_delegation u  "
				+ " LEFT JOIN rin_ma_user_delegation_details ud "
				+ " ON u.idrin_ma_user_delegation_id = ud.idrin_ma_user_delegation_id "
				+ " WHERE ud.idrin_ma_delegated_user_id = " + authDetailsVo.getUserId() + " "
				+ " AND ud.idrin_ma_user_type = 2 AND" + " ud.idrin_ma_user_active_from<='" + currentDate
				+ "' AND ud.idrin_ma_user_active_to>='" + currentDate + "' "
				+ " and ud.delete_flag = "+CommonConstant.FLAG_ZERO
				+ " GROUP BY u.idrin_ma_user_delegation_id ";

		List<Integer> resultUser = (List<Integer>) getEntityManager().createNativeQuery(delegationQuery)
				.getResultList();

		StringBuffer allUsers = new StringBuffer();
		 
			
			if(resultUser.size() == 0){
			allUsers.append('0');
			}else{
			
			for (Integer single : resultUser) {
				allUsers.append(',');
				allUsers.append(single);
			}
			allUsers.replace(0, 1, "");
			}		 
			
			//allUsers.append(authDetailsVo.getUserId());
		 

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String fromDate = new String("");
		String toDate = new String("");
		if (requestResolverVO != null) {
			if (requestResolverVO.getListFromDate() != null) {
				fromDate = formatter.format(requestResolverVO.getListFromDate());
			} else {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.MONTH, -1);
				Date result = cal.getTime();
 
				try {
					fromDate = formatter.format(result);
				} catch (Exception e) {
					logger.error("error", e);
				}
		//	}

			if (requestResolverVO.getListToDate() != null) {
				toDate = formatter.format(requestResolverVO.getListToDate());

			} else {

				toDate = formatter.format(DateUtil.getCalenderDate());
			//}
*/		 

		StringBuffer allUsers =  getResolverIdList(authDetailsVo);
		
		String query = "SELECT CONCAT(userdetail.FIRST_NAME,' ',userdetail.LAST_NAME),req.idrin_tr_request_id,req.rin_tr_request_code,rtype.idrin_ma_request_type_id,"
				+ " rtype.rin_ma_request_type_name,rstype.idrin_ma_request_subtype_id,rstype.rin_ma_request_subtype_name,req.rin_tr_request_date, "
				+ " depart.USER_DEPARTMENT_ID,depart.USER_DEPARTMENT_NAME,loca.USER_LOCATION_ID, loca.USER_LOCATION_NAME,sub.idrin_ma_sublocation_sublocationId, "
				+ " sub.rin_ma_sublocation_name,cur.idrin_ma_current_status_id, cur.rin_ma_current_status_name, audit.idrin_tr_req_workflow_audit_id, "
				+ " audit.rin_ma_req_workflow_id,audit.rin_tr_req_workflow_seq_id,audit.rin_tr_req_workflow_audit_user_id, "
				+ " audit.rin_tr_req_workflow_audit_group_id,audit.rin_tr_req_workflow_audit_sequence,audit.rin_tr_req_workflow_audit_descision_type, "
				+ " audit.rin_tr_req_workflow_audit_approval_executer,audit.rin_tr_req_workflow_audit_remarks,userdetail.LAST_NAME "
				+ " , req.create_by , req.forward_redirect_remarks , req.forward_request_id,req.rin_tr_subrequest, req.remarks"
				+ " FROM  "+getRtaDatabaseSchema()+".rin_tr_request req  "
				//+ " JOIN "+getCommonDatabaseSchema()+".user_location loca ON   req.rin_tr_request_user_location_id = loca.USER_LOCATION_ID  "
				//+ " JOIN "+getCommonDatabaseSchema()+".rin_ma_sublocation sub  ON req.rin_tr_request_sublocation_id = sub.idrin_ma_sublocation_sublocationId "
				
			+ " JOIN "+getCommonDatabaseSchema()+".user_location loca ON   req.rin_tr_request_location_id = loca.USER_LOCATION_ID  "
			+ " JOIN "+getCommonDatabaseSchema()+".rin_ma_sublocation sub  ON req.rin_tr_request_sub_location_id = sub.idrin_ma_sublocation_sublocationId "

				+ " JOIN "+getRtaDatabaseSchema()+".rin_ma_request_type rtype ON req.rin_ma_request_type_id = rtype.idrin_ma_request_type_id "
				+ " JOIN "+getRtaDatabaseSchema()+".rin_ma_request_subtype rstype ON req.rin_ma_request_subtype_id = rstype.idrin_ma_request_subtype_id "
				+ " JOIN "+getCommonDatabaseSchema()+".user_department depart ON req.rin_tr_request_user_department_id = depart.USER_DEPARTMENT_ID "
				+ " JOIN "+getRtaDatabaseSchema()+".rin_ma_current_status cur ON req.current_status_id = cur.idrin_ma_current_status_id  "
				+ " JOIN "+getCommonDatabaseSchema()+".user userdetail ON req.create_by = userdetail.USER_ID "
				+ " JOIN "+getRtaDatabaseSchema()+".rin_tr_req_workflow_audit audit  ON req.idrin_tr_request_id  = audit.rin_tr_request_id  "

				+ " WHERE req.rin_ma_entity_id = '" + authDetailsVo.getEntityId() + "' and req.delete_flag = "
				+ CommonConstant.FLAG_ZERO + " " + " AND cur.delete_flag = " + CommonConstant.FLAG_ZERO
				+ " AND audit.rin_tr_req_workflow_audit_approval_executer=2 " ;
				
			if ( !authDetailsVo.getUserId().equals(CommonConstant.SUPER_ADMIN_ID)) {
					//query = query + " AND audit.rin_tr_req_workflow_audit_user_id IN(" + allUsers + ")" ;
					
					query = query + "  AND (audit.rin_tr_req_workflow_audit_user_id = "+authDetailsVo.getUserId()+" OR "
				    	     + " ( audit.rin_tr_req_workflow_audit_user_id  IN(" + allUsers + ") AND audit.`rin_tr_req_workflow_audit_descision_type` != 5) ) ";
					
				}
				
				query= query 	
				+ " AND req.current_status_id   IN ( 5 ,8,9,10,14)"
				+ " AND cur.rin_ma_current_status_code NOT IN ('COM','CLO') " + " AND req.rin_tr_request_is_cancel = 0 "
				+ " AND audit.rin_tr_req_workflow_audit_descision_type!=9 "
				+ " AND audit.rin_tr_req_workflow_audit_is_active = " + CommonConstant.CONSTANT_ONE
				//+ " AND Date(req.create_date) BETWEEN '" + fromDate + "' AND '" + toDate + "'"

				+ " GROUP BY req.idrin_tr_request_id order by  req.idrin_tr_request_id DESC";

		List<Object[]> totalCount = new ArrayList<>();
		try {
			totalCount = (List<Object[]>) getEntityManager().createNativeQuery(query).getResultList();
		} catch (Exception e) {
			 
		}
	 
		int count = 0 ;
		if (null != totalCount && !totalCount.isEmpty()) {
			 count = totalCount.size();
		}	
		
		return count;
	}
	
	
	public StringBuffer getResolverIdList(AuthDetailsVo authDetailsVo) {

		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");

		Date date = new Date();
		String currentDate = "";
		currentDate = (formatter1.format(date));
		String delegationQuery = "SELECT u.idrin_ma_delegation_userid FROM rin_ma_user_delegation u  "
				+ " LEFT JOIN rin_ma_user_delegation_details ud "
				+ " ON u.idrin_ma_user_delegation_id = ud.idrin_ma_user_delegation_id "
				+ " WHERE ud.idrin_ma_delegated_user_id = " + authDetailsVo.getUserId() + " "
				+ " AND ud.idrin_ma_user_type = 2 AND" + " ud.idrin_ma_user_active_from<='" + currentDate
				+ "' AND ud.idrin_ma_user_active_to>='" + currentDate + "' " + " and ud.delete_flag = "
				+ CommonConstant.FLAG_ZERO
				+ " AND u.rin_ma_entity_id = "+  authDetailsVo.getEntityId()
				+ " and ud.idrin_ma_user_delegation_active = 1"
				+ " GROUP BY u.idrin_ma_user_delegation_id ";

		@SuppressWarnings("unchecked")
		List<Integer> resultUser = (List<Integer>) getEntityManager().createNativeQuery(delegationQuery)
				.getResultList();

		StringBuffer allUsers = new StringBuffer();
		try {
			if (resultUser.size() == 0) {
				allUsers.append('0');
			} else {

				for (Integer single : resultUser) {
					allUsers.append(',');
					allUsers.append(single);
				}
				allUsers.replace(0, 1, "");
			}

			// allUsers.append(authDetailsVo.getUserId());
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
      return allUsers;
	}
}
