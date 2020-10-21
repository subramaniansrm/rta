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
import com.srm.coreframework.vo.SubLocationVO;
import com.srm.coreframework.vo.UserDepartmentVO;
import com.srm.coreframework.vo.UserMasterVO;
import com.srm.coreframework.vo.UserRoleTypeVO;
import com.srm.coreframework.vo.UserRoleVO;

@Service
public class DropDownRestTemplateService {
	
	@Value("${restTemplateUrl}")
	private String restTemplateUrl;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public ResponseEntity<JSONResponse>getLocation(ScreenJsonVO screenJson){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_LOCATION_LOAD;
			response = restTemplate.postForObject(url, screenJson, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	public ResponseEntity<JSONResponse> getdivision(ScreenJsonVO screenJson){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_DIVISIONS_LOAD;
			response = restTemplate.postForObject(url, screenJson, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	public ResponseEntity<JSONResponse>getDepartment( UserDepartmentVO departmentVo){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_DEPARTMENT_LOAD;
			response = restTemplate.postForObject(url, departmentVo, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	/**Method to get User List
	 * @param screenJson
	 * @return ResponseEntity
	 */
	public ResponseEntity<JSONResponse> getUser( UserMasterVO userMasterVo){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_USER_DROPDOWN;
			response = restTemplate.postForObject(url, userMasterVo, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	/**Method to get User List
	 * @param screenJson
	 * @return ResponseEntity
	 */
	public ResponseEntity<JSONResponse> getCountryList(ScreenJsonVO screenJson){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_COUNTRY_LOAD;
			response = restTemplate.postForObject(url, screenJson, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	public ResponseEntity<JSONResponse> getSublocation(SubLocationVO subLocationVo){
		JSONResponse response =new JSONResponse();
		try{
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_SUBLOCATION_LOAD;
			response = restTemplate.postForObject(url, subLocationVo, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	public ResponseEntity<JSONResponse> getRole( UserRoleVO userRoleVo){
		JSONResponse response =new JSONResponse();
		try{
			
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_ROLE_LOAD;
			response = restTemplate.postForObject(url, userRoleVo, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	public ResponseEntity<JSONResponse> getUserDep(UserMasterVO userMasterVo){
		JSONResponse response =new JSONResponse();
		try{
			
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_USER_DEP;
			response = restTemplate.postForObject(url, userMasterVo, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	public ResponseEntity<JSONResponse> loadUserLocationDetails(ScreenJsonVO screenJsonVO){
		JSONResponse response =new JSONResponse();
		try{
			
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_USER_ROLE_LOAD_USER_LOCATION;
			response = restTemplate.postForObject(url, screenJsonVO, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	public ResponseEntity<JSONResponse> getRoleType( UserRoleTypeVO userRoleTypeVo){
		JSONResponse response =new JSONResponse();
		try{
			
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_USER_ROLE_ROLETYPE;
			response = restTemplate.postForObject(url, userRoleTypeVo, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	public ResponseEntity<JSONResponse> loadUserDepartmentDetails( ScreenJsonVO screenJsonVO){
		JSONResponse response =new JSONResponse();
		try{
			
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_USER_ROLE_LOAD_USER_DEPARTMENT;
			response = restTemplate.postForObject(url, screenJsonVO, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	public ResponseEntity<JSONResponse> loadCity( ScreenJsonVO screenJsonVO){
		JSONResponse response =new JSONResponse();
		try{
			
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_CITY_LOAD;
			response = restTemplate.postForObject(url, screenJsonVO, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	public ResponseEntity<JSONResponse> getAllState( ScreenJsonVO screenJsonVO){
		JSONResponse response =new JSONResponse();
		try{
			
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_STATE_LOAD;
			response = restTemplate.postForObject(url, screenJsonVO, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	public ResponseEntity<JSONResponse> loadCurrency( ScreenJsonVO screenJsonVO){
		JSONResponse response =new JSONResponse();
		try{
			
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_CURRENCY_LOAD;
			response = restTemplate.postForObject(url, screenJsonVO, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
}
