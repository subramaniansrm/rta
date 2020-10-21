package com.srm.rta.vo;

import java.util.Date;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.srm.coreframework.vo.CommonVO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = false)
public class UserDelegationMasterVO extends CommonVO {

	private Integer delegationId;

	private Integer delegationUserId;

	private String delegationActive;
	
	private String userName;
	
	private String delegatedUserName;
	
	private Date activeFrom;
	
	private Date activeTo;
	
	private Integer userType;
	
	private String userTypeName;
	
	
	private String status;
	
	private UserDelegationDetailsVO userDelegationDetailsVo;
	
	private List<UserDelegationDetailsVO> userDelegationDetailsVoList;
	
	private List<UserDelegationMasterVO> userDelegationMasterVoList;
	
	private String delegationRemarks;
	
	

}
