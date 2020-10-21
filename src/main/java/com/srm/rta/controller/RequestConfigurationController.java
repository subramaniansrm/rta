package com.srm.rta.controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
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
import com.srm.rta.service.RequestConfigurationService;
import com.srm.rta.vo.RequestWorkFlowDetailsVO;
import com.srm.rta.vo.RequestWorkFlowExecuterVO;
import com.srm.rta.vo.RequestWorkFlowSequenceVO;
import com.srm.rta.vo.RequestWorkFlowSlaVO;
import com.srm.rta.vo.RequestWorkFlowVO;

/**
 * 
 * @author vigneshs
 *
 */
@RestController
public class RequestConfigurationController extends CommonController<RequestWorkFlowVO> {

	private static final Logger logger = LogManager.getLogger(RequestConfigurationController.class);

	@Autowired
	RequestConfigurationService requestConfigurationService;

	/**
	 * 
	 * @param screenAuthorizationMaster
	 * @return
	 * @throws IOException
	 */
	@PostMapping(FilePathConstants.REQUEST_WORK_FLOW_LIST)
	@ResponseBody
	public ResponseEntity<JSONResponse> getAll(HttpServletRequest request, @RequestBody ScreenJsonVO screenJson)
			throws IOException {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);

		AuthDetailsVo authDetailsVo = null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			CommonVO commonVO = requestConfigurationService.getScreenFields(screenJson, authDetailsVo);

			// Get the List of Details for requestConfiguration
			List<RequestWorkFlowVO> requestWorkFlowVoList = requestConfigurationService.getAll(authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(requestWorkFlowVoList);
			jsonResponse.setAuthSuccesObject(commonVO);
			jsonResponse.setResponseMessage(getMessage("successMessage", authDetailsVo));
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
			jsonResponse.setResponseMessage(getMessage("errorMessage", authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}

	/**
	 * 
	 * @param requestWorkFlowVO
	 * @return
	 * @throws IOException
	 */
	@PostMapping(FilePathConstants.REQUEST_WORK_FLOW_SEARCH)
	@ResponseBody
	public ResponseEntity<JSONResponse> getAllSearch(HttpServletRequest request,
			@RequestBody RequestWorkFlowVO requestWorkFlowVO) throws IOException {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);

		AuthDetailsVo authDetailsVo = null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			// Get the List of Details for requestWorkFlow by request from
			// user to search
			List<RequestWorkFlowVO> requestWorkFlowVoList = requestConfigurationService.getAllSearch(requestWorkFlowVO,
					authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(requestWorkFlowVoList);
			jsonResponse.setResponseMessage(getMessage("successMessage", authDetailsVo));
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
			jsonResponse.setResponseMessage(getMessage("errorMessage", authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	/**
	 * 
	 * @param requestWorkFlowVO
	 * @return
	 */
	@PostMapping(FilePathConstants.REQUEST_WORK_FLOW_ADD)
	@ResponseBody
	public ResponseEntity<JSONResponse> addScreenFields(HttpServletRequest request,
			@RequestBody RequestWorkFlowVO requestWorkFlowVO) {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);

		AuthDetailsVo authDetailsVo = null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			CommonVO commonVO = requestConfigurationService.getScreenFields(requestWorkFlowVO.getScreenJson(),
					authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setAuthSuccesObject(commonVO);
			jsonResponse.setResponseMessage(getMessage("successMessage", authDetailsVo));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
		} catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}catch (Exception e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage", authDetailsVo));
		}

		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	/**
	 * 
	 * @param requestWorkFlowVO
	 * @return
	 */
	@PostMapping(FilePathConstants.REQUEST_WORK_FLOW_DELETE)
	@ResponseBody
	public ResponseEntity<JSONResponse> delete(HttpServletRequest request,
			@RequestBody RequestWorkFlowVO requestWorkFlowVO) {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);

		AuthDetailsVo authDetailsVo = null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			// Put deleteFlag in db
			requestConfigurationService.delete(requestWorkFlowVO, authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("deleteSuccessMessage", authDetailsVo));
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
			jsonResponse.setResponseMessage(getMessage("deleteErrorMessage", authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	/**
	 * 
	 * @param requestWorkFlowVO
	 * @return
	 */
	@PostMapping(FilePathConstants.REQUEST_WORK_FLOW_CREATE)
	@ResponseBody
	public ResponseEntity<JSONResponse> create(HttpServletRequest request,
			@RequestBody RequestWorkFlowVO requestWorkFlowVO) {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);

		AuthDetailsVo authDetailsVo = null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			logger.info(getMessage("processValidation", authDetailsVo));
			this.saveval(requestWorkFlowVO, authDetailsVo);
			logger.info(getMessage("processValidationCompleted", authDetailsVo));

			// Save the information to db
			requestConfigurationService.create(requestWorkFlowVO, authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			if (null != requestWorkFlowVO.getRequestWorkFlowCode()) {
				jsonResponse.setResponseMessage(requestWorkFlowVO.getRequestWorkFlowCode() + " - "
						+ getMessage("requestConfigSave", authDetailsVo));
			} else {
				jsonResponse.setResponseMessage(getMessage("saveSuccessMessage", authDetailsVo));
			}
			jsonResponse.setSuccesObject(CommonConstant.NULL);
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			e.printStackTrace();
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
			e.printStackTrace();
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("saveErroMessage", authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	/**
	 * 
	 * @param requestWorkFlowVO
	 * @return
	 */
	@PostMapping(FilePathConstants.REQUEST_WORK_FLOW_COPY)
	@ResponseBody
	public ResponseEntity<JSONResponse> copy(HttpServletRequest request,
			@RequestBody RequestWorkFlowVO requestWorkFlowVO) {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			// Save the information to db
			requestConfigurationService.create(requestWorkFlowVO, authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("saveSuccessMessage", authDetailsVo));
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
			jsonResponse.setResponseMessage(getMessage("saveErroMessage", authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	/**
	 * 
	 * @param requestWorkFlowVO
	 * @return
	 * @throws IOException
	 */
	@PostMapping(FilePathConstants.REQUEST_WORK_FLOW_LOAD)
	@ResponseBody
	public ResponseEntity<JSONResponse> view(HttpServletRequest request,
			@RequestBody RequestWorkFlowVO requestWorkFlowVO) throws IOException {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			CommonVO commonVO = requestConfigurationService.getScreenFields(requestWorkFlowVO.getScreenJson(),
					authDetailsVo);

			logger.info(getMessage("processValidation", authDetailsVo));

			this.idValidation(requestWorkFlowVO, authDetailsVo);

			logger.info(getMessage("processValidationCompleted", authDetailsVo));
			// Get the information based on that workFlowId
			RequestWorkFlowVO requestWorkFlow = requestConfigurationService.findrequestWorkFlow(requestWorkFlowVO,
					authDetailsVo);

			requestWorkFlow.setDefaultCommon(1);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("successMessage", authDetailsVo));
			jsonResponse.setSuccesObject(requestWorkFlow);
			jsonResponse.setAuthSuccesObject(commonVO);
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

		} catch (CommonException e) {
			e.printStackTrace();
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage", authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	/**
	 * 
	 * @param requestWorkFlowVO
	 * @return
	 */
	@PostMapping(FilePathConstants.REQUEST_WORK_FLOW_UPDATE)
	@ResponseBody
	public ResponseEntity<JSONResponse> update(HttpServletRequest request,
			@RequestBody RequestWorkFlowVO requestWorkFlowVO) {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			logger.info(getMessage("processValidation", authDetailsVo));

			this.updateVal(requestWorkFlowVO, authDetailsVo);

			logger.info(getMessage("processValidationCompleted", authDetailsVo));

			// Update the information to db
			requestConfigurationService.update(requestWorkFlowVO, authDetailsVo);
			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);

			if (null != requestWorkFlowVO.getRequestWorkFlowCode()) {
				jsonResponse.setResponseMessage(requestWorkFlowVO.getRequestWorkFlowCode() + " - "
						+ getMessage("requestConfigUpdate", authDetailsVo));
			} else {
				jsonResponse.setResponseMessage(getMessage("updateSuccessMessage", authDetailsVo));
			}
			jsonResponse.setSuccesObject(CommonConstant.NULL);
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			e.printStackTrace();
			e.printStackTrace();
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("updateErrorMessage", authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	/**
	 * 
	 * @param requestWorkFlowVO
	 * @return
	 */
	@PostMapping(FilePathConstants.REQUEST_WORK_FLOW_DETAIL_VALIDATION)
	@ResponseBody
	public ResponseEntity<JSONResponse> detailValidation(HttpServletRequest request,
			@RequestBody RequestWorkFlowVO requestWorkFlowVO) {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			// Put deleteFlag in db
			requestConfigurationService.detailValidation(requestWorkFlowVO, authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("successMessage", authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("saveErroMessage", authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	@PostMapping(FilePathConstants.REQUEST_WORK_FLOW_MODIFY_DELETE)
	@ResponseBody
	public ResponseEntity<JSONResponse> modifyDelete(HttpServletRequest request,
			@RequestBody RequestWorkFlowVO requestWorkFlowVO) {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			// Put deleteFlag in db
			requestConfigurationService.modifyDelete(requestWorkFlowVO, authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("deleteSuccessMessage", authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("saveErroMessage", authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	/**
	 * 
	 * @param requestWorkFlowVo
	 * @throws CommonException
	 */
	private void saveval(RequestWorkFlowVO requestWorkFlowVo, AuthDetailsVo authDetailsVo) {

		float lowHeader = 0;
		float mediumHeader = 0;
		float highHeader = 0;

		float lowSeq = 0;
		float mediumSeq = 0;
		float highSeq = 0;

		int count = requestConfigurationService.saveValidation(requestWorkFlowVo, authDetailsVo);
		if (count >= CommonConstant.CONSTANT_ONE) {
			throw new CommonException(getMessage("rc.config.already", authDetailsVo));
		}
		for (String field : requestWorkFlowVo.getScreenFieldDisplayVoList()) {

			if (ControlNameConstants.REQUEST_CONFIG_REQUEST_TYPE.equals(field)) {
				if (ValidationUtil.isNullOrBlank(requestWorkFlowVo.getRequestTypeId())) {
					throw new CommonException(getMessage("rc.requestType.required", authDetailsVo));
				}
			}
			if (ControlNameConstants.REQUEST_CONFIG_REQUEST_SUBTYPE.equals(field)) {
				if (ValidationUtil.isNullOrBlank(requestWorkFlowVo.getRequestSubTypeId())) {
					throw new CommonException(getMessage("rc.requestSubType.required", authDetailsVo));
				}
			}
			if (ControlNameConstants.REQUEST_CONFIG_DESCRIPTION.equals(field)) {
				if (ValidationUtil.isNullOrBlank(requestWorkFlowVo.getReqWorkFlowDescription().trim())) {
					throw new CommonException(getMessage("rc.description.required", authDetailsVo));
				}
				if (ValidationUtil.noSpecialCharacters(requestWorkFlowVo.getReqWorkFlowDescription().trim())) {
					throw new CommonException(getMessage("rc.noSplChar.required", authDetailsVo));
				}
				if (requestWorkFlowVo.getReqWorkFlowDescription().length() >= 250) {
					throw new CommonException(getMessage("rc.description.lengthVal", authDetailsVo));
				}
			}

			HashSet duplicateDetails = new HashSet();

			if (requestWorkFlowVo.getRequestWorkFlowDetailsVoList() != null) {

				if (requestWorkFlowVo.getRequestWorkFlowDetailsVoList().size() > 1) {
					for (RequestWorkFlowDetailsVO requestWorkFlowDetailsVo : requestWorkFlowVo
							.getRequestWorkFlowDetailsVoList()) {

						if (requestWorkFlowDetailsVo.isReqWorkFlowDetailsIsActive()) {

							String detail = String.valueOf(requestWorkFlowDetailsVo.getWorkFlowLocationId())
									.concat("," + String.valueOf(requestWorkFlowDetailsVo.getWorkFlowSublocationId())
											+ "," + String.valueOf(requestWorkFlowDetailsVo.getWorkFlowDepartmentId()));

							if (!duplicateDetails.add(detail)) {

								throw new CommonException(getMessage("rc.duplicate.add", authDetailsVo));

							}

						}
					}

					if (duplicateDetails.size() > 1) {
						String a = null;
						String b = null;
						String c = null;

						Iterator<String> itr = duplicateDetails.iterator();
						while (itr.hasNext()) {
							String[] value = itr.next().split(",");

							int i = 1;
							for (String s : value) {

								if (i == 1) {
									a = s;
								} else if (i == 2) {
									b = s;
								} else if (i == 3) {
									c = s;
								}
								i++;

							}

							recurionDetail(duplicateDetails, Integer.parseInt(a), Integer.parseInt(b),
									Integer.parseInt(c), authDetailsVo);
						}
					}
				}
			}

			if (requestWorkFlowVo.getRequestWorkFlowExecuterVo() != null
					&& !requestWorkFlowVo.getRequestWorkFlowExecuterVo().isEmpty()) {

				HashSet duplicate = new HashSet();

				for (RequestWorkFlowExecuterVO requestWorkFlowExecuterVo : requestWorkFlowVo
						.getRequestWorkFlowExecuterVo()) {

					if (null == requestWorkFlowExecuterVo.getExecuterRoleId()
							&& null == requestWorkFlowExecuterVo.getExecuterUserId()) {
						throw new CommonException(getMessage("rc.details.mandantory", authDetailsVo));
					}

					String executer = String.valueOf(requestWorkFlowExecuterVo.getExecuterLocationId())
							.concat("," + String.valueOf(requestWorkFlowExecuterVo.getExecuterSublocationId()) + ","
									+ String.valueOf(requestWorkFlowExecuterVo.getExecuterDepartmentId()) + ","
									+ String.valueOf(requestWorkFlowExecuterVo.getExecuterRoleId()) + ","
									+ String.valueOf(requestWorkFlowExecuterVo.getExecuterUserId()));

					if (!duplicate.add(executer)) {

						throw new CommonException(getMessage("rc.dupl.executer", authDetailsVo));

					}

					for (RequestWorkFlowSlaVO requestWorkFlowSlaVo : requestWorkFlowExecuterVo
							.getRequestWorkFlowSlaVo()) {

						if (0 == requestWorkFlowSlaVo.getReqWorkFlowSla()) {
							throw new CommonException(getMessage("rc.sla.required", authDetailsVo));
						}
						if (requestWorkFlowSlaVo.getReqWorkFlowSla() > 99999.99) {
							throw new CommonException(getMessage("rc.sla.length", authDetailsVo));
						}
						if (null == requestWorkFlowSlaVo.getReqWorkFlowSlaType()) {
							throw new CommonException(getMessage("rc.slaType.required", authDetailsVo));
						}
					}

					if (requestWorkFlowExecuterVo.getRequestWorkFlowSlaVo().get(0)
							.getReqWorkFlowSlaType() == CommonConstant.CONSTANT_ONE) {

						lowHeader = (int) (requestWorkFlowExecuterVo.getRequestWorkFlowSlaVo().get(0)
								.getReqWorkFlowSla() * 60);

					} else {

						lowHeader = (int) (requestWorkFlowExecuterVo.getRequestWorkFlowSlaVo().get(0)
								.getReqWorkFlowSla() * 24 * 60);

					}
					if (requestWorkFlowExecuterVo.getRequestWorkFlowSlaVo().get(1)
							.getReqWorkFlowSlaType() == CommonConstant.CONSTANT_ONE) {

						mediumHeader = (int) (requestWorkFlowExecuterVo.getRequestWorkFlowSlaVo().get(1)
								.getReqWorkFlowSla() * 60);

					} else {

						mediumHeader = (int) (requestWorkFlowExecuterVo.getRequestWorkFlowSlaVo().get(1)
								.getReqWorkFlowSla() * 24 * 60);

					}
					if (requestWorkFlowExecuterVo.getRequestWorkFlowSlaVo().get(2)
							.getReqWorkFlowSlaType() == CommonConstant.CONSTANT_ONE) {

						highHeader = (int) (requestWorkFlowExecuterVo.getRequestWorkFlowSlaVo().get(2)
								.getReqWorkFlowSla() * 60);

					} else {

						highHeader = (int) (requestWorkFlowExecuterVo.getRequestWorkFlowSlaVo().get(2)
								.getReqWorkFlowSla() * 24 * 60);

					}

					if (!(lowHeader > mediumHeader)) {

						throw new CommonException(getMessage("rc.sla.lowMedium.validation", authDetailsVo));

					} else if (!(mediumHeader > highHeader)) {

						throw new CommonException(getMessage("rc.sla.mediumHigh.validation", authDetailsVo));

					} else if (!(lowHeader > highHeader)) {

						throw new CommonException(getMessage("rc.sla.lowHigh.validation", authDetailsVo));

					}

				}
			} else {
				throw new CommonException(getMessage("rc.executers.required", authDetailsVo));
			}

			for (RequestWorkFlowSequenceVO requestWorkFlowSequenceVo : requestWorkFlowVo
					.getRequestWorkFlowSequenceList()) {
				for (String seqField : requestWorkFlowVo.getScreenFieldDisplayVoList()) {

					if (ControlNameConstants.REQUEST_DETAIL_CONFIG_LEVEL_TYPE_1.equals(seqField)) {
						if (requestWorkFlowSequenceVo.getReqWorkFlowSeqLevelType() == CommonConstant.CONSTANT_THREE) {

							if (ValidationUtil
									.isNullOrBlank(requestWorkFlowSequenceVo.getReqWorkFlowSeqLevelhierarchy())) {
								throw new CommonException(getMessage("rc.seq.hierarchy.required", authDetailsVo));
							}
							if (String.valueOf(requestWorkFlowSequenceVo.getReqWorkFlowSeqLevelhierarchy())
									.length() >= 10) {
								throw new CommonException(getMessage("rc.seq.hierarchy.length", authDetailsVo));
							}
							if (ValidationUtil.onlyDigits(
									String.valueOf(requestWorkFlowSequenceVo.getReqWorkFlowSeqLevelhierarchy()))) {
								throw new CommonException(getMessage("rc.seq.hierarchy.numeric", authDetailsVo));
							}

							for (RequestWorkFlowSlaVO requestWorkFlowSlaVo : requestWorkFlowSequenceVo
									.getRequestWorkFlowSlaVo()) {

								if (0 == requestWorkFlowSlaVo.getReqWorkFlowSla()) {
									throw new CommonException(getMessage("rc.sla.required", authDetailsVo));
								}
								if (requestWorkFlowSlaVo.getReqWorkFlowSla() > 99999.99) {
									throw new CommonException(getMessage("rc.sla.length", authDetailsVo));
								}
								if (null == requestWorkFlowSlaVo.getReqWorkFlowSlaType()) {
									throw new CommonException(getMessage("rc.slaType.required", authDetailsVo));
								}
							}

						}
					}
					if (null == requestWorkFlowSequenceVo.getReqWorkFlowSeqLevelType()) {
						if (ControlNameConstants.REQUEST_DETAIL_CONFIG_USER_LOCATION.equals(seqField)) {
							if (ValidationUtil.isNullOrBlank(requestWorkFlowSequenceVo.getLocationId())) {
								throw new CommonException(getMessage("rc.location.required", authDetailsVo));
							}
						}
						if (ControlNameConstants.REQUEST_DETAIL_CONFIG_SUBLOCATION.equals(seqField)) {
							if (ValidationUtil.isNullOrBlank(requestWorkFlowSequenceVo.getSublocationId())) {
								throw new CommonException(getMessage("rc.sublocation.required", authDetailsVo));
							}
						}
						if (ControlNameConstants.REQUEST_DETAIL_CONFIG_DEPARTMENT.equals(seqField)) {
							if (ValidationUtil.isNullOrBlank(requestWorkFlowSequenceVo.getUserDepartmentId())) {
								throw new CommonException(getMessage("rc.department.required", authDetailsVo));
							}
						}
						if (ControlNameConstants.REQUEST_DETAIL_CONFIG_ROLE.equals(seqField)) {
							if (ValidationUtil.isNullOrBlank(requestWorkFlowSequenceVo.getUserRoleId())) {
								throw new CommonException(getMessage("rc.role.required", authDetailsVo));
							}
						}
					}

					if (requestWorkFlowSequenceVo.getRequestWorkFlowSlaVo().get(0)
							.getReqWorkFlowSlaType() == CommonConstant.CONSTANT_ONE) {

						lowSeq = (int) (requestWorkFlowSequenceVo.getRequestWorkFlowSlaVo().get(0).getReqWorkFlowSla()
								* 60);

					} else {

						lowSeq = (int) (requestWorkFlowSequenceVo.getRequestWorkFlowSlaVo().get(0).getReqWorkFlowSla()
								* 24 * 60);

					}
					if (requestWorkFlowSequenceVo.getRequestWorkFlowSlaVo().get(1)
							.getReqWorkFlowSlaType() == CommonConstant.CONSTANT_ONE) {

						mediumSeq = (int) (requestWorkFlowSequenceVo.getRequestWorkFlowSlaVo().get(1)
								.getReqWorkFlowSla() * 60);

					} else {

						mediumSeq = (int) (requestWorkFlowSequenceVo.getRequestWorkFlowSlaVo().get(1)
								.getReqWorkFlowSla() * 24 * 60);

					}
					if (requestWorkFlowSequenceVo.getRequestWorkFlowSlaVo().get(2)
							.getReqWorkFlowSlaType() == CommonConstant.CONSTANT_ONE) {

						highSeq = (int) (requestWorkFlowSequenceVo.getRequestWorkFlowSlaVo().get(2).getReqWorkFlowSla()
								* 60);

					} else {

						highSeq = (int) (requestWorkFlowSequenceVo.getRequestWorkFlowSlaVo().get(2).getReqWorkFlowSla()
								* 24 * 60);

					}

					if (!(lowSeq > mediumSeq)) {

						throw new CommonException(getMessage("rc.sla.lowMedium.seq.validation", authDetailsVo));

					} else if (!(mediumSeq > highSeq)) {

						throw new CommonException(getMessage("rc.sla.mediumHigh.seq.validation", authDetailsVo));

					} else if (!(lowSeq > highSeq)) {

						throw new CommonException(getMessage("rc.sla.lowHigh.seq.validation", authDetailsVo));

					}

				}
			}
		}

	}

	public void recurionDetail(HashSet duplicateDetails, int a, int b, int c, AuthDetailsVo authDetailsVo) {

		Iterator<String> itr = duplicateDetails.iterator();

		while (itr.hasNext()) {

			int i = 1;

			int d = 0;
			int e = 0;
			int f = 0;

			String[] value = itr.next().split(",");

			for (String s : value) {
				if (i == 1) {
					d = Integer.parseInt(s);
				} else if (i == 2) {
					e = Integer.parseInt(s);
				} else if (i == 3) {
					f = Integer.parseInt(s);
				}

				/*
				 * if (((a == 0 && d != 0) && (b == 0 && e != 0)) || ((a == 0 &&
				 * d != 0) && (b != 0 && e == 0))) { throw new
				 * CommonException(getMessage("rc.duplicate.add",authDetailsVo))
				 * ; } if (((a == 0 && d != 0) && (b == 0 && e != 0)) || ((a ==
				 * 0 && d != 0) && (b != 0 && e == 0)) && (c == 0 && f != 0) ||
				 * ((a == 0 && d != 0) && (b == 0 && e != 0)) || ((a == 0 && d
				 * != 0) && (b != 0 && e == 0)) && (c != 0 && f == 0)) { throw
				 * new
				 * CommonException(getMessage("rc.duplicate.add",authDetailsVo))
				 * ; } if (((a == 0 && d != 0) && (b == e)) || ((a == 0 && d !=
				 * 0) && (b != e)) && (c == 0 && f != 0) || ((a == 0 && d != 0)
				 * && (b == e)) || ((a == 0 && d != 0) && (b != e)) && (c != 0
				 * && f == 0)) { throw new
				 * CommonException(getMessage("rc.duplicate.add",authDetailsVo))
				 * ; } if (((a == d) && (b == e)) || ((a != d) && (b != e)) &&
				 * (c == 0 && f != 0) || ((a == d) && (b == e)) || ((a != d) &&
				 * (b != e)) && (c != 0 && f == 0)) { throw new
				 * CommonException(getMessage("rc.duplicate.add",authDetailsVo))
				 * ; }
				 */

				i++;

			}

			if (a == d && b == e && c == f) {

			} else {
				if (a == 0 && b == 0 && c == 0) {
					throw new CommonException(getMessage("rc.duplicate.add", authDetailsVo));
				} else if (d == 0 && e == 0 && f == 0) {
					throw new CommonException(getMessage("rc.duplicate.add", authDetailsVo));
				} /*
					 * else if (d == 0 && (b == e || b == 0 || e == 0) && (c ==
					 * f || c == 0 || f == 0)) {
					 * 
					 * throw new CommonException(getMessage("rc.duplicate.add",
					 * authDetailsVo)); } else if ((a == d || a == 0 || d == 0)
					 * && e == 0 && (c == f || c == 0 || f == 0)) { throw new
					 * CommonException(getMessage("rc.duplicate.add",
					 * authDetailsVo)); } else if ((a == d || a == 0) && (b == e
					 * || b == 0 || e == 0) && f == 0) { throw new
					 * CommonException(getMessage("rc.duplicate.add",
					 * authDetailsVo)); }
					 */

				if (a == 0 && d == 0) {
					if (b == 0 && e == 0) {
						if (c == f && c != 0 && f != 0) {
							throw new CommonException(getMessage("rc.duplicate.add", authDetailsVo));
						} /*
							 * else if (c == 0 && f != 0) { throw new
							 * CommonException(getMessage("rc.duplicate.add",
							 * authDetailsVo)); } else if (c != 0 && f == 0) {
							 * throw new
							 * CommonException(getMessage("rc.duplicate.add",
							 * authDetailsVo)); }
							 */

					} else if (b == e && b != 0 && e != 0) {
						if (c == f && c != 0 && f != 0) {
							throw new CommonException(getMessage("rc.duplicate.add", authDetailsVo));
						} else if (c == 0 && f != 0) {
							throw new CommonException(getMessage("rc.duplicate.add", authDetailsVo));
						} else if (c != 0 && f == 0) {
							throw new CommonException(getMessage("rc.duplicate.add", authDetailsVo));
						}

					} else if (b == 0 && e != 0) {
						if (c == f && c != 0 && f != 0) {
							throw new CommonException(getMessage("rc.duplicate.add", authDetailsVo));
						} /*
							 * else if (c == 0 && f != 0) { throw new
							 * CommonException(getMessage("rc.duplicate.add",
							 * authDetailsVo)); } else if (c != 0 && f == 0) {
							 * throw new
							 * CommonException(getMessage("rc.duplicate.add",
							 * authDetailsVo)); }
							 */

					} else if (b != 0 && e == 0) {
						if (c == f && c != 0 && f != 0) {
							throw new CommonException(getMessage("rc.duplicate.add", authDetailsVo));
						} /*
							 * else if (c == 0 && f != 0) { throw new
							 * CommonException(getMessage("rc.duplicate.add",
							 * authDetailsVo)); } else if (c != 0 && f == 0) {
							 * throw new
							 * CommonException(getMessage("rc.duplicate.add",
							 * authDetailsVo)); }
							 */

					}

				} else if (a == d && a != 0 && d != 0) {
					if (b == 0 && e == 0) {
						if (c == f && c != 0 && f != 0) {
							throw new CommonException(getMessage("rc.duplicate.add", authDetailsVo));
						} else if (c == 0 && f != 0) {
							throw new CommonException(getMessage("rc.duplicate.add", authDetailsVo));
						} else if (c != 0 && f == 0) {
							throw new CommonException(getMessage("rc.duplicate.add", authDetailsVo));
						}

					} else if (b == e && b != 0 && e != 0) {
						if (c == f && c != 0 && f != 0) {
							throw new CommonException(getMessage("rc.duplicate.add", authDetailsVo));
						} else if (c == 0 && f != 0) {
							throw new CommonException(getMessage("rc.duplicate.add", authDetailsVo));
						} else if (c != 0 && f == 0) {
							throw new CommonException(getMessage("rc.duplicate.add", authDetailsVo));
						}

					} else if (b == 0 && e != 0) {
						if (c == f && c != 0 && f != 0) {
							throw new CommonException(getMessage("rc.duplicate.add", authDetailsVo));
						} else if (c == 0 && f != 0) {
							throw new CommonException(getMessage("rc.duplicate.add", authDetailsVo));
						} else if (c != 0 && f == 0) {
							throw new CommonException(getMessage("rc.duplicate.add", authDetailsVo));
						}

					} else if (b != 0 && e == 0) {
						if (c == f && c != 0 && f != 0) {
							throw new CommonException(getMessage("rc.duplicate.add", authDetailsVo));
						} else if (c == 0 && f != 0) {
							throw new CommonException(getMessage("rc.duplicate.add", authDetailsVo));
						} else if (c != 0 && f == 0) {
							throw new CommonException(getMessage("rc.duplicate.add", authDetailsVo));
						}

					}

				} else if (a == 0 && d != 0) {
					if (b == 0 && e == 0) {
						if (c == f && c != 0 && f != 0) {
							throw new CommonException(getMessage("rc.duplicate.add", authDetailsVo));
						} /*
							 * else if (c == 0 && f != 0) { throw new
							 * CommonException(getMessage("rc.duplicate.add",
							 * authDetailsVo)); } else if (c != 0 && f == 0) {
							 * throw new
							 * CommonException(getMessage("rc.duplicate.add",
							 * authDetailsVo)); }
							 */

					} else if (b == e && b != 0 && e != 0) {
						if (c == f && c != 0 && f != 0) {
							throw new CommonException(getMessage("rc.duplicate.add", authDetailsVo));
						} else if (c == 0 && f != 0) {
							throw new CommonException(getMessage("rc.duplicate.add", authDetailsVo));
						} else if (c != 0 && f == 0) {
							throw new CommonException(getMessage("rc.duplicate.add", authDetailsVo));
						}

					} else if (b == 0 && e != 0) {
						if (c == f && c != 0 && f != 0) {
							throw new CommonException(getMessage("rc.duplicate.add", authDetailsVo));
						} /*
							 * else if (c == 0 && f != 0) { throw new
							 * CommonException(getMessage("rc.duplicate.add",
							 * authDetailsVo)); } else if (c != 0 && f == 0) {
							 * throw new
							 * CommonException(getMessage("rc.duplicate.add",
							 * authDetailsVo)); }
							 */

					} else if (b != 0 && e == 0) {
						if (c == f && c != 0 && f != 0) {
							throw new CommonException(getMessage("rc.duplicate.add", authDetailsVo));
						} /*
							 * else if (c == 0 && f != 0) { throw new
							 * CommonException(getMessage("rc.duplicate.add",
							 * authDetailsVo)); } else if (c != 0 && f == 0) {
							 * throw new
							 * CommonException(getMessage("rc.duplicate.add",
							 * authDetailsVo)); }
							 */

					}

				} else if (a != 0 && d == 0) {
					if (b == 0 && e == 0) {
						if (c == f && c != 0 && f != 0) {
							throw new CommonException(getMessage("rc.duplicate.add", authDetailsVo));
						} /*
							 * else if (c == 0 && f != 0) { throw new
							 * CommonException(getMessage("rc.duplicate.add",
							 * authDetailsVo)); } else if (c != 0 && f == 0) {
							 * throw new
							 * CommonException(getMessage("rc.duplicate.add",
							 * authDetailsVo)); }
							 */

					} else if (b == e && b != 0 && e != 0) {
						if (c == f && c != 0 && f != 0) {
							throw new CommonException(getMessage("rc.duplicate.add", authDetailsVo));
						} else if (c == 0 && f != 0) {
							throw new CommonException(getMessage("rc.duplicate.add", authDetailsVo));
						} else if (c != 0 && f == 0) {
							throw new CommonException(getMessage("rc.duplicate.add", authDetailsVo));
						}

					} else if (b == 0 && e != 0) {
						if (c == f && c != 0 && f != 0) {
							throw new CommonException(getMessage("rc.duplicate.add", authDetailsVo));
						} /*
							 * else if (c == 0 && f != 0) { throw new
							 * CommonException(getMessage("rc.duplicate.add",
							 * authDetailsVo)); } else if (c != 0 && f == 0) {
							 * throw new
							 * CommonException(getMessage("rc.duplicate.add",
							 * authDetailsVo)); }
							 */

					} else if (b != 0 && e == 0) {
						if (c == f && c != 0 && f != 0) {
							throw new CommonException(getMessage("rc.duplicate.add", authDetailsVo));
						} /*
							 * else if (c == 0 && f != 0) { throw new
							 * CommonException(getMessage("rc.duplicate.add",
							 * authDetailsVo)); } else if (c != 0 && f == 0) {
							 * throw new
							 * CommonException(getMessage("rc.duplicate.add",
							 * authDetailsVo)); }
							 */

					}

				}

			}

			/*
			 * if (((a == 0 && d != 0) && (b == e) && (c == f)) || ((a != 0 && d
			 * == 0) && (b == e) && (c == f)) || ((a != 0 && d == 0) && (b == e)
			 * && (c == 0 && c != f)) || ((a != 0 && d == 0) && (b == e) && (f
			 * == 0 && c != f))) { throw new
			 * CommonException(getMessage("rc.duplicate.add",authDetailsVo)); }
			 * if (((a == d) && (b == 0 && e != 0) && (c == f)) || ((a == d) &&
			 * (b != 0 && e == 0) && (c == f))) { throw new
			 * CommonException(getMessage("rc.duplicate.add",authDetailsVo)); }
			 * if (((a == d) && (b == e)) && (c == 0 && f != 0) || ((a == d) &&
			 * (b == e)) && (c != 0 && f == 0)) { throw new
			 * CommonException(getMessage("rc.duplicate.add",authDetailsVo)); }
			 * if (a != d && b != e && c == f && c != 0 && f != 0) { throw new
			 * CommonException(getMessage("rc.duplicate.add",authDetailsVo)); }
			 */

		}

	}

	/**
	 * 
	 * @param requestWorkFlowVo
	 * @throws CommonException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void updateVal(RequestWorkFlowVO requestWorkFlowVo, AuthDetailsVo authDetailsVo) {

		float lowHeader;
		float mediumHeader;
		float highHeader;

		float lowSeq;
		float mediumSeq;
		float highSeq;

		if (ValidationUtil.isNullOrBlank(requestWorkFlowVo.getReqWorkFlowId())) {
			throw new CommonException(getMessage("rc.workflowId.required", authDetailsVo));
		}
		for (String field : requestWorkFlowVo.getScreenFieldDisplayVoList()) {

			if (ControlNameConstants.REQUEST_CONFIG_REQUEST_TYPE.equals(field)) {
				if (ValidationUtil.isNullOrBlank(requestWorkFlowVo.getRequestTypeId())) {
					throw new CommonException(getMessage("rc.requestType.required", authDetailsVo));
				}
			}
			if (ControlNameConstants.REQUEST_CONFIG_REQUEST_SUBTYPE.equals(field)) {
				if (ValidationUtil.isNullOrBlank(requestWorkFlowVo.getRequestSubTypeId())) {
					throw new CommonException(getMessage("rc.requestSubType.required", authDetailsVo));
				}
			}
			if (ControlNameConstants.REQUEST_CONFIG_DESCRIPTION.equals(field)) {
				if (ValidationUtil.isNullOrBlank(requestWorkFlowVo.getReqWorkFlowDescription().trim())) {
					throw new CommonException(getMessage("rc.description.required", authDetailsVo));
				}
				if (ValidationUtil.noSpecialCharacters(requestWorkFlowVo.getReqWorkFlowDescription().trim())) {
					throw new CommonException(getMessage("rc.noSplChar.required", authDetailsVo));
				}
				if (requestWorkFlowVo.getReqWorkFlowDescription().length() >= 250) {
					throw new CommonException(getMessage("rc.description.lengthVal", authDetailsVo));
				}
			}

			HashSet duplicateDetails = new HashSet();

			if (requestWorkFlowVo.getRequestWorkFlowDetailsVoList() != null) {

				if (requestWorkFlowVo.getRequestWorkFlowDetailsVoList().size() > 1) {
					for (RequestWorkFlowDetailsVO requestWorkFlowDetailsVo : requestWorkFlowVo
							.getRequestWorkFlowDetailsVoList()) {

						if (requestWorkFlowDetailsVo.isReqWorkFlowDetailsIsActive()) {

							String detail = String.valueOf(requestWorkFlowDetailsVo.getWorkFlowLocationId())
									.concat("," + String.valueOf(requestWorkFlowDetailsVo.getWorkFlowSublocationId())
											+ "," + String.valueOf(requestWorkFlowDetailsVo.getWorkFlowDepartmentId()));

							if (!duplicateDetails.add(detail)) {

								throw new CommonException(getMessage("rc.duplicate.add", authDetailsVo));

							}

						}

					}

					if (duplicateDetails.size() > 1) {
						String a = null;
						String b = null;
						String c = null;

						Iterator<String> itr = duplicateDetails.iterator();
						while (itr.hasNext()) {
							String[] value = itr.next().split(",");

							int i = 1;
							for (String s : value) {

								if (i == 1) {
									a = s;
								} else if (i == 2) {
									b = s;
								} else if (i == 3) {
									c = s;
								}
								i++;

							}

							recurionDetail(duplicateDetails, Integer.parseInt(a), Integer.parseInt(b),
									Integer.parseInt(c), authDetailsVo);
						}

					}
				}
			}
			HashSet duplicate = new HashSet();

			if (null != requestWorkFlowVo.getRequestWorkFlowExecuterVo()) {
				for (RequestWorkFlowExecuterVO requestWorkFlowExecuterVo : requestWorkFlowVo
						.getRequestWorkFlowExecuterVo()) {

					if (null == requestWorkFlowExecuterVo.getExecuterRoleId()
							&& null == requestWorkFlowExecuterVo.getExecuterUserId()) {
						throw new CommonException(getMessage("rc.details.mandantory", authDetailsVo));
					}

					String executer = String.valueOf(requestWorkFlowExecuterVo.getExecuterLocationId())
							.concat("," + String.valueOf(requestWorkFlowExecuterVo.getExecuterSublocationId()) + ","
									+ String.valueOf(requestWorkFlowExecuterVo.getExecuterDepartmentId()) + ","
									+ String.valueOf(requestWorkFlowExecuterVo.getExecuterRoleId()) + ","
									+ String.valueOf(requestWorkFlowExecuterVo.getExecuterUserId()));

					if (!duplicate.add(executer)) {

						throw new CommonException(getMessage("rc.dupl.executer", authDetailsVo));

					}

					for (RequestWorkFlowSlaVO requestWorkFlowSlaVo : requestWorkFlowExecuterVo
							.getRequestWorkFlowSlaVo()) {

						if (0 == requestWorkFlowSlaVo.getReqWorkFlowSla()) {
							throw new CommonException(getMessage("rc.sla.required", authDetailsVo));
						}
						if (requestWorkFlowSlaVo.getReqWorkFlowSla() > 99999.99) {
							throw new CommonException(getMessage("rc.sla.length", authDetailsVo));
						}
						if (null == requestWorkFlowSlaVo.getReqWorkFlowSlaType()) {
							throw new CommonException(getMessage("rc.slaType.required", authDetailsVo));
						}
					}

					if (requestWorkFlowExecuterVo.getRequestWorkFlowSlaVo().get(0)
							.getReqWorkFlowSlaType() == CommonConstant.CONSTANT_ONE) {

						lowHeader = (int) (requestWorkFlowExecuterVo.getRequestWorkFlowSlaVo().get(0)
								.getReqWorkFlowSla() * 60);

					} else {

						lowHeader = (int) (requestWorkFlowExecuterVo.getRequestWorkFlowSlaVo().get(0)
								.getReqWorkFlowSla() * 24 * 60);

					}
					if (requestWorkFlowExecuterVo.getRequestWorkFlowSlaVo().get(1)
							.getReqWorkFlowSlaType() == CommonConstant.CONSTANT_ONE) {

						mediumHeader = (int) (requestWorkFlowExecuterVo.getRequestWorkFlowSlaVo().get(1)
								.getReqWorkFlowSla() * 60);

					} else {

						mediumHeader = (int) (requestWorkFlowExecuterVo.getRequestWorkFlowSlaVo().get(1)
								.getReqWorkFlowSla() * 24 * 60);

					}
					if (requestWorkFlowExecuterVo.getRequestWorkFlowSlaVo().get(2)
							.getReqWorkFlowSlaType() == CommonConstant.CONSTANT_ONE) {

						highHeader = (int) (requestWorkFlowExecuterVo.getRequestWorkFlowSlaVo().get(2)
								.getReqWorkFlowSla() * 60);

					} else {

						highHeader = (int) (requestWorkFlowExecuterVo.getRequestWorkFlowSlaVo().get(2)
								.getReqWorkFlowSla() * 24 * 60);

					}

					if (!(lowHeader > mediumHeader)) {

						throw new CommonException(getMessage("rc.sla.lowMedium.validation", authDetailsVo));

					} else if (!(mediumHeader > highHeader)) {

						throw new CommonException(getMessage("rc.sla.mediumHigh.validation", authDetailsVo));

					} else if (!(lowHeader > highHeader)) {

						throw new CommonException(getMessage("rc.sla.lowHigh.validation", authDetailsVo));

					}

				}
			}

			for (RequestWorkFlowSequenceVO requestWorkFlowSequenceVo : requestWorkFlowVo
					.getRequestWorkFlowSequenceList()) {

				/* if (0 != requestWorkFlowSequenceVo.getReqWorkFlowId()) { */

				for (String seqField : requestWorkFlowVo.getScreenFieldDisplayVoList()) {

					if (ControlNameConstants.REQUEST_DETAIL_CONFIG_LEVEL_TYPE_1.equals(seqField)) {

						if (requestWorkFlowSequenceVo.getReqWorkFlowSeqLevelType() == CommonConstant.CONSTANT_THREE) {

							if (ValidationUtil
									.isNullOrBlank(requestWorkFlowSequenceVo.getReqWorkFlowSeqLevelhierarchy())) {
								throw new CommonException(getMessage("rc.seq.hierarchy.required", authDetailsVo));
							}
							if (String.valueOf(requestWorkFlowSequenceVo.getReqWorkFlowSeqLevelhierarchy())
									.length() >= 10) {
								throw new CommonException(getMessage("rc.seq.hierarchy.length", authDetailsVo));
							}
							if (ValidationUtil.onlyDigits(
									String.valueOf(requestWorkFlowSequenceVo.getReqWorkFlowSeqLevelhierarchy()))) {
								throw new CommonException(getMessage("rc.seq.hierarchy.numeric", authDetailsVo));
							}

							for (RequestWorkFlowSlaVO requestWorkFlowSlaVo : requestWorkFlowSequenceVo
									.getRequestWorkFlowSlaVo()) {

								if (0 == requestWorkFlowSlaVo.getReqWorkFlowSla()) {
									throw new CommonException(getMessage("rc.sla.required", authDetailsVo));
								}
								if (requestWorkFlowSlaVo.getReqWorkFlowSla() > 99999.99) {
									throw new CommonException(getMessage("rc.sla.length", authDetailsVo));
								}
								if (null == requestWorkFlowSlaVo.getReqWorkFlowSlaType()) {
									throw new CommonException(getMessage("rc.slaType.required", authDetailsVo));
								}
							}

						}
					}
					if (null == requestWorkFlowSequenceVo.getReqWorkFlowSeqLevelType()) {
						if (ControlNameConstants.REQUEST_DETAIL_CONFIG_USER_LOCATION.equals(seqField)) {

							if (ValidationUtil.isNullOrBlank(requestWorkFlowSequenceVo.getLocationId())) {
								throw new CommonException(getMessage("rc.location.required", authDetailsVo));
							}
						}
						if (ControlNameConstants.REQUEST_DETAIL_CONFIG_SUBLOCATION.equals(seqField)) {
							if (ValidationUtil.isNullOrBlank(requestWorkFlowSequenceVo.getSublocationId())) {
								throw new CommonException(getMessage("rc.sublocation.required", authDetailsVo));
							}
						}
						if (ControlNameConstants.REQUEST_DETAIL_CONFIG_DEPARTMENT.equals(seqField)) {
							if (ValidationUtil.isNullOrBlank(requestWorkFlowSequenceVo.getUserDepartmentId())) {
								throw new CommonException(getMessage("rc.department.required", authDetailsVo));
							}
						}
						if (ControlNameConstants.REQUEST_DETAIL_CONFIG_ROLE.equals(seqField)) {
							if (ValidationUtil.isNullOrBlank(requestWorkFlowSequenceVo.getUserRoleId())) {
								throw new CommonException(getMessage("rc.role.required", authDetailsVo));
							}
						}
					}

					if (requestWorkFlowSequenceVo.getRequestWorkFlowSlaVo() != null
							&& requestWorkFlowSequenceVo.getRequestWorkFlowSlaVo().size() > 0) {
						if (requestWorkFlowSequenceVo.getRequestWorkFlowSlaVo().get(0)
								.getReqWorkFlowSlaType() == CommonConstant.CONSTANT_ONE) {

							lowSeq = (int) (requestWorkFlowSequenceVo.getRequestWorkFlowSlaVo().get(0)
									.getReqWorkFlowSla() * 60);

						} else {

							lowSeq = (int) (requestWorkFlowSequenceVo.getRequestWorkFlowSlaVo().get(0)
									.getReqWorkFlowSla() * 24 * 60);

						}
						if (requestWorkFlowSequenceVo.getRequestWorkFlowSlaVo().get(1)
								.getReqWorkFlowSlaType() == CommonConstant.CONSTANT_ONE) {

							mediumSeq = (int) (requestWorkFlowSequenceVo.getRequestWorkFlowSlaVo().get(1)
									.getReqWorkFlowSla() * 60);

						} else {

							mediumSeq = (int) (requestWorkFlowSequenceVo.getRequestWorkFlowSlaVo().get(1)
									.getReqWorkFlowSla() * 24 * 60);

						}
						if (requestWorkFlowSequenceVo.getRequestWorkFlowSlaVo().get(2)
								.getReqWorkFlowSlaType() == CommonConstant.CONSTANT_ONE) {

							highSeq = (int) (requestWorkFlowSequenceVo.getRequestWorkFlowSlaVo().get(2)
									.getReqWorkFlowSla() * 60);

						} else {

							highSeq = (int) (requestWorkFlowSequenceVo.getRequestWorkFlowSlaVo().get(2)
									.getReqWorkFlowSla() * 24 * 60);

						}

						if (!(lowSeq > mediumSeq)) {

							throw new CommonException(getMessage("rc.sla.lowMedium.seq.validation", authDetailsVo));

						} else if (!(mediumSeq > highSeq)) {

							throw new CommonException(getMessage("rc.sla.mediumHigh.seq.validation", authDetailsVo));

						} else if (!(lowSeq > highSeq)) {

							throw new CommonException(getMessage("rc.sla.lowHigh.seq.validation", authDetailsVo));

						}

					}
				}

			}
		}
	}

	/**
	 * 
	 * @param requestWorkFlowVo
	 */
	private void idValidation(RequestWorkFlowVO requestWorkFlowVo, AuthDetailsVo authDetailsVo) {

		if (requestWorkFlowVo.getReqWorkFlowId() <= CommonConstant.CONSTANT_ZERO) {
			throw new CommonException(getMessage("search.validation", authDetailsVo));
		}
	}

}
