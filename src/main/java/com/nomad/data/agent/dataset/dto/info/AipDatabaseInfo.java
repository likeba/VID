package com.nomad.data.agent.dataset.dto.info;

import org.springframework.util.ObjectUtils;

import com.nomad.data.agent.dataset.dto.req.DataSourceConnectionTestReq;
import com.nomad.data.agent.domain.dao.common.AipDataSource;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AipDatabaseInfo {

	private String dbConnIp;
	private Integer dbConnPort;
	private String dbConnId;
	private String dbConnPswd;
	private String dbTp;
	private String dbNm;
	private String jdbcUrl;
	
	public static AipDatabaseInfo fromDao(DataSourceConnectionTestReq req) {
        if (ObjectUtils.isEmpty(req)) {
            return null;
        }

        AipDatabaseInfo obj = new AipDatabaseInfo();
        obj.setDbConnIp(req.getDsConnIp());
        obj.setDbConnPort(Integer.parseInt(req.getDsConnPort()));
        obj.setDbConnId(req.getDsConnId());
        obj.setDbConnPswd(req.getDsConnPswd());
        obj.setDbTp(req.getDbTp());
        obj.setDbNm(req.getDbNm());

        return obj;
    }
	
	public static AipDatabaseInfo fromDao(AipDataSource dataSource) {
        if (ObjectUtils.isEmpty(dataSource)) {
            return null;
        }

        AipDatabaseInfo obj = new AipDatabaseInfo();
        obj.setDbConnIp(dataSource.getDsConnIp());
        obj.setDbConnPort(Integer.parseInt(dataSource.getDsConnPort()));
        obj.setDbTp(dataSource.getDbTp());
        obj.setDbNm(dataSource.getDbNm());
        obj.setDbConnId(dataSource.getDsConnId());
        obj.setDbConnPswd(dataSource.getDsConnPswd());
        obj.setDbTp(dataSource.getDbTp());
        obj.setJdbcUrl(dataSource.getJdbcUrl());

        return obj;
    }
	
}
