package com.nomad.data.agent.common.dto.info;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AipLogFilesInfo {
	private String defaultLog;
	private List<String> logFiles;
}
