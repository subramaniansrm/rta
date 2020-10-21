package com.srm.rta.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.srm.coreframework.auth.AuthUtil;
import com.srm.coreframework.dao.CommonDAO;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.rta.entity.FaqEntity;

@Repository
public class FaqDAO extends CommonDAO {
	

	/**
	 * This method is to retrieve frequently asked question details from faq
	 * table
	 * 
	 * @return FaqForm
	 */
	public List<FaqEntity> getCommonFaq(AuthDetailsVo authDetailsVo) {

		String query = "select c FROM FaqEntity c where c.deleteFlag = " + CommonConstant.FLAG_ZERO +" and c.entityLicenseId = " + authDetailsVo.getEntityId() ;
		// " ORDER BY create_date DESC ";

		@SuppressWarnings("unchecked")
		List<FaqEntity> listFaqEntity = (List<FaqEntity>) getEntityManager().createQuery(query).getResultList();

		return listFaqEntity;

	}


}
