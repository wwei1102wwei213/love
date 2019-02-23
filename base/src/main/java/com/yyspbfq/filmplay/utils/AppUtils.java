package com.yyspbfq.filmplay.utils;

import android.content.Context;
import android.os.Handler;

import com.yyspbfq.filmplay.BaseApplication;


/**
 * app工具类
 * Created by tankai on 2017/12/13.
 */
public class AppUtils {

    /**
     * 获取全局context
     *
     * @return
     */
    public static Context getContext() {
        return BaseApplication.getInstance().getApplicationContext();
    }

    /**
     * 获取全局handler
     *
     * @return
     */
    public static Handler getHandler() {
        return BaseApplication.getInstance().getHandler();
    }

    /**
     * 判断是否在主线程
     *
     * @return
     */
    public static boolean isRunOnUiThread() {
        int myTid = android.os.Process.myTid();
        return myTid == BaseApplication.mainThreadId;
    }

    /**
     * 运行在主线程
     *
     * @param r
     */
    public static void runOnUiThread(Runnable r) {
        if (isRunOnUiThread()) {
            r.run();
        } else {
            getHandler().post(r);
        }
    }


}
