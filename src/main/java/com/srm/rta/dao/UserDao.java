package com.srm.rta.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.srm.coreframework.dao.CommonDAO;
import com.srm.coreframework.entity.EntityLicenseDetails;
import com.srm.coreframework.entity.UserEntity;
import com.srm.coreframework.entity.UserRole;
import com.srm.coreframework.vo.EmailVo;
import com.srm.coreframework.vo.UserMasterVO;

@Component
public class UserDao extends CommonDAO{

	@SuppressWarnings("unchecked")
	public List<Object[]> getUserList(){
		
		String userQuery = " SELECT  ur.USER_ID ,ur.CHANGE_PASSWORD_DATE , ur.create_date , lic.entity_password_expiry_days,"
						   + " ur.FIRST_NAME , ur.EMAIL_ID, ur.rin_ma_entity_id , ur.LANG_CODE FROM "+getCommonDatabaseSchema()+".user ur "
                           + " LEFT JOIN "+getCommonDatabaseSchema()+".rin_ma_entity_license lic ON ur.rin_ma_entity_id = lic.idrin_ma_entity_id "
                           + " WHERE lic.entity_password_expiry_days IS NOT NULL "
                           + " AND ur.ACTIVE = '1' AND ur.delete_flag = '0'" ;
						
		List<Object[]> list = (List<Object[]>)getEntityManager().createNativeQuery(userQuery).getResultList();
		return list;
		
	}
		
	public List<Object[]> getRenewEntityList() {

		List<Object[]> list = new ArrayList<Object[]>();
		try {

			String query = " SELECT e.idrin_ma_entity_id,e.entity_admin_email,d.rin_ma_entity_to_date , e.entity_lang , e.rin_ma_entity_name FROM " 
                    +  getCommonDatabaseSchema()+".rin_ma_entity_license e "
					+ " LEFT JOIN "+getCommonDatabaseSchema()+".rin_ma_entity_license_details d ON d.rin_ma_entity_id = e.idrin_ma_entity_id"
					+ " WHERE e.entity_status = 1 AND d.rin_ma_entity_to_date IS NOT NULL ";

			list = (List<Object[]>) getEntityManager().createNativeQuery(query).getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
		
	@SuppressWarnings("unchecked")
	public UserMasterVO getUser(int entityId) {

		UserMasterVO userVo = new UserMasterVO();
		try {
		String userQuery = " SELECT FIRST_NAME, EMAIL_ID FROM  " + getCommonDatabaseSchema()
				+ ".user WHERE rin_ma_entity_id  = " + entityId + " ORDER BY user_id limit 1";

		List<Object[]> list = (List<Object[]>) getEntityManager().createNativeQuery(userQuery).getResultList();

		for (Object user : list) {

			if (null != (String) ((Object[]) user)[0]) {
				userVo.setFirstName((String) ((Object[]) user)[0]);
			}

			if (null != (String) ((Object[]) user)[1]) {
				userVo.setEmailId((String) ((Object[]) user)[1]);
			}
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userVo;

	}
	
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getRenewTransactionEntityList() {

		List<Object[]> list = new ArrayList<Object[]>();
		try {

			String query = " SELECT e.idrin_ma_entity_id,e.entity_admin_email,d.rin_ma_entity_to_date , "
					+ " e.entity_lang , e.rin_ma_entity_name , d.rin_ma_entity_used_transaction_license ,rin_ma_entity_license_transaction_count " + " FROM "
					+ getCommonDatabaseSchema() + ".rin_ma_entity_license e " + " LEFT JOIN "
					+ getCommonDatabaseSchema()
					+ ".rin_ma_entity_license_details d ON d.rin_ma_entity_id = e.idrin_ma_entity_id"
					+ " WHERE e.entity_status = 1 AND d.rin_ma_entity_license_transaction_count != 0" ;
					//+ " AND e.idrin_ma_entity_id = 0 ";

			list = (List<Object[]>) getEntityManager().createNativeQuery(query).getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getEntityLogList(int entityId) {

		List<Object[]> list = new ArrayList<Object[]>();
		try {

			String query = "SELECT ENTITY_ID , USED_TRANSACTION_COUNT FROM " + getCommonDatabaseSchema()+".entity_license_log WHERE ENTITY_ID =  "+entityId
					+ " order by ENTITY_LICENSE_LOG_ID desc ";

			list = (List<Object[]>) getEntityManager().createNativeQuery(query).getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
 						