package com.nomad.data.agent.dataset.controller;

import com.nomad.data.agent.dataset.dto.info.AipResponseInfo;
import com.nomad.data.agent.dataset.dto.req.CommandReq;
import com.nomad.data.agent.dataset.service.command.CommandService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;

@Slf4j
@RestController
@RequestMapping("/v1/command")
public class CommandController {

    @Autowired
    CommandService commandService;
    /**
     * 명령어를 실행한다.
     * @param commandReq
     * @return result
     */
    @RequestMapping(method = RequestMethod.POST, value = "exec", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "명령어/실행", notes = "명령어를 실행한다.", tags = "01-명령어")
    public ResponseEntity<AipResponseInfo> exec(
            @ApiParam(required = true, value = "명령어")
            @NotEmpty
            @RequestBody CommandReq commandReq){

        AipResponseInfo result = commandService.execCommand(commandReq);

        return new ResponseEntity<>(result, HttpStatus.OK);

    }
}