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
@Table(name = "rin_tr_req_workflow_audit", schema = "rta_2_local")
public class RequestWorkFlowAuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idrin_tr_req_workflow_audit_id")
	private Integer requestWorkFlowAuditId;

	@Column(name = "rin_ma_req_workflow_id")
	private Integer workFlowId;

	@Column(name = "rin_tr_req_workflow_seq_id")
	private Integer seqId;

	@Column(name = "rin_tr_req_workflow_audit_user_id")
	private Integer userId;

	@Column(name = "rin_tr_req_workflow_audit_group_id")
	private Integer groupId;
	
	@Column(name = "rin_tr_req_workflow_audit_sequence")
	private Integer sequence;

	@Column(name = "rin_tr_request_id")
	private Integer requestId;

	@Column(name = "rin_tr_req_workflow_audit_descision_type")
	private int descisionType; //Dont change to integer

	@Column(name = "rin_tr_req_workflow_audit_approval_executer")
	private Integer approvalExecuter;

	@Column(name = "rin_tr_req_workflow_audit_reassign_flag")
	private Integer reassignFlag;
	
	@Column(name = "rin_tr_req_workflow_audit_reassign_user_id")
	private Integer reassignUserId;
	
	@Column(name = "rin_tr_req_workflow_audit_remarks")
	private String remarks;

	@Column(name = "rin_tr_req_workflow_audit_is_active")
	private boolean requestWorkflowAuditIsActive;
	
	@Column(name = "rin_tr_req_workflow_audit_sla")
	private Integer requestWorkflowAuditSla;
	
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

	@Column(name = "rin_tr_req_workflow_audit_forward_remarks")
	private String auditForwardRemarks;	
		
}
