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
import com.srm.rta.service.UserMasterService1;

@RestController
public class UserController1 extends CommonController<UserMasterVO> {

	private static final Logger logger = LogManager.getLogger(UserController1.class);
	

	@Autowired
	UserMasterService1 userMasterService1;


	@PostMapping(FilePathConstants.USER_LOAD)
	@ResponseBody
	public ResponseEntity<JSONResponse> getAllList(HttpServletRequest request,@RequestBody ScreenJsonVO screenJson) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			screenJson.setAccessToken(accessToken);
			jsonResponse = userMasterService1.getAll(screenJson);

		} catch (CommonException e) {
			logger.error("error", e);
		} catch (Exception e) {
			logger.error("error", e);
		}

		return jsonResponse;
	}

	@PostMapping(FilePathConstants.USER_ADD)
	@ResponseBody
	public ResponseEntity<JSONResponse> addUser(HttpServletRequest request,@RequestBody UserMasterVO userMasterVo) {

		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			userMasterVo.setAccessToken(accessToken);
			jsonResponse = userMasterService1.addUser(userMasterVo);

		} catch (CommonException e) {
			logger.error("error", e);
		} catch (Exception e) {
			logger.error("error", e);
		}

		return jsonResponse;
	}

	@PostMapping(FilePathConstants.USER_CREATE)
	@ResponseBody
	public ResponseEntity<JSONResponse> create(HttpServletRequest request,@RequestBody UserMasterVO userVo) {

		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			userVo.setAccessToken(accessToken);
			jsonResponse = userMasterService1.create(userVo);

		} catch (CommonException e) {
			logger.error("error", e);
		} catch (Exception e) {
			logger.error("error", e);
		}

		return jsonResponse;
	}

	@PostMapping(FilePathConstants.USER_UPDATE)
	@ResponseBody
	public ResponseEntity<JSONResponse> update(HttpServletRequest request,@RequestBody UserMasterVO userVo) {

		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			userVo.setAccessToken(accessToken);
			jsonResponse = userMasterService1.update(userVo);

		} catch (CommonException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
		}

		return jsonResponse;
	}

	@PostMapping(FilePathConstants.USER_DELETE)
	@ResponseBody
	public ResponseEntity<JSONResponse> delete(HttpServletRequest request,@RequestBody UserMasterVO userVo) {

		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			userVo.setAccessToken(accessToken);
			jsonResponse = userMasterService1.delete(userVo);

		} catch (CommonException e) {
			logger.error("error", e);
		} catch (Exception e) {
			logger.error("error", e);
		}

		return jsonResponse;
	}

	@PostMapping(FilePathConstants.USER_VIEW)
	@ResponseBody
	public ResponseEntity<JSONResponse> view(HttpServletRequest request,@RequestBody UserMasterVO userVo) {

		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			userVo.setAccessToken(accessToken);
			jsonResponse = userMasterService1.view(userVo);

		} catch (CommonException e) {
			logger.error("error", e);
		} catch (Exception e) {
			logger.error("error", e);
		}

		return jsonResponse;
	}

	@PostMapping(FilePathConstants.USER_SEARCH)
	@ResponseBody
	public ResponseEntity<JSONResponse> search(HttpServletRequest request,@RequestBody UserMasterVO userVo) {

		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			userVo.setAccessToken(accessToken);
			jsonResponse = userMasterService1.search(userVo);

		} catch (CommonException e) {
			logger.error("error", e);
		} catch (Exception e) {
			logger.error("error", e);
		}

		return jsonResponse;
	}
}
