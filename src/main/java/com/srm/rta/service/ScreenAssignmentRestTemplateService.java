package com.srm.rta.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import com.srm.coreframework.constants.FilePathConstants;
import com.srm.coreframework.response.JSONResponse;
import com.srm.coreframework.vo.ScreenAuthenticationVO;
import com.srm.coreframework.vo.ScreenJsonVO;

@Service
public class ScreenAssignmentRestTemplateService {
	
	@Value("${restTemplateUrl}")
	private String restTemplateUrl;
	
	@Autowired
	private RestTemplate restTemplate;

	public ResponseEntity<JSONResponse> listRoles(ScreenJsonVO screenJson){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_AUTHENTICATION_LIST;
			response = restTemplate.postForObject(url, screenJson, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	public ResponseEntity<JSONResponse> searchScreenAssignmentList(ScreenAuthenticationVO screenAuthorizationMaster){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_AUTHENTICATION_SEARCH;
			response = restTemplate.postForObject(url, screenAuthorizationMaster, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	public ResponseEntity<JSONResponse> load(ScreenAuthenticationVO screenAuthenticationMstr){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_AUTHENTICATION_ADD;
			response = restTemplate.postForObject(url, screenAuthenticationMstr, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	
	public ResponseEntity<JSONResponse> loadRoleBasedSubScreenList(ScreenAuthenticationVO screenAuthenticationMaster){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_AUTHENTICATION_SUB_SCREEN;
			response = restTemplate.postForObject(url, screenAuthenticationMaster, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	public ResponseEntity<JSONResponse> loadRoleBasedScreenFieldsList(ScreenAuthenticationVO screenAuthenticationMaster){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_AUTHENTICATION_FIELD;
			response = restTemplate.postForObject(url, screenAuthenticationMaster, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	public ResponseEntity<JSONResponse> loadRoleBasedFunctionList(ScreenAuthenticationVO screenAuthenticationMaster){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_AUTHENTICATION_FUNCTION;
			response = restTemplate.postForObject(url, screenAuthenticationMaster, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	public ResponseEntity<JSONResponse> saveScreenAssignment(ScreenAuthenticationVO screenAuthenticationMaster){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_AUTHENTICATION_CREATE;
			response = restTemplate.postForObject(url, screenAuthenticationMaster, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	public ResponseEntity<JSONResponse> deleteScreenAssignment(ScreenAuthenticationVO screenAuthenticationMaster){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_AUTHENTICATION_DELETE;
			response = restTemplate.postForObject(url, screenAuthenticationMaster, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
}
