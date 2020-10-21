package com.srm.rta.controller;

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
import com.srm.rta.service.RequestService;
import com.srm.rta.service.SubRequestService;
import com.srm.rta.vo.RequestScreenDetailConfigurationVO;
import com.srm.rta.vo.RequestVO;
import com.srm.rta.vo.RequestWorkFlowAuditVO;

@RestController
public class SubRequestController extends CommonController<RequestWorkFlowAuditVO> {
	private static final Logger logger = LogManager.getLogger(SubRequestController.class);

	@Autowired
	SubRequestService subRequestService;
	
	@Autowired
	RequestService requestService;

	@PostMapping(FilePathConstants.RESOLVER_SUBREQUEST_CREATE)
	@ResponseBody
	public ResponseEntity<JSONResponse> subRequestUpdate(HttpServletRequest request,
			@RequestBody RequestWorkFlowAuditVO requestWorkFlowAuditVo) {
		JSONResponse jsonResponse = new JSONResponse();

		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			RequestVO requestVO = subRequestService.updatesubRequest(requestWorkFlowAuditVo, authDetailsVo);
		 
			addEntityRequest(authDetailsVo);
			
			String successMessage = requestVO.getRequestCode().concat(" "+ getMessage("redirectRequestSuccessMessage",authDetailsVo)) ;
			
			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(successMessage);
			jsonResponse.setSuccesObject(CommonConstant.NULL);
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(getMessage(e.getMessage(), authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			e.printStackTrace();
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("saveErroMessage", authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}
	
	
	@PostMapping(FilePathConstants.SUBREQUEST_SCREENDETAILS)
	@ResponseBody
	public ResponseEntity<JSONResponse> getScreenDetails(HttpServletRequest request,
			@RequestBody RequestVO requestVO) {
		JSONResponse jsonResponse = new JSONResponse();

		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		
		try {
			authDetailsVo = tokenValidate(accessToken); 
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
		
			List<RequestScreenDetailConfigurationVO>  requestScreenDetails = requestService.getAllScreenDetail(requestVO, authDetailsVo);
		
			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("saveSuccessMessage", authDetailsVo));
			jsonResponse.setSuccesObject(requestScreenDetails);
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(getMessage(e.getMessage(), authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			e.printStackTrace();
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("saveErroMessage", authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	
}
