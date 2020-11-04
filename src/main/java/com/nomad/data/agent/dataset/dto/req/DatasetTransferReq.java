package com.nomad.data.agent.dataset.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class DatasetTransferReq {
    @NotEmpty
    @ApiModelProperty(position = 1, required = true, value = "파일경로")
    private String filePath;
    @NotEmpty
    @ApiModelProperty(position = 2, required = true, value = "전송경로")
    private String targetPath;
    @NotEmpty
    @ApiModelProperty(position = 3, required = true, value = "워크스페이스 위치한 서버 ip")
    private String workspaceServerIp;
}
