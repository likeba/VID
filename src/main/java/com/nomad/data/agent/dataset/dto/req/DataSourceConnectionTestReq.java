package com.nomad.data.agent.dataset.dto.req;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataSourceConnectionTestReq {

	@NotEmpty
    @ApiModelProperty(position = 1, required = true, value = "데이터 소스 종류")
	private String dsTp;
	
	@NotEmpty
	@ApiModelProperty(position = 2, required = true, value = "데이터 소스 접속 IP")
	private String dsConnIp;
	
	@NotEmpty
	@ApiModelProperty(position = 3, required = true, value = "데이터 소스 접속 Port")
	private String dsConnPort;
	
	@NotEmpty
	@ApiModelProperty(position = 4, required = true, value = "데이터 소스 접속 ID")
	private String dsConnId;
	
	@NotEmpty
	@ApiModelProperty(position = 5, required = true, value = "데이터 소스 접속 Password")
	private String dsConnPswd;
	
	@ApiModelProperty(position = 6, required = false, value = "데이터베이스 종류")
	private String dbTp;
	
	@ApiModelProperty(position = 7, required = false, value = "데이터베이스 이름")
	private String dbNm;

	
}
