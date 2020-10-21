package com.srm.rta.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.config.MailMessages;
import com.srm.coreframework.config.UserMessages;
import com.srm.coreframework.constants.FilePathConstants;
import com.srm.coreframework.response.JSONResponse;
import com.srm.coreframework.service.CommonService;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.util.DateUtil;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.coreframework.vo.UserMasterVO;
import com.srm.rta.dao.EscalationDao;
import com.srm.rta.dao.RequestDAO;
import com.srm.rta.entity.RequestEntity;
import com.srm.rta.repository.RequestRepository;
import com.srm.rta.vo.EmailVo;
import com.srm.rta.vo.RequestDetailVO;
import com.srm.rta.vo.RequestSubTypeVO;
import com.srm.rta.vo.RequestTypeVO;
import com.srm.rta.vo.RequestVO;
import com.srm.rta.vo.RequestWorkFlowAuditVO;

 

/**
 * This service class is used to get the escalation list of approval and
 * resolver and store the details in the mail parameter.
 * 
 * @author vigneshs
 *
 */
@Component
public class EscalationService extends CommonService {

	Logger logger = LoggerFactory.getLogger(EscalationService.class);

	@Autowired
	private UserMessages userMessages;

	@Autowired
	EscalationDao escalationDao;

	@Autowired
	MailControllerService mailControllerService;

	@Autowired
	RequestTypeService requestTypeService;
	
	@Autowired
	RequestSubTypeService requestSubTypeService;
		
	@Autowired
	RequestDAO requestDAO;
	
	@Autowired
	MailMessages mailMessages;

	@Value("${emailRestTemplateUrl}")
	private String emailRestTemplateUrl;
		
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	RequestRepository requestRepository;
	
	
	/**
	 * This method is used to get the list of approval
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")	 
	public void getListForApproval() throws Exception {

		try {

			List<Object> list = escalationDao.getAllPendingList();

			Iterator itr = list.iterator();

			while (itr.hasNext()) {

				Object[] obj = (Object[]) itr.next();
				RequestVO requestVo = new RequestVO();

				RequestWorkFlowAuditVO requestWorkFlowAuditVo = new RequestWorkFlowAuditVO();

				if (null != ((Object[]) obj)[0]) {
					requestWorkFlowAuditVo.setRequestWorkFlowAuditId((int) ((Object[]) obj)[0]);
				}
				if (null != ((Object[]) obj)[1]) {
					requestWorkFlowAuditVo.setWorkFlowId((int) ((Object[]) obj)[1]);
				}
				if (null != ((Object[]) obj)[2]) {
					requestWorkFlowAuditVo.setSeqId((int) ((Object[]) obj)[2]);
				}
				if (null != ((Object[]) obj)[3]) {
					requestWorkFlowAuditVo.setRequestId((int) ((Object[]) obj)[3]);
				}
				if (null != ((Object[]) obj)[4]) {
					requestWorkFlowAuditVo.setUserId((int) ((Object[]) obj)[4]);
				}
				if (null != ((Object[]) obj)[5]) {
					requestWorkFlowAuditVo.setGroupId((int) ((Object[]) obj)[5]);
				}
				if (null != ((Object[]) obj)[6]) {
					requestWorkFlowAuditVo.setSequence((int) ((Object[]) obj)[6]);
				}
				if (null != ((Object[]) obj)[7]) {
					requestWorkFlowAuditVo.setDescisionType((int) ((Object[]) obj)[7]);
				}
				if (null != ((Object[]) obj)[8]) {
					requestWorkFlowAuditVo.setApprovalExecuter((int) ((Object[]) obj)[8]);
				}
				if (null != ((Object[]) obj)[9]) {
					requestWorkFlowAuditVo.setReassignFlag((int) ((Object[]) obj)[9]);
				}
				if (null != ((Object[]) obj)[10]) {
					requestWorkFlowAuditVo.setReassignUserId((int) ((Object[]) obj)[10]);
				}
				if (null != ((Object[]) obj)[11]) {
					requestWorkFlowAuditVo.setRemarks((String) ((Object[]) obj)[11]);
				}

				if (null != ((Object[]) obj)[13]) {

					requestWorkFlowAuditVo.setMinutes((int) ((Object[]) obj)[13]);
				}

				if (null != ((Object[]) obj)[15]) {

					requestWorkFlowAuditVo.setCreatedDate((Date) ((Object[]) obj)[15]);
				}

				if (null != ((Object[]) obj)[19]) {
					requestWorkFlowAuditVo.setEntityId((int) ((Object[]) obj)[19]);
					requestVo.setEntityId((int) ((Object[]) obj)[19]);
				}
								 
				requestVo.setRequestWorkFlowAuditVo(requestWorkFlowAuditVo);
				requestVo.setUpdatedDate((Date) ((Object[]) obj)[21]);
				requestVo.setRequestCode((String) ((Object[]) obj)[22]);
				
				
				if (null != ((Object[]) obj)[23]) {

					if (((Byte) obj[23]).intValue() == 1) {
						// calculate the sla time and store the details of
						// approval in
						// mail parameter.
						calculateSLATime(requestVo);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(userMessages.getDataFailure());
		}

	}

	/**
	 * This method is used to calculate the sla time and store the details of
	 * approval in mail parameter.
	 * 
	 * @param requestVo
	 *            RequestVo
	 * @throws Exception
	 */
	public void calculateSLATime(RequestVO requestVo) throws Exception {

		JSONResponse response = new JSONResponse();
		int actualSlaTime = DateUtil.getMinutesDifference(CommonConstant.getCalenderDate(), requestVo.getUpdatedDate());

		if (mailMessages.getEscalationBefore() == CommonConstant.CONSTANT_ONE) {

			if (actualSlaTime == (requestVo.getRequestWorkFlowAuditVo().getMinutes()
					- mailMessages.getBeforeEscalationMailMinutes())) {
			
				if (requestVo.getRequestWorkFlowAuditVo().getApprovalExecuter() == 1) {
				 
					if (mailMessages.getEscalationBeforeApproval() == CommonConstant.CONSTANT_ONE) {
						 			 			
						AuthDetailsVo authDetailsVo = new AuthDetailsVo();
						authDetailsVo.setEntityId(requestVo.getEntityId());
						
						EmailVo emailVo = new EmailVo();
						
						emailVo.setMessageCode(CommonConstant.ESCALATION_BEFORE_APPROVAL);
						emailVo.setGroupId(CommonConstant.EBA);
					 
						emailVo.setRequestId(requestVo.getRequestWorkFlowAuditVo().getRequestId());
						emailVo.setRequestCode(requestVo.getRequestCode());
						emailVo.setEmailFlag(0);
						emailVo.setCreateBy(1);
						emailVo.setUpdateBy(1);
						emailVo.setCreateDate(CommonConstant.getCalenderDate());
						emailVo.setUpdateDate(CommonConstant.getCalenderDate());
						emailVo.setEntityId(requestVo.getEntityId());
						emailVo.setEscalationFlag(CommonConstant.CONSTANT_ZERO);
						
						UserMasterVO userMasterVo = getEmailAddress(requestVo.getRequestWorkFlowAuditVo().getUserId(),authDetailsVo);
						emailVo.setToUserAddress(userMasterVo.getEmailId());
						emailVo.setApproverName(userMasterVo.getUserName());
						emailVo.setUserLang(userMasterVo.getLangCode());
						emailVo.setNotificationFlag(CommonConstant.CONSTANT_ZERO);
						emailVo.setToUserId(userMasterVo.getId());
						emailVo.setUserName(userMasterVo.getUserName());
						emailVo.setEscalationFlag(1);
						
						emailVo.setRequestCode(requestVo.getRequestCode());							 
						emailVo.setRequestSubject(requestVo.getRequestSubject());							 						 																
						RequestEntity requestEntity = requestRepository.findOne(emailVo.getRequestId());
							
						
						RequestTypeVO  requestTypeVO = new RequestTypeVO();
						requestTypeVO.setRequestTypeId(requestEntity.getRequestTypeId());
						RequestTypeVO requestType = requestTypeService.view(requestTypeVO,authDetailsVo);
						emailVo.setRequestTypeName(requestType.getRequestTypeName());
						
						RequestSubTypeVO  requestSubTypeVo = new RequestSubTypeVO();
						requestSubTypeVo.setRequestSubTypeId(requestEntity.getRequestSubtypeId());
						requestSubTypeVo = requestSubTypeService.view(requestSubTypeVo, authDetailsVo);							
						emailVo.setRequestSubTypeName(requestSubTypeVo.getRequestSubTypeName());
					 											
						 List<Object[]>  objList = 	requestDAO.getRequestScreenDetails(requestVo.getRequestWorkFlowAuditVo().getRequestId(),authDetailsVo);
							
						 List<RequestDetailVO> requestDetailVOList = new ArrayList<RequestDetailVO>();
						 
						for (Object[] obj : objList) {

							RequestDetailVO requestDetailVO = new RequestDetailVO();

							if (null != (String) ((Object[]) obj)[1]) {
								requestDetailVO.setRequestScreenDetailConfigurationFieldName((String) obj[1]);
							}

							if (null != (String) ((Object[]) obj)[2] && !((String) ((Object[]) obj)[2]).isEmpty()) {
								String val = (String) ((Object[]) obj)[2];
								if (val.charAt(0) == ',') {
									requestDetailVO.setRequestScreenDetailConfigurationFieldValue(val.substring(1));
								} else {
									requestDetailVO.setRequestScreenDetailConfigurationFieldValue(val);
								}
							} else {
								requestDetailVO.setRequestScreenDetailConfigurationFieldValue("-");
							}


							requestDetailVOList.add(requestDetailVO);
						}
						emailVo.setSystemConfigurationVo(getSystemConfigurationDetails(authDetailsVo));	
						emailVo.setTrEmailFlag(CommonConstant.CONSTANT_ONE);			 
						emailVo.setRequestDetailList(requestDetailVOList);													
						emailVo.setNotSaveFlag(true);			
						
						final String uri = emailRestTemplateUrl + FilePathConstants.TOEMAIL;
						response = restTemplate.postForObject(uri, emailVo, JSONResponse.class);																				
																	
					}

				} else if (requestVo.getRequestWorkFlowAuditVo().getApprovalExecuter() == 2) {

					if (mailMessages.getEscalationBeforeResolver() == CommonConstant.CONSTANT_ONE) {
						
						AuthDetailsVo authDetailsVo = new AuthDetailsVo();
						authDetailsVo.setEntityId(requestVo.getEntityId());
						
						EmailVo emailVo = new EmailVo();
						
						emailVo.setMessageCode(CommonConstant.ESCALATION_BEFORE_RESOLVER);
						emailVo.setGroupId(CommonConstant.EBR);
						emailVo.setRequestId(requestVo.getRequestWorkFlowAuditVo().getRequestId());
						emailVo.setRequestCode(requestVo.getRequestCode());
						emailVo.setEmailFlag(0);
						emailVo.setCreateBy(1);
						emailVo.setUpdateBy(1);
						emailVo.setCreateDate(CommonConstant.getCalenderDate());
						emailVo.setUpdateDate(CommonConstant.getCalenderDate());
						emailVo.setEntityId(requestVo.getEntityId());
						emailVo.setEscalationFlag(CommonConstant.CONSTANT_ZERO);
				
						UserMasterVO userMasterVo = getEmailAddress(requestVo.getRequestWorkFlowAuditVo().getUserId(),authDetailsVo);
						emailVo.setToUserAddress(userMasterVo.getEmailId());
						emailVo.setExecutorName(userMasterVo.getUserName());
						emailVo.setUserLang(userMasterVo.getLangCode());
						emailVo.setNotificationFlag(CommonConstant.CONSTANT_ZERO);
						emailVo.setToUserId(userMasterVo.getId());
						emailVo.setUserName(userMasterVo.getUserName());
						
						emailVo.setRequestCode(requestVo.getRequestCode());						 
						emailVo.setRequestSubject(requestVo.getRequestSubject());							 						 																
													
					    RequestEntity requestEntity = requestRepository.findOne(emailVo.getRequestId());
						
						
						RequestTypeVO  requestTypeVO = new RequestTypeVO();
						requestTypeVO.setRequestTypeId(requestEntity.getRequestTypeId());
						RequestTypeVO requestType = requestTypeService.view(requestTypeVO,authDetailsVo);
						emailVo.setRequestTypeName(requestType.getRequestTypeName());
						
						RequestSubTypeVO  requestSubTypeVo = new RequestSubTypeVO();
						requestSubTypeVo.setRequestSubTypeId(requestEntity.getRequestSubtypeId());
						requestSubTypeVo = requestSubTypeService.view(requestSubTypeVo, authDetailsVo);							
						emailVo.setRequestSubTypeName(requestSubTypeVo.getRequestSubTypeName());						 						
						
						List<Object[]>  objList = 	requestDAO.getRequestScreenDetails(requestVo.getRequestWorkFlowAuditVo().getRequestId(),authDetailsVo);
							
						 List<RequestDetailVO> requestDetailVOList = new ArrayList<RequestDetailVO>();

						for (Object[] obj : objList) {

							RequestDetailVO requestDetailVO = new RequestDetailVO();

							if (null != (String) ((Object[]) obj)[1]) {
								requestDetailVO.setRequestScreenDetailConfigurationFieldName((String) obj[1]);
							}

							if (null != (String) ((Object[]) obj)[2] && !((String) ((Object[]) obj)[2]).isEmpty()) {
								String val = (String) ((Object[]) obj)[2];
								if (val.charAt(0) == ',') {
									requestDetailVO.setRequestScreenDetailConfigurationFieldValue(val.substring(1));
								} else {
									requestDetailVO.setRequestScreenDetailConfigurationFieldValue(val);
								}
							} else {
								requestDetailVO.setRequestScreenDetailConfigurationFieldValue("-");
							}


							requestDetailVOList.add(requestDetailVO);
						}
						emailVo.setSystemConfigurationVo(getSystemConfigurationDetails(authDetailsVo));	
						emailVo.setTrEmailFlag(CommonConstant.CONSTANT_ONE);					 
						emailVo.setRequestDetailList(requestDetailVOList);		
						emailVo.setNotSaveFlag(true);
																					
						final String uri = emailRestTemplateUrl + FilePathConstants.TOEMAIL;
						response = restTemplate.postForObject(uri, emailVo, JSONResponse.class);
															
					}

				}

			}
		}
	/*	if (mailMessages.getEscalation() == CommonConstant.CONSTANT_ONE) {

			if (actualSlaTime >= requestVo.getRequestWorkFlowAuditVo().getMinutes()) {

				if (requestVo.getRequestWorkFlowAuditVo().getApprovalExecuter() == 1) {

					if (mailMessages.getEscalationPendingRequest() == CommonConstant.CONSTANT_ONE) {
						// store the details of approval in mail parameter
						MailParameterEntity mailParameterEntity = new MailParameterEntity();
						mailParameterEntity.setMessageCode(CommonConstant.ESCALATION_PENDING_REQUEST);
						mailParameterEntity.setMessage(CommonConstant.EPR);
						mailParameterEntity.setUserId(requestVo.getRequestWorkFlowAuditVo().getUserId());
						mailParameterEntity.setRequestId(requestVo.getRequestWorkFlowAuditVo().getRequestId());
						mailParameterEntity.setTitle(CommonConstant.ESCALATION_PENDING_REQUEST);
						mailParameterEntity.setEmailFlag(0);
						mailParameterEntity.setEscalationFlag(1);
						mailParameterEntity.setCreateBy(AuthUtil.getUserId());
						mailParameterEntity.setCreateDate(DateUtil.getCurrentDate());
						mailParameterEntity.setUpdateBy(AuthUtil.getUserId());
						mailParameterEntity.setUpdateDate(DateUtil.getCurrentDate());
						escalationDao.createMailParameter(mailParameterEntity);
						
						AuthDetailsVo authDetailsVo = new AuthDetailsVo();
						authDetailsVo.setEntityId(requestVo.getEntityId());
						
						EmailVo emailVo = new EmailVo();
						
						emailVo.setMessageCode(CommonConstant.ESCALATION_PENDING_APPROVAL);
						emailVo.setGroupId(CommonConstant.EPA);
					 
						emailVo.setRequestId(requestVo.getRequestId());
						emailVo.setRequestCode(requestVo.getRequestCode());
						emailVo.setEmailFlag(0);
						emailVo.setCreateBy(1);
						emailVo.setUpdateBy(1);
						emailVo.setCreateDate(CommonConstant.getCalenderDate());
						emailVo.setUpdateDate(CommonConstant.getCalenderDate());
						emailVo.setEntityId(requestVo.getEntityId());
						emailVo.setEscalationFlag(CommonConstant.CONSTANT_ZERO);
						
						UserMasterVO userMasterVo = getEmailAddress(requestVo.getRequestWorkFlowAuditVo().getUserId(),authDetailsVo);
						emailVo.setToUserAddress(userMasterVo.getEmailId());
						emailVo.setApproverName(userMasterVo.getUserName());
						
						emailVo.setNotificationFlag(CommonConstant.CONSTANT_ZERO);
						emailVo.setToUserId(userMasterVo.getId());
						emailVo.setUserName(userMasterVo.getUserName());
						emailVo.setEscalationFlag(1);
						
						emailVo.setRequestCode(requestVo.getRequestCode());							 
						emailVo.setRequestSubject(requestVo.getRequestSubject());							 						 																
						
						
						RequestTypeVO  requestTypeVO = new RequestTypeVO();
						requestTypeVO.setRequestTypeId(requestVo.getRequestTypeId());
						RequestTypeVO requestType = requestTypeService.view(requestTypeVO,authDetailsVo);
						emailVo.setRequestTypeName(requestType.getRequestTypeName());
						
						RequestSubTypeVO  requestSubTypeVo = new RequestSubTypeVO();
						requestSubTypeVo.setRequestSubTypeId(requestVo.getRequestSubtypeId());
						requestSubTypeVo = requestSubTypeService.view(requestSubTypeVo, authDetailsVo);							
						emailVo.setRequestSubTypeName(requestSubTypeVo.getRequestSubTypeName());
					 											
						 List<Object[]>  objList = 	requestDAO.getRequestScreenDetails(requestVo.getRequestWorkFlowAuditVo().getRequestId(),authDetailsVo);
							
						 List<RequestDetailVO> requestDetailVOList = new ArrayList<RequestDetailVO>();
						 
						for (Object[] obj : objList) {

							RequestDetailVO requestDetailVO = new RequestDetailVO();

							if (null != (String) ((Object[]) obj)[1]) {
								requestDetailVO.setRequestScreenDetailConfigurationFieldName((String) obj[1]);
							}

							if (null != (String) ((Object[]) obj)[2]) {
								requestDetailVO.setRequestScreenDetailConfigurationFieldValue((String) obj[2]);
							}

							requestDetailVOList.add(requestDetailVO);
						}
						emailVo.setSystemConfigurationVo(getSystemConfigurationDetails(authDetailsVo));	
											 
						emailVo.setRequestDetailList(requestDetailVOList);													
												
						final String uri = emailRestTemplateUrl + FilePathConstants.TOEMAIL;
						response = restTemplate.postForObject(uri, emailVo, JSONResponse.class);																								
						
					}
				} else if (requestVo.getRequestWorkFlowAuditVo().getApprovalExecuter() == 2) {

					if (mailMessages.getEscalationPendingRequest() == CommonConstant.CONSTANT_ONE) {
						// store the details of approval in mail parameter
						MailParameterEntity mailParameterEntity = new MailParameterEntity();
						mailParameterEntity.setMessageCode(CommonConstant.ESCALATION_RESOLVER_REQUEST);
						mailParameterEntity.setMessage(CommonConstant.ERR);
						mailParameterEntity.setUserId(requestVo.getRequestWorkFlowAuditVo().getUserId());
						mailParameterEntity.setRequestId(requestVo.getRequestWorkFlowAuditVo().getRequestId());
						mailParameterEntity.setTitle(CommonConstant.ESCALATION_RESOLVER_REQUEST);
						mailParameterEntity.setEmailFlag(0);
						mailParameterEntity.setEscalationFlag(1);
						mailParameterEntity.setCreateBy(AuthUtil.getUserId());
						mailParameterEntity.setCreateDate(DateUtil.getCurrentDate());
						mailParameterEntity.setUpdateBy(AuthUtil.getUserId());
						mailParameterEntity.setUpdateDate(DateUtil.getCurrentDate());
						escalationDao.createMailParameter(mailParameterEntity);
												
						AuthDetailsVo authDetailsVo = new AuthDetailsVo();
						authDetailsVo.setEntityId(requestVo.getEntityId());
						
						EmailVo emailVo = new EmailVo();
						
						emailVo.setMessageCode(CommonConstant.ESCALATION_PENDING_RESOLVER);
						emailVo.setGroupId(CommonConstant.EPR);
						emailVo.setRequestId(requestVo.getRequestId());
						emailVo.setRequestCode(requestVo.getRequestCode());
						emailVo.setEmailFlag(0);
						emailVo.setCreateBy(1);
						emailVo.setUpdateBy(1);
						emailVo.setCreateDate(CommonConstant.getCalenderDate());
						emailVo.setUpdateDate(CommonConstant.getCalenderDate());
						emailVo.setEntityId(requestVo.getEntityId());
						emailVo.setEscalationFlag(CommonConstant.CONSTANT_ZERO);
				
						UserMasterVO userMasterVo = getEmailAddress(requestVo.getRequestWorkFlowAuditVo().getUserId(),authDetailsVo);
						emailVo.setToUserAddress(userMasterVo.getEmailId());
						emailVo.setExecutorName(userMasterVo.getUserName());
						
						emailVo.setNotificationFlag(CommonConstant.CONSTANT_ZERO);
						emailVo.setToUserId(userMasterVo.getId());
						emailVo.setUserName(userMasterVo.getUserName());
						
						emailVo.setRequestCode(requestVo.getRequestCode());						 
						emailVo.setRequestSubject(requestVo.getRequestSubject());							 						 																
													
						RequestTypeVO  requestTypeVO = new RequestTypeVO();
						requestTypeVO.setRequestTypeId(requestVo.getRequestTypeId());
						RequestTypeVO requestType = requestTypeService.view(requestTypeVO,authDetailsVo);
						emailVo.setRequestTypeName(requestType.getRequestTypeName());
						
						RequestSubTypeVO  requestSubTypeVo = new RequestSubTypeVO();
						requestSubTypeVo.setRequestSubTypeId(requestVo.getRequestSubtypeId());
						requestSubTypeVo = requestSubTypeService.view(requestSubTypeVo, authDetailsVo);							
						emailVo.setRequestSubTypeName(requestSubTypeVo.getRequestSubTypeName());						 						
						
						List<Object[]>  objList = 	requestDAO.getRequestScreenDetails(requestVo.getRequestWorkFlowAuditVo().getRequestId(),authDetailsVo);
							
						 List<RequestDetailVO> requestDetailVOList = new ArrayList<RequestDetailVO>();

						for (Object[] obj : objList) {

							RequestDetailVO requestDetailVO = new RequestDetailVO();

							if (null != (String) ((Object[]) obj)[1]) {
								requestDetailVO.setRequestScreenDetailConfigurationFieldName((String) obj[1]);
							}

							if (null != (String) ((Object[]) obj)[2]) {
								requestDetailVO.setRequestScreenDetailConfigurationFieldValue((String) obj[2]);
							}

							requestDetailVOList.add(requestDetailVO);
						}
						emailVo.setSystemConfigurationVo(getSystemConfigurationDetails(authDetailsVo));	
												 
						emailVo.setRequestDetailList(requestDetailVOList);													
													
						final String uri = emailRestTemplateUrl + FilePathConstants.TOEMAIL;
						response = restTemplate.postForObject(uri, emailVo, JSONResponse.class);

						
						
					}

				}
			}
		}*/

	}

	/**
	 * This method is used to get the list of resolver
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void getListForResolver() throws Exception {

		try {

			List<Object> list = escalationDao.getAllApprovedList();
			Iterator itr = list.iterator();
			while (itr.hasNext()) {
				Object[] obj = (Object[]) itr.next();
				RequestVO requestVo = new RequestVO();

				RequestWorkFlowAuditVO requestWorkFlowAuditVo = new RequestWorkFlowAuditVO();

				if (null != ((Object[]) obj)[0]) {
					requestWorkFlowAuditVo.setRequestWorkFlowAuditId((int) ((Object[]) obj)[0]);
				}
				if (null != ((Object[]) obj)[1]) {
					requestWorkFlowAuditVo.setWorkFlowId((int) ((Object[]) obj)[1]);
				}
				if (null != ((Object[]) obj)[2]) {
					requestWorkFlowAuditVo.setSeqId((int) ((Object[]) obj)[2]);
				}
				if (null != ((Object[]) obj)[3]) {
					requestWorkFlowAuditVo.setRequestId((int) ((Object[]) obj)[3]);
				}
				if (null != ((Object[]) obj)[4]) {
					requestWorkFlowAuditVo.setUserId((int) ((Object[]) obj)[4]);
				}
				if (null != ((Object[]) obj)[5]) {
					requestWorkFlowAuditVo.setGroupId((int) ((Object[]) obj)[5]);
				}
				if (null != ((Object[]) obj)[6]) {
					requestWorkFlowAuditVo.setSequence((int) ((Object[]) obj)[6]);
				}
				if (null != ((Object[]) obj)[7]) {
					requestWorkFlowAuditVo.setDescisionType((int) ((Object[]) obj)[7]);
				}
				if (null != ((Object[]) obj)[8]) {
					requestWorkFlowAuditVo.setApprovalExecuter((int) ((Object[]) obj)[8]);
				}
				if (null != ((Object[]) obj)[9]) {
					requestWorkFlowAuditVo.setReassignFlag((int) ((Object[]) obj)[9]);
				}
				if (null != ((Object[]) obj)[10]) {
					requestWorkFlowAuditVo.setReassignUserId((int) ((Object[]) obj)[10]);
				}
				if (null != ((Object[]) obj)[11]) {
					requestWorkFlowAuditVo.setRemarks((String) ((Object[]) obj)[11]);
				}

				if (null != ((Object[]) obj)[13]) {

					requestWorkFlowAuditVo.setMinutes((int) ((Object[]) obj)[13]);
				}

				if (null != ((Object[]) obj)[15]) {

					requestWorkFlowAuditVo.setCreatedDate((Date) ((Object[]) obj)[15]);
				}

				requestVo.setRequestWorkFlowAuditVo(requestWorkFlowAuditVo);
				requestVo.setRequestCode((String) ((Object[]) obj)[20]);
				requestVo.setUpdatedDate((Date) ((Object[]) obj)[19]);
				// calculate the sla time and store the details of approval in
				// mail parameter.
				calculateSLATime(requestVo);

			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(userMessages.getDataFailure());
		}

	}

}
