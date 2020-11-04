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
	@NotEmpty
	@ApiModelProperty(position = 1, required = true, value = "로컬경로")
	private String localPath;
	@NotEmpty
	@ApiModelProperty(position = 2, required = true, value = "깃사용자이름")
	private String gitUsername;
	@NotEmpty
	@ApiModelProperty(position = 3, required = true, value = "깃사용자이메일")
	private String gitEmail;
	@NotEmpty
	@ApiModelProperty(position = 4, required = true, value = "깃비밀번호")
	private String gitPassword;
	@NotEmpty
	@ApiModelProperty(position = 5, required = true, value = "커밋메세지")
	private String message;
	@ApiModelProperty(position = 6, required = false, value = "워크스페이스백업유무")
	private String workspaceBackupFl;
	@ApiModelProperty(position = 7, required = false, value = "워크스페이스 remote url")
	private String remoteUri;
}
