package com.srm.rta.service;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.constants.FilePathConstants;
import com.srm.coreframework.controller.CommonController;
import com.srm.coreframework.dao.EntityDao;
import com.srm.coreframework.entity.EmailGeneralDetailsEntity;
import com.srm.coreframework.entity.EntityLicenseDetails;
import com.srm.coreframework.response.JSONResponse;
import com.srm.coreframework.service.EmailGeneralDetailsService;
import com.srm.coreframework.service.EmailService;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.coreframework.vo.EmailVo;
import com.srm.coreframework.vo.ScreenJsonVO;
import com.srm.coreframework.vo.UserEntityMappingVo;
import com.srm.coreframework.vo.UserMasterVO;
import com.srm.rta.dao.UserDao;
import com.srm.rta.vo.RequestVO;

/**
 * @author priyankas
 *
 */
@Service
public class UserEntityRestTemplateService extends CommonController<RequestVO> {

	@Value("${passwordExpiryCount}")
	private Integer passwordExpiryCount;
	
	@Value("${entityExpiryCount}")
	private Integer entityExpiryCount;
		
	@Value("${transactionExpiryCount}")
	private Integer transactionExpiryCount;
		
	@Value("${restTemplateUrl}")
	private String restTemplateUrl;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	UserDao userDao;
		
	@Autowired
	EmailGeneralDetailsService emailGeneralDetailsService;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	EntityDao entityDao;
		
	/**
	 * @param screenJsonVO
	 * @return ResponseEntity
	 */
	public ResponseEntity<JSONResponse> loadEntity( ScreenJsonVO screenJsonVO){
		JSONResponse response =new JSONResponse();
		try{
			
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_ENTITY;
			response = restTemplate.postForObject(url, screenJsonVO, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	/**
	 * @param screenJsonVO
	 * @return ResponseEntity
	 */
	public ResponseEntity<JSONResponse> saveEntity( UserEntityMappingVo userEntityMappingVo){
		JSONResponse response =new JSONResponse();
		try{
			
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_ENTITY_SAVE;
			response = restTemplate.postForObject(url, userEntityMappingVo, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	/**
	 * @param screenJsonVO
	 * @return ResponseEntity
	 */
	public ResponseEntity<JSONResponse> deleteEntity(@RequestBody UserEntityMappingVo userEntityMappingVo){
		JSONResponse response =new JSONResponse();
		try{
			
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_USER_ENTITY_DELETE;
			response = restTemplate.postForObject(url, userEntityMappingVo, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	/**
	 * @param screenJsonVO
	 * @return ResponseEntity
	 */
	public ResponseEntity<JSONResponse> listEntity( ScreenJsonVO screenJson){
		JSONResponse response =new JSONResponse();
		try{
			
			String url = restTemplateUrl + FilePathConstants.REST_TEMPLATE_ENTITY_LIST;
			response = restTemplate.postForObject(url, screenJson, JSONResponse.class);
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	@Transactional
	public void userPasswordExpiry() {

		try {
			List<Object[]> userList = userDao.getUserList();

			for (Object user : userList) {

				EmailVo emailVo = new EmailVo();

				Integer expiryDays = 0;
				if (null != (Integer) ((Object[]) user)[3]) {
					expiryDays = (Integer) ((Object[]) user)[3];
				}

				if (null != (String) ((Object[]) user)[4]) {
					emailVo.setUserName((String) ((Object[]) user)[4]);
				}

				if (null != (String) ((Object[]) user)[5]) {
					emailVo.setToUserAddress((String) ((Object[]) user)[5]);
				}

				if (null != (Integer) ((Object[]) user)[6]) {
					emailVo.setEntityId((Integer) ((Object[]) user)[6]);
				}

				if (null != (String) ((Object[]) user)[7]) {
					emailVo.setUserLang((String) ((Object[]) user)[7]);
				}

				if (null !=  ((Object[]) user)[1]) {

					String oldChangePwdDate = CommonConstant.formatDatetoString(
							(Date) ((Object[]) user)[1],
							"yyyy-MM-dd");	

					if (oldChangePwdDate.equals(getformatDate(expiryDays))) {

						sendMail(emailVo, oldChangePwdDate);
					}

				} else {
					String oldChangePwdDate = CommonConstant.formatDatetoString(
							(Date) ((Object[]) user)[2],
							"yyyy-MM-dd");					 					

					if (oldChangePwdDate.equals(getformatDate(expiryDays))) {

						sendMail(emailVo, oldChangePwdDate);
					}
				}
			}
		} catch (CommonException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	public String getformatDate(Integer expiryDays){
					
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_WEEK, -(expiryDays-passwordExpiryCount));
		Date result = cal.getTime();
		String	toDate = formatter.format(result);
		return toDate;
	}
	
	public void sendMail(EmailVo emailVo, String oldChangePwdDate) {

		try {
			emailVo.setTrEmailFlag(CommonConstant.CONSTANT_ONE);

			AuthDetailsVo authDetVo = new AuthDetailsVo();
			authDetVo.setEntityId(emailVo.getEntityId());
			emailVo.setSystemConfigurationVo(getSystemConfigurationDetails(authDetVo));

			if (null != emailVo.getUserLang() && emailVo.getUserLang().equals(CommonConstant.en)) {
				authDetVo.setLangCode(CommonConstant.en);
			} else {
				authDetVo.setLangCode(CommonConstant.jp);
			}

			emailVo.setGroupId(CommonConstant.EXPD);
			EmailGeneralDetailsEntity emailGeneralDetailsEntity = emailGeneralDetailsService
					.getEmailGeneralDet(emailVo, authDetVo);
			emailGeneralDetailsService.getEmailGeneralDetails(emailVo, emailGeneralDetailsEntity);

			emailVo.setMessageContent(getMessage("dear", authDetVo) + " " + emailVo.getUserName() + "\n"
					+ emailVo.getMessageContent() + ":" + oldChangePwdDate + " \n \n \n"
					+ getMessage("bestRegards", authDetVo) + " \n" + getMessage("supportTeam", authDetVo));

			if (null != emailVo.getSystemConfigurationVo()&& emailVo.getSystemConfigurationVo().size() > 0) {
				String url = restTemplateUrl + FilePathConstants.PASSWORD_EXPIRY;
				JSONResponse result = restTemplate.postForObject(url, emailVo, JSONResponse.class);
			}
			
		} catch (CommonException e) {			 
			e.printStackTrace();
		} catch (Exception e) {			 
			e.printStackTrace();
		}
	}
	
	@Transactional
	public void entityRenewAlert() {

		try {
			List<Object[]> userList = userDao.getRenewEntityList();
			String entityToDate = "";
			for (Object user : userList) {
				UserMasterVO userMaster = new UserMasterVO();
				EmailVo emailVo = new EmailVo();

				if (null != (Integer) ((Object[]) user)[0]) {
					emailVo.setEntityId((Integer) ((Object[]) user)[0]);
					userMaster = userDao.getUser((Integer) ((Object[]) user)[0]);
				}

				if (null != (String) ((Object[]) user)[3]) {
					emailVo.setUserLang((String) ((Object[]) user)[3]);
				}
						
				if (null != (String) ((Object[]) user)[4]) {
					emailVo.setEntityName((String) ((Object[]) user)[4]);
				}
				
				if (null != userMaster && null != userMaster.getEmailId()) {
					emailVo.setToUserAddress(userMaster.getEmailId());
				}

				if (null != userMaster && null != userMaster.getFirstName()) {
					emailVo.setUserName(userMaster.getFirstName());
				}

				if (null != ((Object[]) user)[2]) {
				  entityToDate = CommonConstant.formatDatetoString((Date) ((Object[]) user)[2], "yyyy-MM-dd");
				}
				String toDate = getEntityFormatDate(entityExpiryCount);

				if (entityToDate.equals(toDate)) {

					sendEntityMail(emailVo, entityToDate);
				}

			}
		} catch (CommonException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	 
	public String getEntityFormatDate(Integer entityExpiryCount){
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_WEEK, +entityExpiryCount);
		Date result = cal.getTime();
		String	toDate = formatter.format(result);
		return toDate;
	}
	
	
	public void sendEntityMail(EmailVo emailVo, String expiryDate ) {

		try {
			emailVo.setTrEmailFlag(CommonConstant.CONSTANT_ONE);

			AuthDetailsVo authDetVo = new AuthDetailsVo();
			authDetVo.setEntityId(emailVo.getEntityId());
			emailVo.setSystemConfigurationVo(getSystemConfigurationDetails(authDetVo));

			if (null != emailVo.getUserLang() && emailVo.getUserLang().equals(CommonConstant.en)) {
				authDetVo.setLangCode(CommonConstant.en);
			} else {
				authDetVo.setLangCode(CommonConstant.jp);
			}

			emailVo.setGroupId(CommonConstant.RNW);
			EmailGeneralDetailsEntity emailGeneralDetailsEntity = emailGeneralDetailsService
					.getEmailGeneralDet(emailVo, authDetVo);
			emailGeneralDetailsService.getEmailGeneralDetails(emailVo, emailGeneralDetailsEntity);

			emailVo.setMessageContent(getMessage("dear", authDetVo) + " " + emailVo.getUserName() + "\n"
					+ emailVo.getMessageContent() + ":" + emailVo.getEntityName()
					+ "\n"+getMessage("renewalDateMsg", authDetVo) + " "+ expiryDate + " \n \n \n"
					+ getMessage("bestRegards", authDetVo) + " \n" + getMessage("supportTeam", authDetVo));

			
			if (null != emailVo.getSystemConfigurationVo()&& emailVo.getSystemConfigurationVo().size() > 0) {

				String url = restTemplateUrl + FilePathConstants.ENTITY_EXPIRY;
				JSONResponse result = restTemplate.postForObject(url, emailVo, JSONResponse.class);
			}
										
		} catch (CommonException e) {			 
			e.printStackTrace();
		} catch (Exception e) {			 
			e.printStackTrace();
		}
	}
	
	
	@Transactional
	public void entityTransactionAlert() {

		try {
			List<Object[]> userList = userDao.getRenewTransactionEntityList();
			int detTransactionCount = 0;
			int totalTransactionCount = 0;
			
			for (Object user : userList) {
				UserMasterVO userMaster = new UserMasterVO();
				EmailVo emailVo = new EmailVo();

				if (null != (Integer) ((Object[]) user)[0]) {
					emailVo.setEntityId((Integer) ((Object[]) user)[0]);
					userMaster = userDao.getUser((Integer) ((Object[]) user)[0]);
				}

				if (null != (String) ((Object[]) user)[3]) {
					emailVo.setUserLang((String) ((Object[]) user)[3]);
				}
						
				if (null != (String) ((Object[]) user)[4]) {
					emailVo.setEntityName((String) ((Object[]) user)[4]);
				}
				
				if (null != userMaster && null != userMaster.getEmailId()) {
					emailVo.setToUserAddress(userMaster.getEmailId());
				}

				if (null != userMaster && null != userMaster.getFirstName()) {
					emailVo.setUserName(userMaster.getFirstName());
				}

				if (null != (Integer) ((Object[]) user)[6]) {
					 totalTransactionCount = (Integer) ((Object[]) user)[6];
					
				}
				
				if (null != ((Object[]) user)[5]) {				  
					//decrypt count
					detTransactionCount =Integer.parseInt(decrypt((String)((Object[]) user)[5]));				
				}
				 
				calculateTransactionCount(detTransactionCount,emailVo,totalTransactionCount);

			}
		} catch (CommonException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void calculateTransactionCount(Integer detTransactionCount, EmailVo emailVo,
			Integer totalTransactionCount) {
	 
		try {

			List<Object[]> logList = userDao.getEntityLogList(emailVo.getEntityId());

			int usedTransactionCount = calculateNewTransactionCount(logList);

			validateTransactionCount(detTransactionCount, usedTransactionCount, totalTransactionCount, emailVo);

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		 
	}
	
	public static String decrypt(String value) throws Exception {
		byte[] decodedBytes = Base64.getDecoder().decode(value);
		return new String(decodedBytes);
	}
	
	
	public int calculateNewTransactionCount(List<Object[]> logList) {
		 
		int count = 0;
		int usedTransactionCount = 0;

		try {

			if (logList.size() > 0) {
				for (Object obj : logList) {

					if (count == 1) {

						if (null != (String) ((Object[]) obj)[1]) {
							usedTransactionCount = Integer.parseInt(decrypt((String) ((Object[]) obj)[1]));
						}						 
					}
					count = count + 1;
				}
			} else {
				for (Object obj : logList) {
					if (null != (String) ((Object[]) obj)[1]) {
						usedTransactionCount = Integer.parseInt(decrypt((String) ((Object[]) obj)[1]));
					}								 
				}
			}			
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return usedTransactionCount;
	}
	
	public void validateTransactionCount(int detTransactionCount,int usedTransactionCount,int totalTransactionCount,EmailVo emailVo){

		int usedCount = 0;
		if (0 != detTransactionCount) {
			int differencecount = ((detTransactionCount - usedTransactionCount));
			  usedCount = totalTransactionCount - differencecount;

			if (usedCount <= transactionExpiryCount) {
				sendTransactionEntityRenewMail(emailVo,usedCount);	
			}
		}					
	}
	
	public void sendTransactionEntityRenewMail(EmailVo emailVo, int detTransactionCount ) {

		try {
			emailVo.setTrEmailFlag(CommonConstant.CONSTANT_ONE);

			AuthDetailsVo authDetVo = new AuthDetailsVo();
			authDetVo.setEntityId(emailVo.getEntityId());
			emailVo.setSystemConfigurationVo(getSystemConfigurationDetails(authDetVo));

			if (null != emailVo.getUserLang() && emailVo.getUserLang().equals(CommonConstant.en)) {
				authDetVo.setLangCode(CommonConstant.en);
			} else {
				authDetVo.setLangCode(CommonConstant.jp);
			}

			emailVo.setGroupId(CommonConstant.TRNW);
			EmailGeneralDetailsEntity emailGeneralDetailsEntity = emailGeneralDetailsService
					.getEmailGeneralDet(emailVo, authDetVo);
			emailGeneralDetailsService.getEmailGeneralDetails(emailVo, emailGeneralDetailsEntity);

			emailVo.setMessageContent(getMessage("dear", authDetVo) + " " + emailVo.getUserName() + "\n"
					+ emailVo.getMessageContent() + ":" + emailVo.getEntityName()
					+ "\n"+getMessage("usedTrnCountMsg", authDetVo) + " "+ detTransactionCount + " \n \n \n"
					+ getMessage("bestRegards", authDetVo) + " \n" + getMessage("supportTeam", authDetVo));

			
			if (null != emailVo.getSystemConfigurationVo()&& emailVo.getSystemConfigurationVo().size() > 0) {

				String url = restTemplateUrl + FilePathConstants.TRN_ENTITY_EXPIRY;
				JSONResponse result = restTemplate.postForObject(url, emailVo, JSONResponse.class);
			}
										
		} catch (CommonException e) {			 
			e.printStackTrace();
		} catch (Exception e) {			 
			e.printStackTrace();
		}
	}
}
