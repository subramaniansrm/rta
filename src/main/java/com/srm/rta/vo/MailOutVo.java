package com.srm.rta.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MailOutVo {

	private List<String> toMailAddress;
	private List<String> ccMailAddress;
	private String message;
	private String title;
	private boolean htmlFormat;
	private String messageCode;
	

}
