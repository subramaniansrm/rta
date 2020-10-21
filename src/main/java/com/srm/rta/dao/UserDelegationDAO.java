package com.srm.rta.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.dao.CommonDAO;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.rta.entity.UserDelegationDetailsEntity;
import com.srm.rta.entity.UserDelegationMasterEntity;
import com.srm.rta.vo.UserDelegationDetailsVO;
import com.srm.rta.vo.UserDelegationMasterVO;

@Repository
public class UserDelegationDAO extends CommonDAO {
	
	
	public UserDelegationMasterEntity create(UserDelegationMasterEntity userDelegationMasterEntity) {

		getEntityManager().persist(userDelegationMasterEntity);	 

		return userDelegationMasterEntity;
	}

	public void createDetails(UserDelegationDetailsEntity userDelegationDetailsEntity) {
		// TODO Auto-generated method stub
		getEntityManager().persist(userDelegationDetailsEntity);

	}

	public List<Object[]> getAll(UserDelegationMasterVO userDelegationMasterVo,AuthDetailsVo authDetailsVo) {	
		
		String query = " SELECT  m.FIRST_NAME," + " user1.FIRST_NAME as delgatedUser,ud.idrin_ma_user_active_from,"
				+ " ud.idrin_ma_user_active_to,ud.idrin_ma_user_type,ud.idrin_ma_delegated_user_id,ud.idrin_ma_user_delegation_detail_id "
				+ " , ud.delegation_remarks,ud.idrin_ma_user_delegation_active " + " FROM rin_ma_user_delegation u "
				+ " LEFT JOIN rin_ma_user_delegation_details ud "
				+ " ON ud.idrin_ma_user_delegation_id = u.idrin_ma_user_delegation_id " + " LEFT JOIN "
				+ getCommonDatabaseSchema() + ".user m " + " ON m.USER_ID = u.idrin_ma_delegation_userid LEFT JOIN "
				+ getCommonDatabaseSchema() + ".user user1 " + " ON user1.USER_ID = ud.idrin_ma_delegated_user_id"
				+ "  WHERE ud.delete_flag = '0' ";

		if (!authDetailsVo.getUserId().equals(CommonConstant.SUPER_ADMIN_ID)) {
			query = query + " and u.idrin_ma_delegation_userid = " + userDelegationMasterVo.getDelegationUserId() + " ";

		}
		query = query + " and u.rin_ma_entity_id = "+ authDetailsVo.getEntityId()
		              + " order by  ud.idrin_ma_user_delegation_detail_id DESC " ;
				
		@SuppressWarnings("unchecked")
		List<Object[]> resultobject = (List<Object[]>) getEntityManager().createNativeQuery(query).getResultList();

		return resultobject;

	}

	public void delete(UserDelegationMasterVO userDelegationMasterVo,AuthDetailsVo authDetailsVo) {

		String updateQuery = "UPDATE `rin_ma_user_delegation_details` ud " + " LEFT JOIN `rin_ma_user_delegation` u "
				+ " ON ud.idrin_ma_user_delegation_id = u.idrin_ma_user_delegation_id " 
				+ " SET ud.delete_flag = '1' , ud.update_by = "+authDetailsVo.getUserId()+
				" ,ud.update_date = '"+CommonConstant.getCurrentDateTimeAsString()+"'"
				+ " WHERE ud.idrin_ma_delegated_user_id = "
				+ userDelegationMasterVo.getUserDelegationDetailsVo().getDelegatedUserId() + " "
				+ " AND u.idrin_ma_delegation_userid=" + userDelegationMasterVo.getDelegationUserId() ;
				

		getEntityManager().createNativeQuery(updateQuery).executeUpdate();

	}

	public List<Object[]> search(UserDelegationMasterVO userDelegationMasterVo,AuthDetailsVo authDetailsVo) {

		String query =  "SELECT  m.FIRST_NAME,user1.FIRST_NAME AS delgatedUser,ud.idrin_ma_user_active_from,ud.idrin_ma_user_active_to,ud.idrin_ma_user_type "
				+ " , ud.delegation_remarks "
				+ " FROM rin_ma_user_delegation u  LEFT JOIN rin_ma_user_delegation_details ud  "
				+ " ON ud.idrin_ma_user_delegation_id = u.idrin_ma_user_delegation_id "
				+ " LEFT JOIN "+ getCommonDatabaseSchema() +".user m "
				+ " ON m.USER_ID = u.idrin_ma_delegation_userid LEFT JOIN "+ getCommonDatabaseSchema() +".user USER1 "
				+ " ON user1.USER_ID = ud.idrin_ma_delegated_user_id "
				+ " WHERE ud.delete_flag = 0  and  u.idrin_ma_delegation_userid = "+userDelegationMasterVo.getDelegationUserId()
				+ " and u.rin_ma_entity_id = " + authDetailsVo.getEntityId();
							

		StringBuffer modifiedQuery = new StringBuffer(query);

		if (userDelegationMasterVo.getUserType()!=null){
			modifiedQuery.append(" and ud.idrin_ma_user_type= " + userDelegationMasterVo.getUserType());
		}

		@SuppressWarnings("unchecked")
		List<Object[]> userDelegationMasterEntity = (List<Object[]>) getEntityManager()
				.createNativeQuery(modifiedQuery.toString()).getResultList();
		return userDelegationMasterEntity;

	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getAllUsers(UserDelegationMasterVO userDelegationMasterVo,AuthDetailsVo authDetailsVo) {
		String query = " SELECT user.first_name,COUNT(ud.idrin_ma_user_delegation_detail_id),"
				+ " ud.idrin_ma_user_delegation_id, u.idrin_ma_delegation_userid,u.idrin_ma_user_delegation_active"
				+ " , ud.delegation_remarks "
				+ " FROM rin_ma_user_delegation u LEFT JOIN rin_ma_user_delegation_details ud ON "
				+ " ud.idrin_ma_user_delegation_id = u.idrin_ma_user_delegation_id "
				+ " LEFT JOIN "+ getCommonDatabaseSchema() +".user ON "
				+ " u.idrin_ma_delegation_userid = user.user_id "
				+ " WHERE u.delete_flag =0 " + " and u.rin_ma_entity_id = " + authDetailsVo.getEntityId();
				 
		if (!authDetailsVo.getUserId().equals(CommonConstant.SUPER_ADMIN_ID)) {
			query = query + " and u.idrin_ma_delegation_userid = " + authDetailsVo.getUserId();

		}
		
		query = query + " GROUP BY u.idrin_ma_user_delegation_id  " ;
		
		List<Object[]> resultobject = (List<Object[]>) getEntityManager().createNativeQuery(query).getResultList();
		return resultobject;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getUserSearch(UserDelegationMasterVO userDelegationMasterVo,AuthDetailsVo authDetailsVo) {
		String query = " SELECT user.first_name,COUNT(ud.idrin_ma_user_delegation_detail_id)"
				+ " FROM rin_ma_user_delegation u LEFT JOIN rin_ma_user_delegation_details ud ON "
				+ " ud.idrin_ma_user_delegation_id = u.idrin_ma_user_delegation_id "
				+ " LEFT JOIN "+ getCommonDatabaseSchema() +".user ON "
				+ " u.idrin_ma_delegation_userid = user.user_id"
				+ " WHERE u.delete_flag =0 " + " and u.rin_ma_entity_id = " + authDetailsVo.getEntityId();
		
		
		StringBuffer modifiedQuery = new StringBuffer(query);
		
		if (userDelegationMasterVo.getUserName() != null)
			modifiedQuery.append(" and user.first_name  LIKE LOWER('%" +userDelegationMasterVo.getUserName() + "%') ");
		
		modifiedQuery.append(" GROUP BY u.idrin_ma_user_delegation_id ");

				

		List<Object[]> resultobject = (List<Object[]>) getEntityManager().createNativeQuery(modifiedQuery.toString()).getResultList();

		return resultobject;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> update(UserDelegationMasterVO userDelegationMasterVo) {
		
		String query =  "SELECT  ud.idrin_ma_user_delegation_detail_id,ud.idrin_ma_user_delegation_id,ud.idrin_ma_delegated_user_id,u.idrin_ma_user_delegation_active,ud.idrin_ma_user_active_from,ud.idrin_ma_user_active_to"
				+ "  FROM rin_ma_user_delegation u "
				+ " LEFT JOIN rin_ma_user_delegation_details ud ON "
				+ " ud.idrin_ma_user_delegation_id = u.idrin_ma_user_delegation_id  "
				+ " LEFT JOIN "+getCommonDatabaseSchema()+".USER   USER ON  u.idrin_ma_delegation_userid = user.user_id"
				+ " WHERE u.delete_flag =0  AND u.idrin_ma_delegation_userid="+userDelegationMasterVo.getDelegationUserId()+" "
				+ " AND  ud.idrin_ma_delegated_user_id = "+userDelegationMasterVo.getUserDelegationDetailsVo().getDelegatedUserId()+" ";
		
		
		List<Object[]> resultobject = (List<Object[]>) getEntityManager().createNativeQuery(query.toString()).getResultList();

		return resultobject;
		
	}

	public void updateUser(UserDelegationDetailsEntity userDelegationDetailsEntity) {
		
		getEntityManager().persist(userDelegationDetailsEntity);

	}

	@SuppressWarnings("unchecked")
	public List<Object[]> updateOldUser(UserDelegationMasterVO userDelegationMasterVo) {
		
		
		String query =  "SELECT  ud.idrin_ma_user_delegation_detail_id,ud.idrin_ma_user_delegation_id,"
				+ " ud.idrin_ma_delegated_user_id,u.idrin_ma_user_delegation_active,ud.idrin_ma_user_active_from,"
				+ " ud.idrin_ma_user_active_to FROM rin_ma_user_delegation u LEFT JOIN rin_ma_user_delegation_details ud ON "
				+ " ud.idrin_ma_user_delegation_id = u.idrin_ma_user_delegation_id  "
				+ " LEFT JOIN "+getCommonDatabaseSchema()+".USER   USER ON  u.idrin_ma_delegation_userid = user.user_id"
				+ " WHERE u.delete_flag = '0' "
				+ " AND ud.idrin_ma_user_delegation_detail_id = "+userDelegationMasterVo.getUserDelegationDetailsVo().getDelegationDetailId()+ " ";
		
		
		List<Object[]> resultobject = (List<Object[]>) getEntityManager().createNativeQuery(query.toString()).getResultList();

		return resultobject;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> singleView(UserDelegationMasterVO userDelegationMasterVo) {
		String query =  "SELECT  ud.idrin_ma_user_delegation_detail_id,ud.idrin_ma_user_delegation_id,ud.idrin_ma_delegated_user_id,"
				+ " ud.idrin_ma_user_delegation_active,ud.idrin_ma_user_active_from,ud.idrin_ma_user_active_to, ud.idrin_ma_user_type"
				+ " ,ud.delegation_remarks FROM rin_ma_user_delegation u "
				+ " LEFT JOIN rin_ma_user_delegation_details ud ON "
				+ " ud.idrin_ma_user_delegation_id = u.idrin_ma_user_delegation_id  "
				+ " LEFT JOIN "+getCommonDatabaseSchema()+".user  user1 ON  u.idrin_ma_delegation_userid = user1.user_id"
				+ " WHERE u.delete_flag =0  AND ud.idrin_ma_user_delegation_detail_id = "+userDelegationMasterVo.getUserDelegationDetailsVo().getDelegationDetailId()+ " ";
		
		
		List<Object[]> resultobject = (List<Object[]>) getEntityManager().createNativeQuery(query.toString()).getResultList();

		return resultobject;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> view(UserDelegationMasterVO userDelegationMasterVo) {
		String query =  "SELECT user.first_name, user1.first_name as nm,ud.idrin_ma_user_delegation_active,"
				+ " ud.idrin_ma_user_type,ud.idrin_ma_user_active_from,ud.idrin_ma_user_active_to "
				+ " FROM rin_ma_user_delegation u LEFT JOIN rin_ma_user_delegation_details ud ON  ud.idrin_ma_user_delegation_id = u.idrin_ma_user_delegation_id "
				+ " LEFT JOIN "+getCommonDatabaseSchema()+".user ON  u.idrin_ma_delegation_userid = user.user_id "
				+ " LEFT JOIN "+getCommonDatabaseSchema()+".user  user1 ON  ud.idrin_ma_delegated_user_id = user1.user_id "
				+ " WHERE u.delete_flag =0  "
				+ " AND u.idrin_ma_delegation_userid = "+userDelegationMasterVo.getDelegationUserId()+" ";
		List<Object[]> resultobject = (List<Object[]>) getEntityManager().createNativeQuery(query.toString()).getResultList();

		return resultobject;
	}

	public UserDelegationDetailsEntity getDetail(UserDelegationMasterVO userDelegationMasterVo) {

		String query = " select u from  UserDelegationDetailsEntity u where u. delegationDetailId = "+userDelegationMasterVo.getUserDelegationDetailsVo().getDelegationDetailId()+" ";
		UserDelegationDetailsEntity userDelegationDetailsEntity = (UserDelegationDetailsEntity) getEntityManager().createQuery(query).getSingleResult();

		return userDelegationDetailsEntity;
	}
	@SuppressWarnings("unchecked")
	public int findDuplicateUpdate(UserDelegationMasterVO userDelegationMasterVO, AuthDetailsVo authDetailsVo,
			UserDelegationDetailsVO delegationMasterVo)throws CommonException, ParseException {
		 String query;		 		 
		
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				
		 String fromDate = "";
		 fromDate =  (formatter.format(delegationMasterVo.getUserActiveFrom())); 
		 
		 String toDate = "";
		 toDate =  (formatter.format(delegationMasterVo.getUserActiveTo())); 
		 
		
		query = "SELECT r.idrin_ma_user_delegation_id,r.idrin_ma_user_delegation_active FROM " + getRtaDatabaseSchema()
				+ ".rin_ma_user_delegation r  " + " LEFT JOIN "+getRtaDatabaseSchema()+".rin_ma_user_delegation_details ud "
				+ " ON ud.idrin_ma_user_delegation_id = r.idrin_ma_user_delegation_id " + " where r.rin_ma_entity_id = '"
				+ authDetailsVo.getEntityId() + "'  and  r.delete_flag = " + CommonConstant.FLAG_ZERO
				+ " and ud.delete_flag = " + CommonConstant.FLAG_ZERO;
				 		
		if (null != authDetailsVo.getUserId()) {
			query = query + " AND r.idrin_ma_delegation_userid = " + authDetailsVo.getUserId();
		}
		if(null != delegationMasterVo.getDelegationRemarks() && !delegationMasterVo.getDelegationRemarks().isEmpty()){
			query = query + " AND ud.delegation_remarks = '" + delegationMasterVo.getDelegationRemarks()+"'";
		}
		int count = 0;

		if (null != delegationMasterVo.getDelegatedUserId()) {
			query = query + " AND ud. idrin_ma_delegated_user_id = " + delegationMasterVo.getDelegatedUserId();
		}
		if (null != delegationMasterVo.getUserType()) {
			query = query + " AND ud. idrin_ma_user_type = " + delegationMasterVo.getUserType();
		}

		if (null != delegationMasterVo.getUserActiveFrom() && null != delegationMasterVo.getUserActiveTo() ) {
			query = query + " AND ud.idrin_ma_user_active_from = '"+fromDate+"'"   
			  + " AND ud.idrin_ma_user_active_to  = '"+toDate+"'"   ;
		}
		
		if (delegationMasterVo.isDelegatedUserActive()) {
			query = query + " AND ud.idrin_ma_user_delegation_active = " +  1;
		}else{
			query = query + " AND ud.idrin_ma_user_delegation_active = " +  0;
		}
		
		try{
		List<Object[]>  list  = (List<Object[]>) getEntityManager().createNativeQuery(query).getResultList();
		if(null != list && !list.isEmpty()){
			count = list.size();
		}
		}catch(Exception e){
			
		}
		return count;

	}
	@SuppressWarnings("unchecked")
	public int findDuplicate(UserDelegationMasterVO userDelegationMasterVO, AuthDetailsVo authDetailsVo,
			UserDelegationDetailsVO delegationMasterVo) {
		String query;

		query = "SELECT COUNT(r.idrin_ma_user_delegation_id) FROM " + getRtaDatabaseSchema()
				+ ".rin_ma_user_delegation r  " + " LEFT JOIN "+getRtaDatabaseSchema()+".rin_ma_user_delegation_details ud "
				+ " ON ud.idrin_ma_user_delegation_id = r.idrin_ma_user_delegation_id " + " where r.rin_ma_entity_id = '"
				+ authDetailsVo.getEntityId() + "'  and  r.delete_flag = " + CommonConstant.FLAG_ZERO
				+ " and ud.delete_flag = " + CommonConstant.FLAG_ZERO;
		if (null != authDetailsVo.getUserId()) {
			query = query + " AND r.idrin_ma_delegation_userid = " + authDetailsVo.getUserId();
		}
		int count = 0;

		if (null != delegationMasterVo.getDelegatedUserId()) {
			query = query + " AND ud. idrin_ma_delegated_user_id = " + delegationMasterVo.getDelegatedUserId();
		}
		if (null != delegationMasterVo.getUserType()) {
			query = query + " AND ud. idrin_ma_user_type = " + delegationMasterVo.getUserType();
		}
		query = query + " GROUP BY r.idrin_ma_user_delegation_id ";
		try {
			List<Object[]> list = (List<Object[]>) getEntityManager().createNativeQuery(query).getResultList();
			if (null != list && !list.isEmpty()) {
				count = list.size();
			}
		} catch (Exception e) {

		}
		return count;

	}	
	
	public List<Object[]> getAllExpiredDelegatedList()  {
				 
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String toDate = new String("");
		toDate = formatter.format(CommonConstant.getCalenderDate());

		String query = " SELECT det.idrin_ma_user_delegation_detail_id , idrin_ma_user_delegation_active FROM " + getRtaDatabaseSchema()
				+ ".rin_ma_user_delegation_details det " + " WHERE  det.idrin_ma_user_delegation_active = 1 and "
				+ " det.idrin_ma_user_active_to < '" + toDate + "' ";

		@SuppressWarnings("unchecked")
		List<Object[]>  list= (List<Object[]>)getEntityManager().createNativeQuery(query).getResultList();
		
		return list;
	}
	
	public void updateInactive(int delegatedId) throws CommonException {

		String query = "update  " + getRtaDatabaseSchema()+ ".rin_ma_user_delegation_details  "
				+ " set  idrin_ma_user_delegation_active = 0 ,"
				+ " update_date = '"+ CommonConstant.getCurrentDateTimeAsString()+"' "		 
				+ " where idrin_ma_user_delegation_detail_id = " + delegatedId;

		getEntityManager().createNativeQuery(query.toString()).executeUpdate();
	}
}
