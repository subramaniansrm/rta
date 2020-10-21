package com.srm.rta.dao;

import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.config.UserMessages;
import com.srm.coreframework.dao.CommonDAO;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.util.DateUtil;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.rta.entity.RinFlashNewsEntity;
import com.srm.rta.vo.FlashNewsVO;

@Repository
public class FlashNewsDAO extends CommonDAO {
	
	Logger logger = LoggerFactory.getLogger(FlashNewsDAO.class);
	
	@Autowired
	UserMessages userMessages;
	
	
	@SuppressWarnings("unchecked")
	public List<RinFlashNewsEntity> search(FlashNewsVO flashNewsVo,AuthDetailsVo authDetailsVo){
		
		//Map<String, Object> param = new HashMap<>();
		
		try {
			String query = "FROM RinFlashNewsEntity  where deleteFlag = " + CommonConstant.FLAG_ZERO
					+ " and entityLicenseId =" + authDetailsVo.getEntityId();
		
			StringBuilder modifiedQuery = new StringBuilder(query);
			
			if (flashNewsVo.getId() != null)
				modifiedQuery.append(" and id = " + flashNewsVo.getId());

			if (flashNewsVo.getFlashNewsCode() != null && !flashNewsVo.getFlashNewsCode().isEmpty()) {
				modifiedQuery.append(" and LOWER(flashNewsCode) LIKE LOWER('%" + flashNewsVo.getFlashNewsCode() + "%')");
			}
			if (flashNewsVo.getFlashNewsType() != null) {
				modifiedQuery.append(" and flashNewsType = " + flashNewsVo.getFlashNewsType());
			}
			if (flashNewsVo.getFlashNewsDescription() != null) {
				modifiedQuery.append(" and LOWER(flashNewsDescription) LIKE LOWER('%" + flashNewsVo.getFlashNewsDescription() + "%')");
			}

			if (flashNewsVo.getFlashNewsDate() != null) {
				modifiedQuery.append(" and date(flashNewsDate)  =  '"
						+ DateUtil.dateToString(flashNewsVo.getFlashNewsDate(), DateUtil.DATE_YYYY_MM_DD) + "'");
			}
			if (flashNewsVo.getFlashNewsValidFrom() != null) {

				modifiedQuery.append(" and date(flashNewsValidFrom)  =  '"
						+ DateUtil.dateToString(flashNewsVo.getFlashNewsValidFrom(), DateUtil.DATE_YYYY_MM_DD) + "'");
			}
			if (flashNewsVo.getFlashNewsValidTo() != null) {
				modifiedQuery.append(" and date(flashNewsValidTo)  =  '"
						+ DateUtil.dateToString(flashNewsVo.getFlashNewsValidTo(), DateUtil.DATE_YYYY_MM_DD) + "'");
			}
			if (flashNewsVo.getStatus() != null) {
				if (flashNewsVo.getStatus().equals(CommonConstant.Active)) {
					modifiedQuery.append(" and isFlashNewsActive =" + CommonConstant.ACTIVE);
				} else {
					modifiedQuery.append(" and isFlashNewsActive =" + CommonConstant.CONSTANT_ZERO);
				}
			}
			modifiedQuery.append(" ORDER BY id DESC");
			Query querys = getEntityManager().createQuery(modifiedQuery.toString());
			//CommonUtil.setQueryParam(querys, param);

			List<RinFlashNewsEntity> resultList = querys.getResultList();
			
		return resultList;
		
		} catch (Exception e) {
			logger.error("error", e);
			throw new CommonException(userMessages.getDbFailure());
		}

		
		
	}

}
