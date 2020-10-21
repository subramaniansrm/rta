package com.srm.rta.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.srm.coreframework.dao.CommonDAO;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.coreframework.vo.MailParameterVO;

@Repository
public class NotificationDAO extends CommonDAO {

	
	@SuppressWarnings("unchecked")
	public List<Object[]> getAlllMailNotification(MailParameterVO mailParameterVo,AuthDetailsVo authDetailsVo) {

		int limit = 10;

		int offset = 0;
		if (mailParameterVo != null) {
			if (mailParameterVo.getPageLimit() != null) {
				limit = mailParameterVo.getPageLimit();
			} else {
				mailParameterVo.setPageLimit(limit);
			}

			if (mailParameterVo.getPageNo() != null) {
				offset = mailParameterVo.getPageNo() * limit;
			}
		}

		String query = "SELECT  m.maillog_number,m.message_code,m.title,m.message,m.register_datetime "
				+ " FROM rta_2_local.mail_log m "
				+ " WHERE m.user_Id = "
				+ authDetailsVo.getUserId()
				+ " AND  m.rin_ma_entity_id = " + authDetailsVo.getEntityId()
				+ " AND  m.notification_flag = " + CommonConstant.CONSTANT_ZERO
				+ " order by m.maillog_number desc";

		List<Object[]> maillogEntity = (List<Object[]>)getEntityManager().createNativeQuery(query).getResultList();
	
		return maillogEntity;

	}

	public void updateNotificationFlag(int id,AuthDetailsVo authDetailsVo) {

		String query = "update MaillogEntity  set notificationFlag = " + CommonConstant.CONSTANT_ONE
				+ " ,createUserId = " + authDetailsVo.getUserId() + " ,lastUpdateDatetime = ' "
				+ CommonConstant.getCurrentDateTimeAsString() + " ' " + " where maillogNumber = " + id;

		getEntityManager().createQuery(query).executeUpdate();

	}

}
