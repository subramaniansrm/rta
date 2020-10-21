package com.srm.rta.controller;

import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.config.LoginAuthException;
import com.srm.coreframework.constants.ButtonTypeEnum;
import com.srm.coreframework.constants.ControlNameConstants;
import com.srm.coreframework.constants.FilePathConstants;
import com.srm.coreframework.controller.CommonController;
import com.srm.coreframework.response.JSONResponse;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.util.ValidationUtil;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.coreframework.vo.CommonVO;
import com.srm.coreframework.vo.ScreenJsonVO;
import com.srm.rta.service.RequestScreenConfigurationService;
import com.srm.rta.vo.RequestScreenConfigurationVO;
import com.srm.rta.vo.RequestScreenDetailConfigurationVO;

@RestController
public class RequestScreenConfigurationController extends CommonController<RequestScreenConfigurationVO> {

	Logger logger = LoggerFactory.getLogger(RequestScreenConfigurationController.class);

	@Autowired
	 RequestScreenConfigurationService requestScreenConfigurationService;

	/**
	 * This method is used to get all the requestScreenConfiguration details.
	 *
	 * 
	 * @param RequestScreenConfigurationV
	 *            requestScreenConfigurationVo
	 * @return Response response
	 */
	@PostMapping(FilePathConstants.REQUESTSCREEN_LIST)
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

			CommonVO commonVO = requestScreenConfigurationService.getScreenFields(screenJson,authDetailsVo);

			List<RequestScreenConfigurationVO> requestScreenConfigurationList = requestScreenConfigurationService
					.getAll(authDetailsVo);
			logger.info(getMessage("processValidation",authDetailsVo));

			logger.info(getMessage("processValidationCompleted",authDetailsVo));

			jsonResponse.setSuccesObject(requestScreenConfigurationList);
			jsonResponse.setAuthSuccesObject(commonVO);
			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("successMessage",authDetailsVo));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			e.printStackTrace();
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}catch (Exception e) {
			e.printStackTrace();
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	/**
	 * This method is used to get all the different types of
	 * requestScreenConfiguration detail.
	 * 
	 * @param RequestScreenConfigurationVo
	 *            requestScreenConfigurationVo
	 * @return Response response
	 */
	@PostMapping(FilePathConstants.REQUESTSCREEN_SEARCH)
	@ResponseBody
	public ResponseEntity<JSONResponse> getAllSearch(HttpServletRequest request,
			@RequestBody RequestScreenConfigurationVO requestScreenConfigurationVo) {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			List<RequestScreenConfigurationVO> requestScreenConfigurationList = requestScreenConfigurationService
					.getAllSearch(requestScreenConfigurationVo,authDetailsVo);
			/*
			 * if (requestScreenConfigurationList == null ||
			 * requestScreenConfigurationList.size() <= 0) {
			 * loadValidation(requestScreenConfigurationList); }
			 */
			jsonResponse.setSuccesObject(requestScreenConfigurationList);
			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("successMessage",authDetailsVo));
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
		}catch (Exception e) {
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("saveErroMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	/**
	 * Method is used to add the screen fields of request screen configuration
	 * 
	 * @param requestScreenConfigurationVo
	 * @return response
	 */
	@PostMapping(FilePathConstants.REQUESTSCREEN_ADD)
	@ResponseBody
	public ResponseEntity<JSONResponse> addScreenFields(HttpServletRequest request,
			@RequestBody RequestScreenConfigurationVO requestScreenConfigurationVo) {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			CommonVO commonVO = requestScreenConfigurationService
					.getScreenFields(requestScreenConfigurationVo.getScreenJson(),authDetailsVo);
			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("successMessage",authDetailsVo));
			jsonResponse.setAuthSuccesObject(commonVO);
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
		}catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
		}

		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	/**
	 * This method is used to create a requestScreenConfiguration
	 * 
	 * @param RequestScreenConfigurationVo
	 *            requestScreenConfigurationVo
	 * @return Response response
	 */
	@PostMapping(FilePathConstants.REQUESTSCREEN_CREATE)
	@ResponseBody
	public ResponseEntity<JSONResponse> create(HttpServletRequest request,@RequestBody RequestScreenConfigurationVO requestScreenConfigurationVo) {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			logger.info(getMessage("processValidation",authDetailsVo));

			saveValidate(requestScreenConfigurationVo,authDetailsVo);

			logger.info(getMessage("processValidationCompleted",authDetailsVo));
			requestScreenConfigurationService.requestScreen(requestScreenConfigurationVo,authDetailsVo);			
			requestScreenConfigurationService.checkDuplicateConfigName(requestScreenConfigurationVo,authDetailsVo);
			requestScreenConfigurationService.create(requestScreenConfigurationVo,authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			if (null != requestScreenConfigurationVo.getRequestScreenConfigurationCode()) {
				jsonResponse.setResponseMessage(requestScreenConfigurationVo.getRequestScreenConfigurationCode() + " - "
						+ getMessage("requestScreenConfigSave", authDetailsVo));
			} else {
				jsonResponse.setResponseMessage(getMessage("saveSuccessMessage", authDetailsVo));
			}
			jsonResponse.setSuccesObject(CommonConstant.NULL);
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("saveSuccessMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}

		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	/**
	 * This method is used to load a single value of RequestScreenConfiguration
	 * details.
	 *
	 * 
	 * @param RequestScreenConfigurationVo
	 *            requestScreenConfigurationVo
	 * @return Response response
	 * 
	 */
	@PostMapping(FilePathConstants.REQUESTSCREEN_LOAD)
	@ResponseBody
	public ResponseEntity<JSONResponse> load(HttpServletRequest request,@RequestBody RequestScreenConfigurationVO requestScreenConfigurationVo) {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			CommonVO commonVO = requestScreenConfigurationService
					.getScreenFields(requestScreenConfigurationVo.getScreenJson(),authDetailsVo);

			logger.info(getMessage("processValidation",authDetailsVo));

			if (requestScreenConfigurationVo.getRequestScreenConfigId() != 0) {
				idValidate(requestScreenConfigurationVo,authDetailsVo);
			}

			logger.info(getMessage("processValidationCompleted",authDetailsVo));

			RequestScreenConfigurationVO requestScreenConfigurationViewVo = new RequestScreenConfigurationVO();

			requestScreenConfigurationViewVo = requestScreenConfigurationService
					.findScreenConfig(requestScreenConfigurationVo,authDetailsVo);
			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("successMessage",authDetailsVo));
			jsonResponse.setAuthSuccesObject(commonVO);
			jsonResponse.setSuccesObject(requestScreenConfigurationViewVo);
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
		}catch (Exception e) {
			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	/**
	 * This Method is used to check the id in RequestScreenConfiguration
	 * 
	 * @param RequestScreenConfigurationVo
	 *            requestScreenConfigurationVo
	 */
	private void idValidate(RequestScreenConfigurationVO requestScreenConfigurationVo,AuthDetailsVo authDetailsVo) {
		if (requestScreenConfigurationVo.getRequestScreenConfigId() == 0) {
			throw new CommonException(getMessage("search.validation",authDetailsVo));
		}

	}

	/**
	 * This method is used to update a RequestScreenConfiguration and its
	 * requestScreenConfiguration by referring Id and code.
	 * requestScreenConfiguration code should match with same code which is
	 * already created.
	 * 
	 * @param RequestScreenConfigurationVo
	 *            requestScreenConfigurationVo
	 * @return Response response
	 */
	@PostMapping(FilePathConstants.REQUESTSCREEN_UPDATE)
	@ResponseBody
	public ResponseEntity<JSONResponse> update(HttpServletRequest request,@RequestBody RequestScreenConfigurationVO requestScreenConfigurationVo) {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			logger.info(getMessage("processValidation",authDetailsVo));
			updateValidate(requestScreenConfigurationVo,authDetailsVo);
			logger.info(getMessage("processValidationCompleted",authDetailsVo));

			requestScreenConfigurationService.requestScreen(requestScreenConfigurationVo,authDetailsVo);
			//requestScreenConfigurationService.checkDuplicateConfigName(requestScreenConfigurationVo,authDetailsVo);
			requestScreenConfigurationService.update(requestScreenConfigurationVo,authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			if (null != requestScreenConfigurationVo.getRequestScreenConfigurationCode()) {
				jsonResponse.setResponseMessage(requestScreenConfigurationVo.getRequestScreenConfigurationCode() + " - "
						+ getMessage("requestScreenConfigUpdate", authDetailsVo));
			} else {
				jsonResponse.setResponseMessage(getMessage("saveSuccessMessage", authDetailsVo));
			}
			jsonResponse.setSuccesObject(CommonConstant.NULL);
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	/**
	 * This method is used to delete a requestScreenConfiguration and its
	 * related requestScreenConfiguration by referring list of ids.
	 * 
	 * @param RequestScreenConfigurationVo
	 *            requestScreenConfigurationVo
	 * @return Response response
	 */
	@PostMapping(FilePathConstants.REQUESTSCREEN_DELETE)
	@ResponseBody
	public ResponseEntity<JSONResponse> delete(HttpServletRequest request,@RequestBody RequestScreenConfigurationVO requestScreenConfigurationVo) {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			logger.info(getMessage("processValidation",authDetailsVo));
			logger.info(getMessage("processValidationCompleted",authDetailsVo));

			requestScreenConfigurationService.delete(requestScreenConfigurationVo,authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("deleteSuccessMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("deleteErrorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	/**
	 * This method is used to validate the GetAllSearch of
	 * RequestScreenConfiguration detail.
	 * 
	 * @param userList
	 *            UserMasterVo
	 */
	/*
	 * @SuppressWarnings("unused",authDetailsVo) private void
	 * getAllSearchValidation(RequestScreenConfigurationVo
	 * requestScreenConfigurationVoList) { if (requestScreenConfigurationVoList
	 * == null) { throw new CommonException(getMessage("common.noRecord",authDetailsVo)); } }
	 */

	/**
	 * This method is used to validate the create requestScreenConfiguration
	 * details..
	 * 
	 * @param requestScreenConfigurationVoList
	 *            List<RequestScreenConfigurationVo>
	 */

	@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
	private void saveValidate(RequestScreenConfigurationVO requestScreenConfigurationVo,AuthDetailsVo authDetailsVo) {
		for (String field : requestScreenConfigurationVo.getScreenFieldDisplayVoList()) {
			if (ControlNameConstants.REQUEST_SCREEN_CONFIG_NAME.equals(field)) {
				if (ValidationUtil.isNullOrBlank(requestScreenConfigurationVo.getRequestScreenConfigurationName())) {
					throw new CommonException(
							getMessage("requestscreenconfig.requestScreenConfigurationName.required",authDetailsVo));
				}
				if ((requestScreenConfigurationVo.getRequestScreenConfigurationName().length() > 50)) {
					throw new CommonException(getMessage("requestscreenconfig.requestScreenConfigurationName.limit",authDetailsVo));
				}
			}

			if (ControlNameConstants.REQUEST_SCREEN_CONFIG_TYPE.equals(field)) {
				if (ValidationUtil.isNullOrBlank(requestScreenConfigurationVo.getRequestTypeId())) {
					throw new CommonException(getMessage("requestscreenconfig.requestTypeId.required",authDetailsVo));
				}
			}
			if (ControlNameConstants.REQUEST_SCREEN_CONFIG_SUBTYPE.equals(field)) {
				if (ValidationUtil.isNullOrBlank(requestScreenConfigurationVo.getRequestSubtypeId())) {
					throw new CommonException(getMessage("requestscreenconfig.requestSubtypeId.required",authDetailsVo));
				}
			}
			HashSet seq = new HashSet();
			for (RequestScreenDetailConfigurationVO requestScreenDetailConfigurationVo : requestScreenConfigurationVo
					.getRequestScreenDetailConfigurationVoList()) {

				for (String detailField : requestScreenConfigurationVo.getScreenFieldDisplayVoList()) {
					if (ControlNameConstants.REQUEST_SCREEN_DETAIL_CONFIG_FIELD_NAME.equals(detailField)) {
						if (ValidationUtil.isNullOrBlank(
								requestScreenDetailConfigurationVo.getRequestScreenDetailConfigurationFieldName())) {
							throw new CommonException(getMessage(
									"requestScreenDetailConfiguration.RequestScreenDetailConfigName.required",authDetailsVo));
						}

						if ((requestScreenDetailConfigurationVo.getRequestScreenDetailConfigurationFieldName()
								.length() > 250)) {
							throw new CommonException(
									getMessage("requestScreenDetailConfiguration.RequestScreenDetailConfigName.limit",authDetailsVo));
						}
					}
					if (ControlNameConstants.REQUEST_SCREEN_DETAIL_CONFIG_FIELD_TYPE.equals(detailField)) {

						if (ValidationUtil.isNullOrBlank(
								requestScreenDetailConfigurationVo.getRequestScreenDetailConfigurationFieldType())) {
							throw new CommonException(getMessage(
									"requestscreenconfig.RequestScreenDetailConfiguration FieldType.required",authDetailsVo));
						}

						if (ValidationUtil.isNullOrBlank(
								requestScreenDetailConfigurationVo.getRequestScreenDetailConfigurationFieldType())) {
							throw new CommonException(getMessage(
									"requestScreenDetailConfiguration.RequestScreenDetailConfig.Type.required",authDetailsVo));
						}

						if (ButtonTypeEnum.S.toString().equalsIgnoreCase(
								requestScreenDetailConfigurationVo.getRequestScreenDetailConfigurationFieldType())) {
							StringTokenizer st3 = new StringTokenizer(
									requestScreenDetailConfigurationVo.getRequestScreenDetailConfigurationFieldValue(),
									",");
							if (ValidationUtil.isNullOrBlank(requestScreenDetailConfigurationVo
									.getRequestScreenDetailConfigurationFieldValue().trim())) {
								throw new CommonException(getMessage(
										"requestScreenDetailConfiguration.RequestScreenDetailConfig.value.required",authDetailsVo));
							}
						}
					}
					if (ControlNameConstants.REQUEST_SCREEN_DETAIL_CONFIG_SEQUENCE.equals(detailField)) {

						if (ValidationUtil.isNullOrBlank(
								requestScreenDetailConfigurationVo.getRequestScreenDetailConfigurationSequance())) {
							throw new CommonException(getMessage(
									"requestScreenDetailConfiguration.RequestScreenDetailConfig.sequence.required",authDetailsVo));
						}

						if (!seq.add(
								requestScreenDetailConfigurationVo.getRequestScreenDetailConfigurationSequance())) {

							throw new CommonException(getMessage(
									"requestScreenDetailConfiguration.RequestScreenDetailConfigurationSequance.duplicate",authDetailsVo));

						}

						if ((requestScreenDetailConfigurationVo.getRequestScreenDetailConfigurationSequance() > 99)) {

							throw new CommonException(getMessage(
									"requestScreenDetailConfiguration.RequestScreenDetailConfigurationSequance.limit",authDetailsVo));
						}
					}
				}

			}
		}

	}

	/**
	 * This method is used to validate requestScreenConfiguration update detail.
	 * 
	 * @param RequestScreenConfigurationVoList
	 *            requestScreenConfigurationVo
	 */
	@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
	private void updateValidate(RequestScreenConfigurationVO requestScreenConfigurationVo,AuthDetailsVo authDetailsVo) {

		if (ValidationUtil.isNullOrBlank(requestScreenConfigurationVo.getRequestScreenConfigId())) {
			throw new CommonException(getMessage("requestscreenconfig.requestScreenConfigId.required",authDetailsVo));
		}
		for (String field : requestScreenConfigurationVo.getScreenFieldDisplayVoList()) {
			if (ControlNameConstants.REQUEST_SCREEN_CONFIG_NAME.equals(field)) {
				if (ValidationUtil.isNullOrBlank(requestScreenConfigurationVo.getRequestScreenConfigurationName())) {
					throw new CommonException(
							getMessage("requestscreenconfig.requestScreenConfigurationName.required",authDetailsVo));
				}
				if ((requestScreenConfigurationVo.getRequestScreenConfigurationName().length() > 50)) {
					throw new CommonException(getMessage("requestscreenconfig.requestScreenConfigurationName.limit",authDetailsVo));
				}
			}

			if (ControlNameConstants.REQUEST_SCREEN_CONFIG_TYPE.equals(field)) {
				if (ValidationUtil.isNullOrBlank(requestScreenConfigurationVo.getRequestTypeId())) {
					throw new CommonException(getMessage("requestscreenconfig.requestTypeId.required",authDetailsVo));
				}
			}
			if (ControlNameConstants.REQUEST_SCREEN_CONFIG_SUBTYPE.equals(field)) {

				if (ValidationUtil.isNullOrBlank(requestScreenConfigurationVo.getRequestSubtypeId())) {
					throw new CommonException(getMessage("requestscreenconfig.requestSubtypeId.required",authDetailsVo));
				}
			}

			HashSet seq = new HashSet();
			for (RequestScreenDetailConfigurationVO requestScreenDetailConfigurationVo : requestScreenConfigurationVo
					.getRequestScreenDetailConfigurationVoList()) {
				for (String detailField : requestScreenConfigurationVo.getScreenFieldDisplayVoList()) {

					if (ControlNameConstants.REQUEST_SCREEN_DETAIL_CONFIG_FIELD_NAME.equals(detailField)) {
						if (ValidationUtil.isNullOrBlank(
								requestScreenDetailConfigurationVo.getRequestScreenDetailConfigurationFieldName())) {
							throw new CommonException(getMessage(
									"requestScreenDetailConfiguration.RequestScreenDetailConfigName.required",authDetailsVo));
						}
						if ((requestScreenDetailConfigurationVo.getRequestScreenDetailConfigurationFieldName()
								.length() > 50)) {
							throw new CommonException(
									getMessage("requestScreenDetailConfiguration.RequestScreenDetailConfigName.limit",authDetailsVo));
						}

					}
					if (ControlNameConstants.REQUEST_SCREEN_DETAIL_CONFIG_FIELD_TYPE.equals(detailField)) {

						if (ValidationUtil.isNullOrBlank(
								requestScreenDetailConfigurationVo.getRequestScreenDetailConfigurationFieldType())) {
							throw new CommonException(getMessage(
									"requestscreenconfig.RequestScreenDetailConfigurationFieldType.required",authDetailsVo));

						}

						if (ButtonTypeEnum.S.toString().equalsIgnoreCase(
								requestScreenDetailConfigurationVo.getRequestScreenDetailConfigurationFieldType())) {
							StringTokenizer st3 = new StringTokenizer(
									requestScreenDetailConfigurationVo.getRequestScreenDetailConfigurationFieldValue(),
									",");
							if (ValidationUtil.isNullOrBlank(requestScreenDetailConfigurationVo
									.getRequestScreenDetailConfigurationFieldValue().trim())) {
								throw new CommonException(getMessage(
										"requestScreenDetailConfiguration.RequestScreenDetailConfig.value.required",authDetailsVo));

							}

						}
					}
					if (ControlNameConstants.REQUEST_SCREEN_DETAIL_CONFIG_SEQUENCE.equals(detailField)) {
						if (ValidationUtil.isNullOrBlank(
								requestScreenDetailConfigurationVo.getRequestScreenDetailConfigurationSequance())) {
							throw new CommonException(getMessage(
									"requestScreenDetailConfiguration.RequestScreenDetailConfig.sequence.required",authDetailsVo));
						}

						if (!seq.add(
								requestScreenDetailConfigurationVo.getRequestScreenDetailConfigurationSequance())) {

							throw new CommonException(getMessage(
									"requestScreenDetailConfiguration.RequestScreenDetailConfigurationSequance.duplicate",authDetailsVo));

						}

						if ((requestScreenDetailConfigurationVo.getRequestScreenDetailConfigurationSequance() > 99)) {

							throw new CommonException(getMessage(
									"requestScreenDetailConfiguration.RequestScreenDetailConfigurationSequance.limit",authDetailsVo));
						}
					}
				}
			}
		}

	}

	/**
	 * This method is used to validate the load of
	 * requestScreenConfigurationVoList detail.
	 * 
	 * @param requestScreenConfigurationVoList
	 *            List<RequestScreenConfigurationVo>
	 */
	/*
	 * private void loadValidation(List<RequestScreenConfigurationVo>
	 * requestScreenConfigurationList) { if (requestScreenConfigurationList ==
	 * null || requestScreenConfigurationList.size() <= 0) { throw new
	 * CommonException(getMessage("common.noRecord",authDetailsVo)); } }
	 */

	/**
	 * Method used to copy
	 * 
	 * @param requestScreenConfigurationVo
	 * @return
	 */

	@PostMapping(FilePathConstants.REQUESTSCREEN_COPY)
	@ResponseBody
	public ResponseEntity<JSONResponse> copy(HttpServletRequest request,@RequestBody RequestScreenConfigurationVO requestScreenConfigurationVo) {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			logger.info(getMessage("processValidation",authDetailsVo));

			saveValidate(requestScreenConfigurationVo,authDetailsVo);

			logger.info(getMessage("processValidationCompleted",authDetailsVo));
			// requestScreenConfigService.requestScreen(requestScreenConfigurationVo);
			requestScreenConfigurationService.create(requestScreenConfigurationVo,authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("saveSuccessMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("saveSuccessMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}

		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

}
