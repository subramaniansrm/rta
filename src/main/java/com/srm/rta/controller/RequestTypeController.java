package com.srm.rta.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.config.LoginAuthException;
import com.srm.coreframework.constants.ControlNameConstants;
import com.srm.coreframework.constants.FilePathConstants;
import com.srm.coreframework.controller.CommonController;
import com.srm.coreframework.response.JSONResponse;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.util.ValidationUtil;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.coreframework.vo.CommonVO;
import com.srm.coreframework.vo.ScreenJsonVO;
import com.srm.rta.service.RequestTypeService;
import com.srm.rta.vo.RequestTypeVO;

/**
 * This Controller is used to Create , Update , Delete , Get all , Get all
 * search , Load in the Request Type .
 * 
 *
 */

@RestController
public class RequestTypeController extends CommonController<RequestTypeVO> {

	private static final Logger logger = LogManager.getLogger(RequestTypeController.class);
	
	@Autowired
	RequestTypeService requestTypeService;

	/**
	 * Method is used to get all the Request type.
	 * 
	 * @return response Response
	 * @throws CommonException
	 * @throws BusinessException
	 */
	@PostMapping(FilePathConstants.REQUEST_TYPE_LIST)
	@ResponseBody
	public ResponseEntity<JSONResponse> getAll(HttpServletRequest request,@RequestBody ScreenJsonVO screenJson) {
		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			CommonVO commonVO = requestTypeService.getScreenFields(screenJson,authDetailsVo);

			List<RequestTypeVO> requestTypeActionVoList = requestTypeService.getAll(authDetailsVo);

			logger.info(getMessage("processValidation",authDetailsVo));

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(requestTypeActionVoList);
			jsonResponse.setAuthSuccesObject(commonVO);
			jsonResponse.setResponseMessage(getMessage("successMessage",authDetailsVo));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}

	@PostMapping(FilePathConstants.REQUEST_TYPE_SEARCH)
	@ResponseBody
	public ResponseEntity<JSONResponse> getAllSearch(HttpServletRequest request,@RequestBody RequestTypeVO requestTypeVo) {

		JSONResponse jsonResponse = new JSONResponse();

		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			List<RequestTypeVO> requestTypeActionVoList = requestTypeService.getAllSearch(requestTypeVo,authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(requestTypeActionVoList);
			jsonResponse.setResponseMessage(getMessage("successMessage",authDetailsVo));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

		} catch (CommonException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}

	@PostMapping(FilePathConstants.REQUEST_TYPE_CREATE)
	@ResponseBody
	public ResponseEntity<JSONResponse> create(HttpServletRequest request,@RequestBody RequestTypeVO requestTypeVo) {

		JSONResponse jsonResponse = new JSONResponse();

		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			logger.info(getMessage("processValidation",authDetailsVo));
			saveValte(requestTypeVo,authDetailsVo);
			logger.info(getMessage("processValidationCompleted",authDetailsVo));
			requestTypeService.findDuplicate(requestTypeVo,authDetailsVo);

			requestTypeService.create(requestTypeVo,authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(requestTypeVo.getRequestTypeCode() + " - " +getMessage("requestTypeSave",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);

			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}

	@PostMapping(FilePathConstants.REQUEST_TYPE_UPDATE)
	@ResponseBody
	public ResponseEntity<JSONResponse> update(HttpServletRequest request,@RequestBody RequestTypeVO requestTypeVo) {

		JSONResponse jsonResponse = new JSONResponse();

		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			logger.info(getMessage("processValidation",authDetailsVo));

			updteVal(requestTypeVo,authDetailsVo);

			logger.info(getMessage("processValidationCompleted",authDetailsVo));
			requestTypeService.findDuplicate(requestTypeVo,authDetailsVo);

			requestTypeService.update(requestTypeVo,authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(requestTypeVo.getRequestTypeCode() + " - " + getMessage("requestTypeUpdate",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);

			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

		} catch (CommonException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}catch (Exception e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	@PostMapping(FilePathConstants.REQUEST_TYPE_DELETE)
	@ResponseBody
	public ResponseEntity<JSONResponse> delete(HttpServletRequest request,@RequestBody RequestTypeVO requestTypeVo) {

		JSONResponse jsonResponse = new JSONResponse();

		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			logger.info(getMessage("processValidation",authDetailsVo));

			deleteValidate(requestTypeVo,authDetailsVo);

			logger.info(getMessage("processValidationCompleted",authDetailsVo));

			requestTypeService.delete(requestTypeVo,authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("deleteSuccessMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);

			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

		} catch (CommonException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}

	@PostMapping(FilePathConstants.REQUEST_TYPE_LOAD)
	@ResponseBody
	public ResponseEntity<JSONResponse> view(HttpServletRequest request,@RequestBody RequestTypeVO requestTypeVo) {

		JSONResponse jsonResponse = new JSONResponse();

		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			logger.info(getMessage("processValidation",authDetailsVo));
			this.viewValidation(requestTypeVo,authDetailsVo);
			logger.info(getMessage("processValidationCompleted",authDetailsVo));
			CommonVO commonVO = new CommonVO();

			commonVO = requestTypeService.getScreenFields(requestTypeVo.getScreenJson(),authDetailsVo);
			if (requestTypeVo.getRequestTypeId() != 0 && requestTypeVo.getRequestTypeId() != null) {
				this.idValidate(requestTypeVo,authDetailsVo);
			}

			RequestTypeVO requestType = requestTypeService.view(requestTypeVo,authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("successMessage",authDetailsVo));
			jsonResponse.setSuccesObject(requestType);
			jsonResponse.setAuthSuccesObject(commonVO);

			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

		} catch (CommonException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}catch (Exception e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}

	@PostMapping(FilePathConstants.REQUEST_TYPE_ADD)
	@ResponseBody
	public ResponseEntity<JSONResponse> addScreenFields(HttpServletRequest request,@RequestBody RequestTypeVO requestType) {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			CommonVO commonVO = requestTypeService.getScreenFields(requestType.getScreenJson(),authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setAuthSuccesObject(commonVO);
			jsonResponse.setResponseMessage(getMessage("successMessage",authDetailsVo));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
		}catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
		}

		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	/**
	 * This Method is used for view validation in Request Type.
	 * 
	 * @param requestTypeActionVo
	 *            RequestTypeVo
	 * @throws CommonException
	 */
	private void viewValidation(RequestTypeVO requestTypeActionVo,AuthDetailsVo authDetailsVo) throws CommonException {
		if (requestTypeActionVo == null) {
			throw new CommonException(getMessage("common.noRecord",authDetailsVo));
		}

	}

	/**
	 * This Method is used to check the id in Request Type
	 * 
	 * @param requestTypeActionVo
	 *            RequestTypeVo
	 * @throws CommonException
	 */
	private void idValidate(RequestTypeVO requestTypeActionVo,AuthDetailsVo authDetailsVo) throws CommonException {

		if (requestTypeActionVo.getRequestTypeId() == null) {
			throw new CommonException(getMessage("search.validation",authDetailsVo));
		}
	}

	private void saveValte(RequestTypeVO requestTypeVo,AuthDetailsVo authDetailsVo) throws CommonException {

		for (String field : requestTypeVo.getScreenFieldDisplayVoList()) {

			// Request Type Name Validation

			if (ControlNameConstants.REQUEST_TYPE_NAME.equals(field)) {

				if (ValidationUtil.isNullOrBlank(requestTypeVo.getRequestTypeName().trim())) {
					throw new CommonException(getMessage("requestType.requestTypeName.required",authDetailsVo));
				}
				if (ValidationUtil.isNullOrBlanksAndNoSpecialCharacters(requestTypeVo.getRequestTypeName().trim())) {
					throw new CommonException(getMessage("requestType.requestTypeName.spl",authDetailsVo));
				}
				if (requestTypeVo.getRequestTypeName().length() > 250) {
					throw new CommonException(getMessage("requestType.requestTypeName.limit",authDetailsVo));
				}
			}

			// Request Type Validation

			if (ControlNameConstants.REQUEST_TYPE_URL.equals(field)) {

				if (ValidationUtil.isNullOrBlank(requestTypeVo.getRequestTypeUrl().trim())) {
					throw new CommonException(getMessage("requestType.url.required",authDetailsVo));
				}
				if (requestTypeVo.getRequestTypeUrl().length() > 250) {
					throw new CommonException(getMessage("requestType.url.limit",authDetailsVo));
				}
			}

			// Request Type Flag Validation

			if (ControlNameConstants.REQUEST_TYPE_STATUS.equals(field)) {

				if (!requestTypeVo.isRequestTypeIsActive()) {
					throw new CommonException(getMessage("requestType.activeFlag.required",authDetailsVo));
				}
			}
		}
	}

	/**
	 * This Method is used for update validation in Request Type.
	 * 
	 * @param requestTypeActionVo
	 * @throws CommonException
	 */
	private void updteVal(RequestTypeVO requestTypeVo,AuthDetailsVo authDetailsVo) throws CommonException {

		for (String field : requestTypeVo.getScreenFieldDisplayVoList()) {

			// Request Type Id validation
			if (ValidationUtil.isNullOrBlank(requestTypeVo.getRequestTypeId())) {
				throw new CommonException(getMessage("requestType.id.required",authDetailsVo));
			}

			// Request Type Code Validation
			if (ControlNameConstants.REQUEST_TYPE_CODE.equals(field)) {

				if (ValidationUtil.isNullOrBlank(requestTypeVo.getRequestTypeCode())) {
					throw new CommonException(getMessage("requestType.code.required",authDetailsVo));
				}
				if (requestTypeVo.getRequestTypeCode().length() > 10) {
					throw new CommonException(getMessage("requestType.code.limit",authDetailsVo));
				}
			}
			// Request Type Name Validation
			if (ControlNameConstants.REQUEST_TYPE_NAME.equals(field)) {
				if (ValidationUtil.isNullOrBlank(requestTypeVo.getRequestTypeName().trim())) {
					throw new CommonException(getMessage("requestType.requestTypeName.required",authDetailsVo));
				}
				if (requestTypeVo.getRequestTypeName().length() > 250) {
					throw new CommonException(getMessage("requestType.requestTypeName.limit",authDetailsVo));
				}
			}
			// Request Type Validation
			if (ControlNameConstants.REQUEST_TYPE_URL.equals(field)) {

				if (ValidationUtil.isNullOrBlank(requestTypeVo.getRequestTypeUrl().trim())) {
					throw new CommonException(getMessage("requestType.url.required",authDetailsVo));
				}
				if (requestTypeVo.getRequestTypeUrl().length() > 250) {
					throw new CommonException(getMessage("requestType.url.limit",authDetailsVo));
				}
			}
		}
	}

	/**
	 * This Method is used for delete validation in Request Type.
	 * 
	 * @param requestTypeActionVo
	 *            RequestTypeVo
	 * @throws CommonException
	 */
	private void deleteValidate(RequestTypeVO requestTypeActionVo,AuthDetailsVo authDetailsVo) throws CommonException {

		if (null == requestTypeActionVo.getRequestTypeList()) {
			throw new CommonException(getMessage("delete.validation",authDetailsVo));
		}

	}

}
