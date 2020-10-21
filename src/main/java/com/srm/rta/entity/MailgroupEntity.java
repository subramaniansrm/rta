/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srm.rta.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@Data
@Entity
@Table(name = "mail_group")
@XmlRootElement
public class MailgroupEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "mailgroup_number")
	private Long mailgroupNumber;
	@Basic(optional = false)
	@Column(name = "group_id")
	private String groupId;
	@Basic(optional = false)
	@Column(name = "mail_number")
	private long mailNumber;
	@Basic(optional = false)
	@Column(name = "mail_type")
	private Character mailType;
	@Column(name = "email_address")
	private String emailAddress;
	@Basic(optional = false)
	@Column(name = "register_datetime")
	private String registerDatetime;
	@Basic(optional = false)
	@Column(name = "last_update_datetime")
	private String lastUpdateDatetime;
	
	@Column(name = "rin_ma_entity_id")
	private int entityLicenseId;
	
	

}
