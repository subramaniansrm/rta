package com.srm.rta.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.config.PicturePath;
import com.srm.coreframework.repository.UserRepository;
import com.srm.coreframework.service.CommonService;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.coreframework.vo.ScreenAuthenticationVO;
import com.srm.rta.config.RTAPicturePath;
import com.srm.rta.dao.UserProfileDao;
import com.srm.rta.vo.UserProfileVO;

/**
 * 
 * @author raathikaabm
 *
 */
@Service
public class UserProfileService extends CommonService {

	Logger logger = LoggerFactory.getLogger(UserProfileService.class);

	@Autowired
	RTAPicturePath picturePath;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserProfileDao userProfileDao;

	@Autowired
	PicturePath picPath;
	
	@SuppressWarnings("rawtypes")
	@Transactional
	public UserProfileVO loginProfile(UserProfileVO profileVo,AuthDetailsVo authDetailsVo) {
		try {
			Integer entityId = authDetailsVo.getEntityId();
			Integer userId = authDetailsVo.getUserId();

			/*
			 * List<Object> user = userRepository.loginProfile(entityId,
			 * userId);
			 */
			List<Object> user = userProfileDao.getUserDetails(entityId, userId);

			Iterator itr = user.iterator();

			while (itr.hasNext()) {
				Object[] obj = (Object[]) itr.next();

				if (null != obj[0]) {
					/*StringBuffer modifiedQuery = new StringBuffer(picturePath.getUserDownloadPath());
					File file = new File((String) obj[0]);
					modifiedQuery.append(file.getName());*/
					profileVo.setProfile((String) obj[0]);
				}

				if (null != obj[1]) {
					profileVo.setUserId((Integer) obj[1]);

				}

				if (null != obj[2]) {
					profileVo.setFirstName((String) obj[2]);

				}

				if (null != obj[3]) {
					profileVo.setLastName((String) obj[3]);

				}			
				
				if (null != obj[5]) {
					profileVo.setEntityId(Integer.parseInt(String.valueOf(obj[5])));

				}
				
				if (null != obj[4]) {
					profileVo.setEntityName((String) obj[4]);

				}
				
				
				try {
					// image loading
					if (null != profileVo.getProfile()) {
						imageLoading(profileVo);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
				if(null == profileVo.getImageLoad()){
					profileVo.setImageLoad(defaultImage(picPath.getDefaultImagePath()));
				}
				}catch (Exception e) {
						e.printStackTrace();
					}
			}

			return profileVo;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
	}

	private UserProfileVO imageLoading(UserProfileVO userProfileVO) throws IOException {
		BufferedImage originalImage;
		byte[] imageInByte = null;
		
		if(null != userProfileVO.getProfile()){
		originalImage = ImageIO.read(new File(userProfileVO.getProfile()));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(originalImage, getMediaType1(userProfileVO.getProfile()) , baos);
		baos.flush();
		imageInByte = baos.toByteArray();
		userProfileVO.setImageLoad(imageInByte);
		}
		
		return userProfileVO;
	}
	
	public UserProfileVO getScreenFields(AuthDetailsVo authDetailsVo) {

		UserProfileVO profileVo = new UserProfileVO();

		ScreenAuthenticationVO screenAuthenticationMaster = getScreenAuhentication(authDetailsVo);

		if (null != screenAuthenticationMaster) {
			profileVo.setScreenVoList(screenAuthenticationMaster.getScreenVoList());

		} else {
			throw new CommonException(getMessage("noScreenAvailableForThisUser",authDetailsVo));

		}
		
		String fName = "";
		String lName = "";
		
		if(null != authDetailsVo.getFirstName()){
			  fName = authDetailsVo.getFirstName();
		}
		
	   if(null != authDetailsVo.getLastName()){
			  lName =  authDetailsVo.getLastName();
		}
	   
		profileVo.setUserName(fName + " " +lName );

		return profileVo;
	}

}
