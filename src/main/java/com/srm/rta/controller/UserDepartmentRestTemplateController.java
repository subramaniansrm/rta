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
import com.srm.coreframework.vo.ScreenJsonVO;
import com.srm.coreframework.vo.UserDepartmentVO;
import com.srm.rta.service.UserDepartmentRestTemplateService;

/**
 * @author priyankas
 *
 */
@RestController
public class UserDepartmentRestTemplateController extends CommonController<T>{

	private static final Logger logger = LogManager.getLogger(UserDepartmentRestTemplateController.class);

	@Autowired
	private UserDepartmentRestTemplateService userDepartmentRestTemplateService;
	
	/*
	 * Method to Department list
	 * @param screenJson
	 * @return jsonResponse
	 */
	@PostMapping(FilePathConstants.LOAD_DEPARTMENT)
	@ResponseBody
	public ResponseEntity<JSONResponse> getAll(HttpServletRequest request,@RequestBody ScreenJsonVO screenJson) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			screenJson.setAccessToken(accessToken);
			jsonResponse = 	userDepartmentRestTemplateService.getAll(screenJson);
			
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
	 * Method to search Department 
	 * @param screenJson
	 * @return jsonResponse
	 */
	@PostMapping(FilePathConstants.SEARCH_DEPARTMENT)
	@ResponseBody
	public ResponseEntity<JSONResponse> searchUserDepartment(HttpServletRequest request,@RequestBody UserDepartmentVO userDepartmentVO) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			userDepartmentVO.setAccessToken(accessToken);
			jsonResponse = 	userDepartmentRestTemplateService.searchUserDepartment(userDepartmentVO);
			
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
	 * Method to save Department 
	 * @param screenJson
	 * @return jsonResponse
	 */
	@PostMapping(FilePathConstants.ADD_DEPARTMENT)
	@ResponseBody
	public ResponseEntity<JSONResponse> createUserDepartment(HttpServletRequest request,@RequestBody UserDepartmentVO userDepartmentVO) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			userDepartmentVO.setAccessToken(accessToken);
			jsonResponse = 	userDepartmentRestTemplateService.createUserDepartment( userDepartmentVO);
			
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
	 * Method to update Department 
	 * @param screenJson
	 * @return jsonResponse
	 */
	@PostMapping(FilePathConstants.UPDATE_DEPARTMENT)
	@ResponseBody
	public ResponseEntity<JSONResponse> updateUserDepartment(HttpServletRequest request,@RequestBody UserDepartmentVO userDepartmentVO) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			userDepartmentVO.setAccessToken(accessToken);
			jsonResponse = 	userDepartmentRestTemplateService.updateUserDepartment(userDepartmentVO);
			
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
	 * Method to delete Department 
	 * @param screenJson
	 * @return jsonResponse
	 */
	@PostMapping(FilePathConstants.DELETE_DEPARTMENT)
	@ResponseBody
	public ResponseEntity<JSONResponse> deleteUserDepartment(HttpServletRequest request,@RequestBody UserDepartmentVO userDepartmentVO) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			userDepartmentVO.setAccessToken(accessToken);
			jsonResponse = 	userDepartmentRestTemplateService.deleteUserDepartment(userDepartmentVO);
			
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
	 * Method to view Department 
	 * @param screenJson
	 * @return jsonResponse
	 */
	@PostMapping(FilePathConstants.VIEW_DEPARTMENT)
	@ResponseBody
	public ResponseEntity<JSONResponse> viewUserDepartment(HttpServletRequest request,@RequestBody UserDepartmentVO userDepartmentVO) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			userDepartmentVO.setAccessToken(accessToken);
			jsonResponse = 	userDepartmentRestTemplateService.viewUserDepartment(userDepartmentVO);
			
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
	 * Method to load add Department 
	 * @param screenJson
	 * @return jsonResponse
	 */
	@PostMapping(FilePathConstants.DEPARTMENT_AUTH_ADD)
	@ResponseBody
	public ResponseEntity<JSONResponse> addUserDepartment(HttpServletRequest request,@RequestBody UserDepartmentVO userDepartmentVO) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			userDepartmentVO.setAccessToken(accessToken);
			jsonResponse = 	userDepartmentRestTemplateService.addUserDepartment(userDepartmentVO);
			
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
