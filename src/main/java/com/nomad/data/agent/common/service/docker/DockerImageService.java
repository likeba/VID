package com.nomad.data.agent.common.service.docker;

import java.util.List;

import com.nomad.data.agent.common.dto.req.AipDockerImageCreateReq;
import com.nomad.data.agent.common.dto.req.AipDockerRegistryImageDeleteReq;
import com.nomad.data.agent.domain.dao.common.AipDockerImageKey;
import com.nomad.data.agent.domain.dao.common.AipUser;

public interface DockerImageService {

    public void deleteRegistryImage(AipUser user, AipDockerRegistryImageDeleteReq req);

    public AipDockerImageKey create(AipUser user, AipDockerImageCreateReq req);
    
    public List<String> list();
}
