package com.nomad.data.agent.common.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
public class AipDockerRegistryImageDeleteReq {
    @NotEmpty
    @ApiModelProperty(position = 1, required = true, value = "도커이미지아이디")
    public String imgId;
}
