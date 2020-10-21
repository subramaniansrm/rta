package com.srm.rta.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.srm.coreframework.vo.CommonVO;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WidgetVO extends CommonVO {
	private Integer widgetId;// Id of Widget
	private String widgetCode;// Code of Widget
	private Integer widgetIndex;// Index of Widget
	private String widgetTitle;// Title of Widget
	private String widgetIcon;
	private Integer widgetSeq;
	private String status;// Title of Widget

	private boolean widgetIsActive;// 1-Active 0-Inactive
	private List<Integer> widgetsIdList;// List of WidgetId
	private WidgetVO widgetVo;
	private List<WidgetDetailVO> widgetDetailVoList;
	private byte[] widgetIconImage;

}

