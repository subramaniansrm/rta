package com.srm.rta.controller;

import java.util.ArrayList;
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
import com.srm.coreframework.constants.FilePathConstants;
import com.srm.coreframework.controller.CommonController;
import com.srm.coreframework.response.JSONResponse;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.coreframework.vo.CommonVO;
import com.srm.rta.entity.RequestEntity;
import com.srm.rta.service.RequestConfigurationService;
import com.srm.rta.service.RequestResolverService;
import com.srm.rta.service.RequestService;
import com.srm.rta.vo.RequestResolverVO;
import com.srm.rta.vo.RequestVO;
import com.srm.rta.vo.RequestWorkFlowAuditVO;

@RestController
public class RequestResolverController extends CommonController<RequestVO> {

	private static final Logger logger = LogManager.getLogger(RequestResolverController.class);
	
	@Autowired
	RequestResolverService requestResolverService;

	@Autowired
	RequestConfigurationService requestConfigurationService;

	@Autowired
	RequestService requestService;

	/**
	 * Method is used to get all Resolver List
	 * 
	 * 
	 * @param requestVo
	 * @return
	 */
	@PostMapping(FilePathConstants.LOAD_RESOLVER)
	@ResponseBody
	public ResponseEntity<JSONResponse> getAllResolver(HttpServletRequest request,@RequestBody RequestResolverVO requestResolverVO) {
		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			CommonVO commonVO = requestResolverService.getScreenFields(requestResolverVO.getScreenJson(),authDetailsVo);

			List<RequestResolverVO> resolverVoList = new ArrayList<RequestResolverVO>();
			if (null != commonVO) {
				resolverVoList = requestResolverService.getResolverList(requestResolverVO,authDetailsVo);

			}

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("successMessage",authDetailsVo));
			jsonResponse.setSuccesObject(resolverVoList);
			jsonResponse.setAuthSuccesObject(commonVO);
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
		}  catch (Exception e) {
			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}

		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}

	/**
	 * Method used to update Resolver List
	 * 
	 * 
	 * @param requestVo
	 * @return
	 * @throws BusinessException
	 */
	@PostMapping(FilePathConstants.UPDATE_RESOLVER)
	@ResponseBody
	public ResponseEntity<JSONResponse> updateResolver(HttpServletRequest request,@RequestBody RequestWorkFlowAuditVO requestWorkFlowAuditVo)
			throws CommonException {
		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		String requestCode = null;
		String requestStatus= null;
		RequestVO requestVo = new RequestVO();
		
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			requestConfigurationService.repopulate(requestWorkFlowAuditVo,authDetailsVo,requestVo);

			if(null != requestWorkFlowAuditVo.getRequestId()){
				requestCode = requestConfigurationService.getRequestCode(requestWorkFlowAuditVo.getRequestId(),authDetailsVo.getEntityId());
			}
			if(null != requestWorkFlowAuditVo.getRequestId()){
				
				requestStatus = requestConfigurationService.getRequestStatus(requestWorkFlowAuditVo.getRequestId(),authDetailsVo.getEntityId());
			}
			
			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			if(null != requestCode && !requestCode.isEmpty() && null != requestStatus && !requestStatus.isEmpty()){
				 
				jsonResponse.setResponseMessage(requestCode + " - "+ getMessage("request"+requestStatus+"Successfully",authDetailsVo)); 
			}else{
				jsonResponse.setResponseMessage(getMessage("updateSuccessMessage",authDetailsVo));
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
		}  catch (Exception e) {
			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("updateErrorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}

		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}

/*	@PostMapping("/resolver/updateResolver")
	@ResponseBody
	public ResponseEntity<JSONResponse> updateResolver1(HttpServletRequest request,@RequestBody RequestWorkFlowAuditVO requestWorkFlowAuditVo)
			throws CommonException {
		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		RequestVO requestVo = new RequestVO();
		
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			requestConfigurationService.repopulate(requestWorkFlowAuditVo,authDetailsVo,requestVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("updateSuccessMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
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
			jsonResponse.setResponseMessage(getMessage("updateErrorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}

		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}
*/
	/**
	 * Method used to view All approval and resolver list
	 * 
	 * 
	 * @param requestVo
	 * @return
	 */
	@PostMapping(FilePathConstants.VIEW_ALL_RESOLVER)
	@ResponseBody
	public ResponseEntity<JSONResponse> viewAllResolver(HttpServletRequest request,@RequestBody RequestVO requestVo) {
		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			CommonVO commonVO = requestResolverService.getScreenFields(requestVo.getScreenJson(),authDetailsVo);
			List<RequestVO> requestVoList = requestResolverService.viewAllResolverList(requestVo,authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("successMessage",authDetailsVo));
			jsonResponse.setSuccesObject(requestVoList);
			jsonResponse.setAuthSuccesObject(commonVO);
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
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}

		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	/**
	 * 
	 * Method used to view all resolver details
	 * 
	 * @param requestVo
	 * @return
	 */
	@PostMapping(FilePathConstants.VIEW_RESOLVER)
	@ResponseBody
	public ResponseEntity<JSONResponse> viewResolver(HttpServletRequest request,@RequestBody RequestVO requestVo) {
		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			CommonVO commonVO = requestResolverService.getScreenFields(requestVo.getScreenJson(),authDetailsVo);

			List<RequestVO> requestVoList = requestResolverService.load(requestVo,authDetailsVo);
			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("successMessage",authDetailsVo));
			jsonResponse.setAuthSuccesObject(commonVO);
			jsonResponse.setSuccesObject(requestVoList);
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
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}

		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}

	/**
	 * 
	 * Method used to view all resolver details
	 * 
	 * @param requestVo
	 * @return
	 */
	@PostMapping(FilePathConstants.RESOLVER_SEARCH)
	@ResponseBody
	public ResponseEntity<JSONResponse> search(HttpServletRequest request,@RequestBody RequestResolverVO requestVo) {
		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			List<RequestResolverVO> requestVoList = requestResolverService.search(requestVo,authDetailsVo);
			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("successMessage",authDetailsVo));
			jsonResponse.setSuccesObject(requestVoList);
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
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}

		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}

	@PostMapping(FilePathConstants.RESOLVER_REQLOAD)
	@ResponseBody
	public ResponseEntity<JSONResponse> load(HttpServletRequest request,@RequestBody RequestVO requestVo) {
		JSONResponse jsonResponse = new JSONResponse();

		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			CommonVO commonVO = requestResolverService.getScreenFields(requestVo.getScreenJson(),authDetailsVo);

			RequestVO requestVoForLoad = new RequestVO();
			requestVoForLoad.setRequestId(requestVo.getRequestId());
			logger.info(getMessage("processValidationCompleted",authDetailsVo));
			requestVoForLoad = requestService.findRequest(requestVoForLoad,authDetailsVo);

			jsonResponse.setAuthSuccesObject(commonVO);
			jsonResponse.setSuccesObject(requestVoForLoad);
			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("successMessage",authDetailsVo));
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
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}
	
	
	@PostMapping(FilePathConstants.STATUS_CHECK_RESOLVER)
	@ResponseBody
	public ResponseEntity<JSONResponse> statusCheckForHold(HttpServletRequest request,@RequestBody RequestVO requestVo) {
		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			 
		    jsonResponse  = requestResolverService.statusCheckForHold(requestVo,authDetailsVo);		
		
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
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}

		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}
	
	@PostMapping(FilePathConstants.STATUS_CHECK_RESOLVER_RESOLVER)
	@ResponseBody
	public ResponseEntity<JSONResponse> statusCheckForHoldRequestor(HttpServletRequest request,@RequestBody RequestVO requestVo) {
		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;

		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			 
		    jsonResponse  = requestResolverService.statusCheckForHoldRequestor(requestVo,authDetailsVo);		
		
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
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}

		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}


}
