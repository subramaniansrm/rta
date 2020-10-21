package com.srm.rta.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.srm.coreframework.constants.FilePathConstants;
import com.srm.coreframework.response.JSONResponse;
import com.srm.coreframework.service.CommonService;
import com.srm.coreframework.vo.AuthDetailsVo;
import com.srm.coreframework.vo.ScreenJsonVO;
import com.srm.coreframework.vo.SubLocationVO;

@Service
public class SubLocationRtService extends CommonService {

	@Value("${restTemplateUrl}")
	private String restTemplateUrl;
	
	@Autowired
	RestTemplate restTemplate;

	public ResponseEntity<JSONResponse> getAll(ScreenJsonVO screenJson) {
		JSONResponse response = new JSONResponse();
		try {
			
			String url = restTemplateUrl + FilePathConstants.SUBLOCATION_LIST_RT;
			response = restTemplate.postForObject(url, screenJson, JSONResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	public ResponseEntity<JSONResponse> add(SubLocationVO subLocationVo) {
		JSONResponse response = new JSONResponse();
		try {

			String url = restTemplateUrl + FilePathConstants.SUBLOCATION_ADD_RT;
			response = restTemplate.postForObject(url, subLocationVo, JSONResponse.class);

		} catch (Exception e) {

		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	public ResponseEntity<JSONResponse> createsublocation(SubLocationVO subLocationVo) {
		JSONResponse response = new JSONResponse();
		try {


			String url = restTemplateUrl + FilePathConstants.SUBLOCATION_CREATE_RT;
			response = restTemplate.postForObject(url, subLocationVo, JSONResponse.class);
			
		} catch (Exception e) {

		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	public ResponseEntity<JSONResponse> updatesublocation(SubLocationVO subLocationVo) {
		JSONResponse response = new JSONResponse();
		try {


			String url = restTemplateUrl + FilePathConstants.SUBLOCATION_UPDATE_RT;
			response = restTemplate.postForObject(url, subLocationVo, JSONResponse.class);
			
		} catch (Exception e) {
			response.setResponseCode("500");
			response.setResponseMessage("error");
		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	public ResponseEntity<JSONResponse> getAllSearch(SubLocationVO subLocationVo) {
		JSONResponse response = new JSONResponse();
		try {

			String url = restTemplateUrl + FilePathConstants.SUBLOCATION_SEARCH_RT;
			response = restTemplate.postForObject(url, subLocationVo, JSONResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	public ResponseEntity<JSONResponse> viewSublocation(SubLocationVO subLocationVo) {
		JSONResponse response = new JSONResponse();
		try {

			/*response = restTemplate.postForObject("http://localhost:8082/srm-core-security/sr/sub/RT/load",
					subLocationVo, JSONResponse.class);*/
			String url = restTemplateUrl + FilePathConstants.SUBLOCATION_VIEW_RT;
			response = restTemplate.postForObject(url, subLocationVo, JSONResponse.class);

		} catch (Exception e) {

		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	public ResponseEntity<JSONResponse> deleteSublocation(SubLocationVO subLocationVo) {
		JSONResponse response = new JSONResponse();
		try {

			/*response = restTemplate.postForObject("http://localhost:8082/srm-core-security/sr/sub/RT/delete",
					subLocationVo, JSONResponse.class);*/

			String url = restTemplateUrl + FilePathConstants.SUBLOCATION_DELETE_RT;
			response = restTemplate.postForObject(url, subLocationVo, JSONResponse.class);
			
		} catch (Exception e) {

		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
}
