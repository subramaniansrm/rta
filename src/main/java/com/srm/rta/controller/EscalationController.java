package com.srm.rta.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.config.UserMessages;
import com.srm.coreframework.response.JSONResponse;
import com.srm.coreframework.util.CommonConstant;
import com.srm.rta.service.EscalationService;

 
/**
 * This Controller is used to get the escalation list of approval and resolver
 * and store the details in the mail parameter.
 * 
 * @author vigneshs
 *
 */
@RestController
public class EscalationController {

	private static final Logger logger = LogManager.getLogger(EscalationController.class);

	 

	@Autowired
	private UserMessages userMessages;

	@Autowired
	EscalationService escalationService;

	/**
	 * Method is used to Send mail all the Request Approval.
	 * 
	 * @return response Response
	 */
	//@GetMapping(FilePathConstants.ESCALATION_PENDING_LIST)
	@ResponseBody
	public  ResponseEntity<JSONResponse>  mailToApproval() {
		JSONResponse response = new JSONResponse();
		try {

			escalationService.getListForApproval();
			response.setResponseCode(CommonConstant.SUCCESS_CODE);
			response.setResponseMessage(userMessages.getSuccessMessage());
			response.setSuccesObject(CommonConstant.NULL);
		} catch (CommonException e) {
			logger.error(e.getMessage());
			response.setResponseCode(CommonConstant.FAILURE_CODE);
			response.setResponseMessage(e.getMessage());
			response.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			logger.error(e.getMessage());
			response.setResponseCode(CommonConstant.ERROR_CODE);
			response.setResponseMessage(userMessages.getErrorMessage());
			response.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	/**
	 * Method is used to Send mail all the Request Approval.
	 * 
	 * @return response Response
	 */
	//@GetMapping(FilePathConstants.ESCALATION_RESOLVER_LIST)
	@ResponseBody
	public  ResponseEntity<JSONResponse>  mailToResolver() {
		JSONResponse response = new JSONResponse();
		try {

			escalationService.getListForResolver();
			response.setResponseCode(CommonConstant.SUCCESS_CODE);
			response.setResponseMessage(userMessages.getSuccessMessage());
			response.setSuccesObject(CommonConstant.NULL);
		} catch (CommonException e) {
			logger.error(e.getMessage());
			response.setResponseCode(CommonConstant.FAILURE_CODE);
			response.setResponseMessage(e.getMessage());
			response.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			logger.error(e.getMessage());
			response.setResponseCode(CommonConstant.ERROR_CODE);
			response.setResponseMessage(userMessages.getErrorMessage());
			response.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

}
