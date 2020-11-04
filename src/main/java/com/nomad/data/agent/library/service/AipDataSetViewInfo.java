package com.nomad.data.agent.library.service;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AipDataSetViewInfo {
	
	@NotNull
	@ApiModelProperty(position = 1, required = true, value = "데이터 리스트")
	private Object data;
	
}
