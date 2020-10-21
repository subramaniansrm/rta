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
@Table(name = "rin_ma_req_workflow_seq", schema = "rta_2_local")
public class RequestWorkFlowSeqEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idrin_ma_req_workflow_seq_id")
	private Integer reqWorkFlowSeqId;
	
	@Column(name = "rin_ma_req_workflow_id")
	private Integer reqWorkFlowId;
	
	@Column(name = "rin_ma_req_workflow_seq_sequence")
	private Integer reqWorkFlowSeqSequence;
	
	@Column(name = "rin_ma_req_workflow_seq_location_id")
	private Integer locationId;
	
	@Column(name = "rin_ma_req_workflow_seq_sublocation_id")
	private Integer sublocationId;	
	
	@Column(name = "rin_ma_req_workflow_seq_department_id")
	private Integer userDepartmentId;
	
	@Column(name = "rin_ma_req_workflow_seq_role_id")
	private Integer userRoleId;
	
	@Column(name = "rin_ma_req_workflow_seq_user_id")
	private Integer userId;
			
	@Column(name = "rin_ma_req_workflow_seq_level_type")
	private Integer reqWorkFlowSeqLevelType;
		
	@Column(name = "rin_ma_req_workflow_seq_level_hierarchy")
	private Integer reqWorkFlowSeqLevelhierarchy;

	@Column(name = "rin_ma_req_workflow_seq_is_active")
	private boolean reqWorkFlowSeqIsActive;
	
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
