package com.srm.rta.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.srm.coreframework.vo.CommonVO;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestWorkFlowVO extends CommonVO {
	
	
	private Integer reqWorkFlowId; // Id of Request WorkFlow

	private String requestWorkFlowCode; // code of requestWorkFlow

	private Integer requestTypeId; // Id of requestType

	private Integer requestSubTypeId; // Id of requestSubType

	private String requestTypeName; // Name of requestType

	private String requestSubTypeName; // Name of requestSubTye

	private boolean reqWorkFlowIsEscalationRequired;
	
	private Integer slaConfigure;
	
	private Integer defaultCommon;

	private boolean reqWorkFlowIsMailRequired;

	private boolean reqWorkFlowIsNotificationRequired;

	private boolean reqWorkFlowIsMgtEscalationRequired;

	private String reqWorkFlowDescription; // Description of workFlow

	private boolean reqWorkFlowIsActive; // 1-Active 0-Inactive
	
	private boolean reqWorkFlowReassign; // 1-Reassign 0-Not Reassign

	private List<Integer> requestWorkFlowList; // List of requestworkFlowId
	
	private String status;

	private Integer[] deleteItem;
	
	private Integer slaType;
	
	private float workFlowSla;

	// List of requestWorkFlowSequence
	private List<RequestWorkFlowSequenceVO> requestWorkFlowSequenceList;
		
	private List<RequestWorkFlowDetailsVO> requestWorkFlowDetailsVoList;

	private List<RequestWorkFlowExecuterVO> requestWorkFlowExecuterVo;

	private boolean executorWeekend;
	private boolean executorHoliday;
	private boolean approverWeekend;
	private boolean approverHoliday;
	
}
