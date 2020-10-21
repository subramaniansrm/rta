package com.srm.rta.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.srm.coreframework.dao.CommonDAO;
import com.srm.coreframework.util.CommonConstant;

@Component
public class UserProfileDao extends CommonDAO{

	@Value("${commonDatabaseSchema}")
	private String commonDatabaseSchema;
	
	@SuppressWarnings("unchecked")
	public List<Object> getUserDetails(Integer entityId, Integer userId){
		
		String userQuery = " SELECT u.USER_PROFILE,u.USER_ID,u.FIRST_NAME,u.LAST_NAME , lic.rin_ma_entity_name , lic.idrin_ma_entity_id"
						+ " FROM " + commonDatabaseSchema +".user u"
						+ " LEFT JOIN " + commonDatabaseSchema +".rin_ma_entity_license lic ON lic.idrin_ma_entity_id = u.rin_ma_entity_id"
						+ " WHERE u.delete_flag = '"+CommonConstant.FLAG_ZERO
						+ "' and u.rin_ma_entity_id = " + entityId + " and u.USER_ID = " + userId ;
		List<Object> list = (List<Object>)getEntityManager().createNativeQuery(userQuery).getResultList();
		return list;
		
	}
}
 						