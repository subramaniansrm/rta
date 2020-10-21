package com.srm.rta.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.srm.coreframework.constants.FilePathConstants;
import com.srm.coreframework.response.JSONResponse;
import com.srm.coreframework.vo.EntityPlanningVo;
import com.srm.coreframework.vo.ScreenJsonVO;

/**
 * @author priyankas
 *
 */
@Service
public class EntityPlanningRestTemplateService {

	@Value("${restTemplateUrl}")
	private String restTemplateUrl;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public ResponseEntity<JSONResponse> listentityplanning(ScreenJsonVO screenJson){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_ENTITY_PLANNING_MASTER_LIST;
			response = restTemplate.postForObject(url, screenJson, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	public ResponseEntity<JSONResponse> addentityplanning(ScreenJsonVO screenJson){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_ENTITY_PLANNING_MASTER_ADD;
			response = restTemplate.postForObject(url, screenJson, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	public ResponseEntity<JSONResponse> save(EntityPlanningVo entityPlanningVo){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_ENTITY_PLANNING_MASTER_SAVE;
			response = restTemplate.postForObject(url, entityPlanningVo, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	public ResponseEntity<JSONResponse> view(EntityPlanningVo entityPlanningVo){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_ENTITY_PLANNING_MASTER_VIEW;
			response = restTemplate.postForObject(url, entityPlanningVo, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	public ResponseEntity<JSONResponse> update(EntityPlanningVo entityPlanningVo){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_ENTITY_PLANNING_MASTER_UPDATE;
			response = restTemplate.postForObject(url, entityPlanningVo, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	public ResponseEntity<JSONResponse> delete(EntityPlanningVo entityPlanningVo){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_ENTITY_PLANNING_MASTER_DELETE;
			response = restTemplate.postForObject(url, entityPlanningVo, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	
	public ResponseEntity<JSONResponse> search(EntityPlanningVo entityPlanningVo){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_ENTITY_PLANNING_MASTER_SEARCH;
			response = restTemplate.postForObject(url, entityPlanningVo, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
}
