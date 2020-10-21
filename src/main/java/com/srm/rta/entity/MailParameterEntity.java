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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "mail_parameter", schema = "rta_2_local")
public class MailParameterEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "mail_parameter_id")
	private Integer mailParameterId;

	@Column(name = "message_code")
	private String messageCode;
	
	@Column(name = "mail_log_id")
	private Integer mailLogId;

	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "message")
	private String message;

	@Column(name = "title")
	private String title;

	@Column(name = "request_id")
	private Integer requestId;

	@Column(name = "assign_user_id")
	private Integer assignUserId;

	@Column(name = "email_flag")
	private Integer emailFlag;
	
	@Column(name = "escalation_flag")
	private Integer escalationFlag;
	
	@Column(name = "rin_ma_entity_id")
	private Integer entityId;
	
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
