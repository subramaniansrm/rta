package com.srm.rta.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.config.UserMessages;
import com.srm.coreframework.constants.DecisionTypeEnum;
import com.srm.coreframework.controller.CommonController;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.rta.dao.ForwardRequestDAO;
import com.srm.rta.dao.RequestConfigurationDAO;
import com.srm.rta.dao.RequestDAO;
import com.srm.rta.entity.RequestWorkFlowAuditEntity;
import com.srm.rta.vo.RequestVO;
import com.srm.rta.vo.RequestWorkFlowAuditVO;

@Service
public class ForwardRequestService extends CommonController<RequestVO> {
	Logger logger = LoggerFactory.getLogger(ForwardRequestService.class);

	@Autowired
	ForwardRequestDAO forwardRequestDao;

	@Autowired
	RequestService requestService;

	@Autowired
	UserMessages userMessages;
		
	@Autowired
	RequestConfigurationDAO requestConfigurationDAO;
	
	@Autowired
	RequestConfigurationService requestConfigurationService;
	
	@Autowired
	RequestDAO requestDAO;
	
	
	@Transactional
	public RequestVO createClose(RequestWorkFlowAuditVO requestWorkFlowAuditVo,AuthDetailsVo authDetailsVo ) throws CommonException, Exception {

		RequestWorkFlowAuditEntity requestWorkFlowAuditEntity = new RequestWorkFlowAuditEntity();

		RequestVO requestVO = null;
		try {

			forwardRequestValidation(requestWorkFlowAuditVo);
			
			requestValidation(requestWorkFlowAuditVo);
			
			BeanUtils.copyProperties(requestWorkFlowAuditVo, requestWorkFlowAuditEntity);

			requestWorkFlowAuditEntity = setCreateUserdetails(requestWorkFlowAuditEntity, authDetailsVo);

			forwardRequestDao.updateAudit(requestWorkFlowAuditEntity, authDetailsVo);

			forwardRequestDao.updateCurrentStatusInRequest(DecisionTypeEnum.CLO.toString(), requestWorkFlowAuditEntity,
					authDetailsVo);

			requestVO = createNewRequest(requestWorkFlowAuditVo, requestWorkFlowAuditEntity, authDetailsVo);
					
		} catch (CommonException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new CommonException(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new CommonException(e.getMessage());
		}
		return requestVO;
	}

	/**
	 * This method is used to set ,created user and date in
	 * requestWorkflowAudit.
	 * 
	 * @param requestWorkFlowAuditEntity
	 *            RequestWorkFlowAuditEntity
	 * @return requestWorkFlowAuditEntity RequestWorkFlowAuditEntity
	 */
	private RequestWorkFlowAuditEntity setCreateUserdetails(RequestWorkFlowAuditEntity requestWorkFlowAuditEntity,AuthDetailsVo authDetailsVo ) {

		requestWorkFlowAuditEntity.setCreateBy(authDetailsVo .getUserId());
		requestWorkFlowAuditEntity.setCreateDate(CommonConstant.getCalenderDate());
		requestWorkFlowAuditEntity.setUpdateBy(authDetailsVo .getUserId());
		requestWorkFlowAuditEntity.setUpdateDate(CommonConstant.getCalenderDate());

		return requestWorkFlowAuditEntity;
	}

	/**
	 * This method is used to create new request for forward
	 * requestWorkFlowAuditVo
	 * 
	 * @param requestWorkFlowAuditVo
	 *            requestWorkFlowAuditEntity
	 * @throws Exception
	 */

	@Transactional()
	public RequestVO createNewRequest(RequestWorkFlowAuditVO requestWorkFlowAuditVo,
			RequestWorkFlowAuditEntity requestWorkFlowAuditEntity,AuthDetailsVo authDetailsVo) throws CommonException, Exception {
		RequestVO requestVo = new RequestVO();

		try {
 			
			requestVo.setRequestTypeId(requestWorkFlowAuditVo.getRequestTypeId());
			requestVo.setRequestSubtypeId(requestWorkFlowAuditVo.getRequestSubTypeId());
			requestVo.setId(requestWorkFlowAuditVo.getLocationId());  
			requestVo.setSublocationId(requestWorkFlowAuditVo.getSubLocationId());
			requestVo.setDepartmentId(requestWorkFlowAuditVo.getDepartmentId());
			requestVo.setReqLocationId(requestWorkFlowAuditVo.getReqLocationId());
			requestVo.setReqSublocationId(requestWorkFlowAuditVo.getReqSublocationId());
			
			String username = "";
			if (null != authDetailsVo.getFirstName()) {
				username = authDetailsVo.getFirstName();
			}

			if (null != authDetailsVo.getLastName()) {
				username = username + authDetailsVo.getLastName();
			}
			
			if (null != requestWorkFlowAuditVo.getRemarks()) {

				requestVo.setRemarks(requestWorkFlowAuditVo.getRemarks());
			}  
						
			if (null != requestWorkFlowAuditVo.getRequestCode()) {

				requestVo.setForwardRedirectRemarks(
						getMessage("forwardFromRequest",authDetailsVo)+ " " + requestWorkFlowAuditVo.getRequestCode() + " " + getMessage("by",authDetailsVo) + " " + username); //forwardFrom
			}

			if(null != requestWorkFlowAuditVo.getRequestAttachment()){
		    requestVo.setRequestAttachment(requestWorkFlowAuditVo.getRequestAttachment());
			}
			
			if(null != requestWorkFlowAuditVo.getRequestSubject()){
			requestVo.setRequestSubject(requestWorkFlowAuditVo.getRequestSubject());
			}
			
			if(null != requestWorkFlowAuditVo.getRequestFromDate()){
			requestVo.setRequestFromDate(requestWorkFlowAuditVo.getRequestFromDate());
			}
			
			if(null != requestWorkFlowAuditVo.getRequestToDate()){
			requestVo.setRequestToDate(requestWorkFlowAuditVo.getRequestToDate());
			}
			
			if(null != requestWorkFlowAuditVo.getRequestExtension()){
			requestVo.setRequestExtension(requestWorkFlowAuditVo.getRequestExtension());
			}
			
			if(null != requestWorkFlowAuditVo.getRequestMobileNo()){
			requestVo.setRequestMobileNo(requestWorkFlowAuditVo.getRequestMobileNo());
			}
			
			requestVo.setRequestPriority(requestWorkFlowAuditVo.getRequestPriority());
			
			if(null != requestWorkFlowAuditVo.getRequestDetailList()){
			requestVo.setRequestDetailList(requestWorkFlowAuditVo.getRequestDetailList());
			}
					 
			requestVo.setUserId(requestWorkFlowAuditEntity.getUserId());
			requestVo.setForwardRequestId(requestWorkFlowAuditEntity.getRequestId());
			
			Integer requestorId = requestService.getForwardRequesterId(requestWorkFlowAuditEntity.getRequestId());
			requestVo.setCreatedBy(requestorId);
		 
			//validation check
			requestValidation(requestWorkFlowAuditVo);
			
			MultipartFile[] uploadingFiles = null;
			RequestVO request = requestService.create(requestVo, uploadingFiles,authDetailsVo);
						 
			if(null != request.getRequestCode()){
				requestVo.setForwardRedirectRemarks(getMessage("forwardToRequest",authDetailsVo)+ " "+ request.getRequestCode()+ " " + getMessage("by",authDetailsVo) + " " + username ); //forwardTo							
				forwardRequestDao.updateRemarks(requestVo,authDetailsVo);				
			}
								 	
			requestWorkFlowAuditEntity.setDescisionType(CommonConstant.CONSTANT_EIGHT);
			requestWorkFlowAuditEntity.setRemarks(requestVo.getRemarks());
			requestWorkFlowAuditEntity.setAuditForwardRemarks(requestVo.getForwardRedirectRemarks());
			RequestWorkFlowAuditEntity requestWorkFlowAuditEn = new RequestWorkFlowAuditEntity();
				
			Integer forwardFlag = 1;
			requestWorkFlowAuditEn = requestDAO.getWorkFlow(requestWorkFlowAuditEntity,authDetailsVo,forwardFlag);  
			requestWorkFlowAuditEntity.setRequestWorkFlowAuditId(requestWorkFlowAuditEn.getRequestWorkFlowAuditId());
			requestWorkFlowAuditEntity.setSeqId(requestWorkFlowAuditEn.getSeqId());
			requestWorkFlowAuditEntity.setDescisionType(CommonConstant.CONSTANT_EIGHT);
			if(!(requestWorkFlowAuditEn.getDescisionType() == CommonConstant.CONSTANT_EIGHT 
					&& null == requestWorkFlowAuditEn.getRemarks()) ){	
				requestWorkFlowAuditEntity.setRemarks(requestVo.getRemarks());
			requestConfigurationDAO.updateDetailInAudit(requestWorkFlowAuditEntity, authDetailsVo);
			
			}
			
			requestWorkFlowAuditEn = requestDAO.getWorkFlowByRequestor(requestWorkFlowAuditEntity,authDetailsVo); 
			requestWorkFlowAuditEntity.setRequestWorkFlowAuditId(requestWorkFlowAuditEn.getRequestWorkFlowAuditId());
			requestWorkFlowAuditEntity.setSeqId(requestWorkFlowAuditEn.getSeqId());
			
			requestWorkFlowAuditEntity.setRemarks(requestVo.getRemarks());
			requestConfigurationDAO.updateDetailInAudit(requestWorkFlowAuditEntity, authDetailsVo);
				 
		    Integer	nwStatusId =  CommonConstant.CONSTANT_NINE;			 
			requestConfigurationDAO.updateForwardReqDecision(requestWorkFlowAuditEntity,nwStatusId ,authDetailsVo);
					
			return request;
		} catch (CommonException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new CommonException(e.getMessage());
		}catch (Exception e) {
			throw new CommonException("dataFailure");
		}
	}

	
	@SuppressWarnings("unused")
	@Transactional
	public  void  requestValidation(RequestWorkFlowAuditVO requestWorkFlowAuditVO)throws CommonException {

		if (requestWorkFlowAuditVO.getReqLocationId().equals(requestWorkFlowAuditVO.getLocationId())
				&& requestWorkFlowAuditVO.getSubLocationId().equals(requestWorkFlowAuditVO.getReqSublocationId())
				&& requestWorkFlowAuditVO.getDepartmentId().equals(requestWorkFlowAuditVO.getOriginalDepartmentId())
			) {
			throw new CommonException("forwardRequestValidation");

		}
	}
	
	
	@SuppressWarnings("unused")
	@Transactional
	public  void  forwardRequestValidation(RequestWorkFlowAuditVO requestWorkFlowAuditVO)throws CommonException {

		if (null != requestWorkFlowAuditVO.getForwardRequestId() && 0 != requestWorkFlowAuditVO.getForwardRequestId()) {
			throw new CommonException("sameForwardRequestMsg");

		}
	}
	
}
