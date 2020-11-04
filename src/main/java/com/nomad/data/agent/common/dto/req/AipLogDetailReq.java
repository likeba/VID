package com.nomad.data.agent.common.dto.req;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AipLogDetailReq {
	@NotEmpty
	private String logFileName;
	private Long pointer;
}
