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
import com.srm.coreframework.vo.UserMappingVO;
import com.srm.rta.service.UserMappingRestTemplateService;

/**
 * @author sai
 *
 */
@RestController
public class UserMappingRestTemplateController extends CommonController<T>{
	private static final Logger logger = LogManager.getLogger(UserMappingRestTemplateController.class);

	@Autowired
	private UserMappingRestTemplateService userMappingRestTemplateService;

	/*
	 * Method to User Mapping list
	 * 
	 * @param screenJson
	 * 
	 * @return jsonResponse
	 */
	@PostMapping(FilePathConstants.USER_MAPPING_LIST)
	@ResponseBody
	public ResponseEntity<JSONResponse> getAll(HttpServletRequest request,@RequestBody ScreenJsonVO screenJson) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			screenJson.setAccessToken(accessToken);
			jsonResponse = userMappingRestTemplateService.getAll(screenJson);

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
	 * 
	 * @param screenJson
	 * 
	 * @return jsonResponse
	 */
	@PostMapping(FilePathConstants.USER_MAPPING_ADD)
	@ResponseBody
	public ResponseEntity<JSONResponse> addUserMapping(HttpServletRequest request,@RequestBody UserMappingVO userMappingVO) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			userMappingVO.setAccessToken(accessToken);
			jsonResponse = userMappingRestTemplateService.addUserMapping(userMappingVO);

		} catch (CommonException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
		}

		return jsonResponse;
	}

	@PostMapping(FilePathConstants.USER_MAPPING_CREATE)
	@ResponseBody
	public ResponseEntity<JSONResponse> create(HttpServletRequest request,@RequestBody UserMappingVO userMappingVO) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			userMappingVO.setAccessToken(accessToken);
			jsonResponse = userMappingRestTemplateService.create(userMappingVO);

		} catch (CommonException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
		}

		return jsonResponse;
	}

	@PostMapping(FilePathConstants.USER_MAPPING_UPDATE)
	@ResponseBody
	public ResponseEntity<JSONResponse> update(HttpServletRequest request,@RequestBody UserMappingVO userMappingVO) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			userMappingVO.setAccessToken(accessToken);
			jsonResponse = userMappingRestTemplateService.update(userMappingVO);

		} catch (CommonException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
		}

		return jsonResponse;
	}

	@PostMapping(FilePathConstants.USER_MAPPING_DELETE)
	@ResponseBody
	public ResponseEntity<JSONResponse> delete(HttpServletRequest request,@RequestBody UserMappingVO userMappingVO) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			userMappingVO.setAccessToken(accessToken);
			jsonResponse = userMappingRestTemplateService.delete(userMappingVO);

		} catch (CommonException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
		}

		return jsonResponse;
	}

	@PostMapping(FilePathConstants.USER_MAPPING_LOAD)
	@ResponseBody
	public ResponseEntity<JSONResponse> load(HttpServletRequest request,@RequestBody UserMappingVO userMappingVO) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			userMappingVO.setAccessToken(accessToken);
			jsonResponse = userMappingRestTemplateService.load(userMappingVO);

		} catch (CommonException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
		}

		return jsonResponse;
	}

	@PostMapping(FilePathConstants.USER_MAPPING_SEARCH)
	@ResponseBody
	public ResponseEntity<JSONResponse> getAllSearch(HttpServletRequest request,@RequestBody UserMappingVO userMappingVO) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			userMappingVO.setAccessToken(accessToken);
			jsonResponse = userMappingRestTemplateService.getAllSearch(userMappingVO);

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
