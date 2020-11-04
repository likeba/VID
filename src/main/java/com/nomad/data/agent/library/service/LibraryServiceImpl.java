package com.nomad.data.agent.library.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.nomad.data.agent.config.exception.CustomException;
import com.nomad.data.agent.library.dto.req.LibraryDeleteReq;
import com.nomad.data.agent.utils.CommandUtils;
import com.nomad.data.agent.utils.CompressUtils;
import com.nomad.data.agent.utils.enums.ErrorCodeType;
import com.nomad.data.agent.utils.enums.LibraryType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LibraryServiceImpl implements LibraryService{

	@Autowired
	CompressUtils compressUtils;
	
	@Autowired
	CommandUtils commandUtils;
	
	@Value("${api.lib-update-agent-transfer}")
	private String libUpdateAgentTransferAPI;
	
	@Value("${path.lib-transfer-path}")
	private String libTransferPath;
	
	@Value("${path.python-lib-path}")
	private String pythonLibPath;
	
	@Value("${path.r-lib-path}")
	private String rLibPath;
	
	@Value("${script.r-lib-script}")
	private String rLibScript;
	
	@Override
//	@Async("threadPoolTaskExecutor")
	public CompletableFuture<String> applyLibraryFile(String libraryFilePath) throws IOException {

		
		// 패키지 업데이트 서버로 API 호출(param: pkgFileNm) 및 파일 다운로드(다운로드 경로: "../../../../???")
		UriComponentsBuilder libUpdateAgentTransferURI = UriComponentsBuilder.fromHttpUrl(libUpdateAgentTransferAPI)
				.queryParam("libFilePath", libraryFilePath);
		
		RestTemplate template = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		
		HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<Resource> libraryFileResource = template.exchange(libUpdateAgentTransferURI.toUriString(), HttpMethod.GET,requestEntity, Resource.class);
		
		log.debug(">>>>> libraryFile: {}", libraryFileResource.getBody().getFilename());
		
		String pullLibraryFile = libTransferPath + File.separator + libraryFileResource.getBody().getFilename();
		File libraryFile = new File(pullLibraryFile);
		
		try (InputStream is = libraryFileResource.getBody().getInputStream();
				OutputStream os = new FileOutputStream(libraryFile)) {
			byte[]  buf = new byte[1024];
			int len = 0;
			
			while((len = is.read(buf)) > 0) {
				os.write(buf, 0, len);
			}
		}catch(Exception e) {
			log.error(">>>>> apply library file error", e);
			throw new CustomException(ErrorCodeType.FILE_PROCESS_HANDLE);
		}
		
		// 후처리
		if(libraryFilePath.contains("python")) {
			
			// 파이선은 해당 폴더(../../../pypi-server/packages)로 패키지 파일을 복사하면 끝.
			log.info("[PYTHON] >>>>> decompress start! ");
			compressUtils.decompress(pullLibraryFile, pythonLibPath);
			log.info("[PYTHON] >>>>> decompress end! ");
			
		}else {
			
			// 1. R은 해당 폴더(../../cran-server/packages/src/contrib)로 패키지 파일 복사 후
			log.info("[R] >>>>>  decompress start! ");
			compressUtils.decompress(pullLibraryFile, rLibPath);
			log.info("[R] >>>>> decompress end! ");

			// 2. cran-server/update_PACKAGE.sh 파일 실행
			log.info("[R] >>>>> Script start! ");
			List<String> archCmd = Arrays.asList(rLibScript);
			commandUtils.run(archCmd);
			log.info("[R] >>>>> Script end! ");
			
		}
		
		// 전송받은 파일 삭제
		log.info(">>>>> pull file deleteing...{} ", pullLibraryFile);
		Paths.get(pullLibraryFile).toFile().delete();
		log.info(">>>>> pull file deleted... ");
		
		return CompletableFuture.completedFuture("SUCCESS");
	}
	
	@Override
	public void deleteLibrary(@NotEmpty LibraryDeleteReq req) {
		if(req.getLibTp().equals(LibraryType.PYTHON.getValue())) {
			this.deleteLibraryForPython(req.getPkgFlNm());
		}else {
			this.deleteLibraryForR(req.getPkgFlNm());
		}
	}

	private void deleteLibraryForPython(String pkgFlNm) {
		if(Paths.get(pythonLibPath).resolve(pkgFlNm).toFile().exists()) {
			Paths.get(pythonLibPath).resolve(pkgFlNm).toFile().delete();
			log.info(">>>>> Delete Python Package File: {}", pkgFlNm);
		}
	}

	private void deleteLibraryForR(String pkgFlNm) {
		if(Paths.get(rLibPath).resolve(pkgFlNm).toFile().exists()) {
			Paths.get(rLibPath).resolve(pkgFlNm).toFile().delete();
			log.info(">>>>> Delete R Package File: {}", pkgFlNm);
		}
		List<String> archCmd = Arrays.asList(rLibScript);
		commandUtils.run(archCmd);
	}
}
