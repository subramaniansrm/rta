package com.srm.rta.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.config.UserMessages;
import com.srm.coreframework.constants.FilePathConstants;
import com.srm.coreframework.response.JSONResponse;
import com.srm.coreframework.vo.ScreenJsonVO;
import com.srm.coreframework.vo.UserLocationVO;

@Service
public class UserLocationRTService {
	
	@Value("${restTemplateUrl}")
	private String restTemplateUrl;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	UserMessages userMessages;
	
	public ResponseEntity<JSONResponse>getAll(ScreenJsonVO screenJson){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.LOCATION_LIST_RT;
			response = restTemplate.postForObject(url, screenJson, JSONResponse.class);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	public ResponseEntity<JSONResponse>addUserLocation(UserLocationVO userLocationMasterVo){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.LOCATION_ADD_RT;
			response = restTemplate.postForObject(url, userLocationMasterVo, JSONResponse.class);
			
		}catch(Exception e){
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	public ResponseEntity<JSONResponse>create(UserLocationVO userLocationVo){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.LOCATION_CREATE_RT;
			response = restTemplate.postForObject(url, userLocationVo, JSONResponse.class);
			
		}catch(Exception e){
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	public ResponseEntity<JSONResponse>update(UserLocationVO userLocationVo){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.LOCATION_UPDATE_RT;
			response = restTemplate.postForObject(url, userLocationVo, JSONResponse.class);
			
		}catch(Exception e){
			response.setResponseCode("500");
			response.setResponseMessage("error");
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	public ResponseEntity<JSONResponse>getAllSearch(UserLocationVO userLocationMasterVo){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.LOCATION_SEARCH_RT;
			response = restTemplate.postForObject(url, userLocationMasterVo, JSONResponse.class);
			
		}catch(Exception e){
			
			throw new CommonException(userMessages.getDataFailure());
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	public ResponseEntity<JSONResponse>view(UserLocationVO userLocationMasterVo){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.LOCATION_VIEW_RT;
			response = restTemplate.postForObject(url, userLocationMasterVo, JSONResponse.class);
			
		}catch(Exception e){
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	public ResponseEntity<JSONResponse>delete(UserLocationVO userLocationMasterVo){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.LOCATION_DELETE_RT;
			response = restTemplate.postForObject(url, userLocationMasterVo, JSONResponse.class);
			
		}catch(Exception e){
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}

}
