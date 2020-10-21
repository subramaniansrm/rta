package com.srm.rta.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;



@JsonInclude(JsonInclude.Include.NON_NULL) 
@Data
@EqualsAndHashCode(callSuper=false)
public class RequestScreenDetailConfigurationVO {
	
	private Integer requestScreenDetailConfigId;
	
	private Integer requestScreenConfigId;

	private String requestScreenDetailConfigurationFieldName;

	private String requestScreenDetailConfigurationFieldType;

	private String requestScreenDetailConfigurationFieldValue;
	
	private boolean requestScreenDetailConfigurationValidationIsRequired;
	
	private String requestScreenDetailConfigurationFieldStorageType;
	
	private boolean requestScreenDetailConfigurationIsMaster;
	
	private String requestScreenDetailConfigurationMasterCode;
	
	private Integer requestScreenDetailConfigurationSequance;
	
	private boolean requestScreenDetailConfigurationIsActive;

	private List<String> list_value;

}
