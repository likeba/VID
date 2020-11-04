package com.nomad.data.agent.common.service.gitlab;

import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Project;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nomad.data.agent.common.dto.req.GitlabCreateRepositoryReq;
import com.nomad.data.agent.config.exception.CustomException;
import com.nomad.data.agent.utils.enums.ErrorCodeType;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GitlabServiceImpl implements GitlabService{

	@Value("${data.gitlab.url}")
	private String gitlabUrl;
	
	@Value("${admin.gitlab.token}")
	private String adminGitlabToken;
	
	
	@Override
	public Project createRepository(GitlabCreateRepositoryReq req) {
		GitLabApi gitlabApi = new GitLabApi(gitlabUrl, req.getAccessToken());
		
		try {
			Project projectSpec = new Project()
					.withName(req.getRepositoryName())
					.withIssuesEnabled(true)
					.withMergeRequestsEnabled(true)
					.withWikiEnabled(true)
					.withSnippetsEnabled(true)
					.withPublic(true);
			
			return gitlabApi.getProjectApi().createProject(projectSpec);
			
		}catch(GitLabApiException ge) {
			log.error(ge.getMessage(), ge);
			throw new CustomException(ErrorCodeType.GITLAB_CREATE_REPOSITORY_ERROR);
		}
	}
	
	@Override
	public Project getProject(String namespace, String project) {
		GitLabApi gitlabApi = this.createGitlabApiWithAdminToken();
		try {
			return gitlabApi.getProjectApi().getProject(namespace, project);
		}catch(GitLabApiException ge) {
			log.error(ge.getMessage(), ge);
			throw new CustomException(ErrorCodeType.GITLAB_GET_PROJECT_FAILED);
		}
	}

	private GitLabApi createGitlabApiWithAdminToken() {
		return new GitLabApi(gitlabUrl, adminGitlabToken);
	}
	
	@Override
	public void deleteProjectById(String gitToken, Integer projectId) {
		GitLabApi gitlabApi = new GitLabApi(gitlabUrl, gitToken);
		try {
			gitlabApi.getProjectApi().deleteProject(projectId);
		}catch(GitLabApiException ge) {
			log.error(ge.getMessage(), ge);
			throw new CustomException(ErrorCodeType.GITLAB_DELETE_PROJECT_FAILED);
		}
	}
}
