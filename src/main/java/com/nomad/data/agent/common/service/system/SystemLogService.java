package com.nomad.data.agent.common.service.system;

import com.nomad.data.agent.common.dto.info.AipLogDetailInfo;
import com.nomad.data.agent.common.dto.info.AipLogFilesInfo;
import com.nomad.data.agent.common.dto.req.AipLogDetailReq;

public interface SystemLogService {

	AipLogFilesInfo getLogFiles();
	
	AipLogDetailInfo viewLogFile(AipLogDetailReq req);
}
