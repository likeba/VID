package com.nomad.data.agent.dataset.dto.info;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class AipResponseInfo {
    @ApiModelProperty(position = 1, required = false, value = "결과값")
    private Object log;
}
