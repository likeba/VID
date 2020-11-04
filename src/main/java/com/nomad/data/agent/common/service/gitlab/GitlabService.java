package com.nomad.data.agent.common.service.gitlab;

import org.gitlab4j.api.models.Project;

import com.nomad.data.agent.common.dto.req.GitlabCreateRepositoryReq;

public interface GitlabService {
	
	public Project createRepository(GitlabCreateRepositoryReq req);
	
	public Project getProject(String namespace, String project);
	
	public void deleteProjectById(String gitToken, Integer projectId);
	
}
