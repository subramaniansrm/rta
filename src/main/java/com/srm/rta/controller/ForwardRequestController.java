package com.srm.rta.controller;

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
import com.srm.coreframework.util.ValidationUtil;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.rta.service.ForwardRequestService;
import com.srm.rta.vo.RequestVO;
import com.srm.rta.vo.RequestWorkFlowAuditVO;



@RestController
public class ForwardRequestController extends CommonController<RequestVO> {

	private static final Logger logger = LogManager.getLogger(ForwardRequestController.class);

	@Autowired
	ForwardRequestService forwardRequestService;


	@PostMapping(FilePathConstants.CREATE_FORWARD_REQUEST)
	@ResponseBody
	public ResponseEntity<JSONResponse> create(HttpServletRequest request,@RequestBody RequestWorkFlowAuditVO requestWorkFlowAuditVo) {
		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				// get from message.properties
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			Validate(requestWorkFlowAuditVo,authDetailsVo);
			RequestVO requestVO = forwardRequestService.createClose(requestWorkFlowAuditVo,authDetailsVo);

			addEntityRequest(authDetailsVo);
			
			String successMessage = requestVO.getRequestCode().concat(" "+ getMessage("forwardRequestSuccessMessage",authDetailsVo)) ;
			
			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(successMessage);
			jsonResponse.setSuccesObject(CommonConstant.NULL);
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			e.printStackTrace();
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(getMessage(e.getMessage(),authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}catch (Exception e) {
			e.printStackTrace();
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("saveErroMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	/**
	 * this method is used for validation of forward request
	 * 
	 * @param requestWorkFlowAuditVo
	 * 
	 */

	private void Validate(RequestWorkFlowAuditVO requestWorkFlowAuditVo,AuthDetailsVo authDetailsVo ) throws CommonException{

		// Request Type Id validation

		if (ValidationUtil.isNullOrBlank(requestWorkFlowAuditVo.getRequestTypeId())) {
			throw new CommonException(getMessage("requestType.id.required",authDetailsVo));
		}

		// Request SubType Id validation

		if (ValidationUtil.isNullOrBlank(requestWorkFlowAuditVo.getRequestSubTypeId())) {
			throw new CommonException(getMessage("requestSubType.id.required",authDetailsVo));
		}

		// Request Location Id validation

		if (ValidationUtil.isNullOrBlank(requestWorkFlowAuditVo.getLocationId())) {
			throw new CommonException(getMessage("location.id.required",authDetailsVo));
		}

		// Request SubLocation Id validation

		if (ValidationUtil.isNullOrBlank(requestWorkFlowAuditVo.getSubLocationId())) {
			throw new CommonException(getMessage("requestType.id.required",authDetailsVo));
		}
		// Request Department Id validation

		if (ValidationUtil.isNullOrBlank(requestWorkFlowAuditVo.getDepartmentId())) {
			throw new CommonException(getMessage("department.departmentId.required",authDetailsVo));
		}

		// Request User Id validation

		if (ValidationUtil.isNullOrBlank(requestWorkFlowAuditVo.getUserId())) {
			throw new CommonException(getMessage("user.userName.required",authDetailsVo));
		}

		// Request Priority Id validation

		if (ValidationUtil.isNullOrBlank(requestWorkFlowAuditVo.getRequestPriority())) {
			throw new CommonException(getMessage("request.validation.requestPriority",authDetailsVo));
		}
		
		/*if (ValidationUtil.isNullOrBlank(requestWorkFlowAuditVo.getRequesterId())) {
			throw new CommonException(getMessage("forwardRequest.requestorId",authDetailsVo));
		}*/
	}
}
