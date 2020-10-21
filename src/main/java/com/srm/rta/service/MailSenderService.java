package com.srm.rta.service;

import java.util.List;

import org.omg.CORBA.portable.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.srm.coreframework.controller.CommonMessage;
import com.srm.rta.dao.MailSenderDao;
import com.srm.rta.entity.MailParameterEntity;

/**
 * This service is used to get all the record of the mail parameter to send
 * Mail.
 * 
 * @author vigneshs
 *
 */
@Service
public class MailSenderService extends CommonMessage {

	Logger logger = LoggerFactory.getLogger(MailSenderService.class);

	

	@Autowired
	MailSenderDao mailSenderDao;

	@Autowired
	MailControllerService mailControllerService;

	/**
	 * This method is used to get all record and send mail and update the mail
	 * parameter.
	 * 
	 * @throws Exception
	 */
	@Transactional
	public void getAllMailList() throws Exception {

		try {

			//Get All the List of the record.
			List<MailParameterEntity> mailParameterEntityList = mailSenderDao.getAllMailList();

			for (MailParameterEntity mailParameterEntity : mailParameterEntityList) {

				//Update the mail parameter to change the email flag is 1
				mailSenderDao.updateEmailFlag(mailParameterEntity);

				//send the mail
				mailControllerService.generateMail(mailParameterEntity);
				
				//Update the mail parameter to change the email flag is 2
				mailSenderDao.updateEmailFlagSend(mailParameterEntity);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			
		}

	}
	
	

}
