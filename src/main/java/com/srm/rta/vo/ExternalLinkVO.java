package com.srm.rta.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.srm.coreframework.vo.CommonVO;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExternalLinkVO  extends CommonVO{
	private Integer id;// Id of ExternalLink

	private String externalLinkName;// Name of ExternalLink

	private String externalLinkLogo;// Logo of ExternalLink

	private String externalLinkUrl;// URL of ExternalLink

	private Integer externalLinkDisplaySeq;// Display Sequence of ExternalLink

	private boolean externalLinkIsActive;// 1-Active,0-Inactive

	private List<Integer> idList;// List of ExternalLink Id

	private Integer[] deleteItem;

	private String status;
	
	private byte[] externalLinkLogoImage;
}
