package com.nomad.data.agent.dataset.dto.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleDeleteReq {

	private String jobName;
	private String jobGroup;
}
