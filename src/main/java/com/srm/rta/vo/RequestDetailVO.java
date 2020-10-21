package com.srm.rta.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestDetailVO {
	
	private Integer requestDetailId;
	private Integer requestId;
	private Integer requestScreenConfigId;
	private Integer requestScreenDetailConfigId;
	private String requestScreenDetailConfigurationFieldType;
	private String requestScreenDetailConfigurationFieldValue;
	private boolean requestScreenDetailConfigurationIsActive;
	private String requestScreenDetailConfigurationFieldName;
	private boolean requestScreenDetailConfigurationValidationIsRequired;
	private String requestScreenDetailConfigurationFieldStorageType;
	private boolean requestScreenDetailConfigurationIsMaster;
	private String  requestScreenDetailConfigurationMasterCode;
	private Integer requestScreenDetailConfigurationSequance;
	private List<String> objectList;
	private List<String> list_value;

}
