package com.srm.rta.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.controller.CommonController;
import com.srm.coreframework.exception.CoreException;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.rta.dao.FlashNewsDAO;
import com.srm.rta.entity.RinFlashNewsEntity;
import com.srm.rta.repository.FlashNewsRepository;
import com.srm.rta.vo.FlashNewsVO;

/**
 * This Service is used to Create , Update , Delete , Get all , Get all search ,
 * Load the Flash news details.
 * 
 *
 */
@Service
public class FlashNewsService extends CommonController<FlashNewsVO> {

	Logger logger = LoggerFactory.getLogger(FlashNewsService.class);

	@Autowired
	FlashNewsRepository flashNewsRepository;

	@Autowired
	FlashNewsDAO flashNewsDAO;

	/**
	 * Method is used to get all the flash news details
	 * 
	 * @return flashNewsVoList List<FlashNewsVo>
	 * @throws CoreException
	 */
	@Transactional()
	public List<FlashNewsVO> getAll(AuthDetailsVo authDetailsVo) {

		List<FlashNewsVO> flashNewsVoList = new ArrayList<>();

		List<RinFlashNewsEntity> flashNewsEntityList = new ArrayList<>();

		// Get all the details of flash news in DB
		Integer entityId = authDetailsVo.getEntityId();
		try {

			flashNewsEntityList = flashNewsRepository.getAll(entityId);

		} catch (Exception e) {
			logger.error("error", e);
		}

		// Set the Flash news details in VO

		if (flashNewsEntityList != null && !flashNewsEntityList.isEmpty()) {

			flashNewsVoList = getAllList(flashNewsEntityList);
		}

		return flashNewsVoList;
	}

	/**
	 * Method is used to get all List the flash news details
	 * 
	 * @param flashNewsEntityList
	 * @return flashNewsVoList
	 */
	private List<FlashNewsVO> getAllList(List<RinFlashNewsEntity> flashNewsEntityList) {

		List<FlashNewsVO> flashNewsVoList = new ArrayList<>();
		for (RinFlashNewsEntity finFlashNewsEntity : flashNewsEntityList) {

			FlashNewsVO flashNewsVo = new FlashNewsVO();
			flashNewsVo.setId(finFlashNewsEntity.getId());

			if (null != finFlashNewsEntity.getFlashNewsCode()) {
				flashNewsVo.setFlashNewsCode(finFlashNewsEntity.getFlashNewsCode());
			}

			if (null != finFlashNewsEntity.getFlashNewsType()) {
				flashNewsVo.setFlashNewsType(finFlashNewsEntity.getFlashNewsType());
			}

			if (null != finFlashNewsEntity.getFlashNewsDescription()) {
				flashNewsVo.setFlashNewsDescription(finFlashNewsEntity.getFlashNewsDescription());
			}
			if (null != finFlashNewsEntity.getFlashNewsDate()) {
				flashNewsVo.setFlashNewsDate(finFlashNewsEntity.getFlashNewsDate());
			}
			if (null != finFlashNewsEntity.getFlashNewsValidFrom()) {
				flashNewsVo.setFlashNewsValidFrom(finFlashNewsEntity.getFlashNewsValidFrom());
			}
			if (null != finFlashNewsEntity.getFlashNewsValidTo()) {
				flashNewsVo.setFlashNewsValidTo(finFlashNewsEntity.getFlashNewsValidTo());
			}
			flashNewsVo.setIsFlashNewsActive(finFlashNewsEntity.getIsFlashNewsActive());

			if (finFlashNewsEntity.getIsFlashNewsActive()) {
				flashNewsVo.setStatus(CommonConstant.Active);
			} else {
				flashNewsVo.setStatus(CommonConstant.InActive);
			}

			flashNewsVoList.add(flashNewsVo);
		}
		return flashNewsVoList;
	}

	/**
	 * This Method is used to create the flash news details
	 * 
	 * @param flashNewsVo
	 *            FlashNewsVo
	 * @throws CoreException
	 */
	@Transactional()
	public void create(FlashNewsVO flashNewsVo,AuthDetailsVo authDetailsVo) {

		RinFlashNewsEntity rinFlashNewsEntity = new RinFlashNewsEntity();

		// Generate the Auto Generic Code
		try {

			String code = null;

			// Generate the Auto Generic Code
			try {

				code = flashNewsDAO.findAutoGenericCode(CommonConstant.FlashNews,authDetailsVo);

			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new CommonException(getMessage("autoCodeGenerationFailure",authDetailsVo));
			}
			if (code != null) {
				rinFlashNewsEntity.setFlashNewsCode(code);
			}
			rinFlashNewsEntity.setFlashNewsType(flashNewsVo.getFlashNewsType());
			rinFlashNewsEntity.setFlashNewsDescription(flashNewsVo.getFlashNewsDescription());
			rinFlashNewsEntity.setFlashNewsDate(flashNewsVo.getFlashNewsDate());
			rinFlashNewsEntity.setFlashNewsValidFrom(flashNewsVo.getFlashNewsValidFrom());
			rinFlashNewsEntity.setFlashNewsValidTo(flashNewsVo.getFlashNewsValidTo());
			rinFlashNewsEntity.setIsFlashNewsActive(flashNewsVo.getIsFlashNewsActive());
			rinFlashNewsEntity.setCreateBy(authDetailsVo.getUserId());
			rinFlashNewsEntity.setCreateDate(CommonConstant.getCalenderDate());
			rinFlashNewsEntity.setUpdateBy(authDetailsVo.getUserId());
			rinFlashNewsEntity.setUpdateDate(CommonConstant.getCalenderDate());
			rinFlashNewsEntity.setEntityLicenseId(authDetailsVo.getEntityId());
			rinFlashNewsEntity.setDeleteFlag(CommonConstant.FLAG_ZERO);
			flashNewsRepository.save(rinFlashNewsEntity);

		} catch (Exception e) {
			logger.error("error", e);
			throw new CommonException(getMessage("autoCodeGenerationFailure",authDetailsVo));
		}

	}

	/**
	 * This method is used to update the flash news details
	 * 
	 * @param flashNewsVo
	 * @throws CoreException
	 */
	@Transactional()
	public void update(FlashNewsVO flashNewsVo,AuthDetailsVo authDetailsVo) {

		RinFlashNewsEntity rinFlashNewsEntity = new RinFlashNewsEntity();

		// Get the flash news details using ID
		try {

			rinFlashNewsEntity = flashNewsRepository.findOne(flashNewsVo.getId());
			if (rinFlashNewsEntity != null) {
				rinFlashNewsEntity.setFlashNewsCode(flashNewsVo.getFlashNewsCode());
				rinFlashNewsEntity.setFlashNewsType(flashNewsVo.getFlashNewsType());
				rinFlashNewsEntity.setFlashNewsDescription(flashNewsVo.getFlashNewsDescription());
				rinFlashNewsEntity.setFlashNewsDate(flashNewsVo.getFlashNewsDate());
				rinFlashNewsEntity.setFlashNewsValidFrom(flashNewsVo.getFlashNewsValidFrom());
				rinFlashNewsEntity.setFlashNewsValidTo(flashNewsVo.getFlashNewsValidTo());
				rinFlashNewsEntity.setIsFlashNewsActive(flashNewsVo.getIsFlashNewsActive());
				rinFlashNewsEntity.setUpdateBy(authDetailsVo.getUserId());
				rinFlashNewsEntity.setUpdateDate(CommonConstant.getCalenderDate());
				rinFlashNewsEntity.setEntityLicenseId(authDetailsVo.getEntityId());
				flashNewsRepository.save(rinFlashNewsEntity);
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
	 * Method is used to delete the flash news details
	 * 
	 * @param flashNewsVo
	 *            FlashNewsVo
	 * @throws CoreException
	 */
	@Transactional()
	public void delete(FlashNewsVO flashNewsVo,AuthDetailsVo authDetailsVo) {

		for (Integer id : flashNewsVo.getIdList()) {

			RinFlashNewsEntity rinFlashNewsEntity = new RinFlashNewsEntity();

			// Get the flash news details from DB using Flash news Id
			try {

				rinFlashNewsEntity = flashNewsRepository.findOne(id);
				if (rinFlashNewsEntity != null) {
					rinFlashNewsEntity.setDeleteFlag(CommonConstant.FLAG_ONE);
					rinFlashNewsEntity.setUpdateBy(authDetailsVo.getUserId());
					rinFlashNewsEntity.setUpdateDate(CommonConstant.getCalenderDate());

					flashNewsRepository.save(rinFlashNewsEntity);

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
	 * Method is used to load the flash news details
	 * 
	 * @param flashNewsVo
	 * @return
	 * @throws CoreException
	 */
	@Transactional()
	public FlashNewsVO load(FlashNewsVO flashNewsVo,AuthDetailsVo authDetailsVo) {

		RinFlashNewsEntity rinFlashNewsEntity = new RinFlashNewsEntity();
		FlashNewsVO flashNewsViewVo = new FlashNewsVO();

		// Get the details from DB using Flash news Id
		try {
			rinFlashNewsEntity = flashNewsRepository.findOne(flashNewsVo.getId());
			if (rinFlashNewsEntity != null) {
				flashNewsViewVo.setId(rinFlashNewsEntity.getId());
				flashNewsViewVo.setFlashNewsCode(rinFlashNewsEntity.getFlashNewsCode());
				flashNewsViewVo.setFlashNewsType(rinFlashNewsEntity.getFlashNewsType());
				flashNewsViewVo.setFlashNewsDescription(rinFlashNewsEntity.getFlashNewsDescription());
				flashNewsViewVo.setFlashNewsDate(rinFlashNewsEntity.getFlashNewsDate());
				flashNewsViewVo.setFlashNewsValidFrom(rinFlashNewsEntity.getFlashNewsValidFrom());
				flashNewsViewVo.setFlashNewsValidTo(rinFlashNewsEntity.getFlashNewsValidTo());
				flashNewsViewVo.setIsFlashNewsActive(rinFlashNewsEntity.getIsFlashNewsActive());
			}
			// BeanUtils.copyProperties(flashNewsVo, flashNewsViewVo);
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

		return flashNewsViewVo;
	}

	/**
	 * Method is used to get all the search details of flash news
	 * 
	 * @param flashNewsVo
	 * @return flashNewsVoList
	 * @throws CoreException
	 */
	@Transactional()
	public List<FlashNewsVO> getAllSearch(FlashNewsVO flashNewsVo,AuthDetailsVo authDetailsVo) {

		List<FlashNewsVO> flashNewsVoList = new ArrayList<>();

		List<RinFlashNewsEntity> resultList = flashNewsDAO.search(flashNewsVo,authDetailsVo);

		if (resultList != null && !resultList.isEmpty()) {
			flashNewsVoList = getAllList(resultList);
		}

		return flashNewsVoList;
	}

}