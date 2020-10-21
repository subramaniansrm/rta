package com.srm.rta.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.srm.coreframework.dao.CommonDAO;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.rta.entity.RequestTypeEntity;
import com.srm.rta.vo.RequestTypeVO;

@Repository
public class RequestTypeDAO extends CommonDAO{
	
	@SuppressWarnings("unchecked")
	public List<RequestTypeEntity> searchAll(RequestTypeVO requestTypeActionVo,AuthDetailsVo authDetailsVo) {

		String query = "FROM RequestTypeEntity r where r.entityLicenseId = '"+ authDetailsVo.getEntityId() +"' and r.deleteFlag = " + CommonConstant.FLAG_ZERO;

		StringBuffer modifiedQuery = new StringBuffer(query);

		if (requestTypeActionVo.getRequestTypeId() !=null)
			modifiedQuery.append(" and r.requestTypeId = " + requestTypeActionVo.getRequestTypeId());

		if (requestTypeActionVo.getRequestTypeCode() != null && !requestTypeActionVo.getRequestTypeCode().isEmpty()) {
			modifiedQuery.append(
					" and LOWER(r.requestTypeCode) LIKE LOWER('%" + requestTypeActionVo.getRequestTypeCode() + "%')");
		}

		if (requestTypeActionVo.getRequestTypeName() != null && !requestTypeActionVo.getRequestTypeName().isEmpty()) {

			modifiedQuery.append(
					" and LOWER(r.requestTypeName) LIKE LOWER('%" + requestTypeActionVo.getRequestTypeName() + "%')");
		}
		if (requestTypeActionVo.getRequestTypeUrl() != null && !requestTypeActionVo.getRequestTypeUrl().isEmpty()) {
			modifiedQuery.append(
					" and LOWER(r.requestTypeUrl) LIKE LOWER('%" + requestTypeActionVo.getRequestTypeUrl() + "%')");
		}

		if (requestTypeActionVo.getStatus() != null) {
			if (requestTypeActionVo.getStatus().equals(CommonConstant.Active)) {
				modifiedQuery.append(" and r.requestTypeIsActive =" + CommonConstant.ACTIVE);
			} else {
				modifiedQuery.append(" and r.requestTypeIsActive =" + CommonConstant.CONSTANT_ZERO);
			}
		}
		modifiedQuery.append(" ORDER BY r.requestTypeId DESC ");

		List<RequestTypeEntity> requestType = (List<RequestTypeEntity>) getEntityManager()
				.createQuery(modifiedQuery.toString()).getResultList();

		return requestType;
	}
	
	
	public int findDuplicate(RequestTypeVO requestTypeActionVo,AuthDetailsVo authDetailsVo) {
		String query ;

		 query = "SELECT COUNT(r.requestTypeId) FROM RequestTypeEntity r  "
		 		+ " where r.entityLicenseId = '"+ authDetailsVo.getEntityId() +"'  and  r.deleteFlag = "
				+ CommonConstant.FLAG_ZERO + " AND LOWER(r.requestTypeName) = LOWER('"
				+ requestTypeActionVo.getRequestTypeName().trim() + "')";
		
	/*	if (null != requestTypeActionVo.getRequestTypeId()) {
			query = query +" AND r.requestTypeId != " + requestTypeActionVo.getRequestTypeId();
		}*/
		int  count = (int)(long) getEntityManager().createQuery(query).getSingleResult();

		return count;

	}
	
	/**
	 * This method is used to delete RequestType record by checking whether the
	 * particular RequestType id is available in RequestSubType screen or not.
	 * 
	 * 
	 * @param requestTypeId
	 * 
	 * @return void
	 * 
	 */
	public int findRequestSubType(int requestTypeId,AuthDetailsVo authDetailsVo) {
		int count = 0;

		String query = "SELECT COUNT(requestSubTypeId) FROM RequestSubTypeEntity  where  entityLicenseId = '"+ authDetailsVo.getEntityId() +"'  and deleteFlag = "
				+ CommonConstant.FLAG_ZERO + " AND requestTypeId = " + requestTypeId;

		count = (int) (long) getEntityManager().createQuery(query).getSingleResult();

		return count;

	}
	
	/**
	 * This method is used to delete RequestType record by checking whether the
	 * particular RequestType id is available in Request screen or not.
	 * 
	 * 
	 * @param requestTypeId
	 * 
	 * @return void
	 * 
	 */
	public int findDeleteRequest(int requestTypeId,AuthDetailsVo authDetailsVo) {
		int count = 0;

		String query = "SELECT COUNT(requestTypeId) FROM RequestEntity  where entityLicenseId = '"+ authDetailsVo.getEntityId() +"' and deleteFlag = "
				+ CommonConstant.FLAG_ZERO + " AND requestTypeId = " + requestTypeId;

		count = (int) (long) getEntityManager().createQuery(query).getSingleResult();

		return count;

	}

	/**
	 * This method is used to delete RequestType record by checking whether the
	 * particular RequestType id is available in RequestScreenConfig screen or
	 * not.
	 * 
	 * 
	 * @param requestTypeId
	 * 
	 * @return void
	 * 
	 */
	public int findScreenConfig(int requestTypeId,AuthDetailsVo authDetailsVo) {
		int count = 0;

		String query = "SELECT COUNT(requestTypeId) FROM RequestScreenConfigurationEntity  where entityLicenseId = '"+ authDetailsVo.getEntityId() +"'"
				+ " and deleteFlag = " + CommonConstant.FLAG_ZERO + " AND requestTypeId = " + requestTypeId;

		count = (int) (long) getEntityManager().createQuery(query).getSingleResult();

		return count;

	}

	/**
	 * This method is used to delete RequestType record by checking whether the
	 * particular RequestType id is available in RequestWorkFlow Screen or not.
	 * 
	 * 
	 * @param requestTypeId
	 * 
	 * @return void
	 * 
	 */
	public int findDeleteWorkflow(int requestTypeId,AuthDetailsVo authDetailsVo) {
		int count = 0;

		String query = "SELECT COUNT(requestTypeId) FROM RequestWorkFlowEntity  where entityLicenseId = '"+ authDetailsVo.getEntityId() +"' "
				+ " and deleteFlag = "
				+ CommonConstant.FLAG_ZERO + " AND requestTypeId = " + requestTypeId;

		count = (int) (long) getEntityManager().createQuery(query).getSingleResult();

		return count;
	}

	
	

}
