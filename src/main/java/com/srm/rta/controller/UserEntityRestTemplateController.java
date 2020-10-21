package com.srm.rta.controller;

import java.io.IOException;

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
import com.srm.coreframework.vo.ScreenJsonVO;
import com.srm.coreframework.vo.UserEntityMappingVo;
import com.srm.rta.service.UserEntityRestTemplateService;

/**
 * @author priyankas
 *
 */
@RestController
public class UserEntityRestTemplateController extends CommonController<UserEntityMappingVo>{
	
	@Autowired
	UserEntityRestTemplateService userEntityRestTemplateService;
	
	private static final Logger logger = LogManager.getLogger(UserEntityRestTemplateController.class);

	/**
	 * @param request load entity
	 * @param screenJsonVO
	 * @return jsonResponse
	 */
	@PostMapping(FilePathConstants.ENTITY_DROP_DOWN)
	@ResponseBody
	public ResponseEntity<JSONResponse> loadEntity(HttpServletRequest request,@RequestBody ScreenJsonVO screenJsonVO) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);

		try {
			screenJsonVO.setAccessToken(accessToken);
			jsonResponse = 	userEntityRestTemplateService.loadEntity(screenJsonVO);
			
		} catch (CommonException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
		}

		return jsonResponse;
	}
	
	/**
	 * @param request load entity
	 * @param screenJsonVO
	 * @return jsonResponse
	 */
	@PostMapping(FilePathConstants.ENTITY_SAVE)
	@ResponseBody
	public ResponseEntity<JSONResponse> saveEntity(HttpServletRequest request,@RequestBody UserEntityMappingVo userEntityMappingVo) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);

		try {
			userEntityMappingVo.setAccessToken(accessToken);
			jsonResponse = 	userEntityRestTemplateService.saveEntity(userEntityMappingVo);
			
		} catch (CommonException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
		}

		return jsonResponse;
	}
	
	/**
	 * @param request load entity
	 * @param screenJsonVO
	 * @return jsonResponse
	 */
	@PostMapping(FilePathConstants.USER_ENTITY_DELETE)
	@ResponseBody
	public ResponseEntity<JSONResponse> deleteEntity(HttpServletRequest request,@RequestBody UserEntityMappingVo userEntityMappingVo) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);

		try {
			userEntityMappingVo.setAccessToken(accessToken);
			jsonResponse = 	userEntityRestTemplateService.deleteEntity(userEntityMappingVo);
			
		} catch (CommonException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
		}

		return jsonResponse;
	}
	
	/**
	 * @param request load entity
	 * @param screenJsonVO
	 * @return jsonResponse
	 */
	@PostMapping(FilePathConstants.ENTITY_LIST)
	@ResponseBody
	public ResponseEntity<JSONResponse> listEntity(HttpServletRequest request,@RequestBody ScreenJsonVO screenJson) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);

		try {
			screenJson.setAccessToken(accessToken);
			jsonResponse = 	userEntityRestTemplateService.listEntity(screenJson);
			
		} catch (CommonException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
		}

		return jsonResponse;
	}
	
	@PostMapping("/sr/check/usePswdExpirySchedular")
	public ResponseEntity<JSONResponse> userPasswordExpiry() throws IOException {

		JSONResponse jsonResponse = new JSONResponse();

		try {
			userEntityRestTemplateService.userPasswordExpiry();

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);

			jsonResponse.setResponseMessage("success");
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage("error");
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}
	
	
	@PostMapping("/sr/check/entityRenwalSchedular")
	public ResponseEntity<JSONResponse> entityRenewAlert() throws IOException {

		JSONResponse jsonResponse = new JSONResponse();

		try {
			userEntityRestTemplateService.entityRenewAlert();

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);

			jsonResponse.setResponseMessage("success");
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage("error");
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}
 
	
	
	@PostMapping("/sr/check/entityTrnSchedular")
	public ResponseEntity<JSONResponse> entityTransactionAlert() throws IOException {

		JSONResponse jsonResponse = new JSONResponse();

		try {
			userEntityRestTemplateService.entityTransactionAlert();

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);

			jsonResponse.setResponseMessage("success");
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage("error");
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}
	
}
