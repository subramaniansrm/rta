/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srm.rta.entity;

import java.io.Serializable;

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
@Table(name = "mail_message")
@XmlRootElement
public class MailmessageEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "mailmessage_number")
	private Long mailmessageNumber;
	@Basic(optional = false)
	@Column(name = "message_code")
	private String messageCode;
	@Basic(optional = false)
	@Column(name = "group_id")
	private String groupId;
	@Column(name = "message")
	private String message;
	@Basic(optional = false)
	@Column(name = "title")
	private String title;
	@Basic(optional = false)
	@Column(name = "register_datetime")
	private String registerDatetime;
	@Basic(optional = false)
	@Column(name = "last_update_datetime")
	private String lastUpdateDatetime;
	@Basic(optional = false)
	@Column(name = "create_user_id")
	private String createUserId;
	@Column(name = "rin_ma_entity_id")
	private int entityLicenseId;

}
