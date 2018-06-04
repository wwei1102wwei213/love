package com.fuyou.play.util;


import com.fuyou.play.BuildConfig;
import com.orhanobut.logger.Logger;


/**
 * 自定义Log输出
 *
 * @author wwei
 */
public final class LogCustom {

    private static final int FLAG = 1;
    private static boolean isDebug = BuildConfig.DEBUG;
//    private static boolean isDebug = true;

    private static LogCustom logger = new LogCustom();

    public static LogCustom t(String tag){
        if (isDebug){
            Logger.t(tag);
        }
        return logger;
    }

    public void d(String message, Object... args) {
        if (isDebug) {
            Logger.d(message, args);
        }
    }

    public void d(Object object) {
        if (isDebug) {
            Logger.d(object);
        }
    }

    public void e(String message, Object... args) {
        if (isDebug) {
            Logger.e(null, message, args);
        }
    }

    public static void show(String mes) {
        if (isDebug){
            Logger.t(Const.LOG_TAG).d(mes);
        }
    }

    public static void show(String TAG, String mes) {
        if (isDebug){
            Logger.t(TAG).d(mes);
        }
    }

    public static void show(String TAG, Object... args) {
        if (isDebug){
            Logger.t(TAG).d(args);
        }
    }

    public static void h(String mes) {
        if (isDebug){
            Logger.t(Const.LOG_TAG_HTTP).d(mes);
        }
    }

    public static void h(Object... args) {
        if (isDebug){
            Logger.t(Const.LOG_TAG_HTTP).d(args);
        }
    }

    public static void i(String TAG, String mes) {
        if (isDebug){
            Logger.t(TAG).d(mes);
        }
    }

    public static void e(String TAG, String mes) {
        if (isDebug) {
            Logger.t(TAG).e(mes);
        }
    }

    public static void e(String mes) {
        if (isDebug) {
            Logger.t(Const.LOG_TAG_ERROR).e(mes);
        }
    }

    public static void e(Throwable throwable, String message) {
        if (isDebug) {
            Logger.t(Const.LOG_TAG_ERROR).e(throwable, message);
        }
    }

    public static void e(Throwable throwable, String message, Object... args) {
        if (isDebug) {
            Logger.t(Const.LOG_TAG_ERROR).e(throwable, message, args);
        }
    }

    public static void d(String TAG, String mes) {
        if (isDebug){
            Logger.t(TAG).d(mes);
        }
    }

    /*public static void f(String mes, int num){
        if (FLAG > 0) {
            try {
                if (TextUtils.isEmpty(mes) || mes.length() < 20) return;
                int temp = mes.length() / num;
                for (int i = 0, j = temp; i < mes.length() && j < mes.length(); i += temp, j += temp) {
                    Log.i(LOG_TAG, "分段数据" + i / temp + ":" + mes.substring(i, j));
                }
            } catch (Exception e) {
                Log.i(LOG_TAG, "分段打印出错");
            }
        }
    }*/

}
