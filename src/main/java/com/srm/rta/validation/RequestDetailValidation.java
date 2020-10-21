package com.srm.rta.validation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.srm.coreframework.config.UserMessages;
import com.srm.coreframework.constants.ButtonTypeEnum;
import com.srm.coreframework.util.CommonConstant;
import com.srm.rta.vo.RequestDetailVO;

/**
 * RequestDetailValidation.java
 * 
 * This class is a Validation class for the RequestDetail values.
 * 
 * @author manoj
 */

@Component
public class RequestDetailValidation {

	/*
	 * @Autowired private CommonUtils commonUtils;
	 */
	@Autowired
	UserMessages userMessagesProperties;

	/**
	 * This method is used to check whether any of the data in the list is empty
	 * or null
	 * 
	 * 
	 * @param List<RequestDetailVo>
	 *            list_requestDetailVo
	 * @return CommonConstants CommonConstants.VALIDATION_SUCCESS
	 */
	public String validateCreate(List<RequestDetailVO> list_requestDetailVo) {

		for (RequestDetailVO requestDetailVo : list_requestDetailVo) {

			if(requestDetailVo.isRequestScreenDetailConfigurationValidationIsRequired())
			{

			boolean result = validateReqScrId(requestDetailVo.getRequestScreenConfigId());
			if (result == false) {
				return userMessagesProperties.getRequestDetail_validation_requestScreenConfigId();
			}

			result = validateReqScrDetId(requestDetailVo.getRequestScreenDetailConfigId());
			if (result == false) {
				return userMessagesProperties.getRequestDetail_validation_requestScreenConfigDetailId();
			}

			result = validateType(requestDetailVo.getRequestScreenDetailConfigurationFieldType());
			if (result == false) {
				return userMessagesProperties.getRequestDetail_validation_requestDetailFieldType();
			}

			if (requestDetailVo.getRequestScreenDetailConfigurationFieldType().equalsIgnoreCase(ButtonTypeEnum.C.toString())) {
				result = validateListOfString(requestDetailVo.getObjectList());
				if (result == false) {
					return userMessagesProperties.getRequestDetail_validation_requestDetailObjectListValue();
				}
			} else if(!requestDetailVo.getRequestScreenDetailConfigurationFieldType().equalsIgnoreCase(ButtonTypeEnum.C.toString())) {
			
				result = validateValue(requestDetailVo.getRequestScreenDetailConfigurationFieldValue());
				if (result == false) {
					return userMessagesProperties.getRequestDetail_validation_requestDetailFieldValue();
				}
			}

			result = validateActive(requestDetailVo.isRequestScreenDetailConfigurationIsActive());
			if (result == false) {
				return userMessagesProperties.getRequestDetail_validation_requestDetailIsActive();
			}
			}

		}
		return CommonConstant.VALIDATION_SUCCESS;

	}

	/**
	 * This method is used to check whether any of the data in the list is empty
	 * or null
	 * 
	 * 
	 * @param List<RequestDetailVo>
	 *            list_requestDetailVo
	 * @return CommonConstants CommonConstants.VALIDATION_SUCCESS
	 */
	public String validateUpdate(List<RequestDetailVO> list_requestDetailVo) {

		for (RequestDetailVO requestDetailVo : list_requestDetailVo) {
			if(requestDetailVo.isRequestScreenDetailConfigurationValidationIsRequired())
			{
			/*
			 * boolean result = validateReqId(requestDetailVo.getRequestId());
			 * if (result == false) { return
			 * userMessagesProperties.getRequestDetail_validation_requestId(); }
			 */
			boolean result = validateReqScrId(requestDetailVo.getRequestScreenConfigId());
			if (result == false) {
				return userMessagesProperties.getRequestDetail_validation_requestScreenConfigId();
			}

			result = validateReqScrDetId(requestDetailVo.getRequestScreenDetailConfigId());
			if (result == false) {
				return userMessagesProperties.getRequestDetail_validation_requestScreenConfigDetailId();
			}

			result = validateType(requestDetailVo.getRequestScreenDetailConfigurationFieldType());
			if (result == false) {
				return userMessagesProperties.getRequestDetail_validation_requestDetailFieldType();
			}

			if (requestDetailVo.getRequestScreenDetailConfigurationFieldType().equalsIgnoreCase(ButtonTypeEnum.C.toString())) {
				result = validateListOfString(requestDetailVo.getObjectList());
				if (result == false) {
					return userMessagesProperties.getRequestDetail_validation_requestDetailObjectListValue();
				}
			} else if(!requestDetailVo.getRequestScreenDetailConfigurationFieldType().equalsIgnoreCase(ButtonTypeEnum.C.toString())) {
			
				result = validateValue(requestDetailVo.getRequestScreenDetailConfigurationFieldValue());
				if (result == false) {
					return userMessagesProperties.getRequestDetail_validation_requestDetailFieldValue();
				}
			}}

		}
		return CommonConstant.VALIDATION_SUCCESS;

	}


	private boolean validateReqScrId(int reqScrId) {

		if (reqScrId == 0) {
			return false;
		}
		return true;
	}

	private boolean validateReqScrDetId(int reqScrDetId) {

		if (reqScrDetId == 0) {
			return false;
		}
		return true;
	}

	private boolean validateType(String type) {
		if (type == null || type == "" || type.isEmpty()) {
			return false;
		}
		return true;
		// return commonUtils.checkIsEmpty(type);
	}

	private boolean validateValue(String value) {
		if (value == null || value == "" || value.isEmpty()) {
			return false;
		}
		return true;
	}

	private boolean validateListOfString(List<String> objectList) {
		if (objectList == null || objectList.isEmpty()) {
			return false;

		} else
			return true;
	}

	private boolean validateActive(boolean isActive) {
		if (isActive == false) {
			return false;
		}
		return true;
	}
}
