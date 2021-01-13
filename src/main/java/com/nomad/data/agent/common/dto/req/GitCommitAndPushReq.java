package com.nomad.data.agent.common.dto.req;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GitCommitAndPushReq {

	@ApiModelProperty(position = 1, required = true, value = "로컬경로")
	private String localPath;

	@ApiModelProperty(position = 2, required = true, value = "깃사용자이름")
	private String gitUsername;

	@ApiModelProperty(position = 3, required = true, value = "깃사용자이메일")
	private String gitEmail;

	@ApiModelProperty(position = 4, required = true, value = "깃비밀번호")
	private String gitPassword;

	@ApiModelProperty(position = 5, required = true, value = "커밋메세지")
	private String message;

	@ApiModelProperty(position = 6, required = false, value = "워크스페이스백업유무")
	private Boolean workspaceBackupFl;

	@ApiModelProperty(position = 7, required = false, value = "워크스페이스 remote url")
	private String remoteUri;
}
