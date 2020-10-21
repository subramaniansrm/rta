package com.srm.rta.validation;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.srm.coreframework.config.UserMessages;
import com.srm.coreframework.constants.ControlNameConstants;
import com.srm.coreframework.util.CommonConstant;
import com.srm.rta.vo.WidgetDetailVO;
import com.srm.rta.vo.WidgetVO;



/**
 * WidgetDetailValidation.java
 * 
 * This class is a Validation class for the WidgetDetail values.
 * 
 * @author manoj
 */
@Component
public class WidgetDetailValidation {

	@Autowired
	private UserMessages userMessagesProperties;

	/**
	 * This method is used to check whether any of the data in the list is empty
	 * or null
	 * 
	 * 
	 * @param List<WidgetDetailVo>
	 *            list_widgetDetailVo
	 * @return CommonConstants CommonConstants.VALIDATION_SUCCESS
	 */
	public String validateCreate(WidgetVO widgetVo) {
		List<WidgetDetailVO> list_widgetDetailVo = widgetVo.getWidgetDetailVoList();
		boolean result = false;
		for (WidgetDetailVO widgetDetailVo : list_widgetDetailVo) {

			for (String detailField : widgetVo.getScreenFieldDisplayVoList()) {
				if (ControlNameConstants.WIDGETS_HEADING.equals(detailField)) {

					result = validateHeading(widgetDetailVo.getWidgetDetailHeading().trim());
					if (result == false) {
						return userMessagesProperties.getWidgetDetail_validation_heading();
					}
					if (widgetDetailVo.getWidgetDetailHeading().length() > 50) {
						return userMessagesProperties.getWidgetDetail_validation_heading();
					}
				}
				if (ControlNameConstants.WIDGETS_DESCRIPTION.equals(detailField)) {
					result = validateDescript(widgetDetailVo.getWidgetDetailDescription().trim());
					if (result == false) {
						return userMessagesProperties.getWidgetDetail_validation_descript();
					}
					
				}
				/*
				 * result =
				 * validateUrl(widgetDetailVo.getWidgetDetailExternalUrl().trim(
				 * )); if (result == false) { return
				 * userMessagesProperties.getWidgetDetail_validation_externUrl()
				 * ; }
				 * 
				 * if (widgetDetailVo.getWidgetDetailExternalUrl().length() >
				 * 250) { return
				 * userMessagesProperties.getWidgetDetail_validation_externUrl()
				 * ; }
				 */

				if (ControlNameConstants.WIDGETS_DETAILS_STATUS.equals(detailField)) {
					result = validateActive(widgetDetailVo.isWidgetDetailIsActive());
					if (result == false) {
						return userMessagesProperties.getWidgetDetail_validation_isActive();
					}
				}
				if (ControlNameConstants.WIDGETS_ANNOUNCEMENT_DATE.equals(detailField)) {
					result = validateDate(widgetDetailVo.getWidgetDetailAnnouncementDate());
					if (result == false) {
						return userMessagesProperties.getWidgetDetail_validation_announceDate();
					}
				}
				if (ControlNameConstants.WIDGETS_VALID_FROM.equals(detailField)) {
					result = validateValidFrom(widgetDetailVo.getWidgetDetailValidFrom());
					if (result == false) {
						return userMessagesProperties.getWidgetDetail_validation_validFrom();
					}
				}
				if (ControlNameConstants.WIDGETS_VALID_TO.equals(detailField)) {
					result = validateValidTo(widgetDetailVo.getWidgetDetailValidTo());
					if (result == false) {
						return userMessagesProperties.getWidgetDetail_validation_validTo();
					}
				}
			}
			result = validFromAndTo(widgetDetailVo.getWidgetDetailValidFrom(), widgetDetailVo.getWidgetDetailValidTo());
			if (result == false) {
				return userMessagesProperties.getWidgetDetail_validFromAndTo_invalid();
			}

			result = announceDateBWFromAndTo(widgetDetailVo.getWidgetDetailValidFrom(),
					widgetDetailVo.getWidgetDetailValidTo(), widgetDetailVo.getWidgetDetailAnnouncementDate());
			if (result == false) {
				return userMessagesProperties.getWidget_announcement_date_should_be_available_in_between();
			}
		}

		return CommonConstant.VALIDATION_SUCCESS;
	}

	/**
	 * This method is used to check whether any of the data in the list is empty
	 * or null
	 * 
	 * 
	 * @param List<WidgetDetailVo>
	 *            list_widgetDetailVo
	 * @return CommonConstants CommonConstants.VALIDATION_SUCCESS
	 */
	public String validateUpdate(WidgetVO widgetVo) {

		List<WidgetDetailVO> list_widgetDetailVo = widgetVo.getWidgetDetailVoList();
		boolean result = false;

		for (WidgetDetailVO widgetDetailVo : list_widgetDetailVo) {

			for (String detailField : widgetVo.getScreenFieldDisplayVoList()) {
				if (ControlNameConstants.WIDGETS_HEADING.equals(detailField)) {

					result = validateHeading(widgetDetailVo.getWidgetDetailHeading().trim());
					if (result == false) {
						return userMessagesProperties.getWidgetDetail_validation_heading();
					}
				}
				if (ControlNameConstants.WIDGETS_DESCRIPTION.equals(detailField)) {

					result = validateDescript(widgetDetailVo.getWidgetDetailDescription().trim());
					if (result == false) {
						return userMessagesProperties.getWidgetDetail_validation_descript();
					}
					
				}
				/*
				 * result =
				 * validateUrl(widgetDetailVo.getWidgetDetailExternalUrl().trim(
				 * )); if (result == false) { return
				 * userMessagesProperties.getWidgetDetail_validation_externUrl()
				 * ; } if (widgetDetailVo.getWidgetDetailExternalUrl().length()
				 * > 250) { return
				 * userMessagesProperties.getWidgetDetail_validation_externUrl()
				 * ; }
				 */

				if (ControlNameConstants.WIDGETS_ANNOUNCEMENT_DATE.equals(detailField)) {

					result = validateDate(widgetDetailVo.getWidgetDetailAnnouncementDate());
					if (result == false) {
						return userMessagesProperties.getWidgetDetail_validation_announceDate();
					}
				}
				if (ControlNameConstants.WIDGETS_VALID_FROM.equals(detailField)) {
					result = validateValidFrom(widgetDetailVo.getWidgetDetailValidFrom());
					if (result == false) {
						return userMessagesProperties.getWidgetDetail_validation_validFrom();
					}
				}
				if (ControlNameConstants.WIDGETS_VALID_TO.equals(detailField)) {

					result = validateValidTo(widgetDetailVo.getWidgetDetailValidTo());
					if (result == false) {
						return userMessagesProperties.getWidgetDetail_validation_validTo();
					}
				}
			}
			result = validFromAndTo(widgetDetailVo.getWidgetDetailValidFrom(), widgetDetailVo.getWidgetDetailValidTo());
			if (result == false) {
				return userMessagesProperties.getWidgetDetail_validFromAndTo_invalid();
			}

			result = announceDateBWFromAndTo(widgetDetailVo.getWidgetDetailValidFrom(),
					widgetDetailVo.getWidgetDetailValidTo(), widgetDetailVo.getWidgetDetailAnnouncementDate());
			if (result == false) {
				return userMessagesProperties.getWidget_announcement_date_should_be_available_in_between();
			}
		}

		return CommonConstant.VALIDATION_SUCCESS;
	}

	public static boolean validateHeading(String input) {
		
		if (input == null || input.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	private boolean validateDescript(String descript) {
		if (descript == null || descript.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	@SuppressWarnings("unused")
	private boolean validateUrl(String url) {
		if (url == null || url.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	private boolean validateActive(boolean active) {
		if (active == false) {
			return false;
		}
		return true;
	}

	private boolean validateDate(Date newsDate) {
		if (null == newsDate)
			return false;
		else
			return true;

	}

	private boolean validateValidFrom(Date fromDate) {
		if (null == fromDate)
			return false;
		else
			return true;

	}

	private boolean validateValidTo(Date toDate) {
		if (null == toDate)
			return false;
		else
			return true;

	}

	private boolean validFromAndTo(Date fromDate, Date toDate) {
		if (fromDate.getTime() <= toDate.getTime())
			return true;
		else
			return false;

	}

	private boolean announceDateBWFromAndTo(Date fromDate, Date toDate, Date announce) {
		if (fromDate.getTime() <= announce.getTime() && announce.getTime() <= toDate.getTime())
			return true;
		else
			return false;

	}

	public String getFileExtension(MultipartFile[] uploadingFiles2) {
		for (MultipartFile uploadedFile : uploadingFiles2) {

			/*
			 * String fileName = uploadedFile.getOriginalFilename().substring(0,
			 * uploadedFile.getOriginalFilename().lastIndexOf("."));
			 */
			String fileName = uploadedFile.getOriginalFilename();

			if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
				switch (fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase()) {
				case CommonConstant.png:
					return CommonConstant.VALIDATION_SUCCESS;

				case CommonConstant.jpg:
					return CommonConstant.VALIDATION_SUCCESS;

				case CommonConstant.jpeg:
					return CommonConstant.VALIDATION_SUCCESS;

				default:
					return userMessagesProperties.getWidgetDetailPicPath();
				}
			}

		}
		return CommonConstant.VALIDATION_SUCCESS;
	}

}