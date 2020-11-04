package com.nomad.data.agent.common.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nomad.data.agent.common.dto.req.AipAlgorithmUploadReq;
import com.nomad.data.agent.common.service.algorithm.AlgorithmService;
import com.nomad.data.agent.user.service.UserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/algorithm")
public class AlgorithmController {
    
	@Autowired
    private AlgorithmService algorithmService;
    
    @Autowired
    private UserService userService;
    
    @ApiOperation(value = "알고리즘/업로드", notes = "알고리즘을 업로드한다.", tags = "04-알고리즘")
    @RequestMapping(method = RequestMethod.POST, value = "upload", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> uploadAlgorithm(
    		@ApiParam(required = true, value = "토큰", defaultValue = "")
    		@Valid
    		@NotEmpty
    		@RequestHeader final String tkn,
    		@NotEmpty
    		@ModelAttribute AipAlgorithmUploadReq req,
    		@ApiParam(required = true, value = "업로드 할 파일", defaultValue = "")
    		@RequestParam(name = "uploadFile") MultipartFile file){
    	
    	try {
    		String algorithmId = algorithmService.upload(userService.getAipUser(tkn, true), req, file);
    		
    		if(ObjectUtils.isEmpty(algorithmId)) {
    			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    		}
    	}catch(Exception e) {
    		log.error(e.getMessage(), e);
    		throw e;
    	}
    	
    	return new ResponseEntity(HttpStatus.OK);
    }
}
