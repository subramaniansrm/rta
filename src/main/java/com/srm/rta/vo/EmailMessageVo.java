package com.srm.rta.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmailMessageVo {

	private int emailMessageId;
	private String messageCode;
	private String groupId;
	private int toUserId;
	private int ccUserId;
	private int requestId;
	private int entityId;
	private int emailFlag;
	private int escalationFlag;
	private int deleteFlag;
	private String toUserAddress;
	private String ccUserAddress;
	private String messageContent;
	//private String messageContentJp;
	private int htmlFormat;
	private String title;
	//private String titleJp;
	private int retryCount;
	private String exchangeName;
	private String queueName;
	private String routingName;
	private int fetchFlag;
	private int emailFailedFlag;
	private Date createDate;
	private String userLang;

}