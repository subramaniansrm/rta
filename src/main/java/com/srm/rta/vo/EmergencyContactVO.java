package com.srm.rta.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.srm.coreframework.vo.CommonVO;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmergencyContactVO extends CommonVO {
	
	

	private Integer emergencyContactPathId;// Id of phone book

	private String emergencyContactPath;
	
	private String emergencyContactName;
	
	private Integer[] deleteItem;
}
