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
import com.srm.coreframework.vo.WeekendVO;
import com.srm.rta.service.WeekendRestTemplateService;

@RestController
public class WeekendRestTemplateController extends CommonController<UserMasterVO> {

	private static final Logger logger = LogManager.getLogger(WeekendRestTemplateController.class);
	
	@Autowired
	WeekendRestTemplateService weekendRestTemplateService;
	
 
	@PostMapping(FilePathConstants.WEEKEND_RT_GETALL)
	@ResponseBody
	public ResponseEntity<JSONResponse> listWeekend(HttpServletRequest request,@RequestBody ScreenJsonVO screenJson) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			screenJson.setAccessToken(accessToken);
			jsonResponse = 	weekendRestTemplateService.listWeekend(screenJson);
			
		} catch (CommonException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
		}

		return jsonResponse;
	}	
	
 
	@PostMapping(FilePathConstants.WEEKEND_RT_CREATE)
	@ResponseBody
	public ResponseEntity<JSONResponse> saveWeedend(HttpServletRequest request,@RequestBody WeekendVO weekendVO) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			weekendVO.setAccessToken(accessToken);
			jsonResponse = 	weekendRestTemplateService.saveWeedend(weekendVO);
			
		} catch (CommonException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
		}

		return jsonResponse;
	}
	
	 
 
	@PostMapping(FilePathConstants.WEEKEND_RT_DELETE)
	public ResponseEntity<JSONResponse> deleteWeekend(HttpServletRequest request,@RequestBody WeekendVO weekendVO) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		
		String accessToken = getHeaderAccessToken(request);
		try {
			weekendVO.setAccessToken(accessToken);

			jsonResponse = 	weekendRestTemplateService.deleteWeekend(weekendVO);
			
		} catch (CommonException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
		}

		return jsonResponse;
	}
	
	 
	 
	@PostMapping(FilePathConstants.WEEKEND_RT_VIEW)
	@ResponseBody
	public ResponseEntity<JSONResponse> viewWeekend(HttpServletRequest request,@RequestBody WeekendVO weekendVO) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		
		String accessToken = getHeaderAccessToken(request);
		try {
			weekendVO.setAccessToken(accessToken);

			jsonResponse = 	weekendRestTemplateService.viewWeekend(weekendVO);
			
		} catch (CommonException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
		}

		return jsonResponse;
	}
	
	
	@PostMapping(FilePathConstants.WEEKEND_RT_SEARCH)
	@ResponseBody
	public ResponseEntity<JSONResponse> search(HttpServletRequest request,@RequestBody WeekendVO weekendVO) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		
		String accessToken = getHeaderAccessToken(request);
		try {
			weekendVO.setAccessToken(accessToken);

			jsonResponse = 	weekendRestTemplateService.search(weekendVO);
			
		} catch (CommonException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
		}

		return jsonResponse;
	}

	
	@PostMapping(FilePathConstants.WEEKEND_RT_UPDATE)
	@ResponseBody
	public ResponseEntity<JSONResponse> updateWeekend(HttpServletRequest request,@RequestBody WeekendVO weekendVO) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			weekendVO.setAccessToken(accessToken);
			jsonResponse = 	weekendRestTemplateService.updateWeekend(weekendVO);
			
		} catch (CommonException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
		}

		return jsonResponse;
	}
	
	
	@PostMapping(FilePathConstants.WEEKEND_RT_GETADDFIELDS)
	@ResponseBody
	public ResponseEntity<JSONResponse> addScreenFields(HttpServletRequest request,@RequestBody ScreenJsonVO screenJson) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			screenJson.setAccessToken(accessToken);
			jsonResponse = 	weekendRestTemplateService.addScreenFields(screenJson);
			
		} catch (CommonException e) {
			e.printStackTrace();
			logger.error("error", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
		}

		return jsonResponse;
	}
	
	@PostMapping(FilePathConstants.DAYS_RT_LIST)
	@ResponseBody
	public ResponseEntity<JSONResponse> listDays(HttpServletRequest request,@RequestBody ScreenJsonVO screenJson) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			screenJson.setAccessToken(accessToken);
			jsonResponse = 	weekendRestTemplateService.listDays(screenJson);
			
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
