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
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;


@Entity
@Table(name = "rin_ma_request_subtype",schema = "rta_2_local")
@XmlRootElement
@Data
public class RequestSubTypeEntity {
 
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idrin_ma_request_subtype_id")
	private Integer requestSubTypeId;

	@Column(name = "rin_ma_request_subtype_code")
	private String requestSubTypeCode;

	@Column(name = "rin_ma_request_subtype_name")
	private String requestSubTypeName;

	@Column(name = "rin_ma_request_subtype_is_active")
	private Integer requestSubTypeIsActive;

	@Column(name = "rin_ma_request_type_id")
	private Integer requestTypeId;
	
	@Column(name = "rin_ma_request_subtype_priorty")
	private Integer requestSubtypePriorty;

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
