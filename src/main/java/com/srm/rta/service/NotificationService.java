package com.srm.rta.service;

import javax.transaction.Transactional;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.constants.FilePathConstants;
import com.srm.coreframework.response.JSONResponse;
import com.srm.coreframework.service.CommonService;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.rta.vo.EmailMessageVo;
import com.srm.rta.vo.EmailVo;

@Service
public class NotificationService extends CommonService {

	org.slf4j.Logger logger = LoggerFactory.getLogger(NotificationService.class);
	
	@Value("${restTemplateUrl}")
	private String restTemplateUrl;
	
	@Value("${emailRestTemplateUrl}")
	private String emailRestTemplateUrl;
		
	@Autowired
	private RestTemplate restTemplate;
	
 
	@Transactional
	public ResponseEntity<JSONResponse> getAlllMailNotification(AuthDetailsVo authDetailsVo) {
		JSONResponse response = new JSONResponse();

		try {
			EmailVo emailVo = new EmailVo();
			emailVo.setEntityId(authDetailsVo.getEntityId());
			emailVo.setToUserId(authDetailsVo.getUserId());

			// rest template for email
			String url = emailRestTemplateUrl + FilePathConstants.RT_NOTIFICATION_LIST;
			response = restTemplate.postForObject(url, emailVo, JSONResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure", authDetailsVo));
		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	/**
	 * 
	 * @param id
	 *            int
	 */
	@Transactional
	public ResponseEntity<JSONResponse>  deleteNotification(EmailMessageVo emailMessageVo,AuthDetailsVo authDetailsVo) {
		JSONResponse response = new JSONResponse();
		try {					

			emailMessageVo.setToUserId(authDetailsVo.getUserId());
			emailMessageVo.setUserLang(authDetailsVo.getLangCode());
			// rest template for email
			String url = emailRestTemplateUrl + FilePathConstants.RT_DELETE_NOTIFICATION;
			response = restTemplate.postForObject(url, emailMessageVo, JSONResponse.class);
						 			
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("modifyFailure",authDetailsVo));
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
