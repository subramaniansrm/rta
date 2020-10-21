package com.srm.rta.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.config.LoginAuthException;
import com.srm.coreframework.constants.ControlNameConstants;
import com.srm.coreframework.constants.FilePathConstants;
import com.srm.coreframework.controller.CommonController;
import com.srm.coreframework.exception.CoreException;
import com.srm.coreframework.response.JSONResponse;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.util.ValidationUtil;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.coreframework.vo.CommonVO;
import com.srm.coreframework.vo.ScreenJsonVO;
import com.srm.rta.service.ExternalLinkService;
import com.srm.rta.vo.ExternalLinkVO;

@RestController
public class ExternalLinkController extends CommonController<ExternalLinkController> {

	private static final Logger logger = LogManager.getLogger(ExternalLinkController.class);

	@Autowired
	ExternalLinkService externalLinkService;
	
	/**
	 * 
	 * @param screenAuthorizationMaster
	 * @return
	 * @throws CoreException
	 * @throws CommonException
	 */
	@PostMapping(FilePathConstants.EXTERNALLINK_GETALL)
	@ResponseBody
	public ResponseEntity<JSONResponse> getAll(HttpServletRequest request,@RequestBody ScreenJsonVO screenJson)
			throws CoreException, CommonException {
		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			CommonVO commonVO = externalLinkService.getScreenFields(screenJson,authDetailsVo);
			List<ExternalLinkVO> externalLinkVoList = new ArrayList<ExternalLinkVO>();
			if (commonVO != null) {
				externalLinkVoList = externalLinkService.getAllList(authDetailsVo);
			}

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("successMessage",authDetailsVo));
			jsonResponse.setSuccesObject(externalLinkVoList);
			jsonResponse.setAuthSuccesObject(commonVO);
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

		} catch (CommonException e) {

			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(getMessage(e.getMessage(),authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);

		}catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(getMessage(e.getMessage(),authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}

		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}

	@PostMapping(FilePathConstants.EXTERNALLINK_ADD)
	@ResponseBody
	public ResponseEntity<JSONResponse> addScreenFields(HttpServletRequest request,@RequestBody ExternalLinkVO externalLinkVo)
			throws CoreException, CommonException {
		JSONResponse jsonResponse = new JSONResponse();

		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			CommonVO commonVO = externalLinkService.getScreenFields(externalLinkVo.getScreenJson(),authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("successMessage",authDetailsVo));
			jsonResponse.setAuthSuccesObject(commonVO);

			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {

			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(getMessage(e.getMessage(),authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);

		}catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(getMessage(e.getMessage(),authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}

		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}

	@PostMapping(FilePathConstants.EXTERNALLINK_LOAD)
	@ResponseBody
	public ResponseEntity<JSONResponse> load(HttpServletRequest request,@RequestBody ExternalLinkVO externalLinkVo)
			throws CoreException, CommonException {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			CommonVO commonVO = externalLinkService.getScreenFields(externalLinkVo.getScreenJson(),authDetailsVo);

			ExternalLinkVO externalLinkViewVo = new ExternalLinkVO();

			externalLinkViewVo = externalLinkService.load(externalLinkVo,authDetailsVo);
			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("successMessage",authDetailsVo));
			jsonResponse.setSuccesObject(externalLinkViewVo);
			jsonResponse.setAuthSuccesObject(commonVO);

			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {

			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(getMessage(e.getMessage(),authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);

		}
		catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(getMessage(e.getMessage(),authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} 
		catch (Exception e) {
			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}

		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}

	@PostMapping(FilePathConstants.EXTERNALLINK_SEARCH)
	@ResponseBody
	public ResponseEntity<JSONResponse> search(HttpServletRequest request,@RequestBody ExternalLinkVO externalLinkVo)
			throws CoreException, CommonException {
		JSONResponse jsonResponse = new JSONResponse();

		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			List<ExternalLinkVO> externalLinkVoList = externalLinkService.search(externalLinkVo,authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("successMessage",authDetailsVo));
			jsonResponse.setSuccesObject(externalLinkVoList);

			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {

			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(getMessage(e.getMessage(),authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);

		}
		catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(getMessage(e.getMessage(),authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} 
		catch (Exception e) {
			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}

		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}

	@PostMapping(FilePathConstants.EXTERNALLINK_DELETE)
	@ResponseBody
	public ResponseEntity<JSONResponse> delete(HttpServletRequest request,@RequestBody ExternalLinkVO externalLinkVo)
			throws CoreException, CommonException {
		JSONResponse jsonResponse = new JSONResponse();

		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			externalLinkService.delete(externalLinkVo,authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("deleteSuccessMessage",authDetailsVo));
			jsonResponse.setSuccesObject(null);

			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {

			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(getMessage(e.getMessage(),authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);

		}
		catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(getMessage(e.getMessage(),authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} 
		catch (Exception e) {
			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}

		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}

	@RequestMapping(value = FilePathConstants.EXTERNALLINK_CREATE, method = RequestMethod.POST, consumes = {
			"multipart/form-data" })
	@ResponseBody
	public ResponseEntity<JSONResponse> save(HttpServletRequest request,@RequestParam("file") MultipartFile[] uploadingFiles,
			@RequestParam("action") String str)
			throws CoreException, CommonException, JsonParseException, JsonMappingException, IOException {
		JSONResponse jsonResponse = new JSONResponse();

		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			for (MultipartFile uploadedFile : uploadingFiles) {
				// File size Validation
				fileSizeValidation(uploadedFile);

			}
			ExternalLinkVO externalLinkVo = new ExternalLinkVO();
			ObjectMapper mapper = new ObjectMapper();
			externalLinkVo = mapper.readValue(str, ExternalLinkVO.class);
			saveValidate(externalLinkVo,uploadingFiles,authDetailsVo);
			externalLinkService.findDuplicate(externalLinkVo, authDetailsVo);
			externalLinkService.create(externalLinkVo, uploadingFiles,authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("saveSuccessMessage",authDetailsVo));
			jsonResponse.setSuccesObject(null);

			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {

			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(getMessage(e.getMessage(),authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);

		}
		catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(getMessage(e.getMessage(),authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} 
		catch (Exception e) {
			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}

		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}

	@RequestMapping(value = FilePathConstants.EXTERNALLINK_UPDATE, method = RequestMethod.POST, consumes = {
			"multipart/form-data" })
	@ResponseBody
	public ResponseEntity<JSONResponse> updateAttachment(HttpServletRequest request,@RequestParam("file") MultipartFile[] uploadingFiles,
			@RequestParam("action") String str)
			throws CoreException, CommonException, JsonParseException, JsonMappingException, IOException {
		JSONResponse jsonResponse = new JSONResponse();

		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			for (MultipartFile uploadedFile : uploadingFiles) {
				// File size Validation
				fileSizeValidation(uploadedFile);

			}
			ExternalLinkVO externalLinkVo = new ExternalLinkVO();
			ObjectMapper mapper = new ObjectMapper();
			externalLinkVo = mapper.readValue(str, ExternalLinkVO.class);
			saveValidate(externalLinkVo,uploadingFiles,authDetailsVo);
			externalLinkService.findDuplicate(externalLinkVo, authDetailsVo);
			externalLinkService.update(externalLinkVo, uploadingFiles,authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("updateSuccessMessage",authDetailsVo));
			jsonResponse.setSuccesObject(null);

			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {

			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(getMessage(e.getMessage(),authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);

		}
		catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(getMessage(e.getMessage(),authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} 
		catch (Exception e) {
			logger.error(e.getMessage());
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}

		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}

	@PostMapping(FilePathConstants.EXTERNALLINK_DOWNLOAD)
	@ResponseBody
	public ResponseEntity<Resource> getDownload(HttpServletRequest request, HttpServletResponse response,
			@RequestBody ExternalLinkVO externalLinkVo)
			throws CommonException, IllegalAccessException, InvocationTargetException {

		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
		externalLinkVo = externalLinkService.attachmentDownload(externalLinkVo,authDetailsVo);
		if(null != externalLinkVo.getExternalLinkLogo()){
		File file = new File(externalLinkVo.getExternalLinkLogo());
			if (file.exists()) {
				InputStreamResource fileInputStream = new InputStreamResource(new FileInputStream(file));
				return ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename =\"" + file + "\"")
						.contentLength(file.length()).contentType(getMediaType(file.getName())).body(fileInputStream);
			}
		}
		} catch (IOException ioe) {
			ioe.printStackTrace();
			logger.error(ioe.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return null;
	}

	public MediaType getMediaType(String filename) {

		String arr[] = filename.split("\\.");
		String type = arr[arr.length - CommonConstant.CONSTANT_ONE];
		switch (type.toLowerCase()) {
		case "txt":
			return MediaType.TEXT_PLAIN;
		case "png":
			return MediaType.IMAGE_PNG;
		case "jpg":
			return MediaType.IMAGE_JPEG;
		default:
			return MediaType.APPLICATION_OCTET_STREAM;
		}

	}
	/**
	 * Method used to validate external Link Details
	 * 
	 * @param externalLinkVo
	 * @param uploadingFiles 
	 */

	private void saveValidate(ExternalLinkVO externalLinkVo, MultipartFile[] uploadingFiles,AuthDetailsVo authDetailsVo) {



		// Iterate the fields in Screen Field Display List
		for (String field : externalLinkVo.getScreenFieldDisplayVoList()) {

			// External Link Name Valiation
			if (field.equals(ControlNameConstants.EXTERNAL_LINK_NAME)) {

				if (ValidationUtil.isNullOrBlank(externalLinkVo.getExternalLinkName().trim())) {
					throw new CommonException(getMessage("externalLink_name_validation",authDetailsVo));
				}

				if (externalLinkVo.getExternalLinkName().length() > 50) {
					throw new CommonException(getMessage("externalLink_name_length_validation",authDetailsVo));
				}
			}

			// External Link URL Validation
			if (field.equals(ControlNameConstants.EXTERNAL_LINK_URL)) {

				if (ValidationUtil.isWebsite(externalLinkVo.getExternalLinkUrl())) {
					throw new CommonException(getMessage("externalLink_url_validation",authDetailsVo));
				}
				
				if (externalLinkVo.getExternalLinkUrl().length() > 500) {
					throw new CommonException(getMessage("externalLink_name_length_validation",authDetailsVo));
				}
			}

			if (field.equals(ControlNameConstants.EXTERNAL_LINK_DISPLAY_SEQUENCE)) {

				if (externalLinkVo.getExternalLinkDisplaySeq() > 99) {
					throw new CommonException(getMessage("externalLink_displaySeq_validation",authDetailsVo));
				}
			}
			
			
			if (field.equals(ControlNameConstants.EXTERNAL_LINK_LOGO )) {
				if(ValidationUtil.isImage(uploadingFiles))	{
					throw new CommonException(getMessage("image_val",authDetailsVo));

				}
				
			}

		}

	
		
		

	}
}
