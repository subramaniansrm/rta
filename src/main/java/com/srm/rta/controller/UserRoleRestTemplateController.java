package com.srm.rta.controller;

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
import com.srm.coreframework.constants.FilePathConstants;
import com.srm.coreframework.controller.CommonController;
import com.srm.coreframework.response.JSONResponse;
import com.srm.coreframework.vo.ScreenJsonVO;
import com.srm.coreframework.vo.UserMasterVO;
import com.srm.coreframework.vo.UserRoleVO;
import com.srm.rta.service.UserRoleRestTemplateService;

@RestController
public class UserRoleRestTemplateController extends CommonController<UserMasterVO> {

	private static final Logger logger = LogManager.getLogger(UserRoleRestTemplateController.class);
	
	@Autowired
	UserRoleRestTemplateService userRoleRestTemplateService;
	
	/*
	 * Method to list User Role
	 * @param screenJson
	 * @return jsonResponse
	 */
	@PostMapping(FilePathConstants.GET_USER_ROLE_LIST)
	@ResponseBody
	public ResponseEntity<JSONResponse> listUserRole(HttpServletRequest request,@RequestBody ScreenJsonVO screenJson) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			screenJson.setAccessToken(accessToken);
			jsonResponse = 	userRoleRestTemplateService.listUserRole(screenJson);
			
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
	 * Method to load add Role
	 * @param screenJson
	 * @return jsonResponse
	 */
	@PostMapping(FilePathConstants.GET_USER_ROLE_ADD)
	@ResponseBody
	public ResponseEntity<JSONResponse> addScreenFields(HttpServletRequest request,@RequestBody ScreenJsonVO screenJson) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			screenJson.setAccessToken(accessToken);
			jsonResponse = 	userRoleRestTemplateService.addScreenFields(screenJson);
			
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
	 * Method to save Role
	 * @param screenJson
	 * @return jsonResponse
	 */
	@PostMapping(FilePathConstants.SAVE_USER_ROLE)
	@ResponseBody
	public ResponseEntity<JSONResponse> saveUserRole(HttpServletRequest request,@RequestBody UserRoleVO userRoleVO) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			userRoleVO.setAccessToken(accessToken);
			jsonResponse = 	userRoleRestTemplateService.saveUserRole(userRoleVO);
			
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
	 * Method to update Role
	 * @param screenJson
	 * @return jsonResponse
	 */
	@PostMapping(FilePathConstants.UPDATE_USER_ROLE)
	@ResponseBody
	public ResponseEntity<JSONResponse> updateUserRole(HttpServletRequest request,@RequestBody UserRoleVO userRoleVO) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			userRoleVO.setAccessToken(accessToken);
			jsonResponse = 	userRoleRestTemplateService.updateUserRole(userRoleVO);
			
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
	 * Method to delete Role
	 * @param screenJson
	 * @return jsonResponse
	 */
	@PostMapping(FilePathConstants.DELETE_USER_ROLE)
	public ResponseEntity<JSONResponse> deleteUserRole(HttpServletRequest request,@RequestBody UserRoleVO userRoleVO) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		
		String accessToken = getHeaderAccessToken(request);
		try {
			userRoleVO.setAccessToken(accessToken);

			jsonResponse = 	userRoleRestTemplateService.deleteUserRole(userRoleVO);
			
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
	 * Method to search Role
	 * @param screenJson
	 * @return jsonResponse
	 */
	@PostMapping(FilePathConstants.SEARCH_USER_ROLE)
	@ResponseBody
	public ResponseEntity<JSONResponse> search(HttpServletRequest request,@RequestBody UserRoleVO userRoleVO) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		
		String accessToken = getHeaderAccessToken(request);
		try {
			userRoleVO.setAccessToken(accessToken);

			jsonResponse = 	userRoleRestTemplateService.search( userRoleVO);
			
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
	 * Method to view Role
	 * @param screenJson
	 * @return jsonResponse
	 */
	@PostMapping(FilePathConstants.VIEW_USER_ROLE)
	@ResponseBody
	public ResponseEntity<JSONResponse> viewUserRole(HttpServletRequest request,@RequestBody UserRoleVO userRoleVO) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		
		String accessToken = getHeaderAccessToken(request);
		try {
			userRoleVO.setAccessToken(accessToken);

			jsonResponse = 	userRoleRestTemplateService.viewUserRole(userRoleVO);
			
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
	 * Method to view Role
	 * @param screenJson
	 * @return jsonResponse
	 */
	@PostMapping(FilePathConstants.COPY_USER_ROLE)
	@ResponseBody
	public ResponseEntity<JSONResponse> copyUserRole(HttpServletRequest request,@RequestBody ScreenJsonVO screenJson) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		
		String accessToken = getHeaderAccessToken(request);
		try {
			screenJson.setAccessToken(accessToken);

			jsonResponse = 	userRoleRestTemplateService.copyUserRole(screenJson);
			
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
