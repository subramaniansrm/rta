package com.srm.rta.controller;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.config.LoginAuthException;
import com.srm.coreframework.constants.FilePathConstants;
import com.srm.coreframework.controller.CommonController;
import com.srm.coreframework.response.JSONResponse;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.vo.EntityPlanningVo;
import com.srm.coreframework.vo.ScreenJsonVO;
import com.srm.rta.service.EntityPlanningRestTemplateService;

/**
 * @author priyankas
 *
 */
@RestController
public class EntityPlanningRestTemplateController extends CommonController<EntityPlanningVo>{
	
	@Autowired
	EntityPlanningRestTemplateService entityPlanningRestTemplateService;
	
	@Value("${restTemplateUrl}")
	private String restTemplateUrl;
	
	@Autowired
	private RestTemplate restTemplate;
	
	/*
	 * Method to list entity plan
	 * @param screenJson
	 * @return jsonResponse
	 */
	@PostMapping(FilePathConstants.ENTITY_PLANNING_MASTER_LIST)
	@ResponseBody
	public ResponseEntity<JSONResponse> listentityplanning(HttpServletRequest request,@RequestBody ScreenJsonVO screenJson) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			screenJson.setAccessToken(accessToken);
			jsonResponse = 	entityPlanningRestTemplateService.listentityplanning(screenJson);
			
		} catch (CommonException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonResponse;
	}
	
	/*
	 * Method to add entity plan 
	 * @param screenJson
	 * @return jsonResponse
	 */
	@PostMapping(FilePathConstants.ENTITY_PLANNING_MASTER_ADD)
	@ResponseBody
	public ResponseEntity<JSONResponse> addentityplanning(HttpServletRequest request,@RequestBody ScreenJsonVO screenJson) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			screenJson.setAccessToken(accessToken);
			jsonResponse = 	entityPlanningRestTemplateService.addentityplanning(screenJson);
			
		} catch (CommonException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonResponse;
	}
	
	/*
	 * Method to save entity plan
	 * @param screenJson
	 * @return jsonResponse
	 
	@PostMapping(FilePathConstants.ENTITY_PLANNING_MASTER_SAVE)
	@ResponseBody
	public ResponseEntity<JSONResponse> save(HttpServletRequest request,@RequestBody EntityPlanningVo entityPlanningVo) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			entityPlanningVo.setAccessToken(accessToken);
			jsonResponse = 	entityPlanningRestTemplateService.save(entityPlanningVo);
			
		} catch (CommonException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonResponse;
	}*/
	
	
	@RequestMapping(value = FilePathConstants.ENTITY_PLANNING_MASTER_SAVE, method = RequestMethod.POST, consumes = {
			"multipart/form-data" })
	@ResponseBody
	public ResponseEntity<JSONResponse> create(HttpServletRequest request,
			@RequestParam("file") MultipartFile[] uploadingFiles, @RequestParam("action") String str)
					throws HttpClientErrorException {

		JSONResponse jsonResponse = new JSONResponse();

		String accessToken = getHeaderAccessToken(request);

		List<String> tempFileNames = new ArrayList<>();
		String tempFileName;
		FileOutputStream fo;

		try {

			final String uri = restTemplateUrl + FilePathConstants.REST_TEMPLATE_ENTITY_PLANNING_MASTER_SAVE;

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			headers.add("Authorization", "Bearer " + accessToken);

			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			for (MultipartFile file : uploadingFiles) {
				tempFileName = file.getOriginalFilename();
				tempFileNames.add(tempFileName);
				fo = new FileOutputStream(tempFileName);
				fo.write(file.getBytes());
				fo.close();
				body.add("file", new FileSystemResource(tempFileName));
			}
			body.add("action", str);

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

			jsonResponse = restTemplate.postForObject(uri, requestEntity, JSONResponse.class);

			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

		} catch (CommonException e) {
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (LoginAuthException e) {
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			e.printStackTrace();
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}
	/*
	 * Method to view entity plan
	 * @param screenJson
	 * @return jsonResponse
	 */
	@PostMapping(FilePathConstants.ENTITY_PLANNING_MASTER_VIEW)
	@ResponseBody
	public ResponseEntity<JSONResponse> view(HttpServletRequest request,@RequestBody EntityPlanningVo entityPlanningVo) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		
		String accessToken = getHeaderAccessToken(request);
		try {
			entityPlanningVo.setAccessToken(accessToken);

			jsonResponse = 	entityPlanningRestTemplateService.view(entityPlanningVo);
			
		} catch (CommonException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonResponse;
	}
	
	/*
	 * Method to update entity plan
	 * @param screenJson
	 * @return jsonResponse
	 
	@PostMapping(FilePathConstants.ENTITY_PLANNING_MASTER_UPDATE)
	@ResponseBody
	public ResponseEntity<JSONResponse> update(HttpServletRequest request,@RequestBody EntityPlanningVo entityPlanningVo) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			entityPlanningVo.setAccessToken(accessToken);
			jsonResponse = 	entityPlanningRestTemplateService.update(entityPlanningVo);
			
		} catch (CommonException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonResponse;
	}*/
	
	@RequestMapping(value = FilePathConstants.ENTITY_PLANNING_MASTER_UPDATE, method = RequestMethod.POST, consumes = {
			"multipart/form-data" })
	@ResponseBody
	public ResponseEntity<JSONResponse> update(HttpServletRequest request,
			@RequestParam("file") MultipartFile[] uploadingFiles, @RequestParam("action") String str)
					throws HttpClientErrorException {

		JSONResponse jsonResponse = new JSONResponse();

		String accessToken = getHeaderAccessToken(request);

		List<String> tempFileNames = new ArrayList<>();
		String tempFileName;
		FileOutputStream fo;

		try {

			final String uri = restTemplateUrl + FilePathConstants.REST_TEMPLATE_ENTITY_PLANNING_MASTER_UPDATE;

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			headers.add("Authorization", "Bearer " + accessToken);

			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			for (MultipartFile file : uploadingFiles) {
				tempFileName = file.getOriginalFilename();
				tempFileNames.add(tempFileName);
				fo = new FileOutputStream(tempFileName);
				fo.write(file.getBytes());
				fo.close();
				body.add("file", new FileSystemResource(tempFileName));
			}
			body.add("action", str);

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

			jsonResponse = restTemplate.postForObject(uri, requestEntity, JSONResponse.class);

			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

		} catch (CommonException e) {
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (LoginAuthException e) {
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			e.printStackTrace();
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}
	/*
	 * Method to delete entity plan
	 * @param screenJson
	 * @return jsonResponse
	 */
	@PostMapping(FilePathConstants.ENTITY_PLANNING_MASTER_DELETE)
	@ResponseBody
	public ResponseEntity<JSONResponse> delete(HttpServletRequest request,@RequestBody EntityPlanningVo entityPlanningVo) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			entityPlanningVo.setAccessToken(accessToken);
			jsonResponse = 	entityPlanningRestTemplateService.delete(entityPlanningVo);
			
		} catch (CommonException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonResponse;
	}
	
	/*
	 * Method to search entity plan
	 * @param screenJson
	 * @return jsonResponse
	 */
	@PostMapping(FilePathConstants.ENTITY_PLANNING_MASTER_SEARCH)
	@ResponseBody
	public ResponseEntity<JSONResponse> search(HttpServletRequest request,@RequestBody EntityPlanningVo entityPlanningVo) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			entityPlanningVo.setAccessToken(accessToken);
			jsonResponse = 	entityPlanningRestTemplateService.search(entityPlanningVo);
			
		} catch (CommonException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonResponse;
	}
}
