package com.nomad.data.agent.common.service.git;

import com.nomad.data.agent.common.dto.req.GitCommitAndPushReq;

public interface GitService {

	void commitAndPush(GitCommitAndPushReq req) throws Exception;
}
