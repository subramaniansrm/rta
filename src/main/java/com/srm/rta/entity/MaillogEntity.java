/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srm.rta.entity;

import java.util.Date;

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
@Table(name = "mail_log")
@XmlRootElement
public class MaillogEntity {
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "maillog_number")
    private long maillogNumber;
    @Basic(optional = false)
    @Column(name = "message_code")
    private String messageCode;
    @Basic(optional = false)
    @Column(name = "title")
    private String title;
    @Column(name = "user_Id")
    private Integer userId;
    @Column(name = "message")
    private String message;
    @Column(name = "to_email_address")
    private String toEmailAddress;
    @Column(name = "cc_email_address")
    private String ccEmailAddress;
    @Column(name = "notification_flag")
    private int notificationFlag;
    @Basic(optional = false)
    @Column(name = "register_datetime")
    private Date registerDatetime;
    @Basic(optional = false)
    @Column(name = "last_update_datetime")
    private Date lastUpdateDatetime;
    @Basic(optional = false)
    @Column(name = "create_user_id")
    private int createUserId;
	@Column(name = "rin_ma_entity_id")
	private int entityLicenseId;
	    
		
}
