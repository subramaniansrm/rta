package com.srm.rta.dao;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.srm.coreframework.dao.CommonDAO;
import com.srm.coreframework.entity.CityEntity;
import com.srm.coreframework.entity.CountryEntity;
import com.srm.coreframework.entity.Division;
import com.srm.coreframework.entity.Screen;
import com.srm.coreframework.entity.StateEntity;
import com.srm.coreframework.entity.UserDepartment;
import com.srm.coreframework.entity.UserEntity;
import com.srm.coreframework.entity.UserLocation;
import com.srm.coreframework.entity.UserType;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.coreframework.vo.ScreenJsonVO;
import com.srm.coreframework.vo.UserDepartmentVO;
import com.srm.coreframework.vo.UserMasterVO;
import com.srm.coreframework.vo.UserRoleTypeVO;
import com.srm.coreframework.vo.UserRoleVO;
import com.srm.rta.vo.RequestWorkFlowAuditVO;

@Repository
public class DropdownDAO extends CommonDAO {


	/**
	 * Method is used for Load the location
	 * 
	 * @return list List<UserLocationEntity>
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getAllLocation(ScreenJsonVO screenJson,AuthDetailsVo authDetailsVo) {

		try {
			String query = "SELECT c.id,c.userLocationName FROM UserLocation c WHERE  c.entityLicenseEntity.id = "
					+ authDetailsVo.getEntityId() + " AND c.activeFlag = '" + CommonConstant.CONSTANT_ONE
					+ "' AND c.deleteFlag  = '" + CommonConstant.FLAG_ZERO + "' ORDER BY id DESC ";

			List<Object[]> list = (List<Object[]>) getEntityManager().createQuery(query).getResultList();

			return list;
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();

		}
		return null;
	}

	/**
	 * Method is used for Load the Department
	 * 
	 * @return list List<UserDepartmentEntity>
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getAllDepartment(UserDepartmentVO departmentVo,AuthDetailsVo authDetailsVo) {

		List<Object[]> list = null;

		String query = "select c.id,c.userDepartmentName,c.subLocationEntity.subLocationName FROM UserDepartment c WHERE  c.entityLicenseEntity.id = "
				+ authDetailsVo.getEntityId() + " AND c.deleteFlag  = '" + CommonConstant.FLAG_ZERO + "'";

		if (departmentVo.getUserLocation() != null && departmentVo.getUserLocation() != null) {

			query = query + " and c.userLocationEntity.id = " + departmentVo.getUserLocation() + "";
		}
		if (departmentVo.getSublocationId() != null) {

			query = query + " and c.subLocationEntity.sublocationId = " + departmentVo.getSublocationId() + "";
		}

		query = query + " ORDER BY c.id DESC ";

		try {
			list = (List<Object[]>) getEntityManager().createQuery(query).getResultList();
			return list;

		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Method is used for Load the role
	 * 
	 * @return list List<UserRoleEntity>
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getAllRole(UserRoleVO userRoleVo,AuthDetailsVo authDetailsVo) {

		String query = "SELECT c.id,c.userRoleName FROM UserRole c WHERE  c.entityLicenseEntity.id = "
				+ authDetailsVo.getEntityId() + " AND c.deleteFlag  = '" + CommonConstant.FLAG_ZERO + "'";

		if (userRoleVo.getUserLocation() != null && userRoleVo.getUserLocation() != null) {

			query = query + " and c.userLocationEntity.id = " + userRoleVo.getUserLocation();
		}

		if (userRoleVo.getSublocationId() != null) {

			query = query + " and c.subLocationEntity.sublocationId = " + userRoleVo.getSublocationId();
		}

		if (userRoleVo.getUserDepartment() != null && userRoleVo.getUserDepartment() != null) {

			query = query + " and c.userDepartmentEntity.id = " + userRoleVo.getUserDepartment();
		}

		query = query + " ORDER BY c.id DESC ";

		List<Object[]> list = (List<Object[]>) getEntityManager().createQuery(query).getResultList();

		return list;
	}

	/**
	 * Method is used for Load the Division
	 * 
	 * @return list List<DivisionEntity>
	 */
	@SuppressWarnings("unchecked")
	public List<Division> getAllDivision(AuthDetailsVo authDetailsVo) {

		String query = "FROM Division c WHERE   c.entityLicenseEntity.id = " + authDetailsVo.getEntityId()
				+ " AND c.deleteFlag  = '" + CommonConstant.FLAG_ZERO + "' ORDER BY id DESC ";

		try {
			List<Division> list = (List<Division>) getEntityManager().createQuery(query).getResultList();
			return list;

		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();

		}

		return null;
	}

	/**
	 * Method is used for Load the State
	 * 
	 * @return list List<StateEntity>
	 */
	@SuppressWarnings("unchecked")
	public List<StateEntity> getAllState(AuthDetailsVo authDetailsVo) {

		String query = "FROM StateEntity c WHERE c.entityLicenseEntity.id = " + authDetailsVo.getEntityId()
				+ " ORDER BY id DESC ";

		List<StateEntity> list = (List<StateEntity>) getEntityManager().createQuery(query).getResultList();

		return list;
	}

	/**
	 * Method is used for Load the Screen.
	 * 
	 * @return list List<ScreenEntity>
	 */
	@SuppressWarnings("unchecked")
	public List<Screen> getAllScreen() {

		String query = "FROM Screen c ORDER BY screenId DESC ";

		List<Screen> list = (List<Screen>) getEntityManager().createQuery(query).getResultList();

		return list;
	}

	/**
	 * Method is used for Load the City.
	 * 
	 * @return list List<CityEntity>
	 */
	@SuppressWarnings("unchecked")
	public List<CityEntity> getAllCity(AuthDetailsVo authDetailsVo) {

		String query = "FROM CityEntity c "
				+ " ORDER BY id DESC ";

		List<CityEntity> list = (List<CityEntity>) getEntityManager().createQuery(query).getResultList();

		return list;
	}

	/**
	 * Method is used for Load the Country.
	 * 
	 * @return list List<CountryEntity>
	 */
	@SuppressWarnings("unchecked")
	public List<CountryEntity> getAllCountry(AuthDetailsVo authDetailsVo) {

		String query = "FROM CountryEntity c WHERE c.entityLicenseEntity.id = " + authDetailsVo.getEntityId()
				+ "AND c.deleteFlag  = '" + CommonConstant.FLAG_ZERO + "' ORDER BY c.id DESC ";

		List<CountryEntity> list = (List<CountryEntity>) getEntityManager().createQuery(query).getResultList();

		return list;
	}

	/**
	 * Method is used for Load the Request Type.
	 * 
	 * @return listRequestTypeEntity List<RequestTypeEntity>
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getAllTypeList(AuthDetailsVo authDetailsVo) {

		String query = "SELECT idrin_ma_request_type_id,rin_ma_request_type_name FROM rin_ma_request_type WHERE delete_flag = '" + CommonConstant.FLAG_ZERO + "'  "
				+ " AND rin_ma_entity_id = " + authDetailsVo.getEntityId()
				+ " AND rin_ma_request_type_is_active = 1 ORDER BY idrin_ma_request_type_id DESC";

		List<Object[]> listRequestTypeEntity = (List<Object[]>) getEntityManager().createNativeQuery(query).getResultList();

		return listRequestTypeEntity;
	}

	/**
	 * Method is to get all Sub type List.
	 * 
	 * @param typeId
	 * @return listRequestSubTypeEntity List<RequestSubTypeEntity>
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getAllSubTypeList(int typeId,AuthDetailsVo authDetailsVo) {

		String query = "SELECT idrin_ma_request_subtype_id,rin_ma_request_subtype_name,rin_ma_request_subtype_priorty "
				+ " FROM rin_ma_request_subtype WHERE delete_flag = '" + CommonConstant.FLAG_ZERO + "' "
				+ " AND rin_ma_request_subtype_is_active = 1 AND  " + "  rin_ma_entity_id = " + authDetailsVo.getEntityId()
				+ " " + " AND rin_ma_request_type_id =" + typeId + " ORDER BY idrin_ma_request_subtype_id DESC";
		List<Object[]> listRequestSubTypeEntity = (List<Object[]>) getEntityManager().createNativeQuery(query).getResultList();

		return listRequestSubTypeEntity;
	}

	/**
	 * method to load userDepartment
	 * 
	 * @return UserRoleMaster
	 */
	@SuppressWarnings("unchecked")
	public List<UserDepartment> getLoadUserDepartmentDetails(AuthDetailsVo authDetailsVo) {

		String query = "FROM UserDepartment c WHERE c.entityLicenseEntity.id = " + authDetailsVo.getEntityId()
				+ " AND c.deleteFlag  = '" + CommonConstant.FLAG_ZERO + "' ORDER BY id DESC ";

		List<UserDepartment> userDepartmentEntityList = (List<UserDepartment>) getEntityManager().createQuery(query)
				.getResultList();

		return userDepartmentEntityList;
	}

	/**
	 * method to load userLocation
	 * 
	 * @return UserRoleMaster
	 */

	@SuppressWarnings("unchecked")
	public List<UserLocation> getLoadUserLocationDetails(AuthDetailsVo authDetailsVo) {

		String query = "FROM UserLocation c WHERE c.entityLicenseEntity.id = " + authDetailsVo.getEntityId()
				+ " AND c.activeFlag = '" + CommonConstant.CONSTANT_ONE + "' AND c.deleteFlag  = '"
				+ CommonConstant.FLAG_ZERO + "' ORDER BY id DESC ";

		List<UserLocation> userLocationEntityList = (List<UserLocation>) getEntityManager().createQuery(query).getResultList();

		return userLocationEntityList;
	}

	/**
	 * method used to load Location
	 * 
	 * 
	 * @return
	 */
	public List<UserLocation> loadDepartmentLocation(AuthDetailsVo authDetailsVo) {

		String query = "FROM UserLocation c WHERE c.deleteFlag  = '" + CommonConstant.FLAG_ZERO
				+ "' and c.entityLicenseEntity.id = " + authDetailsVo.getEntityId() + " ORDER BY id DESC ";

		@SuppressWarnings("unchecked")
		List<UserLocation> userLocationEntity = (List<UserLocation>) getEntityManager().createQuery(query).getResultList();

		return userLocationEntity;

	}

	/**
	 * Method is used for Load Sub location list
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked") 
	public List<Object[]> getAllSublocatList(int id,AuthDetailsVo authDetailsVo) {
		String query = "SELECT idrin_ma_sublocation_sublocationId,rin_ma_sublocation_name "
				+ " FROM "+ getCommonDatabaseSchema() +".rin_ma_sublocation " + " WHERE delete_flag = '"+ CommonConstant.FLAG_ZERO +"' "
				+ " AND rin_ma_sublocation_subLocationIsActive = 1 " + " and rin_ma_entity_id ="
				+ authDetailsVo.getEntityId();

		if (id != 0) {

			query = query + " AND rin_ma_sublocation_locationId =" + id;
		}

		query = query + " ORDER BY idrin_ma_sublocation_sublocationId DESC";

		List<Object[]> subLocationEntityList = (List<Object[]>) getEntityManager().createNativeQuery(query).getResultList();
		return subLocationEntityList;

	}

	public List<Object> getAllUser(UserMasterVO userMasterVo,AuthDetailsVo authDetailsVo) {

		String query = "select USER_ID,concat(FIRST_NAME,' ',LAST_NAME )FROM user c WHERE " + "  c.ACTIVE = '"
				+ CommonConstant.CONSTANT_ONE + "' AND c.DELETE_FLAG  = '" + CommonConstant.FLAG_ZERO
				+ "' and c.rin_ma_entity_id = " + authDetailsVo.getEntityId();

		if (userMasterVo.getUserLocation() != null && userMasterVo.getUserLocation() != 0) {

			query = query + " and c.USER_LOCATION_ID = " + userMasterVo.getUserLocation();
		}
		if (userMasterVo.getSubLocation() != null && userMasterVo.getSubLocation() != 0) {

			query = query + " and c.USER_SUBLOCATION_ID = " + userMasterVo.getSubLocation();
		}
		if (userMasterVo.getUserDepartment() != null && userMasterVo.getUserDepartment() != 0) {

			query = query + " and c.USER_DEPARTMENT_ID = " + userMasterVo.getUserDepartment();
		}
		if (userMasterVo.getUserRole() != null && userMasterVo.getUserRole() != 0) {

			query = query + " and c.USER_ROLE_ID= " + userMasterVo.getUserRole();
		}

		query = query + " ORDER BY USER_ID DESC ";

		List<Object> list = (List<Object>) getEntityManager().createNativeQuery(query).getResultList();

		return list;
	}

	/**
	 * Method is used for Load user execuer
	 * 
	 * @param userMasterVo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getUserExecuter(UserEntity userEntity,AuthDetailsVo authDetailsVo) {

		String query = "SELECT u.USER_ID,u.FIRST_NAME,u.USER_DEPARTMENT_ID FROM user u "
				+ " WHERE u.DELETE_FLAG = '"+ CommonConstant.FLAG_ZERO +"' AND u.SYS_APP_ID =2 AND u.USER_DEPARTMENT_ID = "
				+ userEntity.getUserDepartmentEntity().getId() + " AND u.USER_ROLE_ID = " + authDetailsVo.getRoleId()
				+ " AND u.USER_ID != " + authDetailsVo.getUserId() + "" + " u.rin_ma_entity_id = '" + authDetailsVo.getEntityId()
				+ "'";

		List<Object[]> list = (List<Object[]>) getEntityManager().createNativeQuery(query).getResultList();

		return list;
	}

	/**
	 * Method is used to get the Level
	 * 
	 * @return list
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getAllLevel(AuthDetailsVo authDetailsVo) {

		String query = "SELECT COMMON_ID,ITEM_VALUE FROM "+getCommonDatabaseSchema()+".`common_storage` "
				+ " WHERE rin_ma_entity_id = "+ authDetailsVo.getEntityId() +" ORDER BY COMMON_ID";

		List<Object[]> list = (List<Object[]>) getEntityManager().createNativeQuery(query).getResultList();

		return list;
	}

	
	/**
	 * Method is used to get the Level
	 * 
	 * @return list
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> languageDropdown(AuthDetailsVo authDetailsVo) {

		String query = "SELECT LANGUAGE_CODE,LANGUAGE FROM "+getCommonDatabaseSchema()+".`common_language` "
				+ " WHERE ACTIVE = "+ CommonConstant.FLAG_ONE +" ORDER BY LANGUAGE";

		List<Object[]> list = (List<Object[]>) getEntityManager().createNativeQuery(query).getResultList();

		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<UserEntity> getUserDep(UserMasterVO userMasterVo,AuthDetailsVo authDetailsVo) {
		String query = "FROM UserEntity c WHERE c.entityLicenseEntity.id = " + authDetailsVo.getEntityId()
				+ "AND c.userDepartmentEntity.id = " + userMasterVo.getUserDepartment() + " AND c.activeFlag = '"
				+ CommonConstant.CONSTANT_ONE + "' AND c.deleteFlag  = '" + CommonConstant.FLAG_ZERO
				+ "' ORDER BY id DESC ";
		List<UserEntity> list = (List<UserEntity>) getEntityManager().createQuery(query).getResultList();
		return list;
	}

	public List<UserType> getRoleType(UserRoleTypeVO userRoleTypeVo,AuthDetailsVo authDetailsVo) {
		String query = "FROM UserType c WHERE c.entityLicenseEntity.id = " + authDetailsVo.getEntityId()
				+ " ORDER BY c.userTypeId DESC ";
		@SuppressWarnings("unchecked")
		List<UserType> list = (List<UserType>) getEntityManager().createQuery(query).getResultList();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getReassignUser(RequestWorkFlowAuditVO requestWorkFlowAuditVo,AuthDetailsVo authDetailsVo) {

		String query = "SELECT DISTINCT(u.USER_ID),u.FIRST_NAME,u.LAST_NAME FROM "+ getCommonDatabaseSchema()
				+ ".user u LEFT JOIN "+getRtaDatabaseSchema()+".rin_tr_req_workflow_audit audit "
				+ " ON u.USER_ID = audit.rin_tr_req_workflow_audit_user_id " + " WHERE audit.rin_tr_request_id = "
				+ requestWorkFlowAuditVo.getRequestId() + " AND " + " audit.rin_tr_req_workflow_audit_user_id != "
				+ authDetailsVo.getUserId() + " " + " AND audit.rin_tr_req_workflow_audit_approval_executer =2"
				+ " and u.rin_ma_entity_id = " + authDetailsVo.getEntityId();

		List<Object[]> list = (List<Object[]>) getEntityManager().createNativeQuery(query).getResultList();
		return list;
	}
	
	/**
	 * Method is to get all Sub type List.
	 * 
	 * @param typeId
	 * @return listRequestSubTypeEntity List<RequestSubTypeEntity>
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getAllSubTypeListBasedOnWorkFlow(int typeId,AuthDetailsVo authDetailsVo) {
		
		List<Object[]> listRequestSubType = new ArrayList<Object[]>();
		
		String query ="SELECT idrin_ma_request_subtype_id,rin_ma_request_subtype_name,rin_ma_request_subtype_priorty "
				+ " FROM rin_ma_request_subtype rst "
				+ " LEFT JOIN rin_ma_req_workflow wf ON rst.idrin_ma_request_subtype_id = wf.rin_ma_request_subtype_id "
				+ " LEFT JOIN rin_ma_req_workflow_details wd ON wf.idrin_ma_req_workflow_id = wd.rin_ma_req_workflow_id "
				+ " WHERE rst.rin_ma_request_type_id = " + typeId
				+ " AND rst.delete_flag = 0 AND wf.delete_flag = 0 "
				+ " AND rst.rin_ma_entity_id ="+authDetailsVo.getEntityId()
				+ " AND wd.rin_ma_req_workflow_details_is_active = 1 AND wd.delete_flag = 0"
				+ " AND (wd.rin_ma_req_workflow_location_id = " + 0
				+ " AND wd.rin_ma_req_workflow_sublocation_id = " + 0
				+ " AND wd.rin_ma_req_workflow_department_id = " + 0
				+ " )"
				+ " AND rst.rin_ma_request_subtype_is_active = 1 GROUP BY idrin_ma_request_subtype_id "
				+ " ORDER BY rst.idrin_ma_request_subtype_id DESC";
		
		List<Object[]> listRequestSubTypeEntity = (List<Object[]>) getEntityManager()
				.createNativeQuery(query).getResultList();
		
		if(listRequestSubTypeEntity.size()>0){
			listRequestSubType.addAll(listRequestSubTypeEntity);
		}
		
		String query1 ="SELECT idrin_ma_request_subtype_id,rin_ma_request_subtype_name,rin_ma_request_subtype_priorty "
				+ " FROM rin_ma_request_subtype rst "
				+ " LEFT JOIN rin_ma_req_workflow wf ON rst.idrin_ma_request_subtype_id = wf.rin_ma_request_subtype_id "
				+ " LEFT JOIN rin_ma_req_workflow_details wd ON wf.idrin_ma_req_workflow_id = wd.rin_ma_req_workflow_id "
				+ " WHERE rst.rin_ma_request_type_id = " + typeId
				+ " AND rst.delete_flag = 0 AND wf.delete_flag = 0"
				+ " AND rst.rin_ma_entity_id ="+authDetailsVo.getEntityId()
				+ " AND wd.rin_ma_req_workflow_details_is_active = 1 AND wd.delete_flag = 0"
				+ " AND (wd.rin_ma_req_workflow_location_id = " + authDetailsVo.getLocationId()
				+ " AND wd.rin_ma_req_workflow_sublocation_id = " + authDetailsVo.getSubLocationId()
				+ " AND wd.rin_ma_req_workflow_department_id = " + authDetailsVo.getDepartmentId()
				+ " )"
				+ " AND rst.rin_ma_request_subtype_is_active = 1 GROUP BY idrin_ma_request_subtype_id "
				+ " ORDER BY rst.idrin_ma_request_subtype_id DESC";
		
		List<Object[]> listRequestSubTypeEntity1 = (List<Object[]>) getEntityManager()
				.createNativeQuery(query1).getResultList();
		
		
		
		
		if(listRequestSubTypeEntity1.size()!=0 && !listRequestSubTypeEntity1.isEmpty()){
			
			listRequestSubType.addAll(listRequestSubTypeEntity1);
			
		}
		
		String query2 ="SELECT idrin_ma_request_subtype_id,rin_ma_request_subtype_name,rin_ma_request_subtype_priorty "
				+ " FROM rin_ma_request_subtype rst "
				+ " LEFT JOIN rin_ma_req_workflow wf ON rst.idrin_ma_request_subtype_id = wf.rin_ma_request_subtype_id "
				+ " LEFT JOIN rin_ma_req_workflow_details wd ON wf.idrin_ma_req_workflow_id = wd.rin_ma_req_workflow_id "
				+ " WHERE rst.rin_ma_request_type_id = " + typeId
				+ " AND rst.delete_flag = 0 AND wf.delete_flag = 0 "
				+ " AND rst.rin_ma_entity_id ="+authDetailsVo.getEntityId()
				+ " AND wd.rin_ma_req_workflow_details_is_active = 1 AND wd.delete_flag = 0"
				+ " AND (wd.rin_ma_req_workflow_location_id = " + authDetailsVo.getLocationId()
				+ " AND wd.rin_ma_req_workflow_sublocation_id = " + authDetailsVo.getSubLocationId()
				+ " AND wd.rin_ma_req_workflow_department_id = " + 0
				+ " )"
				+ " AND rst.rin_ma_request_subtype_is_active = 1 GROUP BY idrin_ma_request_subtype_id "
				+ " ORDER BY rst.idrin_ma_request_subtype_id DESC";
		
		List<Object[]> listRequestSubTypeEntity2 = (List<Object[]>) getEntityManager()
				.createNativeQuery(query2).getResultList();
		
		
		if(listRequestSubTypeEntity2.size()!=0 && !listRequestSubTypeEntity2.isEmpty()){
			
			listRequestSubType.addAll(listRequestSubTypeEntity2);
		}

		String query3 ="SELECT idrin_ma_request_subtype_id,rin_ma_request_subtype_name,rin_ma_request_subtype_priorty "
				+ " FROM rin_ma_request_subtype rst "
				+ " LEFT JOIN rin_ma_req_workflow wf ON rst.idrin_ma_request_subtype_id = wf.rin_ma_request_subtype_id "
				+ " LEFT JOIN rin_ma_req_workflow_details wd ON wf.idrin_ma_req_workflow_id = wd.rin_ma_req_workflow_id "
				+ " WHERE rst.rin_ma_request_type_id = " + typeId
				+ " AND rst.delete_flag = 0 AND wf.delete_flag = 0 "
				+ " AND rst.rin_ma_entity_id ="+authDetailsVo.getEntityId()
				+ " AND wd.rin_ma_req_workflow_details_is_active = 1 AND wd.delete_flag = 0"
				+ " AND (wd.rin_ma_req_workflow_location_id = " + authDetailsVo.getLocationId()
				+ " AND wd.rin_ma_req_workflow_sublocation_id = " + 0
				+ " AND wd.rin_ma_req_workflow_department_id = " + authDetailsVo.getDepartmentId()
				+ " )"
				+ " AND rst.rin_ma_request_subtype_is_active = 1 GROUP BY idrin_ma_request_subtype_id "
				+ " ORDER BY rst.idrin_ma_request_subtype_id DESC";
		
		List<Object[]> listRequestSubTypeEntity3 = (List<Object[]>) getEntityManager()
				.createNativeQuery(query3).getResultList();
		
		
		if(listRequestSubTypeEntity3.size()!=0 && !listRequestSubTypeEntity3.isEmpty()){
			
			listRequestSubType.addAll(listRequestSubTypeEntity3);
			
		}
		
		String query4 ="SELECT idrin_ma_request_subtype_id,rin_ma_request_subtype_name,rin_ma_request_subtype_priorty "
				+ " FROM rin_ma_request_subtype rst "
				+ " LEFT JOIN rin_ma_req_workflow wf ON rst.idrin_ma_request_subtype_id = wf.rin_ma_request_subtype_id "
				+ " LEFT JOIN rin_ma_req_workflow_details wd ON wf.idrin_ma_req_workflow_id = wd.rin_ma_req_workflow_id "
				+ " WHERE rst.rin_ma_request_type_id = " + typeId
				+ " AND rst.delete_flag = 0 AND wf.delete_flag = 0 "
				+ " AND rst.rin_ma_entity_id ="+authDetailsVo.getEntityId()
				+ " AND wd.rin_ma_req_workflow_details_is_active = 1 AND wd.delete_flag = 0"
				+ " AND (wd.rin_ma_req_workflow_location_id = " + authDetailsVo.getLocationId()
				+ " AND wd.rin_ma_req_workflow_sublocation_id = " + 0
				+ " AND wd.rin_ma_req_workflow_department_id = " + 0
				+ " )"
				+ " AND rst.rin_ma_request_subtype_is_active = 1 GROUP BY idrin_ma_request_subtype_id "
				+ " ORDER BY rst.idrin_ma_request_subtype_id DESC";
		
		List<Object[]> listRequestSubTypeEntity4 = (List<Object[]>) getEntityManager()
				.createNativeQuery(query4).getResultList();
		
		
		if(listRequestSubTypeEntity4.size()!=0 && !listRequestSubTypeEntity4.isEmpty()){
			
			listRequestSubType.addAll(listRequestSubTypeEntity4);
			
				
		}
		
		
		String query5 ="SELECT idrin_ma_request_subtype_id,rin_ma_request_subtype_name,rin_ma_request_subtype_priorty "
				+ " FROM rin_ma_request_subtype rst "
				+ " LEFT JOIN rin_ma_req_workflow wf ON rst.idrin_ma_request_subtype_id = wf.rin_ma_request_subtype_id "
				+ " LEFT JOIN rin_ma_req_workflow_details wd ON wf.idrin_ma_req_workflow_id = wd.rin_ma_req_workflow_id "
				+ " WHERE rst.rin_ma_request_type_id = " + typeId
				+ " AND rst.delete_flag = 0 AND wf.delete_flag = 0 "
				+ " AND rst.rin_ma_entity_id ="+authDetailsVo.getEntityId()
				+ " AND wd.rin_ma_req_workflow_details_is_active = 1 AND wd.delete_flag = 0"
				+ " AND (wd.rin_ma_req_workflow_location_id = " + 0
				+ " AND wd.rin_ma_req_workflow_sublocation_id = " + authDetailsVo.getSubLocationId()
				+ " AND wd.rin_ma_req_workflow_department_id = " + authDetailsVo.getDepartmentId()
				+ " )"
				+ " AND rst.rin_ma_request_subtype_is_active = 1 GROUP BY idrin_ma_request_subtype_id "
				+ " ORDER BY rst.idrin_ma_request_subtype_id DESC";
		
		List<Object[]> listRequestSubTypeEntity5 = (List<Object[]>) getEntityManager()
				.createNativeQuery(query5).getResultList();
		
		
		if(listRequestSubTypeEntity5.size()!=0 && !listRequestSubTypeEntity5.isEmpty()){
			
			listRequestSubType.addAll(listRequestSubTypeEntity5);
						
		}
		
		String query6 ="SELECT idrin_ma_request_subtype_id,rin_ma_request_subtype_name,rin_ma_request_subtype_priorty "
				+ " FROM rin_ma_request_subtype rst "
				+ " LEFT JOIN rin_ma_req_workflow wf ON rst.idrin_ma_request_subtype_id = wf.rin_ma_request_subtype_id "
				+ " LEFT JOIN rin_ma_req_workflow_details wd ON wf.idrin_ma_req_workflow_id = wd.rin_ma_req_workflow_id "
				+ " WHERE rst.rin_ma_request_type_id = " + typeId
				+ " AND rst.delete_flag = 0 AND wf.delete_flag = 0 "
				+ " AND rst.rin_ma_entity_id ="+authDetailsVo.getEntityId()
				+ " AND wd.rin_ma_req_workflow_details_is_active = 1 AND wd.delete_flag = 0"
				+ " AND (wd.rin_ma_req_workflow_location_id = " + 0
				+ " AND wd.rin_ma_req_workflow_sublocation_id = " + authDetailsVo.getSubLocationId()
				+ " AND wd.rin_ma_req_workflow_department_id = " + 0
				+ " )"
				+ " AND rst.rin_ma_request_subtype_is_active = 1 GROUP BY idrin_ma_request_subtype_id "
				+ " ORDER BY rst.idrin_ma_request_subtype_id DESC";
		
		List<Object[]> listRequestSubTypeEntity6 = (List<Object[]>) getEntityManager()
				.createNativeQuery(query6).getResultList();
		
		
		if(listRequestSubTypeEntity6.size()!=0 && !listRequestSubTypeEntity6.isEmpty()){
			
			listRequestSubType.addAll(listRequestSubTypeEntity6);
			
			}
		
		String query7 ="SELECT idrin_ma_request_subtype_id,rin_ma_request_subtype_name,rin_ma_request_subtype_priorty "
				+ " FROM rin_ma_request_subtype rst "
				+ " LEFT JOIN rin_ma_req_workflow wf ON rst.idrin_ma_request_subtype_id = wf.rin_ma_request_subtype_id "
				+ " LEFT JOIN rin_ma_req_workflow_details wd ON wf.idrin_ma_req_workflow_id = wd.rin_ma_req_workflow_id "
				+ " WHERE rst.rin_ma_request_type_id = " + typeId
				+ " AND rst.delete_flag = 0 AND wf.delete_flag = 0 "
				+ " AND rst.rin_ma_entity_id ="+authDetailsVo.getEntityId()
				+ " AND wd.rin_ma_req_workflow_details_is_active = 1 AND wd.delete_flag = 0"
				+ " AND (wd.rin_ma_req_workflow_location_id = " + 0
				+ " AND wd.rin_ma_req_workflow_sublocation_id = " + 0
				+ " AND wd.rin_ma_req_workflow_department_id = " + authDetailsVo.getDepartmentId()
				+ " )"
				+ " AND rst.rin_ma_request_subtype_is_active = 1 GROUP BY idrin_ma_request_subtype_id "
				+ " ORDER BY rst.idrin_ma_request_subtype_id DESC";
		
		List<Object[]> listRequestSubTypeEntity7 = (List<Object[]>) getEntityManager()
				.createNativeQuery(query7).getResultList();
		
		
		if(listRequestSubTypeEntity7.size()!=0 && !listRequestSubTypeEntity7.isEmpty()){
			
			listRequestSubType.addAll(listRequestSubTypeEntity7);
			
		}
		
		
		Set<Object[]> obj = new LinkedHashSet<Object[]>();

		obj.addAll(listRequestSubType);

		listRequestSubType.clear();

		listRequestSubType.addAll(obj);

		return listRequestSubType;
				
	}

}
