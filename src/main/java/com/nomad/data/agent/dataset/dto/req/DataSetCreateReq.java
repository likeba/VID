package com.nomad.data.agent.dataset.dto.req;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataSetCreateReq {

	@NotEmpty
    @ApiModelProperty(position = 1, required = true, value = "데이터셋 이름")
	private String dataNm;
	
	@NotEmpty
    @ApiModelProperty(position = 2, required = true, value = "데이터셋 설명")
	private String dataDesc;
	
	@NotEmpty
    @ApiModelProperty(position = 3, required = true, value = "데이터셋 종류")
	private String dataTp;
	
	@NotEmpty
    @ApiModelProperty(position = 4, required = true, value = "데이터셋 시작일자")
	private String exptStDt;
	
	@NotEmpty
    @ApiModelProperty(position = 5, required = true, value = "데이터셋 종료일자")
	private String exptEdDt;
	
	@NotEmpty
    @ApiModelProperty(position = 6, required = true, value = "공개 여부")
	private String pubFl;
	
}
