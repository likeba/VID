package com.nomad.data.agent.dataset.service.dataset;

import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.nomad.data.agent.dataset.dto.info.AipDataObjectInfo;
import com.nomad.data.agent.dataset.dto.req.DataSetCreateReq;
import com.nomad.data.agent.dataset.dto.req.DataSetOverviewReq;
import com.nomad.data.agent.dataset.dto.req.DatasetDeleteReq;
import com.nomad.data.agent.dataset.dto.req.DatasetReadReq;
import com.nomad.data.agent.dataset.dto.req.DatasetTransferReq;
import com.nomad.data.agent.domain.dao.common.AipDataSetKey;
import com.nomad.data.agent.domain.dao.common.AipUser;

public interface DatasetService {


	/**
	 * 다중 데이터셋 파일 업로드
	 *
	 * @param files 업로드 할 파일
	 * @return
	 */
	public AipDataSetKey uploadDataSetFiles(AipUser user, MultipartFile[] files, DataSetCreateReq req);


	/**
	 * 데이터셋 삭제
	 *
	 * @param req 삭제 할 데이터셋 파일
	 */
	public void deleteDatasetFile(DatasetDeleteReq req) throws Exception;


	/**
	 * 데이터셋 내보내기
	 *
	 * @param fileName 내보내기 할 데이터셋 파일
	 * @return
	 */
	public Resource loadFileAsResource(String fileName);

	public List<Map<String, Object>> overviewFile(DatasetReadReq req);

	/**
	 * 데이터셋 전송
	 * 데이터 서버에 있는 데이터 셋을 워크스페이스가 위치한 서버로 전송한다.
	 *
	 * @param req
	 */
	public void transferDatasetToWorkspace(DatasetTransferReq req);


	/**
	 * 데이터셋 추출 및 데이터 셋 생성
	 *
	 * @param user
	 * @param req
	 */
	public void extractDataSet(AipUser user, AipDataSetKey req);


	/**
	 * 데이터셋 파일 미리보기
	 *
	 * @param user
	 * @param req
	 * @return
	 */
	public AipDataObjectInfo overview(AipUser user, DataSetOverviewReq req);


	/**
	 * APM 데이터셋 경로 이동
	 */
	public void moveApmDataSet();


}
