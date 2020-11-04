package com.nomad.data.agent.dataset.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nomad.data.agent.dataset.dto.info.AipDataObjectInfo;
import com.nomad.data.agent.dataset.dto.req.DataSourceConnectionTestReq;
import com.nomad.data.agent.dataset.dto.req.DataSourceExtractReq;
import com.nomad.data.agent.dataset.service.dataset.DataSourceService;
import com.nomad.data.agent.user.service.UserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/datasource")
public class DataSourceController {

	@Autowired
	private DataSourceService dataSourceService;
	
	@Autowired
	private UserService userService;
	
	@ApiOperation(value = "데이터 소스/데이터 소스 접속 테스트", notes = "데이터 소스/데이터 소스 접속 테스트", tags = "데이터 소스")
	@RequestMapping(method = RequestMethod.POST, value = "connection-test", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity connectionTest(
			@ApiParam(required = true, value = "토큰", defaultValue = "")
            @Valid
            @NotEmpty
            @RequestHeader final String tkn,
            @Valid
            @NotEmpty
            @RequestBody DataSourceConnectionTestReq req) {
		
		boolean result = dataSourceService.connectionTest(userService.getAipUser(tkn, true), req);
		if(!result) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	@ApiOperation(value = "데이터 소스/데이터 추출", notes = "데이터 소스/데이터 추출", tags = "데이터 소스")
	@RequestMapping(method = RequestMethod.GET, value = "extract-datas", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<AipDataObjectInfo> extractData(
			@ApiParam(required = true, value = "토큰", defaultValue = "")
            @Valid
            @NotEmpty
            @RequestHeader final String tkn,
			@Valid
			@ModelAttribute
			DataSourceExtractReq req) throws Exception {
	
		AipDataObjectInfo extractDatas = dataSourceService.extractDatas(userService.getAipUser(tkn, true), req);
		
		if(ObjectUtils.isEmpty(extractDatas)) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(extractDatas, HttpStatus.OK);
	}
}
