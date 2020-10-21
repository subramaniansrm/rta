package com.srm.rta.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.entity.CityEntity;
import com.srm.coreframework.entity.CountryEntity;
import com.srm.coreframework.entity.Division;
import com.srm.coreframework.entity.Screen;
import com.srm.coreframework.entity.StateEntity;
import com.srm.coreframework.entity.UserDepartment;
import com.srm.coreframework.entity.UserEntity;
import com.srm.coreframework.entity.UserLocation;
import com.srm.coreframework.entity.UserType;
import com.srm.coreframework.repository.UserRepository;
import com.srm.coreframework.service.CommonService;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.coreframework.vo.CityVO;
import com.srm.coreframework.vo.CommonStorageVO;
import com.srm.coreframework.vo.CountryVO;
import com.srm.coreframework.vo.DivisionMasterVO;
import com.srm.coreframework.vo.DropdownCommonStorageVO;
import com.srm.coreframework.vo.DropdownDepartmentVO;
import com.srm.coreframework.vo.DropdownLocationVO;
import com.srm.coreframework.vo.DropdownSubLocationVO;
import com.srm.coreframework.vo.DropdownUserMasterVO;
import com.srm.coreframework.vo.DropdownUserRoleVO;
import com.srm.coreframework.vo.LanguageDropDownVo;
import com.srm.coreframework.vo.ScreenDropdownVO;
import com.srm.coreframework.vo.ScreenJsonVO;
import com.srm.coreframework.vo.StateVO;
import com.srm.coreframework.vo.UserDepartmentVO;
import com.srm.coreframework.vo.UserLocationVO;
import com.srm.coreframework.vo.UserMasterVO;
import com.srm.coreframework.vo.UserRoleTypeVO;
import com.srm.coreframework.vo.UserRoleVO;
import com.srm.rta.dao.DropdownDAO;
import com.srm.rta.vo.DropdownRequestSubTypeVO;
import com.srm.rta.vo.DropdownRequestTypeVO;
import com.srm.rta.vo.RequestSubTypeVO;
import com.srm.rta.vo.RequestWorkFlowAuditVO;

import lombok.Data;
@Data
@Service
public class DropdownService extends CommonService{

	@Autowired
	private UserRepository userRepository;


	@Autowired
	private DropdownDAO dropdownDao;

	

	/**
	 * Method is used to Load the Location List
	 * 
	 * @return userLocationMasterVo
	 */
	@Transactional
	public List<DropdownLocationVO> getAllLocation( ScreenJsonVO screenJson,AuthDetailsVo authDetailsVo) {
		try {
			List<DropdownLocationVO> dropdownLocationVOList = new ArrayList<DropdownLocationVO>();

			List<Object[]> locationList = dropdownDao.getAllLocation(screenJson,authDetailsVo);

			for (Object[] userLocationEntity : locationList) {
				DropdownLocationVO dropdownLocationVO = new DropdownLocationVO();

				if (null != ((Object[]) userLocationEntity)[0]) {
					dropdownLocationVO.setId((int) ((Object[]) userLocationEntity)[0]);
				}
				if (null != (String) ((Object[]) userLocationEntity)[1]) {
					dropdownLocationVO.setUserLocationName((String) ((Object[]) userLocationEntity)[1]);
				}
				dropdownLocationVOList.add(dropdownLocationVO);
			}

			return dropdownLocationVOList;
		} catch (Exception e) {
			Log.info("Dropdown Service getAllLocation  Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
	}

	/**
	 * Method is used to Load the Department List
	 * 
	 * @return userDepartmentMasterVo
	 */
	@Transactional
	public List<DropdownDepartmentVO> getAllDepartment(UserDepartmentVO departmentVo,AuthDetailsVo authDetailsVo) {

		try {

			List<DropdownDepartmentVO> dropdownDepartmentVOList = new ArrayList<DropdownDepartmentVO>();

			List<Object[]> departmentList = dropdownDao.getAllDepartment(departmentVo,authDetailsVo);

			for (Object[] userDepartmentEntity : departmentList) {
				DropdownDepartmentVO dropdownDepartmentVO = new DropdownDepartmentVO();
				if (null != (Integer) ((Object[]) userDepartmentEntity)[0]) {
					dropdownDepartmentVO.setId((Integer) ((Object[]) userDepartmentEntity)[0]);
				}
				if (null != (String) ((Object[]) userDepartmentEntity)[1]) {
					dropdownDepartmentVO.setUserDepartmentName((String) ((Object[]) userDepartmentEntity)[1]);
				}
				if (null != (String) ((Object[]) userDepartmentEntity)[2]) {
					dropdownDepartmentVO.setToolTipName(dropdownDepartmentVO.getUserDepartmentName()
							.concat(" - " + (String) ((Object[]) userDepartmentEntity)[2]));
				} else {
					dropdownDepartmentVO.setToolTipName(dropdownDepartmentVO.getUserDepartmentName());
				}
				dropdownDepartmentVOList.add(dropdownDepartmentVO);
			}
			return dropdownDepartmentVOList;
		} catch (Exception e) {
			Log.info("Dropdown Service getAllDepartment  Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
	}

	/**
	 * Method is used to Load the Role List
	 * 
	 * @return userRoleMasterVo
	 */
	@Transactional
	public List<DropdownUserRoleVO> getAllRole(UserRoleVO userRoleVo,AuthDetailsVo authDetailsVo) {

		try {

			List<DropdownUserRoleVO> dropdownUserRoleVOList = new ArrayList<DropdownUserRoleVO>();

			List<Object[]> roleList = dropdownDao.getAllRole(userRoleVo,authDetailsVo);

			for (Object[] userRoleEntity : roleList) {

				DropdownUserRoleVO dropdownUserRoleVO = new DropdownUserRoleVO();

				if (0 != (int) ((Object[]) userRoleEntity)[0]) {
					dropdownUserRoleVO.setId((int) ((Object[]) userRoleEntity)[0]);
				}
				if (null != (String) ((Object[]) userRoleEntity)[1]) {
					dropdownUserRoleVO.setUserRoleName((String) ((Object[]) userRoleEntity)[1]);
				}

				dropdownUserRoleVOList.add(dropdownUserRoleVO);
			}
			return dropdownUserRoleVOList;
		} catch (Exception e) {
			Log.info("Dropdown Service getAllRole  Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
	}

	/**
	 * Method is used to Load the Division List
	 * 
	 * @return userDivisionMasterVo
	 */
	@Transactional
	public List<DivisionMasterVO> getAllDivision(AuthDetailsVo authDetailsVo) {

		try {

			List<DivisionMasterVO> userDivisionMasterVo = new ArrayList<DivisionMasterVO>();

			List<Division> divisionList = dropdownDao.getAllDivision(authDetailsVo);

			for (Division divisionEntity : divisionList) {
				DivisionMasterVO divisionMasterVo = new DivisionMasterVO();
				divisionMasterVo.setId(divisionEntity.getId());
				divisionMasterVo.setDivision(divisionEntity.getDivision());
				userDivisionMasterVo.add(divisionMasterVo);
			}

			return userDivisionMasterVo;
		} catch (Exception e) {
			Log.info("Dropdown Service getAllDivision  Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
	}

	/**
	 * Method is used to Load the State List
	 * 
	 * @return stateMasterVoList List<StateMasterVo>
	 */
	@Transactional
	public List<StateVO> getAllState(AuthDetailsVo authDetailsVo) {

		try {

			List<StateVO> stateMasterVoList = new ArrayList<StateVO>();

			List<StateEntity> stateEntityList = dropdownDao.getAllState(authDetailsVo);

			for (StateEntity stateEntity : stateEntityList) {
				StateVO stateMasterVo = new StateVO();
				stateMasterVo.setStateId(stateEntity.getStateId());
				stateMasterVo.setStateName(stateEntity.getStateName());
				stateMasterVoList.add(stateMasterVo);
			}
			return stateMasterVoList;
		} catch (Exception e) {
			Log.info("Dropdown Service getAllState  Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
	}

	/**
	 * Method is used to Load the City List
	 * 
	 * @return cityMasterVoList List<CityMasterVo>
	 */
	@Transactional
	public List<CityVO> getAllCity(AuthDetailsVo authDetailsVo) {

		try {

			List<CityVO> cityMasterVoList = new ArrayList<CityVO>();

			List<CityEntity> cityEntityList = dropdownDao.getAllCity(authDetailsVo);

			for (CityEntity cityEntity : cityEntityList) {
				CityVO cityMasterVo = new CityVO();
				cityMasterVo.setCityId(cityEntity.getCityId());
				cityMasterVo.setCityName(cityEntity.getCityName());
				cityMasterVoList.add(cityMasterVo);
			}
			return cityMasterVoList;
		} catch (Exception e) {
			Log.info("Dropdown Service getAllCity  Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

	}

	/**
	 * Method is used to Load the Screen List
	 * 
	 * @return ScreenDropdownVoList List<ScreenDropdownVo>
	 */
	@Transactional
	public List<ScreenDropdownVO> getAllScreen(AuthDetailsVo authDetailsVo) {

		try {

			List<ScreenDropdownVO> screenDropdownVoList = new ArrayList<ScreenDropdownVO>();

			List<Screen> screenEntityList = dropdownDao.getAllScreen();

			for (Screen screenEntity : screenEntityList) {
				ScreenDropdownVO screenDropdownVo = new ScreenDropdownVO();

				screenDropdownVo.setScreenId(screenEntity.getScreenId());
				screenDropdownVo.setScreenName(screenEntity.getScreenName());
				screenDropdownVoList.add(screenDropdownVo);
			}
			return screenDropdownVoList;
		} catch (Exception e) {
			Log.info("Dropdown Service getAllScreen  Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

	}

	/**
	 * Method is used to Load the Country List
	 * 
	 * @return CountryMasterVoList List<CountryMasterVo>
	 */
	@Transactional
	public List<CountryVO> getAllCountry(AuthDetailsVo authDetailsVo) {

		try {

			List<CountryVO> CountryMasterVoList = new ArrayList<CountryVO>();

			List<CountryEntity> countryEntityList = dropdownDao.getAllCountry(authDetailsVo);

			for (CountryEntity countryEntity : countryEntityList) {
				CountryVO countryMasterVo = new CountryVO();

				countryMasterVo.setCountryId(countryEntity.getId());
				countryMasterVo.setCountryName(countryEntity.getCountry());
				CountryMasterVoList.add(countryMasterVo);
			}
			return CountryMasterVoList;
		} catch (Exception e) {
			Log.info("Dropdown Service getAllCountry  Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}

	}

	/**
	 * Method is used to Load the Request type List
	 * 
	 * @return dropdownRequestTypeVOList List<DropdownRequestTypeVO>
	 */
	@Transactional
	public List<DropdownRequestTypeVO> getAllTypeList(AuthDetailsVo authDetailsVo) {

		try {

			List<DropdownRequestTypeVO> dropdownRequestTypeVOList = new ArrayList<DropdownRequestTypeVO>();

			List<Object[]> listRequestTypeEntity = dropdownDao.getAllTypeList(authDetailsVo);

			for (Object[] requestTypeEntity : listRequestTypeEntity) {

				DropdownRequestTypeVO dropdownRequestTypeVO = new DropdownRequestTypeVO();

				if (0 != (int) ((Object[]) requestTypeEntity)[0]) {
					dropdownRequestTypeVO.setRequestTypeId((int) ((Object[]) requestTypeEntity)[0]);
				}
				if (null != (String) ((Object[]) requestTypeEntity)[1]) {
					dropdownRequestTypeVO.setRequestTypeName((String) ((Object[]) requestTypeEntity)[1]);
				}
				dropdownRequestTypeVOList.add(dropdownRequestTypeVO);
			}

			return dropdownRequestTypeVOList;
		} catch (Exception e) {
			Log.info("Dropdown Service getAllTypeList  Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
	}

	/**
	 * Method is to get all the sub type list.
	 * 
	 * @param typeId
	 * @return listRequestSubTypeVo List<RequestSubTypeVo>
	 */
	@Transactional
	public List<DropdownRequestSubTypeVO> getAllSubTypeList(int typeId,AuthDetailsVo authDetailsVo) {

		try {

			List<DropdownRequestSubTypeVO> dropdownRequestSubTypeVOList = new ArrayList<DropdownRequestSubTypeVO>();

			List<Object[]> listRequestSubTypeEntity = dropdownDao.getAllSubTypeList(typeId,authDetailsVo);

			for (Object[] requestSubTypeEntity : listRequestSubTypeEntity) {

				DropdownRequestSubTypeVO dropdownRequestSubTypeVO = new DropdownRequestSubTypeVO();

				if (0 != (int) ((Object[]) requestSubTypeEntity)[0]) {
					dropdownRequestSubTypeVO.setRequestSubTypeId((int) ((Object[]) requestSubTypeEntity)[0]);
				}
				if (null != (String) ((Object[]) requestSubTypeEntity)[1]) {
					dropdownRequestSubTypeVO.setRequestSubTypeName((String) ((Object[]) requestSubTypeEntity)[1]);
				}
				if (0 != (int) ((Object[]) requestSubTypeEntity)[2]) {
					dropdownRequestSubTypeVO.setRequestSubtypePriorty((int) ((Object[]) requestSubTypeEntity)[2]);
				}
				dropdownRequestSubTypeVOList.add(dropdownRequestSubTypeVO);
			}

			return dropdownRequestSubTypeVOList;
		} catch (Exception e) {
			Log.info("Dropdown Service getAllSubTypeList  Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
	}

	/**
	 * Method is used to Load the Department List
	 * 
	 * @return UserRoleMaster
	 */
	@Transactional
	public List<DropdownDepartmentVO> getLoadUserDepartmentDetails(AuthDetailsVo authDetailsVo) {
		try {
			List<DropdownDepartmentVO> dropdownDepartmentVo = new ArrayList<DropdownDepartmentVO>();

			List<UserDepartment> userDepartmentEntityList = dropdownDao.getLoadUserDepartmentDetails(authDetailsVo);

			for (UserDepartment userDepartmentEntity : userDepartmentEntityList) {

				DropdownDepartmentVO dropdownLocationVo = new DropdownDepartmentVO();

				dropdownLocationVo.setId(userDepartmentEntity.getId());
				dropdownLocationVo.setUserDepartmentName(userDepartmentEntity.getUserDepartmentName());

				dropdownDepartmentVo.add(dropdownLocationVo);

			}
			return dropdownDepartmentVo;
		} catch (Exception e) {
			Log.info("Dropdown Service getLoadUserDepartmentDetails Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
	}

	/**
	 * Method is used to Load the Location List
	 * 
	 * @return UserRoleMaster
	 */

	@Transactional
	public List<DropdownLocationVO> getLoadUserLocationDetails(AuthDetailsVo authDetailsVo) {
		try {
			List<UserLocation> userLocation = dropdownDao.getLoadUserLocationDetails(authDetailsVo);

			List<DropdownLocationVO> locationVoList = new ArrayList<DropdownLocationVO>();

			for (UserLocation userLocationEntity : userLocation) {

				DropdownLocationVO dropdownLocationVo = new DropdownLocationVO();

				dropdownLocationVo.setId(userLocationEntity.getId());
				dropdownLocationVo.setUserLocationName(userLocationEntity.getUserLocationName());
				locationVoList.add(dropdownLocationVo);

			}
			return locationVoList;
		} catch (Exception e) {
			Log.info("Dropdown Service getLoadUserLocationDetails Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
	}

	/**
	 * Method is used to Load the Department List
	 * 
	 * @return
	 */

	@Transactional
	public List<UserLocationVO> loadDepartmentLocation(AuthDetailsVo authDetailsVo) {

		try {

			List<UserLocationVO> userLocationMasterVo = new ArrayList<UserLocationVO>();
			List<UserLocation> userLocationEntity = dropdownDao.loadDepartmentLocation(authDetailsVo);
			userLocationMasterVo = getLocationList(userLocationEntity,authDetailsVo);
			return userLocationMasterVo;

		} catch (Exception e) {
			Log.info("Dropdown Service loadDepartmentLocation Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
	}

	/**
	 * Method is used to Load the Location List
	 * 
	 * @param userLocationEntity
	 * @return
	 */
	@Transactional
	private List<UserLocationVO> getLocationList(List<UserLocation> userLocationEntity,AuthDetailsVo authDetailsVo) {

		try {

			List<UserLocationVO> userLocationMasterVo = new ArrayList<UserLocationVO>();

			for (UserLocation locationEntity : userLocationEntity) {
				UserLocationVO locationMasterVo = new UserLocationVO();
				locationMasterVo.setId(locationEntity.getId());
				locationMasterVo.setUserLocationName(locationEntity.getUserLocationName());
				userLocationMasterVo.add(locationMasterVo);
			}
			return userLocationMasterVo;
		} catch (Exception e) {
			Log.info("Dropdown Service getLocationList Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
	}

	/**
	 * Method is used to Load the Sub Location List
	 * 
	 * @param id
	 * @return
	 */
	@Transactional
	public List<DropdownSubLocationVO> getAllsublocationList(int id,AuthDetailsVo authDetailsVo) {

		try {

			List<DropdownSubLocationVO> dropdownSubLocationVOList = new ArrayList<DropdownSubLocationVO>();

			List<Object[]> listSubLocationEntity = dropdownDao.getAllSublocatList(id,authDetailsVo);

			for (Object[] subLocationEntityEntity : listSubLocationEntity) {

				DropdownSubLocationVO dropdownSubLocationVO = new DropdownSubLocationVO();
				if (null != (Integer) ((Object[]) subLocationEntityEntity)[0]) {
					dropdownSubLocationVO.setSublocationId((Integer) ((Object[]) subLocationEntityEntity)[0]);
				}
				if (null != (String) ((Object[]) subLocationEntityEntity)[1]) {
					dropdownSubLocationVO.setSubLocationName((String) ((Object[]) subLocationEntityEntity)[1]);
				}
				dropdownSubLocationVOList.add(dropdownSubLocationVO);
			}

			return dropdownSubLocationVOList;
		} catch (Exception e) {
			Log.info("Dropdown Service getAllsublocationList Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
	}

	/**
	 * Method is used for GetAll the user for User dropdown
	 * 
	 * @return
	 */
	@Transactional
	public List<DropdownUserMasterVO> getAllUser(UserMasterVO userMasterVo,AuthDetailsVo authDetailsVo) {
		try {
			List<DropdownUserMasterVO> userMasterVoList = new ArrayList<DropdownUserMasterVO>();

			List<Object> userEntityList = dropdownDao.getAllUser(userMasterVo,authDetailsVo);

			for (Object userEntity : userEntityList) {
				DropdownUserMasterVO userMaster = new DropdownUserMasterVO();
				if (0 != (int) ((Object[]) userEntity)[0]) {
					userMaster.setId((int) ((Object[]) userEntity)[0]);
				}
				if (null != (String) ((Object[]) userEntity)[1]) {
					userMaster.setFirstName((String) ((Object[]) userEntity)[1]);
				}
				userMasterVoList.add(userMaster);
			}
			return userMasterVoList;
		} catch (Exception e) {
			Log.info("Dropdown Service getAllUser Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
	}

	/**
	 * Method is used to Load the user Executer List
	 * 
	 * @param userMasterVo
	 * @return
	 */
	@Transactional
	public List<UserMasterVO> getUserExecuter(UserMasterVO userMasterVo,AuthDetailsVo authDetailsVo) {

		try {

			List<UserMasterVO> userMasterVoList = new ArrayList<UserMasterVO>();

			UserEntity userEntity = userRepository.findOne(authDetailsVo.getUserId());

			List<Object[]> userEntityList = dropdownDao.getUserExecuter(userEntity,authDetailsVo);

			for (Object[] object : userEntityList) {
				UserMasterVO userMaster = new UserMasterVO();
				userMaster.setId((int) object[0]);
				userMaster.setFirstName((String) object[1]);
				userMasterVoList.add(userMaster);
			}
			return userMasterVoList;
		} catch (Exception e) {
			Log.info("Dropdown Service getUserExecuter Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
	}

	/**
	 * Method is used to Get the all user level
	 * 
	 * @param commonStorageVo
	 * @return commonStorageVoList
	 */
	@Transactional
	public List<DropdownCommonStorageVO> getUserLevel(CommonStorageVO commonStorageVo,AuthDetailsVo authDetailsVo) {

		try {

			List<DropdownCommonStorageVO> commonStorageVoList = new ArrayList<DropdownCommonStorageVO>();

			List<Object[]> commonStorageEntityList = dropdownDao.getAllLevel(authDetailsVo);

			for (Object[] commonStorageEntity : commonStorageEntityList) {
				DropdownCommonStorageVO commonStorage = new DropdownCommonStorageVO();
				if (0 != (int) ((Object[]) commonStorageEntity)[0]) {
					commonStorage.setCommonId((int) ((Object[]) commonStorageEntity)[0]);
				}
				if (null != (String) ((Object[]) commonStorageEntity)[1]) {
					commonStorage.setItemValue((String) ((Object[]) commonStorageEntity)[1]);
				}
				commonStorageVoList.add(commonStorage);
			}
			return commonStorageVoList;
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Dropdown Service getUserLevel Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
	}

	
	/**
	 * Method is used to Get the all user level
	 * 
	 * @param commonStorageVo
	 * @return commonStorageVoList
	 */
	@Transactional
	public List<LanguageDropDownVo> languageDropdown(CommonStorageVO commonStorageVo,AuthDetailsVo authDetailsVo) {

		try {

			List<LanguageDropDownVo> languageDropDownVoVoList = new ArrayList<LanguageDropDownVo>();

			List<Object[]> commonStorageEntityList = dropdownDao.languageDropdown(authDetailsVo);

			for (Object[] commonStorageEntity : commonStorageEntityList) {
				LanguageDropDownVo languageDropDownVo = new LanguageDropDownVo();
				if (null != (String) ((Object[]) commonStorageEntity)[0]) {
					languageDropDownVo.setLanguageCode((String) ((Object[]) commonStorageEntity)[0]);
				}
				if (null != (String) ((Object[]) commonStorageEntity)[1]) {
					languageDropDownVo.setLanguage((String) ((Object[]) commonStorageEntity)[1]);
				}
				languageDropDownVoVoList.add(languageDropDownVo);
			}
			return languageDropDownVoVoList;
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Dropdown Service languageDropdown Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
	}
	
	@Transactional
	public List<DropdownUserMasterVO> getUserDep(UserMasterVO userMasterVo,AuthDetailsVo authDetailsVo) {
		try {
			List<DropdownUserMasterVO> userMasterVoList = new ArrayList<DropdownUserMasterVO>();
			List<UserEntity> userEntityList = null;

			userEntityList = dropdownDao.getUserDep(userMasterVo,authDetailsVo);

			for (UserEntity userEntity : userEntityList) {
				DropdownUserMasterVO userMaster = new DropdownUserMasterVO();
				userMaster.setId(userEntity.getId());
				if (userEntity.getLastName() != null) {
					userMaster.setFirstName(userEntity.getFirstName().concat(" " + userEntity.getLastName()));
				} else {
					userMaster.setFirstName(userEntity.getFirstName());
				}

				userMasterVoList.add(userMaster);
			}
			return userMasterVoList;
		} catch (Exception e) {
			Log.info("Dropdown Service getUserDep Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
	}

	public List<UserRoleTypeVO> getRoleType(UserRoleTypeVO userRoleTypeVo,AuthDetailsVo authDetailsVo) {
		try {
			List<UserRoleTypeVO> userRoleTypeVoList = new ArrayList<UserRoleTypeVO>();
			List<UserType> userTypeEntityList = null;

			userTypeEntityList = dropdownDao.getRoleType(userRoleTypeVo,authDetailsVo);

			for (UserType userTypeEntity : userTypeEntityList) {
				UserRoleTypeVO userRoleType = new UserRoleTypeVO();
				userRoleType.setUserTypeId(userTypeEntity.getUserTypeId());
				userRoleType.setTypeOfUser(userTypeEntity.getTypeOfUser());
				userRoleTypeVoList.add(userRoleType);
			}
			return userRoleTypeVoList;
		} catch (Exception e) {
			Log.info("Dropdown Service getRoleType Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
	}

	public List<DropdownUserMasterVO> getReassignUser(RequestWorkFlowAuditVO requestWorkFlowAuditVo,AuthDetailsVo authDetailsVo) {

		try {

			List<DropdownUserMasterVO> userMasterVoList = new ArrayList<DropdownUserMasterVO>();

			List<Object[]> userEntityList = dropdownDao.getReassignUser(requestWorkFlowAuditVo,authDetailsVo);

			for (Object[] object : userEntityList) {
				DropdownUserMasterVO userMaster = new DropdownUserMasterVO();
				userMaster.setId((int) object[0]);

				if ((String) object[2] != null) {
					userMaster.setFirstName((String) object[1].toString().concat(" " + (String) object[2]));
				} else {
					userMaster.setFirstName((String) object[1]);
				}
				userMasterVoList.add(userMaster);
			}
			return userMasterVoList;
		} catch (Exception e) {
			Log.info("Dropdown Service getReassignUser Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
	}
	
	/**
	 * Method is to get all the sub type list.
	 * 
	 * @param typeId
	 * @return listRequestSubTypeVo List<RequestSubTypeVo>
	 */
	@Transactional()
	public List<RequestSubTypeVO> getAllSubTypeListBasedOnWorkFlow(int typeId,AuthDetailsVo authDetailsVo) {

		try {

			List<RequestSubTypeVO> listRequestSubTypeVo = new ArrayList<RequestSubTypeVO>();

			List<Object[]> listRequestSubTypeEntity = dropdownDao.getAllSubTypeListBasedOnWorkFlow(typeId,authDetailsVo);

			for (Object[] requestSubTypeEntity : listRequestSubTypeEntity) {
				
				RequestSubTypeVO requestSubTypeVo = new RequestSubTypeVO();
				
				if(0 != (int) ((Object[]) requestSubTypeEntity)[0]){
				requestSubTypeVo.setRequestSubTypeId((int) ((Object[]) requestSubTypeEntity)[0]);
				}
				if(null != (String) ((Object[]) requestSubTypeEntity)[1]){
				requestSubTypeVo.setRequestSubTypeName((String) ((Object[]) requestSubTypeEntity)[1]);
				}
				if(0 != (int) ((Object[]) requestSubTypeEntity)[2]){
				requestSubTypeVo.setRequestSubtypePriorty((int) ((Object[]) requestSubTypeEntity)[2]);
				}
				listRequestSubTypeVo.add(requestSubTypeVo);
			}

			return listRequestSubTypeVo;
		} catch (Exception e) {
			Log.info("Dropdown Service getAllSubTypeListBasedOnWorkFlow Exception",e);
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
	}

}
