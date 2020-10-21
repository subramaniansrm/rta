package com.srm.rta.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.srm.coreframework.dao.CommonDAO;
import com.srm.coreframework.entity.CodeGenerationEntity;
import com.srm.coreframework.entity.EntityLicense;
import com.srm.coreframework.entity.UserEntity;
import com.srm.coreframework.util.CommonConstant;
import com.srm.rta.entity.MailParameterEntity;
import com.srm.rta.entity.MaillogEntity;
import com.srm.rta.vo.MailOutVo;

/**
 * This dao is used to get the details to send the mail
 * 
 * @author vigneshs
 *
 */
@Repository
public class MailControllerDao extends CommonDAO {

	/**
	 * This method is used to get the generate the mail details.
	 * 
	 * @param mailParameterEntity
	 *            MailParameterEntity
	 * @return result List<Object>
	 */
	@SuppressWarnings("unchecked")
	public List<Object> generateMail(MailParameterEntity mailParameterEntity) {

		List<Object> result = new ArrayList<Object>();

		if (null != mailParameterEntity.getMessageCode()) {

			String mailQry = " SELECT mm.message, mm.title, mg.mail_type, mg.email_address FROM `mail_message` mm INNER JOIN `mail_group` mg ON mm.group_id= mg.group_id "
					+ " where mm.message_code = '" + mailParameterEntity.getMessageCode() + "'";

			result = (List<Object>) getEntityManager().createNativeQuery(mailQry).getResultList();
		}

		return result;
	}

	/**
	 * This method is used to get the user details based on user id.
	 * 
	 * @param user
	 *            int
	 * @return
	 */
	public UserEntity getUserEmailId(int user) {

		String query = "SELECT u.* FROM "+getCommonDatabaseSchema()+".user u where u.USER_ID ="+user;
		
		Object[] qry = (Object[])getEntityManager().createNativeQuery(query).getSingleResult();
		
		UserEntity userEntity = new UserEntity();
		
		if(null != ((Object[]) qry)[0]){
			userEntity.setId((Integer)((Object[]) qry)[0]);
		}
		if(null != ((Object[]) qry)[1]){
			userEntity.setUserName((String)((Object[]) qry)[1]);			
		}
		if(null != ((Object[]) qry)[2]){
			userEntity.setUserEmployeeId((String)((Object[]) qry)[2]);			
		}
		if(null != ((Object[]) qry)[3]){
			userEntity.setFirstName((String)((Object[]) qry)[3]);			
		}
		if(null != ((Object[]) qry)[4]){
			userEntity.setMiddleName((String)((Object[]) qry)[4]);			
		}
		if(null != ((Object[]) qry)[5]){
			userEntity.setLastName((String)((Object[]) qry)[5]);			
		}
		if(null != ((Object[]) qry)[13]){
			userEntity.setEmailId((String)((Object[]) qry)[13]);		
		}
		if(null != ((Object[]) qry)[24]){
			
			EntityLicense entityLicenseEntity = new EntityLicense();
			
			entityLicenseEntity.setId((Integer)((Object[]) qry)[24]);
			
			userEntity.setEntityLicenseEntity(entityLicenseEntity);		
		}
		
		return userEntity;
	}

	/**
	 * This method is used to store the details of mail sending information.
	 * 
	 * @param mailOutVo
	 *            MailOutVo
	 */
	@Transactional
	public MaillogEntity createMailLog(MailOutVo mailOutVo,UserEntity userEntity) {

		MaillogEntity maillogEntity = new MaillogEntity();

		maillogEntity.setUserId(userEntity.getId());
		maillogEntity.setMessage(mailOutVo.getMessage());
		maillogEntity.setTitle(mailOutVo.getTitle());
		maillogEntity.setEntityLicenseId(userEntity.getEntityLicenseEntity().getId());

		String toEmail = null;
		String ccEmail = null;

		for (String mailId : mailOutVo.getToMailAddress()) {

			if (null != toEmail) {
				toEmail = toEmail + "," + mailId;
			} else {
				toEmail = mailId;
			}

		}
		/*
		 * for (String ccMailId : mailOutVo.getCcMailAddress()) {
		 * 
		 * if (null != ccEmail) { ccEmail = ccEmail + "," + ccMailId; } else {
		 * ccEmail = ccMailId; }
		 * 
		 * }
		 */
		maillogEntity.setCcEmailAddress(ccEmail);

		maillogEntity.setToEmailAddress(toEmail);
		maillogEntity.setMessageCode(mailOutVo.getMessageCode());
		maillogEntity.setRegisterDatetime(CommonConstant.getCalenderDate());
		maillogEntity.setLastUpdateDatetime(CommonConstant.getCalenderDate());
		maillogEntity.setCreateUserId(1);
		
		getEntityManager().persist(maillogEntity);
		
		return maillogEntity;
	}
	
	public void updateMailLogId(MailParameterEntity mailParameterEntity){
		
		String query = "UPDATE mail_parameter SET mail_log_id ="+ mailParameterEntity.getMailLogId()
		              +" WHERE mail_parameter_id ="+ mailParameterEntity.getMailParameterId();
		
		getEntityManager().createNativeQuery(query).executeUpdate();
	}

}
