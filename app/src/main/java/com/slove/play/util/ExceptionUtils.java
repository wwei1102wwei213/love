package com.slove.play.util;

/**
 *
 * Created by Administrator on 2017/8/29.
 *
 * 异常信息处理工具类
 *
 * @author wwei
 */
public class ExceptionUtils {

    private static final String TAG = "ERROR_MC";

    /**
     * 捕获异常
     * @param e 异常信息
     */
    public static void ExceptionSend(Exception e){
        LogCustom.e(TAG,e.toString());
        try {
            StackTraceElement[] stack = e.getStackTrace();
            StringBuilder builder = new StringBuilder();
            for(int i = 0;i<stack.length;i++){
                builder.append(stack[i].toString()+"\n");
            }
            LogCustom.e(TAG, "异常信息详情：" + builder.toString());
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * 捕获异常，显示异常位置，并打印异常日志，将错误信息上传至TD
     * @param e 异常信息
     * @param where 异常位置标识串
     */
    public static void ExceptionSend(Exception e, String where){
        try {
            LogCustom.e(TAG,"异常位置：" + where + "\n" + "异常信息：" + e.toString());
            StackTraceElement[] stack = e.getStackTrace();
            StringBuilder builder = new StringBuilder();
            for(int i = 0;i<stack.length;i++){
                builder.append(stack[i].toString()+"\n");
            }
            LogCustom.e(TAG, "异常信息详情("+where+")：" + builder.toString());
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
