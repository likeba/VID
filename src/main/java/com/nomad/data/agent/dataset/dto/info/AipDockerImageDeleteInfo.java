package com.nomad.data.agent.dataset.dto.info;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AipDockerImageDeleteInfo {
    @ApiModelProperty(position = 1, required = true, value = "도커이미지이름")
    private String dockerImageName;
}
