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
public class GitlabServiceImpl implements GitlabService {

	@Value("${data.gitlab.url}")
	private String gitLabUrl;

	@Value("${admin.gitlab.token}")
	private String adminGitlabToken;


	@Override
	public Project createRepository(GitlabCreateRepositoryReq req) {
		GitLabApi gitlabApi = new GitLabApi(gitLabUrl, req.getAccessToken());

		try {
			Project projectSpec = new Project()
					.withName(req.getRepositoryName())
					.withIssuesEnabled(true)
					.withMergeRequestsEnabled(true)
					.withWikiEnabled(true)
					.withSnippetsEnabled(true)
					.withPublic(true);

			return gitlabApi.getProjectApi().createProject(projectSpec);
		} catch (GitLabApiException e) {
			log.error(">>>>> create gitlab repository error", e);
			throw new CustomException(ErrorCodeType.GITLAB_CREATE_REPOSITORY_ERROR);
		}
	}

	public GitLabApi createGitlabApiWithAdminToken() {
		return new GitLabApi(gitLabUrl, adminGitlabToken);
	}

	/**
	 * 소유주와 프로젝트 이름으로 프로젝트 정보를 얻는다.
	 *
	 * @param namespace 소유주 이름
	 * @param project 프로젝트 이름
	 * @return 프로젝트 정보
	 */
	@Override
	public Project getProject(String namespace, String project) {
		GitLabApi gitlabApi = this.createGitlabApiWithAdminToken();
		try {
			return gitlabApi.getProjectApi().getProject(namespace, project);
		} catch (GitLabApiException e) {
			log.error(">>>>> get git project error", e);
			throw new CustomException(ErrorCodeType.GITLAB_GET_PROJECT_FAILED);
		}
	}

	@Override
	public void deleteProjectById(String gitToken, Integer projectId) {
		GitLabApi gitlabApi = new GitLabApi(gitLabUrl, gitToken);
		try {
			gitlabApi.getProjectApi().deleteProject(projectId);
		} catch (GitLabApiException e) {
			log.error(">>>>> delete gitlab project error", e);
			throw new CustomException(ErrorCodeType.GITLAB_DELETE_PROJECT_FAILED);
		}
	}
}
