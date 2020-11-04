package com.nomad.data.agent.library.service;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AipDataSetReq {
	
	/**
	 * 데이터셋 이름
	 */
	private String dataNm;
	
	/**
	 * 데이터셋 설명
	 */
	private String dataDesc;
	
	/**
	 * 데이터셋 종류
	 */
	private String dataTp;
	
	/**
	 * 쿼리 디비 종류
	 */
	private String queryDbTp;
	
	/**
	 * 쿼리문
	 */
	private String queryStr;
	
	/**
	 * 쿼리 상태
	 */
	private String querySts;
	
	/**
	 * 파일 이름
	 */
	private String fileNm;
	
	/**
	 * 파일 경로
	 */
	private String filePath;
	
	/**
	 * 파일 크기
	 */
	private Long fileSz;

	/**
	 * 소유자 아이디
	 */
	private String ownerId;
	
	/**
	 * 접근 권한
	 */
	private Integer accAuth;
	
	/**
	 * 공개 여부
	 */
	private String pubFl;
	
	/**
	 * 삭제 여부
	 */
	private String delFl;
	
	/**
	 * 등록일
	 */
	private Date regDt;
	
	/**
	 * 수정일
	 */
	private Date modDt;
	
	
	
}
