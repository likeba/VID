package com.nomad.data.agent.utils;

import java.util.UUID;
import org.springframework.stereotype.Component;
import com.google.common.base.CaseFormat;

@Component
public class StrUtils {

	/**
	 * Camel Case 문자열을 Underscore 조합으로 치환
	 * (예. databaseName -> DATABASE_NAME)
	 * 
	 * @param lowerCamel
	 * @return
	 */
	public static String getDbFieldName(String lowerCamel) {
		return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, lowerCamel);
	}

	/**
	 * 쿼리에 사용할 Like 문장으로 치환
	 * (예. name -> %name$)
	 * 
	 * @param str
	 * @return
	 */
	public static String getLikeString(String str) {
		if(str == null) {
			return null;
		}
		return new StringBuilder("%").append(str).append("%").toString();
	}
	
	/**
	 * 고유한 ID 생성 
	 * 
	 * @return ID (14자리)
	 */
	public static String getUniqueId() {
		return UUID.randomUUID().toString().replace("-", "").substring(18);
	}
	
	/**
	 * 파일 경로에서 파일명 추출
	 * 
	 * @param filePath 파일 경로
	 * @return 파일명
	 */
	public static String getFileName(String filePath) {
		return filePath.substring(filePath.lastIndexOf("/") + 1);
	}

	/**
	 * 문자열 중 첫 글자만 대문자 및 이후 글자들 소문자로 치환
	 * 
	 * @param string
	 * @return
	 */
	public static String getUpperFirstCharacter(String string) {
		return string.substring(0, 1).toUpperCase() + string.substring(1, string.length()).toLowerCase();
	}
}
