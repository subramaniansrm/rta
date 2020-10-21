package com.srm.rta.dao;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.jfree.util.Log;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.config.UserMessages;
import com.srm.coreframework.constants.DecisionTypeEnum;
import com.srm.coreframework.dao.CommonDAO;
import com.srm.coreframework.entity.CodeGenerationEntity;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.rta.entity.RequestDetailEntity;
import com.srm.rta.entity.RequestEntity;
import com.srm.rta.entity.RequestScreenDetailConfigurationEntity;
import com.srm.rta.entity.RequestTypeEntity;
import com.srm.rta.entity.RequestWorkFlowAuditEntity;
import com.srm.rta.entity.RequestWorkFlowEntity;
import com.srm.rta.entity.RequestWorkFlowSeqEntity;
import com.srm.rta.vo.RequestVO;

import lombok.Data;
@Data
@Repository
public class RequestDAO extends CommonDAO {


	@Autowired
	UserMessages userMessages;
	
	@Autowired
	RequestConfigurationDAO requestConfigurationDAO;

	/**
	 * This method is used to list the Request data by executing the query.
	 * 
	 * @param RequestVo
	 *            requestVo
	 * @return List<Object[]> list_RequestEntity
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getAll(RequestVO requestVo, AuthDetailsVo authDetailsVo) {

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

		if (requestVo.getListToDate() != null) {
			toDate = formatter.format(requestVo.getListToDate());

		} else {

			toDate = formatter.format(CommonConstant.getCalenderDate());
		}

		String query = "SELECT req.idrin_tr_request_id,req.rin_tr_request_code,req.rin_tr_request_date,rtype.rin_ma_request_type_name,"// 3
				+ " rstype.rin_ma_request_subtype_name,loca.USER_LOCATION_NAME,sub.rin_ma_sublocation_name,depart.USER_DEPARTMENT_NAME, "// 7
				+ " cur.rin_ma_current_status_name,req.rin_ma_request_type_id,req.rin_ma_request_subtype_id,"// 10
				+ " req.rin_tr_request_user_location_id,req.rin_tr_request_sublocation_id,req.rin_tr_request_user_department_id,"// 13
				+ " req.current_status_id,cur.rin_ma_current_status_code,CONCAT(ur.FIRST_NAME,' ',ur.LAST_NAME),req.rin_tr_request_subject,req.rin_tr_request_priority "// 18
				+ " ,loc.USER_LOCATION_NAME reqloc,sl.rin_ma_sublocation_name reqSubloc,"// 20
				+ " req.rin_tr_request_location_id,req.rin_tr_request_sub_location_id ,req.remarks, req.forward_redirect_remarks"// 24
				+ " , req.forward_request_id,req.rin_tr_subrequest "
				+ " FROM "+getRtaDatabaseSchema()+".rin_tr_request req "
				+ " LEFT JOIN "+getRtaDatabaseSchema()+".rin_ma_request_type rtype ON req.rin_ma_request_type_id = rtype.idrin_ma_request_type_id "
				+ " LEFT JOIN "+getRtaDatabaseSchema()+".rin_ma_request_subtype rstype ON req.rin_ma_request_subtype_id = rstype.idrin_ma_request_subtype_id "
				+ " LEFT JOIN "+getCommonDatabaseSchema()+".user_location loca ON req.rin_tr_request_user_location_id = loca.USER_LOCATION_ID "
				+ " LEFT JOIN "+getCommonDatabaseSchema()+".user_location loc ON req.rin_tr_request_location_id = loc.USER_LOCATION_ID "
				+ " LEFT JOIN "+getCommonDatabaseSchema()+".rin_ma_sublocation sl ON req.rin_tr_request_sub_location_id = sl.idrin_ma_sublocation_sublocationId "
				+ " LEFT JOIN "+getCommonDatabaseSchema()+".rin_ma_sublocation sub ON req.rin_tr_request_sublocation_id = sub.idrin_ma_sublocation_sublocationId "
				+ " LEFT JOIN "+getCommonDatabaseSchema()+".user_department depart ON req.rin_tr_request_user_department_id = depart.USER_DEPARTMENT_ID "
				+ " LEFT JOIN "+getRtaDatabaseSchema()+".rin_ma_current_status cur ON req.current_status_id = cur.idrin_ma_current_status_id "
				+ " LEFT JOIN "+getCommonDatabaseSchema()+".user ur ON req.create_by = ur.USER_ID"
				+ " where req.rin_ma_entity_id = '" + authDetailsVo.getEntityId() + "' and req.delete_flag = "
				+ CommonConstant.FLAG_ZERO  ;
						 
		if ( !authDetailsVo.getUserId().equals(CommonConstant.SUPER_ADMIN_ID)) {
		query = query + " and req.create_by = " + authDetailsVo.getUserId();
		}
		
		query = query + " AND Date(req.create_date) BETWEEN '" + fromDate + "' AND  '" + toDate + "' "
				+ " GROUP BY req.idrin_tr_request_id ORDER BY req.idrin_tr_request_id DESC";

		List<Object[]> requestEntityList = new ArrayList<>();

		try {
			requestEntityList = (List<Object[]>) getEntityManager().createNativeQuery(query).getResultList();
		} catch (Exception e) {
			Log.info("Request Dao getAll Exception",e);
		}
		return requestEntityList;
	}

	/**
	 * This method is used to list the Request data by executing the query under
	 * some search condition.
	 * 
	 * @param RequestVo
	 *            requestVo
	 * @return List<Object[]> list_RequestEntity
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getAllSearch(RequestVO requestVo, AuthDetailsVo authDetailsVo) {

 		int limit = 10;

		int offset = 0;
		if (null != requestVo) {
			if (null != requestVo.getPageLimit() && requestVo.getPageLimit() != 0) {
				limit = requestVo.getPageLimit();
			} else {
				requestVo.setPageLimit(limit);
			}

			if (null != requestVo.getPageNo() && requestVo.getPageNo() != 0) {
				offset = requestVo.getPageNo() * limit;
			}
		}
		/*SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String fromDate = new String("");
		String toDate = new String("");

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
		
		String query = "SELECT req.idrin_tr_request_id,req.rin_tr_request_code,req.rin_tr_request_date,rtype.rin_ma_request_type_name,"
				+ " rstype.rin_ma_request_subtype_name,loca.USER_LOCATION_NAME,sub.rin_ma_sublocation_name,depart.USER_DEPARTMENT_NAME, "
				+ " cur.rin_ma_current_status_name,req.rin_ma_request_type_id,req.rin_ma_request_subtype_id,"
				+ " req.rin_tr_request_user_location_id,req.rin_tr_request_sublocation_id,req.rin_tr_request_user_department_id,"
				+ " req.current_status_id,cur.rin_ma_current_status_code,CONCAT(ur.FIRST_NAME,' ',ur.LAST_NAME),req.rin_tr_request_subject,req.rin_tr_request_priority "
				+ " ,loc.USER_LOCATION_NAME reqloc,sl.rin_ma_sublocation_name reqSubloc,req.rin_tr_request_location_id,req.rin_tr_request_sub_location_id , req.remarks, req.forward_redirect_remarks"
				+ " , req.forward_request_id,req.rin_tr_subrequest "
				+ " FROM "+getRtaDatabaseSchema()+".rin_tr_request req "
				+ " LEFT JOIN "+getRtaDatabaseSchema()+".rin_ma_request_type rtype ON req.rin_ma_request_type_id = rtype.idrin_ma_request_type_id "
				+ " LEFT JOIN "+getRtaDatabaseSchema()+".rin_ma_request_subtype rstype ON req.rin_ma_request_subtype_id = rstype.idrin_ma_request_subtype_id "
				+ " LEFT JOIN "+getCommonDatabaseSchema()+".user_location loca ON req.rin_tr_request_user_location_id = loca.USER_LOCATION_ID "
				+ " LEFT JOIN "+getCommonDatabaseSchema()+".user_location loc ON req.rin_tr_request_location_id = loc.USER_LOCATION_ID "
				+ " LEFT JOIN "+getCommonDatabaseSchema()+".rin_ma_sublocation sl ON req.rin_tr_request_sub_location_id = sl.idrin_ma_sublocation_sublocationId "
				+ " LEFT JOIN "+getCommonDatabaseSchema()+".rin_ma_sublocation sub ON req.rin_tr_request_sublocation_id = sub.idrin_ma_sublocation_sublocationId "
				+ " LEFT JOIN "+getCommonDatabaseSchema()+".user_department depart ON req.rin_tr_request_user_department_id = depart.USER_DEPARTMENT_ID "
				+ " LEFT JOIN "+getRtaDatabaseSchema()+".rin_ma_current_status cur ON req.current_status_id = cur.idrin_ma_current_status_id "
				+ " LEFT JOIN "+getCommonDatabaseSchema()+".user ur ON req.create_by = ur.USER_ID"
				+ " where req.rin_ma_entity_id = '" + authDetailsVo.getEntityId() + "' and req.delete_flag = "
				+ CommonConstant.FLAG_ZERO + " and req.create_by = " + authDetailsVo.getUserId() + " "
				+ " AND Date(req.create_date) BETWEEN'" + fromDate + "' AND '" + toDate + "' ";

		StringBuilder modifiedQuery = new StringBuilder(query);

		if (null != requestVo.getRequestId())
			modifiedQuery.append(" and req.idrin_tr_request_id = " + requestVo.getRequestId());

		if (requestVo.getRequestTypeName() != null && !requestVo.getRequestTypeName().isEmpty())
			modifiedQuery.append(" and LOWER(rtype.rin_ma_request_type_name) LIKE LOWER('%"
					+ requestVo.getRequestTypeName() + "%')");

		if (requestVo.getRequestSubTypeName() != null && !requestVo.getRequestSubTypeName().isEmpty())
			modifiedQuery.append(" and LOWER(rstype.rin_ma_request_subtype_name) LIKE LOWER('%"
					+ requestVo.getRequestSubTypeName() + "%')");

		if (requestVo.getLocationName() != null && !requestVo.getLocationName().isEmpty())
			modifiedQuery
					.append(" and LOWER(loca.USER_LOCATION_NAME) LIKE LOWER('%" + requestVo.getLocationName() + "%')");

		if (requestVo.getSublocationName() != null && !requestVo.getSublocationName().isEmpty())
			modifiedQuery.append(
					" and LOWER(sub.rin_ma_sublocation_name) LIKE LOWER('%" + requestVo.getSublocationName() + "%')");

		if (requestVo.getReqLocationName() != null && !requestVo.getReqLocationName().isEmpty())
			modifiedQuery.append(
					" and LOWER(loc.USER_LOCATION_NAME) LIKE LOWER('%" + requestVo.getReqLocationName() + "%')");

		if (requestVo.getReqSublocationName() != null && !requestVo.getReqSublocationName().isEmpty())
			modifiedQuery.append(
					" and LOWER(sl.rin_ma_sublocation_name) LIKE LOWER('%" + requestVo.getReqSublocationName() + "%')");

		if (requestVo.getUserDepartmentName() != null && !requestVo.getUserDepartmentName().isEmpty())
			modifiedQuery.append(" and LOWER(depart.USER_DEPARTMENT_NAME) LIKE LOWER('%"
					+ requestVo.getUserDepartmentName() + "%')");

		if (requestVo.getRequestSeq() != null)
			modifiedQuery.append(" and req.rin_tr_request_sequence = " + requestVo.getRequestSeq());

		if (null != requestVo.getRequestExtension() && requestVo.getRequestExtension() != 0)
			modifiedQuery.append(" and req.rin_tr_request_extension = " + requestVo.getRequestExtension());

		if (requestVo.getRequestCode() != null && !requestVo.getRequestCode().isEmpty())
			modifiedQuery
					.append(" and LOWER(req.rin_tr_request_code) LIKE LOWER('%" + requestVo.getRequestCode() + "%')");

		if (requestVo.getRequestSubject() != null && !requestVo.getRequestSubject().isEmpty())
			modifiedQuery.append(
					" and LOWER(req.rin_tr_request_subject) LIKE LOWER( '%" + requestVo.getRequestSubject() + "%')");

		if (requestVo.getRequestMobileNo() != null && !requestVo.getRequestMobileNo().isEmpty())
			modifiedQuery.append(
					" and LOWER(req.rin_tr_request_mobile_no) LIKE LOWER('%" + requestVo.getRequestMobileNo() + "%')");

		if (null != requestVo.getCurrentStatusName() && !requestVo.getCurrentStatusName().isEmpty())
			modifiedQuery.append(" and LOWER(cur.rin_ma_current_status_name) LIKE LOWER('%"
					+ requestVo.getCurrentStatusName() + "%')");

		if (null != requestVo.getCurrentStatusId() && 0 != requestVo.getCurrentStatusId())
			modifiedQuery.append(" and req.current_status_id = " + requestVo.getCurrentStatusId());

		if (null != requestVo.getRequestPriority() && requestVo.getRequestPriority() != 0) {
			modifiedQuery.append(" and req.rin_tr_request_priority = " + requestVo.getRequestPriority());
		}
		if (requestVo.getRequestDate() != null) {

			String fromDate1 = formatter.format(requestVo.getRequestDate());

			modifiedQuery.append(" and req.rin_tr_request_date like '%" + fromDate1 + "%'");
		}

		modifiedQuery.append(" GROUP BY req.idrin_tr_request_id ORDER BY req.idrin_tr_request_id DESC");

		List<Object[]> requestEntityList = new ArrayList<>();

		try {
			requestEntityList = (List<Object[]>) getEntityManager().createNativeQuery(modifiedQuery.toString())
					.setFirstResult(offset).setMaxResults(limit).getResultList();
		} catch (Exception e) {
			Log.info("Request Dao getAllSearch Exception",e);
		}
		return requestEntityList;
	}

	/**
	 * Method is used to update the cancel request
	 * 
	 * @param requestId
	 * @param remarks
	 * @param userId
	 */
	public void updateCancelRemarks(int requestId, String remarks, int userId, AuthDetailsVo authDetailsVo) throws CommonException {

		try{
		String query = "update RequestWorkFlowAuditEntity  set remarks = '" + remarks + "' , descisionType = "
				+ CommonConstant.CONSTANT_TEN;

		if (null != authDetailsVo.getUserId()) {
			query = query + " ,updateBy = '" + authDetailsVo.getUserId() + "'";
		} else {

			if (userId != 0) {
				query = query + " ,updateBy = '" + userId + "'";
			}
		}

		query = query + ",updateDate = '" + CommonConstant.getCurrentDateTimeAsString() + "' where requestId = "
				+ requestId + " and approvalExecuter = " + CommonConstant.CONSTANT_THREE;

		getEntityManager().createQuery(query).executeUpdate();
		}catch(Exception e){
			throw new CommonException("updateRemarksFailure");
		}

	}

	/**
	 * This method is used to modify or create the Request data by executing the
	 * query.
	 * 
	 * 
	 * @param String
	 *            status
	 * @return int id
	 */
	public int findStatusId(String status) {

		String query = "SELECT currentStatusId from CurrentStatusEntity where currentStatusCode = '" + status + "'";

		int id = 0;

		try {
			id = (int) getEntityManager().createQuery(query).getSingleResult();
		} catch (Exception e) {
			Log.info("Request Dao findStatusId Exception",e);
		}
		return id;

	}

	/**
	 * This method is used to autogenerate the widgetCode by executing the
	 * query.
	 * 
	 * 
	 * @return String widgetCode
	 */
	public String findRequestCode(String code) {

		String requestCode = null;

		CodeGenerationEntity codeGenerationEntity = generateCode(code);

		if (null != codeGenerationEntity) {

			int startingNumber = codeGenerationEntity.getStartingNumber() + CommonConstant.CONSTANT_ONE;

			for (Integer iterable_element : NumberHolder.requestCodeList) {
				if (iterable_element.equals(startingNumber)) {
					startingNumber = startingNumber + CommonConstant.CONSTANT_ONE;
				}
			}

			NumberHolder.requestCodeList.add(startingNumber);

			requestCode = codeGenerationEntity.getPrefix()
					+ String.format(codeGenerationEntity.getCounter().toString(), startingNumber);

			updateCodeStartNumber(codeGenerationEntity.getCodeGenerationId(), startingNumber);

		}

		return requestCode;

	}

	/**
	 * Method is used to update the current status
	 * 
	 * @param requestId
	 * @param currentStatusId
	 */
	public void updateCurrentStatus(int requestId, int currentStatusId) {

		String query = "update RequestEntity  set currentStatusId = " + currentStatusId + " where requestId = "
				+ requestId;

		getEntityManager().createQuery(query).executeUpdate();

	}

	/**
	 * This method is used to find the list of record in the database by
	 * executng the query.
	 * 
	 * 
	 * @param int
	 *            id
	 * @return List<RequestDetailEntity> requestDetailEntityList
	 */
	@SuppressWarnings("unchecked")
	public List<RequestDetailEntity> findDetail(int id, AuthDetailsVo authDetailsVo) {

		String query = "FROM RequestDetailEntity where deleteFlag = " + CommonConstant.FLAG_ZERO + " AND requestId = "
				+ id + " AND entityLicenseId = '" + authDetailsVo.getEntityId() + "' ";

		List<RequestDetailEntity> requestDetailEntityList = (List<RequestDetailEntity>) getEntityManager()
				.createQuery(query).getResultList();

		return requestDetailEntityList;
	}

	/**
	 * Method is used for update the Audit list
	 * 
	 * @param requestId
	 */
	public void updateAuditList(int requestId) throws CommonException {
		try {

			String query = " update RequestWorkFlowAuditEntity set deleteFlag = " + CommonConstant.FLAG_ONE
					+ " where requestId = " + requestId;

			getEntityManager().createQuery(query).executeUpdate();
		} catch (Exception e) {
			Log.info("Request Dao upadateAuditList Exception",e);
			throw new CommonException("requestAuditListDeletionFailed");
		}
	}

	/**
	 * This method used to get the workFlowSequence based on workFlowId
	 * 
	 * @param id
	 *            int
	 * @return requestWorkFlowSeqEntity List<RequestWorkFlowSeqEntity>
	 */
	@SuppressWarnings("unchecked")
	public List<RequestWorkFlowSeqEntity> findWorkFlowId(int id) throws CommonException {
		try {

			String query = "FROM RequestWorkFlowSeqEntity where deleteFlag = " + CommonConstant.FLAG_ZERO
					+ " AND reqWorkFlowSeqIsActive = " + CommonConstant.CONSTANT_ONE + " AND reqWorkFlowId = " + id
					+ " ORDER BY reqWorkFlowSeqSequence ";

			List<RequestWorkFlowSeqEntity> requestWorkFlowSeqEntity = null;

			requestWorkFlowSeqEntity = (List<RequestWorkFlowSeqEntity>) getEntityManager().createQuery(query)
					.getResultList();
			if (null != requestWorkFlowSeqEntity && requestWorkFlowSeqEntity.size() != 0) {

				return requestWorkFlowSeqEntity;
			} else {
				return null;
			}

		} catch (Exception exe) {
			Log.info("Request Dao findWorkFlowId Exception",exe);

			throw new CommonException("requestWorkFlowSequenceFailure");
		}

	}

	/**
	 * Method is used for fin the approval list
	 * 
	 * @param requestId
	 */
	@SuppressWarnings("unchecked")
	public void findApprovalSequence(RequestEntity requestEntity) throws CommonException , Exception {

		try {
		String query = " FROM RequestWorkFlowAuditEntity WHERE entityLicenseId = " + requestEntity.getEntityLicenseId()
				+ " and deleteFlag = " + CommonConstant.FLAG_ZERO + " AND requestId = " + requestEntity.getRequestId()
				+ " AND descisionType = " + CommonConstant.CONSTANT_ZERO + " AND approvalExecuter != "
				+ CommonConstant.CONSTANT_THREE;

		List<RequestWorkFlowAuditEntity> requestWorkFlowAuditEntityList = (List<RequestWorkFlowAuditEntity>) getEntityManager()
				.createQuery(query).getResultList();
		String sequence = "";

		if (null != requestWorkFlowAuditEntityList && requestWorkFlowAuditEntityList.size() > 0) {
			if (requestWorkFlowAuditEntityList.get(0).getGroupId() == 0) {
				sequence = sequence + "," + requestWorkFlowAuditEntityList.get(0).getSequence();
			} else if (requestWorkFlowAuditEntityList.get(0).getGroupId() != 0) {
				int groupId = requestWorkFlowAuditEntityList.get(0).getGroupId();
				for (RequestWorkFlowAuditEntity requestWorkFlowAuditEntity : requestWorkFlowAuditEntityList) {
					if (requestWorkFlowAuditEntity.getGroupId() == groupId) {
						sequence = sequence + "," + requestWorkFlowAuditEntity.getSequence();

					} else {
						break;
					}
				}
			}
			sequence = sequence + ",";
			String updateQuery = " update RequestEntity set requestSeq = '" + sequence.substring(0) + "', updateBy = "
					+ requestEntity.getCreateBy() + ", updateDate = '" + CommonConstant.getCurrentDateTimeAsString()
					+ "'" + " where deleteFlag = " + CommonConstant.FLAG_ZERO + " and requestId = "
					+ requestEntity.getRequestId();

			getEntityManager().createQuery(updateQuery).executeUpdate();
		}
		
		} catch (Exception e) {
			Log.info("Request Dao findApprovalSequence Exception",e);
			throw new CommonException("requestApprovalSequenceFailure");

		}
	}

	/**
	 * Method is used to get all the resubmit list
	 * 
	 * @param requestId
	 * @return requestWorkFlowAuditEntityList
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getAllReSubmitList(int requestId) throws CommonException , Exception {

		List<Object> requestWorkFlowAuditEntityList = null;
		try {
			String query = "select audit.*,concat(us.FIRST_NAME,' ',us.LAST_NAME), req.rin_tr_request_sequence  "
					+ " from "+getRtaDatabaseSchema()+".rin_tr_req_workflow_audit audit left join common_rta_2_local.user us"
					+ " on audit.rin_tr_req_workflow_audit_user_id = us.USER_ID "
					+ " left join "+getRtaDatabaseSchema()+".rin_tr_request req"
					+ " on req.idrin_tr_request_id = audit.rin_tr_request_id " + " where us.delete_flag = '"
					+ CommonConstant.FLAG_ZERO + "' and audit.delete_flag = " + CommonConstant.FLAG_ONE
					+ " and audit.rin_tr_request_id = " + requestId + " ORDER BY audit.idrin_tr_req_workflow_audit_id ";

			requestWorkFlowAuditEntityList = (List<Object>) getEntityManager().createNativeQuery(query).getResultList();

		} catch (CommonException e) {
			Log.info("Request Dao getAllReSubmitList Exception",e);
			throw new CommonException("requestReSubmitListFailure");

		}

		return requestWorkFlowAuditEntityList;
	}

	/**
	 * This method used to get the workFlow based on request
	 * 
	 * @param requestEntity
	 *            RequestEntity
	 * @return requestWorkFlowEntity RequestWorkFlowEntity
	 */
	@SuppressWarnings("unchecked")
	public List<RequestWorkFlowEntity> findbyRequest(RequestEntity requestEntity) throws CommonException {

		List<RequestWorkFlowEntity> requestWorkFlowEntityList = new ArrayList<RequestWorkFlowEntity>();
		try {
			String query = "SELECT rwf FROM RequestWorkFlowEntity rwf"
					+ " ,RequestWorkFlowDetailsEntity rd where rd.reqWorkFlowId = rwf.reqWorkFlowId"
					+ "  And rwf.deleteFlag = " + CommonConstant.CONSTANT_ZERO + " AND  rd.deleteFlag = "
					+ CommonConstant.CONSTANT_ZERO + " AND rwf.requestTypeId =" + requestEntity.getRequestTypeId()
					+ " AND rwf.requestSubTypeId =" + requestEntity.getRequestSubtypeId()
					+ " AND rd.workFlowDepartmentId =" + requestEntity.getDepartmentId()
					+ " AND rd.workFlowLocationId =" + requestEntity.getReqLocationId()
					+ " AND rd.workFlowSublocationId =" + requestEntity.getReqSublocationId()
					+ " and rd.reqWorkFlowDetailsIsActive =" + CommonConstant.CONSTANT_ONE
					+ " and rwf. reqWorkFlowIsActive = " + CommonConstant.CONSTANT_ONE + " ";

			requestWorkFlowEntityList = (List<RequestWorkFlowEntity>) getEntityManager().createQuery(query)
					.getResultList();

			if (requestWorkFlowEntityList.size() == 0) {

				query = "SELECT rwf FROM RequestWorkFlowEntity rwf"
						+ " ,RequestWorkFlowDetailsEntity rd where rd.reqWorkFlowId = rwf.reqWorkFlowId"
						+ "  And rwf.deleteFlag = " + CommonConstant.CONSTANT_ZERO + " AND  rd.deleteFlag = "
						+ CommonConstant.CONSTANT_ZERO + " AND rwf.requestTypeId =" + requestEntity.getRequestTypeId()
						+ " AND rwf.requestSubTypeId =" + requestEntity.getRequestSubtypeId()
						+ " AND rd.workFlowDepartmentId =" + CommonConstant.CONSTANT_ZERO
						+ " AND rd.workFlowLocationId =" + requestEntity.getReqLocationId()
						+ " AND rd.workFlowSublocationId =" + requestEntity.getReqSublocationId()
						+ " and rd.reqWorkFlowDetailsIsActive =" + CommonConstant.CONSTANT_ONE
						+ " and rwf. reqWorkFlowIsActive = " + CommonConstant.CONSTANT_ONE + " ";

				requestWorkFlowEntityList = new ArrayList<RequestWorkFlowEntity>();
				requestWorkFlowEntityList = (List<RequestWorkFlowEntity>) getEntityManager().createQuery(query)
						.getResultList();

				if (requestWorkFlowEntityList.size() == 0) {

					query = "SELECT rwf FROM RequestWorkFlowEntity rwf"
							+ " ,RequestWorkFlowDetailsEntity rd where rd.reqWorkFlowId = rwf.reqWorkFlowId"
							+ "  And rwf.deleteFlag = " + CommonConstant.CONSTANT_ZERO + " AND  rd.deleteFlag = "
							+ CommonConstant.CONSTANT_ZERO + " AND rwf.requestTypeId ="
							+ requestEntity.getRequestTypeId() + " AND rwf.requestSubTypeId ="
							+ requestEntity.getRequestSubtypeId() + " AND rd.workFlowDepartmentId ="
							+ CommonConstant.CONSTANT_ZERO + " AND rd.workFlowLocationId ="
							+ requestEntity.getReqLocationId() + " AND rd.workFlowSublocationId ="
							+ CommonConstant.CONSTANT_ZERO + " and rd.reqWorkFlowDetailsIsActive ="
							+ CommonConstant.CONSTANT_ONE + " and rwf. reqWorkFlowIsActive = "
							+ CommonConstant.CONSTANT_ONE + " ";

					requestWorkFlowEntityList = new ArrayList<RequestWorkFlowEntity>();
					requestWorkFlowEntityList = (List<RequestWorkFlowEntity>) getEntityManager().createQuery(query)
							.getResultList();

					if (requestWorkFlowEntityList.size() == 0) {

						query = "SELECT rwf FROM RequestWorkFlowEntity rwf"
								+ " ,RequestWorkFlowDetailsEntity rd where rd.reqWorkFlowId = rwf.reqWorkFlowId"
								+ "  And rwf.deleteFlag = " + CommonConstant.CONSTANT_ZERO + " AND  rd.deleteFlag = "
								+ CommonConstant.CONSTANT_ZERO + " AND rwf.requestTypeId ="
								+ requestEntity.getRequestTypeId() + " AND rwf.requestSubTypeId ="
								+ requestEntity.getRequestSubtypeId() + " AND rd.workFlowDepartmentId ="
								+ requestEntity.getDepartmentId() + " AND rd.workFlowLocationId ="
								+ CommonConstant.CONSTANT_ZERO + " AND rd.workFlowSublocationId ="
								+ requestEntity.getReqSublocationId() + " and rd.reqWorkFlowDetailsIsActive ="
								+ CommonConstant.CONSTANT_ONE + " and rwf. reqWorkFlowIsActive = "
								+ CommonConstant.CONSTANT_ONE + " ";

						requestWorkFlowEntityList = new ArrayList<RequestWorkFlowEntity>();
						requestWorkFlowEntityList = (List<RequestWorkFlowEntity>) getEntityManager().createQuery(query)
								.getResultList();

						if (requestWorkFlowEntityList.size() == 0) {

							//check
							query = "SELECT rwf FROM RequestWorkFlowEntity rwf"
									+ " ,RequestWorkFlowDetailsEntity rd where rd.reqWorkFlowId = rwf.reqWorkFlowId"
									+ "  And rwf.deleteFlag = " + CommonConstant.CONSTANT_ZERO
									+ " AND  rd.deleteFlag = " + CommonConstant.CONSTANT_ZERO
									+ " AND rwf.requestTypeId =" + requestEntity.getRequestTypeId()
									+ " AND rwf.requestSubTypeId =" + requestEntity.getRequestSubtypeId()
									+ " AND rd.workFlowDepartmentId =" + CommonConstant.CONSTANT_ZERO
									+ " AND rd.workFlowLocationId =" + CommonConstant.CONSTANT_ZERO
									+ " AND rd.workFlowSublocationId =" + CommonConstant.CONSTANT_ZERO
									+ " and rd.reqWorkFlowDetailsIsActive =" + CommonConstant.CONSTANT_ONE
									+ " and rwf. reqWorkFlowIsActive = " + CommonConstant.CONSTANT_ONE + " ";

							requestWorkFlowEntityList = new ArrayList<RequestWorkFlowEntity>();
							requestWorkFlowEntityList = (List<RequestWorkFlowEntity>) getEntityManager()
									.createQuery(query).getResultList();

							if (requestWorkFlowEntityList.size() == 0) {

								query = "SELECT rwf FROM RequestWorkFlowEntity rwf"
										+ " ,RequestWorkFlowDetailsEntity rd where rd.reqWorkFlowId = rwf.reqWorkFlowId"
										+ "  And  rwf.deleteFlag = " + CommonConstant.CONSTANT_ZERO
										+ " AND  rd.deleteFlag = " + CommonConstant.CONSTANT_ZERO
										+ " AND rwf.requestTypeId =" + requestEntity.getRequestTypeId()
										+ " AND rwf.requestSubTypeId =" + requestEntity.getRequestSubtypeId()
										+ " AND rd.workFlowDepartmentId =" + CommonConstant.CONSTANT_ZERO
										+ " AND rd.workFlowLocationId =" + CommonConstant.CONSTANT_ZERO
										+ " AND rd.workFlowSublocationId =" + requestEntity.getReqSublocationId()
										+ " and rd.reqWorkFlowDetailsIsActive =" + CommonConstant.CONSTANT_ONE
										+ " and rwf. reqWorkFlowIsActive = " + CommonConstant.CONSTANT_ONE + " ";

								requestWorkFlowEntityList = new ArrayList<RequestWorkFlowEntity>();
								requestWorkFlowEntityList = (List<RequestWorkFlowEntity>) getEntityManager()
										.createQuery(query).getResultList();

								if (requestWorkFlowEntityList.size() == 0) {

									query = "SELECT rwf FROM RequestWorkFlowEntity rwf"
											+ " ,RequestWorkFlowDetailsEntity rd where rd.reqWorkFlowId = rwf.reqWorkFlowId"
											+ "  And  rwf.deleteFlag = " + CommonConstant.CONSTANT_ZERO
											+ " AND  rd.deleteFlag = " + CommonConstant.CONSTANT_ZERO
											+ " AND rwf.requestTypeId =" + requestEntity.getRequestTypeId()
											+ " AND rwf.requestSubTypeId =" + requestEntity.getRequestSubtypeId()
											+ " AND rd.workFlowDepartmentId =" + requestEntity.getDepartmentId()
											+ " AND rd.workFlowLocationId =" + CommonConstant.CONSTANT_ZERO
											+ " AND rd.workFlowSublocationId =" + CommonConstant.CONSTANT_ZERO
											+ " and rd.reqWorkFlowDetailsIsActive =" + CommonConstant.CONSTANT_ONE
											+ " and rwf. reqWorkFlowIsActive = " + CommonConstant.CONSTANT_ONE + " ";

									requestWorkFlowEntityList = new ArrayList<RequestWorkFlowEntity>();
									requestWorkFlowEntityList = (List<RequestWorkFlowEntity>) getEntityManager()
											.createQuery(query).getResultList();

									if (requestWorkFlowEntityList.size() == 0) {

										query = "SELECT rwf FROM RequestWorkFlowEntity rwf"
												+ " ,RequestWorkFlowDetailsEntity rd where rd.reqWorkFlowId = rwf.reqWorkFlowId"
												+ "  And  rwf.deleteFlag = " + CommonConstant.CONSTANT_ZERO
												+ " AND  rd.deleteFlag = " + CommonConstant.CONSTANT_ZERO
												+ " AND rwf.requestTypeId =" + requestEntity.getRequestTypeId()
												+ " AND rwf.requestSubTypeId =" + requestEntity.getRequestSubtypeId()
												+ " AND rd.workFlowDepartmentId =" + requestEntity.getDepartmentId()
												+ " AND rd.workFlowLocationId =" + requestEntity.getReqLocationId()
												+ " AND rd.workFlowSublocationId =" + CommonConstant.CONSTANT_ZERO
												+ " and rd.reqWorkFlowDetailsIsActive =" + CommonConstant.CONSTANT_ONE
												+ " and rwf. reqWorkFlowIsActive = " + CommonConstant.CONSTANT_ONE
												+ " ";

										requestWorkFlowEntityList = new ArrayList<RequestWorkFlowEntity>();
										requestWorkFlowEntityList = (List<RequestWorkFlowEntity>) getEntityManager()
												.createQuery(query).getResultList();

										if (requestWorkFlowEntityList.size() == 0) {
											query = "SELECT rwf FROM RequestWorkFlowEntity rwf"
													+ " ,RequestWorkFlowDetailsEntity rd where rd.reqWorkFlowId = rwf.reqWorkFlowId"
													+ "  And  rwf.deleteFlag = " + CommonConstant.FLAG_ZERO
													+ " AND  rd.deleteFlag = " + CommonConstant.FLAG_ZERO
													+ " AND rwf.requestTypeId =" + requestEntity.getRequestTypeId()
													+ " AND rwf.requestSubTypeId ="
													+ requestEntity.getRequestSubtypeId()
													+ " AND rd.workFlowDepartmentId =" + requestEntity.getDepartmentId()
													+ " AND rd.workFlowLocationId =" + requestEntity.getId()
													+ " AND rd.workFlowSublocationId ="
													+ requestEntity.getSublocationId()
													+ " and rd.reqWorkFlowDetailsIsActive ="
													+ CommonConstant.CONSTANT_ONE;

											requestWorkFlowEntityList = new ArrayList<RequestWorkFlowEntity>();
											requestWorkFlowEntityList = (List<RequestWorkFlowEntity>) getEntityManager()
													.createQuery(query).getResultList();

										} else {
											return requestWorkFlowEntityList;
										}

									} else {
										return requestWorkFlowEntityList;
									}

								} else {
									return requestWorkFlowEntityList;
								}

							} else {
								return requestWorkFlowEntityList;
							}

						} else {
							return requestWorkFlowEntityList;
						}

					} else {
						return requestWorkFlowEntityList;
					}

				} else {
					return requestWorkFlowEntityList;
				}

			} else {
				return requestWorkFlowEntityList;
			}

		} catch (Exception exe) {
			Log.info("Request Dao findbyRequest Exception",exe);

			throw new CommonException("dataFailure");
		}
		return requestWorkFlowEntityList;

	}

	public RequestEntity attachmentDownload(RequestVO requestVo, AuthDetailsVo authDetailsVo) {
		String query = "FROM RequestEntity where deleteFlag = " + CommonConstant.FLAG_ZERO + " AND  requestId = "
				+ requestVo.getRequestId() + "and entityLicenseId = '" + authDetailsVo.getEntityId() + "'";

		RequestEntity requestEntity = (RequestEntity) getEntityManager().createQuery(query).getSingleResult();

		return requestEntity;
	}

	@SuppressWarnings("unchecked")
	public List<Object> getAllApproval(int requestId) throws CommonException , Exception {

		List<Object> requestWorkFlowAuditEntityList = null;
		try {

			String query = "select audit.*,CONCAT(us.FIRST_NAME,' ',us.LAST_NAME), req.rin_tr_request_sequence "
					+ " from "+getRtaDatabaseSchema()+".rin_tr_req_workflow_audit audit "
					+ " left join "+getCommonDatabaseSchema()+".user us"
					+ " on audit.rin_tr_req_workflow_audit_user_id = us.USER_ID "
					+ " left join "+getRtaDatabaseSchema()+".rin_tr_request req"
					+ " on req.idrin_tr_request_id = audit.rin_tr_request_id where " + " audit.delete_flag = "
					+ CommonConstant.FLAG_ZERO + " and us.delete_flag = '" + CommonConstant.FLAG_ZERO
					+ "' AND audit.rin_tr_request_id = " + requestId
					+ " ORDER BY audit.idrin_tr_req_workflow_audit_id ";

			requestWorkFlowAuditEntityList = (List<Object>) getEntityManager().createNativeQuery(query).getResultList();

		} catch (Exception e) {
			Log.info("Request Dao getAllApproval Exception",e);
			throw new CommonException("requestWorkFlowAuditListFailure");
		}

		return requestWorkFlowAuditEntityList;
	}

	@SuppressWarnings("unchecked")
	public List<Object> getAllApprovalwithMail(int requestId) throws CommonException ,Exception {

		List<Object> requestWorkFlowAuditEntityList = null;
		try {
		String query = "select audit.*,CONCAT(us.FIRST_NAME,' ',us.LAST_NAME), req.rin_tr_request_sequence "
				+ " from "+getRtaDatabaseSchema()+".rin_tr_req_workflow_audit audit left join common_rta_2_local.user us"
				+ " on audit.rin_tr_req_workflow_audit_user_id = us.USER_ID "
				+ " left join "+getRtaDatabaseSchema()+".rin_tr_request req"
				+ " on req.idrin_tr_request_id = audit.rin_tr_request_id where " + " audit.delete_flag = "
				+ CommonConstant.FLAG_ZERO + " and us.delete_flag = '" + CommonConstant.FLAG_ZERO
				+ "' AND audit.rin_tr_request_id = " + requestId + " ORDER BY audit.idrin_tr_req_workflow_audit_id ";

		  requestWorkFlowAuditEntityList = (List<Object>) getEntityManager().createNativeQuery(query)
				.getResultList();
		} catch (Exception e) {
			Log.info("Request Dao getAllApprovalwithMail Exception",e);
			throw new CommonException("requestWorkFlowAuditListFailure");
		}
		
		return requestWorkFlowAuditEntityList;
	}

	public void directapprovedAudit(int requestId, AuthDetailsVo authDetailsVo) throws CommonException , Exception{

		try {
			String query = " Update RequestWorkFlowAuditEntity Set descisionType = 1," + " updateBy = "
					+ authDetailsVo.getUserId() + " ,updateDate = '" + CommonConstant.getCurrentDateTimeAsString()
					+ "' Where requestId = " + requestId + " AND approvalExecuter = 1";

			getEntityManager().createQuery(query).executeUpdate();

		} catch (CommonException e) {
			Log.info("Request Dao directapprovedAudit Exception",e);
			throw new CommonException("requestWorkFlowAuditListFailure");
		}
	}

	/**
	 * This method is used to find the record in the database by executng the
	 * query.
	 * 
	 * @param requestLoad
	 *            RequestVO
	 * @return resultobject List<Object>
	 */
	@SuppressWarnings("unchecked")
	public List<Object> findRequest(RequestVO requestLoad, AuthDetailsVo authDetailsVo) {

		List<Object> resultobject = new ArrayList<Object>();
		try {

			String query = "SELECT req.idrin_tr_request_id,req.rin_tr_request_code,req.rin_tr_request_date,rtype.rin_ma_request_type_name,"//3
					+ " rstype.rin_ma_request_subtype_name,loca.USER_LOCATION_NAME,sub.rin_ma_sublocation_name,depart.USER_DEPARTMENT_NAME, "//7
					+ " cur.rin_ma_current_status_name,req.rin_ma_request_type_id,req.rin_ma_request_subtype_id,"//10
					+ " req.rin_tr_request_location_id,req.rin_tr_request_sub_location_id,req.rin_tr_request_user_department_id,"//13
					+ " req.current_status_id,req.rin_tr_request_priority,req.rin_tr_request_from_date,req.rin_tr_request_to_date,"//17
					+ " req.rin_tr_request_is_cancel,req.rin_tr_request_mobile_no,req.rin_tr_request_extension,req.rin_tr_request_sequence,"//21
					+ " rdet.idrin_tr_request_detail_id,rdet.rin_tr_request_id,rdet.rin_ma_req_screen_config_id,"//24
					+ " rdet.rin_ma_req_screen_detail_config_id,rdet.rin_tr_request_detail_field_type,"//26
					+ " rdet.rin_tr_request_detail_field_value,rdet.rin_tr_request_detail_is_active,"//28
					+ " req.rin_tr_request_subject, scr.rin_ma_req_screen_detail_config_field_value,"//30
					+ " scr.rin_ma_req_screen_detail_config_field_name,req.create_date,cur.rin_ma_current_status_code,req.rin_tr_request_sub_id, "//34
					+ " req.create_by, CONCAT(us.FIRST_NAME,' ',us.LAST_NAME),req.rin_tr_request_attachment "//37
					+ " ,loc.USER_LOCATION_NAME reqloc,sl.rin_ma_sublocation_name reqSubloc"
					+ " ,req.rin_tr_request_user_location_id,req.rin_tr_request_sublocation_id as sublocationId ,"
					+ "  req.remarks , req.forward_redirect_remarks , req.forward_request_id , req.rin_tr_subrequest"
					+ " FROM "+getRtaDatabaseSchema()+".rin_tr_request req "
					+ " LEFT JOIN "+getRtaDatabaseSchema()+".rin_ma_request_type rtype ON req.rin_ma_request_type_id = rtype.idrin_ma_request_type_id "
					+ " LEFT JOIN "+getRtaDatabaseSchema()+".rin_ma_request_subtype rstype ON req.rin_ma_request_subtype_id = rstype.idrin_ma_request_subtype_id "
					
				/*	+ " LEFT JOIN "+getCommonDatabaseSchema()+".user_location loca ON req.rin_tr_request_location_id = loca.USER_LOCATION_ID "
					+ " LEFT JOIN "+getCommonDatabaseSchema()+".rin_ma_sublocation sub ON req.rin_tr_request_sub_location_id = sub.idrin_ma_sublocation_sublocationId "
*/
					
					+ " LEFT JOIN "+getCommonDatabaseSchema()+".user_location loca ON req.rin_tr_request_user_location_id = loca.USER_LOCATION_ID "
					+ " LEFT JOIN "+getCommonDatabaseSchema()+".rin_ma_sublocation sub ON req.rin_tr_request_sublocation_id = sub.idrin_ma_sublocation_sublocationId "
					+ " LEFT JOIN "+getCommonDatabaseSchema()+".user_department depart ON req.rin_tr_request_user_department_id = depart.USER_DEPARTMENT_ID "
					+ " LEFT JOIN "+getRtaDatabaseSchema()+".rin_ma_current_status cur ON req.current_status_id = cur.idrin_ma_current_status_id "
					+ " LEFT JOIN "+getRtaDatabaseSchema()+".rin_tr_request_detail rdet ON req.idrin_tr_request_id = rdet.rin_tr_request_id "
					+ " LEFT JOIN "+getCommonDatabaseSchema()+".user us ON req.create_by = us.USER_ID"
					+ " LEFT JOIN "+getCommonDatabaseSchema()+".user_location loc ON req.rin_tr_request_location_id = loc.USER_LOCATION_ID "
					+ " LEFT JOIN "+getCommonDatabaseSchema()+".rin_ma_sublocation sl ON req.rin_tr_request_sub_location_id = sl.idrin_ma_sublocation_sublocationId "
					+ " LEFT JOIN "+getRtaDatabaseSchema()+".rin_ma_req_screen_detail_config scr "
					+ " on rdet.rin_ma_req_screen_detail_config_id = scr.idrin_ma_req_screen_detail_config_id"
					+ " LEFT JOIN "+getRtaDatabaseSchema()+".rin_ma_req_screen_config sc "
					+ " on sc.idrin_ma_req_screen_config_id = scr.rin_ma_req_screen_config_id"
					+ " where req.delete_flag = " + CommonConstant.FLAG_ZERO + " and req.idrin_tr_request_id = "
					+ requestLoad.getRequestId() + " and sc.rin_ma_req_screen_config_is_active = "
					+ CommonConstant.ACTIVE + " and sc.delete_flag = " + CommonConstant.FLAG_ZERO
					+ " and scr.rin_ma_req_screen_detail_config_is_active = " + CommonConstant.ACTIVE;

			resultobject = (List<Object>) getEntityManager().createNativeQuery(query).getResultList();

		} catch (Exception e) {
			Log.info("Request Dao findRequest Exception",e);
			throw new CommonException("dataFailure");
		}
		return resultobject;
	}

	
	@SuppressWarnings("unchecked")
	public List<Object> findApproverView(RequestVO requestLoad, AuthDetailsVo authDetailsVo) {

		List<Object> resultobject = new ArrayList<Object>();
		try {

			String query = "SELECT req.idrin_tr_request_id,req.rin_tr_request_code,req.rin_tr_request_date,rtype.rin_ma_request_type_name,"//3
					+ " rstype.rin_ma_request_subtype_name,loca.USER_LOCATION_NAME,sub.rin_ma_sublocation_name,depart.USER_DEPARTMENT_NAME, "//7
					+ " cur.rin_ma_current_status_name,req.rin_ma_request_type_id,req.rin_ma_request_subtype_id,"//10
					+ " req.rin_tr_request_location_id,req.rin_tr_request_sub_location_id,req.rin_tr_request_user_department_id,"//13
					+ " req.current_status_id,req.rin_tr_request_priority,req.rin_tr_request_from_date,req.rin_tr_request_to_date,"//17
					+ " req.rin_tr_request_is_cancel,req.rin_tr_request_mobile_no,req.rin_tr_request_extension,req.rin_tr_request_sequence,"//21
					+ " rdet.idrin_tr_request_detail_id,rdet.rin_tr_request_id,rdet.rin_ma_req_screen_config_id,"//24
					+ " rdet.rin_ma_req_screen_detail_config_id,rdet.rin_tr_request_detail_field_type,"//26
					+ " rdet.rin_tr_request_detail_field_value,rdet.rin_tr_request_detail_is_active,"//28
					+ " req.rin_tr_request_subject, scr.rin_ma_req_screen_detail_config_field_value,"//30
					+ " scr.rin_ma_req_screen_detail_config_field_name,req.create_date,cur.rin_ma_current_status_code,req.rin_tr_request_sub_id, "//34
					+ " req.create_by, CONCAT(us.FIRST_NAME,' ',us.LAST_NAME),req.rin_tr_request_attachment "//37
					+ " ,loc.USER_LOCATION_NAME reqloc,sl.rin_ma_sublocation_name reqSubloc"
					+ " ,req.rin_tr_request_user_location_id,req.rin_tr_request_sublocation_id as sublocationId ,"
					+ "  req.remarks , req.forward_redirect_remarks , req.forward_request_id , req.rin_tr_subrequest"
					+ " FROM "+getRtaDatabaseSchema()+".rin_tr_request req "
					+ " LEFT JOIN "+getRtaDatabaseSchema()+".rin_ma_request_type rtype ON req.rin_ma_request_type_id = rtype.idrin_ma_request_type_id "
					+ " LEFT JOIN "+getRtaDatabaseSchema()+".rin_ma_request_subtype rstype ON req.rin_ma_request_subtype_id = rstype.idrin_ma_request_subtype_id "
					
				/*	+ " LEFT JOIN "+getCommonDatabaseSchema()+".user_location loca ON req.rin_tr_request_location_id = loca.USER_LOCATION_ID "
					+ " LEFT JOIN "+getCommonDatabaseSchema()+".rin_ma_sublocation sub ON req.rin_tr_request_sub_location_id = sub.idrin_ma_sublocation_sublocationId "
*/
					
					+ " LEFT JOIN "+getCommonDatabaseSchema()+".user_location loca ON req.rin_tr_request_user_location_id = loca.USER_LOCATION_ID "
					+ " LEFT JOIN "+getCommonDatabaseSchema()+".rin_ma_sublocation sub ON req.rin_tr_request_sublocation_id = sub.idrin_ma_sublocation_sublocationId "
					+ " LEFT JOIN "+getCommonDatabaseSchema()+".user_department depart ON req.rin_tr_request_user_department_id = depart.USER_DEPARTMENT_ID "
					+ " LEFT JOIN "+getRtaDatabaseSchema()+".rin_ma_current_status cur ON req.current_status_id = cur.idrin_ma_current_status_id "
					+ " LEFT JOIN "+getRtaDatabaseSchema()+".rin_tr_request_detail rdet ON req.idrin_tr_request_id = rdet.rin_tr_request_id "
					+ " LEFT JOIN "+getCommonDatabaseSchema()+".user us ON req.create_by = us.USER_ID"
					+ " LEFT JOIN "+getCommonDatabaseSchema()+".user_location loc ON req.rin_tr_request_location_id = loc.USER_LOCATION_ID "
					+ " LEFT JOIN "+getCommonDatabaseSchema()+".rin_ma_sublocation sl ON req.rin_tr_request_sub_location_id = sl.idrin_ma_sublocation_sublocationId "
					+ " LEFT JOIN "+getRtaDatabaseSchema()+".rin_ma_req_screen_detail_config scr "
					+ " on rdet.rin_ma_req_screen_detail_config_id = scr.idrin_ma_req_screen_detail_config_id"
					+ " LEFT JOIN "+getRtaDatabaseSchema()+".rin_ma_req_screen_config sc "
					+ " on sc.idrin_ma_req_screen_config_id = scr.rin_ma_req_screen_config_id"
					+ " where req.delete_flag = " + CommonConstant.FLAG_ZERO + " and req.idrin_tr_request_id = "
					+ requestLoad.getRequestId() + " and sc.rin_ma_req_screen_config_is_active = "
					+ CommonConstant.ACTIVE + " and sc.delete_flag = " + CommonConstant.FLAG_ZERO
					+ " and scr.rin_ma_req_screen_detail_config_is_active = " + CommonConstant.ACTIVE;

			resultobject = (List<Object>) getEntityManager().createNativeQuery(query).getResultList();

		} catch (Exception e) {
			Log.info("Request Dao findRequest Exception",e);
			throw new CommonException("dataFailure");
		}
		return resultobject;
	}

	
	@SuppressWarnings("unchecked")
	public List<Object> resolverRequestView(RequestVO requestLoad, AuthDetailsVo authDetailsVo) {

		List<Object> resultobject = new ArrayList<Object>();
		try {

			String query = "SELECT req.idrin_tr_request_id,req.rin_tr_request_code,req.rin_tr_request_date,rtype.rin_ma_request_type_name,"//3
					+ " rstype.rin_ma_request_subtype_name,loca.USER_LOCATION_NAME,sub.rin_ma_sublocation_name,depart.USER_DEPARTMENT_NAME, "//7
					+ " cur.rin_ma_current_status_name,req.rin_ma_request_type_id,req.rin_ma_request_subtype_id,"//10
					+ " req.rin_tr_request_location_id,req.rin_tr_request_sub_location_id,req.rin_tr_request_user_department_id,"//13
					+ " req.current_status_id,req.rin_tr_request_priority,req.rin_tr_request_from_date,req.rin_tr_request_to_date,"//17
					+ " req.rin_tr_request_is_cancel,req.rin_tr_request_mobile_no,req.rin_tr_request_extension,req.rin_tr_request_sequence,"//21
					+ " rdet.idrin_tr_request_detail_id,rdet.rin_tr_request_id,rdet.rin_ma_req_screen_config_id,"//24
					+ " rdet.rin_ma_req_screen_detail_config_id,rdet.rin_tr_request_detail_field_type,"//26
					+ " rdet.rin_tr_request_detail_field_value,rdet.rin_tr_request_detail_is_active,"//28
					+ " req.rin_tr_request_subject, scr.rin_ma_req_screen_detail_config_field_value,"//30
					+ " scr.rin_ma_req_screen_detail_config_field_name,req.create_date,cur.rin_ma_current_status_code,req.rin_tr_request_sub_id, "//34
					+ " req.create_by, CONCAT(us.FIRST_NAME,' ',us.LAST_NAME),req.rin_tr_request_attachment "//37
					+ " ,loc.USER_LOCATION_NAME reqloc,sl.rin_ma_sublocation_name reqSubloc"
					+ " ,req.rin_tr_request_user_location_id,req.rin_tr_request_sublocation_id as sublocationId ,"
					+ "  req.remarks , req.forward_redirect_remarks , req.forward_request_id , req.rin_tr_subrequest ,"
					+ " loca1.USER_LOCATION_NAME as location1 ,sub1.rin_ma_sublocation_name as sublocation1"
					+ " FROM "+getRtaDatabaseSchema()+".rin_tr_request req "
					+ " LEFT JOIN "+getRtaDatabaseSchema()+".rin_ma_request_type rtype ON req.rin_ma_request_type_id = rtype.idrin_ma_request_type_id "
					+ " LEFT JOIN "+getRtaDatabaseSchema()+".rin_ma_request_subtype rstype ON req.rin_ma_request_subtype_id = rstype.idrin_ma_request_subtype_id "
					
					+ " LEFT JOIN "+getCommonDatabaseSchema()+".user_location loca1 ON req.rin_tr_request_location_id = loca1.USER_LOCATION_ID "
					+ " LEFT JOIN "+getCommonDatabaseSchema()+".rin_ma_sublocation sub1 ON req.rin_tr_request_sub_location_id = sub1.idrin_ma_sublocation_sublocationId "
					
					+ " LEFT JOIN "+getCommonDatabaseSchema()+".user_location loca ON req.rin_tr_request_user_location_id = loca.USER_LOCATION_ID "
					+ " LEFT JOIN "+getCommonDatabaseSchema()+".rin_ma_sublocation sub ON req.rin_tr_request_sublocation_id = sub.idrin_ma_sublocation_sublocationId "
					
					+ " LEFT JOIN "+getCommonDatabaseSchema()+".user_department depart ON req.rin_tr_request_user_department_id = depart.USER_DEPARTMENT_ID "
					+ " LEFT JOIN "+getRtaDatabaseSchema()+".rin_ma_current_status cur ON req.current_status_id = cur.idrin_ma_current_status_id "
					+ " LEFT JOIN "+getRtaDatabaseSchema()+".rin_tr_request_detail rdet ON req.idrin_tr_request_id = rdet.rin_tr_request_id "
					+ " LEFT JOIN "+getCommonDatabaseSchema()+".user us ON req.create_by = us.USER_ID"
					+ " LEFT JOIN "+getCommonDatabaseSchema()+".user_location loc ON req.rin_tr_request_location_id = loc.USER_LOCATION_ID "
					+ " LEFT JOIN "+getCommonDatabaseSchema()+".rin_ma_sublocation sl ON req.rin_tr_request_sub_location_id = sl.idrin_ma_sublocation_sublocationId "
					+ " LEFT JOIN "+getRtaDatabaseSchema()+".rin_ma_req_screen_detail_config scr "
					+ " on rdet.rin_ma_req_screen_detail_config_id = scr.idrin_ma_req_screen_detail_config_id"
					+ " LEFT JOIN "+getRtaDatabaseSchema()+".rin_ma_req_screen_config sc "
					+ " on sc.idrin_ma_req_screen_config_id = scr.rin_ma_req_screen_config_id"
					+ " where req.delete_flag = " + CommonConstant.FLAG_ZERO + " and req.idrin_tr_request_id = "
					+ requestLoad.getRequestId() + " and sc.rin_ma_req_screen_config_is_active = "
					+ CommonConstant.ACTIVE + " and sc.delete_flag = " + CommonConstant.FLAG_ZERO
					+ " and scr.rin_ma_req_screen_detail_config_is_active = " + CommonConstant.ACTIVE;

			resultobject = (List<Object>) getEntityManager().createNativeQuery(query).getResultList();

		} catch (Exception e) {
			Log.info("Request Dao findRequest Exception",e);
			throw new CommonException("dataFailure");
		}
		return resultobject;
	}
	
	
	
	/**
	 * Method is used for reopen the the request
	 * 
	 * @param requestId
	 * @return
	 */
	public List<RequestWorkFlowAuditEntity> reOpen(RequestVO requestVo) {

		String query = " SELECT audit FROM RequestWorkFlowAuditEntity audit,RequestEntity req"
				+ "  ,CurrentStatusEntity status" + " WHERE " + " audit.requestId = req.requestId and "
				+ " req.currentStatusId= status.currentStatusId and audit.deleteFlag = " + CommonConstant.FLAG_ZERO
				+ " AND req.deleteFlag = " + CommonConstant.FLAG_ZERO

				+ " AND audit.requestId = " + requestVo.getRequestId() + " AND status.currentStatusCode = '"
				+ DecisionTypeEnum.COM.toString() + "'" + " AND audit.approvalExecuter = "
				+ CommonConstant.CONSTANT_THREE;
		if (requestVo.getCurrentStatusId() == CommonConstant.CONSTANT_EIGHT) {
			query = query + " and audit.requestWorkflowAuditIsActive = " + CommonConstant.CONSTANT_ONE;
		} else if (requestVo.getCurrentStatusId() == CommonConstant.CONSTANT_SEVEN) {
			query = query + " and audit.requestWorkflowAuditIsActive = " + CommonConstant.CONSTANT_ONE;

		}

		List<RequestWorkFlowAuditEntity> requestWorkFlowAuditEntityList = (List<RequestWorkFlowAuditEntity>) getEntityManager()
				.createQuery(query, RequestWorkFlowAuditEntity.class).getResultList();

		return requestWorkFlowAuditEntityList;
	}

	public int saveValidation(RequestVO requestVo, AuthDetailsVo authDetailsVo) {

		String query = "SELECT COUNT(*)  FROM rin_tr_request r"
				+ " LEFT JOIN rin_ma_current_status c ON c.idrin_ma_current_status_id = r.current_status_id"
				+ " WHERE r.rin_ma_entity_id = '" + authDetailsVo.getEntityId() + "' "
				+ " and r.rin_ma_request_type_id = " + requestVo.getRequestTypeId()
				+ " AND r.rin_ma_request_subtype_id = " + requestVo.getRequestSubtypeId()
				+ " AND r.rin_tr_request_location_id = " + requestVo.getReqLocationId()
				+ " AND r.rin_tr_request_sublocation_id = " + requestVo.getReqSublocationId() + " AND r.create_by = "
				+ authDetailsVo.getUserId() + " AND (c.rin_ma_current_status_code NOT IN  ('CLO','CAN'))";

		BigInteger i = (BigInteger) getEntityManager().createNativeQuery(query).getSingleResult();

		int count = i.intValue();

		return count;

	}

	/**
	 * Method is used to find the Request type list
	 * 
	 * @return requestTypeList
	 */
	@SuppressWarnings("unchecked")
	public List<RequestTypeEntity> findrequestTypeList(RequestVO requestVo, AuthDetailsVo authDetailsVo) {

		List<RequestTypeEntity> requestTypeList = new ArrayList<RequestTypeEntity>();

		String query = "select distinct(rt) FROM RequestTypeEntity rt "
				+ " ,RequestWorkFlowEntity rwf,RequestWorkFlowDetailsEntity rd "
				+ " where rt.requestTypeId = rwf.requestTypeId" + "   and rwf.reqWorkFlowId = rd.reqWorkFlowId"
				+ "  and rt.entityLicenseId = '" + authDetailsVo.getEntityId() + "' and rwf.deleteFlag = "
				+ CommonConstant.FLAG_ZERO + " AND rd.deleteFlag = " + CommonConstant.FLAG_ZERO
				+ " and rd.workFlowDepartmentId = " + requestVo.getDepartmentId() + " and rd.workFlowLocationId = "
				+ requestVo.getId() + " and rd.workFlowSublocationId = " + requestVo.getSublocationId()
				+ " and rd.reqWorkFlowDetailsIsActive =" + CommonConstant.CONSTANT_ONE
				+ " order by rt.requestTypeId desc ";

		requestTypeList = (List<RequestTypeEntity>) getEntityManager().createQuery(query).getResultList();

		List<RequestTypeEntity> requestTypelist = new ArrayList<RequestTypeEntity>();

		query = "select distinct(rt) FROM RequestTypeEntity rt "
				+ " ,RequestWorkFlowEntity rwf,RequestWorkFlowDetailsEntity rd "
				+ " where rt.requestTypeId = rwf.requestTypeId" + "   and rwf.reqWorkFlowId = rd.reqWorkFlowId"
				+ "  and  rt.entityLicenseId = '" + authDetailsVo.getEntityId() + "' and rwf.deleteFlag = "
				+ CommonConstant.FLAG_ZERO + " AND rd.deleteFlag = " + CommonConstant.FLAG_ZERO
				+ " and rd.workFlowDepartmentId = " + CommonConstant.CONSTANT_ZERO + " and rd.workFlowLocationId = "
				+ requestVo.getId() + " and rd.workFlowSublocationId = " + requestVo.getSublocationId()
				+ " and rd.reqWorkFlowDetailsIsActive =" + CommonConstant.BOOLEAN_TRUE;

		requestTypelist = (List<RequestTypeEntity>) getEntityManager().createQuery(query).getResultList();

		if (requestTypelist.size() != 0) {

			requestTypeList.addAll(requestTypelist);
		}

		List<RequestTypeEntity> requesttypelist = new ArrayList<RequestTypeEntity>();

		query = "select distinct(rt) FROM RequestTypeEntity rt "
				+ " ,RequestWorkFlowEntity rwf,RequestWorkFlowDetailsEntity rd "
				+ " where rt.requestTypeId = rwf.requestTypeId" + "   and rwf.reqWorkFlowId = rd.reqWorkFlowId"
				+ "  and  rt.entityLicenseId = '" + authDetailsVo.getEntityId() + "' and rwf.deleteFlag = "
				+ CommonConstant.FLAG_ZERO + " AND rd.deleteFlag = " + CommonConstant.FLAG_ZERO
				+ " and rd.workFlowDepartmentId = " + CommonConstant.CONSTANT_ZERO + " and rd.workFlowLocationId = "
				+ requestVo.getId() + " and rd.workFlowSublocationId = " + CommonConstant.CONSTANT_ZERO
				+ " and rd.reqWorkFlowDetailsIsActive =" + CommonConstant.CONSTANT_ONE;

		requesttypelist = (List<RequestTypeEntity>) getEntityManager().createQuery(query).getResultList();

		if (requesttypelist.size() != 0) {

			requestTypeList.addAll(requesttypelist);
		}

		List<RequestTypeEntity> requesttype = new ArrayList<RequestTypeEntity>();

		query = "select distinct(rt) FROM RequestTypeEntity rt "
				+ " ,RequestWorkFlowEntity rwf,RequestWorkFlowDetailsEntity rd "
				+ " where rt.requestTypeId = rwf.requestTypeId" + "   and rwf.reqWorkFlowId = rd.reqWorkFlowId"
				+ "  and  rt.entityLicenseId = '" + authDetailsVo.getEntityId() + "' and rwf.deleteFlag = "
				+ CommonConstant.FLAG_ZERO + "  AND rd.deleteFlag = " + CommonConstant.FLAG_ZERO
				+ " and rd.workFlowDepartmentId = " + CommonConstant.CONSTANT_ZERO + " and rd.workFlowLocationId = "
				+ CommonConstant.CONSTANT_ZERO + " and rd.workFlowSublocationId = " + CommonConstant.CONSTANT_ZERO
				+ " and rd.reqWorkFlowDetailsIsActive =" + CommonConstant.CONSTANT_ONE;

		requesttype = (List<RequestTypeEntity>) getEntityManager().createQuery(query).getResultList();

		if (requesttype.size() != 0) {

			requestTypeList.addAll(requesttype);
		}

		List<RequestTypeEntity> requestt = new ArrayList<RequestTypeEntity>();

		query = "select distinct(rt) FROM RequestTypeEntity rt "
				+ " ,RequestWorkFlowEntity rwf,RequestWorkFlowDetailsEntity rd "
				+ " where rt.requestTypeId = rwf.requestTypeId" + "   and rwf.reqWorkFlowId = rd.reqWorkFlowId"
				+ "  and  rt.entityLicenseId = '" + authDetailsVo.getEntityId() + "' and rwf.deleteFlag = "
				+ CommonConstant.FLAG_ZERO + "  AND rd.deleteFlag = " + CommonConstant.FLAG_ZERO
				+ " and rd.workFlowDepartmentId = " + requestVo.getDepartmentId() + " and rd.workFlowLocationId = "
				+ CommonConstant.CONSTANT_ZERO + " and rd.workFlowSublocationId = " + requestVo.getSublocationId()
				+ " and rd.reqWorkFlowDetailsIsActive =" + CommonConstant.CONSTANT_ONE;

		requestt = (List<RequestTypeEntity>) getEntityManager().createQuery(query).getResultList();

		if (requestt.size() != 0) {

			requestTypeList.addAll(requestt);
		}

		List<RequestTypeEntity> request = new ArrayList<RequestTypeEntity>();

		query = "select distinct(rt) FROM RequestTypeEntity rt "
				+ " ,RequestWorkFlowEntity rwf,RequestWorkFlowDetailsEntity rd "
				+ " where rt.requestTypeId = rwf.requestTypeId" + "   and rwf.reqWorkFlowId = rd.reqWorkFlowId"
				+ "  and  rt.entityLicenseId = '" + authDetailsVo.getEntityId() + "' and rwf.deleteFlag = "
				+ CommonConstant.FLAG_ZERO + "  AND rd.deleteFlag = " + CommonConstant.FLAG_ZERO
				+ " and rd.workFlowDepartmentId = " + CommonConstant.CONSTANT_ZERO + " and rd.workFlowLocationId = "
				+ CommonConstant.CONSTANT_ZERO + " and rd.workFlowSublocationId = " + requestVo.getSublocationId()
				+ " and rd.reqWorkFlowDetailsIsActive =" + CommonConstant.CONSTANT_ONE;

		request = (List<RequestTypeEntity>) getEntityManager().createQuery(query).getResultList();

		if (request.size() != 0) {

			requestTypeList.addAll(request);
		}

		List<RequestTypeEntity> reques = new ArrayList<RequestTypeEntity>();

		query = "select distinct(rt) FROM RequestTypeEntity rt "
				+ " ,RequestWorkFlowEntity rwf,RequestWorkFlowDetailsEntity rd "
				+ " where rt.requestTypeId = rwf.requestTypeId" + "   and rwf.reqWorkFlowId = rd.reqWorkFlowId"
				+ "  and  rt.entityLicenseId = '" + authDetailsVo.getEntityId() + "' and rwf.deleteFlag = "
				+ CommonConstant.FLAG_ZERO + "  AND rd.deleteFlag = " + CommonConstant.FLAG_ZERO
				+ " and rd.workFlowDepartmentId = " + requestVo.getDepartmentId() + " and rd.workFlowLocationId = "
				+ CommonConstant.CONSTANT_ZERO + " and rd.workFlowSublocationId = " + CommonConstant.CONSTANT_ZERO
				+ " and rd.reqWorkFlowDetailsIsActive =" + CommonConstant.CONSTANT_ONE;

		reques = (List<RequestTypeEntity>) getEntityManager().createQuery(query).getResultList();

		if (reques.size() != 0) {

			requestTypeList.addAll(reques);
		}

		List<RequestTypeEntity> req = new ArrayList<RequestTypeEntity>();

		query = "select distinct(rt) FROM RequestTypeEntity rt "
				+ " ,RequestWorkFlowEntity rwf,RequestWorkFlowDetailsEntity rd "
				+ " where rt.requestTypeId = rwf.requestTypeId" + "   and rwf.reqWorkFlowId = rd.reqWorkFlowId"
				+ "  and  rt.entityLicenseId = '" + authDetailsVo.getEntityId() + "' and rwf.deleteFlag = "
				+ CommonConstant.FLAG_ZERO + "  AND rd.deleteFlag = " + CommonConstant.FLAG_ZERO
				+ " and rd.workFlowDepartmentId = " + requestVo.getDepartmentId() + " and rd.workFlowLocationId = "
				+ requestVo.getId() + " and rd.workFlowSublocationId = " + CommonConstant.CONSTANT_ZERO
				+ " and rd.reqWorkFlowDetailsIsActive =" + CommonConstant.CONSTANT_ONE;

		req = (List<RequestTypeEntity>) getEntityManager().createQuery(query).getResultList();

		if (req.size() != 0) {

			requestTypeList.addAll(req);
		}

		Set<RequestTypeEntity> obj = new LinkedHashSet<RequestTypeEntity>();

		obj.addAll(requestTypeList);

		requestTypeList.clear();

		requestTypeList.addAll(obj);

		return requestTypeList;

	}

	/**
	 * Method is used to get all the screen details of request
	 * 
	 * @param typeId
	 * @param subTypeId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RequestScreenDetailConfigurationEntity> getAllScreenDetail(int typeId, int subTypeId,
			AuthDetailsVo authDetailsVo) {

		String query = "select scrdet FROM RequestScreenDetailConfigurationEntity scrdet "
				+ " ,RequestScreenConfigurationEntity scr "
				+ " where scrdet.requestScreenConfigId = scr.requestScreenConfigId " + " and scrdet.entityLicenseId = '"
				+ authDetailsVo.getEntityId() + "' and scrdet.deleteFlag = " + CommonConstant.FLAG_ZERO
				+ " and scr.deleteFlag = " + CommonConstant.FLAG_ZERO
				+ " and scrdet.requestScreenDetailConfigurationIsActive = " + CommonConstant.CONSTANT_ONE
				+ " and scr.requestTypeId = " + typeId + " and scr.requestSubtypeId = " + subTypeId
				+ " and scr.requestScreenConfigurationIsActive = " + CommonConstant.CONSTANT_ONE
				+ " ORDER BY scrdet.requestScreenDetailConfigurationSequance asc ";

		List<RequestScreenDetailConfigurationEntity> list_RequestScreenDetailConfigurationEntity = (List<RequestScreenDetailConfigurationEntity>) getEntityManager()
				.createQuery(query).getResultList();

		return list_RequestScreenDetailConfigurationEntity;
	}

	public Object[] findOne(int userId) {

		String query = "SELECT u.MOBILE,u.USER_LOCATION_ID,ul.USER_LOCATION_NAME,u.USER_SUBLOCATION_ID,"
				+ " sl.rin_ma_sublocation_name,u.USER_DEPARTMENT_ID,ud.USER_DEPARTMENT_NAME"
				+ " FROM "+getCommonDatabaseSchema()+".user u"
				+ " LEFT JOIN "+getCommonDatabaseSchema()+".user_location ul ON ul.USER_LOCATION_ID = u.USER_LOCATION_ID"
				+ " LEFT JOIN "+getCommonDatabaseSchema()+".rin_ma_sublocation sl ON sl.idrin_ma_sublocation_sublocationId = u.USER_SUBLOCATION_ID"
				+ " LEFT JOIN "+getCommonDatabaseSchema()+".user_department ud ON ud.USER_DEPARTMENT_ID = u.USER_DEPARTMENT_ID"
				+ " WHERE u.USER_ID =" + userId;

		Object[] qry = (Object[]) getEntityManager().createNativeQuery(query).getSingleResult();

		return qry;
	}

	@SuppressWarnings("unchecked")
	public RequestWorkFlowAuditEntity getWorkFlow(RequestWorkFlowAuditEntity requestWorkFlowAuditEntity,
			AuthDetailsVo authDetailsVo,Integer forwardFlag) throws CommonException , Exception {
		
		RequestWorkFlowAuditEntity requestWorkFlowAuditEn = new RequestWorkFlowAuditEntity();
		try {
		
		String query = "FROM RequestWorkFlowAuditEntity where deleteFlag = " + CommonConstant.FLAG_ZERO
				+ " AND userId = " + authDetailsVo.getUserId() + " AND requestId = "
				+ requestWorkFlowAuditEntity.getRequestId();

		List<RequestWorkFlowAuditEntity> requestWorkFlowAuditEnList = null;
		
		requestWorkFlowAuditEnList = (List<RequestWorkFlowAuditEntity>) getEntityManager().createQuery(query)
				.getResultList();
		if (null != requestWorkFlowAuditEnList && requestWorkFlowAuditEnList.size() > 0) {

			requestWorkFlowAuditEn = requestWorkFlowAuditEnList.get(0);
			
		} else {
			
				requestWorkFlowAuditEn = requestConfigurationDAO.createDelegateResolver(requestWorkFlowAuditEntity, authDetailsVo,forwardFlag);
			}
		} catch (CommonException exe) {
			 
			throw new CommonException(exe.getMessage());
		}catch (Exception exe) {
		 
			throw new CommonException("modifyFailure");
		}

		return requestWorkFlowAuditEn;
	}
 
	 @SuppressWarnings("unchecked")
		public RequestWorkFlowAuditEntity getWorkFlowByRequestor(RequestWorkFlowAuditEntity requestWorkFlowAuditEntity,
				AuthDetailsVo authDetailsVo) {
					 
		 String query = "FROM RequestWorkFlowAuditEntity where deleteFlag = " + CommonConstant.FLAG_ZERO
					+ " AND requestId = " + requestWorkFlowAuditEntity.getRequestId() 
					+ " AND approvalExecuter = 3"
					+ " AND requestWorkflowAuditIsActive = 1";

			RequestWorkFlowAuditEntity requestWorkFlowAuditEn = null;

			requestWorkFlowAuditEn = (RequestWorkFlowAuditEntity) getEntityManager().createQuery(query).getSingleResult();
			if (null != requestWorkFlowAuditEn) {

				return requestWorkFlowAuditEn;
			} else {
				return null;
			}
		}		 
	 
	@SuppressWarnings("unchecked")
	public RequestEntity getRequestDetails(Integer requestId) {

		String query = "FROM RequestEntity where deleteFlag = " + CommonConstant.FLAG_ZERO + " AND requestId = "
				+ requestId;

		RequestEntity requestEntity = null;

		requestEntity = (RequestEntity) getEntityManager().createQuery(query).getSingleResult();

		return requestEntity;

	}
	
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getRequestScreenDetails(Integer requestId , AuthDetailsVo authDetailsVo) {

		String query = "SELECT req.idrin_tr_request_id ,screendetail.rin_ma_req_screen_detail_config_field_name , det.rin_tr_request_detail_field_value  FROM rin_tr_request  req "
				+ " LEFT JOIN rin_tr_request_detail det ON det.rin_tr_request_id = req.idrin_tr_request_id "
				+ " LEFT JOIN rin_ma_req_screen_config screenconfig ON screenconfig.rin_ma_request_type_id = req.rin_ma_request_type_id "
				+ " AND  screenconfig.rin_ma_request_subtype_id = req.rin_ma_request_subtype_id "
				+ " LEFT JOIN rin_ma_req_screen_detail_config screendetail ON screendetail.idrin_ma_req_screen_detail_config_id =det.rin_ma_req_screen_detail_config_id "
				+ " WHERE req.idrin_tr_request_id = " + requestId 
				+ " AND req.rin_ma_entity_id = " + authDetailsVo.getEntityId();

		List<Object[]> objList = new ArrayList<>();
		objList = (List<Object[]>) getEntityManager().createNativeQuery(query).getResultList();

		return objList;

	}	
	
	
	@SuppressWarnings("unchecked")
	public RequestWorkFlowEntity getReassignFlag(Integer workflowId) {

		String query = "FROM RequestWorkFlowEntity where deleteFlag = " + CommonConstant.FLAG_ZERO + " AND reqWorkFlowId = "
				+ workflowId;

		RequestWorkFlowEntity requestWorkFlowEntity = null;

		requestWorkFlowEntity = (RequestWorkFlowEntity) getEntityManager().createQuery(query).getSingleResult();

		return requestWorkFlowEntity;

	}
	
	
	@SuppressWarnings("unchecked")
	public RequestWorkFlowAuditEntity getResolverSlaList(int requestId) {		
		
		String query = "FROM RequestWorkFlowAuditEntity where deleteFlag = " + CommonConstant.FLAG_ZERO				 
						+ " AND requestId = " +  requestId 
						+ " AND approvalExecuter = 1 order by requestWorkFlowAuditId desc ";

		List<RequestWorkFlowAuditEntity> requestWorkFlowAuditEnList = null;

		RequestWorkFlowAuditEntity RequestWorkFlowAuditEntity = new RequestWorkFlowAuditEntity();
		
		requestWorkFlowAuditEnList = (List<RequestWorkFlowAuditEntity>) getEntityManager().createQuery(query).getResultList();
			
		if(null != requestWorkFlowAuditEnList && requestWorkFlowAuditEnList.size() > 0){
			RequestWorkFlowAuditEntity = requestWorkFlowAuditEnList.get(0);
		}
		
		return RequestWorkFlowAuditEntity;
	}
	
	
	@SuppressWarnings("unchecked")
	public RequestWorkFlowAuditEntity getApproverSlaList(int requestId , int sequence) {		
		
		int newSequence = sequence -1;
		
		String query = "FROM RequestWorkFlowAuditEntity where deleteFlag = " + CommonConstant.FLAG_ZERO				 
						+ " AND requestId = " +  requestId 
						+ " AND approvalExecuter = 1 "
						+ " AND descisionType = 1 "
						+ " AND sequence = " + newSequence
						+ " order by requestWorkFlowAuditId desc ";

		List<RequestWorkFlowAuditEntity> requestWorkFlowAuditEnList = null;

		RequestWorkFlowAuditEntity RequestWorkFlowAuditEntity = new RequestWorkFlowAuditEntity();
		
		requestWorkFlowAuditEnList = (List<RequestWorkFlowAuditEntity>) getEntityManager().createQuery(query).getResultList();
			
		/*if(null != requestWorkFlowAuditEnList && requestWorkFlowAuditEnList.size() == 1){
			RequestWorkFlowAuditEntity = null;
			
		}else*/ if(null != requestWorkFlowAuditEnList && requestWorkFlowAuditEnList.size() > 0 ){
			RequestWorkFlowAuditEntity = requestWorkFlowAuditEnList.get(0);
		}
		
		return RequestWorkFlowAuditEntity;
	}
	
	
	@SuppressWarnings("unchecked")
	public RequestWorkFlowAuditEntity getApproverSlaListForGroup(int requestId ) {		
		
		String query = "FROM RequestWorkFlowAuditEntity where deleteFlag = " + CommonConstant.FLAG_ZERO				 
						+ " AND requestId = " +  requestId 
						+ " AND approvalExecuter = 1 "
						+ " AND descisionType = 1 "
						+ " AND sequence = " + 1
						+ " order by requestWorkFlowAuditId desc ";

		List<RequestWorkFlowAuditEntity> requestWorkFlowAuditEnList = null;

		RequestWorkFlowAuditEntity RequestWorkFlowAuditEntity = new RequestWorkFlowAuditEntity();
		
		requestWorkFlowAuditEnList = (List<RequestWorkFlowAuditEntity>) getEntityManager().createQuery(query).getResultList();
			
	    if(null != requestWorkFlowAuditEnList && requestWorkFlowAuditEnList.size() > 0 ){
			RequestWorkFlowAuditEntity = requestWorkFlowAuditEnList.get(0);
		}
		
		return RequestWorkFlowAuditEntity;
	}
	
}
