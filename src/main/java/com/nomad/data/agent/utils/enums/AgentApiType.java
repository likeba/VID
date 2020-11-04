package com.nomad.data.agent.utils.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AgentApiType implements EnumValue<Object> {

	// worker server api
	WORKER_FILE_UPLOAD("/v1/file/upload", "v1/파일/업로드"),
	WORKER_DOCKER_IMAGE_DELETE("/v1/docker/image/delete", "워커서버/도커이미지/삭제"),
    
	// scheduler api
    SCHEDULER_JOB_DELETE("/v1/job/delete-jobs", "스케줄 삭제"),
    ;

    private String value;
    private String name;

    public Object getValue() {
        return value;
    }

}
