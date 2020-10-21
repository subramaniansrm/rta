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
@Table(name = "rin_ma_more_application", schema = "rta_2_local")
public class MoreApplicationEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idrin_ma_more_application_id")
	private Integer moreApplicationId;
	@Column(name = "rin_ma_more_application_name")
	private String moreApplicationName;
	
	@Column(name = "rin_ma_more_application_logo")
	private String logo;
	
	@Column(name = "rin_ma_more_application_url")
	private String url;
	
	@Column(name = "rin_ma_more_application_seq")
	private Integer sequnce;
	
	@Column(name = "rin_ma_more_application_active")
	private boolean moreApplicationIsActive;


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
