package com.nomad.data.agent.common.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.MediaType;

import javax.validation.constraints.NotEmpty;
import java.util.Collections;
import java.util.List;

@Getter
public class AipHttpHeaders {
    @NotEmpty
    @Setter
    private MediaType contentType;

    private List<MediaType> acceptType;

    public void setAcceptType(MediaType acceptType) {
        this.acceptType = Collections.singletonList(acceptType);
    }
}
