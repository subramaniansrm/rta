package com.srm.rta.vo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.srm.coreframework.vo.CommonVO;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestWorkFlowAuditVO extends CommonVO{
	
	private Integer requestWorkFlowAuditId; // Id of requestWorkFlowAudit s

	private Integer workFlowId; // Id of WorkFlow

	private Integer workFlowReassign; // Id of WorkFlow

	private Integer seqId; // Id of sequence

	private Integer userId; // Id of user

	private String userName; // Name of user

	private Integer groupId; // Id of group

	private Integer requestId; // Id of request

	private Integer sequence; // Number of sequence

	//Dont change to integer
	private int descisionType; // 0-pending, 1- approved, 2-rejected

	private Integer approvalExecuter; // 1-approval, 2-executer

	private String remarks; // remark of workFlow Audit

	private boolean requestWorkflowAuditIsActive; // 0-Active, 1-Inactive

	private Integer reassignFlag;

	private Integer reassignUserId;

	private Date updatedDate;

	private Date approvalDate;

	private Date createdDate;

	private Integer status;// 0 - pending, 1 - sla Before Time, 1 - sla After Time

	private Integer minutes;
	private Integer slaType;
	private float sla;
	private String requsetsequence;

	private Integer subrequestId;
	private Integer reqLocationId;
	private Integer reqSublocationId;
	private Integer requestTypeId;
	private Integer requestSubTypeId;
	private Integer createdBy;
	
	private Integer updatedBy;
	
	private Integer entityId;
	
	private Integer locationId;
	private Integer subLocationId;
	private Integer departmentId;
	private Integer id;// Id of Location
	//private Integer sublocationId;// Id of SubLocation
	private Integer currentStatusId;// Id of CurrentStatus
	private Integer requestPriority;
	private List<RequestDetailVO> requestDetailList;// List of RequestDetail

	private Integer requestScreenConfigId;

	private Integer requestScreenDetailConfigId;
	
	private Integer requesterId;
	private String requestSubject;
	private Date requestFromDate;
	private Date requestToDate;
	private String requestMobileNo;
	private Integer requestExtension;
	private String requestAttachment;
	private String requestCode;// Code of Request
	
	private Integer originalRequestTypeId;
	private Integer originalRequestSubTypeId;  
	private Integer originalDepartmentId;
	private Integer forwardRequestId;
	private String forwardRemarks;
	
	private String redirectRequestTypeName;
	private String redirectRequestSubTypeName;
}
