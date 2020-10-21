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
@Table(name = "rin_ma_current_status", schema = "rta_2_local")
public class CurrentStatusEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idrin_ma_current_status_id")
	private Integer currentStatusId;
	
	@Column(name = "rin_ma_current_status_code")
	private String currentStatusCode;
	
	@Column(name = "rin_ma_current_status_name")
	private String currentStatusName;
	
	@Column(name = "rin_ma_current_status_is_active")
	private boolean CurrentStatusIsActive;
	
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
