package com.srm.rta.dao;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.srm.coreframework.dao.CommonDAO;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.rta.entity.WidgetDetailEntity;
import com.srm.rta.entity.WidgetEntity;
import com.srm.rta.vo.WidgetDetailVO;
import com.srm.rta.vo.WidgetVO;

@Repository
public class WidgetDAO extends CommonDAO {
	
	public int findDuplicateIndex(WidgetVO widgetVo,AuthDetailsVo authDetailsVo) {
		int count = 0;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String fromDate = formatter.format(widgetVo.getWidgetDetailVoList().get(0).getWidgetDetailAnnouncementDate());

		String query = "SELECT COUNT(w.widgetId) FROM  WidgetEntity  w , WidgetDetailEntity wd "
				+ " WHERE w.widgetId = wd.widgetId" 
				+ " and  w.entityLicenseId = '"+ authDetailsVo.getEntityId()+"'"
				+ " AND w.widgetIndex = "+widgetVo.getWidgetIndex()+" "
				+ " AND w.widgetIsActive = "+CommonConstant.ACTIVE+" "
				+ " AND w.deleteFlag = "+CommonConstant.FLAG_ZERO+"  ";
				
		StringBuffer modifiedQuery = new StringBuffer(query);
		if (null != widgetVo.getWidgetId()) {
			modifiedQuery.append(" AND w.widgetId != " + widgetVo.getWidgetId());
		}

		
		count = (int) (long) getEntityManager().createQuery(modifiedQuery.toString()).getSingleResult();

		return count;
		
		
		
	}

	public List<WidgetEntity> getAllSearch(WidgetVO widgetVo,AuthDetailsVo authDetailsVo) {
		StringBuffer query = new StringBuffer("FROM WidgetEntity where  entityLicenseId = '"+ authDetailsVo.getEntityId()+"'" + " and deleteFlag = " + CommonConstant.FLAG_ZERO);
		if (widgetVo.getWidgetId() != null)
			query.append(" and widgetId = " + widgetVo.getWidgetId());

		if (widgetVo.getWidgetIndex() != null)
			query.append(" and widgetIndex = " + widgetVo.getWidgetIndex());

		if (widgetVo.getWidgetSeq() != null)
			query.append(" and widgetSeq = " + widgetVo.getWidgetSeq());

		if (widgetVo.getWidgetCode() != null && !widgetVo.getWidgetCode().isEmpty())
			query.append(" and LOWER(widgetCode) LIKE LOWER('%" + widgetVo.getWidgetCode() + "%')");

		if (widgetVo.getWidgetIcon() != null && !widgetVo.getWidgetIcon().isEmpty())
			query.append(" and LOWER(widgetIcon) LIKE LOWER('%" + widgetVo.getWidgetIcon() + "%')");

		if (widgetVo.getWidgetTitle() != null && !widgetVo.getWidgetTitle().isEmpty())
			query.append(" and LOWER(widgetTitle) LIKE LOWER('%" + widgetVo.getWidgetTitle() + "%')");

		if (widgetVo.getStatus() != null) {
			if (widgetVo.getStatus().equals(CommonConstant.Active)) {
				query.append(" and widgetIsActive =" + CommonConstant.ACTIVE);
			} else {
				query.append(" and widgetIsActive =" + CommonConstant.CONSTANT_ZERO);
			}
		}

		query.append(" ORDER BY widgetId DESC ");

		@SuppressWarnings("unchecked")
		List<WidgetEntity> widgetEntityList = (List<WidgetEntity>) getEntityManager().createQuery(query.toString())
				.getResultList();

		return widgetEntityList;
	}

	public List<Object> findWidgetRecord(int widgetId,AuthDetailsVo authDetailsVo) {

		String query = " select wid.widgetId,wid.widgetCode,wid.widgetIndex,wid.widgetTitle,"
				+ " wid.widgetIcon,wid.widgetSeq,wid.widgetIsActive,wdet.widgetDetailId,wdet.widgetId,"
				+ " wdet.widgetDetailHeading,wdet.widgetDetailHeadingIndex,wdet.widgetDetailPicIsRequired,"
				+ " wdet.widgetDetailPicPath,wdet.widgetDetailDescription,wdet.widgetDetailAttIsRequired,"
				+ " wdet.widgetDetailAttPath,wdet.widgetDetailMorePath,wdet.widgetDetailExternalUrl,"
				+ " wdet.widgetDetailIsActive,wdet.widgetDetailAnnouncementDate,wdet.widgetDetailValidFrom,wdet.widgetDetailValidTo"
				+ " FROM WidgetEntity wid , WidgetDetailEntity wdet where wid.widgetId = wdet.widgetId"
				+ " and wid.entityLicenseId = '"+ authDetailsVo.getEntityId()+"'"
				+ " and wid.deleteFlag = " + CommonConstant.FLAG_ZERO + " AND wid.widgetId = " + widgetId;

		@SuppressWarnings("unchecked")
		List<Object> result = (List<Object>) getEntityManager().createQuery(query).getResultList();

		return result;

	}

	public List<WidgetEntity> getWidgetHeader(WidgetDetailVO widgetDetailVo,AuthDetailsVo authDetailsVo) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String date = formatter.format(widgetDetailVo.getWidgetDetailAnnouncementDate());

		String query = "select distinct wid FROM WidgetEntity wid, WidgetDetailEntity widet "
				+ " where wid.widgetId = widet.widgetId"
				+ " and wid.entityLicenseId = '"+ authDetailsVo.getEntityId()+"' and " 
				+ " wid.deleteFlag = " + CommonConstant.FLAG_ZERO
				+ " and widet.deleteFlag = " + CommonConstant.FLAG_ZERO
				+ " and ((widet.widgetDetailAnnouncementDate <= '" + date + "' and widet.widgetDetailValidTo >= '"
				+ date + "')" + " or (widet.widgetDetailAnnouncementDate like '%" + date
				+ "%')) and wid.widgetIsActive = " + CommonConstant.ACTIVE + " and widet.widgetDetailIsActive = "
				+ CommonConstant.ACTIVE + " ORDER BY wid.widgetIndex asc ";

		Query q = getEntityManager().createQuery(query);

		List<WidgetEntity> list_WidgetEntity = (List<WidgetEntity>) q.getResultList();

		return list_WidgetEntity;
	}

	public List<WidgetEntity> fetchData(WidgetDetailVO widgetDetailVo,AuthDetailsVo authDetailsVo) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String fromDate = formatter.format(widgetDetailVo.getWidgetDetailValidFrom());
		String toDate = formatter.format(widgetDetailVo.getWidgetDetailValidTo());

		String query = "select distinct wid FROM WidgetEntity wid join WidgetDetailEntity widet "
				+ "on wid.widgetId = widet.widgetId where "
				+ " widet = entityLicenseId = '"+ authDetailsVo.getEntityId()+"' and wid.deleteFlag = " + CommonConstant.FLAG_ZERO
				+ " and widet.deleteFlag = " + CommonConstant.FLAG_ZERO + " and "
				+ " ((widet.widgetDetailAnnouncementDate <= '" + fromDate + "' and widet.widgetDetailValidTo >= '"
				+ fromDate + "'" + " or  widet.widgetDetailAnnouncementDate <= '" + toDate
				+ "' and widet.widgetDetailValidTo >= '" + toDate + "'" + " or widet.widgetDetailAnnouncementDate >= '"
				+ fromDate + "' and widet.widgetDetailValidTo <= '" + toDate + "') "
				+ " or (widet.widgetDetailAnnouncementDate like '%" + fromDate + "%')) and wid.widgetIsActive = "
				+ CommonConstant.ACTIVE + " and widet.widgetDetailIsActive = " + CommonConstant.ACTIVE
				+ " ORDER BY wid.widgetIndex asc ";

		List<WidgetEntity> widgetEntityList = (List<WidgetEntity>) getEntityManager().createQuery(query).getResultList();

		return widgetEntityList;
	}

	@SuppressWarnings("unchecked")
	public List<WidgetDetailEntity> getWidgetDetail(int widgetId, WidgetDetailVO widgetDetailVo,AuthDetailsVo authDetailsVo) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String date = formatter.format(widgetDetailVo.getWidgetDetailAnnouncementDate());
		String query = "FROM WidgetDetailEntity "
				+ " where entityLicenseId = '"+ authDetailsVo.getEntityId()+"'" 
				+ " and deleteFlag = " + CommonConstant.FLAG_ZERO + " and widgetId = "
				+ widgetId + " and ((widgetDetailAnnouncementDate <= '" + date + "' and widgetDetailValidTo >= '" + date
				+ "') or (widgetDetailAnnouncementDate like '%" + date + "%')) and widgetDetailIsActive = "
				+ CommonConstant.ACTIVE + " ORDER BY widgetDetailHeadingIndex asc ";

		List<WidgetDetailEntity> list_WidgetDetailEntity = (List<WidgetDetailEntity>) getEntityManager()
				.createQuery(query).getResultList();
		return list_WidgetDetailEntity;
	}

	@SuppressWarnings("unchecked")
	public List<WidgetDetailEntity> getWidgetDetailEntityList(int widgetId, WidgetDetailVO widgetDetailVo,AuthDetailsVo authDetailsVo) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String fromDate = formatter.format(widgetDetailVo.getWidgetDetailValidFrom());
		String toDate = formatter.format(widgetDetailVo.getWidgetDetailValidTo());

		String query = "  select wd FROM WidgetDetailEntity wd WHERE"
			    + " wd.entityLicenseId = '"+ authDetailsVo.getEntityId()+"'"
				+ " and wd.deleteFlag =" + CommonConstant.FLAG_ZERO
				+ " and ((wd.widgetDetailAnnouncementDate <= '" + fromDate + "' and wd.widgetDetailValidTo >= '" + fromDate
				+ "')" + " or (wd.widgetDetailAnnouncementDate <= '" + toDate + "' and wd.widgetDetailValidTo >= '" + toDate
				+ "')" + " or (wd.widgetDetailAnnouncementDate >= '" + fromDate + "' and wd.widgetDetailValidTo <= '" + toDate
				+ "'))" + " and wd.widgetId = " + widgetId + " and wd.widgetDetailIsActive = " + CommonConstant.ACTIVE
				+ " ORDER BY wd.widgetDetailHeadingIndex asc ";

		List<WidgetDetailEntity> widgetDetailEntityList = (List<WidgetDetailEntity>) getEntityManager().createQuery(query)
				.getResultList();

		return widgetDetailEntityList;
	}
	
	

}
