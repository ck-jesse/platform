package com.ck.platform.common.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * jdk8 日期工具类
 *
 * @author chenck
 * @date 2019/12/3 15:52
 */
public class DateUtil {
    public static final String yyyyMMddHHmmss = "yyyyMMddHHmmss";
    public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";

    // ----------------

    /**
     * String 转 LocalDateTime
     *
     * @param time   时间
     * @param format 格式
     */
    public static LocalDateTime strToTime(String time, String format) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(time, df);
    }

    /**
     * String 转 LocalDateTime
     *
     * @param time 格式 yyyy-MM-dd HH:mm:ss
     */
    public static LocalDateTime strToTime(String time) {
        return strToTime(time, yyyy_MM_dd_HH_mm_ss);
    }

    /**
     * LocalDateTime 转 String
     *
     * @param localDateTime 时间
     * @param format        格式
     */
    public static String timeToStr(LocalDateTime localDateTime, String format) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        return localDateTime.format(df);
    }

    /**
     * LocalDateTime 转 String
     *
     * @param localDateTime 时间
     * @return 日期，格式 yyyy-MM-dd HH:mm:ss
     */
    public static String timeToStr(LocalDateTime localDateTime) {
        return timeToStr(localDateTime, yyyy_MM_dd_HH_mm_ss);
    }

    // ----------------

    /**
     * long 转 LocalDateTime
     *
     * @param millisTime 时间戳, 毫秒
     */
    public static LocalDateTime longToTime(long millisTime) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millisTime), ZoneId.systemDefault());
    }

    /**
     * LocalDateTime 转 long
     *
     * @param localDateTime 时间
     * @return 时间戳, 毫秒
     */
    public static long timeToLong(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return instant.toEpochMilli();
    }

    // ----------------

    /**
     * Date 转 LocalDateTime
     *
     * @param date 时间
     */
    public static LocalDateTime dateToTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * LocalDateTime 转 Date
     *
     * @param
     */
    public static Date timeToDate(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    // ----------------


    public static void main(String[] args) {
        long now = System.currentTimeMillis();
        System.out.println(now);
        LocalDateTime dateTime = longToTime(now);
        System.out.println("longToTime   " + dateTime);
        String dateStr = timeToStr(dateTime);
        System.out.println("timeToStr    " + dateStr);
        System.out.println("timeToLong   " + timeToLong(dateTime));
        System.out.println("strToTime    " + strToTime(dateStr));
    }

}
