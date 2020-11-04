package com.nomad.data.agent.library.controller;

import java.util.concurrent.CompletableFuture;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nomad.data.agent.config.exception.CustomException;
import com.nomad.data.agent.library.dto.req.LibraryDeleteReq;
import com.nomad.data.agent.library.service.LibraryService;
import com.nomad.data.agent.utils.enums.ErrorCodeType;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/library/v1")
public class LibraryController {

	@Autowired
	private LibraryService libraryService;
	
	
	@RequestMapping(method = RequestMethod.GET, value = "apply")
	@ApiOperation(value = "라이브러리/반영", notes = "라이브러리 업데이트 서버로부터 라이브러리 파일 다운로드 및 반영", tags = "01-라이브러리")
	public ResponseEntity<String> downloadPackageFile (
            @ApiParam(required = true, value = "라이브러리 경로", defaultValue = "")
			@Valid
			@NotEmpty
            @RequestParam(name = "libFilePath")
            final String libFilePath) {
		
		String result = "";
		
		try {
			
			CompletableFuture<String> future = libraryService.applyLibraryFile(libFilePath);
			
			while(true) {
				
				if(future.isDone()) {
					result = future.get();
					break;
				}else {
					Thread.sleep(2000);
				}
			}
			
		}catch(Exception e) {
			log.error(">>>>> download package file error", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}	
		
		if(StringUtils.isEmpty(result)) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}else {
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
	}
	
	
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "라이브러리/라이브러리 삭제", notes = "라이브러리 삭제", tags = "01-라이브러리")
	@RequestMapping(method = RequestMethod.POST, value = "delete", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity deleteLibrary(
			@ApiParam(required = true, value = "삭제할 라이브러리", defaultValue = "")
			@NotEmpty
			@RequestBody LibraryDeleteReq req) {
		
		try {
			libraryService.deleteLibrary(req);
		}catch(Exception e) {
			log.error(e.getMessage(), e);
			throw new CustomException(ErrorCodeType.FILE_DELETE_ERROR);
		}
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
