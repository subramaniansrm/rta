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
public class FlashNewsVO extends CommonVO {

	private Integer id;//Id of FlashNews
	
	private String flashNewsCode;//Id of FlashNews
	
	private Integer flashNewsType;// 1-FlashNews 2-Thought for the day
	
	private String flashNewsDescription;
	
	private Date flashNewsDate;
	
	private Date flashNewsValidFrom;
	
	private Date flashNewsValidTo;
	
	private Boolean isFlashNewsActive;// 1-Active 0-Inactive
	
	private List<Integer> idList;//List of FlashNewsId
	
	private String status;

}
