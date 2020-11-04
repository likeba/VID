package com.nomad.data.agent.common.dto.req;

import com.nomad.data.agent.domain.dao.common.AipDockerImage;
import com.nomad.data.agent.utils.JsonUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
public class AipDockerImageCreateReq {
    @NotEmpty
    @ApiModelProperty(position = 1, required = true, value = "이미지이름")
    private String imgNm;
    @NotEmpty
    @ApiModelProperty(position = 2, required = true, value = "도커이미지이름")
    private String dckrImgNm;
    @NotEmpty
    @ApiModelProperty(position = 3, required = true, value = "도커이미지태그")
    private String dckrImgTag;
    @NotEmpty
    @ApiModelProperty(position = 4, required = true, value = "이미지설명")
    private String imgDesc;
    @ApiModelProperty(position = 5, required = false, value = "고급옵션")
    private String advOpt;
    @NotEmpty
    @ApiModelProperty(position = 6, required = true, value = "프로세스종류")
    private String procTp;
    @NotEmpty
    @ApiModelProperty(position = 7, required = true, value = "이미지포트")
    private String imgPort;
    @NotEmpty
    @ApiModelProperty(position = 8, required = true, value = "이미지파일이름")
    private String dckrImgFileNm;

    public AipDockerImage toDao() {
        return JsonUtils.toObjFromSrc(this, AipDockerImage.class);
    }
}
