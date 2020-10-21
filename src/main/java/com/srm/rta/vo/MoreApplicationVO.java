package com.srm.rta.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MoreApplicationVO {
	
	 private Integer moreApplicationId;
	 private String moreApplicationName;
	 private String logo;
	 private String utl;
	 private Integer sequnce;

}
