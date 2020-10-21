package com.srm.rta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.srm.coreframework.controller.EntityPlanningController;
import com.srm.rta.controller.EntitySelectionController;
import com.srm.rta.controller.EscalationController;
import com.srm.rta.controller.UserDelegationController;
import com.srm.rta.controller.UserEntityRestTemplateController;

@Component
public class AutoProcessImport {


/*	@Autowired
	private MailSenderController mailSenderController;*/
	
	@Autowired
	EntitySelectionController entitySelectionController;
	
	@Autowired
	UserDelegationController userDelegationController;
	
	@Autowired
	UserEntityRestTemplateController userEntityRestTemplateController;
	
	@Autowired
	EscalationController escalationController;
	
	@Autowired
	EntityPlanningController entityPlanningController;
	
	
	// @Scheduled(cron = "0 1 0 * * *")
	public void fixedDelaySch() throws Exception {

		/*
		 * if (mailMessages.getMailPendingList() == CommonConstant.CONSTANT_ONE)
		 * { mailSenderController.getAllMailList(); }
		 */

		// entity update
		entitySelectionController.updateExpiryEntity();

		// delegation update
		userDelegationController.updateInactive();

		//Plan update
		entityPlanningController.updatePlanExpiry();
				
		// Password Expiry
		userEntityRestTemplateController.userPasswordExpiry();

		// Renewal Expiry
		userEntityRestTemplateController.entityRenewAlert();

		// Renewal Expiry
		userEntityRestTemplateController.entityTransactionAlert();

	}
	
	//@Scheduled(cron = "0 1 0 * * *")
	public void escalationMail() throws Exception {

		escalationController.mailToApproval();
		 

	}

}