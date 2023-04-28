package com.xu.pet.core;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义时间转换工具
 *
 * @author xuqingf
 * @date 2023/2/20
 */
public class TimeFormat {
    private static final Pattern hhmm = Pattern.compile("(\\d\\d):(\\d\\d)-(\\d\\d):(\\d\\d)");
    public static final DateTimeFormatter MONTH_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM");
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter DATE_TIME_ISO8901_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    public static final DateTimeFormatter DATE_KEY_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final DateTimeFormatter HOUR_KEY_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHH");
    public static final DateTimeFormatter DATE_TIME_KEY_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    public static final DateTimeFormatter MONTH_KEY_FORMAT = DateTimeFormatter.ofPattern("yyyyMM");
    public static final DateTimeFormatter YEAR_KEY_FORMAT = DateTimeFormatter.ofPattern("yyyy");

    public TimeFormat() {
    }

    public static String getDateString(LocalDate now) {
        return DATE_FORMAT.format(now);
    }

    public static String getDateString(LocalDateTime now) {
        return DATE_FORMAT.format(now);
    }

    public static String getMonthString(YearMonth now) {
        return MONTH_FORMAT.format(now);
    }

    public static String getTimeString(LocalTime now) {
        return TIME_FORMAT.format(now);
    }

    public static String getTimeString(LocalDateTime now) {
        return TIME_FORMAT.format(now);
    }

    public static String getDateTimeString(LocalDateTime now) {
        return DATE_TIME_FORMAT.format(now);
    }

    public static LocalTime getTime(String time) {
        return StringUtils.isBlank(time) ? null : LocalTime.parse(time, TIME_FORMAT);
    }

    public static LocalDate getDate(String date) {
        return StringUtils.isBlank(date) ? null : LocalDate.parse(date, DATE_FORMAT);
    }

    public static YearMonth getYearMonth(String date) {
        return StringUtils.isBlank(date) ? null : YearMonth.parse(date, MONTH_FORMAT);
    }

    public static LocalDateTime getDateTime(String date) {
        return StringUtils.isBlank(date) ? null : LocalDateTime.parse(date, DATE_TIME_FORMAT);
    }

    public static LocalTime[] getHours(String times) {
        Matcher m = hhmm.matcher(times);
        return m.find() ? new LocalTime[]{LocalTime.of(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2))), LocalTime.of(Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4)))} : null;
    }
}