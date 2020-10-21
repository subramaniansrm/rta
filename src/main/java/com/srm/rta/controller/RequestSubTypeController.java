package com.srm.rta.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
import com.srm.coreframework.exception.CoreException;
import com.srm.coreframework.response.JSONResponse;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.util.ValidationUtil;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.coreframework.vo.CommonVO;
import com.srm.coreframework.vo.ScreenJsonVO;
import com.srm.rta.service.RequestSubTypeService;
import com.srm.rta.vo.RequestSubTypeVO;

@RestController
public class RequestSubTypeController extends CommonController<RequestSubTypeVO> {

	@Autowired
	RequestSubTypeService requestSubTypeService;

	@Autowired
	MessageSource messageSource;

	private static final Logger logger = LogManager.getLogger(RequestSubTypeController.class);

	/**
	 * Method is used to get all the Request type.
	 * 
	 * @return response Response
	 * @throws CommonException
	 * @throws BusinessException
	 */

	@PostMapping(FilePathConstants.REQUEST_SUBTYPE_LIST)
	@ResponseBody
	public ResponseEntity<JSONResponse> getAll(HttpServletRequest request, @RequestBody ScreenJsonVO screenJson) {
		RequestSubTypeVO requestSubTypeVo = new RequestSubTypeVO();
		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			int entityId = authDetailsVo.getUserId();

			CommonVO commonVO = requestSubTypeService.getScreenFields(screenJson, authDetailsVo);

			List<RequestSubTypeVO> requestSubTypeVoList = requestSubTypeService.getAll(authDetailsVo);

			logger.info(getMessage("processValidation"));

			logger.info(getMessage("processValidationCompleted"));

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(requestSubTypeVoList);
			jsonResponse.setAuthSuccesObject(commonVO);
			jsonResponse.setResponseMessage(getMessage("successMessage"));
			logger.info(getMessage("successMessage"));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setSuccesObject(CommonConstant.NULL);
			jsonResponse.setResponseMessage(e.getMessage());
		}catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}  catch (Exception e) {
			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage"));
			jsonResponse.setSuccesObject(CommonConstant.NULL);

		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	@PostMapping(FilePathConstants.REQUEST_SUBTYPE_ADD)
	@ResponseBody
	public ResponseEntity<JSONResponse> addUserMapping(HttpServletRequest request,
			@RequestBody RequestSubTypeVO requestSubTypeVo)
					throws CoreException, CommonException, IllegalAccessException, InvocationTargetException {
		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			CommonVO commonVO = requestSubTypeService.getScreenFields(requestSubTypeVo.getScreenJson(), authDetailsVo);
			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setAuthSuccesObject(commonVO);
			jsonResponse.setResponseMessage(getMessage("successMessage"));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage"));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	@PostMapping(FilePathConstants.REQUEST_SUBTYPE_CREATE)
	@ResponseBody
	public ResponseEntity<JSONResponse> create(HttpServletRequest request,
			@RequestBody RequestSubTypeVO requestSubTypeMasterVo) {
		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			logger.info(getMessage("processValidation"));
			saveValidate(requestSubTypeMasterVo);
			logger.info(getMessage("processValidationCompleted"));
			requestSubTypeService.findDuplicate(requestSubTypeMasterVo, authDetailsVo);
			requestSubTypeService.create(requestSubTypeMasterVo, authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(requestSubTypeMasterVo.getRequestSubTypeCode()+ " - "+getMessage("requestSubTypeSave"));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			e.printStackTrace();
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}  catch (Exception e) {
			e.printStackTrace();
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("saveErroMessage"));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	@PostMapping(FilePathConstants.REQUEST_SUBTYPE_UPDATE)
	@ResponseBody
	public ResponseEntity<JSONResponse> update(HttpServletRequest request,
			@RequestBody RequestSubTypeVO requestSubTypeMasterVo) {
		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			logger.info(getMessage("processValidation"));

			updateValidate(requestSubTypeMasterVo);

			logger.info(getMessage("processValidationCompleted"));
			requestSubTypeService.findDuplicate(requestSubTypeMasterVo, authDetailsVo);
			requestSubTypeService.create(requestSubTypeMasterVo, authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(requestSubTypeMasterVo.getRequestSubTypeCode()+ " - "+ getMessage("requestSubTypeUpdate"));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("saveErroMessage"));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	@PostMapping(FilePathConstants.REQUEST_SUBTYPE_DELETE)
	@ResponseBody
	public ResponseEntity<JSONResponse> delete(HttpServletRequest request,
			@RequestBody RequestSubTypeVO requestSubTypeMasterVo) {
		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			logger.info(getMessage("processValidation"));

			deleteValidate(requestSubTypeMasterVo);

			logger.info(getMessage("processValidationCompleted"));
			requestSubTypeService.delete(requestSubTypeMasterVo, authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("deleteSuccessMessage"));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("saveErroMessage"));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	@PostMapping(FilePathConstants.REQUEST_SUBTYPE_VIEW)
	@ResponseBody
	public ResponseEntity<JSONResponse> viewRequestSubtype(HttpServletRequest request,
			@RequestBody RequestSubTypeVO requestSubTypeMasterVo) {
		JSONResponse jsonResponse = new JSONResponse();

		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			CommonVO commonVO = requestSubTypeService.getScreenFields(requestSubTypeMasterVo.getScreenJson(),
					authDetailsVo);

			RequestSubTypeVO requestSubTypeVo = new RequestSubTypeVO();

			logger.info(getMessage("processValidation"));

			if (requestSubTypeMasterVo.getRequestSubTypeId() != 0) {
				this.idValidate(requestSubTypeMasterVo);
				requestSubTypeVo.setRequestSubTypeId(requestSubTypeMasterVo.getRequestSubTypeId());
			}
			logger.info(getMessage("processValidationCompleted"));

			requestSubTypeVo = requestSubTypeService.view(requestSubTypeMasterVo, authDetailsVo);

			logger.info(getMessage("processValidation"));
			logger.info(getMessage("processValidationCompleted"));

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(requestSubTypeVo);
			jsonResponse.setAuthSuccesObject(commonVO);
			jsonResponse.setResponseMessage(getMessage("successMessage"));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("saveErroMessage"));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	@PostMapping(FilePathConstants.REQUEST_SUBTYPE_SEARCH)
	@ResponseBody
	public ResponseEntity<JSONResponse> searchRequestSubtype(HttpServletRequest request,
			@RequestBody RequestSubTypeVO requestSubTypeMasterVo) {
		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			List<RequestSubTypeVO> requestSubTypeMasterVoList = requestSubTypeService.searchAll(requestSubTypeMasterVo,
					authDetailsVo);
			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(requestSubTypeMasterVoList);
			jsonResponse.setResponseMessage(getMessage("successMessage"));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("saveErroMessage"));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	/**
	 * This Validation method is to save Request SubType
	 * 
	 * @param RequestSubTypeVO
	 *            RequestSubTypeVO
	 */
	private void saveValidate(RequestSubTypeVO requestSubTypeVo) {

		for (String field : requestSubTypeVo.getScreenFieldDisplayVoList()) {

			// Request SubType Name Validation
			if (ControlNameConstants.REQUEST_SUB_TYPE_NAME.equals(field)) {

				if (ValidationUtil.isNullOrBlank(requestSubTypeVo.getRequestSubTypeName().trim())) {
					throw new CommonException(getMessage("requestSubType.requestSubTypeName.required"));
				}
				if ((requestSubTypeVo.getRequestSubTypeName().length() > 250)) {
					throw new CommonException(getMessage("requestSubType.requestSubTypeName.limit"));
				}
			}

			// Request Type Validation
			if (ControlNameConstants.REQUEST_TYPE_NAME_REQUEST_SUBTYPE.equals(field)) {

				if (ValidationUtil.isNullOrBlank(requestSubTypeVo.getRequestTypeId())) {
					throw new CommonException(getMessage("requestSubType.requestType.required"));
				}
			}

			if (ControlNameConstants.REQUEST_SUB_TYPE_PRIORTY.equals(field)) {

				if (ValidationUtil.isNullOrBlank(requestSubTypeVo.getRequestSubtypePriorty())) {
					throw new CommonException(getMessage("requestSubType.priorty.required"));
				}
			}

			// Request SubType Flag Validation
			if (ControlNameConstants.REQUEST_SUB_TYPE_STATUS.equals(field)) {

				if (null != requestSubTypeVo.getRequestSubTypeIsActive()
						&& requestSubTypeVo.getRequestSubTypeIsActive() == 0) {
					throw new CommonException(getMessage("requestSubType.activeFlag.required"));
				}
			}

			if (ControlNameConstants.REQUEST_SUB_TYPE_STATUS.equals(field)) {

				if (null != requestSubTypeVo.getRequestSubTypeIsActive()
						&& requestSubTypeVo.getRequestSubTypeIsActive() == 0) {
					throw new CommonException(getMessage("requestSubType.activeFlag.required"));
				}
			}

		}
	}

	/**
	 * This Validation method is to update Request SubType
	 * 
	 * @param requestSubTypeVo
	 *            RequestSubTypeVo
	 */
	private void updateValidate(RequestSubTypeVO requestSubTypeVo) {

		// Request SubType Id
		if (ValidationUtil.isNullOrBlank(requestSubTypeVo.getRequestTypeId())) {
			throw new CommonException(getMessage("requestSubType.id.required"));
		}
		for (String field : requestSubTypeVo.getScreenFieldDisplayVoList()) {

			// Request SubType Code Validation
			if (ControlNameConstants.REQUEST_SUB_TYPE_CODE.equals(field)) {

				if (ValidationUtil.isNullOrBlank(requestSubTypeVo.getRequestSubTypeCode())) {
					throw new CommonException(getMessage("requestSubType.code.required"));
				}
				if ((requestSubTypeVo.getRequestSubTypeCode().length() > 10)) {
					throw new CommonException(getMessage("requestSubType.code.limit"));
				}
			}
			// Request SubType Name Validation
			if (ControlNameConstants.REQUEST_SUB_TYPE_NAME.equals(field)) {

				if (ValidationUtil.isNullOrBlank(requestSubTypeVo.getRequestSubTypeName().trim())) {
					throw new CommonException(getMessage("requestSubType.requestSubTypeName.required"));
				}
				if ((requestSubTypeVo.getRequestSubTypeName().length() > 250)) {
					throw new CommonException(getMessage("requestSubType.requestSubTypeName.limit"));
				}
			}
			// Request Type Validation
			if (ControlNameConstants.REQUEST_TYPE_NAME_REQUEST_SUBTYPE.equals(field)) {

				if (ValidationUtil.isNullOrBlank(requestSubTypeVo.getRequestTypeId())) {
					throw new CommonException(getMessage("requestSubType.requestType.required"));
				}
			}

			if (ControlNameConstants.REQUEST_SUB_TYPE_PRIORTY.equals(field)) {

				if (ValidationUtil.isNullOrBlank(requestSubTypeVo.getRequestSubtypePriorty())) {
					throw new CommonException(getMessage("requestSubType.priorty.required"));
				}
			}

		}

	}

	/**
	 * This Validation method is to delete Request SubType
	 * 
	 * @param requestSubTypeVo
	 *            RequestSubTypeVo
	 */
	private void deleteValidate(RequestSubTypeVO requestSubTypeVo) {

		if (null == requestSubTypeVo.getRequestSubTypeList()) {
			throw new CommonException(getMessage("delete.validation"));
		}

	}

	/**
	 * This Validation method is to Load Request SubType
	 * 
	 * @param requestSubTypeMasterVoList
	 *            List<RequestSubTypeVo>
	 */
	/*
	 * private void loadValidation(List<RequestSubTypeVo>
	 * requestSubTypeMasterVoList) { if (requestSubTypeMasterVoList == null ||
	 * requestSubTypeMasterVoList.size() <= 0) { throw new
	 * CommonException(getMessage("common.noRecord")); }
	 * 
	 * }
	 */

	/**
	 * This Validation method is to View Request SubType
	 * 
	 * @param requestSubTypeVo
	 *            RequestSubTypeVo
	 */
	private void viewValidation(RequestSubTypeVO requestSubTypeVo) {
		if (requestSubTypeVo == null) {
			throw new CommonException(getMessage("common.noRecord"));
		}

	}

	/**
	 * This Method is to validate the id.
	 * 
	 * @param requestSubTypeMasterVo
	 *            RequestSubTypeVo
	 */
	private void idValidate(RequestSubTypeVO requestSubTypeMasterVo) {

		if (requestSubTypeMasterVo.getRequestSubTypeId() == 0) {
			throw new CommonException(getMessage("search.validation"));

		}
	}

	protected String getMessage(String code) {
		return getMessage(code, new Object[] {});
	}

	protected String getMessage(String code, Object args[]) {
		return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
	}
}
