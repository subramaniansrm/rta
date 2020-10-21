package com.srm.rta.entity;

/**
*
*/

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

@Entity
@Table(name = "rin_ma_flash_news", schema = "rta_2_local")
@Data
@EqualsAndHashCode(callSuper=false)
public class RinFlashNewsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "IDRIN_MA_FLASH_NEWS_ID")
	private Integer id;//Id of FlashNews

	@Column(name = "RIN_MA_FLASH_NEWS_CODE")
	private String flashNewsCode;//Id of FlashNews

	@Column(name = "RIN_MA_FLASH_NEWS_TYPE")
	private Integer flashNewsType;// 1-FlashNews 2-Thought for the day

	@Column(name = "RIN_MA_FLASH_NEWS_DESCRIPTION")
	private String flashNewsDescription;

	@Column(name = "RIN_MA_FLASH_NEWS_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date flashNewsDate;

	@Column(name = "RIN_MA_FLASH_NEWS_VALID_FROM")
	@Temporal(TemporalType.TIMESTAMP)
	private Date flashNewsValidFrom;

	@Column(name = "RIN_MA_FLASH_NEWS_VALID_TO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date flashNewsValidTo;

	@Column(name = "RIN_MA_FLASH_NEWS_IS_ACTIVE")
	private Boolean isFlashNewsActive;// 0-Active 1-Inactive
	
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
