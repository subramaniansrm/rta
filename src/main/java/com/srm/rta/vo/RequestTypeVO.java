package com.srm.rta.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.srm.coreframework.vo.CommonVO;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestTypeVO extends CommonVO {

	private Integer requestTypeId;// Id of RequestType
	
	private String requestTypeCode;//Code of RequestType
	
	private String requestTypeName;//Name of RequestType
	
	private String requestTypeUrl;//Url of RequestType
	
	private boolean requestTypeIsActive;//1-Active,0-Inactive
	
	private String status;
	
	private List<Integer>  requestTypeList;//List of RequestType Id
		
	private Integer entityId;// Id of RequestType

}
