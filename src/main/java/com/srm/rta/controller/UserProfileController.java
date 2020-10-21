package com.srm.rta.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.config.LoginAuthException;
import com.srm.coreframework.constants.FilePathConstants;
import com.srm.coreframework.controller.CommonController;
import com.srm.coreframework.response.JSONResponse;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.rta.service.UserProfileService;
import com.srm.rta.vo.UserProfileVO;

/**
 * 
 * 
 * @author raathikaabm
 *
 */
@RestController
public class UserProfileController extends CommonController<UserProfileVO> {

	private static final Logger logger = LogManager.getLogger(UserProfileController.class);

	@Autowired
	UserProfileService userProfileService;

	@PostMapping(FilePathConstants.PROFILE_NAME)
	@ResponseBody
	public ResponseEntity<JSONResponse> getProfile(HttpServletRequest request) {
		JSONResponse jsonResponse = new JSONResponse();
		UserProfileVO profileVo = null;
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			 authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				//get from message.properties
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			
			String messageCode = "";
			
			if (authDetailsVo.getRoleId() != 1) {
				// License date validation
				licenseDateValidation(authDetailsVo);

			}
			
			profileVo = userProfileService.getScreenFields(authDetailsVo);
			
			profileVo = userProfileService.loginProfile(profileVo,authDetailsVo);

			if (profileVo.getLastName() != null) {
				profileVo.setFirstName(profileVo.getFirstName().concat(" " + profileVo.getLastName()));

			} else {
				profileVo.setFirstName(profileVo.getFirstName());
			}
			profileVo.setLangCode(authDetailsVo.getLangCode());
			profileVo.setUserId(authDetailsVo.getUserId());
			jsonResponse.setSuccesObject(profileVo);
			
			if (authDetailsVo.getRoleId() != 1) {
				// password expiry validation
				messageCode = passwordExpiryValidationCheck(authDetailsVo, messageCode);

				// first Time Login Exception
				messageCode = firstTimeLoginException(authDetailsVo, messageCode);
			}
								
			if (!messageCode.isEmpty() && messageCode.equals("412")) {
				jsonResponse.setResponseCode("412");
				jsonResponse.setResponseMessage(getMessage("passwordExpired", authDetailsVo));
				jsonResponse.setSuccesObject(profileVo);
			} else if (!messageCode.isEmpty() && messageCode.equals("413")) {
				jsonResponse.setResponseCode("413");
				jsonResponse.setResponseMessage(getMessage("changeFirstTimePassword", authDetailsVo));
				jsonResponse.setSuccesObject(profileVo);
			}else {

				jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
				jsonResponse.setResponseMessage(getMessage("successMessage", authDetailsVo));
				logger.info(getMessage("successMessage", authDetailsVo));
				jsonResponse.setSuccesObject(profileVo);
			}
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

		} catch (CommonException e) {
			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(profileVo);
		} catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_COUNT);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage", authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

}
