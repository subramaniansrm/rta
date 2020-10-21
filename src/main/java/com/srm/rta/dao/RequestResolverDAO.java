package com.srm.rta.dao;

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
import com.srm.rta.entity.RequestEntity;
import com.srm.rta.vo.RequestResolverVO;
import com.srm.rta.vo.RequestVO;

@Repository
public class RequestResolverDAO extends CommonDAO {

	Logger logger = LoggerFactory.getLogger(RequestResolverDAO.class);

	/**
	 * Method used to get resolver List
	 * 
	 * @param requestVo
	 * @return
	 */

	public StringBuffer getResolverId(AuthDetailsVo authDetailsVo) {

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
				+ CommonConstant.FLAG_ZERO + " and ud.idrin_ma_user_delegation_active = 1"
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
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getResolverList(RequestResolverVO requestResolverVO, AuthDetailsVo authDetailsVo) {
				
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
			}

			if (requestResolverVO.getListToDate() != null) {
				toDate = formatter.format(requestResolverVO.getListToDate());

			} else {

				toDate = formatter.format(DateUtil.getCalenderDate());
			}
		}
				
		StringBuffer allUsers = getResolverId(authDetailsVo);
		
		String query = "SELECT CONCAT(userdetail.FIRST_NAME,' ',userdetail.LAST_NAME),req.idrin_tr_request_id,req.rin_tr_request_code,rtype.idrin_ma_request_type_id,"
				+ " rtype.rin_ma_request_type_name,rstype.idrin_ma_request_subtype_id,rstype.rin_ma_request_subtype_name,req.rin_tr_request_date, "
				+ " depart.USER_DEPARTMENT_ID,depart.USER_DEPARTMENT_NAME,loca.USER_LOCATION_ID, loca.USER_LOCATION_NAME,sub.idrin_ma_sublocation_sublocationId, "
				+ " sub.rin_ma_sublocation_name,cur.idrin_ma_current_status_id, cur.rin_ma_current_status_name, audit.idrin_tr_req_workflow_audit_id, "
				+ " audit.rin_ma_req_workflow_id,audit.rin_tr_req_workflow_seq_id,audit.rin_tr_req_workflow_audit_user_id, "
				+ " audit.rin_tr_req_workflow_audit_group_id,audit.rin_tr_req_workflow_audit_sequence,audit.rin_tr_req_workflow_audit_descision_type, "
				+ " audit.rin_tr_req_workflow_audit_approval_executer,audit.rin_tr_req_workflow_audit_remarks,userdetail.LAST_NAME "
				+ " , req.create_by , req.forward_redirect_remarks , req.forward_request_id,req.rin_tr_subrequest, req.remarks,cur.rin_ma_current_status_code"
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
				+ " AND Date(req.create_date) BETWEEN '" + fromDate + "' AND '" + toDate + "'"

				+ " GROUP BY req.idrin_tr_request_id order by  req.idrin_tr_request_id DESC";

		List<Object[]> listRequestEntity = new ArrayList<>();
		try {
			listRequestEntity = (List<Object[]>) getEntityManager().createNativeQuery(query).getResultList();
		} catch (Exception e) {
			logger.error("error", e);
		}
		return listRequestEntity;

	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getAwaitingResolverList(AuthDetailsVo authDetailsVo) {

		StringBuffer allUsers = getResolverId(authDetailsVo);
		
		String query = "SELECT CONCAT(userdetail.FIRST_NAME,' ',userdetail.LAST_NAME),req.idrin_tr_request_id,req.rin_tr_request_code,rtype.idrin_ma_request_type_id,"
				+ " rtype.rin_ma_request_type_name,rstype.idrin_ma_request_subtype_id,rstype.rin_ma_request_subtype_name,req.rin_tr_request_date, "
				+ " depart.USER_DEPARTMENT_ID,depart.USER_DEPARTMENT_NAME,loca.USER_LOCATION_ID, loca.USER_LOCATION_NAME,sub.idrin_ma_sublocation_sublocationId, "
				+ " sub.rin_ma_sublocation_name,cur.idrin_ma_current_status_id, cur.rin_ma_current_status_name, audit.idrin_tr_req_workflow_audit_id, "
				+ " audit.rin_ma_req_workflow_id,audit.rin_tr_req_workflow_seq_id,audit.rin_tr_req_workflow_audit_user_id, "
				+ " audit.rin_tr_req_workflow_audit_group_id,audit.rin_tr_req_workflow_audit_sequence,audit.rin_tr_req_workflow_audit_descision_type, "
				+ " audit.rin_tr_req_workflow_audit_approval_executer,audit.rin_tr_req_workflow_audit_remarks,userdetail.LAST_NAME"
				+ " FROM  "+getRtaDatabaseSchema()+".rin_tr_request req  "
				+ " JOIN "+getCommonDatabaseSchema()+".user_location loca ON   req.rin_tr_request_user_location_id = loca.USER_LOCATION_ID  "
				+ " JOIN "+getCommonDatabaseSchema()+".rin_ma_sublocation sub  ON req.rin_tr_request_sublocation_id = sub.idrin_ma_sublocation_sublocationId "
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
				+ " GROUP BY req.idrin_tr_request_id order by  req.idrin_tr_request_id DESC";

		List<Object[]> resolverList = new ArrayList<>();

		try {
			resolverList = (List<Object[]>) getEntityManager().createNativeQuery(query).getResultList();
		} catch (Exception e) {
			logger.error("error", e);
		}
		return resolverList;
	}

	/**
	 * Method is used to get the resolver list count.
	 * 
	 * @return count int
	 */
	@SuppressWarnings("unchecked")
	public int getResolverListCount(AuthDetailsVo authDetailsVo) {

		int count = 0;
		StringBuffer allUsers = getResolverId(authDetailsVo);
		
		String query = "SELECT CONCAT(userdetail.FIRST_NAME,' ',userdetail.LAST_NAME),req.idrin_tr_request_id,req.rin_tr_request_code,rtype.idrin_ma_request_type_id,"
				+ " rtype.rin_ma_request_type_name,rstype.idrin_ma_request_subtype_id,rstype.rin_ma_request_subtype_name,req.rin_tr_request_date, "
				+ " depart.USER_DEPARTMENT_ID,depart.USER_DEPARTMENT_NAME,loca.USER_LOCATION_ID, loca.USER_LOCATION_NAME,sub.idrin_ma_sublocation_sublocationId, "
				+ " sub.rin_ma_sublocation_name,cur.idrin_ma_current_status_id, cur.rin_ma_current_status_name, audit.idrin_tr_req_workflow_audit_id, "
				+ " audit.rin_ma_req_workflow_id,audit.rin_tr_req_workflow_seq_id,audit.rin_tr_req_workflow_audit_user_id, "
				+ " audit.rin_tr_req_workflow_audit_group_id,audit.rin_tr_req_workflow_audit_sequence,audit.rin_tr_req_workflow_audit_descision_type, "
				+ " audit.rin_tr_req_workflow_audit_approval_executer,audit.rin_tr_req_workflow_audit_remarks,userdetail.LAST_NAME"
				+ " FROM  "+getRtaDatabaseSchema()+".rin_tr_request req  "
				+ " JOIN "+getCommonDatabaseSchema()+".user_location loca ON   req.rin_tr_request_user_location_id = loca.USER_LOCATION_ID  "
				+ " JOIN "+getCommonDatabaseSchema()+".rin_ma_sublocation sub  ON req.rin_tr_request_sublocation_id = sub.idrin_ma_sublocation_sublocationId "
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
				+ " GROUP BY req.idrin_tr_request_id order by  req.idrin_tr_request_id DESC";
				
		List<Object[]> totalCount = (List<Object[]>) getEntityManager().createNativeQuery(query).getResultList();

		if (null != totalCount && !totalCount.isEmpty()) {
			count = totalCount.size();
		}
		return count;
	}

	/**
	 * 
	 * Method used to get all resolver list
	 * 
	 * @param requestVo
	 * @return
	 */

	@SuppressWarnings("unchecked")
	public List<Object[]> viewAllResolverList(RequestVO requestVo) {
		String query = "SELECT CONCAT(userdetail.FIRST_NAME,' ',userdetail.LAST_NAME),req.idrin_tr_request_id,req.rin_tr_request_code,rtype.idrin_ma_request_type_id, "
				+ " rtype.rin_ma_request_type_name,rstype.idrin_ma_request_subtype_id,rstype.rin_ma_request_subtype_name,req.rin_tr_request_date, "
				+ " depart.USER_DEPARTMENT_ID,depart.USER_DEPARTMENT_NAME,loca.USER_LOCATION_ID, loca.USER_LOCATION_NAME,sub.idrin_ma_sublocation_sublocationId, "
				+ " sub.rin_ma_sublocation_name,cur.idrin_ma_current_status_id, cur.rin_ma_current_status_name, audit.idrin_tr_req_workflow_audit_id, "
				+ " audit.rin_ma_req_workflow_id,audit.rin_tr_req_workflow_seq_id,audit.rin_tr_req_workflow_audit_user_id, "
				+ " audit.rin_tr_req_workflow_audit_group_id,audit.rin_tr_req_workflow_audit_sequence,"
				+ " audit.rin_tr_req_workflow_audit_descision_type, "
				+ " audit.rin_tr_req_workflow_audit_approval_executer,audit.rin_tr_req_workflow_audit_remarks,audit.update_date "
				+ " ,audit.create_date,audit.rin_tr_req_workflow_audit_sla,userdetail.LAST_NAME,"
				+ "  req.remarks , req.forward_redirect_remarks,req.rin_tr_subrequest"
				+ " FROM  "+getRtaDatabaseSchema()+".rin_tr_request req  "
				+ " JOIN "+getCommonDatabaseSchema()+".user_location loca ON   req.rin_tr_request_user_location_id = loca.USER_LOCATION_ID  "
				+ " JOIN "+getCommonDatabaseSchema()+".rin_ma_sublocation sub  ON req.rin_tr_request_sublocation_id = sub.idrin_ma_sublocation_sublocationId "
				+ " JOIN "+getRtaDatabaseSchema()+".rin_ma_request_type rtype ON req.rin_ma_request_type_id = rtype.idrin_ma_request_type_id "
				+ " JOIN "+getRtaDatabaseSchema()+".rin_ma_request_subtype rstype ON req.rin_ma_request_subtype_id = rstype.idrin_ma_request_subtype_id  "
				+ " JOIN "+getCommonDatabaseSchema()+".user_department depart ON req.rin_tr_request_user_department_id = depart.USER_DEPARTMENT_ID  "
				+ " JOIN "+getRtaDatabaseSchema()+".rin_ma_current_status cur ON req.current_status_id = cur.idrin_ma_current_status_id  "
				+ " JOIN "+getRtaDatabaseSchema()+".rin_tr_req_workflow_audit audit  ON req.idrin_tr_request_id  = audit.rin_tr_request_id "
				+ " JOIN "+getCommonDatabaseSchema()+".user userdetail ON audit.rin_tr_req_workflow_audit_user_id = userdetail.USER_ID  "
				+ "  WHERE audit.delete_flag  = " + CommonConstant.FLAG_ZERO + " AND  req.delete_flag = "
				+ CommonConstant.FLAG_ZERO + " AND audit.rin_tr_request_id= " + requestVo.getRequestId()
				+ " AND audit.rin_tr_req_workflow_audit_is_active = " + CommonConstant.CONSTANT_ONE
				+ "  AND audit.rin_tr_req_workflow_audit_approval_executer = 1 "
				+ " ORDER BY audit.idrin_tr_req_workflow_audit_id ";

		List<Object[]> listRequestEntity = new ArrayList<>();

		try {
			listRequestEntity = (List<Object[]>) getEntityManager().createNativeQuery(query).getResultList();
		} catch (Exception e) {
			logger.error("error", e);
		}

		return listRequestEntity;
	}

	/**
	 * 
	 * Method used to get all resolver list
	 * 
	 * @param requestVo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> load(RequestVO requestVo, AuthDetailsVo authDetailsVo) {

		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");

		Date date = new Date();
		String currentDate = "";
		currentDate = (formatter1.format(date));
		String delegationQuery = "SELECT u.idrin_ma_delegation_userid FROM rin_ma_user_delegation u  "
				+ " LEFT JOIN rin_ma_user_delegation_details ud "
				+ " ON u.idrin_ma_user_delegation_id = ud.idrin_ma_user_delegation_id "
				+ " WHERE ud.idrin_ma_delegated_user_id = " + authDetailsVo.getUserId() + " "
				+ " AND ud.idrin_ma_user_type = 2 AND" + " ud.idrin_ma_user_active_from<='" + currentDate
				+ "' AND ud.idrin_ma_user_active_to>='" + currentDate + "' "
				+ " and ud.idrin_ma_user_delegation_active = 1"
				+ " GROUP BY u.idrin_ma_user_delegation_id ";

		List<Integer> resultUser = (List<Integer>) getEntityManager().createNativeQuery(delegationQuery)
				.getResultList();

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
				+ " c.rin_tr_request_id,c.rin_tr_req_workflow_audit_user_id,c.rin_tr_req_workflow_audit_group_id,"
				+ " c.rin_tr_req_workflow_audit_sequence,c.rin_tr_req_workflow_audit_descision_type,c.rin_tr_req_workflow_audit_approval_executer,"
				+ " c.rin_tr_req_workflow_audit_reassign_flag,c.rin_tr_req_workflow_audit_reassign_user_id, "
				+ " c.rin_tr_req_workflow_audit_remarks,c.rin_tr_req_workflow_audit_is_active, wf.rin_ma_req_workflow_executer_reassign,u.LAST_NAME  "
				+ " FROM " + getRtaDatabaseSchema() + ".rin_tr_req_workflow_audit c " 
				+ " LEFT JOIN "+ getRtaDatabaseSchema()
				+ ".`rin_ma_req_workflow` wf ON wf.idrin_ma_req_workflow_id = c.rin_ma_req_workflow_id " + " JOIN "
				+ getCommonDatabaseSchema() + ".`user` u ON c.rin_tr_req_workflow_audit_user_id = u.USER_ID "
				+ " WHERE c.delete_flag  = '" + CommonConstant.FLAG_ZERO + "' and c.rin_tr_request_id= "
				+ requestVo.getRequestId() + " AND c.rin_tr_req_workflow_audit_is_active = "
				+ CommonConstant.CONSTANT_ONE + " " ;
				//+ " and c.rin_tr_req_workflow_audit_user_id IN(" + allUsers + ")  "
				//+ " and  c.rin_tr_req_workflow_audit_approval_executer = 2  ";

		List<Object[]> requestEntity = new ArrayList<>();
		try {
			requestEntity = (List<Object[]>) getEntityManager().createNativeQuery(query).getResultList();
		} catch (Exception e) {
			logger.error("error", e);
		}
		return requestEntity;

	}

	@SuppressWarnings("unchecked")
	public List<Object[]> search(RequestResolverVO requestVo, AuthDetailsVo authDetailsVo) {
 
		/*SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
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

				toDate = formatter.format(DateUtil.getCalenderDate());
			}
		}*/
		
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String fromDate = null;
		String toDate = null;

		Date result = null;
		Date tresult = null;
		
		if (null != requestVo.getFlag()) {
			if (2 == requestVo.getFlag()) {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, -45);
				result = cal.getTime();
				fromDate = formatter.format(result);

				Calendar tcal = Calendar.getInstance();
				tcal.add(Calendar.DATE, -30);
				tresult = tcal.getTime();

				toDate = formatter.format(tresult);
			} else if (3 == requestVo.getFlag()) {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, -90);
				result = cal.getTime();
				fromDate = formatter.format(result);

				Calendar tcal = Calendar.getInstance();
				tcal.add(Calendar.DATE, -45);
				tresult = tcal.getTime();

				toDate = formatter.format(tresult);
			} else if (4 == requestVo.getFlag()) {
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
		
		StringBuffer allUsers = getResolverId(authDetailsVo);
		
		String query = "SELECT CONCAT(userdetail.FIRST_NAME,' ',userdetail.LAST_NAME),req.idrin_tr_request_id,req.rin_tr_request_code,rtype.idrin_ma_request_type_id,"
				+ " rtype.rin_ma_request_type_name,rstype.idrin_ma_request_subtype_id,rstype.rin_ma_request_subtype_name,req.rin_tr_request_date, "
				+ " depart.USER_DEPARTMENT_ID,depart.USER_DEPARTMENT_NAME,loca.USER_LOCATION_ID, loca.USER_LOCATION_NAME,sub.idrin_ma_sublocation_sublocationId, "
				+ " sub.rin_ma_sublocation_name,cur.idrin_ma_current_status_id, cur.rin_ma_current_status_name, audit.idrin_tr_req_workflow_audit_id, "
				+ " audit.rin_ma_req_workflow_id,audit.rin_tr_req_workflow_seq_id,audit.rin_tr_req_workflow_audit_user_id, "
				+ " audit.rin_tr_req_workflow_audit_group_id,audit.rin_tr_req_workflow_audit_sequence,audit.rin_tr_req_workflow_audit_descision_type, "
				+ " audit.rin_tr_req_workflow_audit_approval_executer,audit.rin_tr_req_workflow_audit_remarks,userdetail.LAST_NAME , req.create_by , req.forward_redirect_remarks "
				+ ", req.`forward_request_id`,req.`rin_tr_subrequest` , req.remarks , cur.rin_ma_current_status_code"
				+ " FROM  "+getRtaDatabaseSchema()+".rin_tr_request req  "
				+ " JOIN "+getCommonDatabaseSchema()+".user_location loca ON   req.rin_tr_request_user_location_id = loca.USER_LOCATION_ID  "
				+ " JOIN "+getCommonDatabaseSchema()+".rin_ma_sublocation sub  ON req.rin_tr_request_sublocation_id = sub.idrin_ma_sublocation_sublocationId "
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
				+ " AND req.current_status_id  IN (5 ,8,9,10,14)"
				+ " AND cur.rin_ma_current_status_code NOT IN ('COM','CLO') " + " AND req.rin_tr_request_is_cancel = 0 "
				+ " AND audit.rin_tr_req_workflow_audit_descision_type!=9 "
				+ " AND audit.rin_tr_req_workflow_audit_is_active = " + CommonConstant.CONSTANT_ONE
				+ " AND Date(req.create_date) BETWEEN '" + fromDate + "' AND '" + toDate + "'";


		StringBuilder modifiedQuery = new StringBuilder(query);
		if (requestVo.getRequestCode() != null) {
			modifiedQuery.append(" AND req.rin_tr_request_code like '%" + requestVo.getRequestCode() + "%'");
		}
		if (requestVo.getRequestDate() != null) {
			modifiedQuery.append(" AND req.rin_tr_request_date like '%"
					+ DateUtil.dateToString(requestVo.getRequestDate(), "yyyy-MM-dd") + "%'");
		}
		if (requestVo.getRequestTypeName() != null) {
			modifiedQuery.append(" AND rtype.rin_ma_request_type_name like '%" + requestVo.getRequestTypeName() + "%'");
		}
		if (requestVo.getRequestSubTypeName() != null) {
			modifiedQuery.append(
					" AND rstype.rin_ma_request_subtype_name like '%" + requestVo.getRequestSubTypeName() + "%'");
		}

		if (requestVo.getLocationName() != null) {
			modifiedQuery.append(" AND loca.USER_LOCATION_NAME like '%" + requestVo.getLocationName() + "%'");
		}

		if (requestVo.getSublocationName() != null) {
			modifiedQuery.append(" AND sub.rin_ma_sublocation_name like '%" + requestVo.getSublocationName() + "%'");
		}

		if (requestVo.getUserDepartmentName() != null) {
			modifiedQuery.append(" AND depart.USER_DEPARTMENT_NAME like '%" + requestVo.getUserDepartmentName() + "%'");
		}

		if (requestVo.getCurrentStatusName() != null) {
			modifiedQuery
					.append(" AND cur.rin_ma_current_status_name like '%" + requestVo.getCurrentStatusName() + "%'");
		}

		if (requestVo.getUserName() != null) {
			modifiedQuery.append(" AND CONCAT(userdetail.FIRST_NAME,' ',userdetail.LAST_NAME) like '%"
					+ requestVo.getUserName() + "%'");
		}
		modifiedQuery.append(" GROUP BY req.idrin_tr_request_id order by  req.idrin_tr_request_id DESC");

		List<Object[]> listRequestEntity = new ArrayList<>();
		try {
			listRequestEntity = (List<Object[]>) getEntityManager().createNativeQuery(modifiedQuery.toString())
					.getResultList();
		} catch (Exception e) {
			logger.error("error", e);
		}

		return listRequestEntity;

	}

	@SuppressWarnings("unchecked")
	public List<UserMappingEntity> getListOfUser(int currentLoginUserId) {

		/*
		 * String query = " From UserMappingEntity where deleteFlag = " +
		 * CommonConstant.CONSTANT_ONE + " AND reportingToUser.id = " +
		 * currentLoginUserId;
		 */

		String query = " SELECT * From " + getCommonDatabaseSchema() + ".`user_mapping` where delete_flag = "
				+ CommonConstant.FLAG_ONE + " AND REPORTING_TO = " + currentLoginUserId;

		List<UserMappingEntity> usermappingEntityList = new ArrayList<>();
		try {
			usermappingEntityList = (List<UserMappingEntity>) getEntityManager()
					.createNativeQuery(query, UserMappingEntity.class).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
		}

		return usermappingEntityList;
	}

	/**
	 * Get all the approval for summary
	 * 
	 * @param requestVo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getAllHistory(RequestVO requestVo, List<Integer> subOrdinate, AuthDetailsVo authDetailsVo) {
		int id = authDetailsVo.getUserId();

		String subOrdinateFirst = subOrdinate.toString().replace("[", "");

		String subOrdinateSecond = subOrdinateFirst.replace("]", "");

		if ("".equals(subOrdinateSecond)) {
			subOrdinateSecond = "0";
		}

	/*	int limit = 10;

		int offset = 0;
		if (requestVo != null) {
			if (null != requestVo.getPageLimit() && requestVo.getPageLimit() != 0) {
				limit = requestVo.getPageLimit();
			} else {
				requestVo.setPageLimit(limit);
			}

			if (null != requestVo.getPageNo() && requestVo.getPageNo() != 0) {
				offset = requestVo.getPageNo() * limit;
			}
		}*/

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

				toDate = formatter.format(DateUtil.getCalenderDate());
			}
		}

		String query = " SELECT request.idrin_tr_request_id , request.rin_tr_request_code, request.current_status_id,"
				+ " curstatus.rin_ma_current_status_code ,curstatus.rin_ma_current_status_name,"
				+ " reqtype.idrin_ma_request_type_id , reqtype.rin_ma_request_type_name , subtype.idrin_ma_request_subtype_id ,"
				+ "subtype.rin_ma_request_subtype_name , us.USER_ID , CONCAT(us.FIRST_NAME,' ',us.LAST_NAME) as firstName, request.create_date ,"
				+ " audit.idrin_tr_req_workflow_audit_id , audit.rin_ma_req_workflow_id , audit.rin_tr_req_workflow_seq_id ,"
				+ " audit.rin_tr_req_workflow_audit_group_id , audit.rin_tr_req_workflow_audit_sequence , audit.rin_tr_req_workflow_audit_descision_type,"
				+ " audit.rin_tr_req_workflow_audit_approval_executer , audit.rin_tr_req_workflow_audit_remarks,request.rin_tr_request_priority,request.rin_tr_request_subject"
				+ " FROM "+getRtaDatabaseSchema()+".rin_tr_request request "
				+ " JOIN "+getRtaDatabaseSchema()+".rin_tr_req_workflow_audit audit ON "
				+ " request.idrin_tr_request_id = audit.rin_tr_request_id "
				+ " LEFT JOIN "+getRtaDatabaseSchema()+".rin_ma_request_type reqtype ON"
				+ " request.rin_ma_request_type_id = reqtype.idrin_ma_request_type_id"
				+ " LEFT JOIN "+getRtaDatabaseSchema()+".rin_ma_request_subtype subtype ON"
				+ " request.rin_ma_request_subtype_id = subtype.idrin_ma_request_subtype_id "
				+ "	LEFT JOIN "+getCommonDatabaseSchema()+".user us ON"
				+ " request.create_by = us.USER_ID"
				+ " LEFT JOIN "+getRtaDatabaseSchema()+".rin_ma_current_status curstatus ON request.current_status_id = curstatus.idrin_ma_current_status_id WHERE "
				+ "  request.`rin_ma_entity_id` = " + authDetailsVo.getEntityId() 
				+ "  AND  rin_tr_req_workflow_audit_approval_executer = 2 AND "
				+ " audit.rin_tr_req_workflow_audit_is_active = 1 AND reqtype.delete_flag = " + CommonConstant.FLAG_ZERO
				+ " AND subtype.delete_flag = " + CommonConstant.FLAG_ZERO + " AND "
				+ " request.current_status_id IN (1,2,11,14,10) "
				+ " AND request.rin_tr_request_is_cancel = '0' AND request.delete_flag = " + CommonConstant.FLAG_ZERO
				+ " AND audit.delete_flag = " + CommonConstant.FLAG_ZERO + " " ;
				
		  
		  if ( !authDetailsVo.getUserId().equals(CommonConstant.SUPER_ADMIN_ID)) {
				if(!(subOrdinateSecond == "0"
						)){
					query = query + " AND (audit.rin_tr_req_workflow_audit_user_id = " + id
							+ " OR audit.rin_tr_req_workflow_audit_user_id IN (" + subOrdinateSecond + "))" ;					
				}else{
					query = query +  " AND (audit.rin_tr_req_workflow_audit_user_id = " + id 
							  + ")" ;
				}
						
		  }
		  
				query = query + " AND Date(request.create_date) BETWEEN'" + fromDate + "' AND '" + toDate + "' "
				+ " GROUP BY request.idrin_tr_request_id ORDER BY request.idrin_tr_request_id DESC  ";

		List<Object[]> requestEntityList = new ArrayList<>();

		try {
		/*	requestEntityList = (List<Object[]>) getEntityManager().createNativeQuery(query).setFirstResult(offset)
					.setMaxResults(limit).getResultList();*/
			requestEntityList = (List<Object[]>) getEntityManager().createNativeQuery(query)
					 .getResultList();
		} catch (Exception e) {
			logger.error("error", e);
		}

		return requestEntityList;

	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getAllHistorySearch(RequestVO requestVo, List<Integer> subOrdinate,
			AuthDetailsVo authDetailsVo) {
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

				toDate = formatter.format(DateUtil.getCalenderDate());
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
				+ " JOIN rin_tr_req_workflow_audit audit ON "
				+ " request.idrin_tr_request_id = audit.rin_tr_request_id"
				+ " LEFT JOIN rin_ma_request_type reqtype ON"
				+ " request.rin_ma_request_type_id = reqtype.idrin_ma_request_type_id "
				+ " LEFT JOIN rin_ma_request_subtype subtype ON"
				+ " request.rin_ma_request_subtype_id = subtype.idrin_ma_request_subtype_id "
				+ " LEFT JOIN "+getCommonDatabaseSchema()+".user us ON request.create_by = us.USER_ID"
				+ " LEFT JOIN rin_ma_current_status curstatus ON request.current_status_id = curstatus.idrin_ma_current_status_id WHERE "
				+ "  rin_tr_req_workflow_audit_approval_executer = 2 AND "
				+ " audit.rin_tr_req_workflow_audit_is_active = 1 AND reqtype.delete_flag = " + CommonConstant.FLAG_ZERO
				+ " AND subtype.delete_flag = " + CommonConstant.FLAG_ZERO + " AND " + " request.current_status_id = 1 "
				+ " AND request.rin_tr_request_is_cancel = '0' AND request.delete_flag = " + CommonConstant.FLAG_ZERO
				+ " AND audit.delete_flag = " + CommonConstant.FLAG_ZERO + " "
				+ " AND Date(request.create_date) BETWEEN'" + fromDate + "' AND '" + toDate + "' "
				+ " AND (audit.rin_tr_req_workflow_audit_user_id = " + id
				+ " OR audit.rin_tr_req_workflow_audit_user_id IN (" + subOrdinateSecond + "))";

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
		if (null != requestVo.getRequestPriority() && requestVo.getRequestPriority() != 0) {
			modifiedQuery
					.append(" AND request.rin_tr_request_priority like '%" + requestVo.getRequestPriority() + "%'");
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

	@SuppressWarnings("unchecked")
	public List<RequestEntity> statusCheckForHold(RequestVO requestVO, AuthDetailsVo authDetailsVo) {

		List<RequestEntity> requestEntityList = new ArrayList<>();

		try {

			String query = " SELECT * From " + getRtaDatabaseSchema() + ".`rin_tr_request` where  "
					+ " rin_tr_subrequest = " + requestVO.getRequestId() + " AND current_status_id = "
					+ CommonConstant.CONSTANT_ELEVEN;

			requestEntityList = (List<RequestEntity>) getEntityManager().createNativeQuery(query, RequestEntity.class)
					.getResultList();

		} catch (Exception e) {
			logger.error("error", e);
		}

		return requestEntityList;
	}
		
	@SuppressWarnings("unchecked")
	public List<RequestEntity> validationForInProgress(RequestVO requestVo){

		List<RequestEntity> requestEntityList = new ArrayList<>();

		try {

			String query = " SELECT * FROM " + getRtaDatabaseSchema() 
			+ ".rin_tr_request a WHERE a.`idrin_tr_request_id` =  " + requestVo.getRequestId();

			requestEntityList = (List<RequestEntity>) getEntityManager().createNativeQuery(query, RequestEntity.class)
					.getResultList();

		} catch (Exception e) {
			logger.error("error", e);
		}

		return requestEntityList;
	}
	
	@SuppressWarnings("unchecked")
	public List<RequestEntity> statusCheckForHoldRequestor(RequestVO requestVO, AuthDetailsVo authDetailsVo) {

		List<RequestEntity> requestEntityList = new ArrayList<>();

		try {

			String query = " SELECT * From " + getRtaDatabaseSchema() + ".rin_tr_request where  "
					+ " rin_tr_subrequest = " + requestVO.getRequestId() + " AND current_status_id = "
					+ CommonConstant.CONSTANT_ONE;

			requestEntityList = (List<RequestEntity>) getEntityManager().createNativeQuery(query, RequestEntity.class)
					.getResultList();

		} catch (Exception e) {
			logger.error("error", e);
		}

		return requestEntityList;
	}
	
	
}
