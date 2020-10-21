package com.srm.rta.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.srm.coreframework.vo.CommonVO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HolidayDetailsVO extends CommonVO{

	private Integer holidayDetailsId;
	
	private Integer holidayId;
	
	private Boolean activeFlag;

	private Integer locationId;

	private String locationName;

	private Integer sublocationId;

	private String sublocationName;

	private Integer departmentId;

	private String departmentName;

	private String month;

	private Integer[] deleteItem;

	
}
