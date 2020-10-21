package com.srm.rta.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.config.LoginAuthException;
import com.srm.coreframework.constants.FilePathConstants;
import com.srm.coreframework.controller.CommonController;
import com.srm.coreframework.response.JSONResponse;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.util.ValidationUtil;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.coreframework.vo.CommonVO;
import com.srm.rta.service.RequestConfigurationService;
import com.srm.rta.service.RequestService;
import com.srm.rta.validation.RequestDetailValidation;
import com.srm.rta.validation.RequestValidation;
import com.srm.rta.vo.RequestScreenDetailConfigurationVO;
import com.srm.rta.vo.RequestVO;

import lombok.Data;

/**
 * 
 * @author vigneshs
 *
 */
@RestController
@Data
public class RequestController extends CommonController<RequestVO> {


	@Autowired
	RequestService requestService;

	@Autowired
	RequestDetailValidation requestDetailValidation;

	@Autowired
	RequestValidation requestValidation;

	@Autowired
	RequestConfigurationService requestConfigurationService;
	
	/**
	 * 
	 * @param requestVo
	 * @return
	 * @throws IOException
	 */
	@PostMapping(FilePathConstants.REQUEST_LIST)
	@ResponseBody
	public ResponseEntity<JSONResponse> getAll(HttpServletRequest request, @RequestBody RequestVO requestVo)
			throws IOException {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			CommonVO commonVO = requestService.getScreenFields(requestVo.getScreenJson(), authDetailsVo);

			List<RequestVO> requestVoList = new ArrayList<RequestVO>();
			if (null != commonVO) {
				requestVoList = requestService.getAll(requestVo, authDetailsVo);

			}

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(requestVoList);
			jsonResponse.setAuthSuccesObject(commonVO);
			jsonResponse.setResponseMessage(getMessage("successMessage", authDetailsVo));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			Log.info("Request Controller get All Common Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (LoginAuthException e) {
			Log.info("Request Controller get All Login Auth Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			Log.info("Request Controller get All  Exception",e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage", authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}

	/**
	 * 
	 * @param requestVo
	 * @return
	 * @throws IOException
	 */
	@PostMapping(FilePathConstants.REQUEST_SEARCH)
	@ResponseBody
	public ResponseEntity<JSONResponse> getAllSearch(HttpServletRequest request, @RequestBody RequestVO requestVo)
			throws IOException {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			List<RequestVO> requestVoList = requestService.getAllSearch(requestVo, authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(requestVoList);
			jsonResponse.setResponseMessage(getMessage("successMessage", authDetailsVo));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

		} catch (CommonException e) {
			Log.info("Request Controller get All Search Common Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (LoginAuthException e) {
			Log.info("Request Controller get All Search Login Auth Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			Log.info("Request Controller get All Search  Exception",e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage", authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	/**
	 * 
	 * @param requestVo
	 * @return
	 */
	@PostMapping(FilePathConstants.REQUEST_ADD)
	@ResponseBody
	public ResponseEntity<JSONResponse> addScreenFields(HttpServletRequest request, @RequestBody RequestVO requestVo) {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			requestService.getTransactionValidation(authDetailsVo);

			CommonVO commonVO = requestService.getScreenFields(requestVo.getScreenJson(), authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setAuthSuccesObject(commonVO);
			jsonResponse.setResponseMessage(getMessage("successMessage", authDetailsVo));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

		} catch (CommonException e) {
			e.printStackTrace();
			Log.info("Request Controller get add Common Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_COUNT);
			jsonResponse.setResponseMessage(e.getMessage());
		} catch (LoginAuthException e) {
			Log.info("Request Controller get add Common Login Auth Exception ",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Request Controller get add Common  Exception ",e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage", authDetailsVo));
		}

		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	/**
	 * 
	 * @param requestVo
	 * @return
	 */
	@PostMapping(FilePathConstants.REQUEST_DELETE)
	@ResponseBody
	public ResponseEntity<JSONResponse> cancel(HttpServletRequest request, @RequestBody RequestVO requestVo) {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		String requestCode = null;
		String requestStatus= null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			requestService.deleteEntityRequest(authDetailsVo);

			requestService.validateIsCancel(requestVo, authDetailsVo);
													
			if(null != requestVo.getRequestId()){
				requestCode = getRequestCode(requestVo.getRequestId(),authDetailsVo.getEntityId());
				requestStatus = getRequestStatus(requestVo.getRequestId(),authDetailsVo.getEntityId());
			}
		 
			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			if(null != requestCode && !requestCode.isEmpty() && null != requestStatus && !requestStatus.isEmpty()){
				
				jsonResponse.setResponseMessage(requestCode + " - "+ getMessage("request"+requestStatus+"Successfully",authDetailsVo));
			}else{
				jsonResponse.setResponseMessage(getMessage("cancelSuccessMessage",authDetailsVo));
			}
			 
			
			jsonResponse.setSuccesObject(CommonConstant.NULL);
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			Log.info("Request Controller get cancel Common Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(getMessage(e.getMessage(), authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (LoginAuthException e) {
			Log.info("Request Controller get cancel Login auth Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			Log.info("Request Controller get cancel  Exception",e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("deleteErrorMessage", authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	/**
	 * 
	 * @param uploadingFiles
	 * @param str
	 * @return
	 */
	@RequestMapping(value = FilePathConstants.REQUEST_CREATE, method = RequestMethod.POST, consumes = {
			"multipart/form-data" })
	@ResponseBody
	public ResponseEntity<JSONResponse> create(HttpServletRequest request,
			@RequestParam("file") MultipartFile[] uploadingFiles, @RequestParam("action") String str) {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			for (MultipartFile uploadedFile : uploadingFiles) {
				// File size Validation
				fileSizeValidation(uploadedFile);

			}
			ObjectMapper mapper = new ObjectMapper();

			requestService.getTransactionValidation(authDetailsVo);

			RequestVO requestVo = mapper.readValue(str, RequestVO.class);

			//logger.info(getMessage("processValidation", authDetailsVo));

			String result = requestValidation.validateCreate(requestVo);
			if (!result.equals(CommonConstant.VALIDATION_SUCCESS)) {
				jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
				jsonResponse.setResponseMessage(result);
				return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
			}

			if (requestVo != null && requestVo.getRequestDetailList() != null) {
				result = requestDetailValidation.validateCreate(requestVo.getRequestDetailList());
			}

			if (!result.equals(CommonConstant.VALIDATION_SUCCESS)) {
				jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
				jsonResponse.setResponseMessage(result);
				return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
			}

			//logger.info(getMessage("processValidationCompleted", authDetailsVo));

			RequestVO req = requestService.create(requestVo, uploadingFiles, authDetailsVo);

			addEntityRequest(authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(req.getRequestCode() + " - "+ getMessage("requestCreate", authDetailsVo));
			jsonResponse.setSuccesObject(req);
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

		} catch (CommonException e) {
			e.printStackTrace();
			Log.info("Request Controller get create Common Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(getMessage(e.getMessage(), authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (LoginAuthException e) {
			Log.info("Request Controller get create Login auth Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Request Controller get create  Exception",e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("saveErroMessage", authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	/**
	 * 
	 * @param uploadingFiles
	 * @param str
	 * @return
	 */
	//commented because not used   
	/*@RequestMapping(value = FilePathConstants.REQUEST_MOBILE_CREATE, method = RequestMethod.POST, consumes = {
			"multipart/form-data" })
	@ResponseBody
	public ResponseEntity<JSONResponse> mobileCreate(HttpServletRequest request,
			@RequestParam("file") MultipartFile[] uploadingFiles, @RequestParam("action") String str, int entityId,
			int userId) {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			ObjectMapper mapper = new ObjectMapper();

			RequestVO requestVo = mapper.readValue(str, RequestVO.class);
			requestVo.setEntityId(entityId);
			requestVo.setUserId(userId);

			getTransactionValidation(authDetailsVo);

			//logger.info(getMessage("processValidation", authDetailsVo));

			//logger.info(getMessage("processValidationCompleted", authDetailsVo));

			RequestVO req = requestService.create(requestVo, uploadingFiles, authDetailsVo);
			
			addEntityRequest(authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("saveSuccessMessage", authDetailsVo));
			jsonResponse.setSuccesObject(req);
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

		} catch (CommonException e) {
			Log.info("Request Controller get mobile create Common Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (LoginAuthException e) {
			Log.info("Request Controller get mobile create login auth Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			Log.info("Request Controller get mobile create  Exception",e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("saveErroMessage", authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}
*/
	@PostMapping(FilePathConstants.REQUEST_LOAD)
	@ResponseBody
	public ResponseEntity<JSONResponse> view(HttpServletRequest request, @RequestBody RequestVO requestVo)
			throws IOException {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			CommonVO commonVO = requestService.getScreenFields(requestVo.getScreenJson(), authDetailsVo);

			//logger.info(getMessage("processValidation", authDetailsVo));

			String result = requestValidation.validateLoad(requestVo);

			if (!result.equals(CommonConstant.VALIDATION_SUCCESS)) {
				jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
				jsonResponse.setResponseMessage(result);
				return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
			}

			//logger.info(getMessage("processValidationCompleted", authDetailsVo));

			RequestVO requestVoForLoad = new RequestVO();

			requestVoForLoad = requestService.findRequest(requestVo, authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("successMessage", authDetailsVo));
			jsonResponse.setSuccesObject(requestVoForLoad);
			jsonResponse.setAuthSuccesObject(commonVO);
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

		} catch (CommonException e) {
			e.printStackTrace();
			Log.info("Request Controller get view Common Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (LoginAuthException e) {
			Log.info("Request Controller get mobile create login auth Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Request Controller get mobile create  Exception",e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage", authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	
	@PostMapping(FilePathConstants.RESOLVER_REQUEST_LOAD)
	@ResponseBody
	public ResponseEntity<JSONResponse> resolverView(HttpServletRequest request, @RequestBody RequestVO requestVo)
			throws IOException {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			CommonVO commonVO = requestService.getScreenFields(requestVo.getScreenJson(), authDetailsVo);

			//logger.info(getMessage("processValidation", authDetailsVo));

			String result = requestValidation.validateLoad(requestVo);

			if (!result.equals(CommonConstant.VALIDATION_SUCCESS)) {
				jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
				jsonResponse.setResponseMessage(result);
				return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
			}

			//logger.info(getMessage("processValidationCompleted", authDetailsVo));

			RequestVO requestVoForLoad = new RequestVO();

			requestVoForLoad = requestService.resolverRequestView(requestVo, authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("successMessage", authDetailsVo));
			jsonResponse.setSuccesObject(requestVoForLoad);
			jsonResponse.setAuthSuccesObject(commonVO);
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

		} catch (CommonException e) {
			e.printStackTrace();
			Log.info("Request Controller get view Common Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (LoginAuthException e) {
			Log.info("Request Controller get mobile create login auth Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Request Controller get mobile create  Exception",e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage", authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	
	@RequestMapping(value = FilePathConstants.REQUEST_UPDATE, method = RequestMethod.POST, consumes = {
			"multipart/form-data" })
	@ResponseBody
	public ResponseEntity<JSONResponse> update(HttpServletRequest request,
			@RequestParam("file") MultipartFile[] uploadingFiles, @RequestParam("action") String str) {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			for (MultipartFile uploadedFile : uploadingFiles) {
				// File size Validation
				fileSizeValidation(uploadedFile);

			}
			ObjectMapper mapper = new ObjectMapper();

			RequestVO requestVo = mapper.readValue(str, RequestVO.class);

			String result = requestValidation.validateUpdate(requestVo);

			if (!result.equals(CommonConstant.VALIDATION_SUCCESS)) {
				jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
				jsonResponse.setResponseMessage(result);
				return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
			}
			if (requestVo != null && requestVo.getRequestDetailList() != null) {

				result = requestDetailValidation.validateUpdate(requestVo.getRequestDetailList());
			}
			if (!result.equals(CommonConstant.VALIDATION_SUCCESS)) {
				jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
				jsonResponse.setResponseMessage(result);
				return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
			}

			//logger.info(getMessage("processValidationCompleted", authDetailsVo));

			requestService.getTransactionValidation(authDetailsVo);

			RequestVO req = requestService.create(requestVo, uploadingFiles, authDetailsVo);
			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("updateSuccessMessage", authDetailsVo));
			jsonResponse.setSuccesObject(req);
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			Log.info("Request Controller get update Common Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (LoginAuthException e) {
			Log.info("Request Controller get update login auth Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Request Controller get update  Exception",e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("updateErrorMessage", authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	@PostMapping(FilePathConstants.REQUEST_SCREEN_LIST)
	@ResponseBody
	public ResponseEntity<JSONResponse> getAllScreenDetail(HttpServletRequest request,
			@RequestBody RequestVO requestVo) {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			List<RequestScreenDetailConfigurationVO> list_RequestScreenDetailConfigurationVo = requestService
					.getAllScreenDetail(requestVo, authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("successMessage", authDetailsVo));
			jsonResponse.setSuccesObject(list_RequestScreenDetailConfigurationVo);
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			Log.info("Request Controller get All Screen Detail Common Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (LoginAuthException e) {
			Log.info("Request Controller get All Screen Detail login auth Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			Log.info("Request Controller get All Screen Detail  Exception",e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("saveErroMessage", authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	@PostMapping(FilePathConstants.REQUEST_RE_OPEN)
	@ResponseBody
	public ResponseEntity<JSONResponse> reOpen(HttpServletRequest request, @RequestBody RequestVO requestVo) {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		String requestCode = null;
		String requestStatus= null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			reopenValidation(requestVo, authDetailsVo);

			requestService.reopen(requestVo, authDetailsVo);
			if(null != requestVo.getRequestId()){
				requestCode = getRequestCode(requestVo.getRequestId(),authDetailsVo.getEntityId());
			}
			if(null != requestVo.getRequestId()){
				
				requestStatus = getRequestStatus(requestVo.getRequestId(),authDetailsVo.getEntityId());
			}
			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			if(null != requestCode && !requestCode.isEmpty() && null != requestStatus && !requestStatus.isEmpty()){
				System.out.printf("request"+requestStatus+"Successfully",authDetailsVo);
				jsonResponse.setResponseMessage(requestCode + " - "+ getMessage("request"+requestStatus+"Successfully",authDetailsVo));
			}else{
				jsonResponse.setResponseMessage(getMessage("updateSuccessMessage",authDetailsVo));
			}
			jsonResponse.setSuccesObject(CommonConstant.NULL);
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			e.printStackTrace();
			Log.info("Request Controller get reopen Common Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (LoginAuthException e) {
			Log.info("Request Controller get reopen login auth Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Request Controller get reopen  Exception",e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("saveErroMessage", authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	@PostMapping(FilePathConstants.REQUEST_VO)
	@ResponseBody
	public ResponseEntity<JSONResponse> getVo(HttpServletRequest req, @RequestBody RequestVO requestVo) {
		String accessToken = getHeaderAccessToken(req);
		AuthDetailsVo authDetailsVo = null;
		JSONResponse jsonResponse = new JSONResponse();
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			RequestVO request = requestService.findUser(authDetailsVo);
			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("successMessage", authDetailsVo));
			jsonResponse.setSuccesObject(request);
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			e.printStackTrace();
			Log.info("Request Controller get Vo Common Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (LoginAuthException e) {
			Log.info("Request Controller get Vo login auth Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			Log.info("Request Controller get Vo  Exception",e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("saveErroMessage", authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	@PostMapping(FilePathConstants.REQUEST_WORKFLOW_STATUS)
	@ResponseBody
	public ResponseEntity<JSONResponse> workFlowStatus(HttpServletRequest request, @RequestBody RequestVO requestVo) {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			requestService.getTransactionValidation(authDetailsVo);

			int count = requestService.saveValidation(requestVo, authDetailsVo);

			if (count > 0) {
				throw new CommonException("request.duplicate.val");
			}

			requestService.findWorkFlowStatus(requestVo, authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("successMessage", authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

		} catch (CommonException e) {
			Log.info("Request Controller get All workflow Exception",e);
			
			if(e.getMessage().equals("request.duplicate.val")){
				jsonResponse.setResponseCode(CommonConstant.FAILURE_COUNT);
			}else{
				jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			}				
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (LoginAuthException e) {
			Log.info("Request Controller get All login auth Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			Log.info("Request Controller get All  Exception",e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("saveErroMessage", authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	/**
	 * Method is used to reopen the validation
	 * 
	 * @param requestVo
	 */
	public void reopenValidation(RequestVO requestVo, AuthDetailsVo authDetailsVo) {

		if (requestVo.getCurrentStatusId() == 7) {

			if (ValidationUtil.isNullOrBlank(requestVo.getRemarks())) {

				throw new CommonException(getMessage("remark.required", authDetailsVo));

			}
		}
	}

	@PostMapping(FilePathConstants.REQUEST_ATTACHMENT_DOWNLOAD)
	@ResponseBody
	public ResponseEntity<Resource> getDownload(HttpServletRequest request, HttpServletResponse response,
			@RequestBody RequestVO requestVo) {

		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			requestVo = requestService.attachmentDownload(requestVo, authDetailsVo);

			File file = new File(requestVo.getRequestAttachment());

			String path = file.getName();
			System.out.println("Path----->" + path);

			if (file.exists()) {
				InputStreamResource fileInputStream = new InputStreamResource(new FileInputStream(file));
				return ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename =\"" + path + "\"")
						.contentLength(file.length()).contentType(getMediaType(file.getName())).body(fileInputStream);
			}
		} catch (IOException e) {
			//logger.error(e.getMessage());
		} catch (LoginAuthException e) {
			//logger.error("error", e);
		} catch (Exception e) {
			//logger.error(e.getMessage());
		}
		return null;
	}

	/**
	 * Method used to get Media Type of files
	 * 
	 * @param filename
	 * @return
	 */
	public MediaType getMediaType(String filename) {

		String arr[] = filename.split("\\.");
		String type = arr[arr.length - CommonConstant.CONSTANT_ONE];
		switch (type.toLowerCase()) {
		case "txt":
			return MediaType.TEXT_PLAIN;
		case "png":
			return MediaType.IMAGE_PNG;
		case "jpg":
			return MediaType.IMAGE_JPEG;
		default:
			return MediaType.APPLICATION_OCTET_STREAM;
		}

	}
}
