package com.nomad.data.agent.common.service.algorithm;

import java.io.File;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import org.gitlab4j.api.models.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.nomad.data.agent.common.dto.req.AipAlgorithmUploadReq;
import com.nomad.data.agent.common.dto.req.GitCommitAndPushReq;
import com.nomad.data.agent.common.dto.req.GitlabCreateRepositoryReq;
import com.nomad.data.agent.common.service.git.GitService;
import com.nomad.data.agent.common.service.gitlab.GitlabService;
import com.nomad.data.agent.config.exception.CustomException;
import com.nomad.data.agent.domain.dao.common.AipAlgorithm;
import com.nomad.data.agent.domain.dao.common.AipAlgorithmExample;
import com.nomad.data.agent.domain.dao.common.AipUser;
import com.nomad.data.agent.domain.mappers.common.AipAlgorithmMapper;
import com.nomad.data.agent.utils.CompressUtils;
import com.nomad.data.agent.utils.DateUtils;
import com.nomad.data.agent.utils.FileUtils;
import com.nomad.data.agent.utils.enums.AlgorithmType;
import com.nomad.data.agent.utils.enums.ErrorCodeType;
import com.nomad.data.agent.utils.enums.FlagType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AlgorithmServiceImpl implements AlgorithmService {
    
	@Value("${algorithm.upload.path}")
	String algorithmUploadPath;
	
	@Value("${algorithm.upload.temp.path}")
	String algorithmUploadTempPath;
	
	@Value("${admin.gitlab.token}")
	String adminGitlabToken;
	
	@Value("${data.gitlab.url}")
	String gitlabUrl;
	
	@Value("${admin.algorithm.email}")
	String adminGitEmail;
	
	@Value("${admin.algorithm.username}")
	String adminGitUsername;
	
	@Value("${admin.algorithm.password}")
	String adminGitPassword;
	
	@Autowired
	CompressUtils compressUtils;
	
	@Autowired
	GitlabService gitlabService;
	
	@Autowired
	GitService gitService;
	
	@Autowired
	AipAlgorithmMapper algorithmMapper;
	
	@Autowired
	PlatformTransactionManager transactionManager;
	
	private final String gitlabAdmin = "bap_admin";
	
	@Override
	public String upload(AipUser user, AipAlgorithmUploadReq req, MultipartFile algorithmUploadFile) {

		// 1. ???????????? ?????????????????? ???????????? (.tar, .zip ????????? ??????)
		String algorithmFileTempPath = "";
		try {
			algorithmFileTempPath = this.saveAlgorithmTempPath(algorithmUploadFile);
		}catch(Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		
		// 2. ?????? ???????????? ?????? ????????? ??????(?????? ???????????? ????????? ??????)
		this.checkValidationAlgorithm(req);
		
		TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
		AipAlgorithm newAlgorithm = new AipAlgorithm();
		Project project = null;
		
		try {
			// 3. ???????????? ?????????????????? ?????? ???????????? ??????
			String extensionSeoarator = ".";
			Integer pos = algorithmFileTempPath.lastIndexOf(extensionSeoarator);
			String fileExtension = algorithmFileTempPath.substring(pos + 1, algorithmFileTempPath.length());
			
			if(fileExtension.equals("tar")) {
				compressUtils.decompress(algorithmFileTempPath, algorithmUploadTempPath);
			}else if(fileExtension.equals("zip")) {
				compressUtils.unzip(algorithmFileTempPath, algorithmUploadTempPath);
			}else {
				throw new CustomException(ErrorCodeType.FILE_INVALID_TYPE);
			}
			
			FileUtils.deleteFile(algorithmFileTempPath);
			
			// ??????????????? ??????????????? ?????? ?????? ??? ?????? ????????? ??????
			String renameAlgorithm = this.moveAlgorithmFromTempPath(algorithmUploadTempPath, algorithmUploadPath);
			
			// 4. ???????????? ?????? ?????? ????????? ????????????(bap_admin)
			project = this.createAlgorithmGitRepository(renameAlgorithm);
			
			// 5. ?????????????????? ?????? ??? ?????? ????????????
			this.commitAndPushAlgorithm(project);
			
			// 6. DB??? ????????????
			Date now = DateUtils.getNow();
			
			newAlgorithm.setAthmId(DateUtils.getSystemId());
			newAlgorithm.setAthmNm(req.getAthmNm());
			newAlgorithm.setAthmTp(AlgorithmType.BASE.getValue());
			newAlgorithm.setAthmDesc(req.getAthmDesc());
			newAlgorithm.setRptrNm(project.getPathWithNamespace());
			newAlgorithm.setLibTp(req.getLibTp());
			newAlgorithm.setOwnerId(user.getUserId());
			newAlgorithm.setPubFl(FlagType.TRUE.getValue());
			newAlgorithm.setRegDt(now);
			newAlgorithm.setModDt(now);
			
			algorithmMapper.insert(newAlgorithm);
			
			transactionManager.commit(transactionStatus);

		}catch(Exception e) {
			log.error(e.getMessage(), e);
			transactionManager.rollback(transactionStatus);
			
			if(!ObjectUtils.isEmpty(project)) {
				gitlabService.deleteProjectById(adminGitlabToken, project.getId());
			}
			
			FileUtils.deleteDirectory(Paths.get(algorithmUploadPath, req.getAthmNm()).toFile());
			
			throw new CustomException(ErrorCodeType.ALGORITHM_UPLOAD_ERROR);
		}
		return newAlgorithm.getAthmId();
	}

	/**
	 * ?????? ????????? ???????????? ???????????? ?????????
	 * ?????? ????????? ????????????.
	 * ????????? ????????? ???????????? ?????? ?????? ????????? ????????????.
	 *  
	 * @param source ????????????
	 * @param target ????????????
	 * @return
	 */
	private String moveAlgorithmFromTempPath(String source, String target) {
		File sourceAlgorithmPath = Paths.get(source).toFile();
		File[] sourceAlgorithm = sourceAlgorithmPath.listFiles();
		File renameAlgorithmFile = null;
		
		if (sourceAlgorithm.length > 0) {
			for (File file: sourceAlgorithm) {
				if (file.isDirectory()) {
					renameAlgorithmFile = new File(file.getAbsolutePath() + "-" + DateUtils.getSystemId().substring(10));
					FileUtils.copyDirectory(file, Paths.get(algorithmUploadPath, renameAlgorithmFile.getName()).toFile());
				}
			}
		}
		
		FileUtils.cleanDirectory(new File(algorithmUploadTempPath));
		return renameAlgorithmFile.getName();
	}
	
	
	/**
	 * ???????????? ?????? ???????????? ????????????.
	 * 
	 * @param req ????????? ???????????? ??????
	 */
	private void checkValidationAlgorithm(AipAlgorithmUploadReq req) {
		AipAlgorithmExample algorithmExample = new AipAlgorithmExample();
		algorithmExample.createCriteria().andAthmTpEqualTo(AlgorithmType.BASE.getValue()).andAthmNmEqualTo(req.getAthmNm());
		List<AipAlgorithm> algorithmList = algorithmMapper.selectByExample(algorithmExample);
		if(algorithmList.size() > 0) {
			throw new CustomException(ErrorCodeType.COMMON_DUPLICATED_NAME);
		}
	}

	
	/**
	 * ??????????????? ????????? ?????? ???????????? ????????????.
	 * ???????????? ?????? ?????? ????????? commit ??? ???, push ??????.
	 * 
	 * @param project ?????? ????????? ??????
	 */
	private void commitAndPushAlgorithm(Project project) {
		
		GitCommitAndPushReq req = new GitCommitAndPushReq();
		req.setRemoteUri(gitlabUrl + project.getPathWithNamespace() + ".git");
		
		try {
			req.setLocalPath(Paths.get(algorithmUploadPath, project.getName()).toString());
		}catch(Exception e) {
			log.error(e.getMessage(), e);
			throw new CustomException(ErrorCodeType.FILE_NOT_FOUND);
		}
		
		req.setGitUsername(adminGitUsername);
		req.setGitEmail(adminGitEmail);
		req.setGitPassword(adminGitPassword);
		req.setMessage(project.getName());
		
		try {
			gitService.commitAndPush(req);
		}catch(Exception e) {
			log.error(e.getMessage(), e);
			throw new CustomException(ErrorCodeType.GIT_PUSH_ERROR);
		}
	}
	
	/**
	 * ???????????? ?????? ?????? ????????????
	 * ????????? ???????????? ????????????.
	 * 
	 * @param algorithmName ???????????? ????????? ??????
	 * @return ????????? ?????????
	 */
	private Project createAlgorithmGitRepository(String algorithmName) {
		GitlabCreateRepositoryReq req = new GitlabCreateRepositoryReq();
		
		req.setRepositoryName(algorithmName);
		req.setAccessToken(adminGitlabToken);
		
		return gitlabService.createRepository(req);
	}
	
	private String saveAlgorithmTempPath(MultipartFile algorithmCompressedFile) {
		String fileNameWithExtension = algorithmCompressedFile.getOriginalFilename();
		if (!fileNameWithExtension.contains(".tar") && !fileNameWithExtension.contains(".zip")) {
			throw new CustomException(ErrorCodeType.FILE_UNSUPPORTED_TYPE.getMessage() + " tar ???????????? zip ????????? ?????? ???????????????.",
					ErrorCodeType.FILE_UNSUPPORTED_TYPE);
		}
		
		File uploaTempPath = Paths.get(algorithmUploadTempPath).toFile();
		
		if(!uploaTempPath.exists()) {
			FileUtils.mkdir(uploaTempPath);
		}
		
		if(uploaTempPath.listFiles().length > 0) {
			FileUtils.cleanDirectory(uploaTempPath);
		}
		
		// ?????? ????????? ????????????.
		FileUtils.uploadFile(algorithmCompressedFile, Paths.get(algorithmUploadTempPath));
		
		String compressedFilePath = Paths.get(algorithmUploadTempPath, algorithmCompressedFile.getOriginalFilename()).toString();
		log.debug(">>>>> compressedFilePath: {}", compressedFilePath);
		
		return compressedFilePath;
	}
}
