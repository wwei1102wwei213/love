package com.slove.play.util;

import android.text.TextUtils;
import android.util.Log;

import static com.slove.play.util.Const.LOG_TAG;


/**
 * 自定义Log输出
 *
 * @author wwei
 */
public class LogCustom {

    private static final int FLAG = 1;

    public static void show(String mes) {
        if (FLAG > 0)
            Log.i(LOG_TAG, mes);
    }

    public static void show(String TAG, String mes) {
        if (FLAG > 0)
            Log.i(TAG, mes);
    }

    public static void i(String TAG, String mes) {
        if (FLAG > 0)
            Log.i(TAG, mes);
    }

    public static void i(String TAG, boolean mes) {
        if (FLAG > 0)
            Log.i(TAG, mes + "");
    }

    public static void i(String TAG, int mes) {
        if (FLAG > 0)
            Log.i(TAG, mes + "");
    }

    public static void i(String TAG, float mes) {
        if (FLAG > 0)
            Log.i(TAG, mes + "");
    }

    public static void e(String TAG, String mes) {
        if (FLAG > 0)
            Log.e(TAG, mes);
    }

    public static void v(String TAG, String mes) {
        if (FLAG > 0)
            Log.i(TAG, mes);
    }

    public static void d(String TAG, String mes) {
        if (FLAG > 0)
            Log.d(TAG, mes);
    }

    public static void f(String mes, int num){
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
    }

}
