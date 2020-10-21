package com.srm.rta.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.formula.functions.T;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.constants.FilePathConstants;
import com.srm.coreframework.controller.CommonController;
import com.srm.coreframework.response.JSONResponse;
import com.srm.coreframework.vo.ScreenJsonVO;
import com.srm.coreframework.vo.SubLocationVO;
import com.srm.coreframework.vo.UserDepartmentVO;
import com.srm.coreframework.vo.UserMasterVO;
import com.srm.coreframework.vo.UserRoleTypeVO;
import com.srm.coreframework.vo.UserRoleVO;
import com.srm.rta.service.DropDownRestTemplateService;

import lombok.Data;

/**
 * @author priyankas
 *
 */
@Data
@RestController
public class DropDownRestTemplateController extends CommonController<T>{
	
	
	@Autowired
	DropDownRestTemplateService dropDownRestTemplateService;
	
	/*
	 * Method to Location list
	 * @param screenJson
	 * @return ResponseEntity
	 */
	@PostMapping(FilePathConstants.LOCATION_LOAD)
	@ResponseBody
	public ResponseEntity<JSONResponse> getLocation(HttpServletRequest request,@RequestBody ScreenJsonVO screenJson) {
		String accessToken = getHeaderAccessToken(request);

		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		
		try {
			screenJson.setAccessToken(accessToken);
			jsonResponse = 	dropDownRestTemplateService.getLocation(screenJson);
			
		} catch (CommonException e) {
			e.printStackTrace();
			Log.info("Dropdown Rest Template Controller getLocation Common Exception",e);
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Dropdown Rest Template Controller getLocation Exception",e);
		}

		return jsonResponse;
	}
	

	@PostMapping(FilePathConstants.DIVISIONS_LOAD)
	@ResponseBody
	public ResponseEntity<JSONResponse> getdivision(HttpServletRequest request,@RequestBody ScreenJsonVO screenJson) {
		String accessToken = getHeaderAccessToken(request);

		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		
		try {
			screenJson.setAccessToken(accessToken);
			jsonResponse = 	dropDownRestTemplateService.getdivision(screenJson);
			
		} catch (CommonException e) {
			e.printStackTrace();
			Log.info("Dropdown Rest Template Controller getDivision Common Exception",e);
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Dropdown Rest Template Controller getDivision Exception",e);
		}

		return jsonResponse;
	}

	/**
	 * Method to return Department List
	 * @param screenJson
	 * @return ResponseEntity
	 */
	@PostMapping(FilePathConstants.DEPARTMENT_LOAD)
	@ResponseBody
	public ResponseEntity<JSONResponse> getDepartment(HttpServletRequest request,@RequestBody UserDepartmentVO departmentVo) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);

		try {
			departmentVo.setAccessToken(accessToken);
			jsonResponse = 	dropDownRestTemplateService.getDepartment(departmentVo);
			
		} catch (CommonException e) {
			e.printStackTrace();
			Log.info("Dropdown Rest Template Controller getDepartment Common Exception",e);
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Dropdown Rest Template Controller getDepartment Exception",e);
		}

		return jsonResponse;
	}
	
	/**
	 * Method to return Role List
	 * @param screenJson
	 * @return ResponseEntity
	 */
	@PostMapping(FilePathConstants.ROLE_LOAD)
	@ResponseBody
	public ResponseEntity<JSONResponse> getRole(HttpServletRequest request,@RequestBody UserRoleVO userRoleVo) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);

		try {
			userRoleVo.setAccessToken(accessToken);
			
			/*Integer entityId = AuthUtil.getEntityId();
			userRoleVo.setEntityId(entityId);
			System.out.println(AuthUtil.getEntityId());*/
			
			jsonResponse = 	dropDownRestTemplateService.getRole(userRoleVo);
			
		} catch (CommonException e) {
			e.printStackTrace();
			Log.info("Dropdown Rest Template Controller getRole Common Exception",e);
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Dropdown Rest Template Controller getRole Exception",e);
		}

		return jsonResponse;
	}
	
	/**Method to load User list
	 * @param screenJson
	 * @return jsonResponse
	 */
	@PostMapping(FilePathConstants.USER_DROPDOWN)
	@ResponseBody
	public ResponseEntity<JSONResponse> getUser(HttpServletRequest request,@RequestBody UserMasterVO userMasterVo) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);
		try {
			userMasterVo.setAccessToken(accessToken);
			jsonResponse = 	dropDownRestTemplateService.getUser(userMasterVo);
			
		} catch (CommonException e) {
			e.printStackTrace();
			Log.info("Dropdown Rest Template Controller getUser Common Exception",e);
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Dropdown Rest Template Controller getUser  Exception",e);
		}

		return jsonResponse;
	}
	

	/**Method to load Country list
	 * @param screenJson
	 * @return jsonResponse
	 */
	@PostMapping(FilePathConstants.COUNTRY_LOAD)
	@ResponseBody
	public ResponseEntity<JSONResponse> getCountryList(HttpServletRequest request,@RequestBody ScreenJsonVO screenJson) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);

		try {
			screenJson.setAccessToken(accessToken);
			jsonResponse = 	dropDownRestTemplateService.getCountryList(screenJson);
			
		} catch (CommonException e) {
			e.printStackTrace();
			Log.info("Dropdown Rest Template Controller getCountryList Common Exception",e);
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Dropdown Rest Template Controller getCountryList  Exception",e);
		}

		return jsonResponse;
	}
	@PostMapping(FilePathConstants.SUBLOCATION_LOAD)
	@ResponseBody
	public ResponseEntity<JSONResponse> getSublocation(HttpServletRequest request,@RequestBody SubLocationVO subLocationVo) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);

		try {
			subLocationVo.setAccessToken(accessToken);
			jsonResponse = 	dropDownRestTemplateService.getSublocation(subLocationVo);
			
		} catch (CommonException e) {
			e.printStackTrace();
			Log.info("Dropdown Rest Template Controller getSublocation Common Exception",e);
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Dropdown Rest Template Controller getSublocation  Exception",e);
		}

		return jsonResponse;
	}
	@PostMapping(FilePathConstants.USER_DEP)
	@ResponseBody
	public ResponseEntity<JSONResponse> getUserDep(HttpServletRequest request,@RequestBody UserMasterVO userMasterVo) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);

		try {
			userMasterVo.setAccessToken(accessToken);
			jsonResponse = 	dropDownRestTemplateService.getUserDep(userMasterVo);
			
		} catch (CommonException e) {
			e.printStackTrace();
			Log.info("Dropdown Rest Template Controller getUserDep Common Exception",e);
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Dropdown Rest Template Controller getUserDep  Exception",e);
		}

		return jsonResponse;
	}
	@PostMapping(FilePathConstants.USER_ROLE_LOAD_USER_LOCATION)
	@ResponseBody
	public ResponseEntity<JSONResponse> loadUserLocationDetails(HttpServletRequest request,@RequestBody ScreenJsonVO screenJsonVO) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);

		try {
			screenJsonVO.setAccessToken(accessToken);
			jsonResponse = 	dropDownRestTemplateService.loadUserLocationDetails(screenJsonVO);
			
		} catch (CommonException e) {
			e.printStackTrace();
			Log.info("Dropdown Rest Template Controller loadUserLocationDetails Common Exception",e);
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Dropdown Rest Template Controller loadUserLocationDetails  Exception",e);
		}

		return jsonResponse;
	}
	
	@PostMapping(FilePathConstants.USER_ROLE_ROLETYPE)
	@ResponseBody
	public ResponseEntity<JSONResponse> getRoleType(HttpServletRequest request,@RequestBody UserRoleTypeVO userRoleTypeVo) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);

		try {
			userRoleTypeVo.setAccessToken(accessToken);
			jsonResponse = 	dropDownRestTemplateService.getRoleType(userRoleTypeVo);
			
		} catch (CommonException e) {
			e.printStackTrace();
			Log.info("Dropdown Rest Template Controller getRoleType Common Exception",e);
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Dropdown Rest Template Controller getRoleType  Exception",e);
		}

		return jsonResponse;
	}
	
	@PostMapping(FilePathConstants.USER_ROLE_LOAD_USER_DEPARTMENT)
	@ResponseBody
	public ResponseEntity<JSONResponse> loadUserDepartmentDetails(HttpServletRequest request,@RequestBody ScreenJsonVO screenJsonVO) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);

		try {
			screenJsonVO.setAccessToken(accessToken);
			jsonResponse = 	dropDownRestTemplateService.loadUserDepartmentDetails(screenJsonVO);
			
		} catch (CommonException e) {
			e.printStackTrace();
			Log.info("Dropdown Rest Template loadUserDepartmentDetails  Common Exception",e);
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Dropdown Rest Template loadUserDepartmentDetails   Exception",e);
		}

		return jsonResponse;
	}
	
	@PostMapping(FilePathConstants.CITY_LOAD)
	@ResponseBody
	public ResponseEntity<JSONResponse> loadCity(HttpServletRequest request,@RequestBody ScreenJsonVO screenJsonVO) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);

		try {
			screenJsonVO.setAccessToken(accessToken);
			jsonResponse = 	dropDownRestTemplateService.loadCity(screenJsonVO);
			
		} catch (CommonException e) {
			e.printStackTrace();
			Log.info("Dropdown Rest Template loadCity Common Exception",e);
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Dropdown Rest Template loadCity Exception",e);
		}

		return jsonResponse;
	}
	@PostMapping(FilePathConstants.STATE_LOAD)
	@ResponseBody
	public ResponseEntity<JSONResponse> loadState(HttpServletRequest request,@RequestBody ScreenJsonVO screenJsonVO) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);

		try {
			screenJsonVO.setAccessToken(accessToken);
			jsonResponse = 	dropDownRestTemplateService.getAllState(screenJsonVO);
			
		} catch (CommonException e) {
			e.printStackTrace();
			Log.info("Dropdown Rest Template loadState Common Exception",e);
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Dropdown Rest Template loadState Exception",e);
		}

		return jsonResponse;
	}
	
	@PostMapping(FilePathConstants.CURRENCY_LOAD)
	@ResponseBody
	public ResponseEntity<JSONResponse> loadCurrency(HttpServletRequest request,@RequestBody ScreenJsonVO screenJsonVO) {
		ResponseEntity<JSONResponse> jsonResponse = new ResponseEntity<JSONResponse>(HttpStatus.ACCEPTED);
		String accessToken = getHeaderAccessToken(request);

		try {
			screenJsonVO.setAccessToken(accessToken);
			jsonResponse = 	dropDownRestTemplateService.loadCurrency(screenJsonVO);
			
		} catch (CommonException e) {
			e.printStackTrace();
			Log.info("Dropdown Rest Template loadState Common Exception",e);
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Dropdown Rest Template loadState Exception",e);
		}

		return jsonResponse;
	}
}
