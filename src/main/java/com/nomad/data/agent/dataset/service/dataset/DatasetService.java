package com.nomad.data.agent.dataset.service.dataset;

import java.util.List;
import java.util.Map;

public interface DataSetService {

		
	/**
	 * 
	 * @param database
	 * @return
	 * @throws Exception
	 */
	public List<String> getDatabaselist();
	
	/**
	 * 
	 * @param database
	 * @return
	 * @throws Exception
	 */
	public List<String> getTablelist(String database);
	
	/**
	 * 
	 * @param database
	 * @return
	 */
	public List<Map<String,String>> getTableColumnlist(String database, String table);
	
	/**
	 * 
	 * @param database
	 * @param table
	 * @param pk
	 * @return
	 */
	public Map<String,String> getDataset(String database, String table, String pk);
	
	/**
	 * 
	 * @param database
	 * @param table
	 * @param pk
	 * @return
	 */
	public Map<String,String> storeDataset(String database, String table, String pk);
	
	/**
	 * 
	 * @return
	 */
	public List<String> getDatasetIds();
	
	/**
	 * 
	 * @param dataset_id
	 * @return
	 */
	public Map<String,String> getStoredDataset(String dataset_id);
	
	/**
	 * 
	 * @param dataset_id
	 * @return
	 */
	public Map<String,String> verifyDatasetIntegrity(String dataset_id);
	
}
