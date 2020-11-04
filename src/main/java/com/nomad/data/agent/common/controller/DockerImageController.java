package com.nomad.data.agent.common.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nomad.data.agent.common.dto.req.AipDockerImageCreateReq;
import com.nomad.data.agent.common.dto.req.AipDockerRegistryImageDeleteReq;
import com.nomad.data.agent.common.service.docker.DockerImageService;
import com.nomad.data.agent.domain.dao.common.AipDockerImageKey;
import com.nomad.data.agent.user.service.UserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/docker")
public class DockerImageController {

    @Autowired
    DockerImageService dockerImageService;

    @Autowired
    UserService userService;

    @ApiOperation(value = "도커이미지/삭제", notes = "도커 이미지를 삭제한다.", tags = "03-도커이미지")
    @RequestMapping(method = RequestMethod.POST, value = "delete", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity deleteImage(
            @ApiParam(required = true, value = "토큰", defaultValue = "")
            @Valid
            @NotEmpty
            @RequestHeader final String tkn,
            @NotEmpty
            @RequestBody AipDockerRegistryImageDeleteReq req){

        try {
            dockerImageService.deleteRegistryImage(userService.getAipUser(tkn, true), req);

        } catch (Exception e) {
            throw e;
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "도커이미지/도커이미지/생성", notes = "도커이미지/도커이미지/생성", tags = "03-도커이미지")
    @RequestMapping(method = RequestMethod.POST, value = "create", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AipDockerImageKey> create(
            @ApiParam(required = true, value = "토큰", defaultValue = "")
            @Valid
            @NotEmpty
            @RequestHeader final String tkn,
            @RequestBody AipDockerImageCreateReq req) {

        log.info(">>>>> docker image create req :{}", req.toString());

        AipDockerImageKey result = dockerImageService.create(userService.getAipUser(tkn, true), req);

        if (result == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    public ResponseEntity<List<String>> list() {
    	return new ResponseEntity(dockerImageService.list(), HttpStatus.OK);
    }

}
