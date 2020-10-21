package com.srm.rta.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.transaction.Transactional;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.service.CommonService;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.rta.config.RTAPicturePath;
import com.srm.rta.entity.EmergencyContactEntity;
import com.srm.rta.repository.EmergencyContactRepository;
import com.srm.rta.vo.EmergencyContactVO;

@Service
public class EmergencyContactService extends CommonService {

	Logger logger = LoggerFactory.getLogger(EmergencyContactService.class);

	@Autowired
	EmergencyContactRepository emergencyContactRepo;

	@Autowired
	RTAPicturePath picturePath;

	@Transactional
	public List<EmergencyContactVO> getAll(AuthDetailsVo authDetailsVo) throws CommonException {
		
		List<EmergencyContactEntity> emergencyContactEntityList = new ArrayList<>();

		List<EmergencyContactVO> emergencyContactVo = new ArrayList<>();

		Integer entityId = authDetailsVo.getEntityId();

		// Get all the details of phone book in DB
		try {

			emergencyContactEntityList = emergencyContactRepo.getAll(entityId);

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dbFailure",authDetailsVo));
		}

		// Set all the details in VO
		try {

			if (emergencyContactEntityList != null && emergencyContactEntityList.size() > 0)
				emergencyContactVo = getAllList(emergencyContactEntityList);

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("copyFailure",authDetailsVo));
		}

		return emergencyContactVo;

	}

	private List<EmergencyContactVO> getAllList(List<EmergencyContactEntity> emergencyContactEntityList) {

		List<EmergencyContactVO> emergencyContactVoList = new ArrayList<EmergencyContactVO>();

		for (EmergencyContactEntity emergencyContact : emergencyContactEntityList) {
			EmergencyContactVO emergencyContactVo = new EmergencyContactVO();

			if (0 != emergencyContact.getEmergencyContactPathId()) {
				emergencyContactVo.setEmergencyContactPathId(emergencyContact.getEmergencyContactPathId());
			}

			if (null != emergencyContact.getEmergencyContactName()) {
				emergencyContactVo.setEmergencyContactName(emergencyContact.getEmergencyContactName());
			}

			if (null != emergencyContact.getEmergencyContactPath()) {

				StringBuffer modifiedQuery = new StringBuffer(picturePath.getPhoneBookExcelAttachment());
				File file = new File((String) (emergencyContact.getEmergencyContactPath()));
				modifiedQuery.append(file.getName());
				emergencyContactVo.setEmergencyContactPath(file.getName());

			}

			emergencyContactVoList.add(emergencyContactVo);

		}

		return emergencyContactVoList;

	}

	@Transactional
	public void saveAttachment(EmergencyContactVO emergencyContactVo, MultipartFile[] uploadingFiles,AuthDetailsVo authDetailsVo)
			throws CommonException {

		EmergencyContactEntity emergencyContactEntity = new EmergencyContactEntity();

		// Copy the EmergencyContact details from VO to Entity
		try {

			BeanUtils.copyProperties(emergencyContactEntity, emergencyContactVo);

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("copyFailure",authDetailsVo));
		}

		if (uploadingFiles != null) {
			emergencyContactEntity = saveAttachment(emergencyContactEntity, uploadingFiles,authDetailsVo);
		}

		emergencyContactEntity.setEmergencyContactName(emergencyContactVo.getEmergencyContactName());
		emergencyContactEntity.setCreateBy(authDetailsVo.getUserId());
		emergencyContactEntity.setUpdateBy(authDetailsVo.getUserId());

		emergencyContactEntity.setCreateDate(CommonConstant.getCalenderDate());
		emergencyContactEntity.setUpdateDate(CommonConstant.getCalenderDate());
		emergencyContactEntity.setDeleteFlag(CommonConstant.FLAG_ZERO);
		emergencyContactEntity.setEntityLicenseId(authDetailsVo.getEntityId());
		// Create the new phone book details

		try {

			emergencyContactRepo.save(emergencyContactEntity);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

	}

	@Transactional
	public EmergencyContactEntity saveAttachment(EmergencyContactEntity emergencyContactEntity,
			MultipartFile[] uploadingFiles,AuthDetailsVo authDetailsVo) throws CommonException {

		try {
			for (MultipartFile uploadedFile : uploadingFiles) {

				String fileName = dateAppend(uploadedFile);
				String path = picturePath.getPhoneBookExcelAttachment();
				File fileToCreate = new File(path);
				if (fileToCreate.exists()) {
					path = path + CommonConstant.SLASH;
					if (!fileToCreate.exists()) {
						fileToCreate.mkdir();
					}
				} else {
					fileToCreate.mkdir();
					path = path + CommonConstant.SLASH;
					fileToCreate = new File(path);
					fileToCreate.mkdir();
				}
				fileToCreate = new File(path + fileName);

				uploadedFile.transferTo(fileToCreate);
				path = path + fileName;

				emergencyContactEntity.setEmergencyContactPath(path);

			}
		} catch (JsonParseException e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		} catch (JsonMappingException e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
		return emergencyContactEntity;
	}

	public String dateAppend(MultipartFile uploadedFile) {

		String fileName = uploadedFile.getOriginalFilename().substring(0,
				uploadedFile.getOriginalFilename().lastIndexOf("."));

		String date = CommonConstant.formatDatetoString(new Date(), CommonConstant.FILE_NAME_FORMAT_DATE);

		fileName = fileName + date;

		String format = "." + getfileFormat(uploadedFile.getOriginalFilename());

		fileName = fileName + format;

		return fileName;

	}

	public static String getfileFormat(String attachmentFileName) {

		try {

			return attachmentFileName.substring(attachmentFileName.lastIndexOf(".") + 1);

		} catch (Exception e) {
			return "";
		}
	}

	@Transactional
	public void update(EmergencyContactVO emergencyContactVo, MultipartFile[] uploadingFiles,AuthDetailsVo authDetailsVo) throws CommonException {

		EmergencyContactEntity emergencyContactEntity = new EmergencyContactEntity();

		// Get the phone book details from DB using ID
		try {

			emergencyContactEntity = emergencyContactRepo.findOne(emergencyContactVo.getEmergencyContactPathId());

		} catch (NoResultException e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("noResultFound",authDetailsVo));
		} catch (NonUniqueResultException e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("noRecordFound",authDetailsVo));
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

		emergencyContactEntity.setEmergencyContactName(emergencyContactVo.getEmergencyContactName());

		emergencyContactEntity.setUpdateDate(CommonConstant.getCalenderDate());

		emergencyContactEntity.setUpdateBy(authDetailsVo.getUserId());

		if (uploadingFiles != null) {
			emergencyContactEntity = saveAttachment(emergencyContactEntity, uploadingFiles,authDetailsVo);
		}
		emergencyContactEntity.setEntityLicenseId(authDetailsVo.getEntityId());

		// Update the Phone book details
		try {

			emergencyContactRepo.save(emergencyContactEntity);

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
	}

	@Transactional
	public EmergencyContactVO attachmentDownload(EmergencyContactVO emergencyContactVo,AuthDetailsVo authDetailsVo) throws CommonException {

		EmergencyContactVO emergencyContactVoList = new EmergencyContactVO();

		EmergencyContactEntity emergencyContactEntity = null;

		// Get the Emergency details from DB using ID
		try {
			Integer entityId = authDetailsVo.getEntityId();

			emergencyContactEntity = emergencyContactRepo
					.attachmentDownload(emergencyContactVo.getEmergencyContactPathId(), entityId);

		} catch (NoResultException e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("noResultFound",authDetailsVo));
		} catch (NonUniqueResultException e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("noRecordFound",authDetailsVo));
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

		// Copy the data from entity to VO
		try {

			BeanUtils.copyProperties(emergencyContactVoList, emergencyContactEntity);

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("copyFailure",authDetailsVo));
		}
		return emergencyContactVoList;

	}

	@Transactional
	public void delete(EmergencyContactVO emergencyContactVo,AuthDetailsVo authDetailsVo) throws CommonException {

		for (int id : emergencyContactVo.getDeleteItem()) {

			EmergencyContactEntity emergencyContactEntity = null;

			// Get the phone book details using ID
			try {

				emergencyContactEntity = emergencyContactRepo.findOne(id);

			} catch (NoResultException e) {
				logger.error(e.getMessage());
				throw new CommonException(getMessage("noResultFound",authDetailsVo));
			} catch (NonUniqueResultException e) {
				logger.error(e.getMessage());
				throw new CommonException(getMessage("noRecordFound",authDetailsVo));
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new CommonException(getMessage("dataFailure",authDetailsVo));
			}

			// Delete the phone book details
			try {

				emergencyContactEntity.setDeleteFlag(CommonConstant.FLAG_ONE);
				emergencyContactEntity.setUpdateDate(CommonConstant.getCalenderDate());

				emergencyContactEntity.setUpdateDate(CommonConstant.getCalenderDate());

				emergencyContactRepo.save(emergencyContactEntity);

			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new CommonException(getMessage("dataFailure",authDetailsVo));
			}

		}
	}

	@Transactional
	public EmergencyContactVO load(EmergencyContactVO emergencyContactViewVo,AuthDetailsVo authDetailsVo) throws CommonException {

		EmergencyContactVO emergencyContactVo = new EmergencyContactVO();
		EmergencyContactEntity emergencyContactEntityList = new EmergencyContactEntity();

		// Get the details of phone book in DB using Phone Book ID
		try {

			emergencyContactEntityList = emergencyContactRepo.load(emergencyContactViewVo.getEmergencyContactPathId());

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dbFailure",authDetailsVo));
		}

		// Set all the details in VO
		try {

			emergencyContactVo = getAllListLoad(emergencyContactEntityList, emergencyContactViewVo);
			return emergencyContactVo;

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("copyFailure",authDetailsVo));
		}

	}

	private EmergencyContactVO getAllListLoad(EmergencyContactEntity emergencyContactEntityList,
			EmergencyContactVO emergencyContactViewVo) {
		if (0 != emergencyContactEntityList.getEmergencyContactPathId()) {
			emergencyContactViewVo.setEmergencyContactPathId(emergencyContactEntityList.getEmergencyContactPathId());
		}
		if (null != emergencyContactEntityList.getEmergencyContactName()) {
			emergencyContactViewVo.setEmergencyContactName(emergencyContactEntityList.getEmergencyContactName());
		}

		if (null != emergencyContactEntityList.getEmergencyContactPath()) {

			StringBuffer modifiedQuery = new StringBuffer(picturePath.getPhoneBookExcelAttachment());
			File file = new File((String) (emergencyContactEntityList.getEmergencyContactPath()));
			modifiedQuery.append(file.getName());
			emergencyContactViewVo.setEmergencyContactPath(file.getName());

		}

		return emergencyContactViewVo;
	}

	public List<EmergencyContactVO> getAllDownload(AuthDetailsVo authDetailsVo) throws CommonException {

		List<EmergencyContactEntity> emergencyContactEntityList = null;

		List<EmergencyContactVO> emergencyContactVo = null;

		// Get all the details of phone book in DB
		try {

			Integer entityId = authDetailsVo.getEntityId();

			emergencyContactEntityList = emergencyContactRepo.getAll(entityId);

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dbFailure",authDetailsVo));
		}

		// Set all the details in VO
		try {

			if (emergencyContactEntityList != null && emergencyContactEntityList.size() > 0)
				emergencyContactVo = getAllList(emergencyContactEntityList);

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("copyFailure",authDetailsVo));
		}

		return emergencyContactVo;

	}

}
