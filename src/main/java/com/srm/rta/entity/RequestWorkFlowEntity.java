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
@Table(name = "rin_ma_req_workflow", schema = "rta_2_local")
public class RequestWorkFlowEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idrin_ma_req_workflow_id")
	private Integer reqWorkFlowId;

	@Column(name = "rin_ma_request_workflow_code")
	private String requestWorkFlowCode;

	@Column(name = "rin_ma_request_type_id")
	private Integer requestTypeId;

	@Column(name = "rin_ma_request_subtype_id")
	private Integer requestSubTypeId;
		
	@Column(name = "rin_ma_req_workflow_sla_configure")
	private Integer slaConfigure;
		
	@Column(name = "rin_ma_req_workflow_is_active")
	private boolean reqWorkFlowIsActive;

	@Column(name = "rin_ma_req_workflow_is_mail_required")
	private boolean reqWorkFlowIsMailRequired;

	@Column(name = "rin_ma_req_workflow_is_notification_required")
	private boolean reqWorkFlowIsNotificationRequired;

	@Column(name = "rin_ma_req_workflow_is_mgt_escaltion_required")
	private boolean reqWorkFlowIsMgtEscalationRequired;
	
	@Column(name = "rin_ma_req_workflow_description")
	private String reqWorkFlowDescription;
	
	@Column(name = "rin_ma_req_workflow_executer_reassign")
	private boolean reqWorkFlowReassign;

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