package com.srm.rta.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.srm.coreframework.vo.CommonVO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = false)
public class UserProfileVO extends CommonVO {

	private String firstName;

	private String profile;

	private Integer userId;
	
	private String lastName;
	
	private String langCode;
	
	private byte[] imageLoad;
	
	private Integer entityId;
	
	private String entityName;
}
