package com.srm.rta.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@Entity
@Table(name = "rin_ma_req_screen_detail_config", schema = "rta_2_local")
public class RequestScreenDetailConfigurationEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idrin_ma_req_screen_detail_config_id")
	private Integer requestScreenDetailConfigId;

	
	@Column(name = "rin_ma_req_screen_detail_config_field_name")
	private String requestScreenDetailConfigurationFieldName; //

	
	@Column(name = "rin_ma_req_screen_detail_config_field_type")
	private String requestScreenDetailConfigurationFieldType;
	
	@Column(name = "rin_ma_req_screen_detail_config_field_value")
	private String requestScreenDetailConfigurationFieldValue;

	
	@Column(name = "rin_ma_req_screen_detail_config_validation_is_required")
	private boolean requestScreenDetailConfigurationValidationIsRequired;

	

	@Column(name = "rin_ma_req_screen_detail_config_field_storage_type")
	private String requestScreenDetailConfigurationFieldStorageType;

	
	@Column(name = "rin_ma_req_screen_detail_config_is_master")
	private boolean requestScreenDetailConfigurationIsMaster;

	
	@Column(name = "rin_ma_req_screen_detail_config_master_code")
	private String requestScreenDetailConfigurationMasterCode;

	
	@Column(name = "rin_ma_req_screen_detail_config_sequance")
	private Integer requestScreenDetailConfigurationSequance;

	
	@Column(name = "rin_ma_req_screen_detail_config_is_active")
	private boolean requestScreenDetailConfigurationIsActive;

	@Column(name = "rin_ma_req_screen_config_id")
	private Integer requestScreenConfigId;

	@Column(name = "rin_ma_entity_id")
	private Integer entityLicenseId;
	
	@Column(name = "create_by")
	private Integer createBy;

	@Column(name = "create_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	@Column(name = "update_by")
	private Integer updateBy;

	@Column(name = "update_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;

	@Column(name = "delete_flag")
	private Character deleteFlag;

}
