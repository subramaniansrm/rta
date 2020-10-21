package com.srm.rta.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileEditVO {
	
private Integer id;
	
	private String employeeId;
	
	private String firstName;
	
	private String middleName;
	
	private String lastName;
	
	private Integer phoneBookId;
	
	private String skypeId;
	
	private String emailId;
	
	private String mobile;
	
	private String phoneBookProfile;
	
	private String currentAddress;
	
	private String permanentAddress;
	
	private Integer locationId;
	
	private Integer sublocationId;
	
	private Integer departmentId;
	
	private String locationName;
	
	private String subLocationName;
	
	private String departmentName;
	
	private String langCode;

	private String langCodeValue;
	
	private byte[] imageLoad;
}
