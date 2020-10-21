package com.srm.rta.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ibm.icu.text.SimpleDateFormat;
import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.controller.CommonController;
import com.srm.coreframework.entity.CodeGenerationEntity;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.coreframework.vo.ScreenAuthenticationVO;
import com.srm.rta.config.RTAPicturePath;
import com.srm.rta.dao.WidgetDAO;
import com.srm.rta.entity.WidgetDetailEntity;
import com.srm.rta.entity.WidgetEntity;
import com.srm.rta.repository.WidgetDetailRepository;
import com.srm.rta.repository.WidgetRepository;
import com.srm.rta.vo.WidgetDetailVO;
import com.srm.rta.vo.WidgetVO;

@Service
public class WidgetService extends CommonController<WidgetVO> {

	Logger logger = LoggerFactory.getLogger(WidgetService.class);

	@Autowired
	  WidgetRepository widgetRepository;

	@Autowired
	  WidgetDAO widgetDAO;

	@Autowired
	  RTAPicturePath picturePath;

	@PersistenceContext
	EntityManager manager;

	@Autowired
	WidgetDetailRepository widgetDetailRepository;

	@Transactional
	public List<WidgetVO> getAll(AuthDetailsVo authDetailsVo) throws CommonException {
		List<WidgetEntity> widgetEntityList = null;
		try {

			Integer entityId = authDetailsVo.getEntityId();

			// It is used to get all records from database.
			widgetEntityList = widgetRepository.getAll(entityId);

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
		// It is used to convert all record from Entity to Vo.
		List<WidgetVO> widgetVoList = getAllList(widgetEntityList);

		return widgetVoList;
	}

	public List<WidgetVO> getAllList(List<WidgetEntity> list_WidgetEntityList) {
		List<WidgetVO> list_WidgetVo = new ArrayList<WidgetVO>();
		for (WidgetEntity widgetEntity : list_WidgetEntityList) {

			WidgetVO widgetVo = new WidgetVO();
			if (null != widgetEntity.getWidgetId()) {

				widgetVo.setWidgetId(widgetEntity.getWidgetId());
			}
			if (null != widgetEntity.getWidgetCode()) {

				widgetVo.setWidgetCode(widgetEntity.getWidgetCode());
			}
			if (null != widgetEntity.getWidgetIndex()) {

				widgetVo.setWidgetIndex(widgetEntity.getWidgetIndex());
			}
			if (null != widgetEntity.getWidgetTitle()) {

				widgetVo.setWidgetTitle(widgetEntity.getWidgetTitle());
			}
			if (null != widgetEntity.getWidgetSeq()) {

				widgetVo.setWidgetSeq(widgetEntity.getWidgetSeq());
			}
			if (true == widgetEntity.isWidgetIsActive()) {

				widgetVo.setWidgetIsActive(widgetEntity.isWidgetIsActive());
				widgetVo.setStatus(CommonConstant.Active);
			}
			if (false == widgetEntity.isWidgetIsActive()) {

				widgetVo.setWidgetIsActive(widgetEntity.isWidgetIsActive());
				widgetVo.setStatus(CommonConstant.InActive);

			}

			list_WidgetVo.add(widgetVo);
		}

		return list_WidgetVo;
	}

	@Transactional
	public List<WidgetVO> getAllSearch(WidgetVO widgetVo,AuthDetailsVo authDetailsVo) throws CommonException {
		List<WidgetEntity> widgetEntityList = null;

		try {

			widgetEntityList = widgetDAO.getAllSearch(widgetVo,authDetailsVo);

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
		// It is used to convert all record from Entity to Vo.
		List<WidgetVO> widgetVoList = getAllList(widgetEntityList);
		return widgetVoList;
	}

	public void findDuplicateIndex(WidgetVO widgetVo,AuthDetailsVo authDetailsVo) throws CommonException {
		try {

			int count = widgetDAO.findDuplicateIndex(widgetVo,authDetailsVo);
			if (count > 0) {
				throw new CommonException(getMessage("widgetIndexDuplicate",authDetailsVo));

			}

		} catch (CommonException e) {
			logger.error(e.getMessage());
			throw new CommonException(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

	}

	@Transactional
	public WidgetVO create(WidgetVO widgetVo, MultipartFile[] uploadingFiles, MultipartFile[] uploadingFiles1,
			MultipartFile[] uploadingFiles2,AuthDetailsVo authDetailsVo) throws CommonException {
		WidgetEntity widgetEntity = new WidgetEntity();

		//CodeGenerationEntity codeGenerationEntity = null;
		String widgetCode = null;
		try {
			widgetCode = widgetDAO.findAutoGenericCode(CommonConstant.Widgets,authDetailsVo);

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("autoCodeGenerationFailure",authDetailsVo));
		}

		widgetEntity.setWidgetCode(widgetCode);
		if (widgetVo.getWidgetIndex() != 0) {
			widgetEntity.setWidgetIndex(widgetVo.getWidgetIndex());
		}

		if (widgetVo.getWidgetTitle() != null) {
			widgetEntity.setWidgetTitle(widgetVo.getWidgetTitle());

		}
		// widgetEntity.setWidgetSeq(widgetVo.getWidgetSeq());
		widgetEntity.setWidgetIsActive(widgetVo.isWidgetIsActive());

		// BeanUtils.copyProperties(widgetVo, widgetEntity);

		if (uploadingFiles != null) {
			// It is used to attach the pic or documents
			widgetEntity = saveAttachment(widgetEntity, uploadingFiles);
		}
		widgetEntity = setCreateUserDetails(widgetEntity,authDetailsVo);
		if (null != authDetailsVo.getEntityId()) {
			widgetEntity.setEntityLicenseId(authDetailsVo.getEntityId());
		}
		widgetEntity.setDeleteFlag(CommonConstant.FLAG_ZERO);
		try {
			// It is used to save the record in database.
			widgetRepository.save(widgetEntity);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

		if (widgetVo != null && widgetVo.getWidgetDetailVoList() != null) {

			// It is used to save the widgetDetail record in database.
			fetchWidgetDetailValues(widgetVo, widgetEntity, uploadingFiles1, uploadingFiles2,authDetailsVo);

		}
		widgetVo.setWidgetId(widgetEntity.getWidgetId());

		return widgetVo;

	}

	private WidgetEntity setCreateUserDetails(WidgetEntity widgetEntity,AuthDetailsVo authDetailsVo) {

		widgetEntity.setCreateBy(authDetailsVo.getUserId());
		widgetEntity.setCreateDate(CommonConstant.getCalenderDate());
		widgetEntity.setUpdateBy(authDetailsVo.getUserId());
		widgetEntity.setUpdateDate(CommonConstant.getCalenderDate());

		return widgetEntity;
	}

	@Transactional
	public WidgetEntity saveAttachment(WidgetEntity widgetEntity, MultipartFile[] uploadingFiles) {

		try {
			for (MultipartFile uploadedFile : uploadingFiles) {

				String fileName = dateAppend(uploadedFile);

				String path = picturePath.getWidgetFilePath();
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

				widgetEntity.setWidgetIcon(path);

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
		return widgetEntity;
	}

	public String dateAppend(MultipartFile uploadedFile) {
		String fileName = uploadedFile.getOriginalFilename().replaceAll("\\s", "").substring(0,
				uploadedFile.getOriginalFilename().replaceAll("\\s", "").lastIndexOf("."));

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
	public void fetchWidgetDetailValues(WidgetVO widgetVo, WidgetEntity widgetEntity, MultipartFile[] uploadingFiles1,
			MultipartFile[] uploadingFiles2,AuthDetailsVo authDetailsVo) throws CommonException {

		List<WidgetDetailVO> widgetDetailVoList = widgetVo.getWidgetDetailVoList();
		int entityId = 0;
		if (null != authDetailsVo.getEntityId()) {
			entityId = authDetailsVo.getEntityId();
		}
		for (WidgetDetailVO widgetDetailVo : widgetDetailVoList) {
			int index = 0;
			WidgetDetailEntity widgetDetailEntity = new WidgetDetailEntity();
			if (widgetDetailVo.getWidgetDetailId() != null) {
				widgetDetailEntity = widgetDetailRepository.findOne(widgetDetailVo.getWidgetDetailId());
			}

			if (widgetDetailVo.getWidgetDetailHeadingIndex() == null) {
				try {
					index = widgetDetailRepository.createIndex(widgetEntity.getWidgetId(), entityId);
					index = index + CommonConstant.CONSTANT_ONE;
				} catch (Exception e) {
					logger.error(e.getMessage());
					throw new CommonException(getMessage("autoWidgetIndexFailure",authDetailsVo));
				}
				widgetDetailVo.setWidgetDetailHeadingIndex(index);
			}

			widgetDetailEntity.setWidgetId(widgetEntity.getWidgetId());
			if (widgetDetailVo.getWidgetDetailHeading() != null) {
				widgetDetailEntity.setWidgetDetailHeading(widgetDetailVo.getWidgetDetailHeading());
			}
			if (widgetDetailVo.getWidgetDetailHeadingIndex() != 0) {
				widgetDetailEntity.setWidgetDetailHeadingIndex(widgetDetailVo.getWidgetDetailHeadingIndex());

			}
			if (null != widgetDetailVo.getWidgetDetailDescription()) {
				widgetDetailEntity.setWidgetDetailDescription(widgetDetailVo.getWidgetDetailDescription());

			}

			if (null != widgetDetailVo.getWidgetDetailExternalUrl()) {
				widgetDetailEntity.setWidgetDetailExternalUrl(widgetDetailVo.getWidgetDetailExternalUrl());

			}
			if (widgetDetailVo.getWidgetDetailAnnouncementDate() != null) {
				widgetDetailEntity.setWidgetDetailAnnouncementDate(widgetDetailVo.getWidgetDetailAnnouncementDate());

			}
			if (widgetDetailVo.getWidgetDetailValidFrom() != null) {
				widgetDetailEntity.setWidgetDetailValidFrom(widgetDetailVo.getWidgetDetailValidFrom());

			}
			if (widgetDetailVo.getWidgetDetailValidTo() != null) {
				widgetDetailEntity.setWidgetDetailValidTo(widgetDetailVo.getWidgetDetailValidTo());

			}
			widgetDetailEntity.setWidgetDetailIsActive(widgetDetailVo.isWidgetDetailIsActive());

			if (uploadingFiles1 != null) {
				// It is used to attach the pic or documents
				widgetDetailEntity = savePicAttachment(widgetDetailEntity, uploadingFiles1);
			}
			if (uploadingFiles2 != null) {
				// It is used to attach the pic or documents
				widgetDetailEntity = saveTxtAttachment(widgetDetailEntity, uploadingFiles2);
			}

			if (widgetDetailVo.getWidgetDetailId() == null)
				widgetDetailEntity = setCreateUserWidgetDetails(widgetDetailEntity,authDetailsVo);
			else
				widgetDetailEntity = setUpdatedUserWidgetDetails(widgetDetailEntity,authDetailsVo);

			widgetDetailEntity.setEntityLicenseId(authDetailsVo.getEntityId());
			widgetDetailEntity.setDeleteFlag(CommonConstant.FLAG_ZERO);
			// widgetDetailEntityList.add(widgetDetailEntity);
			try {
				widgetDetailRepository.save(widgetDetailEntity);

			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new CommonException(getMessage("dataFailure",authDetailsVo));
			}

		}

	}

	@Transactional
	public WidgetDetailEntity savePicAttachment(WidgetDetailEntity widgetDetailEntity,
			MultipartFile[] uploadingFiles1) {
		StringBuffer allFile = new StringBuffer();

		try {
			for (MultipartFile uploadedFile : uploadingFiles1) {

				String fileName = dateAppend(uploadedFile);

				String path = picturePath.getWidgetFilePath();
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
				// path = path + CommonConstants.SLASH;
				fileToCreate = new File(path + fileName);
				uploadedFile.transferTo(fileToCreate);
				path = path + fileName;
				allFile.append(path);
				allFile.append(',');

				widgetDetailEntity.setWidgetDetailPicPath(allFile.toString());

			}
			allFile.deleteCharAt(allFile.length() - 1);
			widgetDetailEntity.setWidgetDetailPicPath(allFile.toString());

		} catch (JsonParseException e) {
			logger.error(e.getMessage());

		} catch (JsonMappingException e) {
			logger.error(e.getMessage());

		} catch (IOException e) {
			logger.error(e.getMessage());

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return widgetDetailEntity;
	}

	@Transactional
	public WidgetDetailEntity saveTxtAttachment(WidgetDetailEntity widgetDetailEntity,
			MultipartFile[] uploadingFiles2) {
		StringBuffer allFile = new StringBuffer();

		try {

			for (MultipartFile uploadedFile : uploadingFiles2) {

				String fileName = dateAppend(uploadedFile);

				String path = picturePath.getWidgetFilePath();
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
				allFile.append(path);
				allFile.append(',');

				widgetDetailEntity.setWidgetDetailAttPath(allFile.toString());

			}
			allFile.deleteCharAt(allFile.length() - 1);
			widgetDetailEntity.setWidgetDetailAttPath(allFile.toString());
		} catch (JsonParseException e) {
			logger.error(e.getMessage());

		} catch (JsonMappingException e) {
			logger.error(e.getMessage());

		} catch (IOException e) {
			logger.error(e.getMessage());

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return widgetDetailEntity;

	}

	@Transactional
	private WidgetDetailEntity setCreateUserWidgetDetails(WidgetDetailEntity widgetDetailEntity,AuthDetailsVo authDetailsVo) {

		widgetDetailEntity.setCreateBy(authDetailsVo.getUserId());
		widgetDetailEntity.setCreateDate(CommonConstant.getCalenderDate());
		widgetDetailEntity.setUpdateBy(authDetailsVo.getUserId());
		widgetDetailEntity.setUpdateDate(CommonConstant.getCalenderDate());

		return widgetDetailEntity;
	}

	@Transactional
	private WidgetDetailEntity setUpdatedUserWidgetDetails(WidgetDetailEntity widgetDetailEntity,AuthDetailsVo authDetailsVo) {

		widgetDetailEntity.setUpdateBy(authDetailsVo.getUserId());
		widgetDetailEntity.setUpdateDate(CommonConstant.getCalenderDate());

		return widgetDetailEntity;
	}

	@Transactional
	public WidgetVO findWidget(WidgetVO widgetViewVo,AuthDetailsVo authDetailsVo) throws CommonException {

		List<Object> result = null;
		WidgetVO widgetVo = new WidgetVO();
		List<WidgetDetailVO> widgetDetailVoList = new ArrayList<WidgetDetailVO>();

		//Integer entityId = authDetailsVo.getEntityId();
		WidgetVO widget = new WidgetVO();
		BeanUtils.copyProperties(widgetViewVo, widget);
		try {
			result = widgetDAO.findWidgetRecord(widgetViewVo.getWidgetId(),authDetailsVo);
		} catch (Exception e) {

			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
		if (null != result && result.size() > 0) {
			@SuppressWarnings("rawtypes")
			Iterator itr = result.iterator();
			WidgetDetailVO widgetDetailVo;
			while (itr.hasNext()) {
				Object[] object = (Object[]) itr.next();
				widgetDetailVo = new WidgetDetailVO();

				if (null != object[0]) {
					widgetVo.setWidgetId((int) object[0]);
				}
				if (null != object[1]) {

					widgetVo.setWidgetCode((String) object[1]);
				}
				if (null != object[2]) {

					widgetVo.setWidgetIndex((int) object[2]);
				}
				if (null != object[3]) {

					widgetVo.setWidgetTitle((String) object[3]);
				}
				if (null != object[4]) {
					File file = new File((String) object[4]);
					widgetVo.setWidgetIcon(file.getName());
				}

				if (null != object[5]) {

					widgetVo.setWidgetSeq((int) object[5]);
				}
				if (null != object[6]) {

					widgetVo.setWidgetIsActive((boolean) object[6]);
				}
				if (null != object[7]) {

					widgetDetailVo.setWidgetDetailId((int) object[7]);
				}
				if (null != object[8]) {

					widgetDetailVo.setWidgetId((int) object[8]);
				}
				if (null != object[9]) {

					widgetDetailVo.setWidgetDetailHeading((String) object[9]);
				}
				if (null != object[10]) {

					widgetDetailVo.setWidgetDetailHeadingIndex((int) object[10]);
				}
				if (null != object[11]) {

					widgetDetailVo.setWidgetDetailPicIsRequired((boolean) object[11]);
				}
				StringBuffer allFile = new StringBuffer();
				if (null != object[12]) {
					String name = (String) object[12];
					String[] pic = name.split(",");
					for (int i = 0; i < pic.length; i++) {
						File file = new File(pic[i]);
						allFile.append(file.getName());
						allFile.append(',');

						widgetDetailVo.setWidgetDetailPicPath(allFile.toString());

					}
					allFile.deleteCharAt(allFile.length() - 1);

					widgetDetailVo.setWidgetDetailPicPath(allFile.toString());
				}
				if (null != object[13]) {

					widgetDetailVo.setWidgetDetailDescription((String) object[13]);
				}
				if (null != object[14]) {

					widgetDetailVo.setWidgetDetailAttIsRequired((boolean) object[14]);
				}

				StringBuffer attachFile = new StringBuffer();
				if (null != object[15]) {
					String name = (String) object[15];
					String[] pic = name.split(",");
					for (int i = 0; i < pic.length; i++) {
						File file = new File(pic[i]);
						attachFile.append(file.getName());
						attachFile.append(',');

						widgetDetailVo.setWidgetDetailAttPath(attachFile.toString());
					}
					attachFile.deleteCharAt(attachFile.length() - 1);
					widgetDetailVo.setWidgetDetailAttPath(attachFile.toString());
				}

				if (null != object[16]) {

					widgetDetailVo.setWidgetDetailMorePath((String) object[16]);
				}
				if (null != object[17]) {

					widgetDetailVo.setWidgetDetailExternalUrl((String) object[17]);
				}
				if (null != object[18]) {

					widgetDetailVo.setWidgetDetailIsActive((boolean) object[18]);
				}
				if (null != object[19]) {

					widgetDetailVo.setWidgetDetailAnnouncementDate((Date) object[19]);
				}
				if (null != object[20]) {

					widgetDetailVo.setWidgetDetailValidFrom((Date) object[20]);
				}
				if (null != object[21]) {

					widgetDetailVo.setWidgetDetailValidTo((Date) object[21]);
				}
				widgetDetailVoList.add(widgetDetailVo);
			}

		}
		widget.setWidgetVo(widgetVo);
		if (0 != widgetDetailVoList.get(0).getWidgetDetailId()) {
			widget.setWidgetDetailVoList(widgetDetailVoList);
		}

		return widget;
	}

	@Transactional
	public WidgetVO update(WidgetVO widgetVo, MultipartFile[] uploadingFiles, MultipartFile[] uploadingFiles1,
			MultipartFile[] uploadingFiles2,AuthDetailsVo authDetailsVo) throws CommonException {

		WidgetEntity widgetEntity = null;
		// It is used to find the record in database.
		widgetEntity = widgetRepository.findOne(widgetVo.getWidgetId());

		if (widgetVo.getWidgetIndex() != 0) {
			widgetEntity.setWidgetIndex(widgetVo.getWidgetIndex());
		}
		if (widgetVo.getWidgetTitle() != null) {
			widgetEntity.setWidgetTitle(widgetVo.getWidgetTitle());

		}
		widgetEntity.setWidgetIsActive(widgetVo.isWidgetIsActive());

		if (uploadingFiles != null) {
			widgetEntity = saveAttachment(widgetEntity, uploadingFiles);

		}
		// widgetEntity.setWidgetSeq(widgetVo.getWidgetSeq());
		widgetEntity = setUpdatedUserDetails(widgetEntity,authDetailsVo);

		widgetEntity.setEntityLicenseId(authDetailsVo.getEntityId());
		try {
			// It is used to save the record in database.
			widgetRepository.save(widgetEntity);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
		if (widgetVo != null && widgetVo.getWidgetDetailVoList() != null) {

			fetchWidgetDetailValues(widgetVo, widgetEntity, uploadingFiles1, uploadingFiles2,authDetailsVo);
		}
		widgetVo.setWidgetId(widgetEntity.getWidgetId());

		return widgetVo;
	}

	@Transactional
	private WidgetEntity setUpdatedUserDetails(WidgetEntity widgetEntity,AuthDetailsVo authDetailsVo) {

		widgetEntity.setUpdateBy(authDetailsVo.getUserId());
		widgetEntity.setUpdateDate(CommonConstant.getCalenderDate());

		return widgetEntity;
	}

	@Transactional
	public void delete(WidgetVO widgetVo,AuthDetailsVo authDetailsVo) throws CommonException {

		// boolean widget = true;
		for (Integer widgetId : widgetVo.getWidgetsIdList()) {

			WidgetEntity widgetEntity = new WidgetEntity();
			// It is used to find the record in database.
			widgetEntity = widgetRepository.findOne(widgetId);

			try {
				if (null != widgetEntity) {
					deleteId(widgetEntity,authDetailsVo);
					widgetEntity.setDeleteFlag(CommonConstant.FLAG_ONE);
					// It is used to delete the record in database.
					widgetRepository.save(widgetEntity);

				}

			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new CommonException(getMessage("dataFailure",authDetailsVo));
			}

		}

	}

	@Transactional
	public void deleteId(WidgetEntity widgetEntity,AuthDetailsVo authDetailsVo) throws CommonException {

		try {

			List<WidgetDetailEntity> widgetDetailEntityList = null;
			Integer entityId = authDetailsVo.getEntityId();
			try {
				widgetDetailEntityList = widgetDetailRepository.findDetailList(widgetEntity.getWidgetId(), entityId);
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new CommonException(getMessage("dataFailure",authDetailsVo));
			}

			for (WidgetDetailEntity widgetDetailEntity : widgetDetailEntityList) {
				widgetDetailEntity.setDeleteFlag(CommonConstant.FLAG_ONE);
				try {
					widgetDetailRepository.save(widgetDetailEntity);
				} catch (Exception e) {
					logger.error(e.getMessage());
					throw new CommonException(getMessage("dataFailure",authDetailsVo));
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

	}

	public WidgetVO attachmentDownload(WidgetVO widgetVo,AuthDetailsVo authDetailsVo) throws CommonException {

		WidgetVO widgetVoList = new WidgetVO();
		WidgetEntity widgetEntity = null;
		try {
			widgetEntity = widgetRepository.findOne(widgetVo.getWidgetId());

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
		BeanUtils.copyProperties(widgetEntity, widgetVoList);

		return widgetVoList;
	}

	@Transactional
	public WidgetDetailVO findWidgetDetail(WidgetDetailVO widgetDetailVo) {
		File file;
		WidgetDetailEntity widgetDetailEntity = new WidgetDetailEntity();

		widgetDetailEntity = widgetDetailRepository.findDetailLoad(widgetDetailVo.getWidgetDetailId(),
				widgetDetailVo.getWidgetId());
		WidgetDetailVO widgetDetailVoLoad = new WidgetDetailVO();

		BeanUtils.copyProperties(widgetDetailEntity, widgetDetailVoLoad);

		if (widgetDetailEntity.getWidgetDetailPicPath() != null) {
			file = new File(widgetDetailEntity.getWidgetDetailPicPath());
			widgetDetailVoLoad.setWidgetDetailPicPath(file.getName());
		}
		if (widgetDetailEntity.getWidgetDetailAttPath() != null) {
			file = new File(widgetDetailEntity.getWidgetDetailAttPath());
			widgetDetailVoLoad.setWidgetDetailAttPath(file.getName());
		}

		return widgetDetailVoLoad;
	}

	@Transactional
	public List<WidgetVO> getDateDetail(WidgetDetailVO widgetDetailVo,AuthDetailsVo authDetailsVo) throws CommonException, IOException {
		List<WidgetVO> widgetVoList = new ArrayList<WidgetVO>();
		List<WidgetEntity> widgetEntityList = null;
		//Integer entityId = authDetailsVo.getEntityId();
		try {
			if (widgetDetailVo.getWidgetDetailAnnouncementDate() != null) {

				//SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				//String date = formatter.format(widgetDetailVo.getWidgetDetailAnnouncementDate());

				widgetEntityList = widgetDAO.getWidgetHeader(widgetDetailVo,authDetailsVo);
			} else if (widgetDetailVo.getWidgetDetailValidFrom() != null
					&& widgetDetailVo.getWidgetDetailValidTo() != null) {

				//SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				//String fromDate = formatter.format(widgetDetailVo.getWidgetDetailValidFrom());
				//String toDate = formatter.format(widgetDetailVo.getWidgetDetailValidTo());
				widgetEntityList = widgetDAO.fetchData(widgetDetailVo,authDetailsVo);

			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

		if (widgetEntityList != null && widgetEntityList.size() != 0) {

			widgetVoList = getWidgetDetail(widgetEntityList, widgetDetailVo,authDetailsVo);
			return widgetVoList;

		}
		return widgetVoList;

	}

	@Transactional
	public List<WidgetVO> getWidgetDetail(List<WidgetEntity> list_WidgetEntityList, WidgetDetailVO widgetDetailVo,AuthDetailsVo authDetailsVo)
			throws CommonException, IOException {
		List<WidgetVO> widgetVoList = new ArrayList<WidgetVO>();

		//Integer entityId = authDetailsVo.getEntityId();
		for (WidgetEntity widgetEntity : list_WidgetEntityList) {
			WidgetVO widgetVo = new WidgetVO();
			BeanUtils.copyProperties(widgetEntity, widgetVo);

			/*StringBuffer modifiedQuery = new StringBuffer(picturePath.getWidgetDownloadPath());
			if (null != widgetEntity.getWidgetIcon()) {
				File file = new File(widgetEntity.getWidgetIcon());

				modifiedQuery.append(file.getName());
				widgetVo.setWidgetIcon(modifiedQuery.toString());

			}*/
			
			try {
				// image loading
				if (null != widgetEntity.getWidgetIcon()) {
					widgetVo.setWidgetIconImage(imageLoading(widgetEntity.getWidgetIcon()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			List<WidgetDetailVO> widgetDetailVoList = new ArrayList<WidgetDetailVO>();
			List<WidgetDetailEntity> widgetDetailEntityList = new ArrayList<WidgetDetailEntity>();

			try {

				//SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				/*String date = formatter.format(widgetDetailVo.getWidgetDetailAnnouncementDate());
				String fromDate = formatter.format(widgetDetailVo.getWidgetDetailValidFrom());
				String toDate = formatter.format(widgetDetailVo.getWidgetDetailValidTo());*/
				if (widgetDetailVo.getWidgetDetailAnnouncementDate() != null) {
					widgetDetailEntityList = widgetDAO.getWidgetDetail(widgetEntity.getWidgetId(), widgetDetailVo,authDetailsVo);
				} else if (widgetDetailVo.getWidgetDetailValidFrom() != null
						&& widgetDetailVo.getWidgetDetailValidTo() != null) {
					widgetDetailEntityList = widgetDAO.getWidgetDetailEntityList(widgetEntity.getWidgetId(),
							widgetDetailVo,authDetailsVo);

				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new CommonException(getMessage("dataFailure",authDetailsVo));
			}

			for (WidgetDetailEntity widgetDetailEntity : widgetDetailEntityList) {

				WidgetDetailVO detailVo = new WidgetDetailVO();
				BeanUtils.copyProperties(widgetDetailEntity, detailVo);

				if(null!=widgetDetailEntity.getWidgetDetailPicPath()){
					detailVo.setWidgetDetailPicPath(widgetDetailEntity.getWidgetDetailPicPath());
				}
				
				/*
				 * StringBuffer widgetDetail = new
				 * StringBuffer(picturePath.getWidgetDownloadPath()); if (null
				 * != widgetDetailEntity.getWidgetDetailPicPath()) { File file =
				 * new File(widgetDetailEntity.getWidgetDetailPicPath());
				 * 
				 * widgetDetail.append(file.getName());
				 * DetailVo.setWidgetDetailPicPath(widgetDetail.toString()); }
				 */

				/*StringBuffer allFile = new StringBuffer();
				if (null != widgetDetailEntity.getWidgetDetailPicPath()) {
					
					String name = widgetDetailEntity.getWidgetDetailPicPath();
					String[] pic = name.split(",");
					for (int i = 0; i < pic.length; i++) {
						File file = new File(pic[i]);
						allFile.append(file.getName());
						allFile.append(',');

						detailVo.setWidgetDetailPicPath(allFile.toString());
					}
				//	allFile.deleteCharAt(allFile.length() - 1);
					detailVo.setWidgetDetailPicPath(allFile.toString());
				}*/
				
				
				
				if(null != detailVo.getWidgetDetailPicPath()){
					String[] picValue=detailVo.getWidgetDetailPicPath().split(",");			 
				
				byte[][] picpath =new byte[picValue.length][];
				
				for(int i = 0; i < picValue.length; i++){
					try {
						if (null != picValue[i]) {
							picpath[i]=imageLoading(picValue[i]);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				detailVo.setWidgetDetailPicPathImageLoad(picpath);
				}
				
				if(null != detailVo.getWidgetDetailAttPath()){
				String[] attValue=detailVo.getWidgetDetailAttPath().split(",");				
				
				byte[][] attpath =new byte[attValue.length][];
				
				for(int i = 0; i < attValue.length; i++){
					try {
						if (null != attValue[i]) {
							attpath[i]=imageLoading(attValue[i]);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				detailVo.setWidgetDetailAttPathImageLoad(attpath);
				}
				/*StringBuffer attachFile = new StringBuffer();
				if (null != widgetDetailEntity.getWidgetDetailAttPath()) {
					String name = widgetDetailEntity.getWidgetDetailAttPath();
					String[] pic = name.split(",");
					for (int i = 0; i < pic.length; i++) {
						File file = new File(pic[i]);
						attachFile.append(file.getName());
						attachFile.append(',');

						DetailVo.setWidgetDetailAttPath(attachFile.toString());
					}
					attachFile.deleteCharAt(attachFile.length() - 1);
					DetailVo.setWidgetDetailAttPath(attachFile.toString());
				}*/

				widgetDetailVoList.add(detailVo);
			}
			widgetVo.setWidgetDetailVoList(widgetDetailVoList);

			widgetVoList.add(widgetVo);

		}
		return widgetVoList;

	}

	public byte[] imageLoading(String fileName) throws IOException {
		BufferedImage originalImage;
		byte[] imageInByte;
		originalImage = ImageIO.read(new File(fileName));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(originalImage, getMediaType1(fileName) , baos);
		baos.flush();
		imageInByte = baos.toByteArray();
		return imageInByte;
	}
	
	public WidgetVO getScreenFields(ScreenAuthenticationVO screenAuthorizationMaster) {

		WidgetVO widgetVo = new WidgetVO();

		/*
		 * ScreenAuthorizationMaster screenAuthorizationMasterVo =
		 * getScreenAuhorization(screenAuthorizationMaster); if (null !=
		 * screenAuthorizationMasterVo) {
		 * 
		 * // Get the Fields List
		 * widgetVo.setScreenFieldDisplayVoList(screenAuthorizationMasterVo.
		 * getScreenFieldDisplayVoList());
		 * 
		 * // Get the Functions & Side Tab List
		 * widgetVo.setScreenFunctionDisplayList(screenAuthorizationMasterVo.
		 * getScreenFunctionDisplayList());
		 * 
		 * } else { throw new CommonException(userMessages.
		 * getNoAuthorizationAvailableForThisUser());
		 * 
		 * }
		 * 
		 * ScreenAuthenticationMaster screenAuthenticationMaster =
		 * getScreenAuhentication();
		 * 
		 * if (null != screenAuthenticationMaster) {
		 * widgetVo.setScreenVoList(screenAuthenticationMaster.getScreenVoList()
		 * );
		 * 
		 * } else { throw new
		 * CommonException(getMessage("noScreenAvailableForThisUser"));
		 * 
		 * }
		 * 
		 * widgetVo.setUserName(authDetailsVo.getUserName());
		 */

		return widgetVo;
	}

	@Transactional
	public List<WidgetVO> getAllHR(AuthDetailsVo authDetailsVo) throws CommonException {
		List<WidgetEntity> widgetEntityList = null;
		Integer entityId = authDetailsVo.getEntityId();
		try {

			// It is used to get all records from database.
			widgetEntityList = widgetRepository.getAllHR(entityId);

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
		// It is used to convert all record from Entity to Vo.
		List<WidgetVO> widgetVoList = getAllList(widgetEntityList);

		return widgetVoList;
	}

	@Transactional
	public WidgetDetailVO attachmentDownload(WidgetDetailVO widgetDetailVo,AuthDetailsVo authDetailsVo) throws CommonException {
		WidgetDetailVO widgetDetail = new WidgetDetailVO();
		WidgetDetailEntity widgetDetailEntity = null;

		try {

			widgetDetailEntity = widgetDetailRepository.findOne(widgetDetailVo.getWidgetDetailId());

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
		BeanUtils.copyProperties(widgetDetailEntity, widgetDetail);

		if(null != widgetDetailEntity.getWidgetDetailAttPath() && 
				!widgetDetailEntity.getWidgetDetailAttPath().isEmpty()){
			try {
				if(null != widgetDetailEntity.getWidgetDetailAttPath()){
					String[] attValue=widgetDetailEntity.getWidgetDetailAttPath().split(",");				 
					
					byte[][] attpath =new byte[attValue.length][];
					
					for(int i = 0; i < attValue.length; i++){
						try {
							if (null != attValue[i]) {
								attpath[i]=imageLoading(attValue[i]);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					widgetDetail.setWidgetDetailAttPathDownload(attpath);
					}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return widgetDetail;

	}
}
