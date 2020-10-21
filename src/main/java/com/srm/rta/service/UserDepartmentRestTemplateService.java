package com.srm.rta.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.srm.coreframework.constants.FilePathConstants;
import com.srm.coreframework.response.JSONResponse;
import com.srm.coreframework.vo.ScreenJsonVO;
import com.srm.coreframework.vo.UserDepartmentVO;

/**
 * @author priyankas
 *
 */
@Service
public class UserDepartmentRestTemplateService {


	@Value("${restTemplateUrl}")
	private String restTemplateUrl;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public ResponseEntity<JSONResponse> getAll(ScreenJsonVO screenJson){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_LOAD_DEPARTMENT;
			response = restTemplate.postForObject(url, screenJson, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	public ResponseEntity<JSONResponse> searchUserDepartment( UserDepartmentVO userDepartmentVO){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_SEARCH_DEPARTMENT;
			response = restTemplate.postForObject(url, userDepartmentVO, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	public ResponseEntity<JSONResponse> createUserDepartment( UserDepartmentVO userDepartmentVO){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_ADD_DEPARTMENT;
			response = restTemplate.postForObject(url, userDepartmentVO, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	public ResponseEntity<JSONResponse> updateUserDepartment( UserDepartmentVO userDepartmentVO){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_UPDATE_DEPARTMENT;
			response = restTemplate.postForObject(url, userDepartmentVO, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	public ResponseEntity<JSONResponse> deleteUserDepartment( UserDepartmentVO userDepartmentVO){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_DELETE_DEPARTMENT;
			response = restTemplate.postForObject(url, userDepartmentVO, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	public ResponseEntity<JSONResponse> viewUserDepartment( UserDepartmentVO userDepartmentVO){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_VIEW_DEPARTMENT;
			response = restTemplate.postForObject(url, userDepartmentVO, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	public ResponseEntity<JSONResponse> addUserDepartment( UserDepartmentVO userDepartmentVO){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_DEPARTMENT_AUTH_ADD;
			response = restTemplate.postForObject(url, userDepartmentVO, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
}
