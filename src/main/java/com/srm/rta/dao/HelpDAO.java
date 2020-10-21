package com.srm.rta.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import com.srm.coreframework.auth.AuthUtil;
import com.srm.coreframework.dao.CommonDAO;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.rta.entity.HelpEntity;
import com.srm.rta.vo.HelpVO;


@Repository
public class HelpDAO extends CommonDAO {
	
	@SuppressWarnings("unchecked")
	public List<HelpVO> getHelpVoList(HelpVO helpRequest,AuthDetailsVo authDetailsVo) {
		
		List<HelpVO> helpList = new ArrayList<>();
		
		List<HelpEntity> helpRecords = new ArrayList<>();
		
		if (null != helpRequest.getHelpSearchText() && !"".equals(helpRequest.getHelpSearchText())) {
			
			helpRecords = (List<HelpEntity>) getEntityManager()
					.createQuery("SELECT c FROM  HelpEntity c where  entityLicenseId =" + authDetailsVo.getEntityId()
							+ " and c.topic like '%'||:searchText||'%' OR " + "c.detail like '%'||:searchText||'%' ")
					.setParameter("searchText", helpRequest.getHelpSearchText()).getResultList();
		} else {
			helpRecords = (List<HelpEntity>) getEntityManager().createQuery("FROM HelpEntity c").getResultList();
		}
		HelpVO helpVo = new HelpVO();
		
		for (HelpEntity helpEntity : helpRecords) {
			if (null != helpEntity.getTopic() || !"".equals(helpEntity.getTopic())) {
			}
			helpVo = new HelpVO();
			
			BeanUtils.copyProperties(helpEntity, helpVo);
			
			helpList.add(helpVo);
		}
		return helpList;
	}

}
