package com.nomad.data.agent.dataset.service.dataset;

import com.nomad.data.agent.common.dto.AipHttpHeaders;
import com.nomad.data.agent.config.exception.CustomException;
import com.nomad.data.agent.dataset.dto.info.AipDataObjectInfo;
import com.nomad.data.agent.dataset.dto.info.AipDataSetInfo;
import com.nomad.data.agent.dataset.dto.info.AipDatabaseInfo;
import com.nomad.data.agent.dataset.dto.req.*;
import com.nomad.data.agent.domain.dao.common.*;
import com.nomad.data.agent.domain.mappers.common.AipDataSetFileMapMapper;
import com.nomad.data.agent.domain.mappers.common.AipDataSetMapper;
import com.nomad.data.agent.domain.mappers.common.AipDataSourceMapper;
import com.nomad.data.agent.helper.AipServerHelper;
import com.nomad.data.agent.log.service.LogService;
import com.nomad.data.agent.utils.*;
import com.nomad.data.agent.utils.enums.*;
import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@SuppressWarnings("SpellCheckingInspection")
public class DatasetServiceImpl implements DatasetService {

	private final LogService serviceLogs;
	private final DataSourceService serviceDataSource;

	private final PlatformTransactionManager platformTransactionManager;

	private final AipDataSetMapper aipDataSetMapper;
	private final AipDataSourceMapper aipDataSourceMapper;
	private final AipDataSetFileMapMapper aipDataSetFileMapMapper;

	private final AipServerHelper helper;
	private final AESUtils aesUtils;

	@Value("${dataset.dir}")
	private String datasetPath;

	@Value("${scheduler.port}")
	private String schedulerPort;

	@Value("${dataset.apm.temp.path}")
	private String apmTempPath;

	@Value("${dataset.apm.path}")
	private String apmPath;

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

				serviceLogs.createLog(log);
			}

			return this.getDataSetKey(dataId);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			this.deleteUploadedDataSetFile(files);
			serviceLogs.insertLog(user, LogType.DATASET, LogMessageType.ERROR_FILE_UPLOAD_HANDLE);
			throw new CustomException(ErrorCodeType.UPLOAD_FILE_ERROR);
		}
	}

	/**
	 * 단일 파일 업로드(MultipartFile)
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

			Path targetLocation = fileUploadLocation.resolve(DateUtils.getDateForyearAndMonth());    // 파일이 업로드 될 경로(년월: YYYYMM)
			if (!targetLocation.toFile().exists()) {
				targetLocation.toFile().mkdirs();
			}
			Path targetLocationFile = targetLocation.resolve(this.getNewFileName(fileName));    // 업로드 될 파일 명(파일 명-systemId.확장자)

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

		// 파일 이름이 중복되므로 수정한다.
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
	 * 업로드 된 파일 삭제 (파일 업로드 실패 시 호출)
	 *
	 * @param files
	 */
	private void deleteUploadedDataSetFile(MultipartFile[] files) {
		for (int i = 0; i < files.length; i++) {
			String fileName = StringUtils.cleanPath(files[i].getOriginalFilename());
			if (Paths.get(fileName).toFile().exists()) {
				Paths.get(fileName).toFile().delete();
			}
		}
	}

	@Override
	public void deleteDatasetFile(DatasetDeleteReq req) {
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

		if (!new File(req.getDatasetFile()).exists()) {
			throw new CustomException(ErrorCodeType.FILE_NOT_FOUND);
		}

		List<Map<String, Object>> csvData = new ArrayList<>();
		try (CSVReader reader = new CSVReader(new FileReader(req.getDatasetFile()))) {

			String[] csvContents;
			String[] csvHeaders = reader.readNext();

			for (int j = 0; j < req.getSize(); j++) {

				Map<String, Object> csvMap = new HashMap<>();
				csvContents = reader.readNext();

				for (int i = 0; i < csvHeaders.length; i++) {
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

		AipServer worker = helper.findFirstServerBy(ServerType.WORKER, req.getWorkspaceServerIp());
		String url = RestApiUtils.makeUri(worker.getPrvIp(), worker.getPort(), AgentApiType.WORKER_FILE_UPLOAD);
		log.info("( url : {}", url);
		log.info(" (req.getWorkspaceServerIp()) : {} ", req.getWorkspaceServerIp());

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

		// 1. aip_data_set 조회
		AipDataSet dataSetInfo = aipDataSetMapper.selectByPrimaryKey(key);

		// 2. 해당하는 data_source 정보 조회
		AipDataSourceKey dataSourceKey = new AipDataSourceKey();
		dataSourceKey.setDsId(dataSetInfo.getDsId());
		AipDataSource dataSourceInfo = aipDataSourceMapper.selectByPrimaryKey(dataSourceKey);

		// 3. data_source로 aip_data_set의 쿼리 전송
		try {

			dataSourceInfo.setDsConnId(aesUtils.decrypt(dataSetInfo.getDsConnId()));
			dataSourceInfo.setDsConnPswd(aesUtils.decrypt(dataSetInfo.getDsConnPswd()));

			Path dataSetFileName = this.getDataSetFileName(dataSetInfo);
			log.info(">>>>> dataSetFileName: {}", dataSetFileName.toFile().getAbsolutePath());
			this.createDataSetToCsv(dataSetInfo, dataSourceInfo, dataSetFileName.toString());

			// 4. 로그 저장
			if (dataSetInfo.getDataTp().equals(DataSetType.DATABASE.getValue())) {
				AipLogs log = new AipLogs();
				log.setUserId(user.getUserId());
				log.setLogTp(LogType.DATASET.getValue());
				log.setLogStr(LogType.DATASET.getName() + " > " + LogMessageType.OK_DB_INSERT.getValue() + " " + dataSetInfo.getQueryStr());

				serviceLogs.createLog(log);
			}

			// 4.1. 데이터셋 파일 맵
			this.insertDataSetFileMap(dataSetInfo, dataSetFileName);

			if (ObjectUtils.isEmpty(dataSetInfo.getScheduleTp()) || dataSetInfo.getScheduleTp().equals(ScheduleExecuteType.NEXT_DAY.getValue())) {

				AipServer master = helper.findFirstServerBy(ServerType.MANAGER);

				// 스케줄 삭제
				String url = RestApiUtils.makeUri(master.getPrvIp(), schedulerPort, AgentApiType.SCHEDULER_JOB_DELETE);

				ScheduleDeleteReq scheduleDeleteReq = new ScheduleDeleteReq();
				scheduleDeleteReq.setJobGroup(ScheduleType.DATASET.getValue());
				scheduleDeleteReq.setJobName(dataSetInfo.getDataId());
				RestApiUtils.callApi(url, scheduleDeleteReq, HttpMethod.POST, null);

			}
			serviceLogs.insertLog(user, LogType.SCHEDULE_DATASET, LogMessageType.OK_SCHEDULE);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			serviceLogs.insertLog(user, LogType.SCHEDULE_DATASET, LogMessageType.ERROR_COMMON_SCHEDULE_FAIL);
			throw new CustomException(ErrorCodeType.COMMON_SCHEDULE_FAIL);
		}
	}

	/**
	 * 데이터셋 파일 이름 조회
	 *
	 * @param dataSetInfo
	 * @return
	 * @author fruitson
	 * @date 19.04.21
	 */
	private Path getDataSetFileName(AipDataSet dataSetInfo) {
		Path fileUploadLocation = Paths.get(datasetPath).toAbsolutePath().normalize();
		Path targetLocation = fileUploadLocation.resolve(DateUtils.getDateForyearAndMonth());    // 파일이 업로드 될 경로(년월: YYYYMM)
		if (!targetLocation.toFile().exists()) {
			targetLocation.toFile().mkdirs();
		}
		return targetLocation.resolve(dataSetInfo.getFileNm() + "_" + DateUtils.getFullDateAndTime() + ".dat");    // 업로드 될 파일 명(파일 명-systemId.확장자)
	}

	/**
	 * 추출 데이터 CSV 파일로 변환
	 *
	 * @param dataSetInfo
	 * @param dataSourceInfo
	 * @param dataSetFileName
	 * @author fruitson
	 * @date 19.04.21
	 */
	private void createDataSetToCsv(AipDataSet dataSetInfo, AipDataSource dataSourceInfo, String dataSetFileName) {
		serviceDataSource.extractDatas(AipDatabaseInfo.fromDao(dataSourceInfo), dataSetInfo.getQueryStr(), dataSetFileName);
	}

	/**
	 * 데이터 셋 파일 매핑 테이블에 데이터 저장
	 *
	 * @param dataSetInfo
	 * @param dataSetFileName
	 * @author fruitson
	 * @date 19.04.21
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

		// 0. data_set_id 검증
		AipDataSetKey dataSetKey = new AipDataSetKey();
		dataSetKey.setDataId(req.getDataId());
		AipDataSet aipDataSet = aipDataSetMapper.selectByPrimaryKey(dataSetKey);

		if (ObjectUtils.isEmpty(aipDataSet)) {
			serviceLogs.insertLog(user, LogType.DATASET, LogMessageType.ERROR_COMMON_NOT_FOUND_ID);
			log.error(">>>>> DataSet doesn't exists.....");
			throw new CustomException(ErrorCodeType.COMMON_NOT_FOUND_ID);
		}

		// 1. data_set_file_map_id 검증
		AipDataSetFileMapKey dataSetFileMapKey = new AipDataSetFileMapKey();
		dataSetFileMapKey.setDataFileMapId(req.getDataFileMapId());
		AipDataSetFileMap aipDataSetFileMap = aipDataSetFileMapMapper.selectByPrimaryKey(dataSetFileMapKey);

		if (ObjectUtils.isEmpty(aipDataSetFileMap)) {
			serviceLogs.insertLog(user, LogType.DATASET, LogMessageType.ERROR_COMMON_NOT_FOUND_ID);
			log.error(">>>>> DataSet File doesn't exists.....");
			throw new CustomException(ErrorCodeType.COMMON_NOT_FOUND_ID);
		}

		if (!new File(aipDataSetFileMap.getFilePath()).exists()) {
			throw new CustomException(ErrorCodeType.FILE_NOT_FOUND);
		}

		// 2. csv 파일 read

		log.info(">>>>> datasetfilepath: {}", aipDataSetFileMap.getFilePath());

		List<Map<String, Object>> csvData = new ArrayList<>();
		try (CSVReader reader = new CSVReader(new FileReader(aipDataSetFileMap.getFilePath()))) {

			String[] csvContents;
			String[] csvHeaders = reader.readNext();

			for (int k = 0; k < csvHeaders.length; k++) {
				log.info("     >>>>> csvHeaders: {}", csvHeaders[k]);
			}

			for (int j = 0; j < 10; j++) {

				Map<String, Object> csvMap = new HashMap<>();
				csvContents = reader.readNext();

				if (!ObjectUtils.isEmpty(csvContents)) {
					for (int i = 0; i < csvHeaders.length; i++) {
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

	/**
	 * apm 파일을 이동한다.
	 * 임시 저장경로 -> 저장경로
	 */
	@Override
	public void moveApmDataSet() {
		log.info(">>>>> [APM DataSet Check] Start.....");

		AipUser rootUser = new AipUser();
		rootUser.setUserId("root");

		TransactionStatus transactionStatus = platformTransactionManager.getTransaction(DbUtils.getDefaultTransactionDefinition("### [Transaction] DatasetServiceImpl.moveApmDataSet ###"));

		if (!Paths.get(apmPath).toFile().exists()) {
			FileUtils.mkdir(Paths.get(apmPath).toFile());
		}

		if (!FileUtils.checkDirectoryIsEmpty(apmTempPath)) {
			try {
				log.info(">>>>> [APM DataSet Check] Apm DataSet is moving from temp directory...");
				File[] sourceFiles = Paths.get(apmTempPath).toAbsolutePath().normalize().toFile().listFiles();

				Path targetPathWithDate =
						Paths.get(apmPath).toAbsolutePath().normalize().resolve(DateUtils.getDateForyearAndMonth());
				if (!targetPathWithDate.toFile().exists()) {
					FileUtils.mkdir(targetPathWithDate.toFile());
				}

				for (File sourceFile : sourceFiles) {
					File targetFile = targetPathWithDate.resolve(this.getNewFileName(sourceFile.getName())).toFile();

					FileUtils.moveFiles(sourceFile, targetFile);

					serviceLogs.insertLog(rootUser, LogType.SCHEDULER, LogMessageType.OK_FILE_MOVE.getValue() + " [ " + sourceFile.getName() + " ]");

					String datasetId = this.insertApmDataSet(rootUser, sourceFile.getName());
					this.insertApmDataSetFileMap(sourceFile, targetFile, datasetId);

					log.info(">>>>> [APM DataSet Check] End...");
				}
			} catch (Exception e) {
				platformTransactionManager.rollback(transactionStatus);
				serviceLogs.insertLog(rootUser, LogType.SCHEDULER, LogMessageType.ERROR_DATASET_APM_MOVE);
				log.error(">>>>> apm dataset move error", e);
				throw e;
			}

			platformTransactionManager.commit(transactionStatus);
		}
	}

	/**
	 * db insert : aip_data_set_file_map
	 * apm 파일의 정보를 테이블에 적재한다.
	 *
	 * @param sourceFile 소스 파일
	 * @param targetFile 타겟 파일
	 * @param datasetId  데이터셋 아이디
	 */
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

	/**
	 * db insert : aip_data_set
	 * apm 파일의 정보를 테이블에 적재한다.
	 *
	 * @param aipUser  사용자
	 * @param fileName 파일이름
	 * @return 생성된 데이터셋 아이디
	 */
	private String insertApmDataSet(AipUser aipUser, String fileName) {

		String dataSetId = DateUtils.getSystemId();
		Date now = DateUtils.getNow();

		AipDataSet dataSet = new AipDataSet();
		dataSet.setDataId(dataSetId);
		dataSet.setDataNm(fileName);
		dataSet.setDataDesc(fileName);
		dataSet.setDataTp(DataSetType.APM.getValue());
		dataSet.setOwnerId(aipUser.getUserId());
		dataSet.setRegId(aipUser.getUserId());
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
	 * 데이터셋 Key 생성
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