package com.srm.rta.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.rta.dao.HelpDAO;
import com.srm.rta.vo.HelpVO;


@Service
public class HelpService  {
	
	
	@Autowired
	HelpDAO helpDAO;
	
	
	@Transactional
	public List<HelpVO> getHelpVoList(HelpVO helpRequest,AuthDetailsVo authDetailsVo) {
		return helpDAO.getHelpVoList(helpRequest,authDetailsVo);
	}
}
