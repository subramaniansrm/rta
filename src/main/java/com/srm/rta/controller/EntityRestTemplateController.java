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
import com.srm.coreframework.vo.EntityLicenseVO;
import com.srm.coreframework.vo.ScreenJsonVO;
import com.srm.coreframework.vo.UserMasterVO;
import com.srm.rta.service.EntityRestTemplateService;

@RestController
public class EntityRestTemplateController extends CommonController<EntityLicenseVO> {

	private static final Logger logger = LogManager.getLogger(EntityRestTemplateController.class);
	

	@Autowired
	EntityRestTemplateService entityRestTemplateService;


	@PostMapping(FilePathConstants.ENTITY_LISTALL)
	@ResponseBody
	public ResponseEntity<JSONResponse> getAllList(HttpServletRequest request,@RequestBody ScreenJsonVO screenJson) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			screenJson.setAccessToken(accessToken);
			jsonResponse = entityRestTemplateService.getAll(screenJson);

		} catch (CommonException e) {
			logger.error("error", e);
		} catch (Exception e) {
			logger.error("error", e);
		}

		return jsonResponse;
	}	 	 

	@PostMapping(FilePathConstants.ENTITY_VIEW)
	@ResponseBody
	public ResponseEntity<JSONResponse> view(HttpServletRequest request,@RequestBody EntityLicenseVO entityLicenseVO) {

 		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			entityLicenseVO.setAccessToken(accessToken);
			jsonResponse = entityRestTemplateService.view(entityLicenseVO);

		} catch (CommonException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
		}

		return jsonResponse;
	}
	
	
	@PostMapping(FilePathConstants.ENTITY_ADD)
	@ResponseBody
	public ResponseEntity<JSONResponse> add(HttpServletRequest request,@RequestBody EntityLicenseVO entityLicenseVO) {

 		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			entityLicenseVO.setAccessToken(accessToken);
			jsonResponse = entityRestTemplateService.add(entityLicenseVO);

		} catch (CommonException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
		}

		return jsonResponse;
	}
	
	@PostMapping(FilePathConstants.ENTITY_SEARCH)
	@ResponseBody
	public ResponseEntity<JSONResponse> searchEntity(HttpServletRequest request,@RequestBody EntityLicenseVO entityLicenseVO) {

 		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			entityLicenseVO.setAccessToken(accessToken);
			jsonResponse = entityRestTemplateService.searchEntity(entityLicenseVO);

		} catch (CommonException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
		}

		return jsonResponse;
	}
	
	@PostMapping(FilePathConstants.ENTITY_MASTER_RENEWAL)
	@ResponseBody
	public ResponseEntity<JSONResponse> renewalEntity(HttpServletRequest request,@RequestBody EntityLicenseVO entityLicenseVO) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			entityLicenseVO.setAccessToken(accessToken);
			jsonResponse = entityRestTemplateService.renewalEntity(entityLicenseVO);

		} catch (CommonException e) {
			logger.error("error", e);
		} catch (Exception e) {
			logger.error("error", e);
		}

		return jsonResponse;
	}	 
	@PostMapping(FilePathConstants.ENTITY_MASTER_UPDATE)
	@ResponseBody
	public ResponseEntity<JSONResponse> update(HttpServletRequest request,@RequestBody EntityLicenseVO entityLicenseVO) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			entityLicenseVO.setAccessToken(accessToken);
			jsonResponse = entityRestTemplateService.update(entityLicenseVO);

		} catch (CommonException e) {
			logger.error("error", e);
		} catch (Exception e) {
			logger.error("error", e);
		}

		return jsonResponse;
	}	 
		
	@PostMapping(FilePathConstants.ENTITY_LISTALLENTITY)
	@ResponseBody
	public ResponseEntity<JSONResponse> getAllEntity(HttpServletRequest request,@RequestBody ScreenJsonVO screenJson) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			screenJson.setAccessToken(accessToken);
			jsonResponse = entityRestTemplateService.getAllEntity(screenJson);

		} catch (CommonException e) {
			logger.error("error", e);
		} catch (Exception e) {
			logger.error("error", e);
		}

		return jsonResponse;
	}	
	
	@PostMapping(FilePathConstants.ENTITY_PLANNING_MASTER_DRP)
	@ResponseBody
	public ResponseEntity<JSONResponse> entityPlanningDropDown(HttpServletRequest request,@RequestBody ScreenJsonVO screenJson) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			screenJson.setAccessToken(accessToken);
			jsonResponse = entityRestTemplateService.entityPlanningDropDown(screenJson);

		} catch (CommonException e) {
			logger.error("error", e);
		} catch (Exception e) {
			logger.error("error", e);
		}

		return jsonResponse;
	}	 
	

}
