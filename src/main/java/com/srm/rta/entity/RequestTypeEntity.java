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

@Data
@Entity
@Table(name = "rin_ma_request_type",schema ="rta_2_local")
public class RequestTypeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idrin_ma_request_type_id")
	private Integer requestTypeId;
	@Column(name = "rin_ma_request_type_code")
	private String requestTypeCode;
	@Column(name = "rin_ma_request_type_name")
	private String requestTypeName;
	@Column(name = "rin_ma_request_type_url")
	private String requestTypeUrl;
	@Column(name = "rin_ma_request_type_is_active")
	private boolean requestTypeIsActive;

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

	
	@JoinColumn(name = "rin_ma_request_type_id")
	@OneToMany(targetEntity = RequestSubTypeEntity.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<RequestSubTypeEntity> RequestSubtypeEntityList;

	
}
