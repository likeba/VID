package com.nomad.data.agent.common.controller;

import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nomad.data.agent.common.dto.info.AipLogDetailInfo;
import com.nomad.data.agent.common.dto.info.AipLogFilesInfo;
import com.nomad.data.agent.common.dto.req.AipLogDetailReq;
import com.nomad.data.agent.common.service.system.SystemLogService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/admin/v1/system")
public class SystemContoller {

	@Autowired
	SystemLogService systemLogService;
	
	@ApiOperation(value = "로그/파일목록조회", notes = "로그파일들 목록을 조회한다.", tags = "05-system")
	@RequestMapping(method = RequestMethod.GET, value = "logs/list-files", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<AipLogFilesInfo> listLog() {
		
		AipLogFilesInfo result = systemLogService.getLogFiles();
		
		if(result == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	
	@ApiOperation(value = "로그/상세조회", notes = "로그 상세 내용을 조회한다.", tags = "05-system")
	@RequestMapping(method = RequestMethod.GET, value = "logs", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity viewLog(
			@NotEmpty
			@ModelAttribute AipLogDetailReq req) {
		
		AipLogDetailInfo result = systemLogService.viewLogFile(req);
		
		if(result == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
