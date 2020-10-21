package com.srm.rta.service;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.config.UserMessages;
import com.srm.coreframework.constants.FilePathConstants;
import com.srm.coreframework.controller.CommonController;
import com.srm.coreframework.response.JSONResponse;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.coreframework.vo.ScreenJsonVO;

import com.srm.coreframework.vo.UserMasterVO;
import com.srm.rta.vo.EmailVo;
import com.srm.rta.vo.RequestVO;

 


@Service
public class UserMasterService1 extends CommonController<UserMasterVO> {
	
	@Value("${restTemplateUrl}")
	private String restTemplateUrl;
	
	@Value("${emailRestTemplateUrl}")
	private String emailRestTemplateUrl;
	
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	UserMessages userMessages;
	
	public ResponseEntity<JSONResponse>getAll(ScreenJsonVO screenJson){
		JSONResponse response =new JSONResponse();
		try{
			
			String url = restTemplateUrl + FilePathConstants.USER_LIST;
			response = restTemplate.postForObject(url, screenJson, JSONResponse.class);
			
		}catch(Exception e){
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	public ResponseEntity<JSONResponse>addUser(UserMasterVO userMasterVo){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.USER_AUTH_ADD;
			response = restTemplate.postForObject(url, userMasterVo, JSONResponse.class);
			
		}catch(Exception e){
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
		
	public ResponseEntity<JSONResponse>create(UserMasterVO userVo){
		JSONResponse response = null;
		JSONResponse emailResponse = null;
		try{
			response =new JSONResponse();
			String url = restTemplateUrl + FilePathConstants.USER_SAVE;
			response = restTemplate.postForObject(url, userVo, JSONResponse.class);
									
			//Set Email Param
		 	if(null != response.getResponseCode() && 
		 			response.getResponseCode().equals(CommonConstant.SUCCESS_CODE)){
		 		emailResponse =new JSONResponse();	 	 		 					
				ObjectMapper mapper = new ObjectMapper();
				UserMasterVO userMasterVo = mapper.convertValue(response.getSuccesObject(), UserMasterVO.class);
							 			
				AuthDetailsVo authDetailsVo = mapper.convertValue(response.getAuthSuccesObject(), AuthDetailsVo.class);
				
				EmailVo emailVo = this.emailParam(userMasterVo,authDetailsVo);

				final String uri = emailRestTemplateUrl + FilePathConstants.TOEMAIL;

				emailResponse = restTemplate.postForObject(uri, emailVo, JSONResponse.class);															  			
				emailResponse.setAuthSuccesObject("");
				emailResponse.setSuccesObject("");
			} 
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return new ResponseEntity<>(response, HttpStatus.OK);		
	}
	
	
	public ResponseEntity<JSONResponse>update(UserMasterVO userVo){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.USER_MODIFY;
			response = restTemplate.postForObject(url, userVo, JSONResponse.class);
			
		}catch(Exception e){
			response.setResponseCode("500");
			response.setResponseMessage("error");
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	public ResponseEntity<JSONResponse>search(UserMasterVO userVo){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.USER_SEARCH1;
			response = restTemplate.postForObject(url, userVo, JSONResponse.class);
			
		}catch(Exception e){
			
			throw new CommonException(userMessages.getDataFailure());
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	public ResponseEntity<JSONResponse>view(UserMasterVO userVo){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.USER_VIEW1;
			response = restTemplate.postForObject(url, userVo, JSONResponse.class);
			
		}catch(Exception e){
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	public ResponseEntity<JSONResponse>delete(UserMasterVO userVo){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.USER_DELETE1;
			response = restTemplate.postForObject(url, userVo, JSONResponse.class);
			
		}catch(Exception e){
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	@Transactional
	public EmailVo emailParam(UserMasterVO userMasterVo, AuthDetailsVo authDetailsVo) throws Exception {

		EmailVo emailVo = new EmailVo();
		try{
		
		emailVo.setMessageCode(CommonConstant.NEW_USER);
		emailVo.setGroupId(CommonConstant.USR);
		emailVo.setRequestId(0);
		emailVo.setEmailFlag(0);

		if (null != userMasterVo.getUserId() && 0 != userMasterVo.getUserId()) {
			emailVo.setToUserId(userMasterVo.getUserId());
		}

		if (null != authDetailsVo.getEntityId() || 0 != authDetailsVo.getEntityId()) {
			emailVo.setEntityId(authDetailsVo.getEntityId());
		}

		// Email Address
		if (null != userMasterVo.getFirstName()) {
			emailVo.setUserName(userMasterVo.getFirstName());
		}

		if (null != userMasterVo.getEmailId()) {
			emailVo.setToUserAddress(userMasterVo.getEmailId());
		}

		emailVo.setNotificationFlag(CommonConstant.CONSTANT_ONE);
		
		if(null != userMasterVo.getId()){
			emailVo.setToUserId(userMasterVo.getId());	
		}
		
		emailVo.setTrEmailFlag(CommonConstant.CONSTANT_ONE);
		if(null != userMasterVo.getUserLoginId()){
			emailVo.setUserLoginId(userMasterVo.getUserLoginId());
		}
		
		if(null != userMasterVo.getPassword()){
		emailVo.setPassword(userMasterVo.getPassword());
		}
		
		if(null != userMasterVo.getLangCode()){
			emailVo.setUserLang(userMasterVo.getLangCode());
		}
		
		emailVo.setSystemConfigurationVo(getSystemConfigurationDetails(authDetailsVo));
		
		if (null != authDetailsVo.getUserId()) {
			emailVo.setCreateBy(authDetailsVo.getUserId());
			emailVo.setUpdateBy(authDetailsVo.getUserId());
			emailVo.setCreateDate(CommonConstant.getCalenderDate());
			emailVo.setUpdateDate(CommonConstant.getCalenderDate());
			emailVo.setFromUserId(authDetailsVo.getUserId());
		}	

		}catch(Exception e){
			e.printStackTrace();
			
		}
		return emailVo;
	}
 }
