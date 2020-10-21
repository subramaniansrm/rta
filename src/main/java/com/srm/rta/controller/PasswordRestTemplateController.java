package com.srm.rta.controller;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.constants.FilePathConstants;
import com.srm.coreframework.controller.CommonController;
import com.srm.coreframework.response.JSONResponse;
import com.srm.coreframework.vo.LoginForm;
import com.srm.rta.service.PasswordRestTemplateService;

/**
 * @author sai
 *
 */
@RestController
public class PasswordRestTemplateController extends CommonController<T>{
	private static final Logger logger = LogManager.getLogger(PasswordRestTemplateController.class);

	@Autowired
	private PasswordRestTemplateService passwordRestTemplateService;
	
	
	 /* Method to changePassword
	 * @param changePasswordRequest
	 * @return jsonResponse
	 */
	 
	@PostMapping(FilePathConstants.RTA_CHANGE_PASSWORD)
	@ResponseBody
	public ResponseEntity<JSONResponse> changePassword(HttpServletRequest request,@RequestBody LoginForm changePasswordRequest) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			changePasswordRequest.setAccessToken(accessToken);
			jsonResponse = 	passwordRestTemplateService.changePassword(changePasswordRequest);
			
		} catch (CommonException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
		}

		return jsonResponse;
	}
	
	
	
	/*  Method to forgotPassword 
	 * @param forgotPasswordRequest
	 * @return jsonResponse
	 
	@PostMapping(FilePathConstants.RTA_FORGOT_PASSWORD)
	@ResponseBody
	public ResponseEntity<JSONResponse> forgotPassword(HttpServletRequest request,@RequestBody LoginForm forgotPasswordRequest) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			forgotPasswordRequest.setAccessToken(accessToken);
			jsonResponse = 	passwordRestTemplateService.forgotPassword(forgotPasswordRequest);
			
		} catch (CommonException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
		}

		return jsonResponse;
	}*/
 }
