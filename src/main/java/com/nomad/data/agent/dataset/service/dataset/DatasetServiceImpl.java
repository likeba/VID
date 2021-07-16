package com.nomad.data.agent.dataset.service.dataset;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nomad.data.agent.config.exception.CustomException;
import com.nomad.data.agent.utils.SHA256;
import com.nomad.data.agent.utils.enums.ErrorCodeType;
import com.nomadconnection.dapp.DataVerificationService;
import com.nomadconnection.dapp.exception.IconServiceException;
import com.nomadconnection.dapp.icon.IconServiceConfig;

import foundation.icon.icx.KeyWallet;
import foundation.icon.icx.data.Bytes;
import foundation.icon.icx.data.TransactionResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DataSetServiceImpl implements DataSetService {

	@Value("${database.ip}")
	String databaseIp;
	
	@Value("${database.port}")
	String databasePort;
	
	@Value("${database.username}")
	String databaseUserName;
	
	@Value("${database.password}")
	String databasePassword;
			
	@Qualifier("commonDataSource") @Autowired DataSource dataSource;
	
		
	@Override
	public List<String> getDatabaselist() {

			
		List<String> databaseList = new ArrayList<String>();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {

			con = dataSource.getConnection();
			log.info("Connection established......");
			//Creating a Statement object
			stmt = con.createStatement();
			//Retrieving the data
			rs = stmt.executeQuery("show databases");
//					log.info("Tables in the current database: ");
			log.info("databases in the current database: ");
			while(rs.next()) {
				databaseList.add(rs.getString(1));
				log.info(rs.getString(1));
			}
			
		}catch(Exception e) {
			log.error("DataSetServiceImpl.getTablelist Error", e);
			throw new CustomException(ErrorCodeType.DB_PROCESS_HANDLE);
		}finally {
			try{if (rs!=null ) rs.close();stmt.close();con.close();}catch(Exception e) {}
		}
		
		return databaseList;
	}
	
		
	@Override
	public List<String> getTablelist(String database){
		//Registering the Driver
		List<String> tableList = new ArrayList<String>();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		//StringBuffer sb = new StringBuffer();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			//DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			//Getting the connection
			String mysqlUrl = "jdbc:mysql://"+databaseIp+":"+databasePort+"/"+database;
			con = DriverManager.getConnection(mysqlUrl, databaseUserName, databasePassword);
//			log.info("Connection established......");
			log.info("Connection established......");
			//Creating a Statement object
			stmt = con.createStatement();
			//Retrieving the data
			rs = stmt.executeQuery("show full tables where Table_Type = 'BASE TABLE'");
//			log.info("Tables in the current database: ");
			log.info("Tables in the current database: ");
			while(rs.next()) {
				tableList.add(rs.getString(1));
				//sb.append(rs.getString(1)).append("◆◇□■");
//				log.info(rs.getString(1));
				log.info(rs.getString(1));
			}
						
			
		}catch(Exception e) {
			log.error("DataSetServiceImpl.getTablelist Error", e);
			throw new CustomException(ErrorCodeType.DB_PROCESS_HANDLE);
		}finally {
			try{if (rs!=null ) rs.close();stmt.close();con.close();}catch(Exception e) {}
		}
		
		return tableList;
	}
	
	@Override
	public List<Map<String,String>> getTableColumnlist(String database, String table) {
		//Registering the Driver
		List<Map<String,String>> tableColumnList = new ArrayList<Map<String,String>>();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		//StringBuffer sb = new StringBuffer();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			//Getting the connection
			String mysqlUrl = "jdbc:mysql://"+databaseIp+":"+databasePort+"/"+database;
			con = DriverManager.getConnection(mysqlUrl, databaseUserName, databasePassword);

			log.info("Connection established......");
			//Creating a Statement object
			stmt = con.createStatement();
			//Retrieving the data
			rs = stmt.executeQuery("describe "+table);

			log.info("Tables in the current database: ");
			Map<String,String> column;
			while(rs.next()) {
				column = new HashMap<String,String>();
				column.put("field", rs.getString("Field"));
				column.put("key", rs.getString("Key"));
				
				tableColumnList.add(column);
				
				log.info(rs.getString("Field")+"("+rs.getString("Key")+")");

			}				
			
		}catch(Exception e) {
			log.error("DataSetServiceImpl.getTableColumnlist Error", e);
			throw new CustomException(ErrorCodeType.DB_PROCESS_HANDLE);
		}finally {
			try{if (rs!=null ) rs.close();stmt.close();con.close();}catch(Exception e) {}
		}
		
		return tableColumnList;
	}
	
	@Override
	public Map<String,String> getDataset(String database, String table, String pk) {
		// TODO Auto-generated method stub
		//Registering the Driver
		Map<String,String> dataset = new HashMap<String,String>();
		List<Integer> tablePkValues = new ArrayList<Integer>();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		try {
			
			//dataset id
			dataset.put("dataset_id", database+"."+table+"."+pk);
			dataset.put("dataset_database", database);
			dataset.put("dataset_table", table);
			dataset.put("dataset_pk", pk);
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			//DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			//Getting the connection
			String mysqlUrl = "jdbc:mysql://"+databaseIp+":"+databasePort+"/"+database;
			con = DriverManager.getConnection(mysqlUrl, databaseUserName, databasePassword);
			log.info("Connection established......");

			stmt = con.createStatement();

			//dataset search query
			String qry = "select * from "+table+" order by "+pk;
//			dataset.put("dataset_sql", qry);
			rs = stmt.executeQuery(qry);

			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCnt = rsmd.getColumnCount();
			while(rs.next()) {

				tablePkValues.add(rs.getInt(pk));
//				System.out.print("PK : " + rs.getString("actor_id")+", ");
				for ( int i = 1 ; i <= columnCnt ; i++) {
					sb.append(rs.getString(i)+"").append("|");
				}
			}
			
			log.info("query result value : {}", sb.toString());
			//log.info("sha256 hash value : {}", SHA256.encode(sb.toString()));
			//start of pk
			dataset.put("start_pk", tablePkValues.get(0)+"");
			//end of pk
			dataset.put("end_pk", tablePkValues.get(tablePkValues.size()-1)+"");
			//조회 시점의 sql
			dataset.put("dataset_original_sql", qry);
			//검증 시 사용할 sql
			dataset.put("dataset_sql", "select * from "+table+" where "+pk+" between "+dataset.get("start_pk")+" and "+dataset.get("end_pk")+" order by "+pk+" ASC");
			
			//hash value put
			dataset.put("dataset_hash", SHA256.encode(sb.toString()));
			
			log.info("dataset info : " + dataset);
			
		}catch(Exception e) {
			log.error("DataSetServiceImpl.getTablelist Error", e);
			throw new CustomException(ErrorCodeType.DB_PROCESS_HANDLE);
		}finally {
			try{if (rs!=null ) rs.close();stmt.close();con.close();}catch(Exception e) {}
		}
		
		return dataset;
	}
	
	
	@Override
	public Map<String, String> storeDataset(String database, String table, String table_pk) {

		Map<String, String> datasetInfo = this.getDataset(database, table, table_pk);
		
		// TODO Auto-generated method stub
		List<Integer> tablePkValues = new ArrayList<Integer>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		try {
			
			String uuid = UUID.randomUUID().toString();
						
			Class.forName("com.mysql.cj.jdbc.Driver");
			//DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			//dataset ids 저장 DB
			String mysqlUrl = "jdbc:mysql://"+databaseIp+":"+databasePort+"/dataset-integrity";
			con = DriverManager.getConnection(mysqlUrl, databaseUserName, databasePassword);
			log.info("Connection established......");
			
			//delete first
			String qry = "delete from dataset_info where dataset_id = ? ";
			pstmt = con.prepareStatement(qry);
			pstmt.setString(1, datasetInfo.get("dataset_id"));
			pstmt.execute();
			pstmt.close();

			//dataset insert query
			qry = "insert into dataset_info "
					+ "("
					+ "dataset_id, "
					+ "dataset_database, "
					+ "dataset_table, "
					+ "dataset_pk, "
					+ "start_pk, "
					+ "end_pk, "
					+ "dataset_sql, "
					+ "blockchain_key"
					+ ") values( "
					+ "?, "
					+ "?, "
					+ "?, "
					+ "?, "
					+ "?, "
					+ "?, "
					+ "?, "
					+ "? "
					+ ")";
			
			pstmt = con.prepareStatement(qry);
			pstmt.setString(1, datasetInfo.get("dataset_id"));
			pstmt.setString(2, database);
			pstmt.setString(3, table);
			pstmt.setString(4, table_pk);
			pstmt.setString(5, datasetInfo.get("start_pk"));
			pstmt.setString(6, datasetInfo.get("end_pk"));
			pstmt.setString(7, datasetInfo.get("dataset_sql"));
			pstmt.setString(8, uuid);
			
			
			pstmt.execute();
			
			/**
			 * block chain에 기록
			 */
			String nodeUrl = "https://bicon.net.solidwallet.io/api/v3";
	        BigInteger networkId = BigInteger.valueOf(3);
	        String scoreAddress = "cx576b94f398a12b81be716a4a6568c928cecb1c35";
	        // Euljiro
//	        String nodeUrl = "https://test-ctz.solidwallet.io/api/v3";
//	        BigInteger networkId = BigInteger.valueOf(2);
//	        String scoreAddress = "cxefc236a2fe72a9f5997e28f6e44e1bf83b8d3770";

	        IconServiceConfig config = new IconServiceConfig.Builder()
	                .url(nodeUrl)
	                .networkId(networkId)
	                .scoreAddress(scoreAddress)
	                .build();

	        // 사용자 지갑 객체 생성 (score 를 호출하기 위한 pk 관리 필요)
	        // 테스트용 키
	        String pk = "b6ef08574f742c104bdefd39babacbf30c2861218b36557a9d0eaec49d7ccbf6";
	        KeyWallet wallet = KeyWallet.load(new Bytes(pk));
	        // 새로 생성
//	        KeyWallet wallet = KeyWallet.create();
//	        System.out.println("pk:" + wallet.getPrivateKey().toString());

	        // 서비스 호출을 위한 객체 생성
	        DataVerificationService service = new DataVerificationService(config);


	        // score version 조회
	        try {

	            String key = uuid;
	            String value = datasetInfo.get("dataset_hash");

	            log.info("==== 1. Data 등록하기 전, 등록여부 확인");
	            String registerdValue = service.getData(key);
	            // 등록되어 있지 않으면 빈문자열 return
	            String emptyString = "";
	            boolean registerd = !emptyString.equals(registerdValue);
	            log.info("key(" + key + ") 등록여부:" + registerd);

	            log.info("==== 3. Data 등록");
	            log.info("key(" + key + ") 등록 요청");
	            String txHash = service.putData(key, value, wallet);
	            log.info("트랜잭션 해시:" + txHash);
	            log.info("트랜잭션 결과 조회 중..");
	            // 결과 조회 될 때까지 loop, default timeout: 15s
	            TransactionResult transactionResult = service.getTransactionResult(txHash);
	            // 1 on success, 0 on failure
	            log.info("트랜잭션 결과 status:" + transactionResult.getStatus());

	            log.info("==== 4. 등록여부 확인");
	            registerdValue = service.getData(key);
	            registerd = !emptyString.equals(registerdValue);
	            log.info("key(" + key + "), 등록여부:" + registerd);
	            log.info("key(" + key + "), value:" + registerdValue);

	        } catch (IconServiceException e) {
	        	log.error("error!:" + e.getMessage());
	        }
			
			
		}catch(Exception e) {
			log.error("DataSetServiceImpl.getTablelist Error", e);
			throw new CustomException(ErrorCodeType.DB_PROCESS_HANDLE);
		}finally {
			try{if (rs!=null ) rs.close();pstmt.close();con.close();}catch(Exception e) {}
		}
		
		return datasetInfo;
	}
	
	@Override
	public List<String> getDatasetIds() {
		List<String> datasetIds = new ArrayList<String>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
						
			Class.forName("com.mysql.cj.jdbc.Driver");
			//DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			//Getting the connection
			String mysqlUrl = "jdbc:mysql://"+databaseIp+":"+databasePort+"/dataset-integrity";
			con = DriverManager.getConnection(mysqlUrl, databaseUserName, databasePassword);
			log.info("Connection established......");

			//dataset search query
			String qry = "select dataset_id from dataset_info order by last_update asc";
			pstmt = con.prepareStatement(qry);

			rs = pstmt.executeQuery();

			while(rs.next()) {			
				datasetIds.add(rs.getString(1));
			}
						
			log.info("dataset ids : " + datasetIds);
			
		}catch(Exception e) {
			log.error("DataSetServiceImpl.getTablelist Error", e);
			throw new CustomException(ErrorCodeType.DB_PROCESS_HANDLE);
		}finally {
			try{if (rs!=null ) rs.close();pstmt.close();con.close();}catch(Exception e) {}
		}
		
		return datasetIds;
	}
	
	@Override
	public Map<String, String> getStoredDataset(String dataset_id) {

		Map<String,String> dataset = new HashMap<String,String>();
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
						
			Class.forName("com.mysql.cj.jdbc.Driver");
			//DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			//Getting the connection
			String mysqlUrl = "jdbc:mysql://"+databaseIp+":"+databasePort+"/dataset-integrity";
			con = DriverManager.getConnection(mysqlUrl, databaseUserName, databasePassword);
			log.info("Connection established......");

			//dataset search query
			String qry = "select * from dataset_info where dataset_id = ?";
			pstmt = con.prepareStatement(qry);
			pstmt.setString(1, dataset_id);

			rs = pstmt.executeQuery();

			while(rs.next()) {			
				dataset.put("dataset_id",dataset_id);
				dataset.put("dataset_sql",rs.getString("dataset_sql"));
				dataset.put("databasse",rs.getString("dataset_database"));
				dataset.put("blockchain_key",rs.getString("blockchain_key"));
				
			}
						
			log.info("dataset info : " + dataset);
			
		}catch(Exception e) {
			log.error("DataSetServiceImpl.getTablelist Error", e);
			throw new CustomException(ErrorCodeType.DB_PROCESS_HANDLE);
		}finally {
			try{if (rs!=null ) rs.close();pstmt.close();con.close();}catch(Exception e) {}
		}
		
		return dataset;
	}
	
	@Override
	public Map<String, String> verifyDatasetIntegrity(String dataset_id) {
		
		Map<String,String> original_dataset = this.getStoredDataset(dataset_id);
		Map<String,String> verify_result = new HashMap<String,String>();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		try {
		
			Class.forName("com.mysql.cj.jdbc.Driver");
			//DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			//Getting the connection
			String mysqlUrl = "jdbc:mysql://"+databaseIp+":"+databasePort+"/"+original_dataset.get("databasse");
			con = DriverManager.getConnection(mysqlUrl, databaseUserName, databasePassword);
			log.info("Connection established......");
	
			stmt = con.createStatement();
	
			log.info("original dataset extract sql : "+original_dataset.get("dataset_sql"));
			rs = stmt.executeQuery(original_dataset.get("dataset_sql"));
	
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCnt = rsmd.getColumnCount();
			while(rs.next()) {
	
				for ( int i = 1 ; i <= columnCnt ; i++) {
					sb.append(rs.getString(i)+"").append("|");
				}
			}
			log.info("==== 데이터셋 hash 값 확인");
			String datasetHash = SHA256.encode(sb.toString());
			verify_result.put("dataset_hash", datasetHash);
			log.info("key(" + original_dataset.get("dataset_id") + "), value:" + datasetHash);
			
			/**
			 * block chain에 기록
			 */
			String nodeUrl = "https://bicon.net.solidwallet.io/api/v3";
	        BigInteger networkId = BigInteger.valueOf(3);
	        String scoreAddress = "cx576b94f398a12b81be716a4a6568c928cecb1c35";
	        // Euljiro
//	        String nodeUrl = "https://test-ctz.solidwallet.io/api/v3";
//	        BigInteger networkId = BigInteger.valueOf(2);
//	        String scoreAddress = "cxefc236a2fe72a9f5997e28f6e44e1bf83b8d3770";

	        IconServiceConfig config = new IconServiceConfig.Builder()
	                .url(nodeUrl)
	                .networkId(networkId)
	                .scoreAddress(scoreAddress)
	                .build();

	        // 사용자 지갑 객체 생성 (score 를 호출하기 위한 pk 관리 필요)
	        // 테스트용 키
	        String pk = "b6ef08574f742c104bdefd39babacbf30c2861218b36557a9d0eaec49d7ccbf6";
	        KeyWallet wallet = KeyWallet.load(new Bytes(pk));
	        // 새로 생성
//	        KeyWallet wallet = KeyWallet.create();
//	        System.out.println("pk:" + wallet.getPrivateKey().toString());

	        // 서비스 호출을 위한 객체 생성
	        DataVerificationService service = new DataVerificationService(config);


	        // score version 조회
	        try {

	            String key = original_dataset.get("blockchain_key");

	            log.info("==== 블록체인 Hash 값 확인");
	            String registerdValue = service.getData(key);
	            log.info("key(" + key + "), value:" + registerdValue);
	            
	            verify_result.put("blockchain_hash", registerdValue);
	            
	            verify_result.put("verified", registerdValue.equals(verify_result.get("dataset_hash"))?"1":"0");
	            

	        } catch (IconServiceException e) {
	        	log.error("error!:" + e.getMessage());
	        }
			
		}catch(Exception e) {
			log.error("DataSetServiceImpl.verifyDatasetIntegrity Error", e);
			throw new CustomException(ErrorCodeType.DB_PROCESS_HANDLE);
		}finally {
			try{if (rs!=null ) rs.close();stmt.close();con.close();}catch(Exception e) {}
		}
		
		return verify_result;
	}
	
	
	public static void main(String args[]) {
		
		
		//Registering the Driver
		List<String> tableList = new ArrayList<String>();
		List<Integer> tablePkValues = new ArrayList<Integer>();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			//DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			//Getting the connection
			String mysqlUrl = "jdbc:mysql://113.198.49.89:3306/dataset";
			con = DriverManager.getConnection(mysqlUrl, "mdpi", "test1222");
			System.out.println("Connection established......");
//			log.info("Connection established......");
			//Creating a Statement object
			stmt = con.createStatement();
			//Retrieving the data
			rs = stmt.executeQuery("describe actor");
//			rs = stmt.executeQuery("show tables");
//			rs = stmt.executeQuery("select * from address order by address_id");
			System.out.println("Tables in the current database: ");
//			log.info("Tables in the current database: ");
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCnt = rsmd.getColumnCount();
			while(rs.next()) {
//				tableList.add(rs.getString(1));
//				tablePkValues.add(rs.getInt("address_id"));
//				System.out.print("PK : " + rs.getString("address_id")+", ");
//				for ( int i = 1 ; i <= columnCnt ; i++) {
//					sb.append(rs.getObject(i)+"").append("|");
//				}
				System.out.println(rs.getString("Field")+"("+rs.getString("Key")+")");
//				System.out.println(rs.getString("Key"));
//				log.info(rs.getString(1));
			}
			
//			System.out.println("Start of PK : "+ tablePkValues.get(0));
//			System.out.println("End of PK : "+ tablePkValues.get(tablePkValues.size()-1));
//			System.out.println("111");
//			System.out.println(sb.toString());
//			System.out.println("table append str sha256 : "+ SHA256.encode(sb.toString()));

		}catch(Exception e) {
			log.error("DataSetServiceImpl.getTablelist Error", e);
			throw new CustomException(ErrorCodeType.DB_PROCESS_HANDLE);
		}finally {
			try{if (rs!=null ) rs.close();stmt.close();con.close();}catch(Exception e) {}
		}
	}

}