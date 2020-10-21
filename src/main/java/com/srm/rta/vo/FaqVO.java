package com.srm.rta.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FaqVO {
	
	private Integer faqId;
	private String question;
	private String answer;
	private String faqSearchText;
	private Integer isFaqActive;	
	private Integer deleteFlag;
	
}
