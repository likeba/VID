package com.nomad.data.agent.utils;

import static org.hamcrest.CoreMatchers.containsString;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CompressUtils {

	/**
	 * 압축 해제
	 * 
	 * @param source	압축 파일
	 * @param target	압축 해제 대상 경로
	 * @throws IOException
	 */
    public void decompress(String source, String target) throws IOException {
    	
        try (TarArchiveInputStream fin = new TarArchiveInputStream(new FileInputStream(source))){
            TarArchiveEntry entry;
            while ((entry = fin.getNextTarEntry()) != null) {
                if (entry.isDirectory()) {
                    continue;
                }
                File curfile = new File(target, entry.getName());
                File parent = curfile.getParentFile();
                if (!parent.exists()) {
                    parent.mkdirs();
                }
                IOUtils.copy(fin, new FileOutputStream(curfile));
            }
        }
    }

    /**
     * 파일 압축
     * 
     * @param sourceDir		압축 대상 폴더
     * @param targetFile	최종 압축 파일 경로
     * @throws IOException
     */
    public void compress(String sourceDir, String targetFile) throws IOException {
        
    	// 압축 파일이 생성될 폴더가 존재하지 않을 경우 폴더 생성 후 압축 시작
    	Paths.get(targetFile).getParent().toFile().mkdirs();
    	
    	try (TarArchiveOutputStream out = getTarArchiveOutputStream(targetFile)){
            
        	File temp = new File(sourceDir);
        	File[] files = temp.listFiles();
        	
        	for (File file : files){
        		if(!file.getName().contains("result")) {
        			addToArchiveCompression(out, file, ".");
        		}
            }
        }
    }
    
    
    private TarArchiveOutputStream getTarArchiveOutputStream(String tarFilePath) throws IOException {
        
    	TarArchiveOutputStream taos = new TarArchiveOutputStream(new FileOutputStream(tarFilePath));
        
    	// TAR has an 8 gig file limit by default, this gets around that
        taos.setBigNumberMode(TarArchiveOutputStream.BIGNUMBER_STAR);
        
        // TAR originally didn't support long file names, so enable the support for it
        taos.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        taos.setAddPaxHeadersForNonAsciiNames(true);
        
        return taos;
    }

    private void addToArchiveCompression(TarArchiveOutputStream out, File file, String dir) throws IOException {
        
    	String entry = dir + File.separator + file.getName();
        
    	if (file.isFile()){
            out.putArchiveEntry(new TarArchiveEntry(file, entry));
            try (FileInputStream in = new FileInputStream(file)){
                IOUtils.copy(in, out);
            }
            out.closeArchiveEntry();
            
        } else if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null){
                for (File child : children){
                    addToArchiveCompression(out, child, entry);
                }
            }
        } else {
    	    log.error(">>>>> " + file.getName() + " is not supported");
        }
    }
    
    public static void unzip(String source, String target) throws IOException {
    	
    	try(ZipInputStream fin = new ZipInputStream(new FileInputStream(source))) {
    		ZipEntry entry;
    		while((entry = fin.getNextEntry()) != null) {
    			if(entry.isDirectory()) {
    				continue;
    			}
    			File curfile = new File(target, entry.getName());
    			File parent = curfile.getParentFile();
    			if(!parent.exists()) {
    				parent.mkdirs();
    			}
    			IOUtils.copy(fin, new FileOutputStream(curfile));
    		}
    	}
    }
}
