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
import com.srm.coreframework.vo.SubLocationVO;
import com.srm.rta.service.SubLocationRtService;

@RestController
public class SubLocationRtController extends CommonController<SubLocationVO> {

	private static final Logger logger = LogManager.getLogger(SubLocationRtController.class);
	
	@Autowired
	SubLocationRtService userLocationRTService;

	@PostMapping(FilePathConstants.SUBLOCATION_LIST)
	@ResponseBody
	public ResponseEntity<JSONResponse> getAll(HttpServletRequest request,@RequestBody ScreenJsonVO screenJson) {
		
		ResponseEntity<JSONResponse> jsonResponse1 = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			screenJson.setAccessToken(accessToken);
			jsonResponse1 = userLocationRTService.getAll(screenJson);

		
		} catch (CommonException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return jsonResponse1;
	}

	@PostMapping(FilePathConstants.SUBLOCATION_ADD)
	@ResponseBody
	public ResponseEntity<JSONResponse> add(HttpServletRequest request,@RequestBody SubLocationVO subLocationVo) {
		String accessToken = getHeaderAccessToken(request);
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);

		try {
			subLocationVo.setAccessToken(accessToken);
			jsonResponse = userLocationRTService.add(subLocationVo);

		} catch (CommonException e) {
			logger.error("error", e);
		} catch (Exception e) {
			logger.error("error", e);
		}

		return jsonResponse;
	}

	@PostMapping(FilePathConstants.SUBLOCATION_CREATE)
	@ResponseBody
	public ResponseEntity<JSONResponse> createsublocation(HttpServletRequest request,@RequestBody SubLocationVO subLocationVo) {
		String accessToken = getHeaderAccessToken(request);
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);

		try {
			subLocationVo.setAccessToken(accessToken);
			jsonResponse = userLocationRTService.createsublocation(subLocationVo);

		} catch (CommonException e) {
			logger.error("error", e);
		} catch (Exception e) {
			logger.error("error", e);
		}

		return jsonResponse;
	}

	@PostMapping(FilePathConstants.SUBLOCATION_UPDATE)
	@ResponseBody
	public ResponseEntity<JSONResponse> updatesublocation(HttpServletRequest request,@RequestBody SubLocationVO subLocationVo) {
		String accessToken = getHeaderAccessToken(request);
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);

		try {
			subLocationVo.setAccessToken(accessToken);
			jsonResponse = userLocationRTService.updatesublocation(subLocationVo);

		} catch (CommonException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
		}

		return jsonResponse;
	}

	@PostMapping(FilePathConstants.SUBLOCATION_DELETE)
	@ResponseBody
	public ResponseEntity<JSONResponse> deleteSublocation(HttpServletRequest request,@RequestBody SubLocationVO subLocationVo) {
		String accessToken = getHeaderAccessToken(request);
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);

		try {
			subLocationVo.setAccessToken(accessToken);
			jsonResponse = userLocationRTService.deleteSublocation(subLocationVo);

		} catch (CommonException e) {
			logger.error("error", e);
		} catch (Exception e) {
			logger.error("error", e);
		}

		return jsonResponse;
	}

	@PostMapping(FilePathConstants.SUBLOCATION_VIEW)
	@ResponseBody
	public ResponseEntity<JSONResponse> viewSublocation(HttpServletRequest request,@RequestBody SubLocationVO subLocationVo) {
		String accessToken = getHeaderAccessToken(request);
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);

		try {
			subLocationVo.setAccessToken(accessToken);
			jsonResponse = userLocationRTService.viewSublocation(subLocationVo);

		} catch (CommonException e) {
			logger.error("error", e);
		} catch (Exception e) {
			logger.error("error", e);
		}

		return jsonResponse;
	}

	@PostMapping(FilePathConstants.SUBLOCATION_SEARCH)
	@ResponseBody
	public ResponseEntity<JSONResponse> getAllSearch(HttpServletRequest request,@RequestBody SubLocationVO subLocationVo) {
		String accessToken = getHeaderAccessToken(request);
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);

		try {
			subLocationVo.setAccessToken(accessToken);
			jsonResponse = userLocationRTService.getAllSearch(subLocationVo);

		} catch (CommonException e) {
			logger.error("error", e);
		} catch (Exception e) {
			logger.error("error", e);
		}

		return jsonResponse;
	}

}
