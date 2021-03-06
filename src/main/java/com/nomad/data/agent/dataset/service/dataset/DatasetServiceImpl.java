package com.nomad.data.agent.dataset.service.dataset;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.nomad.data.agent.common.dto.AipHttpHeaders;
import com.nomad.data.agent.config.exception.CustomException;
import com.nomad.data.agent.dataset.dto.info.AipDataObjectInfo;
import com.nomad.data.agent.dataset.dto.info.AipDataSetInfo;
import com.nomad.data.agent.dataset.dto.info.AipDatabaseInfo;
import com.nomad.data.agent.dataset.dto.req.DataSetCreateReq;
import com.nomad.data.agent.dataset.dto.req.DataSetOverviewReq;
import com.nomad.data.agent.dataset.dto.req.DatasetDeleteReq;
import com.nomad.data.agent.dataset.dto.req.DatasetReadReq;
import com.nomad.data.agent.dataset.dto.req.DatasetTransferReq;
import com.nomad.data.agent.dataset.dto.req.ScheduleDeleteReq;
import com.nomad.data.agent.domain.dao.common.AipDataSet;
import com.nomad.data.agent.domain.dao.common.AipDataSetFileMap;
import com.nomad.data.agent.domain.dao.common.AipDataSetFileMapKey;
import com.nomad.data.agent.domain.dao.common.AipDataSetKey;
import com.nomad.data.agent.domain.dao.common.AipDataSource;
import com.nomad.data.agent.domain.dao.common.AipDataSourceKey;
import com.nomad.data.agent.domain.dao.common.AipLogs;
import com.nomad.data.agent.domain.dao.common.AipUser;
import com.nomad.data.agent.domain.mappers.common.AipDataSetFileMapMapper;
import com.nomad.data.agent.domain.mappers.common.AipDataSetMapper;
import com.nomad.data.agent.domain.mappers.common.AipDataSourceMapper;
import com.nomad.data.agent.domain.mappers.common.AipUserMapper;
import com.nomad.data.agent.log.service.LogService;
import com.nomad.data.agent.utils.AESUtils;
import com.nomad.data.agent.utils.DateUtils;
import com.nomad.data.agent.utils.DbUtils;
import com.nomad.data.agent.utils.FileUtils;
import com.nomad.data.agent.utils.RestApiUtils;
import com.nomad.data.agent.utils.StrUtils;
import com.nomad.data.agent.utils.enums.AgentApiType;
import com.nomad.data.agent.utils.enums.DataSetType;
import com.nomad.data.agent.utils.enums.ErrorCodeType;
import com.nomad.data.agent.utils.enums.FlagType;
import com.nomad.data.agent.utils.enums.LogMessageType;
import com.nomad.data.agent.utils.enums.LogType;
import com.nomad.data.agent.utils.enums.ScheduleExecuteType;
import com.nomad.data.agent.utils.enums.ScheduleType;
import com.opencsv.CSVReader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DatasetServiceImpl implements DatasetService {

	@Value("${dataset.dir}")
	private String datasetPath;
	
	@Value("${agent.daplwk1p.port}")
	private String workerAgentPort;
	
	@Value("${daplmg.ip}")
	private String daplmgIp;
	
	@Value("${scheduler.port}")
	private String schedulerPort;
	
	@Value("${dataset.apm.temp.path}")
	private String apmTempPath;
	
	@Value("${dataset.apm.path}")
	private String apmPath;
	
	@Autowired
	private DataSourceService dataSourceService;
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private AipDataSetMapper aipDataSetMapper;
	
	@Autowired
	private AipDataSourceMapper aipDataSourceMapper;
	
	@Autowired
	private AipDataSetFileMapMapper aipDataSetFileMapMapper;

	@Autowired
	private AipUserMapper aipUserMapper;
	
	@Autowired
    private PlatformTransactionManager transactionManager;
	
	@Autowired
	private AESUtils aesUtils;
	
	@Override
	public AipDataSetKey uploadDataSetFiles(AipUser user, MultipartFile[] files, DataSetCreateReq req) {

		String dataId = DateUtils.getSystemId();

		try {
			
			AipDataSet dataSet = new AipDataSet();
			dataSet.setDataId(dataId);
			dataSet.setDataNm(req.getDataNm());
			dataSet.setDataTp(req.getDataTp());
			dataSet.setDataDesc(req.getDataDesc());
			dataSet.setExptStDt(req.getExptStDt());
			dataSet.setExptEdDt(req.getExptEdDt());
			dataSet.setRegId(user.getUserId());
			dataSet.setOwnerId(user.getUserId());
			dataSet.setPubFl(req.getPubFl());
			dataSet.setDelFl(FlagType.FALSE.getValue());
			dataSet.setRegDt(DateUtils.getNow());
			dataSet.setModDt(DateUtils.getNow());
			aipDataSetMapper.insert(dataSet);
			
			for (MultipartFile file : files) {
				this.uploadDataSetFile(file, dataId);
				
				// log insert
				AipLogs log = new AipLogs();
				log.setUserId(user.getUserId());
				log.setLogTp(LogType.DATASET.getValue());
				log.setLogStr(LogType.DATASET.getName() + " > " + LogMessageType.OK_DB_INSERT.getValue() + " " + file.getOriginalFilename());
			
				logService.createLog(log);
			}
			
			return this.getDataSetKey(dataId);
			
		}catch(Exception e) {
			log.error(e.getMessage(), e);
			this.deleteUploadedDataSetFile(files);
			logService.insertLog(user, LogType.DATASET, LogMessageType.ERROR_FILE_UPLOAD_HANDLE);
			throw new CustomException(ErrorCodeType.UPLOAD_FILE_ERROR);
		}
	}

	/**
	 * ?????? ?????? ?????????(MultipartFile)
	 *
	 * @param file
	 * @return
	 */
	private void uploadDataSetFile(MultipartFile file, String dataId) {

		Path fileUploadLocation = Paths.get(datasetPath).toAbsolutePath().normalize();

		AipDataSetInfo info = new AipDataSetInfo();

		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		try {

			if (fileName.contains("..")) {
				throw new CustomException(ErrorCodeType.FILE_WRONG_ACCESS);
			}

			Path targetLocation = fileUploadLocation.resolve(DateUtils.getDateForyearAndMonth());    // ????????? ????????? ??? ??????(??????: YYYYMM)
			if (!targetLocation.toFile().exists()) {
				targetLocation.toFile().mkdirs();
			}
			Path targetLocationFile = targetLocation.resolve(this.getNewFileName(fileName));    // ????????? ??? ?????? ???(?????? ???-systemId.?????????)

			Files.copy(file.getInputStream(), targetLocationFile, StandardCopyOption.REPLACE_EXISTING);

			AipDataSetFileMap dataSetFileMap = new AipDataSetFileMap();
			dataSetFileMap.setDataId(dataId);
			dataSetFileMap.setDataFileMapId(StrUtils.getUniqueId());
			dataSetFileMap.setFileNm(fileName);
			dataSetFileMap.setFilePath(targetLocationFile.toString());
			dataSetFileMap.setFileSz(file.getSize());
			dataSetFileMap.setRegDt(DateUtils.getNow());
			dataSetFileMap.setModDt(DateUtils.getNow());
			
			aipDataSetFileMapMapper.insert(dataSetFileMap);
			
			log.info(">>>>> File uploaded successfully. ");
			log.info(">>>>> dataset store file info :{}", info.toString());

		} catch (Exception e) {
			log.error(">>>>> dataset upload file error", e);
			throw new CustomException(ErrorCodeType.UPLOAD_FILE_ERROR);
		}
	}
	private String getNewFileName(String fileName) {

		String extensionSeperator = ".";

		// ?????? ????????? ??????????????? ????????????.
		Integer pos = fileName.lastIndexOf(extensionSeperator);
		StringBuilder sb = new StringBuilder();
		if (pos == -1) {
			log.debug(">>>>> pos is -1");
			sb.append(fileName);
			sb.append("-").append(DateUtils.getSystemId().substring(10));
		} else {
			log.debug(">>>>> pos is not -1");
			sb.append(fileName.substring(0, pos));
			sb.append("-").append(DateUtils.getSystemId().substring(10));
			sb.append(fileName.substring(pos));
		}

		return sb.toString();
	}
	
	/**
	 * ????????? ??? ?????? ?????? (?????? ????????? ?????? ??? ??????)
	 * 
	 * @param files
	 */
	private void deleteUploadedDataSetFile(MultipartFile[] files) {
		for(int i=0; i<files.length; i++) {
			String fileName = StringUtils.cleanPath(files[i].getOriginalFilename());
			if(Paths.get(fileName).toFile().exists()) {
				Paths.get(fileName).toFile().delete();
			}
		}
	}

	@Override
	public void deleteDatasetFile(DatasetDeleteReq req){
		try {
			req.getDatasetFileList().forEach(deleteFile -> {
				Path datasetRootPath = Paths.get(datasetPath).resolve(deleteFile.trim());
				datasetRootPath.toFile().delete();
				log.info(">>>>> delete to dataset file path: {}", datasetRootPath.toString());
			});
		} catch (Exception e) {
            log.error(">>>>> dataset delete file error", e);
            throw new CustomException(ErrorCodeType.COMMON_INTERNAL_SERVER_ERROR);
        }
	}

	@Override
	public Resource loadFileAsResource(String fileName) {

		try {
			Path filePath = Paths.get(datasetPath).resolve(fileName).normalize();
			log.info(">>>>> load file resource get file path info: {}", filePath.toString());

			Resource resource = new UrlResource(filePath.toUri());

			if (resource.exists()) {
				return resource;
			} else {
				throw new CustomException(ErrorCodeType.FILE_NOT_FOUND);
			}
		} catch (MalformedURLException mue) {
			log.error(">>>>> load file resources malformed URL error", mue);
			throw new CustomException(ErrorCodeType.FILE_NOT_FOUND);
		}
	}

	@Override
	public List<Map<String, Object>> overviewFile(DatasetReadReq req) {
		log.info(">>>>> file path :{}", req.getDatasetFile());

        if(!new File(req.getDatasetFile()).exists()){
			throw new CustomException(ErrorCodeType.FILE_NOT_FOUND);
        }

        List<Map<String, Object>> csvData = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(req.getDatasetFile()))){

            String[] csvContents;
            String[] csvHeaders = reader.readNext();

            for(int j = 0; j < req.getSize(); j++){

                Map<String, Object> csvMap = new HashMap<>();
                csvContents = reader.readNext();

                for(int i = 0; i < csvHeaders.length; i++){
                    csvMap.put(csvHeaders[i], csvContents[i]);
                }
                csvData.add(csvMap);
            }

        } catch (IOException e) {
        	log.error(">>>>> dataset overview error", e);
        	throw new CustomException(ErrorCodeType.FILE_PROCESS_HANDLE);
        }

		return csvData;
	}

	@Override
	public void transferDatasetToWorkspace(DatasetTransferReq req) {

		String url = RestApiUtils.makeUri(req.getWorkspaceServerIp(),
											workerAgentPort,
											AgentApiType.WORKER_FILE_UPLOAD);

		MultiValueMap<String, Object> queryParam = new LinkedMultiValueMap<>();

		FileSystemResource fileSystemResource = new FileSystemResource(new File(req.getFilePath()));
		queryParam.add("file", fileSystemResource);
		queryParam.add("targetPath", req.getTargetPath());

		log.info(">>>>> transfer dataset file name :{}", fileSystemResource.getFilename());
		log.info(">>>>> transfer dataset target path :{}", req.getTargetPath());

		AipHttpHeaders httpHeaders = new AipHttpHeaders();
		httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

//		RestApiUtils.callApi(url, queryParam, HttpMethod.POST, httpHeaders);
		RestApiUtils.callApiFalseBufferRequest(url, queryParam, HttpMethod.POST, httpHeaders);
	}
	
	
	@Override
	public void extractDataSet(AipUser user, AipDataSetKey key) {
		
		// 1. aip_data_set ??????
		AipDataSet dataSetInfo = aipDataSetMapper.selectByPrimaryKey(key);
		
		// 2. ???????????? data_source ?????? ??????
		AipDataSourceKey dataSourceKey = new AipDataSourceKey();
		dataSourceKey.setDsId(dataSetInfo.getDsId());
		AipDataSource dataSourceInfo = aipDataSourceMapper.selectByPrimaryKey(dataSourceKey);
		
		// 3. data_source??? aip_data_set??? ?????? ??????
		try {
			
			dataSourceInfo.setDsConnId(aesUtils.decrypt(dataSetInfo.getDsConnId()));
			dataSourceInfo.setDsConnPswd(aesUtils.decrypt(dataSetInfo.getDsConnPswd()));
			
			Path dataSetFileName = this.getDataSetFileName(dataSetInfo);
			log.info(">>>>> dataSetFileName: {}", dataSetFileName.toFile().getAbsolutePath());
			this.createDataSetToCsv(dataSetInfo, dataSourceInfo, dataSetFileName.toString());

			// 4. ?????? ??????
			if (dataSetInfo.getDataTp().equals(DataSetType.DATABASE.getValue())) {
				AipLogs log = new AipLogs();
				log.setUserId(user.getUserId());
				log.setLogTp(LogType.DATASET.getValue());
				log.setLogStr(LogType.DATASET.getName() + " > " + LogMessageType.OK_DB_INSERT.getValue() + " " + dataSetInfo.getQueryStr());
				
				logService.createLog(log);
			}
			
			// 4.1. ???????????? ?????? ???
			this.insertDataSetFileMap(dataSetInfo, dataSetFileName);
			
			if(ObjectUtils.isEmpty(dataSetInfo.getScheduleTp()) ||
					dataSetInfo.getScheduleTp().equals(ScheduleExecuteType.NEXT_DAY.getValue())) {
				
				// ????????? ??????
				String url = RestApiUtils.makeUri(daplmgIp,	schedulerPort, AgentApiType.SCHEDULER_JOB_DELETE);
				
				ScheduleDeleteReq scheduleDeleteReq = new ScheduleDeleteReq();
	        	scheduleDeleteReq.setJobGroup(ScheduleType.DATASET.getValue());
	        	scheduleDeleteReq.setJobName(dataSetInfo.getDataId());
	        	RestApiUtils.callApi(url, scheduleDeleteReq, HttpMethod.POST, null);
	        	
			}
			logService.insertLog(user, LogType.SCHEDULE_DATASET, LogMessageType.OK_SCHEDULE);
			
		}catch(Exception e) {
			log.error(e.getMessage(), e);
			logService.insertLog(user, LogType.SCHEDULE_DATASET, LogMessageType.ERROR_COMMON_SCHEDULE_FAIL);
			throw new CustomException(ErrorCodeType.COMMON_SCHEDULE_FAIL);
		}
	}
	
	/**
	 * ???????????? ?????? ?????? ??????
	 *   
	 * @author fruitson
	 * @date 19.04.21  
	 *   
	 * @param dataSetInfo
	 * @return
	 */
	private Path getDataSetFileName(AipDataSet dataSetInfo) {
		Path fileUploadLocation = Paths.get(datasetPath).toAbsolutePath().normalize();
		Path targetLocation = fileUploadLocation.resolve(DateUtils.getDateForyearAndMonth());    // ????????? ????????? ??? ??????(??????: YYYYMM)
		if (!targetLocation.toFile().exists()) {
			targetLocation.toFile().mkdirs();
		}
		return targetLocation.resolve(dataSetInfo.getFileNm() + "_" + DateUtils.getFullDateAndTime() + ".csv");    // ????????? ??? ?????? ???(?????? ???-systemId.?????????)
	}

	/**
	 * ?????? ????????? CSV ????????? ??????
	 * 
	 * @author fruitson
	 * @date 19.04.21
	 * 
	 * @param dataSetInfo
	 * @param dataSourceInfo
	 * @param dataSetFileName
	 */
	private void createDataSetToCsv(AipDataSet dataSetInfo, AipDataSource dataSourceInfo, String dataSetFileName) {
		dataSourceService.extractDatas(AipDatabaseInfo.fromDao(dataSourceInfo), dataSetInfo.getQueryStr(), dataSetFileName);
	}
	
	/**
	 * ????????? ??? ?????? ?????? ???????????? ????????? ??????
	 * 
	 * @author fruitson
	 * @date 19.04.21
	 * 
	 * @param dataSetInfo
	 * @param dataSetFileName
	 */
	private void insertDataSetFileMap(AipDataSet dataSetInfo, Path dataSetFileName) {
		AipDataSetFileMap dataSetFileMap = new AipDataSetFileMap();
		dataSetFileMap.setDataFileMapId(DateUtils.getSystemId());
		dataSetFileMap.setDataId(dataSetInfo.getDataId());
		dataSetFileMap.setFilePath(dataSetFileName.toAbsolutePath().toString());
		dataSetFileMap.setFileNm(dataSetFileName.getFileName().toString());
		dataSetFileMap.setFileSz(dataSetFileName.toFile().length());
		dataSetFileMap.setRegDt(DateUtils.getNow());
		dataSetFileMap.setModDt(DateUtils.getNow());
		aipDataSetFileMapMapper.insert(dataSetFileMap);
	}
	
	
	
	@Override
	public AipDataObjectInfo overview(AipUser user, DataSetOverviewReq req) {
		
		// 0. data_set_id ??????
		AipDataSetKey dataSetKey = new AipDataSetKey();
		dataSetKey.setDataId(req.getDataId());
		AipDataSet aipDataSet = aipDataSetMapper.selectByPrimaryKey(dataSetKey);
		
		if (ObjectUtils.isEmpty(aipDataSet)) {
            logService.insertLog(user, LogType.DATASET, LogMessageType.ERROR_COMMON_NOT_FOUND_ID);
            log.error(">>>>> DataSet doesn't exists.....");
            throw new CustomException(ErrorCodeType.COMMON_NOT_FOUND_ID);
        }
		
		// 1. data_set_file_map_id ??????
		AipDataSetFileMapKey dataSetFileMapKey = new AipDataSetFileMapKey();
		dataSetFileMapKey.setDataFileMapId(req.getDataFileMapId());
		AipDataSetFileMap aipDataSetFileMap = aipDataSetFileMapMapper.selectByPrimaryKey(dataSetFileMapKey);
		
		if (ObjectUtils.isEmpty(aipDataSetFileMap)) {
            logService.insertLog(user, LogType.DATASET, LogMessageType.ERROR_COMMON_NOT_FOUND_ID);
            log.error(">>>>> DataSet File doesn't exists.....");
            throw new CustomException(ErrorCodeType.COMMON_NOT_FOUND_ID);
        }
		
		if(!new File(aipDataSetFileMap.getFilePath()).exists()){
			throw new CustomException(ErrorCodeType.FILE_NOT_FOUND);
        }
		
		// 2. csv ?????? read
		
		log.info(">>>>> datasetfilepath: {}", aipDataSetFileMap.getFilePath());
		
		List<Map<String, Object>> csvData = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(aipDataSetFileMap.getFilePath()))){

            String[] csvContents;
            String[] csvHeaders = reader.readNext();
            
            for(int k=0; k<csvHeaders.length; k++) {
            	log.info("     >>>>> csvHeaders: {}", csvHeaders[k]);
            }

            for(int j = 0; j < 10; j++){

                Map<String, Object> csvMap = new HashMap<>();
                csvContents = reader.readNext();

                if(!ObjectUtils.isEmpty(csvContents) ) {
                	for(int i = 0; i < csvHeaders.length; i++){
                        csvMap.put(csvHeaders[i], csvContents[i]);
                    }
                    csvData.add(csvMap);
                }
            }

        } catch (IOException e) {
        	log.error(">>>>> dataset overview error", e);
        	throw new CustomException(ErrorCodeType.FILE_PROCESS_HANDLE);
        }
		
        AipDataObjectInfo dataObjectInfo = new AipDataObjectInfo();
        dataObjectInfo.setData(csvData);
		
		return dataObjectInfo;
	}
	
	@Override
	public void moveApmDataSet() {
		log.info(">>>>> [APM DataSet Check] Start.....");
		
		AipUser rootUser = new AipUser();
		rootUser.setUserId("root");
		
		TransactionStatus transactionStatus = transactionManager.getTransaction(DbUtils.getDefaultTransactionDefinition("### [Transaction] DatasetServiceImpl.moveApmDataSet ###"));
		
		if (!Paths.get(apmPath).toFile().exists()) {
			FileUtils.mkdir(Paths.get(apmPath).toFile());
		}
		
		if(!FileUtils.checkDirectoryIsEmpty(apmTempPath)) {
			try {
				log.info(">>>>> [APM DataSet Check] Apm DataSet is moving from temp directory...");
				File[] sourceFiles = Paths.get(apmTempPath).toAbsolutePath().normalize().toFile().listFiles();
				
				Path targetPathWithDate = 
						Paths.get(apmPath).toAbsolutePath().normalize().resolve(DateUtils.getDateForyearAndMonth());
				if(!targetPathWithDate.toFile().exists()) {
					FileUtils.mkdir(targetPathWithDate.toFile());
				}
				
				for (File sourceFile : sourceFiles) {
					File targetFile = targetPathWithDate.resolve(this.getNewFileName(sourceFile.getName())).toFile();
					
					FileUtils.moveFiles(sourceFile, targetFile);
					
					logService.insertLog(rootUser, LogType.SCHEDULER, LogMessageType.OK_FILE_MOVE.getValue() + " [ " + sourceFile.getName() + " ]");
					
					String datasetId = this.insertApmDataSet(rootUser, sourceFile.getName());
					this.insertApmDataSetFileMap(sourceFile, targetFile, datasetId);
					
					log.info(">>>>> [APM DataSet Check] End...");
				}
			}catch(Exception e) {
				log.error(e.getMessage(), e);
				transactionManager.rollback(transactionStatus);
				logService.insertLog(rootUser, LogType.SCHEDULER, LogMessageType.ERROR_DATASET_APM_MOVE);
				throw e;
			}
			
			transactionManager.commit(transactionStatus);
		}
	}
	
	private void insertApmDataSetFileMap(File sourceFile, File targetFile, String datasetId) {
		
		Date now = DateUtils.getNow();
		
		AipDataSetFileMap dataSetFileMap = new AipDataSetFileMap();
		dataSetFileMap.setDataId(datasetId);
		dataSetFileMap.setDataFileMapId(StrUtils.getUniqueId());
		dataSetFileMap.setFileNm(sourceFile.getName());
		dataSetFileMap.setFilePath(targetFile.getAbsolutePath());
		dataSetFileMap.setFileSz(targetFile.length());
		dataSetFileMap.setRegDt(now);
		dataSetFileMap.setModDt(now);
		
		aipDataSetFileMapMapper.insertSelective(dataSetFileMap);
	}

	private String insertApmDataSet(AipUser rootUser, String fileName) {
		
		String dataSetId = DateUtils.getSystemId();
		Date now = DateUtils.getNow();
		
		AipDataSet dataSet = new AipDataSet();
		dataSet.setDataId(dataSetId);
		dataSet.setDataNm(fileName);
		dataSet.setDataDesc(fileName);
		dataSet.setDataTp(DataSetType.APM.getValue());
		dataSet.setOwnerId(rootUser.getUserId());
		dataSet.setRegId(rootUser.getUserId());
		dataSet.setExptStDt(DateUtils.getDateOnlyString(now));
		dataSet.setExptEdDt(DateUtils.getDateOnlyString(LocalDate.now().plusMonths(3L)));
		dataSet.setPubFl(FlagType.TRUE.getValue());
		dataSet.setDelFl(FlagType.FALSE.getValue());
		dataSet.setRegDt(now);
		dataSet.setModDt(now);
		
		aipDataSetMapper.insertSelective(dataSet);
		return dataSetId;
	}

	/**
	 * ???????????? Key ??????
	 * 
	 * @param dataId
	 * @return
	 */
	private AipDataSetKey getDataSetKey(String dataId) {
		AipDataSetKey key = new AipDataSetKey();
		key.setDataId(dataId);
		return key;
	}
	
}