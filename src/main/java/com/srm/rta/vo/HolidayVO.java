package com.srm.rta.vo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.srm.coreframework.vo.CommonVO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HolidayVO extends CommonVO {

	private Integer id;

	private String description;

	private Date holidayDate;

	private Integer leaveType;
	
	private List<HolidayDetailsVO> holidayDetailsList;

	private String month;

	private Integer[] deleteItem;


}
