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
import com.srm.coreframework.vo.WeekendVO;

@Service
public class WeekendRestTemplateService {

	@Value("${restTemplateUrl}")
	private String restTemplateUrl;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public ResponseEntity<JSONResponse> listWeekend(ScreenJsonVO screenJson){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.WEEKEND_MASTER_GETALL;
			response = restTemplate.postForObject(url, screenJson, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
		 
	public ResponseEntity<JSONResponse> saveWeedend( WeekendVO weekendVO){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.WEEKEND_MASTER_CREATE;
			response = restTemplate.postForObject(url, weekendVO, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
 
	public ResponseEntity<JSONResponse> deleteWeekend( WeekendVO weekendVO){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.WEEKEND_MASTER_DELETE;
			response = restTemplate.postForObject(url,  weekendVO, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);		
	}
 
	public ResponseEntity<JSONResponse> viewWeekend(@RequestBody WeekendVO weekendVO){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.WEEKEND_MASTER_VIEW;
			response = restTemplate.postForObject(url, weekendVO, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);		
	}
	 	
	public ResponseEntity<JSONResponse> search(WeekendVO weekendVO){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.WEEKEND_MASTER_SEARCH;
			response = restTemplate.postForObject(url, weekendVO, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	public ResponseEntity<JSONResponse> updateWeekend(WeekendVO weekendVO){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.WEEKEND_MASTER_UPDATE;
			response = restTemplate.postForObject(url, weekendVO, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	public ResponseEntity<JSONResponse> addScreenFields(ScreenJsonVO screenJson){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.WEEKEND_MASTER_GETADDFIELDS;
			response = restTemplate.postForObject(url, screenJson, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	public ResponseEntity<JSONResponse> listDays(ScreenJsonVO screenJson){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.DAYS_LIST;
			response = restTemplate.postForObject(url, screenJson, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
}
