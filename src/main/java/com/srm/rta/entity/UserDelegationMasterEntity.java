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

@Entity
@Table(name = "rin_ma_user_delegation", schema = "rta_2_local")
@Data
@EqualsAndHashCode(callSuper=false)
public class UserDelegationMasterEntity {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idrin_ma_user_delegation_id")
	private Integer delegationId;//delegationId

	@Column(name = "idrin_ma_delegation_userid")
	private Integer delegationUserId;//Delegated User Id
	
	
	@Column(name = "idrin_ma_user_delegation_active")
	private Character delegationActive;
	
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
	
	@Column(name = "rin_ma_entity_id")
	private Integer entityLicenseId;
	

	

	
	
	

}
