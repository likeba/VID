package com.nomad.data.agent.common.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
public class AipDockerImageDeleteReq {
    @NotEmpty
    @ApiModelProperty(position = 1, required = true, value = "도커이미지이름")
    public String dockerImageName;
    @NotEmpty
    @ApiModelProperty(position = 2, required = true, value = "도커이미지태그")
    public String dockerImageTag;
}
