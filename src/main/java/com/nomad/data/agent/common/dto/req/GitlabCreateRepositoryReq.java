package com.nomad.data.agent.common.dto.req;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GitlabCreateRepositoryReq {
	@NotEmpty
	@ApiModelProperty(position = 1, required = true, value = "저장소 이름")
	private String repositoryName;
	@NotEmpty
	@ApiModelProperty(position = 2, required = true, value = "Access Token")
	private String accessToken;
}
