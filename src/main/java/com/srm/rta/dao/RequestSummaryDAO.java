package com.srm.rta.dao;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.srm.coreframework.dao.CommonDAO;
import com.srm.coreframework.entity.CommonStorageEntity;
import com.srm.coreframework.entity.UserEntity;
import com.srm.coreframework.entity.UserMappingEntity;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.util.DateUtil;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.rta.vo.RequestVO;

@Repository
public class RequestSummaryDAO extends CommonDAO {

	Logger logger = LoggerFactory.getLogger(RequestSummaryDAO.class);

	@Autowired
	ApprovalDAO approvalDao;
	
	
	public BigInteger findRequestSubtype(int requestSubTypeId,AuthDetailsVo authDetailsVo) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String fromDate = new String("");
		String toDate = new String("");

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		Date result = cal.getTime();

		try {
			fromDate = formatter.format(result);
		} catch (Exception e) {
			Log.info("Request Dao getAll Exception",e);
		}
		
		toDate = formatter.format(CommonConstant.getCalenderDate());
		 		
		String query = "SELECT COUNT(idrin_tr_request_id) FROM rin_tr_request request "
				+ " WHERE request.rin_ma_entity_id = '" + authDetailsVo.getEntityId() + "'  "
				+ " and  request.rin_ma_request_subtype_id = " + requestSubTypeId ;
				
		if (!authDetailsVo.getUserId().equals(CommonConstant.SUPER_ADMIN_ID)) {
			query = query + " AND " + "request.create_by =" + authDetailsVo.getUserId() + " ";
		}

		//query = query + " AND Date(request.create_date) BETWEEN '" + fromDate + "' AND  '" + toDate + "' "
		query = query + " ORDER BY request.idrin_tr_request_id DESC";
		
		BigInteger countFirst = BigInteger.valueOf(0);

		try {
			countFirst = (BigInteger) getEntityManager().createNativeQuery(query).getSingleResult();
		} catch (Exception e) {
			logger.error("error", e);
		}

		return countFirst;
	}

	@SuppressWarnings("unchecked")
	public List<Object> getAllApprovalSummary(int requestId) {

		String query = "select audit.*, CONCAT(us.FIRST_NAME,' ',us.LAST_NAME) , req.rin_tr_request_sequence from "+getRtaDatabaseSchema()
				+ ".rin_tr_req_workflow_audit audit left join "+getCommonDatabaseSchema()+".user us"
				+ " on audit.rin_tr_req_workflow_audit_user_id = us.USER_ID " + " left join "+getRtaDatabaseSchema()+".rin_tr_request req "
				+ " on req.idrin_tr_request_id = audit.rin_tr_request_id where " + " audit.delete_flag = "
				+ CommonConstant.CONSTANT_ZERO + " and us.DELETE_FLAG = '" + CommonConstant.FLAG_ZERO
				+ "' AND audit.rin_tr_request_id = " + requestId + " "
				+ " ORDER BY audit.idrin_tr_req_workflow_audit_id  DESC";

		List<Object> requestWorkFlowAuditEntityList = new ArrayList<>();
		try {
			requestWorkFlowAuditEntityList = (List<Object>) getEntityManager().createNativeQuery(query).getResultList();
		} catch (Exception e) {
			logger.error("error", e);
		}
		return requestWorkFlowAuditEntityList;

	}

	@SuppressWarnings("unchecked")
	public List<UserMappingEntity> getListOfUser(int currentLoginUserId,AuthDetailsVo authDetailsVo) {

		/*String query = " From UserMappingEntity where deleteFlag = " + CommonConstant.CONSTANT_ONE
				+ " AND reportingToUser.id = " + currentLoginUserId + " AND entityLicenseEntity.id ="
				+ authDetailsVo.getEntityId();*/
		
		/*String query = " FROM " + getCommonDatabaseSchema() +".user_mapping WHERE delete_flag = " + CommonConstant.FLAG_ONE
						+ " and REPORTING_TO = " +currentLoginUserId + " and rin_ma_entity_id = " +authDetailsVo.getEntityId();
		

		List<UserMappingEntity> usermappingEntityList = new ArrayList<>();

		try {
			usermappingEntityList = (List<UserMappingEntity>) getEntityManager().createNativeQuery(query).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
		}

		return usermappingEntityList;*/
		
		String query = "SELECT USER_MAPPING_ID, LEVEL, REPORTING_TO, USER_DEPARTMENT_ID, USER_LOCATION_ID,"//4
				+ " USER_SUBLOCATION_ID, REPORTING_TO_LOCATION, REPORTING_TO_SUBLOCATION, REPORTING_DEPARTMENT,"//8
				+ " USER_ROLE_ID, rin_ma_entity_id, USER_ID "//11
				+ " FROM "+getCommonDatabaseSchema()+".user_mapping " + " WHERE rin_ma_entity_id = '"
				+ authDetailsVo.getEntityId() + "'" + " AND REPORTING_TO = " + currentLoginUserId + " AND delete_flag = "
				+ CommonConstant.FLAG_ZERO;

		List<Object[]> userMappingEntity = (List<Object[]>) getEntityManager().createNativeQuery(query).getResultList();
		List<UserMappingEntity> list = new ArrayList<>();
		UserMappingEntity userMappingEntity2 = null;
		for(Object[] object : userMappingEntity){
			userMappingEntity2 = new UserMappingEntity();
			if(null != (Integer) (((Object[]) object)[0])){
				userMappingEntity2.setUserMappingId((Integer) (((Object[]) object)[0]));
			}
			if(null != (Integer) (((Object[]) object)[1])){
				CommonStorageEntity commonStorageEntity = new CommonStorageEntity();
				commonStorageEntity.setCommonId((Integer) (((Object[]) object)[1]));
				userMappingEntity2.setLevel(commonStorageEntity);
			}
			if(null != (Integer) (((Object[]) object)[2])){
				UserEntity userEntity = new UserEntity();
				userEntity.setId((Integer) (((Object[]) object)[2]));
				userMappingEntity2.setReportingToUser(userEntity);
			}
			if(null != (Integer) (((Object[]) object)[11])){
				UserEntity userEntity = new UserEntity();
				userEntity.setId((Integer) (((Object[]) object)[11]));
				userMappingEntity2.setUserEntity(userEntity);
			}
			list.add(userMappingEntity2);
		}
		return list;
		
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getAllHistory(RequestVO requestVo, List<Integer> subOrdinate,AuthDetailsVo authDetailsVo) {
		
		List<Object[]> requestEntityList = new ArrayList<>();
		
		try {
			
		int id = authDetailsVo.getUserId();

		String subOrdinateFirst = subOrdinate.toString().replace("[", "");

		String subOrdinateSecond = subOrdinateFirst.replace("]", "");

		if ("".equals(subOrdinateSecond)) {
			subOrdinateSecond = "0";
		}

		//int limit = 10;

	/*	int offset = 0;
		if (requestVo != null) {
			if (null != requestVo.getPageLimit() && requestVo.getPageLimit() != 0) {
				limit = requestVo.getPageLimit();
			} else {
				requestVo.setPageLimit(limit);
			}

			if (null != requestVo.getPageNo() && requestVo.getPageNo() != 0) {
				offset = requestVo.getPageNo() * limit;
			}
		}
*/
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
		String query = "";
				 
		  query = " SELECT request.idrin_tr_request_id , request.rin_tr_request_code, request.current_status_id," //2
				+ " curstatus.rin_ma_current_status_code ,curstatus.rin_ma_current_status_name,"//4
				+ " reqtype.idrin_ma_request_type_id , reqtype.rin_ma_request_type_name , subtype.idrin_ma_request_subtype_id ,"//7
				+ " subtype.rin_ma_request_subtype_name , us.USER_ID , CONCAT(us.FIRST_NAME,' ',us.LAST_NAME), request.create_date ,"//11
				+ " audit.idrin_tr_req_workflow_audit_id , audit.rin_ma_req_workflow_id , audit.rin_tr_req_workflow_seq_id ,"//14
				+ " audit.rin_tr_req_workflow_audit_group_id , audit.rin_tr_req_workflow_audit_sequence , "//16
				+ " audit.rin_tr_req_workflow_audit_descision_type,"//17
				+ " audit.rin_tr_req_workflow_audit_approval_executer , audit.rin_tr_req_workflow_audit_remarks,"//19
				+ " request.rin_tr_request_priority,request.rin_tr_request_subject" //21
				+ " FROM "+getRtaDatabaseSchema()+".rin_tr_request request "
				+ "	JOIN "+getRtaDatabaseSchema()+".rin_tr_req_workflow_audit audit ON "
				+ " request.idrin_tr_request_id = audit.rin_tr_request_id "
				+ " LEFT JOIN "+getRtaDatabaseSchema()+".rin_ma_request_type reqtype ON"
				+ " request.rin_ma_request_type_id = reqtype.idrin_ma_request_type_id "
				+ " LEFT JOIN "+getRtaDatabaseSchema()+".rin_ma_request_subtype subtype ON"
				+ " request.rin_ma_request_subtype_id = subtype.idrin_ma_request_subtype_id "
				+ " LEFT JOIN "+ getCommonDatabaseSchema()+".user us ON"
				+ " request.create_by = us.USER_ID"
				+ " LEFT JOIN "+getRtaDatabaseSchema()+".rin_ma_current_status curstatus ON request.current_status_id = curstatus.idrin_ma_current_status_id "
				+ " WHERE "
				+ " request.rin_ma_entity_id = '" + authDetailsVo.getEntityId()
				+ "' AND audit.rin_tr_req_workflow_audit_is_active = 1 AND reqtype.delete_flag = 0 AND subtype.delete_flag = 0 "
				+ " AND request.rin_tr_request_is_cancel = '0' AND request.delete_flag = '0' AND audit.delete_flag = '0' " ;
				
			if ( !authDetailsVo.getUserId().equals(CommonConstant.SUPER_ADMIN_ID)) {
					query = query	+ " AND (audit.rin_tr_req_workflow_audit_user_id = " + id + " OR request.create_by IN ("
				+ subOrdinateSecond + "))" ;
			}
				
			//query = query + " AND Date(request.create_date) BETWEEN '" + fromDate + "' AND '" + toDate + "' "
					query = query 	+ " GROUP BY request.idrin_tr_request_id ORDER BY request.idrin_tr_request_id DESC  ";
		
			requestEntityList = (List<Object[]>) getEntityManager().createNativeQuery(query)
					 .getResultList();
			
		} catch (Exception e) {
			logger.error("error", e);
		}

		return requestEntityList;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getAll(RequestVO requestVO,AuthDetailsVo authDetailsVo) {
		
		//int id = authDetailsVo.getUserId();
			
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
		StringBuffer allUsers = approvalDao.getApprovalListUser(authDetailsVo);
		
		List<Object[]> requestEntityList = new ArrayList<>();
		try {
		String query = " SELECT request.idrin_tr_request_id , request.rin_tr_request_code, request.current_status_id,"//2
				+ " curstatus.rin_ma_current_status_code ,curstatus.rin_ma_current_status_name,"//4
				+ " reqtype.idrin_ma_request_type_id , reqtype.rin_ma_request_type_name , subtype.idrin_ma_request_subtype_id ,"//7
				+ "subtype.rin_ma_request_subtype_name , us.USER_ID , CONCAT(us.FIRST_NAME,' ',us.LAST_NAME) , request.create_date ,"//11
				+ " audit.idrin_tr_req_workflow_audit_id , audit.rin_ma_req_workflow_id , audit.rin_tr_req_workflow_seq_id ,"//14
				+ " audit.rin_tr_req_workflow_audit_group_id , audit.rin_tr_req_workflow_audit_sequence , "//16
				+ " audit.rin_tr_req_workflow_audit_descision_type,"//17
				+ " audit.rin_tr_req_workflow_audit_approval_executer , audit.rin_tr_req_workflow_audit_remarks,"//19
				+ " request.rin_tr_request_priority,request.rin_tr_request_subject"//21
				+ " FROM "+getRtaDatabaseSchema()+".rin_tr_request request "
				+ " JOIN "+getRtaDatabaseSchema()+".rin_tr_req_workflow_audit audit ON "
				+ " request.idrin_tr_request_id = audit.rin_tr_request_id "
				+ " LEFT JOIN "+getRtaDatabaseSchema()+".rin_ma_request_type reqtype ON"
				+ " request.rin_ma_request_type_id = reqtype.idrin_ma_request_type_id "
				+ " LEFT JOIN "+getRtaDatabaseSchema()+".rin_ma_request_subtype subtype ON"
				+ " request.rin_ma_request_subtype_id = subtype.idrin_ma_request_subtype_id "
				+ " LEFT JOIN "+getCommonDatabaseSchema()+".user us ON request.create_by = us.USER_ID "
				+ " LEFT JOIN "+getRtaDatabaseSchema()+".rin_ma_current_status curstatus ON"
				+ " request.current_status_id = curstatus.idrin_ma_current_status_id "
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
			   // + " AND Date(request.create_date) BETWEEN '" + listfromDate + "' AND  '" + listtoDate + "' "
				+ " GROUP BY request.idrin_tr_request_id ORDER BY request.idrin_tr_request_id DESC  ";

			requestEntityList = (List<Object[]>) getEntityManager().createNativeQuery(query).getResultList();
		} catch (Exception e) {
			logger.error("error", e);
		}
 
		return requestEntityList;

	}

}
