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
import com.srm.coreframework.vo.ScreenJsonVO;
import com.srm.coreframework.vo.UserRoleVO;

@Service
public class UserRoleRestTemplateService {

	@Value("${restTemplateUrl}")
	private String restTemplateUrl;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public ResponseEntity<JSONResponse> listUserRole(ScreenJsonVO screenJson){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_GET_USER_ROLE_LIST;
			response = restTemplate.postForObject(url, screenJson, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	public ResponseEntity<JSONResponse> addScreenFields(ScreenJsonVO screenJson){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_GET_USER_ROLE_ADD;
			response = restTemplate.postForObject(url, screenJson, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	public ResponseEntity<JSONResponse> saveUserRole( UserRoleVO userRoleVO){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_SAVE_USER_ROLE;
			response = restTemplate.postForObject(url, userRoleVO, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	public ResponseEntity<JSONResponse> updateUserRole( UserRoleVO userRoleVO){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_UPDATE_USER_ROLE;
			response = restTemplate.postForObject(url, userRoleVO, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	public ResponseEntity<JSONResponse> deleteUserRole( UserRoleVO userRoleVO){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_DELETE_USER_ROLE;
			response = restTemplate.postForObject(url,  userRoleVO, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	public ResponseEntity<JSONResponse> search( UserRoleVO userRoleVO){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_SEARCH_USER_ROLE;
			response = restTemplate.postForObject(url, userRoleVO, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	public ResponseEntity<JSONResponse> viewUserRole(@RequestBody UserRoleVO userRoleVO){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_VIEW_USER_ROLE;
			response = restTemplate.postForObject(url, userRoleVO, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	public ResponseEntity<JSONResponse> copyUserRole(ScreenJsonVO screenJson){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_COPY_USER_ROLE;
			response = restTemplate.postForObject(url, screenJson, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
}
