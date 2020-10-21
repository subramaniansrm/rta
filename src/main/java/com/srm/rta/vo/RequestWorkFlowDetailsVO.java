package com.srm.rta.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestWorkFlowDetailsVO {

	private Integer reqWorkFlowId; // Id of Request WorkFlow

	private Integer reqWorkFlowDetailsId; // code of requestWorkFlow
	
	private Integer workFlowDepartmentId; // Id of workFlowDepartment
	
	private List<Integer> workFlowDepartment;

	private Integer workFlowLocationId; // Id of workFlowLocation

	private Integer workFlowSublocationId; // Id of workFlowSublocation

	private String workFlowSublocationName; // Name of workFlowSublocation

	private String workFlowLocationName; // Name of workFlowLocation

	private String workFlowDepartmentName; // Name of workFlowDepartment
	
	private boolean reqWorkFlowDetailsIsActive; // 1-Active 0-Inactive
	
	private List<RequestWorkFlowDetailsVO> requestWorkFlowDetailsVoList;
	
	private Integer[] deleteItem;


}
