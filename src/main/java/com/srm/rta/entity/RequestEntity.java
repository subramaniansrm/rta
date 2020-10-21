package com.srm.rta.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author manoj
 */
@Entity
@Table(name = "rin_tr_request", schema = "rta_2_local")
@Data
@EqualsAndHashCode(callSuper=false)
public class RequestEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idrin_tr_request_id")
	private Integer requestId;//Id of Request

	@Column(name = "rin_tr_request_code")
	private String requestCode;//Code of Request

	@Column(name = "rin_tr_request_user_location_id")
	private Integer id;//Id of Location

	@Column(name = "rin_tr_request_location_id")
	private Integer reqLocationId;
	
	@Column(name = "rin_tr_request_sub_location_id")
	private Integer reqSublocationId;
	
	@Column(name = "rin_tr_request_user_department_id")
	private Integer departmentId;//Id of Department

	@Column(name = "rin_tr_request_sublocation_id")
	private Integer sublocationId;//Id of SubLocation

	@Column(name = "rin_tr_request_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date requestDate;

	@Column(name = "rin_tr_request_priority")
	private Integer requestPriority;

	@Column(name = "rin_tr_request_subject")
	private String requestSubject;

	@Column(name = "rin_tr_request_from_date")
	private Date requestFromDate;

	@Column(name = "rin_tr_request_to_date")
	private Date requestToDate;

	@Column(name = "rin_tr_request_is_cancel")
	private boolean requestIsCancel;// 0-Active 1-Inactive

	@Column(name = "rin_tr_request_mobile_no")
	private String requestMobileNo;

	@Column(name = "rin_tr_request_extension")
	private Integer requestExtension;

	@Column(name = "current_status_id")
	private Integer currentStatusId;//Id of CurrentStatus

	@Column(name = "rin_tr_request_sequence")
	private String requestSeq;
	
	@Column(name = "rin_tr_request_sub_id")
	private Integer requestSubId;

	@Column(name = "rin_ma_request_type_id")
	private Integer requestTypeId;//Id of Request Type

	@Column(name = "rin_ma_request_subtype_id")
	private Integer requestSubtypeId;//Id of Request Subtype
	
	
	@Column(name = "rin_tr_request_attachment")
	private String requestAttachment;//Id of Request Subtype

	@JoinColumn(name = "rin_tr_request_id")
	@OneToMany(targetEntity = RequestDetailEntity.class, cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	private List<RequestDetailEntity> requestDetailEntityList;// List of RequestDetail

	@Column(name = "rin_ma_entity_id")
	private Integer entityLicenseId;
	
	@Column(name = "rin_tr_subrequest")
	private Integer subrequestId;
	
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

	@Column(name = "forward_request_id")
	private Integer forwardRequestId;
	
	@Column(name = "remarks")
	private String remarks;
	
	@Column(name = "forward_redirect_remarks")
	private String forwardRedirectRemarks;
		
}
