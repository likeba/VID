package com.nomad.data.agent.log.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nomad.data.agent.config.exception.CustomException;
import com.nomad.data.agent.domain.dao.common.AipLogs;
import com.nomad.data.agent.domain.dao.common.AipUser;
import com.nomad.data.agent.domain.mappers.common.AipLogsMapper;
import com.nomad.data.agent.utils.DateUtils;
import com.nomad.data.agent.utils.StrUtils;
import com.nomad.data.agent.utils.enums.ErrorCodeType;
import com.nomad.data.agent.utils.enums.LogMessageType;
import com.nomad.data.agent.utils.enums.LogType;

@Service
public class LogServiceImpl implements LogService {

	@Autowired
    private AipLogsMapper aipLogsMapper;
	
	@Override
	public void createLog(AipLogs data) {
		Date now = DateUtils.getNow();
		data.setLogId(StrUtils.getUniqueId());
		data.setRegDt(now);
		data.setModDt(now);
		
		int result = aipLogsMapper.insert(data);
		if (result <= 0) {
			throw new CustomException(ErrorCodeType.DB_INSERT_HANDLE);
		}
	}
	
	@Override
	public void insertLog(AipUser user, LogType logType, LogMessageType logMessageType) {
		this.insertLog(user, logType, logMessageType.getValue());
	}
	
	@Override
	public void insertLog(AipUser user, LogType logType, String logMessage) {
		String logMsg = String.format("%s > %s", logType.getName(), logMessage);
		insertLog(user, logType.getValue(), logMsg, null);
	}
	
	private void insertLog(AipUser user, String logType, String logMsg, String logDetail) {
        Date now = DateUtils.getNow();
        AipLogs data = new AipLogs();

        data.setUserId(user.getUserId());
        data.setLogTp(logType);
        data.setLogStr(logMsg);

        data.setLogId(StrUtils.getUniqueId());
        data.setLogDetail(logDetail);
        data.setRegDt(now);
        data.setModDt(now);

        int result = aipLogsMapper.insert(data);
        if (result <= 0) {
            throw new CustomException(ErrorCodeType.DB_INSERT_HANDLE);
        }
    }
}
