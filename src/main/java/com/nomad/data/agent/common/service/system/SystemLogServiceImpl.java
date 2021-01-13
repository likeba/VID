package com.nomad.data.agent.common.service.system;

import java.io.RandomAccessFile;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nomad.data.agent.common.dto.info.AipLogDetailInfo;
import com.nomad.data.agent.common.dto.info.AipLogFilesInfo;
import com.nomad.data.agent.common.dto.req.AipLogDetailReq;
import com.nomad.data.agent.config.exception.CustomException;
import com.nomad.data.agent.utils.FileUtils;
import com.nomad.data.agent.utils.enums.ErrorCodeType;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SystemLogServiceImpl implements SystemLogService {
	@Value("${system.log.path}")
	String systemLogPath;
	
	@Value("${system.log.default.file}")
	String systemLogDefaultFile;
	
	@Override
	public AipLogFilesInfo getLogFiles() {
		AipLogFilesInfo logFilesInfo = new AipLogFilesInfo();
		List<String> logFileList = new ArrayList<>();
		
		try {
			String[] fileList = FileUtils.listFiles(systemLogPath);
			
			for(String file: fileList) {
				
				if(file.substring(file.length()-4).equals(".log")) {
					if(file.equals(systemLogDefaultFile)) {
						logFilesInfo.setDefaultLog(file);
					}else {
						logFileList.add(file);
					}
				}
			}
		}catch(Exception e) {
			log.error(">>>>> get log files error", e);
			throw new CustomException(ErrorCodeType.SYSTEM_LOG_GET_GET_FILES_ERROR);
		}
		
		logFilesInfo.setLogFiles(logFileList);
		
		return logFilesInfo;
	}
	
	@Override
	public AipLogDetailInfo viewLogFile(AipLogDetailReq req) {
	
		String logFile = Paths.get(systemLogPath, req.getLogFileName()).toString();
		Long pointer = req.getPointer();
		
		String readLine = null;
		String logContent = "";
		
		try(RandomAccessFile randomAccessFile = new RandomAccessFile(logFile, "r")) {
			long randomFileLength = randomAccessFile.length();
			long fileLength = logFile.length();
			
			if (pointer <= 0 && randomAccessFile.length() > 4096) {
				pointer = randomAccessFile.length() - 4096;
			}
			
			randomAccessFile.seek(pointer);
			
			while((readLine = randomAccessFile.readLine()) != null) {
				logContent += new String(readLine.getBytes("ISO8859_1"), "UTF-8");
				if (!"".equals(readLine)) {
					logContent += "\n";
				}
			}
			pointer = randomAccessFile.getFilePointer();
		
		}catch (Exception e) {
			log.error(">>>>> view log file error", e);
			throw new CustomException(ErrorCodeType.SYSTEM_LOG_VIEW_ERROR);
		}
		
		AipLogDetailInfo logDetail = new AipLogDetailInfo();
		logDetail.setPointer(pointer);
		logDetail.setContents(logContent);
		
		return logDetail;
	}
}
