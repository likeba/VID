package com.nomad.data.agent.library.service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import javax.validation.constraints.NotEmpty;

import com.nomad.data.agent.library.dto.req.LibraryDeleteReq;

public interface LibraryService {
	
	/**
	 * (업데이트 서버로부터) 라이브러리 파일 다운로드
	 * 
	 * @param libFilePath	라이브러리 다운로드 경로
	 * @return	라이브러리 파일 다운로드 결과
	 * @throws IOException
	 */
	public CompletableFuture<String> applyLibraryFile(String libFilePath) throws IOException;

	
	/**
	 * 라이브러리 파일 삭제
	 * 
	 * @param req
	 */
	public void deleteLibrary(@NotEmpty LibraryDeleteReq req);
}
