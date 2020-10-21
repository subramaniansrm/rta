package com.srm.rta.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DashBoardCurrentStatusVO {

	private Integer  count;
	private String status;
	private Integer total;
	private Integer currentStatusId;
	
}
