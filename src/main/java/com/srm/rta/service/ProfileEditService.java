package com.srm.rta.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.config.PicturePath;
import com.srm.coreframework.entity.EntityLicense;
import com.srm.coreframework.entity.PhoneBookEntity;
import com.srm.coreframework.entity.SubLocation;
import com.srm.coreframework.entity.UserDepartment;
import com.srm.coreframework.entity.UserEntity;
import com.srm.coreframework.entity.UserLocation;
import com.srm.coreframework.repository.PhoneBookRepository;
import com.srm.coreframework.repository.UserRepository;
import com.srm.coreframework.service.CommonService;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.rta.config.RTAPicturePath;
import com.srm.rta.dao.ProfileEditDAO;
import com.srm.rta.vo.ProfileEditVO;

@Service
public class ProfileEditService extends CommonService{

	org.slf4j.Logger logger = LoggerFactory.getLogger(ProfileEditService.class);

	@Autowired
	private ProfileEditDAO profileEditDao;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PhoneBookRepository phoneBookRepository;

	

	@Autowired
	private PicturePath picturePath;
	
	@Autowired
	private RTAPicturePath rtaPicturePath;

	/**
	 * Method is used to Load the Phone Booking Details
	 * 
	 * @param phoneBook
	 *            PhoneBookVo
	 * @return phoneBookVo List<PhoneBookVo>
	 */
	@Transactional
	public ProfileEditVO load(AuthDetailsVo authDetailsVo) {
		try {
			Integer entityId = authDetailsVo.getEntityId();
			Integer userId = authDetailsVo.getUserId();
			ProfileEditVO profileEditVo = new ProfileEditVO();
			List<Object> user = profileEditDao.userProfile(entityId, userId);
			if (null != user) {
				profileEditVo = getAllList(user);
			}

			return profileEditVo;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

	}

	/**
	 * Method is to set the parameters in the phone booking
	 * 
	 * @param object
	 *            Object[]
	 * @return ProfileEditVoList List<ProfileEditVo>
	 * @throws IOException 
	 */
	private ProfileEditVO getAllList(List<Object> user) throws IOException {

		ProfileEditVO profileEditVo = new ProfileEditVO();

		Iterator itr = user.iterator();

		while (itr.hasNext()) {

			Object[] obj = (Object[]) itr.next();

			if (null != obj[0]) {
				profileEditVo.setId((Integer) obj[0]);
			}

			if (null != obj[1]) {
				profileEditVo.setEmployeeId((String) obj[1]);
			}

			if (null != obj[2]) {
				profileEditVo.setFirstName((String) obj[2]);
			}
			if (null != obj[3]) {
				profileEditVo.setMiddleName((String) obj[3]);
			}

			if (null != obj[4]) {
				profileEditVo.setLastName((String) obj[4]);
			}

			if (null != obj[5]) {
				profileEditVo.setSkypeId((String) obj[5]);
			}
			if (null != obj[6]) {
				profileEditVo.setEmailId((String) obj[6]);
			}
			if (null != obj[7]) {
				profileEditVo.setMobile((String) obj[7]);
			}
			if (null != obj[8]) {
				profileEditVo.setCurrentAddress((String) obj[8]);
			}
			if (null != obj[9]) {
				profileEditVo.setPermanentAddress((String) obj[9]);
			}
			if (null != obj[10]) {
				profileEditVo.setLocationId((Integer) obj[10]);
			}
			if (null != obj[11]) {
				profileEditVo.setLocationName((String) obj[11]);
			}
			if (null != obj[12]) {
				profileEditVo.setSublocationId((Integer) obj[12]);
			}
			if (null != obj[13]) {
				profileEditVo.setSubLocationName((String) obj[13]);
			}
			if (null != obj[14]) {
				profileEditVo.setDepartmentId((Integer) obj[14]);
			}
			if (null != obj[15]) {
				profileEditVo.setDepartmentName((String) obj[15]);
			}
			
			if (null != obj[16]) {

				/*StringBuffer modifiedQuery = new StringBuffer(picturePath.getUserDownloadPath());
				File file = new File((String) obj[16]);
				modifiedQuery.append(file.getName());*/
				profileEditVo.setPhoneBookProfile((String) obj[16]);

			}
			try {
				// image loading
				if (null != profileEditVo.getPhoneBookProfile()) {
					imageLoading(profileEditVo);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(null == profileEditVo.getImageLoad()){
				profileEditVo.setImageLoad(defaultImage(picturePath.getDefaultImagePath()));
			}
			if (null != obj[25]) {
				profileEditVo.setLangCode((String) obj[25]);
			}
			if(null != profileEditVo && profileEditVo.getLangCode().equals(CommonConstant.LANG_CODE_EN)){
				profileEditVo.setLangCodeValue(CommonConstant.ENGLISH);
			}else if(null != profileEditVo && profileEditVo.getLangCode().equals(CommonConstant.LANG_CODE_JP)){
				profileEditVo.setLangCodeValue(CommonConstant.JAPANESE);
			}
		}
		return profileEditVo;
	}

	private ProfileEditVO imageLoading(ProfileEditVO profileEditVO) throws IOException {
		BufferedImage originalImage;
		byte[] imageInByte;
		originalImage = ImageIO.read(new File(profileEditVO.getPhoneBookProfile()));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(originalImage, getMediaType1(profileEditVO.getPhoneBookProfile()) , baos);
		baos.flush();
		imageInByte = baos.toByteArray();
		profileEditVO.setImageLoad(imageInByte);
		return profileEditVO;
	}
	
	@Transactional
	public UserEntity saveAttachment(UserEntity userEntity, MultipartFile[] uploadingFiles,AuthDetailsVo authDetailsVo) {

		try {
			for (MultipartFile uploadedFile : uploadingFiles) {

				String fileName = dateAppend(uploadedFile);
				String path = rtaPicturePath.getUserFilePath();
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
				// uploadedFile.getOriginalFilename().replace(uploadedFile.getOriginalFilename(),
				// fileName);
				uploadedFile.transferTo(fileToCreate);
				path = path + fileName;
				userEntity.setUserProfile(path);

			}
		} catch (JsonParseException e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		} catch (JsonMappingException e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
		return userEntity;
	}

	/**
	 * Method used to append Date for Uploaded File
	 * 
	 * @param uploadedFile
	 * @return
	 */
	public String dateAppend(MultipartFile uploadedFile) {
		String fileName = uploadedFile.getOriginalFilename().substring(0,
				uploadedFile.getOriginalFilename().lastIndexOf("."));
		String date = CommonConstant.formatDatetoString(new Date(), CommonConstant.FILE_NAME_FORMAT_DATE);
		fileName = fileName + date;
		String format = "." + getfileFormat(uploadedFile.getOriginalFilename());
		fileName = fileName + format;
		return fileName;

	}

	/**
	 * Method used to Get the File Format
	 * 
	 * @param attachmentFileName
	 * @return
	 */
	public static String getfileFormat(String attachmentFileName) {
		try {
			return attachmentFileName.substring(attachmentFileName.lastIndexOf(".") + 1);
		} catch (Exception e) {
			return "";
		}
	}

	@Transactional
	public void updateAttachment(ProfileEditVO profileEditVo, MultipartFile[] uploadingFiles,AuthDetailsVo authDetailsVo) {
		// TODO Auto-generated method stub
		UserEntity userEntity = new UserEntity();

		List<Object> user = profileEditDao.userProfileUpdate(profileEditVo.getId());

		Iterator itr = user.iterator();

		while (itr.hasNext()) {
			userEntity.setId(profileEditVo.getId());
			Object[] obj = (Object[]) itr.next();
			if (null != profileEditVo.getEmployeeId()) {
				userEntity.setUserEmployeeId((String) obj[0]);
			}
			if (null != profileEditVo.getFirstName()) {
				userEntity.setFirstName((String) obj[1]);
			}
			if (null != profileEditVo.getMiddleName()) {
				userEntity.setMiddleName((String) obj[2]);
			}
			if (null != profileEditVo.getLastName()) {
				userEntity.setLastName((String) obj[3]);
			}
			if (null != profileEditVo.getMobile()) {
				userEntity.setMobile((String) obj[4]);
			}
			if (null != profileEditVo.getCurrentAddress()) {
				userEntity.setCurrentAddress((String) obj[5]);
			}
			if (null != profileEditVo.getPermanentAddress()) {
				userEntity.setPermanentAddress((String) obj[6]);
			}
			if (null != profileEditVo.getEmailId()) {
				userEntity.setEmailId((String) obj[7]);
			}
			if (null != profileEditVo.getSkypeId()) {
				userEntity.setSkypeId((String) obj[8]);
			}

			if (uploadingFiles != null) {
				userEntity = saveAttachment(userEntity, uploadingFiles,authDetailsVo);
			}
			if (null != profileEditVo.getLocationId()) {
				UserLocation userLocationEntity = new UserLocation();
				userLocationEntity.setId(profileEditVo.getLocationId());
				userEntity.setUserLocationEntity(userLocationEntity);
			}
			if (null != profileEditVo.getSublocationId()) {

				SubLocation subLocationEntity = new SubLocation();
				subLocationEntity.setSublocationId(profileEditVo.getSublocationId());
				userEntity.setSubLocationEntity(subLocationEntity);
			}
			if (null != profileEditVo.getDepartmentId()) {
				UserDepartment userDepartmentEntity = new UserDepartment();
				userDepartmentEntity.setId(profileEditVo.getDepartmentId());
				userEntity.setUserDepartmentEntity(userDepartmentEntity);
				// userEntity.getUserDepartmentEntity().setId(profileEditVo.getDepartmentId());
			}
			if(null != profileEditVo.getLangCode()){
				if(profileEditVo.getLangCode().equals(CommonConstant.en)){
					userEntity.setLangCode(CommonConstant.en);
				}else if(profileEditVo.getLangCode().equals(CommonConstant.jp)){
					userEntity.setLangCode(CommonConstant.jp);
				}
			}
		}
		userEntity = setUpdateUserDetails(userEntity,authDetailsVo);

		EntityLicense entityLicenseEntity = new EntityLicense();
		entityLicenseEntity.setId(authDetailsVo.getEntityId());
		userEntity.setEntityLicenseEntity(entityLicenseEntity);

		if(null!=userEntity.getUserProfile()){
			profileEditVo.setPhoneBookProfile(userEntity.getUserProfile());
		}
		//userRepository.save(userEntity);
		profileEditDao.updateSave(profileEditVo,authDetailsVo);
		
		PhoneBookEntity phoneBookEntity = new PhoneBookEntity();

		phoneBookEntity = profileEditDao.findByEmpId(profileEditVo.getEmployeeId(),authDetailsVo);

		if (null != phoneBookEntity) {
			if (null != profileEditVo.getEmployeeId()) {
				phoneBookEntity.setEmployeeId(profileEditVo.getEmployeeId());
			}
			if (null != profileEditVo.getFirstName()) {

				if (null != profileEditVo.getLastName()) {
					phoneBookEntity
							.setEmployeeName(profileEditVo.getFirstName().concat(" " + profileEditVo.getLastName()));
				} else {
					phoneBookEntity.setEmployeeName(profileEditVo.getFirstName());
				}
			}
			if (null != profileEditVo.getMobile()) {
				phoneBookEntity.setPhoneNumber(profileEditVo.getMobile());
			}
			if (null != profileEditVo.getEmailId()) {
				phoneBookEntity.setEmailId(profileEditVo.getEmailId());
			}
			if (null != profileEditVo.getSkypeId()) {
				phoneBookEntity.setSkypeId(profileEditVo.getSkypeId());
			}
			if (userEntity.getUserProfile() != null) {
				phoneBookEntity.setPhoneBookProfile(userEntity.getUserProfile());
			}
			if (null != profileEditVo.getLocationId()) {
				phoneBookEntity.setUserLocationId(profileEditVo.getLocationId());
			}
			if (null != profileEditVo.getSublocationId()) {
				phoneBookEntity.setSublocationId(profileEditVo.getSublocationId());
			}
			if (null != profileEditVo.getDepartmentId()) {
				phoneBookEntity.setUserDepartmentId(profileEditVo.getDepartmentId());
			}
			phoneBookEntity = setUpdateUserDetails(phoneBookEntity,authDetailsVo);

			phoneBookEntity.setEntityLicenseId(authDetailsVo.getEntityId());
			
			phoneBookRepository.save(phoneBookEntity);

		}
	}

	private UserEntity setUpdateUserDetails(UserEntity userEntity,AuthDetailsVo authDetailsVo) {

		userEntity.setUpdateBy(authDetailsVo.getUserId());
		userEntity.setUpdateDate(CommonConstant.getCalenderDate());

		return userEntity;
	}

	private PhoneBookEntity setUpdateUserDetails(PhoneBookEntity phoneBookEntity,AuthDetailsVo authDetailsVo) {

		phoneBookEntity.setUpdateBy(authDetailsVo.getUserId());
		phoneBookEntity.setUpdateDate(CommonConstant.getCalenderDate());

		return phoneBookEntity;
	}

}
