package com.srm.rta.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.config.LoginAuthException;
import com.srm.coreframework.constants.FilePathConstants;
import com.srm.coreframework.controller.CommonController;
import com.srm.coreframework.response.JSONResponse;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.coreframework.vo.CommonStorageVO;
import com.srm.coreframework.vo.DropdownCommonStorageVO;
import com.srm.coreframework.vo.DropdownLocationVO;
import com.srm.coreframework.vo.DropdownUserMasterVO;
import com.srm.coreframework.vo.LanguageDropDownVo;
import com.srm.coreframework.vo.ScreenDropdownVO;
import com.srm.coreframework.vo.ScreenJsonVO;
import com.srm.coreframework.vo.UserMasterVO;
import com.srm.rta.service.DropdownService;
import com.srm.rta.vo.DropdownRequestSubTypeVO;
import com.srm.rta.vo.DropdownRequestTypeVO;
import com.srm.rta.vo.RequestSubTypeVO;
import com.srm.rta.vo.RequestWorkFlowAuditVO;

import lombok.Data;
@Data
@RestController
public class DropdownController extends CommonController<UserMasterVO> {


	@Autowired
	DropdownService dropdownService;

	

	/*//@PostMapping(FilePathConstants.REST_TEMPLATE_LOCATION_LOAD)
	public ResponseEntity<JSONResponse> loadLocation(@RequestBody ScreenJsonVO screenJson) throws IOException {

		JSONResponse jsonResponse = new JSONResponse();
		try {
			List<DropdownLocationVO> locationList = dropdownService.getAllLocation(screenJson);
			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(locationList);
			jsonResponse.setResponseMessage(getMessage("successMessage"));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage"));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}
	
	
	//@PostMapping(FilePathConstants.DEPARTMENT_LOAD)
	public ResponseEntity<JSONResponse> loadDepartment(@RequestBody UserDepartmentVO departmentVo) throws IOException {

		JSONResponse jsonResponse = new JSONResponse();
		try {
			List<DropdownDepartmentVO> departmentList = dropdownService.getAllDepartment(departmentVo);
			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(departmentList);
			jsonResponse.setResponseMessage(getMessage("successMessage"));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage"));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}
	
	
	//@PostMapping(FilePathConstants.ROLE_LOAD)
	public ResponseEntity<JSONResponse> loadRole(@RequestBody UserRoleVO userRoleVo) throws IOException {

		JSONResponse jsonResponse = new JSONResponse();
		try {
			List<DropdownUserRoleVO> roleList = dropdownService.getAllRole(userRoleVo);
			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(roleList);
			jsonResponse.setResponseMessage(getMessage("successMessage"));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage"));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}
	
	//@PostMapping(FilePathConstants.DIVISIONS_LOAD)
	public ResponseEntity<JSONResponse> loadDivision() throws IOException {

		JSONResponse jsonResponse = new JSONResponse();
		try {
			List<DivisionMasterVO> divisionList = dropdownService.getAllDivision();
			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(divisionList);
			jsonResponse.setResponseMessage(getMessage("successMessage"));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage"));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}*/
	
	
	@PostMapping(FilePathConstants.REQUEST_TYPE_DROPDOWN)
	@ResponseBody
	public ResponseEntity<JSONResponse> getAllTypeList(HttpServletRequest request) throws IOException {
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		JSONResponse jsonResponse = new JSONResponse();
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			List<DropdownRequestTypeVO> listRequestTypeVo = dropdownService.getAllTypeList(authDetailsVo);
			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(listRequestTypeVo);
			jsonResponse.setResponseMessage(getMessage("successMessage",authDetailsVo));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			Log.info("Dropdown Controller getAllTypeList Common Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (LoginAuthException e) {
			Log.info("Dropdown Controller getAllTypeList LoginAuth Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			Log.info("Dropdown Controller getAllTypeList  Exception",e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}
	
	@PostMapping(FilePathConstants.REQUEST_SUBTYPE_DROPDOWN)
	@ResponseBody
	public ResponseEntity<JSONResponse> getAllSubTypeList(HttpServletRequest request,@RequestBody RequestSubTypeVO requestSubTypeVo) throws IOException {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				//get from message.properties
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			List<DropdownRequestSubTypeVO> listRequestSubTypeVo = null;
			if(null != requestSubTypeVo.getRequestTypeId()){
				 listRequestSubTypeVo = dropdownService
						.getAllSubTypeList(requestSubTypeVo.getRequestTypeId(),authDetailsVo);
			}
			

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(listRequestSubTypeVo);
			jsonResponse.setResponseMessage(getMessage("successMessage",authDetailsVo));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			e.printStackTrace();
			Log.info("Dropdown Controller getAllSubTypeList Common Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}catch (LoginAuthException e) {
			Log.info("Dropdown Controller getAllSubTypeList Login Auth Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}  catch (Exception e) {
			e.printStackTrace();
			Log.info("Dropdown Controller getAllSubTypeList  Exception",e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}

	/*//@PostMapping(FilePathConstants.USER_ROLE_LOAD_USER_DEPARTMENT)
	public ResponseEntity<JSONResponse> loadUserDepartmentDetails() throws IOException {

		JSONResponse jsonResponse = new JSONResponse();
		try {
			List<DropdownDepartmentVO> dropdownDepartmentVoList = dropdownService.getLoadUserDepartmentDetails();

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(dropdownDepartmentVoList);
			jsonResponse.setResponseMessage(getMessage("successMessage"));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage"));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}
	
	
	//@PostMapping(FilePathConstants.USER_ROLE_LOAD_USER_LOCATION)
	public ResponseEntity<JSONResponse> loadUserLocationDetails(@RequestBody UserMasterVO userMasterVo) throws IOException {

		JSONResponse jsonResponse = new JSONResponse();
		try {
			List<DropdownLocationVO> dropdownLocationVoList = dropdownService.getLoadUserLocationDetails();

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(dropdownLocationVoList);
			jsonResponse.setResponseMessage(getMessage("successMessage"));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage"));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}*/
	
	
	@PostMapping(FilePathConstants.LOAD_DEPARTMENT_LOCATION)
	@ResponseBody
	public ResponseEntity<JSONResponse> listLocation(HttpServletRequest request,@RequestBody ScreenJsonVO screenJson) throws IOException {
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		JSONResponse jsonResponse = new JSONResponse();
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			List<DropdownLocationVO> locationList = dropdownService.getAllLocation(screenJson,authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(locationList);
			jsonResponse.setResponseMessage(getMessage("successMessage",authDetailsVo));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			Log.info("Dropdown Controller listLocation Common Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (LoginAuthException e) {
			Log.info("Dropdown Controller listLocation LoginAuth Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			Log.info("Dropdown Controller listLocation  Exception",e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}
	
	
	/*//@PostMapping(FilePathConstants.COUNTRY_LOAD)
	public ResponseEntity<JSONResponse> loadCountry() throws IOException {

		JSONResponse jsonResponse = new JSONResponse();
		try {
			List<CountryVO> countryMasterVoList = dropdownService.getAllCountry();

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(countryMasterVoList);
			jsonResponse.setResponseMessage(getMessage("successMessage"));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage"));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}
	
	
	//@PostMapping(FilePathConstants.STATE_LOAD)
	public ResponseEntity<JSONResponse> loadState() throws IOException {

		JSONResponse jsonResponse = new JSONResponse();
		try {
			List<StateVO> stateMasterVoList = dropdownService.getAllState();

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(stateMasterVoList);
			jsonResponse.setResponseMessage(getMessage("successMessage"));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage"));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}
	
	
	//@PostMapping(FilePathConstants.CITY_LOAD)
	public ResponseEntity<JSONResponse> loadCity() throws IOException {

		JSONResponse jsonResponse = new JSONResponse();
		try {
			List<CityVO> cityMasterVoList = dropdownService.getAllCity();

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(cityMasterVoList);
			jsonResponse.setResponseMessage(getMessage("successMessage"));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage"));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}*/
	
	@PostMapping(FilePathConstants.SCREEN_LOAD)
	@ResponseBody
	public ResponseEntity<JSONResponse> loadScreen(HttpServletRequest request) throws IOException {
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		JSONResponse jsonResponse = new JSONResponse();
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			List<ScreenDropdownVO> screenDropdownVoList = dropdownService.getAllScreen(authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(screenDropdownVoList);
			jsonResponse.setResponseMessage(getMessage("successMessage",authDetailsVo));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			Log.info("Dropdown Controller loadScreen Common Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}catch (LoginAuthException e) {
			Log.info("Dropdown Controller loadScreen LoginAuth Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}  catch (Exception e) {
			Log.info("Dropdown Controller loadScreen  Exception",e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}
	
	/*//@PostMapping(FilePathConstants.SUBLOCATION_LOAD)
	public ResponseEntity<JSONResponse> getAllSublocation(@RequestBody SubLocationVO subLocationVo) throws IOException {

		JSONResponse jsonResponse = new JSONResponse();
		try {
			List<DropdownSubLocationVO> subLocationVolist = dropdownService.getAllsublocationList(subLocationVo.getId());

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(subLocationVolist);
			jsonResponse.setResponseMessage(getMessage("successMessage"));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage"));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}
	
	
	//@PostMapping(FilePathConstants.USER_DROPDOWN)
	public ResponseEntity<JSONResponse> getAllUser(@RequestBody UserMasterVO userMasterVo)  throws IOException {

		JSONResponse jsonResponse = new JSONResponse();
		try {
			List<DropdownUserMasterVO> userMasterVoList = dropdownService.getAllUser(userMasterVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(userMasterVoList);
			jsonResponse.setResponseMessage(getMessage("successMessage"));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage"));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}*/
	
	
	@PostMapping(FilePathConstants.USER_LEVEL)
	@ResponseBody
	public ResponseEntity<JSONResponse> getUserLevel(HttpServletRequest request,@RequestBody CommonStorageVO commonStorageVo) throws IOException {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			
			List<DropdownCommonStorageVO> commonStorageVoList = dropdownService.getUserLevel(commonStorageVo,authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(commonStorageVoList);
			jsonResponse.setResponseMessage(getMessage("successMessage",authDetailsVo));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			e.printStackTrace();
			Log.info("Dropdown Controller getUserLevel Common Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}catch (LoginAuthException e) {
			Log.info("Dropdown Controller getUserLevel LoginAuth Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}  catch (Exception e) {
			e.printStackTrace();
			Log.info("Dropdown Controller loadScreen  Exception",e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}
	
	@PostMapping(FilePathConstants.LANGUAGE_DROPDOWN)
	@ResponseBody
	public ResponseEntity<JSONResponse> languageDropdown(HttpServletRequest request,@RequestBody CommonStorageVO commonStorageVo) throws IOException {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			
			List<LanguageDropDownVo> commonStorageVoList = dropdownService.languageDropdown(commonStorageVo,authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(commonStorageVoList);
			jsonResponse.setResponseMessage(getMessage("successMessage",authDetailsVo));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			e.printStackTrace();
			Log.info("Dropdown Controller languageDropdown Common Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}catch (LoginAuthException e) {
			Log.info("Dropdown Controller languageDropdown LoginAuth Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}  catch (Exception e) {
			e.printStackTrace();
			Log.info("Dropdown Controller languageDropdown  Exception",e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}
	
	/*//@PostMapping(FilePathConstants.USER_DEP)
	public ResponseEntity<JSONResponse> getUserDep(@RequestBody UserMasterVO userMasterVo) throws IOException {

		JSONResponse jsonResponse = new JSONResponse();
		try {
			List<DropdownUserMasterVO> userMasterVoList = dropdownService.getUserDep(userMasterVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(userMasterVoList);
			jsonResponse.setResponseMessage(getMessage("successMessage"));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage"));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}
	
	
	//@PostMapping(FilePathConstants.USER_ROLE_ROLETYPE)
	public ResponseEntity<JSONResponse> getRoleType(@RequestBody UserRoleTypeVO userRoleTypeVo) throws IOException {

		JSONResponse jsonResponse = new JSONResponse();
		try {
			List<UserRoleTypeVO> userRoleTypeVoList = dropdownService.getRoleType(userRoleTypeVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(userRoleTypeVoList);
			jsonResponse.setResponseMessage(getMessage("successMessage"));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage"));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}*/
	
	
	@PostMapping(FilePathConstants.RESOLVER_REASSIGN)
	@ResponseBody
	public ResponseEntity<JSONResponse> getReassignUser(HttpServletRequest request,@RequestBody RequestWorkFlowAuditVO requestWorkFlowAuditVo) throws IOException {
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		JSONResponse jsonResponse = new JSONResponse();
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			List<DropdownUserMasterVO> userMasterVoList = dropdownService.getReassignUser(requestWorkFlowAuditVo,authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(userMasterVoList);
			jsonResponse.setResponseMessage(getMessage("successMessage",authDetailsVo));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			Log.info("Dropdown Controller getReassignUser Common Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (LoginAuthException e) {
			Log.info("Dropdown Controller getReassignUser LoginAuth Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			Log.info("Dropdown Controller getReassignUser  Exception",e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}
	
	/**
	 * Method is used for Sub type dropdown
	 * 
	 * @param requestSubTypeVo
	 *            RequestSubTypeVo
	 * @return response Response
	 */
	@PostMapping(FilePathConstants.REQUEST_SUBTYPE_WORKFLOW_DROPDOWN)
	@ResponseBody
	public ResponseEntity<JSONResponse> getAllSubTypeListBasedOnWorkFlow(HttpServletRequest request,@RequestBody RequestSubTypeVO requestSubTypeVo) {
		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			List<RequestSubTypeVO> listRequestSubTypeVo = dropdownService
					.getAllSubTypeListBasedOnWorkFlow(requestSubTypeVo.getRequestTypeId(),authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("successMessage",authDetailsVo));
			jsonResponse.setSuccesObject(listRequestSubTypeVo);

		} catch (CommonException e) {
			Log.info("Dropdown Controller getAllSubTypeListBasedOnWorkFlow Common Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}catch (LoginAuthException e) {
			Log.info("Dropdown Controller getAllSubTypeListBasedOnWorkFlow LoginAuth Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}  catch (Exception e) {
			Log.info("Dropdown Controller getAllSubTypeListBasedOnWorkFlow Exception",e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}
	
}
