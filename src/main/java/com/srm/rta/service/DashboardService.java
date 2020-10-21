package com.srm.rta.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.transaction.Transactional;

import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.config.PicturePath;
import com.srm.coreframework.controller.CommonController;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.util.DateUtil;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.rta.dao.DashboardDAO;
import com.srm.rta.dao.ExternalLinkDAO;
import com.srm.rta.dao.RequestConfigurationDAO;
import com.srm.rta.dao.RequestDAO;
import com.srm.rta.entity.ExternalLinkEntity;
import com.srm.rta.entity.MoreApplicationEntity;
import com.srm.rta.vo.ApprovalVO;
import com.srm.rta.vo.DashBoardCurrentStatusVO;
import com.srm.rta.vo.ExternalLinkVO;
import com.srm.rta.vo.MoreApplicationVO;
import com.srm.rta.vo.RequestResolverVO;
import com.srm.rta.vo.RequestTypeVO;
import com.srm.rta.vo.RequestVO;
import com.srm.rta.vo.RequestWorkFlowAuditVO;

import lombok.Data;
@Data
@Service
public class DashboardService extends CommonController<DashBoardCurrentStatusVO> {


	@Autowired
	RequestService requestService;

	@Autowired
	RequestTypeService requestTypeService;

	@Autowired
	RequestDAO requestDao;

	@Autowired
	RequestSubTypeService requestSubTypeService;

	@Autowired
	RequestConfigurationService requestWorkFlowService;

	@Autowired
	DashboardDAO dashBoardDao;

	@Autowired
	RequestConfigurationDAO requestWorkFlowDao;

	@Autowired
    PicturePath picturePath;

	@Autowired
	ExternalLinkDAO externalLinkDao;

	@Autowired
	RequestResolverService requestResolverService;
	
	@Autowired
	ApprovalService approvalService;
	
	/**
	 * This Method is to get all the dashboard request type.
	 * 
	 * @return listRequestTypeVo List<RequestTypeVo>
	 * @throws CommonException
	 *
	 */
	@Transactional
	public List<RequestTypeVO> getDashBoardInfo(AuthDetailsVo authDetailsVo) throws CommonException, CommonException {

		List<RequestTypeVO> listRequestTypeVo = requestTypeService.getAll(authDetailsVo);

		return listRequestTypeVo;
	}

	/**
	 * This Method is to get all the dashboard RequestWorkFlow type.
	 * 
	 * @return listRequestWorkFlowVo
	 *
	 */
	@Transactional
	public List<DashBoardCurrentStatusVO> getmyRequest(AuthDetailsVo authDetailsVo) throws CommonException {
		try {
			List<DashBoardCurrentStatusVO> list = new ArrayList<DashBoardCurrentStatusVO>();

			HashMap<String, Integer> map = new HashMap<String, Integer>();
									 
				map.put("Completed", 1);
				map.put("Pending", 2);
				map.put("Escalated", 3);
				map.put("Approved", 5);
				map.put("Reopen", 10);
				map.put("Closed", 11);
				map.put("Rejected", 6);
				map.put("In-Progress", 8);
				map.put("Hold", 14);
				map.put("Re-Submit", 7);
		 		
			HashMap<String, Integer> requestCountMap = dashBoardDao.getRequestStatus(authDetailsVo);

			Iterator<Entry<String, Integer>> itr = map.entrySet().iterator();

			while (itr.hasNext()) {
				Entry<String, Integer> entry = itr.next();

				DashBoardCurrentStatusVO currentStatusVo = new DashBoardCurrentStatusVO();

				if (requestCountMap.containsKey(entry.getKey())) {
					currentStatusVo.setCount(requestCountMap.get(entry.getKey()));
					
					if ("en".equalsIgnoreCase(authDetailsVo.getLangCode())) {
						currentStatusVo.setStatus(entry.getKey());
					} else {
						currentStatusVo.setStatus(getStatus(entry.getKey()));
					}
					currentStatusVo.setCurrentStatusId(entry.getValue());

				} else {

					if ("en".equalsIgnoreCase(authDetailsVo.getLangCode())) {
						currentStatusVo.setStatus(entry.getKey());
					} else {
						currentStatusVo.setStatus(getStatus(entry.getKey()));
					}
					currentStatusVo.setCurrentStatusId(entry.getValue());
					currentStatusVo.setCount(0);
				}

				list.add(currentStatusVo);

			}

			return list;
		} catch (Exception e) {
			Log.info("Dashboard Service getmyRequest Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

	}	 	 
	
	public String getStatus(String key) {

		String result;
		
		switch (key) {
		case "Completed":
			result = "完成";
			break;
		case "Pending":
			result = "未決";
			break;
		case "Escalated":
			result = "エスカレートされた";
			break;
		case "Approved":
			result = "承認済み";
			break;
		case "Reopen":
			result = "再開";
			break;	
		case "Closed":
			result = "閉まっている";
			break;		
		case "Rejected":
			result = "拒否された";
			break;	
		case "In-Progress":
			result = "進行中";
			break;	
		case "Hold":
			result = "保留";
			break;	
		case "Re-Submit":
			result = "再送信";
			break;	
		default:
			return "";
		}
		return result;
	}
	
	
	/**
	 * This Method is to get all the ExternalLinkVo for dashBoard.
	 * 
	 * @return externalLinkVoList
	 *
	 */
	@Transactional
	public List<ExternalLinkVO> getAllList(AuthDetailsVo authDetailsVo) {
		List<ExternalLinkVO> externalLinkVoList = new ArrayList<ExternalLinkVO>();
		List<ExternalLinkEntity> externalLinkEntityList = dashBoardDao.getAll(authDetailsVo);
		externalLinkVoList = getAllListFields(externalLinkEntityList,authDetailsVo);
		return externalLinkVoList;

	}

	/**
	 * This Method is to get all the ExternalLinkVo for dashBoard and display
	 * the all filed .
	 * 
	 * @return externalLinkVoList
	 *
	 */
	@Transactional
	private List<ExternalLinkVO> getAllListFields(List<ExternalLinkEntity> externalLinkEntityList,AuthDetailsVo authDetailsVo) {
		List<ExternalLinkVO> externalLinkVoList = new ArrayList<ExternalLinkVO>();
		for (ExternalLinkEntity externalLinkEntity : externalLinkEntityList) {
			ExternalLinkVO externalLinkVo = new ExternalLinkVO();
			if (externalLinkEntity.getId() != null) {
				externalLinkVo.setId(externalLinkEntity.getId());
			}
			if (externalLinkEntity.getExternalLinkName() != null) {
				externalLinkVo.setExternalLinkName(externalLinkEntity.getExternalLinkName());
			}
			if (externalLinkEntity.getExternalLinkUrl() != null) {
				externalLinkVo.setExternalLinkUrl(externalLinkEntity.getExternalLinkUrl());
			}
			if (externalLinkEntity.getExternalLinkDisplaySeq() != null) {
				externalLinkVo.setExternalLinkDisplaySeq(externalLinkEntity.getExternalLinkDisplaySeq());
			}
			if (null != externalLinkEntity.getExternalLinkLogo()) {
				/*StringBuffer modifiedQuery = new StringBuffer(picturePath.getExternalLinkDownloadPath());
				File file = new File(externalLinkEntity.getExternalLinkLogo());
				modifiedQuery.append(file.getName());
				externalLinkVo.setExternalLinkLogo(modifiedQuery.toString());*/
				try {
					// image loading
					if (null != externalLinkEntity.getExternalLinkLogo()) {
						externalLinkVo.setExternalLinkLogoImage(imageLoading(externalLinkEntity.getExternalLinkLogo()));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			externalLinkVo.setExternalLinkIsActive(externalLinkEntity.isExternalLinkIsActive());
			externalLinkVoList.add(externalLinkVo);

		}
		return externalLinkVoList;

	}

	/**
	 * This Method is to get all the display the different types of escalation
	 * in dashboard.
	 * 
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 *
	 */
	@Transactional
	public List<DashBoardCurrentStatusVO> getSlaInsight(AuthDetailsVo authDetailsVo) throws IllegalAccessException, InvocationTargetException {

		List<DashBoardCurrentStatusVO> list = new ArrayList<DashBoardCurrentStatusVO>();

		List<Object[]> obj = dashBoardDao.getSlaInsight(authDetailsVo);

		for (Object[] object : obj) {

			DashBoardCurrentStatusVO dashBoardCurrentStatusVo = new DashBoardCurrentStatusVO();

			if (object[0] != null) {

				dashBoardCurrentStatusVo.setStatus((String) object[0]);
			}
			if (object[1] != null) {

				dashBoardCurrentStatusVo.setCurrentStatusId((int) object[1]);
			}

			if (object[2] != null) {

				BigInteger count = (BigInteger) object[2];

				dashBoardCurrentStatusVo.setCount(count.intValue());
			}
			list.add(dashBoardCurrentStatusVo);
		}

		return list;

	}

	@Transactional
	private List<Integer> calculateSLATime(List<Object[]> requestWorkFlowEntityList) {
		List<Integer> list1 = new ArrayList<Integer>();
		for (Object[] object : requestWorkFlowEntityList) {
			boolean status = false;
			String sequence;
			Date updatedDate = null;
			int actualSlaTime = 0;
			RequestWorkFlowAuditVO requestWorkFlowAuditVo = new RequestWorkFlowAuditVO();
			if (null != object[3]) {
				requestWorkFlowAuditVo.setRequestId((int) object[3]);
			}

			if (null != object[6]) {
				requestWorkFlowAuditVo.setSequence((int) object[6]);
			}
			if (null != object[17]) {
				requestWorkFlowAuditVo.setUpdatedDate((Date) object[17]);
			}

			if (null != object[1]) {
				requestWorkFlowAuditVo.setWorkFlowId((int) object[1]);
			}

			if (null != object[4]) {
				requestWorkFlowAuditVo.setUserId((int) object[4]);
			}
			if (null != object[21]) {
				requestWorkFlowAuditVo.setSla((float) object[21]);
			}

			if (null != object[20]) {
				requestWorkFlowAuditVo.setSlaType((int) object[20]);
			}

			if (null != object[19]) {
				sequence = (String) object[19];

				ArrayList<Integer> list = new ArrayList<Integer>();
				for (String sequenceList : sequence.split(",")) {
					list.add(Integer.parseInt(sequenceList));
					if (list.get(0) == CommonConstant.CONSTANT_ONE) {

						updatedDate = requestWorkFlowAuditVo.getUpdatedDate();
						status = true;
						break;
					} else if (requestWorkFlowAuditVo.getSequence() == Integer.parseInt(sequenceList)) {
						status = true;
						try {
							updatedDate = dashBoardDao.findauditse(list.get(0) - 1,
									requestWorkFlowAuditVo.getRequestId());
						} catch (Exception exe) {

						}
						break;
					}
				}
			}
			if (status) {
				actualSlaTime = DateUtil.getMinutesDifference(CommonConstant.getCalenderDate(), updatedDate);

				if (actualSlaTime > requestWorkFlowAuditVo.getSla()) {
					list1.add(requestWorkFlowAuditVo.getRequestId());

				}
			}
		}
		return list1;
	}

	@Transactional
	public List<MoreApplicationVO> getMoreApplication(AuthDetailsVo authDetailsVo) {

		List<MoreApplicationVO> moreApplicationVoList = new ArrayList<MoreApplicationVO>();
		List<MoreApplicationEntity> moreApplicationEntityList = dashBoardDao.getAllMoreApplication(authDetailsVo);
		moreApplicationVoList = getAllList(moreApplicationEntityList);
		return moreApplicationVoList;

	}

	@Transactional
	private List<MoreApplicationVO> getAllList(List<MoreApplicationEntity> moreApplicationEntityList) {
		List<MoreApplicationVO> moreApplicationVoList = new ArrayList<MoreApplicationVO>();
		for (MoreApplicationEntity moreApplicationEntity : moreApplicationEntityList) {
			MoreApplicationVO moreApplicationVo = new MoreApplicationVO();
			if (moreApplicationEntity.getMoreApplicationId() != 0) {
				moreApplicationVo.setMoreApplicationId(moreApplicationEntity.getMoreApplicationId());
			}
			if (moreApplicationEntity.getMoreApplicationName() != null) {
				moreApplicationVo.setMoreApplicationName(moreApplicationEntity.getMoreApplicationName());
			}
			if (moreApplicationEntity.getUrl() != null) {
				moreApplicationVo.setUtl(moreApplicationEntity.getUrl());
			}
			if (moreApplicationEntity.getSequnce() != 0) {
				moreApplicationVo.setSequnce(moreApplicationEntity.getSequnce());
			}
			if (moreApplicationEntity.getLogo() != null) {
				moreApplicationVo.setLogo(moreApplicationEntity.getLogo());
			}
			moreApplicationVoList.add(moreApplicationVo);

		}
		return moreApplicationVoList;
	}

	@Transactional
	public List<RequestVO> findRequest(RequestVO request,AuthDetailsVo authDetailsVo) throws CommonException {

		List<RequestVO> requestVoList = new ArrayList<RequestVO>();
		try {
			List<Object[]> result = dashBoardDao.findRequest(request.getCurrentStatusId(),authDetailsVo);

			for (Object[] object : result) {

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
				requestVoList.add(requestVo);

			}

			return requestVoList;

		} catch (NoResultException e) {
			Log.info("Dashboard Service findRequest NoResultException",e);
			throw new CommonException(getMessage("noResultFound",authDetailsVo));
		} catch (NonUniqueResultException e) {
			Log.info("Dashboard Service findRequest NonUniqueResultException",e);
			throw new CommonException(getMessage("noRecordFound",authDetailsVo));
		} catch (Exception e) {
			Log.info("Dashboard Service findRequest Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
	}

	@Transactional
	public RequestVO getAllApprovalCount(AuthDetailsVo authDetailsVo) {

		RequestVO requestVo = new RequestVO();

		int count = 0;

		// Get all the Approval list from DB
		try {

			count = dashBoardDao.getAllApprovalCount(authDetailsVo);

			if (count != 0) {

				requestVo.setTotalRecords(count);
			}

		} catch (Exception e) {
			Log.info("Dashboard Service getAllApprovalCount Exception",e);
			throw new CommonException(getMessage("dbFailure",authDetailsVo));
		}

		return requestVo;

	}

	@Transactional
	public List<ApprovalVO> getAllApproval(ApprovalVO approvalVO, AuthDetailsVo authDetailsVo) {

		List<ApprovalVO> approvalVOList = new ArrayList<ApprovalVO>();

		List<Object[]> approvalList = null;
		
		try {
			// Get all the Approval list from DB
			approvalList = dashBoardDao.getAllApproval(approvalVO, authDetailsVo);

			// Set all the Fields of Approval details
			if (approvalList != null && approvalList.size() > 0) {
				approvalVOList = approvalService.approvalGetAll(approvalList);
			}

		} catch (Exception e) {
			Log.info("Dashboard Service getAllApproval Exception", e);
			throw new CommonException(getMessage("dbFailure", authDetailsVo));
		}

		return approvalVOList;
	}

	@Transactional
	public List<RequestResolverVO> getAwaitingResolverList(RequestResolverVO requestResolverVO,AuthDetailsVo authDetailsVo) {
		try {

			List<Object[]> listRequestEntity = null;

			listRequestEntity = dashBoardDao.getAwaitingResolverList(requestResolverVO,authDetailsVo);

			List<RequestResolverVO> requestVoList = new ArrayList<RequestResolverVO>();
			requestVoList = requestResolverService.getAllResolverList(listRequestEntity);
			return requestVoList;
		} catch (Exception e) {
			Log.info("Dashboard Service getAwaitingResolverList Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

	}

/*	public List<RequestResolverVO> getAllResolverList(List<Object[]> listrequestEntity) {
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

			if (null !=  (Integer) ((Object[]) object)[26]){
				requestResolverVO.setForwardRequestId(Integer.parseInt(String.valueOf(object[26])));
			}
			
			if (null !=  (Integer) ((Object[]) object)[27]){
				requestResolverVO.setRedirectRequestId(Integer.parseInt(String.valueOf(object[27])));
			}
		 					
			if (null != (String) ((Object[]) object)[28]) {
				requestResolverVO.setForwardRedirectRemarks((String) object[28]);
			}

			if (null != (String) ((Object[]) object)[29]) {
				requestResolverVO.setResolverRemarks((String) object[29]);
			}
			
			requestResolverVOList.add(requestResolverVO);

		}

		return requestResolverVOList;

	}*/

	@Transactional
	public RequestVO getResolverListCount(AuthDetailsVo authDetailsVo) {
		try {

			RequestVO requestVo = new RequestVO();

			int count = 0;

			count = dashBoardDao.getResolverListCount(authDetailsVo);

			if (count != 0) {
				requestVo.setTotalRecords(count);
			}
			return requestVo;
		} catch (Exception e) {
			Log.info("Dashboard Service getResolverListCount Exception",e);
			throw new CommonException(getMessage("dataFailure", authDetailsVo));
		}

	}

/*	public List<ApprovalVO> getAllApprovalList(List<Object[]> approvalList) {

		List<ApprovalVO> approvalVOList = new ArrayList<ApprovalVO>();

		for (Object approval : approvalList) {
			ApprovalVO approvalVO = new ApprovalVO();

			if (null != (Integer) ((Object[]) approval)[0]) {
				approvalVO.setRequestId((int) ((Object[]) approval)[0]);
			}
			if (null != (String) ((Object[]) approval)[1]) {
				approvalVO.setRequestCode((String) ((Object[]) approval)[1]);
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

			approvalVOList.add(approvalVO);
		}
		return approvalVOList;
	}*/
}
