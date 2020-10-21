package com.srm.rta.service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.transaction.Transactional;

import org.jfree.util.Log;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ibm.icu.util.StringTokenizer;
import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.config.MailMessages;
import com.srm.coreframework.config.PicturePath;
import com.srm.coreframework.constants.ButtonTypeEnum;
import com.srm.coreframework.constants.DecisionTypeEnum;
import com.srm.coreframework.constants.FilePathConstants;
import com.srm.coreframework.controller.CommonController;
import com.srm.coreframework.response.JSONResponse;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.util.DateUtil;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.coreframework.vo.EntityLicenseVO;
import com.srm.coreframework.vo.UserMasterVO;
import com.srm.rta.dao.RequestConfigurationDAO;
import com.srm.rta.dao.RequestDAO;
import com.srm.rta.entity.MailParameterEntity;
import com.srm.rta.entity.RequestDetailEntity;
import com.srm.rta.entity.RequestEntity;
import com.srm.rta.entity.RequestScreenDetailConfigurationEntity;
import com.srm.rta.entity.RequestTypeEntity;
import com.srm.rta.entity.RequestWorkFlowAuditEntity;
import com.srm.rta.entity.RequestWorkFlowEntity;
import com.srm.rta.entity.RequestWorkFlowSeqEntity;
import com.srm.rta.repository.MailParameterRepository;
import com.srm.rta.repository.RequestDetailsRepository;
import com.srm.rta.repository.RequestRepository;
import com.srm.rta.vo.EmailVo;
import com.srm.rta.vo.RequestDetailVO;
import com.srm.rta.vo.RequestScreenDetailConfigurationVO;
import com.srm.rta.vo.RequestSubTypeVO;
import com.srm.rta.vo.RequestTypeVO;
import com.srm.rta.vo.RequestVO;
import com.srm.rta.vo.RequestWorkFlowAuditVO;
import com.srm.rta.vo.RequestWorkFlowVO;

import lombok.Data;
@Data
@Service
public class RequestService extends CommonController<RequestVO> {

	@Autowired
	RequestConfigurationService requestConfigurationService;

	@Autowired
	RequestDAO requestDAO;

	@Autowired
	RequestRepository requestRepository;

	@Autowired
	RequestDetailsRepository requestDetailsRepository;

	@Autowired
	MailMessages mailMessages;

	@Autowired
    PicturePath picturePath;
	
	@Autowired
	RequestTypeService requestTypeService;
	
	@Autowired
	RequestSubTypeService requestSubTypeService;
		
	@Autowired
	MailParameterRepository mailParameterRepository;

	@Value("${emailRestTemplateUrl}")
	private String emailRestTemplateUrl;
	
	@Autowired
	private RestTemplate restTemplate;
		
	@Autowired
	RequestConfigurationDAO requestConfigurationDAO;


	@Transactional
	public List<RequestVO> getAll(RequestVO requestVo,AuthDetailsVo authDetailsVo) {

		try {

			List<Object[]> listRequestEntity = requestDAO.getAll(requestVo,authDetailsVo);

			List<RequestVO> listRequestVo = getAllList(listRequestEntity);

			return listRequestVo;

		} catch (Exception e) {
			Log.info("Request Service get All Common Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

	}

	/**
	 * This method is used to copy all the EntityValues to Vo list by using for
	 * Each loop.
	 * 
	 * @param List<Object>
	 *            list_requestEntity, Map<String, String> userFieldMap, List
	 *            <ScreenFieldMaster> screenFieldMasterList
	 * @return List<RequestVo> list_RequestVo
	 */
	public List<RequestVO> getAllList(List<Object[]> listrequestEntity) {

		List<RequestVO> listRequestVo = new ArrayList<RequestVO>();

		for (Object[] object : listrequestEntity) {

			RequestVO requestVo = new RequestVO();

			if (null != object[0]) {
				requestVo.setRequestId((int) object[0]);
			}
			if (null != object[1]) {
				requestVo.setRequestCode((String) object[1]);
			}
			if (null != object[2]) {
				requestVo.setRequestDate((Date) object[2]);
			}
			if (null != object[3]) {
				requestVo.setRequestTypeName((String) object[3]);
			}
			if (null != object[4]) {
				requestVo.setRequestSubTypeName((String) object[4]);
			}
			if (null != object[5]) {
				requestVo.setLocationName((String) object[5]);
			}
			if (null != object[6]) {
				requestVo.setSublocationName((String) object[6]);
			}
			if (null != object[7]) {
				requestVo.setUserDepartmentName((String) object[7]);
			}
			if (null != object[8]) {
				requestVo.setCurrentStatusName((String) object[8]);
			}
			if (null != object[9]) {
				requestVo.setRequestTypeId((int) object[9]);
			}
			if (null != object[10]) {
				requestVo.setRequestSubtypeId((int) object[10]);
			}
			if (null != object[11]) {
				requestVo.setId((int) object[11]);
			}
			if (null != object[12]) {
				requestVo.setSublocationId((int) object[12]);
			}
			if (null != object[13]) {
				requestVo.setDepartmentId((int) object[13]);
			}
			if (null != object[14]) {
				requestVo.setCurrentStatusId((int) object[14]);

			}
			if (null != object[15]) {
				requestVo.setCurrentStatusCode((String) object[15]);

			}
			
			if (null != object[17]) {
				requestVo.setRequestSubject((String) object[17]);

			}
			
			if (null != object[19]) {
				requestVo.setReqLocationName((String) object[19]);

			}
			if (null != object[20]) {
				requestVo.setReqSublocationName((String) object[20]);

			}
			if (null != object[21]) {
				requestVo.setReqLocationId((int) object[21]);

			}
			if (null != object[22]) {
				requestVo.setReqSublocationId((int) object[22]);
			}
			
			if (null != object[23]) {
				requestVo.setResolverRemarks((String) object[23]);
			}									
			
			if (null != object[24]) {
				requestVo.setForwardRedirectRemarks((String) object[24]);
			}	
		 
			if (null != object[25]) {
				requestVo.setForwardRequestId((int) object[25]);
			}
			
			if (null != object[26]) {
				requestVo.setRedirectRequestId((int) object[26]);
			}
			
			listRequestVo.add(requestVo);
		}

		return listRequestVo;

	}

	@Transactional
	public List<RequestVO> getAllSearch(RequestVO requestVoSearch,AuthDetailsVo authDetailsVo) {

		try {

			List<Object[]> listRequestEntity = requestDAO.getAllSearch(requestVoSearch,authDetailsVo);

			List<RequestVO> listRequestVo = getAllList(listRequestEntity);

			return listRequestVo;

		} catch (Exception e) {
			Log.info("Request Service get All Search Common Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

	}
	
	@Transactional
	public Integer getForwardRequesterId(Integer requestId)throws CommonException , Exception{

		Integer requestorId = 0;
		RequestEntity req = requestRepository.findOne(requestId);
		
		if(null != req.getCreateBy()){
			requestorId = req.getCreateBy() ;
		}
		return requestorId;
	}
	
	
	@SuppressWarnings("unused")
	@Transactional
	public RequestVO create(RequestVO requestVo, MultipartFile[] uploadingFiles,AuthDetailsVo authDetailsVo)throws CommonException , Exception{

		RequestVO request = new RequestVO();
		
		try{
		
		RequestEntity requestEntity = new RequestEntity();
		String requestCode = "";
		List<RequestWorkFlowAuditVO> requestWorkFlowAuditVoList = new ArrayList<RequestWorkFlowAuditVO>();
		List<RequestWorkFlowAuditVO> requestWorkFlowAuditVoMailList = new ArrayList<RequestWorkFlowAuditVO>();
		List<Object> requestWorkFlowAuditEntity = null;

		List<Object> object = new ArrayList<>();

		List<RequestWorkFlowSeqEntity> requestWorkFlowSeqEntity = new ArrayList<>();
		List<RequestWorkFlowEntity> requestWorkFlowEntityList = new ArrayList<RequestWorkFlowEntity>();

		if (uploadingFiles != null) {
			requestEntity = saveAttachment(requestEntity, uploadingFiles,authDetailsVo);
		}
		if(null!=requestVo.getForwardRequestId()){
			requestEntity.setForwardRequestId(requestVo.getForwardRequestId());
		}
		if(null != requestEntity.getRequestAttachment()){
			requestEntity.setRequestAttachment(requestEntity.getRequestAttachment());
			requestVo.setRequestAttachment(requestEntity.getRequestAttachment());
		}
		

		// If request is not resubmitted then it will not be updated
		if (requestVo.getRequestId() != null) {
			if (!requestVo.getCurrentStatusCode().equalsIgnoreCase(DecisionTypeEnum.RS.toString())) {
				throw new CommonException("requestUpdateRestriction");
			}
			
			requestVo.setRequestAttachment(requestEntity.getRequestAttachment());
			requestEntity = requestRepository.findOne(requestVo.getRequestId());
			if (!requestEntity.getRequestCode().equalsIgnoreCase(requestVo.getRequestCode())) {
				throw new CommonException("request_validation_requestCodeDidNotMatch");

			}

			// If request is resubmitted only set audit delete flag as 1			 
			requestDAO.updateAuditList(requestEntity.getRequestId());
			 

			// SubId - request is created -->subid- 0 and request is resubmitted
			// -->submit get incremented

			requestVo.setRequestSubId(requestEntity.getRequestSubId() + 1);
		} else if (requestVo.getRequestId() == null) {
			requestVo.setRequestSubId(0);
			 
			requestCode = requestDAO.findAutoGenericCode(CommonConstant.Request,authDetailsVo);
			 
			requestVo.setRequestCode(requestCode);

		}

		int statusId;
		String status = DecisionTypeEnum.PEN.toString();
		statusId = findCurrentStatusId(status,authDetailsVo);
		requestVo.setRequestMobileNo(requestVo.getRequestMobileNo());
		requestVo.setCurrentStatusId(statusId);

		Date date = new Date();
		requestVo.setRequestDate(date);
		requestVo.setRequestAttachment(requestEntity.getRequestAttachment());
		BeanUtils.copyProperties(requestVo, requestEntity);
	 
		
		if (null != requestVo.getForwardRequestId()) {

			int locationId = requestEntity.getId();
			int subLocationId = requestEntity.getSublocationId();

			requestEntity.setId(requestEntity.getReqLocationId());
			requestEntity.setSublocationId(requestEntity.getReqSublocationId());
			requestEntity.setReqLocationId(locationId);
			requestEntity.setReqSublocationId(subLocationId);

		/*	requestRepository.save(requestEntity);
			request.setRequestCode(requestEntity.getRequestCode());*/

		}else if (null != requestVo.getSubrequestId()) {

			int locationId = requestEntity.getId();
			int subLocationId = requestEntity.getSublocationId();

			requestEntity.setId(requestEntity.getReqLocationId());
			requestEntity.setSublocationId(requestEntity.getReqSublocationId());
			requestEntity.setReqLocationId(locationId);
			requestEntity.setReqSublocationId(subLocationId);

			/*requestRepository.save(requestEntity);
			request.setRequestCode(requestEntity.getRequestCode());*/

		}		
		
		// Check whether Request WorkFlow Configuration is available for request
		requestWorkFlowEntityList = (List<RequestWorkFlowEntity>) requestDAO.findbyRequest(requestEntity);

		if (requestWorkFlowEntityList.size() == 0) {
			throw new CommonException("requestWorkflowNotAvailable");

		} else if (requestWorkFlowEntityList.size() > 1) {
			throw new CommonException("requestWorkflowNotAvailable");

		}

		// Check whether the approval sequence is available

		requestWorkFlowSeqEntity = (List<RequestWorkFlowSeqEntity>) requestDAO
				.findWorkFlowId((int) requestWorkFlowEntityList.get(0).getReqWorkFlowId());

		// If no approvals set status as approved
		if (null == requestWorkFlowSeqEntity) {

			status = DecisionTypeEnum.APP.toString();
			statusId = findCurrentStatusId(status, authDetailsVo);

			requestEntity.setCurrentStatusId(statusId);
		}

	/*	if (null != requestVo.getUserId()) {
			requestEntity.setCreateBy(requestVo.getUserId());
		}*/
		requestEntity = setCreateUserDetails(requestEntity,authDetailsVo,requestVo);
		requestVo.setCreatedDate(requestEntity.getCreateDate());				
		
		if (null != requestVo.getEntityId()) {
			requestEntity.setEntityLicenseId(requestVo.getEntityId());
		} else {
			requestEntity.setEntityLicenseId(authDetailsVo.getEntityId());
		}

			requestRepository.save(requestEntity);
			request.setRequestCode(requestEntity.getRequestCode());


		if (null != requestVo  && null != requestVo.getRequestDetailList()) {

			fetchRequestDetailValues(requestVo, requestEntity,authDetailsVo);
		}

		if (null != authDetailsVo.getUserId()) {
			// Corresponding approvers and executers are assigned
			requestConfigurationService.createWorkFlow(requestEntity.getRequestId(), requestVo,authDetailsVo);
		} else {

			requestConfigurationService.createWorkFlow(requestEntity.getRequestId(), requestVo,authDetailsVo);

		}

		// Find all approvers for the request
		RequestEntity req = requestRepository.findOne(requestEntity.getRequestId());

		requestWorkFlowAuditEntity = requestDAO.getAllApproval(requestEntity.getRequestId());

		object = requestDAO.getAllApprovalwithMail(requestEntity.getRequestId());

		if (req.getCurrentStatusId() == 5) {

			if (null != requestWorkFlowSeqEntity) {

				requestDAO.directapprovedAudit(requestEntity.getRequestId(), authDetailsVo);

			}
		}
		 
		// Approval Mail for Request
		
		if (null != requestWorkFlowAuditEntity) {
			requestWorkFlowAuditVoMailList = getAllApprovalWithMail(object, requestVo,authDetailsVo);
			/*requestWorkFlowAuditVoList = getAllApproval(requestWorkFlowAuditEntity, requestVo);
			request.setRequestWorkFlowAuditVoList(requestWorkFlowAuditVoList);*/
		}
		 
		requestWorkFlowAuditEntity = null;
		requestWorkFlowAuditVoList = null;

		// Method to get resubmitted list
		if (0 != requestVo.getRequestSubId()) {

			requestWorkFlowAuditEntity = requestDAO.getAllReSubmitList(requestEntity.getRequestId());

			// Method to set values for resubmitted list
			if (null != requestWorkFlowAuditEntity) {
				requestWorkFlowAuditVoList = getAllApproval(requestWorkFlowAuditEntity,authDetailsVo);
				request.setReSubmitList(requestWorkFlowAuditVoList);
			}
		}

		// Approval Sequence will be find and updated
		requestDAO.findApprovalSequence(requestEntity);
		 
		}catch (CommonException e) {
			Log.info("Request Service create Common Exception",e);
			throw new CommonException(e.getMessage());
		}catch (Exception e) {
			Log.info("Request Service create Exception",e);
			throw new Exception(e.getMessage());
		}
		
		return request;
	}

	@Transactional
	public void reopen(RequestVO requestVo,AuthDetailsVo authDetailsVo) {

		List<RequestWorkFlowAuditEntity> requestWorkFlowAuditEntityList = new ArrayList<>();
		RequestWorkFlowAuditVO requestWorkFlowAuditVo = new RequestWorkFlowAuditVO();
		try {
			requestWorkFlowAuditEntityList = requestDAO.reOpen(requestVo);
		
		if (requestWorkFlowAuditEntityList == null || requestWorkFlowAuditEntityList.size() == 0
				|| requestWorkFlowAuditEntityList.size() > 1) {
			throw new CommonException(getMessage("requestReOpen",authDetailsVo));

		} else if (requestWorkFlowAuditEntityList != null && requestWorkFlowAuditEntityList.size() == 1) {
			BeanUtils.copyProperties(requestWorkFlowAuditEntityList.get(0), requestWorkFlowAuditVo);
			if (null != requestVo.getRemarks()) {
				requestWorkFlowAuditVo.setRemarks(requestVo.getRemarks());
			}
			requestWorkFlowAuditVo.setDescisionType(requestVo.getCurrentStatusId());
			requestWorkFlowAuditVo.setRequestId(requestVo.getRequestId());

			if (null != authDetailsVo.getEntityId()) {
				requestWorkFlowAuditVo.setEntityId(authDetailsVo.getEntityId());

			} else {
				requestWorkFlowAuditVo.setEntityId(requestVo.getEntityId());
			}

			if (null != authDetailsVo.getUserId()) {
				requestWorkFlowAuditVo.setUpdatedBy(authDetailsVo.getUserId());
			} else {
				requestWorkFlowAuditVo.setUpdatedBy(requestVo.getCreatedBy());
			}

			requestConfigurationService.repopulate(requestWorkFlowAuditVo,authDetailsVo,requestVo);
		}
		} catch (CommonException exe) {
			exe.printStackTrace();
			Log.info("Request Service get reopen Common Exception",exe);
			throw new CommonException(getMessage(exe.getMessage(),authDetailsVo));
		}catch (Exception exe) {

			Log.info("Request Service get reopen Exception",exe);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

	}
	
	@SuppressWarnings("rawtypes")
	@Transactional
	public RequestVO resolverRequestView(RequestVO requestLoad,AuthDetailsVo authDetailsVo) throws CommonException, Exception {
		List<Object> result = new ArrayList<>();

		List<RequestWorkFlowAuditVO> list_RequestWorkFlowAuditVo = new ArrayList<RequestWorkFlowAuditVO>();

		List<Object> list_RequestWorkFlowAuditEntity = new ArrayList<>();

		List<RequestDetailVO> listRequestVoList = new ArrayList<RequestDetailVO>();
		RequestVO requestVo = new RequestVO();
		RequestVO request = new RequestVO();
		
		try{
		
			BeanUtils.copyProperties(requestLoad, request);
		
			result = requestDAO.resolverRequestView(requestLoad,authDetailsVo);
	
			//getReassign flag				
			
		if (null != result && result.size() > 0) {

			Iterator itr = result.iterator();
			RequestDetailVO requestDetailVo = null;
			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				requestDetailVo = new RequestDetailVO();
				if (null != object[0]) {
					requestVo.setRequestId((int) object[0]);
				}
				if (null != object[1]) {

					requestVo.setRequestCode((String) object[1]);
				}
				if (null != object[2]) {

					requestVo.setRequestDate((Date) object[2]);
				}
				if (null != object[16]) {

					requestVo.setRequestFromDate((Date) object[16]);
				}
				if (null != object[17]) {

					requestVo.setRequestToDate((Date) object[17]);
				}
				if (null != object[9]) {

					requestVo.setRequestSubject((String) object[29]);
				}
				if (null != object[15]) {

					requestVo.setRequestPriority((int) object[15]);
				}
				if (null != object[19]) {

					requestVo.setRequestMobileNo((String) object[19]);
				}
				if (null != object[20]) {

					requestVo.setRequestExtension((int) object[20]);
				}
				if (null != object[9]) {

					requestVo.setRequestTypeId((int) object[9]);
				}
				if (null != object[10]) {

					requestVo.setRequestSubtypeId((int) object[10]);
				}
				if (null != object[40]) {

					requestVo.setId((int) object[40]);
				}
				if (null != object[41]) {

					requestVo.setSublocationId((int) object[41]);
				}
				if (null != object[13]) {

					requestVo.setDepartmentId((int) object[13]);
				}
				if (null != object[14]) {

					requestVo.setCurrentStatusId((int) object[14]);
				}
				if (null != object[3]) {

					requestVo.setRequestTypeName((String) object[3]);
				}
				if (null != object[4]) {

					requestVo.setRequestSubTypeName((String) object[4]);
				}
				if (null != object[38]) {

					requestVo.setLocationName((String) object[38]);
				} 

				if (null != object[39]) {

					requestVo.setSublocationName((String) object[39]);
				}
				
			/*	if (null != object[5]) {

					requestVo.setLocationName((String) object[5]);
				} 

				if (null != object[6]) {

					requestVo.setSublocationName((String) object[6]);
				}*/
				
				
				if (null != object[7]) {

					requestVo.setUserDepartmentName((String) object[7]);
				}
				if (null != object[8]) {

					requestVo.setCurrentStatusName((String) object[8]);
				}
				if (null != object[21]) {

					requestVo.setRequestSeq((String) object[21]);
				}

				if (null != object[18] && (int) object[18] == 1) {
					requestVo.setRequestIsCancel(true);
				} else if (null != object[18] && (int) object[18] == 0) {
					requestVo.setRequestIsCancel(false);

				}
				if (null != object[22]) {

					requestDetailVo.setRequestDetailId((int) object[22]);
				}
				if (null != object[23]) {

					requestDetailVo.setRequestId((int) object[23]);
				}
				if (null != object[24]) {

					requestDetailVo.setRequestScreenConfigId((int) object[24]);
				}
				if (null != object[25]) {

					requestDetailVo.setRequestScreenDetailConfigId((int) object[25]);
				}
				if (null != object[27] && ButtonTypeEnum.C.toString().equalsIgnoreCase((String) object[26])) {
					StringTokenizer st3 = new StringTokenizer((String) object[27], ",");

					List<String> list_value = new ArrayList<>();
					while (st3.hasMoreElements()) {
						list_value.add(st3.nextToken());

					}
					requestDetailVo.setObjectList(list_value);
				} else if (null != object[27] && !ButtonTypeEnum.C.toString().equalsIgnoreCase((String) object[26])) {
					requestDetailVo.setRequestScreenDetailConfigurationFieldValue((String) object[27]);
				}
				if (null != object[26]) {

					requestDetailVo.setRequestScreenDetailConfigurationFieldType((String) object[26]);
				}

				if (null != object[28] && (int) object[28] == 1) {
					requestDetailVo.setRequestScreenDetailConfigurationIsActive(true);
				} else if (null != object[28] && (int) object[28] == 0) {
					requestDetailVo.setRequestScreenDetailConfigurationIsActive(false);

				}
				if (null != object[30] && (ButtonTypeEnum.S.toString().equalsIgnoreCase((String) object[26])
						|| ButtonTypeEnum.R.toString().equalsIgnoreCase((String) object[26])
						|| ButtonTypeEnum.C.toString().equalsIgnoreCase((String) object[26]))) {

					StringTokenizer st3 = new StringTokenizer((String) object[30], ",");

					List<String> list_value = new ArrayList<>();
					while (st3.hasMoreElements()) {
						list_value.add(st3.nextToken());

					}
					requestDetailVo.setList_value(list_value);
				}

				if (null != object[31]) {

					requestDetailVo.setRequestScreenDetailConfigurationFieldName((String) object[31]);
				}
				if (null != object[32]) {

					requestVo.setCreatedDate((Date) object[32]);
				}
				if (null != object[33]) {

					requestVo.setCurrentStatusCode((String) object[33]);
				}
				if (null != object[34]) {

					requestVo.setRequestSubId((int) object[34]);
				}
				if (null != object[36]) {

					requestVo.setUserName((String) object[36]);
				}

				if (null != (String) ((Object[]) object)[37]) {

					StringBuffer modifiedQuery = new StringBuffer(picturePath.getRequestAttachmentPath());
					File file = new File((String) ((Object[]) object)[37]);
					modifiedQuery.append(file.getName());
					requestVo.setRequestAttachment(file.getName());

				}
				if (null != object[38]) {

					requestVo.setReqLocationName((String) object[38]);
				}

				if (null != object[39]) {

					requestVo.setReqSublocationName((String) object[39]);
				}				 			
				
				if (null != object[11]) {

					requestVo.setReqLocationId((int) object[11]);
				}

				if (null != object[12]) {

					requestVo.setReqSublocationId((int) object[12]);
				}

				if (null != object[42]) {
					requestVo.setResolverRemarks((String) object[42]);
				}
				
				if (null != object[43]) {
					requestVo.setForwardRedirectRemarks((String) object[43]);
				}
				
				if (null != object[44]) {

					requestVo.setForwardRequestId((int) object[44]);
				}
				
				if (null != object[45]) {
					requestVo.setSubrequestId((int) object[45]);
				}
								
				//added for forward/redirect request view
					if (null != requestVo.getSubrequestId()) {
						if (null != object[46]) {

							requestVo.setLocationName((String) object[46]);
						}

						if (null != object[47]) {

							requestVo.setSublocationName((String) object[47]);
						}
					}			
				
				listRequestVoList.add(requestDetailVo);

			}
			
		}
		request.setRequest(requestVo);
		request.setRequestDetailList(listRequestVoList);
		
		try {
			if(null != requestVo.getRequestId()){
				list_RequestWorkFlowAuditEntity = requestDAO.getAllApproval(requestVo.getRequestId());
			}
		} catch (CommonException e) {
			e.printStackTrace();
			Log.info("Request Service get findRequest Common Exception",e);
			throw new CommonException(getMessage("requestWorkFlowAuditListFailure",authDetailsVo));
		}
		if (list_RequestWorkFlowAuditEntity != null && list_RequestWorkFlowAuditEntity.size() > 0) {
			list_RequestWorkFlowAuditVo = getAllApproval(list_RequestWorkFlowAuditEntity , requestVo , authDetailsVo);
			request.setRequestWorkFlowAuditVoList(list_RequestWorkFlowAuditVo);
		}
		list_RequestWorkFlowAuditEntity = new ArrayList<>();
		list_RequestWorkFlowAuditVo = new ArrayList<>();
	/*	if (null != requestVo.getRequestSubId()) {
			try {
				list_RequestWorkFlowAuditEntity = requestDAO.getAllReSubmitList(requestVo.getRequestId());

			} catch (CommonException e) {
				Log.info("Request Service get findRequest Exception",e);
				throw new CommonException(getMessage("requestReSubmitListFailure",authDetailsVo));

			}

			if (list_RequestWorkFlowAuditEntity != null) {
				list_RequestWorkFlowAuditVo = getAllApproval(list_RequestWorkFlowAuditEntity);
				request.setReSubmitList(list_RequestWorkFlowAuditVo);
			}
		}*/
		}catch (CommonException e) {
			e.printStackTrace();
			Log.info("Request Service get findRequest Common Exception",e);
			throw new CommonException(e.toString());
		}
		return request;
	}

	
	
	
	@SuppressWarnings("rawtypes")
	@Transactional
	public RequestVO findRequest(RequestVO requestLoad,AuthDetailsVo authDetailsVo) throws CommonException, Exception {
		List<Object> result = new ArrayList<>();

		List<RequestWorkFlowAuditVO> list_RequestWorkFlowAuditVo = new ArrayList<RequestWorkFlowAuditVO>();

		List<Object> list_RequestWorkFlowAuditEntity = new ArrayList<>();

		List<RequestDetailVO> listRequestVoList = new ArrayList<RequestDetailVO>();
		RequestVO requestVo = new RequestVO();
		RequestVO request = new RequestVO();
		
		try{
		
			BeanUtils.copyProperties(requestLoad, request);
		
			result = requestDAO.findRequest(requestLoad,authDetailsVo);
	
			//getReassign flag				
			
		if (null != result && result.size() > 0) {

			Iterator itr = result.iterator();
			RequestDetailVO requestDetailVo = null;
			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				requestDetailVo = new RequestDetailVO();
				if (null != object[0]) {
					requestVo.setRequestId((int) object[0]);
				}
				if (null != object[1]) {

					requestVo.setRequestCode((String) object[1]);
				}
				if (null != object[2]) {

					requestVo.setRequestDate((Date) object[2]);
				}
				if (null != object[16]) {

					requestVo.setRequestFromDate((Date) object[16]);
				}
				if (null != object[17]) {

					requestVo.setRequestToDate((Date) object[17]);
				}
				if (null != object[9]) {

					requestVo.setRequestSubject((String) object[29]);
				}
				if (null != object[15]) {

					requestVo.setRequestPriority((int) object[15]);
				}
				if (null != object[19]) {

					requestVo.setRequestMobileNo((String) object[19]);
				}
				if (null != object[20]) {

					requestVo.setRequestExtension((int) object[20]);
				}
				if (null != object[9]) {

					requestVo.setRequestTypeId((int) object[9]);
				}
				if (null != object[10]) {

					requestVo.setRequestSubtypeId((int) object[10]);
				}
				if (null != object[40]) {

					requestVo.setId((int) object[40]);
				}
				if (null != object[41]) {

					requestVo.setSublocationId((int) object[41]);
				}
				if (null != object[13]) {

					requestVo.setDepartmentId((int) object[13]);
				}
				if (null != object[14]) {

					requestVo.setCurrentStatusId((int) object[14]);
				}
				if (null != object[3]) {

					requestVo.setRequestTypeName((String) object[3]);
				}
				if (null != object[4]) {

					requestVo.setRequestSubTypeName((String) object[4]);
				}
/*				if (null != object[38]) {

					requestVo.setLocationName((String) object[38]);
				} 

				if (null != object[39]) {

					requestVo.setSublocationName((String) object[39]);
				}*/
				
				if (null != object[5]) {

					requestVo.setLocationName((String) object[5]);
				} 

				if (null != object[6]) {

					requestVo.setSublocationName((String) object[6]);
				}
				
				
				if (null != object[7]) {

					requestVo.setUserDepartmentName((String) object[7]);
				}
				if (null != object[8]) {

					requestVo.setCurrentStatusName((String) object[8]);
				}
				if (null != object[21]) {

					requestVo.setRequestSeq((String) object[21]);
				}

				if (null != object[18] && (int) object[18] == 1) {
					requestVo.setRequestIsCancel(true);
				} else if (null != object[18] && (int) object[18] == 0) {
					requestVo.setRequestIsCancel(false);

				}
				if (null != object[22]) {

					requestDetailVo.setRequestDetailId((int) object[22]);
				}
				if (null != object[23]) {

					requestDetailVo.setRequestId((int) object[23]);
				}
				if (null != object[24]) {

					requestDetailVo.setRequestScreenConfigId((int) object[24]);
				}
				if (null != object[25]) {

					requestDetailVo.setRequestScreenDetailConfigId((int) object[25]);
				}
				if (null != object[27] && ButtonTypeEnum.C.toString().equalsIgnoreCase((String) object[26])) {
					StringTokenizer st3 = new StringTokenizer((String) object[27], ",");

					List<String> list_value = new ArrayList<>();
					while (st3.hasMoreElements()) {
						list_value.add(st3.nextToken());

					}
					requestDetailVo.setObjectList(list_value);
				} else if (null != object[27] && !ButtonTypeEnum.C.toString().equalsIgnoreCase((String) object[26])) {
					requestDetailVo.setRequestScreenDetailConfigurationFieldValue((String) object[27]);
				}
				if (null != object[26]) {

					requestDetailVo.setRequestScreenDetailConfigurationFieldType((String) object[26]);
				}

				if (null != object[28] && (int) object[28] == 1) {
					requestDetailVo.setRequestScreenDetailConfigurationIsActive(true);
				} else if (null != object[28] && (int) object[28] == 0) {
					requestDetailVo.setRequestScreenDetailConfigurationIsActive(false);

				}
				if (null != object[30] && (ButtonTypeEnum.S.toString().equalsIgnoreCase((String) object[26])
						|| ButtonTypeEnum.R.toString().equalsIgnoreCase((String) object[26])
						|| ButtonTypeEnum.C.toString().equalsIgnoreCase((String) object[26]))) {

					StringTokenizer st3 = new StringTokenizer((String) object[30], ",");

					List<String> list_value = new ArrayList<>();
					while (st3.hasMoreElements()) {
						list_value.add(st3.nextToken());

					}
					requestDetailVo.setList_value(list_value);
				}

				if (null != object[31]) {

					requestDetailVo.setRequestScreenDetailConfigurationFieldName((String) object[31]);
				}
				if (null != object[32]) {

					requestVo.setCreatedDate((Date) object[32]);
				}
				if (null != object[33]) {

					requestVo.setCurrentStatusCode((String) object[33]);
				}
				if (null != object[34]) {

					requestVo.setRequestSubId((int) object[34]);
				}
				if (null != object[36]) {

					requestVo.setUserName((String) object[36]);
				}

				if (null != (String) ((Object[]) object)[37]) {

					StringBuffer modifiedQuery = new StringBuffer(picturePath.getRequestAttachmentPath());
					File file = new File((String) ((Object[]) object)[37]);
					modifiedQuery.append(file.getName());
					requestVo.setRequestAttachment(file.getName());

				}
				if (null != object[38]) {

					requestVo.setReqLocationName((String) object[38]);
				}

				if (null != object[39]) {

					requestVo.setReqSublocationName((String) object[39]);
				}				 			
				
				if (null != object[11]) {

					requestVo.setReqLocationId((int) object[11]);
				}

				if (null != object[12]) {

					requestVo.setReqSublocationId((int) object[12]);
				}

				if (null != object[42]) {
					requestVo.setResolverRemarks((String) object[42]);
				}
				
				if (null != object[43]) {
					requestVo.setForwardRedirectRemarks((String) object[43]);
				}
				
				if (null != object[44]) {

					requestVo.setForwardRequestId((int) object[44]);
				}
				
				if (null != object[45]) {
					requestVo.setSubrequestId((int) object[45]);
				}
								
			 
				
				listRequestVoList.add(requestDetailVo);

			}
			
		}
		request.setRequest(requestVo);
		request.setRequestDetailList(listRequestVoList);
		
		try {
			if(null != requestVo.getRequestId()){
				list_RequestWorkFlowAuditEntity = requestDAO.getAllApproval(requestVo.getRequestId());
			}
		} catch (CommonException e) {
			e.printStackTrace();
			Log.info("Request Service get findRequest Common Exception",e);
			throw new CommonException(getMessage("requestWorkFlowAuditListFailure",authDetailsVo));
		}
		if (list_RequestWorkFlowAuditEntity != null && list_RequestWorkFlowAuditEntity.size() > 0) {
			list_RequestWorkFlowAuditVo = getAllApproval(list_RequestWorkFlowAuditEntity , requestVo,authDetailsVo);
			request.setRequestWorkFlowAuditVoList(list_RequestWorkFlowAuditVo);
		}
		list_RequestWorkFlowAuditEntity = new ArrayList<>();
		list_RequestWorkFlowAuditVo = new ArrayList<>();
		if (null != requestVo.getRequestSubId()) {
			try {
				list_RequestWorkFlowAuditEntity = requestDAO.getAllReSubmitList(requestVo.getRequestId());

			} catch (CommonException e) {
				Log.info("Request Service get findRequest Exception",e);
				throw new CommonException(getMessage("requestReSubmitListFailure",authDetailsVo));

			}

			if (list_RequestWorkFlowAuditEntity != null) {
				list_RequestWorkFlowAuditVo = getAllApproval(list_RequestWorkFlowAuditEntity, authDetailsVo);
				request.setReSubmitList(list_RequestWorkFlowAuditVo);
			}
		}
		}catch (CommonException e) {
			e.printStackTrace();
			Log.info("Request Service get findRequest Common Exception",e);
			throw new CommonException(e.toString());
		}
		return request;
	}

	@Transactional
	public void fetchRequestDetailValues(RequestVO requestVo, RequestEntity requestEntity,AuthDetailsVo authDetailsVo) throws CommonException  {

		List<RequestDetailVO> requestDetailVoList = requestVo.getRequestDetailList();

		try {

			for (RequestDetailVO requestDetailVo : requestDetailVoList) {

				String fieldValue = new String(" ");

				RequestDetailEntity requestDetailEntity = null;
				requestDetailVo.setRequestId(requestEntity.getRequestId());

				if (requestDetailVo.getRequestDetailId() != null) {
					requestDetailEntity = new RequestDetailEntity();
					requestDetailEntity = requestDetailsRepository.findOne(requestDetailVo.getRequestDetailId());
				}
				if (requestDetailVo.getObjectList() != null && ButtonTypeEnum.C.toString()
						.equalsIgnoreCase(requestDetailVo.getRequestScreenDetailConfigurationFieldType())) {
					for (String str : requestDetailVo.getObjectList()) {
						fieldValue = fieldValue + "," + str;
					}
					requestDetailVo.setRequestScreenDetailConfigurationFieldValue(fieldValue.substring(1));
				}

				if (null != requestVo.getForwardRequestId()) {

					requestDetailEntity = new RequestDetailEntity();
					requestDetailEntity.setRequestId(requestDetailVo.getRequestId());
					requestDetailEntity.setRequestScreenDetailConfigurationFieldType(
							requestDetailVo.getRequestScreenDetailConfigurationFieldType());
					requestDetailEntity.setRequestScreenDetailConfigurationFieldValue(
							requestDetailVo.getRequestScreenDetailConfigurationFieldValue());
					requestDetailEntity.setRequestScreenDetailConfigurationIsActive(true);
					requestDetailEntity.setRequestScreenConfigId(requestDetailVo.getRequestScreenConfigId());
					requestDetailEntity
							.setRequestScreenDetailConfigId(requestDetailVo.getRequestScreenDetailConfigId());
					requestDetailEntity.setRequestDetailIsCancel(false);

				} else {
					requestDetailEntity = new RequestDetailEntity();
					BeanUtils.copyProperties(requestDetailVo, requestDetailEntity);
				}

				/*
				 * if (null != requestVo.getUserId()) {
				 * requestDetailEntity.setCreateBy(requestVo.getUserId()); }
				 */

				if (null != requestVo.getForwardRequestId()) {
					requestDetailEntity = setCreateUserRequestDetails(requestDetailEntity, authDetailsVo, requestVo);
				} else if (requestDetailVo.getRequestDetailId() == null) {
					requestDetailEntity = setCreateUserRequestDetails(requestDetailEntity, authDetailsVo, requestVo);
				} else {
					requestDetailEntity = setUpdatedUserRequestDetails(requestDetailEntity, authDetailsVo);
				}
				if (null != authDetailsVo.getEntityId()) {
					requestDetailEntity.setEntityLicenseId(authDetailsVo.getEntityId());
				} else {
					requestDetailEntity.setEntityLicenseId(requestVo.getEntityId());
				}

				requestDetailsRepository.save(requestDetailEntity);

			}
		} catch (Exception e) {
			
			Log.info("Request Service get fetchRequestDetailValues  Exception",e);
			throw new CommonException("dataFailure");
		}

	}

	/**
	 * This method is used to copy the list of approvals for the single Request.
	 * 
	 * 
	 * @param List<Object[]>
	 *            list_RequestWorkFlowAuditEntity
	 * @return List<RequestWorkFlowAuditVo> list_RequestWorkFlowAuditVo
	 */
	@Transactional
	public List<RequestWorkFlowAuditVO> getAllApproval(List<Object> list_RequestWorkFlowAuditEntity , AuthDetailsVo authDetailsVo)
			throws CommonException , Exception {
		List<RequestWorkFlowAuditVO> list_RequestWorkFlowAuditVo = new ArrayList<RequestWorkFlowAuditVO>();
		 
		try{
		Date updateDate = null;
		int groupId = 0;
		int actualSlaTime = 0;
		if (!list_RequestWorkFlowAuditEntity.isEmpty()) {
			if (null != (String) ((Object[]) list_RequestWorkFlowAuditEntity.get(0))[22]) {
				String requestSequence = (String) ((Object[]) list_RequestWorkFlowAuditEntity.get(0))[22];
				StringTokenizer st3 = new StringTokenizer(requestSequence, ",");

				List<Integer> list_value = new ArrayList<>();
				while (st3.hasMoreElements()) {

					list_value.add(Integer.parseInt(st3.nextToken()));

				}
			}
		}
	
		for (Object object : list_RequestWorkFlowAuditEntity) {
			RequestWorkFlowAuditVO requestWorkFlowAuditVo = new RequestWorkFlowAuditVO();

			if (null != ((Object[]) object)[0]) {
				requestWorkFlowAuditVo.setRequestWorkFlowAuditId((int) ((Object[]) object)[0]);
			}
			if (null != ((Object[]) object)[1]) {
				requestWorkFlowAuditVo.setWorkFlowId((int) ((Object[]) object)[1]);				
			}
			if (null != ((Object[]) object)[2]) {
				requestWorkFlowAuditVo.setSeqId((int) ((Object[]) object)[2]);
			}
			if (null != ((Object[]) object)[3]) {
				requestWorkFlowAuditVo.setRequestId((int) ((Object[]) object)[3]);
			}
			if (null != ((Object[]) object)[4]) {
				requestWorkFlowAuditVo.setUserId((int) ((Object[]) object)[4]);
			}
			if (null != ((Object[]) object)[5]) {
				requestWorkFlowAuditVo.setGroupId((int) ((Object[]) object)[5]);
			}
			if (null != ((Object[]) object)[6]) {
				requestWorkFlowAuditVo.setSequence((int) ((Object[]) object)[6]);
			}
			if (null != ((Object[]) object)[7]) {
				requestWorkFlowAuditVo.setDescisionType((int) ((Object[]) object)[7]);
			}
			if (null != ((Object[]) object)[8]) {
				requestWorkFlowAuditVo.setApprovalExecuter((int) ((Object[]) object)[8]);
			}
			if (null != ((Object[]) object)[9]) {
				requestWorkFlowAuditVo.setReassignFlag((int) ((Object[]) object)[9]);
			}
			if (null != ((Object[]) object)[10]) {
				requestWorkFlowAuditVo.setReassignUserId((int) ((Object[]) object)[10]);
			}
			if (null != ((Object[]) object)[11]) {
				requestWorkFlowAuditVo.setRemarks((String) ((Object[]) object)[11]);
			}
			if ((int) ((Object[]) object)[12] == 1) {
				requestWorkFlowAuditVo.setRequestWorkflowAuditIsActive(true);
			} else if ((int) ((Object[]) object)[12] == 0) {
				requestWorkFlowAuditVo.setRequestWorkflowAuditIsActive(false);

			}

			//if ((int) ((Object[]) object)[7] != 9) {
				if (null != ((Object[]) object)[17] ){//&& requestWorkFlowAuditVo.getDescisionType() != 0) {
					requestWorkFlowAuditVo.setUpdatedDate((Date) ((Object[]) object)[17]);
				}
			/*} else {
				requestWorkFlowAuditVo.setUpdatedDate(null);
			}
			*/
			if (null != ((Object[]) object)[20]) {
				requestWorkFlowAuditVo.setForwardRemarks((String) ((Object[]) object)[20]);
			}
			
			
			if (null != ((Object[]) object)[21]) {
				requestWorkFlowAuditVo.setUserName((String) ((Object[]) object)[21]);
			}

			if (null != ((Object[]) object)[13]) {

				requestWorkFlowAuditVo.setMinutes((int) ((Object[]) object)[13]);
			}

			if (null != ((Object[]) object)[15]) {

				requestWorkFlowAuditVo.setCreatedDate((Date) ((Object[]) object)[15]);
			}
			if (null != requestWorkFlowAuditVo.getApprovalExecuter() &&
				requestWorkFlowAuditVo.getApprovalExecuter() != 3  
					 
					//&&requestWorkFlowAuditVo.getDescisionType() != 0
					&& requestWorkFlowAuditVo.getDescisionType() != 9) {
				if (requestWorkFlowAuditVo.getSequence() == 1) {
					actualSlaTime = DateUtil.getMinutesDifference(requestWorkFlowAuditVo.getUpdatedDate(),
							requestWorkFlowAuditVo.getCreatedDate());
					updateDate = requestWorkFlowAuditVo.getUpdatedDate();
					groupId = requestWorkFlowAuditVo.getGroupId();

				} else if (requestWorkFlowAuditVo.getSequence() > 1 && requestWorkFlowAuditVo.getGroupId() != 0) {
					if (groupId == requestWorkFlowAuditVo.getGroupId()) {

						groupId = requestWorkFlowAuditVo.getGroupId();

					} else {
						if (null == updateDate) {
							updateDate = requestWorkFlowAuditVo.getCreatedDate();
						}
						actualSlaTime = DateUtil.getMinutesDifference(requestWorkFlowAuditVo.getUpdatedDate(),
								updateDate);
						updateDate = requestWorkFlowAuditVo.getUpdatedDate();

						groupId = requestWorkFlowAuditVo.getGroupId();

					}

				} else if (requestWorkFlowAuditVo.getSequence() > 1 && requestWorkFlowAuditVo.getGroupId() == 0) {					
				 
					if (null == updateDate) {
						 
						updateDate = requestWorkFlowAuditVo.getCreatedDate();					 					
					}
					 
					actualSlaTime = DateUtil.getMinutesDifference(requestWorkFlowAuditVo.getUpdatedDate(), updateDate);
					 
					groupId = requestWorkFlowAuditVo.getGroupId();
					updateDate = requestWorkFlowAuditVo.getUpdatedDate();

				}
				if (actualSlaTime <= requestWorkFlowAuditVo.getMinutes()) {
					 					
					requestWorkFlowAuditVo.setStatus(CommonConstant.CONSTANT_ZERO);
				} else if (actualSlaTime > requestWorkFlowAuditVo.getMinutes()) {
				 				 
					requestWorkFlowAuditVo.setStatus(CommonConstant.CONSTANT_TWO);
				}
			}
			list_RequestWorkFlowAuditVo.add(requestWorkFlowAuditVo);
		}				 				
		
		} catch (Exception exe) {
			Log.info("Request Service get All Aproval  Exception",exe);
			throw new CommonException("dataFailure");
		}
		
		return list_RequestWorkFlowAuditVo;
	}

	@Transactional
	public RequestVO validateIsCancel(RequestVO requestVo,AuthDetailsVo authDetailsVo) throws CommonException {
		int id = 0;
		Iterator<Entry<Integer, String>> itr = requestVo.getRequestList().entrySet().iterator();
		try {
			
			RequestEntity requestEntity = new RequestEntity();
			while (itr.hasNext()) {
				RequestVO request = new RequestVO();
				Entry<Integer, String> entry = itr.next();

				if (DecisionTypeEnum.CAN.toString().equalsIgnoreCase(entry.getValue())
						|| DecisionTypeEnum.COM.toString().equalsIgnoreCase(entry.getValue())
						|| DecisionTypeEnum.CLO.toString().equalsIgnoreCase(entry.getValue())
						|| DecisionTypeEnum.REJ.toString().equalsIgnoreCase(entry.getValue())) {
					throw new CommonException("request_validation_requestDeletionErrorMessage");
				}

				requestEntity = requestRepository.findOne(entry.getKey());
							 
				requestDAO.updateCancelRemarks(entry.getKey(), requestVo.getRemarks(), authDetailsVo.getUserId(),authDetailsVo);
				
				id = entry.getKey();
				validateIsCancel(requestEntity,authDetailsVo);
				requestEntity.setRequestIsCancel(true);
			 
				requestRepository.save(requestEntity);
			 
				request.setRequestId(entry.getKey());
				request.setCurrentStatusCode(DecisionTypeEnum.CAN.toString());
				updateCurrentStatus(request,authDetailsVo);
				if(null != requestEntity && null != requestEntity.getRequestId()){
					requestVo.setRequestId(requestEntity.getRequestId());
				}
			}
			
			requestConfigurationService.updateCancelDecision(requestVo , authDetailsVo);	
			
			//cancel email
			EmailVo emailVo = new EmailVo();
			emailVo.setMessageCode(CommonConstant.CANCEL);
			emailVo.setGroupId(CommonConstant.CAN);
			
			emailVo.setRequestId(requestEntity.getRequestId());
			emailVo.setRequestCode(requestEntity.getRequestCode());
			emailVo.setEmailFlag(0);
			emailVo.setCreateBy(authDetailsVo.getUserId());
			emailVo.setUpdateBy(authDetailsVo.getUserId());
			emailVo.setCreateDate(CommonConstant.getCalenderDate());
			emailVo.setUpdateDate(CommonConstant.getCalenderDate());
			emailVo.setEntityId(authDetailsVo.getEntityId());
			emailVo.setEscalationFlag(CommonConstant.CONSTANT_ZERO);
			emailVo.setRequestSubject(requestEntity.getRequestSubject());																	
					
			UserMasterVO userMasterVO1 = getEmailAddress(requestEntity.getCreateBy(),authDetailsVo);
			emailVo.setToUserAddress(userMasterVO1.getEmailId());
			emailVo.setRequestorName(userMasterVO1.getUserName());
			emailVo.setUserLang(userMasterVO1.getLangCode());			
			
			emailVo.setNotificationFlag(CommonConstant.CONSTANT_ZERO);
			emailVo.setToUserId(userMasterVO1.getId());
			emailVo.setUserName(userMasterVO1.getUserName());
			
			emailVo.setSystemConfigurationVo(getSystemConfigurationDetails(authDetailsVo));
			
			RequestWorkFlowEntity requestWorkFlowEntity  = requestConfigurationDAO.findWorkflowId(requestEntity.getRequestId());
			
			if(requestWorkFlowEntity.isReqWorkFlowIsMailRequired()){
				emailVo.setTrEmailFlag(CommonConstant.CONSTANT_ONE);
			}else{
				emailVo.setTrEmailFlag(CommonConstant.CONSTANT_ZERO);
			}
			
			if (null != emailVo.getSystemConfigurationVo() && emailVo.getSystemConfigurationVo().size() > 0) {

				EntityLicenseVO entityLicenseVO = getEntityDetails(authDetailsVo.getEntityId());
				if (null != entityLicenseVO.getEmailFlag() && entityLicenseVO.getEmailFlag() == 1) {
					final String uri = emailRestTemplateUrl + FilePathConstants.TOEMAIL;
					JSONResponse response = restTemplate.postForObject(uri, emailVo, JSONResponse.class);

				}
			}
				 							
		} catch (NoResultException e) {
			Log.info("Request Service get validateIsCancel  NoResultException ",e);
			throw new CommonException(getMessage("noResultFound",authDetailsVo));
		} catch (NonUniqueResultException e) {
			Log.info("Request Service get validateIsCancel  NonUniqueResultException ",e);
			throw new CommonException(getMessage("noRecordFound",authDetailsVo));
		} catch (CommonException e) {
			Log.info("Request Service get validateIsCancel  CommonException ",e);
			throw new CommonException(getMessage("request_validation_requestDeletionErrorMessage",authDetailsVo));
		} catch (Exception e) {
			Log.info("Request Service get validateIsCancel  Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
		return requestVo;
	}

	/**
	 * This method is used to delete the RequestDetailRecord one by one by
	 * executing the query
	 * 
	 * 
	 * @param RequestEntity
	 *            requestEntity
	 * @return void
	 */
	public void validateIsCancel(RequestEntity requestEntity,AuthDetailsVo authDetailsVo) {

		List<RequestDetailEntity> listrequestDetailEntity = new ArrayList<RequestDetailEntity>();
		try {
			listrequestDetailEntity = requestDAO.findDetail(requestEntity.getRequestId(),authDetailsVo);
		} catch (Exception e) {
			Log.info("Request Service get validateIsCancel Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

		for (RequestDetailEntity requestDetailEntity : listrequestDetailEntity) {
			requestDetailEntity.setRequestDetailIsCancel(true);
			try {
				requestDetailsRepository.save(requestDetailEntity);
			} catch (Exception e) {
				Log.info("Request Service get validateIsCancel Exception",e);
				throw new CommonException(getMessage("dataFailure",authDetailsVo));
			}
		}

	}

	/**
	 * This method is used to update the current status of the Request.
	 * 
	 * 
	 * @param RequestVo
	 *            requestVo
	 * @return void
	 */
	@Transactional
	public void updateCurrentStatus(RequestVO requestVo,AuthDetailsVo authDetailsVo) {

		int currentStatusId = findCurrentStatusId(requestVo.getCurrentStatusCode(),authDetailsVo);

		try {
			requestDAO.updateCurrentStatus(requestVo.getRequestId(), currentStatusId);
		} catch (Exception e) {
			Log.info("Request Service get updateCurrentStatus Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

	}

	/**
	 * This method is used to find the status Id from the database
	 * 
	 * @param String
	 *            status
	 * @return int statusId
	 */
	@Transactional
	public int findCurrentStatusId(String status,AuthDetailsVo authDetailsVo) {

		try {

			int statusId = (int) requestDAO.findStatusId(status);
			return statusId;

		} catch (NoResultException e) {
			Log.info("Request Service get findCurrentStatusId NoResultException",e);
			throw new CommonException(getMessage("noResultFound",authDetailsVo));
		} catch (NonUniqueResultException e) {
			Log.info("Request Service get findCurrentStatusId NonUniqueResultException",e);
			throw new CommonException(getMessage("noRecordFound",authDetailsVo));
		} catch (Exception e) {
			Log.info("Request Service get findCurrentStatusId Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

	}

	@Transactional
	public RequestEntity saveAttachment(RequestEntity requestEntity, MultipartFile[] uploadingFiles,AuthDetailsVo authDetailsVo) throws CommonException{

		try {
			for (MultipartFile uploadedFile : uploadingFiles) {

				String fileName = dateAppend(uploadedFile);
				String path = picturePath.getRequestAttachmentPath();
				File fileToCreate = new File(path);
				if (fileToCreate.exists()) {
					path = path + CommonConstant.SLASH;
					if (!fileToCreate.exists()) {
						fileToCreate.mkdir();
					}
				} else {
					fileToCreate.mkdir();
					path = path + CommonConstant.SLASH;
					fileToCreate = new File(path);
					fileToCreate.mkdir();
				}
				fileToCreate = new File(path + fileName);

				uploadedFile.transferTo(fileToCreate);
				path = path + fileName;
				requestEntity.setRequestAttachment(path);

			}
		} catch (JsonParseException e) {
			Log.info("Request Service get saveAttachment JsonParseException",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		} catch (JsonMappingException e) {
			Log.info("Request Service get saveAttachment JsonMappingException",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		} catch (IOException e) {
			Log.info("Request Service get saveAttachment IOException",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		} catch (Exception e) {
			Log.info("Request Service get saveAttachment Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
		return requestEntity;
	}

	/**
	 * Method used to append Date for Uploaded File
	 * 
	 * @param uploadedFile
	 * @return
	 */
	public String dateAppend(MultipartFile uploadedFile) {

		String fileName = uploadedFile.getOriginalFilename().substring(0,
				uploadedFile.getOriginalFilename().lastIndexOf("."));

		String date = CommonConstant.formatDatetoString(new Date(), CommonConstant.FILE_NAME_FORMAT_DATE);

		fileName = fileName + date;

		String format = "." + getfileFormat(uploadedFile.getOriginalFilename());

		fileName = fileName + format;

		return fileName;

	}

	/**
	 * Method used to Get the File Format
	 * 
	 * @param attachmentFileName
	 * @return
	 */
	public static String getfileFormat(String attachmentFileName) {

		try {

			return attachmentFileName.substring(attachmentFileName.lastIndexOf(".") + 1);

		} catch (Exception e) {
			return "";
		}
	}

	public RequestVO attachmentDownload(RequestVO requestVo,AuthDetailsVo authDetailsVo) {

		RequestVO requestVoList = new RequestVO();

		RequestEntity requestEntity = new RequestEntity();

		// Get the phone book details from DB using ID
		try {

			requestEntity = requestDAO.attachmentDownload(requestVo,authDetailsVo);

		} catch (NoResultException e) {
			Log.info("Request Service get attachmentDownload NoResultException",e);
			throw new CommonException(getMessage("noResultFound",authDetailsVo));
		} catch (NonUniqueResultException e) {
			Log.info("Request Service get attachmentDownload NonUniqueResultException",e);
			throw new CommonException(getMessage("noRecordFound",authDetailsVo));
		} catch (Exception e) {
			Log.info("Request Service get attachmentDownload Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

		// Copy the data from entity to VO
		try {

			BeanUtils.copyProperties(requestEntity, requestVoList);

		} catch (Exception e) {
			Log.info("Request Service get attachmentDownload Exception",e);
			throw new CommonException(getMessage("copyFailure",authDetailsVo));
		}
		return requestVoList;

	}

	/**
	 * This method is used to create the Request user details.
	 * 
	 * 
	 * @param RequestEntity
	 *            requestEntity
	 * @return RequestEntity requestEntity
	 */

	@Transactional
	private RequestEntity setCreateUserDetails(RequestEntity requestEntity,AuthDetailsVo authDetailsVo , RequestVO requestVo) {
				
		if (null != authDetailsVo.getUserId()) {
			//requestEntity.setCreateBy(AuthUtil.getUserId());
			requestEntity.setUpdateBy(authDetailsVo.getUserId());
		} else { 
			//requestEntity.setCreateBy(requestEntity.getCreateBy());
			requestEntity.setUpdateBy(requestEntity.getCreateBy());
		}
		
		if (null != requestVo.getCreatedBy()) {
			requestEntity.setCreateBy(requestVo.getCreatedBy());
		}else if (null != requestVo.getUserId()) {
			requestEntity.setCreateBy(requestVo.getUserId());
		}else if(null != authDetailsVo.getUserId()){
			requestEntity.setCreateBy(authDetailsVo.getUserId());
		}
		
		requestEntity.setCreateDate(CommonConstant.getCalenderDate());

		requestEntity.setUpdateDate(CommonConstant.getCalenderDate());
		requestEntity.setDeleteFlag(CommonConstant.FLAG_ZERO);
		return requestEntity;
	}

	/**
	 * This method is used to set or change the Request user details which is
	 * already existed.
	 * 
	 * 
	 * @param RequestEntity
	 *            requestEntity
	 * @return RequestEntity requestEntity
	 */

	@Transactional
	private RequestEntity setUpdatedUserDetails(RequestEntity requestEntity,AuthDetailsVo authDetailsVo) {

		requestEntity.setUpdateBy(authDetailsVo.getUserId());
		requestEntity.setUpdateDate(CommonConstant.getCalenderDate());

		return requestEntity;
	}

	/**
	 * This method is used to create the RequestDetail user details.
	 * 
	 * 
	 * @param RequestDetailEntity
	 *            requestDetailEntity
	 * @return RequestDetailEntity requestDetailEntity
	 */

	@Transactional
	private RequestDetailEntity setCreateUserRequestDetails(RequestDetailEntity requestDetailEntity,AuthDetailsVo authDetailsVo,
			RequestVO requestVo) {

		if (null != requestVo.getCreatedBy()) {
			requestDetailEntity.setUpdateBy(requestVo.getCreatedBy());
		}else if (null != authDetailsVo.getUserId()) {
			//requestDetailEntity.setCreateBy(authDetailsVo.getUserId());
			requestDetailEntity.setUpdateBy(authDetailsVo.getUserId());
		} else {
			//requestDetailEntity.setCreateBy(requestDetailEntity.getCreateBy());
			requestDetailEntity.setUpdateBy(requestDetailEntity.getCreateBy());
		}
		
		if (null != requestVo.getCreatedBy()) {
			requestDetailEntity.setCreateBy(requestVo.getCreatedBy());
		}else if (null != requestVo.getUserId()) {
			requestDetailEntity.setCreateBy(requestVo.getUserId());
		}else if(null != authDetailsVo.getUserId()){
			requestDetailEntity.setCreateBy(authDetailsVo.getUserId());
		}
				
		requestDetailEntity.setDeleteFlag(CommonConstant.FLAG_ZERO);
		requestDetailEntity.setCreateDate(CommonConstant.getCalenderDate());

		requestDetailEntity.setUpdateDate(CommonConstant.getCalenderDate());

		return requestDetailEntity;
	}

	/**
	 * This method is used to set or change the RequestDetail user details which
	 * is already existed.
	 * 
	 * 
	 * @param RequestDetailEntity
	 *            requestDetailEntity
	 * @return RequestDetailEntity requestDetailEntity
	 */
	@Transactional
	private RequestDetailEntity setUpdatedUserRequestDetails(RequestDetailEntity requestDetailEntity,AuthDetailsVo authDetailsVo) {

		if (null != authDetailsVo.getUserId()) {
			requestDetailEntity.setUpdateBy(authDetailsVo.getUserId());
		} else {
			requestDetailEntity.setUpdateBy(requestDetailEntity.getCreateBy());
		}
		requestDetailEntity.setUpdateDate(CommonConstant.getCalenderDate());

		return requestDetailEntity;
	}

	/**
	 * This method is used to copy the list of approvals for the single Request.
	 * 
	 * 
	 * @param List<Object[]>
	 *            list_RequestWorkFlowAuditEntity
	 * @return List<RequestWorkFlowAuditVo> list_RequestWorkFlowAuditVo
	 */
	@Transactional
	public List<RequestWorkFlowAuditVO> getAllApproval(List<Object> list_RequestWorkFlowAuditEntity,
			RequestVO requestVo , AuthDetailsVo authDetailsVo) throws CommonException {
		List<RequestWorkFlowAuditVO> list_RequestWorkFlowAuditVo = new ArrayList<RequestWorkFlowAuditVO>();

		int workflowId = 0;
		
		for (Object object : list_RequestWorkFlowAuditEntity) {
			RequestWorkFlowAuditVO requestWorkFlowAuditVo = new RequestWorkFlowAuditVO();

			if (null != ((Object[]) object)[0]) {
				requestWorkFlowAuditVo.setRequestWorkFlowAuditId((int) ((Object[]) object)[0]);
			}
			if (null != ((Object[]) object)[1]) {
				requestWorkFlowAuditVo.setWorkFlowId((int) ((Object[]) object)[1]);
				workflowId = (int) ((Object[]) object)[1];
			}
			if (null != ((Object[]) object)[2]) {
				requestWorkFlowAuditVo.setSeqId((int) ((Object[]) object)[2]);
			}
			if (null != ((Object[]) object)[3]) {
				requestWorkFlowAuditVo.setRequestId((int) ((Object[]) object)[3]);
			}
			if (null != ((Object[]) object)[4]) {
				requestWorkFlowAuditVo.setUserId((int) ((Object[]) object)[4]);
			}
			if (null != ((Object[]) object)[5]) {
				requestWorkFlowAuditVo.setGroupId((int) ((Object[]) object)[5]);
			}
			if (null != ((Object[]) object)[6]) {
				requestWorkFlowAuditVo.setSequence((int) ((Object[]) object)[6]);
			}
			if (null != ((Object[]) object)[7]) {
				requestWorkFlowAuditVo.setDescisionType((int) ((Object[]) object)[7]);
			}
			if (null != ((Object[]) object)[8]) {
				requestWorkFlowAuditVo.setApprovalExecuter((int) ((Object[]) object)[8]);
			}
			if (null != ((Object[]) object)[9]) {
				requestWorkFlowAuditVo.setReassignFlag((int) ((Object[]) object)[9]);
			}
			if (null != ((Object[]) object)[10]) {
				requestWorkFlowAuditVo.setReassignUserId((int) ((Object[]) object)[10]);
			}
			if (null != ((Object[]) object)[11]) {
				requestWorkFlowAuditVo.setRemarks((String) ((Object[]) object)[11]);
			}
			if ((int) ((Object[]) object)[12] == 1) {
				requestWorkFlowAuditVo.setRequestWorkflowAuditIsActive(true);
			} else if ((int) ((Object[]) object)[12] == 0) {
				requestWorkFlowAuditVo.setRequestWorkflowAuditIsActive(false);
			}
			
			if (null != ((Object[]) object)[20]) {
				requestWorkFlowAuditVo.setForwardRemarks((String) ((Object[]) object)[20]);
			}
						
			if (requestWorkFlowAuditVo.getApprovalExecuter() == 2) {

				RequestWorkFlowAuditEntity requestWorkFlowAuditEntity = requestDAO
						.getResolverSlaList(requestWorkFlowAuditVo.getRequestId());
				requestWorkFlowAuditVo.setCreatedDate(requestWorkFlowAuditEntity.getUpdateDate());

			} else if (requestWorkFlowAuditVo.getApprovalExecuter() == 1) {
				
				
				if(requestWorkFlowAuditVo.getSequence()== 1){
					if (null != ((Object[]) object)[15] && requestWorkFlowAuditVo.getDescisionType() != 0) {
						requestWorkFlowAuditVo.setCreatedDate((Date) ((Object[]) object)[15]);
					}
					
				} else{
					
					if(requestWorkFlowAuditVo.getGroupId() != 0){
						
						RequestWorkFlowAuditEntity requestWorkFlowAuditEntity = requestDAO
								.getApproverSlaListForGroup(requestWorkFlowAuditVo.getRequestId());
						requestWorkFlowAuditVo.setCreatedDate(requestWorkFlowAuditEntity.getUpdateDate());
					}else{
						
						RequestWorkFlowAuditEntity requestWorkFlowAuditEntity = requestDAO
								.getApproverSlaList(requestWorkFlowAuditVo.getRequestId(), requestWorkFlowAuditVo.getSequence());
						requestWorkFlowAuditVo.setCreatedDate(requestWorkFlowAuditEntity.getUpdateDate());
					}													
				}
															
				
				/*if(requestWorkFlowAuditEntity == null ){
					
					if (null != ((Object[]) object)[15] && requestWorkFlowAuditVo.getDescisionType() != 0) {
						requestWorkFlowAuditVo.setCreatedDate((Date) ((Object[]) object)[15]);
					}
				} else if (null != requestWorkFlowAuditEntity && requestWorkFlowAuditEntity.getSequence() != 1) {
					requestWorkFlowAuditVo.setCreatedDate(requestWorkFlowAuditEntity.getUpdateDate());
				}  
*/
			}else {

				if (null != ((Object[]) object)[15] && requestWorkFlowAuditVo.getDescisionType() != 0) {
					requestWorkFlowAuditVo.setCreatedDate((Date) ((Object[]) object)[15]);
				}
			}			
			
			if (null != ((Object[]) object)[17] && requestWorkFlowAuditVo.getDescisionType() != 0) {
				requestWorkFlowAuditVo.setUpdatedDate((Date) ((Object[]) object)[17]);
			}

			if (null != ((Object[]) object)[21]) {
				requestWorkFlowAuditVo.setUserName((String) ((Object[]) object)[21]);
			}
			if (0 != requestWorkFlowAuditVo.getDescisionType() && null != ((Object[]) object)[13]
					&& (int) ((Object[]) object)[13] != 0) {
				
				if(null != requestWorkFlowAuditVo.getUpdatedDate() && null != requestWorkFlowAuditVo.getCreatedDate() ){
					
					int actualSlaTime = DateUtil.getMinutesDifference(requestWorkFlowAuditVo.getUpdatedDate(),
							requestWorkFlowAuditVo.getCreatedDate());

					requestWorkFlowAuditVo.setMinutes((int) ((Object[]) object)[13]);

					if (actualSlaTime <= requestWorkFlowAuditVo.getMinutes()) {
						requestWorkFlowAuditVo.setStatus(CommonConstant.CONSTANT_ZERO);
					} else if (actualSlaTime > requestWorkFlowAuditVo.getMinutes()) {
						requestWorkFlowAuditVo.setStatus(CommonConstant.CONSTANT_TWO);
					}
				}							
			}

			/*if (null != ((Object[]) object)[23]) {
				requestWorkFlowAuditVo.setRemarks((String) ((Object[]) object)[23]);
			}*/
			
			list_RequestWorkFlowAuditVo.add(requestWorkFlowAuditVo);
		}

		RequestWorkFlowEntity requestWorkFlowEntity = requestDAO.getReassignFlag(workflowId);	
		
		if(requestWorkFlowEntity.isReqWorkFlowReassign()){
			requestVo.setReassignFlag(CommonConstant.CONSTANT_ONE);
		}else{
			requestVo.setReassignFlag(CommonConstant.CONSTANT_ZERO);
		}
				
		return list_RequestWorkFlowAuditVo;
	}

	/**
	 * This method is used to copy the list of approvals for the single Request.
	 * 
	 * 
	 * @param List<Object[]>
	 *            list_RequestWorkFlowAuditEntity
	 * @return List<RequestWorkFlowAuditVo> list_RequestWorkFlowAuditVo
	 */
	@Transactional
	public List<RequestWorkFlowAuditVO> getAllApprovalWithMail(List<Object> list_RequestWorkFlowAuditEntity,
			RequestVO requestVo,AuthDetailsVo authDetailsVo) throws CommonException , Exception{
		List<RequestWorkFlowAuditVO> list_RequestWorkFlowAuditVo = new ArrayList<RequestWorkFlowAuditVO>();
		JSONResponse response = new JSONResponse();
		
		int workflowId = 0;
		try{
		for (Object object : list_RequestWorkFlowAuditEntity) {
			RequestWorkFlowAuditVO requestWorkFlowAuditVo = new RequestWorkFlowAuditVO();

			if (null != ((Object[]) object)[0]) {
				requestWorkFlowAuditVo.setRequestWorkFlowAuditId((int) ((Object[]) object)[0]);
			}
			if (null != ((Object[]) object)[1]) {
				requestWorkFlowAuditVo.setWorkFlowId((int) ((Object[]) object)[1]);
				workflowId = requestWorkFlowAuditVo.getWorkFlowId();
			}
			if (null != ((Object[]) object)[2]) {
				requestWorkFlowAuditVo.setSeqId((int) ((Object[]) object)[2]);
			}
			if (null != ((Object[]) object)[3]) {
				requestWorkFlowAuditVo.setRequestId((int) ((Object[]) object)[3]);
			}
			if (null != ((Object[]) object)[4]) {
				requestWorkFlowAuditVo.setUserId((int) ((Object[]) object)[4]);
			}
			if (null != ((Object[]) object)[5]) {
				requestWorkFlowAuditVo.setGroupId((int) ((Object[]) object)[5]);
			}
			if (null != ((Object[]) object)[6]) {
				requestWorkFlowAuditVo.setSequence((int) ((Object[]) object)[6]);
			}
			if (null != ((Object[]) object)[7]) {
				requestWorkFlowAuditVo.setDescisionType((int) ((Object[]) object)[7]);
			}
			if (null != ((Object[]) object)[8]) {
				requestWorkFlowAuditVo.setApprovalExecuter((int) ((Object[]) object)[8]);
			}
			if (null != ((Object[]) object)[9]) {
				requestWorkFlowAuditVo.setReassignFlag((int) ((Object[]) object)[9]);
			}
			if (null != ((Object[]) object)[10]) {
				requestWorkFlowAuditVo.setReassignUserId((int) ((Object[]) object)[10]);
			}
			if (null != ((Object[]) object)[11]) {
				requestWorkFlowAuditVo.setRemarks((String) ((Object[]) object)[11]);
			}
			if ((int) ((Object[]) object)[12] == 1) {
				requestWorkFlowAuditVo.setRequestWorkflowAuditIsActive(true);
			} else if ((int) ((Object[]) object)[12] == 0) {
				requestWorkFlowAuditVo.setRequestWorkflowAuditIsActive(false);

			}

			if (null != ((Object[]) object)[17] && requestWorkFlowAuditVo.getDescisionType() != 0) {
				requestWorkFlowAuditVo.setUpdatedDate((Date) ((Object[]) object)[16]);
			}

			if (null != ((Object[]) object)[21]) {
				requestWorkFlowAuditVo.setUserName((String) ((Object[]) object)[21]);
			}
			if (0 != requestWorkFlowAuditVo.getDescisionType() && null != ((Object[]) object)[13]
					&& (int) ((Object[]) object)[13] != 0) {
				int actualSlaTime = DateUtil.getMinutesDifference(requestWorkFlowAuditVo.getUpdatedDate(),
						requestWorkFlowAuditVo.getCreatedDate());

				requestWorkFlowAuditVo.setMinutes((int) ((Object[]) object)[13]);

				if (actualSlaTime <= requestWorkFlowAuditVo.getMinutes()) {
					requestWorkFlowAuditVo.setStatus(CommonConstant.CONSTANT_ONE);
				} else if (actualSlaTime > requestWorkFlowAuditVo.getMinutes()) {
					requestWorkFlowAuditVo.setStatus(CommonConstant.CONSTANT_TWO);
				}
			}

		 
				if (0 != requestVo.getRequestSubId()) {
					if (mailMessages.getRequestUpdate() == CommonConstant.CONSTANT_ONE) {
				 
						EmailVo emailVo = new EmailVo();
						
						emailVo.setMessageCode(CommonConstant.REQUEST_UPDATE);
						emailVo.setGroupId(CommonConstant.RU);
						emailVo.setToUserId(requestWorkFlowAuditVo.getUserId());
						emailVo.setRequestId(requestWorkFlowAuditVo.getRequestId());
						 
						emailVo.setEmailFlag(0);
						emailVo.setCreateBy(authDetailsVo.getUserId());
						emailVo.setUpdateBy(authDetailsVo.getUserId());
						emailVo.setCreateDate(CommonConstant.getCalenderDate());
						emailVo.setUpdateDate(CommonConstant.getCalenderDate());
						emailVo.setEntityId(authDetailsVo.getEntityId());
						emailVo.setEscalationFlag(CommonConstant.CONSTANT_ZERO);
						
						UserMasterVO userMasterVo = getEmailAddress(requestWorkFlowAuditVo.getUserId(),authDetailsVo);
						emailVo.setToUserAddress(userMasterVo.getEmailId());
						emailVo.setUserName(userMasterVo.getUserName());
						emailVo.setUserLang(userMasterVo.getLangCode());	
												
						emailVo.setNotificationFlag(CommonConstant.CONSTANT_ZERO);
						emailVo.setToUserId(userMasterVo.getId());
						emailVo.setUserName(userMasterVo.getUserName());
						
						emailVo.setRequestCode(requestVo.getRequestCode());							 
						emailVo.setRequestSubject(requestVo.getRequestSubject());
						emailVo.setSystemConfigurationVo(getSystemConfigurationDetails(authDetailsVo));
						
						EntityLicenseVO  entityLicenseVO = getEntityDetails(authDetailsVo.getEntityId());
						
						RequestWorkFlowEntity requestWorkFlow  = requestConfigurationDAO.getWorkflow(workflowId);
						
						if(requestWorkFlow.isReqWorkFlowIsMailRequired()){
							emailVo.setTrEmailFlag(CommonConstant.CONSTANT_ONE);
						}else{
							emailVo.setTrEmailFlag(CommonConstant.CONSTANT_ZERO);
						}
																				
						if(requestWorkFlow.isReqWorkFlowIsNotificationRequired()){
							emailVo.setNotificationFlag(CommonConstant.CONSTANT_ZERO);								 
						}else{
							emailVo.setNotificationFlag(CommonConstant.CONSTANT_ONE);
						}																				
									
						if (null != emailVo.getSystemConfigurationVo() && emailVo.getSystemConfigurationVo().size() > 0) {
							if (null != entityLicenseVO.getEmailFlag() && entityLicenseVO.getEmailFlag() == 1) {
								final String uri = emailRestTemplateUrl + FilePathConstants.TOEMAIL;
								response = restTemplate.postForObject(uri, emailVo, JSONResponse.class);
							}

						}
					}
				} else {
					if (requestWorkFlowAuditVo.getApprovalExecuter() == CommonConstant.CONSTANT_ONE) {

						if (mailMessages.getApproval() == CommonConstant.CONSTANT_ONE) {
					 											
							EmailVo emailVo = new EmailVo();
							
							emailVo.setMessageCode(CommonConstant.APPROVAL);
							emailVo.setGroupId(CommonConstant.APL);
							emailVo.setToUserId(requestWorkFlowAuditVo.getUserId());
							emailVo.setRequestId(requestWorkFlowAuditVo.getRequestId());
							emailVo.setRequestCode(requestWorkFlowAuditVo.getRequestCode());
							emailVo.setEmailFlag(0);
							emailVo.setCreateBy(authDetailsVo.getUserId());
							emailVo.setUpdateBy(authDetailsVo.getUserId());
							emailVo.setCreateDate(CommonConstant.getCalenderDate());
							emailVo.setUpdateDate(CommonConstant.getCalenderDate());
							emailVo.setEntityId(authDetailsVo.getEntityId());
							emailVo.setEscalationFlag(CommonConstant.CONSTANT_ZERO);
							
							UserMasterVO userMasterVo = getEmailAddress(requestWorkFlowAuditVo.getUserId(),authDetailsVo);
							emailVo.setToUserAddress(userMasterVo.getEmailId());
							emailVo.setApproverName(userMasterVo.getUserName());
							emailVo.setUserLang(userMasterVo.getLangCode());
							
							emailVo.setToUserId(userMasterVo.getId());
							emailVo.setUserName(userMasterVo.getUserName());
							emailVo.setUserLang(userMasterVo.getLangCode());	
							
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
						 													
							 List<Object[]>  objList = 	requestDAO.getRequestScreenDetails(requestWorkFlowAuditVo.getRequestId(),authDetailsVo);
								
							 List<RequestDetailVO> requestDetailVOList = new ArrayList<RequestDetailVO>();
							 
							for (Object[] obj : objList) {

								RequestDetailVO requestDetailVO = new RequestDetailVO();

								if (null != (String) ((Object[]) obj)[1]) {
									requestDetailVO.setRequestScreenDetailConfigurationFieldName((String) obj[1]);
								}

								if (null != (String) ((Object[]) obj)[2] && !((String) ((Object[]) obj)[2]).isEmpty()) {
									String val = (String) ((Object[]) obj)[2];
									if(val.charAt(0) == ','){
									requestDetailVO.setRequestScreenDetailConfigurationFieldValue(val.substring(1));
									}else{
										requestDetailVO.setRequestScreenDetailConfigurationFieldValue(val);
									}
								}else{
									requestDetailVO.setRequestScreenDetailConfigurationFieldValue("-");
								}

								requestDetailVOList.add(requestDetailVO);
							}
							emailVo.setSystemConfigurationVo(getSystemConfigurationDetails(authDetailsVo));	
							
						 
							emailVo.setRequestDetailList(requestDetailVOList);													
							
							EntityLicenseVO  entityLicenseVO = getEntityDetails(authDetailsVo.getEntityId());
							
							RequestWorkFlowEntity requestWorkFlow  = requestConfigurationDAO.getWorkflow(workflowId);
							
							if(requestWorkFlow.isReqWorkFlowIsMailRequired()){
								emailVo.setTrEmailFlag(CommonConstant.CONSTANT_ONE);
							}else{
								emailVo.setTrEmailFlag(CommonConstant.CONSTANT_ZERO);
							}
																					
							if(requestWorkFlow.isReqWorkFlowIsNotificationRequired()){
								emailVo.setNotificationFlag(CommonConstant.CONSTANT_ZERO);								 
							}else{
								emailVo.setNotificationFlag(CommonConstant.CONSTANT_ONE);
							}																											
							
							if (null != emailVo.getSystemConfigurationVo() && emailVo.getSystemConfigurationVo().size() > 0) {
								if (null != entityLicenseVO.getEmailFlag() && entityLicenseVO.getEmailFlag() == 1) {
									final String uri = emailRestTemplateUrl + FilePathConstants.TOEMAIL;
									response = restTemplate.postForObject(uri, emailVo, JSONResponse.class);
								}
							}
						}
					} else if (requestWorkFlowAuditVo.getApprovalExecuter() == CommonConstant.CONSTANT_TWO) {

						if (mailMessages.getResolver() == CommonConstant.CONSTANT_ONE) {

							EmailVo emailVo = new EmailVo();
							
							emailVo.setMessageCode(CommonConstant.RESOLVER);
							emailVo.setGroupId(CommonConstant.RSL);
							emailVo.setToUserId(requestWorkFlowAuditVo.getUserId());
							emailVo.setRequestId(requestWorkFlowAuditVo.getRequestId());
							emailVo.setRequestCode(requestWorkFlowAuditVo.getRequestCode());
							emailVo.setEmailFlag(0);
							emailVo.setCreateBy(authDetailsVo.getUserId());
							emailVo.setUpdateBy(authDetailsVo.getUserId());
							emailVo.setCreateDate(CommonConstant.getCalenderDate());
							emailVo.setUpdateDate(CommonConstant.getCalenderDate());
							emailVo.setEntityId(authDetailsVo.getEntityId());
							emailVo.setEscalationFlag(CommonConstant.CONSTANT_ZERO);
					
							UserMasterVO userMasterVo = getEmailAddress(requestWorkFlowAuditVo.getUserId(),authDetailsVo);
							emailVo.setToUserAddress(userMasterVo.getEmailId());
							emailVo.setExecutorName(userMasterVo.getUserName());
							
							emailVo.setToUserId(userMasterVo.getId());
							emailVo.setUserName(userMasterVo.getUserName());
							emailVo.setUserLang(userMasterVo.getLangCode());
							
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
							
							 List<Object[]>  objList = 	requestDAO.getRequestScreenDetails(requestWorkFlowAuditVo.getRequestId(),authDetailsVo);
								
							 List<RequestDetailVO> requestDetailVOList = new ArrayList<RequestDetailVO>();

							for (Object[] obj : objList) {

								RequestDetailVO requestDetailVO = new RequestDetailVO();

								if (null != (String) ((Object[]) obj)[1]) {
									requestDetailVO.setRequestScreenDetailConfigurationFieldName((String) obj[1]);
								}

								if (null != (String) ((Object[]) obj)[2] && !((String) ((Object[]) obj)[2]).isEmpty()) {
									String val = (String) ((Object[]) obj)[2];
									if(val.charAt(0) == ','){
									requestDetailVO.setRequestScreenDetailConfigurationFieldValue(val.substring(1));
									}else{
										requestDetailVO.setRequestScreenDetailConfigurationFieldValue(val);
									}
								}else{
									requestDetailVO.setRequestScreenDetailConfigurationFieldValue("-");
								}

								requestDetailVOList.add(requestDetailVO);
							}
							emailVo.setSystemConfigurationVo(getSystemConfigurationDetails(authDetailsVo));	
													 
							emailVo.setRequestDetailList(requestDetailVOList);													
														
							EntityLicenseVO  entityLicenseVO = getEntityDetails(authDetailsVo.getEntityId());
							
							RequestWorkFlowEntity requestWorkFlow  = requestConfigurationDAO.getWorkflow(workflowId);
							
							if(requestWorkFlow.isReqWorkFlowIsMailRequired()){
								emailVo.setTrEmailFlag(CommonConstant.CONSTANT_ONE);
							}else{
								emailVo.setTrEmailFlag(CommonConstant.CONSTANT_ZERO);
							}
																					
							if(requestWorkFlow.isReqWorkFlowIsNotificationRequired()){
								emailVo.setNotificationFlag(CommonConstant.CONSTANT_ZERO);								 
							}else{
								emailVo.setNotificationFlag(CommonConstant.CONSTANT_ONE);
							}																				

							if (null != emailVo.getSystemConfigurationVo() && emailVo.getSystemConfigurationVo().size() > 0) {
								if (null != entityLicenseVO.getEmailFlag() && entityLicenseVO.getEmailFlag() == 1) {
									final String uri = emailRestTemplateUrl + FilePathConstants.TOEMAIL;
									response = restTemplate.postForObject(uri, emailVo, JSONResponse.class);
								}
							}	
						}

					} else {

						if (mailMessages.getRequest() == CommonConstant.CONSTANT_ONE) {					 											
							
							EmailVo emailVo = new EmailVo();
							emailVo.setMessageCode(CommonConstant.REQUEST);
							emailVo.setGroupId(CommonConstant.REQ);
							emailVo.setToUserId(requestWorkFlowAuditVo.getUserId());
							emailVo.setRequestId(requestWorkFlowAuditVo.getRequestId());
							emailVo.setRequestCode(requestVo.getRequestCode());						 
							emailVo.setRequestSubject(requestVo.getRequestSubject());		
							emailVo.setEmailFlag(0);
							emailVo.setCreateBy(authDetailsVo.getUserId());
							emailVo.setUpdateBy(authDetailsVo.getUserId());
							emailVo.setCreateDate(CommonConstant.getCalenderDate());
							emailVo.setUpdateDate(CommonConstant.getCalenderDate());
							emailVo.setEntityId(authDetailsVo.getEntityId());
							emailVo.setEscalationFlag(CommonConstant.CONSTANT_ZERO);
							
							UserMasterVO userMasterVo = getEmailAddress(requestWorkFlowAuditVo.getUserId(),authDetailsVo);
							emailVo.setToUserAddress(userMasterVo.getEmailId());
							emailVo.setRequestorName(userMasterVo.getUserName());
							emailVo.setUserLang(userMasterVo.getLangCode());				
							
							emailVo.setToUserId(userMasterVo.getId());
							emailVo.setUserName(userMasterVo.getUserName());
							
							emailVo.setSystemConfigurationVo(getSystemConfigurationDetails(authDetailsVo));	
							
							EntityLicenseVO  entityLicenseVO = getEntityDetails(authDetailsVo.getEntityId());
														
							RequestWorkFlowEntity requestWorkFlow  = requestConfigurationDAO.getWorkflow(workflowId);
							
							if(requestWorkFlow.isReqWorkFlowIsMailRequired()){
								emailVo.setTrEmailFlag(CommonConstant.CONSTANT_ONE);
							}else{
								emailVo.setTrEmailFlag(CommonConstant.CONSTANT_ZERO);
							}
																					
							if(requestWorkFlow.isReqWorkFlowIsNotificationRequired()){
								emailVo.setNotificationFlag(CommonConstant.CONSTANT_ZERO);								 
							}else{
								emailVo.setNotificationFlag(CommonConstant.CONSTANT_ONE);
							}																				
							
							if (null != emailVo.getSystemConfigurationVo() && emailVo.getSystemConfigurationVo().size() > 0) {
								if (null != entityLicenseVO.getEmailFlag() && entityLicenseVO.getEmailFlag() == 1) {
									final String uri = emailRestTemplateUrl + FilePathConstants.TOEMAIL;
									response = restTemplate.postForObject(uri, emailVo, JSONResponse.class);
								}
							}
						}
					}
				}

			list_RequestWorkFlowAuditVo.add(requestWorkFlowAuditVo);
		}
		} catch (Exception exe) {

			throw new CommonException("mailProblem");
		}
		return list_RequestWorkFlowAuditVo;
	}

	@Transactional
	public void findWorkFlowStatus(RequestVO requestVo,AuthDetailsVo authDetailsVo) {

		List<RequestWorkFlowEntity> requestWorkFlowEntityList = new ArrayList<RequestWorkFlowEntity>();
		RequestEntity requestEntity = new RequestEntity();
		BeanUtils.copyProperties(requestVo, requestEntity);
		RequestWorkFlowVO requestWorkFlowVo = new RequestWorkFlowVO();
		try {
			requestWorkFlowEntityList = requestDAO.findbyRequest(requestEntity);

		} catch (Exception exe) {

			throw new CommonException(getMessage("requestWorkflowNotAvailable",authDetailsVo));
		}
		if (requestWorkFlowEntityList.size() == 0 || requestWorkFlowEntityList == null) {
			throw new CommonException(getMessage("requestWorkflowNotAvailable",authDetailsVo));

		} else if (requestWorkFlowEntityList.size() > 1) {
			throw new CommonException(getMessage("request_validation_moreThanOneWorkflow",authDetailsVo));

		}
		BeanUtils.copyProperties(requestWorkFlowEntityList.get(0), requestWorkFlowVo);

	}

	@Transactional
	public RequestVO findUser(AuthDetailsVo authDetailsVo) throws CommonException {

		List<RequestTypeEntity> objectList = new ArrayList<RequestTypeEntity>();
		List<RequestTypeVO> requestTypeList = new ArrayList<RequestTypeVO>();
		Object[] userEntity = null;
		RequestVO requestVo = new RequestVO();
		int userId = authDetailsVo.getUserId();

		try {
			userEntity = requestDAO.findOne(userId);
		} catch (NoResultException e) {
			Log.info("Request Service get findUser NoResultException",e);
			throw new CommonException(getMessage("noResultFound",authDetailsVo));
		} catch (NonUniqueResultException e) {
			Log.info("Request Service get findUser NonUniqueResultException",e);
			throw new CommonException(getMessage("noRecordFound",authDetailsVo));
		} catch (Exception e) {
			Log.info("Request Service get findUser Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
		if (null != ((Object[]) userEntity)[0]) {
			requestVo.setRequestMobileNo((String) ((Object[]) userEntity)[0]);
		}
		if (null != ((Object[]) userEntity)[1]) {
			requestVo.setId((Integer) ((Object[]) userEntity)[1]);
		}
		if (null != ((Object[]) userEntity)[2]) {
			requestVo.setLocationName((String) ((Object[]) userEntity)[2]);
		}
		if (null != ((Object[]) userEntity)[5]) {
			requestVo.setDepartmentId((Integer) ((Object[]) userEntity)[5]);
		}
		if (null != ((Object[]) userEntity)[6]) {
			requestVo.setUserDepartmentName((String) ((Object[]) userEntity)[6]);
		}
		if (null != ((Object[]) userEntity)[3]) {
			requestVo.setSublocationId((Integer) ((Object[]) userEntity)[3]);
		}
		if (null != ((Object[]) userEntity)[4]) {
			requestVo.setSublocationName((String) ((Object[]) userEntity)[4]);
		}
		try {
			objectList = requestDAO.findrequestTypeList(requestVo,authDetailsVo);
		} catch (Exception e) {
			Log.info("Request Service get findUser Exception",e);
			throw new CommonException(getMessage("noResultFound",authDetailsVo));
		}

		if (null != objectList && objectList.size() > 0) {
			requestTypeList = getAllRequestTypeList(objectList);
		} else if (objectList.size() == 0) {
			throw new CommonException(getMessage("requestTypeNotAvailableForThisRequest",authDetailsVo));

		}
		requestVo.setRequestTypeVoList(requestTypeList);
		return requestVo;
	}

	public List<RequestTypeVO> getAllRequestTypeList(List<RequestTypeEntity> objectList) throws CommonException {
		List<RequestTypeVO> requestTypeList = new ArrayList<RequestTypeVO>();
		for (RequestTypeEntity requestTypeEntity : objectList) {
			RequestTypeVO requestTypeVo = new RequestTypeVO();
			BeanUtils.copyProperties(requestTypeEntity, requestTypeVo);

			requestTypeList.add(requestTypeVo);

		}
		return requestTypeList;
	}

	@Transactional
	public List<RequestScreenDetailConfigurationVO> getAllScreenDetail(RequestVO requestVo,AuthDetailsVo authDetailsVo) {

		List<RequestScreenDetailConfigurationVO> list_RequestScreenDetailConfigurationVo = new ArrayList<>();
		List<RequestScreenDetailConfigurationEntity> list_RequestScreenDetailConfigurationEntity = new ArrayList<>();
		try {

			list_RequestScreenDetailConfigurationEntity = requestDAO.getAllScreenDetail(requestVo.getRequestTypeId(),
					requestVo.getRequestSubtypeId(),authDetailsVo);

		} catch (Exception e) {
			Log.info("Request Service getAllScreenDetail Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
		if (null != list_RequestScreenDetailConfigurationEntity
				&& list_RequestScreenDetailConfigurationEntity.size() > 0) {
			list_RequestScreenDetailConfigurationVo = getAllScreenDetailList(
					list_RequestScreenDetailConfigurationEntity);
		} else {
			throw new CommonException(getMessage("detailsNotAvailableForThisRequest",authDetailsVo));
		}

		return list_RequestScreenDetailConfigurationVo;
	}

	@Transactional
	public int saveValidation(RequestVO requestVo,AuthDetailsVo authDetailsVo) {

		int count = requestDAO.saveValidation(requestVo,authDetailsVo);

		return count;

	}

	/**
	 * This method is used to copy the RequestScreenDetailConfigurationEntity
	 * values to the RequestScreenDetailConfigurationVo one by one by using
	 * list_RequestScreenDetailConfigurationEntity
	 * 
	 * 
	 * @param List<RequestScreenDetailConfigurationEntity>
	 *            list_RequestScreenDetailConfigurationEntity
	 * @return List<RequestScreenDetailConfigurationVo>
	 *         requestScreenDetailConfigurationVoList
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public List<RequestScreenDetailConfigurationVO> getAllScreenDetailList(
			List<RequestScreenDetailConfigurationEntity> list_RequestScreenDetailConfigurationEntity) {
		List<RequestScreenDetailConfigurationVO> requestScreenDetailConfigurationVoList = new ArrayList<>();

		for (RequestScreenDetailConfigurationEntity requestScreenDetailConfigurationEntity : list_RequestScreenDetailConfigurationEntity) {

			RequestScreenDetailConfigurationVO requestScreenDetailConfigurationVo = new RequestScreenDetailConfigurationVO();
			BeanUtils.copyProperties(requestScreenDetailConfigurationEntity, requestScreenDetailConfigurationVo);

			if (ButtonTypeEnum.S.toString()
					.equalsIgnoreCase(requestScreenDetailConfigurationVo.getRequestScreenDetailConfigurationFieldType())
					|| ButtonTypeEnum.R.toString().equalsIgnoreCase(
							requestScreenDetailConfigurationVo.getRequestScreenDetailConfigurationFieldType())
					|| ButtonTypeEnum.C.toString().equalsIgnoreCase(
							requestScreenDetailConfigurationVo.getRequestScreenDetailConfigurationFieldType())) {

				StringTokenizer st3 = new StringTokenizer(
						requestScreenDetailConfigurationVo.getRequestScreenDetailConfigurationFieldValue(), ",");

				List<String> list_value = new ArrayList<>();
				while (st3.hasMoreElements()) {
					list_value.add(st3.nextToken());

				}
				requestScreenDetailConfigurationVo.setRequestScreenDetailConfigurationFieldValue("");
				requestScreenDetailConfigurationVo.setList_value(list_value);

			}
			requestScreenDetailConfigurationVoList.add(requestScreenDetailConfigurationVo);

		}
		return requestScreenDetailConfigurationVoList;
	}
	
	public RequestTypeVO getRequestType(RequestVO requestVo , AuthDetailsVo authDetailsVo)throws CommonException {
		RequestTypeVO requestTypeVO = new RequestTypeVO();
		requestTypeVO.setRequestTypeId(requestVo.getRequestTypeId());
		
		requestTypeVO = requestTypeService.view(requestTypeVO,authDetailsVo);
		return requestTypeVO;
	}	
	
	public RequestSubTypeVO getSubRequestType(RequestVO requestVo , AuthDetailsVo authDetailsVo)throws CommonException {
		RequestSubTypeVO requestSubTypeVO = new RequestSubTypeVO();
		requestSubTypeVO.setRequestSubTypeId(requestVo.getRequestSubtypeId());
		
		requestSubTypeVO = requestSubTypeService.view(requestSubTypeVO,authDetailsVo);
		return requestSubTypeVO;
	}
	
	public RequestVO getUserLocationSubLocation(Integer userId, RequestVO requestVo, AuthDetailsVo authDetailsVo)
			throws CommonException {

		Object[] userEntity = null;
		try {
			userEntity = requestDAO.findOne(userId);

			if (null != ((Object[]) userEntity)[1]) {
				requestVo.setReqLocationId((Integer) ((Object[]) userEntity)[1]);
			}
			if (null != ((Object[]) userEntity)[3]) {
				requestVo.setReqSublocationId((Integer) ((Object[]) userEntity)[3]);
			}

		} catch (NoResultException e) {
			Log.info("Request Service get findUser NoResultException", e);
			throw new CommonException(getMessage("noResultFound", authDetailsVo));
		} catch (NonUniqueResultException e) {
			Log.info("Request Service get findUser NonUniqueResultException", e);
			throw new CommonException(getMessage("noRecordFound", authDetailsVo));
		} catch (Exception e) {
			Log.info("Request Service get findUser Exception", e);
			throw new CommonException(getMessage("dataFailure", authDetailsVo));
		}

		return requestVo;
	}
	
}
