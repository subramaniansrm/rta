package com.srm.rta.dao;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.NoResultException;

import org.jfree.util.Log;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.dao.CommonDAO;
import com.srm.coreframework.entity.CommonStorageEntity;
import com.srm.coreframework.entity.EntityLicense;
import com.srm.coreframework.entity.SubLocation;
import com.srm.coreframework.entity.UserDepartment;
import com.srm.coreframework.entity.UserEntity;
import com.srm.coreframework.entity.UserLocation;
import com.srm.coreframework.entity.UserMappingEntity;
import com.srm.coreframework.entity.UserRole;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.coreframework.vo.UserMappingVO;
import com.srm.coreframework.vo.UserMasterVO;
import com.srm.rta.entity.RemarksEntity;
import com.srm.rta.entity.RequestEntity;
import com.srm.rta.entity.RequestWorkFlowAuditEntity;
import com.srm.rta.entity.RequestWorkFlowEntity;
import com.srm.rta.entity.RequestWorkFlowSeqEntity;
import com.srm.rta.entity.RequestWorkFlowSlaEntity;
import com.srm.rta.vo.RequestVO;
import com.srm.rta.vo.RequestWorkFlowDetailsVO;
import com.srm.rta.vo.RequestWorkFlowExecuterVO;
import com.srm.rta.vo.RequestWorkFlowSequenceVO;
import com.srm.rta.vo.RequestWorkFlowVO;

import lombok.Data;

@Data
@Repository
public class RequestConfigurationDAO extends CommonDAO {

	@Value("${commonDatabaseSchema}")
	private String commonDatabaseSchema;

	@SuppressWarnings("unchecked")
	public List<Object> getAllSearch(RequestWorkFlowVO requestWorkFlowVo, AuthDetailsVo authDetailsVo) {

		String query = "SELECT wflow.rin_ma_request_workflow_code,"
				+ " wflow.rin_ma_req_workflow_description,reqtype.rin_ma_request_type_name,subtype.rin_ma_request_subtype_name,"
				+ " wflow.idrin_ma_req_workflow_id,wflow.rin_ma_req_workflow_is_active" + " FROM "
				+ getRtaDatabaseSchema() + ".rin_ma_req_workflow wflow " + " LEFT JOIN " + getRtaDatabaseSchema()
				+ ".rin_ma_request_type reqtype "
				+ " ON wflow.rin_ma_request_type_id = reqtype.idrin_ma_request_type_id " + " LEFT JOIN "
				+ getRtaDatabaseSchema() + ".rin_ma_request_subtype subtype "
				+ " ON wflow.rin_ma_request_subtype_id = subtype.idrin_ma_request_subtype_id "
				+ " where wflow.delete_flag  = " + CommonConstant.FLAG_ZERO + " and wflow.rin_ma_entity_id = "
				+ authDetailsVo.getEntityId();

		StringBuffer modifiedQuery = new StringBuffer(query);
		// search for RequestWorkFlow

		if (null != requestWorkFlowVo.getReqWorkFlowId()
				&& requestWorkFlowVo.getReqWorkFlowId() != CommonConstant.CONSTANT_ZERO) {
			modifiedQuery.append(" and wflow.idrin_ma_req_workflow_id = " + requestWorkFlowVo.getReqWorkFlowId());
		}
		if (requestWorkFlowVo.getRequestWorkFlowCode() != null
				&& !requestWorkFlowVo.getRequestWorkFlowCode().isEmpty()) {
			modifiedQuery.append(" and LOWER(wflow.rin_ma_request_workflow_code) LIKE LOWER('%"
					+ requestWorkFlowVo.getRequestWorkFlowCode() + "%')");
		}
		if (requestWorkFlowVo.getRequestTypeName() != null && !requestWorkFlowVo.getRequestTypeName().isEmpty()) {
			modifiedQuery.append(" and LOWER(reqtype.rin_ma_request_type_name) LIKE LOWER('%"
					+ requestWorkFlowVo.getRequestTypeName() + "%')");
		}

		if (requestWorkFlowVo.getRequestSubTypeName() != null && !requestWorkFlowVo.getRequestSubTypeName().isEmpty()) {

			modifiedQuery.append(" and LOWER(subtype.rin_ma_request_subtype_name) LIKE LOWER('%"
					+ requestWorkFlowVo.getRequestSubTypeName() + "%')");
		}

		if (requestWorkFlowVo.getReqWorkFlowDescription() != null
				&& !requestWorkFlowVo.getReqWorkFlowDescription().isEmpty()) {
			modifiedQuery.append(" and LOWER(wflow.rin_ma_req_workflow_description) LIKE LOWER('%"
					+ requestWorkFlowVo.getReqWorkFlowDescription() + "%')");
		}

		if (requestWorkFlowVo.getStatus() != null) {
			if (requestWorkFlowVo.getStatus().equals(CommonConstant.Active)) {
				modifiedQuery.append(" and wflow.rin_ma_req_workflow_is_active =" + CommonConstant.ACTIVE);
			} else {
				modifiedQuery.append(" and wflow.rin_ma_req_workflow_is_active =" + CommonConstant.CONSTANT_ZERO);
			}
		}

		modifiedQuery.append(" ORDER BY wflow.idrin_ma_req_workflow_id desc");

		List<Object> requestWorkFlowEntityList = (List<Object>) getEntityManager()
				.createNativeQuery(modifiedQuery.toString()).getResultList();

		return requestWorkFlowEntityList;
	}

	public void delete(int id, AuthDetailsVo authDetailsVo) {

		String query = " UPDATE rin_ma_req_workflow w "
				+ " LEFT JOIN rin_ma_req_workflow_details wd ON wd.rin_ma_req_workflow_id = w.idrin_ma_req_workflow_id "
				+ " LEFT JOIN rin_ma_req_workflow_executer we ON we.rin_ma_request_workflow_id =  w.idrin_ma_req_workflow_id "
				+ " LEFT JOIN rin_ma_req_workflow_seq ws ON ws.rin_ma_req_workflow_id = w.idrin_ma_req_workflow_id "
				+ " LEFT JOIN rin_ma_req_workflow_sla s ON s.rin_ma_req_workflow_id = w.idrin_ma_req_workflow_id "
				+ "	SET w.delete_flag = " + CommonConstant.FLAG_ONE + " , wd.delete_flag = " + CommonConstant.FLAG_ONE
				+ " , we.delete_flag = " + CommonConstant.FLAG_ONE + " , ws.delete_flag = " + CommonConstant.FLAG_ONE
				+ " ,s.delete_flag = " + CommonConstant.FLAG_ONE + " ,w.update_by = " + authDetailsVo.getUserId()
				+ " ,w.update_date = '" + CommonConstant.getCurrentDateTimeAsString() + " ' ,wd.update_by = "
				+ authDetailsVo.getUserId() + " ,wd.update_date = '" + CommonConstant.getCurrentDateTimeAsString()
				+ " ',we.update_by = " + authDetailsVo.getUserId() + " ,we.update_date = '"
				+ CommonConstant.getCurrentDateTimeAsString() + " ',ws.update_by = " + authDetailsVo.getUserId()
				+ " ,ws.update_date = '" + CommonConstant.getCurrentDateTimeAsString() + " ',s.update_by = "
				+ authDetailsVo.getUserId() + " ,s.update_date = '" + CommonConstant.getCurrentDateTimeAsString()
				+ "' WHERE w.idrin_ma_req_workflow_id = " + id;

		getEntityManager().createNativeQuery(query).executeUpdate();

	}

	public int saveValidation(RequestWorkFlowVO requestWorkFlowVo, AuthDetailsVo authDetailsVo) {

		StringBuffer query = new StringBuffer();

		String qury = "SELECT COUNT(w.idrin_ma_req_workflow_id) FROM rin_ma_req_workflow w "
				+ " WHERE w.rin_ma_request_type_id = " + requestWorkFlowVo.getRequestTypeId()
				+ " AND w.rin_ma_request_subtype_id = " + requestWorkFlowVo.getRequestSubTypeId()
				+ " AND w.delete_flag = " + CommonConstant.FLAG_ZERO + " AND w.rin_ma_entity_id = "
				+ authDetailsVo.getEntityId();

		BigInteger i = (BigInteger) getEntityManager().createNativeQuery(qury.toString()).getSingleResult();

		int count = i.intValue();

		if (count != 0) {

			for (RequestWorkFlowDetailsVO requestWorkFlowDetailsVo : requestWorkFlowVo
					.getRequestWorkFlowDetailsVoList()) {

				query = new StringBuffer();

				query.append("SELECT COUNT(wd.idrin_ma_req_workflow_details_id) FROM rin_ma_req_workflow_details wd "
						+ " WHERE wd.rin_ma_req_workflow_location_id = "
						+ requestWorkFlowDetailsVo.getWorkFlowLocationId()
						+ " AND wd.rin_ma_req_workflow_sublocation_id = "
						+ requestWorkFlowDetailsVo.getWorkFlowSublocationId()
						+ "	AND wd.rin_ma_req_workflow_department_id = "
						+ requestWorkFlowDetailsVo.getWorkFlowDepartmentId());

				query.append(" AND  wd.rin_ma_req_workflow_details_is_active=" + CommonConstant.CONSTANT_ONE
						+ " AND wd.delete_flag =" + CommonConstant.FLAG_ZERO);

				BigInteger iq = (BigInteger) getEntityManager().createNativeQuery(query.toString()).getSingleResult();

				int countq = iq.intValue();

				if (countq != 0) {
					count = countq;
					return count;
				} else {
					count = 0;
				}

			}
		} else {
			return count;
		}

		return count;

	}

	public RequestWorkFlowSeqEntity findSeqList(int id, AuthDetailsVo authDetailsVo) {

		RequestWorkFlowSeqEntity requestWorkFlowSeqEntity = new RequestWorkFlowSeqEntity();

		String query = " FROM RequestWorkFlowSeqEntity where deleteFlag = " + CommonConstant.FLAG_ZERO
				+ " AND reqWorkFlowId = " + id + " AND reqWorkFlowSeqSequence = " + CommonConstant.CONSTANT_TWO
				+ " AND reqWorkFlowSeqIsActive = " + CommonConstant.CONSTANT_ONE;

		try {
			requestWorkFlowSeqEntity = (RequestWorkFlowSeqEntity) getEntityManager().createQuery(query)
					.getSingleResult();

		} catch (NoResultException e) {
			Log.info("Requst Configuration Dao findSeqList Login NoResultException", e);
		}

		return requestWorkFlowSeqEntity;
	}

	public void modifySequence(int id, AuthDetailsVo authDetailsVo) {

		String query = " UPDATE RequestWorkFlowSeqEntity e" + " SET e.reqWorkFlowSeqIsActive = "
				+ CommonConstant.CONSTANT_ZERO + " , e.updateBy = " + authDetailsVo.getUserId() + " ,e.updateDate = '"
				+ CommonConstant.getCurrentDateTimeAsString() + "' WHERE e.reqWorkFlowSeqId = " + id;

		getEntityManager().createQuery(query).executeUpdate();

	}

	public void modifyDelete(int id, AuthDetailsVo authDetailsVo) {

		String query = " UPDATE rin_ma_req_workflow_executer e" + " LEFT JOIN rin_ma_req_workflow_sla s "
				+ " ON s.idrin_ma_req_workflow_executer_id = e.idrin_ma_req_workflow_executer_id"
				+ " SET e.delete_flag = " + CommonConstant.FLAG_ONE + "  ,s.delete_flag = " + CommonConstant.FLAG_ONE
				+ " ,e.update_by = " + authDetailsVo.getUserId() + " ,e.update_date = '"
				+ CommonConstant.getCurrentDateTimeAsString() + " ',s.update_by = " + authDetailsVo.getUserId()
				+ " ,s.update_date = '" + CommonConstant.getCurrentDateTimeAsString()
				+ "' WHERE e.idrin_ma_req_workflow_executer_id = " + id;

		getEntityManager().createNativeQuery(query).executeUpdate();

	}

	public RequestWorkFlowAuditEntity auditUpdateByDelegateApprover(
			RequestWorkFlowAuditEntity requestWorkFlowAuditEntity, AuthDetailsVo authDetailsVo)
					throws CommonException, Exception {

		RequestWorkFlowAuditEntity delegationUser = null;

		try {
			RequestWorkFlowAuditEntity selectedRequestWorkFlowAuditEntity = getDelegatedApprover(
					requestWorkFlowAuditEntity, authDetailsVo);

			delegationUser = new RequestWorkFlowAuditEntity();
			BeanUtils.copyProperties(requestWorkFlowAuditEntity, delegationUser);
			delegationUser.setUpdateBy(authDetailsVo.getUserId());
			delegationUser.setCreateBy(authDetailsVo.getUserId());
			delegationUser.setCreateDate(CommonConstant.getCalenderDate());
			delegationUser.setUpdateDate(CommonConstant.getCalenderDate());
			delegationUser.setDeleteFlag(CommonConstant.FLAG_ZERO);
			delegationUser.setUserId(authDetailsVo.getUserId());
			delegationUser.setGroupId(selectedRequestWorkFlowAuditEntity.getGroupId());
			delegationUser.setEntityLicenseId(authDetailsVo.getEntityId());
			delegationUser.setRequestWorkflowAuditIsActive(true);
			delegationUser.setRequestWorkFlowAuditId(null);
			delegationUser.setRequestWorkflowAuditSla(selectedRequestWorkFlowAuditEntity.getRequestWorkflowAuditSla());
			delegationUser.setSequence(selectedRequestWorkFlowAuditEntity.getSequence());
			delegationUser.setSeqId(selectedRequestWorkFlowAuditEntity.getSeqId());
			delegationUser.setRequestId(selectedRequestWorkFlowAuditEntity.getRequestId());
			delegationUser.setApprovalExecuter(CommonConstant.CONSTANT_ONE);

			if (requestWorkFlowAuditEntity.getDescisionType() == CommonConstant.CONSTANT_ONE) {
				delegationUser.setRemarks(getMessage("approvedDelegateUser", authDetailsVo) + " "
						+ authDetailsVo.getUserName() + "\n" + requestWorkFlowAuditEntity.getRemarks());
			} else if (requestWorkFlowAuditEntity.getDescisionType() == CommonConstant.CONSTANT_TWO) {
				delegationUser.setRemarks(getMessage("rejectedDelegateUser", authDetailsVo) + " "
						+ authDetailsVo.getUserName() + "\n" + requestWorkFlowAuditEntity.getRemarks());
			} else if (requestWorkFlowAuditEntity.getDescisionType() == CommonConstant.CONSTANT_THREE) {

				delegationUser.setRemarks(getMessage("resubmitDelegateUser", authDetailsVo) + " "
						+ authDetailsVo.getUserName() + "\n" + requestWorkFlowAuditEntity.getRemarks());
			}

			delegationUser.setDescisionType(requestWorkFlowAuditEntity.getDescisionType());
			getEntityManager().persist(delegationUser);

		} catch (CommonException exe) {
			Log.info("Request Configuration Service createApproval Exception", exe);
			throw new CommonException(exe.getMessage());
		} catch (Exception exe) {
			Log.info("Request Configuration Service createApproval Exception", exe);
			throw new CommonException("modifyFailure");
		}
		return delegationUser;
	}

	public RequestWorkFlowAuditEntity getDelegatedApprover(RequestWorkFlowAuditEntity requestWorkFlowAuditEntity,
			AuthDetailsVo authDetailsVo) throws CommonException, Exception {

		RequestWorkFlowAuditEntity selectedRequestWorkFlowAuditEntity = null;

		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			String currentDate = "";
			currentDate = (formatter.format(date));

			String delegatedUserId = " SELECT um.idrin_ma_delegation_userid FROM " + getRtaDatabaseSchema()
					+ ".rin_ma_user_delegation um " + "	 JOIN " + getRtaDatabaseSchema()
					+ ".rin_ma_user_delegation_details ud ON ud.idrin_ma_user_delegation_id = um.idrin_ma_user_delegation_id "
					+ " WHERE ud.rin_ma_entity_id = " + authDetailsVo.getEntityId() + " and ud.delete_flag = "
					+ CommonConstant.FLAG_ZERO + " and ud.idrin_ma_delegated_user_id = " + authDetailsVo.getUserId()
					+ " and ud.idrin_ma_user_delegation_active = '" + CommonConstant.FLAG_ONE
					+ "'and ud.idrin_ma_user_active_from<='" + currentDate + "' AND ud.idrin_ma_user_active_to>='"
					+ currentDate + "' " + " and ud.idrin_ma_user_type ='1' ";

			@SuppressWarnings("unchecked")
			List<Integer> delegatedUserList = (ArrayList<Integer>) getEntityManager().createNativeQuery(delegatedUserId)
					.getResultList();

			String approvalAuditQry = "  FROM RequestWorkFlowAuditEntity WHERE" + "  approvalExecuter = "
					+ CommonConstant.CONSTANT_ONE + " and workFlowId = " + requestWorkFlowAuditEntity.getWorkFlowId()
					+ " and descisionType = 0 and requestId = " + requestWorkFlowAuditEntity.getRequestId()
					+ " and requestWorkflowAuditIsActive = 1";

			@SuppressWarnings("unchecked")
			List<RequestWorkFlowAuditEntity> approvalAuditList = (ArrayList<RequestWorkFlowAuditEntity>) getEntityManager()
					.createQuery(approvalAuditQry).getResultList();

			for (RequestWorkFlowAuditEntity oldApproval : approvalAuditList) {
				selectedRequestWorkFlowAuditEntity = new RequestWorkFlowAuditEntity();
				selectedRequestWorkFlowAuditEntity = oldApproval;
				boolean res = delegatedUserList.contains(oldApproval.getUserId());
				if (res) {
					break;
				}
			}

			if (null != selectedRequestWorkFlowAuditEntity) {

				selectedRequestWorkFlowAuditEntity.setDescisionType(CommonConstant.CONSTANT_NINE);
				selectedRequestWorkFlowAuditEntity.setUpdateBy(authDetailsVo.getUserId());
				selectedRequestWorkFlowAuditEntity.setUpdateDate(CommonConstant.getCalenderDate());
				getEntityManager().persist(selectedRequestWorkFlowAuditEntity);
			} else {
				throw new CommonException("common.noRecord");
			}

		} catch (CommonException exe) {
			Log.info("Request Configuration Service createApproval Exception", exe);
			throw new CommonException(exe.getMessage());
		} catch (Exception exe) {
			Log.info("Request Configuration Service createApproval Exception", exe);
			throw new CommonException("modifyFailure");
		}
		return selectedRequestWorkFlowAuditEntity;
	}

	@SuppressWarnings("null")
	public RequestWorkFlowAuditEntity auditUpdateByDelegateResolver(
			RequestWorkFlowAuditEntity requestWorkFlowAuditEntity, AuthDetailsVo authDetailsVo)
					throws CommonException, Exception {

		RequestWorkFlowAuditEntity delegationUser = null;

		try {
			RequestWorkFlowAuditEntity selectedRequestWorkFlowAuditEntity = getDelegatedResolver(
					requestWorkFlowAuditEntity, authDetailsVo);

			delegationUser = new RequestWorkFlowAuditEntity();
			BeanUtils.copyProperties(requestWorkFlowAuditEntity, delegationUser);
			delegationUser.setUpdateBy(authDetailsVo.getUserId());
			delegationUser.setCreateBy(authDetailsVo.getUserId());
			delegationUser.setCreateDate(CommonConstant.getCalenderDate());
			delegationUser.setUpdateDate(CommonConstant.getCalenderDate());
			delegationUser.setDeleteFlag(CommonConstant.FLAG_ZERO);
			delegationUser.setUserId(authDetailsVo.getUserId());
			delegationUser.setGroupId(selectedRequestWorkFlowAuditEntity.getGroupId());
			delegationUser.setEntityLicenseId(authDetailsVo.getUserId());
			delegationUser.setRequestWorkflowAuditIsActive(true);
			delegationUser.setRequestWorkFlowAuditId(null);
			delegationUser.setRequestWorkflowAuditSla(selectedRequestWorkFlowAuditEntity.getRequestWorkflowAuditSla());
			delegationUser.setSequence(selectedRequestWorkFlowAuditEntity.getSequence());
			delegationUser.setSeqId(selectedRequestWorkFlowAuditEntity.getSeqId());
			delegationUser.setRequestId(requestWorkFlowAuditEntity.getRequestId());
			delegationUser.setApprovalExecuter(selectedRequestWorkFlowAuditEntity.getApprovalExecuter());
			delegationUser.setDescisionType(requestWorkFlowAuditEntity.getDescisionType());

			if (requestWorkFlowAuditEntity.getDescisionType() == CommonConstant.CONSTANT_FOUR) {
				delegationUser.setRemarks(getMessage("resolvedDelegatedUser", authDetailsVo) + " "
						+ authDetailsVo.getUserName() + "\n" + requestWorkFlowAuditEntity.getRemarks());
			} else if (requestWorkFlowAuditEntity.getDescisionType() == CommonConstant.CONSTANT_FIVE) {
				delegationUser.setRemarks(getMessage("inprogressDelegateUser", authDetailsVo) + " "
						+ authDetailsVo.getUserName() + "\n" + requestWorkFlowAuditEntity.getRemarks());
			} else if (requestWorkFlowAuditEntity.getDescisionType() == CommonConstant.CONSTANT_SIX) {

				delegationUser.setRemarks(getMessage("reassignedToDelegatedUser", authDetailsVo) + " "
						+ authDetailsVo.getUserName() + "\n" + requestWorkFlowAuditEntity.getRemarks());
			}

			getEntityManager().persist(delegationUser);
		} catch (CommonException exe) {
			Log.info("Request Configuration Service createApproval Exception", exe);
			throw new CommonException(exe.getMessage());
		} catch (Exception exe) {
			Log.info("Request Configuration Service createApproval Exception", exe);
			throw new CommonException("modifyFailure");
		}
		return delegationUser;
	}

	public RequestWorkFlowAuditEntity createDelegateResolver(RequestWorkFlowAuditEntity requestWorkFlowAuditEntity,
			AuthDetailsVo authDetailsVo, Integer forwardFlag) throws CommonException, Exception {

		RequestWorkFlowAuditEntity delegationUser = new RequestWorkFlowAuditEntity();

		try {
			RequestWorkFlowAuditEntity selectedRequestWorkFlowAuditEntity = getDelegatedResolver(
					requestWorkFlowAuditEntity, authDetailsVo);

			BeanUtils.copyProperties(requestWorkFlowAuditEntity, delegationUser);
			delegationUser.setUpdateBy(authDetailsVo.getUserId());
			delegationUser.setCreateBy(authDetailsVo.getUserId());
			delegationUser.setCreateDate(CommonConstant.getCalenderDate());
			delegationUser.setUpdateDate(CommonConstant.getCalenderDate());
			delegationUser.setDeleteFlag(CommonConstant.FLAG_ZERO);
			delegationUser.setUserId(authDetailsVo.getUserId());
			delegationUser.setGroupId(selectedRequestWorkFlowAuditEntity.getGroupId());
			delegationUser.setEntityLicenseId(authDetailsVo.getEntityId());
			delegationUser.setRequestWorkflowAuditIsActive(true);
			delegationUser.setRequestWorkFlowAuditId(null);
			delegationUser.setRequestId(requestWorkFlowAuditEntity.getRequestId());
			delegationUser.setSeqId(selectedRequestWorkFlowAuditEntity.getSeqId());
			delegationUser.setSequence(selectedRequestWorkFlowAuditEntity.getSequence());
			delegationUser.setApprovalExecuter(selectedRequestWorkFlowAuditEntity.getApprovalExecuter());
			delegationUser.setWorkFlowId(selectedRequestWorkFlowAuditEntity.getWorkFlowId());

			if (forwardFlag.equals(CommonConstant.CONSTANT_ONE)) {
				delegationUser.setDescisionType(CommonConstant.CONSTANT_EIGHT);
			} else {
				delegationUser.setDescisionType(CommonConstant.CONSTANT_FOURTEEN);
			}

			if (null != selectedRequestWorkFlowAuditEntity.getRequestWorkflowAuditSla()) {
				delegationUser
						.setRequestWorkflowAuditSla(selectedRequestWorkFlowAuditEntity.getRequestWorkflowAuditSla());
			}

			if (forwardFlag.equals(CommonConstant.CONSTANT_ONE)) {
				delegationUser.setRemarks(getMessage("forwardByDelegatedUser", authDetailsVo) + " "
						+ authDetailsVo.getUserName() + "," + requestWorkFlowAuditEntity.getRemarks());
			} else {
				delegationUser.setRemarks(getMessage("redirectByDelegatedUser", authDetailsVo) + " "
						+ authDetailsVo.getUserName() + "," + requestWorkFlowAuditEntity.getRemarks());
			}

			if (null != requestWorkFlowAuditEntity.getAuditForwardRemarks()) {
				delegationUser.setAuditForwardRemarks(requestWorkFlowAuditEntity.getAuditForwardRemarks());
			}

			getEntityManager().persist(delegationUser);

		} catch (CommonException exe) {
			Log.info("Request Configuration Service createApproval Exception", exe);
			throw new CommonException(exe.getMessage());
		} catch (Exception exe) {
			Log.info("Request Configuration Service createApproval Exception", exe);
			throw new CommonException("modifyFailure");
		}
		return delegationUser;
	}

	public RequestWorkFlowAuditEntity getDelegatedResolver(RequestWorkFlowAuditEntity requestWorkFlowAuditEntity,
			AuthDetailsVo authDetailsVo) throws CommonException, Exception {

		RequestWorkFlowAuditEntity selectedRequestWorkFlowAuditEntity = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			String currentDate = "";
			currentDate = (formatter.format(date));

			String delegatedUserId = " SELECT um.idrin_ma_delegation_userid FROM " + getRtaDatabaseSchema()
					+ ".rin_ma_user_delegation um " + "	 JOIN " + getRtaDatabaseSchema()
					+ ".rin_ma_user_delegation_details ud ON ud.idrin_ma_user_delegation_id = um.idrin_ma_user_delegation_id "
					+ " WHERE ud.rin_ma_entity_id = " + authDetailsVo.getEntityId() + " and ud.delete_flag = "
					+ CommonConstant.FLAG_ZERO + " and ud.idrin_ma_delegated_user_id = " + authDetailsVo.getUserId()
					+ " and ud.idrin_ma_user_delegation_active = '" + CommonConstant.FLAG_ONE
					+ "'and ud.idrin_ma_user_active_from<='" + currentDate + "' AND ud.idrin_ma_user_active_to>='"
					+ currentDate + "' " + " and ud.idrin_ma_user_type ='2' ";

			@SuppressWarnings("unchecked")
			List<Integer> delegatedUserList = (ArrayList<Integer>) getEntityManager().createNativeQuery(delegatedUserId)
					.getResultList();

			String executorAuditQry = "  FROM RequestWorkFlowAuditEntity WHERE" + "  approvalExecuter = "
					+ CommonConstant.CONSTANT_TWO + " and workFlowId = " + requestWorkFlowAuditEntity.getWorkFlowId()
					+ " and descisionType = 0 and requestId = " + requestWorkFlowAuditEntity.getRequestId()
					+ " and requestWorkflowAuditIsActive = 1";

			@SuppressWarnings("unchecked")
			List<RequestWorkFlowAuditEntity> executorAuditList = (ArrayList<RequestWorkFlowAuditEntity>) getEntityManager()
					.createQuery(executorAuditQry).getResultList();

			for (RequestWorkFlowAuditEntity oldExecutor : executorAuditList) {
				selectedRequestWorkFlowAuditEntity = new RequestWorkFlowAuditEntity();
				selectedRequestWorkFlowAuditEntity = oldExecutor;
				boolean res = delegatedUserList.contains(oldExecutor.getUserId());
				if (res) {
					break;
				}
			}

			if (null != selectedRequestWorkFlowAuditEntity) {

				selectedRequestWorkFlowAuditEntity.setDescisionType(CommonConstant.CONSTANT_NINE);
				selectedRequestWorkFlowAuditEntity.setUpdateBy(authDetailsVo.getUserId());
				selectedRequestWorkFlowAuditEntity.setUpdateDate(CommonConstant.getCalenderDate());
				getEntityManager().persist(selectedRequestWorkFlowAuditEntity);
			} else {
				throw new CommonException(getMessage("common.noRecord", authDetailsVo));
			}
		} catch (CommonException exe) {
			Log.info("Request Configuration Service createApproval Exception", exe);
			throw new CommonException(exe.getMessage());
		} catch (Exception exe) {
			Log.info("Request Configuration Service createApproval Exception", exe);
			throw new CommonException("modifyFailure");
		}
		return selectedRequestWorkFlowAuditEntity;
	}

	@SuppressWarnings("unchecked")
	public void updateDetailInAudit(RequestWorkFlowAuditEntity requestWorkFlowAuditEntity, AuthDetailsVo authDetailsVo)
			throws CommonException, Exception {

		try {
			String remarks = "";

			if (null != requestWorkFlowAuditEntity.getApprovalExecuter() &&
					requestWorkFlowAuditEntity.getApprovalExecuter() == 2) {
			  remarks = getAuditRemarks(requestWorkFlowAuditEntity, authDetailsVo);
			}else{
				remarks = requestWorkFlowAuditEntity.getRemarks() ;
			}
			
			String query = "update RequestWorkFlowAuditEntity  set " + " descisionType = "
					+ requestWorkFlowAuditEntity.getDescisionType() + " , remarks = '" + remarks + "'";

			if (null != requestWorkFlowAuditEntity.getAuditForwardRemarks()) {
				query = query + " ,rin_tr_req_workflow_audit_forward_remarks = '"
						+ requestWorkFlowAuditEntity.getAuditForwardRemarks() + "'";

			}

			if (null != authDetailsVo.getUserId()) {
				query = query + " ,updateBy = " + authDetailsVo.getUserId();
			} else {

				if (requestWorkFlowAuditEntity.getUpdateBy() != null) {
					query = query + " ,updateBy = " + requestWorkFlowAuditEntity.getUpdateBy();
				}
			}

			query = query + " ,updateDate = '" + CommonConstant.getCurrentDateTimeAsString() + "'";

			StringBuffer modifiedQuery = new StringBuffer(query);
			if (requestWorkFlowAuditEntity.getDescisionType() == CommonConstant.CONSTANT_SIX
					&& requestWorkFlowAuditEntity.getApprovalExecuter() == CommonConstant.CONSTANT_TWO) {
				modifiedQuery.append(" , reassignFlag = " + requestWorkFlowAuditEntity.getReassignFlag()
						+ " , reassignUserId = " + requestWorkFlowAuditEntity.getReassignUserId());
			}
			modifiedQuery.append(" where seqId = " + requestWorkFlowAuditEntity.getSeqId() + "  AND  requestId = "
					+ requestWorkFlowAuditEntity.getRequestId() + " AND deleteFlag = " + CommonConstant.FLAG_ZERO
					+ " AND requestWorkflowAuditIsActive = " + CommonConstant.CONSTANT_ONE);

			modifiedQuery
					.append(" and requestWorkFlowAuditId = " + requestWorkFlowAuditEntity.getRequestWorkFlowAuditId());

			getEntityManager().createQuery(modifiedQuery.toString()).executeUpdate();

		} catch (CommonException e) {
			Log.info("Requst Configuration Dao updateDetailInAudit CommonException", e);
			throw new CommonException("dataFailure");
		}

	}

	public boolean getExecutorCount(RequestWorkFlowAuditEntity requestWorkFlowAuditEntity) {
		boolean result = false;
		String executorAuditQry = "  FROM RequestWorkFlowAuditEntity WHERE" + "  approvalExecuter = "
				+ CommonConstant.CONSTANT_TWO + " and workFlowId = " + requestWorkFlowAuditEntity.getWorkFlowId()
				+ " and requestId = " + requestWorkFlowAuditEntity.getRequestId() + " and sequence = "
				+ requestWorkFlowAuditEntity.getSequence() + " and requestWorkflowAuditIsActive = 1";

		@SuppressWarnings("unchecked")
		List<RequestWorkFlowAuditEntity> executorUserList = (ArrayList<RequestWorkFlowAuditEntity>) getEntityManager()
				.createQuery(executorAuditQry).getResultList();

		if (executorUserList.size() > 1) {
			result = true;
		}

		return result;
	}

	public String getAuditRemarks(RequestWorkFlowAuditEntity requestWorkFlowAuditEntity, AuthDetailsVo authDetailsVo) {

		boolean res = false;
		String remarks = "";
		if (null != requestWorkFlowAuditEntity.getApprovalExecuter() &&
				requestWorkFlowAuditEntity.getApprovalExecuter() == 2 
				&& (requestWorkFlowAuditEntity.getDescisionType() == CommonConstant.CONSTANT_FOUR ||
				requestWorkFlowAuditEntity.getDescisionType() == CommonConstant.CONSTANT_SIX)) {
			res = getExecutorCount(requestWorkFlowAuditEntity);
		

		//if (res) {

			if (requestWorkFlowAuditEntity.getDescisionType() == CommonConstant.CONSTANT_FOUR && res) {
				remarks = getMessage("resolvedDelegatedUser", authDetailsVo) + " " + authDetailsVo.getUserName() + "\n"
						+ requestWorkFlowAuditEntity.getRemarks();
			} else if (requestWorkFlowAuditEntity.getDescisionType() == CommonConstant.CONSTANT_FIVE) {
				remarks = getMessage("inprogressDelegateUser", authDetailsVo) + " " + authDetailsVo.getUserName() + "\n"
						+ requestWorkFlowAuditEntity.getRemarks();
			} else if (requestWorkFlowAuditEntity.getDescisionType() == CommonConstant.CONSTANT_SIX && res) {

				remarks = getMessage("reassignedToDelegatedUser", authDetailsVo) + " " + authDetailsVo.getUserName()
						+ "\n" + requestWorkFlowAuditEntity.getRemarks();
			}else{
				remarks = requestWorkFlowAuditEntity.getRemarks();
			}
		//}  
		} 
		return remarks;

	}

	/**
	 * Method is used to get the list of Subordinates.
	 * 
	 * @param currentLoginUserId
	 * @return list List<UserMappingEntity>
	 */
	@SuppressWarnings("unchecked")
	public List<UserMappingEntity> getListOfSubordinates(int currentLoginUserId, AuthDetailsVo authDetailsVo) {

		/*
		 * String query =
		 * "FROM UserMappingEntity se where se.deleteFlag ='0' and se.userEntity.id = "
		 * + currentLoginUserId; List<UserMappingEntity> list =
		 * (List<UserMappingEntity>)
		 * getEntityManager().createQuery(query).getResultList();
		 */

		/*
		 * String query = "SELECT * FROM "+getCommonDatabaseSchema()+
		 * ".user_mapping se where se.delete_flag =" + CommonConstant.FLAG_ZERO
		 * + " and se.USER_ID = " + currentLoginUserId;
		 */

		String query = "SELECT um.USER_MAPPING_ID, um.LEVEL, um.REPORTING_TO, um.USER_DEPARTMENT_ID, um.USER_LOCATION_ID,"
				+ " um.USER_SUBLOCATION_ID, um.REPORTING_TO_LOCATION, um.REPORTING_TO_SUBLOCATION, um.REPORTING_DEPARTMENT,"// 8
				+ " um.USER_ROLE_ID, um.rin_ma_entity_id, um.USER_ID,cs.ITEM_VALUE "// 12
				+ " FROM " + getCommonDatabaseSchema() + ".user_mapping um " + " LEFT JOIN " + getCommonDatabaseSchema()
				+ ".`common_storage` cs ON um.LEVEL = cs.COMMON_ID " + " WHERE um.rin_ma_entity_id = '"
				+ authDetailsVo.getEntityId() + "'" + " AND um.USER_ID = " + currentLoginUserId
				+ " AND um.delete_flag = " + CommonConstant.FLAG_ZERO + " and cs.rin_ma_entity_id = "
				+ authDetailsVo.getEntityId();

		List<Object[]> userMappingEntity = (List<Object[]>) getEntityManager().createNativeQuery(query).getResultList();
		List<UserMappingEntity> list = new ArrayList<>();
		UserMappingEntity userMappingEntity2 = null;
		for (Object[] object : userMappingEntity) {
			userMappingEntity2 = new UserMappingEntity();
			if (null != (Integer) (((Object[]) object)[0])) {
				userMappingEntity2.setUserMappingId((Integer) (((Object[]) object)[0]));
			}
			if (null != (Integer) (((Object[]) object)[1]) && null != (String) (((Object[]) object)[12])) {
				CommonStorageEntity commonStorageEntity = new CommonStorageEntity();
				commonStorageEntity.setCommonId((Integer) (((Object[]) object)[1]));
				commonStorageEntity.setItemValue((String) (((Object[]) object)[12]));
				userMappingEntity2.setLevel(commonStorageEntity);
			}
			if (null != (Integer) (((Object[]) object)[2])) {
				UserEntity userEntity = new UserEntity();
				userEntity.setId((Integer) (((Object[]) object)[2]));
				userMappingEntity2.setReportingToUser(userEntity);
			}

			list.add(userMappingEntity2);
		}
		return list;
	}

	/**
	 * This method is used to get the user details list.
	 * 
	 * @param requestWorkFlowEntity
	 *            RequestWorkFlowEntity
	 * @return userEntity List<UserEntity>
	 */
	@SuppressWarnings("unchecked")
	public List<UserEntity> findExecuterUserId(RequestWorkFlowExecuterVO requestWorkFlowExecuter,
			AuthDetailsVo authDetailsVo) {

		String query = "SELECT USER_ID,USER_ROLE_ID,USER_DEPARTMENT_ID,USER_SUBLOCATION_ID,USER_LOCATION_ID "
				+ "  FROM " + getCommonDatabaseSchema() + ".user";
		if (null != authDetailsVo.getEntityId()) {
			query = query + " WHERE rin_ma_entity_id = '" + authDetailsVo.getEntityId() + "' ";
		} else {
			query = query + " WHERE rin_ma_entity_id = '" + requestWorkFlowExecuter.getEntityId() + "' ";
		}
		query = query + " AND delete_flag = '" + CommonConstant.FLAG_ZERO + " '" + " AND USER_ROLE_ID = "
				+ requestWorkFlowExecuter.getExecuterRoleId();

		List<Object[]> object = (List<Object[]>) getEntityManager().createNativeQuery(query).getResultList();

		List<UserEntity> list = new ArrayList<UserEntity>();
		UserEntity userEntity = null;
		for (Object[] obj : object) {
			userEntity = new UserEntity();
			if (null != obj[0]) {
				userEntity.setId((Integer) obj[0]);
			}
			list.add(userEntity);
		}

		return list;
	}

	/**
	 * Method is used to update the current status
	 * 
	 * @param requestId
	 * @param currentStatusId
	 */
	public void updateCurrentStatus(int requestId, int currentStatusId, AuthDetailsVo authDetailsVo)
			throws CommonException, Exception {
		try {
			String query = "update RequestEntity  set currentStatusId = " + currentStatusId + " where requestId = "
					+ requestId;

			getEntityManager().createQuery(query).executeUpdate();
		} catch (Exception e) {
			throw new CommonException("dataFailure");
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
	public int findStatusId(String status, AuthDetailsVo authDetailsVo) {

		String query = "SELECT currentStatusId from CurrentStatusEntity where currentStatusCode = '" + status + "'";

		int id = (int) getEntityManager().createQuery(query).getSingleResult();

		return id;

	}

	@SuppressWarnings("unchecked")
	public UserMappingVO getDepartmentLevelUser(int userId, AuthDetailsVo authDetailsVo) {

		/* List<UserMappingEntity> userMappingEntity = new ArrayList<>(); */

		List<UserMappingVO> userMappingEntity = new ArrayList<>();

		/*
		 * String query = "SELECT um FROM UserMappingEntity um " +
		 * " ,CommonStorageEntity cs where um.level.commonId = cs.commonId" +
		 * " and  um.entityLicenseEntity.id = '" + authDetailsVo.getEntityId() +
		 * "'" + " AND cs.itemValue = 'LEVEL2' AND um.reportingToUser.id = " +
		 * userId + " GROUP BY  um.reportingToUser.id";
		 */

		String query = " SELECT um.USER_MAPPING_ID, um.LEVEL FROM " + getCommonDatabaseSchema() + ".user_mapping um "
				+ " LEFT JOIN " + getCommonDatabaseSchema() + ".common_storage cs ON um.LEVEL = cs.COMMON_ID"
				+ " and  um.rin_ma_entity_id = '" + authDetailsVo.getEntityId() + "'"
				+ " AND cs.ITEM_VALUE = 'LEVEL2' AND um.REPORTING_TO = " + userId + " GROUP BY  um.REPORTING_TO ";

		try {
			/*
			 * userMappingEntity = (List<UserMappingEntity>)
			 * getEntityManager().createNativeQuery(query,UserMappingEntity.
			 * class).getResultList();
			 */
			List<Object[]> list = (List<Object[]>) getEntityManager().createNativeQuery(query).getResultList();
			if (!list.isEmpty()) {
				for (Object[] object : list) {
					UserMappingVO userMappingEntity1 = new UserMappingVO();
					userMappingEntity1.setLevelId((Integer) (((Object[]) object)[1]));
					userMappingEntity.add(userMappingEntity1);
				}
			}

		} catch (NoResultException e) {
			Log.info("Requst Configuration Dao getDepartmentLevelUser NoResultException", e);
		}
		if (userMappingEntity != null && userMappingEntity.size() > 0) {
			return userMappingEntity.get(0);
		} else {
			return null;
		}

	}

	/**
	 * This method is used to get the user details list.
	 * 
	 * @param requestWorkFlowSequenceVO
	 *            RequestWorkFlowSequenceVo
	 * @return userEntity List<UserEntity>
	 */
	@SuppressWarnings("unchecked")
	public List<UserEntity> findSeqUserId(RequestWorkFlowSequenceVO requestWorkFlowSequenceVo,
			AuthDetailsVo authDetailsVo) {

		String query = " SELECT USER_ID, USER_LOGIN_ID, USER_EMPLOYEE_ID, FIRST_NAME, MIDDLE_NAME, LAST_NAME , "
				+ " USER_SUBLOCATION_ID, USER_LOCATION_ID, USER_ROLE_ID ,  USER_DEPARTMENT_ID , rin_ma_entity_id"
				+ "	FROM " + getCommonDatabaseSchema() + ".user " + " WHERE rin_ma_entity_id = '"
				+ authDetailsVo.getEntityId() + "' " + " AND delete_flag = '" + CommonConstant.FLAG_ZERO + " '";

		if (null != requestWorkFlowSequenceVo.getLocationId() && null != requestWorkFlowSequenceVo.getSublocationId()
				&& null != requestWorkFlowSequenceVo.getUserDepartmentId()
				&& null != requestWorkFlowSequenceVo.getUserRoleId()) {

			query = query + " AND USER_LOCATION_ID = " + requestWorkFlowSequenceVo.getLocationId()
					+ " AND USER_SUBLOCATION_ID = " + requestWorkFlowSequenceVo.getSublocationId()
					+ " AND USER_DEPARTMENT_ID = " + requestWorkFlowSequenceVo.getUserDepartmentId()
					+ " AND USER_ROLE_ID =" + requestWorkFlowSequenceVo.getUserRoleId();

		} else if (null != requestWorkFlowSequenceVo.getLocationId()
				&& null != requestWorkFlowSequenceVo.getSublocationId()
				&& null != requestWorkFlowSequenceVo.getUserRoleId()) {

			query = query + " AND USER_LOCATION_ID = " + requestWorkFlowSequenceVo.getLocationId()
					+ " AND USER_SUBLOCATION_ID = " + requestWorkFlowSequenceVo.getSublocationId()
					+ " AND USER_ROLE_ID =" + requestWorkFlowSequenceVo.getUserRoleId();

		} else if (null != requestWorkFlowSequenceVo.getLocationId()
				&& null != requestWorkFlowSequenceVo.getUserRoleId()) {

			query = query + " AND USER_LOCATION_ID = " + requestWorkFlowSequenceVo.getLocationId()
					+ " AND USER_ROLE_ID =" + requestWorkFlowSequenceVo.getUserRoleId();

		} else {
			query = query + " AND USER_ROLE_ID = " + requestWorkFlowSequenceVo.getUserRoleId();
		}

		List<Object[]> list = (List<Object[]>) getEntityManager().createNativeQuery(query).getResultList();

		List<UserEntity> userEntitiyList = new ArrayList<UserEntity>();

		UserEntity userEntity = null;
		for (Object[] obj : list) {
			userEntity = new UserEntity();

			if (null != obj[0]) {
				userEntity.setId((Integer) obj[0]);
			}
			if (null != obj[1]) {
				userEntity.setUserEmployeeId((String) obj[1]);
			}
			if (null != obj[2]) {
				userEntity.setFirstName((String) obj[2]);
			}
			if (null != obj[3]) {
				userEntity.setMiddleName((String) obj[3]);
			}
			if (null != obj[4]) {
				userEntity.setLastName((String) obj[4]);
			}
			if (null != obj[5]) {
				userEntity.setCurrentAddress((String) obj[5]);
			}
			if (null != obj[6]) {
				SubLocation subLocationEntity = new SubLocation();
				subLocationEntity.setId((Integer) obj[6]);
				userEntity.setSubLocationEntity(subLocationEntity);
			}
			if (null != obj[7]) {
				UserLocation userLocation = new UserLocation();
				userLocation.setId((Integer) obj[7]);
				userEntity.setUserLocationEntity(userLocation);
			}
			if (null != obj[8]) {
				UserRole userRoleEntity = new UserRole();
				userRoleEntity.setId((Integer) obj[8]);
				userEntity.setUserRoleEntity(userRoleEntity);
			}
			if (null != obj[9]) {
				UserDepartment userDepartmentEntity = new UserDepartment();
				userDepartmentEntity.setId((Integer) obj[9]);
				userEntity.setUserDepartmentEntity(userDepartmentEntity);
			}
			if (null != obj[10]) {
				EntityLicense entityLicenseEntity = new EntityLicense();
				entityLicenseEntity.setId((Integer) obj[10]);
				userEntity.setEntityLicenseEntity(entityLicenseEntity);
			}
			userEntitiyList.add(userEntity);
		}

		return userEntitiyList;
	}

	/**
	 * This method is used to get the user mapping list.
	 * 
	 * @param userEntity
	 *            UserEntity
	 * @return userMappingEntity List<UserMappingEntity>
	 */
	@SuppressWarnings("unchecked")
	public List<UserMappingVO> basedOnUserDetails(int userId, AuthDetailsVo authDetailsVo)
			throws CommonException, Exception {

		List<UserMappingVO> list = new ArrayList<>();
		try {
			String query = "SELECT USER_MAPPING_ID, LEVEL, REPORTING_TO, USER_DEPARTMENT_ID, USER_LOCATION_ID,"
					+ " USER_SUBLOCATION_ID, REPORTING_TO_LOCATION, REPORTING_TO_SUBLOCATION, REPORTING_DEPARTMENT,"
					+ " USER_ROLE_ID, rin_ma_entity_id, USER_ID " + " FROM " + getCommonDatabaseSchema()
					+ ".user_mapping " + " WHERE rin_ma_entity_id = '" + authDetailsVo.getEntityId() + "'"
					+ " AND USER_ID = " + userId + " AND delete_flag = " + CommonConstant.FLAG_ZERO;

			List<Object[]> userMappingEntity = (List<Object[]>) getEntityManager().createNativeQuery(query)
					.getResultList();

			UserMappingVO userMappingVO = null;
			for (Object[] object : userMappingEntity) {
				userMappingVO = new UserMappingVO();
				if (null != (Integer) (((Object[]) object)[2])) {
					userMappingVO.setReportingToUser((Integer) (((Object[]) object)[2]));
				}
				list.add(userMappingVO);
			}
		} catch (Exception exe) {
			exe.printStackTrace();
			Log.info("Requst Configuration Dao basedOnUserDetails Exception", exe);
			throw new CommonException("dataFailure");
		}
		return list;
	}

	/**
	 * This method is used to get the list of object based on workflow id and
	 * prototype for executer.
	 * 
	 * @param id
	 *            int
	 * @param sla
	 *            int
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> findWorkFlowExeSla(int id, int sla, AuthDetailsVo authDetailsVo) {

		String query = "select distinct(exe), sla.reqWorkFlowSlaType, sla.reqWorkFlowSla , sla.reqWeekendFlag , sla.reqHolidayFlag "
				+ " FROM RequestWorkFlowExecuterEntity exe "
				+ " ,RequestWorkFlowSlaEntity sla where exe.reqWorkFlowId = sla.reqWorkFlowId "
				+ " and exe.deleteFlag = " + CommonConstant.FLAG_ZERO + " and sla.deleteFlag = "
				+ CommonConstant.FLAG_ZERO + " AND exe.reqWorkFlowId = " + id + " and sla.protoType = " + sla
				+ " and sla.reqWorkFlowSeqId = " + CommonConstant.CONSTANT_ZERO
				+ " GROUP BY exe.reqWorkFlowExecuterId ";

		List<Object[]> requestWorkFlowexeEntity = (List<Object[]>) getEntityManager().createQuery(query)
				.getResultList();
		if (null != requestWorkFlowexeEntity && requestWorkFlowexeEntity.size() != CommonConstant.CONSTANT_ZERO) {

			return requestWorkFlowexeEntity;
		} else {
			return null;
		}

	}

	/*
	 * @SuppressWarnings("unchecked") public List<Object[]>
	 * findRedirectWorkFlowExeSla(int id, int sla,RequestVO requestVo) throws
	 * CommonException , Exception {
	 * 
	 * List<Object[]> requestWorkFlowexeEntity = null; try{ String query =
	 * "select distinct(exe), sla.reqWorkFlowSlaType, sla.reqWorkFlowSla " +
	 * " FROM RequestWorkFlowExecuterEntity exe " +
	 * " ,RequestWorkFlowSlaEntity sla where exe.reqWorkFlowId = sla.reqWorkFlowId "
	 * + " and exe.deleteFlag = " + CommonConstant.FLAG_ZERO +
	 * " and sla.deleteFlag = " + CommonConstant.FLAG_ZERO +
	 * " AND exe.reqWorkFlowId = " + id + " and sla.protoType = " + sla +
	 * " and sla.reqWorkFlowSeqId = " + CommonConstant.CONSTANT_ZERO +
	 * " and exe.executerLocationId = "+ requestVo.getId() +
	 * " and exe.executerSublocationId = "+ requestVo.getSublocationId() ;
	 * 
	 * requestWorkFlowexeEntity = (List<Object[]>)
	 * getEntityManager().createQuery(query).getResultList(); if (null !=
	 * requestWorkFlowexeEntity && requestWorkFlowexeEntity.size() !=
	 * CommonConstant.CONSTANT_ZERO) { requestWorkFlowexeEntity =
	 * requestWorkFlowexeEntity;
	 * 
	 * } else { return null; }
	 * 
	 * }catch(Exception e){ throw new CommonException("dataFailure"); } return
	 * requestWorkFlowexeEntity; }
	 */

	/**
	 * This method is used to get the list of object based on workflow id and
	 * prototype for approval.
	 * 
	 * @param id
	 *            int
	 * @param sla
	 *            int
	 * @return requestWorkFlowSeqEntity List<Object[]>
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> findWorkFlowSeqSla(int id, int sla, AuthDetailsVo authDetailsVo) {
		String query = "select seq, sla.reqWorkFlowSlaType, sla.reqWorkFlowSla , sla.reqWeekendFlag , sla.reqHolidayFlag FROM RequestWorkFlowSeqEntity seq "
				+ " ,RequestWorkFlowSlaEntity sla Where seq.reqWorkFlowSeqId = sla.reqWorkFlowSeqId"
				+ " and seq.reqWorkFlowId = sla.reqWorkFlowId " + " and seq.deleteFlag = " + CommonConstant.FLAG_ZERO
				+ " and sla.deleteFlag = " + CommonConstant.FLAG_ZERO + " AND seq.reqWorkFlowSeqIsActive = "
				+ CommonConstant.CONSTANT_ONE + " AND seq.reqWorkFlowId = " + id + " and sla.protoType = " + sla
				+ " ORDER BY seq.reqWorkFlowSeqSequence ";

		List<Object[]> requestWorkFlowSeqEntity = (List<Object[]>) getEntityManager().createQuery(query)
				.getResultList();
		if (null != requestWorkFlowSeqEntity && requestWorkFlowSeqEntity.size() != CommonConstant.CONSTANT_ZERO) {

			return requestWorkFlowSeqEntity;
		} else {
			return null;
		}

	}

	/**
	 * This method is used to find the sla based on request.
	 * 
	 * @param requestEntity
	 * @return object List<Object[]>
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> findbyRequestSla(RequestEntity requestEntity, AuthDetailsVo authDetailsVo) {

		List<Object[]> object = new ArrayList<>();

		String query = "select rwf, sla.reqWorkFlowSlaType,sla.reqWorkFlowSla " + " FROM RequestWorkFlowEntity rwf "
				+ " ,RequestWorkFlowDetailsEntity rd,RequestWorkFlowSlaEntity sla Where rd.reqWorkFlowId = rwf.reqWorkFlowId"
				+ "  and rwf.reqWorkFlowId = sla.reqWorkFlowId " + " and rwf.deleteFlag = " + CommonConstant.FLAG_ZERO
				+ " AND rd.deleteFlag = " + CommonConstant.FLAG_ZERO + " AND rwf.requestTypeId ="
				+ requestEntity.getRequestTypeId() + " AND rwf.requestSubTypeId =" + requestEntity.getRequestSubtypeId()
				+ " AND rd.workFlowDepartmentId =" + requestEntity.getDepartmentId() + " AND rd.workFlowLocationId = "
				+ requestEntity.getReqLocationId() + " AND rd.workFlowSublocationId = "
				+ requestEntity.getReqSublocationId() + " and sla.deleteFlag = " + CommonConstant.FLAG_ZERO
				+ " and sla.reqWorkFlowSeqId = " + CommonConstant.CONSTANT_ZERO + " and sla.protoType = "
				+ requestEntity.getRequestPriority() + " and rd.reqWorkFlowDetailsIsActive ="
				+ CommonConstant.CONSTANT_ONE;

		object = (List<Object[]>) getEntityManager().createQuery(query).getResultList();

		List<Object[]> objet = new ArrayList<>();

		query = "select rwf, sla.reqWorkFlowSlaType,sla.reqWorkFlowSla " + " FROM RequestWorkFlowEntity rwf "
				+ " ,RequestWorkFlowDetailsEntity rd,RequestWorkFlowSlaEntity sla Where rd.reqWorkFlowId = rwf.reqWorkFlowId"
				+ "  and rwf.reqWorkFlowId = sla.reqWorkFlowId " + " and rwf.deleteFlag = " + CommonConstant.FLAG_ZERO
				+ " AND rd.deleteFlag = " + CommonConstant.FLAG_ZERO + " AND rwf.requestTypeId ="
				+ requestEntity.getRequestTypeId() + " AND rwf.requestSubTypeId =" + requestEntity.getRequestSubtypeId()
				+ " AND rd.workFlowDepartmentId =" + CommonConstant.CONSTANT_ZERO + " AND rd.workFlowLocationId = "
				+ requestEntity.getReqLocationId() + " AND rd.workFlowSublocationId = "
				+ requestEntity.getReqSublocationId() + " and sla.deleteFlag = " + CommonConstant.FLAG_ZERO
				+ " and sla.reqWorkFlowSeqId = " + CommonConstant.CONSTANT_ZERO + " and sla.protoType = "
				+ requestEntity.getRequestPriority() + " and rd.reqWorkFlowDetailsIsActive ="
				+ CommonConstant.CONSTANT_ONE;

		objet = (List<Object[]>) getEntityManager().createQuery(query).getResultList();

		if (objet != null && !objet.isEmpty()) {
			if (objet.size() != 0) {
				object.addAll(objet);
			}
		}

		List<Object[]> obje = new ArrayList<>();

		query = "select rwf, sla.reqWorkFlowSlaType,sla.reqWorkFlowSla " + " FROM RequestWorkFlowEntity rwf "
				+ " ,RequestWorkFlowDetailsEntity rd,RequestWorkFlowSlaEntity sla Where rd.reqWorkFlowId = rwf.reqWorkFlowId"
				+ "  and rwf.reqWorkFlowId = sla.reqWorkFlowId " + " and rwf.deleteFlag = " + CommonConstant.FLAG_ZERO
				+ " AND rd.deleteFlag = " + CommonConstant.FLAG_ZERO + " AND rwf.requestTypeId ="
				+ requestEntity.getRequestTypeId() + " AND rwf.requestSubTypeId =" + requestEntity.getRequestSubtypeId()
				+ " AND rd.workFlowDepartmentId =" + CommonConstant.CONSTANT_ZERO + " AND rd.workFlowLocationId = "
				+ requestEntity.getReqLocationId() + " AND rd.workFlowSublocationId = " + CommonConstant.CONSTANT_ZERO
				+ " and sla.deleteFlag = " + CommonConstant.FLAG_ZERO + " and sla.reqWorkFlowSeqId = "
				+ CommonConstant.CONSTANT_ZERO + " and sla.protoType = " + requestEntity.getRequestPriority()
				+ " and rd.reqWorkFlowDetailsIsActive =" + CommonConstant.CONSTANT_ONE;

		obje = (List<Object[]>) getEntityManager().createQuery(query).getResultList();

		if (obje != null && !obje.isEmpty()) {
			if (obje.size() != 0) {
				object.addAll(obje);
			}
		}

		List<Object[]> ob = new ArrayList<>();

		query = "select rwf, sla.reqWorkFlowSlaType,sla.reqWorkFlowSla " + " FROM RequestWorkFlowEntity rwf "
				+ " ,RequestWorkFlowDetailsEntity rd,RequestWorkFlowSlaEntity sla Where rd.reqWorkFlowId = rwf.reqWorkFlowId"
				+ "  and rwf.reqWorkFlowId = sla.reqWorkFlowId " + " and rwf.deleteFlag = " + CommonConstant.FLAG_ZERO
				+ " AND rd.deleteFlag = " + CommonConstant.FLAG_ZERO + " AND rwf.requestTypeId ="
				+ requestEntity.getRequestTypeId() + " AND rwf.requestSubTypeId =" + requestEntity.getRequestSubtypeId()
				+ " AND rd.workFlowDepartmentId =" + CommonConstant.CONSTANT_ZERO + " AND rd.workFlowLocationId = "
				+ CommonConstant.CONSTANT_ZERO + " AND rd.workFlowSublocationId = " + CommonConstant.CONSTANT_ZERO
				+ " and sla.deleteFlag = " + CommonConstant.FLAG_ZERO + " and sla.reqWorkFlowSeqId = "
				+ CommonConstant.CONSTANT_ZERO + " and sla.protoType = " + requestEntity.getRequestPriority()
				+ " and rd.reqWorkFlowDetailsIsActive =" + CommonConstant.CONSTANT_ONE;

		ob = (List<Object[]>) getEntityManager().createQuery(query).getResultList();

		if (ob != null && !ob.isEmpty()) {
			if (ob.size() != 0) {
				object.addAll(ob);
			}
		}
		List<Object[]> obj1 = new ArrayList<>();

		query = "select rwf, sla.reqWorkFlowSlaType,sla.reqWorkFlowSla " + " FROM RequestWorkFlowEntity rwf "
				+ " ,RequestWorkFlowDetailsEntity rd,RequestWorkFlowSlaEntity sla Where rd.reqWorkFlowId = rwf.reqWorkFlowId"
				+ "  and rwf.reqWorkFlowId = sla.reqWorkFlowId " + " and rwf.deleteFlag = " + CommonConstant.FLAG_ZERO
				+ " AND rd.deleteFlag = " + CommonConstant.FLAG_ZERO + " AND rwf.requestTypeId ="
				+ requestEntity.getRequestTypeId() + " AND rwf.requestSubTypeId =" + requestEntity.getRequestSubtypeId()
				+ " AND rd.workFlowDepartmentId =" + requestEntity.getDepartmentId() + " AND rd.workFlowLocationId = "
				+ CommonConstant.CONSTANT_ZERO + " AND rd.workFlowSublocationId = " + CommonConstant.CONSTANT_ZERO
				+ " and sla.deleteFlag = " + CommonConstant.FLAG_ZERO + " and sla.reqWorkFlowSeqId = "
				+ CommonConstant.CONSTANT_ZERO + " and sla.protoType = " + requestEntity.getRequestPriority()
				+ " and rd.reqWorkFlowDetailsIsActive =" + CommonConstant.CONSTANT_ONE;

		obj1 = (List<Object[]>) getEntityManager().createQuery(query).getResultList();

		if (obj1 != null && !obj1.isEmpty()) {
			if (obj1.size() != 0) {
				object.addAll(obj1);
			}
		}
		List<Object[]> obj2 = new ArrayList<>();

		query = "select rwf, sla.reqWorkFlowSlaType,sla.reqWorkFlowSla " + " FROM RequestWorkFlowEntity rwf "
				+ " ,RequestWorkFlowDetailsEntity rd,RequestWorkFlowSlaEntity sla Where rd.reqWorkFlowId = rwf.reqWorkFlowId"
				+ "  and rwf.reqWorkFlowId = sla.reqWorkFlowId " + " and rwf.deleteFlag = " + CommonConstant.FLAG_ZERO
				+ " AND rd.deleteFlag = " + CommonConstant.FLAG_ZERO + " AND rwf.requestTypeId ="
				+ requestEntity.getRequestTypeId() + " AND rwf.requestSubTypeId =" + requestEntity.getRequestSubtypeId()
				+ " AND rd.workFlowDepartmentId =" + requestEntity.getDepartmentId() + " AND rd.workFlowLocationId = "
				+ CommonConstant.CONSTANT_ZERO + " AND rd.workFlowSublocationId = "
				+ requestEntity.getReqSublocationId() + " and sla.deleteFlag = " + CommonConstant.FLAG_ZERO
				+ " and sla.reqWorkFlowSeqId = " + CommonConstant.CONSTANT_ZERO + " and sla.protoType = "
				+ requestEntity.getRequestPriority() + " and rd.reqWorkFlowDetailsIsActive ="
				+ CommonConstant.CONSTANT_ONE;

		obj2 = (List<Object[]>) getEntityManager().createQuery(query).getResultList();

		if (obj2 != null && !obj2.isEmpty()) {
			if (obj2.size() != 0) {
				object.addAll(obj2);
			}
		}

		List<Object[]> obj3 = new ArrayList<>();

		query = "select rwf, sla.reqWorkFlowSlaType,sla.reqWorkFlowSla " + " FROM RequestWorkFlowEntity rwf "
				+ " ,RequestWorkFlowDetailsEntity rd,RequestWorkFlowSlaEntity sla Where rd.reqWorkFlowId = rwf.reqWorkFlowId"
				+ "  and rwf.reqWorkFlowId = sla.reqWorkFlowId " + " and rwf.deleteFlag = " + CommonConstant.FLAG_ZERO
				+ " AND rd.deleteFlag = " + CommonConstant.FLAG_ZERO + " AND rwf.requestTypeId ="
				+ requestEntity.getRequestTypeId() + " AND rwf.requestSubTypeId =" + requestEntity.getRequestSubtypeId()
				+ " AND rd.workFlowDepartmentId =" + CommonConstant.CONSTANT_ZERO + " AND rd.workFlowLocationId = "
				+ CommonConstant.CONSTANT_ZERO + " AND rd.workFlowSublocationId = "
				+ requestEntity.getReqSublocationId() + " and sla.deleteFlag = " + CommonConstant.FLAG_ZERO
				+ " and sla.reqWorkFlowSeqId = " + CommonConstant.CONSTANT_ZERO + " and sla.protoType = "
				+ requestEntity.getRequestPriority() + " and rd.reqWorkFlowDetailsIsActive ="
				+ CommonConstant.CONSTANT_ONE;

		obj3 = (List<Object[]>) getEntityManager().createQuery(query).getResultList();

		if (obj3 != null && !obj3.isEmpty()) {
			if (obj3.size() != 0) {
				object.addAll(obj3);
			}
		}

		List<Object[]> obj4 = new ArrayList<Object[]>();

		query = "select rwf, sla.reqWorkFlowSlaType,sla.reqWorkFlowSla FROM RequestWorkFlowEntity rwf "
				+ " ,RequestWorkFlowDetailsEntity rd,RequestWorkFlowSlaEntity sla Where rd.reqWorkFlowId = rwf.reqWorkFlowId"
				+ "  and rwf.reqWorkFlowId = sla.reqWorkFlowId " + " and rwf.deleteFlag = " + CommonConstant.FLAG_ZERO
				+ " AND rd.deleteFlag = " + CommonConstant.CONSTANT_ZERO + " AND rwf.requestTypeId ="
				+ requestEntity.getRequestTypeId() + " AND rwf.requestSubTypeId =" + requestEntity.getRequestSubtypeId()
				+ " AND rd.workFlowDepartmentId =" + requestEntity.getDepartmentId() + " AND rd.workFlowLocationId = "
				+ requestEntity.getReqLocationId() + " AND rd.workFlowSublocationId = " + CommonConstant.CONSTANT_ZERO
				+ " and sla.deleteFlag = " + CommonConstant.CONSTANT_ZERO + " and sla.reqWorkFlowSeqId = "
				+ CommonConstant.CONSTANT_ZERO + " and sla.protoType = " + requestEntity.getRequestPriority()
				+ " and rd.reqWorkFlowDetailsIsActive =" + CommonConstant.CONSTANT_ONE;

		obj4 = (List<Object[]>) getEntityManager().createQuery(query).getResultList();

		if (obj4.size() != 0) {
			object.addAll(obj4);
		}
		Set<Object[]> obj = new LinkedHashSet<Object[]>();

		obj.addAll(object);
		object.clear();

		object.addAll(obj);

		// new add:
		List<Object[]> obj5 = new ArrayList<>();

		query = "select rwf, sla.reqWorkFlowSlaType,sla.reqWorkFlowSla " + " FROM RequestWorkFlowEntity rwf "
				+ " ,RequestWorkFlowDetailsEntity rd,RequestWorkFlowSlaEntity sla Where rd.reqWorkFlowId = rwf.reqWorkFlowId"
				+ "  and rwf.reqWorkFlowId = sla.reqWorkFlowId " + " and rwf.deleteFlag = " + CommonConstant.FLAG_ZERO
				+ " AND rd.deleteFlag = " + CommonConstant.FLAG_ZERO + " AND rwf.requestTypeId ="
				+ requestEntity.getRequestTypeId() + " AND rwf.requestSubTypeId =" + requestEntity.getRequestSubtypeId()
				+ " AND rd.workFlowDepartmentId =" + requestEntity.getDepartmentId() + " AND rd.workFlowLocationId = "
				+ requestEntity.getId() + " AND rd.workFlowSublocationId = " + requestEntity.getSublocationId()
				+ " and sla.deleteFlag = " + CommonConstant.FLAG_ZERO + " and sla.reqWorkFlowSeqId = "
				+ CommonConstant.CONSTANT_ZERO + " and sla.protoType = " + requestEntity.getRequestPriority()
				+ " and rd.reqWorkFlowDetailsIsActive =" + CommonConstant.CONSTANT_ONE;

		obj5 = (List<Object[]>) getEntityManager().createQuery(query).getResultList();

		if (obj5 != null && !obj5.isEmpty()) {
			if (obj5.size() != 0) {
				object.addAll(obj5);
			}
		}

		return object;
	}

	/**
	 * This method used to update the group decision in requestWorkFlowAudit.
	 * 
	 * @param requestWorkFlowAuditEntityList
	 *            RequestWorkFlowAuditEntity
	 * @param decision
	 *            int
	 */
	public void updateAppDecision(RequestWorkFlowAuditEntity requestWorkFlowAuditEntity, int decision,
			AuthDetailsVo authDetailsVo) throws CommonException {

		try {

			String query = "update RequestWorkFlowAuditEntity  set descisionType = " + decision + " "
					+ " ,updateDate = ' " + CommonConstant.getCurrentDateTimeAsString() + "'";
			if (null != authDetailsVo.getUserId()) {
				query = query + " ,updateBy = " + authDetailsVo.getUserId();
			} else {
				query = query + " ,updateBy = " + requestWorkFlowAuditEntity.getUpdateBy();
			}

			query = query + " where seqId = " + requestWorkFlowAuditEntity.getSeqId() + "  AND  requestId = "
					+ requestWorkFlowAuditEntity.getRequestId() + "  AND  groupId != " + CommonConstant.CONSTANT_ZERO
					+ " AND requestWorkFlowAuditId != " + requestWorkFlowAuditEntity.getRequestWorkFlowAuditId()
					+ " AND approvalExecuter NOT IN (2,3) " + " AND deleteFlag = " + CommonConstant.FLAG_ZERO
					+ " AND requestWorkflowAuditIsActive = " + CommonConstant.CONSTANT_ONE;

			getEntityManager().createQuery(query).executeUpdate();

		} catch (Exception exe) {
			Log.info("Request Configuration Service createApproval Exception", exe);
			throw new CommonException("modifyFailure");
		}

	}

	public void updateGrpAppDecision(RequestWorkFlowAuditEntity requestWorkFlowAuditEntity, int decision,
			AuthDetailsVo authDetailsVo) throws CommonException {

		try {

			String query = "update RequestWorkFlowAuditEntity  set descisionType = " + decision + " "
					+ " ,updateDate = ' " + CommonConstant.getCurrentDateTimeAsString() + "'";
			if (null != authDetailsVo.getUserId()) {
				query = query + " ,updateBy = " + authDetailsVo.getUserId();
			} else {
				query = query + " ,updateBy = " + requestWorkFlowAuditEntity.getUpdateBy();
			}

			query = query + " where seqId = " + requestWorkFlowAuditEntity.getSeqId() + "  AND  requestId = "
					+ requestWorkFlowAuditEntity.getRequestId() + "  AND  groupId != " + CommonConstant.CONSTANT_ZERO
					+ " AND requestWorkFlowAuditId != " + requestWorkFlowAuditEntity.getRequestWorkFlowAuditId()
					+ " AND approvalExecuter NOT IN (2,3) " + " AND deleteFlag = " + CommonConstant.FLAG_ZERO
					+ " AND requestWorkflowAuditIsActive = " + CommonConstant.CONSTANT_ONE;

			getEntityManager().createQuery(query).executeUpdate();

		} catch (Exception exe) {
			Log.info("Request Configuration Service createApproval Exception", exe);
			throw new CommonException("modifyFailure");
		}

	}

	/**
	 * This method is used to get the approval count in requestWorkFlowAudit.
	 * 
	 * @param requestWorkFlowAuditEntity
	 *            RequestWorkFlowAuditEntity
	 * @return workFlowAusditEntity RequestWorkFlowAuditEntity
	 */
	public int approvalCount(RequestWorkFlowAuditEntity requestWorkFlowAuditEntity, AuthDetailsVo authDetailsVo)
			throws CommonException {
		int count = 0;
		try {
			String query = " SELECT COUNT(*) FROM  RequestWorkFlowAuditEntity " + "WHERE requestId = "
					+ requestWorkFlowAuditEntity.getRequestId() + " AND approvalExecuter = "
					+ requestWorkFlowAuditEntity.getApprovalExecuter() + " AND descisionType NOT IN (1,9)"
					+ " and deleteFlag = " + CommonConstant.FLAG_ZERO;

			Long countL = (Long) getEntityManager().createQuery(query).getSingleResult();
			count = countL.intValue();

		} catch (Exception exe) {
			Log.info("Request Configuration Service createApproval Exception", exe);
			throw new CommonException("dataFailure");
		}

		return count;
	}

	/**
	 * This method is used to update the currentStatus in request.
	 * 
	 * @param currentStatusCode
	 *            String
	 * @param requestWorkFlowAuditEntity
	 *            RequestWorkFlowAuditEntity
	 */
	public void updateCurrentStatusInRequest(String currentStatusCode,
			RequestWorkFlowAuditEntity requestWorkFlowAuditEntity, AuthDetailsVo authDetailsVo) throws CommonException {
		try {

			int currentStatusId = findStatusId(currentStatusCode, authDetailsVo);

			String query = "update RequestEntity  set currentStatusId = " + currentStatusId + " ,updateDate = ' "
					+ CommonConstant.getCurrentDateTimeAsString() + " ' ";

			if (null != authDetailsVo.getUserId()) {
				query = query + " ,updateBy = " + authDetailsVo.getUserId();
			} else {
				query = query + " ,updateBy = " + requestWorkFlowAuditEntity.getUpdateBy();
			}

			query = query + " where requestId = " + requestWorkFlowAuditEntity.getRequestId();
			getEntityManager().createQuery(query).executeUpdate();

		} catch (Exception exe) {
			Log.info("Request Configuration Service createApproval Exception", exe);
			throw new CommonException("modifyFailure");
		}

	}

	/**
	 * This method is used to store the all remark in remark entity
	 * 
	 * @param requestWorkFlowAuditEntity
	 *            RequestWorkFlowAuditEntity
	 */
	public void getAllRemarksList(RequestWorkFlowAuditEntity requestWorkFlowAuditEntity, AuthDetailsVo authDetailsVo)
			throws CommonException {
		RemarksEntity remarksEntity = new RemarksEntity();

		try {
			if (null != requestWorkFlowAuditEntity.getRequestId()) {
				remarksEntity.setRequestId(requestWorkFlowAuditEntity.getRequestId());
			}
			if (null != requestWorkFlowAuditEntity.getUserId()) {
				remarksEntity.setUserId(requestWorkFlowAuditEntity.getUserId());
			}
			if (null != requestWorkFlowAuditEntity.getRemarks()) {
				remarksEntity.setRemarks(requestWorkFlowAuditEntity.getRemarks());
			}

			if (null != authDetailsVo.getUserId()) {
				remarksEntity.setCreateBy(authDetailsVo.getUserId());
				remarksEntity.setUpdateBy(authDetailsVo.getUserId());
			} else {
				remarksEntity.setCreateBy(requestWorkFlowAuditEntity.getCreateBy());
				remarksEntity.setUpdateBy(requestWorkFlowAuditEntity.getUpdateBy());
			}

			remarksEntity.setCreateDate(CommonConstant.getCalenderDate());

			remarksEntity.setUpdateDate(CommonConstant.getCalenderDate());

			// remarksRepository.save(remarksEntity);

			getEntityManager().persist(remarksEntity);

		} catch (Exception exe) {
			Log.info("Request Configuration Service createApproval Exception", exe);
			throw new CommonException("dataFailure");
		}

	}

	/**
	 * Method is used for fin the approval list
	 * 
	 * @param requestId
	 */
	@SuppressWarnings("unchecked")
	public void findApprovalSequence(RequestEntity requestEntity, AuthDetailsVo authDetailsVo) throws CommonException {

		try {
			String query = " FROM RequestWorkFlowAuditEntity WHERE entityLicenseId = "
					+ requestEntity.getEntityLicenseId() + " and deleteFlag = " + CommonConstant.FLAG_ZERO
					+ " AND requestId = " + requestEntity.getRequestId() + " AND descisionType = "
					+ CommonConstant.CONSTANT_ZERO + " AND approvalExecuter != " + CommonConstant.CONSTANT_THREE;

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
				String updateQuery = " update RequestEntity set requestSeq = '" + sequence.substring(0)
						+ "', updateBy = " + requestEntity.getCreateBy() + ", updateDate = '"
						+ CommonConstant.getCurrentDateTimeAsString() + "'" + " where deleteFlag = "
						+ CommonConstant.FLAG_ZERO + " and requestId = " + requestEntity.getRequestId();

				getEntityManager().createQuery(updateQuery).executeUpdate();
			}

		} catch (Exception exe) {
			Log.info("Request Configuration Service createApproval Exception", exe);
			throw new CommonException("modifyFailure");
		}
	}

	/**
	 * This method is used to update the date for repopulate method.
	 * 
	 * @param id
	 *            int
	 */
	public void updateDate(int id, AuthDetailsVo authDetailsVo) {

		String query = "UPDATE rin_tr_request set update_date ='" + CommonConstant.getCurrentDateTimeAsString()
				+ "' WHERE idrin_tr_request_id = " + id;

		getEntityManager().createNativeQuery(query).executeUpdate();

	}

	/**
	 * This method used to update the group decision in requestWorkFlowAudit.
	 * 
	 * @param requestWorkFlowAuditEntityList
	 *            RequestWorkFlowAuditEntity
	 * @param decision
	 *            int
	 */
	@SuppressWarnings("unchecked")
	public void updateDecision(RequestWorkFlowAuditEntity requestWorkFlowAuditEntity, int decision,
			AuthDetailsVo authDetailsVo) {

		String query = "update RequestWorkFlowAuditEntity  set descisionType = " + decision + " ,updateDate = ' "
				+ CommonConstant.getCurrentDateTimeAsString() + "'";

		if (null != authDetailsVo.getUserId()) {
			query = query + " ,updateBy = " + authDetailsVo.getUserId();
		} else {
			query = query + " ,updateBy = " + requestWorkFlowAuditEntity.getUpdateBy();
		}

		query = query + " where requestId = " + requestWorkFlowAuditEntity.getRequestId() + " AND descisionType = 0 "
				+ " AND requestWorkFlowAuditId != " + requestWorkFlowAuditEntity.getRequestWorkFlowAuditId()
				+ " AND approvalExecuter NOT IN (3) " + " AND deleteFlag = " + CommonConstant.FLAG_ZERO
				+ " AND requestWorkflowAuditIsActive = " + CommonConstant.CONSTANT_ONE;

		getEntityManager().createQuery(query).executeUpdate();

		UserMasterVO userVo = getUserDetail(requestWorkFlowAuditEntity, authDetailsVo);

		String rej = getMessage("rejectedBy", authDetailsVo);

		if (requestWorkFlowAuditEntity.getDescisionType() == 2) {

			query = "update RequestWorkFlowAuditEntity  set descisionType = " + decision + ",remarks = '"
					+ rej.concat(" " + userVo.getFirstName() + " " + userVo.getLastName()) + "',updateBy = "
					+ authDetailsVo.getUserId() + " ,updateDate = ' " + CommonConstant.getCurrentDateTimeAsString()
					+ "'";

			if (null != authDetailsVo.getUserId()) {
				query = query + " ,updateBy = " + authDetailsVo.getUserId();
			} else {
				query = query + " ,updateBy = " + requestWorkFlowAuditEntity.getUpdateBy();
			}

			query = query + " where requestId = " + requestWorkFlowAuditEntity.getRequestId()
					+ " AND descisionType = 0 " + " AND requestWorkFlowAuditId != "
					+ requestWorkFlowAuditEntity.getRequestWorkFlowAuditId() + " AND approvalExecuter = 3"
					+ " AND deleteFlag = " + CommonConstant.FLAG_ZERO + " AND requestWorkflowAuditIsActive = "
					+ CommonConstant.CONSTANT_ONE;

			getEntityManager().createQuery(query).executeUpdate();
		}

		if (requestWorkFlowAuditEntity.getDescisionType() == 3) {

			query = "update RequestWorkFlowAuditEntity  set descisionType = " + decision + ",remarks = '"
					+ getMessage("resubmit", authDetailsVo) + "' " + ",updateDate = ' "
					+ CommonConstant.getCurrentDateTimeAsString() + "'";

			if (null != authDetailsVo.getUserId()) {
				query = query + " ,updateBy = " + authDetailsVo.getUserId();
			} else {
				query = query + " ,updateBy = " + requestWorkFlowAuditEntity.getUpdateBy();
			}

			query = query + " where requestId = " + requestWorkFlowAuditEntity.getRequestId()
					+ " AND descisionType = 0 " + " AND requestWorkFlowAuditId != "
					+ requestWorkFlowAuditEntity.getRequestWorkFlowAuditId() + " AND approvalExecuter = 3"
					+ " AND deleteFlag = " + CommonConstant.FLAG_ZERO + " AND requestWorkflowAuditIsActive = "
					+ CommonConstant.CONSTANT_ONE;

			getEntityManager().createQuery(query).executeUpdate();
		}
	}

	public UserMasterVO getUserDetail(RequestWorkFlowAuditEntity requestWorkFlowAuditEntity,
			AuthDetailsVo authDetailsVo) {

		UserMasterVO userVo = new UserMasterVO();
		String qury = "";
		if (null != requestWorkFlowAuditEntity.getReassignUserId()
				&& requestWorkFlowAuditEntity.getReassignUserId() != 0) {

			qury = " select u.FIRST_NAME,u.LAST_NAME from " + getCommonDatabaseSchema() + ".user u WHERE  u.USER_ID = "
					+ authDetailsVo.getUserId();

		} else if (null != requestWorkFlowAuditEntity.getUserId() && requestWorkFlowAuditEntity.getUserId() != 0) {

			qury = " select u.FIRST_NAME,u.LAST_NAME from " + getCommonDatabaseSchema() + ".user u WHERE  u.USER_ID = "
					+ authDetailsVo.getUserId();
		}
		@SuppressWarnings("unchecked")
		List<Object[]> user = (List<Object[]>) getEntityManager().createNativeQuery(qury).getResultList();

		for (Object[] obj : user) {
			if (null != String.valueOf(obj[0])) {
				userVo.setFirstName(String.valueOf(obj[0]));
			}
			if (null != String.valueOf(obj[1])) {
				userVo.setLastName(String.valueOf(obj[1]));
			}
		}

		return userVo;
	}

	/**
	 * This method used to update the group decision in requestWorkFlowAudit.
	 * 
	 * @param requestWorkFlowAuditEntityList
	 *            RequestWorkFlowAuditEntity
	 * @param decision
	 *            int
	 */
	public void updateGroupDecision(RequestWorkFlowAuditEntity requestWorkFlowAuditEntity, int decision,
			AuthDetailsVo authDetailsVo) {

		String query = "update RequestWorkFlowAuditEntity  set descisionType = " + decision + " ,updateDate = ' "
				+ CommonConstant.getCurrentDateTimeAsString() + "'";

		if (null != authDetailsVo.getUserId()) {
			query = query + " ,updateBy = " + authDetailsVo.getUserId();
		} else {
			query = query + " ,updateBy = " + requestWorkFlowAuditEntity.getUpdateBy();
		}

		query = query + " where seqId = " + requestWorkFlowAuditEntity.getSeqId() + "  AND  requestId = "
				+ requestWorkFlowAuditEntity.getRequestId() + " AND requestWorkFlowAuditId != "
				+ requestWorkFlowAuditEntity.getRequestWorkFlowAuditId() + " AND approvalExecuter NOT IN (1,3) "
				+ " AND deleteFlag = " + CommonConstant.FLAG_ZERO + " AND requestWorkflowAuditIsActive = "
				+ CommonConstant.CONSTANT_ONE;

		getEntityManager().createQuery(query).executeUpdate();
	}

	/**
	 * This method used to update the group decision in requestWorkFlowAudit.
	 * 
	 * @param requestWorkFlowAuditEntityList
	 *            RequestWorkFlowAuditEntity
	 * @param decision
	 *            int
	 */
	/*
	 * public void updateGroupDecisionExecutor(RequestWorkFlowAuditEntity
	 * requestWorkFlowAuditEntity, int decision,AuthDetailsVo authDetailsVo
	 * ,Integer delegatedId) {
	 * 
	 * String query = "update RequestWorkFlowAuditEntity  set descisionType = "
	 * + decision + " ,updateDate = ' "+
	 * CommonConstant.getCurrentDateTimeAsString() + "'";
	 * 
	 * if (null != authDetailsVo.getUserId()) { query = query + " ,updateBy = "
	 * + authDetailsVo.getUserId(); } else { query = query + " ,updateBy = " +
	 * requestWorkFlowAuditEntity.getUpdateBy(); }
	 * 
	 * query = query + " where seqId = " + requestWorkFlowAuditEntity.getSeqId()
	 * + "  AND  requestId = " + requestWorkFlowAuditEntity.getRequestId() +
	 * " AND requestWorkFlowAuditId != " +
	 * requestWorkFlowAuditEntity.getRequestWorkFlowAuditId() +
	 * " AND approvalExecuter NOT IN (1,3) " + " AND deleteFlag = " +
	 * CommonConstant.FLAG_ZERO + " AND requestWorkflowAuditIsActive = " +
	 * CommonConstant.CONSTANT_ONE;
	 * 
	 * if(null != delegatedId){ query = query + " and userId NOT IN ( " +
	 * delegatedId+")"; } getEntityManager().createQuery(query).executeUpdate();
	 * 
	 * }
	 */
	/**
	 * This method used to update the group decision in requestWorkFlowAudit.
	 * 
	 * @param requestWorkFlowAuditEntityList
	 *            RequestWorkFlowAuditEntity
	 * @param decision
	 *            int
	 */
	public void updateGrpDecision(RequestWorkFlowAuditEntity requestWorkFlowAuditEntity, int decision,
			AuthDetailsVo authDetailsVo) {

		try {
			/*
			 * String query =
			 * "update RequestWorkFlowAuditEntity  set descisionType = " +
			 * decision + " ,updateDate = ' " +
			 * CommonConstant.getCurrentDateTimeAsString() + "'";
			 * 
			 * if (null != authDetailsVo.getUserId()) { query = query +
			 * " ,updateBy = " + authDetailsVo.getUserId(); } else { query =
			 * query + " ,updateBy = " +
			 * requestWorkFlowAuditEntity.getUpdateBy(); }
			 * 
			 * query = query + " where seqId = " +
			 * requestWorkFlowAuditEntity.getSeqId() + "  AND  requestId = " +
			 * requestWorkFlowAuditEntity.getRequestId() +
			 * " AND approvalExecuter NOT IN (1,3) " + " AND deleteFlag = " +
			 * CommonConstant.FLAG_ZERO + " AND requestWorkflowAuditIsActive = "
			 * + CommonConstant.CONSTANT_ONE;
			 */

			String query = "update RequestWorkFlowAuditEntity  set descisionType = " + decision + " ,updateDate = ' "
					+ CommonConstant.getCurrentDateTimeAsString() + "'";

			if (null != authDetailsVo.getUserId()) {
				query = query + " ,updateBy = " + authDetailsVo.getUserId();
			} else {
				query = query + " ,updateBy = " + requestWorkFlowAuditEntity.getUpdateBy();
			}

			query = query + " where requestWorkFlowAuditId = " + requestWorkFlowAuditEntity.getRequestWorkFlowAuditId()
					+ "  AND  requestId = " + requestWorkFlowAuditEntity.getRequestId()
					+ " AND approvalExecuter NOT IN (1,3) " + " AND deleteFlag = " + CommonConstant.FLAG_ZERO
					+ " AND requestWorkflowAuditIsActive = " + CommonConstant.CONSTANT_ONE;
			getEntityManager().createQuery(query).executeUpdate();

			UserMasterVO userMasterVO = getUserDetail(requestWorkFlowAuditEntity, authDetailsVo);
			UserMasterVO auditUserVO = getUserDetail(requestWorkFlowAuditEntity, authDetailsVo);

			String re = getMessage("reassignTo", authDetailsVo);

			query = "update RequestWorkFlowAuditEntity  set descisionType = " + decision + " ,remarks = '"
					+ auditUserVO.getFirstName()
							.concat(" " + auditUserVO.getLastName() + " "
									+ re.concat(userMasterVO.getFirstName() + " " + userMasterVO.getLastName()))
					+ "',updateBy = " + authDetailsVo.getUserId() + " ,updateDate = ' "
					+ CommonConstant.getCurrentDateTimeAsString() + "'" + " where  " + " requestId = "
					+ requestWorkFlowAuditEntity.getRequestId() + " AND approvalExecuter = 3 " + " AND deleteFlag = "
					+ CommonConstant.FLAG_ZERO + " AND requestWorkflowAuditIsActive = " + CommonConstant.CONSTANT_ONE;

			getEntityManager().createQuery(query).executeUpdate();
		} catch (Exception exe) {
			Log.info("Requst Configuration Dao updateGrpDecision Exception", exe);
			throw new CommonException("dataFailure");
		}
	}

	/**
	 * This method is used to get the approval count in requestWorkFlowAudit.
	 * 
	 * @param requestWorkFlowAuditEntity
	 *            RequestWorkFlowAuditEntity
	 * @return workFlowAusditEntity RequestWorkFlowAuditEntity
	 */
	@SuppressWarnings("unchecked")
	public List<RequestWorkFlowAuditEntity> getApprovalCount(RequestWorkFlowAuditEntity requestWorkFlowAuditEntity,
			AuthDetailsVo authDetailsVo) throws CommonException {
		List<RequestWorkFlowAuditEntity> resultList = new ArrayList<RequestWorkFlowAuditEntity>();
		try {
			String query = " FROM  RequestWorkFlowAuditEntity " + "WHERE requestId = "
					+ requestWorkFlowAuditEntity.getRequestId() + " AND approvalExecuter = "
					+ requestWorkFlowAuditEntity.getApprovalExecuter()
					// + " AND userId = " + authDetailsVo.getUserId()
					+ " AND descisionType = " + CommonConstant.CONSTANT_ONE + " and deleteFlag = "
					+ CommonConstant.FLAG_ZERO;

			resultList = (ArrayList<RequestWorkFlowAuditEntity>) getEntityManager().createQuery(query).getResultList();

		} catch (Exception exe) {
			Log.info("Request Configuration Service createApproval Exception", exe);
			throw new CommonException("dataFailure");
		}
		return resultList;
	}

/*	@SuppressWarnings("unchecked")
	public RequestWorkFlowAuditEntity getLastApproval(RequestWorkFlowAuditEntity requestWorkFlowAuditEntity,
			AuthDetailsVo authDetailsVo) throws CommonException {
		List<RequestWorkFlowAuditEntity> resultList = new ArrayList<RequestWorkFlowAuditEntity>();
		RequestWorkFlowAuditEntity reqWorkFlowAuditEntity = new RequestWorkFlowAuditEntity();
		try {
			String query = " FROM  RequestWorkFlowAuditEntity " + "WHERE requestId = "
					+ requestWorkFlowAuditEntity.getRequestId() + " AND approvalExecuter = "
					+ requestWorkFlowAuditEntity.getApprovalExecuter() + " AND descisionType = "
					+ CommonConstant.CONSTANT_ONE + " and deleteFlag = " + CommonConstant.FLAG_ZERO
					+ " order by  sequence desc";

			resultList = (ArrayList<RequestWorkFlowAuditEntity>) getEntityManager().createQuery(query).getResultList();

			if (resultList.size() > 0) {
				reqWorkFlowAuditEntity = resultList.get(0);
			}

			// get last sequence
			String qry = " FROM  RequestWorkFlowAuditEntity " + "WHERE requestId = "
					+ requestWorkFlowAuditEntity.getRequestId() + " AND approvalExecuter = "
					+ requestWorkFlowAuditEntity.getApprovalExecuter() + " AND seqId = "
					+ reqWorkFlowAuditEntity.getSeqId() + " and deleteFlag = " + CommonConstant.FLAG_ZERO
					+ " order by  sequence desc";

			resultList = (ArrayList<RequestWorkFlowAuditEntity>) getEntityManager().createQuery(qry).getResultList();

			if (resultList.size() > 0) {
				reqWorkFlowAuditEntity = resultList.get(0);
			}

		} catch (Exception exe) {
			Log.info("Request Configuration Service createApproval Exception", exe);
			throw new CommonException("dataFailure");
		}
		return reqWorkFlowAuditEntity;
	}*/

	public int getCount(RequestWorkFlowAuditEntity requestWorkFlowAuditEntity) throws CommonException {
		int count = 0;
		try {
			String query = " SELECT COUNT(*) FROM  RequestWorkFlowAuditEntity " + "WHERE requestId = "
					+ requestWorkFlowAuditEntity.getRequestId() + " AND approvalExecuter = "
					+ requestWorkFlowAuditEntity.getApprovalExecuter() + " AND userId  = "
					+ requestWorkFlowAuditEntity.getUserId() + " AND descisionType = 1" + " and deleteFlag = "
					+ CommonConstant.FLAG_ZERO;

			Long countL = (Long) getEntityManager().createQuery(query).getSingleResult();
			count = countL.intValue();

		} catch (Exception exe) {
			Log.info("Request Configuration Service createApproval Exception", exe);
			throw new CommonException("dataFailure");
		}

		return count;
	}

	@SuppressWarnings("unchecked")
	public List<RequestWorkFlowAuditEntity> getAllAudit(RequestWorkFlowAuditEntity requestWorkFlowAuditEntity,
			AuthDetailsVo authDetailsVo) throws CommonException {
		List<RequestWorkFlowAuditEntity> resultList = new ArrayList<RequestWorkFlowAuditEntity>();
		try {
			String query = "     FROM  RequestWorkFlowAuditEntity " + "WHERE requestId = "
					+ requestWorkFlowAuditEntity.getRequestId() + " AND approvalExecuter = "
					+ requestWorkFlowAuditEntity.getApprovalExecuter() + " and deleteFlag = " + CommonConstant.FLAG_ZERO
					+ " and descisionType =" + 0 + " order by requestWorkFlowAuditId ";

			resultList = (ArrayList<RequestWorkFlowAuditEntity>) getEntityManager().createQuery(query).getResultList();

		} catch (Exception exe) {
			Log.info("Request Configuration Service createApproval Exception", exe);
			throw new CommonException("dataFailure");
		}
		return resultList;
	}

	@SuppressWarnings("unchecked")
	public List<RequestWorkFlowAuditEntity> getSelectedAudit(RequestWorkFlowAuditEntity requestWorkFlowAuditEntity,
			AuthDetailsVo authDetailsVo) throws CommonException {
		List<RequestWorkFlowAuditEntity> resultList = new ArrayList<RequestWorkFlowAuditEntity>();
		try {
			String query = "  FROM  RequestWorkFlowAuditEntity " + "WHERE requestId = "
					+ requestWorkFlowAuditEntity.getRequestId() + " AND approvalExecuter = "
					+ requestWorkFlowAuditEntity.getApprovalExecuter() + " and seqId = "
					+ requestWorkFlowAuditEntity.getSeqId() + " and deleteFlag = " + CommonConstant.FLAG_ZERO;

			resultList = (ArrayList<RequestWorkFlowAuditEntity>) getEntityManager().createQuery(query).getResultList();

		} catch (Exception exe) {
			Log.info("Request Configuration Service createApproval Exception", exe);
			throw new CommonException("dataFailure");
		}
		return resultList;
	}

/*	@SuppressWarnings("unchecked")
	public List<Object[]> getApprovalToApprove(RequestWorkFlowAuditEntity requestWorkFlowAuditEntity,
			AuthDetailsVo authDetailsVo, int sequence) throws CommonException {
		List<Object[]> resultList = new ArrayList<Object[]>();
		try {
			RequestWorkFlowAuditEntity result = new RequestWorkFlowAuditEntity();

			String query = " FROM  RequestWorkFlowAuditEntity " + " WHERE requestId = "
					+ requestWorkFlowAuditEntity.getRequestId() + " AND approvalExecuter = "
					+ requestWorkFlowAuditEntity.getApprovalExecuter() + " AND descisionType = "
					+ CommonConstant.CONSTANT_ZERO + " and deleteFlag = " + CommonConstant.FLAG_ZERO
					+ " and sequence = " + sequence;
			result = (RequestWorkFlowAuditEntity) getEntityManager().createQuery(query).getSingleResult();

			String qry = " select  requestWorkFlowAuditId,userId ,sequence  FROM  RequestWorkFlowAuditEntity "
					+ " WHERE requestId = " + requestWorkFlowAuditEntity.getRequestId() + " AND approvalExecuter = "
					+ requestWorkFlowAuditEntity.getApprovalExecuter() + " AND descisionType = "
					+ CommonConstant.CONSTANT_ZERO + " and deleteFlag = " + CommonConstant.FLAG_ZERO + " and seqId = "
					+ result.getSeqId();

			resultList = (List<Object[]>) getEntityManager().createQuery(qry).getResultList();

		} catch (Exception exe) {
			Log.info("Request Configuration Service createApproval Exception", exe);
			throw new CommonException("dataFailure");
		}
		return resultList;
	}*/

	public RequestWorkFlowAuditEntity getAuditById(int auditId) throws CommonException {
		RequestWorkFlowAuditEntity result = new RequestWorkFlowAuditEntity();
		try {
			String query = " FROM  RequestWorkFlowAuditEntity " + " WHERE requestWorkFlowAuditId = " + auditId
					+ " and deleteFlag = " + CommonConstant.FLAG_ZERO;

			result = (RequestWorkFlowAuditEntity) getEntityManager().createQuery(query).getSingleResult();

		} catch (Exception exe) {
			Log.info("Request Configuration Service createApproval Exception", exe);
			throw new CommonException("dataFailure");
		}
		return result;
	}

	public Integer getUserId(int requestId, int auditId) throws CommonException {
		Integer userId = 0;
		try {
			String query = " SELECT userId FROM  RequestWorkFlowAuditEntity " + "WHERE requestId = " + requestId
					+ " AND approvalExecuter = " + 1 + " AND requestWorkFlowAuditId  = " + auditId
					+ " and deleteFlag = " + CommonConstant.FLAG_ZERO;

			userId = (Integer) getEntityManager().createQuery(query).getSingleResult();

		} catch (Exception exe) {
			Log.info("Request Configuration Service createApproval Exception", exe);
			throw new CommonException("dataFailure");
		}
		return userId;
	}

	/**
	 * Method is used to delete the request
	 * 
	 * @param requestWorkFlowAuditEntity
	 *            RequestWorkFlowAuditEntity
	 */
	public void deleteRequester(RequestWorkFlowAuditEntity requestWorkFlowAuditEntity, AuthDetailsVo authDetailsVo) {

		String query = "UPDATE RequestWorkFlowAuditEntity SET requestWorkflowAuditIsActive = false WHERE requestId = "
				+ requestWorkFlowAuditEntity.getRequestId() + "  AND approvalExecuter IN("
				+ CommonConstant.CONSTANT_THREE + "," + CommonConstant.CONSTANT_TWO + ")";

		getEntityManager().createQuery(query).executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<Object> getAll(Integer entityId, AuthDetailsVo authDetailsVo) {
		String query = " SELECT wflow.rin_ma_request_workflow_code,"
				+ " wflow.rin_ma_req_workflow_description,reqtype.rin_ma_request_type_name,subtype.rin_ma_request_subtype_name,"
				+ " wflow.idrin_ma_req_workflow_id,wflow.rin_ma_req_workflow_is_active " + " FROM "
				+ getRtaDatabaseSchema() + ".rin_ma_req_workflow wflow " + " LEFT JOIN " + getRtaDatabaseSchema()
				+ ".rin_ma_request_type reqtype "
				+ " ON wflow.rin_ma_request_type_id = reqtype.idrin_ma_request_type_id " + " LEFT JOIN "
				+ getRtaDatabaseSchema() + ".rin_ma_request_subtype subtype "
				+ " ON wflow.rin_ma_request_subtype_id = subtype.idrin_ma_request_subtype_id "
				+ " where wflow.delete_flag  = '0' " + " and wflow.rin_ma_entity_id = " + entityId
				+ " order by wflow.create_date desc ";

		List<Object> list = getEntityManager().createNativeQuery(query).getResultList();
		return list;
	}

	public void updateNWDecision(RequestWorkFlowAuditEntity requestWorkFlowAuditEntity, int decision,
			AuthDetailsVo authDetailsVo) throws CommonException, Exception {

		try {
			String query = "update RequestWorkFlowAuditEntity  set descisionType = " + decision + " ,updateDate = ' "
					+ CommonConstant.getCurrentDateTimeAsString() + "'";

			if (null != authDetailsVo.getUserId()) {
				query = query + " ,updateBy = " + authDetailsVo.getUserId();
			} else {
				query = query + " ,updateBy = " + requestWorkFlowAuditEntity.getUpdateBy();
			}

			query = query + " where seqId = " + requestWorkFlowAuditEntity.getSeqId() + "  AND  requestId = "
					+ requestWorkFlowAuditEntity.getRequestId() + " AND approvalExecuter NOT IN (1,3) "
					+ " AND deleteFlag = " + CommonConstant.FLAG_ZERO + " AND requestWorkflowAuditIsActive = "
					+ CommonConstant.CONSTANT_ONE + " AND descisionType = " + CommonConstant.CONSTANT_ZERO;

			getEntityManager().createQuery(query).executeUpdate();
		} catch (Exception exe) {
			Log.info("Requst Configuration Dao updateGrpDecision Exception", exe);
			throw new CommonException("dataFailure");
		}
	}

	public void updateForwardReqDecision(RequestWorkFlowAuditEntity requestWorkFlowAuditEntity, int decision,
			AuthDetailsVo authDetailsVo) throws CommonException, Exception {

		try {
			String query = "update RequestWorkFlowAuditEntity  set descisionType = " + decision + " ,updateDate = ' "
					+ CommonConstant.getCurrentDateTimeAsString() + "'";

			if (null != authDetailsVo.getUserId()) {
				query = query + " ,updateBy = " + authDetailsVo.getUserId();
			} else {
				query = query + " ,updateBy = " + requestWorkFlowAuditEntity.getUpdateBy();
			}

			query = query + " where seqId = " + requestWorkFlowAuditEntity.getSeqId() + "  AND  requestId = "
					+ requestWorkFlowAuditEntity.getRequestId() + " AND approvalExecuter NOT IN (1,3) "
					+ " AND deleteFlag = " + CommonConstant.FLAG_ZERO + " AND requestWorkflowAuditIsActive = "
					+ CommonConstant.CONSTANT_ONE + " AND userId != " + authDetailsVo.getUserId();

			getEntityManager().createQuery(query).executeUpdate();
		} catch (Exception exe) {
			Log.info("Requst Configuration Dao updateForwardReqDecision Exception", exe);
			throw new CommonException("dataFailure");
		}
	}

	public void updateCancelDecision(RequestVO requestVo, int decision, AuthDetailsVo authDetailsVo)
			throws CommonException {

		try {
			String query = "update RequestWorkFlowAuditEntity  set descisionType = " + decision + " ,updateDate = ' "
					+ CommonConstant.getCurrentDateTimeAsString() + "'";

			if (null != authDetailsVo.getUserId()) {
				query = query + " ,updateBy = " + authDetailsVo.getUserId();
			}

			query = query + " where  requestId = " + requestVo.getRequestId() + " AND descisionType = 0 "
					+ " AND deleteFlag = " + CommonConstant.FLAG_ZERO + " AND requestWorkflowAuditIsActive = "
					+ CommonConstant.CONSTANT_ONE;

			getEntityManager().createQuery(query).executeUpdate();
		} catch (Exception exe) {
			Log.info("Requst Configuration Dao updateCancelDecision Exception", exe);
			throw new CommonException("dataFailure");
		}
	}

	@SuppressWarnings("unchecked")
	public RequestWorkFlowSlaEntity findExecutorSlaForCalc(int id, int priority) throws CommonException, Exception {

		RequestWorkFlowSlaEntity requestWorkFlowexeEntity = null;

		List<RequestWorkFlowSlaEntity> requestWorkFlowList = null;

		try {
			String query = " FROM RequestWorkFlowSlaEntity sla where sla.reqWorkFlowId =  " + id
					+ " and sla.deleteFlag = " + CommonConstant.FLAG_ZERO
					+ " and sla.reqWorkFlowSeqId = 0  and sla.protoType = " + priority;

			requestWorkFlowList = (ArrayList<RequestWorkFlowSlaEntity>) getEntityManager().createQuery(query)
					.getResultList();

			requestWorkFlowexeEntity = requestWorkFlowList.get(0);

		} catch (Exception e) {
			throw new CommonException("dataFailure");
		}
		return requestWorkFlowexeEntity;
	}

	@SuppressWarnings("unchecked")
	public RequestWorkFlowSlaEntity findApproverSlaForCalc(int id, int priority) throws CommonException, Exception {

		RequestWorkFlowSlaEntity requestWorkFlowexeEntity = null;

		List<RequestWorkFlowSlaEntity> requestWorkFlowList = null;

		try {
			String query = " FROM RequestWorkFlowSlaEntity sla where sla.reqWorkFlowId =  " + id
					+ " and sla.deleteFlag = " + CommonConstant.FLAG_ZERO
					+ " and sla.reqWorkFlowSeqId != 0  and sla.protoType = " + priority;

			requestWorkFlowList = (ArrayList<RequestWorkFlowSlaEntity>) getEntityManager().createQuery(query)
					.getResultList();

			requestWorkFlowexeEntity = requestWorkFlowList.get(0);

		} catch (Exception e) {
			throw new CommonException("dataFailure");
		}
		return requestWorkFlowexeEntity;
	}

	public void updateSLAForExecutor(RequestEntity requestVo, int workflowId, int sla, AuthDetailsVo authDetailsVo)
			throws CommonException {

		try {
			String query = "update RequestWorkFlowAuditEntity  set requestWorkflowAuditSla = " + sla + " ,"
					+ "updateDate = ' " + CommonConstant.getCurrentDateTimeAsString() + "'";

			if (null != authDetailsVo.getUserId()) {
				query = query + " ,updateBy = " + authDetailsVo.getUserId();
			}

			query = query + " where  requestId = " + requestVo.getRequestId() + " AND approvalExecuter =2 "
					+ " AND deleteFlag = " + CommonConstant.FLAG_ZERO + " AND workFlowId = " + workflowId;

			getEntityManager().createQuery(query).executeUpdate();
		} catch (Exception exe) {
			Log.info("Requst Configuration Dao updateSLAForExecutor Exception", exe);
			throw new CommonException("dataFailure");
		}
	}

	public void updateSLAForApprover(RequestEntity requestVo, int workflowId, int sla, AuthDetailsVo authDetailsVo)
			throws CommonException {

		try {
			String query = "update RequestWorkFlowAuditEntity  set requestWorkflowAuditSla = " + sla + " ,"
					+ "updateDate = ' " + CommonConstant.getCurrentDateTimeAsString() + "'";

			if (null != authDetailsVo.getUserId()) {
				query = query + " ,updateBy = " + authDetailsVo.getUserId();
			}

			query = query + " where  requestId = " + requestVo.getRequestId() + " AND approvalExecuter =1 "
					+ " AND deleteFlag = " + CommonConstant.FLAG_ZERO + " AND workFlowId = " + workflowId
					+ " AND descisionType  = " + 0;

			getEntityManager().createQuery(query).executeUpdate();
		} catch (Exception exe) {
			Log.info("Requst Configuration Dao updateSLAForApprover Exception", exe);
			throw new CommonException("dataFailure");
		}
	}	

	public RequestWorkFlowEntity getWorkflow(int workflowId) throws CommonException {
		RequestWorkFlowEntity requestWorkFlow = new RequestWorkFlowEntity();
		try {
			String query = " From RequestWorkFlowEntity where reqWorkFlowId = " + workflowId;
			requestWorkFlow = (RequestWorkFlowEntity) getEntityManager().createQuery(query).getSingleResult();

		} catch (Exception exe) {
			Log.info("Requst Configuration Dao updateSLAForApprover Exception", exe);
			throw new CommonException("dataFailure");
		}
		return requestWorkFlow;
	}
	
	@SuppressWarnings("unchecked")
	public RequestWorkFlowEntity findWorkflowId(int id) {

		List<RequestWorkFlowEntity> requestWorkFlowList = null;

		RequestWorkFlowEntity requestWorkFlowEntity = new RequestWorkFlowEntity();
		try {
			String query = " select wrk FROM RequestWorkFlowAuditEntity aud ,"
					+ " RequestWorkFlowEntity wrk WHERE wrk.reqWorkFlowId = aud.workFlowId  " 
					+ " and aud.requestId = "
					+ id + " and aud.deleteFlag = " + CommonConstant.FLAG_ZERO;

			requestWorkFlowList = (ArrayList<RequestWorkFlowEntity>) getEntityManager().createQuery(query)
					.getResultList();

			if (requestWorkFlowList.size() > 0) {
				requestWorkFlowEntity = requestWorkFlowList.get(0);
			}

		} catch (Exception exe) {
			Log.info("Request Configuration Service findWorkflowId Exception", exe);
			throw new CommonException("dataFailure");
		}
		return requestWorkFlowEntity;
	}
}
