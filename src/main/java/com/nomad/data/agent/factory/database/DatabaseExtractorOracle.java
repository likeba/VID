package com.nomad.data.agent.factory.database;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.nomad.data.agent.config.exception.CustomException;
import com.nomad.data.agent.dataset.dto.info.AipDatabaseInfo;
import com.nomad.data.agent.utils.AESUtils;
import com.nomad.data.agent.utils.enums.ErrorCodeType;
import com.opencsv.CSVWriter;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository("databaseExtractorOracle")
public class DatabaseExtractorOracle extends DatabaseExtractor {

	@Autowired
	DataSource dataSource;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	ConfigurableApplicationContext context;

	@Autowired
	@Qualifier("oracleDataSource")
	private DataSource oracleDataSource;
	
	@Autowired
	private AESUtils aesUtils;

	@Value("${spring.datasource.oracle.driver-class-name}")
	String oracleDriverClassName;

	@Value("${spring.datasource.oracle.url}")
	String oracleUrl;
	
	@Override
	public void setDatabase(AipDatabaseInfo databaseInfo) throws SQLException {
		String databaseUrl =  databaseInfo.getJdbcUrl();
		try {

			log.info(">>>>> databaseInfo. Connection ID  : {}", databaseInfo.getDbConnId());
			log.info(">>>>> databaseInfo. Connection PSWD: {}", databaseInfo.getDbConnPswd());
			
			((HikariDataSource) oracleDataSource).close();
			
			HikariConfig config = new HikariConfig();
			config.setJdbcUrl(databaseUrl);
			config.setUsername(databaseInfo.getDbConnId());
			config.setPassword(databaseInfo.getDbConnPswd());
			config.setConnectionTimeout(30000);
			config.setIdleTimeout(1000000);
			config.setPoolName("Oracle Connection Pool");
			
			HikariDataSource dataSource = new HikariDataSource(config);
			oracleDataSource = dataSource;
			
			DefaultSingletonBeanRegistry registry = (DefaultSingletonBeanRegistry) context.getBeanFactory();
			registry.destroySingleton("oracleDataSource"); //destroys the bean object
			registry.registerSingleton("oracleDataSource", dataSource); //add to singleton beans cache

		}catch(Exception e) {
			log.error("Database Error", e);
			throw new SQLException(e);
		}
			
	}
	
	@Override
	public boolean connectionTest(AipDatabaseInfo databaseInfo) {
		boolean isConnection = true;

		String databaseUrl = this.replaceDatabaseUrl(oracleUrl, databaseInfo.getDbConnIp(), databaseInfo.getDbConnPort(), databaseInfo.getDbNm());
		if(!isEqualsDataSourceInfo(databaseInfo, databaseUrl)) {
			
			try {
				this.setDatabase(databaseInfo);
			}catch(SQLException sqle) {
				log.error(">>>>> Database Connection Test fail", sqle);
				isConnection = false;
			}                                   
		}
		return isConnection;
	}
	
	
	@Override
	public List<Map<String, Object>> extractDatas(AipDatabaseInfo databaseInfo, String query, boolean isPreview) throws Exception {
		
		if(isPreview) {
			jdbcTemplate.setMaxRows(10);
		}else {
			jdbcTemplate.setMaxRows(-1);
		}

		List<Map<String, Object>> data = new ArrayList<>();

		try {
			if(this.connectionTest(databaseInfo)) {
				jdbcTemplate.setDataSource(oracleDataSource);
				log.info(">>>>> EXTRACT QUERY: {}", query);
				data = jdbcTemplate.queryForList(query);
			}
		}catch(Exception e) {
			log.error("DatabaseExtractorOracle.getDatas Error", e);
			throw new Exception(e.getMessage());
		}

		return data;
	}

	@Override
	public void extractDatas(AipDatabaseInfo databaseInfo, String query, String fileName) {
		
		try(FileOutputStream fos = new FileOutputStream(fileName);
				OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
//				CSVWriter writer = new CSVWriter(osw, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER)) {
				CSVWriter writer = new CSVWriter(osw, '^', CSVWriter.NO_QUOTE_CHARACTER)) {
			log.info(">>>>> fileName: ", fileName);
			if(this.connectionTest(databaseInfo)) {
				jdbcTemplate.setDataSource(oracleDataSource);
				jdbcTemplate.setMaxRows(-1);
				log.info(">>>>> EXTRACT QUERY: {}", query);
				jdbcTemplate.query(query, resultSet -> {
					log.info("result set Ok!");
					try {
						writer.writeAll(resultSet, true);
						writer.flush();
						log.info("result write Ok!");
						return null;
					}catch(IOException ioe) {
						log.error(ioe.getMessage(), ioe);
						throw new CustomException(ErrorCodeType.DATA_EXTRACT_FAIL);
					}
				});
			}
			
		}catch(Exception e) {
			log.error("DatabaseExtractorOracle.getDatas Error", e);
			throw new CustomException(ErrorCodeType.DATA_EXTRACT_FAIL);
		}
	}
	
	/**
	 * 기존 DataSource와 새로 입력된 DataSource와의 정보 비교
	 * 
	 * @param databaseInfo
	 * @param databaseUrl
	 * @return
	 */
	private boolean isEqualsDataSourceInfo(AipDatabaseInfo databaseInfo, String databaseUrl) {
		
		HikariDataSource dataSource = (HikariDataSource) oracleDataSource;
		if(databaseUrl.equals(dataSource.getJdbcUrl())
				&& databaseInfo.getDbConnId().equals(dataSource.getUsername())
					&& databaseInfo.getDbConnPswd().equals(dataSource.getPassword())) {
			return true;
		}
		return false;
	}
	
	
}
