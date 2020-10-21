package com.srm.rta.service;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.constants.DelegationTypeEnum;
import com.srm.coreframework.constants.FilePathConstants;
import com.srm.coreframework.controller.CommonController;
import com.srm.coreframework.response.JSONResponse;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.coreframework.vo.EntityLicenseVO;
import com.srm.coreframework.vo.UserMasterVO;
import com.srm.rta.dao.UserDelegationDAO;
import com.srm.rta.entity.RequestEntity;
import com.srm.rta.entity.UserDelegationDetailsEntity;
import com.srm.rta.entity.UserDelegationMasterEntity;
import com.srm.rta.vo.EmailVo;
import com.srm.rta.vo.RequestDetailVO;
import com.srm.rta.vo.RequestSubTypeVO;
import com.srm.rta.vo.RequestTypeVO;
import com.srm.rta.vo.UserDelegationDetailsVO;
import com.srm.rta.vo.UserDelegationMasterVO;

@Component
public class UserDelegationService extends CommonController<UserDelegationMasterVO> {
	Logger logger = LoggerFactory.getLogger(UserDelegationService.class);

	@Autowired
	UserDelegationDAO userDelegationDao;

	@Value("${emailRestTemplateUrl}")
	private String emailRestTemplateUrl;
		
	@Autowired
	private RestTemplate restTemplate;
	
	@Transactional
	public void create(UserDelegationMasterVO userDelegationMasterVo,AuthDetailsVo authDetailsVo) {

		UserDelegationMasterEntity userDelegationMasterEntity = new UserDelegationMasterEntity();

		if (userDelegationMasterVo.getDelegationId() != null) {
			userDelegationMasterEntity.setDelegationId(userDelegationMasterVo.getDelegationId());
			userDelegationMasterEntity.setUpdateDate(CommonConstant.getCalenderDate());
			userDelegationMasterEntity.setUpdateBy(authDetailsVo.getUserId());
		}

		userDelegationMasterEntity.setDelegationUserId(userDelegationMasterVo.getDelegationUserId());
		if(null != userDelegationMasterVo.getDelegationActive() && userDelegationMasterVo.getDelegationActive().equals(CommonConstant.STRING_ONE)){
			userDelegationMasterEntity.setDelegationActive(CommonConstant.FLAG_ONE);
		}else{
			userDelegationMasterEntity.setDelegationActive(CommonConstant.FLAG_ZERO);
		}
		

		userDelegationMasterEntity.setCreateBy(authDetailsVo.getUserId());
		userDelegationMasterEntity.setCreateDate(CommonConstant.getCalenderDate());
		userDelegationMasterEntity.setUpdateBy(authDetailsVo.getUserId());
		userDelegationMasterEntity.setUpdateDate(CommonConstant.getCalenderDate());
		userDelegationMasterEntity.setDeleteFlag(CommonConstant.FLAG_ZERO);
		userDelegationMasterEntity.setEntityLicenseId(authDetailsVo.getEntityId());
		userDelegationMasterEntity = userDelegationDao.create(userDelegationMasterEntity);

		for (UserDelegationDetailsVO delegationMasterVo : userDelegationMasterVo.getUserDelegationDetailsVoList()) {

			UserDelegationDetailsEntity userDelegationDetailsEntity = new UserDelegationDetailsEntity();

			if (delegationMasterVo.getDelegationDetailId() != null) {
				userDelegationDetailsEntity.setDelegationDetailId(delegationMasterVo.getDelegationDetailId());
				userDelegationDetailsEntity.setUpdateBy(authDetailsVo.getUserId());
				userDelegationDetailsEntity.setUpdateDate(CommonConstant.getCalenderDate());
			}
			userDelegationDetailsEntity.setDelegationId(userDelegationMasterEntity.getDelegationId());
			userDelegationDetailsEntity.setDelegatedUserId(delegationMasterVo.getDelegatedUserId());
			userDelegationDetailsEntity.setDelegatedUserActive(delegationMasterVo.isDelegatedUserActive());
			userDelegationDetailsEntity.setUserActiveFrom(delegationMasterVo.getUserActiveFrom());
			userDelegationDetailsEntity.setUserActiveTo(delegationMasterVo.getUserActiveTo());
			if(null != delegationMasterVo.getDelegationRemarks()){
				userDelegationDetailsEntity.setDelegationRemarks(delegationMasterVo.getDelegationRemarks());
			}
			userDelegationDetailsEntity.setUserType(delegationMasterVo.getUserType());
			userDelegationDetailsEntity.setCreateBy(authDetailsVo.getUserId());
			userDelegationDetailsEntity.setCreateDate(CommonConstant.getCalenderDate());
			userDelegationDetailsEntity.setUpdateBy(authDetailsVo.getUserId());
			userDelegationDetailsEntity.setUpdateDate(CommonConstant.getCalenderDate());
			userDelegationDetailsEntity.setDeleteFlag(CommonConstant.FLAG_ZERO);
			userDelegationDetailsEntity.setEntityLicenseId(authDetailsVo.getEntityId());
			userDelegationDao.createDetails(userDelegationDetailsEntity);
						
			//Delegate Email
			EmailVo emailVo = new EmailVo();
			emailVo.setMessageCode(CommonConstant.DELEGATE_USER);
			emailVo.setGroupId(CommonConstant.DUS);
			//emailVo.setToUserId(requestEntity.getCreateBy());
			//emailVo.setRequestId(requestWorkFlowAuditVo.getRequestId());
			
			emailVo.setEmailFlag(0);
			emailVo.setCreateBy(authDetailsVo.getUserId());
			emailVo.setUpdateBy(authDetailsVo.getUserId());
			emailVo.setCreateDate(CommonConstant.getCalenderDate());
			emailVo.setUpdateDate(CommonConstant.getCalenderDate());
			emailVo.setEntityId(authDetailsVo.getEntityId());
			emailVo.setEscalationFlag(CommonConstant.CONSTANT_ZERO);
					 			  								 			 		 							
			emailVo.setSystemConfigurationVo(getSystemConfigurationDetails(authDetailsVo));
		 				
			UserMasterVO userMasterVO1 = getEmailAddress(userDelegationMasterVo.getDelegationUserId(),authDetailsVo);		 
			emailVo.setDelegatedFromUser(userMasterVO1.getUserName()); 
						
			//executor		 
			UserMasterVO userMasterVO2 = getEmailAddress(delegationMasterVo.getDelegatedUserId(),authDetailsVo);
			emailVo.setToUserAddress(userMasterVO2.getEmailId());
			emailVo.setDelegatedToUser(userMasterVO2.getUserName()); 
			emailVo.setUserLang(userMasterVO2.getLangCode());										
			
			emailVo.setNotificationFlag(CommonConstant.CONSTANT_ONE);
			emailVo.setToUserId(userMasterVO2.getId());
			emailVo.setUserName(userMasterVO2.getUserName());
			emailVo.setTrEmailFlag(CommonConstant.CONSTANT_ONE);
			
			AuthDetailsVo authDetVo = new AuthDetailsVo();
			authDetVo.setLangCode(userMasterVO2.getLangCode());
			
			if (delegationMasterVo.getUserType() == 1) {
				emailVo.setDelegationRole(getMessage("delegateApproverRole" ,authDetVo));
			} else {
				emailVo.setDelegationRole(getMessage("delegateResolverRole" ,authDetVo));
			}
			 		
			if (null != delegationMasterVo ) {
				if (!delegationMasterVo.isDelegatedUserActive()) {
					emailVo.setDelegatedStatus(getMessage("roleInActive", authDetVo));
				} else {
					emailVo.setDelegatedStatus(getMessage("roleActive", authDetVo));
				}

			}

			SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");	 	
			
			
			if( null != delegationMasterVo &&
					  null != delegationMasterVo.getUserActiveFrom()){
				emailVo.setDelegatedFromDate(DATE_FORMAT.format(delegationMasterVo.getUserActiveFrom()));	
			}
			
			
			if( null != delegationMasterVo
					 && null != delegationMasterVo.getUserActiveTo()){
				emailVo.setDelegatedToDate(DATE_FORMAT.format(delegationMasterVo.getUserActiveTo()));	
			}

			if (null != emailVo.getSystemConfigurationVo()) {
				EntityLicenseVO entityLicenseVO = getEntityDetails(authDetailsVo.getEntityId());
				if (null != entityLicenseVO.getEmailFlag() && entityLicenseVO.getEmailFlag() == 1) {
					final String uri = emailRestTemplateUrl + FilePathConstants.TOEMAIL;
					JSONResponse response = restTemplate.postForObject(uri, emailVo, JSONResponse.class);

				}
			}
		}

	}

	@Transactional
	public List<UserDelegationMasterVO> getAll(UserDelegationMasterVO userDelegationMasterVo,AuthDetailsVo authDetailsVo) {

		List<Object[]> delegationEntity = userDelegationDao.getAll(userDelegationMasterVo,authDetailsVo);
		List<UserDelegationMasterVO> delegationMasterVo = getAllList(delegationEntity);
		if (delegationMasterVo != null && delegationMasterVo.size() > 0) {
			userDelegationMasterVo.setUserDelegationMasterVoList(delegationMasterVo);
		}

		return delegationMasterVo;

	}

	private List<UserDelegationMasterVO> getAllList(List<Object[]> delegationEntity) {

		List<UserDelegationMasterVO> userDelegationMasterVo = new ArrayList<UserDelegationMasterVO>();

		for (Object[] object : delegationEntity) {

			UserDelegationMasterVO delegationMasterVo = new UserDelegationMasterVO();
			UserDelegationDetailsVO userDelegationDetailsVO = new UserDelegationDetailsVO();

			if (null != object[0]) {
				delegationMasterVo.setUserName((String) object[0]);

			}

			if (null != object[1]) {
				delegationMasterVo.setDelegatedUserName((String) object[1]);

			}

			if (null != object[2]) {
				try {
					delegationMasterVo.setActiveFrom((Date) object[2]);
				} catch (Exception e) {
					e.getMessage();
				}
			}

			if (null != object[3]) {
				delegationMasterVo.setActiveTo((Date) object[3]);
			}

			if (object[4] != null) {

				if ((int) object[4] == 1) {
					delegationMasterVo.setUserType(CommonConstant.CONSTANT_ONE);
					delegationMasterVo.setUserTypeName(DelegationTypeEnum.APPROVER.toString());
				} else {
					delegationMasterVo.setUserType(CommonConstant.CONSTANT_TWO);
					delegationMasterVo.setUserTypeName(DelegationTypeEnum.RESOLVER.toString());
				}

			}
			if (null != object[5]) {
				userDelegationDetailsVO.setDelegatedUserId((Integer) object[5]);
			}
			if (null != object[6]) {
				userDelegationDetailsVO.setDelegationDetailId((Integer) object[6]);
			}
			if(null != object[7]){
				userDelegationDetailsVO.setDelegationRemarks((String) object[7]);

			} 
			
			if (null != object[8]) {
				if (((Byte) object[8]).intValue() == 1) {
					userDelegationDetailsVO.setDelegatedUserActive(true);
				} else {
					userDelegationDetailsVO.setDelegatedUserActive(false);
				}
			}								
			
			delegationMasterVo.setUserDelegationDetailsVo(userDelegationDetailsVO);
			userDelegationMasterVo.add(delegationMasterVo);

		}

		return userDelegationMasterVo;
	}

	@Transactional
	public void delete(UserDelegationMasterVO userDelegationMasterVo,AuthDetailsVo authDetailsVo) {
		userDelegationDao.delete(userDelegationMasterVo,authDetailsVo);

	}

	@Transactional
	public List<UserDelegationMasterVO> search(UserDelegationMasterVO userDelegationMasterVo,AuthDetailsVo authDetailsVo) {
		List<Object[]> delegationEntity = userDelegationDao.search(userDelegationMasterVo,authDetailsVo);
		List<UserDelegationMasterVO> delegationMasterVo = getAllSearch(delegationEntity);
		if (delegationMasterVo != null && delegationMasterVo.size() > 0) {
			userDelegationMasterVo.setUserDelegationMasterVoList(delegationMasterVo);
		}

		return delegationMasterVo;
	}

	private List<UserDelegationMasterVO> getAllSearch(List<Object[]> delegationEntity) {
		List<UserDelegationMasterVO> userDelegationMasterVo = new ArrayList<UserDelegationMasterVO>();

		for (Object[] object : delegationEntity) {

			UserDelegationMasterVO delegationMasterVo = new UserDelegationMasterVO();

			if (null != object[0]) {
				delegationMasterVo.setUserName((String) object[0]);

			}

			if (null != object[1]) {
				delegationMasterVo.setDelegatedUserName((String) object[1]);

			}

			if (null != object[2]) {
				try {
					delegationMasterVo.setActiveFrom((Date) object[2]);
				} catch (Exception e) {
					e.getMessage();
				}
			}

			if (null != object[3]) {
				delegationMasterVo.setActiveTo((Date) object[3]);
			}

			if (object[4] != null) {

				if ((int) object[4] == 1) {
					delegationMasterVo.setUserType(CommonConstant.CONSTANT_ONE);
					delegationMasterVo.setUserTypeName(DelegationTypeEnum.APPROVER.toString());
				} else {
					delegationMasterVo.setUserType(CommonConstant.CONSTANT_TWO);
					delegationMasterVo.setUserTypeName(DelegationTypeEnum.RESOLVER.toString());
				}

			}
			if (null != object[5]) {
				delegationMasterVo.setDelegationRemarks((String) object[5]);
			}
			userDelegationMasterVo.add(delegationMasterVo);

		}

		return userDelegationMasterVo;
	}

	public List<UserDelegationMasterVO> getAllUsers(UserDelegationMasterVO userDelegationMasterVo,AuthDetailsVo authDetailsVo) {
		List<Object[]> delegationEntity = userDelegationDao.getAllUsers(userDelegationMasterVo,authDetailsVo);
		List<UserDelegationMasterVO> delegationMasterVo = new ArrayList<UserDelegationMasterVO>();
		if (!delegationEntity.isEmpty()) {
			delegationMasterVo = getAllUserList(delegationEntity);
		}
		if (delegationMasterVo != null && delegationMasterVo.size() > 0 && !delegationMasterVo.isEmpty()) {
			userDelegationMasterVo.setUserDelegationMasterVoList(delegationMasterVo);
		}

		return delegationMasterVo;

	}

	private List<UserDelegationMasterVO> getAllUserList(List<Object[]> delegationEntity) {

		List<UserDelegationMasterVO> userDelegationMasterVo = new ArrayList<UserDelegationMasterVO>();
		UserDelegationMasterVO delegationMasterVo = null;
		for (Object[] object : delegationEntity) {

			if (null != object[0] && null != object[1] && ((BigInteger)object[1]).intValue()> 0) {
				delegationMasterVo = new UserDelegationMasterVO();
				delegationMasterVo.setUserName((String) object[0]);

				if (null != object[1]) {
					BigInteger b = (BigInteger) object[1];
					int c = b.intValue();
					if (c > 0) {
						delegationMasterVo.setStatus(DelegationTypeEnum.ASSIGNED.toString());
					} else {
						delegationMasterVo.setStatus(DelegationTypeEnum.UNASSIGNED.toString());
					}
				}

				if (null != object[2]) {
					delegationMasterVo.setDelegationId((Integer) object[2]);
				}

				if (null != object[3]) {
					delegationMasterVo.setDelegationUserId((Integer) object[3]);
				}

				if (null != object[4]) {
					if(((Character) object[4]).equals(CommonConstant.FLAG_ONE)){
						delegationMasterVo.setStatus(CommonConstant.Active);
						delegationMasterVo.setDelegationActive(CommonConstant.Active);
					}else{
						delegationMasterVo.setStatus(CommonConstant.InActive);
						delegationMasterVo.setDelegationActive(CommonConstant.InActive);
					}
					
				}

				if(null != object[5]){
					delegationMasterVo.setDelegationRemarks((String)object[5]);
				}
				
				userDelegationMasterVo.add(delegationMasterVo);
			}
			
		}
		return userDelegationMasterVo;
	}

	public List<UserDelegationMasterVO> getAllSearch(UserDelegationMasterVO userDelegationMasterVo,AuthDetailsVo authDetailsVo) {
		List<Object[]> delegationEntity = userDelegationDao.getUserSearch(userDelegationMasterVo, authDetailsVo);
		List<UserDelegationMasterVO> delegationMasterVo = getAllListUserSearch(delegationEntity);
		if (delegationMasterVo != null && delegationMasterVo.size() > 0) {
			userDelegationMasterVo.setUserDelegationMasterVoList(delegationMasterVo);
		}

		return delegationMasterVo;

	}

	private List<UserDelegationMasterVO> getAllListUserSearch(List<Object[]> delegationEntity) {
		List<UserDelegationMasterVO> userDelegationMasterVo = new ArrayList<UserDelegationMasterVO>();

		for (Object[] object : delegationEntity) {

			UserDelegationMasterVO delegationMasterVo = new UserDelegationMasterVO();

			if (null != object[0]) {
				delegationMasterVo.setUserName((String) object[0]);

			}

			if (null != object[1]) {

				BigInteger b = (BigInteger) object[1];
				int c = b.intValue();

				if (c > 0) {
					delegationMasterVo.setStatus(DelegationTypeEnum.ASSIGNED.toString());

				} else {
					delegationMasterVo.setStatus(DelegationTypeEnum.UNASSIGNED.toString());
				}

			}

			userDelegationMasterVo.add(delegationMasterVo);

		}

		return userDelegationMasterVo;
	}

	public UserDelegationDetailsEntity createUserDelegationDetails(
			UserDelegationDetailsEntity userDelegationDetailsEntity, AuthDetailsVo authDetailsVo) {
		userDelegationDetailsEntity.setCreateDate(CommonConstant.getCalenderDate());
		userDelegationDetailsEntity.setCreateBy(authDetailsVo.getUserId());
		userDelegationDetailsEntity.setUpdateBy(authDetailsVo.getUserId());
		userDelegationDetailsEntity.setUpdateDate(CommonConstant.getCalenderDate());
		userDelegationDetailsEntity.setEntityLicenseId(authDetailsVo.getEntityId());
		return userDelegationDetailsEntity;
	}
	
	@Transactional
	public void update(UserDelegationMasterVO userDelegationMasterVo,AuthDetailsVo authDetailsVo) {


		UserDelegationDetailsEntity userDelegationDetailsEntity = new UserDelegationDetailsEntity();

		if (userDelegationMasterVo.getUserDelegationDetailsVo().getDelegationDetailId() != null) {

			userDelegationDetailsEntity = userDelegationDao.getDetail(userDelegationMasterVo);

			// userDelegationDetailsEntity.setDelegationDetailId(userDelegationMasterVo.getUserDelegationDetailsVo().getDelegationDetailId());

		} else {
			createUserDelegationDetails(userDelegationDetailsEntity, authDetailsVo);
		}

		if (userDelegationMasterVo.getDelegationId() != null) {
			userDelegationDetailsEntity.setDelegationId(userDelegationMasterVo.getDelegationId());
		}
		if (null != userDelegationMasterVo.getDelegationRemarks()) {
			userDelegationDetailsEntity.setDelegationRemarks(userDelegationMasterVo.getDelegationRemarks());
		}
		if (userDelegationMasterVo.getUserDelegationDetailsVo().getDelegatedUserId() != null) {
			userDelegationDetailsEntity
					.setDelegatedUserId(userDelegationMasterVo.getUserDelegationDetailsVo().getDelegatedUserId());

		}

		if (userDelegationMasterVo.getUserDelegationDetailsVo().getUserType() != null) {

			userDelegationDetailsEntity.setUserType(userDelegationMasterVo.getUserDelegationDetailsVo().getUserType());
		}

		if (null != userDelegationMasterVo.getUserDelegationDetailsVo().getUserActiveFrom()) {
			userDelegationDetailsEntity
					.setUserActiveFrom(userDelegationMasterVo.getUserDelegationDetailsVo().getUserActiveFrom());
		}

		if (null != userDelegationMasterVo.getUserDelegationDetailsVo().getUserActiveTo()) {
			userDelegationDetailsEntity
					.setUserActiveTo(userDelegationMasterVo.getUserDelegationDetailsVo().getUserActiveTo());
		}

		if (userDelegationMasterVo.getUserDelegationDetailsVo().isDelegatedUserActive()) {
			userDelegationDetailsEntity.setDelegatedUserActive(true);

		} else {
			userDelegationDetailsEntity.setDelegatedUserActive(false);
		}
			
		if (userDelegationMasterVo.getUserDelegationDetailsVo().getDelegationRemarks() != null) {

			userDelegationDetailsEntity.setDelegationRemarks(userDelegationMasterVo.getUserDelegationDetailsVo().getDelegationRemarks());
		}
				
			userDelegationDetailsEntity.setDeleteFlag(CommonConstant.FLAG_ZERO);
			userDelegationDetailsEntity.setEntityLicenseId(authDetailsVo.getEntityId());
			userDelegationDao.updateUser(userDelegationDetailsEntity);
			
			
			//Delegate Email
			EmailVo emailVo = new EmailVo();
			emailVo.setMessageCode(CommonConstant.DELEGATE_MODIFY_USER);
			emailVo.setGroupId(CommonConstant.DMU);
			//emailVo.setToUserId(requestEntity.getCreateBy());
			//emailVo.setRequestId(requestWorkFlowAuditVo.getRequestId());
			
			emailVo.setEmailFlag(0);
			emailVo.setCreateBy(authDetailsVo.getUserId());
			emailVo.setUpdateBy(authDetailsVo.getUserId());
			emailVo.setCreateDate(CommonConstant.getCalenderDate());
			emailVo.setUpdateDate(CommonConstant.getCalenderDate());
			emailVo.setEntityId(authDetailsVo.getEntityId());
			emailVo.setEscalationFlag(CommonConstant.CONSTANT_ZERO);
					 			  								 			 		 							
			emailVo.setSystemConfigurationVo(getSystemConfigurationDetails(authDetailsVo));
		 				
			UserMasterVO userMasterVO1 = getEmailAddress(userDelegationMasterVo.getDelegationUserId(),authDetailsVo);		 
			emailVo.setDelegatedFromUser(userMasterVO1.getUserName()); 
						
			//executor		 
			UserMasterVO userMasterVO2 = getEmailAddress(userDelegationMasterVo.getUserDelegationDetailsVo().getDelegatedUserId(),authDetailsVo);
			emailVo.setToUserAddress(userMasterVO2.getEmailId());
			emailVo.setDelegatedToUser(userMasterVO2.getUserName()); 
			emailVo.setUserLang(userMasterVO2.getLangCode());						
			
			emailVo.setNotificationFlag(CommonConstant.CONSTANT_ONE);
			emailVo.setToUserId(userMasterVO2.getId());
			emailVo.setUserName(userMasterVO2.getUserName());
			emailVo.setTrEmailFlag(CommonConstant.CONSTANT_ONE);
								 			 
			AuthDetailsVo authDetVo = new AuthDetailsVo();
			authDetVo.setLangCode(userMasterVO2.getLangCode());
			
			if (userDelegationMasterVo.getUserDelegationDetailsVo().getUserType() == 1) {
				emailVo.setDelegationRole(getMessage("delegateApproverRole" ,authDetVo ));
			} else {
				emailVo.setDelegationRole(getMessage("delegateResolverRole" ,authDetVo ));
			}
									
		if (null != userDelegationMasterVo && null != userDelegationMasterVo.getUserDelegationDetailsVo()) {
			if (!userDelegationMasterVo.getUserDelegationDetailsVo().isDelegatedUserActive()) {
				emailVo.setDelegatedStatus(getMessage("roleInActive", authDetVo));
			} else {
				emailVo.setDelegatedStatus(getMessage("roleActive", authDetVo));
			}

		}
			SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");	 		
			
			if( null != userDelegationMasterVo.getUserDelegationDetailsVo() 
					&& null != userDelegationMasterVo.getUserDelegationDetailsVo() && null != userDelegationMasterVo.getUserDelegationDetailsVo().getUserActiveFrom()){
				emailVo.setDelegatedFromDate(DATE_FORMAT.format(userDelegationMasterVo.getUserDelegationDetailsVo().getUserActiveFrom()));	
			}
			
			
			if( null != userDelegationMasterVo.getUserDelegationDetailsVo() 
					&& null != userDelegationMasterVo.getUserDelegationDetailsVo() && null != userDelegationMasterVo.getUserDelegationDetailsVo().getUserActiveTo()){
				emailVo.setDelegatedToDate(DATE_FORMAT.format(userDelegationMasterVo.getUserDelegationDetailsVo().getUserActiveTo()));	
			}	

		if (null != emailVo.getSystemConfigurationVo()) {
			EntityLicenseVO entityLicenseVO = getEntityDetails(authDetailsVo.getEntityId());
			if (null != entityLicenseVO.getEmailFlag() && entityLicenseVO.getEmailFlag() == 1) {
				final String uri = emailRestTemplateUrl + FilePathConstants.TOEMAIL;
				JSONResponse response = restTemplate.postForObject(uri, emailVo, JSONResponse.class);
			}
		}
	}

	@Transactional
	public UserDelegationMasterVO singleView(UserDelegationMasterVO userDelegationMasterVo) {

		List<Object[]> objectList = userDelegationDao.singleView(userDelegationMasterVo);

		UserDelegationMasterVO userDelegationMasterVO = new UserDelegationMasterVO();

		for (Object[] object : objectList) {

			UserDelegationDetailsVO userDelegationDetailsEntity = new UserDelegationDetailsVO();

			if (null != object[0]) {
				userDelegationDetailsEntity.setDelegationDetailId((Integer) object[0]);
			}

			if (null != object[1]) {
				userDelegationDetailsEntity.setDelegationId((Integer) object[1]);
			}

			if (null != object[2]) {
				userDelegationDetailsEntity.setDelegatedUserId((Integer) object[2]);

			}
			if (null != object[3]) {
				if(((Byte) object[3]).intValue() == 1){
					userDelegationDetailsEntity.setDelegatedUserActive(true);
				}else{
					userDelegationDetailsEntity.setDelegatedUserActive(false);
				}
			}

			if (null != object[6]) {
				userDelegationDetailsEntity.setUserType((Integer) object[6]);
			}

			if (null != object[4]) {
				userDelegationDetailsEntity.setUserActiveFrom((Date) object[4]);
			}

			if (null != object[5]) {
				userDelegationDetailsEntity.setUserActiveTo((Date) object[5]);
			}

			if (null != object[7]) {
				userDelegationDetailsEntity.setDelegationRemarks((String) object[7]);
			}
			
			userDelegationMasterVO.setUserDelegationDetailsVo(userDelegationDetailsEntity);
		}

		return userDelegationMasterVO;

	}

	public List<UserDelegationMasterVO> view(UserDelegationMasterVO userDelegationMasterVo) {
		List<Object[]> objectList = userDelegationDao.view(userDelegationMasterVo);

		List<UserDelegationMasterVO> userDelegationMasterVOList = new ArrayList<UserDelegationMasterVO>();

		for (Object[] object : objectList) {

			UserDelegationMasterVO userDelegationMasterVO = new UserDelegationMasterVO();

			if (null != object[0]) {
				userDelegationMasterVO.setUserName((String) object[0]);

			}

			if (null != object[1]) {
				userDelegationMasterVO.setDelegatedUserName((String) object[1]);

			}

			/*
			 * if (null != object[2]) {
			 * userDelegationMasterVO.setDelegatedUserId((Integer) object[2]);
			 * 
			 * }
			 */
			if (null != object[3]) {
				userDelegationMasterVO.setUserType((Integer) object[3]);

			}

			if (null != object[4]) {
				userDelegationMasterVO.setActiveFrom((Date) object[4]);

			}

			if (null != object[5]) {
				userDelegationMasterVO.setActiveTo((Date) object[5]);

			}
			/*
			 * if (null != object[5]) {
			 * userDelegationMasterVO.setUserActiveTo((Date) object[5]);
			 * 
			 * }
			 */

			userDelegationMasterVOList.add(userDelegationMasterVO);
		}

		return userDelegationMasterVOList;
	}

	@Transactional()
	public void findDuplicateUpdate(UserDelegationMasterVO userDelegationMasterVO,AuthDetailsVo authDetailsVo) throws CommonException {
		try {
			int count = userDelegationDao.findDuplicateUpdate(userDelegationMasterVO,authDetailsVo,userDelegationMasterVO.getUserDelegationDetailsVo());
			if (count > 0) {
				throw new CommonException(getMessage("noUniqueFound",authDetailsVo));
			}
		} catch (CommonException e) {
			logger.error(e.getMessage());
			throw new CommonException(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure",authDetailsVo));

		}
	}
	@Transactional()
	public void findDuplicate(UserDelegationMasterVO userDelegationMasterVO,AuthDetailsVo authDetailsVo) throws CommonException {
		try {
			for (UserDelegationDetailsVO delegationMasterVo : userDelegationMasterVO.getUserDelegationDetailsVoList()) {
			int count = userDelegationDao.findDuplicate(userDelegationMasterVO,authDetailsVo,delegationMasterVo);
			if (count > 0) {
				throw new CommonException(getMessage("duplicatemappingFound",authDetailsVo));
			}
			}
		} catch (CommonException e) {
			logger.error(e.getMessage());
			throw new CommonException(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure",authDetailsVo));

		}
	}
	
	@Transactional()
	public void updateInactive() {
		try {
			 			
			List<Object[]> entityLicenseList = userDelegationDao.getAllExpiredDelegatedList();
			
			for (Object delegatedObj : entityLicenseList) {

				if (null != (Integer) ((Object[]) delegatedObj)[0]) {
					int delegationId = (int) ((Object[]) delegatedObj)[0];
					userDelegationDao.updateInactive(delegationId);
				}
			}				
		} catch (Exception e) {
			logger.error(e.getMessage());
			 e.printStackTrace();
		}
	}
	
}
