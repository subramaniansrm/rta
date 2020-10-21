package com.srm.rta.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.constants.FilePathConstants;
import com.srm.coreframework.controller.CommonController;
import com.srm.coreframework.response.JSONResponse;
import com.srm.coreframework.vo.ScreenAuthenticationVO;
import com.srm.coreframework.vo.ScreenJsonVO;
import com.srm.rta.service.ScreenAssignmentRestTemplateService;

@RestController
public class ScreenAssignmentRestTemplateController extends CommonController<T>{

	Logger logger = LoggerFactory.getLogger(ScreenAssignmentRestTemplateController.class);
	
	@Autowired
	private ScreenAssignmentRestTemplateService screenAssignmentRestTemplateService;
	
	
	/*
	 * Method to Role list
	 * @param screenJson
	 * @return jsonResponse
	 */
	@PostMapping(FilePathConstants.AUTHENTICATION_LIST)
	@ResponseBody
	public ResponseEntity<JSONResponse> listRoles(HttpServletRequest request,@RequestBody ScreenJsonVO screenJson) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			screenJson.setAccessToken(accessToken);
			jsonResponse = 	screenAssignmentRestTemplateService.listRoles(screenJson);
			
		} catch (CommonException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
		}
		return jsonResponse;
	}
	
	/*
	 * Method to get Search list
	 * @param screenJson
	 * @return jsonResponse
	 */
	@PostMapping(FilePathConstants.AUTHENTICATION_SEARCH)
	@ResponseBody
	public ResponseEntity<JSONResponse> searchScreenAssignmentList(HttpServletRequest request,@RequestBody ScreenAuthenticationVO screenAuthorizationMaster) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			screenAuthorizationMaster.setAccessToken(accessToken);
			jsonResponse = 	screenAssignmentRestTemplateService.searchScreenAssignmentList(screenAuthorizationMaster);
			
		} catch (CommonException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
		}
		return jsonResponse;
	}
	
	/*
	 * Method to load rights based on role
	 * @param screenJson
	 * @return jsonResponse
	 */
	@PostMapping(FilePathConstants.AUTHENTICATION_ADD)
	@ResponseBody
	public ResponseEntity<JSONResponse> load(HttpServletRequest request,@RequestBody ScreenAuthenticationVO screenAuthenticationMstr) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			screenAuthenticationMstr.setAccessToken(accessToken);
			jsonResponse = 	screenAssignmentRestTemplateService.load(screenAuthenticationMstr);
			
		} catch (CommonException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
		}
		return jsonResponse;
	}
	
	/*
	 * Method to load sub screen list 
	 * @param screenJson
	 * @return jsonResponse
	 */
	@PostMapping(FilePathConstants.AUTHENTICATION_SUB_SCREEN)
	@ResponseBody
	public ResponseEntity<JSONResponse> loadRoleBasedSubScreenList(HttpServletRequest request,@RequestBody ScreenAuthenticationVO screenAuthenticationMaster) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			screenAuthenticationMaster.setAccessToken(accessToken);
			jsonResponse = 	screenAssignmentRestTemplateService.loadRoleBasedSubScreenList(screenAuthenticationMaster);
			
		} catch (CommonException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
		}
		return jsonResponse;
	}
	
	/*
	 * Method to load sub screen list 
	 * @param screenJson
	 * @return jsonResponse
	 */
	@PostMapping(FilePathConstants.AUTHENTICATION_FIELD)
	@ResponseBody
	public ResponseEntity<JSONResponse> loadRoleBasedScreenFieldsList(HttpServletRequest request,
			@RequestBody ScreenAuthenticationVO screenAuthenticationMaster) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			screenAuthenticationMaster.setAccessToken(accessToken);
			jsonResponse = 	screenAssignmentRestTemplateService.loadRoleBasedScreenFieldsList(screenAuthenticationMaster);
			
		} catch (CommonException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
		}
		return jsonResponse;
	}
	
	/*
	 * Method to load sub screen list 
	 * @param screenJson
	 * @return jsonResponse
	 */
	@PostMapping(FilePathConstants.AUTHENTICATION_FUNCTION)
	@ResponseBody
	public ResponseEntity<JSONResponse> loadRoleBasedFunctionList(HttpServletRequest request,
			@RequestBody ScreenAuthenticationVO screenAuthenticationMaster) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			screenAuthenticationMaster.setAccessToken(accessToken);
			jsonResponse = 	screenAssignmentRestTemplateService.loadRoleBasedFunctionList(screenAuthenticationMaster);
			
		} catch (CommonException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
		}
		return jsonResponse;
	}
	/*
	 * Method to save authentication  
	 * @param screenJson
	 * @return jsonResponse
	 */
	@PostMapping(FilePathConstants.AUTHENTICATION_CREATE)
	@ResponseBody
	public ResponseEntity<JSONResponse> saveScreenAssignment(HttpServletRequest request,@RequestBody ScreenAuthenticationVO screenAuthenticationMaster) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			screenAuthenticationMaster.setAccessToken(accessToken);
			jsonResponse = 	screenAssignmentRestTemplateService.saveScreenAssignment(screenAuthenticationMaster);
			
		} catch (CommonException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
		}
		return jsonResponse;
	}
	/*
	 * Method to delete authentication  
	 * @param screenJson
	 * @return jsonResponse
	 */
	@PostMapping(FilePathConstants.AUTHENTICATION_DELETE)
	@ResponseBody
	public ResponseEntity<JSONResponse> deleteScreenAssignment(HttpServletRequest request,@RequestBody ScreenAuthenticationVO screenAuthenticationMaster) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			screenAuthenticationMaster.setAccessToken(accessToken);
			jsonResponse = 	screenAssignmentRestTemplateService.deleteScreenAssignment(screenAuthenticationMaster);
			
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
