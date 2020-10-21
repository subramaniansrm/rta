package com.srm.rta.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestWorkFlowExecuterVO {

	private Integer reqWorkFlowExecuterId;

	private Integer reqWorkFlowId; // Id of Request WorkFlow

	private Integer executerDepartmentId; // Id of executerDepartment

	private Integer executerLocationId; // Id of executerLocation

	private Integer executerSublocationId; // Id of executerSublocation

	private Integer executerRoleId; // Id of executerRole

	private Integer executerUserId;

	private String executerDepartmentName; // Name of executerDepartment

	private String executerLocationName; // Name of executerLocation

	private String executerSublocationName; // Name of executerSubocation

	private String executerRoleName; // Name of executerRole

	private String executerUserName;

	private boolean reqWorkFlowExecuterIsActive; // 1-Active 0-Inactive
	
	private Integer slaType;

	private float workFlowSla;

	private Integer entityId;
	
	private boolean executorWeekend;
	
	private boolean executorHoliday;

	private List<RequestWorkFlowSlaVO> requestWorkFlowSlaVo;

	
}
