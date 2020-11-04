package com.nomad.data.agent.user.service;

import com.nomad.data.agent.domain.dao.common.AipUser;

public interface UserService {

	public AipUser getAipUser(String tkn, boolean throwable);
}
