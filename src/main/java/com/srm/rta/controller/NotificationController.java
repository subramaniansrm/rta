package com.srm.rta.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.config.LoginAuthException;
import com.srm.coreframework.constants.FilePathConstants;
import com.srm.coreframework.controller.CommonController;
import com.srm.coreframework.response.JSONResponse;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.coreframework.vo.MailParameterVO;
import com.srm.rta.service.NotificationService;
import com.srm.rta.vo.EmailMessageVo;

@RestController
public class NotificationController extends CommonController<MailParameterVO> {
	
	private static final Logger logger = LogManager.getLogger(NotificationController.class);
	
	@Autowired
	NotificationService notificationService;

	@PostMapping(FilePathConstants.NOTIFICATION_LIST)
	@ResponseBody
	public ResponseEntity<JSONResponse> getAllNotification(HttpServletRequest request    ) 
			throws IOException {

		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				// get from message.properties
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			jsonResponse = notificationService.getAlllMailNotification( authDetailsVo);

		} catch (CommonException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
	  }			
		return jsonResponse;
	}
	
	@PostMapping(FilePathConstants.DELETE_NOTIFICATION)
	@ResponseBody
	public ResponseEntity<JSONResponse> deleteNotification(HttpServletRequest request,@RequestBody EmailMessageVo emailMessageVo)
			throws IOException {

		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				// get from message.properties
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			jsonResponse = notificationService.deleteNotification( emailMessageVo , authDetailsVo);
		} catch (CommonException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
	  }	
		return jsonResponse;
	}
	
}
