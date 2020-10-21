package com.srm.rta.vo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.srm.coreframework.vo.CommonVO;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestVO extends CommonVO {
	private Integer requestId;// Id of Request
	private Integer requestTypeId;// Id of Request Type
	private Integer requestSubtypeId;// Id of Request Subtype
	private String requestCode;// Code of Request
	private Integer id;// Id of Location
	private Integer sublocationId;// Id of SubLocation
	private Integer reqLocationId;
	private Integer reqSublocationId;
	private String reqLocationName;
	private String reqSublocationName;
	private Integer departmentId;// Id of Department
	private String requestTypeName;// Name of Request Type
	private String requestSubTypeName;// Name of Request Subtype
	private String locationName;// Name of Location
	private String sublocationName;// Name of SubLocation
	private String userDepartmentName;// Name of Department
	private Date requestDate;
	private Integer requestPriority;
	private String requestSubject;
	private Date requestFromDate;
	private Integer requestSubId;
	private Date requestToDate;
	private boolean requestIsCancel;// 0-Active 1-Inactive
	private String requestMobileNo;
	private Integer requestExtension;
	private Integer currentStatusId;// Id of CurrentStatus
	private String currentStatusName;// Name of CurrentStatus
	private String currentStatusCode;
	private String requestSeq;
	private List<Integer> requestIdList;// List of RequestId
	private RequestVO request;// Id of Request
	private Date updatedDate;
	private Date createdDate;
	private Integer descisionType;
	private Integer count;

	private List<RequestDetailVO> requestDetailList;// List of RequestDetail
	private RequestSubTypeVO requestSubTypeVo;
	private RequestTypeVO requestTypeVo;
	private Integer pageLimit;
	private Integer pageNo;
	private Integer totalRecords;
	private Integer userId;
	private String userName;
	private float sla;
	private Integer seqLevelType;
	private Integer slaType;
	private String email;
	private RequestWorkFlowAuditVO requestWorkFlowAuditVo;
	private List<RequestWorkFlowAuditVO> requestWorkFlowAuditVoList;
	private List<RequestTypeVO> requestTypeVoList;
	private String remarks;
	private Integer button;

	private RequestDetailVO requestDetailVo;
	private HashMap<Integer, String> requestList;
	private Integer createdBy;
	private List<RequestWorkFlowAuditVO> reSubmitList;

	private String requestAttachment;

	private Integer subrequestId;

	private Date listFromDate;

	private Date listToDate;

	private Integer entityId;
	
	private Integer forwardRequestId;
	
	private Integer redirectRequestId;
	
	private Integer newRequestId;// Id of Request

	private String forwardRedirectRemarks;

	private String resolverRemarks;
	
	private Integer flag;
	private Integer reassignFlag;
}
