package com.srm.rta.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.srm.coreframework.vo.CommonVO;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DropdownRequestSubTypeVO extends CommonVO {

	private Integer requestSubTypeId;// Id of RequestSubType
	
	private String requestSubTypeName;// Name of RequestSubType
	
	private Integer requestSubtypePriorty;

}
