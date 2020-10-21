package com.srm.rta.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.srm.coreframework.vo.CommonVO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HelpVO extends CommonVO {
	
	private Integer helpId;
	
	private String topic;
	
	private String detail;
	
	private String helpSearchText;
	
}
