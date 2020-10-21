package com.srm.rta.vo;

import java.util.Date;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WidgetDetailVO {
	
	private Integer widgetDetailId;//Id of WidgetDetail
	private String widgetDetailHeading;//Heading of WidgetDetail
	private Integer widgetDetailHeadingIndex;//Index of WidgetDetail
	private boolean widgetDetailPicIsRequired;
	private String widgetDetailPicPath;
	private String widgetDetailDescription;
	private boolean widgetDetailAttIsRequired;
	private String widgetDetailAttPath;
	private String widgetDetailMorePath;
	private String widgetDetailExternalUrl;
	private boolean widgetDetailIsActive;// 1-Active 0-Inactive
	private Date widgetDetailAnnouncementDate;
	private Date widgetDetailValidFrom;
	private Date widgetDetailValidTo;
	private Integer widgetId;//Id of Widget
	
	private ResponseEntity<Resource> widgetDetailPicturePath;

	private byte[][] widgetDetailPicPathImageLoad;
	private byte[][] widgetDetailAttPathImageLoad;
	private byte[][] widgetDetailAttPathDownload;
	
}
