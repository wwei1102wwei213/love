package com.yyspbfq.filmplay.utils;

import android.util.Log;

import com.orhanobut.logger.Logger;
import com.yyspbfq.filmplay.BuildConfig;


/**
 * 自定义Log输出
 */
public final class BLog {

    private static boolean isDebug = BuildConfig.DEBUG;
//    private static boolean isDebug = true;

    private static final String LOG_TAG = "CUSTOM_LOG";
    private static final String LOG_TAG_HTTP = "CUSTOM_LOG_HTTP";
    private static final String LOG_TAG_ERROR = "CUSTOM_LOG_ERROR";

    private static BLog logger = new BLog();

    public static BLog b(String msg) {
        if (isDebug) {
            Log.d(LOG_TAG, msg);
        }
        return logger;
    }

    public static void b(String tag, String msg) {
        if (isDebug) {
            Log.d(tag, msg);
        }
    }

    public static BLog t(String tag) {
        if (isDebug) {
            Logger.t(tag);
        }
        return logger;
    }

    public static void d(String message, Object... args) {
        if (isDebug) {
            Logger.d(message, args);
        }
    }

    public static void d(Object object) {
        if (isDebug) {
            Logger.d(object);
        }
    }

    public static void e(String message, Object... args) {
        if (isDebug) {
            Logger.e(null, message, args);
        }
    }

    public static void e(Exception e) {
        if (isDebug) {
            Logger.e(e, "LOG_TAG_ERROR");
        }
    }

    public static void s(String mes) {
        if (isDebug) {
            Logger.t(LOG_TAG).d(mes);
        }
    }

    public static void s(String TAG, String mes) {
        if (isDebug) {
            Logger.t(TAG).d(mes);
        }
    }

    public static void s(String TAG, Object... args) {
        if (isDebug) {
            Logger.t(TAG).d(args);
        }
    }

    public static void h(String mes) {
        if (isDebug) {
            Logger.t(LOG_TAG_HTTP).d(mes);
        }
    }

    public static void h(Object... args) {
        if (isDebug) {
            Logger.t(LOG_TAG_HTTP).d(args);
        }
    }

    public static void i(String TAG, String mes) {
        if (isDebug) {
            Logger.t(TAG).d(mes);
        }
    }

    /*public static void i(String mes) {
        if (isDebug) {
            Logger.t(TAG).d(mes);
        }
    }*/

    public static void e(String TAG, String mes) {
        if (isDebug) {
            Logger.t(TAG).e(mes);
        }
    }

    public static void e(String mes) {
        if (isDebug) {
            Logger.t(LOG_TAG_ERROR).e(mes);
        }
    }

    public static void e(Throwable throwable, String message) {
        if (isDebug) {
            Logger.t(LOG_TAG_ERROR).e(throwable, message);
        }
    }

    public static void e(Throwable throwable, String message, Object... args) {
        if (isDebug) {
            Logger.t(LOG_TAG_ERROR).e(throwable, message, args);
        }
    }

    public static void d(String TAG, String mes) {
        if (isDebug) {
            Logger.t(TAG).d(mes);
        }
    }

}
