package com.srm.rta.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.dao.UserEntityMappingDao;
import com.srm.coreframework.entity.UserEntity;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.coreframework.vo.EntityLicenseVO;
import com.srm.coreframework.vo.UserEntityMappingVo;
import com.srm.coreframework.vo.UserRoleVO;
import com.srm.rta.dao.EntitySelectionDao;

import net.sf.jasperreports.web.commands.CommandException;

@Service
public class EntitySelectionService {

	@Autowired
	EntitySelectionDao entitySelectionDao;
	
	@Autowired
	UserEntityMappingDao userEntityMappingDao;
	
	@Transactional
	public List<EntityLicenseVO> loadUserEntity(AuthDetailsVo authDetailsVo){
		
		List<EntityLicenseVO> entityLicenseList = new ArrayList<EntityLicenseVO>();
		
		List<Object[]> list = entitySelectionDao.loadUserEntity(authDetailsVo);
		EntityLicenseVO entityLicenseVO = null;
		for(Object[] object : list){
			entityLicenseVO = new EntityLicenseVO();
			if(null != object[0]){
				entityLicenseVO.setId((Integer)object[0]);
			}
			if(null != object[1]){
				entityLicenseVO.setEntityName((String)object[1]);
			}
			entityLicenseList.add(entityLicenseVO);
		}
		
		return entityLicenseList;
		
	}
	
	@Transactional
	public void selectUserEntity(UserEntityMappingVo userEntityMappingVo, AuthDetailsVo authDetailsVo)
			throws CommonException {

		// get logged in user Role
		UserEntity userEntity = userEntityMappingDao.getLoggedInUserRole(authDetailsVo.getUserId());

		UserRoleVO userRoleVO = null;
		if (null != userEntityMappingVo.getEntityId() && null != userEntity.getUserRoleEntity()
				&& null != userEntity.getUserRoleEntity().getUserRoleName()) {
			userRoleVO = userEntityMappingDao.getSelectedEntityRole(userEntityMappingVo.getEntityId(),
					userEntity.getUserRoleEntity().getUserRoleName());
		}

		if (null == userRoleVO) {
			throw new CommonException("roleNotAvailable");
		}

		// update Changed Entity Role
		if (null != userRoleVO && null != userRoleVO.getId()) {

			userEntityMappingDao.updateUserEntityWithRole(authDetailsVo.getUserId(), userEntityMappingVo.getEntityId(),
					userRoleVO.getId());

		} else {
			userEntityMappingDao.updateUserEntity(authDetailsVo.getUserId(), userEntityMappingVo.getEntityId());
		}

	}
	
	
	@Transactional
	public void updateExpiryEntity() {

		List<Object[]> entityLicenseList = entitySelectionDao.getAllExpiredEntityList();

		for (Object approval : entityLicenseList) {

			if (null != (Integer) ((Object[]) approval)[0]) {
				int entityId = (int) ((Object[]) approval)[0];
				entitySelectionDao.updateEntityStatusForExpiry(entityId);
			}

		}

	}
	
}
