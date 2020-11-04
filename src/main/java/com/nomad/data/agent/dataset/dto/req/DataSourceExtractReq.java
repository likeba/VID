package com.nomad.data.agent.dataset.dto.req;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DataSourceExtractReq {

	@NotEmpty
    @ApiModelProperty(position = 1, required = true, value = "데이터 소스 아이디")
	private String dsId;
	
	@NotEmpty
	@ApiModelProperty(position = 2, required = true, value = "추출 쿼리")
	private String query;
	
	@NotEmpty
    @ApiModelProperty(position = 3, required = true, value = "데이터 소스 접속 아이디")
	private String dsConnId;
	
	@NotEmpty
    @ApiModelProperty(position = 4, required = true, value = "데이터 소스 접속 패스워드")
	private String dsConnPswd;
	
	@NotEmpty
	@ApiModelProperty(position = 5, required = true, value = "미리보기 여부")
	private String preview;
	
}
