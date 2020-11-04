package com.nomad.data.agent.common.dto.req;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AipAlgorithmUploadReq {
	@NotEmpty
	@ApiModelProperty(position = 1, required = true, value = "알고리즘 이름")
	private String athmNm;
	@ApiModelProperty(position = 2, required = false, value = "알고리즘 설명")
	private String athmDesc;
	@ApiModelProperty(position = 3, required = false, value = "라이브러리 타입")
	private String libTp;
}
