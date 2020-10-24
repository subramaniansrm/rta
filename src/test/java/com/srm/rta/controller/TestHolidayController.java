package com.srm.rta.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.OngoingStubbing;

import com.srm.coreframework.response.JSONResponse;
import com.srm.coreframework.util.CommonConstant;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.coreframework.vo.CommonVO;
import com.srm.coreframework.vo.ScreenJsonVO;
import com.srm.rta.service.HolidayService;
import com.srm.rta.vo.HolidayVO;

@RunWith(MockitoJUnitRunner.class)
public class TestHolidayController {

	@Mock
	HolidayService holidayMasterService;

	@Mock
	private HttpServletRequest httpServletRequestMock;

	@Mock
	private HttpSession httpSessionMock;

	@Mock
	private ServletContext servletContextMock;

	@InjectMocks
	HolidayController holidayMasterController;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void holidaylist() throws IOException {

		JSONResponse jsonResponse = new JSONResponse();

		List<HolidayVO> holidayVoList = new ArrayList<HolidayVO>();

		String accessToken = getHeaderAccessToken(httpServletRequestMock);

		AuthDetailsVo authDetailsVo = new AuthDetailsVo();

		authDetailsVo = tokenValidate(accessToken);

		HolidayVO holidayVo = new HolidayVO();

		CommonVO commonVO = new CommonVO();
		ScreenJsonVO screenJson = new ScreenJsonVO();
		screenJson.setScreenId(1);
		screenJson.setSubScreenId(1);

		commonVO.setScreenJson(screenJson);

		holidayVo.setDescription("International Holiday");
		holidayVo.setHolidayDate(CommonConstant.getCalenderDate());
		holidayVo.setId(1);
		holidayVo.setLeaveType(1);
		holidayVoList.add(holidayVo);

		jsonResponse.setResponseCode("200");
		jsonResponse.setSuccesObject(holidayVoList);
		jsonResponse.setAuthSuccesObject(commonVO);

		when(httpServletRequestMock.getSession()).thenReturn(httpSessionMock);
		when(httpSessionMock.getServletContext()).thenReturn(servletContextMock);

		

		when(holidayMasterService.getScreenFields(screenJson, authDetailsVo)).thenReturn(commonVO);
		when(holidayMasterService.getHolidayList(authDetailsVo)).thenReturn(holidayVoList);

		holidayMasterController.holidaylist(httpServletRequestMock, screenJson);

		List<HolidayVO> holiday = new ArrayList<HolidayVO>();

		if (null != (List<HolidayVO>) jsonResponse.getSuccesObject()) {
			holiday = (List<HolidayVO>) jsonResponse.getSuccesObject();

			assertEquals("International Holiday", holiday.get(0).getDescription());
			assertEquals(new Integer(1), holiday.get(0).getLeaveType());
			assertEquals("200", jsonResponse.getResponseCode());
		}
	}

	public AuthDetailsVo tokenValidate(String accessToken) {
		AuthDetailsVo authDetailsVo = new AuthDetailsVo();
		authDetailsVo.setUserId(1);

		return authDetailsVo;
	}

	public String getHeaderAccessToken(HttpServletRequest request) {

		return "ZXlKaGJHY2lPaUpJVXpJMU5pSXNJblI1Y0NJNklrcFhWQ0o5LmV5SmxlSEFpT2pFMU9USXdO"
				+ "ekl3TmpNc0luVnpaWEpFWlhSaGFXeHpJanA3SW14cGMzUWlPbTUxYkd3c0luVnpaWEp"
				+ "NYjJkcGJrbGtJam9pYzJocGNtdGxiQ0lzSW5CaGMzTjNiM0prSWpvaUpESmhKREV3SkU4MWJq"
				+ "RlNZM1ZxZFhkUVQwbGhTVGRDVDFWNGJpNWFWVGhCU1ZGUGFrMVVSbTFsYlVaWVNtMVNTRlY0VEd"
				+ "0MVRUTlpjRUV5SWl3aVptbHljM1JPWVcxbElqb2lUR0ZzYVhRaUxDSnNZWE4wVG1GdFpTS"
				+ "TZJbE5vYVhKclpTSXNJbTF2WW1sc1pVNTFiV0psY2lJNmJuVnNiQ3dpWlcxaGFXd2lPb"
				+ "TUxYkd3c0luVnpaWEpKWkNJNk1pd2ljM1Z3WlhKQlpHMXBiaUk2Ym5Wc2JDd2laV"
				+ "zUwYVhSNVNXUWlPakFzSW5KdmJHVkpaQ0k2TVN3aVlXTmpaWE56Vkc5clpXNGlPbTUxYkd3"
				+ "c0luTjBZWFIxY3lJNmRISjFaU3dpZFhObGNtNWhiV1VpT2lKemFHbHlhMlZzSWl3aVpXNW"
				+ "hZbXhsWkNJNmRISjFaU3dpWVhWMGFHOXlhWFJwWlhNaU9tNTFiR3dzSW1GalkyOTFiblJPY"
				+ "jI1RmVIQnBjbVZrSWpwMGNuVmxMQ0poWTJOdmRXNTBUbTl1VEc5am"
				+ "EyVmtJanAwY25WbExDSmpjbVZrWlc1MGFXRnNjMDV2YmtWNGNHbHlaV1FpT25SeWRXVjlMQ0oxYz"
				+ "JWeVgyNWhiV1VpT2lKemFHbHlhMlZzSWl3aWFuUnBJam9pWkRObU5ETmtOekl0TkRZMVpTMDBaV05oT"
				+ "FdKaU5UY3RPR0V3TVRZM1pqVTRZbUkzSWl3aVkyeHBaVzUwWDJsa0lqb2lkR1Z6ZEdwM2RHTnNhV1Z1"
				+ "ZEdsa0lpd2ljMk52Y0dVaU9sc2ljbVZoWkNJc0luZHlhWFJsSWl3aWRISjFjM1FpWFgwLnpuemJiT3hRQjdUZ1"
				+ "N3cmV1Z1A1Tlg0ano2ZTJfTmRXSTFmOW1keFkwdk0=";
	}

}
