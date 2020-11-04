package com.nomad.data.agent.dataset.service.dataset;

import java.util.List;
import java.util.Map;

import com.nomad.data.agent.dataset.dto.info.AipDataObjectInfo;
import com.nomad.data.agent.dataset.dto.info.AipDatabaseInfo;
import com.nomad.data.agent.dataset.dto.req.DataSourceConnectionTestReq;
import com.nomad.data.agent.dataset.dto.req.DataSourceExtractReq;
import com.nomad.data.agent.domain.dao.common.AipUser;

public interface DataSourceService {

	/**
	 * 데이터 소스 연결 테스트
	 * 
     * @author fruitson
     * @date 2020-04-08
	 * 
	 * @param user
	 * @param req
	 * 
	 * @return
	 */
	public boolean connectionTest(AipUser user, DataSourceConnectionTestReq req);

	
	/**
	 * 데이터 소스에서 데이터 추출
	 *  
	 * @author fruitson
     * @date 2020-04-09
	 *  
	 * @param user
	 * @param req
	 * @return
	 * @throws Exception 
	 */
	public AipDataObjectInfo extractDatas(AipUser user, DataSourceExtractReq req) throws Exception;


	/**
	 * 데이터 소스에서 데이터 추출
	 * 
	 * @author fruitson
     * @date 2020-04-20
     * 
	 * @param databaseInfo
	 * @param query
	 * @param isPreview
	 * @return
	 * @throws Exception 
	 */
	public List<Map<String, Object>> extractDatas(AipDatabaseInfo databaseInfo, String query, boolean isPreview) throws Exception;
	
	
	/**
	 * 
	 * @param databaseInfo
	 * @param query
	 * @param fileName
	 */
	public void extractDatas(AipDatabaseInfo databaseInfo, String query, String fileName);
	
}
