/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srm.rta.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.srm.coreframework.entity.EntityLicense;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 */
@Entity
@Table(name = "holiday_detail", schema = "rta_2_local")
@Data
@EqualsAndHashCode(callSuper=false)
public class HolidayDetails implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "HOLIDAY_DETAILS_ID")
	private Integer holidayDetailsId;

	@Column(name = "HOLIDAY_ID")
	private Integer holidayId;

	@Column(name = "ACTIVE_FLAG")
	private boolean activeFlag;

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

	@Column(name = "DEPARTMENT_ID")
	private Integer departmentId;

/*	@JoinColumn(name = "LOCATION_ID", referencedColumnName = "USER_LOCATION_ID")
	@ManyToOne
	private UserLocation userLocationEntity;

	@JoinColumn(name = "SUB_LOCATION_ID", referencedColumnName = "idrin_ma_sublocation_sublocationId")
	@ManyToOne
	private SubLocation subLocationEntity;*/
	
	@Column(name = "LOCATION_ID")
	private Integer locationId;
	
	@Column(name = "SUB_LOCATION_ID")
	private Integer subLocationId;

	/*@JoinColumn(name = "rin_ma_entity_id", referencedColumnName = "idrin_ma_entity_id")
	@ManyToOne
	private EntityLicense entityLicenseEntity;*/
	@Column(name = "rin_ma_entity_id")
	private Integer entityLicenseId;

}
