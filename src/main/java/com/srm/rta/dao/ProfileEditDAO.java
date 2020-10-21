package com.srm.rta.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.srm.coreframework.dao.CommonDAO;
import com.srm.coreframework.entity.PhoneBookEntity;
import com.srm.coreframework.entity.UserEntity;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.rta.vo.ProfileEditVO;


@Repository
public class ProfileEditDAO extends CommonDAO {
	
	public UserEntity load(AuthDetailsVo authDetailsVo) {

		String query = " SELECT u FROM UserEntity u" + " WHERE u.entityLicenseEntity.id = " + authDetailsVo.getEntityId() 
				+ " AND u.deleteFlag = '" + CommonConstant.FLAG_ZERO + "' AND u.id = "
				+ authDetailsVo.getUserId();
		
		
//		String query1 = "select * from user u   where  u.USER_ID = "+authDetailsVo.getUserId()+" and "
//				+ " u.delete_flag='0' and u.rin_ma_entity_id = "+authDetailsVo.getEntityId()+" ";
//		
		

		
		try{
		UserEntity profile = (UserEntity) getEntityManager().createQuery(query).getSingleResult();

		return profile;}catch(Exception e){
			e.getMessage();
			e.printStackTrace();
		}
		return null;

	}

	public PhoneBookEntity findId(int phoneBookId,AuthDetailsVo authDetailsVo) {
		String query = "SELECT e FROM PhoneBookEntity e where deleteFlag = ' " + CommonConstant.FLAG_ZERO
				+ " ' AND phoneBookId = " + phoneBookId 
			    + " AND entityLicenseId = '"+ authDetailsVo.getEntityId()+"' " ;
		PhoneBookEntity phoneBookEntity = (PhoneBookEntity) getEntityManager().createQuery(query).getSingleResult();
		return phoneBookEntity;
	}
	
	public PhoneBookEntity findByEmpId(String employeeId,AuthDetailsVo authDetailsVo) {
		PhoneBookEntity phoneBookEntity=new PhoneBookEntity();
		List<PhoneBookEntity>listPhoneBook=new ArrayList<>();
		
		String query = "SELECT e FROM PhoneBookEntity e where e.deleteFlag = '" + CommonConstant.FLAG_ZERO
				+ "' AND e.phoneBookIsActive = '"+ CommonConstant.CONSTANT_ONE 
				+ "' AND e.employeeId = '"+ employeeId +"'"
				+  " and e.entityLicenseId = '"+ authDetailsVo.getEntityId()+"' ";
		
		listPhoneBook= (List<PhoneBookEntity>) getEntityManager().createQuery(query).getResultList();
		if(listPhoneBook!=null && !listPhoneBook.isEmpty()){
			phoneBookEntity=listPhoneBook.get(0);
		}else{
			return null;
		}
		
		return phoneBookEntity;
	}
	

	public void updateSave(ProfileEditVO profileEditVo,AuthDetailsVo authDetailsVo){

		String query = " UPDATE " + getCommonDatabaseSchema() + ".user " + " SET update_by = " + authDetailsVo.getUserId()
				+ ", update_date = '" + CommonConstant.getCurrentDateTimeAsString() + "' ,FIRST_NAME = '" + profileEditVo.getFirstName()+"'";
		
		if (null != profileEditVo.getMiddleName() && !profileEditVo.getMiddleName().isEmpty()) {
			query = query + " , MIDDLE_NAME = '" + profileEditVo.getMiddleName()+"'";
		}
		if (null != profileEditVo.getLastName() && !profileEditVo.getLastName().isEmpty()) {
			query = query + " , LAST_NAME = '" + profileEditVo.getLastName()+"'";
		}
		if (null != profileEditVo.getMobile() && !profileEditVo.getMobile().isEmpty()) {
			query = query + ",MOBILE = " + profileEditVo.getMobile();
		}
		if (null != profileEditVo.getSkypeId() && !profileEditVo.getSkypeId().isEmpty()) {
			query = query + " , SKYPE_ID = '" + profileEditVo.getSkypeId()+"'";
		}
		if(null != profileEditVo.getLocationId()){
			query = query + ", USER_LOCATION_ID = " + profileEditVo.getLocationId();
		}
		if(null != profileEditVo.getSublocationId()){
			query = query + " , USER_SUBLOCATION_ID = " + profileEditVo.getSublocationId() ;
		}
		if(null != profileEditVo.getDepartmentId()){
			query = query + " ,USER_DEPARTMENT_ID = " + profileEditVo.getDepartmentId() ;
		}
		if(null!=profileEditVo.getPhoneBookProfile() && !profileEditVo.getPhoneBookProfile().isEmpty()){
			query = query + " ,USER_PROFILE = '" + profileEditVo.getPhoneBookProfile() +"'";
		}
		if(null!=profileEditVo.getLangCode() && !profileEditVo.getLangCode().isEmpty()){
			query = query + " ,LANG_CODE = '" + profileEditVo.getLangCode() +"'";
		}
		
		query = query + " WHERE USER_ID = " + profileEditVo.getId();
		
		getEntityManager().createNativeQuery(query).executeUpdate();
	}
	
	public UserEntity findUserById(int userId,AuthDetailsVo authDetailsVo) {
		String query = "SELECT e FROM UserEntity e where deleteFlag = '" + CommonConstant.FLAG_ZERO + "' AND id = "
				+ userId + " and e.entityLicenseEntity.id = " + authDetailsVo.getEntityId();

		UserEntity userEntity = (UserEntity) getEntityManager().createQuery(query).getSingleResult();
		return userEntity;
	}

	@SuppressWarnings("unchecked")
	public List<Object> userProfile(Integer entityId, Integer userId){
		
		String query = " SELECT u.USER_ID, u.USER_EMPLOYEE_ID, u.FIRST_NAME, u.MIDDLE_NAME, u.USER_LOGIN_ID, u.SKYPE_ID,  "
						+ " u.EMAIL_ID, u.MOBILE, u.CURRENT_ADDRESS, u.PERMANENT_ADDRESS, u.USER_LOCATION_ID, loc.USER_LOCATION_NAME, "
						+ " u.USER_SUBLOCATION_ID, subloc.rin_ma_sublocation_name, u.USER_DEPARTMENT_ID,dep.USER_DEPARTMENT_NAME,  "
						+ " u.USER_PROFILE, u.USER_ID as id , u.LAST_NAME, u.PASSWORD, "+ "" + ""
						+ " u.USER_ROLE_ID, rol.USER_ROLE_NAME, u.DIVISION_ID, u.PHONE_NUMBER, u.USER_PROFILE as profile "
						+ " , u.LANG_CODE "
						+ " FROM " +getCommonDatabaseSchema()+".user u "
						+ " LEFT JOIN "+getCommonDatabaseSchema()+".user_location loc ON loc.USER_LOCATION_ID = u.USER_LOCATION_ID "
						+ " LEFT JOIN "+getCommonDatabaseSchema()+".rin_ma_sublocation subloc ON subloc.idrin_ma_sublocation_sublocationId = u.USER_SUBLOCATION_ID "
						+ " LEFT JOIN "+getCommonDatabaseSchema()+".user_department dep ON dep.USER_DEPARTMENT_ID = u.USER_DEPARTMENT_ID "
						+ " LEFT JOIN "+getCommonDatabaseSchema()+".user_role rol ON rol.ROLE_ID = u.USER_ROLE_ID "
						+ " WHERE u.delete_flag = '"+CommonConstant.FLAG_ZERO
						+"' and u.rin_ma_entity_id = " + entityId + " and u.USER_ID = "+userId;
						
		
		List<Object> list = (List<Object>)getEntityManager().createNativeQuery(query).getResultList();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> userProfileUpdate(Integer userId){
		
		String query =" SELECT u.USER_EMPLOYEE_ID, u.FIRST_NAME, u.MIDDLE_NAME,"
				+ " u.LAST_NAME, u.MOBILE, u.CURRENT_ADDRESS, u.PERMANENT_ADDRESS,u.EMAIL_ID, u.SKYPE_ID, "
				+ " u.USER_LOCATION_ID, u.USER_SUBLOCATION_ID, u.USER_DEPARTMENT_ID,u.USER_ID, u.LAST_NAME as ln, u.PASSWORD "
				+ " FROM "+getCommonDatabaseSchema()+".user u  WHERE u.delete_flag = '"+CommonConstant.FLAG_ZERO+"'"
				+ " and u.USER_ID ="+ userId;
		List<Object>list = (List<Object>)getEntityManager().createNativeQuery(query).getResultList();
		return list;
	}
	
}
