package com.srm.rta.service;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.icu.util.StringTokenizer;
import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.config.MailMessages;
import com.srm.coreframework.entity.UserEntity;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.rta.dao.MailControllerDao;
import com.srm.rta.entity.MailParameterEntity;
import com.srm.rta.entity.MaillogEntity;
import com.srm.rta.validation.MailInstance;
import com.srm.rta.vo.MailOutVo;
import com.srm.rta.vo.MessageMasterVo;
import com.srm.rta.vo.RequestDetailVO;
import com.srm.rta.vo.RequestVO;

/**
 * This service is used to configurated to send the mail.
 * 
 * @author vigneshs
 *
 */
@Component
public class MailControllerService {

	@Autowired
	private MailMessages mailMessages;

	@Autowired
	MailControllerDao mailControllerDao;

	@Autowired
	private RequestService requestService;

	/**
	 * This method is used to generate the mail based on the switch case wise.
	 * 
	 * @param mailParameterEntity
	 * @return mailOutVo MailOutVo
	 * @throws Exception 
	 * @throws CommonException 
	 */
	@SuppressWarnings("rawtypes")
	@Transactional
	public MailOutVo generateMail(MailParameterEntity mailParameterEntity) throws CommonException, Exception {

		MailOutVo mailOutVo = new MailOutVo();

		if (null != mailParameterEntity) {
			List<Object> resultList = (List<Object>) mailControllerDao.generateMail(mailParameterEntity);
			if (null != resultList) {
				Iterator itr = resultList.iterator();

				List<String> toAddress = new ArrayList<>();
				// List<String> ccAddress = new ArrayList<>();

				String sendToAddress = null;

				AuthDetailsVo authDetailsVo = new AuthDetailsVo();
				RequestVO requestVo = new RequestVO();

				if (0 != mailParameterEntity.getRequestId()) {
					requestVo.setRequestId(mailParameterEntity.getRequestId());
					requestVo = requestService.findRequest(requestVo, authDetailsVo);
					
				}

				while (itr.hasNext()) {

					Object[] obj = (Object[]) itr.next();

					/* if (obj.length > CommonConstant.CONSTANT_ZERO) { */

					if (null != obj[0]) {
						mailOutVo.setMessage(String.valueOf(obj[0]));
					}
					if (null != obj[1]) {
						mailOutVo.setTitle(String.valueOf(obj[1]));
					}
					if (String.valueOf(obj[2]).equals(CommonConstant.STRING_ONE)) {

						sendToAddress = String.valueOf(obj[3]);

						StringTokenizer stringTokenizer = new StringTokenizer(sendToAddress, ",");

						while (stringTokenizer.hasMoreTokens()) {

							String sendTo = (String) stringTokenizer.nextToken();

							switch (sendTo) {
							case "RAISED":
								if (null != mailParameterEntity.getMessage()) {
									if (mailParameterEntity.getMessage().equals(CommonConstant.REQ)) {

										UserEntity userEntity = null;

										if (0 != mailParameterEntity.getUserId()) {

											userEntity = mailControllerDao
													.getUserEmailId(mailParameterEntity.getUserId());

											if (null != userEntity && null != userEntity.getEmailId()) {
												toAddress.add(userEntity.getEmailId());
											}
										}

										if (null != toAddress && !toAddress.isEmpty()) {

											mailOutVo.setTitle(mailMessages.getName() + " " + userEntity.getFirstName()
													+ " , " + mailMessages.getRequestCode() + " "
													+ requestVo.getRequest().getRequestCode() + " - "
													+ mailOutVo.getTitle());
											
											
											if (null != requestVo) {
												if (null != requestVo.getRequest().getRequestCode()) {
													mailOutVo.setMessage(
															mailOutVo.getMessage().concat(mailMessages.getRequestCode()
																	+ " " + requestVo.getRequest().getRequestCode()));
												}
												if(null != requestVo.getRequest() && null != requestVo.getRequest().getCurrentStatusName()){
													mailOutVo.setMessage(mailOutVo.getMessage().concat("\n"+"Status :"+ ""+requestVo.getRequest().getCurrentStatusName()+"\n"));
												}
												if (null != requestVo.getRequest().getRequestSubject()) {
													mailOutVo
															.setMessage(mailOutVo.getMessage()
																	.concat(" "
																			+ mailMessages.getAnd() + " " + mailMessages
																					.getRequestSubject()
																			+ " " + requestVo.getRequest()
																					.getRequestSubject()));
												}

											}
											
											mailOutVo.setMessage(mailOutVo.getMessage()
													.concat(" " + mailMessages.getRequestRaise()));

											mailOutVo.setMessage(mailOutVo.getMessage()
													.concat(" \n \n \n " + mailMessages.getBestRegards()));
											mailOutVo.setMessage(mailOutVo.getMessage()
													.concat(" \n " + mailMessages.getSupportTeam()));

											mailOutVo.setToMailAddress(toAddress);

											try {

												this.sendMail(mailOutVo, mailParameterEntity,userEntity);
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
									}
								}
								break;
							case "APPROVAL":
								if (null != mailParameterEntity.getMessage()) {
									if (mailParameterEntity.getMessage().equals(CommonConstant.APL)) {

										UserEntity userEntity = null;

										if (0 != mailParameterEntity.getUserId()) {

											userEntity = mailControllerDao
													.getUserEmailId(mailParameterEntity.getUserId());

											if (null != userEntity && null != userEntity.getEmailId()) {
												toAddress.add(userEntity.getEmailId());
											}
										}

										if (null != toAddress && !toAddress.isEmpty()) {

											mailOutVo.setTitle(mailMessages.getName() + " " + userEntity.getFirstName()
													+ " , " + mailMessages.getRequestCode() + " "
													+ requestVo.getRequest().getRequestCode() + " - "
													+ mailOutVo.getTitle());
											if(null != requestVo.getRequest() && null != requestVo.getRequest().getCurrentStatusName()){
												mailOutVo.setMessage(mailOutVo.getMessage().concat("\n"+"Status :"+ ""+requestVo.getRequest().getCurrentStatusName()+"\n"));
											}
											mailOutVo.setMessage(
													mailMessages.getDear().concat(" " + userEntity.getFirstName() + ","
															+ " \n " + " " + mailOutVo.getMessage()));

											if (null != requestVo.getRequest()) {

												if (null != requestVo.getRequest().getRequestCode()) {
													mailOutVo
															.setMessage(
																	mailOutVo.getMessage()
																			.concat(" \n " + mailMessages
																					.getRequestCode() + " "
																			+ requestVo.getRequest().getRequestCode()));
												}
												if (null != requestVo.getRequest().getRequestTypeName()) {
													mailOutVo
															.setMessage(mailOutVo.getMessage()
																	.concat(" \n " + mailMessages
																			.getRequestType() + " "
																	+ requestVo.getRequest().getRequestTypeName()));
												}
												if (null != requestVo.getRequest().getRequestSubject()) {
													mailOutVo
															.setMessage(mailOutVo.getMessage()
																	.concat(" \n " + mailMessages
																			.getRequestSubject() + " "
																	+ requestVo.getRequest().getRequestSubject()));
												}
												if (null != requestVo.getRequest().getRequestSubTypeName()) {
													mailOutVo
															.setMessage(mailOutVo.getMessage()
																	.concat(" \n " + mailMessages
																			.getRequestSubType() + " "
																	+ requestVo.getRequest().getRequestSubTypeName()));
												}
											}
											mailOutVo.setMessage(mailOutVo.getMessage()
													.concat(" \n " + mailMessages.getRequestDetail()));

											if (null != requestVo.getRequestDetailList()) {

												for (RequestDetailVO requestDetailVo : requestVo
														.getRequestDetailList()) {
													if(null != requestDetailVo
															.getRequestScreenDetailConfigurationFieldName()
															&& null != requestDetailVo
															.getRequestScreenDetailConfigurationFieldValue()){
													mailOutVo.setMessage(mailOutVo.getMessage()
															.concat(" \n " + requestDetailVo
																	.getRequestScreenDetailConfigurationFieldName()
															+ " : " + requestDetailVo
																	.getRequestScreenDetailConfigurationFieldValue()));
													}

												}
											}

											mailOutVo.setMessage(mailOutVo.getMessage()
													.concat(" \n \n \n " + mailMessages.getBestRegards()));
											mailOutVo.setMessage(mailOutVo.getMessage()
													.concat(" \n " + mailMessages.getSupportTeam()));

											mailOutVo.setToMailAddress(toAddress);

											try {

												this.sendMail(mailOutVo, mailParameterEntity,userEntity);
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
									}
								}
								break;

							case "RESOLVER":
								if (null != mailParameterEntity.getMessage()) {
									if (mailParameterEntity.getMessage().equals(CommonConstant.RSL)) {

										UserEntity userEntity = null;

										if (0 != mailParameterEntity.getUserId()) {

											userEntity = mailControllerDao
													.getUserEmailId(mailParameterEntity.getUserId());

											if (null != userEntity && null != userEntity.getEmailId()) {
												toAddress.add(userEntity.getEmailId());
											}
										}

										if (null != toAddress && !toAddress.isEmpty()) {

											mailOutVo.setTitle(mailMessages.getName() + " " + userEntity.getFirstName()
													+ " , " + mailMessages.getRequestCode() + " "
													+ requestVo.getRequest().getRequestCode() + " - "
													+ mailOutVo.getTitle());
											if(null != requestVo.getRequest() && null != requestVo.getRequest().getCurrentStatusName()){
												mailOutVo.setMessage(mailOutVo.getMessage().concat("\n"+"Status :"+ ""+requestVo.getRequest().getCurrentStatusName()+"\n"));
											}
											mailOutVo.setMessage(
													mailMessages.getDear().concat(" " + userEntity.getFirstName() + ","
															+ " \n " + " " + mailOutVo.getMessage()));

											if (null != requestVo.getRequest()) {

												if (null != requestVo.getRequest().getRequestCode()) {
													mailOutVo
															.setMessage(mailOutVo.getMessage()
																	.concat(" \n " + mailMessages.getRequestCode()
																			+ "  "
																			+ requestVo.getRequest().getRequestCode()));
												}
												if (null != requestVo.getRequest().getRequestTypeName()) {
													mailOutVo
															.setMessage(mailOutVo.getMessage()
																	.concat(" \n " + mailMessages
																			.getRequestType() + " "
																	+ requestVo.getRequest().getRequestTypeName()));
												}
												if (null != requestVo.getRequest().getRequestSubject()) {
													mailOutVo
															.setMessage(mailOutVo.getMessage()
																	.concat(" \n " + mailMessages
																			.getRequestSubject() + " "
																	+ requestVo.getRequest().getRequestSubject()));
												}
												if (null != requestVo.getRequest().getRequestSubTypeName()) {
													mailOutVo
															.setMessage(mailOutVo.getMessage()
																	.concat(" \n " + mailMessages
																			.getRequestSubType() + " "
																	+ requestVo.getRequest().getRequestSubTypeName()));
												}
											}
											mailOutVo.setMessage(mailOutVo.getMessage()
													.concat(" \n " + mailMessages.getRequestDetail()));

											if (null != requestVo.getRequestDetailList()) {

												for (RequestDetailVO requestDetailVo : requestVo
														.getRequestDetailList()) {

													mailOutVo.setMessage(mailOutVo.getMessage()
															.concat(" \n " + requestDetailVo
																	.getRequestScreenDetailConfigurationFieldName()
															+ " : " + requestDetailVo
																	.getRequestScreenDetailConfigurationFieldValue()));

												}
											}

											mailOutVo.setMessage(mailOutVo.getMessage()
													.concat(" \n \n \n " + mailMessages.getBestRegards()));
											mailOutVo.setMessage(mailOutVo.getMessage()
													.concat(" \n " + mailMessages.getSupportTeam()));

											mailOutVo.setToMailAddress(toAddress);

											try {

												this.sendMail(mailOutVo, mailParameterEntity,userEntity);
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
									}
								}
								break;

							case "REQUEST_UPDATE":
								if (null != mailParameterEntity.getMessage()) {
									if (mailParameterEntity.getMessage().equals(CommonConstant.RU)) {

										UserEntity userEntity = null;

										if (0 != mailParameterEntity.getUserId()) {

											userEntity = mailControllerDao
													.getUserEmailId(mailParameterEntity.getUserId());
											if (null != userEntity && null != userEntity.getEmailId()) {
												toAddress.add(userEntity.getEmailId());
											}
										}

										if (null != toAddress && !toAddress.isEmpty()) {

											mailOutVo.setTitle(mailMessages.getName() + " " + userEntity.getFirstName()
													+ " , " + mailMessages.getRequestCode() + " "
													+ requestVo.getRequest().getRequestCode() + " - "
													+ mailOutVo.getTitle());
											if(null != requestVo.getRequest() && null != requestVo.getRequest().getCurrentStatusName()){
												mailOutVo.setMessage(mailOutVo.getMessage().concat("\n"+"Status :"+ ""+requestVo.getRequest().getCurrentStatusName()+"\n"));
											}
											if (null != requestVo) {
												if (null != requestVo.getRequest().getRequestCode()) {
													mailOutVo.setMessage(
															mailOutVo.getMessage().concat(mailMessages.getRequestCode()
																	+ " " + requestVo.getRequest().getRequestCode()));
												}
												if (null != requestVo.getRequest().getRequestSubject()) {
													mailOutVo
															.setMessage(mailOutVo.getMessage()
																	.concat(" "
																			+ mailMessages.getAnd() + " " + mailMessages
																					.getRequestSubject()
																			+ " " + requestVo.getRequest()
																					.getRequestSubject()));
												}

											}

											mailOutVo.setMessage(mailOutVo.getMessage()
													.concat(" " + mailMessages.getRequestRaise()));

											mailOutVo.setMessage(mailOutVo.getMessage()
													.concat(" \n \n \n " + mailMessages.getBestRegards()));
											mailOutVo.setMessage(mailOutVo.getMessage()
													.concat(" \n " + mailMessages.getSupportTeam()));

											mailOutVo.setToMailAddress(toAddress);

											try {

												this.sendMail(mailOutVo, mailParameterEntity,userEntity);
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
									}
								}
								break;
							case "APPROVED":
								if (null != mailParameterEntity.getMessage()) {
									if (mailParameterEntity.getMessage().equals(CommonConstant.APP)) {

										UserEntity userEntity = null;

										if (0 != mailParameterEntity.getUserId()) {

											userEntity = mailControllerDao
													.getUserEmailId(mailParameterEntity.getUserId());
											if (null != userEntity && null != userEntity.getEmailId()) {
												toAddress.add(userEntity.getEmailId());
											}
										}
										if (null != toAddress && !toAddress.isEmpty()) {

											mailOutVo.setTitle(mailMessages.getName() + " " + userEntity.getFirstName()
													+ " , " + mailMessages.getRequestCode() + " "
													+ requestVo.getRequest().getRequestCode() + " - "
													+ mailOutVo.getTitle());
											if(null != requestVo.getRequest() && null != requestVo.getRequest().getCurrentStatusName()){
												mailOutVo.setMessage(mailOutVo.getMessage().concat("\n"+"Status :"+ ""+requestVo.getRequest().getCurrentStatusName()+"\n"));
											}
											UserEntity user = mailControllerDao
													.getUserEmailId(mailParameterEntity.getAssignUserId());

											mailOutVo.setMessage(
													mailOutVo.getMessage().concat(" " + user.getFirstName()));

											mailOutVo.setMessage(mailOutVo.getMessage()
													.concat(" \n \n \n " + mailMessages.getBestRegards()));
											mailOutVo.setMessage(mailOutVo.getMessage()
													.concat(" \n " + mailMessages.getSupportTeam()));

											mailOutVo.setToMailAddress(toAddress);

											try {

												this.sendMail(mailOutVo, mailParameterEntity,userEntity);
											} catch (Exception e) {
												e.printStackTrace();
											}

										}

									}
								}
								break;
							case "REJECTED":
								if (null != mailParameterEntity.getMessage()) {
									if (mailParameterEntity.getMessage().equals(CommonConstant.REJ)) {

										UserEntity userEntity = null;

										if (0 != mailParameterEntity.getUserId()) {

											userEntity = mailControllerDao
													.getUserEmailId(mailParameterEntity.getUserId());

											if (null != userEntity && null != userEntity.getEmailId()) {
												toAddress.add(userEntity.getEmailId());
											}
										}

										if (null != toAddress && !toAddress.isEmpty()) {

											mailOutVo.setTitle(mailMessages.getName() + " " + userEntity.getFirstName()
													+ " , " + mailMessages.getRequestCode() + " "
													+ requestVo.getRequest().getRequestCode() + " - "
													+ mailOutVo.getTitle());
											if(null != requestVo.getRequest() && null != requestVo.getRequest().getCurrentStatusName()){
												mailOutVo.setMessage(mailOutVo.getMessage().concat("\n"+"Status :"+ ""+requestVo.getRequest().getCurrentStatusName()+"\n"));
											}
											if (null != requestVo) {
												if (null != requestVo.getRequest().getRequestCode()) {
													mailOutVo.setMessage(
															mailOutVo.getMessage().concat(mailMessages.getRequestCode()
																	+ " " + requestVo.getRequest().getRequestCode()));
												}
												if (null != requestVo.getRequest().getRequestSubject()) {
													mailOutVo
															.setMessage(mailOutVo.getMessage()
																	.concat(" "
																			+ mailMessages.getAnd() + " " + mailMessages
																					.getRequestSubject()
																			+ " " + requestVo.getRequest()
																					.getRequestSubject()));
												}

											}

											UserEntity assisgnUser = mailControllerDao
													.getUserEmailId(mailParameterEntity.getAssignUserId());

											mailOutVo.setMessage(
													mailOutVo.getMessage().concat(" " + mailMessages.getRequestReject()
															+ " " + assisgnUser.getFirstName()));

											mailOutVo.setMessage(mailOutVo.getMessage()
													.concat(" \n \n \n " + mailMessages.getBestRegards()));
											mailOutVo.setMessage(mailOutVo.getMessage()
													.concat(" \n " + mailMessages.getSupportTeam()));

											mailOutVo.setToMailAddress(toAddress);
											try {

												this.sendMail(mailOutVo, mailParameterEntity,userEntity);
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
									}
								}
								break;
							case "RESUBMIT":
								if (null != mailParameterEntity.getMessage()) {
									if (mailParameterEntity.getMessage().equals(CommonConstant.RS)) {

										UserEntity userEntity = null;

										if (0 != mailParameterEntity.getUserId()) {

											userEntity = mailControllerDao
													.getUserEmailId(mailParameterEntity.getUserId());
											if (null != userEntity && null != userEntity.getEmailId()) {
												toAddress.add(userEntity.getEmailId());
											}
										}

										if (null != toAddress && !toAddress.isEmpty()) {

											mailOutVo.setTitle(mailMessages.getName() + " " + userEntity.getFirstName()
													+ " , " + mailMessages.getRequestCode() + " "
													+ requestVo.getRequest().getRequestCode() + " - "
													+ mailOutVo.getTitle());
											if(null != requestVo.getRequest() && null != requestVo.getRequest().getCurrentStatusName()){
												mailOutVo.setMessage(mailOutVo.getMessage().concat("\n"+"Status :"+ ""+requestVo.getRequest().getCurrentStatusName()+"\n"));
											}
											if (null != requestVo) {
												if (null != requestVo.getRequest().getRequestCode()) {
													mailOutVo.setMessage(
															mailOutVo.getMessage().concat(mailMessages.getRequestCode()
																	+ " " + requestVo.getRequest().getRequestCode()));
												}
												if (null != requestVo.getRequest().getRequestSubject()) {
													mailOutVo
															.setMessage(mailOutVo.getMessage()
																	.concat(" "
																			+ mailMessages.getAnd() + " " + mailMessages
																					.getRequestSubject()
																			+ " " + requestVo.getRequest()
																					.getRequestSubject()));
												}

											}

											UserEntity assisgnUser = mailControllerDao
													.getUserEmailId(mailParameterEntity.getAssignUserId());

											mailOutVo.setMessage(mailOutVo.getMessage()
													.concat(" " + mailMessages.getRequestResubmit() + " "
															+ assisgnUser.getFirstName()));

											mailOutVo.setMessage(mailOutVo.getMessage()
													.concat(" \n \n \n " + mailMessages.getBestRegards()));
											mailOutVo.setMessage(mailOutVo.getMessage()
													.concat(" \n " + mailMessages.getSupportTeam()));

											mailOutVo.setToMailAddress(toAddress);
											try {

												this.sendMail(mailOutVo, mailParameterEntity,userEntity);
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
									}
								}
								break;
							case "COMPLETED":
								if (null != mailParameterEntity.getMessage()) {
									if (mailParameterEntity.getMessage().equals(CommonConstant.COM)) {

										UserEntity userEntity = null;

										if (0 != mailParameterEntity.getUserId()) {

											userEntity = mailControllerDao
													.getUserEmailId(mailParameterEntity.getUserId());

											if (null != userEntity && null != userEntity.getEmailId()) {
												toAddress.add(userEntity.getEmailId());
											}
										}

										if (null != toAddress && !toAddress.isEmpty()) {

											mailOutVo.setTitle(mailMessages.getName() + " " + userEntity.getFirstName()
													+ " , " + mailMessages.getRequestCode() + " "
													+ requestVo.getRequest().getRequestCode() + " - "
													+ mailOutVo.getTitle());
											if(null != requestVo.getRequest() && null != requestVo.getRequest().getCurrentStatusName()){
												mailOutVo.setMessage(mailOutVo.getMessage().concat("\n"+"Status :"+ ""+requestVo.getRequest().getCurrentStatusName()+"\n"));
											}
											if (null != requestVo) {
												if (null != requestVo.getRequest().getRequestCode()) {
													mailOutVo.setMessage(
															mailOutVo.getMessage().concat(mailMessages.getRequestCode()
																	+ " " + requestVo.getRequest().getRequestCode()));
												}
												if (null != requestVo.getRequest().getRequestSubject()) {
													mailOutVo
															.setMessage(mailOutVo.getMessage()
																	.concat(" "
																			+ mailMessages.getAnd() + " " + mailMessages
																					.getRequestSubject()
																			+ " " + requestVo.getRequest()
																					.getRequestSubject()));
												}

											}

											mailOutVo.setMessage(mailOutVo.getMessage()
													.concat(" " + mailMessages.getRequestResolve()));

											mailOutVo.setMessage(mailOutVo.getMessage()
													.concat(" \n \n \n " + mailMessages.getBestRegards()));
											mailOutVo.setMessage(mailOutVo.getMessage()
													.concat(" \n " + mailMessages.getSupportTeam()));

											mailOutVo.setToMailAddress(toAddress);
											try {

												this.sendMail(mailOutVo, mailParameterEntity,userEntity);
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
									}
								}
								break;
							case "INPROGRESS":
								if (null != mailParameterEntity.getMessage()) {
									if (mailParameterEntity.getMessage().equals(CommonConstant.IP)) {

										if (0 != mailParameterEntity.getUserId()) {

											UserEntity userEntity = mailControllerDao
													.getUserEmailId(mailParameterEntity.getUserId());
											if (null != userEntity && null != userEntity.getEmailId()) {
												toAddress.add(userEntity.getEmailId());
											}

											if (null != toAddress && !toAddress.isEmpty()) {

												mailOutVo.setTitle(
														mailMessages.getName() + " " + userEntity.getFirstName() + " , "
																+ mailMessages.getRequestCode() + " "
																+ requestVo.getRequest().getRequestCode() + " - "
																+ mailOutVo.getTitle());
												if(null != requestVo.getRequest() && null != requestVo.getRequest().getCurrentStatusName()){
													mailOutVo.setMessage(mailOutVo.getMessage().concat("\n"+"Status :"+ ""+requestVo.getRequest().getCurrentStatusName()+"\n"));
												}
												UserEntity user = null;
												if(null != mailParameterEntity.getAssignUserId()){
													user =	mailControllerDao
														.getUserEmailId(mailParameterEntity.getAssignUserId());
												}

												if(null != user && null != user.getFirstName()){
												mailOutVo.setMessage(
														mailOutVo.getMessage().concat(" " + user.getFirstName()));
												}

												mailOutVo.setMessage(mailOutVo.getMessage()
														.concat(" \n \n \n " + mailMessages.getBestRegards()));
												mailOutVo.setMessage(mailOutVo.getMessage()
														.concat(" \n " + mailMessages.getSupportTeam()));

												mailOutVo.setToMailAddress(toAddress);
												try {

													this.sendMail(mailOutVo, mailParameterEntity,userEntity);
												} catch (Exception e) {
													e.printStackTrace();
												}

											}
										}
									}
								}
								break;
							case "REASSIGN":
								if (null != mailParameterEntity.getMessage()) {
									if (mailParameterEntity.getMessage().equals(CommonConstant.RA)) {

										UserEntity userEntity = null;

										if (0 != mailParameterEntity.getUserId()) {

											userEntity = mailControllerDao
													.getUserEmailId(mailParameterEntity.getUserId());
											if (null != userEntity && null != userEntity.getEmailId()) {
												toAddress.add(userEntity.getEmailId());
											}
										}
										if (null != toAddress && !toAddress.isEmpty()) {

											mailOutVo.setTitle(mailMessages.getName() + " " + userEntity.getFirstName()
													+ " , " + mailMessages.getRequestCode() + " "
													+ requestVo.getRequest().getRequestCode() + " - "
													+ mailOutVo.getTitle());
											if(null != requestVo.getRequest() && null != requestVo.getRequest().getCurrentStatusName()){
												mailOutVo.setMessage(mailOutVo.getMessage().concat("\n"+"Status :"+ ""+requestVo.getRequest().getCurrentStatusName()+"\n"));
											}
											UserEntity user = mailControllerDao
													.getUserEmailId(mailParameterEntity.getAssignUserId());

											mailOutVo.setMessage(
													mailOutVo.getMessage().concat(" " + user.getFirstName()));

											mailOutVo.setMessage(mailOutVo.getMessage()
													.concat(" \n \n \n " + mailMessages.getBestRegards()));
											mailOutVo.setMessage(mailOutVo.getMessage()
													.concat(" \n " + mailMessages.getSupportTeam()));

											mailOutVo.setToMailAddress(toAddress);
											try {

												this.sendMail(mailOutVo, mailParameterEntity,userEntity);
											} catch (Exception e) {
												e.printStackTrace();
											}

										}
									}
								}
								break;
							case "REASSIGN_USER":
								if (null != mailParameterEntity.getMessage()) {
									if (mailParameterEntity.getMessage().equals(CommonConstant.RAU)) {

										UserEntity userEntity = null;

										if (0 != mailParameterEntity.getUserId()) {

											userEntity = mailControllerDao
													.getUserEmailId(mailParameterEntity.getUserId());
											if (null != userEntity && null != userEntity.getEmailId()) {
												toAddress.add(userEntity.getEmailId());
											}
										}

										if (null != toAddress && !toAddress.isEmpty()) {

											mailOutVo.setTitle(mailMessages.getName() + " " + userEntity.getFirstName()
													+ " , " + mailMessages.getRequestCode() + " "
													+ requestVo.getRequest().getRequestCode() + " - "
													+ mailOutVo.getTitle());
											if(null != requestVo.getRequest() && null != requestVo.getRequest().getCurrentStatusName()){
												mailOutVo.setMessage(mailOutVo.getMessage().concat("\n"+"Status :"+ ""+requestVo.getRequest().getCurrentStatusName()+"\n"));
											}
											mailOutVo.setMessage(
													mailMessages.getDear().concat(" " + userEntity.getFirstName() + ","
															+ " \n " + " " + mailOutVo.getMessage()));

											if (null != requestVo.getRequest()) {

												if (null != requestVo.getRequest().getRequestCode()) {
													mailOutVo
															.setMessage(mailOutVo.getMessage()
																	.concat(" \n " + mailMessages.getRequestCode()
																			+ "  "
																			+ requestVo.getRequest().getRequestCode()));
												}
												if (null != requestVo.getRequest().getRequestTypeName()) {
													mailOutVo
															.setMessage(mailOutVo.getMessage()
																	.concat(" \n " + mailMessages
																			.getRequestType() + " "
																	+ requestVo.getRequest().getRequestTypeName()));
												}
												if (null != requestVo.getRequest().getRequestSubject()) {
													mailOutVo
															.setMessage(mailOutVo.getMessage()
																	.concat(" \n " + mailMessages
																			.getRequestSubject() + " "
																	+ requestVo.getRequest().getRequestSubject()));
												}
												if (null != requestVo.getRequest().getRequestSubTypeName()) {
													mailOutVo
															.setMessage(mailOutVo.getMessage()
																	.concat(" \n " + mailMessages
																			.getRequestSubType() + " "
																	+ requestVo.getRequest().getRequestSubTypeName()));
												}
											}
											mailOutVo.setMessage(mailOutVo.getMessage()
													.concat(" \n " + mailMessages.getRequestDetail()));

											if (null != requestVo.getRequestDetailList()) {

												for (RequestDetailVO requestDetailVo : requestVo
														.getRequestDetailList()) {

													mailOutVo.setMessage(mailOutVo.getMessage()
															.concat(" \n " + requestDetailVo
																	.getRequestScreenDetailConfigurationFieldName()
															+ " : " + requestDetailVo
																	.getRequestScreenDetailConfigurationFieldValue()));

												}
											}

											mailOutVo.setMessage(mailOutVo.getMessage()
													.concat(" \n \n \n " + mailMessages.getBestRegards()));
											mailOutVo.setMessage(mailOutVo.getMessage()
													.concat(" \n " + mailMessages.getSupportTeam()));

											mailOutVo.setToMailAddress(toAddress);

											try {

												this.sendMail(mailOutVo, mailParameterEntity,userEntity);
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
									}
								}
								break;
							case "REOPEN_EXECUTER":
								if (null != mailParameterEntity.getMessage()) {
									if (mailParameterEntity.getMessage().equals(CommonConstant.ROE)) {

										UserEntity userEntity = null;

										if (0 != mailParameterEntity.getUserId()) {

											userEntity = mailControllerDao
													.getUserEmailId(mailParameterEntity.getUserId());
											if (null != userEntity && null != userEntity.getEmailId()) {
												toAddress.add(userEntity.getEmailId());
											}
										}

										if (null != toAddress && !toAddress.isEmpty()) {

											mailOutVo.setTitle(mailMessages.getName() + " " + userEntity.getFirstName()
													+ " , " + mailMessages.getRequestCode() + " "
													+ requestVo.getRequest().getRequestCode() + " - "
													+ mailOutVo.getTitle());
											if(null != requestVo.getRequest() && null != requestVo.getRequest().getCurrentStatusName()){
												mailOutVo.setMessage(mailOutVo.getMessage().concat("\n"+"Status :"+ ""+requestVo.getRequest().getCurrentStatusName()+"\n"));
											}
											mailOutVo.setMessage(
													mailMessages.getDear().concat(" " + userEntity.getFirstName() + ","
															+ " \n " + " " + mailOutVo.getMessage()));

											if (null != requestVo.getRequest()) {

												if (null != requestVo.getRequest().getRequestCode()) {
													mailOutVo
															.setMessage(mailOutVo.getMessage()
																	.concat(" \n " + mailMessages.getRequestCode()
																			+ "  "
																			+ requestVo.getRequest().getRequestCode()));
												}
												if (null != requestVo.getRequest().getRequestTypeName()) {
													mailOutVo
															.setMessage(mailOutVo.getMessage()
																	.concat(" \n " + mailMessages
																			.getRequestType() + " "
																	+ requestVo.getRequest().getRequestTypeName()));
												}
												if (null != requestVo.getRequest().getRequestSubject()) {
													mailOutVo
															.setMessage(mailOutVo.getMessage()
																	.concat(" \n " + mailMessages
																			.getRequestSubject() + " "
																	+ requestVo.getRequest().getRequestSubject()));
												}
												if (null != requestVo.getRequest().getRequestSubTypeName()) {
													mailOutVo
															.setMessage(mailOutVo.getMessage()
																	.concat(" \n " + mailMessages
																			.getRequestSubType() + " "
																	+ requestVo.getRequest().getRequestSubTypeName()));
												}
											}
											mailOutVo.setMessage(mailOutVo.getMessage()
													.concat(" \n " + mailMessages.getRequestDetail()));

											if (null != requestVo.getRequestDetailList()) {

												for (RequestDetailVO requestDetailVo : requestVo
														.getRequestDetailList()) {

													mailOutVo.setMessage(mailOutVo.getMessage()
															.concat(" \n " + requestDetailVo
																	.getRequestScreenDetailConfigurationFieldName()
															+ " : " + requestDetailVo
																	.getRequestScreenDetailConfigurationFieldValue()));

												}
											}

											mailOutVo.setMessage(mailOutVo.getMessage()
													.concat(" \n \n \n " + mailMessages.getBestRegards()));
											mailOutVo.setMessage(mailOutVo.getMessage()
													.concat(" \n " + mailMessages.getSupportTeam()));

											mailOutVo.setToMailAddress(toAddress);

											try {

												this.sendMail(mailOutVo, mailParameterEntity,userEntity);
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
									}
								}
								break;
							case "REOPEN":
								if (null != mailParameterEntity.getMessage()) {
									if (mailParameterEntity.getMessage().equals(CommonConstant.RO)) {

										UserEntity userEntity = null;

										if (0 != mailParameterEntity.getUserId()) {

											userEntity = mailControllerDao
													.getUserEmailId(mailParameterEntity.getUserId());
											if (null != userEntity && null != userEntity.getEmailId()) {
												toAddress.add(userEntity.getEmailId());
											}
										}
										if (null != toAddress && !toAddress.isEmpty()) {

											mailOutVo.setTitle(mailMessages.getName() + " " + userEntity.getFirstName()
													+ " , " + mailMessages.getRequestCode() + " "
													+ requestVo.getRequest().getRequestCode() + " - "
													+ mailOutVo.getTitle());
											if(null != requestVo.getRequest() && null != requestVo.getRequest().getCurrentStatusName()){
												mailOutVo.setMessage(mailOutVo.getMessage().concat("\n"+"Status :"+ ""+requestVo.getRequest().getCurrentStatusName()+"\n"));
											}
											mailOutVo.setMessage(mailOutVo.getMessage()
													.concat(" \n \n \n " + mailMessages.getBestRegards()));
											mailOutVo.setMessage(mailOutVo.getMessage()
													.concat(" \n " + mailMessages.getSupportTeam()));

											mailOutVo.setToMailAddress(toAddress);
											try {

												this.sendMail(mailOutVo, mailParameterEntity,userEntity);
											} catch (Exception e) {
												e.printStackTrace();
											}

										}
									}
								}
								break;
							case "CLOSE":
								if (null != mailParameterEntity.getMessage()) {
									if (mailParameterEntity.getMessage().equals(CommonConstant.CL)) {

										UserEntity userEntity = null;

										if (0 != mailParameterEntity.getUserId()) {

											userEntity = mailControllerDao
													.getUserEmailId(mailParameterEntity.getUserId());
											if (null != userEntity && null != userEntity.getEmailId()) {
												toAddress.add(userEntity.getEmailId());
											}
										}

										if (null != toAddress && !toAddress.isEmpty()) {

											mailOutVo.setTitle(mailMessages.getName() + " " + userEntity.getFirstName()
													+ " , " + mailMessages.getRequestCode() + " "
													+ requestVo.getRequest().getRequestCode() + " - "
													+ mailOutVo.getTitle());
											if(null != requestVo.getRequest() && null != requestVo.getRequest().getCurrentStatusName()){
												mailOutVo.setMessage(mailOutVo.getMessage().concat("\n"+"Status :"+ ""+requestVo.getRequest().getCurrentStatusName()+"\n"));
											}
											mailOutVo.setMessage(mailOutVo.getMessage()
													.concat(" \n \n \n " + mailMessages.getBestRegards()));
											mailOutVo.setMessage(mailOutVo.getMessage()
													.concat(" \n " + mailMessages.getSupportTeam()));

											mailOutVo.setToMailAddress(toAddress);
											try {

												this.sendMail(mailOutVo, mailParameterEntity,userEntity);
											} catch (Exception e) {
												e.printStackTrace();
											}

										}
									}
								}
								break;
							case "CANCEL":
								if (null != mailParameterEntity.getMessage()) {
									if (mailParameterEntity.getMessage().equals(CommonConstant.CAN)) {

										UserEntity userEntity = null;

										if (0 != mailParameterEntity.getUserId()) {

											userEntity = mailControllerDao
													.getUserEmailId(mailParameterEntity.getUserId());
											if (null != userEntity && null != userEntity.getEmailId()) {
												toAddress.add(userEntity.getEmailId());
											}
										}

										if (null != toAddress && !toAddress.isEmpty()) {

											mailOutVo.setTitle(mailMessages.getName() + " " + userEntity.getFirstName()
													+ " , " + mailMessages.getRequestCode() + " "
													+ requestVo.getRequest().getRequestCode() + " - "
													+ mailOutVo.getTitle());
											if(null != requestVo.getRequest() && null != requestVo.getRequest().getCurrentStatusName()){
												mailOutVo.setMessage(mailOutVo.getMessage().concat("\n"+"Status :"+ ""+requestVo.getRequest().getCurrentStatusName()+"\n"));
											}
											mailOutVo.setMessage(mailOutVo.getMessage()
													.concat(" \n \n \n " + mailMessages.getBestRegards()));
											mailOutVo.setMessage(mailOutVo.getMessage()
													.concat(" \n " + mailMessages.getSupportTeam()));

											mailOutVo.setToMailAddress(toAddress);
											try {

												this.sendMail(mailOutVo, mailParameterEntity,userEntity);
											} catch (Exception e) {
												e.printStackTrace();
											}

										}

									}
								}
								break;
							case "ESCALATION_BEFORE_APPROVAL":
								if (null != mailParameterEntity.getMessage()) {
									if (mailParameterEntity.getMessage().equals(CommonConstant.EBA)) {

										if (0 != mailParameterEntity.getUserId()) {

											UserEntity userEntity = mailControllerDao
													.getUserEmailId(mailParameterEntity.getUserId());
											if (null != userEntity && null != userEntity.getEmailId()) {
												toAddress.add(userEntity.getEmailId());
											}
										}

										mailOutVo.setMessage(mailOutVo.getMessage()
												.concat(" \n \n \n " + mailMessages.getBestRegards()));
										mailOutVo.setMessage(
												mailOutVo.getMessage().concat(" \n " + mailMessages.getSupportTeam()));
										mailOutVo.setToMailAddress(toAddress);
										try {

											//this.sendMail(mailOutVo, mailParameterEntity);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}
								break;
							case "ESCALATION_BEFORE_RESOLVER":
								if (null != mailParameterEntity.getMessage()) {
									if (mailParameterEntity.getMessage().equals(CommonConstant.EBR)) {

										if (0 != mailParameterEntity.getUserId()) {

											UserEntity userEntity = mailControllerDao
													.getUserEmailId(mailParameterEntity.getUserId());
											if (null != userEntity && null != userEntity.getEmailId()) {
												toAddress.add(userEntity.getEmailId());
											}
										}

										mailOutVo.setMessage(mailOutVo.getMessage()
												.concat(" \n \n \n " + mailMessages.getBestRegards()));
										mailOutVo.setMessage(
												mailOutVo.getMessage().concat(" \n " + mailMessages.getSupportTeam()));
										mailOutVo.setToMailAddress(toAddress);
										try {

											//this.sendMail(mailOutVo, mailParameterEntity);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}
								break;

							case "ESCALATION_PENDING_REQUEST":
								if (null != mailParameterEntity.getMessage()) {
									if (mailParameterEntity.getMessage().equals(CommonConstant.EPR)) {

										if (0 != mailParameterEntity.getUserId()) {

											UserEntity userEntity = mailControllerDao
													.getUserEmailId(mailParameterEntity.getUserId());
											if (null != userEntity && null != userEntity.getEmailId()) {
												toAddress.add(userEntity.getEmailId());
											}
										}

										mailOutVo.setMessage(mailOutVo.getMessage()
												.concat(" \n \n \n " + mailMessages.getBestRegards()));
										mailOutVo.setMessage(
												mailOutVo.getMessage().concat(" \n " + mailMessages.getSupportTeam()));
										mailOutVo.setToMailAddress(toAddress);
										try {

											//this.sendMail(mailOutVo, mailParameterEntity);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}
								break;
							case "ESCALATION_RESOLVER_REQUEST":
								if (null != mailParameterEntity.getMessage()) {
									if (mailParameterEntity.getMessage().equals(CommonConstant.EPR)) {

										if (0 != mailParameterEntity.getUserId()) {

											UserEntity userEntity = mailControllerDao
													.getUserEmailId(mailParameterEntity.getUserId());
											if (null != userEntity && null != userEntity.getEmailId()) {
												toAddress.add(userEntity.getEmailId());
											}
										}

										mailOutVo.setMessage(mailOutVo.getMessage()
												.concat(" \n \n \n " + mailMessages.getBestRegards()));
										mailOutVo.setMessage(
												mailOutVo.getMessage().concat(" \n " + mailMessages.getSupportTeam()));
										mailOutVo.setToMailAddress(toAddress);
										try {

											//this.sendMail(mailOutVo, mailParameterEntity,userEntity);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}
								break;

							}
						}
					}

				}

			}
		}

		return mailOutVo;

	}

	/**
	 * This method is used to send the mail to user.
	 * 
	 * @param mailOutVo
	 *            MailOutVo
	 * @param mailParameterEntity
	 *            MailParameterEntity
	 * @throws Exception
	 */
	@Transactional
	private void sendMail(MailOutVo mailOutVo, MailParameterEntity mailParameterEntity,UserEntity userEntity) throws Exception {

		MessageMasterVo messageMasterVo = new MessageMasterVo();

		mailOutVo.setMessageCode(mailParameterEntity.getMessageCode());
		// To form Message
		StringBuilder sblMessage = new StringBuilder();

		sblMessage.append(mailOutVo.getMessage());
		mailOutVo.setMessage(sblMessage.toString());

		// To form Title
		StringBuilder sblTitle = new StringBuilder();

		if (mailParameterEntity.getTitle() != null && mailParameterEntity.getTitle().length() > 0) {
			sblTitle.append(MessageFormat.format(mailOutVo.getTitle(), (Object) mailParameterEntity.getTitle()));
		} else {

			if (null != mailParameterEntity.getTitle()) {
				sblTitle.append(mailOutVo.getTitle() + " - " + mailParameterEntity.getTitle());
			}

		}
		mailOutVo.setTitle(sblTitle.toString());
		messageMasterVo.setSubject(sblTitle.toString());
		messageMasterVo.setMessage(sblMessage.toString());

		MailInstance obj = MailInstance.getInstance(mailMessages);
		Session mailSession = obj.getSession();

		// create a message
		MimeMessage msg = new MimeMessage(mailSession);

		// set the from address
		InternetAddress addressFrom = new InternetAddress(mailMessages.getFromMailAddress());
		msg.setFrom(addressFrom);
		List<String> toAddress = mailOutVo.getToMailAddress();
		InternetAddress[] addressTo = new InternetAddress[toAddress.size()];
		int i = 0;
		for (String recipient : toAddress) {
			addressTo[i] = new InternetAddress(recipient);
			i++;
		}
		msg.setRecipients(javax.mail.Message.RecipientType.TO, addressTo);

		// set the CC address
		/*
		 * List<String> ccAddress = mailOutVo.getCcMailAddress();
		 * InternetAddress[] addressCC = new InternetAddress[ccAddress.size()];
		 * i = 0; for (String recipient : ccAddress) { addressCC[i] = new
		 * InternetAddress(recipient); i++; } if (addressCC != null &&
		 * addressCC.length > 0) {
		 * msg.setRecipients(javax.mail.Message.RecipientType.CC, addressCC); }
		 */
		// Setting the Subject and message
		msg.setHeader("Content-Type", "text/html; charset=UTF-8");
		// Setting the Subject and message
		msg.setSubject(mailOutVo.getTitle(), "UTF-8");

		if (mailOutVo.isHtmlFormat()) {
			msg.setContent(mailOutVo.getMessage(), "text/html; charset=UTF-8");
		} else {
			msg.setText(mailOutVo.getMessage(), "UTF-8");
		}
		msg.saveChanges();

		MaillogEntity maillogEntity = mailControllerDao.createMailLog(mailOutVo,userEntity);

		mailParameterEntity.setMailLogId((int) maillogEntity.getMaillogNumber());

		mailControllerDao.updateMailLogId(mailParameterEntity);

	}

}
