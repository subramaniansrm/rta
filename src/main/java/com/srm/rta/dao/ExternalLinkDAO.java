package com.srm.rta.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.srm.coreframework.dao.CommonDAO;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.rta.entity.ExternalLinkEntity;
import com.srm.rta.vo.ExternalLinkVO;

@Repository
public class ExternalLinkDAO extends CommonDAO {
	
	@SuppressWarnings("unchecked")
	public List<ExternalLinkEntity> search(ExternalLinkVO externalLinkVo,AuthDetailsVo authDetailsVo) {

		StringBuffer query = new StringBuffer(
				"FROM ExternalLinkEntity where entityLicenseId ="+authDetailsVo.getEntityId()
				+ " and deleteFlag = " + CommonConstant.FLAG_ZERO);
		if (null != externalLinkVo.getId() ) {
			query.append(" and id = " + externalLinkVo.getId());
		}
		if (externalLinkVo.getExternalLinkName() != null && !externalLinkVo.getExternalLinkName().isEmpty()) {
			query.append(" and LOWER(externalLinkName) LIKE LOWER('%" + externalLinkVo.getExternalLinkName() + "%')");
		}
		if (externalLinkVo.getExternalLinkLogo() != null && !externalLinkVo.getExternalLinkLogo().isEmpty()) {
			query.append(" and LOWER(externalLinkLogo) LIKE LOWER('%" + externalLinkVo.getExternalLinkLogo() + "%')");
		}
		if (externalLinkVo.getExternalLinkUrl() != null && !externalLinkVo.getExternalLinkUrl().isEmpty()) {
			query.append(" and LOWER(externalLinkUrl) LIKE LOWER('%" + externalLinkVo.getExternalLinkUrl() + "%')");
		}
		if (null != externalLinkVo.getExternalLinkDisplaySeq() ) {
			query.append(" and externalLinkDisplaySeq = " + externalLinkVo.getExternalLinkDisplaySeq());
		}
		if (externalLinkVo.getStatus() != null) {
			if (externalLinkVo.getStatus().equals(CommonConstant.Active)) {
				query.append(" and externalLinkIsActive =" + CommonConstant.ACTIVE);
			} else {
				query.append(" and externalLinkIsActive =" + CommonConstant.CONSTANT_ZERO);
			}
		}
		query.append(" order by id asc ");

		List<ExternalLinkEntity> externalLinkEntityList = (List<ExternalLinkEntity>) getEntityManager()
				.createQuery(query.toString()).getResultList();

		return externalLinkEntityList;
	}
	
	public int findDuplicate(ExternalLinkVO externalLinkVO,AuthDetailsVo authDetailsVo) {

		int count = 0;

		String query = "SELECT COUNT(r.id) FROM ExternalLinkEntity r  "
				+ " where entityLicenseId = '"+ authDetailsVo.getEntityId()+"' and r.deleteFlag = "
				+ CommonConstant.FLAG_ZERO 
				/*+ " AND LOWER(r.externalLinkName) = LOWER('"
				+ externalLinkVO.getExternalLinkName().trim()+ "')"*/
						+ " AND r.externalLinkDisplaySeq = " +externalLinkVO.getExternalLinkDisplaySeq();
	
		
		if (null!= externalLinkVO.getId()) {
			query = query + " AND r.id != " + externalLinkVO.getId();
		} 
		
		  count = (int)(long) getEntityManager().createQuery(query).getSingleResult();

		return count;
	}
	

}
