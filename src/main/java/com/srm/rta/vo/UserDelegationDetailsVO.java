package com.srm.rta.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = false)
public class UserDelegationDetailsVO {
	
	private Integer delegationDetailId;
	
	private Integer delegationId;
	
	private Integer delegatedUserId;
	
	private boolean delegatedUserActive;
	
	private Integer userType;
	
	private Date userActiveFrom;
	
	private Date userActiveTo;

	private String delegationRemarks;
}
