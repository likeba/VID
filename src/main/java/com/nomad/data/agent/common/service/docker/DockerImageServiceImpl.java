package com.nomad.data.agent.common.service.docker;

import com.nomad.data.agent.common.dto.AipHttpHeaders;
import com.nomad.data.agent.common.dto.req.AipDockerImageCreateReq;
import com.nomad.data.agent.common.dto.req.AipDockerImageDeleteReq;
import com.nomad.data.agent.common.dto.req.AipDockerRegistryImageDeleteReq;
import com.nomad.data.agent.config.exception.CustomException;
import com.nomad.data.agent.domain.dao.common.*;
import com.nomad.data.agent.domain.mappers.common.AipDockerImageMapper;
import com.nomad.data.agent.domain.mappers.common.AipImgLibMapMapper;
import com.nomad.data.agent.domain.mappers.common.AipWorkspaceMapper;
import com.nomad.data.agent.helper.AipServerHelper;
import com.nomad.data.agent.log.service.LogService;
import com.nomad.data.agent.utils.*;
import com.nomad.data.agent.utils.enums.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@SuppressWarnings("SpellCheckingInspection")
public class DockerImageServiceImpl implements DockerImageService {

	private final LogService serviceLogs;

	private final PlatformTransactionManager platformTransactionManager;

	private final AipDockerImageMapper aipDockerImageMapper;
	private final AipImgLibMapMapper aipImgLibMapMapper;
	private final AipWorkspaceMapper aipWorkspaceMapper;

	private final AipServerHelper helper;
	private final DockerUtils dockerUtils;
	private final CompressUtils compressUtils;

	@Value("${docker.registry.port}")
	String dockerRegistryPort;

	@Value("${docker.registry.path}")
	String dockerRegistryPath;

	@Value("${docker.image.custom.tar.path}")
	String dockerImageCustomTarPath;

	/**
	 * 도커 레지스트리의 이미지를 삭제한다.
	 *
	 * @param user 사용자
	 * @param req  도커이미지삭제 객체
	 * @return 도커이미지 삭제 정보(이미지이름)
	 * @author eunbeelee
	 * @date 2020.04.08
	 */
	@Override
	public void deleteRegistryImage(AipUser user, AipDockerRegistryImageDeleteReq req) {
		log.debug(">>>>> delete registry image start");

		String dockerImageId = req.getImgId();
		AipDockerImage aipDockerImage = read(dockerImageId);

		if (ObjectUtils.isEmpty(aipDockerImage)) {
			log.error(">>>>> aipdockerimage is empty");
			serviceLogs.insertLog(user, LogType.DOCKER, LogMessageType.ERROR_COMMON_NOT_FOUND_ID);
			throw new CustomException(ErrorCodeType.COMMON_NOT_FOUND_ID);
		}

		String dockerImageName = aipDockerImage.getDckrImgNm();
		String dockerImageTag = aipDockerImage.getDckrImgTag();

		log.info(">>>>> docker image name :{}", dockerImageName);
		log.info(">>>>> docker image tag :{}", dockerImageTag);

		try {
			this.deletePulledImageOnWorkerServer(dockerImageId, dockerImageName, dockerImageTag);

			String dockerImageDigest = this.getDockerImageId(dockerImageName, dockerImageTag);

			this.deleteRegistryImageByDockerApi(dockerImageName, dockerImageDigest);

			this.execRegistryGarbageCollect();

			TransactionStatus transactionStatus = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());

			try {
				this.deleteImageLibMapInfo(dockerImageId);
				this.deleteDockerImageInfo(dockerImageName, dockerImageTag);

				serviceLogs.insertLog(user, LogType.DOCKER, LogMessageType.OK_DB_DELETE);

				platformTransactionManager.commit(transactionStatus);

			} catch (Exception e) {
				log.error(">>>>> delete docker image database error", e);
				platformTransactionManager.rollback(transactionStatus);
				serviceLogs.insertLog(user, LogType.DOCKER, LogMessageType.ERROR_DB_DELETE_HANDLE);
			}

		} catch (Exception e) {
			log.error(">>>>> delete registry image error", e);
			throw new CustomException(e.getMessage(), ErrorCodeType.DOCKER_IMAGE_DELETE_ERROR);
		}
	}

	/**
	 * 도커레지스트리 가비지콜렉터를 실행한다.
	 *
	 * @author eunbeelee
	 * @date 2020.05.08
	 */
	private void execRegistryGarbageCollect() {
		CommandUtils.run("docker exec -it docker-registry_registry_1 bin/registry garbage-collect  /etc/docker/registry/config.yml");
	}

	/**
	 * 도커이미지 관련 테이블 - aip_img_lib_map
	 * 해당 정보를 삭제 한다.
	 *
	 * @param dockerImageId 도커이미지아이디
	 * @return 쿼리 실행 결과
	 * @author eunbeelee
	 * @date 2020.04.29
	 */
	private Integer deleteImageLibMapInfo(String dockerImageId) {
		AipImgLibMapExample example = new AipImgLibMapExample();
		example.createCriteria().andImgIdEqualTo(dockerImageId);

		if (ObjectUtils.isEmpty(example)) {
			log.error(">>>>> image lib map example error");
			throw new CustomException(ErrorCodeType.DB_PROCESS_HANDLE);
		}

		return aipImgLibMapMapper.deleteByExample(example);
	}

	/**
	 * 도커이미지 tar 파일을 사용하여
	 * 도커 레지스트리에 이미지를 등록한다.
	 *
	 * @param user 사용자
	 * @param req  도커이미지등록 객체
	 * @author eunbeelee
	 * @date 2020.04.10
	 * @retrn 도커이미지등록정보 객체
	 */
	@Override
	public AipDockerImageKey create(AipUser user, AipDockerImageCreateReq req) {
		log.debug(">>>>> create registry image start");
		log.debug(">>>>> docker image create req:{}", req.toString());

		String dockerImageName = req.getDckrImgNm();
		String dockerImageTag = req.getDckrImgTag();

		String fileOriginalFilename = req.getDckrImgFileNm().replace(".tar", "");

		try {
			this.checkDuplicateDockerImage(dockerImageName, dockerImageTag);
		} catch (Exception e) {
			log.error(">>>>> duplicate docker image error", e);
			throw e;
		}

		String loadedDockerImageInfo = this.getDockerImageRepoTag(req.getDckrImgFileNm());

		log.info(">>>>> file load end");
		log.info(">>>>> {}", loadedDockerImageInfo);

		try {

			log.debug(">>>>> get docker image load start");
			this.loadDockerImage(fileOriginalFilename);
			log.debug(">>>>> get docker image load end");

			log.debug(">>>>> get docker image tag start");
			this.tagDockerImage(loadedDockerImageInfo, dockerImageName, dockerImageTag);
			log.debug(">>>>> get docker image tag end");

			log.debug(">>>>> get docker image push start");
			this.pushDockerImageToRegistry(dockerImageName, dockerImageTag);
			log.debug(">>>>> get docker image push end");

			log.debug(">>>>> get docker image delete start");
			this.deleteDockerImage(loadedDockerImageInfo.split(":")[0], loadedDockerImageInfo.split(":")[1]);
			this.deleteDockerImage(this.getRegistryImageName(dockerImageName), dockerImageTag);
			log.debug(">>>>> get docker image delete end");

			String dockerImageId = DateUtils.getSystemId();
			Integer dockerImageResult = this.insertDockerImageInfo(user, req, dockerImageId);

			Integer dockerImageLibMapResult = this.insertImageLibMapInfo(dockerImageId);

			if (dockerImageResult > 0 && dockerImageLibMapResult > 0) {
				serviceLogs.insertLog(user, LogType.DOCKER, LogMessageType.OK_DB_INSERT);
				return getKey(dockerImageId);
			}
			throw new CustomException(ErrorCodeType.DB_INSERT_HANDLE);

		} catch (Exception e) {
			log.error(">>>>> load docker image error", e);
			throw new CustomException(ErrorCodeType.DOCKER_IMAGE_REGISTER_ERROR);
		}
	}

	@Override
	public List<String> list() {
		List<String> dockerFileList = new ArrayList<>();

		String[] fileList = FileUtils.listFiles(dockerImageCustomTarPath);
		if (fileList == null) {
			return null;
		}
		for (String file : fileList) {
			if (file.contains(".tar")) {
				dockerFileList.add(file);
			}
		}
		return dockerFileList;
	}

	/**
	 * 도커이미지 아이디를
	 * 관련 테이블에 적재한다. (aip_img_lib_map)
	 *
	 * @param dockerImageId 도커이미지아이디
	 * @return db insert 결과 값
	 * @author eunbeelee
	 * @date 2020.04.29
	 */
	private Integer insertImageLibMapInfo(String dockerImageId) {
		AipImgLibMap aipImgLibMap = new AipImgLibMap();

		aipImgLibMap.setImgId(dockerImageId);
		aipImgLibMap.setLibSmlCd(CodeEditorType.CST.getValue());
		aipImgLibMap.setRegDt(DateUtils.getNow());
		aipImgLibMap.setModDt(DateUtils.getNow());

		return aipImgLibMapMapper.insert(aipImgLibMap);
	}

	/**
	 * 중복되는 도커이미지 정보가 존재하는 지
	 * db 정보를 조회한다.
	 *
	 * @param dockerImageName 도커이미지이름
	 * @param dockerImageTag  도커이미지태그
	 * @author eunbeelee
	 * @date 2020.04.29
	 */
	private void checkDuplicateDockerImage(String dockerImageName, String dockerImageTag) {
		AipDockerImageExample aipDockerImageExample = new AipDockerImageExample();
		aipDockerImageExample.createCriteria().andDckrImgNmEqualTo(dockerImageName).andDckrImgTagEqualTo(dockerImageTag).andDelFlEqualTo(FlagType.FALSE.getValue());

		List<AipDockerImage> result = aipDockerImageMapper.selectByExample(aipDockerImageExample);

		if (!ObjectUtils.isEmpty(result)) {
			throw new CustomException(ErrorCodeType.DOCKER_IMAGE_DUPLICATE_ERROR);
		}
	}

	/**
	 * 파일 유효성 검사를 진행한다.
	 * (tar 파일 인지 확인)
	 *
	 * @param file
	 * @return file 확장자 제외한 파일이름
	 * @author eunbeelee
	 * @date 2020.04.08
	 */
	private String checkFileValidation(MultipartFile file) {
		String fileOriginalFilename = file.getOriginalFilename();

		if (!fileOriginalFilename.contains(".tar")) {
			throw new CustomException(ErrorCodeType.FILE_INVALID_TYPE);
		}
		return fileOriginalFilename.replace(".tar", "");
	}

	/**
	 * 도커이미지아이디를 조회한다.
	 *
	 * @param dockerImageName 도커이미지이름
	 * @param dockerImageTag  도커이미지태그
	 * @return 도커이미지아이디 (ex :  sha256:2018287052387a5ba149ec7310c2289e3c2385bba08f4ef8ca043ba8201d0e60)
	 * @author eunbeelee
	 * @date 2020.04.08
	 */
	private String getDockerImageId(String dockerImageName, String dockerImageTag) {
		log.debug(">>>>> get docker image id start");

		AipServer dataServer = helper.findFirstServerBy(ServerType.DATA);

		String apiType = String.format("/v2/%s/manifests/%s", dockerImageName, dockerImageTag);
		String uri = RestApiUtils.makeUri(dataServer.getPrvIp(), dockerRegistryPort, apiType);

		log.info(">>>>> docker api uri:{}", uri);

		ResponseEntity responseEntity = null;

		try {
			AipHttpHeaders httpHeaders = new AipHttpHeaders();
			httpHeaders.setAcceptType(new MediaType("application", "vnd.docker.distribution.manifest.v2+json"));

			responseEntity = (ResponseEntity) RestApiUtils.callApiAll(uri, null, HttpMethod.GET, httpHeaders);

		} catch (Exception e) {
			log.error(">>>>> get docker image id error", e);
			throw new CustomException(ErrorCodeType.DOCKER_API_EXCEPTION);
		}

		return responseEntity.getHeaders().getValuesAsList("Docker-Content-Digest").get(0);
	}

	/**
	 * docker api를 통해서
	 * 레지스트리에 있는 이미지를 삭제한다.
	 *
	 * @param dockerImageName   도커이미지이름
	 * @param dockerImageDigest 도커이미지아이디
	 * @author eunbeelee
	 * @date 2020.04.08
	 */
	private void deleteRegistryImageByDockerApi(String dockerImageName, String dockerImageDigest) {
		log.debug(">>>>> delete registry image by docker api start");

		AipServer dataServer = helper.findFirstServerBy(ServerType.DATA);

		String apiType = String.format("/v2/%s/manifests/%s", dockerImageName, dockerImageDigest);

		String uri = RestApiUtils.makeUri(dataServer.getPrvIp(), dockerRegistryPort, apiType);

		log.info(">>>>> delete docker image api uri:{}", uri);

		try {
			AipHttpHeaders httpHeaders = new AipHttpHeaders();
			httpHeaders.setAcceptType(new MediaType("application", "vnd.docker.distribution.manifest.v2+json"));

			RestApiUtils.callApiAll(uri, null, HttpMethod.DELETE, httpHeaders);

		} catch (Exception e) {
			log.error(">>>>> delete registry image by docker api error", e);
			throw new CustomException(ErrorCodeType.DOCKER_API_EXCEPTION);
		}
	}

	/**
	 * 마운트된 폴더에서
	 * 도커레지스트리이미지를 물리적으로 삭제한다.
	 *
	 * @param dockerImageName 도커이미지이름
	 * @author eunbeelee
	 * @date 2020.04.08
	 */
	private void deleteRegistryImageOnMountedDirectory(String dockerImageName, String dockerImageDigest) {
		log.debug(">>>>> delete registry image on mounted directory start");

		Path dockerImagePath = Paths.get(dockerRegistryPath, dockerImageName);
		log.debug(">>>>> docker image path :{}", dockerImagePath);

		if (!dockerImagePath.toFile().exists()) {
			log.error(">>>>> docker image path not found");
			throw new CustomException(ErrorCodeType.FILE_NOT_FOUND);
		}
		Path digestPath = Paths.get(dockerImagePath.toString(), "_manifests/revisions/sha256");
		log.info(">>>>> digest path:{}", digestPath);
		File[] digestsList = digestPath.toFile().listFiles();

		File deleteTarget = null;
		if (digestsList.length == 1) {
			deleteTarget = dockerImagePath.toFile();
		}
		if (digestsList.length > 1) {
			deleteTarget = Paths.get(digestPath.toString(), dockerImageDigest).toFile();
		}
		try {
			FileUtils.deleteDirectory(deleteTarget);

		} catch (Exception e) {
			log.error(">>>>> delete registry image on mounted directory error", e);
			throw new CustomException(ErrorCodeType.FILE_DELETE_ERROR);
		}


	}

	/**
	 * 요청받은 도커이미지 파일을 저장한다.
	 *
	 * @param file 도커이미지파일
	 * @author eunbeelee
	 * @date 2020.04.16
	 */
	private String uploadDockerImageFile(MultipartFile file) {
		log.debug(">>>>> upload docker image file start");

		if (ObjectUtils.isEmpty(file)) {
			throw new CustomException(ErrorCodeType.FILE_NOT_FOUND);
		}

		try {
			FileUtils.uploadFile(file, Paths.get(dockerImageCustomTarPath));
		} catch (Exception e) {
			log.error(">>>>> upload docker file error", e);
			throw e;
		}

		try {
			String repoTag = this.getDockerImageRepoTag(file.getOriginalFilename());
			if (StringUtils.isEmpty(repoTag)) {
				throw new CustomException(ErrorCodeType.FILE_PROCESS_HANDLE);
			}
			return repoTag;

		} catch (Exception e) {
			log.error(">>>>> get docker image repoTag error", e);
			throw new CustomException(ErrorCodeType.DOCKER_IMAGE_INVALID_ERROR);
		}
	}

	/**
	 * 파일을 압축 해제하고
	 * 내부의 manifest.json 파일 정보를 조회 한후
	 * repoTag 정보를 추출한다.
	 *
	 * @param originalFilename 파일 이름(확장자명 포함)
	 * @return 도커이미지 repoTag
	 */
	private String getDockerImageRepoTag(String originalFilename) {
		String sourceFile = Paths.get(dockerImageCustomTarPath, originalFilename).toString();
		String fileName = originalFilename.replace(".tar", "");
		String target = Paths.get(dockerImageCustomTarPath, fileName).toString();


		log.info(sourceFile);
		if (!new File(sourceFile).exists()) {
			log.error(">>>>>" + ErrorCodeType.FILE_NOT_FOUND.getMessage());
			throw new CustomException(ErrorCodeType.FILE_NOT_FOUND);
		}
		try {
			compressUtils.decompress(sourceFile, target);
		} catch (IOException e) {
			log.error(">>>>> file decompress error", e);
			throw new CustomException(ErrorCodeType.FILE_PROCESS_HANDLE);
		}

		File manifestFile = Paths.get(dockerImageCustomTarPath, fileName, "manifest.json").toFile();
		try (BufferedReader fileReader = new BufferedReader(new FileReader(manifestFile))) {
			String line = "";
			while ((line = fileReader.readLine()) != null) {
				String[] infoElements = line.split(",");
				for (String element : infoElements) {
					if (element.contains("RepoTags")) {
						String repoTag = element.substring(element.indexOf("[") + 1, element.indexOf("]")).replaceAll("\"", "");
						FileUtils.deleteDirectory(new File(target));
						return repoTag;
					}
				}
			}
		} catch (Exception e) {
			log.error(">>>>> get docker image tag error", e);
//            FileUtils.deleteDirectory(new File(target));
			throw new CustomException(ErrorCodeType.DOCKER_IMAGE_INVALID_ERROR);
		}
		return null;
	}

	/**
	 * 도커이미지이름으로 저장된 tar 파일을 사용하여
	 * 도커이미지를 만든다
	 *
	 * @param dockerImageName 도커이미지이름
	 * @author eunbeelee
	 * @date 2020.04.10
	 */
	private void loadDockerImage(String dockerImageName) {
		log.debug(">>>>> load docker image start");
		dockerUtils.loadImage(dockerImageName);
		log.debug(">>>>> load docker image end");
	}

	/**
	 * 도커이미지 tag를 하여
	 * 이미지 복사를 한다.
	 *
	 * @param dockerImageName 도커이미지이름
	 * @param dockerImageTag  도커이미지태그
	 */
	private void tagDockerImage(String loadedImageInfo, String dockerImageName, String dockerImageTag) {
		log.debug(">>>>> tag docker image start");

		try {
			dockerUtils.tagImage(loadedImageInfo, dockerImageName, dockerImageTag);
			log.debug(">>>>> tag docker image end");

		} catch (Exception e) {
			log.error(">>>>> tag docker image error", e);
//            dockerUtils.deleteImage(dockerImageName, dockerImageTag);

			throw new CustomException(ErrorCodeType.DOCKER_API_EXCEPTION);
		}
	}

	/**
	 * 도커이미지를 레지스트리에 등록한다.
	 *
	 * @param dockerImageName 도커이미지이름
	 * @param dockerImageTag  도커이미지태그
	 * @author eunbeelee
	 * @date 2020.04.10
	 */
	private void pushDockerImageToRegistry(String dockerImageName, String dockerImageTag) {
		log.debug(">>>>> push docker image start");

		try {
			dockerUtils.pushImage(dockerImageName, dockerImageTag);

		} catch (Exception e) {
			log.error(">>>>> push docker image error", e);
			dockerUtils.deleteImage(this.getRegistryImageName(dockerImageName), dockerImageTag);

			throw new CustomException(ErrorCodeType.DOCKER_API_EXCEPTION);
		}
	}

	/**
	 * 도커 이미지이름과 태그에 해당하는 도커이미지를 삭제한다.
	 *
	 * @param dockerImageName
	 * @param dockerImageTag
	 * @author eunbeelee
	 * @date 2020.04.16
	 */
	private void deleteDockerImage(String dockerImageName, String dockerImageTag) {
		log.debug(">>>>> delete docker image start");

		try {
			dockerUtils.deleteImage(dockerImageName, dockerImageTag);

		} catch (Exception e) {
			log.error(">>>>> delete docker image error", e);
			throw new CustomException(ErrorCodeType.DOCKER_API_EXCEPTION);
		}
	}

	/**
	 * 등록한 도커이미지를 데이터베이스에 적재한다.
	 *
	 * @param req 도커이미지등록 객체
	 * @author eunbeelee
	 * @date 2020.04.16
	 */
	private int insertDockerImageInfo(AipUser user, AipDockerImageCreateReq req, String imageId) {
		log.debug(">>>>> insert docker image start");
		log.debug(">>>>> docker image create req{}", req.toString());

		AipDockerImage aipDockerImage = req.toDao();

		aipDockerImage.setImgId(imageId);
		aipDockerImage.setImgTp(ImageType.CUSTOM.getValue());
		aipDockerImage.setRegId(user.getUserId());
		aipDockerImage.setDelFl(FlagType.FALSE.getValue());
		Date now = DateUtils.getNow();
		aipDockerImage.setRegDt(now);
		aipDockerImage.setModDt(now);

		try {
			return aipDockerImageMapper.insertSelective(aipDockerImage);

		} catch (Exception e) {
			log.error(">>>>> insert docker image info error", e);

			String dockerImageName = req.getDckrImgNm();
			String dockerImageTag = req.getDckrImgTag();

			String dockerImageId = this.getDockerImageId(dockerImageName, dockerImageTag);

			this.deleteRegistryImageByDockerApi(dockerImageName, dockerImageId);

			this.deleteRegistryImageOnMountedDirectory(dockerImageName, dockerImageId);

			throw new CustomException(ErrorCodeType.DB_INSERT_HANDLE);
		}
	}

	/**
	 * 기존에 docker registry에서
	 * worker server로 pull 받은
	 * 도커이미지를 삭제한다.
	 * <p>
	 * 모든 worker server 애서
	 * 삭제 예정인 도커이미지의 유무를 확인하고 있을 경우 삭제한다.
	 * - work server api 호출
	 *
	 * @param dockerImageId   도커이미지아이디
	 * @param dockerImageName 도커이미지이름
	 * @param dockerImageTag  도커이미지태그
	 * @author eunbeelee
	 * @date 2020-04-14
	 */
	private void deletePulledImageOnWorkerServer(String dockerImageId, String dockerImageName, String dockerImageTag) {
		log.debug(">>>>> delete pulled image on worker server start");

		if (this.existInUseWorkspaceByImage(dockerImageId)) {
			throw new CustomException(ErrorCodeType.WORKSPACE_EXIST);
		}

		AipDockerImageDeleteReq aipDockerImageDeleteReq = new AipDockerImageDeleteReq();
		aipDockerImageDeleteReq.setDockerImageName(dockerImageName);
		aipDockerImageDeleteReq.setDockerImageTag(dockerImageTag);

		try {
			List<AipServer> workers = helper.findServersBy(ServerType.WORKER);
			if (!ObjectUtils.isEmpty(workers)) {
				ExecutorService executor = Executors.newFixedThreadPool(workers.size());
				workers.forEach(worker -> {
					RestApiUtils.callApiRunnableThread(
							RestApiUtils.makeUri(worker.getPrvIp(), worker.getPort(), AgentApiType.WORKER_DOCKER_IMAGE_DELETE),
							aipDockerImageDeleteReq,
							HttpMethod.POST,
							null,
							executor
					);
				});
				if (executor.awaitTermination(10, TimeUnit.MINUTES)) {
					log.info("( deletePulledImageOnWorkerServer#awaitTermination ) done !! $docker.image.id={}, $docker.image.name={}, $docker.image.tag={}", dockerImageId, dockerImageName, dockerImageTag);
				} else {
					log.warn("( deletePulledImageOnWorkerServer#awaitTermination ) timeout !! $docker.image.id={}, $docker.image.name={}, $docker.image.tag={}", dockerImageId, dockerImageName, dockerImageTag);
				}
				executor.shutdown();
			}
		} catch (Exception e) {
			log.error(">>>>> delete pulled image on worker server error:", e);
			throw new CustomException(ErrorCodeType.API_CALL_ERROR_WRONG);
		}
	}

	/**
	 * 도커이미지아이디에 해당하는
	 * 워크스페이스가 존재하는지 조회한다
	 * 존재한다면 true, 존재하지 않는다면 false를 리턴한다.
	 *
	 * @param dockerImageId 도커이미지아이디
	 * @return 조건에 해당하는 워크스페이스 존재 여부
	 * @author eunbeelee
	 * @date 2020-04-29
	 */
	private boolean existInUseWorkspaceByImage(String dockerImageId) {
		AipWorkspaceExample workspaceExample = new AipWorkspaceExample();

		workspaceExample.createCriteria()
				.andImgIdEqualTo(dockerImageId)
				.andDelFlEqualTo(FlagType.FALSE.getValue());

		long workspaceCount = aipWorkspaceMapper.countByExample(workspaceExample);

		return workspaceCount > 0;
	}

	/**
	 * 삭제된 도커이미지
	 * 데이터베이스 DEL_FL 업데이트
	 *
	 * @param dockerImageName 도커이미지이름
	 * @param dockerImageTag  도커이미지태그
	 * @author eunbeelee
	 * @date 2020.04.16
	 */
	private int deleteDockerImageInfo(String dockerImageName, String dockerImageTag) {
		log.debug(">>>>> delete docker image info start");

		AipDockerImageExample aipDockerImageExample = new AipDockerImageExample();
		aipDockerImageExample.createCriteria().andDckrImgNmEqualTo(dockerImageName).andDckrImgTagEqualTo(dockerImageTag);

		AipDockerImage record = new AipDockerImage();
		record.setDelFl(FlagType.TRUE.getValue());
		record.setModDt(DateUtils.getNow());

		try {
			return aipDockerImageMapper.updateByExampleSelective(record, aipDockerImageExample);

		} catch (Exception e) {
			log.error(">>>>> docker image update error", e);
			throw new CustomException(ErrorCodeType.DB_UPDATE_HANDLE);
		}
	}

	/**
	 * 도커이미지 아이디로
	 * 데이터베이스 상의 도커이미지를 조회한다.
	 *
	 * @param dockerImageId 도커이미지아이디
	 * @return 도커이미지
	 * @author eunbeelee
	 * @date 2020.04.16
	 */
	private AipDockerImage read(String dockerImageId) {
		AipDockerImageKey key = new AipDockerImageKey();
		key.setImgId(dockerImageId);

		return aipDockerImageMapper.selectByPrimaryKey(key);
	}

	/**
	 * 이미지아이디를 사용하여 도커이미지 키를 리턴한다.
	 *
	 * @param dockerImageId
	 * @return 도커이미지키
	 * @author eunbeelee
	 * @date 2020.04.16
	 */
	private AipDockerImageKey getKey(String dockerImageId) {
		AipDockerImageKey key = new AipDockerImageKey();
		key.setImgId(dockerImageId);
		return key;
	}

	/**
	 * 도커이미지이름 앞에 도커레지스트리 정보를 붙인다.
	 * ex) 192.342.43.15:3666/{dockerImageName}
	 *
	 * @param dockerImageName 도커이미지이름
	 * @return 도커레지스트리 정보가 포함된 문자열
	 * @author eunbeelee
	 * @date 2020.04.17
	 */
	private String getRegistryImageName(String dockerImageName) {
		AipServer dataServer = helper.findFirstServerBy(ServerType.DATA);
		return String.format("%s:%s/%s", dataServer.getPrvIp(), dockerRegistryPort, dockerImageName);
	}
}

