package com.srm.rta.service;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.beanutils.BeanUtils;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.icu.util.StringTokenizer;
import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.config.PicturePath;
import com.srm.coreframework.constants.ButtonTypeEnum;
import com.srm.coreframework.controller.CommonController;
import com.srm.coreframework.entity.UserMappingEntity;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.util.DateUtil;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.rta.dao.ApprovalDAO;
import com.srm.rta.dao.RequestDAO;
import com.srm.rta.entity.RequestWorkFlowAuditEntity;
import com.srm.rta.vo.ApprovalVO;
import com.srm.rta.vo.RequestDetailVO;
import com.srm.rta.vo.RequestVO;
import com.srm.rta.vo.RequestWorkFlowAuditVO;

import lombok.Data;
@Data
@Service
public class ApprovalService extends CommonController<RequestVO> {


	@Autowired
	PicturePath picturePath;

	@Autowired
	RequestDAO requestDAO;

	@Autowired
	ApprovalDAO approvalDao;

	/**
	 * Method is used to get all the Approval details.
	 * 
	 * @param requestVo
	 * @return requestVo
	 */
	@Transactional
	public List<ApprovalVO> getAll(AuthDetailsVo authDetailsVo) {

		List<ApprovalVO> approvalVOList = new ArrayList<>();

		List<Object[]> approvalList = new ArrayList<>();
		
		try {

			// Get all the Approval list from DB
			approvalList = approvalDao.getAll(authDetailsVo);
			// Set all the Fields of Approval details

			if (approvalList != null && !approvalList.isEmpty()) {
				approvalVOList = approvalGetAll(approvalList);
			}

		} catch (Exception e) {
			Log.info("Approval Service getAll  Exception",e);
			throw new CommonException(getMessage("dbFailure",authDetailsVo));
		}
	
		return approvalVOList;
	}

	/**
	 * Method is used to get all the approval list.
	 * 
	 * @param requestEntitylist
	 * @return
	 */
	public List<ApprovalVO> getAllList(List<Object[]> approvalList) {

		List<ApprovalVO> approvalVOList = new ArrayList<>();

		for (Object approval : approvalList) {
			ApprovalVO approvalVO = new ApprovalVO();

			if (null != (Integer) ((Object[]) approval)[0]) {
				approvalVO.setRequestId((int) ((Object[]) approval)[0]);
			}
			if (null != (String) ((Object[]) approval)[1]) {
				approvalVO.setRequestCode((String) ((Object[]) approval)[1]);
			}
			
			if (null != (String) ((Object[]) approval)[3]) {
				approvalVO.setCurrentStatusCode((String) ((Object[]) approval)[3]);
			}
			
			if (null != (String) ((Object[]) approval)[4]) {
				approvalVO.setCurrentStatusName((String) ((Object[]) approval)[4]);
			}
			if (null != (String) ((Object[]) approval)[6]) {
				approvalVO.setRequestTypeName((String) ((Object[]) approval)[6]);
			}
			if (null != (String) ((Object[]) approval)[8]) {
				approvalVO.setRequestSubTypeName((String) ((Object[]) approval)[8]);
			}
			if (null != (String) ((Object[]) approval)[10]) {
				approvalVO.setUserName((String) ((Object[]) approval)[10]);
			}
			if (null != (Date) ((Object[]) approval)[11]) {
				approvalVO.setRequestDate((Date) ((Object[]) approval)[11]);
			}
			if (null != (Integer) ((Object[]) approval)[17]) {
				approvalVO.setDescisionType((int) ((Object[]) approval)[17]);
			}
			
			//search adding
			if (null != (String) ((Object[]) approval)[22]) {
				approvalVO.setResolverRemarks((String) ((Object[]) approval)[22]);
			}

			if (null != (String) ((Object[]) approval)[23]) {
				approvalVO.setForwardRedirectRemarks((String) ((Object[]) approval)[23]);
			}
						
			if (null != (String) ((Object[]) approval)[24]) {
				approvalVO.setLocationName((String) ((Object[]) approval)[24]);
			}
			
			if (null != (String) ((Object[]) approval)[25]) {
				approvalVO.setSublocationName((String) ((Object[]) approval)[25]);
			}
			
			if (null != (String) ((Object[]) approval)[26]) {
				approvalVO.setUserDepartmentName((String) ((Object[]) approval)[26]);
			}
			 
			if (null !=  ((Object[]) approval)[27]) {
				approvalVO.setForwardRequestId((int) ((Object[]) approval)[27]);
			}
			
			if (null != ((Object[]) approval)[28]) {
				approvalVO.setRedirectRequestId((int) ((Object[]) approval)[28]);
			}
					
			approvalVOList.add(approvalVO);
		}
		return approvalVOList;
	}
	
	public List<ApprovalVO> approvalGetAll(List<Object[]> approvalList) {

		List<ApprovalVO> approvalVOList = new ArrayList<>();

		for (Object approval : approvalList) {
			ApprovalVO approvalVO = new ApprovalVO();

			if (null != (Integer) ((Object[]) approval)[0]) {
				approvalVO.setRequestId((int) ((Object[]) approval)[0]);
			}
			if (null != (String) ((Object[]) approval)[1]) {
				approvalVO.setRequestCode((String) ((Object[]) approval)[1]);
			}
			if (null != (String) ((Object[]) approval)[3]) {
				approvalVO.setCurrentStatusCode((String) ((Object[]) approval)[3]);
			}
			
			if (null != (String) ((Object[]) approval)[4]) {
				approvalVO.setCurrentStatusName((String) ((Object[]) approval)[4]);
			}
			if (null != (String) ((Object[]) approval)[6]) {
				approvalVO.setRequestTypeName((String) ((Object[]) approval)[6]);
			}
			if (null != (String) ((Object[]) approval)[8]) {
				approvalVO.setRequestSubTypeName((String) ((Object[]) approval)[8]);
			}
			if (null != (String) ((Object[]) approval)[10]) {
				approvalVO.setUserName((String) ((Object[]) approval)[10]);
			}
			if (null != (Date) ((Object[]) approval)[11]) {
				approvalVO.setRequestDate((Date) ((Object[]) approval)[11]);
			}
			if (null != (Integer) ((Object[]) approval)[17]) {
				approvalVO.setDescisionType((int) ((Object[]) approval)[17]);
			}
			if (null != (Integer) ((Object[]) approval)[20]) {
				approvalVO.setRequestPriority((Integer) ((Object[]) approval)[20]);
			}
			
			if (null != (String) ((Object[]) approval)[22]) {
				approvalVO.setResolverRemarks((String) ((Object[]) approval)[22]);
			}

			if (null != (String) ((Object[]) approval)[23]) {
				approvalVO.setForwardRedirectRemarks((String) ((Object[]) approval)[23]);
			}
			
			if (null != (Integer) ((Object[]) approval)[2]) {
				approvalVO.setCurrentStatusId((int) ((Object[]) approval)[2]);
			}
			
			if (null != (String) ((Object[]) approval)[24]) {
				approvalVO.setLocationName((String) ((Object[]) approval)[24]);
			}
			
			if (null != (String) ((Object[]) approval)[25]) {
				approvalVO.setSublocationName((String) ((Object[]) approval)[25]);
			}
			
			if (null != (String) ((Object[]) approval)[26]) {
				approvalVO.setUserDepartmentName((String) ((Object[]) approval)[26]);
			}
			 
			if (null != (Integer) ((Object[]) approval)[27]) {
				approvalVO.setForwardRequestId((Integer) ((Object[]) approval)[27]);
			}
			
			if (null != (Integer) ((Object[]) approval)[28]) {
				approvalVO.setRedirectRequestId((Integer) ((Object[]) approval)[28]);
			}
						
			approvalVOList.add(approvalVO);
		}
		return approvalVOList;
	}
	/**
	 * Method is used for load the approval details
	 * 
	 * @param requestVo
	 * @return
	 */
	@Transactional
	public RequestVO load(RequestVO requestVo,AuthDetailsVo authDetailsVo) {

		List<RequestWorkFlowAuditVO> requestVoList = new ArrayList<>();

		List<Object[]> requestEntityList = new ArrayList<>();

		// Get the approval data in DB
		try {

			requestEntityList = approvalDao.load(requestVo.getRequestWorkFlowAuditVo(),authDetailsVo);


		// Set the Approval data in VO

		if (requestEntityList != null && requestEntityList.size() > 0) {
			requestVoList = getLoadApproval(requestEntityList);
			requestVo.setRequestWorkFlowAuditVoList(requestVoList);
		}

		int button = approvalDao.getbuttonCount(requestVo,authDetailsVo);

		requestVo.setButton(button);

		// requestVo.setButton(0);


		} catch (Exception e) {
			Log.info("Approval Service load  Exception",e);
			throw new CommonException(getMessage("dbFailure",authDetailsVo));
		}
		return requestVo;

	}

	/**
	 * Method is used for List all the approval details.
	 * 
	 * @param requestEntityList
	 * @return
	 */
	@Transactional
	private List<RequestWorkFlowAuditVO> getLoadApproval(List<Object[]> requestEntityList) {

		List<RequestWorkFlowAuditVO> requestList = new ArrayList<>();

		Date updateDate = null;
		int groupId = 0;
		int actualSlaTime = 0;

		for (Object requestEntity : requestEntityList) {

			RequestWorkFlowAuditVO requestWorkFlowAuditVo = new RequestWorkFlowAuditVO();

			if (null != (String) ((Object[]) requestEntity)[0]) {
				requestWorkFlowAuditVo.setUserName((String) ((Object[]) requestEntity)[0]);
			}
			if (0 != (int) ((Object[]) requestEntity)[1]) {
				requestWorkFlowAuditVo.setRequestWorkFlowAuditId((int) ((Object[]) requestEntity)[1]);
			}
			if (0 != (int) ((Object[]) requestEntity)[2]) {
				requestWorkFlowAuditVo.setWorkFlowId((int) ((Object[]) requestEntity)[2]);
			}
			if (0 != (int) ((Object[]) requestEntity)[3]) {
				requestWorkFlowAuditVo.setSeqId((int) ((Object[]) requestEntity)[3]);
			}
			if (0 != (int) ((Object[]) requestEntity)[4]) {
				requestWorkFlowAuditVo.setRequestId((int) ((Object[]) requestEntity)[4]);
			}
			if (0 != (int) ((Object[]) requestEntity)[5]) {
				requestWorkFlowAuditVo.setUserId((int) ((Object[]) requestEntity)[5]);
			}
			if (0 != (int) ((Object[]) requestEntity)[6]) {
				requestWorkFlowAuditVo.setGroupId((int) ((Object[]) requestEntity)[6]);
			}
			if (0 != (int) ((Object[]) requestEntity)[7]) {
				requestWorkFlowAuditVo.setSequence((int) ((Object[]) requestEntity)[7]);
			}
			// if (0 != (int) ((Object[]) requestEntity)[8]) {
			requestWorkFlowAuditVo.setDescisionType((int) ((Object[]) requestEntity)[8]);
			// }
			if (0 != (int) ((Object[]) requestEntity)[9]) {
				requestWorkFlowAuditVo.setApprovalExecuter((int) ((Object[]) requestEntity)[9]);
			}
			if (null != (Integer) ((Object[]) requestEntity)[10]) {
				requestWorkFlowAuditVo.setReassignFlag((int) ((Object[]) requestEntity)[10]);
			}
			if (null != (Integer) ((Object[]) requestEntity)[11]) {
				requestWorkFlowAuditVo.setReassignUserId((int) ((Object[]) requestEntity)[11]);
			}
			if (null != (String) ((Object[]) requestEntity)[12]) {
				requestWorkFlowAuditVo.setRemarks((String) ((Object[]) requestEntity)[12]);
			}
			
			if (null != (String) ((Object[]) requestEntity)[17]) {
				requestWorkFlowAuditVo.setForwardRemarks((String) ((Object[]) requestEntity)[17]);
			}
			 
			if (0 != requestWorkFlowAuditVo.getDescisionType() && requestWorkFlowAuditVo.getDescisionType() != 0) {
				if (null != (Date) ((Object[]) requestEntity)[14]) {
					requestWorkFlowAuditVo.setApprovalDate((Date) ((Object[]) requestEntity)[14]);
				}
			}
			//if (0 != requestWorkFlowAuditVo.getDescisionType() && requestWorkFlowAuditVo.getDescisionType() != 9) {
				if (null != ((Object[]) requestEntity)[14] ) {//&& requestWorkFlowAuditVo.getDescisionType() != 0) {
					requestWorkFlowAuditVo.setUpdatedDate((Date) ((Object[]) requestEntity)[14]);
				}
			//}
				 
				
				/* if (requestWorkFlowAuditVo.getApprovalExecuter() == 2) {

						RequestWorkFlowAuditEntity requestWorkFlowAuditEntity = requestDAO
								.getResolverSlaList(requestWorkFlowAuditVo.getRequestId());
						requestWorkFlowAuditVo.setCreatedDate(requestWorkFlowAuditEntity.getUpdateDate());

					} else if (requestWorkFlowAuditVo.getApprovalExecuter() == 1){
						
						RequestWorkFlowAuditEntity requestWorkFlowAuditEntity = requestDAO
								.getApproverSlaList(requestWorkFlowAuditVo.getRequestId());
						requestWorkFlowAuditVo.setCreatedDate(requestWorkFlowAuditEntity.getUpdateDate());
						
					} else if (null != ((Object[]) requestEntity)[15]) {
						requestWorkFlowAuditVo.setCreatedDate((Date) ((Object[]) requestEntity)[15]);
					}*/
									
				if (requestWorkFlowAuditVo.getApprovalExecuter() == 2) {

					RequestWorkFlowAuditEntity requestWorkFlowAuditEntity = requestDAO
							.getResolverSlaList(requestWorkFlowAuditVo.getRequestId());
					requestWorkFlowAuditVo.setCreatedDate(requestWorkFlowAuditEntity.getUpdateDate());

				} else if (requestWorkFlowAuditVo.getApprovalExecuter() == 1) {
										
					if(null != requestWorkFlowAuditVo.getSequence() &&
							requestWorkFlowAuditVo.getSequence()== 1){
						if (null != ((Object[]) requestEntity)[15] && requestWorkFlowAuditVo.getDescisionType() != 0) {
							requestWorkFlowAuditVo.setCreatedDate((Date) ((Object[]) requestEntity)[15]);
						}
						
					} else{
						
						/*RequestWorkFlowAuditEntity requestWorkFlowAuditEntity = requestDAO
								.getApproverSlaList(requestWorkFlowAuditVo.getRequestId(), requestWorkFlowAuditVo.getSequence());
						requestWorkFlowAuditVo.setCreatedDate(requestWorkFlowAuditEntity.getUpdateDate());*/
						
						if( null != requestWorkFlowAuditVo.getGroupId() && requestWorkFlowAuditVo.getGroupId() != 0){
							
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

					if (null != ((Object[]) requestEntity)[15] && requestWorkFlowAuditVo.getDescisionType() != 0) {
						requestWorkFlowAuditVo.setCreatedDate((Date) ((Object[]) requestEntity)[15]);
					}
				}							
				
			if (0 != requestWorkFlowAuditVo.getDescisionType() && null != ((Object[]) requestEntity)[16]
					&& (int) ((Object[]) requestEntity)[16] != 0) {
				
				if(null != requestWorkFlowAuditVo.getUpdatedDate() && null != requestWorkFlowAuditVo.getCreatedDate() ){
					
					  actualSlaTime = DateUtil.getMinutesDifference(requestWorkFlowAuditVo.getUpdatedDate(),
							requestWorkFlowAuditVo.getCreatedDate());

					requestWorkFlowAuditVo.setMinutes((int) ((Object[]) requestEntity)[16]);

					if (actualSlaTime <= requestWorkFlowAuditVo.getMinutes()) {
						requestWorkFlowAuditVo.setStatus(CommonConstant.CONSTANT_ZERO);
					} else if (actualSlaTime > requestWorkFlowAuditVo.getMinutes()) {
						requestWorkFlowAuditVo.setStatus(CommonConstant.CONSTANT_TWO);
					}
				}							
			}
									
			//commented old sla
/*			
  if (null != ((Object[]) requestEntity)[16]) {
				requestWorkFlowAuditVo.setMinutes((int) ((Object[]) requestEntity)[16]);
			}
    
  if ( 0 != requestWorkFlowAuditVo.getDescisionType() && 
					null != requestWorkFlowAuditVo.getApprovalExecuter()
					&& requestWorkFlowAuditVo.getApprovalExecuter() != 3)  {
					//&& requestWorkFlowAuditVo.getDescisionType() != 0)
				if (requestWorkFlowAuditVo.getSequence() == 1) {
					actualSlaTime = DateUtil.getMinutesDifference(requestWorkFlowAuditVo.getUpdatedDate(),
							requestWorkFlowAuditVo.getCreatedDate());
					updateDate = requestWorkFlowAuditVo.getUpdatedDate();
					if(null != requestWorkFlowAuditVo.getGroupId()){
						groupId = requestWorkFlowAuditVo.getGroupId();
					}
					

				} else if (requestWorkFlowAuditVo.getSequence() > 1 && null != requestWorkFlowAuditVo.getGroupId() ) {
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

				} else if (requestWorkFlowAuditVo.getSequence() > 1 && null!= requestWorkFlowAuditVo.getGroupId() && requestWorkFlowAuditVo.getGroupId() == 0) {
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
			}*/
		
			requestList.add(requestWorkFlowAuditVo);

		}
		return requestList;

	}

	/**
	 * Method is used for load all the approval details.
	 * 
	 * @param requestVo
	 * @return
	 */
	@Transactional
	public RequestVO loadAll(RequestVO requestVo,AuthDetailsVo authDetailsVo) {

		List<RequestWorkFlowAuditVO> listRequestVo = new ArrayList<>();

		List<Object[]> listRequestEntity = new ArrayList<>();

		// Get the approval details of the certain request except the Login
		// UserId

		try {

			listRequestEntity = approvalDao.loadAll(requestVo.getRequestWorkFlowAuditVo(),authDetailsVo);

		} catch (Exception e) {
			Log.info("Approval Service loadAll  Exception",e);
			throw new CommonException(getMessage("dbFailure",authDetailsVo));
		}

		// Set the Approval details in VO

		if (listRequestEntity != null && listRequestEntity.size() > 0) {

			listRequestVo = getLoadApproval(listRequestEntity);
			requestVo.setRequestWorkFlowAuditVoList(listRequestVo);
		}

		return requestVo;
	}

	/**
	 * Method is used to get all the searches
	 * 
	 * @param requestVo
	 * @return requestVoList
	 */
	@Transactional
	public List<ApprovalVO> getAllSearch(ApprovalVO approvalVO,AuthDetailsVo authDetailsVo) {

		List<ApprovalVO> approvalVOList = new ArrayList<>();

		List<Object[]> approvalList = new ArrayList<>();

		// Get the details from DB using the search conditions
		try {

			approvalList = approvalDao.getAllSearch(approvalVO,authDetailsVo);


		// Set all the fields in VO

		if (approvalList != null && approvalList.size() > 0) {
			approvalVOList = getAllList(approvalList);
		}
		
		} catch (Exception e) {
			Log.info("Approval Service getAllSearch  Exception",e);
			throw new CommonException(getMessage("dbFailure",authDetailsVo));
		}

		return approvalVOList;
	}

	/**
	 * Method is used to get all history the summary screen
	 * 
	 * @param requestVo
	 * @return
	 */
	@Transactional
	public List<RequestVO> getAllHistory(RequestVO requestVo,AuthDetailsVo authDetailsVo) {

		List<RequestVO> requestVoList = new ArrayList<>();

		List<Object[]> requestEntityList = new ArrayList<>();

		List<Integer> subOrdinate = getsubOrdinateList(authDetailsVo.getUserId(),authDetailsVo);

		// Get all the Approval list from DB
		try {

			requestEntityList = approvalDao.getAllHistory(requestVo, subOrdinate,authDetailsVo);

		} catch (Exception e) {
			Log.info("Approval Service getAllHistory  Exception",e);
			throw new CommonException(getMessage("dbFailure",authDetailsVo));
		}

		// Set all the Fields of Approval details

		if (requestEntityList != null && requestEntityList.size() > 0) {
			requestVoList = getAllListSummary(requestEntityList);
		}

		return requestVoList;

	}

	/**
	 * Method is used to get all search history the summary screen
	 * 
	 * @param requestVo
	 * @return
	 */
	@Transactional
	public List<RequestVO> getAllHistorySearch(RequestVO requestVo,AuthDetailsVo authDetailsVo) {

		List<RequestVO> requestVoList = new ArrayList<>();

		List<Object[]> requestEntityList = new ArrayList<>();

		List<Integer> subOrdinate = getsubOrdinateList(authDetailsVo.getUserId(),authDetailsVo);

		// Get all the Approval list from DB
		try {

			requestEntityList = approvalDao.getAllHistorySearch(requestVo, subOrdinate,authDetailsVo);

		} catch (Exception e) {
			Log.info("Approval Service getAllHistorySearch  Exception",e);
			throw new CommonException(getMessage("dbFailure",authDetailsVo));
		}

		// Set all the Fields of Approval details

		if (requestEntityList != null && !requestEntityList.isEmpty()) {
			requestVoList = getAllListSummary(requestEntityList);
		}

		return requestVoList;

	}

	/**
	 * 
	 * @param currentLoginUserId
	 * @return
	 */
	@Transactional
	public List<Integer> getsubOrdinateList(int currentLoginUserId,AuthDetailsVo authDetailsVo) {

		List<UserMappingEntity> userMappingEntityList = new ArrayList<>();
		List<Integer> subOrdinateList = new ArrayList<>();

		userMappingEntityList = getlist(currentLoginUserId, userMappingEntityList,authDetailsVo);

		for (UserMappingEntity screenAuthenticationEntity : userMappingEntityList) {

			if (screenAuthenticationEntity.getUserEntity() != null) {
				subOrdinateList.add(screenAuthenticationEntity.getUserEntity().getId());
			}

		}
		return subOrdinateList;
	}

	public List<RequestVO> getAllListSummary(List<Object[]> requestEntitylist) {

		List<RequestVO> requestVoList = new ArrayList<>();

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
			if (null != (Integer) ((Object[]) requestEntity)[9]) {
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

	/**
	 * 
	 * @param currentLoginUserId
	 * @param userMappingEntity
	 * @return
	 */
	@Transactional
	private List<UserMappingEntity> getlist(int currentLoginUserId, List<UserMappingEntity> userMappingEntity,AuthDetailsVo authDetailsVo) {

		List<UserMappingEntity> userMappingEntityList = approvalDao.getListOfUser(currentLoginUserId,authDetailsVo);

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

	@SuppressWarnings("rawtypes")
	@Transactional
	public RequestVO findRequestApproval(RequestVO requestLoad,AuthDetailsVo authDetailsVo) throws IllegalAccessException, InvocationTargetException {
		List<Object> result = new ArrayList<>();

		List<RequestDetailVO> listRequestVoList = new ArrayList<>();
		RequestVO requestVo = new RequestVO();
		RequestVO request = new RequestVO();
		try {
			 
			BeanUtils.copyProperties(request, requestLoad);
			
			result = requestDAO.findApproverView(requestLoad,authDetailsVo);
		 
		if (null != result && result.size() > 0) {

			Iterator itr = result.iterator();
			RequestDetailVO requestDetailVo = new RequestDetailVO();
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
			/*	if (null != object[5]) {

					requestVo.setLocationName((String) object[5]);
				}
				if (null != object[6]) {

					requestVo.setSublocationName((String) object[6]);
				}*/
				
				
				if (null != object[38]) {

					requestVo.setLocationName((String) object[38]);
				}
				if (null != object[39]) {

					requestVo.setSublocationName((String) object[39]);
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
				if (null != object[36]) {

					requestVo.setUserName((String) object[36]);
				}
				if (null != (String) ((Object[]) object)[37]) {

					StringBuilder modifiedQuery = new StringBuilder(picturePath.getRequestAttachmentPath());
					File file = new File((String) ((Object[]) object)[37]);
					modifiedQuery.append(file.getName());
					requestVo.setRequestAttachment(modifiedQuery.toString());

				}
				
				if (null != object[42]) {
					requestVo.setResolverRemarks((String) object[42]);
				}
				
				if (null != object[43]) {
					requestVo.setForwardRedirectRemarks((String) object[43]);
				}
				if(null != object[37]){
					requestVo.setRequestAttachment((String) object[37]);
				}
				listRequestVoList.add(requestDetailVo);

			}
		}
		request.setRequest(requestVo);
		request.setRequestDetailList(listRequestVoList);

		return request;
	
	} catch (CommonException e) {
		Log.info("Approval Controller findRequestApproval CommonException",e);
		throw new CommonException(getMessage("beanUtilPropertiesFailure",authDetailsVo));
	}
}
}
