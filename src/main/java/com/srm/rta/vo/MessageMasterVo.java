package com.srm.rta.vo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageMasterVo {


	private Integer messageId;
	private String subject;
	private String message;
	private Date dateTime;
	private String sendTo;
	private String sendBy;
	private String sentDateTime;

	private List<MessageMasterVo> messageMasterVoList;
	
	
}
