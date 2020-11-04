package com.nomad.data.agent.dataset.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@ToString
public class CommandReq {
    @NotEmpty
    @ApiModelProperty(position = 1, required = true, value = "명령어리스트")
    private List<String> command;

}
