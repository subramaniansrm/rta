package com.srm.rta.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Table(name = "rin_ma_req_screen_config", schema = "rta_2_local")
@Data
public class RequestScreenConfigurationEntity {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idrin_ma_req_screen_config_id")
	private Integer requestScreenConfigId;

	@Column(name = "rin_ma_req_screen_config_name")
	private String requestScreenConfigurationName;

	@Column(name = "rin_ma_req_screen_config_code")
	private String requestScreenConfigurationCode;
	
	@Column(name = "rin_ma_req_screen_config_is_active")
	private boolean requestScreenConfigurationIsActive;

	@Column(name = "rin_ma_request_type_id")
	private Integer requestTypeId;
	
	@Column(name = "rin_ma_request_subtype_id")
	private Integer requestSubtypeId;

	@JoinColumn(name = "rin_ma_req_screen_config_id")
	@OneToMany(targetEntity = RequestScreenDetailConfigurationEntity.class, cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	private List<RequestScreenDetailConfigurationEntity> requestScreenDetailConfigurationEntityList;
	
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
