package com.srm.rta.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.srm.coreframework.vo.CommonVO;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApprovalVO extends CommonVO {
	
	private Integer requestId;// Id of Request
	
	private String requestCode;// Code of Request
	
	private String requestTypeName;// Name of Request Type
	
	private String requestSubTypeName;// Name of Request Subtype
	
	private Date requestDate;
	
	private String currentStatusName;// Name of CurrentStatus
	
	private Integer descisionType;
	
	private String userName;
	
	private Integer requestPriority;
	
	private String forwardRedirectRemarks;
	
	private String resolverRemarks;
	
	private Integer currentStatusId;

	
	private String locationName;
	
	private String sublocationName;
	
	private String userDepartmentName;
	
	private Integer forwardRequestId;
	private Integer redirectRequestId;
	private Integer flag;	
	private String currentStatusCode;
	 
}
