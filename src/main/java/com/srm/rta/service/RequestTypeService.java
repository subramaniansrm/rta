package com.srm.rta.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.jfree.util.Log;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.controller.CommonController;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.rta.dao.RequestTypeDAO;
import com.srm.rta.entity.RequestTypeEntity;
import com.srm.rta.repository.RequestTypeRepository;
import com.srm.rta.vo.RequestTypeVO;

import lombok.Data;

@Data
@Service
public class RequestTypeService extends CommonController<RequestTypeVO>{

	@Autowired
    RequestTypeRepository requestTypeRepo;


	@Autowired
	RequestTypeDAO requestTypeDAO;

	/**
	 * This Method is to get all the request type.
	 * 
	 * @return requestTypeActionVoList List<RequestTypeVo>
	 * @throws CommonException
	 *
	 */
	@Transactional()
	public List<RequestTypeVO> getAll(AuthDetailsVo authDetailsVo) throws CommonException {
		try {
			List<RequestTypeVO> requestTypeActionVoList = new ArrayList<RequestTypeVO>();
			List<RequestTypeEntity> listRequestTypeEntity = new ArrayList<RequestTypeEntity>();
			
			
			
			listRequestTypeEntity = requestTypeRepo.getAllRequestType(authDetailsVo.getEntityId());
			
			requestTypeActionVoList = getAllList(listRequestTypeEntity);
			
			return requestTypeActionVoList;
		} catch (Exception e) {
			Log.info("Request Type Service getAll Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
	}

	public List<RequestTypeVO> getAllList(List<RequestTypeEntity> listRequestTypeEntity) {

		List<RequestTypeVO> requestTypeActionVoList = new ArrayList<RequestTypeVO>();
		for (RequestTypeEntity requestTypeEntity : listRequestTypeEntity) {
			RequestTypeVO requestTypeActionVo = new RequestTypeVO();

			BeanUtils.copyProperties(requestTypeEntity, requestTypeActionVo);

			if (requestTypeEntity.isRequestTypeIsActive()) {
				requestTypeActionVo.setStatus(CommonConstant.Active);
			} else {
				requestTypeActionVo.setStatus(CommonConstant.InActive);
			}

			requestTypeActionVoList.add(requestTypeActionVo);
		}
		return requestTypeActionVoList;
	}

	@Transactional()
	public List<RequestTypeVO> getAllSearch(RequestTypeVO requestTypeActionVo,AuthDetailsVo authDetailsVo) throws CommonException {
		try {
			List<RequestTypeVO> requestTypeActionVoList = new ArrayList<RequestTypeVO>();

			RequestTypeVO requestTypeVo;

			List<RequestTypeEntity> requestType = requestTypeDAO.searchAll(requestTypeActionVo,authDetailsVo);

			for (RequestTypeEntity requestTypeEntity : requestType) {

				requestTypeVo = new RequestTypeVO();

				BeanUtils.copyProperties(requestTypeEntity, requestTypeVo);
				if (requestTypeVo.isRequestTypeIsActive()) {
					requestTypeVo.setStatus(CommonConstant.Active);
				} else {
					requestTypeVo.setStatus(CommonConstant.InActive);
				}

				requestTypeActionVoList.add(requestTypeVo);

			}

			return requestTypeActionVoList;
		} catch (Exception e) {
			Log.info("RequestTypeService getAllSearch Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
	}

	/**
	 * Method is to Create Request type.
	 * 
	 * @param requestTypeActionVo
	 *            RequestTypeVo
	 * @throws CommonException
	 * 
	 */
	@Transactional()
	public RequestTypeVO create(RequestTypeVO requestTypeActionVo,AuthDetailsVo authDetailsVo) throws CommonException {
		RequestTypeEntity requestTypeEntity = new RequestTypeEntity();
		String code = null;

		try {
			code = requestTypeDAO.findAutoGenericCode(CommonConstant.RequestType,authDetailsVo);

		} catch (Exception e) {
			Log.info("RequestTypeService create Exception",e);
			throw new CommonException(getMessage("autoCodeGenerationFailure",authDetailsVo));
		}

		try {

			BeanUtils.copyProperties(requestTypeActionVo, requestTypeEntity);
			requestTypeEntity.setRequestTypeCode(code);
			requestTypeEntity.setEntityLicenseId(authDetailsVo.getEntityId());
			requestTypeEntity = setCreateUserDetails(requestTypeEntity,authDetailsVo);
		} catch (Exception e) {
			Log.info("RequestTypeService create Exception",e);
			throw new CommonException(getMessage("beanUtilPropertiesFailure",authDetailsVo));
		}

		try {
			requestTypeRepo.save(requestTypeEntity);
		} catch (Exception e) {
			Log.info("RequestTypeService create Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
		if(null != requestTypeEntity && null != requestTypeEntity.getRequestTypeCode()){
			requestTypeActionVo.setRequestTypeCode(requestTypeEntity.getRequestTypeCode());
		}
		return requestTypeActionVo;
	}

	/**
	 * Method is to update Request type.
	 * 
	 * @param requestTypeActionVo
	 *            RequestTypeVo
	 * @throws CommonException
	 */
	@Transactional()
	public RequestTypeVO update(RequestTypeVO requestTypeActionVo,AuthDetailsVo authDetailsVo) throws CommonException {
		RequestTypeEntity requestTypeEntity = new RequestTypeEntity();
		boolean requestType = false;

		if (requestTypeActionVo.isRequestTypeIsActive() == false) {
			requestType = deleteRequestType(requestTypeActionVo.getRequestTypeId(),authDetailsVo);
		}
		if (requestType) {
			throw new CommonException(getMessage("cannotsetinactive",authDetailsVo));
		}

		try {

			requestTypeEntity = requestTypeRepo.findOne(requestTypeActionVo.getRequestTypeId());
		} catch (NoResultException e) {
			Log.info("RequestTypeService upate NoResultException",e);
			throw new CommonException(getMessage("noResultFound",authDetailsVo));
		} catch (NonUniqueResultException e) {
			Log.info("RequestTypeService upate NonUniqueResultException",e);
			throw new CommonException(getMessage("noRecordFound",authDetailsVo));
		} catch (Exception e) {
			Log.info("RequestTypeService upate Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
		try {
			BeanUtils.copyProperties(requestTypeActionVo, requestTypeEntity);

			requestTypeEntity = setUpdatedUserDetails(requestTypeEntity,authDetailsVo);
		} catch (Exception e) {
			Log.info("RequestTypeService upate Exception",e);
			throw new CommonException(getMessage("beanUtilPropertiesFailure",authDetailsVo));
		}
		try {
			requestTypeRepo.save(requestTypeEntity);
		} catch (Exception e) {
			Log.info("RequestTypeService upate Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
		if(null != requestTypeEntity && null != requestTypeEntity.getRequestTypeCode()){
			requestTypeActionVo.setRequestTypeCode(requestTypeEntity.getRequestTypeCode());
		}
		return requestTypeActionVo;

	}

	/**
	 * Method is used to Update the user details
	 * 
	 * @param requestTypeEntity
	 * @return
	 */
	private RequestTypeEntity setUpdatedUserDetails(RequestTypeEntity requestTypeEntity,AuthDetailsVo authDetailsVo) {

		requestTypeEntity.setUpdateBy(authDetailsVo.getUserId());
		requestTypeEntity.setUpdateDate(CommonConstant.getCalenderDate());

		return requestTypeEntity;

	}

	@Transactional()
	public boolean deleteRequestType(int requestTypeId,AuthDetailsVo authDetailsVo) throws CommonException {
		boolean status = false;
		
		if (status == false) {
			status = deleteScreenConfig(requestTypeId,authDetailsVo);

		}
		if (status == false) {
			status = deleteWorkflow(requestTypeId,authDetailsVo);
		}
		if (status == false) {
			status = deleteRequest(requestTypeId,authDetailsVo);
		}
		if (status == false) {
			status = deleteRequestSubType(requestTypeId,authDetailsVo);
		}
		 
		return status;
	}

	@Transactional()
	private boolean deleteScreenConfig(int requestTypeId,AuthDetailsVo authDetailsVo) throws CommonException {
		int count = 0;

		try {
			count = (int) (long) requestTypeDAO.findScreenConfig(requestTypeId,authDetailsVo);
			if (count > 0) {
				return true;
			}
		} catch (Exception exe) {

			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
		return false;

	}

	/**
	 * This method is used to find whether the particular RequestType id is
	 * available in RequestWorkFlow or not.
	 * 
	 * 
	 * @param requestTypeId
	 * 
	 * @return void
	 * 
	 */
	@Transactional()
	private boolean deleteWorkflow(int requestTypeId,AuthDetailsVo authDetailsVo) throws CommonException {
		int count = 0;

		try {
			count = (int) (long) requestTypeDAO.findDeleteWorkflow(requestTypeId,authDetailsVo);
			if (count > 0) {
				return true;
			}
		} catch (Exception exe) {

			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
		return false;

	}

	/**
	 * This method is used to find whether the particular RequestType id is
	 * available in Request or not.
	 * 
	 * 
	 * @param requestTypeId
	 * 
	 * @return void
	 * 
	 */
	@Transactional()
	private boolean deleteRequest(int requestTypeId,AuthDetailsVo authDetailsVo) throws CommonException {
		int count = 0;

		try {
			count = (int) (long) requestTypeDAO.findDeleteRequest(requestTypeId,authDetailsVo);
			if (count > 0) {
				return true;
			}
		} catch (Exception exe) {

			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
		return false;

	}

	/**
	 * This method is used to find whether the particular RequestType id is
	 * available in RequestSubType or not.
	 * 
	 * 
	 * @param requestTypeId
	 * 
	 * @return void
	 * 
	 */
	@Transactional()
	private boolean deleteRequestSubType(int requestTypeId,AuthDetailsVo authDetailsVo) throws CommonException {
		int count = 0;

		try {
			count = (int) (long) requestTypeDAO.findRequestSubType(requestTypeId,authDetailsVo);
			if (count > 0) {
				return true;
			}
		} catch (Exception exe) {
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
		return false;

	}

	@Transactional()
	public void delete(RequestTypeVO requestTypeActionVo,AuthDetailsVo authDetailsVo) throws CommonException {

		boolean requestType = false;

		List<String> codeList = new ArrayList<String>();

		try {
			for (Integer id : requestTypeActionVo.getRequestTypeList()) {
				boolean Type = false;

				RequestTypeEntity requestTypeEntity = new RequestTypeEntity();
				Type = deleteRequestType(id,authDetailsVo);
				requestTypeEntity = requestTypeRepo.findOne(id);
				if (Type) {
					requestType = true;
					codeList.add(requestTypeEntity.getRequestTypeCode());
					continue;
				}

				requestTypeEntity.setDeleteFlag(CommonConstant.FLAG_ONE);

				requestTypeRepo.save(requestTypeEntity);

			}
		} catch (NoResultException e) {
			Log.info("RequestTypeService delete NoResultException",e);
			throw new CommonException(getMessage("noResultFound",authDetailsVo));
		} catch (NonUniqueResultException e) {
			Log.info("RequestTypeService delete NonUniqueResultException",e);
			throw new CommonException(getMessage("noRecordFound",authDetailsVo));
		} catch (Exception e) {
			Log.info("RequestTypeService delete Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
		if (requestType) {
			throw new CommonException(
					CommonConstant.format(getMessage("unUsedRecordOnlyBeDeleted",authDetailsVo), codeList));

		}
	}

	/**
	 * Method is to view all the request type.
	 * 
	 * @param requestTypeActionVo
	 *            RequestTypeVo
	 * @return requestTypeActionVo
	 * @throws CommonException
	 */
	@Transactional()
	public RequestTypeVO view(RequestTypeVO requestTypeActionVo,AuthDetailsVo authDetailsVo) throws CommonException {
		RequestTypeEntity requestTypeEntity = new RequestTypeEntity();
		try {

			requestTypeEntity = requestTypeRepo.findOne(requestTypeActionVo.getRequestTypeId());
		} catch (NoResultException e) {
			Log.info("RequestTypeService view NoResultException",e);
			throw new CommonException(getMessage("noResultFound",authDetailsVo));
		} catch (NonUniqueResultException e) {
			Log.info("RequestTypeService view NonUniqueResultException",e);
			throw new CommonException(getMessage("noRecordFound",authDetailsVo));
		} catch (Exception e) {
			Log.info("RequestTypeService view Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
		try {
			BeanUtils.copyProperties(requestTypeEntity, requestTypeActionVo);
		} catch (Exception e) {
			Log.info("RequestTypeService view Exception",e);
			throw new CommonException(getMessage("beanUtilPropertiesFailure",authDetailsVo));
		}
		return requestTypeActionVo;

	}

	@Transactional()
	public void findDuplicate(RequestTypeVO requestTypeActionVo,AuthDetailsVo authDetailsVo) throws CommonException {
		try {
			int count = requestTypeDAO.findDuplicate(requestTypeActionVo,authDetailsVo);
			if (count > 0) {
				throw new CommonException(getMessage("requestType_name_dup",authDetailsVo));
			}
		} catch (CommonException e) {
			Log.info("RequestTypeService findDuplicate Common Exception",e);
			throw new CommonException(e.getMessage());
		} catch (Exception e) {
			Log.info("RequestTypeService findDuplicate Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));

		}
	}

	/**
	 * Method is used to create the user Details
	 * 
	 * @param requestTypeEntity
	 * @return
	 */
	private RequestTypeEntity setCreateUserDetails(RequestTypeEntity requestTypeEntity,AuthDetailsVo authDetailsVo) {

		requestTypeEntity.setCreateBy(authDetailsVo.getUserId());
		requestTypeEntity.setCreateDate(CommonConstant.getCalenderDate());
		requestTypeEntity.setUpdateBy(authDetailsVo.getUserId());
		requestTypeEntity.setUpdateDate(CommonConstant.getCalenderDate());
		requestTypeEntity.setDeleteFlag(CommonConstant.FLAG_ZERO);
		return requestTypeEntity;
	}


}
