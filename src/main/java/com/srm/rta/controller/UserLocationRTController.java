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
import com.srm.coreframework.vo.UserLocationVO;
import com.srm.rta.service.UserLocationRTService;

@RestController
public class UserLocationRTController extends CommonController<UserLocationVO> {

	private static final Logger logger = LogManager.getLogger(UserLocationRTController.class);
	
	@Autowired
	UserLocationRTService userLocationRTService;

	@PostMapping(FilePathConstants.LOCATION_LIST)
	@ResponseBody
	public ResponseEntity<JSONResponse> getAll(HttpServletRequest request,@RequestBody ScreenJsonVO screenJson) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			screenJson.setAccessToken(accessToken);
			jsonResponse = userLocationRTService.getAll(screenJson);

		} catch (CommonException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (Exception e) {
			logger.error("error", e);
		}

		return jsonResponse;
	}

	@PostMapping(FilePathConstants.LOCATION_ADD)
	@ResponseBody
	public ResponseEntity<JSONResponse> addUserLocation(HttpServletRequest request,@RequestBody UserLocationVO userLocationMasterVo) {

		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			userLocationMasterVo.setAccessToken(accessToken);
			jsonResponse = userLocationRTService.addUserLocation(userLocationMasterVo);

		} catch (CommonException e) {
			logger.error("error", e);
		} catch (Exception e) {
			logger.error("error", e);
		}

		return jsonResponse;
	}

	@PostMapping(FilePathConstants.LOCATION_CREATE)
	@ResponseBody
	public ResponseEntity<JSONResponse> create(HttpServletRequest request,@RequestBody UserLocationVO userLocationMasterVo) {

		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			userLocationMasterVo.setAccessToken(accessToken);
			jsonResponse = userLocationRTService.create(userLocationMasterVo);

		} catch (CommonException e) {
			logger.error("error", e);
		} catch (Exception e) {
			logger.error("error", e);
		}

		return jsonResponse;
	}

	@PostMapping(FilePathConstants.LOCATION_UPDATE)
	@ResponseBody
	public ResponseEntity<JSONResponse> update(HttpServletRequest request,@RequestBody UserLocationVO userLocationMasterVo) {

		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			userLocationMasterVo.setAccessToken(accessToken);
			jsonResponse = userLocationRTService.update(userLocationMasterVo);

		} catch (CommonException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
		}

		return jsonResponse;
	}

	@PostMapping(FilePathConstants.LOCATION_DELETE)
	@ResponseBody
	public ResponseEntity<JSONResponse> delete(HttpServletRequest request,@RequestBody UserLocationVO userLocationMasterVo) {

		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			userLocationMasterVo.setAccessToken(accessToken);
			jsonResponse = userLocationRTService.delete(userLocationMasterVo);

		} catch (CommonException e) {
			logger.error("error", e);
		} catch (Exception e) {
			logger.error("error", e);
		}

		return jsonResponse;
	}

	@PostMapping(FilePathConstants.LOCATION_VIEW)
	@ResponseBody
	public ResponseEntity<JSONResponse> view(HttpServletRequest request,@RequestBody UserLocationVO userLocationMasterVo) {

		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			userLocationMasterVo.setAccessToken(accessToken);
			jsonResponse = userLocationRTService.view(userLocationMasterVo);

		} catch (CommonException e) {
			logger.error("error", e);
		} catch (Exception e) {
			logger.error("error", e);
		}

		return jsonResponse;
	}

	@PostMapping(FilePathConstants.LOCATION_SEARCH)
	@ResponseBody
	public ResponseEntity<JSONResponse> getAllSearch(HttpServletRequest request,@RequestBody UserLocationVO userLocationMasterVo) {

		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			userLocationMasterVo.setAccessToken(accessToken);
			jsonResponse = userLocationRTService.getAllSearch(userLocationMasterVo);

		} catch (CommonException e) {
			logger.error("error", e);
		} catch (Exception e) {
			logger.error("error", e);
		}

		return jsonResponse;
	}

}
