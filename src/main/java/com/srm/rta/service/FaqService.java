package com.srm.rta.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.srm.coreframework.config.CommonException;
import com.srm.coreframework.service.CommonService;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.rta.dao.FaqDAO;
import com.srm.rta.entity.FaqEntity;
import com.srm.rta.vo.FaqVO;

@Service
public class FaqService extends CommonService {

	org.slf4j.Logger logger = LoggerFactory.getLogger(FaqService.class);

	@Autowired
	FaqDAO faqDao;

	/**
	 * This method is to retrieve frequently asked question details from faq
	 * table
	 * 
	 * @return FaqVo
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws BusinessException
	 */
	@Transactional
	public List<FaqVO> getCommonFaq(AuthDetailsVo authDetailsVo) throws CommonException, IllegalAccessException, InvocationTargetException {

		List<FaqVO> questionAnswersList = new ArrayList<FaqVO>();
		List<FaqEntity> faqRecords = null;
		try {
			faqRecords = faqDao.getCommonFaq(authDetailsVo);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CommonException(getMessage("dataFailure",authDetailsVo));
		}
		for (FaqEntity faqEntity : faqRecords) {
			FaqVO faqVo = new FaqVO();
			BeanUtils.copyProperties(faqVo, faqEntity);
			questionAnswersList.add(faqVo);
		}
		return questionAnswersList;

	}

}
