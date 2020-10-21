package com.srm.rta.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.srm.coreframework.config.UserMessages;
import com.srm.coreframework.constants.FilePathConstants;
import com.srm.coreframework.response.JSONResponse;
import com.srm.coreframework.vo.EntityLicenseVO;
import com.srm.coreframework.vo.ScreenJsonVO;

@Service
public class EntityRestTemplateService {

	@Value("${restTemplateUrl}")
	private String restTemplateUrl;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	UserMessages userMessages;

	public ResponseEntity<JSONResponse> getAll(ScreenJsonVO screenJson) {
		JSONResponse response = new JSONResponse();
		try {
			String url = restTemplateUrl + FilePathConstants.ENTITY_MASTER_LIST;
			response = restTemplate.postForObject(url, screenJson, JSONResponse.class);

		} catch (Exception e) {

		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	public ResponseEntity<JSONResponse> view(EntityLicenseVO entityLicenseVO) {
		JSONResponse response = new JSONResponse();
		try {
			String url = restTemplateUrl + FilePathConstants.ENTITY_MASTER_VIEW;
			response = restTemplate.postForObject(url, entityLicenseVO, JSONResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	public ResponseEntity<JSONResponse> add(EntityLicenseVO entityLicenseVO) {
		JSONResponse response = new JSONResponse();
		try {
			String url = restTemplateUrl + FilePathConstants.ENTITY__REST_TEMPLATE_ADD;
			response = restTemplate.postForObject(url, entityLicenseVO, JSONResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
	
	public ResponseEntity<JSONResponse> searchEntity(EntityLicenseVO entityLicenseVO) {
		JSONResponse response = new JSONResponse();
		try {
			String url = restTemplateUrl + FilePathConstants.ENTITY_MASTER_SEARCH;
			response = restTemplate.postForObject(url, entityLicenseVO, JSONResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	public ResponseEntity<JSONResponse> renewalEntity(EntityLicenseVO entityLicenseVO) {
		JSONResponse response = new JSONResponse();
		try {
			String url = restTemplateUrl + FilePathConstants.ENTITY_MASTER_RENEWAL_REST_TEMPLATE;
			response = restTemplate.postForObject(url, entityLicenseVO, JSONResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
	public ResponseEntity<JSONResponse> update(EntityLicenseVO entityLicenseVO) {
		JSONResponse response = new JSONResponse();
		try {
			String url = restTemplateUrl + FilePathConstants.ENTITY_MASTER_UPDATE_REST_TEMPLATE;
			response = restTemplate.postForObject(url, entityLicenseVO, JSONResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
	
	public ResponseEntity<JSONResponse> getAllEntity(ScreenJsonVO screenJson) {
		JSONResponse response = new JSONResponse();
		try {
			String url = restTemplateUrl + FilePathConstants.ENTITY_MASTER_ALLLIST;
			response = restTemplate.postForObject(url, screenJson, JSONResponse.class);

		} catch (Exception e) {

		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}	
	
	public ResponseEntity<JSONResponse> entityPlanningDropDown(ScreenJsonVO screenJson) {
		JSONResponse response = new JSONResponse();
		try {
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_ENTITY_PLANNING_DRP;
			response = restTemplate.postForObject(url, screenJson, JSONResponse.class);

		} catch (Exception e) {

		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
	
	
	
}