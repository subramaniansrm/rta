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
@Table(name = "rin_ma_user_delegation_details", schema = "rta_2_local")
@Data
@EqualsAndHashCode(callSuper=false)
public class UserDelegationDetailsEntity  {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idrin_ma_user_delegation_detail_id")
	private Integer delegationDetailId;//delegationId
	
	@Column(name = "idrin_ma_user_delegation_id")
	private Integer delegationId;//Delegated User Id
	
	
	@Column(name = "idrin_ma_delegated_user_id")
	private Integer delegatedUserId;//Delegated User Id
	

	@Column(name = "idrin_ma_user_delegation_active")
	private boolean delegatedUserActive;//Delegated User Id
	
	@Column(name = "idrin_ma_user_type")
	private Integer userType;//Delegated User Id
	
	
	@Column(name = "idrin_ma_user_active_from")
	private Date userActiveFrom;//Delegated User Id
	
	@Column(name = "idrin_ma_user_active_to")
	private Date userActiveTo;

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
	
	@Column(name = "delegation_remarks")
	private String delegationRemarks;

}
