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
import com.srm.coreframework.constants.FilePathConstants;
import com.srm.coreframework.controller.CommonController;
import com.srm.coreframework.response.JSONResponse;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.vo.MailParameterVO;
import com.srm.rta.service.MailSenderService;

/**
 * This controller is used to get all the record of the mail parameter to send Mail.
 * mail.
 * 
 * @author vigneshs
 *
 */
@RestController
public class MailSenderController extends CommonController<MailParameterVO> {

	private static final Logger logger = LogManager.getLogger(MailSenderController.class);

	
	@Autowired
	MailSenderService mailSenderService;

	/**
	 * Method is used to Send mail all the Request Approval and resolver.
	 * 
	 * @return response Response
	 */
	@GetMapping(FilePathConstants.MAIL_PENDING_LIST)
	@ResponseBody
	public ResponseEntity<JSONResponse> getAllMailList() {
		
		JSONResponse jsonResponse = new JSONResponse();

		try {

			mailSenderService.getAllMailList();
			
			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			e.printStackTrace();
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}
	
	
	

}
