package com.srm.rta.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestWorkFlowSlaVO {
	
	
	private Integer reqWorkFlowSlaId;

	private Integer reqWorkFlowId;
	
	private Integer reqWorkFlowSeqId = 0;
	
	private Integer reqWorkFlowExecuterId;
	
	private Integer type;
	
	private Integer protoType;	
	
	private float reqWorkFlowSla;
	
	private Integer reqWorkFlowSlaType;
	
	private Integer reqWorkFlowSlaResolution;
	
	private float reqWorkFlowSlaRespond;
			
	private boolean reqWorkFlowSlaIsActive;

}
