package com.srm.rta.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
import com.srm.rta.config.RTAPicturePath;
import com.srm.rta.service.WidgetService;
import com.srm.rta.validation.WidgetDetailValidation;
import com.srm.rta.validation.WidgetValidation;
import com.srm.rta.vo.WidgetDetailVO;
import com.srm.rta.vo.WidgetVO;

@RestController
public class WidgetController extends CommonController<WidgetVO> {

	@Autowired
	WidgetValidation widgetValidation;
	
	@Autowired
	WidgetService widgetService;

	@Autowired
	WidgetDetailValidation widgetDetailValidation;

	@Autowired
	RTAPicturePath picturePath;

	private static final Logger logger = LogManager.getLogger(WidgetController.class);

	@PostMapping(FilePathConstants.WIDGET_LIST)
	@ResponseBody
	public ResponseEntity<JSONResponse> getAll(HttpServletRequest request,@RequestBody ScreenJsonVO screenJson) {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				// get from message.properties
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			CommonVO commonVO = widgetService.getScreenFields(screenJson,authDetailsVo);
			List<WidgetVO> widgetVoList = widgetService.getAll(authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("successMessage",authDetailsVo));
			jsonResponse.setSuccesObject(widgetVoList);
			jsonResponse.setAuthSuccesObject(commonVO);
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			e.printStackTrace();
			logger.error("error", e);
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
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}

	@PostMapping(FilePathConstants.WIDGET_SEARCH)
	@ResponseBody
	public ResponseEntity<JSONResponse> getAllSearch(HttpServletRequest request,@RequestBody WidgetVO widgetVo) {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			List<WidgetVO> widgetVoList = widgetService.getAllSearch(widgetVo,authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("successMessage",authDetailsVo));
			jsonResponse.setSuccesObject(widgetVoList);
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
		} catch (Exception e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}

	@PostMapping(FilePathConstants.WIDGET_ADD)
	@ResponseBody
	public ResponseEntity<JSONResponse> addScreenFields(HttpServletRequest request,@RequestBody WidgetVO widgetVo) {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			CommonVO commonVO = widgetService.getScreenFields(widgetVo.getScreenJson(),authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("successMessage",authDetailsVo));
			jsonResponse.setSuccesObject(commonVO);
			jsonResponse.setAuthSuccesObject(commonVO);
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
		} catch (Exception e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}

	@RequestMapping(value = FilePathConstants.WIDGET_CREATE, method = RequestMethod.POST, consumes = {
			"multipart/form-data" })
	@ResponseBody
	public ResponseEntity<JSONResponse> saveAttachment(HttpServletRequest request,@RequestParam("file") MultipartFile[] uploadingFiles,
			@RequestParam("file1") MultipartFile[] uploadingFiles1,
			@RequestParam("file2") MultipartFile[] uploadingFiles2, @RequestParam("action") String str) {
		WidgetVO widgetVo = new WidgetVO();
		ObjectMapper mapper = new ObjectMapper();
		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			widgetVo = mapper.readValue(str, WidgetVO.class);

			String result = widgetValidation.validateCreate(widgetVo);

			for (MultipartFile uploadedFile : uploadingFiles) {
				// File size Validation
				fileSizeValidation(uploadedFile);

			}
			for (MultipartFile uploadedFile : uploadingFiles1) {
				// File size Validation
				fileSizeValidation(uploadedFile);

			}
			for (MultipartFile uploadedFile : uploadingFiles2) {
				// File size Validation
				fileSizeValidation(uploadedFile);

			}
			if (!result.equals(CommonConstant.VALIDATION_SUCCESS)) {
				jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
				jsonResponse.setResponseMessage(result);
				return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
			}
			if (widgetVo != null && widgetVo.getWidgetDetailVoList() != null) {
				result = widgetDetailValidation.validateCreate(widgetVo);
			}
			if (!result.equals(CommonConstant.VALIDATION_SUCCESS)) {
				jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
				jsonResponse.setResponseMessage(result);
				return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
			}
			result = widgetDetailValidation.getFileExtension(uploadingFiles1);

			if (!result.equals(CommonConstant.VALIDATION_SUCCESS)) {
				jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
				jsonResponse.setResponseMessage(result);
				return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
			}

			WidgetVO widgetRecord;
			widgetService.findDuplicateIndex(widgetVo,authDetailsVo);
			widgetRecord = widgetService.create(widgetVo, uploadingFiles, uploadingFiles1, uploadingFiles2,authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("saveSuccessMessage",authDetailsVo));
			jsonResponse.setSuccesObject(widgetRecord);
		} catch (CommonException e) {
			e.printStackTrace();
			logger.error("error", e);
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
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("saveErroMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}

		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

	}

	@PostMapping(FilePathConstants.WIDGET_LOAD)
	@ResponseBody
	public ResponseEntity<JSONResponse> load(HttpServletRequest request,@RequestBody WidgetVO widgetVo) {

		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			CommonVO commonVO = widgetService.getScreenFields(widgetVo.getScreenJson(),authDetailsVo);

			String result = widgetValidation.validateLoad(widgetVo);

			if (!result.equals(CommonConstant.VALIDATION_SUCCESS)) {
				jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
				jsonResponse.setResponseMessage(result);
				return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
			}
			WidgetVO widgetViewVo = new WidgetVO();
			widgetViewVo.setWidgetId(widgetVo.getWidgetId());

			widgetViewVo = widgetService.findWidget(widgetViewVo,authDetailsVo);

			jsonResponse.setResponseMessage(getMessage("successMessage",authDetailsVo));
			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setSuccesObject(widgetViewVo);
			jsonResponse.setAuthSuccesObject(commonVO);
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
		} catch (Exception e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("successMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	@RequestMapping(value = FilePathConstants.WIDGET_UPDATE, method = RequestMethod.POST, consumes = {
			"multipart/form-data" })
	@ResponseBody
	ResponseEntity<JSONResponse> updateAttachment(HttpServletRequest request,@RequestParam("file") MultipartFile[] uploadingFiles,
			@RequestParam("file1") MultipartFile[] uploadingFiles1,
			@RequestParam("file2") MultipartFile[] uploadingFiles2, @RequestParam("action") String str) {
		WidgetVO widgetVo = new WidgetVO();
		ObjectMapper mapper = new ObjectMapper();
		JSONResponse jsonResponse = new JSONResponse();

		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			widgetVo = mapper.readValue(str, WidgetVO.class);

			String result = widgetValidation.validateUpdate(widgetVo);

			for (MultipartFile uploadedFile : uploadingFiles) {
				// File size Validation
				fileSizeValidation(uploadedFile);

			}
			for (MultipartFile uploadedFile : uploadingFiles1) {
				// File size Validation
				fileSizeValidation(uploadedFile);

			}
			for (MultipartFile uploadedFile : uploadingFiles2) {
				// File size Validation
				fileSizeValidation(uploadedFile);

			}
			if (!result.equals(CommonConstant.VALIDATION_SUCCESS)) {
				jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
				jsonResponse.setResponseMessage(result);
				return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
			}
			if (widgetVo != null && widgetVo.getWidgetDetailVoList() != null) {

				result = widgetDetailValidation.validateUpdate(widgetVo);
			}
			if (!result.equals(CommonConstant.VALIDATION_SUCCESS)) {
				jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
				jsonResponse.setResponseMessage(result);
				return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
			}
			widgetService.findDuplicateIndex(widgetVo,authDetailsVo);
			WidgetVO widget = widgetService.update(widgetVo, uploadingFiles, uploadingFiles1, uploadingFiles2,authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("updateSuccessMessage",authDetailsVo));
			jsonResponse.setSuccesObject(widget);
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
		} catch (Exception e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("updateErrorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}

		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	@PostMapping(FilePathConstants.WIDGET_DELETE)
	@ResponseBody
	public ResponseEntity<JSONResponse> delete(HttpServletRequest request,@RequestBody WidgetVO widgetVo) {
		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			widgetService.delete(widgetVo,authDetailsVo);
			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("deleteSuccessMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		} catch (CommonException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (LoginAuthException e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.FAILURE_CODE);
			jsonResponse.setResponseMessage(e.getMessage());
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}catch (Exception e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("deleteErrorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}

		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	@GetMapping("/sr/Wid/picdownload/{picture:.+}")
	@ResponseBody
	public ResponseEntity<Resource> getAllPic(@PathVariable String picture) {
		try {

			String filePath = picturePath.getWidgetFilePath() + "/" + picture + "";
			File file = new File(filePath);
			String path = file.getName();

			InputStreamResource fileInputStream = new InputStreamResource(new FileInputStream(file));
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename =\"" + path + "\"")
					.contentLength(file.length()).contentType(getMediaType(file.getName())).body(fileInputStream);

		} catch (Exception e) {
			logger.error("error", e);
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

	@GetMapping("/sr/Wid/attdownload/{attachment:.+}")
	@ResponseBody
	public ResponseEntity<Resource> getAllAtt(@PathVariable String attachment) {

		String filePath = picturePath.getWidgetFilePath() + "/" + attachment + "";
		File file = new File(filePath);
		String path = file.getName();
		try {
			if (file.exists()) {
				InputStreamResource fileInputStream = new InputStreamResource(new FileInputStream(file));
				return ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename =\"" + path + "\"")
						.contentLength(file.length()).contentType(getMediaType(file.getName())).body(fileInputStream);
			}
		} catch (IOException ioe) {
			logger.error("error", ioe);
		} catch (Exception e) {
			logger.error("error", e);
		}
		return null;
	}

	@PostMapping(FilePathConstants.WIDGET_DOWNLOAD)
	@ResponseBody
	public ResponseEntity<Resource> getDownload(HttpServletRequest request,@RequestBody WidgetVO widgetVo) throws CommonException {
		
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
		WidgetVO widgetAttVo = widgetService.attachmentDownload(widgetVo,authDetailsVo);
		File file = new File(widgetAttVo.getWidgetIcon());
		String path = file.getName();
		
			if (file.exists()) {
				InputStreamResource fileInputStream = new InputStreamResource(new FileInputStream(file));
				return ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename =\"" + path + "\"")
						.contentLength(file.length()).contentType(getMediaType(file.getName())).body(fileInputStream);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
			logger.error("error", ioe);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error", e);
		}
		return null;
	}

	@PostMapping(FilePathConstants.WIDGET_LOAD_DETAIL)
	@ResponseBody
	ResponseEntity<JSONResponse> loadDetail(HttpServletRequest request,@RequestBody WidgetDetailVO widgetDetailVo) {
		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			WidgetDetailVO widgetDetailVoForLoad = widgetService.findWidgetDetail(widgetDetailVo);
			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("successMessage",authDetailsVo));
			jsonResponse.setSuccesObject(widgetDetailVoForLoad);
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

		}catch (LoginAuthException e) {
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

	@PostMapping(FilePathConstants.WIDGET_DATE)
	@ResponseBody
	ResponseEntity<JSONResponse> getDateDetail(HttpServletRequest request,@RequestBody WidgetDetailVO widgetDetailVo) {
		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			List<WidgetVO> widgetVoList = widgetService.getDateDetail(widgetDetailVo,authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("successMessage",authDetailsVo));
			jsonResponse.setSuccesObject(widgetVoList);
			return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		} catch (CommonException e) {
			e.printStackTrace();
			logger.error("error", e);
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
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}
		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
	}

	public ResponseEntity<Resource> getAllPicture(@RequestBody WidgetDetailVO widgetDetailVo) {

		File file = new File(widgetDetailVo.getWidgetDetailPicPath());
		try {
			if (file.exists()) {
				InputStreamResource fileInputStream = new InputStreamResource(new FileInputStream(file));
				return ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename =\"" + file + "\"")
						.contentLength(file.length()).contentType(getMediaType(file.getName())).body(fileInputStream);
			}
		} catch (IOException ioe) {
			logger.error("error", ioe);
		} catch (Exception e) {
			logger.error("error", e);
		}
		return null;
	}

	@PostMapping(FilePathConstants.WIDGET_LIST_HR)
	@ResponseBody
	public ResponseEntity<JSONResponse> getAllAdmin(HttpServletRequest request,@RequestBody ScreenJsonVO screenJson) {
		JSONResponse jsonResponse = new JSONResponse();
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		
		try {
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}

			CommonVO commonVO = widgetService.getScreenFields(screenJson,authDetailsVo);

			List<WidgetVO> widgetVoList = widgetService.getAllHR(authDetailsVo);

			jsonResponse.setResponseCode(CommonConstant.SUCCESS_CODE);
			jsonResponse.setResponseMessage(getMessage("successMessage",authDetailsVo));
			jsonResponse.setSuccesObject(widgetVoList);
			jsonResponse.setAuthSuccesObject(commonVO);
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
		}catch (Exception e) {
			logger.error("error", e);
			jsonResponse.setResponseCode(CommonConstant.ERROR_CODE);
			jsonResponse.setResponseMessage(getMessage("errorMessage",authDetailsVo));
			jsonResponse.setSuccesObject(CommonConstant.NULL);
		}

		return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
		
	}

	@PostMapping(FilePathConstants.WIDGET_PIC_ATTACH_DOWNLOAD)
	@ResponseBody
	public ResponseEntity<byte[]> pictureDownload(HttpServletRequest request,@RequestBody WidgetDetailVO widgetDetailVo,
			HttpServletResponse response) throws CommonException {

		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		
		
			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
		WidgetDetailVO widgetDetVo = new WidgetDetailVO();
		widgetDetVo = widgetService.attachmentDownload(widgetDetailVo,authDetailsVo);
		String name = widgetDetVo.getWidgetDetailPicPath();
		String[] pic = name.split(",");
		List<File> listFile = new ArrayList<>();

		for (int i = 0; i < pic.length; i++) {
			File input = new File(pic[i]);
			listFile.add(input);
		}
		byte[] bytes = zipFiles(listFile);
		HttpHeaders headers = new HttpHeaders();
		headers.setCacheControl(CacheControl.noCache().getHeaderValue());
		response.setHeader("Pragma", "public");
		response.setHeader("Expires", "0");
		response.setHeader("Cache - Control", "must - revalidate, post - check = 0, pre - check = 0");
		response.setHeader("Content - type", "application - download");
		response.setHeader("Content - Disposition", "attachment; filename = WidgetPictures "
				+ new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".zip");
		response.setHeader("Content - Transfer - Encoding", "binary");
		headers.setContentDispositionFormData("Content - Disposition", "attachment; filename = -WidgetPictures "
				+ new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".zip");
		headers.setLastModified(Calendar.getInstance().getTime().getTime());
		headers.setCacheControl("no-cache");
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		if (bytes != null) {
			headers.setContentLength(bytes.length);
		}
		ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(bytes, headers, HttpStatus.OK);
		return responseEntity;
	}

	public byte[] zipFiles(List<File> files) {

		byte[] result = null;

		try (ByteArrayOutputStream fos = new ByteArrayOutputStream();
				ZipOutputStream zipOut = new ZipOutputStream(fos);) {
			for (File fileToZip : files) {
				try (FileInputStream fis = new FileInputStream(fileToZip);) {
					ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
					zipOut.putNextEntry(zipEntry);

					IOUtils.copy(fis, zipOut);
				}
			}
			zipOut.close();
			fos.close();

			result = fos.toByteArray();
		} catch (Exception ex) {
			logger.error("error", ex);

		}

		return result;
	}

	@PostMapping(FilePathConstants.WIDGET_ATTACHMENT_DOWNLOAD)
	@ResponseBody
	public ResponseEntity<Resource> attachDownload(HttpServletRequest request,
			@RequestBody WidgetDetailVO widgetDetailVo, HttpServletResponse response) throws CommonException {
		String accessToken = getHeaderAccessToken(request);
		AuthDetailsVo authDetailsVo = null;
		try {

			authDetailsVo = tokenValidate(accessToken);
			if (null == authDetailsVo) {
				throw new LoginAuthException("Token Expired / Already Logined");
			}
			WidgetDetailVO widgetDetVo = widgetService.attachmentDownload(widgetDetailVo, authDetailsVo);
			if(null != widgetDetVo.getWidgetDetailAttPath()){
			File file = new File(widgetDetVo.getWidgetDetailAttPath());

			String path = file.getName();		 

			if (file.exists()) {
				InputStreamResource fileInputStream = new InputStreamResource(new FileInputStream(file));
				return ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename =\"" + path + "\"")
						.contentLength(file.length()).contentType(getMediaType(file.getName())).body(fileInputStream);
			}
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		} catch (LoginAuthException e) {
			logger.error("error", e);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}
		
}
		/*List<File> listFile = new ArrayList<>();
		if(null != widgetDetVo.getWidgetDetailAttPath()){
		String name = widgetDetVo.getWidgetDetailAttPath();
		String[] pic = name.split(",");
		for (int i = 0; i < pic.length; i++) {
			File input = new File(pic[i]);
			listFile.add(input);
		}
		}
		byte[] bytes = zipFiles(listFile);
		HttpHeaders headers = new HttpHeaders();
		headers.setCacheControl(CacheControl.noCache().getHeaderValue());
		response.setHeader("Pragma", "public");
		response.setHeader("Expires", "0");
		response.setHeader("Cache - Control", "must - revalidate, post - check = 0, pre - check = 0");
		response.setHeader("Content - type", "application - download");
		response.setHeader("Content - Disposition", "attachment; filename = WidgetAttachment "
				+ new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".zip");
		response.setHeader("Content - Transfer - Encoding", "binary");
		headers.setContentDispositionFormData("Content - Disposition", "attachment; filename = WidgetAttachment"
				+ new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".zip");
		headers.setLastModified(Calendar.getInstance().getTime().getTime());
		headers.setCacheControl("no-cache");
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		if (bytes != null) {
			headers.setContentLength(bytes.length);
		}
		ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(bytes, headers, HttpStatus.OK);
		return responseEntity;*/


