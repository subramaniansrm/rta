package com.srm.rta.service;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.transaction.Transactional;

import org.apache.commons.beanutils.BeanUtils;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.config.MailMessages;
import com.srm.coreframework.constants.DecisionTypeEnum;
import com.srm.coreframework.constants.FilePathConstants;
import com.srm.coreframework.controller.CommonController;
import com.srm.coreframework.entity.UserEntity;
import com.srm.coreframework.entity.UserMappingEntity;
import com.srm.coreframework.response.JSONResponse;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.coreframework.vo.EntityLicenseVO;
import com.srm.coreframework.vo.MailParameterVO;
import com.srm.coreframework.vo.SystemConfigurationVo;
import com.srm.coreframework.vo.UserMappingVO;
import com.srm.coreframework.vo.UserMasterVO;
import com.srm.rta.dao.HolidayDAO;
import com.srm.rta.dao.RequestConfigurationDAO;
import com.srm.rta.dao.RequestDAO;
import com.srm.rta.entity.RequestEntity;
import com.srm.rta.entity.RequestWorkFlowAuditEntity;
import com.srm.rta.entity.RequestWorkFlowDetailsEntity;
import com.srm.rta.entity.RequestWorkFlowEntity;
import com.srm.rta.entity.RequestWorkFlowExecuterEntity;
import com.srm.rta.entity.RequestWorkFlowSeqEntity;
import com.srm.rta.entity.RequestWorkFlowSlaEntity;
import com.srm.rta.repository.MailParameterRepository;
import com.srm.rta.repository.RequestRepository;
import com.srm.rta.repository.RequestWorkFlowAuditRepository;
import com.srm.rta.repository.RequestWorkFlowDetailsRepository;
import com.srm.rta.repository.RequestWorkFlowExecuterRepository;
import com.srm.rta.repository.RequestWorkFlowRepository;
import com.srm.rta.repository.RequestWorkFlowSequenceRepository;
import com.srm.rta.repository.RequestWorkFlowSlaRepository;
import com.srm.rta.vo.EmailVo;
import com.srm.rta.vo.RequestDetailVO;
import com.srm.rta.vo.RequestSubTypeVO;
import com.srm.rta.vo.RequestTypeVO;
import com.srm.rta.vo.RequestVO;
import com.srm.rta.vo.RequestWorkFlowAuditVO;
import com.srm.rta.vo.RequestWorkFlowDetailsVO;
import com.srm.rta.vo.RequestWorkFlowExecuterVO;
import com.srm.rta.vo.RequestWorkFlowSequenceVO;
import com.srm.rta.vo.RequestWorkFlowSlaVO;
import com.srm.rta.vo.RequestWorkFlowVO;

import lombok.Data;
@Data
@Service
public class RequestConfigurationService extends CommonController<RequestWorkFlowVO> {

	int hierachy = CommonConstant.CONSTANT_ZERO;
	int count = CommonConstant.CONSTANT_ZERO;
	int localcount = CommonConstant.CONSTANT_ZERO;

	@Autowired
	RequestConfigurationDAO requestConfigurationDAO;

	@Autowired
	MailMessages mailMessages;

	
	@Autowired
	MailParameterRepository mailParameterRepository;
	 

	@Autowired
	RequestRepository requestRepository;

	@Autowired
	RequestWorkFlowAuditRepository requestWorkFlowAuditRepository;

	@Autowired
	RequestWorkFlowRepository requestWorkFlowRepository;

	@Autowired
	RequestWorkFlowSequenceRepository requestWorkFlowSequenceRepository;

	@Autowired
	RequestWorkFlowDetailsRepository requestWorkFlowDetailsRepository;

	@Autowired
	RequestWorkFlowExecuterRepository requestWorkFlowExecuterRepository;

	@Autowired
	RequestWorkFlowSlaRepository requestWorkFlowSlaRepository;
		
	@Autowired
	RequestDAO requestDAO;
		
	@Autowired
	HolidayDAO holidayDAO;
	
	@Autowired
	RequestTypeService requestTypeService;
	
	@Autowired
	RequestSubTypeService requestSubTypeService;
	
	@Value("${emailRestTemplateUrl}")
	private String emailRestTemplateUrl;
		
	@Autowired
	private RestTemplate restTemplate;
	
	
	@Transactional
	public List<RequestWorkFlowVO> getAll(AuthDetailsVo authDetailsVo) throws CommonException {

		try {
			List<RequestWorkFlowVO> requestWorkFlowVoList = new ArrayList<RequestWorkFlowVO>();

			// Get all the information for requestWorkFlow
			// List<Object> requestWorkFlowEntityList =
			// requestWorkFlowRepository.getAll(authDetailsVo.getEntityId());

			List<Object> requestWorkFlowEntityList = requestConfigurationDAO.getAll(authDetailsVo.getEntityId(),
					authDetailsVo);

			if (requestWorkFlowEntityList != null && requestWorkFlowEntityList.size() > CommonConstant.CONSTANT_ZERO) {
				// Get all the List of information for requestWorkFlow
				requestWorkFlowVoList = getAllList(requestWorkFlowEntityList);

			}

			return requestWorkFlowVoList;
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Request Configuration Service get All Exception",e);
			throw new CommonException(getMessage("dataFailure", authDetailsVo));
		}
	}

	@Transactional
	public List<RequestWorkFlowVO> getAllList(List<Object> requestWorkFlowEntityList) throws CommonException {

		List<RequestWorkFlowVO> requestWorkFlowVoList = new ArrayList<RequestWorkFlowVO>();

		for (Object requestWorkFlowEntity : requestWorkFlowEntityList) {

			RequestWorkFlowVO requestWorkFlowVo = new RequestWorkFlowVO();

			if (null != (String) ((Object[]) requestWorkFlowEntity)[0]) {
				requestWorkFlowVo.setReqWorkFlowId((int) ((Object[]) requestWorkFlowEntity)[4]);
				requestWorkFlowVo.setRequestWorkFlowCode((String) ((Object[]) requestWorkFlowEntity)[0]);
			}

			if (null != (String) ((Object[]) requestWorkFlowEntity)[2]) {
				requestWorkFlowVo.setRequestTypeName((String) ((Object[]) requestWorkFlowEntity)[2]);
			}
			if (null != (String) ((Object[]) requestWorkFlowEntity)[3]) {
				requestWorkFlowVo.setRequestSubTypeName((String) ((Object[]) requestWorkFlowEntity)[3]);
			}

			if ((int) ((Object[]) requestWorkFlowEntity)[5] == 1) {
				requestWorkFlowVo.setReqWorkFlowIsActive(CommonConstant.BOOLEAN_TRUE);
				requestWorkFlowVo.setStatus(CommonConstant.Active);

			} else {
				requestWorkFlowVo.setReqWorkFlowIsActive(CommonConstant.BOOLEAN_FALSE);
				requestWorkFlowVo.setStatus(CommonConstant.InActive);

			}

			if (null != (String) ((Object[]) requestWorkFlowEntity)[1]) {
				requestWorkFlowVo.setReqWorkFlowDescription((String) ((Object[]) requestWorkFlowEntity)[1]);
			}

			requestWorkFlowVoList.add(requestWorkFlowVo);
		}

		return requestWorkFlowVoList;
	}

	@Transactional
	public List<RequestWorkFlowVO> getAllSearch(RequestWorkFlowVO requestWorkFlowVoSearch, AuthDetailsVo authDetailsVo)
			throws CommonException {

		List<RequestWorkFlowVO> requestWorkFlowVoList = new ArrayList<RequestWorkFlowVO>();

		List<Object> requestWorkFlowEntityList = null;

		try {

			// Get all the information for particular values
			requestWorkFlowEntityList = requestConfigurationDAO.getAllSearch(requestWorkFlowVoSearch, authDetailsVo);

			if (requestWorkFlowEntityList != null && requestWorkFlowEntityList.size() > CommonConstant.CONSTANT_ZERO) {
				// Get all the list of information for particular values
				requestWorkFlowVoList = getAllList(requestWorkFlowEntityList);
			}
		} catch (Exception exe) {
			Log.info("Request Configuration Service get AllSearch Exception",exe);
			throw new CommonException(getMessage("dataFailure", authDetailsVo));
		}

		return requestWorkFlowVoList;
	}

	@Transactional
	public void delete(RequestWorkFlowVO requestWorkFlowVo, AuthDetailsVo authDetailsVo) throws CommonException {

		for (Integer id : requestWorkFlowVo.getRequestWorkFlowList()) {

			try {
				// Put the deleteFlag 1 to db
				requestConfigurationDAO.delete(id, authDetailsVo);
			} catch (Exception exe) {
				Log.info("Request Configuration Service delete Exception",exe);
				throw new CommonException(getMessage("modifyFailure", authDetailsVo));
			}
		}

	}

	@Transactional
	public RequestWorkFlowVO findrequestWorkFlow(RequestWorkFlowVO requestWorkFlow, AuthDetailsVo authDetailsVo)
			throws CommonException {

		try {
			Object object = new Object();
			List<RequestWorkFlowSeqEntity> requestWorkFlowSeqEntityList = null;
			List<RequestWorkFlowExecuterEntity> requestWorkFlowExecuterEntityList = null;

			List<RequestWorkFlowDetailsEntity> requestWorkFlowDetailsEntity = null;

			object = requestWorkFlowRepository.findWorkFlow(requestWorkFlow.getReqWorkFlowId());
			RequestWorkFlowVO requestWorkFlowVo = new RequestWorkFlowVO();
			try {
				BeanUtils.copyProperties(requestWorkFlowVo, requestWorkFlow);
			} catch (CommonException e) {
				Log.info("Request Configuration Service findrequestWorkFlow Common Exception",e);
				throw new CommonException(getMessage("beanUtilPropertiesFailure", authDetailsVo));
			}
			if(null != object){
			if ((Integer) ((Object[]) object)[0] != null) {
				requestWorkFlowVo.setReqWorkFlowId((int) ((Object[]) object)[0]);
			}
			if ((String) ((Object[]) object)[1] != null) {
				requestWorkFlowVo.setRequestWorkFlowCode((String) ((Object[]) object)[1]);
			}
			if ((Integer) ((Object[]) object)[2] != null) {
				requestWorkFlowVo.setRequestTypeId((int) ((Object[]) object)[2]);
			}
			if ((Integer) ((Object[]) object)[3] != null) {
				requestWorkFlowVo.setRequestSubTypeId((int) ((Object[]) object)[3]);
			}
			if ((String) ((Object[]) object)[17] != null) {
				requestWorkFlowVo.setRequestTypeName((String) ((Object[]) object)[17]);
			}
			if ((String) ((Object[]) object)[18] != null) {
				requestWorkFlowVo.setRequestSubTypeName((String) ((Object[]) object)[18]);
			}
			if (null != ((Object[]) object)[7]) {
				requestWorkFlowVo.setSlaConfigure((int) ((Object[]) object)[7]);
			}
			if (null != (String) ((Object[]) object)[8]) {
				requestWorkFlowVo.setReqWorkFlowDescription((String) ((Object[]) object)[8]);
			}
			if ((int) ((Object[]) object)[10] == CommonConstant.CONSTANT_ONE) {
				requestWorkFlowVo.setReqWorkFlowIsActive(CommonConstant.BOOLEAN_TRUE);
			} else {
				requestWorkFlowVo.setReqWorkFlowIsActive(CommonConstant.BOOLEAN_FALSE);
			}
			if ((int) ((Object[]) object)[4] == CommonConstant.CONSTANT_ONE) {
				requestWorkFlowVo.setReqWorkFlowIsMailRequired(CommonConstant.BOOLEAN_TRUE);
			} else {
				requestWorkFlowVo.setReqWorkFlowIsMailRequired(CommonConstant.BOOLEAN_FALSE);
			}
			if ((int) ((Object[]) object)[5] == CommonConstant.CONSTANT_ONE) {
				requestWorkFlowVo.setReqWorkFlowIsNotificationRequired(CommonConstant.BOOLEAN_TRUE);
			} else {
				requestWorkFlowVo.setReqWorkFlowIsNotificationRequired(CommonConstant.BOOLEAN_FALSE);
			}
			if ((int) ((Object[]) object)[6] == CommonConstant.CONSTANT_ONE) {
				requestWorkFlowVo.setReqWorkFlowIsMgtEscalationRequired(CommonConstant.BOOLEAN_TRUE);
			} else {
				requestWorkFlowVo.setReqWorkFlowIsMgtEscalationRequired(CommonConstant.BOOLEAN_FALSE);
			}
			if ((int) ((Object[]) object)[9] == CommonConstant.CONSTANT_ONE) {
				requestWorkFlowVo.setReqWorkFlowReassign(CommonConstant.BOOLEAN_TRUE);
			} else {
				requestWorkFlowVo.setReqWorkFlowReassign(CommonConstant.BOOLEAN_FALSE);
			}
			}
			requestWorkFlowDetailsEntity = findWorkFlowDetails(requestWorkFlow.getReqWorkFlowId(), authDetailsVo);
			if (requestWorkFlowDetailsEntity != null) {
				fetchRequestWorkFlowDetails(requestWorkFlowVo, requestWorkFlowDetailsEntity, authDetailsVo);
			}

			requestWorkFlowExecuterEntityList = findWorkFlowExecuter(requestWorkFlow.getReqWorkFlowId(), authDetailsVo);
			if (requestWorkFlowExecuterEntityList != null) {

				fetchRequestWorkFlowExecuter(requestWorkFlowVo, requestWorkFlowExecuterEntityList, authDetailsVo);
			}

			requestWorkFlowSeqEntityList = findWorkFlowDetail(requestWorkFlow.getReqWorkFlowId(), authDetailsVo);
			if (requestWorkFlowSeqEntityList != null) {

				fetchRequestWorkFlowDetailValuesToVo(requestWorkFlowVo, requestWorkFlowSeqEntityList, authDetailsVo);
			}
			return requestWorkFlowVo;
		} catch (NoResultException e) {
			Log.info("Request Configuration Service findrequestWorkFlow NoResultException ",e);
			throw new CommonException(getMessage("noResultFound", authDetailsVo));
		} catch (NonUniqueResultException e) {
			Log.info("Request Configuration Service findrequestWorkFlow NoResultException",e);
			throw new CommonException(getMessage("noUniqueFound", authDetailsVo));
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Request Configuration Service findrequestWorkFlow  Exception",e);
			throw new CommonException(getMessage("dataFailure", authDetailsVo));
		}
	}

	@Transactional
	public List<RequestWorkFlowDetailsEntity> findWorkFlowDetails(int id, AuthDetailsVo authDetailsVo)
			throws CommonException {

		List<RequestWorkFlowDetailsEntity> requestWorkFlowDetailsEntity = new ArrayList<>();
		try {
			requestWorkFlowDetailsEntity = (List<RequestWorkFlowDetailsEntity>) requestWorkFlowDetailsRepository
					.findWorkFlowDetails(id);

		} catch (Exception exe) {
			Log.info("Request Configuration Service findWorkFlowDetails  Exception",exe);
			throw new CommonException(getMessage("dataFailure", authDetailsVo));
		}

		return requestWorkFlowDetailsEntity;

	}

	@Transactional
	public List<RequestWorkFlowExecuterEntity> findWorkFlowExecuter(int id, AuthDetailsVo authDetailsVo)
			throws CommonException {

		List<RequestWorkFlowExecuterEntity> requestWorkFlowExecuterEntityList = null;
		try {
			requestWorkFlowExecuterEntityList = (List<RequestWorkFlowExecuterEntity>) requestWorkFlowExecuterRepository
					.findWorkFlowExecuter(id);

		} catch (Exception exe) {
			Log.info("Request Configuration Service findWorkFlowExecuter  Exception",exe);
			throw new CommonException(getMessage("dataFailure", authDetailsVo));
		}

		return requestWorkFlowExecuterEntityList;

	}

	/**
	 * This method is used to find whether the Details information based on
	 * workFlowId.
	 * 
	 * @param id
	 *            int
	 * @return requestWorkFlowSeqEntityList List<RequestWorkFlowSeqEntity>
	 * @throws CommonException
	 */
	@Transactional
	public List<RequestWorkFlowSeqEntity> findWorkFlowDetail(int id, AuthDetailsVo authDetailsVo)
			throws CommonException {

		List<RequestWorkFlowSeqEntity> requestWorkFlowSeqEntityList = null;
		try {
			requestWorkFlowSeqEntityList = (List<RequestWorkFlowSeqEntity>) requestWorkFlowSequenceRepository
					.findWorkFlowId(id);

		} catch (Exception exe) {
			Log.info("Request Configuration Service findWorkFlowDetail  Exception",exe);
			throw new CommonException(getMessage("dataFailure", authDetailsVo));
		}

		return requestWorkFlowSeqEntityList;

	}

	@Transactional
	public RequestWorkFlowVO fetchRequestWorkFlowDetails(RequestWorkFlowVO requestWorkFlowVo,
			List<RequestWorkFlowDetailsEntity> requestWorkFlowDetailsEntityList, AuthDetailsVo authDetailsVo)
					throws CommonException {

		List<RequestWorkFlowDetailsVO> requestWorkFlowDetailsVoList = new ArrayList<RequestWorkFlowDetailsVO>();

		for (RequestWorkFlowDetailsEntity requestWorkFlowDetailsEntity : requestWorkFlowDetailsEntityList) {

			RequestWorkFlowDetailsVO requestWorkFlowDetailsVo = new RequestWorkFlowDetailsVO();
			try {
				BeanUtils.copyProperties(requestWorkFlowDetailsVo, requestWorkFlowDetailsEntity);
			} catch (Exception exe) {
				Log.info("Request Configuration Service fetchRequestWorkFlowDetails  Exception",exe);
				throw new CommonException(getMessage("beanUtilPropertiesFailure", authDetailsVo));
			}

			List<Integer> workFlowDepartment = new ArrayList<>();

			workFlowDepartment.add(requestWorkFlowDetailsEntity.getWorkFlowDepartmentId());

			requestWorkFlowDetailsVo.setWorkFlowDepartment(workFlowDepartment);
			requestWorkFlowDetailsVoList.add(requestWorkFlowDetailsVo);

		}

		requestWorkFlowVo.setRequestWorkFlowDetailsVoList(requestWorkFlowDetailsVoList);

		return requestWorkFlowVo;

	}

	@Transactional
	public RequestWorkFlowVO fetchRequestWorkFlowExecuter(RequestWorkFlowVO requestWorkFlowVo,
			List<RequestWorkFlowExecuterEntity> requestWorkFlowExecuterEntityList, AuthDetailsVo authDetailsVo)
					throws CommonException {

		List<RequestWorkFlowExecuterVO> requestWorkFlowExecuterVoList = new ArrayList<RequestWorkFlowExecuterVO>();

		for (RequestWorkFlowExecuterEntity requestWorkFlowExecuterEntity : requestWorkFlowExecuterEntityList) {

			RequestWorkFlowExecuterVO requestWorkFlowExecuterVo = new RequestWorkFlowExecuterVO();
			try {
				BeanUtils.copyProperties(requestWorkFlowExecuterVo, requestWorkFlowExecuterEntity);
			} catch (Exception exe) {
				Log.info("Request Configuration Service fetchRequestWorkFlowExecuter  Exception",exe);
				throw new CommonException(getMessage("beanUtilPropertiesFailure", authDetailsVo));
			}

			List<RequestWorkFlowSlaEntity> requestWorkFlowSlaEntityList = null;
			try {
				requestWorkFlowSlaEntityList = requestWorkFlowSlaRepository.findExecuterSlaId(
						requestWorkFlowVo.getReqWorkFlowId(), requestWorkFlowExecuterEntity.getReqWorkFlowExecuterId());
			} catch (Exception exe) {
				Log.info("Request Configuration Service fetchRequestWorkFlowExecuter  Exception",exe);
				throw new CommonException(getMessage("dataFailure", authDetailsVo));
			}
			List<RequestWorkFlowSlaVO> requestWorkFlowSlaVoList = new ArrayList<RequestWorkFlowSlaVO>();

			for (RequestWorkFlowSlaEntity requestWorkFlowSlaEntity : requestWorkFlowSlaEntityList) {

				RequestWorkFlowSlaVO requestWorkFlowSlaVo = new RequestWorkFlowSlaVO();
				try {
					BeanUtils.copyProperties(requestWorkFlowSlaVo, requestWorkFlowSlaEntity);
				} catch (Exception exe) {
					Log.info("Request Configuration Service fetchRequestWorkFlowExecuter  Exception",exe);
					throw new CommonException(getMessage("beanUtilPropertiesFailure", authDetailsVo));
				}

				requestWorkFlowSlaVoList.add(requestWorkFlowSlaVo);

				//Weekend & Holiday flag
				if(requestWorkFlowSlaEntity.isReqWeekendFlag()){
					requestWorkFlowVo.setExecutorWeekend(true);
				}else{
					requestWorkFlowVo.setExecutorWeekend(false);
				}
				if(requestWorkFlowSlaEntity.isReqHolidayFlag()){
					requestWorkFlowVo.setExecutorHoliday(true);
				}else{
					requestWorkFlowVo.setExecutorHoliday(false);
				}													
			}

			requestWorkFlowExecuterVo.setRequestWorkFlowSlaVo(requestWorkFlowSlaVoList);

			requestWorkFlowExecuterVoList.add(requestWorkFlowExecuterVo);

		}

		requestWorkFlowVo.setRequestWorkFlowExecuterVo(requestWorkFlowExecuterVoList);

		return requestWorkFlowVo;

	}

	@Transactional
	public RequestWorkFlowVO fetchRequestWorkFlowDetailValuesToVo(RequestWorkFlowVO requestWorkFlowVo,
			List<RequestWorkFlowSeqEntity> requestWorkFlowEntityList, AuthDetailsVo authDetailsVo)
					throws CommonException {

		List<RequestWorkFlowSequenceVO> requestWorkFlowSequenceVoList = new ArrayList<RequestWorkFlowSequenceVO>();

		for (RequestWorkFlowSeqEntity requestWorkFlowSeqEntity : requestWorkFlowEntityList) {

			RequestWorkFlowSequenceVO requestWorkFlowSequenceVo = new RequestWorkFlowSequenceVO();
			try {
				BeanUtils.copyProperties(requestWorkFlowSequenceVo, requestWorkFlowSeqEntity);
				if (null != requestWorkFlowSeqEntity.getReqWorkFlowSeqLevelType()) {
					requestWorkFlowSequenceVo
							.setReqWorkFlowSeqLevelType(requestWorkFlowSeqEntity.getReqWorkFlowSeqLevelType());
				}
			} catch (Exception exe) {
				Log.info("Request Configuration Service fetchRequestWorkFlowDetailValuesToVo  Exception",exe);
				throw new CommonException(getMessage("beanUtilPropertiesFailure", authDetailsVo));
			}

			List<RequestWorkFlowSlaEntity> requestWorkFlowSlaEntityList = null;
			try {
				requestWorkFlowSlaEntityList = requestWorkFlowSlaRepository.findSlaId(
						requestWorkFlowVo.getReqWorkFlowId(), requestWorkFlowSeqEntity.getReqWorkFlowSeqId());
			} catch (Exception exe) {
				Log.info("Request Configuration Service fetchRequestWorkFlowDetailValuesToVo  Exception",exe);
				throw new CommonException(getMessage("dataFailure", authDetailsVo));
			}
			List<RequestWorkFlowSlaVO> requestWorkFlowSlaVoList = new ArrayList<RequestWorkFlowSlaVO>();

			for (RequestWorkFlowSlaEntity requestWorkFlowSlaEntity : requestWorkFlowSlaEntityList) {

				RequestWorkFlowSlaVO requestWorkFlowSlaVo = new RequestWorkFlowSlaVO();
				try {
					BeanUtils.copyProperties(requestWorkFlowSlaVo, requestWorkFlowSlaEntity);
				} catch (Exception exe) {
					Log.info("Request Configuration Service fetchRequestWorkFlowDetailValuesToVo  Exception",exe);
					throw new CommonException(getMessage("beanUtilPropertiesFailure", authDetailsVo));
				}
							
				//Holiday & Weekend flag
				if(requestWorkFlowSlaEntity.isReqWeekendFlag()){
					requestWorkFlowVo.setApproverWeekend(true);
				}else{
					requestWorkFlowVo.setApproverWeekend(false);
				}
				if(requestWorkFlowSlaEntity.isReqHolidayFlag()){
					requestWorkFlowVo.setApproverHoliday(true);
				}else{
					requestWorkFlowVo.setApproverHoliday(false);
				}	
				
				requestWorkFlowSlaVoList.add(requestWorkFlowSlaVo);

			}

			requestWorkFlowSequenceVo.setRequestWorkFlowSlaVo(requestWorkFlowSlaVoList);

			requestWorkFlowSequenceVoList.add(requestWorkFlowSequenceVo);

		}

		requestWorkFlowVo.setRequestWorkFlowSequenceList(requestWorkFlowSequenceVoList);

		return requestWorkFlowVo;

	}

	@Transactional
	public RequestWorkFlowVO create(RequestWorkFlowVO requestWorkFlowVo, AuthDetailsVo authDetailsVo) throws CommonException {

		RequestWorkFlowEntity requestWorkFlowEntity = new RequestWorkFlowEntity();

		try {
			BeanUtils.copyProperties(requestWorkFlowEntity, requestWorkFlowVo);
		} catch (Exception e) {
			Log.info("Request Configuration Service create  Exception",e);
			throw new CommonException(getMessage("beanUtilPropertiesFailure", authDetailsVo));
		}

		try {
			String code = requestConfigurationDAO.findAutoGenericCode(CommonConstant.RequestConfiguration,authDetailsVo);
			requestWorkFlowEntity.setRequestWorkFlowCode(code);
		} catch (Exception e) {
			Log.info("Request Configuration Service create  Exception",e);
			throw new CommonException(getMessage("autoCodeGenerationFailure", authDetailsVo));
		}

		try {
			// Set the values for create details information
			requestWorkFlowEntity = setCreateUserDetails(requestWorkFlowEntity, authDetailsVo);
			if (null != authDetailsVo.getEntityId()) {
				requestWorkFlowEntity.setEntityLicenseId(authDetailsVo.getEntityId());
			}
			// Set the values to db
			requestWorkFlowRepository.save(requestWorkFlowEntity);
		} catch (Exception exe) {
			Log.info("Request Configuration Service create  Exception",exe);
			throw new CommonException(getMessage("dataFailure", authDetailsVo));
		}
		
		
		
		List<RequestWorkFlowDetailsVO> requestWorkFlowDetailsVoList = requestWorkFlowVo
				.getRequestWorkFlowDetailsVoList();

		for (RequestWorkFlowDetailsVO requestWorkFlowDetailsVo : requestWorkFlowDetailsVoList) {

			RequestWorkFlowDetailsEntity requestWorkFlowDetailsEntity = new RequestWorkFlowDetailsEntity();

			if(null != requestWorkFlowDetailsVo.getWorkFlowDepartmentId()){
				requestWorkFlowDetailsEntity.setWorkFlowDepartmentId(requestWorkFlowDetailsVo.getWorkFlowDepartmentId());
			}
			if(null != requestWorkFlowDetailsVo.getWorkFlowLocationId()){
				requestWorkFlowDetailsEntity.setWorkFlowLocationId(requestWorkFlowDetailsVo.getWorkFlowLocationId());
			}
			if(null != requestWorkFlowDetailsVo.getWorkFlowSublocationId()){
				requestWorkFlowDetailsEntity.setWorkFlowSublocationId(requestWorkFlowDetailsVo.getWorkFlowSublocationId());
			}
			if(requestWorkFlowDetailsVo.isReqWorkFlowDetailsIsActive()){
				requestWorkFlowDetailsEntity.setReqWorkFlowDetailsIsActive(true);
			}else{
				requestWorkFlowDetailsEntity.setReqWorkFlowDetailsIsActive(false);
			}
			if (null != requestWorkFlowDetailsVo.getReqWorkFlowDetailsId() ) {
				requestWorkFlowDetailsEntity = findDetailsId(requestWorkFlowDetailsVo.getReqWorkFlowDetailsId(),authDetailsVo);
			}

			try {
				BeanUtils.copyProperties(requestWorkFlowDetailsVo, requestWorkFlowDetailsEntity);
				
				
			} catch (Exception e) {
				Log.info("Request Configuration Service create  Exception",e);
				throw new CommonException(getMessage("beanUtilPropertiesFailure",authDetailsVo));
			}

			requestWorkFlowDetailsEntity.setReqWorkFlowId(requestWorkFlowEntity.getReqWorkFlowId());
			if (null != requestWorkFlowDetailsEntity.getReqWorkFlowDetailsId()) {
				requestWorkFlowDetailsEntity = setUpdateUserDetails(requestWorkFlowDetailsEntity,authDetailsVo);
			}else {
				requestWorkFlowDetailsEntity = setCreateUserDetails(requestWorkFlowDetailsEntity,authDetailsVo);
			}
			try {
				requestWorkFlowDetailsRepository.save(requestWorkFlowDetailsEntity);
			} catch (Exception exe) {
				Log.info("Request Configuration Service create  Exception",exe);
				throw new CommonException(getMessage("dataFailure", authDetailsVo));
			}

		}

		List<RequestWorkFlowExecuterVO>  requestWorkFlowExecuterVoList = requestWorkFlowVo
				.getRequestWorkFlowExecuterVo();

		for (RequestWorkFlowExecuterVO requestWorkFlowExecuterVo : requestWorkFlowExecuterVoList) {

			RequestWorkFlowExecuterEntity requestWorkFlowExecuterEntity = new RequestWorkFlowExecuterEntity();

			requestWorkFlowExecuterVo.setReqWorkFlowId(requestWorkFlowEntity.getReqWorkFlowId());

			try {
				BeanUtils.copyProperties(requestWorkFlowExecuterEntity, requestWorkFlowExecuterVo);
			} catch (Exception e) {
				Log.info("Request Configuration Service create  Exception",e);
				throw new CommonException(getMessage("beanUtilPropertiesFailure", authDetailsVo));
			}

			if(null == requestWorkFlowExecuterVo.getExecuterUserId()){
				requestWorkFlowExecuterEntity.setExecuterUserId(0);
			}
			requestWorkFlowExecuterEntity.setReqWorkFlowId(requestWorkFlowEntity.getReqWorkFlowId());

			if (null == requestWorkFlowExecuterEntity.getReqWorkFlowExecuterId()) {
				requestWorkFlowExecuterEntity = setCreatedUserDetails(requestWorkFlowExecuterEntity, authDetailsVo);
			}
			try {
				if (null != authDetailsVo.getEntityId()) {
					requestWorkFlowExecuterEntity.setEntityLicenseId(authDetailsVo.getEntityId());
				}
				requestWorkFlowExecuterRepository.save(requestWorkFlowExecuterEntity);
			} catch (Exception exe) {
				Log.info("Request Configuration Service create  Exception",exe);
				throw new CommonException(getMessage("dataFailure", authDetailsVo));
			}

			List<RequestWorkFlowSlaVO> requestWorkFlowSlaVoList = requestWorkFlowExecuterVo.getRequestWorkFlowSlaVo();

			for (RequestWorkFlowSlaVO requestWorkFlowSlaVo : requestWorkFlowSlaVoList) {

				RequestWorkFlowSlaEntity requestWorkFlowSlaEntity = new RequestWorkFlowSlaEntity();

				requestWorkFlowSlaVo.setReqWorkFlowId(requestWorkFlowEntity.getReqWorkFlowId());

				requestWorkFlowSlaVo.setReqWorkFlowExecuterId(requestWorkFlowExecuterEntity.getReqWorkFlowExecuterId());

				if (null != requestWorkFlowSlaVo.getReqWorkFlowSlaId()
						&& requestWorkFlowSlaVo.getReqWorkFlowSlaId() != CommonConstant.CONSTANT_ZERO) {
					requestWorkFlowSlaEntity = findSlaId(requestWorkFlowSlaVo.getReqWorkFlowSlaId(), authDetailsVo);
				}
				try {

					BeanUtils.copyProperties(requestWorkFlowSlaEntity, requestWorkFlowSlaVo);

				} catch (Exception exe) {
					Log.info("Request Configuration Service create  Exception",exe);
					throw new CommonException(getMessage("beanUtilPropertiesFailure", authDetailsVo));
				}

				if (null == requestWorkFlowSlaEntity) {
					throw new CommonException(getMessage("errorMessage", authDetailsVo));
				}
				try {

					requestWorkFlowSlaEntity.setReqWorkFlowId(requestWorkFlowEntity.getReqWorkFlowId());

					requestWorkFlowSlaEntity
							.setReqWorkFlowExecuterId(requestWorkFlowExecuterEntity.getReqWorkFlowExecuterId());

					if (null == requestWorkFlowSlaVo.getReqWorkFlowSlaId()) {
						requestWorkFlowSlaEntity = setCreatedUserDetails(requestWorkFlowSlaEntity, authDetailsVo);
					} else {
						requestWorkFlowSlaEntity = setUpdateUserDetails(requestWorkFlowSlaEntity, authDetailsVo);
					}
					if (null != authDetailsVo.getEntityId()) {
						requestWorkFlowSlaEntity.setEntityLicenseId(authDetailsVo.getEntityId());
					}
					
					//Holiday & Weekend flag for executors 
					if(requestWorkFlowVo.isExecutorWeekend()){
						requestWorkFlowSlaEntity.setReqWeekendFlag(true);
					}else{
						requestWorkFlowSlaEntity.setReqWeekendFlag(false);
					}
							
					if(requestWorkFlowVo.isExecutorHoliday()){
						requestWorkFlowSlaEntity.setReqHolidayFlag(true);
					}else{
						requestWorkFlowSlaEntity.setReqHolidayFlag(false);
					}								
					
					requestWorkFlowSlaRepository.save(requestWorkFlowSlaEntity);
				} catch (Exception exe) {
					Log.info("Request Configuration Service create  Exception",exe);
					throw new CommonException(getMessage("dataFailure", authDetailsVo));
				}
			}

		}

		if (null != requestWorkFlowVo && null != requestWorkFlowVo.getRequestWorkFlowSequenceList()) {

			// Set the details information to db
			saveWorkFlowDetailValues(requestWorkFlowVo, requestWorkFlowEntity, authDetailsVo);
		}
		if(null != requestWorkFlowEntity && null != requestWorkFlowEntity.getRequestWorkFlowCode()){
			requestWorkFlowVo.setRequestWorkFlowCode(requestWorkFlowEntity.getRequestWorkFlowCode());
		}
		return requestWorkFlowVo;
	}

	@Transactional
	public RequestWorkFlowVO update(RequestWorkFlowVO requestWorkFlowVo, AuthDetailsVo authDetailsVo) throws CommonException {

		RequestWorkFlowEntity requestWorkFlowEntity = new RequestWorkFlowEntity();
		requestWorkFlowEntity = findId(requestWorkFlowVo.getReqWorkFlowId(), authDetailsVo);

		if (null != requestWorkFlowEntity) {
			if (!(requestWorkFlowVo.getRequestWorkFlowCode().equals(requestWorkFlowEntity.getRequestWorkFlowCode()))) {

				throw new CommonException(getMessage("workFlowCode", authDetailsVo));
			}
		} else {
			requestWorkFlowEntity = new RequestWorkFlowEntity();
		}
		try {

			BeanUtils.copyProperties(requestWorkFlowEntity, requestWorkFlowVo);

		} catch (Exception e) {
			Log.info("Request Configuration Service update Exception",e);
			throw new CommonException(getMessage("beanUtilPropertiesFailure", authDetailsVo));
		}

		try {

			try {
				// Set the values for update details information
				requestWorkFlowEntity = setUpdatedUserDetails(requestWorkFlowEntity, authDetailsVo);

				if (null != authDetailsVo.getEntityId()) {
					requestWorkFlowEntity.setEntityLicenseId(authDetailsVo.getEntityId());
				}

				// Update the value to db
				requestWorkFlowRepository.save(requestWorkFlowEntity);

			} catch (Exception exe) {
				Log.info("Request Configuration Service update Exception",exe);
				throw new CommonException(getMessage("dataFailure", authDetailsVo));
			}
			List<RequestWorkFlowDetailsVO> requestWorkFlowDetailsVoList = requestWorkFlowVo
					.getRequestWorkFlowDetailsVoList();

			for (RequestWorkFlowDetailsVO requestWorkFlowDetailsVo : requestWorkFlowDetailsVoList) {

				RequestWorkFlowDetailsEntity requestWorkFlowDetailsEntity = new RequestWorkFlowDetailsEntity();

				if(null != requestWorkFlowDetailsVo.getWorkFlowDepartmentId()){
					requestWorkFlowDetailsEntity.setWorkFlowDepartmentId(requestWorkFlowDetailsVo.getWorkFlowDepartmentId());
				}
				if(null != requestWorkFlowDetailsVo.getWorkFlowLocationId()){
					requestWorkFlowDetailsEntity.setWorkFlowLocationId(requestWorkFlowDetailsVo.getWorkFlowLocationId());
				}
				if(null != requestWorkFlowDetailsVo.getWorkFlowSublocationId()){
					requestWorkFlowDetailsEntity.setWorkFlowSublocationId(requestWorkFlowDetailsVo.getWorkFlowSublocationId());
				}
				if(requestWorkFlowDetailsVo.isReqWorkFlowDetailsIsActive()){
					requestWorkFlowDetailsEntity.setReqWorkFlowDetailsIsActive(true);
				}else{
					requestWorkFlowDetailsEntity.setReqWorkFlowDetailsIsActive(false);
				}
				if (null != requestWorkFlowDetailsVo.getReqWorkFlowDetailsId() ) {
					requestWorkFlowDetailsEntity = findDetailsId(requestWorkFlowDetailsVo.getReqWorkFlowDetailsId(),authDetailsVo);
				}
				 
			    //BeanUtils.copyProperties(requestWorkFlowDetailsVo, requestWorkFlowDetailsEntity);
				 

				requestWorkFlowDetailsEntity.setReqWorkFlowId(requestWorkFlowEntity.getReqWorkFlowId());
				if (null != requestWorkFlowDetailsEntity.getReqWorkFlowDetailsId() ) {
					requestWorkFlowDetailsEntity = setUpdateUserDetails(requestWorkFlowDetailsEntity,authDetailsVo);
				}else{
					requestWorkFlowDetailsEntity = setCreateUserDetails(requestWorkFlowDetailsEntity,authDetailsVo);
				}
				if(requestWorkFlowDetailsVo.isReqWorkFlowDetailsIsActive()){
					requestWorkFlowDetailsEntity.setReqWorkFlowDetailsIsActive(true);
				}else{
					requestWorkFlowDetailsEntity.setReqWorkFlowDetailsIsActive(false);
				}
				try {
					requestWorkFlowDetailsRepository.save(requestWorkFlowDetailsEntity);
				} catch (Exception exe) {
					Log.info("Request Configuration Service update Exception",exe);
					throw new CommonException(getMessage("dataFailure", authDetailsVo));
				}

			}

			List<RequestWorkFlowExecuterVO> requestWorkFlowExecuterVoList = requestWorkFlowVo
					.getRequestWorkFlowExecuterVo();

			for (RequestWorkFlowExecuterVO requestWorkFlowExecuterVo : requestWorkFlowExecuterVoList) {

				RequestWorkFlowExecuterEntity requestWorkFlowExecuterEntity = new RequestWorkFlowExecuterEntity();

				requestWorkFlowExecuterVo.setReqWorkFlowId(requestWorkFlowEntity.getReqWorkFlowId());

				if (null != requestWorkFlowExecuterVo.getReqWorkFlowExecuterId()) {
					requestWorkFlowExecuterEntity = findExecuterId(requestWorkFlowExecuterVo.getReqWorkFlowExecuterId(),
							authDetailsVo);
				}
				
			 
			    BeanUtils.copyProperties(requestWorkFlowExecuterEntity, requestWorkFlowExecuterVo);
				 
				requestWorkFlowExecuterEntity.setReqWorkFlowId(requestWorkFlowEntity.getReqWorkFlowId());

				if (null != requestWorkFlowExecuterEntity.getReqWorkFlowExecuterId()) {
					requestWorkFlowExecuterEntity = setUpdateUserDetails(requestWorkFlowExecuterEntity, authDetailsVo);
				} else {
					if (null != authDetailsVo.getEntityId()) {
						requestWorkFlowExecuterEntity.setEntityLicenseId(authDetailsVo.getEntityId());
					}
					requestWorkFlowExecuterEntity = setCreatedUserDetails(requestWorkFlowExecuterEntity, authDetailsVo);
				}
				if(null == requestWorkFlowExecuterVo.getExecuterUserId()){
					requestWorkFlowExecuterEntity.setExecuterUserId(0);
				}				
			 
				requestWorkFlowExecuterRepository.save(requestWorkFlowExecuterEntity);
				 

				List<RequestWorkFlowSlaVO> requestWorkFlowSlaVoList = requestWorkFlowExecuterVo
						.getRequestWorkFlowSlaVo();

				for (RequestWorkFlowSlaVO requestWorkFlowSlaVo : requestWorkFlowSlaVoList) {

					RequestWorkFlowSlaEntity requestWorkFlowSlaEntity = new RequestWorkFlowSlaEntity();

					requestWorkFlowSlaVo.setReqWorkFlowId(requestWorkFlowEntity.getReqWorkFlowId());

					requestWorkFlowSlaVo
							.setReqWorkFlowExecuterId(requestWorkFlowExecuterEntity.getReqWorkFlowExecuterId());

					if (null != requestWorkFlowSlaVo.getReqWorkFlowSlaId()) {
						requestWorkFlowSlaEntity = findSlaId(requestWorkFlowSlaVo.getReqWorkFlowSlaId(), authDetailsVo);
					}

				   BeanUtils.copyProperties(requestWorkFlowSlaEntity, requestWorkFlowSlaVo);

					if (null == requestWorkFlowSlaEntity) {
						throw new CommonException(getMessage("errorMessage", authDetailsVo));
					}

					try {
						requestWorkFlowSlaEntity.setReqWorkFlowId(requestWorkFlowEntity.getReqWorkFlowId());

						requestWorkFlowSlaEntity
								.setReqWorkFlowExecuterId(requestWorkFlowExecuterEntity.getReqWorkFlowExecuterId());

						if (null == requestWorkFlowSlaVo.getReqWorkFlowSlaId()) {
							requestWorkFlowSlaEntity = setCreatedUserDetails(requestWorkFlowSlaEntity, authDetailsVo);
							if (null != authDetailsVo.getEntityId()) {
								requestWorkFlowSlaEntity.setEntityLicenseId(authDetailsVo.getEntityId());
							}
						} else {
							requestWorkFlowSlaEntity = setUpdateUserDetails(requestWorkFlowSlaEntity, authDetailsVo);
						}
						
						//Modify Holiday & Weekend flag for executors 					 											
						if(requestWorkFlowVo.isExecutorWeekend()){
							requestWorkFlowSlaEntity.setReqWeekendFlag(true);
						}else{
							requestWorkFlowSlaEntity.setReqWeekendFlag(false);
						}
								
						if(requestWorkFlowVo.isExecutorHoliday()){
							requestWorkFlowSlaEntity.setReqHolidayFlag(true);
						}else{
							requestWorkFlowSlaEntity.setReqHolidayFlag(false);
						}		
												
						requestWorkFlowSlaRepository.save(requestWorkFlowSlaEntity);
					} catch (Exception exe) {
						Log.info("Request Configuration Service update Exception",exe);
						throw new CommonException(getMessage("dataFailure", authDetailsVo));
					}
				}
			}

			if (null != requestWorkFlowVo && null != requestWorkFlowVo.getRequestWorkFlowSequenceList()
					&& requestWorkFlowVo.getRequestWorkFlowSequenceList().size() > 0) {
				// Update the detail information to db
				saveWorkFlowDetailValues(requestWorkFlowVo, requestWorkFlowEntity, authDetailsVo);
			}

		} catch (Exception exe) {
			exe.printStackTrace();
			Log.info("Request Configuration Service update Exception",exe);
			throw new CommonException(getMessage("dataFailure", authDetailsVo));
		}
		if(null != requestWorkFlowEntity && null != requestWorkFlowEntity.getRequestWorkFlowCode()){
			requestWorkFlowVo.setRequestWorkFlowCode(requestWorkFlowEntity.getRequestWorkFlowCode());
		}
		return requestWorkFlowVo;

	}

	@Transactional
	public RequestWorkFlowEntity findId(int id, AuthDetailsVo authDetailsVo) throws CommonException {

		RequestWorkFlowEntity requestWorkFlowEntity = null;
		try {

			// Get the information based on workFlowId
			requestWorkFlowEntity = (RequestWorkFlowEntity) requestWorkFlowRepository.findOne(id);
			return requestWorkFlowEntity;
		} catch (NoResultException e) {
			Log.info("Request Configuration Service findId NoResultException",e);
			throw new CommonException(getMessage("noResultFound", authDetailsVo));
		} catch (NonUniqueResultException e) {
			Log.info("Request Configuration Service findId NonUniqueResultException",e);
			throw new CommonException(getMessage("noUniqueFound", authDetailsVo));
		} catch (Exception e) {
			Log.info("Request Configuration Service findId Exception",e);
			throw new CommonException(getMessage("dataFailure", authDetailsVo));
		}

	}

	@Transactional
	public RequestWorkFlowDetailsEntity findDetailsId(int id, AuthDetailsVo authDetailsVo) throws CommonException {

		RequestWorkFlowDetailsEntity requestWorkFlowDetailsEntity = new RequestWorkFlowDetailsEntity();
		try {
			requestWorkFlowDetailsEntity = requestWorkFlowDetailsRepository.findOne(id);

		} catch (NoResultException e) {
			Log.info("Request Configuration Service findDetailsId NoResultException",e);
			throw new CommonException(getMessage("noResultFound", authDetailsVo));
		} catch (NonUniqueResultException e) {
			Log.info("Request Configuration Service findDetailsId NonUniqueResultException",e);
			throw new CommonException(getMessage("noUniqueFound", authDetailsVo));
		} catch (Exception e) {
			Log.info("Request Configuration Service findDetailsId Exception",e);
			throw new CommonException(getMessage("dataFailure", authDetailsVo));
		}

		if (null == requestWorkFlowDetailsEntity) {
			
			throw new CommonException(getMessage("errorMessage", authDetailsVo));
		}

		return requestWorkFlowDetailsEntity;

	}

	@Transactional
	public RequestWorkFlowExecuterEntity findExecuterId(int id, AuthDetailsVo authDetailsVo) throws CommonException {

		RequestWorkFlowExecuterEntity requestWorkFlowExecuterEntity = new RequestWorkFlowExecuterEntity();
		try {
			requestWorkFlowExecuterEntity = requestWorkFlowExecuterRepository.findOne(id);

		} catch (NoResultException e) {
			Log.info("Request Configuration Service findExecuterId NoResultException",e);
			throw new CommonException(getMessage("noResultFound", authDetailsVo));
		} catch (NonUniqueResultException e) {
			Log.info("Request Configuration Service findExecuterId NonUniqueResultException",e);
			throw new CommonException(getMessage("noUniqueFound", authDetailsVo));
		} catch (Exception e) {
			Log.info("Request Configuration Service findExecuterId Exception",e);
			throw new CommonException(getMessage("dataFailure", authDetailsVo));
		}

		if (null == requestWorkFlowExecuterEntity) {
			throw new CommonException(getMessage("errorMessage", authDetailsVo));
		}

		return requestWorkFlowExecuterEntity;

	}

	@Transactional
	public void saveWorkFlowDetailValues(RequestWorkFlowVO requestWorkFlowVo,
			RequestWorkFlowEntity requestWorkFlowEntity, AuthDetailsVo authDetailsVo) throws CommonException {

		List<RequestWorkFlowSequenceVO> requestWorkFlowSequenceVoList = requestWorkFlowVo
				.getRequestWorkFlowSequenceList();

		for (RequestWorkFlowSequenceVO requestWorkFlowSequenceVo : requestWorkFlowSequenceVoList) {

			RequestWorkFlowSeqEntity requestWorkFlowSeqEntity = new RequestWorkFlowSeqEntity();
			requestWorkFlowSequenceVo.setReqWorkFlowId(requestWorkFlowEntity.getReqWorkFlowId());

			if (null != requestWorkFlowSequenceVo.getReqWorkFlowSeqId()) {
				requestWorkFlowSeqEntity = findDetailId(requestWorkFlowSequenceVo.getReqWorkFlowSeqId(), authDetailsVo);
			}
			try {

				BeanUtils.copyProperties(requestWorkFlowSeqEntity, requestWorkFlowSequenceVo);
			} catch (Exception exe) {
				Log.info("Request Configuration Service saveWorkFlowDetailValues Exception",exe);
				throw new CommonException(getMessage("beanUtilPropertiesFailure", authDetailsVo));
			}

			if (null == requestWorkFlowSeqEntity) {
				throw new CommonException(getMessage("errorMessage", authDetailsVo));
			}

			try {
				requestWorkFlowSeqEntity.setReqWorkFlowId(requestWorkFlowEntity.getReqWorkFlowId());

				if (null == requestWorkFlowSequenceVo.getReqWorkFlowSeqId()) {
					requestWorkFlowSeqEntity = setCreatedUserDetails(requestWorkFlowSeqEntity, authDetailsVo);
					requestWorkFlowSeqEntity.setEntityLicenseId(authDetailsVo.getEntityId());
				} else {
					requestWorkFlowSeqEntity = setUpdateUserDetails(requestWorkFlowSeqEntity, authDetailsVo);
				}
				if (null != requestWorkFlowSequenceVo.getReqWorkFlowSeqLevelType()) {
					requestWorkFlowSeqEntity
							.setReqWorkFlowSeqLevelType(requestWorkFlowSequenceVo.getReqWorkFlowSeqLevelType());
				} else {
					requestWorkFlowSeqEntity.setReqWorkFlowSeqLevelType(CommonConstant.CONSTANT_ZERO);
				}
				requestWorkFlowSequenceRepository.save(requestWorkFlowSeqEntity);

			} catch (Exception exe) {
				Log.info("Request Configuration Service saveWorkFlowDetailValues Exception",exe);
				throw new CommonException(getMessage("dataFailure", authDetailsVo));
			}

			if (null != requestWorkFlowSequenceVo.getReqWorkFlowSeqId()) {
				if (requestWorkFlowSeqEntity.getReqWorkFlowSeqSequence() == 1) {

					if (requestWorkFlowSeqEntity.getReqWorkFlowSeqLevelType() != 1) {

						RequestWorkFlowSeqEntity requestWorkFlowSeq = requestConfigurationDAO
								.findSeqList(requestWorkFlowSeqEntity.getReqWorkFlowId(), authDetailsVo);

						if (requestWorkFlowSeq != null && requestWorkFlowSeq.getReqWorkFlowSeqId() != null) {

							requestConfigurationDAO.modifySequence(requestWorkFlowSeq.getReqWorkFlowSeqId(),
									authDetailsVo);

						}

					}

				}
			}

			List<RequestWorkFlowSlaVO> requestWorkFlowSlaVoList = requestWorkFlowSequenceVo.getRequestWorkFlowSlaVo();

			for (RequestWorkFlowSlaVO requestWorkFlowSlaVo : requestWorkFlowSlaVoList) {

				RequestWorkFlowSlaEntity requestWorkFlowSlaEntity = new RequestWorkFlowSlaEntity();

				requestWorkFlowSlaVo.setReqWorkFlowId(requestWorkFlowEntity.getReqWorkFlowId());

				requestWorkFlowSlaVo.setReqWorkFlowSeqId(requestWorkFlowSeqEntity.getReqWorkFlowSeqId());

				if (null != requestWorkFlowSlaVo.getReqWorkFlowSlaId()) {
					requestWorkFlowSlaEntity = findSlaId(requestWorkFlowSlaVo.getReqWorkFlowSlaId(), authDetailsVo);
				}
				try {

					BeanUtils.copyProperties(requestWorkFlowSlaEntity, requestWorkFlowSlaVo);

				} catch (Exception exe) {
					Log.info("Request Configuration Service saveWorkFlowDetailValues Exception",exe);
					throw new CommonException(getMessage("beanUtilPropertiesFailure", authDetailsVo));
				}

				if (null == requestWorkFlowSlaEntity) {
					throw new CommonException(getMessage("errorMessage", authDetailsVo));
				}
				try {

					requestWorkFlowSlaEntity.setReqWorkFlowId(requestWorkFlowEntity.getReqWorkFlowId());

					requestWorkFlowSlaEntity.setReqWorkFlowSeqId(requestWorkFlowSeqEntity.getReqWorkFlowSeqId());

					if (null == requestWorkFlowSlaVo.getReqWorkFlowSlaId()) {
						requestWorkFlowSlaEntity = setCreatedUserDetails(requestWorkFlowSlaEntity, authDetailsVo);
						requestWorkFlowSlaEntity.setEntityLicenseId(authDetailsVo.getEntityId());
					} else {
						requestWorkFlowSlaEntity = setUpdateUserDetails(requestWorkFlowSlaEntity, authDetailsVo);
					}

					//holiday & executor flag
					if(requestWorkFlowVo.isApproverWeekend()){
						requestWorkFlowSlaEntity.setReqWeekendFlag(true);
					}else{
						requestWorkFlowSlaEntity.setReqWeekendFlag(false);
					}
							
					if(requestWorkFlowVo.isApproverHoliday()){
						requestWorkFlowSlaEntity.setReqHolidayFlag(true);
					}else{
						requestWorkFlowSlaEntity.setReqHolidayFlag(false);
					}		
														
					requestWorkFlowSlaRepository.save(requestWorkFlowSlaEntity);
				} catch (Exception exe) {
					Log.info("Request Configuration Service saveWorkFlowDetailValues Exception",exe);
					throw new CommonException(getMessage("dataFailure", authDetailsVo));
				}
			}

		}

	}

	@Transactional
	public RequestWorkFlowSeqEntity findDetailId(int id, AuthDetailsVo authDetailsVo) throws CommonException {

		RequestWorkFlowSeqEntity requestWorkFlowSeqEntity = null;
		try {
			requestWorkFlowSeqEntity = (RequestWorkFlowSeqEntity) requestWorkFlowSequenceRepository.findOne(id);

		} catch (NoResultException e) {
			Log.info("Request Configuration Service findDetailId NoResultException",e);
			throw new CommonException(getMessage("noResultFound", authDetailsVo));
		} catch (NonUniqueResultException e) {
			Log.info("Request Configuration Service findDetailId NonUniqueResultException",e);
			throw new CommonException(getMessage("noUniqueFound", authDetailsVo));
		} catch (Exception e) {
			Log.info("Request Configuration Service findDetailId Exception",e);
			throw new CommonException(getMessage("dataFailure", authDetailsVo));
		}

		if (null == requestWorkFlowSeqEntity) {
			throw new CommonException(getMessage("errorMessage", authDetailsVo));
		}

		return requestWorkFlowSeqEntity;

	}

	@Transactional
	public RequestWorkFlowSlaEntity findSlaId(int id, AuthDetailsVo authDetailsVo) throws CommonException {

		RequestWorkFlowSlaEntity requestWorkFlowSlaEntity = new RequestWorkFlowSlaEntity();
		try {
			requestWorkFlowSlaEntity = requestWorkFlowSlaRepository.findOne(id);

		} catch (NoResultException e) {
			Log.info("Request Configuration Service findSlaId NoResultException",e);
			throw new CommonException(getMessage("noResultFound", authDetailsVo));
		} catch (NonUniqueResultException e) {
			Log.info("Request Configuration Service findSlaId NonUniqueResultException",e);
			throw new CommonException(getMessage("noUniqueFound", authDetailsVo));
		} catch (Exception e) {
			Log.info("Request Configuration Service findSlaId Exception",e);
			throw new CommonException(getMessage("dataFailure", authDetailsVo));
		}

		if (null == requestWorkFlowSlaEntity) {
			throw new CommonException(getMessage("errorMessage", authDetailsVo));
		}

		return requestWorkFlowSlaEntity;

	}

	@Transactional
	public int saveValidation(RequestWorkFlowVO requestWorkFlowVo, AuthDetailsVo authDetailsVo) {

		int count = 0;

		try {
			count = requestConfigurationDAO.saveValidation(requestWorkFlowVo, authDetailsVo);
		} catch (NoResultException e) {
			Log.info("Request Configuration Service saveValidation NoResultException",e);
			throw new CommonException(getMessage("noResultFound", authDetailsVo));
		} catch (NonUniqueResultException e) {
			Log.info("Request Configuration Service saveValidation NonUniqueResultException",e);
			throw new CommonException(getMessage("noUniqueFound", authDetailsVo));
		} catch (Exception e) {
			Log.info("Request Configuration Service saveValidation Exception",e);
			throw new CommonException(getMessage("dataFailure", authDetailsVo));
		}

		return count;
	}

	private RequestWorkFlowSlaEntity setCreatedUserDetails(RequestWorkFlowSlaEntity requestWorkFlowSlaEntity,
			AuthDetailsVo authDetailsVo) {

		requestWorkFlowSlaEntity.setCreateBy(authDetailsVo.getUserId());
		requestWorkFlowSlaEntity.setCreateDate(CommonConstant.getCalenderDate());
		requestWorkFlowSlaEntity.setUpdateBy(authDetailsVo.getUserId());
		requestWorkFlowSlaEntity.setUpdateDate(CommonConstant.getCalenderDate());
		requestWorkFlowSlaEntity.setDeleteFlag(CommonConstant.FLAG_ZERO);
		return requestWorkFlowSlaEntity;
	}

	@SuppressWarnings("unused")
	private RequestWorkFlowDetailsEntity setCreatedUserDetails(
			RequestWorkFlowDetailsEntity requestWorkFlowDetailsEntity, AuthDetailsVo authDetailsVo) {

		requestWorkFlowDetailsEntity.setCreateBy(authDetailsVo.getUserId());
		requestWorkFlowDetailsEntity.setCreateDate(CommonConstant.getCalenderDate());
		requestWorkFlowDetailsEntity.setUpdateBy(authDetailsVo.getUserId());
		requestWorkFlowDetailsEntity.setUpdateDate(CommonConstant.getCalenderDate());
		requestWorkFlowDetailsEntity.setDeleteFlag(CommonConstant.FLAG_ZERO);
		return requestWorkFlowDetailsEntity;
	}

	/**
	 * This method is used to set , created user and date in
	 * requestWorkFlowExecuterEntity.
	 * 
	 * @param requestWorkFlowExecuterEntity
	 *            RequestWorkFlowExecuterEntity
	 * @return requestWorkFlowExecuterEntity RequestWorkFlowExecuterEntity
	 */
	private RequestWorkFlowExecuterEntity setCreatedUserDetails(
			RequestWorkFlowExecuterEntity requestWorkFlowExecuterEntity, AuthDetailsVo authDetailsVo) {

		requestWorkFlowExecuterEntity.setCreateBy(authDetailsVo.getUserId());
		requestWorkFlowExecuterEntity.setCreateDate(CommonConstant.getCalenderDate());
		requestWorkFlowExecuterEntity.setUpdateBy(authDetailsVo.getUserId());
		requestWorkFlowExecuterEntity.setUpdateDate(CommonConstant.getCalenderDate());
		requestWorkFlowExecuterEntity.setDeleteFlag(CommonConstant.FLAG_ZERO);
		return requestWorkFlowExecuterEntity;
	}

	/**
	 * Method is used to update user details requestWorkFlowSlaEntity.
	 * 
	 * @param requestWorkFlowSlaEntity
	 *            RequestWorkFlowSlaEntity
	 * @return requestWorkFlowSlaEntity RequestWorkFlowSlaEntity
	 */
	private RequestWorkFlowSlaEntity setUpdateUserDetails(RequestWorkFlowSlaEntity requestWorkFlowSlaEntity,
			AuthDetailsVo authDetailsVo) {

		requestWorkFlowSlaEntity.setUpdateBy(authDetailsVo.getUserId());
		requestWorkFlowSlaEntity.setUpdateDate(CommonConstant.getCalenderDate());

		return requestWorkFlowSlaEntity;
	}

	private RequestWorkFlowDetailsEntity setUpdateUserDetails(RequestWorkFlowDetailsEntity requestWorkFlowDetailsEntity,
			AuthDetailsVo authDetailsVo) {

		requestWorkFlowDetailsEntity.setUpdateBy(authDetailsVo.getUserId());
		requestWorkFlowDetailsEntity.setUpdateDate(CommonConstant.getCalenderDate());
		requestWorkFlowDetailsEntity.setDeleteFlag(CommonConstant.FLAG_ZERO);
		requestWorkFlowDetailsEntity.setEntityLicenseId(authDetailsVo.getEntityId());
		return requestWorkFlowDetailsEntity;
	}

	private RequestWorkFlowDetailsEntity setCreateUserDetails(RequestWorkFlowDetailsEntity requestWorkFlowDetailsEntity,
			AuthDetailsVo authDetailsVo) {

		requestWorkFlowDetailsEntity.setUpdateBy(authDetailsVo.getUserId());
		requestWorkFlowDetailsEntity.setUpdateDate(CommonConstant.getCalenderDate());
		requestWorkFlowDetailsEntity.setCreateBy(authDetailsVo.getUserId());
		requestWorkFlowDetailsEntity.setCreateDate(CommonConstant.getCalenderDate());
		requestWorkFlowDetailsEntity.setDeleteFlag(CommonConstant.FLAG_ZERO);
		requestWorkFlowDetailsEntity.setEntityLicenseId(authDetailsVo.getEntityId());
		return requestWorkFlowDetailsEntity;
	}
	
	/**
	 * Method is used to update user details requestWorkFlowExecuterEntity.
	 * 
	 * @param requestWorkFlowExecuterEntity
	 *            RequestWorkFlowExecuterEntity
	 * @return requestWorkFlowExecuterEntity RequestWorkFlowExecuterEntity
	 */
	private RequestWorkFlowExecuterEntity setUpdateUserDetails(
			RequestWorkFlowExecuterEntity requestWorkFlowExecuterEntity, AuthDetailsVo authDetailsVo) {

		requestWorkFlowExecuterEntity.setUpdateBy(authDetailsVo.getUserId());
		requestWorkFlowExecuterEntity.setUpdateDate(CommonConstant.getCalenderDate());

		return requestWorkFlowExecuterEntity;
	}

	/**
	 * Method is used to created the user detail
	 * 
	 * @param requestWorkFlowSeqEntity
	 *            RequestWorkFlowSeqEntity
	 * @return requestWorkFlowSeqEntity RequestWorkFlowSeqEntity
	 */
	private RequestWorkFlowSeqEntity setCreatedUserDetails(RequestWorkFlowSeqEntity requestWorkFlowSeqEntity,
			AuthDetailsVo authDetailsVo) {

		requestWorkFlowSeqEntity.setCreateBy(authDetailsVo.getUserId());
		requestWorkFlowSeqEntity.setCreateDate(CommonConstant.getCalenderDate());
		requestWorkFlowSeqEntity.setUpdateBy(authDetailsVo.getUserId());
		requestWorkFlowSeqEntity.setUpdateDate(CommonConstant.getCalenderDate());
		requestWorkFlowSeqEntity.setDeleteFlag(CommonConstant.FLAG_ZERO);
		return requestWorkFlowSeqEntity;
	}

	/**
	 * This method is used to set, updated user and date in
	 * requestWorkFlowSeqEntity.
	 *
	 *
	 * @param RequestWorkFlowSeqEntity
	 *            requestWorkFlowSeqEntity
	 * @return RequestWorkFlowSeqEntity requestWorkFlowSeqEntity
	 */
	private RequestWorkFlowSeqEntity setUpdateUserDetails(RequestWorkFlowSeqEntity requestWorkFlowSeqEntity,
			AuthDetailsVo authDetailsVo) {

		requestWorkFlowSeqEntity.setUpdateBy(authDetailsVo.getUserId());
		requestWorkFlowSeqEntity.setUpdateDate(CommonConstant.getCalenderDate());

		return requestWorkFlowSeqEntity;
	}

	/**
	 * This method is used to set ,created user and date in requestWorkFlow.
	 *
	 *
	 * @param RequestWorkFlowEntity
	 *            requestWorkFlowEntity
	 * @return RequestWorkFlowEntity requestWorkFlowEntity
	 */
	private RequestWorkFlowEntity setCreateUserDetails(RequestWorkFlowEntity requestWorkFlowEntity,
			AuthDetailsVo authDetailsVo) {

		requestWorkFlowEntity.setCreateBy(authDetailsVo.getUserId());
		requestWorkFlowEntity.setCreateDate(CommonConstant.getCalenderDate());
		requestWorkFlowEntity.setUpdateBy(authDetailsVo.getUserId());
		requestWorkFlowEntity.setUpdateDate(CommonConstant.getCalenderDate());
		requestWorkFlowEntity.setDeleteFlag(CommonConstant.FLAG_ZERO);
		return requestWorkFlowEntity;
	}

	/**
	 * This method is used to set ,created user and date in
	 * requestWorkflowAudit.
	 * 
	 * @param requestWorkFlowAuditEntity
	 *            RequestWorkFlowAuditEntity
	 * @return requestWorkFlowAuditEntity RequestWorkFlowAuditEntity
	 */
	private RequestWorkFlowAuditEntity setCreateUserDetails(RequestWorkFlowAuditEntity requestWorkFlowAuditEntity,
			AuthDetailsVo authDetailsVo) {

		if (null != authDetailsVo.getUserId()) {
			requestWorkFlowAuditEntity.setCreateBy(authDetailsVo.getUserId());
			requestWorkFlowAuditEntity.setUpdateBy(authDetailsVo.getUserId());
		}
		requestWorkFlowAuditEntity.setCreateDate(CommonConstant.getCalenderDate());
		requestWorkFlowAuditEntity.setUpdateDate(CommonConstant.getCalenderDate());
		requestWorkFlowAuditEntity.setDeleteFlag(CommonConstant.FLAG_ZERO);
		return requestWorkFlowAuditEntity;
	}

	/**
	 * This method is used to set,updated user and date in request work flow.
	 *
	 * @param RequestWorkFlowEntity
	 *            requestWorkFlowEntity
	 * @Return RequestWorkFlowEntity requestWorkFlowEntity
	 */
	private RequestWorkFlowEntity setUpdatedUserDetails(RequestWorkFlowEntity requestWorkFlowEntity,
			AuthDetailsVo authDetailsVo) {

		requestWorkFlowEntity.setUpdateBy(authDetailsVo.getUserId());
		requestWorkFlowEntity.setUpdateDate(CommonConstant.getCalenderDate());

		return requestWorkFlowEntity;
	}

	/**
	 * This method is used to set,updated user and date in request
	 * workFlowAudit.
	 * 
	 * @param requestWorkFlowAuditEntity
	 *            RequestWorkFlowAuditEntity
	 * @return requestWorkFlowAuditEntity RequestWorkFlowAuditEntity
	 */
	@SuppressWarnings("unused")
	private RequestWorkFlowAuditEntity setUpdatedUserDetails(RequestWorkFlowAuditEntity requestWorkFlowAuditEntity,
			AuthDetailsVo authDetailsVo) {

		requestWorkFlowAuditEntity.setUpdateBy(authDetailsVo.getUserId());
		requestWorkFlowAuditEntity.setUpdateDate(CommonConstant.getCalenderDate());

		return requestWorkFlowAuditEntity;
	}

	@Transactional
	public void modifyDelete(RequestWorkFlowVO requestWorkFlowVo, AuthDetailsVo authDetailsVo) throws CommonException {

		int id = requestWorkFlowVo.getRequestWorkFlowExecuterVo().get(0).getReqWorkFlowExecuterId();

		try {
			// Put the deleteFlag 1 to db
			requestConfigurationDAO.modifyDelete(id, authDetailsVo);
		} catch (Exception exe) {
			Log.info("Request Configuration Service modifyDelete Exception",exe);
			throw new CommonException(getMessage("modifyFailure", authDetailsVo));
		}

	}

	@Transactional
	public void detailValidation(RequestWorkFlowVO requestWorkFlowVo, AuthDetailsVo authDetailsVo)
			throws CommonException {

		if (requestWorkFlowVo.getRequestWorkFlowDetailsVoList() != null) {
			List<RequestWorkFlowDetailsVO> requestWorkFlowDetailsVo = requestWorkFlowVo
					.getRequestWorkFlowDetailsVoList();

			for (RequestWorkFlowDetailsVO requestWorkFlowDetails : requestWorkFlowDetailsVo) {

				if (requestWorkFlowDetails.getRequestWorkFlowDetailsVoList() != null) {
					for (RequestWorkFlowDetailsVO requestWorkFlowDetail : requestWorkFlowDetails
							.getRequestWorkFlowDetailsVoList()) {

						if (requestWorkFlowDetail.isReqWorkFlowDetailsIsActive()) {

							if (requestWorkFlowDetail.getWorkFlowLocationId() == requestWorkFlowDetails
									.getWorkFlowLocationId()
									&& requestWorkFlowDetail.getWorkFlowSublocationId() == requestWorkFlowDetails
											.getWorkFlowSublocationId()
									&& requestWorkFlowDetail.getWorkFlowDepartmentId() == requestWorkFlowDetails
											.getWorkFlowDepartmentId()) {

								throw new CommonException(getMessage("rc.duplicate.add", authDetailsVo));
							}
							if (requestWorkFlowDetail.getWorkFlowLocationId() == requestWorkFlowDetails
									.getWorkFlowLocationId()
									&& requestWorkFlowDetail.getWorkFlowSublocationId() == requestWorkFlowDetails
											.getWorkFlowSublocationId()
									&& requestWorkFlowDetail.getWorkFlowDepartmentId() == CommonConstant.CONSTANT_ZERO
									&& requestWorkFlowDetails
											.getWorkFlowDepartmentId() != CommonConstant.CONSTANT_ZERO) {
								throw new CommonException(getMessage("rc.all.dept", authDetailsVo));
							}
							if (requestWorkFlowDetail.getWorkFlowLocationId() == requestWorkFlowDetails
									.getWorkFlowLocationId()
									&& requestWorkFlowDetail.getWorkFlowSublocationId() == CommonConstant.CONSTANT_ZERO
									&& requestWorkFlowDetails.getWorkFlowSublocationId() != CommonConstant.CONSTANT_ZERO
									&& requestWorkFlowDetail.getWorkFlowDepartmentId() == requestWorkFlowDetails
											.getWorkFlowDepartmentId()) {
								throw new CommonException(getMessage("rc.all.sub", authDetailsVo));
							}
							if (requestWorkFlowDetail.getWorkFlowLocationId() == requestWorkFlowDetails
									.getWorkFlowLocationId()
									&& requestWorkFlowDetail.getWorkFlowSublocationId() == CommonConstant.CONSTANT_ZERO
									&& requestWorkFlowDetails.getWorkFlowSublocationId() != CommonConstant.CONSTANT_ZERO
									&& requestWorkFlowDetail.getWorkFlowDepartmentId() == CommonConstant.CONSTANT_ZERO
									&& requestWorkFlowDetails
											.getWorkFlowDepartmentId() != CommonConstant.CONSTANT_ZERO) {
								throw new CommonException(getMessage("rc.all.sub", authDetailsVo));
							}
							if (requestWorkFlowDetail.getWorkFlowLocationId() == CommonConstant.CONSTANT_ZERO
									&& requestWorkFlowDetails.getWorkFlowLocationId() != CommonConstant.CONSTANT_ZERO
									&& requestWorkFlowDetail.getWorkFlowSublocationId() == requestWorkFlowDetails
											.getWorkFlowSublocationId()
									&& requestWorkFlowDetail.getWorkFlowDepartmentId() == requestWorkFlowDetails
											.getWorkFlowDepartmentId()) {
								throw new CommonException(getMessage("rc.all.loc", authDetailsVo));
							}
							if (requestWorkFlowDetail.getWorkFlowLocationId() == CommonConstant.CONSTANT_ZERO
									&& requestWorkFlowDetails.getWorkFlowLocationId() != CommonConstant.CONSTANT_ZERO
									&& requestWorkFlowDetail.getWorkFlowSublocationId() == CommonConstant.CONSTANT_ZERO
									&& requestWorkFlowDetails.getWorkFlowSublocationId() != CommonConstant.CONSTANT_ZERO
									&& requestWorkFlowDetail.getWorkFlowDepartmentId() == CommonConstant.CONSTANT_ZERO
									&& requestWorkFlowDetails
											.getWorkFlowDepartmentId() != CommonConstant.CONSTANT_ZERO) {
								throw new CommonException(getMessage("rc.all.loc", authDetailsVo));
							}

						}
					}
				}

			}
		}

	}

	/**
	 * This method is used to create the workFlow of Request Configuration based
	 * on request id.
	 * 
	 * @param reqBookId
	 *            int @throws CommonException @throws
	 */
	@Transactional
	public void createWorkFlow(int requestId, RequestVO requestVo, AuthDetailsVo authDetailsVo) throws CommonException , Exception {

		try {
		
		RequestEntity requestEntity = new RequestEntity();

		List<RequestWorkFlowSequenceVO> requestWorkFlowSequenceVoList = new ArrayList<RequestWorkFlowSequenceVO>();

		List<RequestWorkFlowExecuterVO> requestWorkFlowExecuterVoList = new ArrayList<RequestWorkFlowExecuterVO>();

		requestEntity = requestRepository.findOne(requestId);
 
		RequestWorkFlowVO requestWorkFlowVo = new RequestWorkFlowVO();

		getSla(requestEntity,requestVo,authDetailsVo,requestWorkFlowVo);
			
		getWorflowSeqSla(requestWorkFlowVo,requestEntity,authDetailsVo,requestWorkFlowSequenceVoList);
				
		getWorkflowExeSla(authDetailsVo,requestWorkFlowVo,requestEntity,
				requestWorkFlowExecuterVoList);
						
		createWorkflowByUser(requestWorkFlowVo, requestWorkFlowSequenceVoList, requestEntity, requestId,
				requestWorkFlowExecuterVoList, requestVo, authDetailsVo);

		} catch (CommonException exe) {
			Log.info("Request Configuration Service createWorkFlow Common Exception",exe);
			throw new CommonException(exe.getMessage());
		} catch (Exception exe) {
			Log.info("Request Configuration Service createWorkFlow Exception",exe);
			throw new CommonException("beanUtilPropertiesFailure");
		}
		
	}

	@Transactional
	public void getSla(RequestEntity requestEntity, RequestVO requestVo ,AuthDetailsVo authDetailsVo , RequestWorkFlowVO requestWorkFlowVo  ) throws CommonException ,Exception {
		try {
			List<Object[]> object = new ArrayList<Object[]>();
			if(null != requestVo.getForwardRequestId()){
							 
			object = requestConfigurationDAO.findbyRequestSla(requestEntity, authDetailsVo);
			}else{
				object = requestConfigurationDAO.findbyRequestSla(requestEntity, authDetailsVo);
			}
			
			if (object.size() == CommonConstant.CONSTANT_ZERO) {
				throw new CommonException("requestWorkflowNotAvailable");

			}
			if (!object.isEmpty()) {
				BeanUtils.copyProperties(requestWorkFlowVo, object.get(0)[0]);
				requestWorkFlowVo.setSlaType((int) object.get(0)[1]);
				requestWorkFlowVo.setWorkFlowSla((float) object.get(0)[2]);
			}

		} catch (CommonException exe) {
			Log.info("Request Configuration Service getSla Common Exception",exe);
			throw new CommonException(exe.getMessage());
		} catch (Exception exe) {
			Log.info("Request Configuration Service getSla Exception",exe);
			throw new CommonException("beanUtilPropertiesFailure");
		}
		
	}
		
	
	public void getWorflowSeqSla(RequestWorkFlowVO requestWorkFlowVo, RequestEntity requestEntity,
			AuthDetailsVo authDetailsVo, List<RequestWorkFlowSequenceVO> requestWorkFlowSequenceVoList) throws CommonException , Exception {

		List<Object[]> workFlowSeqEntity = null;
		try {
			workFlowSeqEntity = requestConfigurationDAO.findWorkFlowSeqSla(requestWorkFlowVo.getReqWorkFlowId(),
					requestEntity.getRequestPriority(), authDetailsVo);

			if (workFlowSeqEntity != null) {
				for (Object[] object1 : workFlowSeqEntity) {

					RequestWorkFlowSequenceVO requestWorkFlowSequenceVo = new RequestWorkFlowSequenceVO();

					BeanUtils.copyProperties(requestWorkFlowSequenceVo, object1[0]);
					requestWorkFlowSequenceVo.setSlaType((int) object1[1]);
					requestWorkFlowSequenceVo.setWorkFlowSla((float) object1[2]);									
					
					if ((boolean) object1[3] == true) {
						requestWorkFlowSequenceVo.setApproverWeekend(true);
					} else {
						requestWorkFlowSequenceVo.setApproverWeekend(false);
					}

					if ((boolean) object1[4] == true) {
						requestWorkFlowSequenceVo.setApproverHoliday(true);
					} else {
						requestWorkFlowSequenceVo.setApproverHoliday(false);
					}
								
					requestWorkFlowSequenceVoList.add(requestWorkFlowSequenceVo);

				}
			}

		} catch (Exception exe) {
			Log.info("Request Configuration Service getWorflowSeqSla Exception",exe);
			throw new CommonException("dataFailure");
		}
	}
	
 		
	public void getWorkflowExeSla(AuthDetailsVo authDetailsVo, RequestWorkFlowVO requestWorkFlowVo,
			RequestEntity requestEntity, List<RequestWorkFlowExecuterVO> requestWorkFlowExecuterVoList) throws CommonException , Exception{

		List<Object[]> workFlowExecuterEntity = null;

		try {
		/*	if (null != requestVo.getSubrequestId()) {
				workFlowExecuterEntity = requestConfigurationDAO.findRedirectWorkFlowExeSla(
						requestWorkFlowVo.getReqWorkFlowId(), requestEntity.getRequestPriority(), requestVo);
			} else {*/
				workFlowExecuterEntity = requestConfigurationDAO.findWorkFlowExeSla(
						requestWorkFlowVo.getReqWorkFlowId(), requestEntity.getRequestPriority(), authDetailsVo);
			//}

			if (workFlowExecuterEntity != null) {
				for (Object[] object1 : workFlowExecuterEntity) {

					RequestWorkFlowExecuterVO requestWorkFlowExecuterVo = new RequestWorkFlowExecuterVO();

					BeanUtils.copyProperties(requestWorkFlowExecuterVo, object1[0]);
					requestWorkFlowExecuterVo.setSlaType((int) object1[1]);
					requestWorkFlowExecuterVo.setWorkFlowSla((float) object1[2]);

					if ((boolean) object1[3] == true) {
						requestWorkFlowExecuterVo.setExecutorWeekend(true);
					} else {
						requestWorkFlowExecuterVo.setExecutorWeekend(false);
					}

					if ((boolean) object1[4] == true) {
						requestWorkFlowExecuterVo.setExecutorHoliday(true);
					} else {
						requestWorkFlowExecuterVo.setExecutorWeekend(false);
					}

					requestWorkFlowExecuterVoList.add(requestWorkFlowExecuterVo);

					if (requestWorkFlowExecuterVoList.size() == 0) {
						throw new CommonException("request_validation_executersNotAvailble");
					}

				}
			}
		} catch (CommonException exe) {
			Log.info("Request Configuration Service getWorkflowExeSla Common Exception",exe);
			throw new CommonException(exe.getMessage());
		} catch (Exception exe) {
			Log.info("Request Configuration Service getWorkflowExeSla Exception",exe);
			throw new CommonException("dataFailure");
		}		
	}
	
	
	
	/**
	 * This method used to workFlow for user request configuration based on the
	 * parameter, it can store in audit db.
	 * 
	 * @param requestWorkFlowEntity
	 *            RequestWorkFlowEntity
	 * @param requestWorkFlowSequenceVoList
	 *            List<RequestWorkFlowSequenceVo>
	 * @param requestEntity
	 *            RequestEntity
	 * @param requestId
	 *            int
	 * @throws Exception 
	 */
	@Transactional
	private void createWorkflowByUser(RequestWorkFlowVO requestWorkFlowEntity,
			List<RequestWorkFlowSequenceVO> requestWorkFlowSequenceVoList, RequestEntity requestEntity, int requestId,
			List<RequestWorkFlowExecuterVO> requestWorkFlowExecuterVoList, RequestVO requestVo,
			AuthDetailsVo authDetailsVo) throws CommonException , Exception {

		
		try{
		if (null != authDetailsVo.getEntityId()) {
			requestVo.setEntityId(authDetailsVo.getEntityId());
			requestVo.setUserId(authDetailsVo.getUserId());
		}

		int i = 0;

		int executerUser = 0;
		int minutes;
		localcount = CommonConstant.CONSTANT_ZERO;
		hierachy = CommonConstant.CONSTANT_ZERO;
		count = CommonConstant.CONSTANT_ZERO;

		if (requestWorkFlowSequenceVoList.size() > 0 && !requestWorkFlowSequenceVoList.isEmpty()) {
			for (RequestWorkFlowSequenceVO requestWorkFlowSequenceVo : requestWorkFlowSequenceVoList) {

				if (requestWorkFlowSequenceVo.getReqWorkFlowSeqSequence() == CommonConstant.CONSTANT_ONE) {

					if (requestWorkFlowSequenceVo.getReqWorkFlowSeqLevelType() == CommonConstant.CONSTANT_ONE) {
							List<UserMappingVO> userMappingEntityList = new ArrayList<>();

							userMappingEntityList = requestConfigurationDAO
									.basedOnUserDetails(requestEntity.getCreateBy(), authDetailsVo);

							if (userMappingEntityList.isEmpty()) {
								throw new CommonException(getMessage("approvalNotAvailable", authDetailsVo));
						   }
						if (userMappingEntityList.size() > CommonConstant.CONSTANT_ONE) {
							count = count + CommonConstant.CONSTANT_ONE;
						} else {
							count = CommonConstant.CONSTANT_ZERO;
						}

						for (UserMappingVO userMappingEntity : userMappingEntityList) {

							RequestWorkFlowAuditEntity requestWorkFlowAuditEntity = new RequestWorkFlowAuditEntity();

							if (null != userMappingEntity.getReportingToUser()) {

								requestWorkFlowAuditEntity.setWorkFlowId(requestWorkFlowSequenceVo.getReqWorkFlowId());
								requestWorkFlowAuditEntity.setSeqId(requestWorkFlowSequenceVo.getReqWorkFlowSeqId());
								requestWorkFlowAuditEntity.setRequestId(requestId);
								requestWorkFlowAuditEntity.setUserId(userMappingEntity.getReportingToUser());
								requestWorkFlowAuditEntity.setSequence(++localcount);
								requestWorkFlowAuditEntity.setApprovalExecuter(CommonConstant.CONSTANT_ONE);
								requestWorkFlowAuditEntity.setGroupId(count);
								if (null != authDetailsVo.getEntityId()) {
									requestWorkFlowAuditEntity.setEntityLicenseId(authDetailsVo.getEntityId());
								} else {
									requestWorkFlowAuditEntity.setEntityLicenseId(requestEntity.getEntityLicenseId());
								}
								requestWorkFlowAuditEntity.setRequestWorkflowAuditIsActive(CommonConstant.BOOLEAN_TRUE);
								
								boolean weekendFlag  = false;
								boolean holidayFlag =  false;
								
								if(requestWorkFlowSequenceVo.isApproverWeekend()){
									weekendFlag = true;
								}
								
								if(requestWorkFlowSequenceVo.isApproverHoliday()){
									holidayFlag = true;
								}			
								
								UserMasterVO userMasterVo = getEmailAddress(userMappingEntity.getReportingToUser(),authDetailsVo);
																							
								minutes = calculateSLATime(requestWorkFlowSequenceVo.getSlaType(),
										requestWorkFlowSequenceVo.getWorkFlowSla(),weekendFlag,holidayFlag,
										userMasterVo.getUserLocation(), userMasterVo.getSubLocation(),authDetailsVo);
																 					
								requestWorkFlowAuditEntity.setRequestWorkflowAuditSla(minutes);
							 
								requestWorkFlowAuditEntity.setAuditForwardRemarks(requestVo.getForwardRedirectRemarks());
								
								saveAudit(requestWorkFlowAuditEntity, authDetailsVo,requestVo);
								 
							}
						}

					} else if (requestWorkFlowSequenceVo.getReqWorkFlowSeqLevelType() == CommonConstant.CONSTANT_TWO) {

						List<Integer> userId = getListOfSalesProject(requestEntity.getCreateBy(), authDetailsVo);
						
						if(userId.isEmpty()){
							throw new CommonException(getMessage("approvalNotAvailable", authDetailsVo));
						}
						localcount = CommonConstant.CONSTANT_ZERO;
						for (int reportingId : userId) {

							RequestWorkFlowAuditEntity requestWorkFlowAuditEntity = new RequestWorkFlowAuditEntity();

							if (CommonConstant.CONSTANT_ZERO != reportingId) {

								requestWorkFlowAuditEntity.setWorkFlowId(requestWorkFlowSequenceVo.getReqWorkFlowId());
								requestWorkFlowAuditEntity.setSeqId(requestWorkFlowSequenceVo.getReqWorkFlowSeqId());
								requestWorkFlowAuditEntity.setRequestId(requestId);
								requestWorkFlowAuditEntity.setUserId(reportingId);
								requestWorkFlowAuditEntity.setSequence(++localcount);
								requestWorkFlowAuditEntity.setApprovalExecuter(CommonConstant.CONSTANT_ONE);
								requestWorkFlowAuditEntity.setGroupId(CommonConstant.CONSTANT_ZERO);
								if (null != authDetailsVo.getEntityId()) {
									requestWorkFlowAuditEntity.setEntityLicenseId(authDetailsVo.getEntityId());
								} else {
									requestWorkFlowAuditEntity.setEntityLicenseId(requestEntity.getEntityLicenseId());
								}
								requestWorkFlowAuditEntity.setRequestWorkflowAuditIsActive(CommonConstant.BOOLEAN_TRUE);
								
								boolean weekendFlag  = false;
								boolean holidayFlag =  false;
								
								if(requestWorkFlowSequenceVo.isApproverWeekend()){
									weekendFlag = true;
								}
								
								if(requestWorkFlowSequenceVo.isApproverHoliday()){
									holidayFlag = true;
								}		
								
								UserMasterVO userMasterVo = getEmailAddress(reportingId,authDetailsVo);
								
								minutes = calculateSLATime(requestWorkFlowSequenceVo.getSlaType(),
										requestWorkFlowSequenceVo.getWorkFlowSla(), weekendFlag ,
										holidayFlag ,userMasterVo.getUserLocation(), userMasterVo.getSubLocation(),authDetailsVo);
								requestWorkFlowAuditEntity.setRequestWorkflowAuditSla(minutes);
								requestWorkFlowAuditEntity.setAuditForwardRemarks(requestVo.getForwardRedirectRemarks()); 
								
								saveAudit(requestWorkFlowAuditEntity, authDetailsVo,requestVo);
								 
							}
						}

					} else
						if (requestWorkFlowSequenceVo.getReqWorkFlowSeqLevelType() == CommonConstant.CONSTANT_THREE) {

						List<Integer> userId = getListOfSalesProject(requestEntity.getCreateBy(), authDetailsVo);

						if(userId.isEmpty()){
							throw new CommonException(getMessage("approvalNotAvailable", authDetailsVo));
						}
						localcount = CommonConstant.CONSTANT_ZERO;
						for (int reportingId : userId) {

							RequestWorkFlowAuditEntity requestWorkFlowAuditEntity = new RequestWorkFlowAuditEntity();

							if (0 != reportingId) {

								requestWorkFlowAuditEntity.setWorkFlowId(requestWorkFlowSequenceVo.getReqWorkFlowId());
								requestWorkFlowAuditEntity.setSeqId(requestWorkFlowSequenceVo.getReqWorkFlowSeqId());
								requestWorkFlowAuditEntity.setRequestId(requestId);
								requestWorkFlowAuditEntity.setUserId(reportingId);
								requestWorkFlowAuditEntity.setSequence(++localcount);
								requestWorkFlowAuditEntity.setApprovalExecuter(CommonConstant.CONSTANT_ONE);
								if (null != authDetailsVo.getEntityId()) {
									requestWorkFlowAuditEntity.setEntityLicenseId(authDetailsVo.getEntityId());
								} else {
									requestWorkFlowAuditEntity.setEntityLicenseId(requestEntity.getEntityLicenseId());
								}
								requestWorkFlowAuditEntity.setGroupId(CommonConstant.CONSTANT_ZERO);
								requestWorkFlowAuditEntity.setRequestWorkflowAuditIsActive(CommonConstant.BOOLEAN_TRUE);
								
								boolean weekendFlag  = false;
								boolean holidayFlag =  false;
								
								if(requestWorkFlowSequenceVo.isApproverWeekend()){
									weekendFlag = true;
								}
								
								if(requestWorkFlowSequenceVo.isApproverHoliday()){
									holidayFlag = true;
								}		
								
								UserMasterVO userMasterVo = getEmailAddress(reportingId,authDetailsVo);
																
								minutes = calculateSLATime(requestWorkFlowSequenceVo.getSlaType(),
										requestWorkFlowSequenceVo.getWorkFlowSla(),weekendFlag,holidayFlag ,
										userMasterVo.getUserLocation(), userMasterVo.getSubLocation(),authDetailsVo);
								
								requestWorkFlowAuditEntity.setRequestWorkflowAuditSla(minutes);
								requestWorkFlowAuditEntity.setAuditForwardRemarks(requestVo.getForwardRedirectRemarks());
								
								saveAudit(requestWorkFlowAuditEntity, authDetailsVo,requestVo);
							
								hierachy++;
								if (hierachy == requestWorkFlowSequenceVo.getReqWorkFlowSeqLevelhierarchy()) {
									break;
								}
							}
						}

					}

				} 
				if ((requestWorkFlowSequenceVo.getReqWorkFlowSeqSequence() == CommonConstant.CONSTANT_TWO
						|| requestWorkFlowSequenceVo.getReqWorkFlowSeqSequence() == CommonConstant.CONSTANT_ONE)
						&& requestWorkFlowSequenceVo.getReqWorkFlowSeqLevelType() == CommonConstant.CONSTANT_FOUR) {

					List<Integer> userId = getDepartmentUser(requestEntity.getCreateBy(), authDetailsVo);
					if(userId.isEmpty()){
						throw new CommonException(getMessage("departmentNotAvailable", authDetailsVo));
					}
					if (userId.size() > 0) {

						RequestWorkFlowAuditEntity requestWorkFlowAuditEntity = new RequestWorkFlowAuditEntity();

						requestWorkFlowAuditEntity.setWorkFlowId(requestWorkFlowSequenceVo.getReqWorkFlowId());
						requestWorkFlowAuditEntity.setSeqId(requestWorkFlowSequenceVo.getReqWorkFlowSeqId());
						requestWorkFlowAuditEntity.setRequestId(requestId);
						requestWorkFlowAuditEntity.setUserId(userId.get(userId.size() - 1));
						requestWorkFlowAuditEntity.setSequence(++localcount);
						requestWorkFlowAuditEntity.setApprovalExecuter(CommonConstant.CONSTANT_ONE);
						if (null != authDetailsVo.getEntityId()) {
							requestWorkFlowAuditEntity.setEntityLicenseId(authDetailsVo.getEntityId());
						} else {
							requestWorkFlowAuditEntity.setEntityLicenseId(requestEntity.getEntityLicenseId());
						}
						requestWorkFlowAuditEntity.setGroupId(CommonConstant.CONSTANT_ZERO);
						requestWorkFlowAuditEntity.setRequestWorkflowAuditIsActive(CommonConstant.BOOLEAN_TRUE);
					
						boolean weekendFlag  = false;
						boolean holidayFlag =  false;
						
						if(requestWorkFlowSequenceVo.isApproverWeekend()){
							weekendFlag = true;
						}
						
						if(requestWorkFlowSequenceVo.isApproverHoliday()){
							holidayFlag = true;
						}		
												
						UserMasterVO userMasterVo = getEmailAddress(requestWorkFlowAuditEntity.getUserId(),authDetailsVo);
						
						minutes = calculateSLATime(requestWorkFlowSequenceVo.getSlaType(),
								requestWorkFlowSequenceVo.getWorkFlowSla(),weekendFlag,holidayFlag , 
								userMasterVo.getUserLocation(), userMasterVo.getSubLocation(),authDetailsVo);
						
						requestWorkFlowAuditEntity.setRequestWorkflowAuditSla(minutes);
						requestWorkFlowAuditEntity.setAuditForwardRemarks(requestVo.getForwardRedirectRemarks()); 
						
						saveAudit(requestWorkFlowAuditEntity, authDetailsVo,requestVo);
					 
					} else {
						throw new CommonException(getMessage("rc.depthead.config", authDetailsVo));
					}
				}
				if (requestWorkFlowSequenceVo.getReqWorkFlowSeqSequence() >= CommonConstant.CONSTANT_ONE) {

					if (requestWorkFlowSequenceVo.getReqWorkFlowSeqLevelType() == CommonConstant.CONSTANT_ZERO) {

						i++;

						if (requestWorkFlowSequenceVo.getUserId() == CommonConstant.CONSTANT_ZERO) {

							List<UserEntity> userEntityList = requestConfigurationDAO
									.findSeqUserId(requestWorkFlowSequenceVo, authDetailsVo);

							if (userEntityList.size() > CommonConstant.CONSTANT_ONE
									&& count == CommonConstant.CONSTANT_ZERO) {
								count = count + CommonConstant.CONSTANT_ONE;
							} else if (userEntityList.size() > CommonConstant.CONSTANT_ONE) {
								count = count + CommonConstant.CONSTANT_TWO;
							} else {
								count = CommonConstant.CONSTANT_ZERO;
							}

							for (UserEntity userEntity : userEntityList) {

								RequestWorkFlowAuditEntity requestWorkFlowAuditEntity = new RequestWorkFlowAuditEntity();

								requestWorkFlowAuditEntity.setWorkFlowId(requestWorkFlowSequenceVo.getReqWorkFlowId());
								requestWorkFlowAuditEntity.setSeqId(requestWorkFlowSequenceVo.getReqWorkFlowSeqId());
								requestWorkFlowAuditEntity.setRequestId(requestId);
								requestWorkFlowAuditEntity.setUserId(userEntity.getId());
								requestWorkFlowAuditEntity.setSequence(++localcount);
								requestWorkFlowAuditEntity.setApprovalExecuter(CommonConstant.CONSTANT_ONE);
								if (null != authDetailsVo.getEntityId()) {
									requestWorkFlowAuditEntity.setEntityLicenseId(authDetailsVo.getEntityId());
								} else {
									requestWorkFlowAuditEntity.setEntityLicenseId(requestEntity.getEntityLicenseId());
								}
								requestWorkFlowAuditEntity.setGroupId(count);
								requestWorkFlowAuditEntity.setRequestWorkflowAuditIsActive(CommonConstant.BOOLEAN_TRUE);
								
								boolean weekendFlag  = false;
								boolean holidayFlag =  false;
								
								if(requestWorkFlowSequenceVo.isApproverWeekend()){
									weekendFlag = true;
								}
								
								if(requestWorkFlowSequenceVo.isApproverHoliday()){
									holidayFlag = true;
								}		
												
								UserMasterVO userMasterVo = getEmailAddress(requestWorkFlowAuditEntity.getUserId(),authDetailsVo);
								
								minutes = calculateSLATime(requestWorkFlowSequenceVo.getSlaType(),
										requestWorkFlowSequenceVo.getWorkFlowSla(),weekendFlag,holidayFlag ,
										userMasterVo.getUserLocation(), userMasterVo.getSubLocation(),authDetailsVo);
																
								requestWorkFlowAuditEntity.setRequestWorkflowAuditSla(minutes);		
								requestWorkFlowAuditEntity.setAuditForwardRemarks(requestVo.getForwardRedirectRemarks());
								saveAudit(requestWorkFlowAuditEntity, authDetailsVo,requestVo);								 
							}

						} else {
							RequestWorkFlowAuditEntity requestWorkFlowAuditEntity = new RequestWorkFlowAuditEntity();
							requestWorkFlowAuditEntity.setWorkFlowId(requestWorkFlowSequenceVo.getReqWorkFlowId());
							requestWorkFlowAuditEntity.setSeqId(requestWorkFlowSequenceVo.getReqWorkFlowSeqId());
							requestWorkFlowAuditEntity.setRequestId(requestId);
							requestWorkFlowAuditEntity.setUserId(requestWorkFlowSequenceVo.getUserId());
							requestWorkFlowAuditEntity.setSequence(++localcount);
							if (null != authDetailsVo.getEntityId()) {
								requestWorkFlowAuditEntity.setEntityLicenseId(authDetailsVo.getEntityId());
							} else {
								requestWorkFlowAuditEntity.setEntityLicenseId(requestEntity.getEntityLicenseId());
							}
							requestWorkFlowAuditEntity.setApprovalExecuter(CommonConstant.CONSTANT_ONE);
							requestWorkFlowAuditEntity.setGroupId(CommonConstant.CONSTANT_ZERO);
							requestWorkFlowAuditEntity.setRequestWorkflowAuditIsActive(CommonConstant.BOOLEAN_TRUE);
							
							boolean weekendFlag  = false;
							boolean holidayFlag =  false;
							
							if(requestWorkFlowSequenceVo.isApproverWeekend()){
								weekendFlag = true;
							}
							
							if(requestWorkFlowSequenceVo.isApproverHoliday()){
								holidayFlag = true;
						    }									
							
							UserMasterVO userMasterVo = getEmailAddress(requestWorkFlowAuditEntity.getUserId(),authDetailsVo);
							
							minutes = calculateSLATime(requestWorkFlowSequenceVo.getSlaType(),
									requestWorkFlowSequenceVo.getWorkFlowSla(),weekendFlag,holidayFlag , 
									userMasterVo.getUserLocation(), userMasterVo.getSubLocation(),authDetailsVo);
							requestWorkFlowAuditEntity.setRequestWorkflowAuditSla(minutes);
							requestWorkFlowAuditEntity.setAuditForwardRemarks(requestVo.getForwardRedirectRemarks());
							saveAudit(requestWorkFlowAuditEntity, authDetailsVo,requestVo);						 

						}
					}

				}

			}  

			if (i == 0) {
			 
				UserMappingVO userMappingEntity = new UserMappingVO();

				if (null != authDetailsVo.getUserId()) {

					userMappingEntity = requestConfigurationDAO.getDepartmentLevelUser(authDetailsVo.getUserId(),
							authDetailsVo);
				} else {
					userMappingEntity = requestConfigurationDAO.getDepartmentLevelUser(requestVo.getUserId(),
							authDetailsVo);
				}
				if (userMappingEntity != null) {

					if (userMappingEntity.getLevelId() <= CommonConstant.CONSTANT_TWO) {

						String status = DecisionTypeEnum.APP.toString();
						int statusId = findCurrentStatusId(status, authDetailsVo);

						requestConfigurationDAO.updateCurrentStatus(requestEntity.getRequestId(), statusId,
								authDetailsVo);
					}
				}
			}

		}

		 

			int count = 0;
			if (requestWorkFlowExecuterVoList != null && !requestWorkFlowExecuterVoList.isEmpty()) {
				for (RequestWorkFlowExecuterVO requestWorkFlowExecuter : requestWorkFlowExecuterVoList) {

					  count = executerWorkflowCheck(requestEntity, requestWorkFlowExecuter);
					
				/*	if(null == requestVo.getSubrequestId()){
					
					  count = executerWorkflowCheck(requestEntity, requestWorkFlowExecuter);
					}else{
						count = 1;
					}*/
										
					if (count == 1) {

						if (requestWorkFlowExecuter.getExecuterUserId() == CommonConstant.CONSTANT_ZERO) {

							List<UserEntity> userEntityList = requestConfigurationDAO
									.findExecuterUserId(requestWorkFlowExecuter, authDetailsVo);

							if (userEntityList.size() > CommonConstant.CONSTANT_ONE
									&& count == CommonConstant.CONSTANT_ZERO) {
								count = count + CommonConstant.CONSTANT_TWO;
							} else if (userEntityList.size() > CommonConstant.CONSTANT_ONE) {
								count = count + CommonConstant.CONSTANT_THREE;
							} else {
								count = CommonConstant.CONSTANT_ZERO;
							}

							for (UserEntity userEntity : userEntityList) {

								RequestWorkFlowAuditEntity requestWorkFlowAuditEntity = new RequestWorkFlowAuditEntity();

								requestWorkFlowAuditEntity.setWorkFlowId(requestWorkFlowEntity.getReqWorkFlowId());
								requestWorkFlowAuditEntity.setSeqId(0);
								requestWorkFlowAuditEntity.setRequestId(requestId);
								requestWorkFlowAuditEntity.setUserId(userEntity.getId());
								requestWorkFlowAuditEntity.setSequence(++localcount);
								if (null != authDetailsVo.getEntityId()) {
									requestWorkFlowAuditEntity.setEntityLicenseId(authDetailsVo.getEntityId());
								} else {
									requestWorkFlowAuditEntity.setEntityLicenseId(requestEntity.getEntityLicenseId());
								}
								requestWorkFlowAuditEntity.setApprovalExecuter(CommonConstant.CONSTANT_TWO);
								requestWorkFlowAuditEntity.setGroupId(count);
								requestWorkFlowAuditEntity.setRequestWorkflowAuditIsActive(CommonConstant.BOOLEAN_TRUE);
								
								boolean weekendFlag  = false;
								boolean holidayFlag =  false;
								
								if(requestWorkFlowExecuter.isExecutorWeekend()){
									weekendFlag = true;
								}
								
								if(requestWorkFlowExecuter.isExecutorHoliday()){
									holidayFlag = true;
								}																
								
								UserMasterVO userMasterVo = getEmailAddress(requestWorkFlowAuditEntity.getUserId(),authDetailsVo);
								
								minutes = calculateSLATime(requestWorkFlowEntity.getSlaType(),
										requestWorkFlowEntity.getWorkFlowSla() ,weekendFlag , holidayFlag ,
										userMasterVo.getUserLocation(), userMasterVo.getSubLocation(),authDetailsVo);
								requestWorkFlowAuditEntity.setRequestWorkflowAuditSla(minutes);		
								requestWorkFlowAuditEntity.setAuditForwardRemarks(requestVo.getForwardRedirectRemarks());
								saveAudit(requestWorkFlowAuditEntity, authDetailsVo,requestVo);
									executerUser++;								
							}
						} else {

							RequestWorkFlowAuditEntity requestWorkFlowAuditEntity = new RequestWorkFlowAuditEntity();

							if(null != requestWorkFlowEntity.getReqWorkFlowId()){
								requestWorkFlowAuditEntity.setWorkFlowId(requestWorkFlowEntity.getReqWorkFlowId());	
							}
							
							
							requestWorkFlowAuditEntity.setSeqId(CommonConstant.CONSTANT_ZERO);
							requestWorkFlowAuditEntity.setRequestId(requestId);
							
							if(null != requestWorkFlowExecuter.getExecuterUserId()){
								requestWorkFlowAuditEntity.setUserId(requestWorkFlowExecuter.getExecuterUserId());	
							}
														
							requestWorkFlowAuditEntity.setSequence(++localcount);
							
							if (null != authDetailsVo.getEntityId()) {
								requestWorkFlowAuditEntity.setEntityLicenseId(authDetailsVo.getEntityId());
							} else {
								requestWorkFlowAuditEntity.setEntityLicenseId(requestEntity.getEntityLicenseId());
							}
							
							requestWorkFlowAuditEntity.setApprovalExecuter(CommonConstant.CONSTANT_TWO);
							requestWorkFlowAuditEntity.setGroupId(CommonConstant.CONSTANT_ZERO);
							requestWorkFlowAuditEntity.setRequestWorkflowAuditIsActive(CommonConstant.BOOLEAN_TRUE);
						
							boolean weekendFlag  = false;
							boolean holidayFlag =  false;
							
							if(requestWorkFlowExecuter.isExecutorWeekend()){
								weekendFlag = true;
							}
							
							if(requestWorkFlowExecuter.isExecutorHoliday()){
								holidayFlag = true;
							}																
										
							UserMasterVO userMasterVo = getEmailAddress(requestWorkFlowAuditEntity.getUserId(),authDetailsVo);
							
							minutes = calculateSLATime(requestWorkFlowEntity.getSlaType(),
									requestWorkFlowEntity.getWorkFlowSla() , weekendFlag , holidayFlag ,
									userMasterVo.getUserLocation(), userMasterVo.getSubLocation(),authDetailsVo);
							
							
							requestWorkFlowAuditEntity.setRequestWorkflowAuditSla(minutes);
							requestWorkFlowAuditEntity.setAuditForwardRemarks(requestVo.getForwardRedirectRemarks());
							
							saveAudit(requestWorkFlowAuditEntity, authDetailsVo,requestVo);
								executerUser++;							
						}
					}
				}
			}
	 

		if (executerUser != 0) {
			RequestWorkFlowAuditEntity requestWorkFlowAuditEntity = new RequestWorkFlowAuditEntity();
			requestWorkFlowAuditEntity.setWorkFlowId(requestWorkFlowEntity.getReqWorkFlowId());
			requestWorkFlowAuditEntity.setSeqId(CommonConstant.CONSTANT_ZERO);
			requestWorkFlowAuditEntity.setRequestId(requestId);
			requestWorkFlowAuditEntity.setUserId(requestEntity.getCreateBy());
			if (null != authDetailsVo.getEntityId()) {
				requestWorkFlowAuditEntity.setEntityLicenseId(authDetailsVo.getEntityId());
			} else {
				requestWorkFlowAuditEntity.setEntityLicenseId(requestEntity.getEntityLicenseId());
			}
			requestWorkFlowAuditEntity.setSequence(++localcount);
			requestWorkFlowAuditEntity.setApprovalExecuter(CommonConstant.CONSTANT_THREE);
			requestWorkFlowAuditEntity.setGroupId(CommonConstant.CONSTANT_ZERO);
			requestWorkFlowAuditEntity.setRequestWorkflowAuditIsActive(true);
			requestWorkFlowAuditEntity.setAuditForwardRemarks(requestVo.getForwardRedirectRemarks());
			
			saveAudit(requestWorkFlowAuditEntity, authDetailsVo,requestVo);
			
		} else {

			throw new CommonException(getMessage("request_validation_executersNotAvailble", authDetailsVo));
		}
		
		
		} catch (NoResultException e) {
			Log.info("Request Configuration Service createWorkflowByUser NoResultException",e);
			throw new CommonException("noRecordFound");
		} catch (CommonException e) {
			Log.info("Request Configuration Service createWorkflowByUser Common Exception",e);
			throw new CommonException(e.getMessage());
		}catch (Exception exe) {
			exe.printStackTrace();
			Log.info("Request Configuration Service createWorkflowByUser  Exception",exe);
			throw new Exception(getMessage("dataFailure", authDetailsVo));
		}
	}

	/**
	 * This method is used for get the list of user id.
	 * 
	 * @param currentLoginUserId
	 *            int
	 * @return user List<Integer>
	 */
	@Transactional
	public List<Integer> getDepartmentUser(int currentLoginUserId, AuthDetailsVo authDetailsVo) {

		int i = 0;
		List<UserMappingEntity> list_UserMappingEntity = new ArrayList<UserMappingEntity>();
		List<Integer> user = new ArrayList<Integer>();

		list_UserMappingEntity = getDepartmentUser(currentLoginUserId, list_UserMappingEntity, i, authDetailsVo);

		for (UserMappingEntity screenAuthenticationEntity : list_UserMappingEntity) {

			if (screenAuthenticationEntity.getReportingToUser() != null) {
				user.add(screenAuthenticationEntity.getReportingToUser().getId());
			}

		}
		return user;
	}

	/**
	 * This method is used for get the all subordinates list of user id.
	 * 
	 * @param currentLoginUserId
	 *            int
	 * @param userMappingEntity
	 *            List<UserMappingEntity>
	 * @return userMappingEntity List<UserMappingEntity>
	 */
	@Transactional
	private List<UserMappingEntity> getDepartmentUser(int currentLoginUserId, List<UserMappingEntity> userMappingEntity,
			int i, AuthDetailsVo authDetailsVo) {

		List<UserMappingEntity> list_UserMappingEntityy = requestConfigurationDAO
				.getListOfSubordinates(currentLoginUserId, authDetailsVo);

		if (list_UserMappingEntityy != null) {

			for (UserMappingEntity usermappingEntity : list_UserMappingEntityy) {

				//old without entity
				/*if (usermappingEntity.getLevel().getCommonId() > 1) {

					if (usermappingEntity.getLevel().getCommonId() == 2) {

						i++;

						if (i <= 1) {
							if (usermappingEntity.getReportingToUser() != null) {

								userMappingEntity.add(usermappingEntity);
								getDepartmentUser(usermappingEntity.getReportingToUser().getId(), userMappingEntity, i,
										authDetailsVo);

							} else {
								userMappingEntity.add(usermappingEntity);
							}
						}

					} else {
						getDepartmentUser(usermappingEntity.getReportingToUser().getId(), userMappingEntity, i,
								authDetailsVo);
					}
				}*/
				
				//New as per entity

					if (usermappingEntity.getLevel().getItemValue().equals(CommonConstant.LEVEL2)) {

						i++;

						if (i <= 1) {
							if (usermappingEntity.getReportingToUser() != null) {

								userMappingEntity.add(usermappingEntity);
								getDepartmentUser(usermappingEntity.getReportingToUser().getId(), userMappingEntity, i,
										authDetailsVo);

							} else {
								userMappingEntity.add(usermappingEntity);
							}
						}

					} else {
						getDepartmentUser(usermappingEntity.getReportingToUser().getId(), userMappingEntity, i,
								authDetailsVo);
					}
			}

		}

		return userMappingEntity;

	}

	/**
	 * This method is used for get the list of user id.
	 * 
	 * @param currentLoginUserId
	 *            int
	 * @return user List<Integer>
	 */
	@Transactional
	public List<Integer> getListOfSalesProject(int currentLoginUserId, AuthDetailsVo authDetailsVo) {

		int i = 0;

		List<UserMappingEntity> list_UserMappingEntity = new ArrayList<UserMappingEntity>();
		List<Integer> user = new ArrayList<Integer>();

		list_UserMappingEntity = getAllSubordinates(currentLoginUserId, list_UserMappingEntity, i, authDetailsVo);

		for (UserMappingEntity screenAuthenticationEntity : list_UserMappingEntity) {

			if (screenAuthenticationEntity.getReportingToUser() != null) {
				user.add(screenAuthenticationEntity.getReportingToUser().getId());
			}

		}
		return user;
	}

	/**
	 * This method is used for get the all subordinates list of user id.
	 * 
	 * @param currentLoginUserId
	 *            int
	 * @param userMappingEntity
	 *            List<UserMappingEntity>
	 * @return userMappingEntity List<UserMappingEntity>
	 */
	@Transactional
	private List<UserMappingEntity> getAllSubordinates(int currentLoginUserId,
			List<UserMappingEntity> userMappingEntity, int i, AuthDetailsVo authDetailsVo) {

		List<UserMappingEntity> list_UserMappingEntityy = requestConfigurationDAO
				.getListOfSubordinates(currentLoginUserId, authDetailsVo);

		if (list_UserMappingEntityy != null) {

			for (UserMappingEntity usermappingEntity : list_UserMappingEntityy) {

				if (usermappingEntity.getLevel().getCommonId() > 2) {

					i++;

					if (usermappingEntity.getReportingToUser() != null) {

						userMappingEntity.add(usermappingEntity);
						getAllSubordinates(usermappingEntity.getReportingToUser().getId(), userMappingEntity, i,
								authDetailsVo);

					} else {
						userMappingEntity.add(usermappingEntity);
					}
				}
			}

		}

		return userMappingEntity;
	}

	/**
	 * 
	 * @param status
	 * @return
	 */
	public int findCurrentStatusId(String status, AuthDetailsVo authDetailsVo) {

		try {

			int statusId = (int) requestConfigurationDAO.findStatusId(status, authDetailsVo);
			return statusId;

		} catch (NoResultException e) {
			Log.info("Request Configuration Service findCurrentStatusId  NoResultException",e);
			throw new CommonException(getMessage("noResultFound", authDetailsVo));
		} catch (NonUniqueResultException e) {
			Log.info("Request Configuration Service findCurrentStatusId  NonUniqueResultException",e);
			throw new CommonException(getMessage("noRecordFound", authDetailsVo));
		} catch (Exception e) {
			Log.info("Request Configuration Service findCurrentStatusId  Exception",e);
			throw new CommonException(getMessage("dataFailure", authDetailsVo));
		}

	}

	/**
	 * This method used to save the aduit details to db
	 * 
	 * @param requestWorkFlowAuditEntity
	 *            RequestWorkFlowAuditEntity
	 * @throws Exception  
	 */
	@Transactional
	private void save(RequestWorkFlowAuditEntity requestWorkFlowAuditEntity, AuthDetailsVo authDetailsVo)
			throws CommonException, Exception  {

		try {
			requestWorkFlowAuditEntity = setCreateUserDetails(requestWorkFlowAuditEntity, authDetailsVo);
			requestConfigurationDAO.updateDetailInAudit(requestWorkFlowAuditEntity, authDetailsVo);
		} catch (CommonException e) {
			Log.info("Request Configuration Service save Common Exception",e);
			throw new CommonException(getMessage("modifyFailure", authDetailsVo));
		}

	}

	/**
	 * This method used to save the audit details to db
	 * 
	 * @param requestWorkFlowAuditEntity
	 *            RequestWorkFlowAuditEntity
	 * @throws CommonException
	 */
	@Transactional
	private void saveAudit(RequestWorkFlowAuditEntity requestWorkFlowAuditEntity, AuthDetailsVo authDetailsVo , RequestVO requestVo )
			throws CommonException {

		try {
			requestWorkFlowAuditEntity = setCreateUserDetails(requestWorkFlowAuditEntity, authDetailsVo);
			
			requestWorkFlowAuditRepository.save(requestWorkFlowAuditEntity);
			
			/*if(null == requestVo.getForwardRequestId()){
				requestWorkFlowAuditRepository.save(requestWorkFlowAuditEntity);
			}else if(null != requestVo.getForwardRequestId() &&
					!(authDetailsVo.getUserId().equals(requestWorkFlowAuditEntity.getUserId()))){
			requestWorkFlowAuditRepository.save(requestWorkFlowAuditEntity);
			}*/

		} catch (Exception exe) {
			Log.info("Request Configuration Service saveAudit Exception",exe);
			throw new CommonException(getMessage("dataFailure", authDetailsVo));
		}

	}

	public int calculateSLATime(int slaType, float slaTime ,
			boolean weekendFlag , boolean holidayFlag , 
			Integer locationId, Integer subLocationId, AuthDetailsVo authDetailsVo ) throws CommonException, ParseException {
		
		int  wkDayCount = 0 ;		 
		int  holidayCount = 0 ;
			 
		if (slaType == CommonConstant.CONSTANT_TWO) {
			int bothFlag = 0;
			if (weekendFlag == true && holidayFlag == true) {
				bothFlag = 1;
				wkDayCount = calcWeekendSLA(slaTime, locationId, subLocationId, authDetailsVo);
				holidayCount = calcHolidaySLA(slaTime, locationId, subLocationId, authDetailsVo, bothFlag);
			} else if (weekendFlag) {
				wkDayCount = calcWeekendSLA(slaTime, locationId,subLocationId, authDetailsVo);
			} else if (holidayFlag) {
				holidayCount = calcHolidaySLA(slaTime, locationId, subLocationId, authDetailsVo, bothFlag);
			}
		}
		
		float minutes = CommonConstant.CONSTANT_ZERO;		

		if (slaType == CommonConstant.CONSTANT_ONE) {

			minutes = slaTime * 60;

		} else if (slaType == CommonConstant.CONSTANT_TWO) {		 
			minutes = ((slaTime+wkDayCount+holidayCount) * 24 * 60)  ;
			
		}
		int min = (int) Math.round(minutes);
		return min;

	}
	
	public int calcWeekendSLA(float slaTime, Integer locationId, Integer subLocationId , AuthDetailsVo authDetailsVo) throws ParseException {

		int wkDayCount = 0;

		// get weekend days
		 ArrayList<Integer> weekdays = getWeekendDays(locationId,subLocationId,authDetailsVo);		
		ArrayList<String> slaDaysDates = new ArrayList<String>();
		ArrayList<String> slaDayNames = new ArrayList<String>();
		
		slaDaysDates = getSLAWeekEndDates(slaTime);
			 		
		for (String ldate : slaDaysDates) {

			SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
			Date dt1 = format1.parse(ldate);
			DateFormat format2 = new SimpleDateFormat("EEEE");
			String dayOfWeek = format2.format(dt1);
			slaDayNames.add(dayOfWeek);	
		}
	 
		 // ADDED FOR SINGLE DAY CALC	
		int mapcount = 1;
		HashMap<Integer ,Integer> map = new HashMap<Integer , Integer>();
		for( Integer wkdy : weekdays){
					
			map.put(wkdy,mapcount);
			mapcount = mapcount + 1;
		}				
		
		int remainingWeekendDay =  0;
		int containsday = 0;
				
		for (String slaDayNm : slaDayNames) {
			int dayNumberres = getWeekDayNum(slaDayNm);

			if (weekdays.contains(dayNumberres)) {
				wkDayCount = wkDayCount + 1;
				map.remove(dayNumberres);
				containsday = dayNumberres;
			}
		}
			
		for (Entry<Integer, Integer> entry : map.entrySet()) {
			remainingWeekendDay = entry.getKey();
		}
	 
		int newcontaindays  = containsday + 1;

		if (remainingWeekendDay == newcontaindays) {
			wkDayCount = wkDayCount + 1;
		}
				
		return wkDayCount;
	}
		
	public int  calcHolidaySLA(float slaTime,Integer locationId,Integer subLocationId,
			AuthDetailsVo authDetailsVo , int bothFlag) throws ParseException{
			 	 		
		int  holidayCount = 0 ;
		ArrayList<String> holidayDates =  new ArrayList<String>();	
		holidayDates = 	getHoliDays(locationId,subLocationId,authDetailsVo); //string
				
		ArrayList<String> slaHoliDayDates = new ArrayList<String>();
		 			
		int day= 1;		
		LocalDate now = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		 		 	 			
		for (int i = day; i < slaTime + 1; i++) {
			 
			LocalDate newdate = now.plusDays(i);
			String formattedDate1 = newdate.format(formatter);
			LocalDate nextparsedDate = LocalDate.parse(formattedDate1, formatter);
			slaHoliDayDates.add(nextparsedDate.toString());
		}

		for (String ldate : slaHoliDayDates) {			
			if (holidayDates.contains(ldate)) {
				holidayCount = holidayCount + 1;
			}
		}
			
		if (bothFlag == 1) {
			ArrayList<String> slaWeekenddays = getSLAWeekEndDatesInYYYYMMDD(slaTime);

			for (String slaHoliday : holidayDates) {

				if (slaWeekenddays.contains(slaHoliday)) {

					holidayCount = holidayCount - 1;

				}
			}
		}
		
	   return holidayCount ;
	}
	 			
	@Transactional
	public ArrayList<Integer>  getWeekendDays(Integer locationId, Integer subLocationId, AuthDetailsVo authDetailsVo){
		
	 return holidayDAO.getWeekendDays(locationId , subLocationId, authDetailsVo);	
			
	}
	
	public ArrayList<String> getHoliDays(Integer locationId, Integer subLocationId, AuthDetailsVo authDetailsVo) {

		ArrayList<String> holidayDates = new ArrayList<String>();
		ArrayList<Date> holidayDateList = holidayDAO.getHoliDays(locationId, subLocationId, authDetailsVo);

		for (Date dt : holidayDateList) {

			String parsedate = dt.toString();

			LocalDate myDate1 = LocalDate.parse(parsedate);

			holidayDates.add(myDate1.toString());

		}

		return holidayDates;
	}
	
	public ArrayList<String> getSLAWeekEndDates(float slaTime){
		ArrayList<String> slaDaysDates = new ArrayList<String>();
		for (int i = 1; i < slaTime + 1; i++) {
			LocalDate now = LocalDate.now();
			LocalDate newdate = now.plusDays(i);
			String formattedDateTest = newdate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
			slaDaysDates.add(formattedDateTest);
		}
		return slaDaysDates;
	}
	
	
	public ArrayList<String> getSLAWeekEndDatesInYYYYMMDD(float slaTime){
		ArrayList<String> slaDaysDates = new ArrayList<String>();
		for (int i = 1; i < slaTime + 1; i++) {
			LocalDate now = LocalDate.now();
			LocalDate newdate = now.plusDays(i);
			String formattedDateTest = newdate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			slaDaysDates.add(formattedDateTest);
		}
		return slaDaysDates;
	}
	
	public int getWeekDayNum(String dayName) {

		int dayNumberres = 0;
		switch (dayName) {

		case "Monday":
			dayNumberres = 1;
			break;
		case "Tuesday":
			dayNumberres = 2;
			break;
		case "Wednesday":
			dayNumberres = 3;
			break;
		case "Thursday":
			dayNumberres = 4;
			break;
		case "Friday":
			dayNumberres = 5;
			break;
		case "Saturday":
			dayNumberres = 6;
			break;
		case "Sunday":
			dayNumberres = 7;
			break;
		}
		return dayNumberres;
	}
	
	public  LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
		return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public String formatDatetoString(Date date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		String dateString = sdf.format(date);
		return dateString;
	}
	
	
	public int executerWorkflowCheck(RequestEntity requestEntity, RequestWorkFlowExecuterVO requestWorkFlowExecuter) {

		int count = 0;

		if (null != requestWorkFlowExecuter.getExecuterLocationId()
				&& null != requestWorkFlowExecuter.getExecuterSublocationId()
				&& null != requestWorkFlowExecuter.getExecuterDepartmentId() && null != requestEntity.getReqLocationId()
				&& null != requestEntity.getReqSublocationId() && null != requestEntity.getDepartmentId()) {
			if (requestWorkFlowExecuter.getExecuterLocationId().equals(requestEntity.getReqLocationId())
					&& requestWorkFlowExecuter.getExecuterSublocationId().equals(requestEntity.getReqSublocationId())
					&& requestWorkFlowExecuter.getExecuterDepartmentId().equals(requestEntity.getDepartmentId())) {

				count = executerWorkflowCheckUserRoleAndUser(requestEntity, requestWorkFlowExecuter);

			} /* else if (requestWorkFlowExecuter.getExecuterLocationId().equals(requestEntity.getReqLocationId())
					&& requestWorkFlowExecuter.getExecuterDepartmentId().equals(requestEntity.getDepartmentId())) {

				count = executerWorkflowCheckUserRoleAndUser(requestEntity, requestWorkFlowExecuter);

			} else if (requestWorkFlowExecuter.getExecuterLocationId().equals(requestEntity.getReqLocationId())
					&& requestWorkFlowExecuter.getExecuterSublocationId().equals(requestEntity.getReqSublocationId())) {

				count = executerWorkflowCheckUserRoleAndUser(requestEntity, requestWorkFlowExecuter);

			} */
		} else if (null != requestWorkFlowExecuter.getExecuterLocationId()
				&& null != requestWorkFlowExecuter.getExecuterSublocationId()
				&& null == requestWorkFlowExecuter.getExecuterDepartmentId() && null != requestEntity.getReqLocationId()
				&& null != requestEntity.getReqSublocationId()) {
			if (requestWorkFlowExecuter.getExecuterLocationId().equals(requestEntity.getReqLocationId())
					&& requestWorkFlowExecuter.getExecuterSublocationId().equals(requestEntity.getReqSublocationId())) {

				count = executerWorkflowCheckUserRoleAndUser(requestEntity, requestWorkFlowExecuter);

			}  /*else if (requestWorkFlowExecuter.getExecuterLocationId().equals(requestEntity.getReqLocationId())) {

				count = executerWorkflowCheckUserRoleAndUser(requestEntity, requestWorkFlowExecuter);

			} */
		} else if (null != requestWorkFlowExecuter.getExecuterLocationId() && null != requestEntity.getReqLocationId()) {
			if (requestWorkFlowExecuter.getExecuterLocationId().equals(requestEntity.getReqLocationId())
					&& requestWorkFlowExecuter.getExecuterSublocationId() == null) {

				count = executerWorkflowCheckUserRoleAndUser(requestEntity, requestWorkFlowExecuter);
			}
		} else if (requestWorkFlowExecuter.getExecuterLocationId() == null ) {

			count = executerWorkflowCheckUserRoleAndUser(requestEntity, requestWorkFlowExecuter);

		}
		return count;

	}

	public int executerWorkflowCheckUserRoleAndUser(RequestEntity requestEntity,
			RequestWorkFlowExecuterVO requestWorkFlowExecuter) {

		if (requestWorkFlowExecuter.getExecuterRoleId() != null
				&& requestWorkFlowExecuter.getExecuterUserId() != null) {

			return 1;

		} else if (requestWorkFlowExecuter.getExecuterUserId() != null) {

			return 1;

		} else if (requestWorkFlowExecuter.getExecuterRoleId() != null) {

			return 1;

		} else {

			return 0;
		}

	}

	/**
	 * This method is used approval and resolver to update the audit details.
	 * 
	 * @param requestWorkFlowAuditVo
	 *            RequestWorkFlowAuditVO
	 * @throws CommonException
	 */
	@Transactional
	public void repopulate(RequestWorkFlowAuditVO requestWorkFlowAuditVo, AuthDetailsVo authDetailsVo,RequestVO requestVo)
			throws CommonException {

		switch (requestWorkFlowAuditVo.getDescisionType()) {

		case 1:
			createApproval(requestWorkFlowAuditVo, authDetailsVo);
			break;
		case 2:
			createReject(requestWorkFlowAuditVo, authDetailsVo);
			break;
		case 3:
			createReSubmit(requestWorkFlowAuditVo, authDetailsVo);
			break;
		case 4:
			createComplete(requestWorkFlowAuditVo, authDetailsVo);
			break;
		case 5:
			createInProgress(requestWorkFlowAuditVo, authDetailsVo);
			break;
		case 6:
			createReassign(requestWorkFlowAuditVo, authDetailsVo,requestVo);
			break;
		case 7:
			createReopen(requestWorkFlowAuditVo, authDetailsVo, requestVo);
			break;
		case 8:
			createClose(requestWorkFlowAuditVo, authDetailsVo);
			break;

		}

	}

	/**
	 * This method is used to approval for put approved for request.
	 * 
	 * @param requestWorkFlowAuditVo
	 *            RequestWorkFlowAuditVo
	 * @throws CommonException
	 */
	@SuppressWarnings("unused")
	@Transactional
	public void createApproval(RequestWorkFlowAuditVO requestWorkFlowAuditVo, AuthDetailsVo authDetailsVo)
			throws CommonException {

		RequestWorkFlowAuditEntity requestWorkFlowAuditEntity = new RequestWorkFlowAuditEntity();
		JSONResponse response = new JSONResponse();
		try {
			 
			BeanUtils.copyProperties(requestWorkFlowAuditEntity, requestWorkFlowAuditVo);
			requestWorkFlowAuditEntity.setRemarks(requestWorkFlowAuditVo.getRemarks());
			requestWorkFlowAuditEntity.setUpdateBy(requestWorkFlowAuditVo.getUpdatedBy());

			requestWorkFlowAuditEntity = updateApproverStatus( requestWorkFlowAuditEntity , authDetailsVo,requestWorkFlowAuditVo);										 								
			
			requestConfigurationDAO.updateAppDecision(requestWorkFlowAuditEntity, CommonConstant.NOT_WORKING, authDetailsVo);
			
			int approvecount = requestConfigurationDAO.approvalCount(requestWorkFlowAuditEntity, authDetailsVo);			

			if (approvecount != CommonConstant.CONSTANT_ZERO) {			 
					requestConfigurationDAO.updateCurrentStatusInRequest(DecisionTypeEnum.PEN.toString(),
							requestWorkFlowAuditEntity, authDetailsVo);			
			} else {				 
					requestConfigurationDAO.updateCurrentStatusInRequest(DecisionTypeEnum.APP.toString(),
							requestWorkFlowAuditEntity, authDetailsVo);			
			}
			 
			requestConfigurationDAO.getAllRemarksList(requestWorkFlowAuditEntity, authDetailsVo);
			
			RequestEntity requestEntity = requestRepository.findOne(requestWorkFlowAuditVo.getRequestId());
			 
			requestConfigurationDAO.findApprovalSequence(requestEntity, authDetailsVo);
			

			if (mailMessages.getApproved() == CommonConstant.CONSTANT_ONE) {
							
				EmailVo emailVo = new EmailVo();
				emailVo.setMessageCode(CommonConstant.APPROVED);
				emailVo.setGroupId(CommonConstant.APP);
				emailVo.setToUserId(requestEntity.getCreateBy());
				emailVo.setRequestId(requestWorkFlowAuditVo.getRequestId());
				emailVo.setRequestCode(requestEntity.getRequestCode());
				emailVo.setEmailFlag(0);
				emailVo.setCreateBy(authDetailsVo.getUserId());
				emailVo.setUpdateBy(authDetailsVo.getUserId());
				emailVo.setCreateDate(CommonConstant.getCalenderDate());
				emailVo.setUpdateDate(CommonConstant.getCalenderDate());
				emailVo.setEntityId(authDetailsVo.getEntityId());
				emailVo.setEscalationFlag(CommonConstant.CONSTANT_ZERO);
								
				UserMasterVO userMasterVO1 =	getEmailAddress(requestEntity.getCreateBy(),authDetailsVo);
				emailVo.setToUserAddress(userMasterVO1.getEmailId());
				emailVo.setRequestorName(userMasterVO1.getUserName());
				emailVo.setUserLang(userMasterVO1.getLangCode());				
							 
				emailVo.setToUserId(userMasterVO1.getId());
				emailVo.setUserName(userMasterVO1.getUserName());
				
				UserMasterVO userMasterVO2 = getEmailAddress(requestEntity.getUpdateBy(),authDetailsVo);
				emailVo.setApproverName(userMasterVO2.getUserName()); 
				 	
				emailVo.setSystemConfigurationVo(getSystemConfigurationDetails(authDetailsVo));
				EntityLicenseVO  entityLicenseVO = getEntityDetails(authDetailsVo.getEntityId());
				
				RequestWorkFlowEntity requestWorkFlow  = requestConfigurationDAO.getWorkflow(requestWorkFlowAuditEntity.getWorkFlowId());
				
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
				
				if (null != emailVo.getSystemConfigurationVo()) {
					if (null != entityLicenseVO.getEmailFlag() && entityLicenseVO.getEmailFlag() == 1) {
						final String uri = emailRestTemplateUrl + FilePathConstants.TOEMAIL;
						response = restTemplate.postForObject(uri, emailVo, JSONResponse.class);

					}
				}
			}

			requestConfigurationDAO.updateDate(requestEntity.getRequestId(), authDetailsVo);
						
			calcSLAForApprover(requestWorkFlowAuditEntity , authDetailsVo,requestEntity);
			
			calcSLAForExecutor(requestWorkFlowAuditEntity , authDetailsVo,requestEntity);						
			
		} catch (Exception exe) {
			Log.info("Request Configuration Service createApproval Exception",exe);
			throw new CommonException("dataFailure");
		}

	}
		
	public RequestWorkFlowAuditEntity updateApproverStatus(RequestWorkFlowAuditEntity requestWorkFlowAuditEntity,
			AuthDetailsVo authDetailsVo, RequestWorkFlowAuditVO requestWorkFlowAuditVo)
					throws CommonException, Exception {

		try {

			boolean delegateuser = true;
			List<RequestWorkFlowAuditEntity> allApproverList = requestConfigurationDAO
					.getAllAudit(requestWorkFlowAuditEntity, authDetailsVo);
			RequestWorkFlowAuditEntity newRequestWorkFlowAuditEntity = new RequestWorkFlowAuditEntity();

			if (allApproverList.size() > 0) {
				newRequestWorkFlowAuditEntity = allApproverList.get(0);
			}

			List<RequestWorkFlowAuditEntity> selectedApproverList = requestConfigurationDAO
					.getSelectedAudit(newRequestWorkFlowAuditEntity, authDetailsVo);

			for (RequestWorkFlowAuditEntity curWorkFlowAuditEntity : selectedApproverList) {

				if (curWorkFlowAuditEntity.getUserId().equals(authDetailsVo.getUserId())) {

					requestConfigurationDAO.updateDetailInAudit(requestWorkFlowAuditEntity, authDetailsVo);
					delegateuser = false;
					break;
				}
			}

			if (delegateuser) {
				requestWorkFlowAuditEntity = requestConfigurationDAO
						.auditUpdateByDelegateApprover(requestWorkFlowAuditEntity, authDetailsVo);
			}

		} catch (CommonException e) {
			throw new CommonException(getMessage(e.getMessage(), authDetailsVo));
		} catch (Exception e) {
			throw new CommonException(getMessage("dataFailure", authDetailsVo));
		}
		return requestWorkFlowAuditEntity;
	}

	public void calcSLAForApprover(RequestWorkFlowAuditEntity requestWorkFlowAuditEntity , AuthDetailsVo authDetailsVo,
			RequestEntity requestEntity) throws CommonException, Exception {

		RequestWorkFlowSlaEntity reqWorkFlowSlaEntity = requestConfigurationDAO
				.findApproverSlaForCalc(requestWorkFlowAuditEntity.getWorkFlowId(), requestEntity.getRequestPriority());

		boolean weekendFlag = false;
		boolean holidyFlag = false;

		if (reqWorkFlowSlaEntity.isReqWeekendFlag()) {
			weekendFlag = true;
		}

		if (reqWorkFlowSlaEntity.isReqHolidayFlag()) {
			holidyFlag = true;
		}

		UserMasterVO userMasterVo = getEmailAddress(requestWorkFlowAuditEntity.getUserId(),authDetailsVo);
		
		int sla = calculateSLATime(reqWorkFlowSlaEntity.getReqWorkFlowSlaType(),
				reqWorkFlowSlaEntity.getReqWorkFlowSla(), weekendFlag, holidyFlag, userMasterVo.getUserLocation(),
				userMasterVo.getSubLocation(), authDetailsVo);

		requestConfigurationDAO.updateSLAForApprover(requestEntity, requestWorkFlowAuditEntity.getWorkFlowId(), sla,
				authDetailsVo);

	}
	
	
	public void calcSLAForExecutor(RequestWorkFlowAuditEntity requestWorkFlowAuditEntity , AuthDetailsVo authDetailsVo,
			RequestEntity requestEntity) throws CommonException, Exception {

		RequestWorkFlowSlaEntity reqWorkFlowSlaEntity = requestConfigurationDAO
				.findExecutorSlaForCalc(requestWorkFlowAuditEntity.getWorkFlowId(), requestEntity.getRequestPriority());

		boolean weekendFlag = false;
		boolean holidyFlag = false;

		if (reqWorkFlowSlaEntity.isReqWeekendFlag()) {
			weekendFlag = true;
		}

		if (reqWorkFlowSlaEntity.isReqHolidayFlag()) {
			holidyFlag = true;
		}

		UserMasterVO userMasterVo = getEmailAddress(requestWorkFlowAuditEntity.getUserId(),authDetailsVo);
		
		int sla = calculateSLATime(reqWorkFlowSlaEntity.getReqWorkFlowSlaType(),
				reqWorkFlowSlaEntity.getReqWorkFlowSla(), weekendFlag, holidyFlag, userMasterVo.getUserLocation(),
				userMasterVo.getSubLocation(), authDetailsVo);

		requestConfigurationDAO.updateSLAForExecutor(requestEntity, requestWorkFlowAuditEntity.getWorkFlowId(), sla,
				authDetailsVo);

	}	
	
	/**
	 * This method is used to approval for put rejected for request.
	 * 
	 * @param requestWorkFlowAuditVo
	 *            RequestWorkFlowAuditVo
	 * @throws CommonException
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@Transactional
	public void createReject(RequestWorkFlowAuditVO requestWorkFlowAuditVo, AuthDetailsVo authDetailsVo)
			throws CommonException  {
		JSONResponse response = new JSONResponse();
		
		try {

			RequestWorkFlowAuditEntity requestWorkFlowAuditEntity = new RequestWorkFlowAuditEntity();			
			BeanUtils.copyProperties(requestWorkFlowAuditEntity, requestWorkFlowAuditVo);
			requestWorkFlowAuditEntity.setRemarks(requestWorkFlowAuditVo.getRemarks());
		
		if (0 != requestWorkFlowAuditVo.getCreatedBy()) {
			requestWorkFlowAuditEntity.setCreateBy(requestWorkFlowAuditVo.getCreatedBy());
			requestWorkFlowAuditEntity.setUpdateBy(requestWorkFlowAuditVo.getUpdatedBy());
		}
						
			requestWorkFlowAuditEntity = setCreateUserDetails(requestWorkFlowAuditEntity, authDetailsVo);		 							
						
			requestWorkFlowAuditEntity = updateApproverStatus( requestWorkFlowAuditEntity , authDetailsVo,requestWorkFlowAuditVo);
								 
			requestConfigurationDAO.updateDecision(requestWorkFlowAuditEntity, CommonConstant.NOT_WORKING, authDetailsVo);					
			
			requestConfigurationDAO.updateCurrentStatusInRequest(DecisionTypeEnum.REJ.toString(),
					requestWorkFlowAuditEntity, authDetailsVo);
				
			requestConfigurationDAO.getAllRemarksList(requestWorkFlowAuditEntity, authDetailsVo);

			RequestEntity requestEntity = null;

			requestEntity = requestRepository.findOne(requestWorkFlowAuditEntity.getRequestId());	

			if (mailMessages.getRejected() == CommonConstant.CONSTANT_ONE) {			
				
				//Reject
				EmailVo emailVo = new EmailVo();
				emailVo.setMessageCode(CommonConstant.REJECTED);
				emailVo.setGroupId(CommonConstant.REJ);
				emailVo.setToUserId(requestEntity.getCreateBy());
				emailVo.setRequestId(requestWorkFlowAuditVo.getRequestId());
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
				emailVo.setToUserId(userMasterVO1.getId());
				emailVo.setUserName(userMasterVO1.getUserName());
				
				UserMasterVO userMasterVO2 = getEmailAddress(requestEntity.getUpdateBy(),authDetailsVo);
				emailVo.setApproverName(userMasterVO2.getUserName()); 
				 	
				emailVo.setSystemConfigurationVo(getSystemConfigurationDetails(authDetailsVo));
				 				 
				EntityLicenseVO  entityLicenseVO = getEntityDetails(authDetailsVo.getEntityId());
				
				RequestWorkFlowEntity requestWorkFlow  = requestConfigurationDAO.getWorkflow(requestWorkFlowAuditEntity.getWorkFlowId());
				
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
				
				if (null != emailVo.getSystemConfigurationVo()) {
					if (null != entityLicenseVO.getEmailFlag() && entityLicenseVO.getEmailFlag() == 1) {
						final String uri = emailRestTemplateUrl + FilePathConstants.TOEMAIL;
						response = restTemplate.postForObject(uri, emailVo, JSONResponse.class);
					}
				}
			}

			requestConfigurationDAO.updateDate(requestEntity.getRequestId(), authDetailsVo);
		} catch (Exception exe) {
			Log.info("Request Configuration Service createReject Exception",exe);
			throw new CommonException(getMessage("dataFailure", authDetailsVo));
		}
	}

	/**
	 * This method is used to approval for put resubmit for request.
	 * 
	 * @param requestWorkFlowAuditVo
	 *            RequestWorkFlowAuditVo
	 * @throws CommonException
	 */
	@Transactional
	public void createReSubmit(RequestWorkFlowAuditVO requestWorkFlowAuditVo, AuthDetailsVo authDetailsVo)
			throws CommonException {

		RequestWorkFlowAuditEntity requestWorkFlowAuditEntity = new RequestWorkFlowAuditEntity();
		 		
		try {
			
			BeanUtils.copyProperties(requestWorkFlowAuditEntity, requestWorkFlowAuditVo);
			requestWorkFlowAuditEntity.setRemarks(requestWorkFlowAuditVo.getRemarks());

			if (0 != requestWorkFlowAuditVo.getCreatedBy()) {
				requestWorkFlowAuditEntity.setCreateBy(requestWorkFlowAuditVo.getCreatedBy());
				requestWorkFlowAuditEntity.setUpdateBy(requestWorkFlowAuditVo.getUpdatedBy());
			}
		
			requestWorkFlowAuditEntity = setCreateUserDetails(requestWorkFlowAuditEntity, authDetailsVo);					   
			   
			requestWorkFlowAuditEntity = updateApproverStatus( requestWorkFlowAuditEntity , authDetailsVo,requestWorkFlowAuditVo);
				 
			requestConfigurationDAO.updateDecision(requestWorkFlowAuditEntity, CommonConstant.NOT_WORKING, authDetailsVo);					
							   			   	
			requestConfigurationDAO.updateCurrentStatusInRequest(DecisionTypeEnum.RS.toString(),
						requestWorkFlowAuditEntity, authDetailsVo);
		 
			requestConfigurationDAO.getAllRemarksList(requestWorkFlowAuditEntity, authDetailsVo);

			RequestEntity requestEntity = null;
			 
				requestEntity = requestRepository.findOne(requestWorkFlowAuditEntity.getRequestId());
		 
			if (mailMessages.getResubmit() == CommonConstant.CONSTANT_ONE) {			
								
				//Resubmit
				EmailVo emailVo = new EmailVo();
				emailVo.setMessageCode(CommonConstant.RESUBMIT);
				emailVo.setGroupId(CommonConstant.RS);
				emailVo.setToUserId(requestEntity.getCreateBy());
				emailVo.setRequestId(requestWorkFlowAuditVo.getRequestId());
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
				emailVo.setToUserId(userMasterVO1.getId());
				emailVo.setUserName(userMasterVO1.getUserName());
								
				UserMasterVO userMasterVO2 =	getEmailAddress(requestEntity.getUpdateBy(),authDetailsVo);
				emailVo.setApproverName(userMasterVO2.getUserName()); 
				 	
				emailVo.setSystemConfigurationVo(getSystemConfigurationDetails(authDetailsVo));
										
				EntityLicenseVO  entityLicenseVO = getEntityDetails(authDetailsVo.getEntityId());
				
				RequestWorkFlowEntity requestWorkFlow  = requestConfigurationDAO.getWorkflow(requestWorkFlowAuditEntity.getWorkFlowId());
				
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

				if (null != emailVo.getSystemConfigurationVo()) {
					if (null != entityLicenseVO.getEmailFlag() && entityLicenseVO.getEmailFlag() == 1) {
						final String uri = emailRestTemplateUrl + FilePathConstants.TOEMAIL;
						JSONResponse response = restTemplate.postForObject(uri, emailVo, JSONResponse.class);

					}
				}
			}
			requestConfigurationDAO.updateDate(requestEntity.getRequestId(), authDetailsVo);
		} catch (Exception exe) {
			Log.info("Request Configuration Service createReSubmit Exception",exe);
			throw new CommonException(getMessage("dataFailure", authDetailsVo));
		}
	}

	/**
	 * This method is used to resolver for put completed for request.
	 * 
	 * @param requestWorkFlowAuditVo
	 *            RequestWorkFlowAuditVo
	 * @throws CommonException
	 */
	@Transactional
	public void createComplete(RequestWorkFlowAuditVO requestWorkFlowAuditVo, AuthDetailsVo authDetailsVo)
			throws CommonException {

		RequestWorkFlowAuditEntity requestWorkFlowAuditEntity = new RequestWorkFlowAuditEntity();
		 			
		try {

			BeanUtils.copyProperties(requestWorkFlowAuditEntity, requestWorkFlowAuditVo);
			requestWorkFlowAuditEntity.setRemarks(requestWorkFlowAuditVo.getRemarks());

			if (null != requestWorkFlowAuditVo.getCreatedBy()) {
				requestWorkFlowAuditEntity.setCreateBy(requestWorkFlowAuditVo.getCreatedBy());
				requestWorkFlowAuditEntity.setUpdateBy(requestWorkFlowAuditVo.getUpdatedBy());
			}

			requestWorkFlowAuditEntity = setCreateUserDetails(requestWorkFlowAuditEntity, authDetailsVo);

			if (null == requestWorkFlowAuditEntity.getRequestWorkFlowAuditId()) {
				requestWorkFlowAuditEntity = requestConfigurationDAO
						.auditUpdateByDelegateResolver(requestWorkFlowAuditEntity, authDetailsVo);
			} else {
				requestConfigurationDAO.updateDetailInAudit(requestWorkFlowAuditEntity, authDetailsVo);
			}
			requestConfigurationDAO.updateCurrentStatusInRequest(DecisionTypeEnum.COM.toString(),
					requestWorkFlowAuditEntity, authDetailsVo);

			int decision = CommonConstant.NOT_WORKING;
			requestConfigurationDAO.updateGroupDecision(requestWorkFlowAuditEntity, decision, authDetailsVo);
//updateGroupDecisionExecutor
			
			requestConfigurationDAO.getAllRemarksList(requestWorkFlowAuditEntity, authDetailsVo);

			RequestEntity requestEntity = null;
			 
			requestEntity = requestRepository.findOne(requestWorkFlowAuditEntity.getRequestId());
			 
			if (mailMessages.getCompleted() == CommonConstant.CONSTANT_ONE) {

				//Completed
				EmailVo emailVo = new EmailVo();
				emailVo.setMessageCode(CommonConstant.COMPLETED);
				emailVo.setGroupId(CommonConstant.COM);
				emailVo.setToUserId(requestEntity.getCreateBy());
				emailVo.setRequestId(requestWorkFlowAuditVo.getRequestId());
				emailVo.setRequestCode(requestEntity.getRequestCode());
				emailVo.setEmailFlag(0);
				emailVo.setCreateBy(authDetailsVo.getUserId());
				emailVo.setUpdateBy(authDetailsVo.getUserId());
				emailVo.setCreateDate(CommonConstant.getCalenderDate());
				emailVo.setUpdateDate(CommonConstant.getCalenderDate());
				emailVo.setEntityId(authDetailsVo.getEntityId());
				emailVo.setEscalationFlag(CommonConstant.CONSTANT_ZERO);
				emailVo.setRequestSubject(requestEntity.getRequestSubject());
								
				UserMasterVO userMasterVO1 =	getEmailAddress(requestEntity.getCreateBy(),authDetailsVo);
				emailVo.setToUserAddress(userMasterVO1.getEmailId());
				emailVo.setRequestorName(userMasterVO1.getUserName());
				emailVo.setUserLang(userMasterVO1.getLangCode()); 
				emailVo.setToUserId(userMasterVO1.getId());
				emailVo.setUserName(userMasterVO1.getUserName());
				
				UserMasterVO userMasterVO2 =	getEmailAddress(authDetailsVo.getUserId(),authDetailsVo);
				emailVo.setExecutorName(userMasterVO2.getUserName()); 
				 
				emailVo.setSystemConfigurationVo(getSystemConfigurationDetails(authDetailsVo));			 			
													
				EntityLicenseVO  entityLicenseVO = getEntityDetails(authDetailsVo.getEntityId());
				
				RequestWorkFlowEntity requestWorkFlow  = requestConfigurationDAO.getWorkflow(requestWorkFlowAuditEntity.getWorkFlowId());
				
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
				
				if (null != emailVo.getSystemConfigurationVo()) {
					if (null != entityLicenseVO.getEmailFlag() && entityLicenseVO.getEmailFlag() == 1) {
						final String uri = emailRestTemplateUrl + FilePathConstants.TOEMAIL;
						JSONResponse response = restTemplate.postForObject(uri, emailVo, JSONResponse.class);

					}
				}
			}
			requestConfigurationDAO.updateDate(requestEntity.getRequestId(), authDetailsVo);
		} catch (Exception exe) {
			Log.info("Request Configuration Service createComplete Exception", exe);
			throw new CommonException(getMessage("dataFailure", authDetailsVo));
		}

	}

	/**
	 * This method is used to resolver for put Inprogress for request.
	 * 
	 * @param requestWorkFlowAuditVo
	 *            RequestWorkFlowAuditVo
	 * @throws CommonException
	 */
	@Transactional
	public void createInProgress(RequestWorkFlowAuditVO requestWorkFlowAuditVo, AuthDetailsVo authDetailsVo)
			throws CommonException {

		RequestWorkFlowAuditEntity requestWorkFlowAuditEntity = new RequestWorkFlowAuditEntity();
		 	
		try {

			BeanUtils.copyProperties(requestWorkFlowAuditEntity, requestWorkFlowAuditVo);
			requestWorkFlowAuditEntity.setRemarks(requestWorkFlowAuditVo.getRemarks());

			if (null != requestWorkFlowAuditVo.getCreatedBy()) {
				requestWorkFlowAuditEntity.setCreateBy(requestWorkFlowAuditVo.getCreatedBy());
				requestWorkFlowAuditEntity.setUpdateBy(requestWorkFlowAuditVo.getUpdatedBy());
			}
			
			requestWorkFlowAuditEntity = setCreateUserDetails(requestWorkFlowAuditEntity, authDetailsVo);

			if (null == requestWorkFlowAuditEntity.getRequestWorkFlowAuditId()) {
				requestWorkFlowAuditEntity = requestConfigurationDAO
						.auditUpdateByDelegateResolver(requestWorkFlowAuditEntity, authDetailsVo);
			} else {
				requestConfigurationDAO.updateDetailInAudit(requestWorkFlowAuditEntity, authDetailsVo);
			}

			requestConfigurationDAO.updateCurrentStatusInRequest(DecisionTypeEnum.IP.toString(),
					requestWorkFlowAuditEntity, authDetailsVo);

			int decision = CommonConstant.NOT_WORKING;
			requestConfigurationDAO.updateGroupDecision(requestWorkFlowAuditEntity, decision, authDetailsVo);

			requestConfigurationDAO.getAllRemarksList(requestWorkFlowAuditEntity, authDetailsVo);

			RequestEntity requestEntity = null;

			requestEntity = requestRepository.findOne(requestWorkFlowAuditEntity.getRequestId());

			if (mailMessages.getInprogress() == CommonConstant.CONSTANT_ONE) {
							
				//InProgress
				EmailVo emailVo = new EmailVo();
				emailVo.setMessageCode(CommonConstant.INPROGRESS);
				emailVo.setGroupId(CommonConstant.IP);
				//emailVo.setToUserId(requestEntity.getCreateBy());
				emailVo.setRequestId(requestWorkFlowAuditVo.getRequestId());
				emailVo.setRequestCode(requestEntity.getRequestCode());
				emailVo.setEmailFlag(0);
				emailVo.setCreateBy(authDetailsVo.getUserId());
				emailVo.setUpdateBy(authDetailsVo.getUserId());
				emailVo.setCreateDate(CommonConstant.getCalenderDate());
				emailVo.setUpdateDate(CommonConstant.getCalenderDate());
				emailVo.setEntityId(authDetailsVo.getEntityId());
				emailVo.setEscalationFlag(CommonConstant.CONSTANT_ZERO);
				
				UserMasterVO userMasterVO1 = getEmailAddress(requestEntity.getCreateBy(),authDetailsVo);
				emailVo.setToUserAddress(userMasterVO1.getEmailId());
				emailVo.setRequestorName(userMasterVO1.getUserName());
				emailVo.setUserLang(userMasterVO1.getLangCode());								
				emailVo.setToUserId(userMasterVO1.getId());
				emailVo.setUserName(userMasterVO1.getUserName());
				
				UserMasterVO userMasterVO2 = getEmailAddress(authDetailsVo.getUserId(),authDetailsVo);
				emailVo.setExecutorName(userMasterVO2.getUserName()); 
				 
				emailVo.setSystemConfigurationVo(getSystemConfigurationDetails(authDetailsVo));
				 						
				EntityLicenseVO  entityLicenseVO = getEntityDetails(authDetailsVo.getEntityId());
				
				RequestWorkFlowEntity requestWorkFlow  = requestConfigurationDAO.getWorkflow(requestWorkFlowAuditEntity.getWorkFlowId());
				
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
			
				if (null != emailVo.getSystemConfigurationVo()) {
					if (null != entityLicenseVO.getEmailFlag() && entityLicenseVO.getEmailFlag() == 1) {
						final String uri = emailRestTemplateUrl + FilePathConstants.TOEMAIL;
						JSONResponse response = restTemplate.postForObject(uri, emailVo, JSONResponse.class);

					}
				}
			}
			requestConfigurationDAO.updateDate(requestEntity.getRequestId(), authDetailsVo);
		} catch (Exception exe) {
			Log.info("Request Configuration Service createInProgress Exception",exe);
			throw new CommonException(getMessage("dataFailure", authDetailsVo));
		}
	}

	/**
	 * This method is used to resolver for put Reassign for request.
	 * 
	 * @param requestWorkFlowAuditVo
	 *            RequestWorkFlowAuditVo
	 * @throws CommonException
	 */
	@Transactional
	public void createReassign(RequestWorkFlowAuditVO requestWorkFlowAuditVo, AuthDetailsVo authDetailsVo ,RequestVO requestVo)
			throws CommonException {

		RequestWorkFlowAuditEntity requestWorkFlowAuditEntity = new RequestWorkFlowAuditEntity();
		 			
		try {

			BeanUtils.copyProperties(requestWorkFlowAuditEntity, requestWorkFlowAuditVo);
			requestWorkFlowAuditEntity.setRemarks(requestWorkFlowAuditVo.getRemarks());

			if (null != requestWorkFlowAuditVo.getCreatedBy() && 0 != requestWorkFlowAuditVo.getCreatedBy()) {
				requestWorkFlowAuditEntity.setCreateBy(requestWorkFlowAuditVo.getCreatedBy());
				requestWorkFlowAuditEntity.setUpdateBy(requestWorkFlowAuditVo.getUpdatedBy());
			}
			
			requestWorkFlowAuditEntity = setCreateUserDetails(requestWorkFlowAuditEntity, authDetailsVo);

			requestConfigurationDAO.updateCurrentStatusInRequest(DecisionTypeEnum.IP.toString(),
					requestWorkFlowAuditEntity, authDetailsVo);

			if (null == requestWorkFlowAuditEntity.getRequestWorkFlowAuditId()) {
				requestWorkFlowAuditEntity = requestConfigurationDAO
						.auditUpdateByDelegateResolver(requestWorkFlowAuditEntity, authDetailsVo);
			} else {
				requestConfigurationDAO.updateDetailInAudit(requestWorkFlowAuditEntity, authDetailsVo);
			}													
				
			int decision = CommonConstant.NOT_WORKING;
			requestConfigurationDAO.updateGrpDecision(requestWorkFlowAuditEntity, decision, authDetailsVo);

			requestConfigurationDAO.getAllRemarksList(requestWorkFlowAuditEntity, authDetailsVo);

			reassign(requestWorkFlowAuditVo, authDetailsVo, requestVo);
			RequestEntity requestEntity = null;

			requestEntity = requestRepository.findOne(requestWorkFlowAuditEntity.getRequestId());

			if (mailMessages.getReassign() == CommonConstant.CONSTANT_ONE) {				
				
				EmailVo emailVo = new EmailVo();
				emailVo.setMessageCode(CommonConstant.REASSIGN);
				emailVo.setGroupId(CommonConstant.RA);
			
				emailVo.setRequestId(requestWorkFlowAuditVo.getRequestId());				
				emailVo.setEmailFlag(0);
				emailVo.setCreateBy(authDetailsVo.getUserId());
				emailVo.setUpdateBy(authDetailsVo.getUserId());
				emailVo.setCreateDate(CommonConstant.getCalenderDate());
				emailVo.setUpdateDate(CommonConstant.getCalenderDate());
				emailVo.setEntityId(authDetailsVo.getEntityId());
				emailVo.setEscalationFlag(CommonConstant.CONSTANT_ZERO);
							
				UserMasterVO userMasterVO1 =	getEmailAddress(requestEntity.getCreateBy(),authDetailsVo);
				emailVo.setToUserAddress(userMasterVO1.getEmailId());
				emailVo.setRequestorName(userMasterVO1.getUserName());	
				emailVo.setUserLang(userMasterVO1.getLangCode());
				
				emailVo.setToUserId(userMasterVO1.getId());
				emailVo.setRequestCode(requestEntity.getRequestCode());								
				emailVo.setToUserId(userMasterVO1.getId());
				emailVo.setUserName(userMasterVO1.getUserName());
				
				//executor
				UserMasterVO userMasterVO2 =	getEmailAddress(requestWorkFlowAuditVo.getReassignUserId(),authDetailsVo);
				emailVo.setExecutorName(userMasterVO2.getUserName()); 
															
				emailVo.setSystemConfigurationVo(getSystemConfigurationDetails(authDetailsVo));
			 													
				EntityLicenseVO  entityLicenseVO = getEntityDetails(authDetailsVo.getEntityId());
				
				RequestWorkFlowEntity requestWorkFlow  = requestConfigurationDAO.getWorkflow(requestWorkFlowAuditEntity.getWorkFlowId());
				
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
			
				if (null != emailVo.getSystemConfigurationVo()) {
					if (null != entityLicenseVO.getEmailFlag() && entityLicenseVO.getEmailFlag() == 1) {
						final String uri = emailRestTemplateUrl + FilePathConstants.TOEMAIL;
						JSONResponse response = restTemplate.postForObject(uri, emailVo, JSONResponse.class);
					}
				}
			}
			requestConfigurationDAO.updateDate(requestEntity.getRequestId(), authDetailsVo);
		} catch (Exception exe) {
			Log.info("Request Configuration Service createReassign Exception",exe);
			throw new CommonException(getMessage("dataFailure", authDetailsVo));
		}
	}

	/**
	 * This method is used to resolver for put Reassign for request and put new
	 * entry for reassign user.
	 * 
	 * @param requestWorkFlowAuditVo
	 *            RequestWorkFlowAuditVo
	 * @throws CommonException
	 */
	@Transactional
	public void reassign(RequestWorkFlowAuditVO requestWorkFlowAuditVo, AuthDetailsVo authDetailsVo,RequestVO requestVo)
			throws CommonException {

		RequestWorkFlowAuditEntity requestWorkFlowAudit = new RequestWorkFlowAuditEntity();
		try {
			BeanUtils.copyProperties(requestWorkFlowAudit, requestWorkFlowAuditVo);

			requestConfigurationDAO.deleteRequester(requestWorkFlowAudit, authDetailsVo);

			RequestWorkFlowVO requestWorkFlowVo = new RequestWorkFlowVO();
			requestWorkFlowVo.setReqWorkFlowId(requestWorkFlowAuditVo.getWorkFlowId());

			requestWorkFlowVo = findrequestWorkFlow(requestWorkFlowVo, authDetailsVo);

			count = 0;

			RequestWorkFlowAuditEntity requestWorkFlowAuditEntity = new RequestWorkFlowAuditEntity();

			requestWorkFlowAuditEntity.setWorkFlowId(requestWorkFlowAuditVo.getWorkFlowId());
			requestWorkFlowAuditEntity.setSeqId(CommonConstant.CONSTANT_ZERO);
			requestWorkFlowAuditEntity.setRequestId(requestWorkFlowAuditVo.getRequestId());
			requestWorkFlowAuditEntity.setUserId(requestWorkFlowAuditVo.getReassignUserId());
			requestWorkFlowAuditEntity.setSequence(CommonConstant.CONSTANT_ZERO);
			requestWorkFlowAuditEntity.setEntityLicenseId(authDetailsVo.getEntityId());
			requestWorkFlowAuditEntity.setApprovalExecuter(CommonConstant.CONSTANT_TWO);
			requestWorkFlowAuditEntity.setGroupId(CommonConstant.CONSTANT_ZERO);
			requestWorkFlowAuditEntity.setRequestWorkflowAuditIsActive(CommonConstant.BOOLEAN_TRUE);

			saveAudit(requestWorkFlowAuditEntity, authDetailsVo, requestVo);

			if (mailMessages.getReassignUser() == CommonConstant.CONSTANT_ONE) {		
			
			EmailVo emailVo = new EmailVo();
			emailVo.setMessageCode(CommonConstant.REASSIGN_USER);
			emailVo.setGroupId(CommonConstant.RAU);
			emailVo.setRequestId(requestWorkFlowAuditVo.getRequestId());
			
			emailVo.setEmailFlag(0);
			emailVo.setCreateBy(authDetailsVo.getUserId());
			emailVo.setUpdateBy(authDetailsVo.getUserId());
			emailVo.setCreateDate(CommonConstant.getCalenderDate());
			emailVo.setUpdateDate(CommonConstant.getCalenderDate());
			emailVo.setEntityId(authDetailsVo.getEntityId());
			emailVo.setEscalationFlag(CommonConstant.CONSTANT_ZERO);
					 
			RequestEntity requestEntity = requestDAO.getRequestDetails(requestWorkFlowAuditEntity.getRequestId());
			 					
			emailVo.setRequestCode(requestEntity.getRequestCode());
			emailVo.setRequestSubject(requestEntity.getRequestSubject());
			
			RequestTypeVO  requestTypeVO = new RequestTypeVO();
			requestTypeVO.setRequestTypeId(requestEntity.getRequestTypeId());
			RequestTypeVO requestType = requestTypeService.view(requestTypeVO,authDetailsVo);
			emailVo.setRequestTypeName(requestType.getRequestTypeName());
			
			RequestSubTypeVO  requestSubTypeVo = new RequestSubTypeVO();
			requestSubTypeVo.setRequestSubTypeId(requestEntity.getRequestSubtypeId());
			requestSubTypeVo = requestSubTypeService.view(requestSubTypeVo, authDetailsVo);
			
			emailVo.setRequestSubTypeName(requestSubTypeVo.getRequestSubTypeName());
		 			 							 			
			emailVo.setSystemConfigurationVo(getSystemConfigurationDetails(authDetailsVo));
		 				
			//executor		 
			UserMasterVO userMasterVO2 = getEmailAddress(requestWorkFlowAuditVo.getReassignUserId(),authDetailsVo);
			emailVo.setToUserAddress(userMasterVO2.getEmailId());
			emailVo.setExecutorName(userMasterVO2.getUserName()); 
			emailVo.setUserLang(userMasterVO2.getLangCode());									
			emailVo.setToUserId(userMasterVO2.getId());
			emailVo.setUserName(userMasterVO2.getUserName());
						 						
			 List<Object[]>  objList = 	requestDAO.getRequestScreenDetails(requestWorkFlowAuditEntity.getRequestId(),authDetailsVo);
			
			 List<RequestDetailVO> requestDetailVOList = new ArrayList<RequestDetailVO>();
			 
			for (Object[] object : objList) {

				RequestDetailVO requestDetailVO = new RequestDetailVO();

				if (null != (String) ((Object[]) object)[1]) {
					requestDetailVO.setRequestScreenDetailConfigurationFieldName((String) object[1]);
				}

				if (null != (String) ((Object[]) object)[2] && !((String) ((Object[]) object)[2]).isEmpty()) {
					String val = (String) ((Object[]) object)[2];
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
			
			emailVo.setRequestDetailList(requestDetailVOList); 				
			
			EntityLicenseVO  entityLicenseVO = getEntityDetails(authDetailsVo.getEntityId());
			
			RequestWorkFlowEntity requestWorkFlow  = requestConfigurationDAO.getWorkflow(requestWorkFlowAuditEntity.getWorkFlowId());
			
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
			
				if (null != emailVo.getSystemConfigurationVo()) {
					if (null != entityLicenseVO.getEmailFlag() && entityLicenseVO.getEmailFlag() == 1) {
						final String uri = emailRestTemplateUrl + FilePathConstants.TOEMAIL;
						JSONResponse response = restTemplate.postForObject(uri, emailVo, JSONResponse.class);

					}
				}
		}
		
		RequestEntity requestEntity = requestRepository.findOne(requestWorkFlowAuditVo.getRequestId());

		RequestWorkFlowAuditEntity reqWorkFlowAudit = new RequestWorkFlowAuditEntity();

		reqWorkFlowAudit.setWorkFlowId(requestWorkFlowAuditVo.getWorkFlowId());
		reqWorkFlowAudit.setSeqId(CommonConstant.CONSTANT_ZERO);
		reqWorkFlowAudit.setRequestId(requestWorkFlowAuditVo.getRequestId());
		reqWorkFlowAudit.setUserId(requestEntity.getCreateBy());
		requestWorkFlowAuditEntity.setEntityLicenseId(authDetailsVo.getEntityId());
		reqWorkFlowAudit.setSequence(CommonConstant.CONSTANT_ZERO);
		reqWorkFlowAudit.setApprovalExecuter(CommonConstant.CONSTANT_THREE);
		reqWorkFlowAudit.setGroupId(CommonConstant.CONSTANT_ZERO);
		reqWorkFlowAudit.setRequestWorkflowAuditIsActive(true);
	 
			saveAudit(reqWorkFlowAudit, authDetailsVo,requestVo);
		} catch (Exception exe) {
			Log.info("Request Configuration Service reassign Exception",exe);
			throw new CommonException(getMessage("dataFailure", authDetailsVo));
		}

	}

	/**
	 * This method is used to requester for put close for request.
	 * 
	 * @param requestWorkFlowAuditVo
	 *            RequestWorkFlowAuditVo
	 * @throws CommonException
	 */
	@Transactional
	public void createClose(RequestWorkFlowAuditVO requestWorkFlowAuditVo, AuthDetailsVo authDetailsVo)
			throws CommonException {

		RequestWorkFlowAuditEntity requestWorkFlowAuditEntity = new RequestWorkFlowAuditEntity();
		try {
			BeanUtils.copyProperties(requestWorkFlowAuditEntity, requestWorkFlowAuditVo);
		} catch (Exception exe) {
			Log.info("Request Configuration Service createClose Exception",exe);
			throw new CommonException(getMessage("beanUtilPropertiesFailure", authDetailsVo));
		}
		if (null != requestWorkFlowAuditVo.getCreatedBy()) {
			requestWorkFlowAuditEntity.setCreateBy(requestWorkFlowAuditVo.getCreatedBy());
			requestWorkFlowAuditEntity.setUpdateBy(requestWorkFlowAuditVo.getUpdatedBy());
		}
		try {

			requestWorkFlowAuditEntity = setCreateUserDetails(requestWorkFlowAuditEntity, authDetailsVo);
			try {
				requestConfigurationDAO.updateDetailInAudit(requestWorkFlowAuditEntity, authDetailsVo);

				requestConfigurationDAO.updateCurrentStatusInRequest(DecisionTypeEnum.CLO.toString(),
						requestWorkFlowAuditEntity, authDetailsVo);

			} catch (Exception exe) {
				Log.info("Request Configuration Service createClose Exception",exe);
				throw new CommonException(getMessage("modifyFailure", authDetailsVo));
			}
			try {
				requestConfigurationDAO.getAllRemarksList(requestWorkFlowAuditEntity, authDetailsVo);
			} catch (Exception exe) {
				Log.info("Request Configuration Service createClose Exception",exe);
				throw new CommonException(getMessage("dataFailure", authDetailsVo));
			}
			if (mailMessages.getClose() == CommonConstant.CONSTANT_ONE) {
				
				//CLOSE
				EmailVo emailVo = new EmailVo();
				emailVo.setMessageCode(CommonConstant.CLOSE);
				emailVo.setGroupId(CommonConstant.CL);
				//emailVo.setToUserId(requestEntity.getCreateBy());
				emailVo.setRequestId(requestWorkFlowAuditVo.getRequestId());				
				emailVo.setEmailFlag(0);
				emailVo.setCreateBy(authDetailsVo.getUserId());
				emailVo.setUpdateBy(authDetailsVo.getUserId());
				emailVo.setCreateDate(CommonConstant.getCalenderDate());
				emailVo.setUpdateDate(CommonConstant.getCalenderDate());
				emailVo.setEntityId(authDetailsVo.getEntityId());
				emailVo.setEscalationFlag(CommonConstant.CONSTANT_ZERO);
					
				RequestEntity requestEntity = requestDAO.getRequestDetails(requestWorkFlowAuditEntity.getRequestId());
				emailVo.setRequestCode(requestEntity.getRequestCode());		
				
				UserMasterVO userMasterVO1 =	getEmailAddress(requestEntity.getCreateBy(),authDetailsVo);
				emailVo.setToUserAddress(userMasterVO1.getEmailId());
				emailVo.setRequestorName(userMasterVO1.getUserName());	
				emailVo.setUserLang(userMasterVO1.getLangCode());
				emailVo.setToUserId(userMasterVO1.getId());
				emailVo.setUserName(userMasterVO1.getUserName());
				
				emailVo.setSystemConfigurationVo(getSystemConfigurationDetails(authDetailsVo));
			 						
				EntityLicenseVO  entityLicenseVO = getEntityDetails(authDetailsVo.getEntityId());
				
				RequestWorkFlowEntity requestWorkFlow  = requestConfigurationDAO.getWorkflow(requestWorkFlowAuditEntity.getWorkFlowId());
				
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

				if (null != emailVo.getSystemConfigurationVo()) {
					if (null != entityLicenseVO.getEmailFlag() && entityLicenseVO.getEmailFlag() == 1) {
						final String uri = emailRestTemplateUrl + FilePathConstants.TOEMAIL;
						JSONResponse response = restTemplate.postForObject(uri, emailVo, JSONResponse.class);
					}
				}
							 				
			}
			requestConfigurationDAO.updateDate(requestWorkFlowAuditVo.getRequestId(), authDetailsVo);
		} catch (Exception exe) {
			Log.info("Request Configuration Service createClose Exception",exe);
			throw new CommonException(getMessage("dataFailure", authDetailsVo));
		}

	}

	/**
	 * This method is used to requester for put reopen for request.
	 * 
	 * @param requestWorkFlowAuditVo
	 *            RequestWorkFlowAuditVo
	 * @throws CommonException
	 */
	@Transactional
	public void createReopen(RequestWorkFlowAuditVO requestWorkFlowAuditVo, AuthDetailsVo authDetailsVo , RequestVO requestVo)
			throws CommonException {

		RequestWorkFlowAuditEntity requestWorkFlowAuditEntity = new RequestWorkFlowAuditEntity();
		try {
			BeanUtils.copyProperties(requestWorkFlowAuditEntity, requestWorkFlowAuditVo);
		} catch (Exception exe) {
			Log.info("Request Configuration Service createReopen Exception",exe);
			throw new CommonException(getMessage("beanUtilPropertiesFailure", authDetailsVo));
		}
		if (null != requestWorkFlowAuditVo.getCreatedBy()) {
			requestWorkFlowAuditEntity.setCreateBy(requestWorkFlowAuditVo.getCreatedBy());
			requestWorkFlowAuditEntity.setUpdateBy(requestWorkFlowAuditVo.getUpdatedBy());
		}
		try {

			requestWorkFlowAuditEntity = setCreateUserDetails(requestWorkFlowAuditEntity, authDetailsVo);
			try {
				requestConfigurationDAO.updateDetailInAudit(requestWorkFlowAuditEntity, authDetailsVo);
				requestConfigurationDAO.deleteRequester(requestWorkFlowAuditEntity, authDetailsVo);
				requestConfigurationDAO.updateCurrentStatusInRequest(DecisionTypeEnum.RO.toString(),
						requestWorkFlowAuditEntity, authDetailsVo);
			} catch (Exception exe) {
				Log.info("Request Configuration Service createReopen Exception",exe);
				throw new CommonException(getMessage("modifyFailure", authDetailsVo));
			}
			try {
				requestConfigurationDAO.getAllRemarksList(requestWorkFlowAuditEntity, authDetailsVo);
			} catch (Exception exe) {
				Log.info("Request Configuration Service createReopen Exception",exe);
				throw new CommonException(getMessage("dataFailure", authDetailsVo));
			}
			
			//set location & sublocation ID
			RequestEntity requestEntity = requestRepository.findOne(requestVo.getRequestId());
			
			requestVo.setReqLocationId(requestEntity.getReqLocationId());
			requestVo.setReqSublocationId(requestEntity.getReqSublocationId());
			
			reopen(requestWorkFlowAuditVo, authDetailsVo, requestVo);

			if (mailMessages.getReopen() == CommonConstant.CONSTANT_ONE) {			
				
				//Reopen
				EmailVo emailVo = new EmailVo();
				emailVo.setMessageCode(CommonConstant.REOPEN1);
				emailVo.setGroupId(CommonConstant.RO);
				//emailVo.setToUserId(requestEntity.getCreateBy());
				emailVo.setRequestId(requestWorkFlowAuditVo.getRequestId());				
				emailVo.setEmailFlag(0);
				emailVo.setCreateBy(authDetailsVo.getUserId());
				emailVo.setUpdateBy(authDetailsVo.getUserId());
				emailVo.setCreateDate(CommonConstant.getCalenderDate());
				emailVo.setUpdateDate(CommonConstant.getCalenderDate());
				emailVo.setEntityId(authDetailsVo.getEntityId());
				emailVo.setEscalationFlag(CommonConstant.CONSTANT_ZERO);
								
				RequestEntity requestEntity1 = requestDAO.getRequestDetails(requestWorkFlowAuditEntity.getRequestId());
				emailVo.setRequestCode(requestEntity1.getRequestCode());
				emailVo.setRequestSubject(requestEntity1.getRequestSubject());	
				
				UserMasterVO userMasterVO1 =	getEmailAddress(requestEntity.getCreateBy(),authDetailsVo);
				emailVo.setToUserAddress(userMasterVO1.getEmailId());
				emailVo.setRequestorName(userMasterVO1.getUserName());		
				emailVo.setUserLang(userMasterVO1.getLangCode());							
				emailVo.setToUserId(userMasterVO1.getId());
				emailVo.setUserName(userMasterVO1.getUserName());
				
				emailVo.setSystemConfigurationVo(getSystemConfigurationDetails(authDetailsVo));
			 																	
				EntityLicenseVO  entityLicenseVO = getEntityDetails(authDetailsVo.getEntityId());
				
				RequestWorkFlowEntity requestWorkFlow  = requestConfigurationDAO.getWorkflow(requestWorkFlowAuditEntity.getWorkFlowId());
				
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
			
				if (null != emailVo.getSystemConfigurationVo()) {
					if (null != entityLicenseVO.getEmailFlag() && entityLicenseVO.getEmailFlag() == 1) {
						final String uri = emailRestTemplateUrl + FilePathConstants.TOEMAIL;
						JSONResponse response = restTemplate.postForObject(uri, emailVo, JSONResponse.class);

					}
				}
			}

			requestConfigurationDAO.updateDate(requestWorkFlowAuditVo.getRequestId(), authDetailsVo);
		} catch (Exception exe) {
			exe.printStackTrace();
			Log.info("Request Configuration Service createReopen Exception",exe);
			throw new CommonException(getMessage("dataFailure", authDetailsVo));
		}

	}

	/**
	 * This method is used to requester for put reopen for request and put the
	 * again entry for resolver.
	 * 
	 * @param requestWorkFlowAuditVo
	 * @throws CommonException
	 * @throws ParseException 
	 */
	@Transactional
	public void reopen(RequestWorkFlowAuditVO requestWorkFlowAuditVo, AuthDetailsVo authDetailsVo, 
			RequestVO requestVo)
			throws CommonException, ParseException {
		RequestWorkFlowVO requestWorkFlowVo = new RequestWorkFlowVO();
		
		try {
		requestWorkFlowVo.setReqWorkFlowId(requestWorkFlowAuditVo.getWorkFlowId());
		 
		requestWorkFlowVo = findrequestWorkFlow(requestWorkFlowVo, authDetailsVo);
		 
		RequestWorkFlowEntity requestWorkFlowEntity = new RequestWorkFlowEntity();
		 
		BeanUtils.copyProperties(requestWorkFlowEntity, requestWorkFlowVo);
		 

		int sequence = requestWorkFlowAuditVo.getSequence();

		count = CommonConstant.CONSTANT_ZERO;

		List<RequestWorkFlowExecuterVO> requestWorkFlowExecuterVoList = new ArrayList<RequestWorkFlowExecuterVO>();
		RequestEntity requestEntity = new RequestEntity();

		requestEntity = requestRepository.findOne(requestWorkFlowAuditVo.getRequestId());

		List<Object[]> workFlowExecuterEntity = null;
		try {
			workFlowExecuterEntity = requestConfigurationDAO.findWorkFlowExeSla(requestWorkFlowVo.getReqWorkFlowId(),
					requestEntity.getRequestPriority(), authDetailsVo);
		} catch (Exception exe) {
			Log.info("Request Configuration Service reopen Exception",exe);
			throw new CommonException(getMessage("dataFailure", authDetailsVo));
		}

		if (workFlowExecuterEntity != null) {
			for (Object[] object1 : workFlowExecuterEntity) {
				 
					RequestWorkFlowExecuterVO requestWorkFlowExecuterVo = new RequestWorkFlowExecuterVO();

					BeanUtils.copyProperties(requestWorkFlowExecuterVo, object1[0]);
					requestWorkFlowExecuterVo.setSlaType((int) object1[1]);
					requestWorkFlowExecuterVo.setWorkFlowSla((float) object1[2]);

					if ((boolean) object1[3] == true) {
						requestWorkFlowExecuterVo.setExecutorWeekend(true);
					} else {

						requestWorkFlowExecuterVo.setExecutorWeekend(false);
					}
					if ((boolean) object1[4] == true) {
						requestWorkFlowExecuterVo.setExecutorHoliday(true);
					} else {
						requestWorkFlowExecuterVo.setExecutorHoliday(false);
					}														
					
					requestWorkFlowExecuterVoList.add(requestWorkFlowExecuterVo);
			 
			}
		}
		int minutes;

		for (RequestWorkFlowExecuterVO requestWorkFlowExecuter : requestWorkFlowExecuterVoList) {

			int count = executerWorkflowCheck(requestEntity, requestWorkFlowExecuter);

			if (count == 1) {

				if (requestWorkFlowExecuter.getExecuterUserId() == null || requestWorkFlowExecuter.getExecuterUserId()==0) {

					RequestWorkFlowAuditEntity requestWorkFlowAuditEntity = null;
					requestWorkFlowExecuter.setEntityId(requestWorkFlowAuditVo.getEntityId());
					List<UserEntity> userEntityList = requestConfigurationDAO
							.findExecuterUserId(requestWorkFlowExecuter, authDetailsVo);
					if (userEntityList.size() > CommonConstant.CONSTANT_ONE) {
						count = count + CommonConstant.CONSTANT_ONE;
					} else {
						count = CommonConstant.CONSTANT_ZERO;
					}
					for (UserEntity userEntity : userEntityList) {

						requestWorkFlowAuditEntity = new RequestWorkFlowAuditEntity();

						requestWorkFlowAuditEntity.setWorkFlowId(requestWorkFlowAuditVo.getWorkFlowId());
						requestWorkFlowAuditEntity.setSeqId(CommonConstant.CONSTANT_ZERO);
						requestWorkFlowAuditEntity.setRequestId(requestWorkFlowAuditVo.getRequestId());
						requestWorkFlowAuditEntity.setUserId(userEntity.getId());
						if (null != authDetailsVo.getEntityId()) {
							requestWorkFlowAuditEntity.setEntityLicenseId(authDetailsVo.getEntityId());
						} else {
							requestWorkFlowAuditEntity.setEntityLicenseId(requestWorkFlowExecuter.getEntityId());
						}
						requestWorkFlowAuditEntity.setSequence(sequence++);
						requestWorkFlowAuditEntity.setApprovalExecuter(CommonConstant.CONSTANT_TWO);
						requestWorkFlowAuditEntity.setGroupId(count);
						requestWorkFlowAuditEntity.setRequestWorkflowAuditIsActive(CommonConstant.BOOLEAN_TRUE);
						
						boolean weekendFlag  = false;
						boolean holidayFlag =  false;
						
						if(requestWorkFlowExecuter.isExecutorWeekend()){
							weekendFlag = true;
						}
						
						if(requestWorkFlowExecuter.isExecutorHoliday()){
							holidayFlag = true;
						}		
						
						UserMasterVO userMasterVo = getEmailAddress(requestWorkFlowAuditEntity.getUserId(),authDetailsVo);
												
						minutes = calculateSLATime(requestWorkFlowExecuter.getSlaType(),
								requestWorkFlowExecuter.getWorkFlowSla(),weekendFlag,holidayFlag, 
								userMasterVo.getUserLocation() , userMasterVo.getSubLocation(),authDetailsVo);
						
						requestWorkFlowAuditEntity.setRequestWorkflowAuditSla(minutes);
						if (null != requestWorkFlowAuditVo.getCreatedBy()) {
							requestWorkFlowAuditEntity.setCreateBy(requestWorkFlowAuditVo.getCreatedBy());
							requestWorkFlowAuditEntity.setUpdateBy(requestWorkFlowAuditVo.getUpdatedBy());
						}
					 
						saveAudit(requestWorkFlowAuditEntity, authDetailsVo,requestVo);
					 
						if (mailMessages.getReopenExecuter() == CommonConstant.CONSTANT_ONE) {
							
							EmailVo emailVo = new EmailVo();
							emailVo.setMessageCode(CommonConstant.REOPEN_EXECUTER);
							emailVo.setGroupId(CommonConstant.ROE);
							//emailVo.setToUserId(requestEntity.getCreateBy());
							emailVo.setRequestId(requestWorkFlowAuditVo.getRequestId());
							emailVo.setRequestCode(requestWorkFlowAuditVo.getRequestCode());
							emailVo.setEmailFlag(0);
							emailVo.setCreateBy(authDetailsVo.getUserId());
							emailVo.setUpdateBy(authDetailsVo.getUserId());
							emailVo.setCreateDate(CommonConstant.getCalenderDate());
							emailVo.setUpdateDate(CommonConstant.getCalenderDate());
							emailVo.setEntityId(authDetailsVo.getEntityId());
							emailVo.setEscalationFlag(CommonConstant.CONSTANT_ZERO);
							
							//RequestEntity requestEntity = requestDAO.getRequestDetails(requestWorkFlowAuditEntity.getRequestId());
							emailVo.setRequestCode(requestEntity.getRequestCode());
							emailVo.setRequestSubject(requestEntity.getRequestSubject());
							
							RequestTypeVO  requestTypeVO = new RequestTypeVO();
							requestTypeVO.setRequestTypeId(requestEntity.getRequestTypeId());
							RequestTypeVO requestType = requestTypeService.view(requestTypeVO,authDetailsVo);
							emailVo.setRequestTypeName(requestType.getRequestTypeName());
							
							RequestSubTypeVO  requestSubTypeVo = new RequestSubTypeVO();
							requestSubTypeVo.setRequestSubTypeId(requestEntity.getRequestSubtypeId());
							requestSubTypeVo = requestSubTypeService.view(requestSubTypeVo, authDetailsVo);
							
							emailVo.setRequestSubTypeName(requestSubTypeVo.getRequestSubTypeName());
																											
							UserMasterVO userMasterVO1 =	getEmailAddress(requestEntity.getCreateBy(),authDetailsVo);
							emailVo.setToUserAddress(userMasterVO1.getEmailId());
							emailVo.setRequestorName(userMasterVO1.getUserName());	
							emailVo.setUserLang(userMasterVO1.getLangCode());														
							emailVo.setToUserId(userMasterVO1.getId());
							emailVo.setUserName(userMasterVO1.getUserName());
														
							emailVo.setSystemConfigurationVo(getSystemConfigurationDetails(authDetailsVo));
						 				
							//executor
							UserMasterVO userMasterVO2 =	getEmailAddress(requestWorkFlowExecuter.getExecuterUserId(),authDetailsVo);
							emailVo.setExecutorName(userMasterVO2.getUserName()); 
							 							
							 List<Object[]>  objList = 	requestDAO.getRequestScreenDetails(requestWorkFlowAuditEntity.getRequestId(),authDetailsVo);
							
							 List<RequestDetailVO> requestDetailVOList = new ArrayList<RequestDetailVO>();
							 
							for (Object[] object : objList) {

								RequestDetailVO requestDetailVO = new RequestDetailVO();

								if (null != (String) ((Object[]) object)[1]) {
									requestDetailVO.setRequestScreenDetailConfigurationFieldName((String) object[1]);
								}

								if (null != (String) ((Object[]) object)[2] && !((String) ((Object[]) object)[2]).isEmpty()) {
									String val = (String) ((Object[]) object)[2];
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
							
							emailVo.setRequestDetailList(requestDetailVOList);
																			
							EntityLicenseVO  entityLicenseVO = getEntityDetails(authDetailsVo.getEntityId());
							
							RequestWorkFlowEntity requestWorkFlow  = requestConfigurationDAO.getWorkflow(requestWorkFlowAuditEntity.getWorkFlowId());
							
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
						
								if (null != emailVo.getSystemConfigurationVo()) {
									if (null != entityLicenseVO.getEmailFlag() && entityLicenseVO.getEmailFlag() == 1) {
										final String uri = emailRestTemplateUrl + FilePathConstants.TOEMAIL;
										JSONResponse response = restTemplate.postForObject(uri, emailVo,
												JSONResponse.class);
									}
								}
							
						}
					}
				} else {

					RequestWorkFlowAuditEntity requestWorkFlowAuditEntity = new RequestWorkFlowAuditEntity();

					requestWorkFlowAuditEntity.setWorkFlowId(requestWorkFlowAuditVo.getWorkFlowId());
					requestWorkFlowAuditEntity.setSeqId(CommonConstant.CONSTANT_ZERO);
					requestWorkFlowAuditEntity.setRequestId(requestWorkFlowAuditVo.getRequestId());
					requestWorkFlowAuditEntity.setUserId(requestWorkFlowExecuter.getExecuterUserId());
					requestWorkFlowAuditEntity.setSequence(sequence++);
					if (null != authDetailsVo.getEntityId()) {
						requestWorkFlowAuditEntity.setEntityLicenseId(authDetailsVo.getEntityId());
					} else {
						requestWorkFlowAuditEntity.setEntityLicenseId(requestWorkFlowExecuter.getEntityId());
					}
					requestWorkFlowAuditEntity.setApprovalExecuter(CommonConstant.CONSTANT_TWO);
					requestWorkFlowAuditEntity.setGroupId(CommonConstant.CONSTANT_ZERO);
					requestWorkFlowAuditEntity.setRequestWorkflowAuditIsActive(CommonConstant.BOOLEAN_TRUE);
										
					boolean weekendFlag  = false;
					boolean holidayFlag =  false;
					
					if(requestWorkFlowExecuter.isExecutorWeekend()){
						weekendFlag = true;
					}
					
					if(requestWorkFlowExecuter.isExecutorHoliday()){
						holidayFlag = true;
					}		
									
					UserMasterVO userMasterVo = getEmailAddress(requestWorkFlowAuditEntity.getUserId(),authDetailsVo);
					
					minutes = calculateSLATime(requestWorkFlowExecuter.getSlaType(),
							requestWorkFlowExecuter.getWorkFlowSla(),weekendFlag,holidayFlag,
							userMasterVo.getUserLocation() , userMasterVo.getSubLocation(),authDetailsVo);
					
					requestWorkFlowAuditEntity.setRequestWorkflowAuditSla(minutes);
					if (null != requestWorkFlowAuditVo.getCreatedBy()) {
						requestWorkFlowAuditEntity.setCreateBy(requestWorkFlowAuditVo.getCreatedBy());
						requestWorkFlowAuditEntity.setUpdateBy(requestWorkFlowAuditVo.getUpdatedBy());
					}
				 
					saveAudit(requestWorkFlowAuditEntity, authDetailsVo,requestVo);
					 
					if (mailMessages.getReopenExecuter() == CommonConstant.CONSTANT_ONE) {				
						
						EmailVo emailVo = new EmailVo();
						emailVo.setMessageCode(CommonConstant.REOPEN_EXECUTER);
						emailVo.setGroupId(CommonConstant.ROE);
						//emailVo.setToUserId(requestEntity.getCreateBy());
						emailVo.setRequestId(requestWorkFlowAuditVo.getRequestId());
						//emailVo.setRequestCode(requestWorkFlowAuditVo.getRequestCode());
						emailVo.setEmailFlag(0);
						emailVo.setCreateBy(authDetailsVo.getUserId());
						emailVo.setUpdateBy(authDetailsVo.getUserId());
						emailVo.setCreateDate(CommonConstant.getCalenderDate());
						emailVo.setUpdateDate(CommonConstant.getCalenderDate());
						emailVo.setEntityId(authDetailsVo.getEntityId());
						emailVo.setEscalationFlag(CommonConstant.CONSTANT_ZERO);
															
						emailVo.setRequestCode(requestEntity.getRequestCode());
						emailVo.setRequestSubject(requestEntity.getRequestSubject());
						
						RequestTypeVO  requestTypeVO = new RequestTypeVO();
						requestTypeVO.setRequestTypeId(requestEntity.getRequestTypeId());
						RequestTypeVO requestType = requestTypeService.view(requestTypeVO,authDetailsVo);
						emailVo.setRequestTypeName(requestType.getRequestTypeName());
						
						RequestSubTypeVO  requestSubTypeVo = new RequestSubTypeVO();
						requestSubTypeVo.setRequestSubTypeId(requestEntity.getRequestSubtypeId());
						requestSubTypeVo = requestSubTypeService.view(requestSubTypeVo, authDetailsVo);
						
						emailVo.setRequestSubTypeName(requestSubTypeVo.getRequestSubTypeName());
																										
						UserMasterVO userMasterVO1 = getEmailAddress(requestEntity.getCreateBy(),authDetailsVo);
						emailVo.setToUserAddress(userMasterVO1.getEmailId());
						emailVo.setRequestorName(userMasterVO1.getUserName());	
						emailVo.setUserLang(userMasterVO1.getLangCode());										
						emailVo.setToUserId(userMasterVO1.getId());
						emailVo.setUserName(userMasterVO1.getUserName());
						
						//executor
						UserMasterVO userMasterVO2 =	getEmailAddress(requestWorkFlowExecuter.getExecuterUserId(),authDetailsVo);
						emailVo.setExecutorName(userMasterVO2.getUserName()); 
																	
						emailVo.setSystemConfigurationVo(getSystemConfigurationDetails(authDetailsVo));
					 				
						 List<Object[]>  objList = 	requestDAO.getRequestScreenDetails(requestWorkFlowAuditEntity.getRequestId(),authDetailsVo);
						
						 List<RequestDetailVO> requestDetailVOList = new ArrayList<RequestDetailVO>();
						 
						for (Object[] object : objList) {

							RequestDetailVO requestDetailVO = new RequestDetailVO();

							if (null != (String) ((Object[]) object)[1]) {
								requestDetailVO.setRequestScreenDetailConfigurationFieldName((String) object[1]);
							}

							if (null != (String) ((Object[]) object)[2] && !((String) ((Object[]) object)[2]).isEmpty()) {
								String val = (String) ((Object[]) object)[2];
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
						
						emailVo.setRequestDetailList(requestDetailVOList);

						EntityLicenseVO entityLicenseVO = getEntityDetails(authDetailsVo.getEntityId());

						RequestWorkFlowEntity requestWorkFlow = requestConfigurationDAO
								.getWorkflow(requestWorkFlowAuditEntity.getWorkFlowId());

						if (requestWorkFlow.isReqWorkFlowIsMailRequired()) {
							emailVo.setTrEmailFlag(CommonConstant.CONSTANT_ONE);
						} else {
							emailVo.setTrEmailFlag(CommonConstant.CONSTANT_ZERO);
						}

						if (requestWorkFlow.isReqWorkFlowIsNotificationRequired()) {
							emailVo.setNotificationFlag(CommonConstant.CONSTANT_ZERO);
						} else {
							emailVo.setNotificationFlag(CommonConstant.CONSTANT_ONE);
						}

							if (null != emailVo.getSystemConfigurationVo()) {
								if (null != entityLicenseVO.getEmailFlag() && entityLicenseVO.getEmailFlag() == 1) {
									final String uri = emailRestTemplateUrl + FilePathConstants.TOEMAIL;
									JSONResponse response = restTemplate.postForObject(uri, emailVo,
											JSONResponse.class);

								}
							}
						}
				}

			}

		}

		RequestWorkFlowAuditEntity requestWorkFlowAuditEntity = new RequestWorkFlowAuditEntity();
		requestWorkFlowAuditEntity.setWorkFlowId(requestWorkFlowAuditVo.getWorkFlowId());
		requestWorkFlowAuditEntity.setSeqId(CommonConstant.CONSTANT_ZERO);
		requestWorkFlowAuditEntity.setRequestId(requestWorkFlowAuditVo.getRequestId());
		requestWorkFlowAuditEntity.setUserId(requestWorkFlowAuditVo.getUserId());
		requestWorkFlowAuditEntity.setSequence(sequence++);
		if (null != authDetailsVo.getEntityId()) {
			requestWorkFlowAuditEntity.setEntityLicenseId(authDetailsVo.getEntityId());
		} else {
			requestWorkFlowAuditEntity.setEntityLicenseId(requestWorkFlowAuditVo.getEntityId());
		}
		requestWorkFlowAuditEntity.setApprovalExecuter(CommonConstant.CONSTANT_THREE);
		requestWorkFlowAuditEntity.setGroupId(CommonConstant.CONSTANT_ZERO);
		requestWorkFlowAuditEntity.setRequestWorkflowAuditIsActive(CommonConstant.BOOLEAN_TRUE);
		if (null != requestWorkFlowAuditVo.getCreatedBy()) {
			requestWorkFlowAuditEntity.setCreateBy(requestWorkFlowAuditVo.getCreatedBy());
			requestWorkFlowAuditEntity.setUpdateBy(requestWorkFlowAuditVo.getUpdatedBy());
		}
		
			saveAudit(requestWorkFlowAuditEntity, authDetailsVo,requestVo);
			
		}catch (CommonException exe) {
			Log.info("Request Configuration Service reopen Exception",exe);
			throw new CommonException(getMessage(exe.getMessage(), authDetailsVo));
		} 		
		catch (Exception exe) {
			Log.info("Request Configuration Service reopen Exception",exe);
			throw new CommonException(getMessage("dataFailure", authDetailsVo));
		}
	}

	/*@Transactional
	public void updateMailLog(MailParameterVO mailParameterVo, AuthDetailsVo authDetailsVo) {

		requestConfigurationDAO.updateMailLog(mailParameterVo, authDetailsVo);

	}*/
	
	@Transactional
	public void updateCancelDecision(RequestVO requestVo, AuthDetailsVo authDetailsVo) throws CommonException {
		try {
			int decision = CommonConstant.CONSTANT_NINE;
			requestConfigurationDAO.updateCancelDecision(requestVo, decision, authDetailsVo);

		} catch (CommonException e) {
			throw new CommonException(e.getMessage());
		}
	}
	
}
