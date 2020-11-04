package com.nomad.data.agent.common.service.git;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Service;

import com.nomad.data.agent.common.dto.req.GitCommitAndPushReq;
import com.nomad.data.agent.config.exception.CustomException;
import com.nomad.data.agent.utils.FileUtils;
import com.nomad.data.agent.utils.enums.ErrorCodeType;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GitServiceImpl implements GitService {
	
	@Override
	public void commitAndPush(GitCommitAndPushReq req) throws Exception {
		try {
			this.initAndSetOrigin(req.getLocalPath(), req.getRemoteUri());
			
			this.add(req.getLocalPath(), ".");
			
			this.commit(req.getLocalPath(), req.getGitUsername(), req.getGitEmail(), req.getMessage());
			
			this.push(req.getLocalPath(), req.getGitUsername(), req.getGitPassword());
		}catch(Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private void push(String localPath, String gitUsername, String gitPassword) {
		log.info(">>>>> git push start");
		
		try {
			Git.open(new File(localPath))
				.push()
				.setCredentialsProvider(new UsernamePasswordCredentialsProvider(gitUsername, gitPassword))
				.call();
		}catch(GitAPIException ge) {
			log.error(ge.getMessage(), ge);
			throw new CustomException(ErrorCodeType.GIT_PUSH_ERROR);
		}catch(IOException ioe) {
			log.error(ioe.getMessage(), ioe);
			throw new CustomException(ErrorCodeType.FILE_NOT_FOUND);
		}
		
	}

	private void commit(String localPath, String gitUsername, String gitEmail, String message) {
		log.info(">>>>> git commit start");
		
		try {
			Git.open(new File(localPath))
				.commit()
				.setAuthor(gitUsername, gitEmail)
				.setMessage(message)
				.call();
		}catch(GitAPIException ge) {
			log.error(ge.getMessage(), ge);
			throw new CustomException(ErrorCodeType.GIT_ADD_ERROR);
		}catch(IOException ioe) {
			log.error(ioe.getMessage(), ioe);
			throw new CustomException(ErrorCodeType.FILE_NOT_FOUND);
		}
	}

	private void add(String localPath, String filePattern) {
		log.info(">>>>> git add start");
		
		try {
			Git.open(new File(localPath))
				.add()
				.addFilepattern(filePattern)
				.call();
		}catch(GitAPIException ge) {
			log.error(ge.getMessage(), ge);
			throw new CustomException(ErrorCodeType.GIT_ADD_ERROR);
		}catch(IOException ioe) {
			log.error(ioe.getMessage(), ioe);
			throw new CustomException(ErrorCodeType.FILE_NOT_FOUND);
		}
	}

	private void initAndSetOrigin(String localPath, String remoteUri) {
		log.info(">>>>> git init and set origin start");
		
		log.info(">>>>> localPath: {}", localPath);
		log.info(">>>>> remoteUri: {}", remoteUri);
		
		try {
			File[] fileList = Paths.get(localPath).toFile().listFiles();
			
			for(File file : fileList) {
				if (file.isDirectory() && file.getName().equals(".git")) {
					FileUtils.deleteDirectory(file);
				}
			}
			
			Git git = Git.init()
						.setDirectory(new File(localPath))
						.call();
			
			git.remoteAdd()
				.setName("origin")
				.setUri(new URIish(remoteUri))
				.call();
			
		}catch (GitAPIException | URISyntaxException e) {
			log.error(e.getMessage(), e);
			throw new CustomException(ErrorCodeType.GIT_INIT_ERROR);
		}
	}

}
