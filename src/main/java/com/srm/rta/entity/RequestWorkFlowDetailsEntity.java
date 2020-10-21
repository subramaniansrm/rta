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
@Table(name = "rin_ma_req_workflow_details", schema = "rta_2_local")
public class RequestWorkFlowDetailsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idrin_ma_req_workflow_details_id")
	private Integer reqWorkFlowDetailsId;

	@Column(name = "rin_ma_req_workflow_id")
	private Integer reqWorkFlowId;

	@Column(name = "rin_ma_req_workflow_department_id")
	private Integer workFlowDepartmentId;

	@Column(name = "rin_ma_req_workflow_location_id")
	private Integer workFlowLocationId;

	@Column(name = "rin_ma_req_workflow_sublocation_id")
	private Integer workFlowSublocationId;
	
	@Column(name = "rin_ma_req_workflow_details_is_active")
	private boolean reqWorkFlowDetailsIsActive;

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