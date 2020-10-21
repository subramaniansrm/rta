package com.srm.rta.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.srm.coreframework.constants.FilePathConstants;
import com.srm.coreframework.response.JSONResponse;
import com.srm.coreframework.vo.LoginForm;

/**
 * @author sai
 *
 */
@Service
public class PasswordRestTemplateService {

	@Value("${restTemplateUrl}")
	private String restTemplateUrl;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public ResponseEntity<JSONResponse> changePassword(LoginForm changePasswordRequest){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.RT_RTA_CHANGE_PASSWORD;
			response = restTemplate.postForObject(url, changePasswordRequest, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	/*public ResponseEntity<JSONResponse> forgotPassword( LoginForm forgotPasswordRequest){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.RT_RTA_FORGOT_PASSWORD;
			response = restTemplate.postForObject(url, forgotPasswordRequest, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}*/
}
