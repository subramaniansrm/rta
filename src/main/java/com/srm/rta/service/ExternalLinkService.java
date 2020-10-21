package com.srm.rta.service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.config.PicturePath;
import com.srm.coreframework.service.CommonService;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.rta.dao.ExternalLinkDAO;
import com.srm.rta.entity.ExternalLinkEntity;
import com.srm.rta.repository.ExternalLinkRepository;
import com.srm.rta.vo.ExternalLinkVO;
import com.srm.rta.vo.RequestSubTypeVO;

@Service
public class ExternalLinkService extends CommonService {

	Logger logger = LoggerFactory.getLogger(ExternalLinkService.class);

	@Autowired
	ExternalLinkRepository externalLinkRepo;

	@Autowired
	PicturePath picturePath;

	@Autowired
	ExternalLinkDAO externalLinkDAO;

	@Transactional
	public List<ExternalLinkVO> getAllList(AuthDetailsVo authDetailsVo) throws CommonException {

		List<ExternalLinkEntity> externalLinkEntityList = new ArrayList<ExternalLinkEntity>();
		try {

			Integer entityId = authDetailsVo.getEntityId();

			externalLinkEntityList = externalLinkRepo.getAll(entityId);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dbFailure",authDetailsVo));
		}
		List<ExternalLinkVO> externalLinkVoList = new ArrayList<ExternalLinkVO>();
		try {
			externalLinkVoList = getAllListFields(externalLinkEntityList);

			return externalLinkVoList;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
	}

	private List<ExternalLinkVO> getAllListFields(List<ExternalLinkEntity> externalLinkEntityList) {

		List<ExternalLinkVO> externalLinkVoList = new ArrayList<ExternalLinkVO>();

		for (ExternalLinkEntity externalLinkEntity : externalLinkEntityList) {

			ExternalLinkVO externalLinkVo = new ExternalLinkVO();
			if (null != externalLinkEntity.getId()) {
				externalLinkVo.setId(externalLinkEntity.getId());
			}
			if (externalLinkEntity.getExternalLinkName() != null) {
				externalLinkVo.setExternalLinkName(externalLinkEntity.getExternalLinkName());
			}

			if (externalLinkEntity.getExternalLinkLogo() != null) {
				File file = new File((String) externalLinkEntity.getExternalLinkLogo());
				externalLinkVo.setExternalLinkLogo(file.getName());
			}
			if (externalLinkEntity.getExternalLinkUrl() != null) {
				externalLinkVo.setExternalLinkUrl(externalLinkEntity.getExternalLinkUrl());
			}
			if (null != externalLinkEntity.getExternalLinkDisplaySeq()) {
				externalLinkVo.setExternalLinkDisplaySeq(externalLinkEntity.getExternalLinkDisplaySeq());
			}
			externalLinkVo.setExternalLinkIsActive(externalLinkEntity.isExternalLinkIsActive());

			if (externalLinkEntity.isExternalLinkIsActive()) {
				externalLinkVo.setStatus(CommonConstant.Active);
			} else {
				externalLinkVo.setStatus(CommonConstant.InActive);
			}
			externalLinkVoList.add(externalLinkVo);

		}
		return externalLinkVoList;

	}

	@Transactional
	public ExternalLinkVO load(ExternalLinkVO externalLinkVo,AuthDetailsVo authDetailsVo) throws CommonException {

		ExternalLinkEntity externalLinkEntityList = new ExternalLinkEntity();

		try {

			externalLinkEntityList = externalLinkRepo.findOne(externalLinkVo.getId());
		} catch (NoResultException e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("noResultFound",authDetailsVo));
		} catch (NonUniqueResultException e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("noRecordFound",authDetailsVo));
		}
		ExternalLinkVO externalLinkVoList = new ExternalLinkVO();
		try {
			externalLinkVoList = getAllListView(externalLinkEntityList, externalLinkVo);
			return externalLinkVoList;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

	}

	@Transactional
	private ExternalLinkVO getAllListView(ExternalLinkEntity externalLinkEntity, ExternalLinkVO externalLinkVo)
			throws IllegalAccessException, InvocationTargetException {
		ExternalLinkVO externalLinkViewVo = new ExternalLinkVO();
		BeanUtils.copyProperties(externalLinkVo, externalLinkViewVo);

		if (externalLinkEntity.getId() != 0) {
			externalLinkViewVo.setId(externalLinkEntity.getId());
		}
		if (externalLinkEntity.getExternalLinkName() != null) {
			externalLinkViewVo.setExternalLinkName(externalLinkEntity.getExternalLinkName());
		}

		if (externalLinkEntity.getExternalLinkLogo() != null) {
			File file = new File((String) externalLinkEntity.getExternalLinkLogo());
			externalLinkViewVo.setExternalLinkLogo(file.getName());
		}
		if (externalLinkEntity.getExternalLinkUrl() != null) {
			externalLinkViewVo.setExternalLinkUrl(externalLinkEntity.getExternalLinkUrl());
		}
		if (externalLinkEntity.getExternalLinkDisplaySeq() != null) {
			externalLinkViewVo.setExternalLinkDisplaySeq(externalLinkEntity.getExternalLinkDisplaySeq());
		}
		externalLinkViewVo.setExternalLinkIsActive(externalLinkEntity.isExternalLinkIsActive());
		return externalLinkViewVo;

	}

	@Transactional
	public List<ExternalLinkVO> search(ExternalLinkVO externalLinkVo,AuthDetailsVo authDetailsVo) throws CommonException {

		List<ExternalLinkEntity> externalLinkEntityList = new ArrayList<ExternalLinkEntity>();
		try {
			externalLinkEntityList = externalLinkDAO.search(externalLinkVo,authDetailsVo);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
		List<ExternalLinkVO> externalLinkVoList = new ArrayList<ExternalLinkVO>();
		try {
			externalLinkVoList = getAllListFields(externalLinkEntityList);
			return externalLinkVoList;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
	}

	@Transactional
	public void delete(ExternalLinkVO externalLinkVo,AuthDetailsVo authDetailsVo) throws CommonException {

		for (int id : externalLinkVo.getDeleteItem()) {
			ExternalLinkEntity externalLinkEntity = new ExternalLinkEntity();
			try {
				externalLinkEntity = externalLinkRepo.findOne(id);
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
			if (null != authDetailsVo.getUserId()) {
				externalLinkEntity.setCreateBy(authDetailsVo.getUserId());
				externalLinkEntity.setUpdateBy(authDetailsVo.getUserId());

			}

			externalLinkEntity.setCreateDate(CommonConstant.getCalenderDate());
			;
			externalLinkEntity.setUpdateDate(CommonConstant.getCalenderDate());
			externalLinkEntity.setDeleteFlag(CommonConstant.FLAG_ONE);
			try {
				externalLinkEntity = externalLinkRepo.save(externalLinkEntity);
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new CommonException(getMessage("dbFailure",authDetailsVo));
			}
		}

	}

	@Transactional
	public void create(ExternalLinkVO externalLinkVo, MultipartFile[] uploadingFiles,AuthDetailsVo authDetailsVo) throws CommonException {

		try {

			ExternalLinkEntity externalLinkEntity = new ExternalLinkEntity();
			BeanUtils.copyProperties(externalLinkVo, externalLinkEntity);
			if (uploadingFiles != null) {
				externalLinkEntity = saveAttachment(externalLinkEntity, uploadingFiles);
			}
			externalLinkEntity = setCreateUserDetails(externalLinkEntity,authDetailsVo);
			if (null != authDetailsVo.getEntityId()) {
				externalLinkEntity.setEntityLicenseId(authDetailsVo.getEntityId());
			}
			try {
				externalLinkRepo.save(externalLinkEntity);
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new CommonException(getMessage("dbFailure",authDetailsVo));
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

	}

	private ExternalLinkEntity setCreateUserDetails(ExternalLinkEntity externalLinkEntity,AuthDetailsVo authDetailsVo) {

		externalLinkEntity.setCreateBy(authDetailsVo.getUserId());
		externalLinkEntity.setCreateDate(CommonConstant.getCalenderDate());
		externalLinkEntity.setUpdateBy(authDetailsVo.getUserId());
		externalLinkEntity.setUpdateDate(CommonConstant.getCalenderDate());
		externalLinkEntity.setDeleteFlag(CommonConstant.FLAG_ZERO);
		return externalLinkEntity;
	}

	@Transactional
	public ExternalLinkEntity saveAttachment(ExternalLinkEntity externalLinkEntity, MultipartFile[] uploadingFiles) {

		try {

			for (MultipartFile uploadedFile : uploadingFiles) {

				String fileName = dateAppend(uploadedFile);
				String path = picturePath.getExternalLinkFilePath();
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
				externalLinkEntity.setExternalLinkLogo(path);

			}
		} catch (JsonParseException e) {

			logger.error(e.getMessage());
		} catch (JsonMappingException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return externalLinkEntity;
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

		return attachmentFileName.substring(attachmentFileName.lastIndexOf(".") + 1);

	}

	@Transactional
	public void update(ExternalLinkVO externalLinkVo, MultipartFile[] uploadingFiles,AuthDetailsVo authDetailsVo) throws CommonException {

		ExternalLinkEntity externalLinkEntity = new ExternalLinkEntity();

		externalLinkEntity = externalLinkRepo.findOne(externalLinkVo.getId());
		if (externalLinkEntity.getId() != 0) {
			externalLinkEntity.setId(externalLinkVo.getId());
		}
		if (externalLinkEntity.getExternalLinkName() != null) {
			externalLinkEntity.setExternalLinkName(externalLinkVo.getExternalLinkName());
		}

		if (externalLinkEntity.getExternalLinkUrl() != null) {
			externalLinkEntity.setExternalLinkUrl(externalLinkVo.getExternalLinkUrl());
		}
		if (externalLinkEntity.getExternalLinkDisplaySeq() != 0) {
			externalLinkEntity.setExternalLinkDisplaySeq(externalLinkVo.getExternalLinkDisplaySeq());
		}
		externalLinkEntity.setExternalLinkIsActive(externalLinkVo.isExternalLinkIsActive());
		externalLinkEntity = setUpdatedUserDetails(externalLinkEntity,authDetailsVo);
		if (uploadingFiles != null) {
			externalLinkEntity = saveAttachment(externalLinkEntity, uploadingFiles);
		}
		if (null != authDetailsVo.getEntityId()) {
			externalLinkEntity.setEntityLicenseId(authDetailsVo.getEntityId());
		}
		try {
			externalLinkRepo.save(externalLinkEntity);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dbFailure",authDetailsVo));
		}

	}

	private ExternalLinkEntity setUpdatedUserDetails(ExternalLinkEntity externalLinkEntity,AuthDetailsVo authDetailsVo) {

		externalLinkEntity.setUpdateBy(authDetailsVo.getUserId());
		externalLinkEntity.setUpdateDate(CommonConstant.getCalenderDate());

		return externalLinkEntity;
	}

	@Transactional
	public ExternalLinkVO attachmentDownload(ExternalLinkVO externalLinkVo,AuthDetailsVo authDetailsVo)
			throws CommonException{

		ExternalLinkVO externalLinkVoList = new ExternalLinkVO();

		ExternalLinkEntity externalLinkEntity = new ExternalLinkEntity();
		try {

			externalLinkEntity = externalLinkRepo.attachmentDownload(externalLinkVo.getId());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dbFailure",authDetailsVo));
		}

		if(null != externalLinkEntity && null != externalLinkEntity.getExternalLinkLogo()){
			externalLinkVoList.setExternalLinkLogo(externalLinkEntity.getExternalLinkLogo());
		}
		//BeanUtils.copyProperties(externalLinkEntity,externalLinkVoList );

		return externalLinkVoList;

	}
	
	public void findDuplicate(ExternalLinkVO externalLinkVO,AuthDetailsVo authDetailsVo) {
		try {
			int count = externalLinkDAO.findDuplicate(externalLinkVO,authDetailsVo);
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

}
