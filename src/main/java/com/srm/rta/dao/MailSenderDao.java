package com.srm.rta.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.srm.coreframework.dao.CommonDAO;
import com.srm.coreframework.util.CommonConstant;
import com.srm.rta.entity.MailParameterEntity;

/**
 * This service is used to get all the record of the mail parameter to send Mail
 * and Update the mail parameter.
 * 
 * @author vigneshs
 *
 */
@Component
public class MailSenderDao extends CommonDAO {

	Logger logger = LoggerFactory.getLogger(MailSenderDao.class);

	/**
	 * This method is used to get all the list .
	 * 
	 * @return mailParameterEntityList List<MailParameterEntity>
	 */
	@SuppressWarnings("unchecked")
	public List<MailParameterEntity> getAllMailList() {
		List<MailParameterEntity> mailParameterEntityList = null;
		try{
		String query = " SELECT m FROM MailParameterEntity m WHERE m.emailFlag = " + CommonConstant.CONSTANT_ZERO
				+ " AND m.escalationFlag = " + CommonConstant.CONSTANT_ZERO
				+ " AND m.deleteFlag = " + CommonConstant.CONSTANT_ZERO;

		mailParameterEntityList = (List<MailParameterEntity>) getEntityManager()
				.createQuery(query).getResultList();
		}catch(Exception e){
			e.printStackTrace();
			e.printStackTrace();
		}

		return mailParameterEntityList;

	}
	
	
	/**
	 * This method is used to get all the escalation list .
	 * 
	 * @return mailParameterEntityList List<MailParameterEntity>
	 */
	@SuppressWarnings("unchecked")
	public List<MailParameterEntity> getAllEscalationMailList() {

		String query = " SELECT m FROM MailParameterEntity m WHERE m.emailFlag = " + CommonConstant.CONSTANT_ZERO
				+ " AND m.escalationFlag = " + CommonConstant.CONSTANT_ONE
				+ " AND m.deleteFlag = " + CommonConstant.CONSTANT_ZERO;

		List<MailParameterEntity> mailParameterEntityList = (List<MailParameterEntity>) getEntityManager()
				.createQuery(query).getResultList();

		return mailParameterEntityList;

	}
	

	/**
	 * This method is used to update the email flag is 1
	 * 
	 * @param mailParameterEntity
	 *            MailParameterEntity
	 */
	public void updateEmailFlag(MailParameterEntity mailParameterEntity) {

		String query = "UPDATE MailParameterEntity SET emailFlag = " + CommonConstant.CONSTANT_ONE + ",updateBy = "
				+ CommonConstant.CONSTANT_ONE + " ,updateDate = ' " + CommonConstant.getCurrentDateTimeAsString() + "'"
				+ " WHERE  mailParameterId = " + mailParameterEntity.getMailParameterId();

		getEntityManager().createQuery(query).executeUpdate();

	}

	/**
	 * This method is used to update the email flag is 2
	 * 
	 * @param mailParameterEntity
	 *            MailParameterEntity
	 */
	public void updateEmailFlagSend(MailParameterEntity mailParameterEntity) {

		String query = "UPDATE MailParameterEntity SET emailFlag = " + CommonConstant.CONSTANT_TWO + ",updateBy = "
				+ CommonConstant.CONSTANT_ONE + " ,updateDate = ' " + CommonConstant.getCurrentDateTimeAsString() + "'"
				+ " WHERE  mailParameterId = " + mailParameterEntity.getMailParameterId();

		getEntityManager().createQuery(query).executeUpdate();

	}

}
