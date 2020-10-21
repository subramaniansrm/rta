package com.srm.rta.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.transaction.Transactional;

import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.controller.CommonController;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.rta.dao.HolidayDAO;
import com.srm.rta.entity.Holiday;
import com.srm.rta.entity.HolidayDetails;
import com.srm.rta.repository.HolidayDetailsRepository;
import com.srm.rta.repository.HolidayRepository;
import com.srm.rta.vo.HolidayDetailsVO;
import com.srm.rta.vo.HolidayVO;

@Service
public class HolidayService extends CommonController<HolidayVO> {

	@Autowired
	HolidayDAO holidayDAO;


	@Autowired
	HolidayRepository holidayRepository;

	@Autowired
	HolidayDetailsRepository holidayDetailsRepository;

	org.slf4j.Logger logger = LoggerFactory.getLogger(HolidayService.class);

	@Transactional
	public List<HolidayVO> getHolidayList(AuthDetailsVo authDetailsVo) {

		List<HolidayVO> holidayVoList = new ArrayList<>();

		HolidayVO holidayVo = new HolidayVO();

		Integer entityId = authDetailsVo.getEntityId();
		try {
			List<Holiday> list = holidayRepository.getHolidayList(entityId);

			for (Holiday holidayEntity : list) {

				holidayVo = new HolidayVO();

				try {
					BeanUtils.copyProperties(holidayEntity, holidayVo);
				} catch (CommonException e) {
					logger.error("error", e);
					throw new CommonException(getMessage("beanUtilPropertiesFailure",authDetailsVo));
				}

				holidayVoList.add(holidayVo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
		return holidayVoList;
	}

	@Transactional
	public List<HolidayVO> getSearchHoliday(HolidayVO holiday,AuthDetailsVo authDetailsVo) {

		List<HolidayVO> holidayVoList = new ArrayList<>();

		HolidayVO holidayVo = new HolidayVO();

		try {
			List<Holiday> list = holidayDAO.getHolidaySearch(holiday,authDetailsVo);

			for (Holiday holidayEntity : list) {

				holidayVo = new HolidayVO();

				try {
					BeanUtils.copyProperties(holidayEntity, holidayVo);
				} catch (CommonException e) {
					e.printStackTrace();
					logger.error("error", e);
					throw new CommonException(getMessage("beanUtilPropertiesFailure",authDetailsVo));
				}

				holidayVoList.add(holidayVo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
		return holidayVoList;
	}

	@Transactional
	public void deleteHoliday(HolidayVO holidayVo,AuthDetailsVo authDetailsVo) {

		try {
			Integer[] deleteItems = holidayVo.getDeleteItem();

			for (int i = 0; i < deleteItems.length; i++) {

				holidayDAO.deleteHoliday(deleteItems[i],authDetailsVo);
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

	@Transactional
	public HolidayVO viewHoliday(HolidayVO holidayVo,AuthDetailsVo authDetailsVo) {

		HolidayVO holiday = new HolidayVO();

		BeanUtils.copyProperties(holidayVo, holiday);

		Holiday holidayEntity = new Holiday();

		try {
			holidayEntity = holidayRepository.findOne(holidayVo.getId());
			if (holidayEntity != null) {
				BeanUtils.copyProperties(holidayEntity, holiday);
			}
		} catch (NoResultException e) {
			logger.error("error", e);
			throw new CommonException(getMessage("noResultFound",authDetailsVo));
		} catch (NonUniqueResultException e) {
			logger.error("error", e);
			throw new CommonException(getMessage("noUniqueFound",authDetailsVo));
		} catch (Exception e) {
			logger.error("error", e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

		List<HolidayDetails> holidayDetailsEntity = holidayDetailsRepository.findDetailId(authDetailsVo.getEntityId(),
  				holidayVo.getId());

		List<HolidayDetailsVO> holidayDetailsVoList = new ArrayList<>();
		if (!holidayDetailsEntity.isEmpty()) {
			for (HolidayDetails holidayDetail : holidayDetailsEntity) {

				HolidayDetailsVO holidayDetails = new HolidayDetailsVO();

				holidayDetails.setLocationId(holidayDetail.getLocationId());
				//holidayDetails.setLocationName(holidayDetail.getUserLocationEntity().getUserLocationName());

				holidayDetails.setSublocationId(holidayDetail.getSubLocationId());
				//holidayDetails.setSublocationName(holidayDetail.getSubLocationEntity().getSubLocationName());

				BeanUtils.copyProperties(holidayDetail, holidayDetails);

				holidayDetailsVoList.add(holidayDetails);
			}
			holiday.setHolidayDetailsList(holidayDetailsVoList);
		}
		return holiday;

	}

	@Transactional
	public void saveHoliday(HolidayVO holidayVo,AuthDetailsVo authDetailsVo) {

		Holiday holidayEntity = new Holiday();
		try {
			if (null != holidayVo) {
				BeanUtils.copyProperties(holidayVo, holidayEntity);

				holidayEntity.setDeleteFlag(CommonConstant.FLAG_ZERO);
				if (null != authDetailsVo.getUserId()) {
					holidayEntity.setCreateBy(authDetailsVo.getUserId());
					holidayEntity.setUpdateBy(authDetailsVo.getUserId());
				}
				holidayEntity.setCreateDate(CommonConstant.getCalenderDate());

				holidayEntity.setUpdateDate(CommonConstant.getCalenderDate());
				holidayEntity.setDeleteFlag(CommonConstant.FLAG_ZERO);
				/*EntityLicense entityLicenseEntity = new EntityLicense();
				entityLicenseEntity.setId(authDetailsVo.getEntityId());*/
				holidayEntity.setEntityLicenseId(authDetailsVo.getEntityId());
				holidayRepository.save(holidayEntity);

				saveholidayDetails(holidayVo, holidayEntity,authDetailsVo);

			}

		} catch (CommonException e) {
			logger.error("error", e);
			throw new CommonException(getMessage("beanUtilPropertiesFailure",authDetailsVo));
		}

	}

	public void saveholidayDetails(HolidayVO holidayVo, Holiday holidayEntity,AuthDetailsVo authDetailsVo) {

		if (holidayVo != null) {
			for (HolidayDetailsVO holidayDetailsVo : holidayVo.getHolidayDetailsList()) {

				HolidayDetails holidayDetailsEntity = new HolidayDetails();

				if (null != holidayDetailsVo.getLocationId()) {
					holidayDetailsEntity.setLocationId(holidayDetailsVo.getLocationId());
					/*UserLocation locationEntity = new UserLocation();
					locationEntity.setId(holidayDetailsVo.getLocationId());
					holidayDetailsEntity.setUserLocationEntity(locationEntity);*/
				}

				if (null != holidayDetailsVo.getSublocationId()) {
					holidayDetailsEntity.setSubLocationId(holidayDetailsVo.getSublocationId());
					/*SubLocation subLocationEntity = new SubLocation();
					subLocationEntity.setSublocationId(holidayDetailsVo.getSublocationId());
					holidayDetailsEntity.setSubLocationEntity(subLocationEntity);*/
				}
				holidayDetailsEntity.setHolidayId(holidayEntity.getId());
				holidayDetailsEntity.setActiveFlag(holidayDetailsVo.getActiveFlag());
				holidayDetailsEntity.setDeleteFlag(CommonConstant.FLAG_ZERO);

				holidayDetailsEntity.setCreateBy(authDetailsVo.getUserId());
				holidayDetailsEntity.setUpdateBy(authDetailsVo.getUserId());

				holidayDetailsEntity.setCreateDate(CommonConstant.getCalenderDate());

				holidayDetailsEntity.setUpdateDate(CommonConstant.getCalenderDate());

				/*EntityLicense entityLicenseEntity = new EntityLicense();
				entityLicenseEntity.setId(authDetailsVo.getEntityId());*/
				holidayDetailsEntity.setEntityLicenseId(authDetailsVo.getEntityId());
				holidayDetailsRepository.save(holidayDetailsEntity);

			}
		}

	}

	@Transactional
	public void updateHoliday(HolidayVO holidayVo,AuthDetailsVo authDetailsVo) {

		try {
			if (null != holidayVo.getId()) {

				Holiday holidayEntity = holidayRepository.findOne(holidayVo.getId());

				if (null != holidayEntity) {

					BeanUtils.copyProperties(holidayVo, holidayEntity);

					holidayEntity.setDeleteFlag(CommonConstant.FLAG_ZERO);
					holidayEntity.setUpdateBy(authDetailsVo.getUserId());
					holidayEntity.setUpdateDate(CommonConstant.getCalenderDate());

					holidayRepository.save(holidayEntity);

					updateHolidayDetails(holidayVo,authDetailsVo);

				}
			}
		} catch (Exception e) {
			logger.error("error", e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
	}

	public void updateHolidayDetails(HolidayVO holidayVo,AuthDetailsVo authDetailsVo) {

		for (HolidayDetailsVO holidayDetailsVo : holidayVo.getHolidayDetailsList()) {

			HolidayDetails holidayDetailsEntity = new HolidayDetails();

			if (holidayDetailsVo.getHolidayDetailsId() != null) {
				holidayDetailsEntity = holidayDetailsRepository.findOne(holidayDetailsVo.getHolidayDetailsId());
			}

			if (null != holidayDetailsVo.getLocationId()) {
				holidayDetailsEntity.setLocationId(holidayDetailsVo.getLocationId());
				/*UserLocation locationEntity = new UserLocation();
				locationEntity.setId(holidayDetailsVo.getLocationId());
				holidayDetailsEntity.setUserLocationEntity(locationEntity);*/
			}

			if (null != holidayDetailsVo.getSublocationId()) {
				holidayDetailsEntity.setSubLocationId(holidayDetailsVo.getSublocationId());
				/*SubLocation subLocationEntity = new SubLocation();
				subLocationEntity.setSublocationId(holidayDetailsVo.getSublocationId());
				holidayDetailsEntity.setSubLocationEntity(subLocationEntity);*/
			}
			holidayDetailsEntity.setActiveFlag(holidayDetailsVo.getActiveFlag());

			holidayDetailsEntity.setHolidayId(holidayVo.getId());

			if (holidayDetailsVo.getHolidayDetailsId() != null) {
				holidayDetailsEntity.setDeleteFlag(CommonConstant.FLAG_ZERO);
				holidayDetailsEntity.setUpdateBy(authDetailsVo.getUserId());
				holidayDetailsEntity.setUpdateDate(CommonConstant.getCalenderDate());
			} else {
				holidayDetailsEntity.setDeleteFlag(CommonConstant.FLAG_ZERO);

				holidayDetailsEntity.setCreateBy(authDetailsVo.getUserId());
				holidayDetailsEntity.setUpdateBy(authDetailsVo.getUserId());

				holidayDetailsEntity.setCreateDate(CommonConstant.getCalenderDate());

				holidayDetailsEntity.setUpdateDate(CommonConstant.getCalenderDate());
			}

			/*EntityLicense entityLicenseEntity = new EntityLicense();
			entityLicenseEntity.setId(authDetailsVo.getEntityId());*/

			holidayDetailsEntity.setEntityLicenseId(authDetailsVo.getEntityId());

			holidayDetailsRepository.save(holidayDetailsEntity);
		}

	}

	@Transactional
	public List<HolidayDetailsVO> getAllHolidayList(HolidayDetailsVO holidayDetails,AuthDetailsVo authDetailsVo) {

		List<HolidayDetailsVO> holidayDetailsVoList = new ArrayList<HolidayDetailsVO>();

		HolidayDetailsVO holidayDetailsVo = new HolidayDetailsVO();

		List<HolidayDetails> list = holidayDAO.getAllHolidayList(holidayDetails,authDetailsVo);

		for (HolidayDetails holidayDetailsEntity : list) {

			holidayDetailsVo = new HolidayDetailsVO();

			try {
				BeanUtils.copyProperties(holidayDetailsEntity, holidayDetailsVo);
			} catch (CommonException e) {
				logger.error("error", e);
				throw new CommonException(getMessage("beanUtilPropertiesFailure",authDetailsVo));
			}
			holidayDetailsVo.setLocationId(holidayDetailsEntity.getLocationId());
			//holidayDetailsVo.setLocationName(holidayDetailsEntity.getUserLocationEntity().getUserLocationName());

			holidayDetailsVo.setSublocationId(holidayDetailsEntity.getSubLocationId());
			//holidayDetailsVo.setSublocationName(holidayDetailsEntity.getSubLocationEntity().getSubLocationName());

			holidayDetailsVoList.add(holidayDetailsVo);
		}

		return holidayDetailsVoList;
	}

}
