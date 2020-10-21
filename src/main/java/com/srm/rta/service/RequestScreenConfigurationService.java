package com.srm.rta.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.controller.CommonController;
import com.srm.coreframework.entity.CodeGenerationEntity;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.rta.dao.RequestScreenConfigurationDAO;
import com.srm.rta.entity.RequestScreenConfigurationEntity;
import com.srm.rta.entity.RequestScreenDetailConfigurationEntity;
import com.srm.rta.repository.RequestScreenConfigurationRepository;
import com.srm.rta.repository.RequestScreenDetailConfigRepository;
import com.srm.rta.vo.RequestScreenConfigurationVO;
import com.srm.rta.vo.RequestScreenDetailConfigurationVO;

@Service
public class RequestScreenConfigurationService extends CommonController<RequestScreenConfigurationVO> {

	Logger logger = LoggerFactory.getLogger(RequestScreenConfigurationService.class);

	@Autowired
	private RequestScreenConfigurationDAO requestScreenConfigurationDAO;

	@Autowired
	RequestTypeService requestTypeService;

	@Autowired
	RequestSubTypeService requestSubTypeService;

	@Autowired
	RequestScreenConfigurationRepository requestScreenConfigurationRepository;

	@Autowired
	RequestScreenDetailConfigRepository requestScreenDetailConfigRepository;

	/**
	 * This method is used to list all the RequestScreenConfiguration details .
	 * 
	 * @param RequestScreenConfigurationVo
	 *            requestScreenConfigurationVogetAll
	 * @return List
	 *         <RequestScreenConfigurationVo> requestScreenConfigurationVoList
	 *
	 */

	@Transactional
	public List<RequestScreenConfigurationVO> getAll(AuthDetailsVo authDetailsVo) {

		List<RequestScreenConfigurationVO> listRequestScreenConfigurationVo = new ArrayList<RequestScreenConfigurationVO>();
		List<Object[]> listRequestScreenConfigurationEntity = null;
		try {

			listRequestScreenConfigurationEntity = requestScreenConfigurationDAO.getAll(authDetailsVo);
			listRequestScreenConfigurationVo = getAllList(listRequestScreenConfigurationEntity,authDetailsVo);

			return listRequestScreenConfigurationVo;

		} catch (Exception e) {
			logger.error("error", e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
	}

	/**
	 * This method is used to get all the different types of values in external
	 * link.
	 * 
	 * @param RequestScreenConfigurationVo
	 *            requestScreenConfigurationVoSearch
	 * @return List
	 *         <RequestScreenConfigurationVo> listRequestScreenConfigurationVo
	 *
	 */

	@Transactional
	public List<RequestScreenConfigurationVO> getAllSearch(RequestScreenConfigurationVO requestScreenConfigurationVo,AuthDetailsVo authDetailsVo) {

		List<RequestScreenConfigurationVO> listRequestScreenConfigurationVo = new ArrayList<RequestScreenConfigurationVO>();
		List<Object[]> listRequestScreenConfigurationEntity = null;
		try {

			listRequestScreenConfigurationEntity = requestScreenConfigurationDAO
					.getAllSearch(requestScreenConfigurationVo,authDetailsVo);
			listRequestScreenConfigurationVo = getAllList(listRequestScreenConfigurationEntity,authDetailsVo);
			return listRequestScreenConfigurationVo;

		} catch (Exception e) {
			logger.error("error", e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
	}

	/**
	 * Method is used to Get all list the RequestScreenConfiguration details.
	 * 
	 * 
	 * @param List<RequestScreenConfigurationEntity>
	 *            list_RequestScreenConfigurationVo
	 * @return RequestScreenConfigurationVo RequestScreenConfigurationVolist
	 *         list <List<Object[]>
	 */

	public List<RequestScreenConfigurationVO> getAllList(List<Object[]> listRequestScreenConfigurationEntity,AuthDetailsVo authDetailsVo) {

		List<RequestScreenConfigurationVO> listRequestScreenConfigurationVo = new ArrayList<RequestScreenConfigurationVO>();
		try {
			for (Object[] object : listRequestScreenConfigurationEntity) {
				RequestScreenConfigurationVO requestScreenConfigurationVo = new RequestScreenConfigurationVO();
				requestScreenConfigurationVo.setRequestScreenConfigId((int) object[0]);
				requestScreenConfigurationVo.setRequestScreenConfigurationCode((String) object[1]);
				requestScreenConfigurationVo.setRequestScreenConfigurationName((String) object[2]);
				requestScreenConfigurationVo.setRequestTypeName((String) object[4]);
				requestScreenConfigurationVo.setRequestSubTypeName((String) object[5]);
				requestScreenConfigurationVo.setRequestScreenConfigurationIsActive((boolean) object[3]);
				requestScreenConfigurationVo.setRequestTypeId((int) object[6]);
				requestScreenConfigurationVo.setRequestSubtypeId((int) object[7]);

				if (requestScreenConfigurationVo.isRequestScreenConfigurationIsActive()) {
					requestScreenConfigurationVo.setStatus(CommonConstant.Active);
				} else {
					requestScreenConfigurationVo.setStatus(CommonConstant.InActive);
				}

				listRequestScreenConfigurationVo.add(requestScreenConfigurationVo);
			}
			return listRequestScreenConfigurationVo;
		} catch (Exception e) {
			logger.error("error", e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
	}

	/**
	 * This method is used to create the requestScreenConfiguration and its
	 * requestScreenConfigurationDetail. check requestScreenConfiguration code
	 * already exist and also check from and to date should not collide with any
	 * other dates in DB as well as in the list of
	 * requestScreenConfigurationVoDetail inputs provided.
	 * 
	 * @param RequestScreenConfigurationVo
	 *            requestScreenConfigurationVo
	 * @return void
	 */
	@Transactional
	public RequestScreenConfigurationVO create(RequestScreenConfigurationVO requestScreenConfigurationVo,AuthDetailsVo authDetailsVo) {
		 
		String code = null;

		RequestScreenConfigurationEntity screenConfigurationEntity = new RequestScreenConfigurationEntity();

		try {

			code = requestScreenConfigurationDAO.findAutoGenericCode(CommonConstant.RequestScreenConfiguration,authDetailsVo);
		} catch (Exception e) {
			logger.error("error", e);
			throw new CommonException(getMessage("autoCodeGenerationFailure",authDetailsVo));
		}
		try {
			BeanUtils.copyProperties(requestScreenConfigurationVo, screenConfigurationEntity);
			screenConfigurationEntity.setRequestScreenConfigurationCode(code);
		} catch (Exception e) {
			logger.error("error", e);
			throw new CommonException(getMessage("beanUtilPropertiesFailure",authDetailsVo));
		}
		if (null != authDetailsVo.getEntityId()) {
			screenConfigurationEntity.setEntityLicenseId(authDetailsVo.getEntityId());
		}
		try {
			requestScreenConfigurationRepository.save(screenConfigurationEntity);

			if (null != requestScreenConfigurationVo
					&& null != requestScreenConfigurationVo.getRequestScreenDetailConfigurationVoList()) {

				screenConfigurationEntity = setCreateUserDetails(screenConfigurationEntity,authDetailsVo);

				fetchRequestScreenDetailConfigValues(requestScreenConfigurationVo, screenConfigurationEntity,authDetailsVo);

			}
		} catch (Exception e) {
			logger.error("error", e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
		if(null != screenConfigurationEntity && null != screenConfigurationEntity.getRequestScreenConfigurationCode()){
			requestScreenConfigurationVo.setRequestScreenConfigurationCode(screenConfigurationEntity.getRequestScreenConfigurationCode());
		}
		return requestScreenConfigurationVo;
	}

	/**
	 * This method is used to check RequestScreenConfiguration Configuration
	 * Code is already Available or not.
	 * 
	 * 
	 * @param String
	 *            RequestScreenConfigurationCode
	 * @return boolean
	 */
	@Transactional
	public boolean isRequestScreenConfigurationCodeAvailable(String requestScreenConfigurationCode,AuthDetailsVo authDetailsVo) {

		Integer entityId = authDetailsVo.getEntityId();
		boolean result = true;
		Integer count;
		try {

			count = requestScreenConfigurationRepository.isRequestScreenConfigurationCodeAvailable(entityId,
					requestScreenConfigurationCode);
			if (count == 0) {
				return false;
			}
			return result;
		} catch (NoResultException e) {
			logger.error("error", e);
			throw new CommonException(getMessage("noResultFound",authDetailsVo));
		} catch (NonUniqueResultException e) {
			logger.error("error", e);
			throw new CommonException(getMessage("noRecordFound",authDetailsVo));
		} catch (Exception e) {
			logger.error("error", e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
	}

	/**
	 * This method is used to fetch RequestScreenConfigurationDetail Values and
	 * set in to Entity.
	 * 
	 * 
	 * @param RequestScreenConfigurationVo
	 *            requestScreenConfigurationVo, RequestScreenConfigurationEntity
	 *            screenConfigurationEntity
	 * @return
	 */
	@SuppressWarnings("unused")
	@Transactional
	private void fetchRequestScreenDetailConfigValues(RequestScreenConfigurationVO requestScreenConfigurationVo,
			RequestScreenConfigurationEntity screenConfigurationEntity,AuthDetailsVo authDetailsVo) {

		List<RequestScreenDetailConfigurationEntity> screenDetailConfigurationEntityList = new ArrayList<RequestScreenDetailConfigurationEntity>();
		List<RequestScreenDetailConfigurationVO> requestScreenDetailConfigurationVoList = requestScreenConfigurationVo
				.getRequestScreenDetailConfigurationVoList();

		for (RequestScreenDetailConfigurationVO requestScreenDetailConfigurationVo : requestScreenDetailConfigurationVoList) {

			requestScreenDetailConfigurationVo
					.setRequestScreenConfigId(screenConfigurationEntity.getRequestScreenConfigId());

			RequestScreenDetailConfigurationEntity screenDetailConfigurationEntity = new RequestScreenDetailConfigurationEntity();
			if (null != requestScreenDetailConfigurationVo.getRequestScreenDetailConfigId()
					&& requestScreenDetailConfigurationVo.getRequestScreenDetailConfigId() != 0) {
				screenDetailConfigurationEntity = findDetailId(
						requestScreenDetailConfigurationVo.getRequestScreenDetailConfigId(),authDetailsVo);
			}
			try {
				BeanUtils.copyProperties(requestScreenDetailConfigurationVo, screenDetailConfigurationEntity);
				screenDetailConfigurationEntity = setUpdatedUserDetails(screenDetailConfigurationEntity,authDetailsVo);
			} catch (Exception e) {
				logger.error("error", e);
				throw new CommonException(getMessage("beanUtilPropertiesFailure",authDetailsVo));
			}
			if (null != authDetailsVo.getEntityId()) {
				screenDetailConfigurationEntity.setEntityLicenseId(authDetailsVo.getEntityId());
			}
			try {
				requestScreenDetailConfigRepository.save(screenDetailConfigurationEntity);

			} catch (NoResultException e) {
				logger.error("error", e);
				throw new CommonException(getMessage("noResultFound",authDetailsVo));
			} catch (NonUniqueResultException e) {
				logger.error("error", e);
				throw new CommonException(getMessage("noRecordFound",authDetailsVo));
			} catch (Exception e) {
				logger.error("error", e);
				throw new CommonException(getMessage("dataFailure",authDetailsVo));
			}
		}

	}

	private RequestScreenDetailConfigurationEntity setUpdatedUserDetails(
			RequestScreenDetailConfigurationEntity screenDetailConfigurationEntity,AuthDetailsVo authDetailsVo) {

		screenDetailConfigurationEntity.setCreateBy(authDetailsVo.getUserId());
		screenDetailConfigurationEntity.setCreateDate(CommonConstant.getCalenderDate());
		screenDetailConfigurationEntity.setUpdateBy(authDetailsVo.getUserId());
		screenDetailConfigurationEntity.setUpdateDate(CommonConstant.getCalenderDate());
		screenDetailConfigurationEntity.setDeleteFlag(CommonConstant.FLAG_ZERO);
		return screenDetailConfigurationEntity;
	}

	/**
	 * This method is used to update the requestScreenConfiguration and its
	 * requestScreenConfigurationDetail. check requestScreenConfiguration code.
	 * 
	 * @param RequestScreenConfigurationVo
	 *            requestScreenConfigurationVo
	 * @return void
	 */
	@Transactional
	public RequestScreenConfigurationVO update(RequestScreenConfigurationVO requestScreenConfigurationVo,AuthDetailsVo authDetailsVo) {

		RequestScreenConfigurationEntity requestScreenConfigurationEntity = findId(
				requestScreenConfigurationVo.getRequestScreenConfigId(),authDetailsVo);

		try {
			BeanUtils.copyProperties(requestScreenConfigurationVo, requestScreenConfigurationEntity);
		} catch (Exception e) {
			logger.error("error", e);
			throw new CommonException(getMessage("beanUtilPropertiesFailure",authDetailsVo));
		}

		if (null != authDetailsVo.getEntityId()) {
			requestScreenConfigurationEntity.setEntityLicenseId(authDetailsVo.getEntityId());
		}

		try {
			requestScreenConfigurationRepository.save(requestScreenConfigurationEntity);
			if (null != requestScreenConfigurationVo
					&& null != requestScreenConfigurationVo.getRequestScreenDetailConfigurationVoList()) {

				fetchRequestScreenDetailConfigValues(requestScreenConfigurationVo, requestScreenConfigurationEntity,authDetailsVo);
				requestScreenConfigurationEntity = setUpdatedUserDetails(requestScreenConfigurationEntity,authDetailsVo);
			}
		} catch (NoResultException e) {
			logger.error("error", e);
			throw new CommonException(getMessage("noResultFound",authDetailsVo));
		} catch (NonUniqueResultException e) {
			logger.error("error", e);
			throw new CommonException(getMessage("noRecordFound",authDetailsVo));
		} catch (Exception e) {
			logger.error("error", e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
		if(null != requestScreenConfigurationEntity && null != requestScreenConfigurationEntity.getRequestScreenConfigurationCode()){
			requestScreenConfigurationVo.setRequestScreenConfigurationCode(requestScreenConfigurationEntity.getRequestScreenConfigurationCode());
		}
		return requestScreenConfigurationVo;

	}

	private RequestScreenConfigurationEntity setUpdatedUserDetails(
			RequestScreenConfigurationEntity requestScreenConfigurationEntity,AuthDetailsVo authDetailsVo) {
		requestScreenConfigurationEntity.setCreateBy(authDetailsVo.getUserId());
		requestScreenConfigurationEntity.setCreateDate(CommonConstant.getCalenderDate());
		requestScreenConfigurationEntity.setUpdateBy(authDetailsVo.getUserId());
		requestScreenConfigurationEntity.setUpdateDate(CommonConstant.getCalenderDate());

		return requestScreenConfigurationEntity;
	}

	/**
	 * This method is used to find the corresponding id related details to
	 * modify it.
	 * 
	 * @param int
	 *            id
	 * @return RequestScreenConfigurationEntity requestScreenConfigurationEntity
	 */

	@Transactional
	public RequestScreenConfigurationEntity findId(int id,AuthDetailsVo authDetailsVo) {
		try {
			RequestScreenConfigurationEntity requestScreenConfigurationEntity = requestScreenConfigurationRepository
					.findOne(id);
			return requestScreenConfigurationEntity;
		} catch (NoResultException e) {
			logger.error("error", e);
			throw new CommonException(getMessage("noResultFound",authDetailsVo));
		} catch (CommonException e) {
			logger.error("error", e);
			throw new CommonException(getMessage("noRecordFound",authDetailsVo));
		} catch (Exception e) {
			logger.error("error", e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

	}

	/**
	 * This method is used to delete the requestScreenConfigurationVo and its
	 * requestScreenConfigurationVoDetail.
	 * 
	 * @param RequestScreenConfigurationVo
	 *            requestScreenConfigurationVo
	 * @return void
	 */
	@SuppressWarnings("unused")
	@Transactional
	public void delete(RequestScreenConfigurationVO requestScreenConfigurationVo,AuthDetailsVo authDetailsVo) {

		List<Integer> listRequestScreenConfigurationId = new ArrayList<Integer>();

		for (Integer requestScreenConfigurationId : requestScreenConfigurationVo.getRequestScreenConfigurationList()) {

			RequestScreenConfigurationEntity requestScreenConfigurationEntity = new RequestScreenConfigurationEntity();

			requestScreenConfigurationEntity = findId(requestScreenConfigurationId,authDetailsVo);

			try {
				if (null != requestScreenConfigurationEntity) {
					deleteId(requestScreenConfigurationEntity,authDetailsVo);
					requestScreenConfigurationEntity.setDeleteFlag(CommonConstant.FLAG_ONE);

					requestScreenConfigurationRepository.save(requestScreenConfigurationEntity);
				}
			} catch (NoResultException e) {
				logger.error("error", e);
				throw new CommonException(getMessage("noResultFound",authDetailsVo));
			} catch (NonUniqueResultException e) {
				logger.error("error", e);
				throw new CommonException(getMessage("noRecordFound",authDetailsVo));
			} catch (Exception e) {
				logger.error("error", e);
				throw new CommonException(getMessage("dataFailure",authDetailsVo));
			}
		}
	}

	/**
	 * This method is used to delete the RequestScreenConfigurationDetailRecord
	 * one by one by executing the query
	 * 
	 * 
	 * @param RequestScreenConfigurationEntity
	 *            requestScreenConfigurationEntity
	 */
	@Transactional
	private void deleteId(RequestScreenConfigurationEntity requestScreenConfigurationEntity,AuthDetailsVo authDetailsVo) {

		try {
			List<RequestScreenDetailConfigurationEntity> requestScreenDetailConfigurationEntityList = requestScreenConfigurationEntity
					.getRequestScreenDetailConfigurationEntityList();

			if (null != requestScreenDetailConfigurationEntityList) {
				for (RequestScreenDetailConfigurationEntity requestScreenDetailConfigurationEntity : requestScreenDetailConfigurationEntityList) {
					requestScreenDetailConfigurationEntity.setDeleteFlag(CommonConstant.FLAG_ONE);
					requestScreenDetailConfigRepository.save(requestScreenDetailConfigurationEntity);
				}
			}
		} catch (NoResultException e) {
			logger.error("error", e);
			throw new CommonException(getMessage("noResultFound",authDetailsVo));
		} catch (NonUniqueResultException e) {
			logger.error("error", e);
			throw new CommonException(getMessage("noRecordFound",authDetailsVo));
		} catch (Exception e) {
			logger.error("error", e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

	}

	/**
	 * This method is used to find the corresponding id related details to
	 * modify or view it.
	 * 
	 * @param int
	 *            reqScreenConfig
	 * @return RequestScreenConfigurationVo requestScreenConfigurationVo
	 */
	@SuppressWarnings("rawtypes")
	@Transactional
	public RequestScreenConfigurationVO findScreenConfig(
			RequestScreenConfigurationVO requestScreenConfigurationViewVo,AuthDetailsVo authDetailsVo) {

		List<Object> result = null;
		try {
			result = requestScreenConfigurationDAO
					.findScreenConfig(requestScreenConfigurationViewVo.getRequestScreenConfigId(),authDetailsVo);
			RequestScreenConfigurationVO headerVo = new RequestScreenConfigurationVO();
			List<RequestScreenDetailConfigurationVO> listRequestScreenDetailConfigurationVo = new ArrayList<RequestScreenDetailConfigurationVO>();
			RequestScreenConfigurationVO requestScreenConfiguration = new RequestScreenConfigurationVO();
			try {
				BeanUtils.copyProperties(requestScreenConfigurationViewVo, requestScreenConfiguration);
			} catch (CommonException e) {
				logger.error("error", e);
				throw new CommonException(getMessage("beanUtilPropertiesFailure",authDetailsVo));
			}
			RequestScreenDetailConfigurationVO requestScreenDetailConfigurationVo = null;
			Iterator itr = result.iterator();
			while (itr.hasNext()) {
				Object[] obj = (Object[]) itr.next();

				requestScreenDetailConfigurationVo = new RequestScreenDetailConfigurationVO();

				if (null != obj[0]) {
					headerVo.setRequestScreenConfigId(Integer.parseInt(String.valueOf(obj[0])));
				}

				if (null != obj[1]) {
					headerVo.setRequestScreenConfigurationName(String.valueOf(obj[1]));
				}

				if (null != obj[2]) {
					headerVo.setRequestScreenConfigurationCode(String.valueOf(obj[2]));
				}

				if (null != obj[3]) {
					headerVo.setRequestScreenConfigurationIsActive((boolean) obj[3]);
				}

				if (null != obj[4]) {
					headerVo.setRequestTypeId((int) obj[4]);
				}

				if (null != obj[5]) {
					headerVo.setRequestSubtypeId((int) obj[5]);
				}

				if (null != obj[6]) {
					headerVo.setRequestTypeName((String) obj[6]);
				}

				if (null != obj[7]) {
					headerVo.setRequestSubTypeName((String) obj[7]);
				}

				if (null != obj[8]) {
					requestScreenDetailConfigurationVo.setRequestScreenDetailConfigId((int) obj[8]);
				}

				if (null != obj[9]) {
					requestScreenDetailConfigurationVo.setRequestScreenConfigId((int) obj[9]);
				}

				if (null != obj[10]) {
					requestScreenDetailConfigurationVo.setRequestScreenDetailConfigurationFieldName((String) obj[10]);
				}

				if (null != obj[11]) {
					requestScreenDetailConfigurationVo.setRequestScreenDetailConfigurationFieldType((String) obj[11]);
				}

				if (null != obj[12]) {
					requestScreenDetailConfigurationVo.setRequestScreenDetailConfigurationFieldValue((String) obj[12]);
				}

				if (null != obj[13]) {
					requestScreenDetailConfigurationVo
							.setRequestScreenDetailConfigurationValidationIsRequired((boolean) obj[13]);
				}

				if (null != obj[14]) {
					requestScreenDetailConfigurationVo.setRequestScreenDetailConfigurationSequance((int) obj[14]);
				}

				if (null != obj[15]) {
					requestScreenDetailConfigurationVo.setRequestScreenDetailConfigurationIsActive((boolean) obj[15]);
				}
				listRequestScreenDetailConfigurationVo.add(requestScreenDetailConfigurationVo);
			}
			requestScreenConfiguration.setRequestScreenConfigurationVo(headerVo);
			requestScreenConfiguration
					.setRequestScreenDetailConfigurationVoList(listRequestScreenDetailConfigurationVo);

			return requestScreenConfiguration;
		} catch (NoResultException e) {
			logger.error("error", e);
			throw new CommonException(getMessage("noResultFound",authDetailsVo));
		} catch (NonUniqueResultException e) {
			logger.error("error", e);
			throw new CommonException(getMessage("noRecordFound",authDetailsVo));
		} catch (Exception e) {
			logger.error("error", e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

	}

	/**
	 * This method is used to
	 *
	 * 
	 * @param int
	 *            reqScreenConfig
	 * @return RequestScreenConfigurationVo requestScreenConfigurationVo
	 */
	@Transactional
	private RequestScreenConfigurationVO fetchRequestScreenDetailConfigurationVo(Object obj,AuthDetailsVo authDetailsVo) {

		try {
			RequestScreenConfigurationEntity requestScreenConfigurationEntity = new RequestScreenConfigurationEntity();
			List<RequestScreenDetailConfigurationEntity> requestScreenDetailConfigurationEntityList = requestScreenConfigurationEntity
					.getRequestScreenDetailConfigurationEntityList();
			RequestScreenConfigurationVO requestScreenConfigurationVo = new RequestScreenConfigurationVO();

			List<RequestScreenDetailConfigurationVO> requestScreenDetailConfigurationVoList = new ArrayList<RequestScreenDetailConfigurationVO>();

			for (RequestScreenDetailConfigurationEntity requestScreenDetailConfigurationEntity : requestScreenDetailConfigurationEntityList) {
				if (requestScreenDetailConfigurationEntity.isRequestScreenDetailConfigurationIsActive() == false) {
					continue;
				}

				RequestScreenDetailConfigurationVO requestScreenDetailConfigurationVo = new RequestScreenDetailConfigurationVO();
				BeanUtils.copyProperties(requestScreenDetailConfigurationEntity, requestScreenDetailConfigurationVo);
				requestScreenDetailConfigurationVoList.add(requestScreenDetailConfigurationVo);
			}
			requestScreenConfigurationVo
					.setRequestScreenDetailConfigurationVoList(requestScreenDetailConfigurationVoList);

			return requestScreenConfigurationVo;

		} catch (Exception e) {
			logger.error("error", e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

	}

	/**
	 * This method is used to set Created User and Date Details.
	 * 
	 * 
	 * @param RequestScreenConfigurationEntity
	 *            screenConfigurationEntity
	 * 
	 * @return RequestScreenConfigurationEntity
	 */
	private RequestScreenConfigurationEntity setCreateUserDetails(
			RequestScreenConfigurationEntity screenConfigurationEntity,AuthDetailsVo authDetailsVo) {

		try {
			screenConfigurationEntity.setCreateBy(authDetailsVo.getUserId());
			screenConfigurationEntity.setCreateDate(CommonConstant.getCalenderDate());
			screenConfigurationEntity.setUpdateBy(authDetailsVo.getUserId());
			screenConfigurationEntity.setUpdateDate(CommonConstant.getCalenderDate());
			screenConfigurationEntity.setDeleteFlag(CommonConstant.FLAG_ZERO);
			return screenConfigurationEntity;

		} catch (Exception e) {
			logger.error("error", e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

	}

	/**
	 * This method is used to create the requestScreenDetailConfiguration user
	 * details.
	 * 
	 * 
	 * @param requestScreenDetailConfigurationEntity
	 *            requestScreenDetailConfigurationEntity
	 * @return requestScreenDetailConfigurationEntity
	 *         requestScreenDetailConfigurationEntity
	 */

	@Transactional
	private RequestScreenDetailConfigurationEntity setCreateRequestScreenDetailConfiguration(
			RequestScreenDetailConfigurationEntity requestScreenDetailConfigurationEntity,AuthDetailsVo authDetailsVo) {

		try {
			requestScreenDetailConfigurationEntity.setCreateBy(authDetailsVo.getUserId());
			requestScreenDetailConfigurationEntity.setCreateDate(CommonConstant.getCalenderDate());
			requestScreenDetailConfigurationEntity.setUpdateBy(authDetailsVo.getUserId());
			requestScreenDetailConfigurationEntity.setUpdateDate(CommonConstant.getCalenderDate());

			return requestScreenDetailConfigurationEntity;

		} catch (Exception e) {
			logger.error("error", e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

	}

	/**
	 * This method is used to set or change the RequestScreenDetailConfiguration
	 * user details which is already existed.
	 * 
	 * 
	 * @param requestScreenDetailConfigurationEntity
	 *            requestScreenDetailConfigurationEntity
	 * @return requestScreenDetailConfigurationEntity
	 *         requestScreenDetailConfigurationEntity
	 */

	@Transactional
	private RequestScreenDetailConfigurationEntity setUpdatedRequestScreenDetailConfiguration(
			RequestScreenDetailConfigurationEntity requestScreenDetailConfigurationEntity,AuthDetailsVo authDetailsVo) {

		try {
			requestScreenDetailConfigurationEntity.setUpdateBy(authDetailsVo.getUserId());
			requestScreenDetailConfigurationEntity.setUpdateDate(CommonConstant.getCalenderDate());

			return requestScreenDetailConfigurationEntity;
		} catch (Exception e) {
			logger.error("error", e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

	}

	/**
	 * This method is used to update the requestScreenConfiguration and its
	 * requestScreenConfigurationDetail. check requestScreenConfiguration code.
	 * 
	 * @param RequestScreenConfigurationVo
	 *            requestScreenConfigurationVo
	 * @return void
	 */

	@Transactional
	private RequestScreenConfigurationEntity setUpdatedRequestScreenConfiguration(
			RequestScreenConfigurationEntity requestScreenConfigurationEntity,AuthDetailsVo authDetailsVo) {

		try {
			requestScreenConfigurationEntity.setUpdateBy(authDetailsVo.getUserId());
			requestScreenConfigurationEntity.setUpdateDate(CommonConstant.getCalenderDate());

			return requestScreenConfigurationEntity;

		} catch (Exception e) {
			logger.error("error", e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

	}

	/**
	 * This method is used find the requestScreenConfigurationDetail Id.
	 * 
	 * 
	 * @param RequestScreenConfigurationVo
	 *            requestScreenConfigurationVo
	 * @return RequestScreenDetailConfigurationEntity
	 *         requestScreenDetailConfigurationEntity
	 */
	@Transactional
	public RequestScreenDetailConfigurationEntity findDetailId(int id,AuthDetailsVo authDetailsVo) {

		try {
			RequestScreenDetailConfigurationEntity requestScreenDetailConfigurationEntity = null;

			requestScreenDetailConfigurationEntity = (RequestScreenDetailConfigurationEntity) requestScreenDetailConfigRepository
					.findOne(id);

			return requestScreenDetailConfigurationEntity;

		} catch (NoResultException e) {
			logger.error("error", e);
			throw new CommonException(getMessage("noResultFound",authDetailsVo));
		} catch (NonUniqueResultException e) {
			logger.error("error", e);
			throw new CommonException(getMessage("noRecordFound",authDetailsVo));
		} catch (Exception e) {
			logger.error("error", e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

	}

	public int requestScreen(RequestScreenConfigurationVO requestScreenConfigurationVo,AuthDetailsVo authDetailsVo) {

		try {
			int count = requestScreenConfigurationDAO.requestscreen(requestScreenConfigurationVo,authDetailsVo);
			if (count > 0) {
				throw new CommonException(getMessage("duplicatemappingFound",authDetailsVo));
			}
		} catch (CommonException e) {
			logger.error("error", e);
			throw new CommonException(e.getMessage());
		} catch (Exception e) {
			logger.error("error", e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

		return 0;

	}
	
	public int checkDuplicateConfigName(RequestScreenConfigurationVO requestScreenConfigurationVo,AuthDetailsVo authDetailsVo) {

		try {
			int count = requestScreenConfigurationDAO.checkDuplicateConfigName(requestScreenConfigurationVo,authDetailsVo);
			if (count > 0) {
				throw new CommonException(getMessage("duplicateScreenConfigName",authDetailsVo));
			}
		} catch (CommonException e) {
			logger.error("error", e);
			throw new CommonException(e.getMessage());
		} catch (Exception e) {
			logger.error("error", e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

		return 0;

	}
	
	
	

}
