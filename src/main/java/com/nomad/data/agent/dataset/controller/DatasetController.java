package com.nomad.data.agent.dataset.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nomad.data.agent.dataset.service.dataset.DataSetService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/datasets")
public class DataSetController {

	@Autowired
	private DataSetService dataSourceService;
	
	@ApiOperation(value = "데이터베이스 리스트", notes = "데이터베이스 리스트", tags = "데이터베이스 리스트")
	@RequestMapping(method = RequestMethod.GET, value = "databases", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity getDatabases() {
		
		return new ResponseEntity<>(dataSourceService.getDatabaselist(), HttpStatus.OK);
	}
	
	@ApiOperation(value = "테이블 조회", notes = "테이블 조회", tags = "테이블 조회")
	@RequestMapping(method = RequestMethod.GET, value = "tables", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity getTables(
			@ApiParam(required = true, value = "database 이름", defaultValue = "dataset")
			@Valid
			@NotEmpty
            @RequestParam(name = "database")
            final String database) {
		
		return new ResponseEntity<>(dataSourceService.getTablelist(database), HttpStatus.OK);
	}
	
	@ApiOperation(value = "테이블 컬럼 조회", notes = "테이블 컬럼 조회", tags = "테이블 컬럼 조회")
	@RequestMapping(method = RequestMethod.GET, value = "{database}/{table}/columns", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity getColumns(
			@PathVariable("database") String database,
			@PathVariable("table") String table) {
		
		return new ResponseEntity<>(dataSourceService.getTableColumnlist(database, table), HttpStatus.OK);
	}
	
	@ApiOperation(value = "원본 데이터셋 조회", notes = "원본 데이터셋 조회", tags = "원본 데이터셋 조회")
	@RequestMapping(method = RequestMethod.GET, value = "{database}/{table}/{pk}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity searchDataset(
			@PathVariable("database") String database,
			@PathVariable("table") String table,
			@PathVariable("pk") String pk) {
		
		return new ResponseEntity<>(dataSourceService.getDataset(database, table, pk), HttpStatus.OK);
	}
	
	@ApiOperation(value = "데이터셋 저장(DB+blockchain)", notes = "데이터셋 저장(DB+blockchain)", tags = "데이터셋 저장(DB+blockchain)")
	@RequestMapping(method = RequestMethod.POST, value = "dataset/{database}/{table}/{pk}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity storeDataset(
			@PathVariable("database") String database,
			@PathVariable("table") String table,
			@PathVariable("pk") String pk) {
		
		return new ResponseEntity<>(dataSourceService.storeDataset(database, table, pk), HttpStatus.OK);
	}
	
	@ApiOperation(value = "저장된 데이터셋  id list 조회", notes = "저장된 데이터셋  id list 조회", tags = "저장된 데이터셋  id list 조회")
	@RequestMapping(method = RequestMethod.GET, value = "datasets/id", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity getDatasetIds() {
		
		return new ResponseEntity<>(dataSourceService.getDatasetIds(), HttpStatus.OK);
	}
	
	@ApiOperation(value = "저장된 데이터셋 조회", notes = "저장된 데이터셋 조회", tags = "저장된 데이터셋 조회")
	@RequestMapping(method = RequestMethod.GET, value = "dataset/{dataset_id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity getStoredDataset(
			@PathVariable("dataset_id") String dataset_id) {
		
		return new ResponseEntity<>(dataSourceService.getStoredDataset(dataset_id), HttpStatus.OK);
	}
	
	@ApiOperation(value = "데이터셋 검증", notes = "데이터셋 검증", tags = "데이터셋 검증")
	@RequestMapping(method = RequestMethod.GET, value = "dataset/verification/{dataset_id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity verifyDatasetIntegrity(
			@PathVariable("dataset_id") String dataset_id) {
		
		return new ResponseEntity<>(dataSourceService.verifyDatasetIntegrity(dataset_id), HttpStatus.OK);
	}
}
