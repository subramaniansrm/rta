package com.srm.rta.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.srm.coreframework.vo.CommonVO;

import lombok.Data;
import lombok.EqualsAndHashCode;


@JsonInclude(JsonInclude.Include.NON_NULL) 
@Data
@EqualsAndHashCode(callSuper=false)
public class RequestSummaryVO extends CommonVO {
	
private String requestSubtypeName;
	
	private Integer subtypeCount;
	
    private String currentStatusName;
	
	private Integer currentStatusCount;
	
	private List<RequestSummaryVO> subtypeList;
	
	private List<RequestSummaryVO> statuswiseList;

}
