package com.srm.rta.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.srm.coreframework.vo.CommonVO;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DropdownRequestTypeVO extends CommonVO {

	private Integer requestTypeId;// Id of RequestType
			
	private String requestTypeName;//Name of RequestType
	
}
