package com.nomad.data.agent.dataset.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nomad.data.agent.config.exception.CustomException;
import com.nomad.data.agent.dataset.dto.info.AipDataObjectInfo;
import com.nomad.data.agent.dataset.dto.req.DataSetCreateReq;
import com.nomad.data.agent.dataset.dto.req.DataSetOverviewReq;
import com.nomad.data.agent.dataset.dto.req.DatasetDeleteReq;
import com.nomad.data.agent.dataset.dto.req.DatasetTransferReq;
import com.nomad.data.agent.dataset.service.dataset.DatasetService;
import com.nomad.data.agent.domain.dao.common.AipDataSetKey;
import com.nomad.data.agent.user.service.UserService;
import com.nomad.data.agent.utils.enums.ErrorCodeType;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/dataset")
public class DatasetController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private DatasetService datasetService;
	
	@ApiOperation(value = "데이터셋/다중 파일 업로드", notes = "다중 데이터셋 파일 업로드", tags = "01. 데이터셋")
	@RequestMapping(method = RequestMethod.POST, value = "upload/files")
	public ResponseEntity<AipDataSetKey> uploadFiles(
			@ApiParam(required = true, value = "토큰", defaultValue = "")
            @Valid
            @NotEmpty
            @RequestHeader final String tkn,
            @ModelAttribute DataSetCreateReq req,
            @ApiParam(required = true, value = "업로드 할 파일", defaultValue = "")
            @NotEmpty
            @RequestParam(name = "files") MultipartFile[] files) {
		
		
		if(files.length == 0) {
			log.error(">>>>> no upload files...");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		AipDataSetKey result = datasetService.uploadDataSetFiles(userService.getAipUser(tkn, true), files, req); 
		if (result == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "데이터셋/데이터셋 삭제", notes = "데이터셋 삭제", tags = "01. 데이터셋")
	@RequestMapping(method = RequestMethod.POST, value = "delete", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity deleteDatasetFiles(
			@ApiParam(required = true, value = "삭제 할 파일", defaultValue = "")
			@NotEmpty
			@RequestBody DatasetDeleteReq req) {

//		String[] deleteFiles = files.replace("[", "").replace("]", "").split(",");
		try {
			datasetService.deleteDatasetFile(req);

		} catch (Exception e) {
			log.error(">>>>> dataset delete error", e);
			throw new CustomException(ErrorCodeType.FILE_DELETE_ERROR);
		}

		return new ResponseEntity<>(HttpStatus.OK);
	}


	@ApiOperation(value = "데이터셋/데이터셋 내보내기", notes = "데이터셋 내보내기", tags = "01. 데이터셋")
	@RequestMapping(method = RequestMethod.GET, value = "export")
	public ResponseEntity<Resource> exportDatasetFiles(
			@ApiParam(required = true, value = "내보내기 할 파일", defaultValue = "")
			@NotEmpty
			@RequestParam(name = "fileName") String fileName,
			HttpServletRequest request) {

		if(StringUtils.isEmpty(fileName)) {
			log.error(">>>>> no export file...");
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		Resource resource = datasetService.loadFileAsResource(fileName);
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		}catch(IOException ioe) {
			log.error(">>>>> Could not determine file type.", ioe);
			throw new CustomException(ErrorCodeType.FILE_PROCESS_HANDLE);
		}

		if(null == contentType) {
			contentType = "application/octet-stream";
		}

		log.info(">>>>> Dataset file exported successfully... (File Name: {})", resource.getFilename());

		return ResponseEntity.ok()
							.contentType(MediaType.parseMediaType(contentType))
							.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + resource.getFilename() + "\"")
							.body(resource);
	}
	
	@ApiOperation(value = "데이터 파일 전송", notes = "데이터셋 파일을 워커서버 내부 워크스페이스로 전송한다.", tags = "01-데이터셋")
	@RequestMapping(method = RequestMethod.POST, value = "transfer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity transferDataset(
			@NotEmpty
			@RequestBody DatasetTransferReq req){

		datasetService.transferDatasetToWorkspace(req);

		return new ResponseEntity(HttpStatus.OK);
	}
	
	
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "데이터셋/데이터셋 추출", notes = "데이터셋 추출", tags = "01. 데이터셋")
	@RequestMapping(method = RequestMethod.POST, value = "extract", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity extractDataSet(
			@ApiParam(required = true, value = "토큰", defaultValue = "")
            @Valid
            @NotEmpty
            @RequestHeader final String tkn,
            @Valid
            @NotEmpty
            @RequestBody AipDataSetKey req) {

		try {
			datasetService.extractDataSet(userService.getAipUser(tkn, true), req);
		}catch(CustomException e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@ApiOperation(value = "데이터셋/데이터셋 미리보기", notes = "데이터셋 미리보기", tags = "01. 데이터셋")
	@RequestMapping(method = RequestMethod.GET, value = "overview", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<AipDataObjectInfo> overview(
			@ApiParam(required = true, value = "토큰", defaultValue = "")
            @Valid
            @NotEmpty
            @RequestHeader final String tkn,
			@Valid
			@ModelAttribute
			DataSetOverviewReq req) {
	
		AipDataObjectInfo extractDatas = datasetService.overview(userService.getAipUser(tkn, true), req);
		if(ObjectUtils.isEmpty(extractDatas)) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(extractDatas, HttpStatus.OK);
	}
}
