package com.srm.rta.vo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.srm.coreframework.vo.SystemConfigurationVo;

import lombok.Data;

 
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmailVo {	
			
	private String messageCode;	
	private String groupId;
	private int toUserId;
	private String requestCode;
	private int requestId;
	private int entityId;
	private String requestSubject;
	
	private String requestTypeName;
	private String requestSubTypeName;
	
	//private String queueName;
	//private String exchangeName;
	//private String routingName;
	//private String fromAddress;
	private String toUserAddress;
	private String ccAddress;
	//private String firstName;
	private List<RequestDetailVO> requestDetailList;// List of RequestDetail
	
	private String messageContent;
	private String title;
	private int emailFlag;
	private int assignUserId;
	
	private int createBy;
	private int updateBy;
	private Date createDate;
	private Date updateDate;
	
	private String errorMessage;
	private int fromUserId;
	private int emailMessageId;		
		
	private String userLoginId;
	private String password;
	private Integer notificationFlag;
	private Integer escalationFlag;
	private String userName;
		
	private String requestorName;
	private String approverName;
	private String executorName;
	
	private List<SystemConfigurationVo> systemConfigurationVo;
	
	private String delegatedToUser;
	private String delegatedFromUser;
	private String delegationRole;
	private String delegatedFromDate;	
	private String delegatedToDate;
	private String delegatedStatus;
	private String oldPassword;
	private String newPassword;
	
	private String entityName;
	private String userLang;
		
	private int trEmailFlag;
	private int trNotificationFlag;
	private boolean notSaveFlag;
}