package com.nomad.data.agent.utils;

import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;

import com.nomad.data.agent.config.exception.CustomException;
import com.nomad.data.agent.utils.enums.ErrorCodeType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateUtils {
	private static final String ISO8601_DATE_PATTERN_STRING = "yyyy-MM-dd'T'HH:mm:ss";
	private static final String DEFAULT_TIME_ZONE = "GMT+9:00";
	private static final String DATE_ONLY_PATTERN_STRING = "yyyyMMdd";
	private static final String YEAR_AND_MONTH_PATTERN_STRING = "yyyyMM";
	private static final String FULL_DATE_AND_TIME_PATTERN_STRING = "yyyyMMddHH24mmss";
	
	public static Date getNow() {
		return new Date();
	}
	
	public static String getSystemId() {
		long now = System.currentTimeMillis();
		int postfix = new SecureRandom().nextInt(10);
		
		return new StringBuilder().append(now).append(postfix).substring(0, 14);
	}
	
	public static Date fromISO8601String(String iso8601String) {
		if(StringUtils.isEmpty(iso8601String)) {
			return null;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat(ISO8601_DATE_PATTERN_STRING);
		
		try {
			return sdf.parse(iso8601String);
		}catch(ParseException e) {
			log.error(">>>>> date parse error", e);
			throw new CustomException(ErrorCodeType.COMMON_INTERNAL_SERVER_ERROR);
		}
		
	}
	
	public static String toISO8601String(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(ISO8601_DATE_PATTERN_STRING, Locale.KOREA);
		sdf.setTimeZone(TimeZone.getTimeZone(DEFAULT_TIME_ZONE));
		
		return sdf.format(date);
	}
	
	public static String getDateOnlyString() {
		return getDateOnlyString(getNow());
	}
	
	public static String getDateForyearAndMonth() {
		return getYearAndMonth(getNow());
	}
	
	public static String getExpirationDateString() {
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, +8);
		
		return getDateOnlyString(cal);
	}
	
	public static String getFullDateAndTime() {
		return getFullDateAndTime(getNow());
	}
	
	public static String getDateOnlyString(Calendar cal) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_ONLY_PATTERN_STRING);
		return sdf.format(cal.getTime());
	}
	
	public static String getDateOnlyString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_ONLY_PATTERN_STRING);
		return sdf.format(date);
	}
	
	public static String getDateOnlyString(LocalDate localDate) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_ONLY_PATTERN_STRING);
		return localDate.format(dateTimeFormatter);
	}
	
	public static String getYearAndMonth(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(YEAR_AND_MONTH_PATTERN_STRING);
		return sdf.format(date);
	}
	
	public static String getFullDateAndTime(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(FULL_DATE_AND_TIME_PATTERN_STRING);
		return sdf.format(date);
	}

	
}
