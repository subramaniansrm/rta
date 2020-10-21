package com.srm.rta.controller;

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
import com.srm.coreframework.response.JSONResponse;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.util.ValidationUtil;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.coreframework.vo.CommonVO;
import com.srm.coreframework.vo.ScreenAuthorizationVO;
import com.srm.coreframework.vo.ScreenJsonVO;
import com.srm.rta.service.FlashNewsService;
import com.srm.rta.vo.FlashNewsVO;

@RestController
public class FlashNewsController extends CommonController<FlashNewsVO>{

	private static final Logger logger = LogManager.getLogger(FlashNewsController.class);
	
	@Autowired
	FlashNewsService flashNewsService;

	@Autowired
	MessageSource messageSource;

	@PostMapping(FilePathConstants.FLASH_NEWS_LOAD)
	@ResponseBody
	public ResponseEntity<JSONResponse> getAll(HttpServletRequest request,@RequestBody ScreenJsonVO screenJson) {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				// get from message.properties
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			
			CommonVO commonVO = flashNewsService.getScreenFields(screenJson,authDetailsVo);
			if (commonVO != null) {

				List<FlashNewsVO> flashNewsList = flashNewsService.getAll(authDetailsVo);

				logger.info(getMessage("processValidation"));

				logger.info(getMessage("processValidationCompleted"));

				jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
				jsonResponse.setSuccesObject(flashNewsList);
				jsonResponse.setAuthSuccesObject(commonVO);
				jsonResponse.setResponseMessage(getMessage("successMessage"));
				logger.info(getMessage("successMessage"));
				return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
			}
		} catch (CommonException e) {
			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setSuccesObject(CommonConstant.NULL);
			jsonResponse.setResponseMessage(e.getMessage());
		} catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}catch (Exception e) {
			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage"));
			jsonResponse.setSuccesObject(CommonConstant.NULL);

		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	/**
	 * Method is used to get all the Flash news details
	 * 
	 * @return response Response
	 */
	@PostMapping(FilePathConstants.FLASH_NEWS_GETALL)
	@ResponseBody
	public ResponseEntity<JSONResponse> getAllList(HttpServletRequest request) {
		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				// get from message.properties
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			List<FlashNewsVO> flashNewsList = flashNewsService.getAll(authDetailsVo);

			logger.info(getMessage("processValidation"));

			logger.info(getMessage("processValidationCompleted"));
			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(flashNewsList);
			jsonResponse.setResponseMessage(getMessage("successMessage"));
			logger.info(getMessage("successMessage"));
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
			jsonResponse.setResponseMessage(getMessage("errorMessage"));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.EXPECTATION_FAILED);
	}

	/**
	 * Method is used to get all the Flash news details
	 * 
	 * @return response Response
	 */
	@PostMapping(FilePathConstants.FLASH_NEWS_LOAD_DASHBOARD)
	@ResponseBody
	public ResponseEntity<JSONResponse> getAllDashboard(HttpServletRequest request,@RequestBody ScreenAuthorizationVO screenAuthorizationMaster) {
		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				// get from message.properties
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			List<FlashNewsVO> flashNewsList = flashNewsService.getAll(authDetailsVo);

			logger.info(getMessage("processValidation"));

			// loadValidation(flashNewsList);

			logger.info(getMessage("processValidationCompleted"));

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(flashNewsList);
			jsonResponse.setResponseMessage(getMessage("successMessage"));
			logger.info(getMessage("successMessage"));
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
			jsonResponse.setResponseMessage(getMessage("errorMessage"));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.EXPECTATION_FAILED);
	}

	/**
	 * Method used to authenticate and authorize user for Create and Update
	 * 
	 * @param flashNewsVo
	 * @return
	 */

	@PostMapping(FilePathConstants.FLASHNEWS_ADD)
	@ResponseBody
	public ResponseEntity<JSONResponse> addUserDepartment(HttpServletRequest request,@RequestBody FlashNewsVO flashNewsVo) {
		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				// get from message.properties
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			CommonVO commonVO = flashNewsService.getScreenFields(flashNewsVo.getScreenJson(),authDetailsVo);
			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setAuthSuccesObject(commonVO);
			jsonResponse.setResponseMessage(getMessage("successMessage"));
			logger.info(getMessage("successMessage"));
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
			jsonResponse.setResponseMessage(getMessage("errorMessage"));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.EXPECTATION_FAILED);
	}

	/**
	 * This Method is used to create the flash news details
	 * 
	 * @param flashNewsVo
	 *            FlashNewsVo
	 * @return response Response
	 */
	@PostMapping(FilePathConstants.FLASH_NEWS_CREATE)
	@ResponseBody
	public ResponseEntity<JSONResponse> create(HttpServletRequest request,@RequestBody FlashNewsVO flashNewsVo) {
		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				// get from message.properties
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			logger.info(getMessage("processValidation"));

			// saveValidation(flashNewsVo);

			logger.info(getMessage("processValidationCompleted"));

			flashNewsService.create(flashNewsVo,authDetailsVo);
			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("saveSuccessMessage"));
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
			jsonResponse.setResponseMessage(getMessage("saveErroMessage"));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.EXPECTATION_FAILED);
	}

	/**
	 * This method is used to update the flash news details
	 * 
	 * @param flashNewsVo
	 *            FlashNewsVo
	 * @return response Response
	 */
	@PostMapping(FilePathConstants.FLASH_NEWS_UPDATE)
	@ResponseBody
	public ResponseEntity<JSONResponse> update(HttpServletRequest request,@RequestBody FlashNewsVO flashNewsVo) {
		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				// get from message.properties
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			logger.info(getMessage("processValidation"));

			// idValidation(flashNewsVo);

			// saveValidation(flashNewsVo);

			logger.info(getMessage("processValidationCompleted"));

			flashNewsService.update(flashNewsVo,authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("updateSuccessMessage"));
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
		}catch (Exception e) {
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("updateErrorMessage"));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.EXPECTATION_FAILED);
	}

	/**
	 * Method is used to validate the id of flash news
	 * 
	 * @param flashNewsVo
	 *            FlashNewsVo
	 *//*
		 * private void idValidation(FlashNewsVO flashNewsVo) {
		 * 
		 * if (flashNewsVo.getId() <= 0 || flashNewsVo.getId() == 0) { throw new
		 * CommonException(getMessage("search.validation")); } }
		 */

	/**
	 * Method is used to validate the save flash news
	 * 
	 * @param flashNewsVo
	 *            FlashNewsVo
	 *//*
		 * private void saveValidation(FlashNewsVO flashNewsVo) {
		 * 
		 * for (String field : flashNewsVo.getScreenFieldDisplayVoList()) {
		 * 
		 * // Flash News Type Validation
		 * 
		 * if (field.equals(ControlNameConstants.FLASH_NEWS_TYPE)) { if
		 * (ValidationUtil.isNullOrBlank(flashNewsVo.getFlashNewsType())) {
		 * throw new CommonException(getMessage("flashnews.type.required")); } }
		 * 
		 * // Flash News Description Validation
		 * 
		 * if (field.equals(ControlNameConstants.FLASH_NEWS_DESCRIPTION)) { if
		 * (flashNewsVo.getFlashNewsDescription() != null) { if
		 * ((flashNewsVo.getFlashNewsDescription().length() >= 250)) { throw new
		 * CommonException(getMessage("flashnews.des.limit")); } } }
		 * 
		 * // Flash News date Validation
		 * 
		 * if (field.equals(ControlNameConstants.FLASH_NEWS_DATE)) { if
		 * (flashNewsVo.getFlashNewsDate() == null) { throw new
		 * CommonException(getMessage("flashnews.date.required")); } }
		 * 
		 * // Flash News from date Validation
		 * 
		 * if (field.equals(ControlNameConstants.FLASH_NEWS_VALID_FROM)) { if
		 * (flashNewsVo.getFlashNewsValidFrom() == null) { throw new
		 * CommonException(getMessage("flashnews.fromdate.required")); } }
		 * 
		 * // Flash News to date Validation
		 * 
		 * if (field.equals(ControlNameConstants.FLASH_NEWS_VALID_TO)) { if
		 * (flashNewsVo.getFlashNewsValidTo() == null) { throw new
		 * CommonException(getMessage("flashnews.todate.required")); }
		 * 
		 * } // Flash News to date and from date Validation if
		 * (flashNewsVo.getFlashNewsValidFrom().equals(flashNewsVo.
		 * getFlashNewsValidTo())) { if
		 * (!flashNewsVo.getFlashNewsValidFrom().equals(flashNewsVo.
		 * getFlashNewsDate())) { throw new
		 * CommonException(getMessage("flashnews.date.validation")); } }
		 * 
		 * if (!flashNewsVo.getFlashNewsValidFrom().equals(flashNewsVo.
		 * getFlashNewsValidTo())) {
		 * 
		 * if (flashNewsVo.getFlashNewsValidTo().before(flashNewsVo.
		 * getFlashNewsValidFrom())) { throw new
		 * CommonException(getMessage("flashnews.fromToDate.validation")); } //
		 * Flash News date and from date and to date Validation if
		 * (!flashNewsVo.getFlashNewsValidFrom().equals(flashNewsVo.
		 * getFlashNewsDate())) { if
		 * (!flashNewsVo.getFlashNewsValidTo().equals(flashNewsVo.
		 * getFlashNewsDate())) { if
		 * (!flashNewsVo.getFlashNewsValidFrom().before(flashNewsVo.
		 * getFlashNewsDate())) { throw new
		 * CommonException(getMessage("flashnews.date.validation")); }
		 * 
		 * if (!flashNewsVo.getFlashNewsDate().before(flashNewsVo.
		 * getFlashNewsValidTo())) { throw new
		 * CommonException(getMessage("flashnews.date.validation")); } } } } } }
		 */

	/**
	 * Method is used to delete the flash news details
	 * 
	 * @param flashNewsVo
	 *            FlashNewsVo
	 * @return response Response
	 */
	@PostMapping(FilePathConstants.FLASH_NEWS_DELETE)
	@ResponseBody
	public ResponseEntity<JSONResponse> delete(HttpServletRequest request,@RequestBody FlashNewsVO flashNewsVo) {
		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			 authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				//get from message.properties
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			logger.info(getMessage("processValidation"));

			// deleteValidation(flashNewsVo);

			logger.info(getMessage("processValidationCompleted"));

			flashNewsService.delete(flashNewsVo,authDetailsVo);
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
		}catch (Exception e) {
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("deleteErrorMessage"));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.EXPECTATION_FAILED);
	}

	/**
	 * This method is used to validate the delete flash news
	 * 
	 * @param flashNewsVo
	 *            FlashNewsVo
	 *//*
		 * private void deleteValidation(FlashNewsVO flashNewsVo) {
		 * 
		 * if (null == flashNewsVo.getIdList() || flashNewsVo.getIdList().size()
		 * <= 0) { throw new CommonException(getMessage("delete.validation")); }
		 * }
		 */

	/**
	 * Method is used to load the flash news details
	 * 
	 * @param flashNewsVo
	 *            FlashNewsVo
	 * @return response Response
	 */
	@PostMapping(FilePathConstants.FLASH_NEWS_VIEW)
	@ResponseBody
	public ResponseEntity<JSONResponse> load(HttpServletRequest request,@RequestBody FlashNewsVO flashNewsVo) {
		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			 authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				//get from message.properties
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			logger.info(getMessage("processValidation"));

			// idValidation(flashNewsVo);

			logger.info(getMessage("processValidationCompleted"));

			CommonVO commonVO = flashNewsService.getScreenFields(flashNewsVo.getScreenJson(),authDetailsVo);

			FlashNewsVO flashNewsViewVo = new FlashNewsVO();

			flashNewsViewVo = flashNewsService.load(flashNewsVo,authDetailsVo);

			logger.info(getMessage("processValidation"));

			logger.info(getMessage("processValidationCompleted"));

			jsonResponse.setSuccesObject(flashNewsViewVo);
			jsonResponse.setAuthSuccesObject(commonVO);
			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("successMessage"));
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
			jsonResponse.setResponseMessage(getMessage("errorMessage"));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.EXPECTATION_FAILED);
	}

	/**
	 * Method is used to get all the search details of flash news
	 * 
	 * @param flashNewsVo
	 *            FlashNewsVo
	 * @return response Response
	 */
	@PostMapping(FilePathConstants.FLASH_NEWS_SEARCH)
	@ResponseBody
	public ResponseEntity<JSONResponse> getAllSearch(HttpServletRequest request,@RequestBody FlashNewsVO flashNewsVo) {
		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			 authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				//get from message.properties
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			List<FlashNewsVO> flashNewsVoList = flashNewsService.getAllSearch(flashNewsVo,authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("successMessage"));
			jsonResponse.setSuccesObject(flashNewsVoList);
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
			jsonResponse.setResponseMessage(getMessage("errorMessage"));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.EXPECTATION_FAILED);
	}

	/**
	 * Method is used to validate the save flash news
	 * 
	 * @param flashNewsVo
	 *            FlashNewsVo
	 */
	private void saveValidation(FlashNewsVO flashNewsVo) {

		for (String field : flashNewsVo.getScreenFieldDisplayVoList()) {

			// Flash News Type Validation

			if (field.equals(ControlNameConstants.FLASH_NEWS_TYPE)) {
				if (ValidationUtil.isNullOrBlank(flashNewsVo.getFlashNewsType())) {
					throw new CommonException(getMessage("flashnews.type.required"));
				}
			}

			// Flash News Description Validation

			if (field.equals(ControlNameConstants.FLASH_NEWS_DESCRIPTION)) {
				if (flashNewsVo.getFlashNewsDescription() != null) {
					if ((flashNewsVo.getFlashNewsDescription().length() >= 250)) {
						throw new CommonException(getMessage("flashnews.des.limit"));
					}
				}
			}

			// Flash News date Validation

			if (field.equals(ControlNameConstants.FLASH_NEWS_DATE)) {
				if (flashNewsVo.getFlashNewsDate() == null) {
					throw new CommonException(getMessage("flashnews.date.required"));
				}
			}

			// Flash News from date Validation

			if (field.equals(ControlNameConstants.FLASH_NEWS_VALID_FROM)) {
				if (flashNewsVo.getFlashNewsValidFrom() == null) {
					throw new CommonException(getMessage("flashnews.fromdate.required"));
				}
			}

			// Flash News to date Validation

			if (field.equals(ControlNameConstants.FLASH_NEWS_VALID_TO)) {
				if (flashNewsVo.getFlashNewsValidTo() == null) {
					throw new CommonException(getMessage("flashnews.todate.required"));
				}

			}
			// Flash News to date and from date Validation
			if (flashNewsVo.getFlashNewsValidFrom().equals(flashNewsVo.getFlashNewsValidTo())) {
				if (!flashNewsVo.getFlashNewsValidFrom().equals(flashNewsVo.getFlashNewsDate())) {
					throw new CommonException(getMessage("flashnews.date.validation"));
				}
			}

			if (!flashNewsVo.getFlashNewsValidFrom().equals(flashNewsVo.getFlashNewsValidTo())) {

				if (flashNewsVo.getFlashNewsValidTo().before(flashNewsVo.getFlashNewsValidFrom())) {
					throw new CommonException(getMessage("flashnews.fromToDate.validation"));
				}
				// Flash News date and from date and to date Validation
				if (!flashNewsVo.getFlashNewsValidFrom().equals(flashNewsVo.getFlashNewsDate())) {
					if (!flashNewsVo.getFlashNewsValidTo().equals(flashNewsVo.getFlashNewsDate())) {
						if (!flashNewsVo.getFlashNewsValidFrom().before(flashNewsVo.getFlashNewsDate())) {
							throw new CommonException(getMessage("flashnews.date.validation"));
						}

						if (!flashNewsVo.getFlashNewsDate().before(flashNewsVo.getFlashNewsValidTo())) {
							throw new CommonException(getMessage("flashnews.date.validation"));
						}
					}
				}
			}
		}
	}

	/**
	 * This method is used to validate the delete flash news
	 * 
	 * @param flashNewsVo
	 *            FlashNewsVo
	 */
	private void deleteValidation(FlashNewsVO flashNewsVo) {

		if (null == flashNewsVo.getIdList() || flashNewsVo.getIdList().size() <= 0) {
			throw new CommonException(getMessage("delete.validation"));
		}
	}

	/**
	 * Method is used to validate the id of flash news
	 * 
	 * @param flashNewsVo
	 *            FlashNewsVo
	 */
	private void idValidation(FlashNewsVO flashNewsVo) {

		if (flashNewsVo.getId() <= 0 || flashNewsVo.getId() == 0) {
			throw new CommonException(getMessage("search.validation"));
		}
	}

	/**
	 * Method is used to get all the search validation
	 * 
	 * @param flashNewsVo
	 *            FlashNewsVo
	 */
	/*
	 * private void getAllSearchValidation(FlashNewsVo flashNewsVo) { if
	 * (flashNewsVo == null) { throw new
	 * CommonException(getMessage("common.noRecord")); } }
	 */

	/**
	 * Method is used to validate the load flash news
	 * 
	 * @param flashNewsVo
	 *            List<FlashNewsVo>
	 */
	/*
	 * private void loadValidation(List<FlashNewsVo> flashNewsVo) { if
	 * (flashNewsVo == null || flashNewsVo.size() <= 0) { throw new
	 * CommonException(getMessage("common.noRecord")); } }
	 */
	protected String getMessage(String code) {
		return getMessage(code, new Object[] {});
	}

	protected String getMessage(String code, Object args[]) {
		return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
	}
}
