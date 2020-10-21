package com.srm.rta.dao;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.stereotype.Component;

import com.srm.coreframework.dao.CommonDAO;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.vo.AuthDetailsVo;

@Component
public class EntitySelectionDao extends CommonDAO{

	@SuppressWarnings("unchecked")
	public List<Object[]> loadUserEntity(AuthDetailsVo authDetailsVo){
		
		String query = " select el.idrin_ma_entity_id , el.rin_ma_entity_name FROM " + getCommonDatabaseSchema()+".rin_ma_entity_license el  "
						+ " LEFT JOIN "+getCommonDatabaseSchema()+".user_entity_mapping um ON um.ENTITY_ID = el.idrin_ma_entity_id "
						+ " WHERE um.USER_ID = "+ authDetailsVo.getUserId()
						+ " and  el.entity_status = 1 ";
		
		List<Object[]> list = getEntityManager().createNativeQuery(query).getResultList();
		return list;

	}	
	
	
public List<Object[]> getAllExpiredEntityList() {

	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	String toDate = new String("");

	toDate = formatter.format(CommonConstant.getCalenderDate());

	String query = " SELECT lic.idrin_ma_entity_id , det.rin_ma_entity_to_date FROM " 
			 +getCommonDatabaseSchema()+".rin_ma_entity_license lic "
			+ " LEFT JOIN  "  +getCommonDatabaseSchema()+".rin_ma_entity_license_details det ON  det.rin_ma_entity_id = lic.idrin_ma_entity_id "
			+ " WHERE det.rin_ma_entity_to_date < '"+toDate+"' ";

	@SuppressWarnings("unchecked")
	List<Object[]> list = (List<Object[]>) getEntityManager().createNativeQuery(query).getResultList();		
	
	return list;

}
 
public void updateEntityStatusForExpiry(int entityId) {
	String query = "update  "+getCommonDatabaseSchema()+".rin_ma_entity_license  set  entity_status = 0 ,"
			 + " update_date = '"+ CommonConstant.getCurrentDateTimeAsString()+ "'"
			 + " where idrin_ma_entity_id = "+ entityId;

	getEntityManager().createNativeQuery(query.toString()).executeUpdate();

}

	
}