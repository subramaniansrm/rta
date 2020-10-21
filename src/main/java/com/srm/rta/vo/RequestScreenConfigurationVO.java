package com.srm.rta.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.srm.coreframework.vo.CommonVO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonInclude(JsonInclude.Include.NON_NULL) 
@Data
@EqualsAndHashCode(callSuper=false)
public class RequestScreenConfigurationVO extends CommonVO{
	
	private Integer requestScreenConfigId;

	private String requestScreenConfigurationName;

	private String requestScreenConfigurationCode;
	
	private boolean requestScreenConfigurationIsActive;

	private List<Integer> requestScreenConfigurationList;

	private String requestTypeName;

	private String requestSubTypeName;

	private List<RequestScreenDetailConfigurationVO> requestScreenDetailConfigurationVoList;

	private Integer requestTypeId;

	private Integer requestSubtypeId;
	
	private Integer[] deleteItem;
	
	private RequestScreenConfigurationVO requestScreenConfigurationVo;

	private String status;
	
}
