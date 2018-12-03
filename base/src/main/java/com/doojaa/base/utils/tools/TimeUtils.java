package com.doojaa.base.utils.tools;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {

    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat DATE_FORMAT_DATE_0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat DATE_FORMAT_DATE_1 = new SimpleDateFormat("yyyy-MM-dd");
    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat DATE_FORMAT_DATE_2 = new SimpleDateFormat("MM-dd HH:mm");
    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat DATE_FORMAT_DATE_3 = new SimpleDateFormat("yyyy年MM月dd日到期");

    private TimeUtils() {
        throw new AssertionError();
    }

    /**
     * long time to string
     *
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * long time to string, format is {@link #DATE_FORMAT_DATE_0}
     *
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DATE_FORMAT_DATE_0);
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #DATE_FORMAT_DATE_0}
     *
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }

    /**
     * 显示时间，如果与当前时间差别小于一天，则自动用**秒(分，小时)前，如果大于一天则用format规定的格式显示
     * @return
     */
    public static String showTime(long timeInMillis, SimpleDateFormat dateFormat) {
        String timeStr = "";
        Date ctime = new Date(timeInMillis);
        if(dateFormat==null)dateFormat= DATE_FORMAT_DATE_0;

        long nowtimelong = System.currentTimeMillis();
        long ctimelong = ctime.getTime();
        long result = Math.abs(nowtimelong - ctimelong);

        if (result < 60000)// 一分钟内
        {
            long seconds = result / 1000;
            timeStr = seconds + "秒钟前";
        } else if (result >= 60000 && result < 3600000)// 一小时内
        {
            long seconds = result / 60000;
            timeStr = seconds + "分钟前";
        } else if (result >= 3600000 && result < 86400000)// 一天内
        {
            long seconds = result / 3600000;
            timeStr = seconds + "小时前";
        } else// 日期格式
        {
            timeStr = TimeUtils.getTime(timeInMillis, dateFormat);
        }
        return timeStr;
    }
}
