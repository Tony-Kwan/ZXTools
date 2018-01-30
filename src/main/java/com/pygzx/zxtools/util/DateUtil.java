package com.pygzx.zxtools.util;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

public class DateUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);

	public static final String ISO_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	public static final String LOCAL_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String BASIC_YEAR_MONTH_FORMAT = "yyyyMM";
	public static final String BASIC_DATE_FORMAT = "yyyyMMdd";

	public static final ZoneOffset SYSTEM_DEFAULT_ZONE_OFFSET = OffsetDateTime.now(ZoneId.systemDefault()).getOffset();

	private static final String[] DATE_FORMAT_LIST = {ISO_DATETIME_FORMAT, LOCAL_DATETIME_FORMAT, "yyyy-MM-dd HH:mm"};

	public static Date str2Date(String dateStr) {
		if (StringUtils.isEmpty(dateStr)) {
			return null;
		}
		try {
			return DateUtils.parseDate(dateStr, DATE_FORMAT_LIST);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return null;
	}

	public static Date str2Date(String dateStr, String pattern) {
		if (dateStr == null || StringUtils.isEmpty(pattern)) {
			return null;
		}
		try {
			return DateUtils.parseDate(dateStr, pattern);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return null;
	}

	public static String date2Str(Date date, String pattern) {
		if (date == null || pattern == null || pattern.isEmpty()) {
			return null;
		}
		return date.toInstant()
			.atZone(ZoneId.systemDefault())
			.toLocalDateTime()
			.format(DateTimeFormatter.ofPattern(pattern));
	}

	public static String date2Str(long ts, String pattern) {
		return date2Str(new Date(ts), pattern);
	}

	public static String date2Str(Date date, DateTimeFormatter fmt) {
		if (date == null || fmt == null) {
			return null;
		}
		return date.toInstant()
			.atZone(ZoneId.systemDefault())
			.toLocalDateTime()
			.format(fmt);
	}

	public static String date2Str(long timestamp, DateTimeFormatter fmt) {
		return Instant.ofEpochMilli(timestamp)
			.atZone(ZoneId.systemDefault())
			.toLocalDateTime()
			.format(fmt);
	}

	public static String date2Str(long ts, int zoneOffsetHour, String pattern) {
		return Instant.ofEpochMilli(ts)
			.atZone(ZoneOffset.ofHours(zoneOffsetHour))
			.toLocalDateTime()
			.format(DateTimeFormatter.ofPattern(pattern));
	}


	/**
	 * 获取数个月前的第一天的时间戳
	 * @return long-某个月第一天开始的时间戳 (eg: 当前时间=2017年03月13日15:56:29, monthsAgo=1, return=1485878400000)
	 */
	public static long getMonthsAgoTs(int monthsAgo) {
		return LocalDate.now().minusMonths(monthsAgo).with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay().toEpochSecond(SYSTEM_DEFAULT_ZONE_OFFSET) * 1000;
	}

	/**
	 * 获取某个时间点所在月份的开始时间的时间戳
	 * @param ts-时间戳(单位: 毫秒)
	 * @return long-(eg: ts=1485878410000, return=1485878400000)
	 */
	public static long getFirstMillisecondOfMonth(long ts) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(ts), ZoneId.systemDefault()).with(TemporalAdjusters.firstDayOfMonth()).toLocalDate().atStartOfDay().toEpochSecond(SYSTEM_DEFAULT_ZONE_OFFSET) * 1000;
	}

	public static long getFirstMillisecondOfDay(long ts) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(ts), ZoneId.systemDefault()).toLocalDate().atStartOfDay().toEpochSecond(SYSTEM_DEFAULT_ZONE_OFFSET) * 1000;
	}

	public static long nowTs() {
		return Instant.now().toEpochMilli();
	}
}
