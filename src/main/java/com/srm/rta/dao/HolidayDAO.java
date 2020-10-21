package com.srm.rta.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.srm.coreframework.dao.CommonDAO;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.util.DateUtil;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.rta.entity.Holiday;
import com.srm.rta.entity.HolidayDetails;
import com.srm.rta.vo.HolidayDetailsVO;
import com.srm.rta.vo.HolidayVO;

@Repository
public class HolidayDAO extends CommonDAO {

	@SuppressWarnings("unchecked")
	public List<Holiday> getHolidaySearch(HolidayVO holidayVo,AuthDetailsVo authDetailsVo) {

		String query = "FROM Holiday c WHERE c.entityLicenseId = " + authDetailsVo.getEntityId()
				+ " AND c.deleteFlag  = '" + CommonConstant.FLAG_ZERO + "'";

		if (null != holidayVo.getLeaveType()) {
			query = query + " and c.leaveType = " + holidayVo.getLeaveType();
		}

		if (holidayVo.getHolidayDate() != null) {
			query = query + " and date(holidayDate)  =  '"
					+ DateUtil.dateToString(holidayVo.getHolidayDate(), DateUtil.DATE_YYYY_MM_DD) + "'";
		}

		if (holidayVo.getDescription() != null && !holidayVo.getDescription().isEmpty()) {
			query = query + " and LOWER(c.description) LIKE LOWER('%" + holidayVo.getDescription() + "%')";
		}
		query = query + " ORDER BY id DESC";

		List<Holiday> list = (List<Holiday>) getEntityManager().createQuery(query).getResultList();

		return list;
	}

	public void deleteHoliday(int id,AuthDetailsVo authDetailsVo) {

		String query = " UPDATE "+getRtaDatabaseSchema()+".holiday h LEFT JOIN "+getRtaDatabaseSchema()+".holiday_detail hd ON hd.HOLIDAY_ID = h.HOLIDAY_ID"
				+ " SET h.delete_flag = '" + CommonConstant.FLAG_ONE + "'" + " ,hd.delete_flag = '"
				+ CommonConstant.FLAG_ONE + "'" + " ,h.update_by = " + authDetailsVo.getUserId()
				+ " ,h.update_date = '" + CommonConstant.getCurrentDateTimeAsString() + " ',hd.update_by = "
				+ authDetailsVo.getUserId() + " ,hd.update_date = '" + CommonConstant.getCurrentDateTimeAsString()
				+ " ' WHERE h.HOLIDAY_ID = " + id;

		getEntityManager().createNativeQuery(query).executeUpdate();

	}
	
	
	@SuppressWarnings("unchecked")
	public List<HolidayDetails> getAllHolidayList(HolidayDetailsVO holidayDetails,AuthDetailsVo authDetailsVo) {

		String query = "FROM HolidayDetails c WHERE  "
		      + " c.holidayDate like '%-" + holidayDetails.getMonth() + "-%'" + " and c.entityLicenseEntity.id = " + authDetailsVo.getEntityId()
				+ " AND c.deleteFlag  = '" + CommonConstant.FLAG_ZERO + "' ORDER BY id DESC ";

		List<HolidayDetails> list = (List<HolidayDetails>) getEntityManager().createQuery(query)
				.getResultList();

		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	public ArrayList<Integer> getWeekendDays(Integer locationId ,Integer subLocationId,AuthDetailsVo authDetailsVo) {
		
		String query = " SELECT det.rin_ma_weekend_day FROM "
		             +   ""+getCommonDatabaseSchema()+".weekend wk "
                     + " LEFT JOIN "
		             + " "+getCommonDatabaseSchema()+".weekend_details det ON wk.idrin_ma_weekend_id = det.rin_ma_weekend_id "
                     + " WHERE wk.rinma_location_id = "+ locationId +" AND det.rin_ma_sublocation_id = " + subLocationId 
                     + " AND wk.delete_flag = 0 AND wk.rinma_active = 1 "
                     + " AND det.delete_flag = 0 AND det.rinma_active = 1 "
                     + " AND wk.rin_ma_entity_id = "+ authDetailsVo.getEntityId()
                     + " GROUP BY det.idrin_ma_weekend_details_id ";

		ArrayList<Integer> list = (ArrayList<Integer>) getEntityManager().createNativeQuery(query)
					.getResultList();	
		
		   return list;		
	}	
	
	
	@SuppressWarnings("unchecked")
	public ArrayList<Date> getHoliDays(Integer locationId, Integer sublocationId, AuthDetailsVo authDetailsVo) {

		ArrayList<Date> datelist = null;

		String query = " SELECT h.HOLIDAY_DATE FROM holiday h "
				+ " LEFT JOIN  holiday_detail hd ON hd.HOLIDAY_ID = h.HOLIDAY_ID " + " WHERE hd.LOCATION_ID = "
				+ locationId + " AND hd.SUB_LOCATION_ID = " + sublocationId + " AND h.rin_ma_entity_id = "
				+ authDetailsVo.getEntityId() + " AND h.delete_flag = 0 AND hd.delete_flag = 0";

		datelist = (ArrayList<Date>) getEntityManager().createNativeQuery(query).getResultList();

		return datelist;
	}
	
}
