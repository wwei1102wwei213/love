package com.fuyou.play.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Administrator on 2017/9/14 0014.
 */

public class TimeUtils {

    public static void getTimeZoneName(int offset) {
        String name = "GTM+";
        int n = (offset / 1000) / 3600;
    }

    /**
     * 把显示的时间 如07,08,2014转化为时间（秒数）
     *
     * @return
     */
    private static long showTimeToLoadTime(String showtime) {
        if (TextUtils.isEmpty(showtime) || "0".equals(showtime)) {
            return 0;
        }
        SimpleDateFormat format = new SimpleDateFormat("MMM,dd,yyyy");
        long loadingTime = 0;
        try {
            Date time = format.parse(showtime);
            loadingTime = time.getTime() + TimeZone.getDefault().getRawOffset();
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("输入的时间格式不对");
        }
        return loadingTime;
    }

    /**
     * 把显示的时间 如07,08,2014转化为时间（秒数）
     *
     * @return
     */
    public static long showTimeToLoadTime(String showtime, String reg) {
        if (TextUtils.isEmpty(showtime) || "0".equals(showtime)) {
            return 0;
        }
        SimpleDateFormat format = new SimpleDateFormat(reg);
        long loadingTime = 0;
        try {
            Date time = format.parse(showtime);
            loadingTime = time.getTime()/1000;
            LogCustom.show(loadingTime+"");
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("输入的时间格式不对");
        }
        return loadingTime;
    }

    public static long formatTimeForStrAndZoneOffset(String showtime) {
        try {
//            long time = showTimeToLoadTime(showtime);
            SimpleDateFormat format = new SimpleDateFormat("MMM,dd,yyyy");
            long loadingTime = 0;
            try {
                Date dtime = format.parse(showtime);
                loadingTime = dtime.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
                throw new RuntimeException("输入的时间格式不对");
            }
            loadingTime = loadingTime / 1000;
            return loadingTime;
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "formatTimeForStrAndZoneOffset");
        }
        return 0;
    }

    public static long formatTimeForStrAndZoneOffset(String showtime, String reg) {
        try {
            long time = showTimeToLoadTime(showtime, reg);
            time = time / 1000;
            return time;
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "formatTimeForStrAndZoneOffset");
        }
        return 0;
    }

    public static int getMillForHHmm(String time) {
        if (TextUtils.isEmpty(time)) return 0;
        try {
            SimpleDateFormat format = new SimpleDateFormat("KK:mm a");
            long loadingTime = 0;
            try {
                Date timeData = format.parse(time);
                loadingTime = timeData.getTime()/1000 + TimeZone.getDefault().getRawOffset()/1000;
                LogCustom.show(loadingTime+"");
            } catch (ParseException e) {
                e.printStackTrace();
                throw new RuntimeException("输入的时间格式不对");
            }
            return Integer.valueOf(loadingTime+"");
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "getMillForHHmm");
        }
        return 0;
    }

    public static String getTimeStringForAstrolabe(long tills) {
        tills = tills * 1000;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        try {
            String result = format.format(new Date(tills));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "1970-01-01-00-00";
    }

    public static String getTimeStringForUnix(long tills) {
        String timeStr = "";
        try {
            tills *= 1000;
            long currentTime = System.currentTimeMillis();  // 单位毫秒
            if ((currentTime - tills) < 60000) {
                timeStr = "Just now";
            } else if ((currentTime - tills) < 60 * 60000) {
                timeStr = ((currentTime - tills) / 60000) + "m ago";
            } else if ((currentTime - tills) < 24 * 60 * 60000) {
                timeStr = ((currentTime - tills) / 60000 / 60) + "h ago";
            } else if ((currentTime - tills) < 2 * 24 * 60 * 60000) {
                timeStr = "Yesterday";
            } else {
                Date date = new Date(tills);
                SimpleDateFormat format = new SimpleDateFormat("MMM dd,yyyy");
                timeStr = format.format(date);
            }
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "TimeUtils getTimeStringForUnix");
        }
        return timeStr;
    }

    public static void main(String[] args){
        String str = "05:01 AM";
        String timeStr = "1488357600";
        SimpleDateFormat format = new SimpleDateFormat("KK:mm a");
        long loadingTime = 0;
        try {
            Date time = format.parse(str);
            loadingTime = time.getTime();
            System.out.println(loadingTime+"");
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("输入的时间格式不对");
        }
    }

}
