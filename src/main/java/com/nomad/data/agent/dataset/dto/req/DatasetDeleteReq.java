package com.nomad.data.agent.dataset.dto.req;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatasetDeleteReq {
    
	@NotEmpty
    @ApiModelProperty(position = 1, required = true, value = "삭제 대상 데이터셋 파일 목록")
    private List<String> datasetFileList;
}
