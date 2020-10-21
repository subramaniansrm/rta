package com.srm.rta.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.controller.CommonController;
import com.srm.coreframework.entity.UserMappingEntity;
import com.srm.coreframework.response.JSONResponse;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.util.DateUtil;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.rta.dao.RequestResolverDAO;
import com.srm.rta.entity.RequestEntity;
import com.srm.rta.vo.RequestResolverVO;
import com.srm.rta.vo.RequestVO;
import com.srm.rta.vo.RequestWorkFlowAuditVO;

@Service
public class RequestResolverService extends CommonController<RequestVO> {

	Logger logger = LoggerFactory.getLogger(RequestResolverService.class);

	@Autowired
	RequestResolverDAO requestResolverDAO;

	/**
	 * 
	 * Method used to get Resolver List
	 * 
	 * @param requestVo
	 * @return
	 */

	@Transactional
	public List<RequestResolverVO> getResolverList(RequestResolverVO requestResolverVO,AuthDetailsVo authDetailsVo) {
		try {

			List<Object[]> listRequestEntity = null;

			listRequestEntity = requestResolverDAO.getResolverList(requestResolverVO,authDetailsVo);

			List<RequestResolverVO> requestVoList = new ArrayList<RequestResolverVO>();
			requestVoList = getAllResolverList(listRequestEntity);
			return requestVoList;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

	}

	public List<RequestResolverVO> getAllResolverList(List<Object[]> listrequestEntity) {
		List<RequestResolverVO> requestResolverVOList = new ArrayList<RequestResolverVO>();
		for (Object[] object : listrequestEntity) {

			RequestResolverVO requestResolverVO = new RequestResolverVO();
			if (null != (String) ((Object[]) object)[0]) {

				requestResolverVO.setUserName((String) object[0]);
			}

			if (null != (Integer) ((Object[]) object)[1]) {
				requestResolverVO.setRequestId((Integer) object[1]);
			}

			if (null != (String) ((Object[]) object)[2]) {
				requestResolverVO.setRequestCode((String) object[2]);

			}

			if (null != (String) ((Object[]) object)[4]) {
				requestResolverVO.setRequestTypeName((String) object[4]);
			}

			if (null != (String) ((Object[]) object)[6]) {
				requestResolverVO.setRequestSubTypeName((String) object[6]);
			}

			if (null != (Date) ((Object[]) object)[7]) {
				requestResolverVO.setRequestDate((Date) object[7]);
			}

			if (null != (String) ((Object[]) object)[9]) {
				requestResolverVO.setUserDepartmentName((String) object[9]);
			}

			if (null != (String) ((Object[]) object)[11]) {
				requestResolverVO.setLocationName((String) object[11]);
			}

			if (null != (String) ((Object[]) object)[13]) {
				requestResolverVO.setSublocationName((String) object[13]);
			}

			if (null != (String) ((Object[]) object)[15]) {
				requestResolverVO.setCurrentStatusName((String) object[15]);
			}
			
			if (null !=  object[26]) {
				requestResolverVO.setRequesterId(Integer.parseInt(String.valueOf(object[26])));
			}

			if (null != (String) ((Object[]) object)[27]) {
				requestResolverVO.setForwardRedirectRemarks((String) object[27]);
			}
			
			if (null !=  object[28]) {
				requestResolverVO.setForwardRequestId(Integer.parseInt(String.valueOf(object[28])));
			}
			
			if (null !=  object[29]) {
				requestResolverVO.setRedirectRequestId(Integer.parseInt(String.valueOf(object[29])));
			}
			
			if (null != (String) ((Object[]) object)[30]) {
				requestResolverVO.setResolverRemarks((String) object[30]);
			}
			
			
			if (null != (String) ((Object[]) object)[31]) {

				requestResolverVO.setCurrentStatusCode((String) object[31]);
			}
			
			requestResolverVOList.add(requestResolverVO);

		}

		return requestResolverVOList;

	}

	@Transactional
	public List<RequestVO> getAwaitingResolverList(RequestVO request,AuthDetailsVo authDetailsVo) {
		try {

			List<Object[]> listRequestEntity = null;
			listRequestEntity = requestResolverDAO.getAwaitingResolverList(authDetailsVo);
			List<RequestVO> requestVoList = new ArrayList<RequestVO>();
			requestVoList = getAllList(listRequestEntity);
			return requestVoList;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

	}

	@Transactional
	public RequestVO getResolverListCount(AuthDetailsVo authDetailsVo) {
		try {

			RequestVO requestVo = new RequestVO();

			int count = 0;

			count = requestResolverDAO.getResolverListCount(authDetailsVo);

			if (count != 0) {
				requestVo.setTotalRecords(count);
			}
			return requestVo;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

	}

	/**
	 * 
	 * Method used to get List of values
	 * 
	 * @param listrequestEntity
	 * @return
	 */
	public List<RequestVO> getAllList(List<Object[]> listrequestEntity) {
		List<RequestVO> list_RequestVo = new ArrayList<RequestVO>();
		for (Object[] object : listrequestEntity) {
			RequestVO requestVo = new RequestVO();
			RequestWorkFlowAuditVO requestWorkFlowAuditVo = new RequestWorkFlowAuditVO();

			if (null != (String) ((Object[]) object)[0]) {

				requestVo.setUserName((String) object[0]);
			}
			requestVo.setRequestId((int) object[1]);
			requestVo.setRequestCode((String) object[2]);
			requestVo.setRequestTypeId((int) object[3]);
			requestVo.setRequestTypeName((String) object[4]);
			requestVo.setRequestSubtypeId((int) object[5]);
			requestVo.setRequestSubTypeName((String) object[6]);
			requestVo.setRequestDate((Date) object[7]);
			requestVo.setDepartmentId((int) object[8]);
			requestVo.setUserDepartmentName((String) object[9]);
			requestVo.setId((int) object[10]);
			requestVo.setLocationName((String) object[11]);
			requestVo.setSublocationId((int) object[12]);
			requestVo.setSublocationName((String) object[13]);
			requestVo.setCurrentStatusId((int) object[14]);
			requestVo.setCurrentStatusName((String) object[15]);
			requestWorkFlowAuditVo.setRequestWorkFlowAuditId((int) object[16]);
			requestWorkFlowAuditVo.setWorkFlowId((int) object[17]);
			requestWorkFlowAuditVo.setSeqId((int) object[18]);
			requestWorkFlowAuditVo.setUserId((int) object[19]);
			requestWorkFlowAuditVo.setGroupId((int) object[20]);
			requestWorkFlowAuditVo.setSequence((int) object[21]);
			requestWorkFlowAuditVo.setDescisionType((int) object[22]);
			requestWorkFlowAuditVo.setApprovalExecuter((int) object[23]);
			requestWorkFlowAuditVo.setRemarks((String) object[24]);
			/*
			 * requestWorkFlowAuditVo.setRequestScreenConfigId((int)
			 * object[26]);
			 * requestWorkFlowAuditVo.setRequestScreenDetailConfigId((int)
			 * object[27]);
			 */
			requestVo.setRequestWorkFlowAuditVo(requestWorkFlowAuditVo);

			list_RequestVo.add(requestVo);
		}

		return list_RequestVo;

	}

	/**
	 * Method used to view all approval and resolver list
	 * 
	 * @param requestVo
	 * @return
	 */

	public List<RequestVO> viewAllResolverList(RequestVO requestVo1,AuthDetailsVo authDetailsVo) {
		// RequestVo requestVo = new RequestVo();
		try {
			List<RequestVO> listRequestVo = new ArrayList<RequestVO>();
			List<Object[]> listRequestEntity = null;
			listRequestEntity = requestResolverDAO.viewAllResolverList(requestVo1);
			listRequestVo = getViewAllResolver(listRequestEntity);
			return listRequestVo;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
	}

	private List<RequestVO> getViewAllResolver(List<Object[]> listRequestEntity) {

		Date updateDate = null;
		int groupId = 0;
		int actualSlaTime = 0;

		List<RequestVO> list_RequestVo = new ArrayList<RequestVO>();
		for (Object[] object : listRequestEntity) {
			RequestVO requestVo = new RequestVO();
			RequestWorkFlowAuditVO requestWorkFlowAuditVo = new RequestWorkFlowAuditVO();
			if (null != (String) ((Object[]) object)[0]) {
				requestVo.setUserName((String) object[0]);
			}
			
			if(null != object[1]){
				requestVo.setRequestId((int) object[1]);	
			}
			
			if(null != object[2]){
				requestVo.setRequestCode((String) object[2]);
			}
			
			if(null != object[3]){
				requestVo.setRequestTypeId((int) object[3]);
			}
			
			if(null != object[4]){
				requestVo.setRequestTypeName((String) object[4]);
			}
			
			if (null != object[5]) {
				requestVo.setRequestSubtypeId((int) object[5]);
			}
			
			if(null != object[6]){
				requestVo.setRequestSubTypeName((String) object[6]);
			}
			
			if(null != object[7]){
				requestVo.setRequestDate((Date) object[7]);
			}
		
			if(null !=  object[8]){
				requestVo.setDepartmentId((int) object[8]);
			}
		
			if(null != object[9]){
				requestVo.setUserDepartmentName((String) object[9]);
			}
			
			if(null != object[10]){
				requestVo.setId((int) object[10]);
			}
			
			if(null != object[11]){
				requestVo.setLocationName((String) object[11]);
			}
			
			if(null != object[12]){
				requestVo.setSublocationId((int) object[12]);
			}
			
			if(null != object[13]){
				requestVo.setSublocationName((String) object[13]);
			}
			
			if(null != object[14]){
				requestVo.setCurrentStatusId((int) object[14]);
			}
			
			if(null != object[15]){
				requestVo.setCurrentStatusName((String) object[15]);
			}
			
			if(null != object[16]){
				requestWorkFlowAuditVo.setRequestWorkFlowAuditId((int) object[16]);
			}
			
			if(null != object[17]){
				requestWorkFlowAuditVo.setWorkFlowId((int) object[17]);
			}
			
			if(null != object[18]){
				requestWorkFlowAuditVo.setSeqId((int) object[18]);
			}
			
			if(null != object[19]){
				requestWorkFlowAuditVo.setUserId((int) object[19]);
			}
		
			if(null != object[20]){
				requestWorkFlowAuditVo.setGroupId((int) object[20]);
			}
			
			if(null != object[21]){
				requestWorkFlowAuditVo.setSequence((int) object[21]);
			}
			
			if(null != object[22]){
				requestWorkFlowAuditVo.setDescisionType((int) object[22]);
			}
			
			if (null != object[23]) {
				requestWorkFlowAuditVo.setApprovalExecuter((int) object[23]);
			}
			
			if(null != object[24]){
				requestWorkFlowAuditVo.setRemarks((String) object[24]);
			}
						
			if (requestWorkFlowAuditVo.getDescisionType() != 9) {
				if (requestWorkFlowAuditVo.getDescisionType() != 0) {
					if(null != object[25]){
					requestWorkFlowAuditVo.setUpdatedDate((Date) object[25]);
					}
				}
			} else {
				requestWorkFlowAuditVo.setUpdatedDate(null);
			}

			
			if(null != object[26]){
				requestWorkFlowAuditVo.setCreatedDate((Date) object[26]);
			}
			
			if(null != object[27]){
				requestWorkFlowAuditVo.setMinutes((int) object[27]);
			}
			
			if (requestWorkFlowAuditVo.getApprovalExecuter() != 3 && requestWorkFlowAuditVo.getDescisionType() != 0) {
				if (requestWorkFlowAuditVo.getSequence() == 1) {
					actualSlaTime = DateUtil.getMinutesDifference(requestWorkFlowAuditVo.getUpdatedDate(),
							requestWorkFlowAuditVo.getCreatedDate());
					updateDate = requestWorkFlowAuditVo.getUpdatedDate();
					groupId = requestWorkFlowAuditVo.getGroupId();

				} else if (requestWorkFlowAuditVo.getSequence() > 1 && requestWorkFlowAuditVo.getGroupId() != 0) {
					if (groupId == requestWorkFlowAuditVo.getGroupId()) {

						groupId = requestWorkFlowAuditVo.getGroupId();

					} else {
						actualSlaTime = DateUtil.getMinutesDifference(requestWorkFlowAuditVo.getUpdatedDate(),
								updateDate);
						updateDate = requestWorkFlowAuditVo.getUpdatedDate();

						groupId = requestWorkFlowAuditVo.getGroupId();

					}

				} else if (requestWorkFlowAuditVo.getSequence() > 1 && requestWorkFlowAuditVo.getGroupId() == 0) {
					actualSlaTime = DateUtil.getMinutesDifference(requestWorkFlowAuditVo.getUpdatedDate(), updateDate);
					groupId = requestWorkFlowAuditVo.getGroupId();
					updateDate = requestWorkFlowAuditVo.getUpdatedDate();

				}
				if (actualSlaTime <= requestWorkFlowAuditVo.getMinutes()) {
					requestWorkFlowAuditVo.setStatus(CommonConstant.CONSTANT_ONE);
				} else if (actualSlaTime > requestWorkFlowAuditVo.getMinutes()) {
					requestWorkFlowAuditVo.setStatus(CommonConstant.CONSTANT_TWO);
				}

				if (null != (String) ((Object[]) object)[29]) {
					requestVo.setRemarks((String) object[29]);
				}
				
				if (null != (String) ((Object[]) object)[30]) {
					requestVo.setForwardRedirectRemarks((String) object[30]);
				}
				
				if(null != object[31]){
				requestWorkFlowAuditVo.setSubrequestId((int) object[31]);
				}
				
				requestVo.setRequestWorkFlowAuditVo(requestWorkFlowAuditVo);
				
			} // if
			list_RequestVo.add(requestVo);
		}
		return list_RequestVo;
	}

	/**
	 * Method used to view all resolver
	 * 
	 * 
	 * @param listRequestEntity
	 * @return
	 */
	private List<RequestVO> getViewResolver(List<Object[]> listRequestEntity) {

		List<RequestVO> list_RequestVo = new ArrayList<RequestVO>();

		for (Object[] object : listRequestEntity) {
			RequestVO requestVo = new RequestVO();
			RequestWorkFlowAuditVO requestWorkFlowAuditVo = new RequestWorkFlowAuditVO();

			if (null != (String) ((Object[]) object)[0]) {

				requestWorkFlowAuditVo.setUserName((String) ((Object[]) object)[0]);
			}
			if (null != (Integer) ((Object[]) object)[1]) {
				requestWorkFlowAuditVo.setRequestWorkFlowAuditId((int) ((Object[]) object)[1]);
			}
			if (null != (Integer) ((Object[]) object)[2]) {
				requestWorkFlowAuditVo.setWorkFlowId((int) ((Object[]) object)[2]);
			}
			if (null != (Integer) ((Object[]) object)[3]) {
				requestWorkFlowAuditVo.setSeqId((int) ((Object[]) object)[3]);
			}
			if (null != (Integer) ((Object[]) object)[4]) {
				requestWorkFlowAuditVo.setRequestId((int) ((Object[]) object)[4]);
			}
			if (null != (Integer) ((Object[]) object)[5]) {
				requestWorkFlowAuditVo.setUserId((int) ((Object[]) object)[5]);
			}
			if (null != (Integer) ((Object[]) object)[6]) {
				requestWorkFlowAuditVo.setGroupId((int) ((Object[]) object)[6]);
			}
			if (null != (Integer) ((Object[]) object)[7]) {
				requestWorkFlowAuditVo.setSequence((int) ((Object[]) object)[7]);
			}
			if (0 != (int) ((Object[]) object)[8]) {
				requestWorkFlowAuditVo.setDescisionType((int) ((Object[]) object)[8]);
			}
			if (null != (Integer) ((Object[]) object)[9]) {
				requestWorkFlowAuditVo.setApprovalExecuter((int) ((Object[]) object)[9]);
			}
			if (null != (Integer) ((Object[]) object)[10]) {
				requestWorkFlowAuditVo.setReassignFlag((int) ((Object[]) object)[10]);
			}
			if (null != (Integer) ((Object[]) object)[11]) {
				requestWorkFlowAuditVo.setReassignUserId((int) ((Object[]) object)[11]);
			}
			if (null != (String) ((Object[]) object)[12]) {
				requestWorkFlowAuditVo.setRemarks((String) ((Object[]) object)[12]);
			}

			requestWorkFlowAuditVo.setWorkFlowReassign((int) ((Object[]) object)[14]);

			requestVo.setRequestWorkFlowAuditVo(requestWorkFlowAuditVo);

			list_RequestVo.add(requestVo);
		}

		return list_RequestVo;

	}

	/**
	 * Method used to view resolver
	 * 
	 * @param requestVo
	 * @return
	 */

	@Transactional
	public List<RequestVO> load(RequestVO requestVo,AuthDetailsVo authDetailsVo) {
		try {
			List<RequestVO> requestVoList = new ArrayList<RequestVO>();
			List<Object[]> requestEntityList = null;

			requestEntityList = requestResolverDAO.load(requestVo,authDetailsVo);
			if (requestEntityList != null) {
				requestVoList = getViewResolver(requestEntityList);
			}
			return requestVoList;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
	}

	@Transactional
	public List<RequestResolverVO> search(RequestResolverVO requestVo,AuthDetailsVo authDetailsVo) {
		try {
			List<RequestResolverVO> listRequestVo = new ArrayList<RequestResolverVO>();
			List<Object[]> listRequestEntity = null;
			listRequestEntity = requestResolverDAO.search(requestVo,authDetailsVo);
			listRequestVo = getAllResolverList(listRequestEntity);
			return listRequestVo;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
	}

	/**
	 * Method is used to get all history the summary screen
	 * 
	 * @param requestVo
	 * @return
	 */
	@Transactional
	public List<RequestVO> getAllHistory(RequestVO requestVo,AuthDetailsVo authDetailsVo) {

		List<RequestVO> requestVoList = new ArrayList<RequestVO>();

		List<Object[]> requestEntityList = null;

		try {

			List<Integer> subOrdinate = getsubOrdinateList(authDetailsVo.getUserId());
			
			// Get all the Approval list from DB
			requestEntityList = requestResolverDAO.getAllHistory(requestVo, subOrdinate,authDetailsVo);

			// Set all the Fields of Approval details

			if (requestEntityList != null && requestEntityList.size() > 0) {
				requestVoList = getAllListHistory(requestEntityList);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dbFailure",authDetailsVo));
		}
	
		return requestVoList;

	}

	/**
	 * 
	 * @param currentLoginUserId
	 * @return
	 */
	@Transactional
	public List<Integer> getsubOrdinateList(int currentLoginUserId) {

		List<UserMappingEntity> userMappingEntityList = new ArrayList<UserMappingEntity>();
		List<Integer> subOrdinateList = new ArrayList<Integer>();

		userMappingEntityList = getlist(currentLoginUserId, userMappingEntityList);

		for (UserMappingEntity screenAuthenticationEntity : userMappingEntityList) {

			if (screenAuthenticationEntity.getUserEntity() != null) {
				subOrdinateList.add(screenAuthenticationEntity.getUserEntity().getId());
			}

		}
		return subOrdinateList;
	}

	/**
	 * 
	 * @param currentLoginUserId
	 * @param userMappingEntity
	 * @return
	 */
	@Transactional
	private List<UserMappingEntity> getlist(int currentLoginUserId, List<UserMappingEntity> userMappingEntity) {

		List<UserMappingEntity> userMappingEntityList = requestResolverDAO.getListOfUser(currentLoginUserId);

		if (null != userMappingEntityList) {

			for (UserMappingEntity screenAuthenticationEntity : userMappingEntityList) {
				if (null != screenAuthenticationEntity.getUserEntity()) {
					getlist(screenAuthenticationEntity.getUserEntity().getId(), userMappingEntity);
				}
				userMappingEntity.add(screenAuthenticationEntity);
			}

		}

		return userMappingEntity;

	}

	public List<RequestVO> getAllListHistory(List<Object[]> requestEntitylist) {

		List<RequestVO> requestVoList = new ArrayList<RequestVO>();

		for (Object requestEntity : requestEntitylist) {
			RequestVO requestVo = new RequestVO();
			RequestWorkFlowAuditVO requestWorkFlowAuditVo = new RequestWorkFlowAuditVO();

			if (null != (Integer) ((Object[]) requestEntity)[0]) {
				requestVo.setRequestId((int) ((Object[]) requestEntity)[0]);
			}
			if (null != (String) ((Object[]) requestEntity)[1]) {
				requestVo.setRequestCode((String) ((Object[]) requestEntity)[1]);
			}
			if (null != (Integer) ((Object[]) requestEntity)[2]) {
				requestVo.setCurrentStatusId((int) ((Object[]) requestEntity)[2]);
			}
			if (null != (String) ((Object[]) requestEntity)[3]) {
				requestVo.setCurrentStatusCode((String) ((Object[]) requestEntity)[3]);
			}
			if (null != (String) ((Object[]) requestEntity)[4]) {
				requestVo.setCurrentStatusName((String) ((Object[]) requestEntity)[4]);
			}

			if (null != (Integer) ((Object[]) requestEntity)[5]) {
				requestVo.setRequestTypeId((int) ((Object[]) requestEntity)[5]);
			}
			if (null != (String) ((Object[]) requestEntity)[6]) {
				requestVo.setRequestTypeName((String) ((Object[]) requestEntity)[6]);
			}
			if (null != (Integer) ((Object[]) requestEntity)[7]) {
				requestVo.setRequestSubtypeId((int) ((Object[]) requestEntity)[7]);
			}
			if (null != (String) ((Object[]) requestEntity)[8]) {
				requestVo.setRequestSubTypeName((String) ((Object[]) requestEntity)[8]);
			}
			if (null != (Integer) ((Object[]) requestEntity)[9]) {
				requestVo.setUserId((int) ((Object[]) requestEntity)[9]);
			}
			if (null != (String) ((Object[]) requestEntity)[10]) {
				requestVo.setUserName((String) ((Object[]) requestEntity)[10]);
			}
			if (null != (Date) ((Object[]) requestEntity)[11]) {
				requestVo.setRequestDate((Date) ((Object[]) requestEntity)[11]);
			}
			if (null != (Integer) ((Object[]) requestEntity)[12]) {
				requestWorkFlowAuditVo.setRequestWorkFlowAuditId((int) ((Object[]) requestEntity)[12]);
			}
			if (null != (Integer) ((Object[]) requestEntity)[13]) {
				requestWorkFlowAuditVo.setWorkFlowId((int) ((Object[]) requestEntity)[13]);
			}
			if (null != (Integer) ((Object[]) requestEntity)[14]) {
				requestWorkFlowAuditVo.setSeqId((int) ((Object[]) requestEntity)[14]);
			}
			if (null != (Integer) ((Object[]) requestEntity)[15]) {
				requestWorkFlowAuditVo.setGroupId((int) ((Object[]) requestEntity)[15]);
			}
			if (null != (Integer) ((Object[]) requestEntity)[16]) {
				requestWorkFlowAuditVo.setSequence((int) ((Object[]) requestEntity)[16]);
			}
			if (null != (Integer) ((Object[]) requestEntity)[17]) {
				requestWorkFlowAuditVo.setDescisionType((int) ((Object[]) requestEntity)[17]);
			}
			if (null != (Integer) ((Object[]) requestEntity)[17]) {
				requestVo.setDescisionType((int) ((Object[]) requestEntity)[17]);
			}
			if (null != (Integer) ((Object[]) requestEntity)[18]) {
				requestWorkFlowAuditVo.setApprovalExecuter((int) ((Object[]) requestEntity)[18]);
			}
			if (null != (String) ((Object[]) requestEntity)[19]) {
				requestWorkFlowAuditVo.setRemarks((String) ((Object[]) requestEntity)[19]);
			}
			if (null != (Integer) ((Object[]) requestEntity)[9] || null != (Integer) ((Object[]) requestEntity)[9]) {
				requestWorkFlowAuditVo.setUserId((int) ((Object[]) requestEntity)[9]);
			}
			if (null != (String) ((Object[]) requestEntity)[10]) {
				requestWorkFlowAuditVo.setUserName((String) ((Object[]) requestEntity)[10]);
			}
			if (null != (Integer) ((Object[]) requestEntity)[0]) {
				requestWorkFlowAuditVo.setRequestId((int) ((Object[]) requestEntity)[0]);
			}
			if (null != (Integer) ((Object[]) requestEntity)[20]) {
				requestVo.setRequestPriority((int) ((Object[]) requestEntity)[20]);
			}
			if (null != (String) ((Object[]) requestEntity)[21]) {
				requestVo.setRequestSubject((String) ((Object[]) requestEntity)[21]);
			}

			requestVo.setRequestWorkFlowAuditVo(requestWorkFlowAuditVo);
			requestVoList.add(requestVo);
		}
		return requestVoList;
	}

	@Transactional
	public List<RequestVO> getAllHistorySearch(RequestVO requestVo,AuthDetailsVo authDetailsVo) {

		List<RequestVO> requestVoList = new ArrayList<RequestVO>();

		List<Object[]> requestEntityList = null;

		List<Integer> subOrdinate = getsubOrdinateList(authDetailsVo.getUserId());

		// Get all the Approval list from DB
		try {

			requestEntityList = requestResolverDAO.getAllHistorySearch(requestVo, subOrdinate,authDetailsVo);

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dbFailure",authDetailsVo));
		}

		// Set all the Fields of Approval details

		if (requestEntityList != null && requestEntityList.size() > 0) {
			requestVoList = getAllListHistory(requestEntityList);
		}

		return requestVoList;

	}

	
	/**
	 * 
	 * Method used to get Resolver List
	 * 
	 * @param requestVo
	 * @return
	 */

	@Transactional
	public JSONResponse statusCheckForHold(RequestVO requestVO,AuthDetailsVo authDetailsVo) {
		JSONResponse jsonResponse = new JSONResponse();
		
		try {

			boolean result = false;
			List<RequestEntity> listRequestEntityList = null;

			listRequestEntityList = requestResolverDAO.statusCheckForHold(requestVO,authDetailsVo);
		 
			if(null != listRequestEntityList && listRequestEntityList.size() > 0){
				result = true;
			} 							
			
			if (result) {
				jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
				jsonResponse.setResponseMessage(getMessage("successMessage", authDetailsVo));
				jsonResponse.setSuccesObject(CommonConstant.NULL);
			} else {
				jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
				jsonResponse.setResponseMessage(getMessage("holdRequestMessage", authDetailsVo));
				jsonResponse.setSuccesObject(CommonConstant.NULL);
			}
						
			return jsonResponse;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

	}
	
	public void validationForInProgress(RequestVO requestVo, RequestWorkFlowAuditVO requestWorkFlowAuditVo)
			throws CommonException {

		List<RequestEntity> requestEntityList = requestResolverDAO.validationForInProgress(requestVo);

		if (requestEntityList.size() > 0) {

			RequestEntity requestEntity = requestEntityList.get(0);

			if (requestEntity.getCurrentStatusId().equals(CommonConstant.CONSTANT_EIGHT)) {
				throw new CommonException("inProgressRequestMessage");
			}

		}

	}
	

	@Transactional
	public JSONResponse statusCheckForHoldRequestor(RequestVO requestVO,AuthDetailsVo authDetailsVo) {
		JSONResponse jsonResponse = new JSONResponse();
		
		try {

			boolean result = false;
			List<RequestEntity> listRequestEntityList = null;

			listRequestEntityList = requestResolverDAO.statusCheckForHoldRequestor(requestVO,authDetailsVo);
		 
			if(null != listRequestEntityList && listRequestEntityList.size() > 0){
				result = true;
			} 							
			
			if (result) {
				jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
				jsonResponse.setResponseMessage(getMessage("successMessage", authDetailsVo));
				jsonResponse.setSuccesObject(CommonConstant.NULL);
			} else {
				jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
				jsonResponse.setResponseMessage(getMessage("holdRequestMessage", authDetailsVo));
				jsonResponse.setSuccesObject(CommonConstant.NULL);
			}
						
			return jsonResponse;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

	}

	
	
}
