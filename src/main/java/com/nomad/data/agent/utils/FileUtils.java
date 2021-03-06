package com.nomad.data.agent.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

import com.nomad.data.agent.config.exception.CustomException;
import com.nomad.data.agent.utils.enums.ErrorCodeType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtils {
    public static void uploadFile(MultipartFile file, Path path){

        try {
            Files.copy(file.getInputStream(),
                    Paths.get(path + File.separator + file.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            log.error(">>>>> upload file error", e);
            throw new CustomException(ErrorCodeType.FILE_UPLOAD_HANDLE);
        }
    }

    public static File downloadFile(String path, String fileName){

        File downloadFile = null;

        try {
            downloadFile = Paths.get(path, fileName).toFile();

        } catch (Exception e) {
            log.error(">>>>> download file error", e);
            throw new CustomException(ErrorCodeType.FILE_DOWNLOAD_HANDLE);
        }

        return downloadFile;
    }
    
    public static void mkdir(File file){
        file.mkdirs();
    }
    
    public static void deleteDirectory(File file){

        try {
            org.apache.tomcat.util.http.fileupload.FileUtils.deleteDirectory(file);
        } catch (IOException e) {
            log.error(">>>>> delete file error", e);
            throw new CustomException(ErrorCodeType.FILE_DELETE_ERROR);
        }
    }
    
    public static void deleteFile(String path) {
    	try {
    		if (Paths.get(path).toFile().exists()) {
    			Files.delete(Paths.get(path));
    		}else {
    			throw new CustomException(ErrorCodeType.FILE_NOT_FOUND);
    		}
    	}catch(IOException ioe) {
    		log.error(ioe.getMessage(), ioe);
    		throw new CustomException(ErrorCodeType.FILE_DELETE_ERROR);
    	}
    }
    
    /**
     * ?????? ???????????? ????????? ??????????????? ????????????.
     * 
     * @param path ????????? ??????
     * @return true, false ????????? ???????????? ????????? true, ???????????? false
     */
    public static boolean checkDirectoryIsEmpty(String path) {
    	if (Paths.get(path).toFile().exists() &&
    			Paths.get(path).toFile().isDirectory()) {
    		return Paths.get(path).toFile().list().length > 0 ? false : true;
    	}else {
    		return true;
    	}
    }
    
    /**
     * ?????? ????????? ????????? ????????????.
     * 
     * @param sourceFile ?????? ??????
     * @param targetFile ?????? ??????
     */
    public static void moveFiles(File sourceFile, File targetFile) {
    	try {
    		if (!sourceFile.exists()) {
    			log.error(">>>>> move files error - source file not found.");
    			throw new CustomException(ErrorCodeType.FILE_NOT_FOUND);
    		}
    		sourceFile.renameTo(targetFile);
    	}catch(Exception e) {
    		log.error(e.getMessage(), e);
    		throw new CustomException(ErrorCodeType.FILE_PROCESS_HANDLE);
    	}
    }
    
    /**
     * ???????????? ?????? ???????????? ?????????.
     * 
     * @param path ????????????
     */
    public static void cleanDirectory(File path) {
    	try {
    		org.apache.commons.io.FileUtils.cleanDirectory(path);
    	}catch(IOException ioe) {
    		log.error(ioe.getMessage(), ioe);
    		throw new CustomException(ErrorCodeType.FILE_PROCESS_HANDLE);
    	}
    }
    
    /**
     * ??????????????? ????????????.
     * 
     * @param source ??????
     * @param target ??????
     */
    public static void copyDirectory(File source, File target) {
    	try {
    		org.apache.commons.io.FileUtils.copyDirectory(source, target);
    	}catch(IOException ioe) {
    		log.error(ioe.getMessage(), ioe);
    		throw new CustomException(ErrorCodeType.FILE_PROCESS_HANDLE);
    	}
    }
    
    public static String[] listFiles(String path) {
    	return Paths.get(path).toFile().list();
    }
}
