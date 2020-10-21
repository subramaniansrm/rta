package com.srm.rta.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.srm.coreframework.vo.CommonVO;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestResolverVO extends CommonVO {
	
	private Integer requestId;// Id of Request

	private String requestCode;// Code of Request

	private Date requestDate;
	
	private String requestTypeName;// Name of Request Type

	private String requestSubTypeName;// Name of Request Subtype

	private String locationName;// Name of Location

	private String sublocationName;// Name of SubLocation

	private String userDepartmentName;// Name of Department

	private String currentStatusName;

	private String userName;
	
	private Date listFromDate;

	private Date listToDate;
	
	private Integer requesterId;
	
	private String forwardRedirectRemarks;
	
	private Integer forwardRequestId;
	private Integer redirectRequestId;
	private String resolverRemarks;
	private Integer flag;
	private String currentStatusCode;
	
}