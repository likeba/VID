package com.nomad.data.agent.common.service.common;

import java.util.List;

import com.nomad.data.agent.common.dto.info.AipTestInfo;
import com.nomad.data.agent.common.dto.req.AipTestReq;
import com.nomad.data.agent.domain.dao.common.AipTestKey;
import com.nomad.data.agent.domain.dao.common.AipUser;

public interface CommonService {

	List<AipTestInfo> list(AipUser user, AipTestReq req);

	AipTestKey save(AipUser user, AipTestReq req);

}
