package com.srm.rta.validation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.srm.coreframework.config.UserMessages;
import com.srm.coreframework.constants.ControlNameConstants;
import com.srm.coreframework.util.CommonConstant;
import com.srm.rta.vo.WidgetVO;


/**
 * WidgetValidation.java
 * 
 * This class is a Validation class for the Widget values.
 * 
 * @author manoj
 */
@Component
public class WidgetValidation {

	@Autowired
	private UserMessages userMessagesProperties;

	/**
	 * This method is used to check whether any of the data is empty or null
	 * 
	 * 
	 * @param WidgetVo
	 *            widgetVo
	 * @return CommonConstants CommonConstants.VALIDATION_SUCCESS
	 */
	public String validateCreate(WidgetVO widgetVo) {
		boolean result;
		for (String field : widgetVo.getScreenFieldDisplayVoList()) {
			if (ControlNameConstants.WIDGETS_INDEX.equals(field)) {
				result = validateIndex(widgetVo.getWidgetIndex());
				if (result == false) {
					return userMessagesProperties.getWidget_validation_widgetIndex();
				}
			}
			if (ControlNameConstants.WIDGETS_TITLE.equals(field)) {
				result = validateTitle(widgetVo.getWidgetTitle().trim());
				if (result == false) {
					return userMessagesProperties.getWidget_validation_widgetTitle();
				}

				if (widgetVo.getWidgetTitle().length() > 50) {
					return userMessagesProperties.getWidget_validation_widgetTitle();
				}
			}
			if (ControlNameConstants.WIDGETS_STATUS.equals(field)) {
				result = validateActive(widgetVo.isWidgetIsActive());
				if (result == false) {
					return userMessagesProperties.getWidget_validation_widgetIsActive();
				}
			}
		}
		return CommonConstant.VALIDATION_SUCCESS;
	}

	/**
	 * This method is used to check whether any of the data is empty or null
	 * 
	 * 
	 * @param WidgetVo
	 *            widgetVo
	 * @return CommonConstants CommonConstants.VALIDATION_SUCCESS
	 */
	public String validateUpdate(WidgetVO widgetVo) {

		boolean result;
		for (String field : widgetVo.getScreenFieldDisplayVoList()) {
			if (ControlNameConstants.WIDGETS_CODE.equals(field)) {

				result = validateCode(widgetVo.getWidgetCode());
				if (result == false) {
					return userMessagesProperties.getWidget_validation_widgetCode();
				}
				if (widgetVo.getWidgetCode().length() > 10) {
					return userMessagesProperties.getWidget_validation_widgetCode();
				}
			}
			if (ControlNameConstants.WIDGETS_INDEX.equals(field)) {
				result = validateIndex(widgetVo.getWidgetIndex());
				if (result == false) {
					return userMessagesProperties.getWidget_validation_widgetIndex();
				}
			}
			if (ControlNameConstants.WIDGETS_TITLE.equals(field)) {
				result = validateTitle(widgetVo.getWidgetTitle().trim());
				if (result == false) {
					return userMessagesProperties.getWidget_validation_widgetTitle();
				}
				if (widgetVo.getWidgetTitle().length() > 50) {
					return userMessagesProperties.getWidget_validation_widgetTitle();
				}
			}
		}

		result = validateId(widgetVo.getWidgetId());
		if (result == false) {
			return userMessagesProperties.getWidget_validation_widgetId();
		}

		return CommonConstant.VALIDATION_SUCCESS;
	}

	/**
	 * This method is used to check whether the list of widgetId are present or
	 * not.
	 * 
	 * @param WidgetVo
	 *            widgetVo
	 * @return CommonConstants CommonConstants.VALIDATION_SUCCESS
	 */
	public String validateDelete(WidgetVO widgetVo) {

		List<Integer> widgetIdList = widgetVo.getWidgetsIdList();

		if ((widgetIdList == null) || (widgetIdList.size() == 0)) {
			return userMessagesProperties.getWidget_validation_deleteIdList();
		}

		return CommonConstant.VALIDATION_SUCCESS;
	}

	/**
	 * This method is used to check whether the widgetId is present or not.
	 * 
	 * @param WidgetVo
	 *            widgetVo
	 * @return CommonConstants CommonConstants.VALIDATION_SUCCESS
	 */
	public String validateLoad(WidgetVO widgetVo) {
		boolean result = validateId(widgetVo.getWidgetId());
		if (result == false) {
			return userMessagesProperties.getWidget_validation_widgetId();
		}
		return CommonConstant.VALIDATION_SUCCESS;
	}

	private boolean validateId(Integer id) {

		if (id == 0) {
			return false;
		}
		return true;
	}

	public static boolean validateCode(String input) {
		String field = input;
		String pattern = "^.*(?=.*[/|`~!(){}_,\"'*?:;<>,@#$%^&+=-]).*$";
		if (input == null || input.isEmpty()) {
			return false;
		} else if (field.matches(pattern)) {
			return false;
		} else {
			return true;
		}
	}

	private boolean validateIndex(int index) {
		if (index == 0) {
			return false;
		}
		return true;
	}

	public static boolean validateTitle(String input) {

		if (input == null || input.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	private boolean validateActive(boolean flashNewsIsActive) {

		if (true == flashNewsIsActive) {
			return true;
		}
		return false;
	}

}
