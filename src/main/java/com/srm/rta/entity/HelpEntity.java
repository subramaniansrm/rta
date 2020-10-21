package com.srm.rta.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "help")
@Data
public class HelpEntity {
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "HELP_ID")
	private Integer helpId;
	@Column(name = "TOPIC")
	private String topic;
	@Column(name = "DETAIL")
	private String detail;
	@Column(name = "rin_ma_entity_id")
	private Integer entityLicenseId;
	
	


}
