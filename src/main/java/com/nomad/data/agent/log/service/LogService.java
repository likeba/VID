package com.nomad.data.agent.log.service;

import com.nomad.data.agent.domain.dao.common.AipLogs;
import com.nomad.data.agent.domain.dao.common.AipUser;
import com.nomad.data.agent.utils.enums.LogMessageType;
import com.nomad.data.agent.utils.enums.LogType;

public interface LogService {

	public void createLog(AipLogs data);
	
	public void insertLog(AipUser user, LogType logType, LogMessageType logMessageType);

	public void insertLog(AipUser user, LogType logType, String logMessage);
}
