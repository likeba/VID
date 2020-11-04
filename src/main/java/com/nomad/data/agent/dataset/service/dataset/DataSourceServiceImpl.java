package com.nomad.data.agent.dataset.service.dataset;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.nomad.data.agent.config.exception.CustomException;
import com.nomad.data.agent.dataset.dto.info.AipDataObjectInfo;
import com.nomad.data.agent.dataset.dto.info.AipDatabaseInfo;
import com.nomad.data.agent.dataset.dto.req.DataSourceConnectionTestReq;
import com.nomad.data.agent.dataset.dto.req.DataSourceExtractReq;
import com.nomad.data.agent.domain.dao.common.AipDataSource;
import com.nomad.data.agent.domain.dao.common.AipDataSourceKey;
import com.nomad.data.agent.domain.dao.common.AipUser;
import com.nomad.data.agent.domain.mappers.common.AipDataSourceMapper;
import com.nomad.data.agent.factory.database.DatabaseExtractor;
import com.nomad.data.agent.factory.database.DatabaseExtractorFactory;
import com.nomad.data.agent.log.service.LogService;
import com.nomad.data.agent.utils.enums.DataSourceType;
import com.nomad.data.agent.utils.enums.ErrorCodeType;
import com.nomad.data.agent.utils.enums.LogMessageType;
import com.nomad.data.agent.utils.enums.LogType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DataSourceServiceImpl implements DataSourceService {

	@Autowired
	private LogService logService;
	
	@Autowired
	private AipDataSourceMapper aipDataSourceMapper;
	
	@Autowired
	private DatabaseExtractorFactory databaseExtractorFactory;
	
	@Override
	public boolean connectionTest(AipUser user, DataSourceConnectionTestReq req) {
		
		boolean isConnected = false;
    	
    	// 1. 데이터 소스 종류 확인
    	if(DataSourceType.DATABASE.getValue().equals(req.getDsTp())) {
    		// 2.1.1. 데이터 베이스면..
    		isConnected = this.connectionTestForDatabase(req);
    	}else {
    		// 2.2.1. SFTP 면..
    		isConnected = this.connectionTestForSFTP(req);
    	}
    	
    	if(!isConnected) {
			logService.insertLog(user, LogType.DATASOURCE, LogMessageType.ERROR_CONNECTION_TEST);
			throw new CustomException(ErrorCodeType.CONNECTION_TEST_FAIL);
		}
		logService.insertLog(user, LogType.DATASOURCE, LogMessageType.OK_CONNECTION_TEST);
		return isConnected;
	}
	
   /**
     * Data Source 연결 테스트 (Database)
     * 
     * @author fruitson
     * @date 2020-04-07
     * 
     * @param req
     * @return
     */
	private boolean connectionTestForDatabase(DataSourceConnectionTestReq req) {
		
		AipDatabaseInfo databaseInfo = AipDatabaseInfo.fromDao(req);
		try {
			DatabaseExtractor extractor = databaseExtractorFactory.getDatabaseExtractor(databaseInfo);
			if(extractor.connectionTest(databaseInfo)) {
				log.info(">>>>> Database Connection Test is success...");
				return true;
			}else {
				log.error(">>>>> Database Connection Test error");
				return false;
			}
		}catch(CustomException e) {
			log.error("", e);
			throw new CustomException(ErrorCodeType.CONNECTION_TEST_FAIL);
		}
		
	}
	
   /**
     * Data Source 연결 테스트 (SFTP)
     * 
     * @author fruitson
     * @date 2020-04-07
     * 
     * @param req
     * @return
     */	
	private boolean connectionTestForSFTP(DataSourceConnectionTestReq req) {
		Session session = null;
    	boolean isConencted = false;
    	
    	try {
    		JSch jsch = new JSch();
        	session = jsch.getSession(req.getDsConnId(), req.getDsConnIp(), Integer.parseInt(req.getDsConnPort()));
        	session.setPassword(req.getDsConnPswd());
        	Properties config = new Properties();
        	config.put("StrictHostKeyChecking", "no");
        	session.setConfig(config);
        	session.connect();
        	
        	log.info(">>>>> SFTP Connection Test is success...");
        	isConencted = true;
        	
    	}catch(JSchException jsche) {
    		log.error(">>>>> SFTP Connection Test fail...", jsche);
    	}finally {
    		session.disconnect();
    	}
    	
		return isConencted;
	}
	
	
	@Override
	public AipDataObjectInfo extractDatas(AipUser user, DataSourceExtractReq req) throws Exception {
		
		AipDataObjectInfo info = new AipDataObjectInfo();
		
		// 1. 데이터 소스 정보 조회
		AipDataSourceKey dataSourceKey = new AipDataSourceKey();
		dataSourceKey.setDsId(req.getDsId());
		AipDataSource dataSourceInfo = aipDataSourceMapper.selectByPrimaryKey(dataSourceKey);
		dataSourceInfo.setDsConnId(req.getDsConnId());
		dataSourceInfo.setDsConnPswd(req.getDsConnPswd());
		
		if(ObjectUtils.isEmpty(dataSourceInfo)) {
			logService.insertLog(user, LogType.DATASOURCE, LogMessageType.ERROR_COMMON_NOT_FOUND_ID);
            throw new CustomException(ErrorCodeType.COMMON_NOT_FOUND_ID);
		}
		
		// 2. 데이터 소스 종류 확인
		if(DataSourceType.DATABASE.getValue().equals(dataSourceInfo.getDsTp())) {
    		// 2.1.1. 데이터 베이스면..
			List<Map<String, Object>> extractDatas = this.extractDatas(AipDatabaseInfo.fromDao(dataSourceInfo), req.getQuery(), "Y".equals(req.getPreview()) ? true : false);
    		info.setData(extractDatas);
    		info.setSize(extractDatas.size());
    	}
		
		// TODO: SFTP일 경우에는 어떻게??
		
		return info;
	}

	/**
	 * 데이터베이스에서의 데이터 추출
	 * 
	 * @author fruitson
     * @date 2020-04-09
	 * 
	 * @param databaseInfo
	 * @param query
	 * @param isPreview
	 * @return
	 * @throws Exception 
	 */
	@Override
	public List<Map<String, Object>> extractDatas(AipDatabaseInfo databaseInfo, String query, boolean isPreview) throws Exception {
		try {
			DatabaseExtractor extractor = databaseExtractorFactory.getDatabaseExtractor(databaseInfo);
			return extractor.extractDatas(databaseInfo, query, isPreview);			
		}catch(Exception e) {
			log.error("MgmtDatabaseServiceImpl.getDatasInDatabase Error", e);
			throw new Exception(e.getMessage());
		}
	}
	
	@Override
	public void extractDatas(AipDatabaseInfo databaseInfo, String query, String fileName) {
		try {
			DatabaseExtractor extractor = databaseExtractorFactory.getDatabaseExtractor(databaseInfo);
			extractor.extractDatas(databaseInfo, query, fileName);			
		}catch(Exception e) {
			log.error("MgmtDatabaseServiceImpl.getDatasInDatabase Error", e);
			throw new CustomException(ErrorCodeType.DB_PROCESS_HANDLE);
		}
	}
}