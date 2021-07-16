package com.nomad.data.agent.dataset.dto.req;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataSetQryReq {

	@NotEmpty
    @ApiModelProperty(position = 1, required = true, value = "데이터베이스")
	private String database;
	
	@NotEmpty
    @ApiModelProperty(position = 2, required = true, value = "테이블")
	private String table;
	
	@NotEmpty
    @ApiModelProperty(position = 3, required = true, value = "테이블 pk")
	private String pk;
	
}
