package com.ck.platform.common.util;

import com.ck.platform.common.exception.BizException;
import com.ck.platform.common.exception.BizResultCode;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtils {

    // 默认的时间格式
    public static final String DEFUALT_SHOT_TIME_FORMAT = "yyyy-MM-dd";
    public static final String SHOT_DATE_FORMAT = "yyyyMMdd";
    public static final String DEFUALT_LONG_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String LONG_TIME_FORMAT = "HH:mm:ss";
    public static final String SHOT_TIME_FORMAT = "HHmmss";
    public static final String DATE_FORMAT = "yyyyMMddHHmmss";

    private DateUtils() {

    }

    /**
     * 将毫秒转换为日期yyyy-MM-dd HH:mm:ss
     *
     * @param millis
     * @return
     */
    public static String getDateFromMillis(long millis) {
        try {
            Date nowTime = new Date(millis);
            SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdFormatter.format(nowTime);
        } catch (Throwable e) {
            throw new BizException(BizResultCode.ERR_DATE_FORMAT, e);
        }
    }

    /**
     * 字串日期转换
     *
     * @param strDate 字符串日期
     * @param format  日期的格式
     * @return
     */
    public static Date getDate(String strDate, String format) {
        try {
            SimpleDateFormat sdFormatter = new SimpleDateFormat(format);
            return sdFormatter.parse(strDate);
        } catch (Exception e) {
            throw new BizException(BizResultCode.ERR_DATE_FORMAT, e);
        }
    }


    /**
     * 8位日期转换为10位日期
     *
     * @param dateStr
     * @return
     */
    public static String getEighthToTen(String dateStr) {
        if (8 == dateStr.length()) {
            String year = dateStr.substring(0, 4);
            String month = dateStr.substring(4, 6);
            String day = dateStr.substring(6, 8);
            return year + "-" + month + "-" + day;
        }
        return dateStr;
    }

    /**
     * 清除小时分钟和秒
     */
    public static Date clearHourMiniteSecond(Date date) {
        if (date == null) {
            return null;
        }

        Calendar gc = Calendar.getInstance();
        gc.clear();
        gc.setTime(date);

        gc.set(Calendar.HOUR_OF_DAY, 0);
        gc.set(Calendar.MINUTE, 0);
        gc.set(Calendar.SECOND, 0);
        gc.set(Calendar.MILLISECOND, 0);

        return gc.getTime();
    }

    /**
     * 清除秒
     */
    public static Date clearSecond(Date date) {
        if (date == null) {
            return null;
        }

        Calendar gc = Calendar.getInstance();
        gc.clear();
        gc.setTime(date);

        gc.set(Calendar.SECOND, 0);
        gc.set(Calendar.MILLISECOND, 0);

        return gc.getTime();
    }

    /**
     * 按照指定格式返回当前时间，如果格式为空，则默认为yyyy-MM-dd
     *
     * @param dateFormat
     * @return
     */
    public static String getCurrentDateToString(String dateFormat) {
        if (StringUtils.isBlank(dateFormat)) {
            dateFormat = DEFUALT_SHOT_TIME_FORMAT;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(getCurrentDate());
    }

    /**
     * 按照指定格式返回当前时间，如果格式为空，则默认为yyyy-MM-dd
     *
     * @param dateFormat
     * @return
     */
    public static Date strToDate(String date, String dateFormat) {
        if (date == null) {
            return null;
        }

        if (StringUtils.isBlank(dateFormat)) {
            if (date.length() > 10) {
                dateFormat = DEFUALT_LONG_TIME_FORMAT;
            } else {
                dateFormat = DEFUALT_SHOT_TIME_FORMAT;
            }
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            return sdf.parse(date);
        } catch (Exception e) {
            throw new BizException(BizResultCode.ERR_DATE_FORMAT, e);
        }
    }

    /**
     * 格式化日期输出，如果格式为空，则默认为yyyy-MM-dd
     *
     * @param date
     * @param dateFormat
     * @return
     */
    public static String dateToStr(Date date, String dateFormat) {
        if (date == null) {
            return "";
        }

        if (StringUtils.isBlank(dateFormat)) {
            dateFormat = DEFUALT_SHOT_TIME_FORMAT;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            return sdf.format(date);
        } catch (Exception e) {
            throw new BizException(BizResultCode.ERR_DATE_FORMAT, e);
        }
    }

    /**
     * 得到当前时间
     *
     * @return
     */
    public static Date getCurrentDate() {
        return new Date();
    }

    /**
     * 获取当前日期，格式yyyyMMddHH
     *
     * @return
     */
    public static String getCurrentDateYYYYMMDD() {
        return dateToStr(getCurrentDate(), SHOT_DATE_FORMAT);
    }

    /**
     * 获取当前日期，格式yyyyMMddHHmmss
     *
     * @return
     */
    public static String getCurrentDateYYYYMMDDHHMMSS() {
        return dateToStr(getCurrentDate(), DATE_FORMAT);
    }

    /**
     * 比较两个时间的间隔，单位为毫秒
     *
     * @param endDate
     * @param startDate
     * @return
     */
    public static Long diffTwoDateWithTime(Date endDate, Date startDate) {
        if (startDate == null || endDate == null) {
            return -1l;
        }
        Long diff = (endDate.getTime() - startDate.getTime());

        return diff;
    }

    /**
     * 比较两个时间的间隔，单位为小时
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int diffTwoDateWithMinute(Date startDate, Date endDate) {
        return (int) ((endDate.getTime() - startDate.getTime()) / (60 * 1000) + 1);
    }

    /**
     * 比较两个时间的间隔，单位为小时
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int diffTwoDateWithHour(Date startDate, Date endDate) {
        return (int) ((endDate.getTime() - startDate.getTime()) / (60 * 60 * 1000) + 1);
    }

    /**
     * @param startDate
     * @param endDate
     * @return int 返回类型
     * @throws @author Administrator
     * @Title: diffTwoDateWithDay
     * @Description: 比较两个时间的间隔，单位为天
     */
    public static int diffTwoDateWithDay(Date startDate, Date endDate) {
        return (int) ((endDate.getTime() - startDate.getTime()) / (24 * 60 * 60 * 1000));
    }

    /**
     * 时间增加一天Calendar的使用yyyyMMdd
     *
     * @param s
     * @param n
     * @return
     */
    public static String addDate(String s, int n) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(SHOT_DATE_FORMAT);

            Calendar cd = Calendar.getInstance();
            cd.setTime(sdf.parse(s));
            cd.add(Calendar.DATE, n);// 增加一天
            // cd.add(Calendar.MONTH, n);//增加一个月
            return sdf.format(cd.getTime());
        } catch (Exception e) {
            throw new BizException(BizResultCode.ERR_DATE_FORMAT, e);
        }
    }

    /**
     * 日期处理
     *
     * @param srcDate 原日期
     * @param days    增加的天数
     * @param minutes 增加的分钟数
     * @param seconds 增加的秒数
     * @return
     */
    public static Date addDate(Date srcDate, int days, int minutes, int seconds) {
        Calendar c = Calendar.getInstance();
        c.setTime(srcDate);
        if (days != 0) {
            c.add(Calendar.DATE, days);
        }
        if (minutes != 0) {
            c.add(Calendar.MINUTE, minutes);
        }
        if (seconds != 0) {
            c.add(Calendar.SECOND, seconds);
        }
        return c.getTime();
    }

    /**
     * 给指定日期添加指定天数
     *
     * @param srcDate yyyyMMdd
     * @param days    增加的天数
     * @return
     */
    public static Date addDay(String srcDate, int days) {
        Date date = DateUtils.strToDate(srcDate, DateUtils.SHOT_DATE_FORMAT);
        return DateUtils.addDate(date, days, 0, 0);
    }

    /**
     * @param date
     * @param days
     * @return String 返回类型 yyyy-MM-dd
     * @throws @author Administrator
     * @Title: addDay
     * @Description: 给指定日期添加指定天数
     */
    public static String addDay(Date date, int days) {
        Date tempDate = DateUtils.addDate(date, days, 0, 0);
        return DateUtils.dateToStr(tempDate, "");
    }

    /**
     * 根据日期获取日
     *
     * @param date 格式yyyyMMdd
     * @return
     */
    public static int getDayByDate(String date) {
        try {
            Calendar calen = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            calen.setTime(dateFormat.parse(date));
            return calen.get(Calendar.DAY_OF_MONTH);
        } catch (ParseException e) {
            throw new BizException(BizResultCode.ERR_DATE_FORMAT, e);
        }
    }

    public static void main(String[] args) {
        System.out.println(DateUtils.strToDate("20131121100521", "yyyyMMddHHmmss"));

        Date srcDate = new Date();
        System.out.println(DateUtils.dateToStr(srcDate, DEFUALT_LONG_TIME_FORMAT));
        System.out.println(DateUtils.dateToStr(DateUtils.addDate(srcDate, 1, 0, 0), DEFUALT_LONG_TIME_FORMAT));
        System.out.println(DateUtils.dateToStr(DateUtils.addDate(srcDate, 1, 1, 0), DEFUALT_LONG_TIME_FORMAT));
        System.out.println(DateUtils.dateToStr(DateUtils.addDate(srcDate, 1, 1, 1), DEFUALT_LONG_TIME_FORMAT));
        System.out.println(DateUtils.dateToStr(srcDate, DEFUALT_LONG_TIME_FORMAT));
        System.out.println(DateUtils.dateToStr(DateUtils.addDate(srcDate, 0, 1440, 0), DEFUALT_LONG_TIME_FORMAT));

        System.out.println(DateUtils.dateToStr(DateUtils.addDate(DateUtils.strToDate("20131016", "yyyyMMdd"), -5, 0, 0),
                DEFUALT_LONG_TIME_FORMAT));

        System.out.println(DateUtils.getDayByDate("20150930"));

        Date date = DateUtils.addDay("20150930", 1);
        String sendDate = DateUtils.dateToStr(date, DateUtils.SHOT_DATE_FORMAT);
        System.out.println(sendDate);

        Date nowDate = new Date();
        String flyDate = DateUtils.addDay(nowDate, 11);
        System.out.println(flyDate);

        Date startDate = DateUtils.strToDate("2016-03-12", "yyyy-MM-dd");
        Date endDate = DateUtils.strToDate("2016-03-11", "yyyy-MM-dd");
        int day = DateUtils.diffTwoDateWithDay(startDate, endDate);
        System.out.println(day);
        for (int i = 0; i <= day; i++) {
            String flightTime = DateUtils.addDay(startDate, i);
            System.out.println(flightTime);
        }
        System.out.println(DateUtils.dateToStr(new Date(), "MMddHHmm"));
    }

}
