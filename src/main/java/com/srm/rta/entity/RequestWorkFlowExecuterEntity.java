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

@Data
@Entity
@EqualsAndHashCode(callSuper=false)
@Table(name = "rin_ma_req_workflow_executer", schema = "rta_2_local")
public class RequestWorkFlowExecuterEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idrin_ma_req_workflow_executer_id")
	private Integer reqWorkFlowExecuterId;

	@Column(name = "rin_ma_request_workflow_id")
	private Integer reqWorkFlowId;
	
	@Column(name = "rin_ma_req_executer_department_id")
	private Integer executerDepartmentId;

	@Column(name = "rin_ma_req_executer_location_id")
	private Integer executerLocationId;

	@Column(name = "rin_ma_req_executer_sublocation_id")
	private Integer executerSublocationId;

	@Column(name = "rin_ma_req_executer_role_id")
	private Integer executerRoleId;
	
	@Column(name = "rin_ma_req_executer_user_id")
	private Integer executerUserId  ;

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