package com.srm.rta.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.config.UserMessages;
import com.srm.coreframework.service.CommonService;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.rta.dao.RequestConfigurationDAO;
import com.srm.rta.dao.RequestDAO;
import com.srm.rta.dao.SubRequestDAO;
import com.srm.rta.entity.RequestWorkFlowAuditEntity;
import com.srm.rta.repository.RequestDetailsRepository;
import com.srm.rta.repository.RequestRepository;
import com.srm.rta.vo.RequestSubTypeVO;
import com.srm.rta.vo.RequestTypeVO;
import com.srm.rta.vo.RequestVO;
import com.srm.rta.vo.RequestWorkFlowAuditVO;

@Service
public class SubRequestService extends CommonService {

	Logger logger = LoggerFactory.getLogger(RequestService.class);

	@Autowired
	private SubRequestDAO subRequestDao;

	@Autowired
	RequestService requestService;

	@Autowired
	RequestDAO requestDAO;

	@Autowired
	RequestRepository requestRepository;

	@Autowired
	RequestConfigurationService requestConfigurationService;

	@Autowired
	RequestDetailsRepository requestDetailsRepository;	
	
	@Autowired
	RequestConfigurationDAO requestConfigurationDAO;
	
	@Autowired
	UserMessages userMessages;
	
	@Transactional
	public RequestVO updatesubRequest(RequestWorkFlowAuditVO requestWorkFlowAuditVo, AuthDetailsVo authDetailsVo)
			throws CommonException, Exception {

		RequestVO requestVo =  null;
		try {
			subRequestDao.updateRequest(requestWorkFlowAuditVo, authDetailsVo);
			// subRequestDao.updateAudit(requestWorkFlowAuditVo);
			subRequestDao.updateAuditExecutor(requestWorkFlowAuditVo, authDetailsVo);

			// create sub request
			requestVo = createSubRequest(requestWorkFlowAuditVo, authDetailsVo);

		} catch (CommonException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new CommonException(e.getMessage());
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return requestVo;
	}

	@Transactional
	public RequestVO createSubRequest(RequestWorkFlowAuditVO requestWorkFlowAuditVo, AuthDetailsVo authDetailsVo) throws CommonException ,  Exception {
		RequestVO requestVo = new RequestVO();
		try {
			requestVo.setSubrequestId(requestWorkFlowAuditVo.getRequestId());
			requestVo.setDepartmentId(requestWorkFlowAuditVo.getDepartmentId());
		 
			if (null != requestWorkFlowAuditVo.getUserId()) {

				requestService.getUserLocationSubLocation(requestWorkFlowAuditVo.getUserId(), requestVo, authDetailsVo);
			}
			
			requestVo.setRequestTypeId(requestWorkFlowAuditVo.getRequestTypeId());
			requestVo.setRequestSubtypeId(requestWorkFlowAuditVo.getRequestSubTypeId());
			requestVo.setId(requestWorkFlowAuditVo.getId());
			requestVo.setSublocationId(requestWorkFlowAuditVo.getSubLocationId());
			requestVo.setCurrentStatusId(requestWorkFlowAuditVo.getCurrentStatusId());
			requestVo.setRequestPriority(requestWorkFlowAuditVo.getRequestPriority());

			if (null != requestWorkFlowAuditVo.getRequestDetailList()) {
				requestVo.setRequestDetailList(requestWorkFlowAuditVo.getRequestDetailList());
			}
			
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
				requestVo.setForwardRedirectRemarks(getMessage("redirectRequestFrom" , authDetailsVo)+ " " + requestWorkFlowAuditVo.getRequestCode()+ " " + getMessage("by" , authDetailsVo) + " "  + username);
			}
										 
			if (null != requestWorkFlowAuditVo.getRequestAttachment()) {
				requestVo.setRequestAttachment(requestWorkFlowAuditVo.getRequestAttachment());
			}

			if (null != requestWorkFlowAuditVo.getRequestSubject()) {
				
				requestVo.setRequestTypeId(requestWorkFlowAuditVo.getRequestTypeId());
				requestVo.setRequestSubtypeId(requestWorkFlowAuditVo.getRequestSubTypeId());
				
				RequestTypeVO requestTypeVO =	requestService.getRequestType(requestVo,authDetailsVo);
				
				RequestSubTypeVO requestSubTypeVO = requestService.getSubRequestType(requestVo,authDetailsVo);
				
				String typeName = "";
				String subTypeName = "" ;
				
				if(null != requestTypeVO.getRequestTypeName()){
					typeName = requestTypeVO.getRequestTypeName();
				}
				if(null != requestSubTypeVO.getRequestSubTypeName()){
					subTypeName = requestSubTypeVO.getRequestSubTypeName();
				}
				
				requestVo.setRequestSubject(typeName + " " +subTypeName);
			}

			if (null != requestWorkFlowAuditVo.getRequestFromDate()) {
				requestVo.setRequestFromDate(requestWorkFlowAuditVo.getRequestFromDate());
			}

			if (null != requestWorkFlowAuditVo.getRequestToDate()) {
				requestVo.setRequestToDate(requestWorkFlowAuditVo.getRequestToDate());
			}

			if (null != requestWorkFlowAuditVo.getRequestExtension()) {
				requestVo.setRequestExtension(requestWorkFlowAuditVo.getRequestExtension());
			}

			if (null != requestWorkFlowAuditVo.getRequestMobileNo()) {
				requestVo.setRequestMobileNo(requestWorkFlowAuditVo.getRequestMobileNo());
			}

			//validation check
			requestValidation(requestWorkFlowAuditVo);
						
			MultipartFile[] uploadingFiles = null;
			RequestVO request = requestService.create(requestVo, uploadingFiles, authDetailsVo);
					 
			if(null != request.getRequestCode()){
				requestVo.setForwardRedirectRemarks(getMessage("redirectToRequest" , authDetailsVo) + " "+ request.getRequestCode()+ " " + getMessage("by" , authDetailsVo) + " " + username );//redirectRequestTo	
				requestVo.setRequestId(requestWorkFlowAuditVo.getRequestId());
				 
				subRequestDao.updateRemarks(requestVo,authDetailsVo);				
			}
		 
			RequestWorkFlowAuditEntity requestWorkFlowAuditEntity = new RequestWorkFlowAuditEntity();								 
			requestWorkFlowAuditEntity.setDescisionType(CommonConstant.CONSTANT_FOURTEEN);
			requestWorkFlowAuditEntity.setRemarks(requestVo.getRemarks());
			requestWorkFlowAuditEntity.setRequestId(requestWorkFlowAuditVo.getRequestId());
			RequestWorkFlowAuditEntity requestWorkFlowAuditEn = new RequestWorkFlowAuditEntity();
			requestWorkFlowAuditEntity.setAuditForwardRemarks(requestVo.getForwardRedirectRemarks());
			
			Integer forwardFlag = 0;
			requestWorkFlowAuditEn = requestDAO.getWorkFlow(requestWorkFlowAuditEntity,authDetailsVo,forwardFlag); //AuthDetailsVo authDetailsVo
			requestWorkFlowAuditEntity.setRequestWorkFlowAuditId(requestWorkFlowAuditEn.getRequestWorkFlowAuditId());
			requestWorkFlowAuditEntity.setSeqId(requestWorkFlowAuditEn.getSeqId());
			
			if(!(requestWorkFlowAuditEn.getDescisionType() == CommonConstant.CONSTANT_FOURTEEN 
					&& null != requestWorkFlowAuditEn.getRemarks())){				
				requestWorkFlowAuditEntity.setRemarks(requestVo.getRemarks());				
				requestWorkFlowAuditEntity.setAuditForwardRemarks(requestVo.getForwardRedirectRemarks());
			requestConfigurationDAO.updateDetailInAudit(requestWorkFlowAuditEntity, authDetailsVo);
			}
									
			requestWorkFlowAuditEn = requestDAO.getWorkFlowByRequestor(requestWorkFlowAuditEntity,authDetailsVo); //AuthDetailsVo authDetailsVo
			requestWorkFlowAuditEntity.setRequestWorkFlowAuditId(requestWorkFlowAuditEn.getRequestWorkFlowAuditId());
			requestWorkFlowAuditEntity.setSeqId(requestWorkFlowAuditEn.getSeqId());
			requestWorkFlowAuditEntity.setRemarks("");
			requestConfigurationDAO.updateDetailInAudit(requestWorkFlowAuditEntity, authDetailsVo);
								 	
		    Integer	nwStatusId =  CommonConstant.CONSTANT_NINE;			 
			requestConfigurationDAO.updateNWDecision(requestWorkFlowAuditEntity,nwStatusId ,authDetailsVo);
					
			return request;
			
		} catch (CommonException e) {
			e.printStackTrace();
			logger.error(e.getMessage());					
			throw new CommonException(e.getMessage());
		}catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	
	@SuppressWarnings("unused")
	@Transactional
	public  void  requestValidation(RequestWorkFlowAuditVO requestWorkFlowAuditVO)throws CommonException {

		if (requestWorkFlowAuditVO.getReqLocationId().equals(requestWorkFlowAuditVO.getLocationId())
				&& requestWorkFlowAuditVO.getSubLocationId().equals(requestWorkFlowAuditVO.getReqSublocationId())
				&& requestWorkFlowAuditVO.getDepartmentId().equals(requestWorkFlowAuditVO.getOriginalDepartmentId())
				&& requestWorkFlowAuditVO.getRequestTypeId().equals(requestWorkFlowAuditVO.getOriginalRequestTypeId())
				&& requestWorkFlowAuditVO.getRequestSubTypeId().equals(requestWorkFlowAuditVO.getOriginalRequestSubTypeId())) {
			throw new CommonException("forwardRequestValidation");
			  
		}
	}

	
}