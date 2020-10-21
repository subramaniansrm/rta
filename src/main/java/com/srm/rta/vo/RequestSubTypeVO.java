package com.srm.rta.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.srm.coreframework.vo.CommonVO;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestSubTypeVO extends CommonVO {

	private Integer requestSubTypeId;// Id of RequestSubType

	private String requestSubTypeCode;// Code of RequestSubType

	private Integer requestTypeId;// Id of RequestType

	private String requestTypeName;// Name of RequestType

	private String requestSubTypeName;// Name of RequestSubType

	private Integer requestSubTypeIsActive;// 1-Active,0-InActive

	private List<Integer> requestSubTypeList;// List of RequestSubType Id

	private Integer requestSubtypePriorty;

	private String status;


}
