package com.nomad.data.agent.factory.database;

import java.sql.SQLException;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import com.nomad.data.agent.config.exception.CustomException;
import com.nomad.data.agent.dataset.dto.info.AipDatabaseInfo;
import com.nomad.data.agent.utils.StrUtils;
import com.nomad.data.agent.utils.enums.ErrorCodeType;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class DatabaseExtractorFactory implements ApplicationContextAware {

	ApplicationContext applicationContext;
	
	/**
	 * 데이터베이스 종류에 맞춰서 extractor를 셋팅 
	 * 
	 * @author fruitson
	 * @date 2020-04-08
	 * 
	 * @param databaseInfo
	 * @return
	 */
	public DatabaseExtractor getDatabaseExtractor(AipDatabaseInfo databaseInfo) {
		
		DatabaseExtractor extractor = null;
		
		try {
			String databaseExtractorBeanName = "databaseExtractor" + StrUtils.getUpperFirstCharacter(databaseInfo.getDbTp());
			extractor = (DatabaseExtractor) applicationContext.getBean(databaseExtractorBeanName);
			extractor.setDatabase(databaseInfo);
		}catch(SQLException sqle) {
			log.error("DatabaseExtractorFactory.getDatabaseExtractor", sqle);
			throw new CustomException(ErrorCodeType.DB_INVALID_CONNECTION_INFO);
		}
		return extractor;
	}
	
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
