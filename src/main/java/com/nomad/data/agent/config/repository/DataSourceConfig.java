package com.nomad.data.agent.config.repository;

import java.io.IOException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableConfigurationProperties
public class DataSourceConfig {
	
	@Value("${spring.datasource.driver-class-name}")
	String dbDriverClassName;
	
	@Value("${spring.datasource.url}")
	String url;

	@Value("${database.ip}")
	String databaseIp;
	
	@Value("${database.port}")
	String databasePort;
	
	@Value("${database.username}")
	String databaseUserName;
	
	@Value("${database.password}")
	String databasePassword;
	
	@Value("${database.databasename}")
	String databaseName;
	
	@Primary
	@Bean(name = "commonDataSource")
	public DataSource dataSource() {
		
		String databaseUrl = url.replace("@ip", databaseIp).replace("@port", databasePort).replace("@databaseName", databaseName);
		
		log.info("*************************");
		log.info("Connection to database...");
		log.info("URL: {}", databaseUrl);
		log.info("*************************");
		
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(databaseUrl);
		config.setUsername(databaseUserName);
		config.setPassword(databasePassword);
		config.setDriverClassName(dbDriverClassName);
		
		HikariDataSource dataSource = null;
		try {
			dataSource = new HikariDataSource(config);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return dataSource;
	}
	
}
