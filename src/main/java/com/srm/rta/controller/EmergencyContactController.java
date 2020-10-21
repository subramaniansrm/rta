package com.srm.rta.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.config.LoginAuthException;
import com.srm.coreframework.constants.FilePathConstants;
import com.srm.coreframework.controller.CommonController;
import com.srm.coreframework.response.JSONResponse;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.coreframework.vo.CommonVO;
import com.srm.coreframework.vo.ScreenJsonVO;
import com.srm.rta.service.EmergencyContactService;
import com.srm.rta.vo.EmergencyContactVO;

@RestController
public class EmergencyContactController extends CommonController<EmergencyContactVO>{

	@Autowired
	EmergencyContactService emergencyContactService;

	

	private static final Logger logger = LogManager.getLogger(EmergencyContactController.class);

	@PostMapping(FilePathConstants.EMERGENCY_CONTACT_GETALL)
	@ResponseBody
	public ResponseEntity<JSONResponse> getAll(HttpServletRequest request,@RequestBody ScreenJsonVO screenJson) {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			CommonVO commonVO = emergencyContactService.getScreenFields(screenJson,authDetailsVo);
			List<EmergencyContactVO> emergencyContactVoList = new ArrayList<EmergencyContactVO>();
		 					
			if (commonVO != null) {
				emergencyContactVoList = emergencyContactService.getAll(authDetailsVo);
			}
			 
			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(emergencyContactVoList);
			jsonResponse.setAuthSuccesObject(commonVO);
			jsonResponse.setResponseMessage(getMessage("successMessage",authDetailsVo));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}

		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}

	@PostMapping(FilePathConstants.EMERGENCY_EXCEL_UPLOAD)
	@ResponseBody
	public ResponseEntity<JSONResponse> create(HttpServletRequest request,@RequestParam("file") MultipartFile[] uploadingFiles,
			@RequestParam("action") String str) throws IOException {
		JSONResponse jsonResponse = new JSONResponse();

		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			ObjectMapper mapper = new ObjectMapper();
			for (MultipartFile uploadedFile : uploadingFiles) {
				// File size Validation
				fileSizeValidation(uploadedFile);

			}
			EmergencyContactVO emergencyContactVo = mapper.readValue(str, EmergencyContactVO.class);
			emergencyContactService.saveAttachment(emergencyContactVo, uploadingFiles,authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(emergencyContactVo);
			jsonResponse.setResponseMessage(getMessage("saveSuccessMessage",authDetailsVo));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}  catch (Exception e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	@PostMapping(FilePathConstants.EMERGENCY_EXCEL_UPDATE)
	@ResponseBody
	public ResponseEntity<JSONResponse> update(HttpServletRequest request,@RequestParam("file") MultipartFile[] uploadingFiles,
			@RequestParam("action") String str) throws IOException {
		JSONResponse jsonResponse = new JSONResponse();

		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			ObjectMapper mapper = new ObjectMapper();

			EmergencyContactVO emergencyContactVo = mapper.readValue(str, EmergencyContactVO.class);
			emergencyContactService.update(emergencyContactVo, uploadingFiles,authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(emergencyContactVo);
			jsonResponse.setResponseMessage(getMessage("updateSuccessMessage",authDetailsVo));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}

		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}

	@PostMapping(FilePathConstants.EMERGENCY_EXCEL_DOWNLOAD)
	@ResponseBody
	public ResponseEntity<InputStreamResource> getDownload(HttpServletRequest request,@RequestBody EmergencyContactVO emergencyContactVo) throws CommonException	{

		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
		EmergencyContactVO emergencyContactViewVo = emergencyContactService.attachmentDownload(emergencyContactVo,authDetailsVo);
		File file = new File(emergencyContactViewVo.getEmergencyContactPath());
		
			if (file.exists()) {
				InputStreamResource fileInputStream = new InputStreamResource(new FileInputStream(file));
				return ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename =\"" + file + "\"")
						.contentLength(file.length()).contentType(getMediaType(file.getName())).body(fileInputStream);
			}
		} catch (Exception e) {
			logger.error("error",e);
		}
		return null;

	}

	/**
	 * Method used to get Media Type of files
	 * 
	 * @param filename
	 * @return
	 */
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

	@SuppressWarnings("rawtypes")
	@PostMapping(FilePathConstants.EMERGENCY_CONTACT_DELETE)
	@ResponseBody
	public ResponseEntity delete(HttpServletRequest request,@RequestBody EmergencyContactVO emergencyContactVo) {
		JSONResponse jsonResponse = new JSONResponse();

		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			emergencyContactService.delete(emergencyContactVo,authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(emergencyContactVo);
			jsonResponse.setResponseMessage(getMessage("deleteSuccessMessage",authDetailsVo));
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			logger.error("error",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);

		} catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			logger.error("error",e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}

		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}

	@PostMapping(FilePathConstants.EMERGENCY_CONTACT_VIEW)
	@ResponseBody
	public ResponseEntity<JSONResponse> load(HttpServletRequest request,@RequestBody EmergencyContactVO emergencyContactVo) {

		JSONResponse jsonResponse = new JSONResponse();

		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			CommonVO commonVO = emergencyContactService.getScreenFields(emergencyContactVo.getScreenJson(),authDetailsVo);

			EmergencyContactVO emergencyContactViewVo = new EmergencyContactVO();
			if (emergencyContactVo != null) {
				emergencyContactViewVo.setEmergencyContactPathId(emergencyContactVo.getEmergencyContactPathId());
			}

			emergencyContactViewVo = emergencyContactService.load(emergencyContactViewVo,authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(emergencyContactViewVo);
			jsonResponse.setAuthSuccesObject(commonVO);
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			logger.error("error",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			logger.error("error",e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}

		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}

	@PostMapping(FilePathConstants.EMERGENCY_CONTACT_DOWMNLOAD_GETALL)
	@ResponseBody
	public ResponseEntity<JSONResponse> getAllDownload(HttpServletRequest request) {
		JSONResponse jsonResponse = new JSONResponse();

		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			List<EmergencyContactVO> emergencyContactVo = emergencyContactService.getAllDownload(authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(emergencyContactVo);

			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

		} catch (CommonException e) {
			logger.error("error",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}  catch (Exception e) {
			logger.error("error",e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}

		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}

	@PostMapping(FilePathConstants.EMERGENCY_CONTACT_LOADADD)
	@ResponseBody
	public ResponseEntity<JSONResponse> loadAdd(HttpServletRequest request,@RequestBody EmergencyContactVO emergencyContactVo) {
		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			CommonVO commonVO = emergencyContactService
					.getScreenFields(emergencyContactVo.getScreenJson(),authDetailsVo);
			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("saveSuccessMessage",authDetailsVo));
			jsonResponse.setAuthSuccesObject(commonVO);
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			logger.error("error",e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
		} catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (Exception e) {
			logger.error("error",e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
		}

		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}
}
