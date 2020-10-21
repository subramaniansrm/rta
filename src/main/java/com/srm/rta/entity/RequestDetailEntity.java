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
import lombok.EqualsAndHashCode;

/**
 *
 * @author manoj
 */
@Entity
@Table(name = "rin_tr_request_detail", schema = "rta_2_local")
@Data
@EqualsAndHashCode(callSuper=false)
public class RequestDetailEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idrin_tr_request_detail_id")
	private Integer requestDetailId;

	@Column(name = "rin_tr_request_id")
	private Integer requestId;

	@Column(name = "rin_tr_request_detail_field_type")
	private String requestScreenDetailConfigurationFieldType;

	@Column(name = "rin_tr_request_detail_field_value")
	private String requestScreenDetailConfigurationFieldValue;

	@Column(name = "rin_tr_request_detail_is_active")
	private boolean requestScreenDetailConfigurationIsActive;

	@Column(name = "rin_ma_req_screen_config_id")
	private Integer requestScreenConfigId;

	@Column(name = "rin_ma_req_screen_detail_config_id")
	private Integer requestScreenDetailConfigId;
	
	@Column(name = "rin_tr_request_detail_is_cancel")
	private boolean requestDetailIsCancel;


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
