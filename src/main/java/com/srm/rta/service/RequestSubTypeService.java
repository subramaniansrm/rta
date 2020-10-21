package com.srm.rta.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.controller.CommonController;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.rta.dao.RequestSubTypeDAO;
import com.srm.rta.entity.RequestSubTypeEntity;
import com.srm.rta.repository.RequestSubTypeRepository;
import com.srm.rta.vo.RequestSubTypeVO;

import lombok.Data;
@Data
@Service
public class RequestSubTypeService extends CommonController<RequestSubTypeVO> {

	@Autowired
	 RequestSubTypeRepository requestSubTypeRepository;

	@Autowired
	 RequestSubTypeDAO requestSubTypeDao;
	
	

	/**
	 * This Method is to get all the details.
	 * 
	 * @return requestSubTypeMasterVoList List<RequestSubTypeVo>
	 */
	@Transactional
	public List<RequestSubTypeVO> getAll(AuthDetailsVo authDetailsVo) {
		try {
			Integer entityId = authDetailsVo.getEntityId();
			List<RequestSubTypeVO> requestSubTypeMasterVoList = new ArrayList<RequestSubTypeVO>();
			List<Object[]> requestSubTypeEntityList = null;
			requestSubTypeEntityList = requestSubTypeRepository.getAll(entityId);
			requestSubTypeMasterVoList = getAllList(requestSubTypeEntityList);

			return requestSubTypeMasterVoList;
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("RequestSubType Service getAll Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
	}

	/**
	 * This Method is to get all the list.
	 * 
	 * @param requestSubTypeEntityList
	 *            List<Object[]>
	 * @return requestSubTypeMasterVoList List<RequestSubTypeVo>
	 */
	@Transactional
	private List<RequestSubTypeVO> getAllList(List<Object[]> requestSubTypeEntityList) {

		List<RequestSubTypeVO> requestSubTypeMasterVoList = new ArrayList<RequestSubTypeVO>();
		RequestSubTypeVO requestSubTypeMasterVo = null;
		for (Object[] requestSubTypeEntity : requestSubTypeEntityList) {

			requestSubTypeMasterVo = new RequestSubTypeVO();

			if (null != (String) requestSubTypeEntity[0]) {
				requestSubTypeMasterVo.setRequestSubTypeCode((String) requestSubTypeEntity[0]);
			}

			if (null != (String) requestSubTypeEntity[1]) {
				requestSubTypeMasterVo.setRequestSubTypeName((String) requestSubTypeEntity[1]);
			}
			if (null != (Integer) requestSubTypeEntity[2]) {
				requestSubTypeMasterVo.setRequestSubTypeIsActive((Integer) requestSubTypeEntity[2]);
			}
		
			if (null != (String) requestSubTypeEntity[3]) {
				requestSubTypeMasterVo.setRequestTypeName((String) requestSubTypeEntity[3]);
			}

			if (null != (Integer) requestSubTypeEntity[4] && null != (Integer) requestSubTypeEntity[4]) {
				requestSubTypeMasterVo.setRequestSubTypeId((Integer) requestSubTypeEntity[4]);
			}

			if (0 != (int) requestSubTypeEntity[5] && null != (Integer) requestSubTypeEntity[5]) {
				requestSubTypeMasterVo.setRequestTypeId((Integer) requestSubTypeEntity[5]);
			}

			if (0 != (int) requestSubTypeEntity[6] || null != (Integer) requestSubTypeEntity[6]) {
				requestSubTypeMasterVo.setRequestSubtypePriorty((Integer) requestSubTypeEntity[6]);
			}

			if (null != requestSubTypeMasterVo.getRequestSubTypeIsActive() && requestSubTypeMasterVo.getRequestSubTypeIsActive() != 0) {
				requestSubTypeMasterVo.setStatus(CommonConstant.Active);
			} else {
				requestSubTypeMasterVo.setStatus(CommonConstant.InActive);
			}
			requestSubTypeMasterVoList.add(requestSubTypeMasterVo);
		}
		return requestSubTypeMasterVoList;
	}

	/**
	 * This Method is to create Request Sub type
	 * 
	 * @param requestSubTypeMasterVo
	 *            RequestSubTypeVo
	 */
	@Transactional
	public RequestSubTypeVO create(RequestSubTypeVO requestSubTypeMasterVo,AuthDetailsVo authDetailsVo) throws CommonException {
		String code = null;
		boolean requestSubType = false;
		if (null != requestSubTypeMasterVo.getRequestSubTypeIsActive()
				&& requestSubTypeMasterVo.getRequestSubTypeIsActive() == 0) {

			requestSubType = deleteRequestSubtype(requestSubTypeMasterVo.getRequestSubTypeId(),authDetailsVo);
		}
		if (requestSubType) {
			throw new CommonException(getMessage("cannotsetinactive",authDetailsVo));
		}
	 
		try {
			RequestSubTypeEntity requestSubTypeEntity = null;
			if (requestSubTypeMasterVo.getRequestSubTypeId() != null) {
				requestSubTypeEntity = findId(requestSubTypeMasterVo.getRequestSubTypeId(),authDetailsVo);
				requestSubTypeEntity.setRequestSubTypeCode(requestSubTypeMasterVo.getRequestSubTypeCode());
				requestSubTypeEntity.setRequestSubTypeName(requestSubTypeMasterVo.getRequestSubTypeName());
				if (null != requestSubTypeMasterVo.getRequestSubTypeIsActive()) {
					requestSubTypeEntity.setRequestSubTypeIsActive(requestSubTypeMasterVo.getRequestSubTypeIsActive());
				}
				requestSubTypeEntity.setRequestTypeId(requestSubTypeMasterVo.getRequestTypeId());
				requestSubTypeEntity.setRequestSubtypePriorty(requestSubTypeMasterVo.getRequestSubtypePriorty());
				requestSubTypeEntity.setRequestSubTypeIsActive(requestSubTypeMasterVo.getRequestSubTypeIsActive());
				requestSubTypeEntity = setUpdatedUserDetails(requestSubTypeEntity,authDetailsVo);

				requestSubTypeEntity.setEntityLicenseId(authDetailsVo.getEntityId());
			} else {

				requestSubTypeEntity = new RequestSubTypeEntity();
				code = requestSubTypeDao.findAutoGenericCode(CommonConstant.RequestSubType,authDetailsVo);
				requestSubTypeEntity.setRequestSubTypeCode(code);
				requestSubTypeEntity.setRequestSubTypeName(requestSubTypeMasterVo.getRequestSubTypeName());
				if (null != requestSubTypeMasterVo.getRequestSubTypeIsActive()) {
					requestSubTypeEntity.setRequestSubTypeIsActive(requestSubTypeMasterVo.getRequestSubTypeIsActive());
				}
				requestSubTypeEntity.setRequestSubTypeIsActive(requestSubTypeMasterVo.getRequestSubTypeIsActive());
				requestSubTypeEntity.setRequestTypeId(requestSubTypeMasterVo.getRequestTypeId());
				requestSubTypeEntity.setRequestSubtypePriorty(requestSubTypeMasterVo.getRequestSubtypePriorty());
				requestSubTypeEntity = setCreateUserDetails(requestSubTypeEntity,authDetailsVo);
				requestSubTypeEntity.setEntityLicenseId(authDetailsVo.getEntityId());
			}
			if(null != requestSubTypeEntity && null != requestSubTypeEntity.getRequestSubTypeCode()){
				requestSubTypeMasterVo.setRequestSubTypeCode(requestSubTypeEntity.getRequestSubTypeCode());
			}
			requestSubTypeRepository.save(requestSubTypeEntity);
		} catch (Exception e) {
			Log.info("RequestSubType Service create Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
		
		return requestSubTypeMasterVo;

	}

	/**
	 * This method is used to set ,created user and date in requestSubType.
	 * 
	 * @param requestSubTypeEntity
	 *            RequestWorkFlowAuditEntity
	 * @return requestSubTypeEntity RequestSubTypeEntity
	 */
	private RequestSubTypeEntity setUpdatedUserDetails(RequestSubTypeEntity requestSubTypeEntity,AuthDetailsVo authDetailsVo) {

		requestSubTypeEntity.setCreateBy(authDetailsVo.getUserId());
		requestSubTypeEntity.setCreateDate(CommonConstant.getCalenderDate());
		requestSubTypeEntity.setUpdateBy(authDetailsVo.getUserId());
		requestSubTypeEntity.setUpdateDate(CommonConstant.getCalenderDate());

		return requestSubTypeEntity;

	}

	/**
	 * This method is used to set ,created user and date in requestSubType.
	 * 
	 * @param requestSubTypeEntity
	 *            RequestWorkFlowAuditEntity
	 * @return requestSubTypeEntity RequestSubTypeEntity
	 */
	/* @SuppressWarnings("unused",authDetailsVo) */
	private RequestSubTypeEntity setCreateUserDetails(RequestSubTypeEntity requestSubTypeEntity,AuthDetailsVo authDetailsVo) {

		requestSubTypeEntity.setCreateBy(authDetailsVo.getUserId());
		requestSubTypeEntity.setCreateDate(CommonConstant.getCalenderDate());
		requestSubTypeEntity.setUpdateBy(authDetailsVo.getUserId());
		requestSubTypeEntity.setUpdateDate(CommonConstant.getCalenderDate());
		requestSubTypeEntity.setDeleteFlag(CommonConstant.FLAG_ZERO);
		return requestSubTypeEntity;
	}

	/**
	 * This Method is to find id.
	 * 
	 * @param id
	 * @return requestSubTypeEntity RequestSubTypeEntity
	 */
	private RequestSubTypeEntity findId(int id,AuthDetailsVo authDetailsVo) {
		try {
			RequestSubTypeEntity requestSubTypeEntity = null;
			requestSubTypeEntity = (RequestSubTypeEntity) requestSubTypeRepository.findOne(id);
			return requestSubTypeEntity;

		} catch (NoResultException e) {
			Log.info("RequestSubType Service findId NoResultException",e);
			throw new CommonException(getMessage("noResultFound",authDetailsVo));
		} catch (NonUniqueResultException e) {
			Log.info("RequestSubType Service findId NonUniqueResultException",e);
			throw new CommonException(getMessage("noRecordFound",authDetailsVo));
		} catch (Exception e) {
			Log.info("RequestSubType Service findId Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
	}

	/**
	 * This Method is to delete the Request sub type.
	 * 
	 * @param requestSubTypeMasterVo
	 *            RequestSubTypeVo
	 */
	@Transactional
	public void delete(RequestSubTypeVO requestSubTypeMasterVo,AuthDetailsVo authDetailsVo) {

		boolean requestSubType = false;
		List<String> codeList = new ArrayList<String>();

		try {
			for (int id : requestSubTypeMasterVo.getRequestSubTypeList()) {
				boolean SubType = false;

				SubType = deleteRequestSubtype(id,authDetailsVo);

				RequestSubTypeEntity requestSubTypeEntity = requestSubTypeRepository.findOne(id);

				if (SubType) {
					requestSubType = true;
					codeList.add(requestSubTypeEntity.getRequestSubTypeCode());
					continue;
				}

				requestSubTypeEntity.setDeleteFlag(CommonConstant.FLAG_ONE);
				requestSubTypeRepository.save(requestSubTypeEntity);
			}
		} catch (NoResultException e) {
			Log.info("RequestSubType Service delete NoResultException",e);
			throw new CommonException(getMessage("noResultFound",authDetailsVo));
		} catch (NonUniqueResultException e) {
			Log.info("RequestSubType Service delete NonUniqueResultException",e);
			throw new CommonException(getMessage("noRecordFound",authDetailsVo));
		} catch (Exception e) {
			Log.info("RequestSubType Service delete Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
		if (requestSubType) {
			throw new CommonException(CommonConstant.format(getMessage("unUsedRecordOnlyBeDeleted",authDetailsVo), codeList));

		}
	}

	/**
	 * This Method is to view the request sub type.
	 * 
	 * @param requestSubTypeMasterVo
	 *            RequestSubTypeVo
	 * @return requestSubTypeMasterVo RequestSubTypeVo
	 */
	@Transactional
	public RequestSubTypeVO view(RequestSubTypeVO requestSubTypeMasterVo,AuthDetailsVo authDetailsVo) {
		try {
			RequestSubTypeVO requestSubTypeVo = new RequestSubTypeVO();
			// Integer entityId = authDetailsVo.getEntityId();
			// Integer Id = requestSubTypeVo.getRequestSubTypeId() ;

			Object[] requestSubTypeEntity = null;
			requestSubTypeEntity = requestSubTypeDao.findSubType(requestSubTypeMasterVo.getRequestSubTypeId(),authDetailsVo);

			BeanUtils.copyProperties(requestSubTypeMasterVo, requestSubTypeVo);

			BeanUtils.copyProperties(requestSubTypeEntity[0], requestSubTypeVo);

			requestSubTypeVo.setRequestTypeName((String) requestSubTypeEntity[1]);
			return requestSubTypeVo;
		} catch (NoResultException e) {
			Log.info("RequestSubType Service view NoResultException",e);
			throw new CommonException(getMessage("noResultFound",authDetailsVo));
		} catch (NonUniqueResultException e) {
			Log.info("RequestSubType Service view NonUniqueResultException",e);
			throw new CommonException(getMessage("noRecordFound",authDetailsVo));
		} catch (Exception e) {
			Log.info("RequestSubType Service view Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

	}

	/**
	 * This Method is to search the request sub type.
	 * 
	 * @param requestSubTypeMasterVo
	 *            RequestSubTypeVo
	 * @return requestSubTypeMasterVoList List<RequestSubTypeVo>
	 */
	@Transactional
	public List<RequestSubTypeVO> searchAll(RequestSubTypeVO requestSubTypeMasterVo,AuthDetailsVo authDetailsVo) {
		try {
			List<RequestSubTypeVO> requestSubTypeMasterVoList = new ArrayList<RequestSubTypeVO>();
			List<Object[]> requestSubTypeEntityList = null;
			requestSubTypeEntityList = requestSubTypeDao.searchAll(requestSubTypeMasterVo,authDetailsVo);
			requestSubTypeMasterVoList = getAllList(requestSubTypeEntityList);
			return requestSubTypeMasterVoList;
		} catch (NoResultException e) {
			Log.info("RequestSubType Service searchAll NoResultException",e);
			throw new CommonException(getMessage("noResultFound",authDetailsVo));
		} catch (NonUniqueResultException e) {
			Log.info("RequestSubType Service searchAll NonUniqueResultException",e);
			throw new CommonException(getMessage("noRecordFound",authDetailsVo));
		} catch (Exception e) {
			Log.info("RequestSubType Service searchAll Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
	}

	/**
	 * This Method is to get the code.
	 * 
	 * @param requestSubTypeVo
	 *            RequestSubTypeVo
	 * @return requestSubTypeVo Boolean
	 */
	@Transactional
	public Boolean getCode(RequestSubTypeVO requestSubTypeVo,AuthDetailsVo authDetailsVo) {
		try {
			return requestSubTypeDao.getCode(requestSubTypeVo,authDetailsVo);
		} catch (Exception e) {
			Log.info("RequestSubType Service getCode Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
	}

	@Transactional
	public boolean deleteRequestSubtype(int requestsubTypeId,AuthDetailsVo authDetailsVo) throws CommonException {
		boolean status = false;
		if (status == false) {
			status = findRequest(requestsubTypeId,authDetailsVo);
		}
		if (status == false) {
			status = findScreenConfig(requestsubTypeId,authDetailsVo);
		}
		if (status == false) {
			status = findWorkflow(requestsubTypeId,authDetailsVo);
		}
		return status;
	}

	/**
	 * This method is used to find whether the requestSubtypeId and
	 * requestTypeId is already available or not in the RequestTable. If
	 * available user has to type another TypeId.
	 * 
	 * @param int
	 *            subTypeId,int typeId
	 */
	@Transactional
	public boolean findRequest(int subTypeId,AuthDetailsVo authDetailsVo) throws CommonException {
		int count = 0;

		try {
			count = (int) (long) requestSubTypeDao.findRequest(subTypeId,authDetailsVo);
			if (count > 0) {
				return true;
			}
		} catch (CommonException exe) {

			throw new CommonException(exe.getMessage());
		} catch (Exception exe) {

			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
		return false;

	}

	/**
	 * This method is used to find whether the requestSubtypeId and
	 * requestTypeId is already available or not in the WorkFlowTable. If
	 * available user has to type another TypeId.
	 * 
	 * @param int
	 *            subTypeId,int typeId
	 */
	@Transactional
	public boolean findWorkflow(int subTypeId,AuthDetailsVo authDetailsVo) throws CommonException {
		int count = 0;

		try {

			count = (int) (long) requestSubTypeDao.findWorkflow(subTypeId,authDetailsVo);
			if (count > 0) {
				return true;
			}
		} catch (CommonException exe) {

			throw new CommonException(exe.getMessage());
		} catch (Exception exe) {

			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

		return false;

	}

	/**
	 * This method is used to find whether the requestSubtypeId and
	 * requestTypeId is already available or not in the
	 * RequestScreenConfigTable. If available user has to type another TypeId.
	 * 
	 * @param int
	 *            subTypeId,int typeId
	 */
	@Transactional
	public boolean findScreenConfig(int subTypeId,AuthDetailsVo authDetailsVo) throws CommonException {
		int count = 0;

		try {
			count = (int) (long) requestSubTypeDao.findScreenConfig(subTypeId,authDetailsVo);
			if (count > 0) {
				return true;

			}
		} catch (CommonException exe) {

			throw new CommonException(exe.getMessage());
		} catch (Exception exe) {

			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

		return false;

	}

	public void findDuplicate(RequestSubTypeVO requestSubTypeMasterVo,AuthDetailsVo authDetailsVo) {
		try {
			int count = requestSubTypeDao.findDuplicate(requestSubTypeMasterVo,authDetailsVo);
			if (count > 0) {
				throw new CommonException(getMessage("requestSubType_name_dup",authDetailsVo));
			}
		} catch (CommonException e) {
			Log.info("RequestSubType Service findDuplicate Common Exception",e);
			throw new CommonException(e.getMessage());
		} catch (Exception e) {
			Log.info("RequestSubType Service findDuplicate Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));

		}
	}
	
}
