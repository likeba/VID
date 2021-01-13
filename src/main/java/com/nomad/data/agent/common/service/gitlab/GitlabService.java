package com.nomad.data.agent.common.service.gitlab;

import org.gitlab4j.api.models.Project;

import com.nomad.data.agent.common.dto.req.GitlabCreateRepositoryReq;

public interface GitlabService {
	
	Project createRepository(GitlabCreateRepositoryReq req);
	
	Project getProject(String namespace, String project);
	
	void deleteProjectById(String gitToken, Integer projectId);
	
}
