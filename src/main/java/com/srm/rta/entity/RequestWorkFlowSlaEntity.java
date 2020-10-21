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
@Table(name = "rin_ma_req_workflow_sla", schema = "rta_2_local")
public class RequestWorkFlowSlaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idrin_ma_req_workflow_sla_id")
	private Integer reqWorkFlowSlaId;
	
	@Column(name = "rin_ma_req_workflow_id")
	private Integer reqWorkFlowId;
	
	@Column(name = "idrin_ma_req_workflow_seq_id")
	private Integer reqWorkFlowSeqId;
	
	@Column(name = "idrin_ma_req_workflow_executer_id")
	private Integer reqWorkFlowExecuterId;
	
	@Column(name = "rin_ma_req_workflow_type")
	private Integer type;
	
	@Column(name = "rin_ma_req_workflow_prototype")
	private Integer protoType;	
	
	@Column(name = "rin_ma_req_workflow_sla")
	private float reqWorkFlowSla;
	
	@Column(name = "rin_ma_req_workflow_sla_type")
	private Integer reqWorkFlowSlaType;
	
	@Column(name = "rin_ma_req_workflow_sla_resolution")
	private Integer reqWorkFlowSlaResolution;
	
	@Column(name = "rin_ma_req_workflow_sla_respond")
	private float reqWorkFlowSlaRespond;
			
	@Column(name = "rin_ma_req_workflow_sla_is_active")
	private boolean reqWorkFlowSlaIsActive;
	
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
	
	@Column(name = "rin_ma_req_weekend_flag")
	private boolean reqWeekendFlag;
		
	@Column(name = "rin_ma_req_holiday_flag")
	private boolean reqHolidayFlag;
	
}
