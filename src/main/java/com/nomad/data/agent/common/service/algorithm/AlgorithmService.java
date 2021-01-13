package com.nomad.data.agent.common.service.algorithm;

import org.springframework.web.multipart.MultipartFile;

import com.nomad.data.agent.common.dto.req.AipAlgorithmUploadReq;
import com.nomad.data.agent.domain.dao.common.AipUser;

public interface AlgorithmService {
	
	String upload(AipUser aipUser, AipAlgorithmUploadReq req, MultipartFile file);
	
}
