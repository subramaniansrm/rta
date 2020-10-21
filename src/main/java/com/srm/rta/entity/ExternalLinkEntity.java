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
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@Entity
@Table(name = "rin_ma_external_link", schema = "rta_2_local")
@XmlRootElement
@Data
public class ExternalLinkEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@Column(name = "IDRIN_MA_EXTERNAL_LINK_ID")
	private Integer id;// Id of ExternalLink

	@Column(name = "RIN_MA_EXTERNAL_LINK_NAME")
	private String externalLinkName;// Name of ExternalLink

	@Column(name = "RIN_MA_EXTERNAL_LINK_LOGO")
	private String externalLinkLogo;// Logo of ExternalLink

	@Column(name = "RIN_MA_EXTERNAL_LINK_URL")
	private String externalLinkUrl;// URL of ExternalLink

	@Column(name = "RIN_MA_EXTERNAL_LINK_DISPLAY_SEQ")
	private Integer externalLinkDisplaySeq;// Display Sequence of ExternalLink

	@Column(name = "RIN_MA_EXTERNAL_LINK_IS_ACTIVE")
	private boolean externalLinkIsActive;// 1-Active,0-Inactive

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
