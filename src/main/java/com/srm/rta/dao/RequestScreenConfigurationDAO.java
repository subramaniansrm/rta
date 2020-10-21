package com.srm.rta.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.srm.coreframework.dao.CommonDAO;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.rta.entity.RequestScreenConfigurationEntity;
import com.srm.rta.vo.RequestScreenConfigurationVO;

@Repository
public class RequestScreenConfigurationDAO extends CommonDAO {

	public List<Object[]> getAll(AuthDetailsVo authDetailsVo) {

		String query = "select request.requestScreenConfigId,request.requestScreenConfigurationCode,request.requestScreenConfigurationName,"
				+ " request.requestScreenConfigurationIsActive,req.requestTypeName,sub.requestSubTypeName,"
				+ " request.requestTypeId,request.requestSubtypeId FROM RequestScreenConfigurationEntity  request , RequestTypeEntity req,RequestSubTypeEntity  sub "
				+ " where request.requestTypeId = req.requestTypeId   "
				+ " and sub.requestSubTypeId = request.requestSubtypeId  "
				+ "  and request.entityLicenseId = '"+ authDetailsVo.getEntityId()+"' and request.deleteFlag = "
				+ CommonConstant.FLAG_ZERO + " and req.deleteFlag =" + CommonConstant.FLAG_ZERO
				+ "and sub.deleteFlag =" + CommonConstant.FLAG_ZERO + " ORDER BY request.requestScreenConfigId DESC ";

		@SuppressWarnings("unchecked")
		List<Object[]> listRequestScreenConfigurationEntity = (List<Object[]>) getEntityManager().createQuery(query)
				.getResultList();
		return listRequestScreenConfigurationEntity;
	
		
	}
		

	public List<Object[]> getAllSearch(RequestScreenConfigurationVO requestScreenConfigurationVo,AuthDetailsVo authDetailsVo) {

		String query = "select request.requestScreenConfigId,request.requestScreenConfigurationCode,request.requestScreenConfigurationName,"
				+ " request.requestScreenConfigurationIsActive,req.requestTypeName,sub.requestSubTypeName,"
				+ " request.requestTypeId,request.requestSubtypeId FROM RequestScreenConfigurationEntity  request , RequestTypeEntity req, RequestSubTypeEntity  sub "
				//+ " where request.requestTypeId = req.requestTypeId join "
				//+ " and sub.requestSubTypeId = request.requestSubtypeId and 
				+ " where request.entityLicenseId = '"
				+ authDetailsVo.getEntityId() + "' and request.deleteFlag = " + CommonConstant.FLAG_ZERO
				+ " and req.deleteFlag =" + CommonConstant.FLAG_ZERO + " and sub.deleteFlag = "
				+ CommonConstant.FLAG_ZERO;

		StringBuffer modifiedQuery = new StringBuffer(query);

		if (null != requestScreenConfigurationVo.getRequestScreenConfigId())
			modifiedQuery.append(
					" and request.requestScreenConfigId = " + requestScreenConfigurationVo.getRequestScreenConfigId());

		if (requestScreenConfigurationVo.getRequestTypeName() != null
				&& !requestScreenConfigurationVo.getRequestTypeName().isEmpty())
			modifiedQuery.append(" and LOWER(req.requestTypeName) LIKE LOWER('%"
					+ requestScreenConfigurationVo.getRequestTypeName() + "%')");

		if (requestScreenConfigurationVo.getRequestSubTypeName() != null
				&& !requestScreenConfigurationVo.getRequestSubTypeName().isEmpty())
			modifiedQuery.append(" and LOWER(sub.requestSubTypeName) LIKE LOWER('%"
					+ requestScreenConfigurationVo.getRequestSubTypeName() + "%')");

		if (requestScreenConfigurationVo.getRequestScreenConfigurationCode() != null
				&& !requestScreenConfigurationVo.getRequestScreenConfigurationCode().isEmpty())
			modifiedQuery.append(" and LOWER(request.requestScreenConfigurationCode) LIKE LOWER('%"
					+ requestScreenConfigurationVo.getRequestScreenConfigurationCode() + "%')");

		if (requestScreenConfigurationVo.getRequestScreenConfigurationName() != null
				&& !requestScreenConfigurationVo.getRequestScreenConfigurationName().isEmpty())
			modifiedQuery.append(" and LOWER(request.requestScreenConfigurationName) LIKE LOWER('%"
					+ requestScreenConfigurationVo.getRequestScreenConfigurationName() + "%')");

		if (requestScreenConfigurationVo.getStatus() != null) {
			if (requestScreenConfigurationVo.getStatus().equals(CommonConstant.Active)) {
				modifiedQuery.append(" and request.requestScreenConfigurationIsActive =" + CommonConstant.ACTIVE);
			} else {
				modifiedQuery
						.append(" and request.requestScreenConfigurationIsActive =" + CommonConstant.CONSTANT_ZERO);
			}
		}

		modifiedQuery.append(" group by request.requestScreenConfigId order by request.requestScreenConfigId desc  ");
		 
		List<Object[]> listRequestScreenConfigurationEntity = (List<Object[]>) getEntityManager()
				.createQuery(modifiedQuery.toString()).getResultList();

		return listRequestScreenConfigurationEntity;
	}

	public List<Object> findScreenConfig(int requestScreenConfigId,AuthDetailsVo authDetailsVo) {

		String query = "select request.requestScreenConfigId,request.requestScreenConfigurationName,"
				+ " request.requestScreenConfigurationCode,request.requestScreenConfigurationIsActive,"
				+ " request.requestTypeId,request.requestSubtypeId, req.requestTypeName,sub.requestSubTypeName,"
				+ " rsdc.requestScreenDetailConfigId,rsdc.requestScreenConfigId,rsdc.requestScreenDetailConfigurationFieldName,"
				+ " rsdc.requestScreenDetailConfigurationFieldType,rsdc.requestScreenDetailConfigurationFieldValue,"
				+ " rsdc.requestScreenDetailConfigurationValidationIsRequired,rsdc.requestScreenDetailConfigurationSequance,"
				+ " rsdc.requestScreenDetailConfigurationIsActive FROM RequestScreenConfigurationEntity  request, "
				+ "  RequestTypeEntity req, RequestSubTypeEntity  sub ,RequestScreenDetailConfigurationEntity rsdc"
				+ " where request.requestTypeId = req.requestTypeId  "
				+ " and sub.requestSubTypeId = request.requestSubtypeId  "
				+ " and request.requestScreenConfigId = rsdc.requestScreenConfigId and request.entityLicenseId = '"+ authDetailsVo.getEntityId()+"' and request.deleteFlag = "
				+ CommonConstant.FLAG_ZERO + " and request.requestScreenConfigId = " + requestScreenConfigId;

		List<Object> result = new ArrayList<Object>();

		result = (List<Object>) getEntityManager().createQuery(query).getResultList();

		return result;

	}

	public int requestscreen(RequestScreenConfigurationVO requestScreenConfigurationVo,AuthDetailsVo authDetailsVo) {
		String query = " SELECT COUNT(req.requestScreenConfigId) FROM RequestScreenConfigurationEntity req WHERE req.entityLicenseId = '"+ authDetailsVo.getEntityId()+"' and req.requestTypeId  = "
				+ requestScreenConfigurationVo.getRequestTypeId() + " AND req.requestSubtypeId = "
				+ requestScreenConfigurationVo.getRequestSubtypeId() + " and deleteFlag = "
				+ CommonConstant.FLAG_ZERO + " and requestScreenConfigurationIsActive = " + CommonConstant.ACTIVE;
		StringBuffer modifiedQuery = new StringBuffer(query);
		if (null != requestScreenConfigurationVo.getRequestScreenConfigId() && 0 != requestScreenConfigurationVo.getRequestScreenConfigId()) {
			modifiedQuery.append(
					" AND req.requestScreenConfigId != " + requestScreenConfigurationVo.getRequestScreenConfigId());
		}

		int count = (int) (long) getEntityManager().createQuery(modifiedQuery.toString()).getSingleResult();
		return count;

	
	}
	
	
	@SuppressWarnings("unchecked")
	public int checkDuplicateConfigName(RequestScreenConfigurationVO requestScreenConfigurationVo,AuthDetailsVo authDetailsVo) {
		
		String query = " SELECT req.idrin_ma_req_screen_config_id,req.rin_ma_req_screen_config_code FROM "+getRtaDatabaseSchema() 
				+ ".rin_ma_req_screen_config req "
				+ " WHERE req.rin_ma_entity_id = "+ authDetailsVo.getEntityId()+" "
				+ " and req.rin_ma_req_screen_config_name  = '"+ requestScreenConfigurationVo.getRequestScreenConfigurationName().trim() +"' "  
				+ " and req.delete_flag = "
				+ CommonConstant.FLAG_ZERO ;
		
		
		if(requestScreenConfigurationVo.isRequestScreenConfigurationIsActive()){
			query = query + " and req.rin_ma_req_screen_config_is_active = '1' " ;
		}else{
			query = query + " and req.rin_ma_req_screen_config_is_active = '0' " ;
		}
			
		
		if(null != requestScreenConfigurationVo.getRequestScreenConfigId()){
			query = query + " and req.idrin_ma_req_screen_config_id != "+ requestScreenConfigurationVo.getRequestScreenConfigId();
		}
		 

		List<Object[]> list = (List<Object[]>) getEntityManager().createNativeQuery(query).getResultList();
		
		int count = 0;
		
		if(null != list && !list.isEmpty() && list.size()>0){
			count = list.size();
		}
		return count;
	}
	
		
	public int isRequestScreenConfigurationCodeAvailable(String configCode,AuthDetailsVo authDetailsVo) {
		int count = 0;

		String query = "SELECT COUNT(requestScreenConfigurationCode) FROM RequestScreenConfigurationEntity where request.entityLicenseId = '"+ authDetailsVo.getEntityId()+"' and deleteFlag = "
				+ CommonConstant.FLAG_ZERO + " and requestScreenConfigurationCode = '" + configCode + "'";

		count = (int) (long) getEntityManager().createQuery(query).getSingleResult();

		return count;

	}

}
