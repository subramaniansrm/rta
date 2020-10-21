package com.srm.rta.service;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.constants.ButtonTypeEnum;
import com.srm.coreframework.controller.CommonController;
import com.srm.coreframework.entity.UserMappingEntity;
import com.srm.coreframework.repository.UserMappingRepository;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.util.DateUtil;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.rta.config.RTAPicturePath;
import com.srm.rta.dao.RequestConfigurationDAO;
import com.srm.rta.dao.RequestDAO;
import com.srm.rta.dao.RequestSummaryDAO;
import com.srm.rta.entity.CurrentStatusEntity;
import com.srm.rta.entity.RequestWorkFlowAuditEntity;
import com.srm.rta.repository.CurrentStatusRepository;
import com.srm.rta.vo.RequestDetailVO;
import com.srm.rta.vo.RequestSubTypeVO;
import com.srm.rta.vo.RequestSummaryVO;
import com.srm.rta.vo.RequestVO;
import com.srm.rta.vo.RequestWorkFlowAuditVO;

@Service
public class RequestSummaryService extends CommonController<RequestVO> {

	Logger logger = LoggerFactory.getLogger(RequestSummaryService.class);

	@Autowired
	RequestSummaryDAO requestSummaryDAO;

	@Autowired
	RequestService requestService;

	@Autowired
	RequestSubTypeService requestSubTypeService;

	@Autowired
	RequestResolverService requestResolverService;

	@Autowired
	UserMappingRepository userMappingRepository;

	@Autowired
	RequestDAO requestDAO;

	@Autowired
	RequestConfigurationDAO requestConfigurationDAO;

	@Autowired
	RTAPicturePath picturePath;

	@Autowired
	CurrentStatusRepository currentStatusRepository;

	@Autowired
	ApprovalService approvalService;

	@Transactional
	public List<RequestVO> getReqList(RequestVO requestVo, AuthDetailsVo authDetailsVo) {

		requestVo.setCreatedBy(authDetailsVo.getUserId());

		List<Object[]> requestList = null;
		List<RequestVO> requestVoList = new ArrayList<RequestVO>();
		// Get all the my request details
		try {

			requestList = requestDAO.getAll(requestVo, authDetailsVo);

			// Set all the fields of my request
			requestVoList = getAllList(requestList, authDetailsVo);

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dbFailure", authDetailsVo));
		}

		return requestVoList;
	}

	@Transactional
	private List<RequestVO> getOtherDetailsList(List<Object[]> requestEntityList,AuthDetailsVo authDetailsVo) throws Exception {

		/*String query = " SELECT request.idrin_tr_request_id , request.rin_tr_request_code, request.current_status_id," //2
				+ " curstatus.rin_ma_current_status_code ,curstatus.rin_ma_current_status_name,"//4
				+ " reqtype.idrin_ma_request_type_id , reqtype.rin_ma_request_type_name , subtype.idrin_ma_request_subtype_id ,"//7
				+ " subtype.rin_ma_request_subtype_name , us.USER_ID , CONCAT(us.FIRST_NAME,' ',us.LAST_NAME), request.create_date ,"//11
				+ " audit.idrin_tr_req_workflow_audit_id , audit.rin_ma_req_workflow_id , audit.rin_tr_req_workflow_seq_id ,"//14
				+ " audit.rin_tr_req_workflow_audit_group_id , audit.rin_tr_req_workflow_audit_sequence , "//16
				+ " audit.rin_tr_req_workflow_audit_descision_type,"//17
				+ " audit.rin_tr_req_workflow_audit_approval_executer , audit.rin_tr_req_workflow_audit_remarks,"//19
				+ " request.rin_tr_request_priority,request.rin_tr_request_subject" //21
*/				
		//Changed for other Details
		List<RequestVO> requestVoList = new ArrayList<RequestVO>();
		for (Object[] request : requestEntityList) {

			RequestVO requestVo = new RequestVO();
			if (null != (Integer) request[0]) {
				requestVo.setRequestId((Integer) request[0]);
			}
			if (null != (String) request[1]) {
				requestVo.setRequestCode((String) request[1]);
			}
			if (null != (String) request[4]) {
				requestVo.setCurrentStatusName((String) request[4]);
			}
			if (null != (Integer) request[2]) {
				requestVo.setCurrentStatusId((Integer) request[2]);
			}
			
			if (null != (Integer) request[5]) {
				requestVo.setRequestTypeId((Integer) request[5]);
			}
			/*if (null != (String) request[6]) {
				requestVo.setRequestTypeName((String) request[6]);
			}*/
			if (null != (Integer) request[7]) {
				requestVo.setRequestSubtypeId((Integer) request[7]);
			}
			
			
			if (null != (String) request[8]) {
				requestVo.setRequestSubTypeName((String) request[8]);
			}
			if (null != (String) request[10]) {
				requestVo.setUserName((String) request[10]);
			}
			if (null != (Date) request[11]) {
				requestVo.setRequestDate((Date) request[11]);
			}

			requestVo.setUserId(authDetailsVo.getUserId());

			if (null != (Integer) request[20]) {
				requestVo.setRequestPriority((Integer) request[20]);
			}
			if (null != (String) request[21]) {
				requestVo.setRequestSubject((String) request[21]);
			}

			requestVoList.add(requestVo);
		}

		return requestVoList;
	}
	/**
	 * Method is used for Get all the Summary List
	 * 
	 * @param requestEntityList
	 * @param listRequestVo
	 * @return requestVoList
	 */
	@Transactional
	private List<RequestVO> getAllList(List<Object[]> requestEntityList,AuthDetailsVo authDetailsVo) {

		//Changed for 
		List<RequestVO> requestVoList = new ArrayList<RequestVO>();
		for (Object[] request : requestEntityList) {

			RequestVO requestVo = new RequestVO();
			if (null != (Integer) request[0]) {
				requestVo.setRequestId((Integer) request[0]);
			}
			if (null != (String) request[1]) {
				requestVo.setRequestCode((String) request[1]);
			}
			if (null != (String) request[8]) {
				requestVo.setCurrentStatusName((String) request[8]);
			}
			if (null != (Integer) request[14]) {
				requestVo.setCurrentStatusId((Integer) request[14]);
			}
			
			if (null != (Integer) request[9]) {
				requestVo.setRequestTypeId((Integer) request[9]);
			}
			if (null != (String) request[3]) {
				requestVo.setRequestTypeName((String) request[3]);
			}
			if (null != (Integer) request[10]) {
				requestVo.setRequestSubtypeId((Integer) request[10]);
			}
			
			
			if (null != (String) request[4]) {
				requestVo.setRequestSubTypeName((String) request[4]);
			}
			if (null != (String) request[16]) {
				requestVo.setUserName((String) request[16]);
			}
			if (null != (Date) request[2]) {
				requestVo.setRequestDate((Date) request[2]);
			}

			requestVo.setUserId(authDetailsVo.getUserId());

			if (null != (Integer) request[18]) {
				requestVo.setRequestPriority((Integer) request[18]);
			}
			if (null != (String) request[17]) {
				requestVo.setRequestSubject((String) request[17]);
			}

			requestVoList.add(requestVo);
		}

		return requestVoList;
	}

	@Transactional
	private List<RequestVO> getApprovalDashboardList(List<Object[]> requestEntityList,AuthDetailsVo authDetailsVo) {

		/*String query = " SELECT request.idrin_tr_request_id , request.rin_tr_request_code, request.current_status_id,"//2
				+ " curstatus.rin_ma_current_status_code ,curstatus.rin_ma_current_status_name,"//4
				+ " reqtype.idrin_ma_request_type_id , reqtype.rin_ma_request_type_name , subtype.idrin_ma_request_subtype_id ,"//7
				+ "subtype.rin_ma_request_subtype_name , us.USER_ID , CONCAT(us.FIRST_NAME,' ',us.LAST_NAME) , request.create_date ,"//11
				+ " audit.idrin_tr_req_workflow_audit_id , audit.rin_ma_req_workflow_id , audit.rin_tr_req_workflow_seq_id ,"//14
				+ " audit.rin_tr_req_workflow_audit_group_id , audit.rin_tr_req_workflow_audit_sequence , "//16
				+ " audit.rin_tr_req_workflow_audit_descision_type,"//17
				+ " audit.rin_tr_req_workflow_audit_approval_executer , audit.rin_tr_req_workflow_audit_remarks,"//19
				+ " request.rin_tr_request_priority,request.rin_tr_request_subject"//21
*/				
		//Changed for approval list
		List<RequestVO> requestVoList = new ArrayList<RequestVO>();
		for (Object[] request : requestEntityList) {

			RequestVO requestVo = new RequestVO();
			if (null != (Integer) request[0]) {
				requestVo.setRequestId((Integer) request[0]);
			}
			if (null != (String) request[1]) {
				requestVo.setRequestCode((String) request[1]);
			}
			if (null != (String) request[4]) {
				requestVo.setCurrentStatusName((String) request[4]);
			}
			if (null != (Integer) request[2]) {
				requestVo.setCurrentStatusId((Integer) request[2]);
			}
			
			if (null != (Integer) request[5]) {
				requestVo.setRequestTypeId((Integer) request[5]);
			}
			if (null != (String) request[6]) {
				requestVo.setRequestTypeName((String) request[6]);
			}
			if (null != (Integer) request[7]) {
				requestVo.setRequestSubtypeId((Integer) request[7]);
			}
			if (null != (String) request[8]) {
				requestVo.setRequestSubTypeName((String) request[8]);
			}
			if (null != (String) request[10]) {
				requestVo.setUserName((String) request[10]);
			}
			if (null != (Date) request[11]) {
				requestVo.setRequestDate((Date) request[11]);
			}

			requestVo.setUserId(authDetailsVo.getUserId());

			if (null != (Integer) request[20]) {
				requestVo.setRequestPriority((Integer) request[20]);
			}
			if (null != (String) request[21]) {
				requestVo.setRequestSubject((String) request[21]);
			}

			requestVoList.add(requestVo);
		}

		return requestVoList;
	}
	/**
	 * Method is used to search the my request
	 * 
	 * @param requestVo
	 * @return
	 */
	@Transactional
	public List<RequestVO> getReqListSearch(RequestVO requestVo,AuthDetailsVo authDetailsVo) {

		requestVo.setCreatedBy(authDetailsVo.getUserId());

		List<Object[]> requestList = null;

		// Get all the my request details
		try {

			requestList = requestDAO.getAllSearch(requestVo,authDetailsVo);

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dbFailure",authDetailsVo));
		}

		// Set all the fields of my request
		List<RequestVO> requestVoList = getAllList(requestList,authDetailsVo);

		return requestVoList;
	}

	/**
	 * Method is used to get the my request dash board count in summary
	 * 
	 * @return summary
	 */
	public List<RequestSummaryVO> getMyrequestDashboardCount(AuthDetailsVo authDetailsVo) {

		List<RequestSummaryVO> summary = new ArrayList<RequestSummaryVO>();

		List<RequestSubTypeVO> requestSubTypeVo = null;

		BigInteger count = null;

		// Get all the request sub type details		 
			requestSubTypeVo = requestSubTypeService.getAll(authDetailsVo);	

		for (RequestSubTypeVO requestSubType : requestSubTypeVo) {

			// Find the count of request for the specified request sub type
			try {

				count = requestSummaryDAO.findRequestSubtype(requestSubType.getRequestSubTypeId(),authDetailsVo);

			} catch (NoResultException e) {
				logger.error(e.getMessage());
				throw new CommonException(getMessage("noResultFound",authDetailsVo));
			} catch (NonUniqueResultException e) {
				logger.error(e.getMessage());
				throw new CommonException(getMessage("noUniqueFound",authDetailsVo));
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new CommonException(getMessage("dataFailure",authDetailsVo));
			}

			if (count.intValue() != 0) {

				RequestSummaryVO summaryVo = new RequestSummaryVO();

				summaryVo.setRequestSubtypeName(requestSubType.getRequestSubTypeName());
				summaryVo.setSubtypeCount(count.intValue());
				summary.add(summaryVo);
			}
		}

		return summary;
	}

	/**
	 * Method is to get the approval dash board count of summary
	 * 
	 * @return summary
	 */
	public List<RequestSummaryVO> getResolverNumberCount(AuthDetailsVo authDetailsVo) {

		RequestVO requestVo = new RequestVO();

		List<RequestSummaryVO> summary = new ArrayList<RequestSummaryVO>();

		List<RequestVO> requestList = null;

		List<RequestSubTypeVO> requestSubTypeVo = null;
		
		try {
		// get all the approval details
		 
         requestList = requestResolverService.getAllHistory(requestVo,authDetailsVo);		

		// Get all the request sub type details	
		requestSubTypeVo = requestSubTypeService.getAll(authDetailsVo);
		
		for (RequestSubTypeVO requestSubType : requestSubTypeVo) {

			int count = 0;

			for (RequestVO request : requestList) {

				if (request.getRequestSubtypeId().equals(requestSubType.getRequestSubTypeId())) {
					count++;
				}
			}

			if (count != 0) {

				RequestSummaryVO summaryVo = new RequestSummaryVO();

				summaryVo.setRequestSubtypeName(requestSubType.getRequestSubTypeName());
				summaryVo.setSubtypeCount(count);

				summary.add(summaryVo);
			}
		}
		
		} catch (CommonException e) {
			logger.error(e.getMessage());
			throw new CommonException(e.getMessage());
		}

		return summary;
	}

	/**
	 * Method is to get the approval dash board count of summary
	 * 
	 * @return summary
	 */
	public RequestSummaryVO getOthersDashboardCount(AuthDetailsVo authDetailsVo) {

		RequestVO requestVo = new RequestVO();

		List<RequestVO> requestList = null;

		List<RequestSubTypeVO> requestSubTypeVo = null;

		List<CurrentStatusEntity> currentStatusList = null;

		// Get the history of all approval
		try {

			requestList = getAllHistory(requestVo,authDetailsVo);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new CommonException(e.getMessage());
		}

		List<RequestSummaryVO> summaryList = new ArrayList<RequestSummaryVO>();

		List<RequestSummaryVO> summaryListNext = new ArrayList<RequestSummaryVO>();

		RequestSummaryVO summary = new RequestSummaryVO();

		// Get all the request sub type
		try {

			requestSubTypeVo = requestSubTypeService.getAll(authDetailsVo);

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(e.getMessage());
		}

		if (requestList != null) {

			if (requestSubTypeVo != null) {

				for (RequestSubTypeVO requestSubType : requestSubTypeVo) {

					int count = 0;

					for (RequestVO request : requestList) {

						if (request.getRequestSubtypeId() == requestSubType.getRequestSubTypeId()) {
							count++;
						}
					}

					if (count != 0) {

						RequestSummaryVO summaryVo = new RequestSummaryVO();

						summaryVo.setRequestSubtypeName(requestSubType.getRequestSubTypeName());
						summaryVo.setSubtypeCount(count);

						summaryList.add(summaryVo);
					}

				}
			}
		}

		// Get all the current status
		try {

			currentStatusList = currentStatusRepository.getAllCurrentStatus();

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dbFailure",authDetailsVo));
		}

		if (requestList != null) {

			if (currentStatusList != null) {
				for (CurrentStatusEntity currentStatusEntity : currentStatusList) {

					int count = 0;

					for (RequestVO request : requestList) {

						if (request.getCurrentStatusId() == currentStatusEntity.getCurrentStatusId()) {
							count++;
						}
					}

					if (count != 0) {

						RequestSummaryVO summaryVo = new RequestSummaryVO();

						summaryVo.setCurrentStatusName(currentStatusEntity.getCurrentStatusName());
						summaryVo.setCurrentStatusCount(count);

						summaryListNext.add(summaryVo);
					}

				}
			}
		}

		summary.setSubtypeList(summaryList);
		summary.setStatuswiseList(summaryListNext);
		return summary;
	}

	@Transactional
	public List<RequestVO> getAll(RequestVO requestVo,AuthDetailsVo authDetailsVo) throws CommonException {

		List<RequestVO> requestVoList = new ArrayList<RequestVO>();

		List<Object[]> requestEntityList = null;

		// Get all the Approval list from DB
		try {

			requestEntityList = requestSummaryDAO.getAll(requestVo,authDetailsVo);

			// Set all the Fields of Approval details

			if (requestEntityList != null && requestEntityList.size() > 0) {
				requestVoList = getApprovalDashboardList(requestEntityList,authDetailsVo);

			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dbFailure",authDetailsVo));
		}
		return requestVoList;
	}

	/**
	 * Method is to get the approval dash board count of summary
	 * 
	 * @return summary
	 */
	public List<RequestSummaryVO> getApprovalDashboardCount(AuthDetailsVo authDetailsVo) throws CommonException {

		RequestVO requestVo = new RequestVO();

		List<RequestSummaryVO> summary = new ArrayList<RequestSummaryVO>();

		List<RequestVO> requestList = null;

		List<RequestSubTypeVO> requestSubTypeVo = null;

		// get all the approval details
		try {

		requestList = getAll(requestVo,authDetailsVo);
	
		if (requestList != null) {
			for (RequestVO request : requestList) {
				if (request.getRequestId() == 0) {
					throw new CommonException(getMessage("noRecordFound",authDetailsVo));

				}
			}

			// Get all the request sub type details
			requestSubTypeVo = requestSubTypeService.getAll(authDetailsVo);
			 
				for (RequestSubTypeVO requestSubType : requestSubTypeVo) {

					int count = 0;

					for (RequestVO request : requestList) {

						if (request.getRequestSubtypeId().equals(requestSubType.getRequestSubTypeId())) {
							count++;
						}
					}

					if (count != 0) {

						RequestSummaryVO summaryVo = new RequestSummaryVO();

						summaryVo.setRequestSubtypeName(requestSubType.getRequestSubTypeName());
						summaryVo.setSubtypeCount(count);

						summary.add(summaryVo);
					}

				}
		} else {
			throw new CommonException(getMessage("noRecordFound",authDetailsVo));
		}
		} catch (CommonException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new CommonException(e.getMessage());
		}
		return summary;
	}

	@SuppressWarnings("rawtypes")
	@Transactional
	public RequestVO findRequestSummary(RequestVO requestLoad,AuthDetailsVo authDetailsVo) throws Exception {
		List<Object> result = null;

		List<RequestWorkFlowAuditVO> list_RequestWorkFlowAuditVo = new ArrayList<RequestWorkFlowAuditVO>();
		List<Object> list_RequestWorkFlowAuditEntity = null;

		List<RequestDetailVO> listRequestVoList = new ArrayList<RequestDetailVO>();
		RequestVO requestVo = new RequestVO();
		RequestVO request = new RequestVO();
		try {

			BeanUtils.copyProperties(requestLoad, request);
		} catch (CommonException e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("beanUtilPropertiesFailure",authDetailsVo));
		}

		//try {
			result = requestDAO.findRequest(requestLoad,authDetailsVo);
		/*} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}*/

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
				if (null != object[7]) {

					requestVo.setUserDepartmentName((String) object[7]);
				}
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
				if (null != object[5]) {

					requestVo.setReqLocationName((String) object[5]);
				}

				if (null != object[6]) {

					requestVo.setReqSublocationName((String) object[6]);
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
				
				listRequestVoList.add(requestDetailVo);

			}
		}
		request.setRequest(requestVo);
		request.setRequestDetailList(listRequestVoList);

		try {
			list_RequestWorkFlowAuditEntity = requestSummaryDAO.getAllApprovalSummary(requestVo.getRequestId());

		} catch (CommonException e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("requestWorkFlowAuditListFailure",authDetailsVo));
		}
		if (list_RequestWorkFlowAuditEntity != null && list_RequestWorkFlowAuditEntity.size() > 0) {
			list_RequestWorkFlowAuditVo = getAllApproval(list_RequestWorkFlowAuditEntity,authDetailsVo);
			request.setRequestWorkFlowAuditVoList(list_RequestWorkFlowAuditVo);
		}
		list_RequestWorkFlowAuditEntity = null;
		list_RequestWorkFlowAuditVo = null;
		if (0 != requestVo.getRequestSubId()) {
			try {
				list_RequestWorkFlowAuditEntity = requestDAO.getAllReSubmitList(requestVo.getRequestId());

			} catch (CommonException e) {
				logger.error(e.getMessage());
				throw new CommonException(getMessage("requestReSubmitListFailure",authDetailsVo));

			}

			if (list_RequestWorkFlowAuditEntity != null) {
				list_RequestWorkFlowAuditVo = getAllApproval(list_RequestWorkFlowAuditEntity,authDetailsVo);
				request.setReSubmitList(list_RequestWorkFlowAuditVo);
			}
		}

		return request;
	}

	@Transactional
	public List<RequestWorkFlowAuditVO> getAllApproval(List<Object> list_RequestWorkFlowAuditEntity,AuthDetailsVo authDetailsVo)
			throws CommonException {
		Date updateDate = null;
		int groupId = 0;
		int actualSlaTime = 0;

		if (null != (String) ((Object[]) list_RequestWorkFlowAuditEntity.get(0))[22]) {
			String requestSequence = (String) ((Object[]) list_RequestWorkFlowAuditEntity.get(0))[22];
			StringTokenizer st3 = new StringTokenizer(requestSequence, ",");

			List<Integer> list_value = new ArrayList<>();
			while (st3.hasMoreElements()) {

				list_value.add(Integer.parseInt(st3.nextToken()));

			}
		}
		List<RequestWorkFlowAuditVO> list_RequestWorkFlowAuditVo = new ArrayList<RequestWorkFlowAuditVO>();
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
			//if (null != ((Object[]) object)[7]) {
				requestWorkFlowAuditVo.setDescisionType((int) ((Object[]) object)[7]);
			//}
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

			if ((int) ((Object[]) object)[7] != 9) {
				if (null != ((Object[]) object)[17] && requestWorkFlowAuditVo.getDescisionType() != 0) {
					requestWorkFlowAuditVo.setUpdatedDate((Date) ((Object[]) object)[17]);
				}
			} else {
				requestWorkFlowAuditVo.setUpdatedDate(null);
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
		/*	if (requestWorkFlowAuditVo.getApprovalExecuter() != 3 &&  requestWorkFlowAuditVo.getDescisionType() != 0
					&& requestWorkFlowAuditVo.getDescisionType() != 9 ) {
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
					requestWorkFlowAuditVo.setStatus(CommonConstant.CONSTANT_ONE);
				} else if (actualSlaTime > requestWorkFlowAuditVo.getMinutes()) {
					requestWorkFlowAuditVo.setStatus(CommonConstant.CONSTANT_TWO);
				}
			}*/
			
			
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
															
			}else {

				if (null != ((Object[]) object)[15] && requestWorkFlowAuditVo.getDescisionType() != 0) {
					requestWorkFlowAuditVo.setCreatedDate((Date) ((Object[]) object)[15]);
				}
			}		
			
			list_RequestWorkFlowAuditVo.add(requestWorkFlowAuditVo);
		}

		return list_RequestWorkFlowAuditVo;
	}

	@Transactional
	public List<RequestVO> getAllHistory(RequestVO requestVo,AuthDetailsVo authDetailsVo) {

		List<RequestVO> requestVoList = new ArrayList<RequestVO>();

		List<Object[]> requestEntityList = null;

		try{
		List<Integer> subOrdinate = getsubOrdinateList(authDetailsVo.getUserId(),authDetailsVo);
	 		
		// Get all the Approval list from DB	 
        requestEntityList = requestSummaryDAO.getAllHistory(requestVo, subOrdinate,authDetailsVo);
		 
		// Set all the Fields of Approval details	
		if (requestEntityList != null && requestEntityList.size() > 0) {
			requestVoList = getOtherDetailsList(requestEntityList,authDetailsVo);
		}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dbFailure",authDetailsVo));
		}
		return requestVoList;

	}

	@Transactional
	public List<Integer> getsubOrdinateList(int currentLoginUserId,AuthDetailsVo authDetailsVo) {

		List<UserMappingEntity> userMappingEntityList = new ArrayList<UserMappingEntity>();
		List<Integer> subOrdinateList = new ArrayList<Integer>();

		userMappingEntityList = getlist(currentLoginUserId, userMappingEntityList,authDetailsVo);

		for (UserMappingEntity screenAuthenticationEntity : userMappingEntityList) {

			if (screenAuthenticationEntity.getUserEntity() != null) {
				subOrdinateList.add(screenAuthenticationEntity.getUserEntity().getId());
			}

		}
		return subOrdinateList;
	}

	@Transactional
	private List<UserMappingEntity> getlist(int currentLoginUserId, List<UserMappingEntity> userMappingEntity,AuthDetailsVo authDetailsVo) {

		List<UserMappingEntity> userMappingEntityList = requestSummaryDAO.getListOfUser(currentLoginUserId,authDetailsVo);

		if (null != userMappingEntityList) {

			for (UserMappingEntity screenAuthenticationEntity : userMappingEntityList) {
				if (null != screenAuthenticationEntity.getUserEntity()) {
					getlist(screenAuthenticationEntity.getUserEntity().getId(), userMappingEntity,authDetailsVo);
				}
				userMappingEntity.add(screenAuthenticationEntity);
			}

		}

		return userMappingEntity;

	}
}
