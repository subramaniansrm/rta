package com.srm.rta.validation;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.srm.coreframework.config.UserMessages;
import com.srm.coreframework.constants.ControlNameConstants;
import com.srm.coreframework.util.CommonConstant;
import com.srm.rta.dao.RequestDAO;
import com.srm.rta.vo.RequestVO;

/**
 * RequestValidation.java
 * 
 * This class is a Validation class for the Request values.
 * 
 * @author manoj
 */
@Component
public class RequestValidation {

	/*
	 * @Autowired private CommonUtils commonUtils;
	 */

	@Autowired
	RequestDAO requestDao;
	
	@Autowired
	private UserMessages userMessagesProperties;

	/**
	 * This method is used to check whether any of the data is empty or null
	 * 
	 * 
	 * @param RequestVo
	 *            requestVo
	 * @return CommonConstants CommonConstants.VALIDATION_SUCCESS
	 */
	public String validateCreate(RequestVO requestVo) {

		boolean result;
		for (String field : requestVo.getScreenFieldDisplayVoList()) {
			
			
				
			if (ControlNameConstants.REQUEST_TYPE.equals(field)) {
				result = validateTypeId(requestVo.getRequestTypeId());
				if (result == false) {
					return userMessagesProperties.getRequest_validation_requestTypeId();
				}
			}
			if (ControlNameConstants.REQUEST_SUBTYPE.equals(field)) {

				result = validateSubTypeId(requestVo.getRequestSubtypeId());
				if (result == false) {
					return userMessagesProperties.getRequest_validation_requestSubTypeId();
				}
			}
			if (ControlNameConstants.REQUEST_USER_LOCATION.equals(field)) {

				result = validateLocationId(requestVo.getId());
				if (result == false) {
					return userMessagesProperties.getRequest_validation_locationId();
				}
			}

			if (ControlNameConstants.REQUEST_USER_SUBLOCATION.equals(field)) {

				result = validateSubLocationId(requestVo.getSublocationId());
				if (result == false) {
					return userMessagesProperties.getRequest_validation_sublocationId();
				}
			}
			if (ControlNameConstants.REQUEST_DEPARTMENT.equals(field)) {

				result = validateDepartmentId(requestVo.getDepartmentId());
				if (result == false) {
					return userMessagesProperties.getRequest_validation_departmentId();
				}
			}
			if (ControlNameConstants.REQUEST_DATE.equals(field)) {

				result = validateDate(requestVo.getRequestDate());
				if (result == false) {
					return userMessagesProperties.getRequest_validation_requestDate();
				}
			}
			if (ControlNameConstants.REQUEST_PRIORITY.equals(field)) {

				result = validatePriority(requestVo.getRequestPriority());
				if (result == false) {
					return userMessagesProperties.getRequest_validation_requestPriority();
				}
			}
			if (ControlNameConstants.REQUEST_SUBJECT.equals(field)) {
				result = validateSub(requestVo.getRequestSubject().trim());
				if (result == false) {
					return userMessagesProperties.getRequest_validation_requestSubject();
				}
			}

			/*if (ControlNameConstants.REQUEST_EXTENSION.equals(field)) {

				result = validateExten(requestVo.getRequestExtension());
				if (result == false) {
					return userMessagesProperties.getRequest_validation_requestExtension();
				}

				result = validateExtenSize(requestVo.getRequestExtension());
				if (result == false) {
					return userMessagesProperties.getRequest_validation_requestExtensionSize();
				}
				result = validateExtenSplChar(requestVo.getRequestExtension());
				if (result == false) {
					return userMessagesProperties.getRequest_validation_requestExtensionSplChar();
				}
			}*/
		}
		return CommonConstant.VALIDATION_SUCCESS;
	}

	/**
	 * This method is used to check whether any of the data is empty or null
	 * 
	 * 
	 * @param RequestVo
	 *            requestVo
	 * @return CommonConstants CommonConstants.VALIDATION_SUCCESS
	 */
	public String validateUpdate(RequestVO requestVo) {
		boolean result;
		for (String field : requestVo.getScreenFieldDisplayVoList()) {
			if (ControlNameConstants.REQUEST_TYPE.equals(field)) {
				result = validateTypeId(requestVo.getRequestTypeId());
				if (result == false) {
					return userMessagesProperties.getRequest_validation_requestTypeId();
				}
			}
			if (ControlNameConstants.REQUEST_SUBTYPE.equals(field)) {
				result = validateSubTypeId(requestVo.getRequestSubtypeId());
				if (result == false) {
					return userMessagesProperties.getRequest_validation_requestSubTypeId();
				}
			}
			if (ControlNameConstants.REQUEST_USER_LOCATION.equals(field)) {

				result = validateLocationId(requestVo.getId());
				if (result == false) {
					return userMessagesProperties.getRequest_validation_locationId();
				}
			}

			if (ControlNameConstants.REQUEST_USER_SUBLOCATION.equals(field)) {

				result = validateSubLocationId(requestVo.getSublocationId());
				if (result == false) {
					return userMessagesProperties.getRequest_validation_sublocationId();
				}
			}
			if (ControlNameConstants.REQUEST_DEPARTMENT.equals(field)) {

				result = validateDepartmentId(requestVo.getDepartmentId());
				if (result == false) {
					return userMessagesProperties.getRequest_validation_departmentId();
				}
			}
			if (ControlNameConstants.REQUEST_DATE.equals(field)) {

				result = validateDate(requestVo.getRequestDate());
				if (result == false) {
					return userMessagesProperties.getRequest_validation_requestDate();
				}
			}
			if (ControlNameConstants.REQUEST_PRIORITY.equals(field)) {

				result = validatePriority(requestVo.getRequestPriority());
				if (result == false) {
					return userMessagesProperties.getRequest_validation_requestPriority();
				}
			}
			if (ControlNameConstants.REQUEST_SUBJECT.equals(field)) {
				result = validateSub(requestVo.getRequestSubject().trim());
				if (result == false) {
					return userMessagesProperties.getRequest_validation_requestSubject();
				}
			}

			/*if (ControlNameConstants.REQUEST_EXTENSION.equals(field)) {

				result = validateExten(requestVo.getRequestExtension());
				if (result == false) {
					return userMessagesProperties.getRequest_validation_requestExtension();
				}
			}*/
		}
		result = validateReqId(requestVo.getRequestId());
		if (result == false) {
			return userMessagesProperties.getRequest_validation_requestId();
		}

		return CommonConstant.VALIDATION_SUCCESS;

	}

	public String validateDelete(RequestVO requestVo) {

		List<Integer> requestIdList = requestVo.getRequestIdList();

		if ((requestIdList == null) || (requestIdList.size() == 0)) {
			return userMessagesProperties.getRequest_validation_deleteIdList();
		}

		return CommonConstant.VALIDATION_SUCCESS;
	}

	public String validateLoad(RequestVO requestVo) {
		boolean result = validateReqId(requestVo.getRequestId());
		if (result == false) {
			return userMessagesProperties.getRequest_validation_requestId();
		}
		return CommonConstant.VALIDATION_SUCCESS;
	}

	private boolean validateReqId(int id) {

		if (id == 0) {
			return false;
		}
		return true;
	}

	private boolean validateTypeId(int typeId) {

		if (typeId == 0) {
			return false;
		}
		return true;
	}

	private boolean validateSubTypeId(int subtypeId) {

		if (subtypeId == 0) {
			return false;
		}
		return true;
	}

	private boolean validateLocationId(int locationId) {

		if (locationId == 0) {
			return false;
		}
		return true;
	}

	private boolean validateDepartmentId(int departmentId) {

		if (departmentId == 0) {
			return false;
		}
		return true;
	}

	private boolean validatePriority(int priority) {
		if (priority == 0) {
			return false;
		}
		return true;
	}

	private boolean validateSub(String subject) {
		return true;
	}

	private boolean validateCancel(boolean isCancel) {
		if (isCancel == true) {
			return false;
		}
		return true;
	}

	private boolean validateExten(int extension) {

		if (extension == 0) {
			return false;
		}
		return true;
	}

	private boolean validateExtenSize(int extension) {

		if (extension > 1000000000) {
			return false;
		}
		return true;
	}

	public boolean validateExtenSplChar(int value) {
		String pattern = "^[0-9]*$";

		if (String.valueOf(value).matches(pattern)) {
			return true;
		} else {
			return false;
		}

	}

	private boolean validateDate(Date newsDate) {
		if (null == newsDate)
			return false;
		else
			return true;

	}

	private boolean validateSubLocationId(int subLocationId) {

		if (subLocationId == 0) {
			return false;
		}
		return true;
	}

}
