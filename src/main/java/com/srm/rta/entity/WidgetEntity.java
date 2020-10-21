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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rin_ma_widget", schema = "rta_2_local")
public class WidgetEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idrin_ma_widget_id")
	private Integer widgetId;// Id of Widget

	@Column(name = "rin_ma_widget_code")
	private String widgetCode;// Code of Widget

	@Column(name = "rin_ma_widget_index")
	private Integer widgetIndex;// Index of Widget

	@Column(name = "rin_ma_widget_title")
	private String widgetTitle;// Title of Widget

	@Column(name = "rin_ma_widget_icon")
	private String widgetIcon;

	@Column(name = "rin_ma_widget_seq")
	private Integer widgetSeq;

	@Column(name = "rin_ma_widget_is_active")
	private boolean widgetIsActive;// Title of Widget

	@JoinColumn(name = "rin_ma_widget_id")
	@OneToMany(targetEntity = WidgetDetailEntity.class, cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	private List<WidgetDetailEntity> widgetDetailEntityList;// List of WidgetDetails

	@Column(name = "rin_ma_entity_id")
	private int entityLicenseId;

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
