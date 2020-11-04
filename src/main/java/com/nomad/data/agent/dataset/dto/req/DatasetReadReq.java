package com.nomad.data.agent.dataset.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class DatasetReadReq {
    @NotEmpty
    @ApiModelProperty(position = 1, required = true, value = "파일경로")
    private String datasetFile;
    @ApiModelProperty(position = 2, required = false, value = "파일 줄 수")
    private Integer size;
}
