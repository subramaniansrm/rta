package com.srm.rta.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.srm.coreframework.dao.CommonDAO;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.rta.entity.RequestSubTypeEntity;
import com.srm.rta.vo.RequestSubTypeVO;

@Repository
public class RequestSubTypeDAO extends CommonDAO {
	
	
	public List<Object[]> searchAll(RequestSubTypeVO requestSubTypeMasterVo,AuthDetailsVo authDetailsVo) {

		String query = "select sub.requestSubTypeCode,sub.requestSubTypeName,sub.requestSubTypeIsActive,"
				+ "type.requestTypeName,sub.requestSubTypeId,sub.requestTypeId,sub.requestSubtypePriorty FROM RequestSubTypeEntity sub,"
				+ "  RequestTypeEntity type where type.requestTypeId = sub.requestTypeId "
				+ " and sub.entityLicenseId = '"+ authDetailsVo.getEntityId()+"' and sub.deleteFlag = "
				+ CommonConstant.FLAG_ZERO + " and type.deleteFlag = " + CommonConstant.FLAG_ZERO;

		StringBuffer modifiedQuery = new StringBuffer(query);

		if (requestSubTypeMasterVo.getRequestSubTypeId() != null)
			modifiedQuery.append(" and sub.requestSubTypeId = " + requestSubTypeMasterVo.getRequestSubTypeId());

		if (requestSubTypeMasterVo.getRequestSubTypeCode() != null
				&& !requestSubTypeMasterVo.getRequestSubTypeCode().isEmpty()) {
			modifiedQuery.append(
					" and LOWER(sub.requestSubTypeCode) LIKE LOWER('%" + requestSubTypeMasterVo.getRequestSubTypeCode() + "%')");
		}

		if (requestSubTypeMasterVo.getRequestTypeName() != null) {

			modifiedQuery
					.append(" and LOWER(type.requestTypeName) LIKE LOWER('%" + requestSubTypeMasterVo.getRequestTypeName() + "%')");
		}
		if (requestSubTypeMasterVo.getRequestSubTypeName() != null) {
			modifiedQuery.append(
					" and LOWER(sub.requestSubTypeName) LIKE LOWER('%" + requestSubTypeMasterVo.getRequestSubTypeName() + "%')");
		}
		
		if (null != requestSubTypeMasterVo.getRequestSubtypePriorty() ){
			modifiedQuery.append(" and sub.requestSubtypePriorty = " + requestSubTypeMasterVo.getRequestSubtypePriorty());
		}
		
		if (requestSubTypeMasterVo.getStatus() != null) {
			
			if (requestSubTypeMasterVo.getStatus().equals(CommonConstant.Active)) {
				modifiedQuery.append(" and sub.requestSubTypeIsActive =" + CommonConstant.ACTIVE);
			} else {
				modifiedQuery.append(" and sub.requestSubTypeIsActive =" + CommonConstant.CONSTANT_ZERO);
			}
		}

		modifiedQuery.append(" ORDER BY sub.requestSubTypeId DESC ");

		List<Object[]> requestSubType = getEntityManager().createQuery(modifiedQuery.toString()).getResultList();

		return requestSubType;
	}
	
	
	
	
	/**
	 * This Method is to get code.
	 * 
	 * @param requestSubTypeVo
	 *            RequestSubTypeVo
	 * @return check
	 */
	public Boolean getCode(RequestSubTypeVO requestSubTypeVo,AuthDetailsVo authDetailsVo) {

		Boolean check = true;
		String query = "FROM RequestSubTypeEntity where entityLicenseId = '"+ authDetailsVo.getEntityId()+"' and deleteFlag = " + CommonConstant.FLAG_ZERO
				+ " AND requestSubTypeCode = '" + requestSubTypeVo.getRequestSubTypeCode() + "'";
		List<RequestSubTypeEntity> requestSubTypeEntity = new ArrayList<RequestSubTypeEntity>();
		requestSubTypeEntity = (List<RequestSubTypeEntity>) getEntityManager().createQuery(query).getResultList();

		if (requestSubTypeEntity != null && requestSubTypeEntity.size() > 0) {
			check = false;
		}
		return check;
	}

	
	
	/**
	 * This method is used to delete RequestSubType and RequestType record by
	 * checking whether the particular RequestType and RequestSubType id is
	 * available in RequestSubType screen or not.
	 * 
	 * 
	 * @param requestTypeId
	 * 
	 * @return void
	 * 
	 */
	public int findRequest(int requestSubTypeId,AuthDetailsVo authDetailsVo) {
		int count = 0;
		String query = "SELECT COUNT(requestSubtypeId) FROM RequestEntity where entityLicenseId = '"+ authDetailsVo.getEntityId()+"' and  deleteFlag = "
				+ CommonConstant.FLAG_ZERO + "	AND requestSubtypeId = " + requestSubTypeId;
		
		javax.persistence.Query query1 = getEntityManager().createQuery(query.toString());
			count = (int) (long)query1.getSingleResult();
		
		return count;

	}

	/**
	 * This method is used to delete RequestSubType and RequestType record by
	 * checking whether the particular RequestType and RequestSubType id is
	 * available in Requestworkflow screen or not.
	 * 
	 * 
	 * @param int
	 *            requestTypeId
	 * @param int
	 *            requestSubTypeId
	 * @return void
	 * 
	 */
	public int findWorkflow(int requestSubTypeId,AuthDetailsVo authDetailsVo) {
		int count = 0;

		String query = "SELECT COUNT(requestSubTypeId) FROM RequestWorkFlowEntity  where  entityLicenseId = '"+ authDetailsVo.getEntityId()+"' and deleteFlag = "
				+ CommonConstant.FLAG_ZERO + "	AND	requestSubTypeId = " + requestSubTypeId;
		javax.persistence.Query query1 = getEntityManager().createQuery(query.toString());
		count = (int) (long)query1.getSingleResult();
		
		return count;

	}

	/**
	 * This method is used to delete RequestSubType and RequestType record by
	 * checking whether the particular RequestType and RequestSubType id is
	 * available in RequestSubType screen or not.
	 * 
	 * 
	 * @param requestSubTypeId
	 * 
	 * @return void
	 * 
	 */
	public int findScreenConfig(int requestSubTypeId,AuthDetailsVo authDetailsVo) {
		int count = 0;

		String query = "SELECT COUNT(requestSubtypeId) FROM RequestScreenConfigurationEntity  where entityLicenseId = '"+ authDetailsVo.getEntityId()+"' and deleteFlag = "
				+ CommonConstant.FLAG_ZERO + "	AND	requestSubtypeId = " + requestSubTypeId;
		javax.persistence.Query query1 = getEntityManager().createQuery(query.toString());
		count = (int) (long)query1.getSingleResult();
		
		return count;

	}
	public int findDuplicate(RequestSubTypeVO requestSubTypeMasterVo,AuthDetailsVo authDetailsVo) {

		//int count = 0;

		String query = "SELECT COUNT(r.requestSubTypeId) FROM RequestSubTypeEntity r  where entityLicenseId = '"+ authDetailsVo.getEntityId()+"' and r.deleteFlag = "
				+ CommonConstant.FLAG_ZERO + " AND LOWER(r.requestSubTypeName) = LOWER('"
				+ requestSubTypeMasterVo.getRequestSubTypeName().trim()+ "') AND r.requestTypeId = " +requestSubTypeMasterVo.getRequestTypeId();
		//StringBuffer modifiedQuery = new StringBuffer(query);
	
		
		if (null!= requestSubTypeMasterVo.getRequestSubTypeId()) {
			query = query + " AND r.requestSubTypeId != " + requestSubTypeMasterVo.getRequestSubTypeId();
		} 
		
		int  count = (int)(long) getEntityManager().createQuery(query).getSingleResult();

		return count;
	}
	public Object[] findSubType(int id,AuthDetailsVo authDetailsVo) {
		String query = "select sub,type.requestTypeName FROM RequestSubTypeEntity sub ,"
				+ "  RequestTypeEntity type where type.requestTypeId = sub.requestTypeId"
				+ " and sub.entityLicenseId = '"+ authDetailsVo.getEntityId()+"' and sub.deleteFlag = "
				+ CommonConstant.FLAG_ZERO + " and type.deleteFlag = " + CommonConstant.FLAG_ZERO
				+ " and sub.requestSubTypeId = " + id;
		Object[] requestSubTypeEntity = null;

		requestSubTypeEntity = (Object[]) getEntityManager().createQuery(query).getSingleResult();

		return requestSubTypeEntity;

	}
}
