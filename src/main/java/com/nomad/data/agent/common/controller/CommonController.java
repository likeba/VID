package com.nomad.data.agent.common.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nomad.data.agent.common.dto.info.AipTestInfo;
import com.nomad.data.agent.common.dto.req.AipTestReq;
import com.nomad.data.agent.common.service.common.CommonService;
import com.nomad.data.agent.domain.dao.common.AipTestKey;
import com.nomad.data.agent.user.service.UserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/common")
public class CommonController {
	
	@Autowired
	CommonService commonService;
	
	@Autowired
	UserService userService;
	
	@RequestMapping(method = RequestMethod.GET, value = "test", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "테스트/GET", notes = "테스트/GET", tags = "00-테스트")
    public ResponseEntity<List<AipTestInfo>> test(
    		@ApiParam(required = true, value = "토큰", defaultValue = "")
            @Valid
            @NotEmpty
            @RequestHeader final String tkn,
            @Valid
            @ModelAttribute AipTestReq req) {

		log.info(">>>>> TEST.GET Req: {}", req.toString());
		
		List<AipTestInfo> result = commonService.list(userService.getAipUser(tkn, true), req);
		
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
	
	@RequestMapping(method = RequestMethod.POST, value = "test", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "테스트/POST", notes = "테스트/POST", tags = "00-테스트")
    public ResponseEntity<AipTestKey> read(
            @ApiParam(required = true, value = "토큰", defaultValue = "")
            @Valid
            @NotEmpty
            @RequestHeader final String tkn,
            @Valid
            @NotEmpty
            @RequestBody AipTestReq req) {

		log.info(">>>>> TEST.POST Req: {}", req.toString());
		
		AipTestKey result = commonService.save(userService.getAipUser(tkn, true), req);
		
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
