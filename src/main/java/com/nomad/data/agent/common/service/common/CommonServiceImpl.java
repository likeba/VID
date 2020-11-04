package com.nomad.data.agent.common.service.common;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.nomad.data.agent.common.dto.info.AipTestInfo;
import com.nomad.data.agent.common.dto.req.AipTestReq;
import com.nomad.data.agent.config.exception.CustomException;
import com.nomad.data.agent.domain.dao.common.AipTestKey;
import com.nomad.data.agent.domain.dao.common.AipUser;
import com.nomad.data.agent.log.service.LogService;
import com.nomad.data.agent.utils.enums.ErrorCodeType;
import com.nomad.data.agent.utils.enums.LogMessageType;
import com.nomad.data.agent.utils.enums.LogType;

@Service
public class CommonServiceImpl implements CommonService {
	
	@Autowired
	LogService logService;

	@Override
	public List<AipTestInfo> list(AipUser user, AipTestReq req) {
		
		List<AipTestInfo> result = new ArrayList<>();
		
		AipTestInfo testInfo = new AipTestInfo();
		testInfo.setKey1("Value1_1");
		testInfo.setKey2("Value1_2");
		testInfo.setKey3("Value1_3");
		result.add(testInfo);
		
		testInfo = new AipTestInfo();
		testInfo.setKey1("Value2_1");
		testInfo.setKey2("Value2_2");
		testInfo.setKey3("Value2_3");
		result.add(testInfo);
		
		testInfo = new AipTestInfo();
		testInfo.setKey1("Value3_1");
		testInfo.setKey2("Value3_2");
		testInfo.setKey3("Value3_3");
		result.add(testInfo);
		
		if(result.size() == 0) {
			logService.insertLog(user, LogType.TEST, LogMessageType.ERROR_DB_PROCESS_HANDLE);
			throw new CustomException(ErrorCodeType.DB_PROCESS_HANDLE);
		}
		logService.insertLog(user, LogType.TEST, LogMessageType.OK_LIST);
		return result;
	}
	
	@Override
	public AipTestKey save(AipUser user, AipTestReq req) {
		
		AipTestKey key = new AipTestKey();
		key.setKey("key");
		
		if(ObjectUtils.isEmpty(key)) {
			logService.insertLog(user, LogType.TEST, LogMessageType.ERROR_DB_INSERT_HANDLE);
			throw new CustomException(ErrorCodeType.DB_INSERT_HANDLE);
		}
		logService.insertLog(user, LogType.TEST, LogMessageType.OK_DB_INSERT);
		
		return key;
	}
	
}
