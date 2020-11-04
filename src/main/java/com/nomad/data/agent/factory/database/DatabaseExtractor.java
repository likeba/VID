package com.nomad.data.agent.factory.database;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.nomad.data.agent.config.exception.CustomException;
import com.nomad.data.agent.dataset.dto.info.AipDatabaseInfo;

public abstract class DatabaseExtractor {
	
	/**
	 * 데이터베이스 종류에 따라 DataSource 셋팅
	 * 
	 * @author fruitson
	 * @date 2020-04-08
	 * 
	 * @param database
	 * @throws SQLException
	 */
	public abstract void setDatabase(AipDatabaseInfo database) throws SQLException;

	
	/**
	 * 데이터베이스 접속 테스트
	 * 
	 * @author fruitson
	 * @date 2020-04-08
	 * 
	 * @param databaseInfo
	 * @return
	 */
	public abstract boolean connectionTest(AipDatabaseInfo databaseInfo);
	

	/**
	 * 데이터베이스 내의 데이터 추출(by Query)
	 * 
	 * @author fruitson
	 * @date 2020-04-09
	 * 
	 * @param databaseInfo
	 * @param query
	 * @param isPreview
	 * @return
	 * @throws Exception 
	 * @throws CustomException
	 */
	public abstract List<Map<String, Object>> extractDatas(AipDatabaseInfo databaseInfo, String query, boolean isPreview) throws Exception;
	
	/**
	 * 데이터베이스 내 데이터 추출 후 저장
	 * 
	 * @author fruitson
	 * @date 2020-04-20
	 * 
	 * @param databaseInfo
	 * @param query
	 * @param fileName
	 */
	public abstract void extractDatas(AipDatabaseInfo databaseInfo, String query, String fileName);
	
	/**
	 * 데이터베이스 URL 매핑
	 * 
	 * @author fruitson
	 * @date 2020-04-08
	 * 
	 * @param databaseUrl
	 * @param ip
	 * @param port
	 * @param schema
	 * @return
	 */
	public String replaceDatabaseUrl(String databaseUrl, String ip, int port, String schema) {
		return databaseUrl.replace("@ip", ip).replace("@port", String.valueOf(port)).replace("@databaseName", schema);
	}
}
