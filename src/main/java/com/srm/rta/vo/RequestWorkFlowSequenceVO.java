package com.srm.rta.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestWorkFlowSequenceVO {
	
	private Integer reqWorkFlowSeqId; // Id of requestWorkFlowSequence

	private Integer reqWorkFlowId; // Id of requestWorkFlow

	private Integer reqWorkFlowSeqSequence; // Number of Sequence

	private Integer locationId; // Id of Location

	private Integer sublocationId; // Id of Sublocation

	private Integer userDepartmentId; // Id of userDepartment

	private Integer userRoleId; // Id of userRole

	private Integer userId;

	// 0-None 1-ReportingTo 2-Hierarchy 3-RestrictionHierarchy
	// 4-DepartmentHead
	private Integer reqWorkFlowSeqLevelType;

	private Integer reqWorkFlowSeqLevelhierarchy; // Level of Restriction Hierarchy

	private boolean reqWorkFlowSeqIsActive; // 0-Active 1-Inactive

	private List<RequestWorkFlowSlaVO> requestWorkFlowSlaVo;

	private Integer slaType;

	private float workFlowSla;

	private boolean approverWeekend;
	private boolean approverHoliday;		
}
