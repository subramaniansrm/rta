package com.srm.rta.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.config.LoginAuthException;
import com.srm.coreframework.constants.FilePathConstants;
import com.srm.coreframework.controller.CommonController;
import com.srm.coreframework.response.JSONResponse;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.util.ValidationUtil;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.rta.service.ProfileEditService;
import com.srm.rta.vo.ProfileEditVO;

@RestController
public class ProfileEditController extends CommonController<ProfileEditVO> {

	Logger logger = LoggerFactory.getLogger(DropdownController.class);
	
	@Autowired
	ProfileEditService profileEditService;

	@PostMapping(FilePathConstants.PROFILE_EDIT_VIEW)
	@ResponseBody
	public ResponseEntity<JSONResponse> load(HttpServletRequest request) throws IOException {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				// get from message.properties
				throw new LoginAuthException("Token Expired / Already Logined");
			}
		
			ProfileEditVO profileEdit = profileEditService.load(authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(profileEdit);
			jsonResponse.setResponseMessage(getMessage("successMessage",authDetailsVo));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}

	@RequestMapping(value = FilePathConstants.PROFILE_EDIT_UPDATE, method = RequestMethod.PUT, consumes = {
			"multipart/form-data" })
	@ResponseBody
	public ResponseEntity<JSONResponse> updateAttachment(HttpServletRequest request,@RequestParam("file") MultipartFile[] uploadingFiles,
			@RequestParam("action") String str) throws IOException {

		JSONResponse jsonResponse = new JSONResponse();
		ProfileEditVO profileEditVo = new ProfileEditVO();
		ObjectMapper mapper = new ObjectMapper();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				// get from message.properties
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			profileEditVo = mapper.readValue(str, ProfileEditVO.class);
			for (MultipartFile uploadedFile : uploadingFiles) {
				// File size Validation
				fileSizeValidation(uploadedFile);

			}
			logger.error(getMessage("processValidation",authDetailsVo));
			this.updateValidation(profileEditVo, uploadingFiles,authDetailsVo);
			logger.error(getMessage("processValidationCompleted",authDetailsVo));

			profileEditService.updateAttachment(profileEditVo, uploadingFiles,authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(CommonConstant.NULL);
			jsonResponse.setResponseMessage(getMessage("saveSuccessMessage",authDetailsVo));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}

	private void updateValidation(ProfileEditVO profileEditVo, MultipartFile[] uploadingFiles,AuthDetailsVo authDetailsVo) {

		// First Name Validation
		if (ValidationUtil.isNullOrBlank(profileEditVo.getFirstName().trim())) {
			throw new CommonException(getMessage("user.firstName.required",authDetailsVo));
		}
		if (ValidationUtil.onlyAlphabets(profileEditVo.getFirstName())) {
			throw new CommonException(getMessage("profileEdit.firstName.alphabets",authDetailsVo));
		}
		if ((profileEditVo.getFirstName().length() > 255)) {
			throw new CommonException(getMessage("user.firstName.limit",authDetailsVo));
		}

		// Last Name Validation
		if (ValidationUtil.isNullOrBlank(profileEditVo.getLastName().trim())) {
			throw new CommonException(getMessage("user.lasttName.required",authDetailsVo));
		}
		if (ValidationUtil.onlyAlphabets(profileEditVo.getLastName())) {
			throw new CommonException(getMessage("profileEdit.lastName.alphabets",authDetailsVo));
		}
		if ((profileEditVo.getLastName().length() > 255)) {
			throw new CommonException(getMessage("user.lasttName.limit",authDetailsVo));
		}

		// Email Validation
		if (ValidationUtil.isNullOrBlank(profileEditVo.getEmailId().trim())) {
			throw new CommonException(getMessage("user.email.required",authDetailsVo));
		}
		if (ValidationUtil.isEmail(profileEditVo.getEmailId())) {
			throw new CommonException(getMessage("user.email.validation",authDetailsVo));
		}
		if ((profileEditVo.getEmailId().length() > 255)) {
			throw new CommonException(getMessage("user.email.limit",authDetailsVo));
		}
		// Image Validation
		if (ValidationUtil.isImage(uploadingFiles)) {
			throw new CommonException(getMessage("image_val",authDetailsVo));

		}

	}
 }
