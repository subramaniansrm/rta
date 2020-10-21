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

@Data
@Entity
@Table(name = "rin_ma_widget_detail", schema = "rta_2_local")
public class WidgetDetailEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idrin_ma_widget_detail_id")
	private Integer widgetDetailId;// Id of WidgetDetail

	@Column(name = "rin_ma_widget_id")
	private Integer widgetId;// Id of Widget

	@Column(name = "rin_ma_widget_detail_heading")
	private String widgetDetailHeading;// Heading of WidgetDetail

	@Column(name = "rin_ma_widget_detail_heading_index")
	private Integer widgetDetailHeadingIndex;// Index of WidgetDetail

	@Column(name = "rin_ma_widget_detail_pic_is_required")
	private boolean widgetDetailPicIsRequired;

	@Column(name = "rin_ma_widget_detail_pic_path")
	private String widgetDetailPicPath;

	@Column(name = "rin_ma_widget_detail_description")
	private String widgetDetailDescription;

	@Column(name = "rin_ma_widget_detail_att_is_required")
	private boolean widgetDetailAttIsRequired;

	@Column(name = "rin_ma_widget_detail_att_path")
	private String widgetDetailAttPath;

	@Column(name = "rin_ma_widget_detail_more_path")
	private String widgetDetailMorePath;

	@Column(name = "rin_ma_widget_detail_external_url")
	private String widgetDetailExternalUrl;

	@Column(name = "rin_ma_widget_detail_is_active")
	private boolean widgetDetailIsActive;// 1-Active 0-Inactive

	@Column(name = "rin_ma_widget_detail_announcement_date")
	private Date widgetDetailAnnouncementDate;

	@Column(name = "rin_ma_widget_detail_valid_from")
	private Date widgetDetailValidFrom;

	@Column(name = "rin_ma_widget_detail_valid_to")
	private Date widgetDetailValidTo;

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
