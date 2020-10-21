package com.srm.rta.controller;

import java.io.IOException;
import java.util.ArrayList;
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
import com.srm.coreframework.util.ValidationUtil;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.coreframework.vo.CommonVO;
import com.srm.coreframework.vo.MailParameterVO;
import com.srm.rta.service.ApprovalService;
import com.srm.rta.service.RequestConfigurationService;
import com.srm.rta.vo.ApprovalVO;
import com.srm.rta.vo.RequestVO;
import com.srm.rta.vo.RequestWorkFlowAuditVO;

import lombok.Data;

@Data
@RestController
public class ApprovalController extends CommonController<ApprovalVO> {

	@Autowired
	ApprovalService approvalService;

	@Autowired
	RequestConfigurationService requestConfigurationService;

	@PostMapping(FilePathConstants.APPROVAL_LIST)
	@ResponseBody
	public ResponseEntity<JSONResponse> getAll(HttpServletRequest request,@RequestBody ApprovalVO approvalVO) throws IOException {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			 authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				//get from message.properties
				throw new LoginAuthException("Token Expired / Already Logined");
			}
		
			CommonVO commonVO = approvalService.getScreenFields(approvalVO.getScreenJson(),authDetailsVo);

			List<ApprovalVO> approvalVOList = new ArrayList<>();
			if (null != commonVO) {
				approvalVOList = approvalService.getAll(authDetailsVo);

			}
			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(approvalVOList);
			jsonResponse.setAuthSuccesObject(commonVO);
			jsonResponse.setResponseMessage(getMessage("successMessage",authDetailsVo));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			e.printStackTrace();
			Log.info("Approval Controller getAll Common Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}catch (LoginAuthException e) {
			Log.info("Approval Controller getAll Login Auth Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Approval Controller getAll  Exception",e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}

	@PostMapping(FilePathConstants.APPROVAL_MODIFY)
	@ResponseBody
	public ResponseEntity<JSONResponse> update(HttpServletRequest request,@RequestBody RequestWorkFlowAuditVO requestWorkFlowAuditVo)
			throws IOException {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		RequestVO requestVo = new RequestVO();
		String requestCode = null;
		String requestStatus= null;
		try {
			 authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				//get from message.properties
				throw new LoginAuthException("Token Expired / Already Logined");
			}
		
			updateValidation(requestWorkFlowAuditVo,authDetailsVo);
			requestWorkFlowAuditVo.setCreatedBy(authDetailsVo.getUserId());

			requestConfigurationService.repopulate(requestWorkFlowAuditVo,authDetailsVo, requestVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(CommonConstant.NULL);

			if(null != requestWorkFlowAuditVo.getRequestId()){
				requestCode = getRequestCode(requestWorkFlowAuditVo.getRequestId(),authDetailsVo.getEntityId());
			}
			if(null != requestWorkFlowAuditVo.getRequestId()){
				
				requestStatus = getRequestStatus(requestWorkFlowAuditVo.getRequestId(),authDetailsVo.getEntityId());
			}
			
			if(null != requestCode && !requestCode.isEmpty() && null != requestStatus && !requestStatus.isEmpty()){
				 
				jsonResponse.setResponseMessage(requestCode + " - "+ getMessage("request"+requestStatus+"Successfully",authDetailsVo));
			}else{
				jsonResponse.setResponseMessage(getMessage("updateSuccessMessage",authDetailsVo));
			}
			
			// Auto Delete for notification
		/*	MailParameterVO mailParameterVo = new MailParameterVO();
			mailParameterVo.setRequestId(requestWorkFlowAuditVo.getRequestId());
			mailParameterVo.setUserId(requestWorkFlowAuditVo.getUserId());
			mailParameterVo.setMessage(CommonConstant.APL);
			requestConfigurationService.updateMailLog(mailParameterVo,authDetailsVo);*/
								
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
			
		} catch (CommonException e) {
			e.printStackTrace();
			Log.info("Approval Controller get update Common Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (LoginAuthException e) {
			Log.info("Approval Controller get update login auth Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}catch (Exception e) {
			e.printStackTrace();
			Log.info("Approval Controller get update  Exception",e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}

	/**
	 * Method is used for update validation
	 * 
	 * @param requestWorkFlowAuditVo
	 */
	private void updateValidation(RequestWorkFlowAuditVO requestWorkFlowAuditVo,AuthDetailsVo authDetailsVo) {

		if (ValidationUtil.isNullOrBlank(requestWorkFlowAuditVo.getRemarks().trim())) {
			throw new CommonException(getMessage("approval.remarks.manditory",authDetailsVo));
		}

	}

	@PostMapping(FilePathConstants.APPROVAL_LOAD)
	@ResponseBody
	public ResponseEntity<JSONResponse> load(HttpServletRequest request,@RequestBody RequestVO requestVo) throws IOException {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			 authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				//get from message.properties
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			CommonVO commonVO = approvalService.getScreenFields(requestVo.getScreenJson(),authDetailsVo);
			RequestVO req = new RequestVO();
			if (requestVo.getRequestWorkFlowAuditVo() != null) {
				req.setRequestWorkFlowAuditVo(requestVo.getRequestWorkFlowAuditVo());
			}

			req = approvalService.load(req,authDetailsVo);
			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(req);
			jsonResponse.setAuthSuccesObject(commonVO);
			jsonResponse.setResponseMessage(getMessage("successMessage",authDetailsVo));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			e.printStackTrace();
			Log.info("Approval Controller get load Common Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}catch (LoginAuthException e) {
			Log.info("Approval Controller get load login auth Common Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Approval Controller get load Exception",e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}

	@PostMapping(FilePathConstants.APPROVAL_LOADALL)
	@ResponseBody
	public ResponseEntity<JSONResponse> loadAll(HttpServletRequest request,@RequestBody RequestVO requestVo) throws IOException {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			 authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				//get from message.properties
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			CommonVO commonVO = approvalService.getScreenFields(requestVo.getScreenJson(),authDetailsVo);

			RequestVO req = approvalService.loadAll(requestVo,authDetailsVo);
			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(req);
			jsonResponse.setAuthSuccesObject(commonVO);
			jsonResponse.setResponseMessage(getMessage("successMessage",authDetailsVo));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			e.printStackTrace();
			Log.info("Approval Controller get load All Common Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}catch (LoginAuthException e) {
			Log.info("Approval Controller get load All Login Auth Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Approval Controller get load All  Exception",e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}

	@PostMapping(FilePathConstants.APPROVAL_GETALLSEARCH)
	@ResponseBody
	public ResponseEntity<JSONResponse> getAllSearch(HttpServletRequest request,@RequestBody ApprovalVO approvalVO) throws IOException {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			 authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				//get from message.properties
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			List<ApprovalVO> approvalVOList = approvalService.getAllSearch(approvalVO,authDetailsVo);
			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(approvalVOList);
			jsonResponse.setResponseMessage(getMessage("successMessage",authDetailsVo));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			Log.info("Approval Controller get All Search Common Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (LoginAuthException e) {
			Log.info("Approval Controller get All Search Login auth Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}catch (Exception e) {
			Log.info("Approval Controller get All Search  Exception",e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}

	@PostMapping(FilePathConstants.APPROVAL_REQLOAD)
	@ResponseBody
	public ResponseEntity<JSONResponse> requestLoad(HttpServletRequest request,@RequestBody RequestVO requestVo) throws IOException {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			 authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				//get from message.properties
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			CommonVO commonVO = approvalService.getScreenFields(requestVo.getScreenJson(),authDetailsVo);

			RequestVO req = approvalService.findRequestApproval(requestVo,authDetailsVo);
			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(req);
			jsonResponse.setAuthSuccesObject(commonVO);
			jsonResponse.setResponseMessage(getMessage("successMessage",authDetailsVo));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			e.printStackTrace();
			Log.info("Approval Controller get request load Common Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}catch (LoginAuthException e) {
			Log.info("Approval Controller get request load Login Auth Exception",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Approval Controller get request load  Exception",e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}

}
